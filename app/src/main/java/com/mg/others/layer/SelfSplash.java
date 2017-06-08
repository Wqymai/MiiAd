package com.mg.others.layer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;

import com.mg.others.utils.CommonUtils;

/**
 * Created by wuqiyan on 17/4/20.
 */

public class SelfSplash extends AdShowBase {
    private static final double H_PERCENT=0.85;

    @Override
    public WindowManager.LayoutParams showSelfAd(String adLoc,int isH5, Context context,
                                                     WindowManager.LayoutParams params) {
        int screenH = CommonUtils.getScreenH(context);
        int screenW = CommonUtils.getScreenW(context);
        if (screenH > screenW){
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.width= WindowManager.LayoutParams.MATCH_PARENT;
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
            params.height =(int)(screenH * H_PERCENT);
            params.width=screenH;
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        return params;
    }
}
