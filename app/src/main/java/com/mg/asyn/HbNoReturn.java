package com.mg.asyn;

import com.mg.comm.MConstant;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import java.util.Map;

/**
 * Created by wuqiyan on 17/6/15.
 * 仅仅是心跳操作，不需要返回结果码，不需要发送消息
 */

public class HbNoReturn extends RequestAsync {


    public HbNoReturn(ReqAsyncModel model) {
        super(model);
    }

    @Override
    protected void startRequest() {
        if (! checkSDKConfigModel()){
            requestHb();
        }else {
            if(! checkHbTime()){

                requestHb();
            }
        }
    }

    @Override
    protected void requestHb() {
      try {
        if (!CommonUtils.isNetworkAvailable(mContext)){

            return;
        }
        if (httpManager == null){
            httpManager= HttpManager.getInstance(mContext);
        }

        final String url = httpManager.getHbUrl();

        if (url == null||url.equals("")){

            return;
        }

        Map<String,String> params = httpManager.getHbParams(appid,lid);
        if (params == null){
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.post(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc(response);
            }

            @Override
            public void onFail(Exception e) {

            }
        },params);
      }catch (Exception e){
          e.printStackTrace();
      }
    }


    @Override
    protected void dealHbSuc(HttpResponse response) {
        try {
            SDKConfigModel sdk = null;

            String data = response.entity();

            if (data == null){
                return;
            }
            sdk = ConfigParser.parseConfig(data);

            if (sdk == null){
                return;
            }


            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            checkReShowCount();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
