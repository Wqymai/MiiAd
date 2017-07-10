package com.mg.splash;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/5.
 */

public class MgSplashAD {


    public MgSplashAD(Activity activity, ViewGroup adContainer, View skipContainer, MiiADListener adListener){

       MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadSplashADMethod(activity,adContainer,skipContainer,adListener);
    }



}
