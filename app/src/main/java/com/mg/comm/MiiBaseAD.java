package com.mg.comm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.mg.mv4.ActivityCompat;
import com.mg.mv4.ContextCompat;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        if (sdk == null){
            return null;
        }
        SourceAssignModel saModel=new SourceAssignModel();
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
        return saModel;
    }



    
    protected SDKConfigModel checkSdkConfig(SDKConfigModel sdkConfigModel,Context mContext){
        if (sdkConfigModel == null){

            return CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        return sdkConfigModel;
    }


    protected Map<String,String> getGdtIds(Context context){
        Map<String,String> maps=new HashMap<>();
        SDKConfigModel sdkConfig=checkSdkConfig(sdk,context);
        try {
            String gdtIds = sdkConfig.getList();
            String gdtIds_json = MiiLocalStrEncrypt.deCodeStringToString(gdtIds, LocalKeyConstants.LOCAL_GDT);
            JSONObject object=new JSONObject(gdtIds_json);
            maps.put("AID",object.optString("AID"));
            maps.put("SPID",object.optString("SPID"));
            maps.put("BPID",object.optString("SPID"));
            maps.put("IPID",object.optString("IPID"));
            maps.put("NPID",object.optString("NPID"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return maps;
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
