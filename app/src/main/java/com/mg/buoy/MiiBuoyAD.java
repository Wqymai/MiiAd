package com.mg.buoy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mg.asyn.HbReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MiiBaseAD;
import com.mg.gif.GifView;
import com.mg.interf.MiiADListener;
import com.mg.interstitial.MiiCircleTextView;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.SP;

import java.io.File;
import java.io.FileInputStream;

import static com.mg.comm.ImageDownloadHelper.md5;

/**
 * Created by wuqiyan on 17/8/7.
 */

public class MiiBuoyAD extends MiiBaseAD {

    private Context mContext;
    private Activity mActivity;
    private String mAppid;
    private String mLid;
    private AdModel adModel;
    private WebView webView;
    private MiiADListener mListener;
    private RelativeLayout relativeLayout;
    private ReqAsyncModel reqAsyncModel;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmparams;

    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    startupAD();
                    break;
                case 200:
                    try {
                        adModel= (AdModel) msg.obj;
                        if (adModel == null){
                            mListener.onMiiNoAD(3002);
                            return;
                        }
                        checkADType(adModel);
                    }
                    catch (Exception e){
                        mListener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;
                case 300:
                    try {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        if (bitmap == null){
                            mListener.onMiiNoAD(3011);
                            return;
                        }
                        showBuoyAD(bitmap);
                    }
                    catch (Exception e){
                        mListener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    mListener.onMiiNoAD(1000);
                    break;
                case 600:
                    new HbReturn(reqAsyncModel).fetchMGAD();
                    break;
            }
        }
    };

    private void showBuoyAD(Bitmap bitmap) {
      try {


        createWindowManager();

        MiiCircleTextView close = new MiiCircleTextView(mContext);
        close.setGravity(Gravity.CENTER);
        close.setText("X");
        close.setWidth(60);
        close.setHeight(60);
        close.setBackgroundColor(Color.argb(20, 41, 36, 33));
        close.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams closeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeParam.setMargins(0,0,10,0);
        close.setLayoutParams(closeParam);
        relativeLayout.addView(close);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams ivParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ivParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        ivParam.setMargins(0,50,50,0);
        if (adModel.getImage().contains("gif")){
            GifView gifView = new GifView(mContext);
            String path = mContext.getCacheDir().getPath()+ File.separator+md5(adModel.getImage());
            gifView.setGifImage(new FileInputStream(path));
            gifView.setLayoutParams(ivParam);
            relativeLayout.addView(gifView);
            gifView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else{
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(ivParam);
            iv.setImageBitmap(bitmap);
            relativeLayout.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        mWindowManager.addView(relativeLayout,wmparams);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycle();
                mListener.onMiiADDismissed();
            }
        });
      }
      catch (Exception e){

          mListener.onMiiNoAD(3009);
          e.printStackTrace();

      }

    }

    private void checkADType(AdModel model) {

        if (true){//H5广告
            try {

                openH5Ad(model);

            }catch (Exception e){

                mListener.onMiiNoAD(3010);
                e.printStackTrace();

            }
        }
        else {
            model.setImage("https://yun.tuia.cn/tuia-media/img/uu2p7758jn.gif");
            try {
                if (adModel.getImage() == null || adModel.getImage().equals("") || adModel.getImage().equals("null")){

                    if (adModel.getIcon() == null || adModel.getIcon().equals("") || adModel.getIcon().equals("null")){

                        mListener.onMiiNoAD(3011);
                    }
                    else {

                        new ImageDownloadHelper().downloadShowImage(mContext,adModel.getIcon(),mainHandler);
                    }

                }
                else {

                    new ImageDownloadHelper().downloadShowImage(mContext,adModel.getImage(),mainHandler);

                }

            }catch (Exception e){

                mListener.onMiiNoAD(3011);
                e.printStackTrace();

            }
        }
    }



    private void createWindowManager(){

      try {

        mWindowManager=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        wmparams = new WindowManager.LayoutParams();
        wmparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmparams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wmparams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            wmparams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        wmparams.format = PixelFormat.TRANSLUCENT;
        wmparams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        wmparams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
      }
      catch (Exception e){
          mListener.onMiiNoAD(3009);
          e.printStackTrace();
      }
    }

    private void openH5Ad(final AdModel admodel){

        String h5 ="<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width," +
                "initial-scale=1,maximum-scale=1,user-scalable=no'><meta " +
                "charset='utf-8'><title>Insert title here</title><style " +
                "type='text/css'>*{margin:0;padding:0}html,body{width:100%;height:100%;" +
                "background-color:#FFF;overflow:hidden}img{border:0}</style></head><body " +
                "style=\"height: 100%;width: 100%;\"><a href=\"http://www.baidu.com\"><img " +
                "src=\"http://192.168.199.192:8080/TestDemo/image/hb.png\" height=\"100%\" " +
                "width=\"100%\" /></a></body></body></html>";
        createWindowManager();


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setLayoutParams(layoutParams);


        MiiCircleTextView close = new MiiCircleTextView(mContext);
        close.setGravity(Gravity.CENTER);
        close.setText("X");
        close.setWidth(60);
        close.setHeight(60);
        close.setBackgroundColor(Color.argb(20, 41, 36, 33));
        close.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams closeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeParam.setMargins(0,0,10,0);
        close.setLayoutParams(closeParam);

        RelativeLayout.LayoutParams params_webview = new RelativeLayout.LayoutParams(dip2px(mContext,50),dip2px(mContext,50));
        webView = new WebView(mContext);
        params_webview.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_webview.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params_webview.setMargins(0,50,50,0);
        webView.setLayoutParams(params_webview);



        relativeLayout.addView(webView);
        relativeLayout.addView(close);
        mWindowManager.addView(relativeLayout,wmparams);


        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError
                    error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //广告点击回调
                mListener.onMiiADClicked();

                view.stopLoading();
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                //点击上报
                HttpManager.reportEvent(admodel, AdReport.EVENT_CLICK, mContext);

                return true;
            }
        });

        webView.loadDataWithBaseURL("", h5, "text/html", "utf-8", "");


        //展示上报
        HttpManager.reportEvent(admodel, AdReport.EVENT_SHOW, mContext);

        //广告成功展示
        mListener.onMiiADPresent();

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycle();
            }
        });

    }

    private void startupAD() {
        try {

            SourceAssignModel saModel = checkADSource(mContext,1);

            if (saModel == null) {
                new HbReturn(reqAsyncModel).fetchMGAD();
                return;
            }

            if (saModel.type == 1) {

                mListener.onMiiNoAD(3005);
                return;

            }
            new RaReturn(reqAsyncModel).fetchMGAD();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public MiiBuoyAD(Activity activity, String appid, String lid, MiiADListener listener){
        try{
            this.mActivity = activity;
            this.mContext = activity.getApplicationContext();
            this.mAppid = appid;
            this.mLid = lid;
            this.mListener = listener;
            reqAsyncModel = new ReqAsyncModel();
            reqAsyncModel.context = mContext;
            reqAsyncModel.handler = mainHandler;
            reqAsyncModel.listener = listener;
            reqAsyncModel.pt = 0;
            reqAsyncModel.appid = appid;
            reqAsyncModel.lid = lid;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                check23AbovePermission(activity, mainHandler);
                return;
            }

            new HbReturn(reqAsyncModel).fetchMGAD();

        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public void recycle() {
        mWindowManager.removeView(relativeLayout);
    }
}
