package com.mg.others.layer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.others.manager.ApkDownloadManager;
import com.mg.others.manager.HttpManager;
import com.mg.others.message.Handler;
import com.mg.others.message.ISender;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdPercentage;
import com.mg.others.model.AdReport;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;
import com.mg.others.utils.imager.ImageLoader;
import com.mg.others.view.FadeInImageView;


public class AdLayer extends AbsLayer implements Handler {

    private ISender mSender = ISender.Factory.newMainThreadSender(this);
    private FadeInImageView mImageView;
    private TextView mTextView;
    private TextView mTextView_GUANGGAO;
    private TextView mTextView_Jump;
    private WebView mWebView;
    private RelativeLayout mRelativeLayout;
    private static final String TEXT_JUMP1 = "MDZCRjNDNEY2QTEyMDQxOEFBQjlENDZDN0EyQUVBOUU=";//"|　跳过";
    private static final String TEXT_JUMP2 = "RDVEQjgyMkRCNkQ0ODAwNzhFQjZEOEFGNzIzRkZBMjU=";//"跳过";
    private static final String TEXT_AD = "OTc3MEIwNzFFQThCOTFFODEwRTVERjUzNjM5RjgyMUU=";//"广告";
    private static final String BG_COLOR="#5a000000";
    private static final String TEXT_CODE="utf-8";
    private static final String BANNER_TOP="0";
    private static final String BANNER_BOTTOM="1";
    private long mShowTime = 0;
    private AdModel adModel;
    private int displayTime;
    private boolean isHtml5Ad=false;
    private int ADTYPE=0;
    private  int toggleSence=0;
    private double B_PERCENT=6.5;
    private double I_PERCENT=1.2;
    private double W_PERCENT=0.9;
    private double H_PERCENT=0.85;
    private int    HTML5_SOURCE=4;


    @Override
    public String getLayerName() {
        return AdLayer.class.getSimpleName();
    }

