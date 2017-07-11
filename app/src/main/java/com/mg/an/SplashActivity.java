//package com.mg.an;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.android.others.R;
//import com.mg.comm.MConstant;
//import com.mg.interf.MiiADListener;
//import com.mg.others.utils.LogUtils;
//import com.mg.splash.MiiSplashAD;
//
///**
// * Created by wuqiyan on 17/6/9.
// */
//
//public class  SplashActivity extends Activity implements MiiADListener {
//
//    private MiiSplashAD splashAD;
//    private ViewGroup container;
//    private TextView skipView;
//    private ImageView splashHolder;
//    private static final String SKIP_TEXT = "点击跳过 %d";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        container = (ViewGroup) this.findViewById(R.id.splash_container);
//        skipView = (TextView) findViewById(R.id.skip_view);
//        splashHolder = (ImageView) findViewById(R.id.splash_holder);
//        fetchAD(this, container, skipView, this);
//    }
//
//    private void fetchAD(SplashActivity mainActivity, ViewGroup container, TextView skipView,
//                               MiiADListener listener) {
//        splashAD=new MiiSplashAD(mainActivity,container,skipView,MConstant.APPID,listener);
//    }
//
//    @Override
//    public void onMiiNoAD(int errCode) {
//        LogUtils.i(MConstant.TAG, "SplashNoAD "+errCode);
//    }
//
//    @Override
//    public void onMiiADDismissed() {
//        LogUtils.i(MConstant.TAG, "SplashADDismissed");
//        finish();
//    }
//
//    @Override
//    public void onMiiADPresent() {
//        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
//    }
//
//    @Override
//    public void onMiiADClicked() {
//        LogUtils.i(MConstant.TAG, "SplashADClicked");
//    }
//
//    @Override
//    public void onMiiADTouched() {
//
//    }
//
//    @Override
//    public void onMiiADTick(long millisUntilFinished) {
//        LogUtils.i(MConstant.TAG, "SplashADTick " + millisUntilFinished+ "ms");
//        skipView.setText(String.format(SKIP_TEXT, (Math.round(millisUntilFinished / 1000f))));
//    }
//}
