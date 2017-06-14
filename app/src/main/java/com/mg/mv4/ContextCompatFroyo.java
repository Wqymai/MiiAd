package com.mg.mv4;

import android.content.Context;

import java.io.File;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ContextCompatFroyo {
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    public static File getExternalFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type);
    }
}
