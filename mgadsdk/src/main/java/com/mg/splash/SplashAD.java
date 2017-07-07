package com.mg.splash;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.others.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/5.
 */

public class SplashAD {


    public SplashAD(Activity activity, ViewGroup adContainer, View skipContainer, MiiADListener adListener){

       MethodDynamicLoad.loadSplashADMethod(activity,adContainer,skipContainer,adListener);
    }



}
