package com.mg.mv4;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ContextCompatApi21 {
    public static Drawable getDrawable(Context context, int id) {
        return context.getDrawable(id);
    }

    public static File getNoBackupFilesDir(Context context) {
        return context.getNoBackupFilesDir();
    }

    public static File getCodeCacheDir(Context context) {
        return context.getCodeCacheDir();
    }
}
