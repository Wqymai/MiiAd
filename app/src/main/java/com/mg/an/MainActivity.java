package com.mg.an;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.others.R;
import com.mg.Interstitial.MiiInterstitialAD;
import com.mg.interf.MiiADListener;
import com.mg.interf.MiiNativeADDataRef;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class MainActivity extends Activity {
    Button openSplash;
    Button openInterstitial1;
    Button openInterstitial2;
    Button openPopu;
    Button openNativeInterstitial;
    Button openDialogAct;
    Button openDialogAct2;
    private MiiNativeADDataRef adDataRef;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case ACTION_DOWN:
//                LogUtils.i(MConstant.TAG,"DOWN X="+event.getRawX()+"   Y="+event.getRawY());
//                break;
//            case ACTION_UP:
//                LogUtils.i(MConstant.TAG,"UP X="+event.getRawX()+"   Y="+event.getRawY());
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Log.i("TAG", MiiLocalStrEncrypt.enCodeStringToString("{\"a\":\"1101152570\"," +
//                "\"s\":\"8863364436303842593\",\"b\":\"9079537218417626401\"," +
//                "\"i\":\"8575134060152130849\"}", LocalKeyConstants.LOCAL_GDT));


//        Log.i("TAG",MiiLocalStrEncrypt.enCodeStringToString("1105667610",LocalKeyConstants.LOCAL_GDT));
//        Log.i("TAG",MiiLocalStrEncrypt.enCodeStringToString("3020822156957219",LocalKeyConstants.LOCAL_GDT));



        openSplash= (Button) findViewById(R.id.open_splash_ad);

        openSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SplashActivity.class));

            }
        });

        openInterstitial1= (Button) findViewById(R.id.open_interstitial_ad1);
        openInterstitial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MiiInterstitialAD(MainActivity.this,Constants.APPID,Constants.InterteristalPosID,false, new MiiADListener() {
                    @Override
                    public void onMiiNoAD(int errCode) {
                        Log.i(Constants.TAG,"onMiiNoAD "+errCode);
                    }

                    @Override
                    public void onMiiADDismissed() {
                        Log.i(Constants.TAG,"onMiiADDismissed");
                    }

                    @Override
                    public void onMiiADPresent() {
                        Log.i(Constants.TAG,"onMiiADPresent");
                    }

                    @Override
                    public void onMiiADClicked() {
                        Log.i(Constants.TAG,"onMiiADClicked");
                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }
            });
            }
        });

        openInterstitial2 = (Button) findViewById(R.id.open_interstitial_ad2);
        openInterstitial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MiiInterstitialAD(MainActivity.this,Constants.APPID,Constants.InterteristalPosID,true, new MiiADListener() {
                    @Override
                    public void onMiiNoAD(int errCode) {
                        Log.i(Constants.TAG,"onMiiNoAD 2"+errCode);
                    }

                    @Override
                    public void onMiiADDismissed() {
                        Log.i(Constants.TAG,"onMiiADDismissed 2");
                    }

                    @Override
                    public void onMiiADPresent() {
                        Log.i(Constants.TAG,"onMiiADPresent 2");
                    }

                    @Override
                    public void onMiiADClicked() {
                        Log.i(Constants.TAG,"onMiiADClicked 2");
                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }
                });
            }
        });

        openPopu= (Button) findViewById(R.id.open_banner);
        openPopu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,BannerAcitvity.class));

            }
        });

        openNativeInterstitial= (Button) findViewById(R.id.open_nativeinterstitial);
        openNativeInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,NativeActivity.class));
            }
        });




        openDialogAct = (Button) findViewById(R.id.open_dialogAct);
        openDialogAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DialogActivity.class));
            }
        });







//        new MiiNativeAD(this, new MiiNativeListener() {
//            @Override
//            public void onADLoaded(MiiNativeADDataRef dataRef) {
//                if (dataRef != null){
//                    LogUtils.i(MConstant.TAG,"原生广告加载成功");
//                    openDialogAct2.setEnabled(true);
//                    adDataRef = dataRef;
//                }
//            }
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
//                Intent intent=new Intent(MainActivity.this,DialogActivity2.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("AdData", adDataRef);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

    }

}
