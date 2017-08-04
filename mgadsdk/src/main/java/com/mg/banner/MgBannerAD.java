package com.mg.banner;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.others.model.DynamicModel;
import com.mg.utils.MethodLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgBannerAD {

    private DynamicModel model;
    private Context mContext;
    public MgBannerAD(Activity activity, ViewGroup adContainer,String appid,String lid, MiiADListener listener){
        this.mContext = activity.getApplicationContext();
       model = MethodLoad.getInstance(mContext).loadBannerADMethod(activity,adContainer,appid,lid,listener);
    }

    public void recycle(){
       MethodLoad.getInstance(mContext).loadRecycleMethod(model);
    }

    public void loadBannerAD(){
        MethodLoad.getInstance(mContext).loadBanner(model);
    }
}
