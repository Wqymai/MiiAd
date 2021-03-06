//package com.mg.interstitial;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//
//import com.mg.asyn.HbReturn;
//import com.mg.asyn.RaReturn;
//import com.mg.asyn.ReqAsyncModel;
//import com.mg.comm.ADClickHelper;
//import com.mg.comm.ImageDownloadHelper;
//import com.mg.comm.MConstant;
//import com.mg.comm.MiiBaseAD;
//import com.mg.interf.MiiADListener;
//import com.mg.others.manager.HttpManager;
//import com.mg.others.model.AdModel;
//import com.mg.others.model.AdReport;
//import com.mg.others.utils.CommonUtils;
//import com.mg.others.utils.LogUtils;
//import com.mg.others.utils.SP;
//
///**
// * Created by wuqiyan on 17/6/19.
// * 自定义的插屏
// */
//
//public class MiiAutoInterstitialAD extends MiiBaseAD {
//
//    private Activity mActivity;
//    private Context mContext;
//    private ViewGroup adContainer;
//    private MiiADListener listener;
//    private AdModel adModel;
//    private WebView webView;
//    private ImageView adImageView;
//
//    private ReqAsyncModel reqAsyncModel = new ReqAsyncModel();
//
//    Handler mainHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 100:
//                    startupAD();
//                    break;
//                case 200:
//                    try {
//                        adModel = (AdModel) msg.obj;
//                        if (adModel == null){
//                            listener.onMiiNoAD(3002);
//                            return;
//                        }
//                        checkADType(adModel);
//                    }
//                    catch (Exception e){
//
//                        listener.onMiiNoAD(3002);
//                        e.printStackTrace();
//
//                    }
//                    break;
//                case 300:
//                    try {
//                        Bitmap bitmap= (Bitmap) msg.obj;
//                        if (bitmap == null){
//                            listener.onMiiNoAD(3011);
//                            return;
//                        }
//                        showAutoInterstitialAD(bitmap);
//                    }
//                    catch (Exception e){
//
//                        listener.onMiiNoAD(3011);
//                        e.printStackTrace();
//
//                    }
//                    break;
////                case 400:
////                    break;
//                case 500:
//                    listener.onMiiNoAD(1000);
//                    break;
//                case 600:
//                    new HbReturn(reqAsyncModel).fetchMGAD();
//                    break;
//                case 700:
//                    listener.onMiiNoAD(3011);
//                    break;
//            }
//
//        }
//    };
//
//    public MiiAutoInterstitialAD(Activity activity, ViewGroup adContainer,String appid,String lid, MiiADListener listener){
//
//      try {
//
//
//          this.mContext = activity.getApplicationContext();
//          this.mActivity = activity;
//          this.adContainer = adContainer;
//          this.listener = listener;
//
//
//
//          if (adContainer == null) {
//              listener.onMiiNoAD(2000);
//              return;
//          }
//
//          reqAsyncModel.context = this.mContext;
//          reqAsyncModel.handler = this.mainHandler;
//          reqAsyncModel.listener = this.listener;
//          reqAsyncModel.pt = 3;
//          reqAsyncModel.appid = appid;
//          reqAsyncModel.lid = lid;
//
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//              check23AbovePermission(mActivity, mainHandler);
//              return;
//          }
//          new HbReturn(reqAsyncModel).fetchMGAD();
//      }
//      catch (Exception e){
//
//          listener.onMiiNoAD(2001);
//          e.printStackTrace();
//
//      }
//    }
//
//    private void startupAD(){
//
//      try {
//
//
//        SourceAssignModel saModel = checkADSource(mContext,3);
//
//        if (saModel == null){
//            new HbReturn(reqAsyncModel).fetchMGAD();
//            return;
//        }
//
//        if (saModel.type == 1){
//
//            listener.onMiiNoAD(3005);
//            return;
//        }
//
//        new RaReturn(reqAsyncModel).fetchMGAD();
//      }
//      catch (Exception e){
//
//          listener.onMiiNoAD(3012);
//          e.printStackTrace();
//
//      }
//    }
//
//    private void  checkADType(final AdModel adModel){
//        //检查横竖屏
//        checkOrientation(mActivity);
//
//        if (adModel.getType() == 4){//h5广告
//
//            try {
//                webView = new WebView(mActivity);
//                FrameLayout.LayoutParams params_webview = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//                webView.setLayoutParams(params_webview);
//                WebSettings settings = webView.getSettings();
//                settings.setDefaultTextEncodingName("utf-8") ;
//                settings.setJavaScriptEnabled(true);
//                settings.setDomStorageEnabled(true);
//                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                webView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                        handler.proceed();
//                    }
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        view.stopLoading();
//                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//
//                        //点击上报
//                        HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, mContext);
//
//                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                        listener.onMiiADClicked();
//
//                        return true;
//                    }
//                });
//
//                webView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
//                adContainer.addView(webView);
//
//                //展示上报
//                HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);
//
//                //广告成功展示
//                listener.onMiiADPresent();
//
//
//                //记录展示次数
//                int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
//                SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);
//
//            }
//            catch (Exception e){
//
//                listener.onMiiNoAD(3010);
//                e.printStackTrace();
//            }
//        }else {
//
//           try {
//
//             if (adModel.getImage() == null || adModel.getImage().equals("")){
//
//                 listener.onMiiNoAD(3011);
//
//             }
//             else {
//
//                new ImageDownloadHelper(0).downloadShowImage(mContext,adModel.getImage(),1,mainHandler);
//
//             }
//           }
//           catch (Exception e){
//
//               listener.onMiiNoAD(3011);
//               e.printStackTrace();
//
//           }
//        }
//
//    }
//
//    private void showAutoInterstitialAD(final Bitmap bitmap){
//
//        try {
//
//            int screenW = CommonUtils.getScreenW(mContext);
//            double value = div(bitmap.getWidth(),bitmap.getHeight(),1);
//
//
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenW, (int)(screenW/value));
//            adImageView = new ImageView(mActivity);
//            adImageView.setLayoutParams(layoutParams);
//            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            adContainer.addView(adImageView);
//
////            TextView tv = tvADCreate(mActivity);
////            adContainer.addView(tv);
//
//
//
//
//            adImageView.setImageBitmap(bitmap);
//
//            //展示上报
//            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);
//
//            //广告成功展示
//            listener.onMiiADPresent();
//
//
//            //记录展示次数
//            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
//            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);
//
//
//            adImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                    AdModel ad = (AdModel) adModel.clone();
//                    new ADClickHelper(mContext).AdClick(ad);
//                    listener.onMiiADClicked();
//                }
//            });
//            adImageView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            adModel.setDownx(String.valueOf(event.getX()));
//                            adModel.setDowny(String.valueOf(event.getY()));
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            adModel.setUpx(String.valueOf(event.getX()));
//                            adModel.setUpy(String.valueOf(event.getY()));
//                            break;
//                        default:
//                            break;
//                    }
//                    listener.onMiiADTouched();
//                    return false;
//                }
//            });
//        }catch (Exception e){
//
//            listener.onMiiNoAD(3009);
//            e.printStackTrace();
//
//        }
//    }
//
//
//    @Override
//    public void recycle(){
//        try {
//            LogUtils.i(MConstant.TAG,"调用了自由插屏的recycle()");
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//
//            listener.onMiiADDismissed();
//
//            if (adImageView != null){
//                Drawable drawable = adImageView.getDrawable();
//                if (drawable != null && drawable instanceof BitmapDrawable) {
//                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    if (bitmap != null && !bitmap.isRecycled()) {
//                        bitmap.recycle();
//                    }
//                }
//
//            }
//            if ( webView!= null){
//                ViewParent parent = webView.getParent();
//                if (parent != null) {
//                    ((ViewGroup) parent).removeView(webView);
//                }
//                webView.stopLoading();
//                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//                webView.getSettings().setJavaScriptEnabled(false);
//                webView.clearHistory();
//                webView.clearView();
//                webView.removeAllViews();
//                try {
//                    webView.destroy();
//                } catch (Throwable ex) {
//
//                }
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
