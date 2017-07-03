package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.others.R;
import com.mg.Interstitial.MiiAutoInterstitialAD;
import com.mg.interf.MiiADListener;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class DialogActivity extends Activity {
    TextView button;
    ViewGroup viewGroup;
    MiiAutoInterstitialAD miiAutoInterstitialAD;
    ImageView adHolder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        viewGroup = (ViewGroup) findViewById(R.id.adContainer);
        adHolder = (ImageView) findViewById(R.id.ad_holder);
        miiAutoInterstitialAD =  new MiiAutoInterstitialAD(DialogActivity.this, viewGroup, new MiiADListener() {
            @Override
            public void onMiiNoAD(int errCode) {

            }

            @Override
            public void onMiiADDismissed() {
                //不回调
            }

            @Override
            public void onMiiADPresent() {
                adHolder.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = viewGroup.getLayoutParams();
                params.height = 500;
                params.width = 500;
                viewGroup.setLayoutParams(params);



                Toast.makeText(DialogActivity.this,"广告加载成功",Toast.LENGTH_SHORT).show();
                viewGroup.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMiiADClicked() {

            }

            @Override
            public void onMiiADTick(long millisUntilFinished) {
                //不回调
            }
        });






        button= (TextView) findViewById(R.id.closeDialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
