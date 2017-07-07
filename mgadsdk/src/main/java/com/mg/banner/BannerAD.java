package com.mg.banner;

import android.app.Activity;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.d.c.DynamicModel;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class BannerAD {

    private DynamicModel model;
    public BannerAD(Activity activity, ViewGroup adContainer, MiiADListener listener){
       model = MethodDynamicLoad.loadBannerADMethod(activity,adContainer,listener);
    }

    public void recycle(){
       MethodDynamicLoad.loadRecycleMethod(model);
    }
}
