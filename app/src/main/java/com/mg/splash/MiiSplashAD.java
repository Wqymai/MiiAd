package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mg.comm.ADClickHelper;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MConstant;
import com.mg.comm.MhttpRequestHelper;
import com.mg.comm.MiiADListener;
import com.mg.demo.Constants;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;




/**
 * Created by wuqiyan on 17/6/9.
 */

public class MiiSplashAD {

     private Context mContext;
     private SDKConfigModel sdk;
     private ViewGroup adContainer;
     private View skipContainer;
     private Activity mActivity;
     private MiiADListener listener;
     private AdModel adModel;
     private ImageView adImageView;
     private WebView webView;
     CountDownTimer timer;
     private String html="<!DOCTYPE html><html><head><meta " +
             "charset=\"utf-8\"><title>订婚宴送婚车</title><meta name=\"keywords\" " +
             "content=\"订婚宴送婚车\"><meta name=\"description\" content=\"订婚宴送婚车_daoxila.com\"><meta " +
             "name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1," +
             "minimum-scale=1,user-scalable=0,minimal-ui\"><meta " +
             "name=\"apple-mobile-web-app-capable\" content=\"yes\"><meta " +
             "name=\"apple-touch-fullscreen\" content=\"yes\"><meta name=\"full-screen\" " +
             "content=\"yes\"><meta name=\"apple-mobile-web-app-status-bar-style\" " +
             "content=\"black\"><meta name=\"format-detection\" content=\"telephone=no\"><link " +
             "rel=\"apple-touch-icon\" href=\"//s4.dxlfile.com/public/img/logo/wap.png\"><link " +
             "href=\"//s4.dxlfile.com/??public/css/m.css,m-www/css/m-www.css?v=14254570119\" " +
             "rel=\"stylesheet\" type=\"text/css\"><script src=\"//s4.dxlfile" +
             ".com/??public/js/jquery-2.1.0.min.js,public/js/dxl2.js,public/js/m" +
             ".js?v=14254570119\"></script><link rel=\"stylesheet\" href=\"//s4.dxlfile" +
             ".com/m-www/css/CuXiao/1607.css?v=14254570119\"><script type=\"text/javascript\" " +
             "src=\"//s4.dxlfile.com/m-www/js/CuXiao/1607" +
             ".js?v=14254570119\"></script></head><script>var opts={ID:\"1039\",city:\"sh\"," +
             "time:\"2017/01/18 23:59:59\",promotion_sign:\"新用户专享定婚宴送婚车\",coupon_id:\"0\"," +
             "order_pro:\"3\",pro_smsflag:\"1\"};</script><body><header><div " +
             "class=\"secTitMain\"><a class=\"prev\"></a> 订婚宴送婚车 " +
             "<a href=\"/\" class=\"home\"></a></div></header><div id=\"cityMain\"><img " +
             "src=\"https://iq.dxlfile.com/promotion/large/2016-12/20161221114355345.jpg\"> " +
             "</div><div id=\"return\"><div class=\"timeMain\"><p>距离活动结束： <span " +
             "class=\"timeBox\"><i class=\"day\"></i>天<i class=\"hour\"></i>时<i " +
             "class=\"min\"></i>分</span></p></div><div " +
             "class=\"rules\"><h3>活动简介：</h3><p>订婚宴，送豪华婚车！ 订婚宴>=8万元；即送一辆 保时捷Panamera 婚车头车 " +
             "订婚宴>=5万元；即送一辆 特斯拉 婚车头车 本活动仅限上海地区</p><div " +
             "class=\"getReturn\">我要报名</div></div></div><div class=\"activeRule\" " +
             "style=\"background:#fff\"><p></p><p><img src=\"https://iq.dxlfile" +
             ".com/promotion/large/2016-11/20161107173543724.jpg\" _src=\"https://iq.dxlfile" +
             ".com/promotion/large/2016-11/20161107173543724.jpg\"></p></div><div id=\"hunYan\" " +
             "class=\"items\"><h3 class=\"tit\"><span>婚宴酒店</span></h3><ul class=\"clearfix\"><li " +
             "dxlAppTouchAction=\"$.dxlAppHotelDeatil(5499)\"><a " +
             "href=\"//m.daoxila.com/sh/HunYan/HuaJiaLiShe-ShiBo-Info#cid=CuXiao_1039\" " +
             "dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile" +
             ".com/hotel/small/2015-09/20150901140389510.jpg\"><div " +
             "class=\"names\">花嫁丽舍私人婚礼会所（世博店）</div><div class=\"infos\">婚礼会所 浦东新区 " +
             "可容纳55桌</div><div class=\"discounts\"></div><p " +
             "class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil" +
             "(83)\"><a " +
             "href=\"//m.daoxila.com/sh/HunYan/jintingzhuangyuan-Info#cid=CuXiao_1039\" " +
             "dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile" +
             ".com/hotel/small/2017-02/20170204175983536.jpg\"><div " +
             "class=\"names\">金庭庄园酒店</div><div class=\"infos\">婚礼会所 长宁区 可容纳50桌</div><div " +
             "class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li " +
             "dxlAppTouchAction=\"$.dxlAppHotelDeatil(4784)" +
             "\"><a href=\"//m.daoxila.com/sh/HunYan/DongFangWeiTing-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2015-08/20150818175081685.jpg\"><div class=\"names\">东方薇婷私人婚礼会所</div><div class=\"infos\">婚礼会所 浦东新区 可容纳30桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(7071)\"><a href=\"//m.daoxila.com/sh/HunYan/ShengLaWei-WaiTan-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2017-04/20170411110135098.jpg\"><div class=\"names\">圣拉维一站式婚礼会馆（外滩幸福码头店）</div><div class=\"infos\">婚礼会所 黄浦区 可容纳30桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(3253)\"><a href=\"//m.daoxila.com/sh/HunYan/yangzijiangwanli-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2012-12/20121229131839.jpg\"><div class=\"names\">上海扬子江万丽大酒店</div><div class=\"infos\">星级酒店 长宁区 可容纳50桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(9031)\"><a href=\"//m.daoxila.com/sh/HunYan/MeiGuiLi-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2017-05/20170519171682933.jpg\"><div class=\"names\">LAVIN玫瑰里（外滩店）</div><div class=\"infos\">婚礼会所 杨浦区 可容纳40桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(2268)\"><a href=\"//m.daoxila.com/sh/HunYan/LaoFengGe-GuXiang-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2014-09/20140929150811.jpg\"><div class=\"names\">老丰阁品珍轩（古象店）</div><div class=\"infos\">特色餐厅 黄浦区 可容纳17桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(2321)\"><a href=\"//m.daoxila.com/sh/HunYan/hengshanbeijiao-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2015-10/20151015104476142.jpg\"><div class=\"names\">上海衡山北郊宾馆</div><div class=\"infos\">星级酒店 宝山区 可容纳38桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li><li dxlAppTouchAction=\"$.dxlAppHotelDeatil(8364)\"><a href=\"//m.daoxila.com/sh/HunYan/QinYuan-SH-Info#cid=CuXiao_1039\" dxlAppRemoveHref=\"true\"><img _src=\"https://iq.dxlfile.com/hotel/small/2015-10/20151021164481234.jpg\"><div class=\"names\">秦源</div><div class=\"infos\">婚礼会所 浦东新区 可容纳18桌</div><div class=\"discounts\"></div><p class=\"details\">查看优惠详情</p></a></li></ul><div class=\"more\" dxlAppTouchAction=\"$.dxlAppHotelList()\"><a href=\"//m.daoxila.com/sh/HunYan/\" dxlAppRemoveHref=\"true\">查看更多 &gt;</a></div></div><div id=\"pinPai\"></div><div class=\"botHeight\"></div><div class=\"botEntry\"><input type=\"tel\" class=\"mobile\" placeholder=\"请输入手机号\" maxlength=\"11\"><div class=\"toEntry\">我要报名</div></div></body></html>";


