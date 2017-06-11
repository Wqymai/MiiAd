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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mg.comm.ADClickHelper;
import com.mg.comm.MiiADListener;
import com.mg.demo.Constants;
import com.mg.others.manager.ApkDownloadManager;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.CommonUtils;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MhttpRequestHelper;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;


/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD {

     private Context mContext;
     private SDKConfigModel sdk;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiADListener listener;
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
                        ImageDownloadHelper.downloadShowImage(mContext,adModel.getImage(),adImageView,mainHandler);
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
                 new ADClickHelper(mContext).AdClick(adModel);


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

         CountDownTimer timer=new CountDownTimer((time+1)*1000,1000){
             @Override
             public void onTick(long millisUntilFinished) {
                 Log.i(Constants.TAG,"倒计时 "+millisUntilFinished);
                 listener.onMiiADTick((long) ((Math.floor(millisUntilFinished/1000))*1000));
             }

             @Override
             public void onFinish() {
                 Log.i(Constants.TAG,"倒计时结束 ");
                 listener.onMiiADDismissed();

             }
         };
         timer.start();

     }

     public  MiiSplashAD(Activity activity, ViewGroup adContainer,View skipContainer,MiiADListener adListener){

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
             MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,2,listener);
             mhttpRequest.fetchMGAD();
         }
     }





}
