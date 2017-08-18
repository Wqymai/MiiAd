package com.mg.comm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mg.mv4.ActivityCompat;
import com.mg.mv4.ContextCompat;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.OtherInfoModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import org.json.JSONObject;

import java.math.BigDecimal;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static java.lang.Thread.sleep;


/**
 * Created by wuqiyan on 2017/6/13.
 */

public abstract class MiiBaseAD {

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
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public  double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected  SDKConfigModel checkSdkConfig(Context mContext){
        if (sdk == null){
            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        return sdk;
    }
    /**
    判断广告来源方式 1：广告源都关闭了 2：按比例展示其中一家 3：先哪家再哪家
    */
    protected SourceAssignModel checkADSource(Context mContext,int pt){


      SourceAssignModel saModel=new SourceAssignModel();
      try {
        sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        if (sdk == null){
            return null;
        }

        int sf_mg = 0 ;
        int sf_gdt = 0 ;
        if (pt == 1){//banner
            sf_mg = sdk.getBsf_mg();
            sf_gdt =sdk.getBsf_gdt();
        }
        else if (pt == 2){//开屏
            sf_mg = sdk.getKsf_mg();
            sf_gdt =sdk.getKsf_gdt();
        }
        else if (pt == 3){//插屏
            sf_mg = sdk.getCsf_mg();
            sf_gdt = sdk.getCsf_gdt();
        }else {//信息流
            sf_mg = sdk.getXsf_mg();
            sf_gdt = sdk.getXsf_gdt();
        }

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

    protected boolean autoAd(Context context, AdModel adModel, int pt){

        sdk = checkSdkConfig(sdk,context);
        int show_percentage = (int) ((Math.random() * 100)+1);

        if (pt == 2){
             if (show_percentage < sdk.getKc()){

                 executeAuto(adModel,context);

                 return true;
             }
        }
        if (pt == 1){
            if (show_percentage < sdk.getBc()){

                executeAuto(adModel,context);
                return true;

            }
        }
        if (pt == 3){
            if (show_percentage < sdk.getXc()){

                executeAuto(adModel,context);
                return true;

            }
        }

        return false;
    }

    private void executeAuto(AdModel adModel,Context context){

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, context);


        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, context, SP.FOT, 0);
        SP.setParam(SP.CONFIG, context, SP.FOT, show_num + 1);


        try {
            int show_percentage = (int) (Math.random() * 4);
            sleep(show_percentage*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        int h = CommonUtils.getScreenH(context);
        int w = CommonUtils.getScreenW(context);

        int x =(int) ((Math.random() * w)+1);
        int y =(int) ((Math.random() * h)+1);

        int small_x = (int) (Math.random() * 1000);
        int small_y = (int) (Math.random() * 1000);

        adModel.setDownx(x+"."+small_x);
        adModel.setDownx(y+"."+small_y);

        adModel.setUpx(x+"."+small_x);
        adModel.setUpy(y+"."+small_y);


        //点击上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, context);



        if (adModel.getType() !=4){
            AdModel ad= (AdModel) adModel.clone();
            new ADClickHelper(context).AdClick(ad);
        }
    }


    // a=AID s=SPID b=BPID i=IPID(广点通)
    protected OtherInfoModel getOtherIds(Context context){
        SDKConfigModel sdkConfig = checkSdkConfig(sdk,context);
        OtherInfoModel otherInfo = new OtherInfoModel();
        try {
            String gdtIds = sdkConfig.getSk();
            String gdtIds_json = MiiLocalStrEncrypt.deCodeStringToString(gdtIds, LocalKeyConstants.LOCAL_GDT);
            JSONObject object=new JSONObject(gdtIds_json);
            otherInfo.setGdtAPPID(object.optString("a"));
            otherInfo.setGdtSplashPosID(object.optString("s"));
            otherInfo.setGdtInterteristalPosID(object.optString("i"));
            otherInfo.setGdtBannerPosID(object.optString("b"));
            otherInfo.setTtAPPID(object.optString("tt_aid"));
            otherInfo.setTtNativePosID(object.optString("tt_nid"));
        }
        catch (Exception e){
            otherInfo.setGdtAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_AID,LocalKeyConstants.LOCAL_GDT));
            otherInfo.setGdtSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_SPID,LocalKeyConstants.LOCAL_GDT));
            otherInfo.setGdtInterteristalPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_IPID,LocalKeyConstants.LOCAL_GDT));
            otherInfo.setGdtBannerPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_BPID,LocalKeyConstants.LOCAL_GDT));
            otherInfo.setTtAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.TT_AID,LocalKeyConstants.LOCAL_GDT));
            otherInfo.setTtNativePosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.TT_NID,LocalKeyConstants.LOCAL_GDT));
            e.printStackTrace();
        }
        if (otherInfo.getGdtAPPID() == null || otherInfo.getGdtAPPID().equals("")){
            otherInfo.setGdtAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_AID,LocalKeyConstants.LOCAL_GDT));
        }
        if (otherInfo.getGdtSplashPosID() == null || otherInfo.getGdtSplashPosID().equals("")){
            otherInfo.setGdtSplashPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_SPID,LocalKeyConstants.LOCAL_GDT));
        }
        if (otherInfo.getGdtInterteristalPosID() == null || otherInfo.getGdtInterteristalPosID().equals("")){
            otherInfo.setGdtInterteristalPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_IPID,LocalKeyConstants.LOCAL_GDT));
        }
        if (otherInfo.getGdtBannerPosID() == null || otherInfo.getGdtBannerPosID().equals("")){
            otherInfo.setGdtBannerPosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.GDT_BPID,LocalKeyConstants.LOCAL_GDT));
        }
        if (otherInfo.getTtAPPID() == null || otherInfo.getTtAPPID().equals("")){
           otherInfo.setTtAPPID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.TT_AID,LocalKeyConstants.LOCAL_GDT));
        }
        if (otherInfo.getTtNativePosID() == null || otherInfo.getTtNativePosID().equals("")){
           otherInfo.setTtNativePosID(MiiLocalStrEncrypt.deCodeStringToString(MConstant.TT_NID,LocalKeyConstants.LOCAL_GDT));
        }
        return otherInfo;
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
            mainHandler.sendEmptyMessage(500);
            e.printStackTrace();
        }
    }
    //"广告"提示
    public TextView tvADCreate(Activity mActivity){
        TextView tv=new TextView(mActivity);
        tv.setText("广告");
        tv.setTextSize(10);
        tv.setPadding(5,3,5,3);
        tv.setBackgroundColor(Color.argb(10, 41, 36, 33));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT|Gravity.BOTTOM;
        tv.setLayoutParams(lp);
        return tv;
    }

    //true是竖屏 false是横屏
    public boolean checkOrientation(Activity mActivity){
      try {

        Configuration mConfiguration = mActivity.getResources().getConfiguration(); //获取设置的配置信息

        int ori = mConfiguration.orientation ; //获取屏幕方向

        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;

        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){

            mActivity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            return false;

        }
      }
      catch (Exception e){
          e.printStackTrace();
      }
        return true;
    }

    public abstract void recycle();

}
