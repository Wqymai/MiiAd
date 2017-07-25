package com.mg.tuia;

import android.content.Context;

import com.db.ta.sdk.NonStandardTm;
import com.db.ta.sdk.NsTmListener;
import com.mg.interf.MiiCustomAdListener;

/**
 * Created by wuqiyan on 17/7/24.
 */

public class MiiTuiaAD {
    private NonStandardTm mNonStandardTm;
    private MiiCustomAdListener listener;


    public void loadAd(Context context,int id){
        if (mNonStandardTm == null){
            mNonStandardTm = new NonStandardTm(context);
        }
        mNonStandardTm.loadAd(id);
        mNonStandardTm.setAdListener(new NsTmListener() {
            @Override
            public void onReceiveAd(String s) {
                listener.onReceiveAd(s);
            }

            @Override
            public void onFailedToReceiveAd() {
                listener.onFailedToReceiveAd();
            }
        });
    }

    public void setAdListener(MiiCustomAdListener listener){
       this.listener = listener;
    }
    public void adExposed(){
        mNonStandardTm.adExposed();
    }
    public void adClicked(){
        mNonStandardTm.adClicked();
    }
}
