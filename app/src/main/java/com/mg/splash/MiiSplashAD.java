package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MhttpRequestHelper;
import com.mg.comm.MiiADListener;
import com.mg.comm.MiiBaseAD;
import com.mg.demo.Constants;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;




/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD extends MiiBaseAD {

     private Context mContext;
     private SDKConfigModel sdk;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiADListener listener;
     private AdModel adModel;
     private ImageView adImageView;
     private WebView webView;
     CountDownTimer timer;



     Handler mainHandler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case 100:
                     startupAD();
                     break;

                 case 200:
                    Log.i(Constants.TAG,"收到RA请求成功的消息");
                    try {
                        adModel= (AdModel) msg.obj;
                        checkADType(adModel);
                    }
                    catch (Exception e){

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
                 case 400:
                     try {

                        openGDTAD(true);

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                     break;
             }

         }
     };


    private void  checkADType(AdModel adModel){

        if (adModel.getType() == 4){//h5广告

            Log.i(Constants.TAG,"是HTML5广告...");
            webView = new WebView(mActivity);
            FrameLayout.LayoutParams params_webview = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            webView.setLayoutParams(params_webview);
            WebSettings settings = webView.getSettings();
            settings.setDefaultTextEncodingName("utf-8") ;
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    //广告点击回调
                    listener.onMiiADClicked();

                    return true;
                }
            });

            webView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
            adContainer.addView(webView);


            TextView tv=tvADCreate();
            adContainer.addView(tv);

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);
            //广告成功展示
            listener.onMiiADPresent();

            //倒计时开始
            adCountDownTimer();

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

            skipContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //广告关闭回调
                    listener.onMiiADDismissed();
                    mActivity.finish();
                }
            });

        }else {
            Log.i("Constants.TAG","不是HTML5广告...");
            FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            adImageView=new ImageView(mActivity);
            adImageView.setLayoutParams(layoutParams);
            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            adContainer.addView(adImageView);

            TextView tv=tvADCreate();
            adContainer.addView(tv);


            new ImageDownloadHelper(0).downloadShowImage(mContext,adModel.getImage(),adImageView,mainHandler);
        }

    }

     //"广告"提示
     private TextView tvADCreate(){
         TextView tv=new TextView(mActivity);
         tv.setText("广告");
         tv.setTextSize(10);
         tv.setPadding(5,3,5,3);
         tv.setBackgroundColor(Color.argb(50, 41, 36, 33));
         tv.setGravity(Gravity.CENTER);
         tv.setTextColor(Color.parseColor("#FFF0F5"));
         FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         lp.gravity=Gravity.RIGHT|Gravity.BOTTOM;
         tv.setLayoutParams(lp);
         return tv;
     }

     private void showSplashAD(final Bitmap bitmap){

         adImageView.setImageBitmap(bitmap);

         //展示上报
         HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

         //广告成功展示
         listener.onMiiADPresent();


         //倒计时开始
         adCountDownTimer();

         //记录展示次数
         int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
         SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

         skipContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.onMiiADDismissed();
                 if (bitmap!=null){
                     bitmap.recycle();
                 }

                 timer.cancel();
                 mActivity.finish();
             }
         });
         adImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 listener.onMiiADClicked();

                 //点击广告后相关行为
                 new ADClickHelper(mContext).AdClick(adModel);

                 timer.cancel();

                 mActivity.finish();

             }
         });

     }
     private void adCountDownTimer(){

         sdk = checkSdkConfig(sdk,mContext);

         long time = sdk.getDisplayTime(2);
         Log.i(Constants.TAG,"开始倒计时 time="+time);

         timer = new CountDownTimer((time+1)*1000,1000){
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

         if (skipContainer == null){
             return;
         }

//         boolean isFirst= (boolean) SP.getParam(SP.CONFIG,mContext,SP.FIRSTHB,true);
//         if (isFirst){
//             SP.setParam(SP.CONFIG,mContext,SP.FIRSTHB,false);
//             new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD(isFirst);
//             return;
//         }
         if (isFirstEnter(mContext)){
             new MhttpRequestHelper(mContext,mainHandler,0,listener).fetchMGAD(true);
             return;
         }

         //根据条件判断是展示广点通还是麦广
         startupAD();


     }

    private void startupAD(){

//        if (sdk == null){
//            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
//        }
//        int sf_mg = sdk.getSf_mg();
//        int sf_gdt = sdk.getSf_gdt();
//        int sum = sf_gdt + sf_mg;
//        if (sum == 0){
//
//            Log.i(Constants.TAG,"sum==0");
//            return;
//
//        }
//        else if (sum == 100){
//            int show_percentage = (int) ((Math.random() * 100)+1);
//            if (show_percentage <= sf_mg){
//
//                 Log.i(Constants.TAG,"sum==100 MG");
//                 openGMAD();
//
//            }
//            else {
//
//                Log.i(Constants.TAG,"sum==100 GDT");
//                openGDTAD(true);
//
//            }
//        }
//        else if (sum > 100){
//            if (sf_mg > sf_gdt){
//
//                Log.i(Constants.TAG,"sum > 100 MG");
//               if (sdk.isAdShow()){
//
//                  new MhttpRequestHelper(mContext,mainHandler,2,listener).fetchMGAD1(false);
//
//               }
//               else {
//
//                   mainHandler.sendEmptyMessage(400);
//
//               }
//            }
//            else {
//
//                Log.i(Constants.TAG,"sum > 100 GDT");
//                openGDTAD(false);
//
//            }
//        }
        SourceAssignModel saModel = checkADSource(mContext);

        if (saModel.type == 1){
            return;
        }
        else if (saModel.type == 2){

            if (saModel.firstChoose == 1){


                openMGAD_Single(mContext,mainHandler,listener,sdk,2);
            }
            else {

                openGDTAD(true);

            }
        }
        else if (saModel.type == 3){

            if (saModel.firstChoose == 1){


                openMGAD_Sequ(mContext,mainHandler,listener,sdk,2);

            }
            else {
                openGDTAD(false);
            }
        }


    }


//    private void openMGAD1(){
//        Log.i(Constants.TAG,"加载麦广广告...openGMAD1");
//
//        if (sdk == null){
//            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
//        }
//
//        if (sdk.isAdShow()){
//
//            new MhttpRequestHelper(mContext,mainHandler,2,listener).fetchMGAD1(false);
//
//        }
//        else {
//
//            mainHandler.sendEmptyMessage(400);
//
//        }
//    }


//    private void openGMAD(){
//        Log.i(Constants.TAG,"加载麦广广告...openGMAD");
//
//        if (sdk == null){
//            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
//        }
//        if (sdk.isAdShow()){
//
//            new MhttpRequestHelper(mContext,mainHandler,2,listener).fetchMGAD(false);
//
//        }else {
//
//            listener.onMiiNoAD(2000);
//
//        }
//
//    }
    private void openGDTAD(final boolean shouldReturn){
        Log.i(Constants.TAG,"加载广点通广告...");

        new SplashAD(mActivity, adContainer, skipContainer, Constants.APPID, Constants.SplashPosID, new SplashADListener() {
            @Override
            public void onADDismissed() {

                listener.onMiiADDismissed();
            }

            @Override
            public void onNoAD(int i) {
                if (!shouldReturn){

//                    if (isADShow(sdk,mContext)){
//                        new MhttpRequestHelper(mContext,mainHandler,2,listener).fetchMGAD1(true);
//                    }
//                    else {
//                        Log.i(Constants.TAG,"openGDTAD...o=0");
//                        listener.onMiiNoAD(2000);
//                    }
                    openMGAD_Sequ_Gdtfail(mContext,mainHandler,listener,sdk,2);


                    return;
                }
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





}
