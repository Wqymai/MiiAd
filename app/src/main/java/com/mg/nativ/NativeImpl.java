package com.mg.nativ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mg.comm.ADClickHelper;
import com.mg.interf.MiiCpClickListener;
import com.mg.interf.MiiNativeADDataRef;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.SP;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class NativeImpl implements MiiNativeADDataRef {


    private AdModel adModel;


    public void setAdModel(AdModel model){
        this.adModel = model;
    }

    @Override
    public String getImg() {
        return adModel.getImage();
    }


    @Override
    public int getType() {
        return adModel.getType() == 4? 1 : 0;
    }

    @Override
    public String getName() {
        return adModel.getName();
    }

    @Override
    public String getTitle() {
        return adModel.getTitle();
    }

    @Override
    public String getDesc() {
        return adModel.getDesc();
    }

    @Override
    public String getPage() {
        return adModel.getPage();
    }

    @Override
    public String getIcon() {
        return  adModel.getIcon();
    }


    @Override
    public void setNormalClick(final Activity activity, final View view, final MiiCpClickListener cpClickListener) {

        if (adModel.getType() != 4){
            //点击调用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  try {
                    AdModel ad= (AdModel) adModel.clone();
                    new ADClickHelper(activity.getApplicationContext()).AdClick(ad);
                  }
                  catch (Exception e){
                      e.printStackTrace();
                  }
                  cpClickListener.click();
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
                    return false;
                }
            });
        }

    }


    @Override
    public void onExposured(Context context) {
        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, context, SP.FOT, 0);
        SP.setParam(SP.CONFIG, context, SP.FOT, show_num + 1);

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, context);
    }

    @Override
    public void setWVClick(final Activity activity, final WebView webView, final MiiCpClickListener cpClickListener) {
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
    }

}
