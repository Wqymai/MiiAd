package com.mg.nativ;

import android.view.View;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface MiiNativeADDataRef {
    String getImgUrl();
    int getADType(int adType);//1.html5的 2.非html5的
    void onClick(View view);
    void onExposured();
}
