package com.mg.others.task;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import com.mg.comm.MConstant;
import com.mg.others.model.DeviceInfo;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.SystemProperties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * 异步初始化用户信息Task
 */
public class DeviceInfoTask extends MTask<Void, Void, DeviceInfo> {

    private DeviceInfo mDeviceInfo;
    private IDeviceInfoListener mListener;
    private Context mContext;
    private int count = 0;


    public DeviceInfoTask(IDeviceInfoListener mListener, Context mContext) {
        this.mListener = mListener;
        this.mContext = mContext;
    }


    @Override
    protected DeviceInfo doInBackground(Void... params) {
        try {

            String androidId = "";
            String androidAdid = "";
            String imei = "";
            String imsi = "";
            String mac = "";
            int mcc = -1;
            int mnc = -1;
            int cid = -1;
            int lac = -1;
            int osVersion = -1;
            String osVersionName = "";
            int apkVersion = -1;
            String apkVersionName = "";
            int otaVersion = -1;
            int screenWidth = -1;
            int screenHeight = -1;
            float screenSize = -1.0f;
            String memoryTotal = "";
            String kernelVersion = "";
            String productBrand = "";
            String productName = "";
            String productModel = "";
            String fingerPrint = "";
            String buildDate = "";
            String serialNumber = "";
            String uuid = "";
            String iccid = "";
            float dip = 0f;
            float density = 0f;
            String advertisingId = "";
            double lon = 0;
            double lat=0;


            androidId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            //imei
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            //imsi
            imsi = tm.getSubscriberId();
            //iccid
            iccid = tm.getSimSerialNumber();
//            //inet_mac
//            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifi.getConnectionInfo();
//            mac = wifiInfo.getMacAddress();
            //mcc mnc
            String networkOperator = tm.getNetworkOperator();
            if (TextUtils.isEmpty(networkOperator)) {
                try {
                    mcc = Integer.parseInt(networkOperator.substring(0, 3));
                    mnc = Integer.parseInt(networkOperator.substring(3));
                } catch (StringIndexOutOfBoundsException e) {
//                e.printStackTrace();
                } catch (NullPointerException e) {
//                e.printStackTrace();
                }
            }

            // lac cid
            int type = tm.getPhoneType();
            try {
                if (type == TelephonyManager.PHONE_TYPE_CDMA) {
                    CdmaCellLocation cdma = (CdmaCellLocation) tm.getCellLocation();
                    lac = cdma.getNetworkId();
                    cid = cdma.getBaseStationId();
                } else if (type == TelephonyManager.PHONE_TYPE_GSM) {
                    GsmCellLocation gsm = (GsmCellLocation) tm.getCellLocation();
                    lac = gsm.getLac();
                    cid = gsm.getCid();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            //osVersion
            osVersion = Build.VERSION.SDK_INT;
            //osVersionName
            osVersionName = Build.VERSION.RELEASE;
            //apkVersion
            try {
                PackageInfo packageInfo = mContext.getPackageManager()
                        .getPackageInfo(mContext.getPackageName(), 0);
                if (null != packageInfo) {
                    apkVersion = packageInfo.versionCode;
                    apkVersionName = packageInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            }

            //screen_width
            //screen_height
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;

            float sWidth = metrics.widthPixels / metrics.xdpi;
            float sHeight = metrics.heightPixels / metrics.ydpi;
            screenSize = Math.round(Math.sqrt(Math.pow(sWidth, 2) + Math.pow(sHeight, 2)));

            //memory total
            FileInputStream mtFis = null;
            BufferedReader mtReader = null;
            try {
                mtFis = new FileInputStream("/proc/meminfo");
                mtReader = new BufferedReader(new InputStreamReader(mtFis));
                String memoryInfos = mtReader.readLine();
                if (!TextUtils.isEmpty(memoryInfos)) {
                    String[] values = memoryInfos.split(":");
                    if (null != values && values.length > 1 && !TextUtils.isEmpty(values[1])) {
                        memoryTotal = values[1].trim();
                    }
                }

            } catch (FileNotFoundException e) {
                LogUtils.i(MConstant.TAG, "get memory total error, FileNotFoundException :" + e);
            } catch (IOException e) {
                LogUtils.i(MConstant.TAG, "get memory total error, IOException :" + e);
            } finally {
                if (null != mtReader) {
                    try {
                        mtReader.close();
                    } catch (IOException e) {
                        LogUtils.i(MConstant.TAG, "get memory total close reader error, IOException :" + e);
                    }
                }
            }

            //kernelVersion
            FileInputStream kvFis = null;
            BufferedReader kvReader = null;
            try {
                kvFis = new FileInputStream("/proc/version");
                kvReader = new BufferedReader(new InputStreamReader(kvFis));
                kernelVersion = kvReader.readLine();
            } catch (FileNotFoundException e) {
                LogUtils.i(MConstant.TAG, "get kernel version error, FileNotFoundException :" + e);
            } catch (IOException e) {
                LogUtils.i(MConstant.TAG, "get kernel version error, IOException :" + e);
            } finally {
                if (null != kvReader) {
                    try {
                        kvReader.close();
                    } catch (IOException e) {
                        LogUtils.i(MConstant.TAG, "get kernel version close reader error, IOException :" + e);
                    }
                }
            }

            productBrand = android.os.Build.BRAND;
            productName = SystemProperties.get(MConstant.PRODUCT_NAME);
            productModel = Build.MODEL;
            fingerPrint = SystemProperties.get(MConstant.FINGER_PRINT);
            buildDate = SystemProperties.get(MConstant.FINGER_PRINT);
            serialNumber = SystemProperties.get(MConstant.RO_SERIALNO);

            //uuid
            uuid = android.util.Base64.encodeToString((screenWidth + screenHeight + imei)
                            .getBytes(),
                    android.util.Base64.NO_WRAP);


            DisplayMetrics metric = mContext.getResources().getDisplayMetrics();
            dip = metric.density;
            density = metric.densityDpi;

            try {
                AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
                advertisingId = adInfo.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

               LoactionHelper.LocModel location = LoactionHelper.GetUserLocation(mContext);
               lat = location.lat;
               lon = location.lon;

            }
            catch (Exception e){
                e.printStackTrace();
            }

            mDeviceInfo.setAndroidId(androidId);
            mDeviceInfo.setAndroidAdid(androidAdid);
            mDeviceInfo.setImei(imei);
            mDeviceInfo.setImsi(imsi);
            mDeviceInfo.setMac(getMacAddress());
            mDeviceInfo.setMcc(mcc);
            mDeviceInfo.setMnc(mnc);
            mDeviceInfo.setOsVersion(osVersion);
            mDeviceInfo.setOsVersionName(osVersionName);
            mDeviceInfo.setApkVersion(apkVersion);
            mDeviceInfo.setApkVersionName(apkVersionName);
            mDeviceInfo.setOtaVersion(otaVersion);
            mDeviceInfo.setScreenWidth(screenWidth);
            mDeviceInfo.setScreenHeight(screenHeight);
            mDeviceInfo.setScreenSize(screenSize);
            mDeviceInfo.setMemoryTotal(memoryTotal);
            mDeviceInfo.setKernelVersion(kernelVersion);
            mDeviceInfo.setProductBrand(productBrand);
            mDeviceInfo.setProductName(productName);
            mDeviceInfo.setProductModel(productModel);
            mDeviceInfo.setFingerPrint(fingerPrint);
            mDeviceInfo.setBuildDate(buildDate);
            mDeviceInfo.setSerialNumber(serialNumber);
            mDeviceInfo.setUuid(uuid);
            mDeviceInfo.setLac(lac);
            mDeviceInfo.setCid(cid);
            mDeviceInfo.setIccid(iccid);
            mDeviceInfo.setDip(String.valueOf(dip));
            mDeviceInfo.setDensity(String.valueOf(density));
            mDeviceInfo.setAdvertisingId(advertisingId);
            mDeviceInfo.setLon(String.valueOf(lon));
            mDeviceInfo.setLat(String.valueOf(lat));

      }
      catch (Exception e){
          e.printStackTrace();
      }
        return mDeviceInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        WebView webView = new WebView(mContext);
        String userAgent = webView.getSettings().getUserAgentString();
        mDeviceInfo = new DeviceInfo();
        mDeviceInfo.setUserAgent(userAgent);

    }

    @Override
    protected void onPostExecute(DeviceInfo deviceInfo) {
        super.onPostExecute(deviceInfo);
        if (null != mListener){
            mListener.deviceInfoLoaded(deviceInfo);
        }
    }

    public  String getMacAddress(){
 /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }



}
