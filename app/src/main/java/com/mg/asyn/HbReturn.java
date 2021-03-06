package com.mg.asyn;

import com.mg.comm.MConstant;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import java.util.Map;

/**
 * Created by wuqiyan on 17/7/14.
 */

public class HbReturn extends RequestAsync  {
    public HbReturn(ReqAsyncModel model) {
        super(model);
    }


    @Override
    protected void startRequest() {
        if (! checkSDKConfigModel()){

            requestHb();

        }else {
            if(checkHbTime()) {

                if (checkNumber()){

                    listener.onMiiNoAD(3004);
                    return;
                }
                if (checkADShow()){

                    listener.onMiiNoAD(3003);
                    return;
                }
                //请求广告
                mainHandler.sendEmptyMessage(100);
            }
            else {
                requestHb();
            }
        }
    }

    @Override
    public void requestHb(){

        try {
            if (!CommonUtils.isNetworkAvailable(mContext)){

                listener.onMiiNoAD(3000);//未检测到网络
                return;
            }
            if (httpManager == null){
                httpManager= HttpManager.getInstance(mContext);
            }

            final String url = httpManager.getHbUrl();
            if (url == null||url.equals("")){

                listener.onMiiNoAD(3001);
                return;
            }
            Map<String,String> params = httpManager.getHbParams(appid,lid);
            if (params == null){
                listener.onMiiNoAD(3001);
                return;
            }
            HttpUtils httpUtils = new HttpUtils(mContext);
            httpUtils.post(url.trim(), new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealHbSuc(response);
                }

                @Override
                public void onFail(Exception e) {

                    listener.onMiiNoAD(3001);
                }
            },params);
        }catch (Exception e){

            listener.onMiiNoAD(3001);
            e.printStackTrace();
        }

    }
    @Override
    public void dealHbSuc(HttpResponse response ){
        try {

            String data = response.entity();
            LogUtils.i(MConstant.TAG,"data="+data);

            if (data == null){

                listener.onMiiNoAD(3001);
                return;
            }
            sdkConfigModel = ConfigParser.parseConfig(data);

            if (sdkConfigModel == null){

                listener.onMiiNoAD(3001);
                return;
            }

            CommonUtils.writeParcel(mContext, MConstant.CONFIG_FILE_NAME,sdkConfigModel);

            checkReShowCount();

            if (checkNumber()){

                listener.onMiiNoAD(3004);
                return;
            }
            if (checkADShow()){

                listener.onMiiNoAD(3003);
                return;
            }
            //请求广告
            mainHandler.sendEmptyMessage(100);
        }
        catch (Exception e){

            listener.onMiiNoAD(3001);
            e.printStackTrace();
        }
    }
}
