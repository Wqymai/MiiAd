package com.mg.banner;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mg.interf.MiiBannerADListener;
import com.mg.others.model.DynamicModel;
import com.mg.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgBannerAD {

    private DynamicModel model;
    private Context mContext;
    public MgBannerAD(Activity activity, ViewGroup adContainer,String appid,String lid, MiiBannerADListener listener){
        this.mContext = activity.getApplicationContext();
       model = MethodDynamicLoad.getInstance(mContext).loadBannerADMethod(activity,adContainer,appid,lid,listener);
    }

    public void recycle(){
       MethodDynamicLoad.getInstance(mContext).loadRecycleMethod(model);
    }

    public void loadBannerAD(){
        MethodDynamicLoad.getInstance(mContext).loadBanner(model);
    }
}
