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

import com.mg.c.c.a;
import com.mg.c.utils.MethodDynamicLoad;
import com.mg.c.utils.SPUtil;
import com.mg.interf.MiiCpClickListener;
import com.mg.interf.MiiCpTouchListener;
import com.mg.interf.MiiNativeADDataRef;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class b implements MiiNativeADDataRef {


    private a adModel;


    public void a(a model){
        this.adModel = model;
    }

    @Override
    public String getImg() {
        return adModel.i();
    }


    @Override
    public int getType() {
        return adModel.k() == 4? 1 : 0;
    }

    @Override
    public String getName() {
        return adModel.f();
    }

    @Override
    public String getTitle() {
        return adModel.g();
    }

    @Override
    public String getDesc() {
        return adModel.h();
    }

    @Override
    public String getPage() {
        return adModel.o();
    }

    @Override
    public String getIcon() {
        return  adModel.l();
    }
    public final String getSourceMark()
    {
        return adModel.b();
    }

    @Override
    public void setNormalClick(final Activity activity, final View view, final MiiCpClickListener cpClickListener, final MiiCpTouchListener cpTouchListener) {

        if (adModel.k() != 4){
            //点击调用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  try {
                    a ad= (a) adModel.clone();
                    //点击操作
                    MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadAdClickMethod(activity.getApplicationContext(),ad);

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

                            adModel.o(String.valueOf(event.getX()));
                            adModel.p(String.valueOf(event.getY()));
                            break;
                        case MotionEvent.ACTION_UP:

                            adModel.q(String.valueOf(event.getX()));
                            adModel.r(String.valueOf(event.getY()));
                            break;
                    }
                    cpTouchListener.touch();
                    return false;
                }
            });
        }

    }


    @Override
    public void onExposured(Context context) {
        //记录展示次数
        int show_num = (int) SPUtil.getParam(SPUtil.CONFIG, context, SPUtil.FOT, 0);
        SPUtil.setParam(SPUtil.CONFIG, context, SPUtil.FOT, show_num + 1);

        //展示上报
        MethodDynamicLoad.getInstance(context).loadReportMethod(adModel,0, context);
    }

    @Override
    public void setWVClick(final Activity activity, final WebView webView, final MiiCpClickListener cpClickListener) {
        if (adModel.k() == 4){

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
                        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadReportMethod(adModel, 1, activity.getApplicationContext());
                        cpClickListener.click();
                        return true;
                    }
                });
        }
    }



}
