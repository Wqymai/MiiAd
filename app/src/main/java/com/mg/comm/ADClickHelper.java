package com.mg.comm;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.ApkDownloadManager;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wuqiyan on 2017/6/11.
 */

public class ADClickHelper {
    private Context mContext;
    public ADClickHelper(Context context){
        this.mContext=context;

    }
    //点击打开
    public void AdClick(final AdModel ad) {

        if (ad == null) {
            return;
        }
        String adUrl = replaceAdUrl(ad);
        if (ad.getType()!=5){
            //点击上报
            HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

            if (ad.getType() != 1) {
                CommonUtils.openBrowser(mContext, adUrl);
                return;
            }
            else {
                if (ad.getUrl().contains("click")){
                    CommonUtils.openBrowser(mContext, adUrl);
                    return;
                }
                if (CommonUtils.getNetworkSubType(mContext) == CommonUtils.NET_TYPE_WIFI) {

                    Intent intent=new Intent(mContext,LoadHelperService.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("ad",ad);
                    intent.putExtras(bundle);
                    mContext.startService(intent);
                    //开始下载上报
                    HttpManager.reportEvent(ad, AdReport.EVENT_DOWNLOAD_START, mContext);
                    return;
                }
            }
        }
        else {
            HttpUtils httpUtils = new HttpUtils(mContext);
            httpUtils.get(adUrl, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                  try {

                    Map<String,String> map = parseType5Res(response.entity());
                    //点击上报
                    ad.setClickid(map.get("clickid"));
                    HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

                    //下载
                    String dstlink = map.get("dstlink");
                    ad.setUrl(dstlink);

                    Pattern p = Pattern.compile("fsname=(?<name>.*?)_");
                    Matcher m = p.matcher(dstlink);
                    if (m.find()){
                        LogUtils.i(MConstant.TAG,m.group(1));
                        ad.setPkName(m.group(1));
                    }
                    if (CommonUtils.getNetworkSubType(mContext) == CommonUtils.NET_TYPE_WIFI) {

                        Intent intent=new Intent(mContext,LoadHelperService.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("ad",ad);
                        intent.putExtras(bundle);
                        mContext.startService(intent);
                        //开始下载上报
                        HttpManager.reportEvent(ad, AdReport.EVENT_DOWNLOAD_START, mContext);
                        return;
                    }
                  }catch (Exception e){
                      e.printStackTrace();
                  }
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        }
    }
    public void apkDownload(AdModel ad){
        ApkDownloadManager manager = ApkDownloadManager.getIntance(mContext);
        manager.downloadFile(ad);
    }

    private String replaceAdUrl(AdModel adModel){
        String originUrl = adModel.getUrl();
        if (originUrl.contains("%%DOWNX%%")){
            originUrl = originUrl.replace("%%DOWNX%%",adModel.getDownx());
        }
        if (originUrl.contains("%%DOWNY%%")){
            originUrl = originUrl.replace("%%DOWNY%%",adModel.getDowny());
        }
        if (originUrl.contains("%%UPX%%")){
            originUrl = originUrl.replace("%%UPX%%",adModel.getUpx());
        }
        if (originUrl.contains("%%UPY%%")){
            originUrl = originUrl.replace("%%UPY%%",adModel.getUpy());
        }
        return originUrl;
    }
    private Map<String,String> parseType5Res(String res){
        Map<String,String> map=new HashMap<>();
        try {
            JSONObject objectP=new JSONObject(res);
            JSONObject objectC=new JSONObject(objectP.optString("data"));
            map.put("clickid",objectC.optString("clickid"));
            map.put("dstlink",objectC.optString("dstlink"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;

    }
}
