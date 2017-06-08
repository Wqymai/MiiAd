package com.mg.others.layer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.WindowManager;

import com.mg.others.utils.CommonUtils;

/**
 * Created by wuqiyan on 17/4/20.
 */

public class SelfBanner extends AdShowBase {
    private static final String BANNER_TOP="0";
    private static final String BANNER_BOTTOM="1";
    private static final int HTML5_SOURCE=4;
    private static final double B_PERCENT=6.5;

    @Override
    public WindowManager.LayoutParams showSelfAd( String adLoc, int adSource,Context context,
                                                     WindowManager.LayoutParams params) {

        int screenH = CommonUtils.getScreenH(context);
        int screenW = CommonUtils.getScreenW(context);
        if (adLoc.equals(BANNER_TOP)){
            params.gravity = Gravity.TOP;
        }
        if (adLoc.equals(BANNER_BOTTOM)){
            params.gravity = Gravity.BOTTOM;
        }
        if (adSource == HTML5_SOURCE){
            if (screenH > screenW){
                params.height=(int) (screenH/B_PERCENT);
                params.width= WindowManager.LayoutParams.MATCH_PARENT;
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else {
                params.width = screenH;
                params.height = (int) (screenW/B_PERCENT);
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }
        else {
            if (screenH > screenW){
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else {
                params.height = (int) (screenW/B_PERCENT);
                params.width = screenH;
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }
        return params;
    }
}
