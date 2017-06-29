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
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

import com.mg.asyn.FirstEnter;
import com.mg.asyn.HbRaNoReturn;
import com.mg.asyn.HbRaReturn;
import com.mg.asyn.JustHbRelative;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.GdtInfoModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;
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
    private String appid;
    private String interstitialid;
    private boolean isShade;
    private ReqAsyncModel reqAsyncModel = new ReqAsyncModel();


    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    checkOpenAD();
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
                case 500:
                    listener.onMiiNoAD(1000);
                    break;
                case 600:
                    Init();
                    break;
            }

        }
    };

    public MiiInterstitialAD(Activity mActivity, String appid,String interstitialid,boolean isShade,final MiiADListener listener){
        this.mContext = mActivity.getApplicationContext();
        this.mActivity = mActivity;
        this.listener = listener;
        this.isShade = isShade;
        this.appid = appid;
        this.interstitialid = interstitialid;

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
        checkOpenAD();
    }


    private void openGDTAD(final boolean shouldReturn){
        LogUtils.i(MConstant.TAG,"load gdt");

        new JustHbRelative(reqAsyncModel).fetchMGAD();

        String AID = "";
        String IPID = "";
        GdtInfoModel model = getGdtIds(mContext);
        try {
            AID = model.getAPPID();
            IPID = model.getInterteristalPosID();
        }catch (Exception e){
            e.printStackTrace();
        }

        iad = new InterstitialAD(mActivity, AID, IPID);
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

                    new HbRaReturn(reqAsyncModel).fetchMGAD();

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

        SourceAssignModel saModel = checkADSource(mContext);

        if (saModel == null){
            new FirstEnter(reqAsyncModel).fetchMGAD();
            return;
        }

        if (saModel.type == 1){
            return;
        }
        else if (saModel.type == 2){

            if (saModel.firstChoose == 1){

                new HbRaReturn(reqAsyncModel).fetchMGAD();
            }
            else {

                openGDTAD(true);

            }
        }
        else if (saModel.type == 3){

            if (saModel.firstChoose == 1){

                new HbRaNoReturn(reqAsyncModel).fetchMGAD();

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
        if (adModel.getType() == 4){//h5广告
            checkShade(null,adModel.getPage());
        }
        else {
            new ImageDownloadHelper(mActivity.getResources().getConfiguration().orientation)
                    .downloadShowImage(mContext,adModel.getImage(),3,mainHandler);
        }

    }

    private void buildDialog(){
        dlg = new AlertDialog.Builder(mActivity).create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }

    /**
    弹出框无遮罩效果的
    */
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
            params1.width =(int) (screenW * W_P);
            params1.height = (int) ((screenW * W_P)/H_W_P);
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

        setClick(ishtml5,bitmap);
    }


    /**
    弹出框有遮罩效果的
    */
    public void showShade(Bitmap bitmap,String html) {

        //检查横竖屏
        oren = checkOrientation();

        buildDialog();

        Window window = dlg.getWindow();

        int screenH = CommonUtils.getScreenH(mContext);
        int screenW = CommonUtils.getScreenW(mContext);

        boolean ishtml5 = bitmap!=null ? false:true;
        double H_W_P = ishtml5 ? 1.2: bitmap.getWidth()/bitmap.getHeight();

        if (oren){
          window.setLayout((int) (screenW * W_P),(int) ((screenW * W_P)/H_W_P));
        }
        else {
            window.setLayout((int)(screenH * H_P),(int) (screenH * H_P/H_W_P));
        }


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        relativeLayout = new RelativeLayout(mActivity);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundColor(Color.BLUE);


        if (ishtml5){

            buildWebView(html);
        }
        else {
            buildImageView(bitmap);
        }

        buildOthersView();


        window.setContentView(relativeLayout);

        //广告成功展示
        listener.onMiiADPresent();

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        setClick(ishtml5,bitmap);

    }

    private void setClick(boolean ishtml5, final Bitmap bitmap){

       try {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    listener.onMiiADDismissed();

                    if (bitmap != null){
                        bitmap.recycle();
                    }

                    if (dlg != null){
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


                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    new ADClickHelper(mContext).AdClick(adModel);

                    //广告点击
                    listener.onMiiADClicked();
                    //广告关闭
                    listener.onMiiADDismissed();

                    if (bitmap != null){
                        bitmap.recycle();
                    }
                    if (dlg != null){
                        dlg.dismiss();
                    }
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

            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){

            mActivity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            return false;
        }
        return true;
    }

}
