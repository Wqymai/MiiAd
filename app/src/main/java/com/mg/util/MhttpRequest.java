package com.mg.util;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.mg.demo.Constants;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;
import com.mg.splash.MiiSplashADListener;

import java.util.List;
import java.util.Map;

import static com.mg.others.manager.HttpManager.NI;
import static com.mg.others.manager.HttpManager.RA;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class MhttpRequest {


    private Handler mainHandler;
    private Context mContext;
    private HttpManager httpManager=null;
    private MiiSplashADListener listener=null;
    private int pt;
    public MhttpRequest(Context context, Handler handler,int pt, MiiSplashADListener listener){
        this.mainHandler=handler;
        this.mContext=context;
        this.listener=listener;
        this.pt=pt;
    }
    public  void startRequest(){

        SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            requestHb();
        }
        else {
            long hbTime= (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

            long currTime=System.currentTimeMillis();

            int next=sdkConfigModel.getNext();

            if (((currTime-hbTime)/1000) < next){
                requestRa();
                return;
            }
            requestHb();
        }

    }

    private void requestRa(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        Log.i(Constants.TAG,"ni 网络不可用......1");
        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext, null);
        }
        Log.i(Constants.TAG,"ni 网络不可用......2");
        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl(RA);
        if (url==null || url.equals("")){
            return;
        }
        Log.i(Constants.TAG,"ni 网络不可用......2");
        Map<String,String> params=httpManager.getParams2(RA,pt,0);//暂时写的

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                Log.i(Constants.TAG,"ni 网络不可用......4");
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealRaSuc(response);
            }
            @Override
            public void onFail(Exception e) {
                LogUtils.i(MConstant.TAG,new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
                listener.onMiiNoAD(3003);
            }
        },params);
    }
    public void requestHb(){

        if (httpManager==null){
            httpManager= HttpManager.getInstance(mContext, null);
        }

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
                    dealHbSuc(response);
                }

                @Override
                public void onFail(Exception e) {
                    LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                    listener.onMiiNoAD(3001);
                }
            });

        }else{
            Log.i(Constants.TAG,"ni 网络不可用......");
        }
    }

    private void dealHbSuc(HttpResponse response){
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

            requestRa();
        }
        catch (Exception e){
            listener.onMiiNoAD(3002);
            e.printStackTrace();
        }

    }
    private  void dealRaSuc(HttpResponse response){

      try {
        List<AdModel> ads;
        String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (temp==null){
            return;
        }
        Log.i(Constants.TAG,"ni 网络不可用......5");
        ads = AdParser.parseAd(temp);

        if (ads == null || ads.size() <= 0){
            return;
        }

          Log.i(Constants.TAG,"ni 网络不可用......6");
        AdModel ad = ads.get(0);
        if (ad == null){
            return;
        }
        Log.i(Constants.TAG,"RA请求结果："+ad.toString());
        Message msg=new Message();
        msg.obj = ad;
        msg.what=200;//ra访问成功
        mainHandler.sendMessage(msg);

      }
      catch (Exception e){
          listener.onMiiNoAD(3004);
          e.printStackTrace();
      }
    }


}
