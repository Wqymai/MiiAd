package com.mg.interstitial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.asyn.HbReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MiiBaseAD;

import com.mg.interf.MiiInterADListener;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;



/**
 * Created by wuqiyan on 2017/6/11.
 * 固定形式的插屏
 */

public class MiiInterstitialAD extends MiiBaseAD{

    private Context mContext;
    private Activity mActivity;
    private AdModel adModel;
    private MiiInterADListener listener;
    private MiiImageView imageView;
    private WebView webView;
    private MiiCircleTextView cancel;
    private RelativeLayout relativeLayout;
    private AlertDialog dlg;
    private TextView tv;
    boolean oren;
    private   double  H_P = 0.8;
    private    double W_P = 0.8;
    private boolean isShade;
    private boolean isJoinImg = false;
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
                        adModel = (AdModel) msg.obj;
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
                        checkShade(bitmap,null);
                    }
                    catch (Exception e){
                        listener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    listener.onMiiNoAD(1000);
                    break;
                case 600:
                    new HbReturn(reqAsyncModel).fetchMGAD();
                    break;
                case 700:
                    listener.onMiiNoAD(3011);
                    break;
            }

        }
    };

    public MiiInterstitialAD(Activity mActivity, boolean isShade, String appid, String lid, final MiiInterADListener listener){

      try {

          this.mContext = mActivity.getApplicationContext();
          this.mActivity = mActivity;
          this.listener = listener;
          this.isShade = isShade;

          reqAsyncModel.context = this.mContext;
          reqAsyncModel.handler = this.mainHandler;
          reqAsyncModel.listener = this.listener;
          reqAsyncModel.pt = 3;
          reqAsyncModel.appid = appid;
          reqAsyncModel.lid = lid;

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              check23AbovePermission(mActivity, mainHandler);
              return;
          }
          new HbReturn(reqAsyncModel).fetchMGAD();

      }
      catch (Exception e){

          listener.onMiiNoAD(2001);
          e.printStackTrace();
      }
    }

    private void checkOpenAD(){

        try {

            SourceAssignModel saModel = checkADSource(mContext,3);

            if (saModel == null){
                new HbReturn(reqAsyncModel).fetchMGAD();
                return;
            }

            if (saModel.type == 1){
                listener.onMiiNoAD(3005);
                return;
            }
            new RaReturn(reqAsyncModel).fetchMGAD();

        }
        catch (Exception e){

            listener.onMiiNoAD(3012);
            e.printStackTrace();
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
        if (adModel.getType() == 4){

            checkShade(null,adModel.getPage());

        }
        else {
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

                }else {

                     isJoinImg = false;
                     new ImageDownloadHelper().downloadShowImage(mContext,adModel.getImage(),mainHandler);

                }
            }
            catch (Exception e){

                listener.onMiiNoAD(3011);
                e.printStackTrace();

            }

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

        try {

            //检查横竖屏
            oren = checkOrientation(mActivity);

            buildDialog();

            Window window = dlg.getWindow();
            WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
            int screenH = CommonUtils.getScreenH(mContext);
            int screenW = CommonUtils.getScreenW(mContext);


            boolean ishtml5 = bitmap != null ? false : true;
            double H_W_P = ishtml5 ? 1.2 : div(bitmap.getWidth(),bitmap.getHeight(),1);

            SDKConfigModel sdk = checkSdkConfig(mContext);
            if (sdk != null){
                W_P = div(sdk.getCz(),100,1);
            }


            if (oren) {//竖屏
                params1.width = (int) (screenW * W_P);
                params1.height = (int) ((screenW * W_P) / H_W_P);

            } else {
                params1.width = (int) (screenH * H_P);
                params1.height = (int) (screenH * H_P / H_W_P);
            }
            window.setAttributes(params1);


            RelativeLayout.LayoutParams pParams = new RelativeLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            relativeLayout = new RelativeLayout(mActivity);
            relativeLayout.setLayoutParams(pParams);
            relativeLayout.setBackgroundColor(Color.TRANSPARENT);


            if (ishtml5) {

                buildWebView(html);
            } else {
                buildImageView(bitmap);
            }

            buildOthersView();

            window.addContentView(relativeLayout, pParams);

            //广告成功展示
            listener.onMiiADPresent();

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

            setClick(ishtml5, bitmap);
        }
        catch (Exception e){

            listener.onMiiNoAD(3009);
            e.printStackTrace();

        }
    }


    /**
    弹出框有遮罩效果的
    */
    public void showShade(Bitmap bitmap,String html) {

      try {

        //检查横竖屏
        oren = checkOrientation(mActivity);

        buildDialog();

        Window window = dlg.getWindow();

        int screenH = CommonUtils.getScreenH(mContext);
        int screenW = CommonUtils.getScreenW(mContext);

        boolean ishtml5 = bitmap!=null ? false:true;
        double H_W_P = ishtml5 ? 1.2: div(bitmap.getWidth(),bitmap.getHeight(),1);

        SDKConfigModel sdk = checkSdkConfig(mContext);
        if (sdk != null){
          W_P = div(sdk.getCz(),100,1);
        }

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
      catch (Exception e){

          listener.onMiiNoAD(3009);
          e.printStackTrace();

      }
    }

    private void setClick(boolean ishtml5, final Bitmap bitmap){

       try {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  try {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                        if (bitmap != null){
                            bitmap.recycle();
                        }

                        if (webView != null){
                            ViewParent parent = webView.getParent();
                            if (parent != null) {
                                ((ViewGroup) parent).removeView(webView);
                            }
                            webView.stopLoading();
                            webView.getSettings().setJavaScriptEnabled(false);
                            webView.clearHistory();
                            webView.clearView();
                            webView.removeAllViews();
                            try {

                                webView.destroy();

                            } catch (Exception e) {

                                e.printStackTrace();

                            }
                        }
                        if (dlg != null){
                          dlg.dismiss();
                        }
                  }
                  catch (Exception e){

                      e.printStackTrace();

                  }
                  listener.onMiiADDismissed();

                }
            });
            if (ishtml5){
                return;
            }
            if (isJoinImg){
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                            listener.onMiiADClicked();
                            listener.onMiiADDismissed();

                            AdModel ad= (AdModel) adModel.clone();
                            new ADClickHelper(mContext).AdClick(ad);

                            if (bitmap != null){
                                bitmap.recycle();
                            }
                            if (dlg != null){
                                dlg.dismiss();
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });

                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  try {

                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                        listener.onMiiADClicked();
                        listener.onMiiADDismissed();

                        AdModel ad= (AdModel) adModel.clone();
                        new ADClickHelper(mContext).AdClick(ad);

                        if (bitmap != null){
                            bitmap.recycle();
                        }
                        if (dlg != null){
                            dlg.dismiss();
                        }

                  }
                  catch (Exception e){
                      e.printStackTrace();
                  }


                }
            });

            imageView.setOnTouchListener(new View.OnTouchListener() {
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

           e.printStackTrace();

       }
    }


    private void JoinImg(Bitmap bitmap){
        relativeLayout.setBackgroundColor(Color.parseColor("#d1dbee"));
        ImageView iv = new ImageView(mActivity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dip2px(mContext,60),dip2px(mContext,60));
        layoutParams.setMargins(0,100,0,50);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setId(MConstant.id.iimgiv);
        relativeLayout.addView(iv);
        iv.setImageBitmap(bitmap);

        TextView nameTxt= new TextView(mActivity);
        RelativeLayout.LayoutParams params1= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params1.addRule(RelativeLayout.BELOW,MConstant.id.iimgiv);
        params1.setMargins(100,0,100,0);
        nameTxt.setId(MConstant.id.inametv);
        nameTxt.setTextSize(15);
        nameTxt.setTextColor(Color.parseColor("#8B7D6B"));
        if (adModel.getName() != null && ! adModel.getName().equals("null")){
            nameTxt.setText(adModel.getName());
        }

        TextView descTxt= new TextView(mActivity);
        RelativeLayout.LayoutParams params2= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params2.setMargins(100,50,100,0);
        params2.addRule(RelativeLayout.BELOW,MConstant.id.inametv);
        descTxt.setTextColor(Color.parseColor("#8B7D6B"));
        descTxt.setTextSize(15);
        if (adModel.getDesc() != null && ! adModel.getDesc().equals("null")){
            descTxt.setText(adModel.getDesc());
        }

        relativeLayout.addView(nameTxt,params1);
        relativeLayout.addView(descTxt,params2);

    }
    private void buildImageView(Bitmap bitmap){

       if (isJoinImg){
           JoinImg(bitmap);
       }
       else {
        imageView = new MiiImageView(mActivity);
        RelativeLayout.LayoutParams ivParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(ivParam);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        relativeLayout.addView(imageView);
        imageView.setImageBitmap(bitmap);
       }
    }
    private void buildWebView(String html){

        try{
            webView = new WebView(mActivity);
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

                    //广告点击回调
                    listener.onMiiADClicked();

                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    //点击上报
                    HttpManager.reportEvent(adModel, AdReport.EVENT_CLICK, mContext);

                    return true;
                }
            });

            webView.loadDataWithBaseURL("",html , "text/html", "utf-8", "");

        }catch (Exception e){

            listener.onMiiNoAD(3010);
            e.printStackTrace();

        }
    }

    private void buildOthersView(){

      try {
        
        //添加关闭按钮
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

        RelativeLayout.LayoutParams tvlp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //添加"广告"字样
        tv = tvADCreate(mActivity);
        if (adModel.getSourceMark()!= null && !adModel.getSourceMark().equals("")){
            tv.setText(adModel.getSourceMark()+"|广告");
        }
        relativeLayout.addView(tv,tvlp);

      }catch (Exception e){

          listener.onMiiNoAD(3009);
          e.printStackTrace();

      }

    }



    @Override
    public void recycle() {}
}
