package com.mg.others.layer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.WindowManager;

import com.mg.others.utils.CommonUtils;

/**
 * Created by wuqiyan on 17/4/20.
 */

public class SelfInterstitial extends AdShowBase {
    private static final int HTML5_SOURCE=4;
    private static final double I_PERCENT=1.2;
    private static final double W_PERCENT=0.9;
    private static final double H_PERCENT=0.85;


    @Override
    public WindowManager.LayoutParams showSelfAd(String adLoc,int adSource, Context context,
                                                           WindowManager.LayoutParams params) {
        int screenH = CommonUtils.getScreenH(context);
        int screenW = CommonUtils.getScreenW(context);
        params.gravity = Gravity.CENTER;

        if (adSource == HTML5_SOURCE){
            if (screenH > screenW){
                params.height = (int) ((screenW * W_PERCENT)/I_PERCENT);
                params.width = (int) (screenW * W_PERCENT);
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else {
                params.width = screenH;
                params.height = (int) (screenH/I_PERCENT);
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }
        else {
            if (screenH > screenW){
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = (int) (screenW * W_PERCENT);
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else {
                params.width = screenH;
                params.height = (int) (screenH * H_PERCENT);
                params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }
        return params;
    }

}
