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
//import com.mg.comm.MConstant;
//import com.mg.interf.AbsADData;
//import com.mg.interf.MiiADListener;
//import com.mg.interf.MiiNativeListener;
//import com.mg.interstitial.MiiFixedInterstitialAD;
//import com.mg.nativ.MiiNativeAD;
//import com.mg.others.utils.LogUtils;
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
//    Button openDialogAct;
//    Button openDialogAct2;
//    private AbsADData adDataRef;
//
////    @Override
////    public boolean onTouchEvent(MotionEvent event) {
////        switch (event.getAction()){
////            case ACTION_DOWN:
////                LogUtils.i(MConstant.TAG,"DOWN X="+event.getRawX()+"   Y="+event.getRawY());
////                break;
////            case ACTION_UP:
////                LogUtils.i(MConstant.TAG,"UP X="+event.getRawX()+"   Y="+event.getRawY());
////                break;
////        }
////
////        return super.onTouchEvent(event);
////    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
////        Log.i("TAG", MiiLocalStrEncrypt.enCodeStringToString("{\"a\":\"1101152570\"," +
////                "\"s\":\"8863364436303842593\",\"b\":\"9079537218417626401\"," +
////                "\"i\":\"8575134060152130849\"}", LocalKeyConstants.LOCAL_GDT));
//        //测试的黑白名单
//        //QzJEODg0MUQxODRBMjk5QjNBNUFGNjhDNjZBRThFMTkyOTgzQjBGRUEyRjg4OTQ5RDQ3QjAzMDA2RDA4RjA3MzE2M0ExREVGRTY3Q0UwRDc4QzNFM0M0Njk4REYyMjFEMUM0QjY5Q0I0QjhBOTNGNjcwOTI2MzUyOUU0MkQ0N0Y1NTlCNDU2MDNEQzQzMERGNEY4QTIwRTQ5NEE4MERBN0E4RjBDNjczQUQ5NUQ1RDdGRkQwODlDMUFGMjA4RTc2MjU3RTVDRkFBMEZDNjcxQUVFOUY0NEI1RUJGMTQ5RDU=
//
//
//
////        Log.i("TAG", MiiLocalStrEncrypt.enCodeStringToString("{\"a\":\"1105667610\"," +
////                "\"s\":\"3020822156957219\",\"b\":\"\"," +
////                "\"i\":\"\"}", LocalKeyConstants.LOCAL_GDT));
//
//        //正式的黑白名单
//        //QUUwMjQ4OEI4ODY3OUM0MUExODYxNkNBOEREQjcwNEU3RkMxODcwMUZBQjVBQjMxOEM2OEU2NjA1QkY2NTYwODhGN0M3MUEwMDY5MUQwRTI4RTYxNTg0N0VCRkE1Nzk1QUVDRTAzNDdGNDVCODA0RDdGNUM3MDZBNjhGMTU2QTk=
////        Log.i("wqy", MiiLocalStrEncrypt.deCodeStringToString("QUUwMjQ4OEI4ODY3OUM0MUExODYxNkNBOEREQjcwNEU3RkMxODcwMUZBQjVBQjMxOEM2OEU2NjA1QkY2NTYwODhGN0M3MUEwMDY5MUQwRTI4RTYxNTg0N0VCRkE1Nzk1QUVDRTAzNDdGNDVCODA0RDdGNUM3MDZBNjhGMTU2QTk=", LocalKeyConstants.LOCAL_GDT));
//
////        Log.i("TAG","a="+MiiLocalStrEncrypt.enCodeStringToString("1101152570", LocalKeyConstants.LOCAL_GDT));
////        Log.i("TAG", "s="+MiiLocalStrEncrypt.enCodeStringToString("8863364436303842593",LocalKeyConstants.LOCAL_GDT));
////        Log.i("TAG", "i="+MiiLocalStrEncrypt.enCodeStringToString("8575134060152130849",LocalKeyConstants.LOCAL_GDT));
////        Log.i("TAG", "i="+MiiLocalStrEncrypt.enCodeStringToString("9079537218417626401",LocalKeyConstants.LOCAL_GDT));
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
//        //固定插屏
//        openInterstitial1= (Button) findViewById(R.id.open_interstitial_ad1);
//        openInterstitial1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MiiFixedInterstitialAD(MainActivity.this,false, new MiiADListener() {
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
//        //固定插屏
//        openInterstitial2 = (Button) findViewById(R.id.open_interstitial_ad2);
//        openInterstitial2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MiiFixedInterstitialAD(MainActivity.this,true, new MiiADListener() {
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
//        //banner
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
////        //自由插屏
////        openNativeInterstitial= (Button) findViewById(R.id.open_nativeinterstitial);
////        openNativeInterstitial.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////               startActivity(new Intent(MainActivity.this, AutoInterActivity.class));
////            }
////        });
//
//
//
//
//        //自由插屏
//        openDialogAct = (Button) findViewById(R.id.open_dialogAct);
//        openDialogAct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,DialogActivity.class));
//            }
//        });
//
//
//
//
//
//
//        //原生
//        new MiiNativeAD(this, new MiiNativeListener() {
//            @Override
//            public void onADLoaded(AbsADData dataRef) {
//                if (dataRef != null){
//                    LogUtils.i(MConstant.TAG,"原生广告加载成功");
//                    openDialogAct2.setEnabled(true);
//                    adDataRef = dataRef;
//                }
//            }
//
////            @Override
////            public void onADLoaded(MiiNativeADDataRef dataRef) {
////                if (dataRef != null){
////                    LogUtils.i(MConstant.TAG,"原生广告加载成功");
////                    openDialogAct2.setEnabled(true);
////                    adDataRef = dataRef;
////                }
////            }
//
//            @Override
//            public void onMiiNoAD(int errCode) {
//                LogUtils.i(MConstant.TAG,"原生广告加载失败 "+errCode);
//            }
//        });
//        openDialogAct2 = (Button) findViewById(R.id.open_dialogAct2);
//        openDialogAct2.setEnabled(false);
//        openDialogAct2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(MainActivity.this,MNativeActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("AdData", adDataRef);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//}
