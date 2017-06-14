package com.mg.mv4;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by wuqiyan on 17/3/24.
 */

public class EnvironmentCompat {
    private static final String TAG = "EnvironmentCompat";

    /**
     * Unknown storage state, such as when a path isn't backed by known storage
     * media.
     *
     * @see #getStorageState(File)
     */
    public static final String MEDIA_UNKNOWN = "unknown";

    /**
     * Returns the current state of the storage device that provides the given
     * path.
     *
     * @return one of {@link #MEDIA_UNKNOWN}, {@link Environment#MEDIA_REMOVED},
     *         {@link Environment#MEDIA_UNMOUNTED},
     *         {@link Environment#MEDIA_CHECKING},
     *         {@link Environment#MEDIA_NOFS},
     *         {@link Environment#MEDIA_MOUNTED},
     *         {@link Environment#MEDIA_MOUNTED_READ_ONLY},
     *         {@link Environment#MEDIA_SHARED},
     *         {@link Environment#MEDIA_BAD_REMOVAL}, or
     *         {@link Environment#MEDIA_UNMOUNTABLE}.
     */
    public static String getStorageState(File path) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return EnvironmentCompatKitKat.getStorageState(path);
        }

        try {
            final String canonicalPath = path.getCanonicalPath();
            final String canonicalExternal = Environment.getExternalStorageDirectory()
                    .getCanonicalPath();

            if (canonicalPath.startsWith(canonicalExternal)) {
                return Environment.getExternalStorageState();
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to resolve canonical path: " + e);
        }

        return MEDIA_UNKNOWN;
    }
}
