package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.android.others.R;
import com.mg.banner.MiiBannerAD;
import com.mg.comm.MConstant;
import com.mg.comm.MiiADListener;
import com.mg.others.utils.LogUtils;


/**
 * Created by wuqiyan on 17/6/15.
 */

public class BannerAcitvity extends Activity {
    ViewGroup bannerContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bannerContainer = (ViewGroup) this.findViewById(R.id.bannerContainer);
//        bannerContainer.setVisibility(View.GONE);
        new MiiBannerAD(this, bannerContainer, Constants.APPID, Constants.BannerPosID, new MiiADListener() {


            @Override
            public void onMiiNoAD(int errCode) {
                LogUtils.i(MConstant.TAG,"错误码="+errCode);
            }

            @Override
            public void onMiiADDismissed() {
                LogUtils.i(MConstant.TAG,"onMiiADDismissed=");
            }

            @Override
            public void onMiiADPresent() {
                LogUtils.i(MConstant.TAG,"onMiiADPresent");
//                bannerContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMiiADClicked() {
                LogUtils.i(MConstant.TAG,"onMiiADClicked");
            }

            @Override
            public void onMiiADTick(long millisUntilFinished) {

            }
        });
    }
}
