package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.others.R;
import com.mg.comm.MConstant;
import com.mg.interf.MiiADListener;
import com.mg.interstitial.MiiAutoInterstitialAD;

/**
 * Created by wuqiyan on 17/6/19.
 */

public class AutoInterActivity extends Activity {
    ViewGroup viewGroup;
    Button btnOpen;
    Button btnClose;
    MiiAutoInterstitialAD miiAutoInterstitialAD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativeinterstitial);
        viewGroup= (ViewGroup) findViewById(R.id.native_interstitial_adcontainer);

        btnClose= (Button) findViewById(R.id.close_native);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miiAutoInterstitialAD.recycle();
                viewGroup.removeAllViews();
                viewGroup.setVisibility(View.GONE);
            }
        });


        btnOpen= (Button) findViewById(R.id.open_native);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miiAutoInterstitialAD =  new MiiAutoInterstitialAD(AutoInterActivity.this, viewGroup, MConstant.APPID,MConstant.LID, new MiiADListener() {
                    @Override
                    public void onMiiNoAD(int errCode) {

                    }

                    @Override
                    public void onMiiADDismissed() {
                        //不回调
                    }

                    @Override
                    public void onMiiADPresent() {
                        Toast.makeText(AutoInterActivity.this,"广告加载成功",Toast.LENGTH_SHORT).show();
                        viewGroup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onMiiADClicked() {

                    }

                    @Override
                    public void onMiiADTouched() {

                    }

                    @Override
                    public void onMiiADTick(long millisUntilFinished) {
                        //不回调
                    }
                });
            }
        });


    }
}
