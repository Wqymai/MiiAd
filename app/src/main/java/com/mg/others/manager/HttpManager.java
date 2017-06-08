package com.mg.others.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.message.ISender;
import com.mg.others.message.MessageObjects;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.RequestModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MConstant;
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
    private ISender mSender;
    private int adType;
    private int ts;
    private static boolean isUpdate;

    public static HttpManager getInstance(Context mContext, ISender mSender) {
        if (instance == null){
            synchronized (HttpManager.class){
                if (instance == null){
                    synchronized (HttpManager.class){
                        instance = new HttpManager(mContext, mSender);
                    }
                }
            }
        }
        return instance;
    }

    public HttpManager(Context mContext,ISender mSender) {
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        }
        this.mContext = mContext;
        this.mSender = mSender;
    }

    public  boolean updateNi(SDKConfigModel config){

        long currenTime = System.currentTimeMillis();
        long diff_Time = (currenTime- config.getUpdateTime()) / 1000;
        LogUtils.i(MConstant.TAG,"updateNi......diff_Time:"+diff_Time+" config.getNext():"+config.getNext());
        if (diff_Time > config.getNext()){
            requestNi(true);
            return true;
        }
        return false;
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
            return null;
        }
        return RequestModel.getRequestParams2(Action,mDeviceInfo, type, tiggleSence,mContext);
    }

    /**
     * 请求广告
     * @param adType
     * @param ts
     */
    public void requestRa(final int adType,final int ts){
        this.adType = adType;
        this.ts = ts;
        long currentTime = System.currentTimeMillis();
        long lastRa = (long) SP.getParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA,0l);
        long diffRa = (currentTime - lastRa)/1000;

        if (lastRa !=0l && diffRa < 20){
            return;
        }
        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=getRaUrl(RA);
        if (url==null || url.equals("")){
            return;
        }
        Map<String,String> params=getParams2(RA,adType,ts);

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealHbSuc(response);
            }
            @Override
            public void onFail(Exception e) {
                LogUtils.i(MConstant.TAG,new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
            }
        },params);

    }

    private void dealHbSuc(HttpResponse response){
        LogUtils.i(MConstant.TAG,"type="+adType);
        List<AdModel> ads;
        String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (temp==null){
            return;
        }
        ads = AdParser.parseAd(temp);

        LogUtils.i(MConstant.TAG,"as="+ads);

        if (ads == null || ads.size()>0){
            return;
        }

        AdModel ad = ads.get(0);

        SharedPreferences sp=mContext.getSharedPreferences(SP.CONFIG,Context.MODE_PRIVATE);
        ad.setFlag(sp.getInt("ce",0));//默认触发广告

        if (mSender==null){
            return;
        }
        MessageObjects messageObjects = MessageObjects.obtain();
        messageObjects.obj0 = ad;
        messageObjects.arg0 = adType;
        messageObjects.arg1 = ts;
        mSender.sendMessage(mSender.obtainMessage(MConstant.what.ads_result, messageObjects));
    }

    /**
     * 配置信息初始化
     */
    public void requestNi(boolean isUpdate){
        LogUtils.i(MConstant.TAG,"requestNi");
        this.isUpdate = isUpdate;
        if (CommonUtils.isNetworkAvailable(mContext)){

            final long currenTime = System.currentTimeMillis();

            long lastNi = (long) SP.getParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, 0l);

            long diff_ni = (currenTime - lastNi)/1000;

            LogUtils.i(MConstant.TAG,"lastNi:"+lastNi+" diff_ni:"+diff_ni);

            if (lastNi != 0l && diff_ni < MConstant.DIFF_NI){
                return;
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = getParams(NI, 0, 0);
            if (url == null||url.equals("")){
                return;
            }


            httpUtils.get(url, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST=MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST,LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealNiSuc(response);
                }

                @Override
                public void onFail(Exception e) {
                    requestVGD();
                    LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                }
            });

        }else{
            LogUtils.i(MConstant.TAG,"ni 网络不可用......");
        }
    }

    private void dealNiSuc(HttpResponse response){
        SDKConfigModel sdk = null;
        String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (data==null){
            return;
        }

        sdk = ConfigParser.parseConfig(data);

        if (sdk == null){
            return;
        }
        sdk.setUpdateTime(System.currentTimeMillis());

        CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

        long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);


        SharedPreferences.Editor editor=mContext.getSharedPreferences(SP.CONFIG,Context.MODE_PRIVATE).edit();
        editor.putLong("time0",sdk.getTime0()*1000);//应用外
        editor.putLong("time1",sdk.getTime1()*1000);//解锁
        editor.putLong("time2",sdk.getTime2()*1000);//安装
        editor.putLong("time3",sdk.getTime3()*1000);//卸载
        editor.putLong("time4",sdk.getTime4()*1000);//网络切换
        editor.putLong("timeComm",sdk.getTimeComm()*1000);//公共冷却时间
        editor.putInt("ce",sdk.getCe());
        editor.commit();

        long currentTime = System.currentTimeMillis();
        if (DateCompare(writeTime) || writeTime == 0l){
            SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
        }

        if (mSender != null && isUpdate){
            MessageObjects messageObjects = MessageObjects.obtain();
            messageObjects.obj0 = sdk;
            mSender.sendMessage(mSender.obtainMessage(MConstant.what.update_config, messageObjects));
        }

    }



    //是否过了今天
    private boolean DateCompare(long when){
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

    /**
     * 请求新域名
     */
    public void requestVGD(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.get(MiiLocalStrEncrypt.deCodeStringToString(MConstant.VGD, LocalKeyConstants.LOCAL_VSGD), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                String result;
                result = MiiBase64.decode(response.entity());
                try {
                    JSONArray array = new JSONArray(result);
                    VGD(array,0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception e) {
                LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
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
            LogUtils.e(new AdError(AdError.ERROR_CODE_NETWORK_ERROR));
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
