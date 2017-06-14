package com.mg.mv4;

import android.content.Context;

import java.io.File;

/**
 * Created by wuqiyan on 17/3/24.
 */

public class ContextCompatKitKat {
    public static File[] getExternalCacheDirs(Context context) {
        return context.getExternalCacheDirs();
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        return context.getExternalFilesDirs(type);
    }

    public static File[] getObbDirs(Context context) {
        return context.getObbDirs();
    }
}
