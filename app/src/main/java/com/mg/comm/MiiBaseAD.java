package com.mg.comm;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.mg.demo.Constants;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;



/**
 * Created by wuqiyan on 2017/6/13.
 */

public class MiiBaseAD {

    protected boolean isFirstEnter(Context mContext){
        boolean isFirst= (boolean) SP.getParam(SP.CONFIG,mContext,SP.FIRSTHB,true);
        if (isFirst){
            SP.setParam(SP.CONFIG,mContext,SP.FIRSTHB,false);
            return true;
        }
        return false;
    }
    protected class SourceAssignModel{
        public int type;//分配类型
        public int firstChoose;

    }
    protected SourceAssignModel checkADSource(Context mContext){

        SDKConfigModel sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        SourceAssignModel saModel=new SourceAssignModel();
        int sf_mg = sdk.getSf_mg();
        int sf_gdt = sdk.getSf_gdt();
        int sum = sf_gdt + sf_mg;
        if (sum == 0){
            Log.i(Constants.TAG,"sum==0");
            saModel.type = 1;
        }
        else if (sum == 100){
            saModel.type = 2;
            int show_percentage = (int) ((Math.random() * 100)+1);
            if (show_percentage <= sf_mg){

                Log.i(Constants.TAG,"sum==100 MG");
                saModel.firstChoose = 1;

            }
            else {
                saModel.firstChoose = 2;
                Log.i(Constants.TAG,"sum==100 GDT");
            }
        }
        else if (sum > 100){
            saModel.type = 3;
            if (sf_mg > sf_gdt){
                saModel.firstChoose = 1;
            }
            else {

                Log.i(Constants.TAG,"sum > 100 GDT");
                saModel.firstChoose = 2;

            }
        }
        return saModel;
    }

    protected void openMGAD_Sequ_Gdtfail(Context mContext, Handler mainHandler, MiiADListener listener,SDKConfigModel sdk, int pt){

        if (isADShow(sdk,mContext)){

            new MhttpRequestHelper(mContext,mainHandler,2,listener).fetchMGAD1(true);

        }
        else {

            listener.onMiiNoAD(2000);
        }
    }

    protected  void openMGAD_Sequ(Context mContext, Handler mainHandler, MiiADListener listener,SDKConfigModel sdk, int pt){

        Log.i(Constants.TAG,"加载麦广广告...openMGAD_Sequ");

        SDKConfigModel sdkConfigModel = checkSdkConfig(sdk,mContext);

        if (sdkConfigModel.isAdShow()){

            new MhttpRequestHelper(mContext,mainHandler,pt,listener).fetchMGAD1(false);

        }
        else {

            mainHandler.sendEmptyMessage(400);

        }
    }

    protected void openMGAD_Single(Context mContext, Handler mainHandler, MiiADListener listener,SDKConfigModel sdk, int pt){

        Log.i(Constants.TAG,"加载麦广广告...openMGAD_Single");

        if (isADShow(sdk,mContext)){

            new MhttpRequestHelper(mContext,mainHandler,pt,listener).fetchMGAD(false);

        }else {

            listener.onMiiNoAD(2000);

        }
    }

    protected SDKConfigModel checkSdkConfig(SDKConfigModel sdkConfigModel,Context mContext){
        if (sdkConfigModel == null){

            return CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        return sdkConfigModel;
    }

    protected boolean isADShow(SDKConfigModel sdkConfigModel,Context mContext){

        SDKConfigModel sdk = checkSdkConfig(sdkConfigModel,mContext);

        if (sdk.isAdShow()){
            return true;
        }
        else {
            return false;
        }
    }
}
