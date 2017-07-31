package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.asyn.HbReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;

import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiSplashADListener;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;

import com.mg.others.utils.SP;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD extends MiiBaseAD{

     private Context mContext;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiSplashADListener listener;
     private AdModel adModel;
     private ImageView adImageView;
     private WebView webView;
     private CountDownTimer timer;
     private boolean isJoinImg = false;

     private ReqAsyncModel reqAsyncModel = new ReqAsyncModel();

     Handler mainHandler = new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case 100:
                     startupAD();
                     break;
                 case 200:
                    try {
                        adModel = (AdModel) msg.obj;
                        if (adModel == null){
                            listener.onMiiNoAD(3002);
                            return;
                        }
                        checkADType(adModel);
                    }
                    catch (Exception e){
                        listener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;
                 case 300:
                     try {
                         Bitmap bitmap = (Bitmap) msg.obj;
                         if (bitmap == null){
                             listener.onMiiNoAD(3011);
                             return;
                         }
                         showSplashAD(bitmap);
                     }
                     catch (Exception e){
                         listener.onMiiNoAD(3011);
                         e.printStackTrace();
                     }
                     break;
                 case 500:
                     listener.onMiiNoAD(1000);
                     break;
                 case 600:
                     new HbReturn(reqAsyncModel).fetchMGAD();
                     break;
                 case 700:
                     listener.onMiiNoAD(3011);
                     break;

             }
         }
     };

    public  MiiSplashAD(Activity activity, ViewGroup adContainer,View skipContainer ,String appid,String lid,MiiSplashADListener adListener){
      try{
            this.mActivity = activity;

            this.mContext = activity.getApplicationContext();

            this.adContainer = adContainer;

            this.skipContainer = skipContainer;

            this.listener = adListener;

            if ( activity == null || adContainer == null || skipContainer == null){
                listener.onMiiNoAD(2000);
                return;
            }

            reqAsyncModel.context = this.mContext;
            reqAsyncModel.handler = this.mainHandler;
            reqAsyncModel.listener = this.listener;
            reqAsyncModel.pt = 2;
            reqAsyncModel.appid = appid;
            reqAsyncModel.lid = lid;

            if(Build.VERSION.SDK_INT >= M){
                check23AbovePermission(mActivity,mainHandler);
                return;
            }

            new HbReturn(reqAsyncModel).fetchMGAD();
      }
      catch (Exception e){

          listener.onMiiNoAD(2001);
          e.printStackTrace();

      }
    }


    private void  checkADType(final AdModel adModel){

      if (adModel.getType()==4){

         try {
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

                    listener.onMiiADClicked();
                    listener.onMiiADDismissed();

                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    //点击上报
                    HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, mContext);

                    if (timer != null){
                        timer.cancel();
                    }

                    return true;
                }
            });

            webView.loadDataWithBaseURL("",adModel.getPage(), "text/html", "utf-8", "");
            adContainer.addView(webView);

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

                  try {
                      if (timer != null){
                          timer.cancel();
                      }
                      if (webView != null){
                          ViewParent parent = webView.getParent();
                          if (parent != null) {
                              ((ViewGroup) parent).removeView(webView);
                          }
                          webView.stopLoading();
                          webView.getSettings().setJavaScriptEnabled(false);
                          webView.clearHistory();
                          webView.clearView();
                          webView.removeAllViews();
                          try {
                              webView.destroy();
                          } catch (Exception e) {

                              e.printStackTrace();

                          }
                      }
                  }
                  catch (Exception e){

                      e.printStackTrace();

                  }

                  listener.onMiiADDismissed();

                }
            });
         }catch (Exception e){

               listener.onMiiNoAD(3010);
               e.printStackTrace();

         }

      }else {
          try {

            if (adModel.getImage() == null || adModel.getImage().equals("") || adModel.getImage().equals("null")){

                if (adModel.getIcon() == null || adModel.getIcon().equals("") || adModel.getIcon().equals("null")){

                  isJoinImg = false;
                  listener.onMiiNoAD(3011);
                }
                else {
                    isJoinImg = true;
                    new ImageDownloadHelper().downloadShowImage(mContext,adModel.getIcon(),mainHandler);
                }

            }
            else {
              isJoinImg = false;
              new ImageDownloadHelper().downloadShowImage(mContext,adModel.getImage(),mainHandler);

            }

          }catch (Exception e){

              listener.onMiiNoAD(3011);
              e.printStackTrace();
          }
      }

    }


    //拼图
    private void JoinImg(final Bitmap bitmap){
      try {

        RelativeLayout relativeLayout = new RelativeLayout(mActivity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setBackgroundColor(Color.parseColor("#d1dbee"));
        relativeLayout.setLayoutParams(layoutParams);


        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dip2px(mContext,80), dip2px(mContext,80));
        adImageView = new ImageView(mActivity);
        layoutParams1.setMargins(0,300,0,50);
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        adImageView.setId(100);
        adImageView.setScaleType(ImageView.ScaleType.FIT_XY);


        TextView nameTxt= new TextView(mActivity);
        RelativeLayout.LayoutParams params1= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params1.addRule(RelativeLayout.BELOW,100);
        params1.setMargins(100,0,100,0);
        nameTxt.setId(200);
        nameTxt.setTextSize(20);
        nameTxt.setTextColor(Color.parseColor("#8B7D6B"));
        if (adModel.getName() != null && ! adModel.getName().equals("null")){
          nameTxt.setText(adModel.getName());
        }

        TextView descTxt= new TextView(mActivity);
        RelativeLayout.LayoutParams params2= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params2.setMargins(100,50,100,0);
        params2.addRule(RelativeLayout.BELOW,200);
        descTxt.setTextColor(Color.parseColor("#8B7D6B"));
        descTxt.setTextSize(20);
        if (adModel.getDesc() != null && ! adModel.getDesc().equals("null")){
          descTxt.setText(adModel.getDesc());
        }

        relativeLayout.addView(nameTxt,params1);
        relativeLayout.addView(descTxt,params2);
        relativeLayout.addView(adImageView,layoutParams1);
        adContainer.addView(relativeLayout);

        TextView tv = tvADCreate(mActivity);
        if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
            tv.setText(adModel.getSourceMark()+"|广告");
        }
        adContainer.addView(tv);

        adImageView.setImageBitmap(bitmap);

        //广告成功展示
        listener.onMiiADPresent();

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

        //倒计时开始
        adCountDownTimer();

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        skipContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (bitmap != null){
                        bitmap.recycle();
                    }
                    if(timer != null){
                        timer.cancel();
                    }

                }
                catch (Exception e){

                    e.printStackTrace();

                }

                listener.onMiiADDismissed();

            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    listener.onMiiADClicked();
                    listener.onMiiADDismissed();

                    AdModel ad= (AdModel) adModel.clone();
                    new ADClickHelper(mContext).AdClick(ad);
                    if (bitmap != null){
                        bitmap.recycle();
                    }
                    if (timer != null){
                        timer.cancel();
                    }

                }catch (Exception e){

                    e.printStackTrace();

                }

            }
        });
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        adModel.setDownx(String.valueOf(event.getX()));
                        adModel.setDowny(String.valueOf(event.getY()));
                        break;
                    case MotionEvent.ACTION_UP:
                        adModel.setUpx(String.valueOf(event.getX()));
                        adModel.setUpy(String.valueOf(event.getY()));
                        break;
                    default:
                        break;
                }
                listener.onMiiADTouched();
                return false;
            }
        });
      }catch (Exception e){
          listener.onMiiNoAD(3009);
          e.printStackTrace();
      }
    }

     private void showSplashAD(final Bitmap bitmap){

       try {
             if (isJoinImg){
                 JoinImg(bitmap);
                 return;
             }

             FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
             adImageView = new ImageView(mActivity);
             adImageView.setLayoutParams(layoutParams);
             adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
             adContainer.addView(adImageView);

             TextView tv = tvADCreate(mActivity);
             if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
                tv.setText(adModel.getSourceMark()+"|广告");
             }
             adContainer.addView(tv);

             adImageView.setImageBitmap(bitmap);

             //广告成功展示
             listener.onMiiADPresent();

             //展示上报
             HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

             //倒计时开始
             adCountDownTimer();

             //记录展示次数
             int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
             SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

             skipContainer.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                   try {

                     if (bitmap != null){
                         bitmap.recycle();
                     }
                     if(timer != null){
                       timer.cancel();
                     }

                   }
                   catch (Exception e){

                       e.printStackTrace();

                   }

                   listener.onMiiADDismissed();

                 }
             });
             adImageView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                    try {
                        listener.onMiiADClicked();
                        listener.onMiiADDismissed();

                         AdModel ad= (AdModel) adModel.clone();
                         new ADClickHelper(mContext).AdClick(ad);
                         if (bitmap != null){
                             bitmap.recycle();
                         }
                         if (timer != null){
                             timer.cancel();
                         }

                    }catch (Exception e){

                        e.printStackTrace();

                    }

                 }
             });
            adImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            adModel.setDownx(String.valueOf(event.getX()));
                            adModel.setDowny(String.valueOf(event.getY()));
                            break;
                        case MotionEvent.ACTION_UP:
                            adModel.setUpx(String.valueOf(event.getX()));
                            adModel.setUpy(String.valueOf(event.getY()));
                            break;
                        default:
                            break;
                    }
                    listener.onMiiADTouched();
                    return false;
                }
            });

       }catch (Exception e){

           listener.onMiiNoAD(3009);
           e.printStackTrace();

       }
     }
     private void adCountDownTimer(){

       try {

         sdk = checkSdkConfig(sdk,mContext);

         long time = sdk.getSplash_time();

         timer = new CountDownTimer((time+1)*1000,1000){
             @Override
             public void onTick(long millisUntilFinished) {

                 listener.onMiiADTick((long) ((Math.floor(millisUntilFinished/1000))*1000));
             }

             @Override
             public void onFinish() {

                 listener.onMiiADDismissed();
             }
         };
         timer.start();

       }catch (Exception e){

           listener.onMiiNoAD(3008);
           e.printStackTrace();

       }

     }


    private void startupAD(){

      try{

        SourceAssignModel saModel = checkADSource(mContext,2);

        if (saModel == null){
            new HbReturn(reqAsyncModel).fetchMGAD();
            return;
        }

        if (saModel.type == 1){

            listener.onMiiNoAD(3005);
            return;

        }
        new RaReturn(reqAsyncModel).fetchMGAD();

      }catch (Exception e){

          listener.onMiiNoAD(3012);
          e.printStackTrace();

      }
    }


    @Override
    public void recycle() {
        if (mActivity != null){
           mActivity.finish();
        }
    }
}
