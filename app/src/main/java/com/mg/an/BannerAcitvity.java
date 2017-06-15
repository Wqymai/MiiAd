package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.android.others.R;


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
    }
}
