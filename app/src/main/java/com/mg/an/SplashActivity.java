package com.mg.an;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.others.R;
import com.mg.comm.MConstant;
import com.mg.splash.MiiSplashAD;
import com.mg.comm.MiiADListener;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class    SplashActivity extends Activity implements MiiADListener {

    private MiiSplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private ImageView splashHolder;
    private static final String SKIP_TEXT = "点击跳过 %d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);
        fetchAD(this, container, skipView, this);
    }

    private void fetchAD(SplashActivity mainActivity, ViewGroup container, TextView skipView,
                               MiiADListener listener) {
        splashAD=new MiiSplashAD(mainActivity,container,skipView,"","",listener);
    }

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
}
