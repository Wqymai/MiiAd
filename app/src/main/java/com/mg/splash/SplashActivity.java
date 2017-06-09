//package com.mg.splash;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//
//
///**
// * Created by wuqiyan on 17/6/8.
// */
//
//public class SplashActivity extends Activity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        RelativeLayout.LayoutParams pParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        RelativeLayout  pLayout=new RelativeLayout(this);
//        pLayout.setLayoutParams(pParams);
//        pLayout.setBackgroundColor(Color.BLUE);
//        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        ImageView imageView=new ImageView(this);
//        imageView.setLayoutParams(pParams);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        pLayout.addView(imageView);
//
//        setContentView(pLayout,lp);
//
//    }
//
//
//}
