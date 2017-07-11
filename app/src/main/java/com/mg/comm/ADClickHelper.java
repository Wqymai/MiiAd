package com.mg.comm;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    private HttpUtils httpUtils=null;
    public ADClickHelper(Context context){
        this.mContext=context;

    }
    //点击打开
    public void AdClick(final AdModel ad) {

        if (ad == null) {
            return;
        }
        //优先处理deeplink
        String deepLink = ad.getDeeplink();
        if (deepLink != null && !deepLink.equals("")){
          try {
            //点击上报
            HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

            //通过deeplink打开应用
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(deepLink);
            intent.setData(uri);
            mContext.startActivity(intent);

          }catch (Exception e){//如果打不开deeplink就使用url

              normalClick(ad);
          }
        }
        else {
            normalClick(ad);
        }

    }

    private void normalClick(final AdModel ad){
        String adUrl = replaceAdUrl(ad);
        if (ad.getType()!=5){
            //点击上报
            HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, mContext);

            if (ad.getType() != 1) {
                CommonUtils.openBrowser(mContext, adUrl);
                return;
            }
            else {
                if (adUrl.contains("click")){
                    CommonUtils.openBrowser(mContext, adUrl);
                    return;
                }
                startDownload(ad);
            }
        }
        else {

            if (httpUtils == null){
                httpUtils = new HttpUtils(mContext);
            }
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
                        String pn = getPName(dstlink);
                        if (pn!=null && !pn.equals("")){
                            ad.setPkName(pn);
                        }
                        startDownload(ad);

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


    private void startDownload(AdModel ad){

        if (CommonUtils.getNetworkSubType(mContext) == CommonUtils.NET_TYPE_WIFI) {
            Intent intent=new Intent(mContext,LoadHelperService.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("ad",ad);
            intent.putExtras(bundle);
            mContext.startService(intent);

//            Intent intent = new Intent();
//            ComponentName cn = new ComponentName(mContext, "com.mg.service.LoadHelperService");
//            intent.setComponent(cn);
//            Bundle bundle=new Bundle();
//            bundle.putSerializable("ad",ad);
//            intent.putExtras(bundle);
//            mContext.startService(intent);
        }

    }

    private String getPName(String dstlink){
        String pn = null;
        Pattern p = Pattern.compile("fsname=(.*?)_");
        Matcher m = p.matcher(dstlink);
        if (m.find()){
            LogUtils.i(MConstant.TAG,m.group(1));
            pn=m.group(1);
        }
        return pn;
    }

    public void apkDownload(AdModel ad){
        if (ad == null){
            return;
        }
        String installedList = CommonUtils.getInstalledSafeWare(mContext);
        if (installedList.contains(ad.getPkName())){ //如果存在已安装应用，直接打开不用下载了
            LogUtils.i(MConstant.TAG,"already installed");
            PackageManager packageManager = mContext.getPackageManager();
            Intent it= packageManager.getLaunchIntentForPackage(ad.getPkName());
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(it);
        }
        else {
            LogUtils.i(MConstant.TAG,"need download");
            //开始下载上报
            HttpManager.reportEvent(ad, AdReport.EVENT_DOWNLOAD_START, mContext);
            ApkDownloadManager manager = ApkDownloadManager.getIntance(mContext);
            manager.downloadFile(ad);
        }
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
