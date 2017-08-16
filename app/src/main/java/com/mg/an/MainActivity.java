//package com.mg.an;
//
//import android.app.Activity;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.mg.R;
//import com.mg.comm.MConstant;
//import com.mg.dobber.MiiDobberAD;
//import com.mg.gif.GifView;
//import com.mg.interf.MiiADListener;
//import com.mg.interf.MiiNativeADDataRef;
//import com.mg.interf.MiiNativeListener;
//import com.mg.interstitial.MiiInterstitialAD;
//import com.mg.nativ.MiiNativeAD;
//import com.mg.others.utils.LogUtils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
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
//    ImageView showIv;
//    Button openNative;
//    private MiiNativeADDataRef adDataRef;
//    Button openTuiaAd;
//    Button openBuoy;
//    GifView gifView ;
//    Button openNotify;
//    Button open_headup;
//    int ID = 0x123;
//    NotificationManager manager ;
//
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
//        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        hangup(getApplicationContext());
////        simpleNotify();
////        simpleNotify();
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
////        Log.i("TAG","真服域名="+ MiiLocalStrEncrypt.enCodeStringToString("http://sspapi.ilast.cc",
////                LocalKeyConstants.LOCAL_KEY_DOMAINS));
////
////        Log.i("TAG","测服域名="+ MiiLocalStrEncrypt.enCodeStringToString("http://adtestf.maimob.net:8082",
////                LocalKeyConstants.LOCAL_KEY_DOMAINS));
////        Log.i("TAG","ACTION="+MiiLocalStrEncrypt.enCodeStringToString("/v/hb",LocalKeyConstants.LOCAL_KEY_ACTIONS));
////        Log.i("TAG","ACTION="+ MiiLocalStrEncrypt.enCodeStringToString("/v/tra", LocalKeyConstants.LOCAL_KEY_ACTIONS));
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
//                new MiiInterstitialAD(MainActivity.this,false, MConstant.APPID,MConstant.IID, new MiiADListener() {
//                    @Override
//                    public void onMiiNoAD(int errCode) {
//                        Log.i(Constants.TAG,"固定插屏 noShade onMiiNoAD "+errCode);
//                    }
//
//                    @Override
//                    public void onMiiADDismissed() {
//                        Log.i(Constants.TAG,"固定插屏 noShade onMiiADDismissed");
//                    }
//
//                    @Override
//                    public void onMiiADPresent() {
//                        Log.i(Constants.TAG,"固定插屏 noShade onMiiADPresent");
//                    }
//
//                    @Override
//                    public void onMiiADClicked() {
//                        Log.i(Constants.TAG,"固定插屏 noShade onMiiADClicked");
//                    }
//
//                    @Override
//                    public void onMiiADTouched() {
//                        Log.i(Constants.TAG,"固定插屏 noShade onMiiADTouched");
//                    }
//
//                    @Override
//                    public void onMiiADTick(long millisUntilFinished) {
//
//                    }
//
//                });
//            }
//        });
//
//        //固定插屏
//        openInterstitial2 = (Button) findViewById(R.id.open_interstitial_ad2);
//        openInterstitial2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MiiInterstitialAD(MainActivity.this,true,MConstant.APPID,MConstant.IID, new MiiADListener() {
//                    @Override
//                    public void onMiiNoAD(int errCode) {
//                        Log.i(Constants.TAG,"固定插屏 Shade onMiiNoAD "+errCode);
//                    }
//
//                    @Override
//                    public void onMiiADDismissed() {
//                        Log.i(Constants.TAG,"固定插屏 Shade onMiiADDismissed ");
//                    }
//
//                    @Override
//                    public void onMiiADPresent() {
//                        Log.i(Constants.TAG,"固定插屏 Shade onMiiADPresent ");
//                    }
//
//                    @Override
//                    public void onMiiADClicked() {
//                        Log.i(Constants.TAG,"固定插屏 Shade onMiiADClicked ");
//                    }
//
//                    @Override
//                    public void onMiiADTouched() {
//                        Log.i(Constants.TAG,"固定插屏 Shade onMiiADTouched ");
//                    }
//
//                    @Override
//                    public void onMiiADTick(long millisUntilFinished) {
//
//                    }
//
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
////        //自由插屏1
////        openNativeInterstitial= (Button) findViewById(R.id.open_nativeinterstitial);
////        openNativeInterstitial.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////               startActivity(new Intent(MainActivity.this, AutoInterActivity.class));
////            }
////        });
////
////
////
////
////        //自由插屏2
////        openDialogAct = (Button) findViewById(R.id.open_dialogAct);
////        openDialogAct.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this,DialogActivity.class));
////            }
////        });
//
//        openNative = (Button) findViewById(R.id.open_native);
//        openNative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //原生
//                new MiiNativeAD(MainActivity.this,MConstant.APPID,MConstant.NID, new MiiNativeListener() {
//                    @Override
//                    public void onADLoaded(MiiNativeADDataRef dataRef) {
//                        if (dataRef != null){
//                            LogUtils.i(MConstant.TAG,"原生广告加载成功");
//                            openDialogAct2.setEnabled(true);
//                            adDataRef = dataRef;
//                        }
//                    }
//
//
//                    @Override
//                    public void onMiiNoAD(int errCode) {
//                        LogUtils.i(MConstant.TAG,"原生广告加载失败 "+errCode);
//                    }
//                });
//            }
//        });
//
//
//
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
////        openTuiaAd = (Button) findViewById(R.id.open_tuia_customad);
////        openTuiaAd.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this,TuiaCustomActivity.class));
////            }
////        });
//
//
//
////        String url = "openapp.jdmobile://virtual?params=%7B%22des%22%3A%22getCoupon%22%2C%22m%5Fparam%22%3A%7B%22jdv%22%3A%22238571484%7Cxunfei%7Ct%5F301476161%5F207944%5F1712%7Ctuiguang%7C%5F2%5Fapp%5F0%5F1f3de5d787da4ca38cc9c5bf5bc577f3%2Dp%5F1712%22%7D%2C%22url%22%3A%22http%3A%5C%2F%5C%2Fccc%2Ex%2Ejd%2Ecom%5C%2Fdsp%5C%2Fnc%3Fext%3DY2xpY2sudW5pb24uamQuY29tL0pkQ2xpY2svP3VuaW9uSWQ9MzAxNDc2MTYxJnNpdGVpZD0yMDc5NDRfMTcxMiZ1dG10ZXJtX2V4dD1wXzE3MTImdG89aHR0cDovL3JlLm0uamQuY29tL2xpc3QvaXRlbS8xMTk3Ny0xNzk2MDQ4Lmh0bWw%5FcmVfZGNwPTQ1bFJUYWdfWDRrM2hQMFBDc1dBS1BLemtwZUVPdWk1aV8wNldXSGpXS3NmVDNaU3JldjZObmthd3ZMQkRtakpyM2l2dHpjNGJhUmZhYloxeHpOUHBCdWpsQUMyVWQwU1JrV0tRd1F5TXZGNF81OTZ4SVhETnFDbGRtSEJCVWJpLUxMWlBDT2Y4d3pX%26log%3DpcPUpdkdJVkdIsvCYwuMNRXHvw0lqr5e%5FZiwaFWnRpLnT4BgD%2DUP3WvS2uf8gx1JzQ4XCU8BQS01n0Qn%5FRiv0tuc5OVGbUp46X8ZIgxbqulKXT0k%5FtCQdwaOj53B8%5Fb5obfGslUzV5UGEmgdelNjqUvTRfVt%2DZiPgDKE7f8a9RgYgUyjxXqxIHdj6Ldcx9CuFDCQ62CXJaihyZcNMks2BVX1FKcUHkWWlBNkpNtAEscgF6sv3TK5hlULO982IEb1ZZzdGNdTSajQ%2DFCJ%5FVPfjuIMJ80iepp%2DyAyOaErGblPS4lyrMnwoOYvajrjBMubWKDg0gukaQBlDFqHn%2Dk5zn2MWqIV41Xv31tp51gZb%2DrTKZjOr4GoS56jNmv6Hq10R9GH2%5FjX3w0wZV1r4qAMiwbC7vzcdo%2Dbdcusoyvmp2dTwVLMSbfKCoCwYiwpFGVZEfvEIkZOGk8RSV0V5Pu4%2DHIF5f1itKb7QqYcrUD%2DvoEo%26v%3D404%26SE%3D1%22%2C%22SE%22%3A%22ADC%5FY6SIXa18K2tkEf3rTu3WtF6IGQzLuCviuZPJDbwzQnzR6WO8Lj30BRIA6fPhZOp2qTAjzNPR35nB4C3%5C%2FdNo4VMlqzdJzXzx19JJqJRbl8Sx4TYVqJcHZQoiVs2IGl4Suap4kuQYxcWkAypugEMAovQ%3D%3D%22%2C%22category%22%3A%22jump%22%2C%22action%22%3A%22to%22%2C%22sourceType%22%3A%22adx%22%2C%22sourceValue%22%3A%22xunfei%5F52%22%7D";
////        Intent intent1 = new Intent();
////        intent1.setAction("android.intent.action.VIEW");
////        Uri uri = Uri.parse(url);
////        intent1.setData(uri);
////        startActivity(intent1);
//
//
////        showIv = (ImageView) findViewById(R.id.show_iv);
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////               final Bitmap bitmap = getHttpBitmap("http://ubmcmm.baidustatic.com/media/v1/0f000Fn8ezb2RwBL6bLud6.webp");
////               runOnUiThread(new Runnable() {
////                   @Override
////                   public void run() {
////                       showIv.setImageBitmap(bitmap);
////                   }
////               });
////            }
////        }).start();
//
////        openBrowser(this,"http://m.baidu.com/cpro.php?TjCK00jP7BRjGSbTEaUflixmcuORZ3C-9oDXbA9cVlbIJJkTxTFF203u-HjGa-iPYGHFFw0gb03UqlCle8JlvnwlUQ1VTid8ws2Ju9E4UsSs_WmBjbJecF-wG3bnodRnHMgSH23igeAECaWX_t9SMSrBIxoKRsc5b2k4gV_UgPPP5pKFds.DR_iuTAMZ4RTDH5-8vc6hes_YD_gnEeTjngy5ZGmeQeoSkM5MulyxZug8iWzzUPvH_3eV-xgKfYt_U_DY2ycYlTqerQKM3P2knnqQ5W_oLF4mTTzs1f_I-MZk3J0.IgF_5y9YIZ0lQztYmhwBPADVQyDvmHT3nHN-QhPEUit0IAYqn0KYIHYYPH01n1ms0Zwb5H00Ivsqn6KWUMw85yDvmHT3nHN-gvPsT6KWpAdb5HD0TvPCUyfqnfK1TjYYnfK-XZfqn0K9uAP_mgP15HDk0ZPbpdqv5Hf0ULnqn0KBTWYs0ZIGuZwC5Hnvn0KCuy-MpZfqn10s0ZIG5HD0XA-s5fK1uykh5H00TvwogLu-TMPGUv3qPi3sQWc0Uh71IZ0qn0KzT1Ys0APh5H00mLwV5yF9pywdfLN1IDG1Uv30Uh7YIHY40A-Ypyfqn0KGIA-8uhqGujYs0AIspyfqn0Kzuyuspyfqn0KWTZFEpyfq0APEUgK_uHYs0A7bgLnqPvwhnHDzPjFBuWDsrjIBrfK_mysq0A7Ypyfq0A7YIZbq0ZI1Thnqn0K9uNqYmgcq0A-1gvVYmHY0Tvk1Thnqn0K1UAPo5H00Tvkopyfqn0KBIywMugwxug9spyfqn0K9pg0qUhRvIDfzyD-Afhm0Iy-s5NwGgL7vNNuWgdG80APC5H00IA7z5Hc0TvN_UANzgv-b5Hc0pgPxmgKs5HD0mgKsgv-b5HcLrHR4rHRYrjm0mLN1IjY0pgPxIv-zuyk-TLnqnfKLmgKxIZ-suHYk0ZK_5H00UynqmWFbPAFhnhD0UgmqnfK8IM0qna3snj0snj0sn0KMrHYk0AuGTMPYgLF-uv-EUWYz0ZP-mvq8u7qzuyIGUv3qrjcz0ZP1TjYz0ZP1Tjcqn0KYIZ0qnsKbpgPYTjYs0A7B5HD0myw35HD0TAuYTh7buHYzn0KsTLwzmyw-5HcsnjD0TA-b5H00ILKGujYs0A7bIZ-suHYs0ZPGThN8uANz5H00TLIGujYs0ZPYXgK-5H00mgKsgLPGujY0uZws5Hc0pgwV5H00UMwYTjY10A7bmWYs0APzm1YdnjRkns0");
//
//
//        openBuoy = (Button) findViewById(R.id.open_buoy);
//        final MiiDobberAD miiBuoyAD = new MiiDobberAD(MainActivity.this, MConstant.APPID, MConstant.BID, new MiiADListener() {
//
//
//            @Override
//            public void onMiiADDismissed() {
//
//            }
//
//            @Override
//            public void onMiiADPresent() {
//
//            }
//
//            @Override
//            public void onMiiADClicked() {
//
//            }
//
//            @Override
//            public void onMiiADTouched() {
//
//            }
//
//            @Override
//            public void onMiiADTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onMiiNoAD(int errCode) {
//                LogUtils.i(MConstant.TAG,"浮标广告 "+errCode);
//            }
//        });
//        openBuoy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                miiBuoyAD.loadDobberAD();
//            }
//        });
//
////        gifView = (GifView) findViewById(R.id.gif);
////
////        String path = Environment.getExternalStorageDirectory()+"/"+"hello.gif";
////        Log.i(MConstant.TAG,path);
////        try {
////            gifView.setGifImage(new FileInputStream(path));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//
//
//
////        openNotify = (Button) findViewById(R.id.open_notify);
////        openNotify.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////
////
////                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
////                String[] events = new String[10];
////
////                inboxStyle.setBigContentTitle("大视图内容");
////
////                for (int i=0; i < events.length; i++) {
////                    inboxStyle.addLine("hello"+i);
////                }
////
////
////                NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
////                pictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.banner));
////
////                //先设定RemoteViews
////                RemoteViews view_custom = new RemoteViews(getPackageName(), R.layout.notify_layout);
////
////
////                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
////                        builder
////
////                        .setAutoCancel(true)
////
////                        .setTicker("有通知")
//////                                .setSubText("setSubText")
////                        .setPriority(Notification.PRIORITY_MAX)
////                                .setVisibility(Notification.VISIBILITY_PUBLIC)
////
//////                        .setSmallIcon(R.mipmap.gdticon)
//////                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.gdticon))
////
////                        .setContentTitle("标题")
////
////                        .setContentText("@@@@这是内容@@@@")
//////                                .setStyle(pictureStyle)
////
////
////                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
////
////                        .setWhen(System.currentTimeMillis())
////
////                        .setContentIntent(null);
////
////
//////                builder.setContent(view_custom);
//////                builder.setCustomBigContentView(view_custom);
////
////                Notification notification = builder.build();
//////
//////                if(Build.VERSION.SDK_INT >= 16){
//////                    notification.bigContentView = view_custom;
//////                    notification.contentView = view_custom;
//////                }
////                manager.notify(ID, notification);
//////                simpleNotify();
////            }
////        });
//
////        LogUtils.i("youle",getStatusBarHeight(this)+"");
//
////        final MiiHeadupAD notifyAD =  new MiiHeadupAD(this,false, MConstant.APPID, MConstant.BID, new MiiADListener() {
////            @Override
////            public void onMiiADDismissed() {
////
////            }
////
////            @Override
////            public void onMiiADPresent() {
////
////            }
////
////            @Override
////            public void onMiiADClicked() {
////
////            }
////
////            @Override
////            public void onMiiADTouched() {
////
////            }
////
////            @Override
////            public void onMiiNoAD(int errCode) {
////                LogUtils.i(MConstant.TAG,"横幅广告 "+errCode);
////            }
////        });
//
//
//
//        open_headup = (Button) findViewById(R.id.open_headup);
//        open_headup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,MListActivity.class));
////                notifyAD.loadAD();
//            }
//        });
//    }
//
//
////    private void simpleNotify(){
////        //为了版本兼容  选择V7包下的NotificationCompat进行构造
////        Notification.Builder builder = new Notification.Builder(this);
////        //Ticker是状态栏显示的提示
////        builder.setTicker("简单Notification");
////        //第一行内容  通常作为通知栏标题
////        builder.setContentTitle("标题");
////        //第二行内容 通常是通知正文
////        builder.setContentText("通知内容");
////        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
////        builder.setSubText("这里显示的是通知第三行内容！");
////        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
////        //builder.setContentInfo("2");
////        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
////        builder.setNumber(2);
////        //可以点击通知栏的删除按钮删除
////        builder.setAutoCancel(true);
////        //系统状态栏显示的小图标
////        builder.setSmallIcon(R.mipmap.gdticon);
////        //下拉显示的大图标
////        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.gdticon));
////        Intent intent = new Intent(this,BannerAcitvity.class);
////        PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);
////        //点击跳转的intent
////        builder.setContentIntent(pIntent);
////        //通知默认的声音 震动 呼吸灯
////        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
////        Notification notification = builder.build();
////        manager.notify(ID,notification);
////    }
//
//
//    /**
//     * 横幅通知
//     *
//     * @param context
//     */
//    private void hangup(Context context) {
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//
//        builder.setSmallIcon(R.mipmap.gdticon);
//        builder.setContentTitle("My notification");
//        builder.setContentText("Hello World!");
//        // 设置通知的优先级
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        // 设置通知的提示音
//        builder.setSound(alarmSound);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(110, builder.build());
//    }
//
//
//
//    public static int getStatusBarHeight(Context context){
//        Class<?> c = null;
//        Object obj = null;
//        Field field = null;
//        int x = 0, statusBarHeight = 0;
//        try {
//            c = Class.forName("com.android.internal.R$dimen");
//            obj = c.newInstance();
//            field = c.getField("status_bar_height");
//            x = Integer.parseInt(field.get(obj).toString());
//            statusBarHeight = context.getResources().getDimensionPixelSize(x);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return statusBarHeight;
//    }
//    public static Bitmap getHttpBitmap(String url) {
//        URL myFileUrl = null;
//        Bitmap bitmap = null;
//        try {
//
//            myFileUrl = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//            conn.setConnectTimeout(0);
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    public static void openBrowser(Context mContext , String url) {
//        try {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            Uri content_url = Uri.parse(url);
//            intent.addCategory(Intent.CATEGORY_BROWSABLE);
//            intent.setData(content_url);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
