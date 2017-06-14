package com.mg.mv4;

import android.os.Environment;

import java.io.File;

/**
 * Created by wuqiyan on 17/3/24.
 */

 class EnvironmentCompatKitKat {
    public static String getStorageState(File path) {
        return Environment.getStorageState(path);
    }
}
