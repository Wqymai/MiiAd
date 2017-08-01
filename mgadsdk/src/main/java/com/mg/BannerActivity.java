package com.mg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mg.banner.MgBannerAD;
import com.mg.interf.MiiADListener;

/**
 * Created by wuqiyan on 17/7/7.
 */

public class BannerActivity extends Activity {

    ViewGroup bannerContainer;
    Button refresh;
    Button close;
    MgBannerAD bannerAD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_layout);
        bannerContainer = (ViewGroup) this.findViewById(R.id.bannerContainer);

        bannerAD = new MgBannerAD(BannerActivity.this, bannerContainer,Contants.APPID,Contants.BID, new MiiADListener() {

            @Override
            public void onMiiNoAD(int errCode) {
                Log.i("ad_demo","错误码="+errCode);
            }

            @Override
            public void onMiiADDismissed() {
                Log.i("ad_demo","onMiiADDismissed=");
            }

            @Override
            public void onMiiADPresent() {
                Log.i("ad_demo","onMiiADPresent");
                bannerContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMiiADClicked() {
                Log.i("ad_demo","onMiiADClicked");
            }

            @Override
            public void onMiiADTouched() {

            }

        });

        refresh= (Button) findViewById(R.id.refreshBanner);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerAD.loadBannerAD();

            }
        });
        close= (Button) findViewById(R.id.closeBanner);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerAD.recycle();
                bannerContainer.removeAllViews();
                bannerContainer.setVisibility(View.GONE);
            }
        });
    }
}