    @Override
    public WindowManager.LayoutParams createLayoutParams(int adtype, String bp, int adSource) {
        LogUtils.i(MConstant.TAG,"type="+adtype+" bp="+bp+" source="+adSource);
        ADTYPE=adtype;
        WindowManager.LayoutParams params = super.createLayoutParams(adtype,bp,adSource);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.format = PixelFormat.TRANSLUCENT;

        ShowController controller=ShowController.getInstance();
        controller.setAdType(adtype);
        controller.showSelfAd(bp,adSource,this,params);

//        int screenH = CommonUtils.getScreenH(this);
//        int screenW = CommonUtils.getScreenW(this);

//        if (adtype == AdPercentage.SPLASH) {
//            if (screenH > screenW){
//                params.height = WindowManager.LayoutParams.MATCH_PARENT;
//                params.width= WindowManager.LayoutParams.MATCH_PARENT;
//                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//            }
//            else {
//                params.height =(int)(screenH * H_PERCENT);
//                params.width=screenH;
//                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//            }
//
//        }
//        if (adtype == AdPercentage.BANNER) {
//            if (bp.equals(BANNER_TOP)){
//                params.gravity = Gravity.TOP;
//            }
//            if (bp.equals(BANNER_BOTTOM)){
//                params.gravity = Gravity.BOTTOM;
//            }
//            if (adSource == HTML5_SOURCE){
//                isHtml5Ad = true;
//                if (screenH > screenW){
//                    params.height=(int) (screenH/B_PERCENT);
//                    params.width= WindowManager.LayoutParams.MATCH_PARENT;
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                }
//                else {
//                    params.width = screenH;
//                    params.height = (int) (screenW/B_PERCENT);
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                }
//            }
//            else {
//                if (screenH > screenW){
//                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                }
//                else {
//                    params.height = (int) (screenW/B_PERCENT);
//                    params.width = screenH;
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                }
//            }
//
//        }
//        if (adtype== AdPercentage.INTERSTITIAL){
//            params.gravity = Gravity.CENTER;
//            if (adSource == HTML5_SOURCE){
//                isHtml5Ad = true;
//                if (screenH > screenW){
//                    params.height = (int) ((screenW * W_PERCENT)/I_PERCENT);
//                    params.width = (int) (screenW * W_PERCENT);
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                }
//                else {
//                    params.width = screenH;
//                    params.height = (int) (screenH/I_PERCENT);
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                }
//            }
//            else {
//                if (screenH > screenW){
//                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    params.width = (int) (screenW * W_PERCENT);
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                }
//                else {
//                    params.width = screenH;
//                    params.height = (int) (screenH * H_PERCENT);
//                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                }
//            }
//
//        }
       params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        if (adtype == AdPercentage.BANNER){
           params.x = 0;
           params.y = -50;
        }
        params.x = 0;
        params.y = 0;
        return params;
    }


    @Override
    public void onCreate(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                adModel = (AdModel) bundle.get(MConstant.key.ADS_DATA);
                if(adModel.getType()==4){
                    isHtml5Ad=true;
                }
                displayTime = adModel.getDisplayTime() * 1000;
                toggleSence=adModel.getToggleSence();
                initView();
            }
        }
    }

    private void initView() {
//        LogUtils.i(MConstant.TAG,"ish5="+isHtml5Ad);
        RelativeLayout.LayoutParams params_parent = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout = new RelativeLayout(this);
        if (adModel != null && adModel.getPt()==AdPercentage.BANNER) {
            mRelativeLayout.setPadding(0,50,0,0);
        }
        mRelativeLayout.setLayoutParams(params_parent);
        mRelativeLayout.setBackgroundColor(Color.TRANSPARENT);

//        FrameLayout frameLayout=new FrameLayout(this);
//        FrameLayout.LayoutParams frameParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        frameLayout.setId(MConstant.id.frame);
//        frameLayout.setLayoutParams(frameParams);
//        mRelativeLayout.addView(frameLayout);
//
//        BannerView bv=new BannerView(this, ADSize.BANNER,MConstant.APPID,MConstant.BannerPosID);

        if (!isHtml5Ad) {
            mImageView = new FadeInImageView(this);
            RelativeLayout.LayoutParams params_wrap = new RelativeLayout.LayoutParams(FrameLayout
                    .LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params_wrap.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params_wrap.addRule(RelativeLayout.CENTER_IN_PARENT);
            mImageView.setLayoutParams(params_wrap);
            mImageView.setOnClickListener(this);
            mImageView.setId(MConstant.id.adcontent);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mRelativeLayout.addView(mImageView);
        }
        else{
            mWebView = new WebView(this);
            RelativeLayout.LayoutParams params_webview = new RelativeLayout.LayoutParams(FrameLayout
                    .LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            mWebView.setLayoutParams(params_webview);
            WebSettings settings = mWebView.getSettings();
            settings.setDefaultTextEncodingName(TEXT_CODE) ;
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError
                        error) {
                    handler.proceed();
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    finish();
                    return true;
                }
            });
//            mWebView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_UP:
//                           MLogManager.i("TAG","ACTION_UP");
//                            finish();
//                            break;
//                    }
//                    return false;
//                }
//            });
            mWebView.setId(MConstant.id.adwebview);
            mWebView.setVisibility(View.INVISIBLE);
            mRelativeLayout.addView(mWebView);
        }
        mTextView_GUANGGAO=new TextView(this);
        RelativeLayout.LayoutParams params_GG = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (ADTYPE==AdPercentage.BANNER||ADTYPE==AdPercentage.INTERSTITIAL){
            params_GG.setMargins(0,10,0,0);
        }
        else {
            params_GG.setMargins(0,50,0,0);
        }
        params_GG.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mTextView_GUANGGAO.setLayoutParams(params_GG);
        mTextView_GUANGGAO.setTextColor(Color.WHITE);
        mTextView_GUANGGAO.setBackgroundColor(Color.parseColor(BG_COLOR));
        mTextView_GUANGGAO.setGravity(Gravity.CENTER);
        int margin02 = dip2px(3);
        mTextView_GUANGGAO.setPadding(margin02, margin02, margin02, margin02);
        mTextView_GUANGGAO.setTextSize(dip2px(5));
        mTextView_GUANGGAO.setText(MiiLocalStrEncrypt.deCodeStringToString(TEXT_AD, "fire"));
        mRelativeLayout.addView(mTextView_GUANGGAO);
        mTextView_GUANGGAO.setVisibility(View.INVISIBLE);
        mTextView = new TextView(this);
        RelativeLayout.LayoutParams params_text = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (ADTYPE==AdPercentage.BANNER||ADTYPE==AdPercentage.INTERSTITIAL){
            params_text.setMargins(0,10,0,0);
        }
        else {
            params_text.setMargins(0,50,0,0);
        }
        if (adModel.isHasJumpButton()){
            mTextView_Jump=new TextView(this);
            RelativeLayout.LayoutParams params_jump = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (ADTYPE==AdPercentage.BANNER||ADTYPE==AdPercentage.INTERSTITIAL){
                params_jump.setMargins(0,10,0,0);
            }

            else {
                params_jump.setMargins(0,50,0,0);
            }
            params_jump.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mTextView_Jump.setLayoutParams(params_jump);
            mTextView_Jump.setBackgroundColor(Color.parseColor(BG_COLOR));
            mTextView_Jump.setTextColor(Color.WHITE);
            mTextView_Jump.setGravity(Gravity.CENTER);
            int margin03 = dip2px(3);
            mTextView_Jump.setPadding(margin03, margin03, margin03, margin03);
            mTextView_Jump.setTextSize(dip2px(5));
            if (ADTYPE==AdPercentage.BANNER){
                mTextView_Jump.setText(MiiLocalStrEncrypt.deCodeStringToString(TEXT_JUMP2,
                        "fire"));

            }
            else {
                mTextView_Jump.setText(MiiLocalStrEncrypt.deCodeStringToString(TEXT_JUMP1,
                        "fire"));
            }

            mTextView_Jump.setOnClickListener(this);
            mTextView_Jump.setId(MConstant.id.tvjump);
            mTextView_Jump.setVisibility(View.INVISIBLE);
            mRelativeLayout.addView(mTextView_Jump);
            params_text.addRule(RelativeLayout.LEFT_OF,MConstant.id.tvjump);
        }
        else {
            params_text.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        mTextView.setLayoutParams(params_text);
        mTextView.setBackgroundColor(Color.parseColor(BG_COLOR));
        mTextView.setTextColor(Color.WHITE);
        mTextView.setGravity(Gravity.CENTER);
        int margin = dip2px(3);
        mTextView.setPadding(margin, margin, margin, margin);
        mTextView.setTextSize(dip2px(5));
        mRelativeLayout.addView(mTextView);
        mTextView.setVisibility(View.INVISIBLE);
        mTextView.setOnClickListener(this);
        mTextView.setId(MConstant.id.clsoebutton);
        setContentView(mRelativeLayout);
        if (adModel!=null&&toggleSence==0&&adModel.getPt()!=AdPercentage.SPLASH){
            LogUtils.i(MConstant.TAG,"delaytime="+adModel.getDelayTime());
            mSender.sendEmptyMessageDelayed(MConstant.what.delay_show, adModel.getDelayTime() * 1000);
        }
        else {
            if (isHtml5Ad){
                mSender.sendMessage(mSender.obtainMessage(MConstant.what.h5_splash_show,null));
            }
            else {
                ImageLoader.getInstance().loadImageviewBitmap(adModel.getImage(), mImageView, true, mSender);
            }
        }
        }

    @Override
    public void onResume(Intent intent) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }
    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case MConstant.what.time_change:
                int pastTime = (int) ((System.currentTimeMillis()) - mShowTime);
                String temp = ((displayTime - pastTime) / 1000 + 1) + "s";
                mTextView.setText(temp);
                if (pastTime <= displayTime) {
                    mSender.sendEmptyMessageDelayed(MConstant.what.time_change, 1000);
                } else {
                    finish();
                }
                break;

            case MConstant.what.image_loaded:
                Bitmap bitmap = (Bitmap) msg.obj;
                int maxHeight = CommonUtils.getScreenH(this);
                int maxWidth = CommonUtils.getScreenW(this);
                Drawable drawable = new BitmapDrawable(this.getResources(), bitmap);
                int drawableHeight = drawable.getMinimumHeight();
                int drawableWidth = drawable.getMinimumWidth();
                RelativeLayout.LayoutParams params;
                //显示插屏广告，图片与屏幕等高，宽度自适应。横幅和插屏与屏幕等宽，高度自适应
                if (adModel.getPt() != AdPercentage.SPLASH) {
                    int height = (int) ((float) maxWidth / drawableWidth * drawableHeight);
                    if (height > maxHeight) height = maxHeight;
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                            .MATCH_PARENT, height);
                } else {
                    int width = (int) ((float) maxHeight / drawableHeight * drawableWidth);
                    if (width > maxWidth) width = maxWidth;
                    params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams
                            .MATCH_PARENT);
                }


                Configuration cf= this.getResources().getConfiguration();
                int ori = cf.orientation ;
                if(ori == cf.ORIENTATION_LANDSCAPE){
                    RelativeLayout.LayoutParams params_parent = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT);
                    mRelativeLayout = new RelativeLayout(this);
                    mRelativeLayout.setLayoutParams(params_parent);
                    mRelativeLayout.setBackgroundColor(Color.TRANSPARENT);


                    int height = (int) ((float) maxWidth / drawableWidth * drawableHeight);
                    if (height > maxHeight) height = maxHeight;
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                            .MATCH_PARENT, height);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);


                }else if(ori == cf.ORIENTATION_PORTRAIT){
//                    int width = (int) ((float) maxHeight / drawableHeight * drawableWidth);
//                    if (width > maxWidth) width = maxWidth;
//                    params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams
//                            .MATCH_PARENT);
                }

                mImageView.setLayoutParams(params);
                mImageView.setImageBitmap(bitmap);
                mImageView.startAnimation(null);
                if (adModel != null) {
                    if (MConstant.MTest) {
                        mTextView.setVisibility(View.VISIBLE);
                    } else {
                        if (adModel.isHasJumpButton()) {
                            mTextView_Jump.setVisibility(View.VISIBLE);
                        }
                        if(ADTYPE!=AdPercentage.BANNER){
                            mTextView.setVisibility(View.VISIBLE);
                        }
                        mTextView_GUANGGAO.setVisibility(View.VISIBLE);
                    }
                }
                mShowTime = System.currentTimeMillis();
                mSender.sendEmptyMessage(MConstant.what.time_change);
                int show_num = (int) SP.getParam(SP.CONFIG, this, SP.FOT, 0);
                SP.setParam(SP.CONFIG, this, SP.FOT, show_num + 1);
                HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, this);
                break;
            case MConstant.what.delay_show:
                if (isHtml5Ad){
                    mWebView.setVisibility(View.VISIBLE);
                    if (adModel.isHasJumpButton()) {
                        mTextView_Jump.setVisibility(View.VISIBLE);
                    }
                    if (ADTYPE!=AdPercentage.BANNER){
                        mTextView.setVisibility(View.VISIBLE);
                    }
                    mTextView_GUANGGAO.setVisibility(View.VISIBLE);
                    mWebView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
                    mShowTime = System.currentTimeMillis();
                    mSender.sendEmptyMessage(MConstant.what.time_change);
                    int show_num_h5 = (int) SP.getParam(SP.CONFIG, this, SP.FOT, 0);
                    SP.setParam(SP.CONFIG, this, SP.FOT, show_num_h5 + 1);
                    HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, this);
                }
                else {
                ImageLoader.getInstance().loadImageviewBitmap(adModel.getImage(), mImageView,
                        true, mSender);
                }
                break;
            case MConstant.what.h5_splash_show:
                mWebView.setVisibility(View.VISIBLE);
                if (adModel.isHasJumpButton()) {
                    mTextView_Jump.setVisibility(View.VISIBLE);
                }
                if (ADTYPE!=AdPercentage.BANNER){
                    mTextView.setVisibility(View.VISIBLE);
                }
                mTextView_GUANGGAO.setVisibility(View.VISIBLE);
                mWebView.loadDataWithBaseURL("",adModel.getPage() , "text/html", "utf-8", "");
                mShowTime = System.currentTimeMillis();
                mSender.sendEmptyMessage(MConstant.what.time_change);
                int show_num_h5 = (int) SP.getParam(SP.CONFIG, this, SP.FOT, 0);
                SP.setParam(SP.CONFIG, this, SP.FOT, show_num_h5 + 1);
                HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        LogUtils.i(MConstant.TAG,"click="+v.getId());
        switch (v.getId()) {
            case MConstant.id.adcontent:
                int flag =adModel.getFlag();
                if (flag == 1) {
                    finish();
                } else if (flag == 0) {
                    AdClick(adModel);
                    finish();
                }
                break;
            case MConstant.id.clsoebutton:
//                if (adModel.getJumpFunction() == MConstant.jump.click){
//                    onClick(mImageView);
//                }else if (adModel.getJumpFunction() ==MConstant.jump.normal){
//                    finish();
//                }
//                finish();
                break;
            case MConstant.id.tvjump:
                if (isHtml5Ad){
                    finish();
                }
                else {
                if (adModel.getJumpFunction() == MConstant.jump.click){
                    onClick(mImageView);
                }else if (adModel.getJumpFunction() ==MConstant.jump.normal){
                    finish();
                }
                }
                break;
        }
    }

    private int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //点击打开
    private void AdClick(AdModel ad) {
        HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, this);
        if (ad == null) {
            return;
        }

        if (ad.getType() != MConstant.adClickType.app) {
            CommonUtils.openBrowser(this, ad.getUrl());
            return;
        }

        else {
            if (ad.getUrl().contains("click")){
                CommonUtils.openBrowser(this, ad.getUrl());
                return;
            }
            if (CommonUtils.getNetworkSubType(this) == CommonUtils.NET_TYPE_WIFI) {
                ApkDownloadManager manager = ApkDownloadManager.getIntance(this);
                manager.downloadFile(ad);
                HttpManager.reportEvent(adModel, AdReport.EVENT_DOWNLOAD_START, this);
                return;
            }
        }
    }

}
