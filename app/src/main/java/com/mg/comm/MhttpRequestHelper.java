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

    //=====================简单心跳=====================
    public void fetchMGAD3(){
        //麦广相关的
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){
            Log.i(Constants.TAG,"未获取设别信息，获取。。。");
            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {
                    Log.i(Constants.TAG,"获取到的设备信息。。"+deviceInfo.toString());

                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    startRequest3();
                }
            }, mContext).execute();
        }
        else {
            startRequest3();
        }
    }


    public  void startRequest3(){

        SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            requestHb3();
        }
        else {

            long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

            long currTime = System.currentTimeMillis();

            int next = sdkConfigModel.getNext();

            if (((currTime - hbTime)/1000) < next){
                LogUtils.i(MConstant.TAG,"not need hb");
                return;
            }
            else {
                LogUtils.i(MConstant.TAG,"need hb");
                requestHb3();
            }
        }

    }


    public void requestHb3(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            LogUtils.i(MConstant.TAG,"network error");
            return;
        }
        if (httpManager == null){
            httpManager= HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final String url = httpManager.getParams(NI, 0, 0);
        if (url == null||url.equals("")){
            LogUtils.i(MConstant.TAG,"url="+url);
            return;
        }
        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc3(response);
            }

            @Override
            public void onFail(Exception e) {
            }
        });

    }
    private void dealHbSuc3(HttpResponse response){
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
            LogUtils.i(MConstant.TAG,"hb res："+sdk.toString());

            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);
            long currentTime = System.currentTimeMillis();

            if (httpManager.DateCompare(writeTime) || writeTime == 0l){
                SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
                SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    //===============================分割线===========================
    public void fetchMGAD(final boolean isFirst){

        //麦广相关的
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {


                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    startRequest(isFirst);
                }
            }, mContext).execute();
        }
        else {
            startRequest(isFirst);
        }
    }



    public  void startRequest(final boolean isFirst){

        SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            requestHb(isFirst);
        }
        else {
            if (isFirst){
                return;
            }

            long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

            long currTime = System.currentTimeMillis();

            int next = sdkConfigModel.getNext();

            if (((currTime - hbTime)/1000) < next){
                LogUtils.i(MConstant.TAG,"not need hb");

                int real_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);//广告已展示的次数
                int sdk_num = sdkConfigModel.getShow_sum();

                LogUtils.i(MConstant.TAG,"real="+real_num+" sdknum="+sdk_num);
                if (real_num >= sdk_num){

                    LogUtils.i(MConstant.TAG,"startRequest real_num > sdk_num");
                    listener.onMiiNoAD(3004);
                    return;
                }
                if (!sdkConfigModel.isAdShow()){

                    listener.onMiiNoAD(3003);
                    return;
                }
                requestRa();

                return;
            }
            else {
                LogUtils.i(MConstant.TAG,"need hb");
                requestHb(false);
            }
        }

    }

    private void requestRa(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            LogUtils.i(MConstant.TAG,"network error");
            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }

        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl(RA);
        if (url == null || url.equals("")){
            listener.onMiiNoAD(3002);
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

                listener.onMiiNoAD(3002);
            }
        },params);
    }
    public void requestHb(final boolean isFirst){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            LogUtils.i(MConstant.TAG,"network error");
            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }
        if (httpManager == null){
            httpManager= HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final String url = httpManager.getParams(NI, 0, 0);
        if (url == null||url.equals("")){
            Log.i(Constants.TAG,"url="+url);
            listener.onMiiNoAD(3001);
            return;
        }
        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc(response,isFirst);
            }

            @Override
            public void onFail(Exception e) {

                listener.onMiiNoAD(3001);
            }
        });

    }


    private void dealHbSuc(HttpResponse response,boolean isFirst){
        try {
            SDKConfigModel sdk = null;

            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

            if (data == null){
                listener.onMiiNoAD(3001);
                return;
            }
            sdk = ConfigParser.parseConfig(data);

            if (sdk == null){
                listener.onMiiNoAD(3001);
                return;
            }
            LogUtils.i(MConstant.TAG,"hb res："+sdk.toString());

            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);
            long currentTime = System.currentTimeMillis();

            if (httpManager.DateCompare(writeTime) || writeTime == 0l){
                SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
                SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
            }

            if (isFirst){
                mainHandler.sendEmptyMessage(100);
                return;
            }

            int real_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);//广告已展示的次数
            int sdk_num = sdk.getShow_sum();

            LogUtils.i(MConstant.TAG,"real="+real_num+" sdknum="+sdk_num);

            if (real_num >= sdk_num){

                LogUtils.i(MConstant.TAG,"startRequest real_num > sdk_num");
                listener.onMiiNoAD(3004);
                return;
            }
            if (!sdk.isAdShow()){

                listener.onMiiNoAD(3003);
                return;
            }

            requestRa();
        }
        catch (Exception e){
            listener.onMiiNoAD(3001);//hb解析失败
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
            listener.onMiiNoAD(3002);//ra 解析失败
            return;
        }


        AdModel ad = ads.get(0);
        if (ad == null){
            listener.onMiiNoAD(3002);//ra 解析失败
            return;
        }

        Message msg=new Message();
        msg.obj = ad;
        msg.what=200;//ra访问成功
        mainHandler.sendMessage(msg);

      }
      catch (Exception e){
          listener.onMiiNoAD(3002);//ra 解析失败
          e.printStackTrace();
      }
    }


    //============================分割线====================================================

    public void fetchMGAD1(final boolean shouldReturn){
        //麦广相关的
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);

        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {

                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    startRequest1(shouldReturn);
                }
            }, mContext).execute();
        }
        else {
            startRequest1(shouldReturn);
        }
    }



    public  void startRequest1(boolean shouldReturn){

        SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            requestHb1(shouldReturn);
        }
        else {
            long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

            long currTime = System.currentTimeMillis();

            int next = sdkConfigModel.getNext();

            if (((currTime-hbTime)/1000) < next){

                int real_num = (Integer) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);

                int sdk_num = sdkConfigModel.getShow_sum();

                if (real_num >= sdk_num){
                    LogUtils.i(MConstant.TAG,"startRequest1 real_num > sdk_num");

                    if (shouldReturn){
                        listener.onMiiNoAD(3004);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }
                if (!sdkConfigModel.isAdShow()){

                    if (shouldReturn){
                        listener.onMiiNoAD(3003);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;

                }
                requestRa1(shouldReturn);
                return;
            }
            requestHb1(shouldReturn);
        }

    }

    private void requestRa1(final boolean shouldReturn){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            LogUtils.i(MConstant.TAG,"network error");

            if (shouldReturn) {
                listener.onMiiNoAD(3000);//未检测到网络
            }
            else {
                mainHandler.sendEmptyMessage(400);
            }
            return;
        }

        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl(RA);
        if (url == null || url.equals("")){
            if (!shouldReturn){
                mainHandler.sendEmptyMessage(400);
            }
            return;
        }

        Map<String,String> params=httpManager.getParams2(RA,pt,0);

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {

                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealRaSuc1(response,shouldReturn);
            }
            @Override
            public void onFail(Exception e) {

                if (shouldReturn){

                    listener.onMiiNoAD(3002);
                }else {
                    mainHandler.sendEmptyMessage(400);
                }
            }
        },params);
    }
    public void requestHb1(final boolean shouldReturn){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            LogUtils.i(MConstant.TAG,"network error");
            if (shouldReturn){
                listener.onMiiNoAD(3000);//未检测到网络
            }
            else {
                 mainHandler.sendEmptyMessage(400);
            }
            return;
        }
        if (httpManager==null){
            httpManager= HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final String url = httpManager.getParams(NI, 0, 0);
        if (url == null||url.equals("")){
            if (shouldReturn){
                listener.onMiiNoAD(3001);
            }
            else {
                mainHandler.sendEmptyMessage(400);
            }
            return;
        }
        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc1(response,shouldReturn);
            }

            @Override
            public void onFail(Exception e) {

                if (shouldReturn){

                     listener.onMiiNoAD(3001);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
            }
        });



    }

    private void dealHbSuc1(HttpResponse response,boolean shouldReturn){
        try {
            SDKConfigModel sdk = null;

            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

            if (data == null){
                if (shouldReturn){
                   listener.onMiiNoAD(3001);//hb解析失败
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }
            sdk = ConfigParser.parseConfig(data);

            if (sdk == null){
                if (shouldReturn) {
                    listener.onMiiNoAD(3001);//hb解析失败
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }
            Log.i(Constants.TAG,"hb res："+sdk.toString());



            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);
            long currentTime = System.currentTimeMillis();

            if (httpManager.DateCompare(writeTime) || writeTime == 0l){
                SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
                SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
            }

            int real_num = (Integer) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            int sdk_num = sdk.getShow_sum();

            if (real_num >= sdk_num){
                LogUtils.i(MConstant.TAG,"startRequest1 real_num > sdk_num");

                if (shouldReturn){

                    listener.onMiiNoAD(3004);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }
            if (!sdk.isAdShow()){

                if (shouldReturn){
                    listener.onMiiNoAD(3003);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;

            }

            requestRa1(shouldReturn);
        }
        catch (Exception e){
           if (shouldReturn) {
               listener.onMiiNoAD(3001);//hb解析失败
           }
           else {
               mainHandler.sendEmptyMessage(400);
           }
            e.printStackTrace();
        }

    }
    private  void dealRaSuc1(HttpResponse response,boolean shouldReturn){

        try {
            List<AdModel> ads;
            String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
            if (temp == null){
               if (shouldReturn) {
                   listener.onMiiNoAD(3002);//ra 解析失败
               }
               else {
                   mainHandler.sendEmptyMessage(400);
               }
                return;
            }

            ads = AdParser.parseAd(temp);

            if (ads == null || ads.size() <= 0){
               if (shouldReturn) {
                   listener.onMiiNoAD(3002);//ra 解析失败
               }
               else {
                   mainHandler.sendEmptyMessage(400);
               }
                return;
            }


            AdModel ad = ads.get(0);
            if (ad == null){
                if (shouldReturn) {
                    listener.onMiiNoAD(3002);//ra 解析失败
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }

            Message msg=new Message();
            msg.obj = ad;
            msg.what=200;//ra访问成功
            mainHandler.sendMessage(msg);

        }
        catch (Exception e){
            if (shouldReturn) {
                listener.onMiiNoAD(3002);//ra 解析失败
            }
            else {
                mainHandler.sendEmptyMessage(400);
            }
            e.printStackTrace();
        }
    }


}
