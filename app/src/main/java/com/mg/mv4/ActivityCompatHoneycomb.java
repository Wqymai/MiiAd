package com.mg.mv4;

import android.app.Activity;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ActivityCompatHoneycomb {
    static void invalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
    }

    static void dump(Activity activity, String prefix, FileDescriptor fd,
                     PrintWriter writer, String[] args) {
        activity.dump(prefix, fd, writer, args);
    }
}
