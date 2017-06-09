package com.mg.splash;



/**
 * Created by wuqiyan on 17/6/9.
 */

public interface MiiSplashADListener {
    void onMiiNoAD(int errCode);
    void onMiiADDismissed();
    void onMiiADPresent();
    void onMiiADClicked();
    void onMiiADTick(long millisUntilFinished);
}
