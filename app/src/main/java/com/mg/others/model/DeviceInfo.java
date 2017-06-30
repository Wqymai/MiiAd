package com.mg.others.model;

import java.io.Serializable;


public class DeviceInfo implements Serializable {

    private String userAgent;
    private String androidId;
    private String imei;
    private String imsi;
    private String mac;//手机本身网卡的MAC地址
    private String bssid;//所连接的WIFI设备的MAC地址
    private int mcc;
    private int mnc;
    private int lac;
    private int cid;
    private int osVersion;//数字版本号
    private String osVersionName;
    private int screenWidth;
    private int screenHeight;
    private float screenSize;
    private String productBrand;//手机品牌
    private String productName;//手机正式（官方）名称
    private String productModel;//手机代号，手机号
    private String serialNumber;//唯一标识号
    private String iccid;

    //可能用的上
    private String kernelVersion;
    private int apkVersion;
    private String apkVersionName;
    private int otaVersion;
    private String memoryTotal; //所有可用内存
    private String fingerPrint; // 机身码
    private String buildDate; // 制作者及时间
    private String androidAdid;
    private String uuid;
    private String dip;//手机分辨率

    private String advertisingId;//Android Advertising ID
    private String density;//屏幕密度

    private String lon;//经度
    private String lat;//纬度

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
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

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("userAgent:").append(userAgent).append("\n")
                .append("androidId:").append(androidId).append("\n")
                .append("imei:").append(imei).append("\n")
                .append("imsi:").append(imsi).append("\n")
                .append("mac:").append(mac).append("\n")
                .append("mcc:").append(mcc).append("\n")
                .append("mnc:").append(mnc).append("\n")
                .append("cid:").append(cid).append("\n")
                .append("lac:").append(lac).append("\n")
                .append("osVersion:").append(osVersion).append("\n")
                .append("osVersionName:").append(osVersionName).append("\n")
                .append("kernelVersion:").append(kernelVersion).append("\n")
                .append("apkVersion:").append(apkVersion).append("\n")
                .append("apkVersionName:").append(apkVersionName).append("\n")
                .append("otaVersion:").append(otaVersion).append("\n")
                .append("screenWidth:").append(screenWidth).append("\n")
                .append("screenHeight:").append(screenHeight).append("\n")
                .append("screenSize:").append(screenSize).append("\n")
                .append("memoryTotal:").append(memoryTotal).append("\n")
                .append("productBrand:").append(productBrand).append("\n")
                .append("productName:").append(productName).append("\n")
                .append("productModel:").append(productModel).append("\n")
                .append("fingerPrint:").append(fingerPrint).append("\n")
                .append("buildDate:").append(buildDate).append("\n")
                .append("androidAdid:").append(androidAdid).append("\n")
                .append("uuid:").append(uuid).append("\n")
                .append("serialNumber:").append(serialNumber).append("\n");
        return sb.toString();
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(int osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsVersionName() {
        return osVersionName;
    }

    public void setOsVersionName(String osVersionName) {
        this.osVersionName = osVersionName;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(float screenSize) {
        this.screenSize = screenSize;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public int getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(int apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public void setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
    }

    public int getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(int otaVersion) {
        this.otaVersion = otaVersion;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getAndroidAdid() {
        return androidAdid;
    }

    public void setAndroidAdid(String androidAdid) {
        this.androidAdid = androidAdid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
