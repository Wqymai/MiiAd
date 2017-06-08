package com.mg.splash;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Base64;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.message.Handler;
import com.mg.others.message.ISender;
import com.mg.others.message.MessageObjects;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import static com.mg.others.manager.HttpManager.NI;

/**
 * Created by wuqiyan on 17/6/8.
 */

public class MSplashController implements Handler {
    private ISender mSender = ISender.Factory.newMainThreadSender(this);
    private Activity mActivity;
    private Context mContext;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MConstant.what.update_config:
                MessageObjects messageObjects = (MessageObjects) msg.obj;
                SDKConfigModel temp = (SDKConfigModel) messageObjects.obj0;
                messageObjects.recycle();
                sdkConfig = temp;
                appList = null;
                break;
        }
        
    }
    public MSplashController(Activity mActivity){
        this.mActivity=mActivity;
        this.mContext=mActivity.getApplicationContext();
    }
    private void requestHb(){
       HttpManager httpManager = HttpManager.getInstance(mActivity.getApplicationContext(), mSender);

        if (CommonUtils.isNetworkAvailable(mContext)){

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
                    dealNiSuc(response);
                }

                @Override
                public void onFail(Exception e) {

                    LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                }
            });

        }else{
            LogUtils.i(MConstant.TAG,"ni 网络不可用......");
        }
    }

    private void dealNiSuc(HttpResponse response){
        SDKConfigModel sdk = null;
        String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (data==null){
            return;
        }
        sdk = ConfigParser.parseConfig(data);

        if (sdk == null){
            return;
        }

        CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);


        if (mSender != null){
            MessageObjects messageObjects = MessageObjects.obtain();
            messageObjects.obj0 = sdk;
            mSender.sendMessage(mSender.obtainMessage(MConstant.what.update_config, messageObjects));
        }

    }


}
