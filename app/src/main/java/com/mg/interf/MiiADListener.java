package com.mg.interf;


/**
 * Created by wuqiyan on 17/6/9.
 */

public interface MiiADListener extends MiiAbsADListener {
    void onMiiADDismissed();
    void onMiiADPresent();
    void onMiiADClicked();
    void onMiiADTick(long millisUntilFinished);
}
