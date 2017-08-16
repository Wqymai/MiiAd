package com.mg.headup;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.asyn.HbReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.interstitial.MiiCircleTextView;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by wuqiyan on 17/8/8.
 */

public class MiiHeadupAD extends MiiBaseAD {



    private AdModel adModel;
    private MiiADListener cListener;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmparams;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private MiiCircleTextView close;
    private TextView adTxt_tv;
    private boolean isTop;
    private boolean isCloseWin= false;
    private WebView webView;



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
                            cListener.onMiiNoAD(3002);
                            return;
                        }
                        checkADType();
                    }
                    catch (Exception e){
                        cListener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;
                case 300:
                    try {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        if (bitmap == null){
                            cListener.onMiiNoAD(3011);
                            return;
                        }
                        showNotifyAD(bitmap);
                    }
                    catch (Exception e){
                        cListener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    cListener.onMiiNoAD(1000);
                    break;
                case 700:
                    cListener.onMiiNoAD(3011);
                    break;
            }
        }
    };





    public MiiHeadupAD(Activity activity, boolean isTop, String appid, String lid, MiiADListener adListener){
        try{
            this.isTop = isTop;
            this.cListener = adListener;
            super.plistener = adListener;
            super.activity = activity;
            super.context = activity.getApplicationContext();
            super.reqAsyncModel = new ReqAsyncModel();

            reqAsyncModel.context = super.context;
            reqAsyncModel.handler = mainHandler;
            reqAsyncModel.listener = adListener;
            reqAsyncModel.pt = 0;
            reqAsyncModel.appid = appid;
            reqAsyncModel.lid = lid;

            if (Build.VERSION.SDK_INT >= M) {
                check23AbovePermission(activity, mainHandler);
                return;
            }

        }catch (Exception e){
            adListener.onMiiNoAD(2001);
            e.printStackTrace();
        }
    }

    public   void loadHeadupAD(){
        new HbReturn(reqAsyncModel).fetchMGAD();
    }

    private void createWindowManager(int w,int h){

        try {

            if (mWindowManager == null) {
                mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                wmparams = new WindowManager.LayoutParams();
                wmparams.height = h;
                wmparams.width = w;
                wmparams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
                wmparams.format = PixelFormat.TRANSLUCENT;
                wmparams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

                if (isTop){
                    wmparams.gravity = Gravity.TOP;
                    wmparams.y = getStatusHeight();
                }
                else {
                    wmparams.gravity = Gravity.BOTTOM;
                }

            }
        }
        catch (Exception e){
            cListener.onMiiNoAD(3009);
            e.printStackTrace();
        }
    }

    private int getStatusHeight(){

        int statusBarHeight = 60;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

    private RelativeLayout buildWrapRelativeLayout(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(layoutParams);
        return layout;
    }
    private TextView buildCloseTv(){
        MiiCircleTextView close = new MiiCircleTextView(context);
        close.setGravity(Gravity.CENTER);
        close.setText("X");
        close.setBackgroundColor(Color.argb(40, 41, 36, 33));
        close.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams closeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeParam.setMargins(0,0,10,0);
        close.setLayoutParams(closeParam);
        return close;
    }

    private void showNotifyAD(Bitmap bitmap){
      try {

            double H_W_P = div(bitmap.getWidth(),bitmap.getHeight(),1);
            int h = (int)(CommonUtils.getScreenW(context)/H_W_P);
            int w = CommonUtils.getScreenW(context);

            createWindowManager(w,h);

            if (relativeLayout == null){
                relativeLayout = buildWrapRelativeLayout();
            }

            mWindowManager.addView(relativeLayout,wmparams);



            if (imageView == null){
              imageView = new ImageView(context);
              relativeLayout.addView(imageView);
            }


            RelativeLayout.LayoutParams ivParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(ivParam);
            imageView.setImageBitmap(bitmap);


            if (close == null){
                close = (MiiCircleTextView) buildCloseTv();
                relativeLayout.addView(close);
            }

            if (adTxt_tv == null){
                adTxt_tv = new TextView(context);
                adTxt_tv.setText("广告");
                adTxt_tv.setTextSize(10);
                adTxt_tv.setPadding(5,3,5,3);
                adTxt_tv.setBackgroundColor(Color.argb(30, 41, 36, 33));
                adTxt_tv.setGravity(Gravity.CENTER);
                adTxt_tv.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams tvParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                tvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adTxt_tv.setLayoutParams(tvParams);
                relativeLayout.addView(adTxt_tv);
            }

            if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
                adTxt_tv.setText(adModel.getSourceMark()+"|广告");
            }


            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, context);

            //广告成功展示
            cListener.onMiiADPresent();

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, context, SP.FOT, 0);
            SP.setParam(SP.CONFIG, context, SP.FOT, show_num + 1);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        cListener.onMiiADClicked();

                        AdModel ad= (AdModel) adModel.clone();
                        new ADClickHelper(context).AdClick(ad);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {
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
                    cListener.onMiiADTouched();
                    return false;
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   recycle();
                   cListener.onMiiADDismissed();
                }
            });
      }
      catch (Exception e){

          cListener.onMiiNoAD(3009);
          e.printStackTrace();

      }

    }

    private void showH5Ad(){
        try {



            int w = CommonUtils.getScreenW(context);
            int imgH = adModel.getImgh();
            if (imgH == 0){
                imgH = (int) (CommonUtils.getScreenH(context) * 0.1);
            }

            createWindowManager(w,imgH);

            if (relativeLayout == null){
                relativeLayout = buildWrapRelativeLayout();
            }

            mWindowManager.addView(relativeLayout,wmparams);


            if (webView == null){
                webView = new WebView(context);
                relativeLayout.addView(webView);
            }


            RelativeLayout.LayoutParams params_webview = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            webView.setLayoutParams(params_webview);
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
                    cListener.onMiiADClicked();

                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    //点击上报
                    HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, context);

                    return true;
                }
            });

            webView.loadDataWithBaseURL("", adModel.getPage(), "text/html", "utf-8", "");


            if (close == null){
                close = (MiiCircleTextView) buildCloseTv();
                relativeLayout.addView(close);
            }

            if (adTxt_tv == null){
                adTxt_tv = new TextView(context);
                adTxt_tv.setText("广告");
                adTxt_tv.setTextSize(10);
                adTxt_tv.setPadding(5,3,5,3);
                adTxt_tv.setBackgroundColor(Color.argb(10, 41, 36, 33));
                adTxt_tv.setGravity(Gravity.CENTER);
                adTxt_tv.setTextColor(Color.WHITE);
                RelativeLayout.LayoutParams tvParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                tvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adTxt_tv.setLayoutParams(tvParams);
                relativeLayout.addView(adTxt_tv);
            }

            if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
                adTxt_tv.setText(adModel.getSourceMark()+"|广告");
            }


            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, context);

            //广告成功展示
            cListener.onMiiADPresent();

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, context, SP.FOT, 0);
            SP.setParam(SP.CONFIG, context, SP.FOT, show_num + 1);


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycle();
                    cListener.onMiiADDismissed();
                }
            });

        }catch (Exception e){

            cListener.onMiiNoAD(3010);
            e.printStackTrace();

        }
    }

    private void checkADType() {

        recycle();
        isCloseWin = false;

        if (adModel.getType() == 4){

//            String h5="<!DOCTYPE html><html><head><meta name='viewport' " +
//                    "content='width=device-width,initial-scale=1,maximum-scale=1," +
//                    "user-scalable=no'><meta charset='utf-8'><title>Insert title " +
//                    "here</title><style type='text/css'>*{margin:0;padding:0}html," +
//                    "body{width:100%;height:100%;background-color:#FFF;" +
//                    "overflow:hidden}img{border:0}a:link{font-size:12px;color:#000;" +
//                    "text-decoration:none}a:visited{font-size:12px;color:#000;" +
//                    "text-decoration:none}a:hover{font-size:12px;color:#999;" +
//                    "text-decoration:underline}*{-webkit-tap-highlight-color:rgba(0,0,0,0)" +
//                    "}</style></head><body style=\"height: 100%;width: 100%;" +
//                    "\"><a href=\"http://www.baidu.com\" onclick=\"\"><img " +
//                    "src=\"http://192.168.199.192:8080/TestDemo/image/banner.jpg\" " +
//                    "height=\"100%\" width=\"100%\" /></a></body></body></html>";
//            adModel.setPage(h5);
            showH5Ad();
        }
        else {

            try {
                if (adModel.getImage() == null || adModel.getImage().equals("") || adModel.getImage().equals("null")){

                    if (adModel.getIcon() == null || adModel.getIcon().equals("") || adModel.getIcon().equals("null")){

                        cListener.onMiiNoAD(3011);
                    }
                    else {

                        new ImageDownloadHelper().downloadShowImage(context,adModel.getIcon(),mainHandler);
                    }

                }
                else {

                    new ImageDownloadHelper().downloadShowImage(context,adModel.getImage(),mainHandler);

                }

            }catch (Exception e){

                cListener.onMiiNoAD(3011);
                e.printStackTrace();

            }
        }

    }


    @Override
    public void recycle() {

      try {
           if (!isCloseWin) {
               if (mWindowManager != null) {
                   if (relativeLayout != null) {
                       mWindowManager.removeViewImmediate(relativeLayout);
                   }
               }
               isCloseWin = true;
           }
      }
      catch (Exception e){
          e.printStackTrace();
      }

    }
}
