package com.mg.comm;



/**
 * Created by wuqiyan on 17/6/9.
 */

public interface MiiADListener {
    void onMiiNoAD(int errCode);
    void onMiiADDismissed();
    void onMiiADPresent();
    void onMiiADClicked();
    void onMiiADTick(long millisUntilFinished);//插屏广告没有此回调
}
