package com.mg.mv4;

import android.content.Context;
import android.content.Intent;

import java.io.File;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ContextCompatHoneycomb {
    static void startActivities(Context context, Intent[] intents) {
        context.startActivities(intents);
    }

    public static File getObbDir(Context context) {
        return context.getObbDir();
    }
}
