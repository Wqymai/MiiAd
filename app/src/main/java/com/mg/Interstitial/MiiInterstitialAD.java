package com.mg.Interstitial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MhttpRequestHelper;
import com.mg.comm.MiiADListener;
import com.mg.comm.MiiBaseAD;
import com.mg.demo.Constants;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;



/**
 * Created by wuqiyan on 2017/6/11.
 */

public class MiiInterstitialAD  extends MiiBaseAD{

    private Context mContext;
    private Activity mActivity;
    private AdModel adModel;
    private SDKConfigModel sdk;
    private MiiADListener listener;
    private MiiImageView imageView;
    private MiiCircleTextView cancel;
    private RelativeLayout relativeLayout;
    private AlertDialog dlg;
    private TextView tv;
    boolean oren;
    final static double  H_P = 0.8;
    final  static double W_P = 0.8;
    private InterstitialAD iad;
    private boolean isShade;





    Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    checkOpenAD();
                    break;
                case 200:
                    Log.i(Constants.TAG,"收到RA请求成功的消息 插屏");
                    try {
                        adModel= (AdModel) msg.obj;
                        checkADType(adModel);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 300:
                    Log.i(Constants.TAG,"收到下载图片bitmap 插屏");
                    try {
                        Bitmap bitmap= (Bitmap) msg.obj;
                        if (bitmap == null){
                            return;
                        }
                        checkShade(bitmap,null);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    try {
                        openGDTAD(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    public MiiInterstitialAD(Activity mActivity, final boolean isShade, final MiiADListener listener){
        this.mContext=mActivity.getApplicationContext();
        this.mActivity=mActivity;
        this.listener=listener;
        this.isShade=isShade;
//        boolean isFirst= (boolean) SP.getParam(SP.CONFIG,mContext,SP.FIRSTHB,true);
//        if (isFirst){
//            SP.setParam(SP.CONFIG,mContext,SP.FIRSTHB,false);
//            new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD(isFirst);
//            return;
//        }
//        checkOpenAD();

        if (isFirstEnter(mContext)){
            new MhttpRequestHelper(mContext,mainHandler,0,listener).fetchMGAD(true);
            return;
        }
        checkOpenAD();



    }
//    private void openGMAD(){
//        Log.i(Constants.TAG,"加载麦广广告...插屏");
//        if (sdk == null){
//            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
//        }
//        if (!sdk.isAdShow()){
//            Log.i(Constants.TAG,"openGMAD O=0");
//            listener.onMiiNoAD(2000);
//            return;
//        }
//        MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,3,listener);
//        mhttpRequest.fetchMGAD(false);
//
//    }
    private void openGDTAD(final boolean shouldReturn){
        Log.i(Constants.TAG,"加载广点通广告...插屏");
        iad = new InterstitialAD(mActivity, Constants.APPID, Constants.InterteristalPosID);
        iad.setADListener(new AbstractInterstitialADListener() {
            @Override
            public void onADReceive() {
                if (isShade){
                    iad.show();

                }else {
                    iad.showAsPopupWindow();
                }
                listener.onMiiADPresent();
            }

            @Override
            public void onNoAD(int i) {
                if (!shouldReturn){

//                    if (isADShow(sdk,mContext)){
//                        new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD1(true);
//                    }
//                    else {
//                        Log.i(Constants.TAG,"openGDTAD...o=0");
//                        listener.onMiiNoAD(2000);
//                    }
//                    openMGAD_Sequ_Gdtfail(mContext,mainHandler,listener,sdk,3);

                    new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD1(true);

                    return;
                }
            }

            @Override
            public void onADClicked() {
                listener.onMiiADClicked();
            }

            @Override
            public void onADClosed() {

                listener.onMiiADDismissed();
            }
        });
        iad.loadAD();
    }


    private void checkOpenAD(){
//        if (sdk == null){
//            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
//        }
//        int sf_mg = sdk.getSf_mg();
//        int sf_gdt = sdk.getSf_gdt();
//        int sum = sf_gdt + sf_mg;
//        if (sum == 0){
//
//            Log.i(Constants.TAG,"sum==0");
//            return;
//
//        }
//        else if (sum == 100){
//            int show_percentage = (int) ((Math.random() * 100)+1);
//            if (show_percentage <= sf_mg){
//                Log.i(Constants.TAG,"sum==100 MG");
//                openGMAD();
//            }
//            else {
//                Log.i(Constants.TAG,"sum==100 GDT");
//                openGDTAD(true);
//
//            }
//        }
//        else if (sum > 100){
//            if (sf_mg > sf_gdt){
//                Log.i(Constants.TAG,"sum > 100 MG");
//                if (sdk.isAdShow()){
//                    MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,3,listener);
//                    mhttpRequest.fetchMGAD1(false);
//                }
//                else {
//                    mainHandler.sendEmptyMessage(400);
//                }
//            }
//            else {
//                Log.i(Constants.TAG,"sum > 100 GDT");
//                openGDTAD(false);
//            }
//        }

        SourceAssignModel saModel=checkADSource(mContext);

        if (saModel.type == 1){
            return;
        }
        else if (saModel.type == 2){

            if (saModel.firstChoose == 1){


//                openMGAD_Single(mContext,mainHandler,listener,sdk,3);
                new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD(false);
            }
            else {

                openGDTAD(true);

            }
        }
        else if (saModel.type == 3){

            if (saModel.firstChoose == 1){


//                openMGAD_Sequ(mContext,mainHandler,listener,sdk,3 );
                new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD1(false);

            }
            else {
                openGDTAD(false);
            }
        }

    }




    private void checkShade(Bitmap bitmap,String html){
        if (isShade){
            showShade(bitmap,html);
        }
        else {
            showNoShade(bitmap,html);
        }
    }


    private void checkADType(AdModel adModel){
        if (adModel.getType()==4){//h5广告
            checkShade(null,adModel.getPage());
        }
        else {
            new ImageDownloadHelper(mActivity.getResources().getConfiguration().orientation)
                    .downloadShowImage(mContext,adModel.getImage(),null,mainHandler);
        }

    }

    private void buildDialog(){
        dlg = new AlertDialog.Builder(mActivity).create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }

    //无遮罩效果
    private void  showNoShade(Bitmap bitmap,String html){

        //检查横竖屏
        oren = checkOrientation();

        buildDialog();

        Window window = dlg.getWindow();
        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
        int screenH = CommonUtils.getScreenH(mContext);
        int screenW = CommonUtils.getScreenW(mContext);

        boolean ishtml5 = bitmap!=null ? false:true;
        double H_W_P = ishtml5 ? 1.2: bitmap.getWidth()/bitmap.getHeight();


        if (oren){//竖屏
            params1.height =(int) (screenW * W_P);
            params1.width = (int) ((screenW * W_P)/H_W_P);
        }else {
            params1.width = (int)(screenH * H_P);
            params1.height = (int) (screenH * H_P/H_W_P);
        }
        window.setAttributes(params1);


        RelativeLayout.LayoutParams pParams=new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout=new RelativeLayout(mActivity);
        relativeLayout.setLayoutParams(pParams);
        relativeLayout.setBackgroundColor(Color.TRANSPARENT);


        if (ishtml5){
            buildWebView(html);
        }
        else {
            buildImageView(bitmap);
        }

        buildOthersView();


        window.addContentView(relativeLayout,pParams);


        //广告成功展示
        listener.onMiiADPresent();

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        setClick(ishtml5);
    }


    //有遮罩效果
    public void showShade(Bitmap bitmap,String html) {

        //检查横竖屏
        oren = checkOrientation();

        buildDialog();

        Window window = dlg.getWindow();
        int screenH = CommonUtils.getScreenH(mContext);
        int screenW = CommonUtils.getScreenW(mContext);

        boolean ishtml5 = bitmap!=null ? false:true;

        double H_W_P = ishtml5 ? 1.2: bitmap.getWidth()/bitmap.getHeight();

        RelativeLayout.LayoutParams layoutParams ;
        if (oren){//竖屏
            layoutParams = new RelativeLayout.LayoutParams((int) (screenW * W_P),(int) ((screenW * W_P)/H_W_P));
        }else {//横屏
            layoutParams = new RelativeLayout.LayoutParams( (int)(screenH * H_P), (int) (screenH * H_P/H_W_P));
        }
        relativeLayout=new RelativeLayout(mActivity);
        relativeLayout.setBackgroundColor(Color.TRANSPARENT);


        if (ishtml5){

            buildWebView(html);
        }
        else {
            buildImageView(bitmap);
        }

        buildOthersView();


        window.addContentView(relativeLayout,layoutParams);

        //广告成功展示
        listener.onMiiADPresent();

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        setClick(ishtml5);

    }

    private void setClick(boolean ishtml5){
       Log.i(Constants.TAG,"setClick ishtml5="+ishtml5);
       try {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    //广告关闭
                    listener.onMiiADDismissed();

                    if (dlg == null){
                      dlg.dismiss();
                    }

                }
            });
            if (ishtml5){
                return;
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(Constants.TAG,"点击广告图片了。。。"+adModel.toString());
                    //点击广告后相关行为
                    new ADClickHelper(mContext).AdClick(adModel);

                    //广告点击
                    listener.onMiiADClicked();
                    //广告关闭
                    listener.onMiiADDismissed();
                }
            });
       }catch (Exception e){
           e.printStackTrace();
       }
    }



    private void buildImageView(Bitmap bitmap){
        imageView=new MiiImageView(mActivity);
        RelativeLayout.LayoutParams ivParam=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(ivParam);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        relativeLayout.addView(imageView);
    }
    private void buildWebView(String html){
        WebView webView = new WebView(mActivity);
        RelativeLayout.LayoutParams params_webview = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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

                //广告点击回调
                listener.onMiiADClicked();

                return true;
            }
        });

        webView.loadDataWithBaseURL("",html , "text/html", "utf-8", "");
    }

    private void buildOthersView(){

        //关闭按钮
        cancel=new MiiCircleTextView(mActivity);
        cancel.setGravity(Gravity.CENTER);
        cancel.setText("X");
        cancel.setWidth(60);
        cancel.setHeight(60);
        cancel.setBackgroundColor(Color.argb(10, 41, 36, 33));
        cancel.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(cancel, lp);

        //"广告"提示
        tv=new TextView(mActivity);
        tv.setText("广告");
        tv.setTextSize(8);
        tv.setPadding(5,3,5,3);
        tv.setBackgroundColor(Color.argb(50, 41, 36, 33));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#FFF0F5"));
        RelativeLayout.LayoutParams tvlp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(tv,tvlp);

    }

    //true是竖屏 false是横屏
    public boolean checkOrientation(){
        Configuration mConfiguration = mActivity.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation ; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            Log.i(Constants.TAG,"竖屏...");
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){
            Log.i(Constants.TAG,"横屏...");
            mActivity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            return false;
        }
        return true;
    }




}
