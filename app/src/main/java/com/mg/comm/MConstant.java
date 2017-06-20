package com.mg.comm;

import android.os.Environment;

import com.mg.others.v4.IdRes;


public final   class MConstant {
    public static final String TAG="ci";
    public static final String DEVICE_FILE_NAME = "mDevice";
    public static final String CONFIG_FILE_NAME = "mConf";
    public static final String Dir_TEMP = Environment.getExternalStorageDirectory() + "/mmFolder/";
    public static final String Dir = "Apk";
    public static final String MSDK_VERSION = "2000";
    public static final int SUC_CODE = 1;
    public static  boolean isBlack=false;
    //http://117.135.144.63:8081/index.php/GD/VSGD";
    public static final String VGD = "NEFCMTMxQThBMUM3RUUxNjA1NDcxNEQ0RDBCMzY3OENBOTQwMjU5QTM2NDg4RjQ3QTc1NDY3REFEMDc3QTg4NkM1MTY4NUI3MTE0RTBENENGOUY4QUEwRTMyODgxOUM0";
    //测试初始化最低间隔时间
    public static final long DIFF_NI = 60;
//    public static final String APPID = "A39AFB45-A5AE-5CF5-77DF-A861DE4770FF";

    public static final String APPID = "496823C4-E315-AC93-BCE0-77F1D3EDE751";

    public static String HOST;
    static {
        if(false){
            //测试服务器地址
            //http://vs.maimob.net";
            HOST = "MzUwQkY1QzUwMkQ0QTZEQUZCMTU3REQ0MUY1OTBFOUYxNEQxNjNBMjU4NTQzQTkwODdEREUxM0VFQUJGQTlGNw==";

        }
        else{
            //正式服务器地址
            //http://s1.pagefarmer.net";
            HOST= "QTBBMkRGQ0U0QzRCNUM4QTZDRDlGQTQ4NEY0MkIxRDE3NkVERTBBNERCMTk3RkIzQjdDMUM0QjQ4QUM2MDAzQg==";
        }
    }
    public static String HB_HOST="";
    //"/index.php/VSAPI";
    public static String SUFFIX = "QjM5NzdCMEMzOTVBOTE3MDdENDdCN0FBQjAyRkU4ODM2RDQ3MUYyNzg5MDExNUU4MDkyQzA0QTVEOTg2NjkyNQ==";
    public static final String GET_KEY = "m=";

    public static final String PRODUCT_BRAND = "ro.product.brand";
    public static final String PRODUCT_NAME = "ro.product.name";
    public static final String PRODUCT_MODEL = "ro.product.model";
    public static final String FINGER_PRINT = "ro.build.fingerprint";
    public static final String VERSION_SDK = "ro.build.version.sdk";
    public static final String VERSION_RELEASE = "ro.build.version.release";
    public static final String BUILD_DATE = "ro.build.date";
    public static final String BUILD_DATE_UTC = "ro.build.date.utc";
    public static final String CPU_ID = "ro.boot.cpuid";
    public static final String VENDOR = "ro.btconfig.vendor";
    public static final String TIME_ZONE = "persist.sys.timezone";
    public static final String COUNTRY = "persist.sys.country";
    public static final String LANGUAGE = "persist.sys.language";
    public static final String VM_LIB = "persist.sys.dalvik.vm.lib";
    public static final String DESCRIPTION = "ro.build.description";
    public static final String FIRST_BOOT = "ro.runtime.firstboot";
    public static final String RO_SERIALNO = "ro.serialno";

    public static final Boolean MTest = false;
    public static final Boolean ENCODE_TEST = false;

    public static final class what{
        public static final int time_change = 1;
        public static final int ad_loaded = 2;
        public static final int ad_resume = 3;
        public static final int ad_pause = 4;
        public static final int ad_remove = 5;
        public static final int image_loaded = 6;
        public static final int ads_result = 7;
        public static final int delay_show = 8;
        public static final int update_config = 9;
        public static final int show_notNormalAd_finish = 10;
        public static final int h5_splash_show=11;
    }




    public static final class id{
        @IdRes
        public static final int pastTime = 2;
        @IdRes
        public static final int adcontent =3;
        @IdRes
        public static final int clsoebutton = 1;
        @IdRes
        public static final int adwebview = 4;
        @IdRes
        public static final int tvjump = 5;
        @IdRes
        public static final int frame = 6;

    }

    public static final class adClickType{
        public static final int app = 1;
        public static final int web = 3;
    }

    public static final class sence{
        public static final String screen_off = "screen_off";
        public static final String screen_on = "screen_on";
        public static final String install = "install";
        public static final String uninstall = "uninstall";
        public static final String normal = "normal";
        public static final String battery_change = "battery_change";
        public static final String user_present="user_present";
    }

    public static final class request_type{
        public static final String hb = "HB";
        public static final String ra = "RA";
        public static final String ni = "NI";
        public static final String p = "P";
        public static final String vgd = "VGD";
    }

    public static final class jump{
        public static final int click = 0;
        public static final int normal = 1;
    }

    public static final class showjump{
        public static final int show = 1;
        public static final int hide = 0;
    }

    public static final class key{
        public static final String ADS_DATA = "as_data";
        public static final String ADTYPE = "asType";
    }


//    public static final String APPID = "1101152570";
//    public static final String BannerPosID = "9079537218417626401";
//    public static final String APPWallPosID = "9007479624379698465";
//    public static final String InterteristalPosID = "8575134060152130849";
//    public static final String SplashPosID = "8863364436303842593";
//    public static final String GridAppWallPosID = "9007479624379698465";
//    public static final String NativePosID = "5000709048439488";
//    public static final String NativeVideoPosID = "2050206699818455";
}
