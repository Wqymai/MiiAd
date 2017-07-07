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

import com.mg.d.c.a;
import com.mg.d.utils.MethodDynamicLoad;
import com.mg.d.utils.SP;
import com.mg.interf.MiiCpClickListener;
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
        return adModel.f();
    }


    @Override
    public int getType() {
        return adModel.h() == 4? 1 : 0;
    }

    @Override
    public String getName() {
        return adModel.c();
    }

    @Override
    public String getTitle() {
        return adModel.d();
    }

    @Override
    public String getDesc() {
        return adModel.e();
    }

    @Override
    public String getPage() {
        return adModel.l();
    }

    @Override
    public String getIcon() {
        return  adModel.i();
    }


    @Override
    public void setNormalClick(final Activity activity, final View view, final MiiCpClickListener cpClickListener) {

        if (adModel.h() != 4){
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

                            adModel.m(String.valueOf(event.getX()));
                            adModel.n(String.valueOf(event.getY()));
                            break;
                        case MotionEvent.ACTION_UP:

                            adModel.o(String.valueOf(event.getX()));
                            adModel.p(String.valueOf(event.getY()));
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
        MethodDynamicLoad.getInstance(context).loadReportMethod(adModel,0, context);
    }

    @Override
    public void setWVClick(final Activity activity, final WebView webView, final MiiCpClickListener cpClickListener) {
        if (adModel.h() == 4){

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
