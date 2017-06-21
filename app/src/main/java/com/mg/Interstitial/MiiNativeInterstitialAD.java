package com.mg.Interstitial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mg.asyn.FirstEnter;
import com.mg.asyn.HbRaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MiiADListener;
import com.mg.comm.MiiBaseAD;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.SP;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

/**
 * Created by wuqiyan on 17/6/19.
 */

public class MiiNativeInterstitialAD extends MiiBaseAD {

    private Activity mActivity;
    private Context mContext;
    private ViewGroup adContainer;
    private MiiADListener listener;
    private AdModel adModel;
    private WebView webView;
    private ImageView adImageView;
    boolean oren;
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
                        Bitmap bitmap= (Bitmap) msg.obj;
                        if (bitmap == null){
                            return;
                        }
                        showNativeInterstitialAD(bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case 400:
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

    public MiiNativeInterstitialAD(Activity activity, ViewGroup adContainer, MiiADListener listener){


        if (activity == null || listener==null){
            return;
        }

        this.mContext = activity.getApplicationContext();
        this.mActivity = activity;
        this.adContainer = adContainer;
        this.listener = listener;

        if (adContainer == null){
            listener.onMiiNoAD(2000);
            return;
        }

        reqAsyncModel.context = this.mContext;
        reqAsyncModel.handler = this.mainHandler;
        reqAsyncModel.listener = this.listener;
        reqAsyncModel.pt = 3;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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
    private void startupAD(){

        SourceAssignModel saModel = checkADSource(mContext);

        if (saModel == null){
            new FirstEnter(reqAsyncModel).fetchMGAD();
            return;
        }

        if (saModel.type == 1){
            LogUtils.i(MConstant.TAG," SUM = 0");
            listener.onMiiNoAD(3005);
            return;
        }
        LogUtils.i(MConstant.TAG,"SUM = 100");

        new HbRaReturn(reqAsyncModel).fetchMGAD();

    }

    private void  checkADType(AdModel adModel){
        //检查横竖屏
        checkOrientation();

        if (adModel.getType() == 4){//h5广告
            LogUtils.i(MConstant.TAG,"加载H5广告...");
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

                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    listener.onMiiADClicked();

                    return true;
                }
            });

            String html5="<!DOCTYPE html><html><head><meta name='viewport' " +
                    "content='width=device-width,initial-scale=1,maximum-scale=1," +
                    "user-scalable=no'><meta charset='utf-8'><title>Insert title " +
                    "here</title><style type='text/css'>*{margin:0;padding:0}html," +
                    "body{width:100%;height:100%;background-color:#FFF;" +
                    "overflow:hidden}img{border:0}</style></head><body style=\"height: 100%;" +
                    "width: 100%;\"><img src=\"http://192.168.199.191:8080/TestDemo/image/xfzg" +
                    ".jpg\" height=\"100%\" width=\"100%\"/></body></body></html>";
            webView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
            adContainer.addView(webView);

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

            //广告成功展示
            listener.onMiiADPresent();


            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);


        }else {

            new ImageDownloadHelper(0).downloadShowImage(mContext,adModel.getImage(),1,mainHandler);
        }

    }

    private void showNativeInterstitialAD(final Bitmap bitmap){
        try {

            LogUtils.i(MConstant.TAG,"开始展示...");
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            adImageView = new ImageView(mActivity);
            adImageView.setLayoutParams(layoutParams);
            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            adContainer.addView(adImageView);

            adImageView.setImageBitmap(bitmap);

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

            //广告成功展示
            listener.onMiiADPresent();


            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);


            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    listener.onMiiADClicked();
                    new ADClickHelper(mContext).AdClick(adModel);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //true是竖屏 false是横屏
    public boolean checkOrientation(){
        Configuration mConfiguration = mActivity.getResources().getConfiguration(); //获取设置的配置信息

        int ori = mConfiguration.orientation ; //获取屏幕方向

        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;

        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){

            mActivity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            return false;

        }
        return true;
    }

    public void recycle(){
      try {

        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        listener.onMiiADDismissed();

        if (adImageView != null){
            Drawable drawable = adImageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }

        }
        if (webView != null){
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
      }
      catch (Exception e){
          e.printStackTrace();
      }
    }

}