     Handler mainHandler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case 100:
                     checkOpenAD();
                     break;

                 case 200:
                    Log.i(Constants.TAG,"收到RA请求成功的消息");
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
                             adContainer.removeView(adImageView);
                             return;
                         }
                        showSplashAD(bitmap);
                     }
                     catch (Exception e){
                         adContainer.removeView(adImageView);
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


    private void  checkADType(AdModel adModel){

        if (adModel.getType() == 4){//h5广告

            Log.i(Constants.TAG,"是HTML5广告...");
            webView = new WebView(mActivity);
            FrameLayout.LayoutParams params_webview = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
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
            adContainer.addView(webView);


            TextView tv=tvADCreate();
            adContainer.addView(tv);

            //展示上报
            HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);
            //广告成功展示
            listener.onMiiADPresent();

            //倒计时开始
            adCountDownTimer();

            //记录展示次数
            int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

            skipContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //广告关闭回调
                    listener.onMiiADDismissed();
                    mActivity.finish();
                }
            });

        }else {
            Log.i("Constants.TAG","不是HTML5广告...");
            FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            adImageView=new ImageView(mActivity);
            adImageView.setLayoutParams(layoutParams);
            adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            adContainer.addView(adImageView);

            TextView tv=tvADCreate();
            adContainer.addView(tv);


            new ImageDownloadHelper(0).downloadShowImage(mContext,adModel.getImage(),adImageView,mainHandler);
        }

    }

     //"广告"提示
     private TextView tvADCreate(){
         TextView tv=new TextView(mActivity);
         tv.setText("广告");
         tv.setTextSize(10);
         tv.setPadding(5,3,5,3);
         tv.setBackgroundColor(Color.argb(50, 41, 36, 33));
         tv.setGravity(Gravity.CENTER);
         tv.setTextColor(Color.parseColor("#FFF0F5"));
         FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         lp.gravity=Gravity.RIGHT|Gravity.BOTTOM;
         tv.setLayoutParams(lp);
         return tv;
     }

     private void showSplashAD(final Bitmap bitmap){

         adImageView.setImageBitmap(bitmap);

         //展示上报
         HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);

         //广告成功展示
         listener.onMiiADPresent();


         //倒计时开始
         adCountDownTimer();

         //记录展示次数
         int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
         SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

         skipContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.onMiiADDismissed();
                 if (bitmap!=null){
                     bitmap.recycle();
                 }

                 timer.cancel();
                 mActivity.finish();
             }
         });
         adImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 listener.onMiiADClicked();

                 //点击广告后相关行为
                 new ADClickHelper(mContext).AdClick(adModel);

                 timer.cancel();

                 mActivity.finish();

             }
         });

     }
     private void adCountDownTimer(){
         //广告倒计时
         if (sdk == null){
             sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
         }
         long time = sdk.getDisplayTime(2);
         Log.i(Constants.TAG,"开始倒计时 time="+time);

         timer = new CountDownTimer((time+1)*1000,1000){
             @Override
             public void onTick(long millisUntilFinished) {
                 Log.i(Constants.TAG,"倒计时 "+millisUntilFinished);
                 listener.onMiiADTick((long) ((Math.floor(millisUntilFinished/1000))*1000));
             }

             @Override
             public void onFinish() {
                 Log.i(Constants.TAG,"倒计时结束 ");
                 listener.onMiiADDismissed();
             }
         };
         timer.start();

     }

     public  MiiSplashAD(Activity activity, ViewGroup adContainer,View skipContainer,MiiADListener adListener){

         this.mActivity=activity;

         this.mContext = activity.getApplicationContext();

         this.adContainer=adContainer;

         this.skipContainer=skipContainer;

         this.listener=adListener;

         if (skipContainer==null){
             return;
         }

         boolean isFirst= (boolean) SP.getParam(SP.CONFIG,mContext,SP.FIRSTHB,true);
         if (isFirst){
             SP.setParam(SP.CONFIG,mContext,SP.FIRSTHB,false);
             new MhttpRequestHelper(mContext,mainHandler,3,listener).fetchMGAD(isFirst);
             return;
         }
         //根据条件判断是展示广点通还是麦广
         checkOpenAD();


     }

    private void checkOpenAD(){

        if (sdk == null){
            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        int sf_mg = sdk.getSf_mg();
        int sf_gdt = sdk.getSf_gdt();
        int sum = sf_gdt + sf_mg;
        if (sum == 0){

            Log.i(Constants.TAG,"sum==0");
            return;

        }
        else if (sum == 100){
            int show_percentage = (int) ((Math.random() * 100)+1);
            if (show_percentage <= sf_mg){

                 Log.i(Constants.TAG,"sum==100 MG");
                 openGMAD();

            }
            else {

                Log.i(Constants.TAG,"sum==100 GDT");
                openGDTAD(true);

            }
        }
        else if (sum > 100){
            if (sf_mg > sf_gdt){

                Log.i(Constants.TAG,"sum > 100 MG");
               if (sdk.isAdShow()){

                  MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,2,listener);
                  mhttpRequest.fetchMGAD1(false);

               }
               else {

                   mainHandler.sendEmptyMessage(400);

               }
            }
            else {

                Log.i(Constants.TAG,"sum > 100 GDT");
                openGDTAD(false);

            }
        }

    }



    private void openGMAD(){
        Log.i(Constants.TAG,"加载麦广广告...");

        if (sdk == null){
            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        if (!sdk.isAdShow()){
            Log.i(Constants.TAG,"openGMAD O=0");
            listener.onMiiNoAD(2000);
            return;
        }
        MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,2,listener);
        mhttpRequest.fetchMGAD(false);
    }
    private void openGDTAD(final boolean shouldReturn){
        Log.i(Constants.TAG,"加载广点通广告...");
        if (sdk == null){
            sdk = CommonUtils.readParcel(mContext, MConstant.CONFIG_FILE_NAME);
        }
        new SplashAD(mActivity, adContainer, skipContainer, Constants.APPID, Constants.SplashPosID, new SplashADListener() {
            @Override
            public void onADDismissed() {

                listener.onMiiADDismissed();
            }

            @Override
            public void onNoAD(int i) {
                if (!shouldReturn){

                    if (sdk.isAdShow()){
                        MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,2,listener);
                        mhttpRequest.fetchMGAD1(true);
                    }
                    else {
                        Log.i(Constants.TAG,"openGDTAD...o=0");
                        listener.onMiiNoAD(2000);
                    }

                    return;
                }
                listener.onMiiNoAD(i);
            }

            @Override
            public void onADPresent() {

                listener.onMiiADPresent();
            }

            @Override
            public void onADClicked() {

                listener.onMiiADClicked();
            }

            @Override
            public void onADTick(long l) {

                listener.onMiiADTick(l);
            }
        }, 0);
    }





}
