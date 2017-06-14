package com.mg.mv4;

import android.app.Activity;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ActivityCompatApi23 {
    public interface RequestPermissionsRequestCodeValidator {
        public void validateRequestPermissionsRequestCode(int requestCode);
    }

    public static void requestPermissions(Activity activity, String[] permissions,
                                          int requestCode) {
        if (activity instanceof ActivityCompatApi23.RequestPermissionsRequestCodeValidator) {
            ((ActivityCompatApi23.RequestPermissionsRequestCodeValidator) activity)
                    .validateRequestPermissionsRequestCode(requestCode);
        }
        activity.requestPermissions(permissions, requestCode);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity,
                                                               String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }
}
