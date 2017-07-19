package com.mg.others.model;


import android.content.Context;
import android.content.res.Configuration;

import com.mg.comm.MConstant;
import com.mg.others.utils.CommonUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mg.comm.MConstant.request_type.p;


/**
 *
 * 请求内容
 */
public class RequestModel {
    private String is;
    private String dt;
    private String dtv;
    private String ic;
    private String w;
    private String h;
    private String brand;
    private String mod;
    private String ov;
    private String sdkVersion;
    private String mcc;
    private String mnc;
    private String lac;
    private String cid;
    private int nt;
    private String mac;
    private String pl;
    private String adrid;
    private int adnum;
    private String ua;
    private String dip;
    private String sign;
    private int pt;
    private int st;
    private String action;
    private String appid;
    private String ver;
    private String tp;
    private String aaid;
    private String lon;
    private String lat;
    private String density;

    public String getAaid() {
        return aaid;
    }

    public void setAaid(String aaid) {
        this.aaid = aaid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDtv() {
        return dtv;
    }

    public void setDtv(String dtv) {
        this.dtv = dtv;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOv() {
        return ov;
    }

    public void setOv(String ov) {
        this.ov = ov;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public int getNt() {
        return nt;
    }

    public void setNt(int nt) {
        this.nt = nt;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getAdrid() {
        return adrid;
    }

    public void setAdrid(String adrid) {
        this.adrid = adrid;
    }

    public int getAdnum() {
        return adnum;
    }

    public void setAdnum(int adnum) {
        this.adnum = adnum;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    private static RequestModel getRequestModel(String Action, DeviceInfo mDeviceInfo, int type, Context mContext){
        String key = MConstant.APPID;
        long currentTime = System.currentTimeMillis();
        if (mDeviceInfo == null){
            return null;
        }
        RequestModel requestModel = new RequestModel();
        requestModel.setAction(Action);
        requestModel.setAppid(key);
        requestModel.setVer(MConstant.MSDK_VERSION);
        requestModel.setTp(String.valueOf(currentTime));
        switch (Action) {
            case MConstant.request_type.ra:
                requestModel.setIs(mDeviceInfo.getImsi());
                requestModel.setDt("1");
                requestModel.setDtv(mDeviceInfo.getImei());
                requestModel.setIc(mDeviceInfo.getIccid());
                requestModel.setW(String.valueOf(mDeviceInfo.getScreenWidth()));
                requestModel.setH(String.valueOf(mDeviceInfo.getScreenHeight()));
                requestModel.setBrand(mDeviceInfo.getProductBrand());
                requestModel.setMod(mDeviceInfo.getProductModel());
                requestModel.setOv(mDeviceInfo.getOsVersionName());
                requestModel.setSdkVersion(String.valueOf(mDeviceInfo.getOsVersion()));
                requestModel.setMcc(String.valueOf(mDeviceInfo.getMcc()));
                requestModel.setMnc(String.valueOf(mDeviceInfo.getMnc()));
                requestModel.setLac(String.valueOf(mDeviceInfo.getLac()));
                requestModel.setCid(String.valueOf(mDeviceInfo.getCid()));
                requestModel.setNt(CommonUtils.getNetworkSubType(mContext));
                requestModel.setMac(mDeviceInfo.getMac());
                //设置请求广告类型
                requestModel.setPt(type);
                requestModel.setSt(0);
                requestModel.setPl(CommonUtils.getInstalledSafeWare(mContext));
                requestModel.setAdrid(mDeviceInfo.getAndroidId());
                requestModel.setAdnum(1);
                requestModel.setUa(mDeviceInfo.getUserAgent());
                requestModel.setDip(String.valueOf(mDeviceInfo.getDip()));
                requestModel.setAaid(mDeviceInfo.getAdvertisingId());
                requestModel.setLon(mDeviceInfo.getLon());
                requestModel.setLat(mDeviceInfo.getLat());
                requestModel.setDensity(mDeviceInfo.getDensity());
                requestModel.setSign(CommonUtils.hashSign(Action+key+ MConstant.MSDK_VERSION
                        + currentTime+"1"+mDeviceInfo.getImei() + mDeviceInfo.getScreenWidth() + mDeviceInfo.getScreenHeight()));
                break;

            case MConstant.request_type.ni:
                requestModel.setW(String.valueOf(mDeviceInfo.getScreenWidth()));
                requestModel.setH(String.valueOf(mDeviceInfo.getScreenHeight()));
                requestModel.setDt("1");
                requestModel.setDtv(mDeviceInfo.getImei());
                requestModel.setSign(CommonUtils.hashSign(Action+key+ MConstant.MSDK_VERSION
                        + currentTime+"1"+mDeviceInfo.getImei()));
                break;

            case p:

                break;

            case MConstant.request_type.vgd:

                break;
        }
        return requestModel;
    }



    public static Map<String,String> getRequestParams2(DeviceInfo deviceInfo, int pt,String appid,String lid, Context mContext){
        if (deviceInfo == null){
            return null;
        }
        Map<String,String> params = new HashMap<>();
        try {
            long currTime = System.currentTimeMillis();
            params.put("action","sra");
            params.put("appid", appid);
            params.put("ver", MConstant.MSDK_VERSION);
            params.put("tp", String.valueOf(currTime));
            params.put("is",deviceInfo.getImsi());
            params.put("dt","1");
            params.put("dtv",deviceInfo.getImei());
            params.put("ic",deviceInfo.getIccid());
            params.put("w",String.valueOf(deviceInfo.getScreenWidth()));
            params.put("h",String.valueOf(deviceInfo.getScreenHeight()));
            params.put("brand",deviceInfo.getProductBrand());
            params.put("mod",deviceInfo.getProductModel());
            params.put("os","android");
            params.put("ov",deviceInfo.getOsVersionName());
            params.put("sdkVersion",String.valueOf(deviceInfo.getOsVersion()));
            params.put("mcc", String.valueOf(deviceInfo.getMcc()));
            params.put("mnc", String.valueOf(deviceInfo.getMnc()));
            params.put("lac",String.valueOf(deviceInfo.getLac()));
            params.put("cid",String.valueOf(deviceInfo.getCid()));
            params.put("nt", String.valueOf(CommonUtils.getNetworkSubType(mContext)));
            params.put("mac", deviceInfo.getMac());
            params.put("pt", String.valueOf(pt));
            params.put("pl",CommonUtils.getInstalledSafeWare(mContext));
            params.put("adrid",deviceInfo.getAndroidId());
            params.put("adnum", "1");
            try {
                params.put("ua", URLEncoder.encode(deviceInfo.getUserAgent(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put("dip",String.valueOf(deviceInfo.getDip()));
            params.put("aaid",deviceInfo.getAdvertisingId());
            params.put("lon",deviceInfo.getLon());
            params.put("lat",deviceInfo.getLat());
            params.put("density",deviceInfo.getDensity());
            params.put("bssid",deviceInfo.getBssid());
            params.put("brk",String.valueOf(isRootSystem()));
            params.put("dl","1");
            params.put("sign",CommonUtils.hashSign("sra"+appid+ MConstant.MSDK_VERSION
                    + currTime+"1"+deviceInfo.getImei() + deviceInfo.getScreenWidth() + deviceInfo.getScreenHeight()));
            params.put("lid",lid);
            params.put("orientation",String.valueOf(getOri(mContext)));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return params;
    }
    public static int getOri(Context context){
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation ; //获取屏幕方向
        int i = 0;
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            i = 0;

        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){
            i = 1;
        }
        return i;
    }
    public static int isRootSystem() {
        if(isRootSystem1()||isRootSystem2()){
            return 1;
        }else{
            return 0;
        }
    }
    private static boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/" };
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()&&f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    private static boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i),"su");
                if (f != null && f.exists()&&f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }

    public static Map<String,String> getRequestSdkEpParams(DeviceInfo deviceInfo,int type,int pt, String errorcode,long dt){
        Map<String,String> params=new HashMap<>();
        try {
            long curr=System.currentTimeMillis()/1000;
            params.put("action","SDKEP");
            params.put("appid",MConstant.APPID);
            params.put("ver",MConstant.MSDK_VERSION);
            params.put("tp",String.valueOf(curr));
            params.put("imei",deviceInfo.getImei());
            params.put("w",String.valueOf(deviceInfo.getScreenWidth()));
            params.put("h",String.valueOf(deviceInfo.getScreenHeight()));
            params.put("type",String.valueOf(type));
            params.put("dt",String.valueOf(dt));
            params.put("resultCode",errorcode);
            params.put("pt",String.valueOf(pt));
            params.put("sign",CommonUtils.hashSign("SDKEP"+MConstant.APPID+ MConstant.MSDK_VERSION + curr + type + deviceInfo.getImei() + dt));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return params;
    }

}
