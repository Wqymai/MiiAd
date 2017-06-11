package com.mg.comm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mg.others.manager.ApkDownloadManager;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.CommonUtils;

/**
 * Created by wuqiyan on 2017/6/11.
 */

public class ADClickHelper {
    private Context mContext;
    public ADClickHelper(Context context){
        this.mContext=context;

    }
    //点击打开
    public void AdClick(AdModel ad) {
        //点击上报
        HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

        if (ad == null) {
            return;
        }

        if (ad.getType() != MConstant.adClickType.app) {
            CommonUtils.openBrowser(mContext, ad.getUrl());
            return;
        }

        else {
            if (ad.getUrl().contains("click")){
                CommonUtils.openBrowser(mContext, ad.getUrl());
                return;
            }
            if (CommonUtils.getNetworkSubType(mContext) == CommonUtils.NET_TYPE_WIFI) {

                Intent intent=new Intent(mContext,LoadHelperService.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("ad",ad);
                intent.putExtras(bundle);
                //开始下载上报
                HttpManager.reportEvent(ad, AdReport.EVENT_DOWNLOAD_START, mContext);
                return;
            }
        }
    }
    public void apkDownload(AdModel ad){
        ApkDownloadManager manager = ApkDownloadManager.getIntance(mContext);
        manager.downloadFile(ad);
    }
}
