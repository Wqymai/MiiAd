package com.mg.headup;

import android.app.Activity;
import android.content.Context;

import com.mg.interf.MiiADListener;
import com.mg.others.model.DynamicModel;
import com.mg.utils.MethodLoad;

/**
 * Created by wuqiyan on 17/8/14.
 */

public class MgHeadupAD {

    private DynamicModel model;
    private Context mContext;
    public MgHeadupAD(Activity activity, boolean isTop, String appid, String lid, MiiADListener listener){
        this.mContext = activity.getApplicationContext();
        model = MethodLoad.getInstance(mContext).loadHeadupADMethod(activity,isTop,appid,lid,listener);

    }
    public void recycle(){
        MethodLoad.getInstance(mContext).loadRecycleMethod(model);
    }

    public void loadHeadupAD(){
        MethodLoad.getInstance(mContext).loadHeadup(model);
    }
}
