//package com.mg.others;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//
//import com.android.others.R;
//import com.mg.others.utils.AppManager;
//import com.qq.e.ads.banner.ADSize;
//import com.qq.e.ads.banner.AbstractBannerADListener;
//import com.qq.e.ads.banner.BannerView;
//
//
///**
// * Created by wuqiyan on 17/6/7.
// */
//
//public class FrameActivity extends Activity {
//    ViewGroup bannerContainer;
//    BannerView bv;
//    Button btn;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppManager.getAppManager().addActivity(this);
////           AppManager.getAppManager().finishActivity("com.android.others");
//
////        try{
////            Runtime runtime=Runtime.getRuntime();
////            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
////        }catch(IOException e){
////            Log.e("Exception when doBack", e.toString());
////        }
//
//
////        try {
////            AppManager.getAppManager().finishActivity(Class.forName("SplashActivity"));
////        } catch (ClassNotFoundException e) {
////            e.printStackTrace();
////        }
//
//        Window window=getWindow();
//        window.setGravity(Gravity.LEFT | Gravity.TOP);
//        WindowManager.LayoutParams params=window.getAttributes();
//        WindowManager wm = this.getWindowManager();
//        params.x=0;
//        params.y=0;
//        params.height=200;
//        params.width=wm.getDefaultDisplay().getWidth();
//        window.setAttributes(params);
//        setContentView(R.layout.adframe);
//        btn= (Button) findViewById(R.id.closeAD);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doCloseBanner();
////                finish();
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
////                        try{
////                            Runtime runtime=Runtime.getRuntime();
////                            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
////                        }catch(IOException e){
////                            Log.e("Exception when doBack", e.toString());
////                        }
////                       AppManager.getAppManager().finishAllActivity();
//                    }
//                },10);
//
////                AppManager.getAppManager().finishAllActivity();
//
//            }
//        });
//        bannerContainer = (ViewGroup) this.findViewById(R.id.bannerContainer);
//        this.initBanner();
//        this.bv.loadAD();
//    }
//    private void doCloseBanner() {
//        bannerContainer.removeAllViews();
//        if (bv != null) {
//            bv.destroy();
//            bv = null;
//        }
//    }
//    private void initBanner() {
//        this.bv = new BannerView(this, ADSize.BANNER, "1101152570", "9079537218417626401");
//        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
//        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
//        bv.setRefresh(30);
//        bv.setADListener(new AbstractBannerADListener() {
//
//            @Override
//            public void onNoAD(int arg0) {
//                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
//            }
//
//            @Override
//            public void onADReceiv() {
//                Log.i("AD_DEMO", "ONBannerReceive");
//            }
//        });
//        bannerContainer.addView(bv);
//    }
//
//}
