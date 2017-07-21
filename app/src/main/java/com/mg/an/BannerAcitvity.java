package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.others.R;
import com.mg.banner.MiiBannerAD;
import com.mg.comm.MConstant;
import com.mg.interf.MiiADListener;
import com.mg.others.utils.LogUtils;


/**
 * Created by wuqiyan on 17/6/15.
 */

public class BannerAcitvity extends Activity {
    ViewGroup bannerContainer;
    Button refresh;
    Button close;
    MiiBannerAD bannerAD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bannerContainer = (ViewGroup) this.findViewById(R.id.bannerContainer);

        refresh= (Button) findViewById(R.id.refreshBanner);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bannerAD = new MiiBannerAD(BannerAcitvity.this, bannerContainer,MConstant.APPID,MConstant.BID,new MiiADListener() {

                    @Override
                    public void onMiiNoAD(int errCode) {
                        LogUtils.i(MConstant.TAG,"banner 错误码="+errCode);
                    }

                    @Override
                    public void onMiiADDismissed() {
                        LogUtils.i(MConstant.TAG,"onMiiADDismissed");
                    }

                    @Override
                    public void onMiiADPresent() {
                        LogUtils.i(MConstant.TAG,"onMiiADPresent");
                        bannerContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onMiiADClicked() {
                        LogUtils.i(MConstant.TAG,"onMiiADClicked");
                    }

                   @Override
                   public void onMiiADTouched() {

                   }

                   @Override
                    public void onMiiADTick(long millisUntilFinished) {

                    }
                });

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
