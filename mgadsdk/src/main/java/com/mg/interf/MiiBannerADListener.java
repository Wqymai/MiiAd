package com.mg.interf;

/**
 * Created by wuqiyan on 17/8/2.
 */

public interface MiiBannerADListener extends MiiAbsADListener {
    void onMiiADDismissed();
    void onMiiADPresent();
    void onMiiADClicked();
    void onMiiADTouched();
}
