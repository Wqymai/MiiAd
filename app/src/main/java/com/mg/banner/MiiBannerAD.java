package com.mg.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
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
import android.widget.TextView;

import com.mg.asyn.HbReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;


/**
 * Created by wuqiyan on 17/6/15.
 */

public class MiiBannerAD extends MiiBaseAD {

    private Context mContext;
    private Activity mActivity;
    private ViewGroup adContainer;
    private MiiADListener listener;
    private AdModel adModel;
    private WebView webView;
    private ImageView adImageView;
    private ReqAsyncModel reqAsyncModel = new ReqAsyncModel();
    private boolean isJoinImg = false;
    private TextView name_tv;
    private TextView desc_tv;
    private TextView adTxt_tv;



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
                        showBannerAD(bitmap);
                    }
                    catch (Exception e){
                        listener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    listener.onMiiNoAD(1000);
                    break;

                case 700:
                    listener.onMiiNoAD(3011);
                    break;
            }

        }
    };


    public MiiBannerAD(Activity activity, ViewGroup adContainer,String appid,String lid, MiiADListener listener){

      try{
        this.mContext = activity.getApplicationContext();
        this.mActivity = activity;
        this.adContainer = adContainer;
        this.listener = listener;

        if (activity == null || adContainer == null){
            listener.onMiiNoAD(2000);
            return;
        }
        reqAsyncModel.context = this.mContext;
        reqAsyncModel.handler = this.mainHandler;
        reqAsyncModel.listener = this.listener;
        reqAsyncModel.pt = 1;
        reqAsyncModel.appid = appid;
        reqAsyncModel.lid = lid;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            check23AbovePermission(mActivity,mainHandler);
            return;
        }


      }catch (Exception e){

          listener.onMiiNoAD(2001);
          e.printStackTrace();

      }

    }

    public void loadBannerAD(){
        new HbReturn(reqAsyncModel).fetchMGAD();
    }

    private void startupAD(){

      try {


          SourceAssignModel saModel = checkADSource(mContext,1);

          if (saModel == null) {
              new HbReturn(reqAsyncModel).fetchMGAD();
              return;
          }

          if (saModel.type == 1) {

              listener.onMiiNoAD(3005);
              return;

          }
          new RaReturn(reqAsyncModel).fetchMGAD();

      }catch (Exception e){

          listener.onMiiNoAD(3012);
          e.printStackTrace();

      }

    }


    private void  checkADType(final AdModel adModel){

        if (adModel.getType() == 4){//h5广告

          try {
              if (webView == null){
                webView = new WebView(mActivity);
                adContainer.addView(webView);
              }

              int imgH = adModel.getImgh();
              if (imgH == 0){
                imgH = (int) (CommonUtils.getScreenH(mContext) * 0.1);
              }
              FrameLayout.LayoutParams params_webview = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, imgH);

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
                      listener.onMiiADClicked();

                      view.stopLoading();
                      view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                      //点击上报
                      HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, mContext);

                      return true;
                  }
              });

              webView.loadDataWithBaseURL("", adModel.getPage(), "text/html", "utf-8", "");


              //展示上报
              HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

              //广告成功展示
              listener.onMiiADPresent();

              //记录展示次数
              int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
              SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

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



    private void showBannerAD(final Bitmap bitmap){
        try {
            if (adModel == null){
                listener.onMiiNoAD(3009);
                return;
            }
            if (isJoinImg){
                adContainer.setBackgroundColor(Color.parseColor("#d1dbee"));
                if (adImageView == null){
                    adImageView = new ImageView(mActivity);
                    adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    adContainer.addView(adImageView);
                }
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dip2px(mContext,60),dip2px(mContext,60));
                adImageView.setLayoutParams(layoutParams);
                //name
                if (name_tv == null){
                    name_tv = new TextView(mActivity);
                    FrameLayout.LayoutParams nameTvLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    nameTvLayoutParams.setMargins(dip2px(mContext,70),dip2px(mContext,5),0,0);
                    name_tv.setLayoutParams(nameTvLayoutParams);
                    adContainer.addView(name_tv);
                }
                name_tv.setText(adModel.getName());
                //desc
                if (desc_tv == null){
                    desc_tv = new TextView(mActivity);
                    FrameLayout.LayoutParams descTvLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    descTvLayoutParams.setMargins(dip2px(mContext,70),dip2px(mContext,25),0,0);
                    desc_tv.setLayoutParams(descTvLayoutParams);
                    adContainer.addView(desc_tv);
                }
                desc_tv.setText(adModel.getDesc());
            }
            else {
                if (name_tv != null){
                    name_tv.setText(null);
                }
                name_tv = null;
                if (desc_tv != null){
                    desc_tv.setText(null);
                }
                desc_tv = null;
                if (adImageView == null){
                    adImageView = new ImageView(mActivity);
                    adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    adContainer.addView(adImageView);
                }
                int screenW = CommonUtils.getScreenW(mContext);
                double value = div(bitmap.getWidth(),bitmap.getHeight(),1);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenW, (int)(screenW/value));
                adImageView.setLayoutParams(layoutParams);
            }

            if(adTxt_tv == null){
               adTxt_tv = tvADCreate(mActivity);
               adContainer.addView(adTxt_tv);
            }
            if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
                adTxt_tv.setText(adModel.getSourceMark()+"|广告");
            }

            adImageView.setImageBitmap(bitmap);

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

            //广告成功展示
            listener.onMiiADPresent();

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);


            if (isJoinImg){
                adContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            listener.onMiiADClicked();

                            AdModel ad= (AdModel) adModel.clone();
                            new ADClickHelper(mContext).AdClick(ad);

                        }catch (Exception e){

                            e.printStackTrace();

                        }

                    }
                });
                adContainer.setOnTouchListener(new View.OnTouchListener() {
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

                return;
            }


            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  try {

                    listener.onMiiADClicked();

                    AdModel ad= (AdModel) adModel.clone();
                    new ADClickHelper(mContext).AdClick(ad);

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


    //关闭banner广告时调用
    @Override
    public void recycle() {
        try {

            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            if (adImageView != null){
                Drawable drawable = adImageView.getDrawable();
                if (drawable != null && drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                adImageView = null;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                webView = null;
            }

            listener.onMiiADDismissed();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
