package com.mg.asyn;

import android.content.Context;
import android.os.Handler;

import com.mg.comm.MConstant;
import com.mg.comm.MiiADListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;

/**
 * Created by wuqiyan on 17/6/15.
 */

public class RequestAsync {
    protected Handler mainHandler;
    protected Context mContext;
    protected HttpManager httpManager;
    protected MiiADListener listener;
    protected SDKConfigModel sdkConfigModel;
    protected int pt;
    public RequestAsync(Context context, Handler handler, int pt, MiiADListener listener){
        this.mainHandler=handler;
        this.mContext=context;
        this.listener=listener;
        this.pt=pt;
    }

    public void fetchMGAD(){

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

    protected boolean checkSDKConfigModel(){
        sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            return false;
        }
        return true;
    }
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

    protected boolean checkNumber(){
        int real_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);//广告已展示的次数
        int sdk_num = sdkConfigModel.getShow_sum();
        if (real_num >= sdk_num){

            return true;
        }
        return false;

    }
    protected boolean checkADShow(){
        if (!sdkConfigModel.isAdShow()){
            return true;
        }
        return false;
    }
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
