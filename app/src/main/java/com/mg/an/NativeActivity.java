package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.android.others.R;
import com.mg.Interstitial.MiiNativeInterstitialAD;
import com.mg.comm.MiiADListener;

/**
 * Created by wuqiyan on 17/6/19.
 */

public class NativeActivity extends Activity {
    ViewGroup viewGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativeinterstitial);
        viewGroup= (ViewGroup) findViewById(R.id.native_interstitial_adcontainer);
        new MiiNativeInterstitialAD(this, viewGroup, new MiiADListener() {
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
