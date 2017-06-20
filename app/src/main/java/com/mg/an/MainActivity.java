//package com.mg.an;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.android.others.R;
//import com.mg.Interstitial.MiiInterstitialAD;
//import com.mg.comm.MiiADListener;
//
///**
// * Created by wuqiyan on 17/6/9.
// */
//
//public class MainActivity extends Activity {
//    Button openSplash;
//    Button openInterstitial1;
//    Button openInterstitial2;
//    Button openPopu;
//    Button openNativeInterstitial;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//
//        openSplash= (Button) findViewById(R.id.open_splash_ad);
//
//        openSplash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,SplashActivity.class));
//
//            }
//        });
//
//        openInterstitial1= (Button) findViewById(R.id.open_interstitial_ad1);
//        openInterstitial1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MiiInterstitialAD(MainActivity.this,Constants.APPID,Constants.InterteristalPosID,false, new MiiADListener() {
//                    @Override
//                    public void onMiiNoAD(int errCode) {
//                        Log.i(Constants.TAG,"onMiiNoAD "+errCode);
//                    }
//
//                    @Override
//                    public void onMiiADDismissed() {
//                        Log.i(Constants.TAG,"onMiiADDismissed");
//                    }
//
//                    @Override
//                    public void onMiiADPresent() {
//                        Log.i(Constants.TAG,"onMiiADPresent");
//                    }
//
//                    @Override
//                    public void onMiiADClicked() {
//                        Log.i(Constants.TAG,"onMiiADClicked");
//                    }
//
//                    @Override
//                    public void onMiiADTick(long millisUntilFinished) {
//
//                    }
//            });
//            }
//        });
//
//        openInterstitial2 = (Button) findViewById(R.id.open_interstitial_ad2);
//        openInterstitial2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MiiInterstitialAD(MainActivity.this,Constants.APPID,Constants.InterteristalPosID,true, new MiiADListener() {
//                    @Override
//                    public void onMiiNoAD(int errCode) {
//                        Log.i(Constants.TAG,"onMiiNoAD 2"+errCode);
//                    }
//
//                    @Override
//                    public void onMiiADDismissed() {
//                        Log.i(Constants.TAG,"onMiiADDismissed 2");
//                    }
//
//                    @Override
//                    public void onMiiADPresent() {
//                        Log.i(Constants.TAG,"onMiiADPresent 2");
//                    }
//
//                    @Override
//                    public void onMiiADClicked() {
//                        Log.i(Constants.TAG,"onMiiADClicked 2");
//                    }
//
//                    @Override
//                    public void onMiiADTick(long millisUntilFinished) {
//
//                    }
//                });
//            }
//        });
//
//        openPopu= (Button) findViewById(R.id.open_banner);
//        openPopu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(MainActivity.this,BannerAcitvity.class));
//
//            }
//        });
//
//        openNativeInterstitial= (Button) findViewById(R.id.open_nativeinterstitial);
//        openNativeInterstitial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               startActivity(new Intent(MainActivity.this,NativeActivity.class));
//            }
//        });
//
//
//
//
//    }
//
//
//}
