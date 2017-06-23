package com.mg.others.model;


import android.content.Context;
import android.util.Base64;

import com.mg.comm.MConstant;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


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

    private static RequestModel getRequestModel(String Action, DeviceInfo mDeviceInfo, int type, int tiggleSence, Context mContext){
        String key= MConstant.APPID;
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
                requestModel.setSt(tiggleSence);
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
                requestModel.setDt("1");
                requestModel.setDtv(mDeviceInfo.getImei());
                requestModel.setSign(CommonUtils.hashSign(Action+key+ MConstant.MSDK_VERSION
                        + currentTime+"1"+mDeviceInfo.getImei()));
                break;

            case MConstant.request_type.p:

                break;

            case MConstant.request_type.vgd:

                break;
        }
        return requestModel;
    }

    public static String getRequestParams(String Action, DeviceInfo deviceInfo,int type,int tiggleSence, Context mContext){
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        RequestModel requestModel = getRequestModel(Action, deviceInfo, type, tiggleSence, mContext);
        params.put("action",requestModel.getAction());
        params.put("appid", requestModel.getAppid());
        params.put("ver", requestModel.getVer());
        params.put("tp", requestModel.getTp());
        boolean isImeiTest = false;
        switch(Action){
            case MConstant.request_type.hb:
                isImeiTest = false;
                params.put("is",requestModel.getIs());
                params.put("dt",requestModel.getDt());
                params.put("dtv",requestModel.getDtv());
                params.put("ic",requestModel.getIc());
                params.put("w",requestModel.getW());
                params.put("h",requestModel.getH());
                params.put("brand",requestModel.getBrand().trim().replace(" ",""));
                params.put("mod",requestModel.getMod().trim().replace(" ",""));
                params.put("os","android");
                params.put("ov",requestModel.getOv());
                params.put("sdkVersion",requestModel.getSdkVersion());
                params.put("mcc", requestModel.getMcc());
                params.put("mnc", requestModel.getMnc());
                params.put("lac",requestModel.getLac());
                params.put("cid",requestModel.getCid());
                params.put("nt",String.valueOf(requestModel.getNt()));
                params.put("mac", requestModel.getMac());
                params.put("pt", String.valueOf(requestModel.getPt()));
                params.put("st",String.valueOf(tiggleSence));
                params.put("pl",requestModel.getPl());
                params.put("adrid",requestModel.getAdrid());
                params.put("adnum",String.valueOf(requestModel.getAdnum()));
                try {
                    params.put("ua", URLEncoder.encode(requestModel.getUa(),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                params.put("dip",requestModel.getDip());
                params.put("sign",requestModel.getSign());
                break;

            case MConstant.request_type.ni:
                params.put("dt",requestModel.getDt());
                params.put("dtv",requestModel.getDtv());
                params.put("sign",requestModel.getSign());
                break;
        }
        String url = CommonUtils.MapToString(params);
        String url_base64 = Base64.encodeToString(CommonUtils.MapToString(params).getBytes(),Base64.NO_WRAP);
        StringBuffer sb = new StringBuffer();
        LogUtils.i(MConstant.TAG,"HOST:"+ MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS)+" HB_HOST:"+MConstant.HB_HOST+" Action:"+Action);
        if (Action.equals(MConstant.request_type.ni)){
            sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
        }
        if (Action.equals(MConstant.request_type.hb)){
            if (MConstant.HB_HOST.equals("")){
                sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
            }
            else {
                sb.append(MConstant.HB_HOST);
            }
        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        sb.append("/");
        sb.append(Action);
        sb.append("?");
        sb.append(MConstant.GET_KEY);
        sb.append(isImeiTest ? url : url_base64);
        return sb.toString();
    }

    public static Map<String,String> getRequestParams2(String Action, DeviceInfo deviceInfo, int type, int tiggleSence, Context mContext){
        Map<String,String> params=new HashMap<>();
        try {
            RequestModel requestModel = getRequestModel(Action, deviceInfo, type, tiggleSence, mContext);
            params.put("action",requestModel.getAction());
            params.put("appid", requestModel.getAppid());
            params.put("ver", requestModel.getVer());
            params.put("tp", requestModel.getTp());
            params.put("is",requestModel.getIs());
            params.put("dt",requestModel.getDt());
            params.put("dtv",requestModel.getDtv());
            params.put("ic",requestModel.getIc());
            params.put("w",requestModel.getW());
            params.put("h",requestModel.getH());
            params.put("brand",requestModel.getBrand());
            params.put("mod",requestModel.getMod());
            params.put("os","android");
            params.put("ov",requestModel.getOv());
            params.put("sdkVersion",requestModel.getSdkVersion());
            params.put("mcc", requestModel.getMcc());
            params.put("mnc", requestModel.getMnc());
            params.put("lac",requestModel.getLac());
            params.put("cid",requestModel.getCid());
            params.put("nt", String.valueOf(requestModel.getNt()));
            params.put("mac", requestModel.getMac());
            params.put("pt", String.valueOf(requestModel.getPt()));
            params.put("st", String.valueOf(tiggleSence));
            params.put("pl",requestModel.getPl());
            params.put("adrid",requestModel.getAdrid());
            params.put("adnum", String.valueOf(requestModel.getAdnum()));
            try {
                params.put("ua", URLEncoder.encode(requestModel.getUa(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put("dip",requestModel.getDip());
            params.put("aaid",requestModel.getAaid());
            params.put("lon",requestModel.getLon());
            params.put("lat",requestModel.getLat());
            params.put("density",requestModel.getDensity());
            params.put("sign",requestModel.getSign());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return params;
    }

}
