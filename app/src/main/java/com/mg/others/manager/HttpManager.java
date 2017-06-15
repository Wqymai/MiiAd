package com.mg.others.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.mg.comm.MConstant;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.RequestModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiBase64;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;
import com.mg.others.v4.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by nemo on 2016/6/28.
 */
public class HttpManager {
    public static final String NI = "NI";
    public static final String P = "P";
    public static final String RA="RA";
    public static HttpManager instance;
    private DeviceInfo mDeviceInfo;
    private Context mContext;
    private JSONArray hosts;
    private int index;

    private int adType;
    private int ts;
    private static boolean isUpdate;

    public static HttpManager getInstance(Context mContext) {
        if (instance == null){
            synchronized (HttpManager.class){
                if (instance == null){
                    synchronized (HttpManager.class){
                        instance = new HttpManager(mContext);
                    }
                }
            }
        }
        return instance;
    }

    public HttpManager(Context mContext) {
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        }
        this.mContext = mContext;

    }





    public String getRaUrl(String action){
        StringBuilder sb=new StringBuilder();
        if (action.equals(MConstant.request_type.ra)){
            if (MConstant.HB_HOST.equals("")){
                sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
            }
            else {
                sb.append(MConstant.HB_HOST);
            }
        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        sb.append("/");
        sb.append(action);
        return sb.toString();
    }


    public Map<String, String> getParams2(@NonNull String Action, int type, int tiggleSence){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
        }
        return RequestModel.getRequestParams2(Action,mDeviceInfo, type, tiggleSence,mContext);
    }










    //是否过了今天
    public boolean DateCompare(long when){
        try {
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat yFormat = new SimpleDateFormat("y"); //打印年份
            SimpleDateFormat mFormat = new SimpleDateFormat("M"); //打印月份
            SimpleDateFormat dFormat = new SimpleDateFormat("d"); //打印日份
            int yearNow=Integer.parseInt(yFormat.format(date));
            int monthNow=Integer.parseInt(mFormat.format(date));
            int dayNow=Integer.parseInt(dFormat.format(date));
            date.setTime(when);
            int year=Integer.parseInt(yFormat.format(date));
            int month=Integer.parseInt(mFormat.format(date));
            int day=Integer.parseInt(dFormat.format(date));
            if (yearNow > year||monthNow > month||dayNow > day){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public String getParams(@NonNull String Action, int type, int tiggleSence){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }
        String url = RequestModel.getRequestParams(Action,mDeviceInfo, type, tiggleSence,mContext);
        return url;
    }

    public String getUA(){
        return mDeviceInfo == null ? null : mDeviceInfo.getUserAgent();
    }

    public synchronized void requestPing(final String host, final onPingListener pingListener){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        String url = host + MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS) +"/" + P;

        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                if (!TextUtils.isEmpty(response.entity())){
                    if ("1".equals(response.entity())){
                        if (pingListener != null){
                            pingListener.pingSuc(host);
                        }
                    }
                }
                if (Integer.valueOf(response.entity()) == 1){
                }
            }
            @Override
            public void onFail(Exception e) {
                if (pingListener != null){
                    VGD(hosts,++index);
                }
            }
        });

    }



    public void VGD(final JSONArray hosts, int i){
        this.index = i;
        this.hosts = hosts;
        if (i >= hosts.length()){
            return;
        }
        requestPing(hosts.optString(i), new onPingListener() {
            @Override
            public void pingSuc(String url) {

                MConstant.HB_HOST = url;

            }
        });

    }

    public interface onPingListener{
        void pingSuc(String url);
    }

    /**
     * 数据上报
     * @param adModel
     * @param eventType
     */
    public static void reportEvent(AdModel adModel, int eventType, Context mContext){
        if (adModel == null){
            return;
        }
        if (!CommonUtils.isNetworkAvailable(mContext)){

            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        String urls[] = null;
        String type  =  null;
        switch(eventType){
            case AdReport.EVENT_CLICK:
                type = "EVENTT_CLICK";
                urls = adModel.getReportBean().getUrlClick();
                break;

            case AdReport.EVENT_DOWNLOAD_COMPLETE:
                type = "EVENTT_DOWNLOAD_COMPLETE";
                urls = adModel.getReportBean().getUrlDownloadComplete();
                break;

            case AdReport.EVENT_DOWNLOAD_START:
                type = "EVENTT_DOWNLOAD_START";
                urls = adModel.getReportBean().getUrlDownloadStart();
                break;

            case AdReport.EVENT_INSTALL_COMLETE:
                type = "EVENTT_INSTALL_COMPLETE";
                urls = adModel.getReportBean().getUrlInstallComplete();
                break;

            case AdReport.EVENT_OPEN:
                type = "EVENTT_OPEN";
                urls = adModel.getReportBean().getUrlOpen();
                break;

            case AdReport.EVENT_SHOW:
                type = "EVENTT_SHOW";
                urls = adModel.getReportBean().getUrlShow();
                break;
        }
        if (urls == null){
            return;
        }

        for (String str : urls){
            if (!TextUtils.isEmpty(str)){
                httpUtils.get(str,null);
            }
        }

    }
}
