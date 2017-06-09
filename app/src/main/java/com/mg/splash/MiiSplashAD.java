package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mg.demo.Constants;
import com.mg.others.manager.ApkDownloadManager;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.MConstant;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.CommonUtils;
import com.mg.util.DownloadUtil;
import com.mg.util.MhttpRequest;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;


/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD {

     private Context mContext;
     private SDKConfigModel sdk;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiSplashADListener listener;
     private AdModel adModel;
     private ImageView adImageView;


     Handler mainHandler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case 200:
                    Log.i(Constants.TAG,"收到RA请求成功的消息");
                    try {
                        adModel= (AdModel) msg.obj;
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        adImageView=new ImageView(mActivity);
                        adImageView.setLayoutParams(layoutParams);
                        adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        adContainer.addView(adImageView);
                        DownloadUtil.downloadShowImage(mContext,adModel.getImage(),adImageView,mainHandler);
                    }
                    catch (Exception e){
                        adContainer.removeView(adImageView);
                        e.printStackTrace();
                    }
                    break;
                 case 300:
                     try {
                         Bitmap bitmap= (Bitmap) msg.obj;
                         if (bitmap == null){
                             adContainer.removeView(adImageView);
                             return;
                         }
                        showSplashAD(bitmap);
                     }
                     catch (Exception e){
                         adContainer.removeView(adImageView);
                         e.printStackTrace();
                     }
                     break;
             }

         }
     };


     private void showSplashAD(Bitmap bitmap){

         adImageView.setImageBitmap(bitmap);

         //展示上报
         HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

         //广告成功展示
         listener.onMiiADPresent();

         //倒计时开始
         adCountDownTimer();

         skipContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.onMiiADDismissed();
                 mActivity.finish();
             }
         });
         adImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 listener.onMiiADClicked();
                 listener.onMiiADDismissed();

                 //点击广告后相关行为
                 AdClick(adModel);

                 mActivity.finish();

             }
         });

     }
     private void adCountDownTimer(){
         //广告倒计时
         if (sdk == null){
             sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
         }
         long time = sdk.getDisplayTime(2);
         Log.i(Constants.TAG,"开始倒计时 time="+time);
         CountDownTimer timer=new CountDownTimer(time*1000,1000){
             @Override
             public void onTick(long millisUntilFinished) {
                 Log.i(Constants.TAG,"倒计时 "+millisUntilFinished);
                 listener.onMiiADTick(millisUntilFinished);
             }

             @Override
             public void onFinish() {
                 Log.i(Constants.TAG,"倒计时结束 ");
                 listener.onMiiADDismissed();
                 mActivity.finish();
             }
         };
         timer.start();

     }

     public  MiiSplashAD(Activity activity, ViewGroup adContainer,View skipContainer,MiiSplashADListener adListener){

         this.mActivity=activity;

         this.mContext = activity.getApplicationContext();

         this.adContainer=adContainer;

         this.skipContainer=skipContainer;

         this.listener=adListener;

         sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);

         //根据条件判断是展示广点通还是麦广
         boolean isGDT=true;

         if (false){

             Log.i(Constants.TAG,"加载广点通广告...");
             new SplashAD(activity, adContainer, skipContainer, Constants.APPID, Constants.SplashPosID, new SplashADListener() {
                 @Override
                 public void onADDismissed() {
                     listener.onMiiADDismissed();
                 }

                 @Override
                 public void onNoAD(int i) {
                     listener.onMiiNoAD(i);
                 }

                 @Override
                 public void onADPresent() {
                     listener.onMiiADPresent();
                 }

                 @Override
                 public void onADClicked() {
                     listener.onMiiADClicked();
                 }

                 @Override
                 public void onADTick(long l) {
                    listener.onMiiADTick(l);
                 }
             }, 0);
         }
         else {
            Log.i(Constants.TAG,"加载麦广广告...");
            fetchMGAD();
         }
     }


    private void fetchMGAD(){
        //麦广相关的
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);

        final MhttpRequest mhttpRequest = new MhttpRequest(mContext,mainHandler,2,listener);

        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {

                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    mhttpRequest.startRequest();
                }
            }, mContext).execute();
        }
        else {
            mhttpRequest.startRequest();
        }
    }

    //点击打开
    private void AdClick(AdModel ad) {
        //点击上报
        HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

        if (ad == null) {
            return;
        }

        if (ad.getType() != MConstant.adClickType.app) {
            CommonUtils.openBrowser(mContext, ad.getUrl());
            return;
        }

        else {
            if (ad.getUrl().contains("click")){
                CommonUtils.openBrowser(mContext, ad.getUrl());
                return;
            }
            if (CommonUtils.getNetworkSubType(mContext) == CommonUtils.NET_TYPE_WIFI) {
                ApkDownloadManager manager = ApkDownloadManager.getIntance(mContext);
                manager.downloadFile(ad);

                //开始下载上报
                HttpManager.reportEvent(adModel, AdReport.EVENT_DOWNLOAD_START, mContext);
                return;
            }
        }
    }


}
