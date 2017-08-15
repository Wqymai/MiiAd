package com.mg.nativ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.mg.comm.ADClickHelper;
import com.mg.interf.MiiCpClickListener;
import com.mg.interf.MiiCpTouchListener;
import com.mg.interf.MiiNativeADDataRef;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.SP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class NativeImpl implements MiiNativeADDataRef {


    private AdModel adModel;
    private TTFeedAd ttFeedAd;
    boolean isTt = false;

    public void setAdModel(AdModel model){
        this.adModel = model;
        if (model.isTt()){
            isTt = true;
            ttFeedAd = model.getTtFeedAd();
        }
    }

    @Override
    public String getImg() {
        return isTt ? ttFeedAd.getImageList().get(0).getImageUrl():adModel.getImage();
    }


    @Override
    public int getType() {
        return isTt ? 0:(adModel.getType() == 4? 1 : 0);
    }

    @Override
    public String getName() {
        return isTt ? ttFeedAd.getTitle() : adModel.getName();
    }

    @Override
    public String getTitle() {
        return isTt ? ttFeedAd.getTitle() : adModel.getTitle();
    }

    @Override
    public String getDesc() {
        return isTt ? ttFeedAd.getDescription() : adModel.getDesc();
    }

    @Override
    public String getPage() {
        return isTt ? "" : adModel.getPage();
    }

    @Override
    public String getIcon() {
        return  isTt ? String.valueOf(ttFeedAd.getIcon()) : adModel.getIcon();
    }

    @Override
    public String getSourceMark() {
        return isTt ? ttFeedAd.getSource() : adModel.getSourceMark();
    }




    @Override
    public void setNormalClick(final Activity activity, final ImageView view, final MiiCpClickListener cpClickListener, final MiiCpTouchListener cpTouchListener) {
      try {

        if (adModel.isTt()){//头条广告
            List<View> views = new ArrayList<>();
            views.add(view);
            ttFeedAd.registerViewForInteraction((ViewGroup) view.getParent(), views, views, new TTFeedAd.AdInteractionListener() {

                @Override
                public void onAdClicked(View view, TTFeedAd ttFeedAd) {

                }

                @Override
                public void onAdCreativeClick(View view, TTFeedAd ttFeedAd) {

                    cpClickListener.click();
                }

                @Override
                public void onAdShow(TTFeedAd ttFeedAd) {

                }
            });
            TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
                @Override
                public void onIdle() {
                }

                @Override
                public void onDownloadActive(long totalBytes, long currBytes, String fileName) {

                }

                @Override
                public void onDownloadPaused(long totalBytes, long currBytes, String fileName) {

                }

                @Override
                public void onDownloadFailed(long totalBytes, long currBytes, String fileName) {

                }

                @Override
                public void onInstalled(String fileName) {

                }

                @Override
                public void onDownloadFinished(long totalBytes, String fileName) {

                }

            };
            ttFeedAd.setDownloadListener(downloadListener); // 注册下载监听器
            return;
        }

        if (adModel.getType() != 4){
            //点击调用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  try {

                    cpClickListener.click();

                    AdModel ad= (AdModel) adModel.clone();
                    new ADClickHelper(activity.getApplicationContext()).AdClick(ad);
                  }
                  catch (Exception e){
                      e.printStackTrace();
                  }

                }
            });


            view.setOnTouchListener(new View.OnTouchListener() {
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
                    }
                    cpTouchListener.touch();
                    return false;
                }
            });
        }
      }catch (Exception e){
          e.printStackTrace();
      }

    }


    @Override
    public void onExposured(Context context) {
        if (isTt){
            return;
        }
        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, context, SP.FOT, 0);
        SP.setParam(SP.CONFIG, context, SP.FOT, show_num + 1);
        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, context);
    }

    @Override
    public void setWVClick(final Activity activity, final WebView webView, final MiiCpClickListener cpClickListener) {
       try{
            if (adModel.getType() == 4){
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
                        HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, activity.getApplicationContext());
                        cpClickListener.click();
                        return true;
                    }
                });
            }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
