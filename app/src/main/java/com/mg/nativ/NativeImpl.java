package com.mg.nativ;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mg.comm.ADClickHelper;
import com.mg.interf.MiiNativeADDataRef;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.SP;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class NativeImpl implements MiiNativeADDataRef {


    private String imageContent;
    private int adType;
    private AdModel adModel;


    public void setAdModel(AdModel model){
        this.adModel = model;
    }

    public void setAdType(int adType){
        this.adType = adType;
    }

    public void setImgContent(String imageContent) {
        this.imageContent = imageContent;
    }

    @Override
    public String getImgContent() {
        return imageContent;
    }



    @Override
    public int getADType() {
        return adType;
    }

    @Override
    public void onClick(final Context context, final View view) {

        if (adType == 0){
            //点击调用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ADClickHelper(context).AdClick(adModel);
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
    public void setWVClick(final Context context,final WebView webView) {
        if (adType == 1){
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
                        //点击上报
                        HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, context);

                        view.stopLoading();
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }
                });
        }
    }

}
