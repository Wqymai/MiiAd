package com.mg.asyn;

import android.content.Context;
import android.os.Handler;

import com.mg.comm.MConstant;
import com.mg.interf.MiiAbsADListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.SP;

/**
 * Created by wuqiyan on 17/6/15.
 */

public class RequestAsync {

    protected Handler mainHandler;
    protected Context mContext;
    protected HttpManager httpManager;
    protected MiiAbsADListener listener;
    protected String appid;
    protected SDKConfigModel sdkConfigModel;
    protected int pt;//请求广告的类型  2-开屏 3-插屏

    public RequestAsync(ReqAsyncModel model){

        if (model == null){
            return;
        }
        this.mainHandler = model.handler;
        this.mContext = model.context;
        this.listener = model.listener;
        this.pt = model.pt;
        this.appid = model.appid;
    }


    public void fetchMGAD(){

        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {
                    LogUtils.i(MConstant.TAG,"获取的设别="+deviceInfo.toString());
                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);
                    startRequest();
                }
            }, mContext).execute();
        }
        else {
            startRequest();
        }

    }

    /**
     * 检查本地缓存中是否已有sdk的配置 true:存在 false:不存在
     * */
    protected boolean checkSDKConfigModel(){

        sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            return false;
        }
        return true;
    }
    /**
    检查是否到了心跳时间 true:没到 false:到了
    */
    protected boolean checkHbTime(){
        long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

        long currTime = System.currentTimeMillis();

        int next = sdkConfigModel.getNext();

        if (((currTime - hbTime)/1000) < next){
            return true;
        }
        else {
            return false;
        }
    }

    /**
    检查广告展示次数是否超过sdk设置次数 true:超过了 false:没超过
    */
    protected boolean checkNumber(){
        int real_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);//广告已展示的次数
        int sdk_num = sdkConfigModel.getShow_sum();
        if (real_num >= sdk_num){

            return true;
        }
        return false;

    }
    /**
    检查sdk设置是否开了广告 true:没开 false:开了
    */
    protected boolean checkADShow(){
        if (!sdkConfigModel.isAdShow()){
            return true;
        }
        return false;
    }

    /**
    检查是否要要更新广告展示的次数
    */
    protected void checkReShowCount(){
        long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);
        long currentTime = System.currentTimeMillis();
        if (httpManager.DateCompare(writeTime) || writeTime == 0l){
            SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
        }
    }
    protected  void startRequest(){

    }
    protected void requestHb(){

    }
    protected void dealHbSuc(final HttpResponse response){

    }

    protected void requestRa(){

    }
    protected  void dealRaSuc(final HttpResponse response){

    }

}
