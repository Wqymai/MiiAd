package com.mg.asyn;

import android.util.Base64;

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

import static com.mg.others.manager.HttpManager.NI;

/**
 * Created by wuqiyan on 17/6/15.
 * 仅仅是心跳操作，不需要返回结果码，不需要发送消息
 */

public class JustHbRelative extends RequestAsync {


    public JustHbRelative(ReqAsyncModel model) {
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
        if (!CommonUtils.isNetworkAvailable(mContext)){

            return;
        }
        if (httpManager == null){
            httpManager= HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final String url = httpManager.getParams(NI, 0, 0);

        if (url == null||url.equals("")){

            return;
        }
        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc(response);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    @Override
    protected void dealHbSuc(HttpResponse response) {
        try {
            SDKConfigModel sdk = null;

            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

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
