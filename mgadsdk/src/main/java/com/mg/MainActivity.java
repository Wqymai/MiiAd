package com.mg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mg.interf.MiiADListener;
import com.mg.interf.MiiNativeADDataRef;
import com.mg.interf.MiiNativeListener;
import com.mg.interstitial.MgFixedInterstitialAD;
import com.mg.nativ.MgNativeAD;


public class MainActivity extends Activity {

    private Button openSplash;
    private Button openInter_noshade;
    private Button openInter_shade;
    private Button openNative;
    private Button openBanner;
    private Button openAutoInter;


    private MiiNativeADDataRef adDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openSplash= (Button) findViewById(R.id.open_splash_ad);

        openSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
            }
        });

        openInter_noshade = (Button) findViewById(R.id.open_interstitial_noshade);
        openInter_noshade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MgFixedInterstitialAD(MainActivity.this, false,Contants.APPID, new MiiADListener() {
                    @Override
                    public void onMiiADDismissed() {

                    }

                    @Override
                    public void onMiiADPresent() {

                    }

                    @Override
                    public void onMiiADClicked() {

                    }

                    @Override
                    public void onMiiADTouched() {

                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onMiiNoAD(int errCode) {

                    }
                });
            }
        });

        openInter_shade = (Button) findViewById(R.id.open_interstitial_shade);
        openInter_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MgFixedInterstitialAD(MainActivity.this, true, Contants.APPID,new MiiADListener() {
                    @Override
                    public void onMiiADDismissed() {

                    }

                    @Override
                    public void onMiiADPresent() {

                    }

                    @Override
                    public void onMiiADClicked() {

                    }

                    @Override
                    public void onMiiADTouched() {

                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onMiiNoAD(int errCode) {

                    }
                });
            }
        });


        openNative = (Button) findViewById(R.id.open_native);
//        原生广告
        new MgNativeAD(this,Contants.APPID, new MiiNativeListener() {
            @Override
            public void onADLoaded(MiiNativeADDataRef dataRef) {
                if (dataRef != null){
                    openNative.setEnabled(true);
                    adDataRef = dataRef;
                }
            }



            @Override
            public void onMiiNoAD(int errCode) {
                Log.i("ad_demo","原生广告加载失败 "+errCode);
            }
        });
        openNative.setEnabled(false);
        openNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,MNativeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AdData", adDataRef);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        openBanner = (Button) findViewById(R.id.open_banner);
        openBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BannerActivity.class));
            }
        });

        openAutoInter = (Button) findViewById(R.id.open_autoInter);
        openAutoInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AutoInterActivity.class));
            }
        });
    }




}
