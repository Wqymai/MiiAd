package com.mg.nativ;

import android.content.Context;
import android.view.View;

import com.mg.comm.ADClickHelper;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.SP;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class NativeModel implements MiiNativeADDataRef {

    private String imageUrl;
    private int adType;
    private AdModel adModel;
    private Context mContext;
    public NativeModel(Context context){
        this.mContext = context;
    }

    public void setAdModel(AdModel model){
        this.adModel = model;
    }

    public void setAdType(int adType){
        this.adType = adType;
    }

    public void setImgUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getImgUrl() {
        return imageUrl;
    }

    @Override
    public int getADType(int adType) {
        return adType;
    }


    @Override
    public void onClick(View view) {
        //点击调用
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ADClickHelper(mContext).AdClick(adModel);
            }
        });
    }

    @Override
    public void onExposured() {

        //记录展示次数
        int show_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);
        SP.setParam(SP.CONFIG, mContext, SP.FOT, show_num + 1);

        //展示上报
        HttpManager.reportEvent(adModel, AdReport.EVENT_SHOW, mContext);
    }
}
