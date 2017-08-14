package com.mg.interf;

/**
 * Created by wuqiyan on 17/7/21.
 */

public interface MiiADListener extends MiiAbsADListener {
    void onMiiADDismissed();
    void onMiiADPresent();
    void onMiiADClicked();
    void onMiiADTouched();
    void onMiiADTick(long millisUntilFinished);
}
