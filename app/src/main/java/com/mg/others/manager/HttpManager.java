package com.mg.others.manager;

import android.content.Context;
import android.text.TextUtils;

import com.mg.comm.MConstant;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.RequestModel;
import com.mg.others.task.LoactionHelper;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by nemo on 2016/6/28.
 */
public class HttpManager {
    public static final String NI = "NI";
    public static final String P = "P";
    public static final String RA="RA";
    public static final String HB ="hb";
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


    public String getHbUrl(){
//        StringBuilder sb=new StringBuilder();
//        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST,LocalKeyConstants.LOCAL_KEY_DOMAINS));
//        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX_HB,LocalKeyConstants.LOCAL_KEY_ACTIONS));
//        return sb.toString();

        return  "http://192.168.199.192:8080/TestDemo/servlet/HbServlet";
    }

    public Map<String,String> getHbParams(String appid,String lid){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }
        Map<String, String> params = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        params.put("action",HB);
        params.put("appid", appid);
        params.put("ver", MConstant.MSDK_VERSION);
        params.put("tp", String.valueOf(currentTime));
        params.put("dt","1");
        params.put("dtv",mDeviceInfo.getImei());
        params.put("sign",CommonUtils.hashSign(HB + appid + MConstant.MSDK_VERSION
                + currentTime+"1"+mDeviceInfo.getImei()));
        params.put("w",String.valueOf(mDeviceInfo.getScreenWidth()));
        params.put("h",String.valueOf(mDeviceInfo.getScreenHeight()));
        params.put("lid",lid);
        return params;
    }



    public String getRaUrl(){
        StringBuilder sb = new StringBuilder();
        if (MConstant.HB_HOST.equals("")){
            sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
        }
        else {
            sb.append(MConstant.HB_HOST);
        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX_SRA,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        return sb.toString();
    }

    public  String getSdkEpUrl(){
        StringBuilder sb=new StringBuilder();
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST_SDKEP, LocalKeyConstants.LOCAL_KEY_DOMAINS));
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        sb.append("/");
        sb.append("SDKEP");
        return sb.toString();
    }


    public Map<String, String> getSraParams( int pt,String appid,String lid){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }

        return RequestModel.getRequestParams2(mDeviceInfo, pt,appid,lid,mContext);
    }


    public Map<String, String> getSDKEpParams(int type,int pt, String errorcode,long dt){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }
        return RequestModel.getRequestSdkEpParams(mDeviceInfo,type,pt, errorcode,dt);
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



//    public String getParams(@NonNull String Action, int type, int tiggleSence,String appid){
//        if (mDeviceInfo == null){
//            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
//            return null;
//        }
//        String url = RequestModel.getRequestParams(Action,mDeviceInfo, type, tiggleSence,appid,mContext);
//        return url;
//    }

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
     * 广点通数据上报
     */
    public static void reportGdtEvent(int type,int pt,String errorCode,Context mContext){

      try{
        long curr = System.currentTimeMillis();
        long st = (long) SP.getParam(SP.CONFIG, mContext, SP.GDT_ST, System.currentTimeMillis());
        long diff = curr - st;

        String url = getInstance(mContext).getSdkEpUrl();
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.post(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {

            }
            @Override
            public void onFail(Exception e) {

            }
        }, getInstance(mContext).getSDKEpParams(type,pt, errorCode, diff));
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    /**
     * 数据上报
     * @param adModel
     * @param eventType
     */
    public static void reportEvent(AdModel adModel, int eventType, Context mContext){

      try{
        if (adModel == null){
            return;
        }
        if (!CommonUtils.isNetworkAvailable(mContext)){

            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        String urls[] = null;


        switch(eventType){
            case AdReport.EVENT_CLICK:

                urls = adModel.getReportBean().getUrlClick();
                break;

            case AdReport.EVENT_DOWNLOAD_COMPLETE:

                urls = adModel.getReportBean().getUrlDownloadComplete();
                break;

            case AdReport.EVENT_DOWNLOAD_START:

                urls = adModel.getReportBean().getUrlDownloadStart();
                break;

            case AdReport.EVENT_INSTALL_COMLETE:

                urls = adModel.getReportBean().getUrlInstallComplete();
                break;

            case AdReport.EVENT_OPEN:

                urls = adModel.getReportBean().getUrlOpen();
                break;

            case AdReport.EVENT_SHOW:

                urls = adModel.getReportBean().getUrlShow();
                break;
            case AdReport.EVENT_INSTALL_START:

                urls = adModel.getReportBean().getUrlInstallStart();
                break;
        }
        if (urls == null){
            return;
        }

        for (String str : urls){
            if (!TextUtils.isEmpty(str)){

              if (adModel.getType() == 4){

                httpUtils.get(str,null);

              } else {

                httpUtils.get(replaceReportEventUrl(adModel,str,mContext),null);

              }
            }
        }
      }catch (Exception e){
          e.printStackTrace();
      }
    }


    //替换点击上报
    private static String replaceReportEventUrl(AdModel adModel,String originUrl,Context context){

        LoactionHelper.LocModel locModel=LoactionHelper.GetUserLocation(context);
        if (originUrl.contains("%%LON%%")){
            originUrl = originUrl.replace("%%LON%%",String.valueOf(locModel.lon));
        }
        if (originUrl.contains("%%LAT%%")){
            originUrl = originUrl.replace("%%LAT%%",String.valueOf(locModel.lat));
        }
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
        if (originUrl.contains("%%CLICKID%%")){
            originUrl = originUrl.replace("%%CLICKID%%",adModel.getClickid());
        }
        return originUrl;
    }


}
