package com.mg.comm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.mg.mv4.ActivityCompat;
import com.mg.mv4.ContextCompat;
import com.mg.others.model.GdtInfoModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by wuqiyan on 2017/6/13.
 */

public class MiiBaseAD {

    protected SDKConfigModel sdk;

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
        public int firstChoose;//优先选择

    }
    /**
    判断广告来源方式 1：广告源都关闭了 2：按比例展示其中一家 3：先哪家再哪家
    */
    protected SourceAssignModel checkADSource(Context mContext){
      SourceAssignModel saModel=new SourceAssignModel();
      try {
        sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        if (sdk == null){
            return null;
        }
        int sf_mg = sdk.getSf_mg();
        int sf_gdt = sdk.getSf_gdt();
        int sum = sf_gdt + sf_mg;
        if (sum == 0){
            saModel.type = 1;
        }
        else if (sum == 100){
            saModel.type = 2;
            int show_percentage = (int) ((Math.random() * 100)+1);
            if (show_percentage <= sf_mg){
                saModel.firstChoose = 1;
            }
            else {
                saModel.firstChoose = 2;
            }
        }
        else if (sum > 100){
            saModel.type = 3;
            if (sf_mg > sf_gdt){
                saModel.firstChoose = 1;
            }
            else {
                saModel.firstChoose = 2;
            }
        }
      }catch (Exception e){
          e.printStackTrace();
      }
        return saModel;
    }



    
    protected SDKConfigModel checkSdkConfig(SDKConfigModel sdkConfigModel,Context mContext){
        if (sdkConfigModel == null){

            return CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        return sdkConfigModel;
    }


    // a=AID s=SPID b=BPID i=IPID
    protected GdtInfoModel getGdtIds(Context context){
        SDKConfigModel sdkConfig = checkSdkConfig(sdk,context);
        GdtInfoModel gdt=new GdtInfoModel();
        try {
            String gdtIds = sdkConfig.getList();
            String gdtIds_json = MiiLocalStrEncrypt.deCodeStringToString(gdtIds, LocalKeyConstants.LOCAL_GDT);
            JSONObject object=new JSONObject(gdtIds_json);
            gdt.setAPPID(object.optString("a"));
            gdt.setSplashPosID(object.optString("s"));
            gdt.setInterteristalPosID(object.optString("i"));
            gdt.setBannerPosID(object.optString("b"));
        }
        catch (Exception e){
            gdt.setAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_AID,LocalKeyConstants.LOCAL_GDT));
            gdt.setSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_SPID,LocalKeyConstants.LOCAL_GDT));
            gdt.setInterteristalPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_IPID,LocalKeyConstants.LOCAL_GDT));
            gdt.setBannerPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_BPID,LocalKeyConstants.LOCAL_GDT));
            e.printStackTrace();
        }
        if (gdt.getAPPID() == null || gdt.getAPPID().equals("")){
            gdt.setAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_AID,LocalKeyConstants.LOCAL_GDT));
        }
        if (gdt.getSplashPosID() == null || gdt.getSplashPosID().equals("")){
            gdt.setSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_SPID,LocalKeyConstants.LOCAL_GDT));
        }
        if (gdt.getInterteristalPosID() == null || gdt.getInterteristalPosID().equals("")){
            gdt.setSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_IPID,LocalKeyConstants.LOCAL_GDT));
        }
        if (gdt.getBannerPosID() == null || gdt.getBannerPosID().equals("")){
            gdt.setSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_BPID,LocalKeyConstants.LOCAL_GDT));
        }
        return gdt;
    }



    /**
    android 6.0以上检查权限WRITE_EXTERNAL_STORAGE
    */
    public void check23AbovePermission(final Activity activity, final Handler mainHandler){
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity.getApplicationContext(), ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(activity, new String[]{READ_PHONE_STATE,ACCESS_COARSE_LOCATION,WRITE_EXTERNAL_STORAGE}, 123);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity.getApplicationContext(), ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED
                                ) {

                            mainHandler.sendEmptyMessage(500);
                        }
                        else {

                            mainHandler.sendEmptyMessage(600);
                        }
                    }
                },5000);

            }
            else {

                mainHandler.sendEmptyMessage(600);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
