package com.mg.comm;

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
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MConstant;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import java.util.List;
import java.util.Map;

import static com.mg.others.manager.HttpManager.NI;
import static com.mg.others.manager.HttpManager.RA;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class MhttpRequestHelper {


    private Handler mainHandler;
    private Context mContext;
    private HttpManager httpManager=null;
    private MiiADListener listener=null;
    private int pt;
    public MhttpRequestHelper(Context context, Handler handler, int pt, MiiADListener listener){
        this.mainHandler=handler;
        this.mContext=context;
        this.listener=listener;
        this.pt=pt;
    }
    public void fetchMGAD(){
        //麦广相关的
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);

        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {

                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    startRequest();
                }
            }, mContext).execute();
        }
        else {
            startRequest();
        }
    }
    public  void startRequest(){

        SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            requestHb();
        }
        else {
            long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

            long currTime = System.currentTimeMillis();

            int next = sdkConfigModel.getNext();

            if (((currTime-hbTime)/1000) < next){
                requestRa();
                return;
            }
            requestHb();
        }

    }

    private void requestRa(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            Log.i(Constants.TAG,"ra 网络不可用......");
            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }

        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext, null);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl(RA);
        if (url == null || url.equals("")){
            return;
        }

        Map<String,String> params=httpManager.getParams2(RA,pt,0);//暂时写的

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {

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

        if (!CommonUtils.isNetworkAvailable(mContext)){
            Log.i(Constants.TAG,"ni 网络不可用......");
            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }
        if (httpManager==null){
            httpManager= HttpManager.getInstance(mContext, null);
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
                LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                listener.onMiiNoAD(3001);
            }
        });



    }

    private void dealHbSuc(HttpResponse response){
        try {
            SDKConfigModel sdk = null;

            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

            if (data == null){
                listener.onMiiNoAD(3002);//hb解析失败
                return;
            }
            sdk = ConfigParser.parseConfig(data);

            if (sdk == null){
                listener.onMiiNoAD(3002);//hb解析失败
                return;
            }
            Log.i(Constants.TAG,"HB请求结果："+sdk.toString());
            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            requestRa();
        }
        catch (Exception e){
            listener.onMiiNoAD(3002);//hb解析失败
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

        ads = AdParser.parseAd(temp);

        if (ads == null || ads.size() <= 0){
            listener.onMiiNoAD(3004);//ra 解析失败
            return;
        }


        AdModel ad = ads.get(0);
        if (ad == null){
            listener.onMiiNoAD(3004);//ra 解析失败
            return;
        }

        Message msg=new Message();
        msg.obj = ad;
        msg.what=200;//ra访问成功
        mainHandler.sendMessage(msg);

      }
      catch (Exception e){
          listener.onMiiNoAD(3004);//ra 解析失败
          e.printStackTrace();
      }
    }


}
