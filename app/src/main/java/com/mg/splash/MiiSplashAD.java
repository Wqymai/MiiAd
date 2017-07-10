package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mg.asyn.FirstEnter;
import com.mg.asyn.HbRaNoReturn;
import com.mg.asyn.HbRaReturn;
import com.mg.asyn.JustHbRelative;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.GdtInfoModel;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.SP;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD extends MiiBaseAD{

     private Context mContext;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiADListener listener;
     private AdModel adModel;
     private ImageView adImageView;
     private WebView webView;
     private CountDownTimer timer;

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
                        adModel= (AdModel) msg.obj;
                        checkADType(adModel);
                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                    break;
                 case 300:
                     try {

                         Bitmap bitmap = (Bitmap) msg.obj;

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
                 case 500:
                     listener.onMiiNoAD(1000);
                     break;
                 case 600:
                     Init();
                     break;

             }
         }
     };

    public  MiiSplashAD(Activity activity, ViewGroup adContainer,View skipContainer ,MiiADListener adListener){

        this.mActivity = activity;

        this.mContext = activity.getApplicationContext();

        this.adContainer=adContainer;

        this.skipContainer=skipContainer;

        this.listener = adListener;


        if ( activity == null || adContainer == null || skipContainer == null){
            listener.onMiiNoAD(2000);
            return;
        }

        reqAsyncModel.context = this.mContext;
        reqAsyncModel.handler = this.mainHandler;
        reqAsyncModel.listener = this.listener;
        reqAsyncModel.pt = 2;

        if(Build.VERSION.SDK_INT >= M){
            check23AbovePermission(mActivity,mainHandler);
            return;
        }

        Init();


    }

    private void Init(){

        if (isFirstEnter(mContext)){
            new FirstEnter(reqAsyncModel).fetchMGAD();
            return;
        }
        startupAD();
    }

    private void  checkADType(final AdModel adModel){

        if (adModel.getType() == 4){//h5广告

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

                    //点击上报
                    HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, mContext);


                    listener.onMiiADClicked();
                    listener.onMiiADDismissed();

                    if (timer != null){
                        timer.cancel();
                    }

                    return true;
                }
            });

            webView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
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
                    //广告关闭回调
                    listener.onMiiADDismissed();
                    if (timer != null){
                        timer.cancel();
                    }
                }
            });

        }else {

            new ImageDownloadHelper(0).downloadShowImage(mContext,adModel.getImage(),2,mainHandler);
        }

    }

//     //"广告"提示
//     private TextView tvADCreate(){
//         TextView tv=new TextView(mActivity);
//         tv.setText("广告");
//         tv.setTextSize(10);
//         tv.setPadding(5,3,5,3);
//         tv.setBackgroundColor(Color.argb(50, 41, 36, 33));
//         tv.setGravity(Gravity.CENTER);
//         tv.setTextColor(Color.parseColor("#FFF0F5"));
//         FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//         lp.gravity=Gravity.RIGHT|Gravity.BOTTOM;
//         tv.setLayoutParams(lp);
//         return tv;
//     }

     private void showSplashAD(final Bitmap bitmap){

       try {

             FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
             adImageView = new ImageView(mActivity);
             adImageView.setLayoutParams(layoutParams);
             adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
             adContainer.addView(adImageView);

             TextView tv = tvADCreate(mActivity);
             adContainer.addView(tv);

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

                     if (bitmap != null){
                         bitmap.recycle();
                     }
                     if(timer != null){
                       timer.cancel();
                     }
                     if (mActivity!=null){
                         mActivity.finish();
                     }

                     listener.onMiiADDismissed();
                     LogUtils.i(MConstant.TAG,"调用了dismiss在skipContainer onClick中");
                 }
             });
             adImageView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     new ADClickHelper(mContext).AdClick(adModel);

                     if (timer != null){
                       timer.cancel();
                     }

                     if (bitmap != null){
                         bitmap.recycle();
                     }

                     if (mActivity!=null){
                         mActivity.finish();
                     }
                     LogUtils.i(MConstant.TAG,"调用了dismiss在adImageView onClick中");
                     listener.onMiiADClicked();
                     listener.onMiiADDismissed();
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
                    return false;
                }
            });

       }catch (Exception e){
           e.printStackTrace();
       }
     }
     private void adCountDownTimer(){

         sdk = checkSdkConfig(sdk,mContext);

         long time = sdk.getDisplayTime(2);
         LogUtils.i(MConstant.TAG,"倒计时="+time);

         timer = new CountDownTimer((time+1)*1000,1000){
             @Override
             public void onTick(long millisUntilFinished) {
                 LogUtils.i(MConstant.TAG,"倒计时="+millisUntilFinished);
                 listener.onMiiADTick((long) ((Math.floor(millisUntilFinished/1000))*1000));
             }

             @Override
             public void onFinish() {
                 if (mActivity!=null){
                     mActivity.finish();
                 }
                 LogUtils.i(MConstant.TAG,"调用了dismiss在timer中");
                 listener.onMiiADDismissed();
             }
         };
         timer.start();

     }


    private int count = 0;
    private void startupAD(){

        SourceAssignModel saModel = checkADSource(mContext);

        if (saModel == null){

            if (count < 3){
              count++;
              new FirstEnter(reqAsyncModel).fetchMGAD();
            }
            return;
        }
        count = 0;
        int type = saModel.type;
        int firstChoose = saModel.firstChoose;

        LogUtils.i(MConstant.TAG,"assign source: type="+type+" firstChoose="+firstChoose);

        if (type == 1){

            listener.onMiiNoAD(3005);
            return;
        }
        else if (type == 2){

            if (firstChoose == 1){

                new HbRaReturn(reqAsyncModel).fetchMGAD();
            }
            else {

                openGDTAD(true);

            }
        }
        else if (type ==3){

            if (firstChoose == 1){

                new HbRaNoReturn(reqAsyncModel).fetchMGAD();

            }
            else {

                openGDTAD(false);
            }
        }

    }

    private void openGDTAD(final boolean shouldReturn){

        LogUtils.i(MConstant.TAG,"load gdt...");

        new JustHbRelative(reqAsyncModel).fetchMGAD();

        String AID = "";
        String SPID = "";
        GdtInfoModel model = getGdtIds(mContext);
        try {
           AID = model.getAPPID();
           SPID = model.getSplashPosID();

        }catch (Exception e){
           e.printStackTrace();
        }

        //记录开始请求广点通时间戳
        SP.setParam(SP.CONFIG, mContext, SP.GDT_ST, System.currentTimeMillis());

        new SplashAD(mActivity, adContainer, skipContainer, AID,SPID, new SplashADListener() {
            @Override
            public void onADDismissed() {

                listener.onMiiADDismissed();

            }

            @Override
            public void onNoAD(int i) {
                //广点通请求广告失败上报
                HttpManager.reportGdtEvent(0,2,String.valueOf(i),mContext);

                if (!shouldReturn){
                    new HbRaReturn(reqAsyncModel).fetchMGAD();
                    return;
                }
                listener.onMiiNoAD(i);
            }

            @Override
            public void onADPresent() {
                //广点通请求广告成功上报
                HttpManager.reportGdtEvent(1,2,null,mContext);
                listener.onMiiADPresent();
            }

            @Override
            public void onADClicked() {
                //广点通请求广告成功上报
                HttpManager.reportGdtEvent(2,2,null,mContext);
                //广点通点击上报
                listener.onMiiADClicked();

            }

            @Override
            public void onADTick(long l) {

                listener.onMiiADTick(l);
            }
        }, 0);
    }

    @Override
    public void recycle() {

    }
}
