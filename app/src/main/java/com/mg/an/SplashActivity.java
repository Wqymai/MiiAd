package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.others.R;
import com.mg.comm.MConstant;
import com.mg.interf.MiiADListener;
import com.mg.others.utils.LogUtils;
import com.mg.splash.MiiSplashAD;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class  SplashActivity extends Activity implements MiiADListener {

    private String TAG = "AD_DEMO";
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
        fetchAD(this, container, skipView,MConstant.APPID,MConstant.KID,this);
    }

    /**
     * @param activity  展示广告的activity
     * @param container 展示广告的容器
     * @param skipView  自定义跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制
     * @param appid     麦广广告后台提供
     * @param listener  广告回调
     */
    private void fetchAD(SplashActivity activity, ViewGroup container, View skipView,String appid,String lid,MiiADListener listener) {
        splashAD = new MiiSplashAD(activity,container,skipView,appid,lid,listener);
    }

    @Override
    public void onMiiNoAD(int errCode) {
        Log.i(TAG, "SplashNoAD "+errCode);
    }

    @Override
    public void onMiiADDismissed() {
        Log.i(TAG, "SplashADDismissed");
        finish();
    }

    @Override
    public void onMiiADPresent() {
        LogUtils.i(TAG, "SplashADPresent");
        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onMiiADClicked() {
        Log.i(TAG, "SplashADClicked");
    }

    @Override
    public void onMiiADTouched() {
        Log.i(TAG, "SplashADTouched");
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onMiiADTick(long millisUntilFinished) {
        Log.i(TAG, "SplashADTick " + millisUntilFinished+ "ms");
        skipView.setText(String.format(SKIP_TEXT, (Math.round(millisUntilFinished / 1000f))));
    }
}
