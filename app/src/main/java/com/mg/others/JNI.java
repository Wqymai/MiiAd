package com.mg.others;

/**
 * Created by wuqiyan on 17/12/4.
 */

public class JNI {
    static {
        System.loadLibrary("cencrypt");
    }
    public native String encodeInC(String action,String appid,String ver,String tp,String dt,String dtv,String w,String h,String sver);
}
