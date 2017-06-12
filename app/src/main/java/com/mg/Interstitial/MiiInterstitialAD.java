package com.mg.Interstitial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MhttpRequestHelper;
import com.mg.comm.MiiADListener;
import com.mg.demo.Constants;
import com.mg.others.model.AdModel;
import com.mg.others.utils.CommonUtils;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static com.mg.others.ooa.MConstant.request_type.ni;


/**
 * Created by wuqiyan on 2017/6/11.
 */

public class MiiInterstitialAD {

    Context mContext;
    Activity mActivity;
    AdModel adModel;
    MiiADListener listener;
    ImageView imageView;
    CircleTextView cancel;
    RelativeLayout relativeLayout;
    AlertDialog dlg;
    TextView tv;
    boolean oren;
    final static double  H_P = 0.8;
    final  static double W_P = 0.8;




    Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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
                        showShade(bitmap,null);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    public MiiInterstitialAD(Activity mActivity, MiiADListener listener){
        this.mContext=mActivity.getApplicationContext();
        this.mActivity=mActivity;
        this.listener=listener;
        if (false){


        }else {
            Log.i(Constants.TAG,"加载麦广广告...");
            MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,3,listener);
            mhttpRequest.fetchMGAD();
        }

    }


    private void checkADType(AdModel adModel){
        if (adModel.getType()==4){//h5广告

            showShade(null,adModel.getPage());
        }
        else {
            new ImageDownloadHelper(mActivity.getResources().getConfiguration().orientation)
                    .downloadShowImage(mContext,adModel.getImage(),null,mainHandler);
        }

    }

    private void buildDialog(){
        // 生成对话框
        dlg = new AlertDialog.Builder(mActivity).create();
        dlg.setCanceledOnTouchOutside(false);
        //显示对框框
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
        double H_W_P = (bitmap != null ? bitmap.getWidth()/bitmap.getHeight():1.2);


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



        buildAllView(bitmap,html);


        //添加自定义的Layout以及布局方式，注意传入dlg对象本身方便关闭该提示框
        window.addContentView(relativeLayout,pParams);


        //广告成功展示
        listener.onMiiADPresent();


        setClick();
    }


    //有遮罩效果
    public void showShade(Bitmap bitmap,String html) {

        //检查横竖屏
        oren = checkOrientation();

        buildDialog();

        Window window = dlg.getWindow();
        int screenH = CommonUtils.getScreenH(mContext);
        int screenW = CommonUtils.getScreenW(mContext);

        double H_W_P = (bitmap != null ? bitmap.getWidth()/bitmap.getHeight():1.2);

        RelativeLayout.LayoutParams layoutParams ;
        if (oren){//竖屏
            layoutParams = new RelativeLayout.LayoutParams((int) (screenW * W_P),(int) ((screenW * W_P)/H_W_P));
        }else {//横屏
            layoutParams = new RelativeLayout.LayoutParams( (int)(screenH * H_P), (int) (screenH * H_P/H_W_P));
        }
        relativeLayout=new RelativeLayout(mActivity);
        relativeLayout.setBackgroundColor(Color.TRANSPARENT);



        buildAllView(bitmap,html);


        window.addContentView(relativeLayout,layoutParams);

        //广告成功展示
        listener.onMiiADPresent();

        setClick();

    }

    private void setClick(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();

                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                //广告关闭
                listener.onMiiADDismissed();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击广告后相关行为
                new ADClickHelper(mContext).AdClick(adModel);

                //广告点击
                listener.onMiiADClicked();
                //广告关闭
                listener.onMiiADDismissed();
            }
        });
    }



    private void buildImageView(Bitmap bitmap){
        //展示广告的imageview
        imageView=new ImageView(mActivity);
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

    private void buildAllView(Bitmap bitmap,String html){

        if (bitmap!=null && html==null){
            buildImageView(bitmap);
        }
        else {
            buildWebView(html);
        }


        //关闭按钮
        cancel=new CircleTextView(mActivity);
        cancel.setGravity(Gravity.CENTER);
        cancel.setText("X");
        cancel.setWidth(50);
        cancel.setHeight(50);
        cancel.setBackgroundColor(Color.argb(50, 41, 36, 33));
        cancel.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(cancel, lp);

        //"广告"提示
        tv=new TextView(mActivity);
        tv.setText("广告");
        tv.setTextSize(13);
        tv.setPadding(10,5,10,5);
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

    private class CircleTextView extends TextView {

        private Paint mBgPaint = new Paint();


        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            init(context);
        }

        public CircleTextView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public CircleTextView(Context context) {
            super(context);

            init(context);
        }

        public void init(Context context) {
            mContext = context;
            mBgPaint.setAntiAlias(true);
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int max = Math.max(measuredWidth, measuredHeight);
            setMeasuredDimension(max, max);
        }
        @Override
        public void setBackgroundColor(int color) {

            mBgPaint.setColor(color);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.setDrawFilter(pfd);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
            super.draw(canvas);
        }
    }


}
