package com.mg.an;

import android.app.Activity;
import android.os.Bundle;

import android.view.ViewGroup;

import com.mg.Interstitial.MiiInterstitialAD;
import com.mg.comm.MiiADListener;
import com.mg.splash.MiiSplashAD;



/**
 * Created by wuqiyan on 17/6/14.
 */

public class OtherActivity extends Activity {
    private ViewGroup container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MiiSplashAD(this,null, null, "", "", new MiiADListener() {
            @Override
            public void onMiiNoAD(int errCode) {

            }

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
            public void onMiiADTick(long millisUntilFinished) {

            }
        });

        new MiiInterstitialAD(OtherActivity.this,"","", new MiiADListener() {
                    @Override
                    public void onMiiNoAD(int errCode) {

                    }

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
                    public void onMiiADTick(long millisUntilFinished) {

                    }
            });

    }
}