package com.mg.others.layer;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wuqiyan on 17/4/20.
 */

public abstract class AdShowBase {
  abstract WindowManager.LayoutParams showSelfAd(String adLoc,int adSource, Context context,WindowManager.LayoutParams params);
}
