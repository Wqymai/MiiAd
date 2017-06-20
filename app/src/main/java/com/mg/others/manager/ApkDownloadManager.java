package com.mg.others.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.mg.others.http.HttpDownloadListener;
import com.mg.others.http.HttpUtils;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nemo on 2016/7/2.
 */
public class ApkDownloadManager implements HttpDownloadListener {
    public static ApkDownloadManager instance;
    private Map<String, AdModel> downloadingList;
    private Map<String, AdModel> downloadedList;
    private HttpUtils httpUtils;
    private ApkInstallReceiver apkInstallReceiver;
    private Context mContext;
    public ApkDownloadManager(Context context) {
        this.mContext = context;
        downloadingList = new HashMap<>();
        downloadedList = new HashMap<>();
        httpUtils = new HttpUtils(context);

        apkInstallReceiver = new ApkInstallReceiver();
        IntentFilter apkFilter = new IntentFilter();
        apkFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        apkFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        apkFilter.addDataScheme("package");
        mContext.registerReceiver(apkInstallReceiver, apkFilter);
    }

    public static ApkDownloadManager getIntance(Context context) {
        if (instance == null){
            synchronized (ApkDownloadManager.class){
                if (instance == null){
                    instance = new ApkDownloadManager(context);
                }
            }
        }
        return instance;
    }

    public void downloadFile(AdModel adModel){

        downloadingList.put(adModel.getUrl(), adModel);
        String fileName = adModel.getName() + ".apk";
        String path =mContext.getFilesDir().getPath()+"/"; //CommonUtils.getFileDownloadLocation(mContext);
        File file = new File(path, fileName);
        adModel.setApkFilePath(file.getPath());
        if (file.exists()){
            file.delete();
        }
        httpUtils.download(adModel.getUrl(),this, path,fileName,false);


    }

    @Override
    public void onDownloadStart(long fileSize) {
    }

    @Override
    public void onDownloading(long downloadSize, long incrementSize, float percentage) {
    }

    @Override
    public void onDownloadSuccess(String key) {
        AdModel adModel1 = downloadingList.remove(key);
        if (adModel1 != null){

            //下载完成上报
            HttpManager.reportEvent(adModel1, AdReport.EVENT_DOWNLOAD_COMPLETE,mContext);
            String filePath = adModel1.getApkFilePath();
            CommonUtils.installNormal(mContext,filePath);
            downloadedList.put(adModel1.getPkName(),adModel1);

        }
    }

    @Override
    public void onDownloadFailed(Exception e) {
        LogUtils.e("Failed = " +e.toString());
        downloadingList.clear();
    }


    private class ApkInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
           try {
            if (intent != null && intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
                Uri data = intent.getData();
                if (data != null){
                    String pkName = data.getSchemeSpecificPart();
                    final AdModel adModel = downloadedList.remove(pkName);
                    if (adModel != null){
                        //安装完成上报
                        HttpManager.reportEvent(adModel, AdReport.EVENT_INSTALL_COMLETE, mContext);
                        File file = new File(adModel.getApkFilePath());
                        if (file.exists()){
                            file.delete();
                        }
//                        new MiiServiceHelper().checkActive(context,adModel);
                    }
                }
            }
           }catch (Exception e){
               e.printStackTrace();
           }

        }
    }
}
