package com.mg.dobber;

import android.app.Activity;
import android.content.Context;

import com.mg.interf.MiiADListener;
import com.mg.others.model.DynamicModel;
import com.mg.utils.MethodLoad;

/**
 * Created by wuqiyan on 17/8/14.
 */

public class MgDobberAD {

    private DynamicModel model;
    private Context mContext;
    public MgDobberAD(Activity activity, String appid, String lid, MiiADListener listener){
        this.mContext = activity.getApplicationContext();
        model = MethodLoad.getInstance(mContext).loadDobberADMethod(activity,appid,lid,listener);

    }
    public void recycle(){
        MethodLoad.getInstance(mContext).loadRecycleMethod(model);
    }

    public void loadDobberAD(){
        MethodLoad.getInstance(mContext).loadDobber(model);
    }
}
