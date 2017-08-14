package com.mg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mg.dobber.MgDobberAD;
import com.mg.interf.MiiADListener;
import com.mg.interf.MiiNativeADDataRef;
import com.mg.interf.MiiNativeListener;
import com.mg.interstitial.MgInterstitialAD;
import com.mg.nativ.MgNativeAD;


public class MainActivity extends Activity {

    private Button openSplash;
    private Button openInter_noshade;
    private Button openInter_shade;
    private Button openNative;
    private Button openBanner;
    private Button showNative;
    private Button openDobber;
    private Button openNotify;


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
                new MgInterstitialAD(MainActivity.this, false,Contants.APPID,Contants.IID, new MiiADListener() {
                    @Override
                    public void onMiiADDismissed() {
                        Log.i("ad_demo", "插屏 ADDismissed");
                    }

                    @Override
                    public void onMiiADPresent() {
                        Log.i("ad_demo", "插屏 ADPresent");
                    }

                    @Override
                    public void onMiiADClicked() {
                        Log.i("ad_demo", "插屏 ADClicked");
                    }

                    @Override
                    public void onMiiADTouched() {

                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onMiiNoAD(int errCode) {
                        Log.i("ad_demo", "插屏 NoAD " + errCode);
                    }
                });
            }
        });

        openInter_shade = (Button) findViewById(R.id.open_interstitial_shade);
        openInter_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MgInterstitialAD(MainActivity.this, true, Contants.APPID,Contants.IID,new MiiADListener() {
                    @Override
                    public void onMiiADDismissed() {
                        Log.i("ad_demo", "插屏 ADDismissed");
                    }

                    @Override
                    public void onMiiADPresent() {
                        Log.i("ad_demo", "插屏 ADPresent");
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
                        Log.i("ad_demo", "插屏 NoAD " + errCode);
                    }
                });
            }
        });




        openNative = (Button) findViewById(R.id.open_native);
        openNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MgNativeAD(MainActivity.this, Contants.APPID, Contants.NID, new MiiNativeListener() {

                    @Override
                    public void onADLoaded(MiiNativeADDataRef dataRef) {
                        if (dataRef != null){
                            showNative.setEnabled(true);
                            adDataRef = dataRef;
                        }
                    }

                    @Override
                    public void onMiiNoAD(int errCode) {
                        Log.i("ad_demo","原生广告加载失败 "+errCode);
                    }
                });
            }
        });
        showNative = (Button) findViewById(R.id.show_native);
        showNative.setEnabled(false);
        showNative.setOnClickListener(new View.OnClickListener() {
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



        final MgDobberAD dobberAD = new MgDobberAD(this, Contants.APPID, Contants.BID, new MiiADListener() {
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
        openDobber = (Button) findViewById(R.id.open_dobber);
        openDobber .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobberAD.loadDobberAD();
            }
        });

        openNotify = (Button) findViewById(R.id.open_headup);
        openNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        });
    }




}
