package com.mg.mv4;

import android.content.Context;
import android.content.res.ColorStateList;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ContextCompatApi23 {
    public static ColorStateList getColorStateList(Context context, int id) {
        return context.getColorStateList(id);
    }

    public static int getColor(Context context, int id) {
        return context.getColor(id);
    }
}
