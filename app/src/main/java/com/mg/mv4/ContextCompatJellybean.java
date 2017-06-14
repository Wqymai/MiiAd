package com.mg.mv4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by wuqiyan on 17/3/24.
 */

 class ContextCompatJellybean {
    public static void startActivities(Context context, Intent[] intents, Bundle options) {
        context.startActivities(intents, options);
    }
}
