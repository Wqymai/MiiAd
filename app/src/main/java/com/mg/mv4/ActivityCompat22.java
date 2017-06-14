package com.mg.mv4;

import android.app.Activity;
import android.net.Uri;

/**
 * Created by wuqiyan on 17/3/24.
 */

 class ActivityCompat22 {
    public static Uri getReferrer(Activity activity) {
        return activity.getReferrer();
    }
}
