package com.mg.others.utils;

import com.mg.comm.MConstant;
import com.mg.others.model.SDKConfigModel;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfigParser {


    public static final String RESULTCODE = "resultCode";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    private static final String CONFIG = "c";
    private static final String LIST_TYPE = "o";
    private static final String LIST = "l";
    private static final String NEXT = "n";
    private static final String AUTOPERCENTAGE = "cr";

    private static final String ADSHOW = "o";
    private static final String PERCENTAGE = "p";
    private static final String SPLASH_TIME = "kt";
    private static final String INTERSTITIAL_TIME = "ct";
    private static final String BANNER_TIME = "bt";
    private static final String INTERSTITIAL_DELAY_TIME = "cl";
    private static final String JUMP = "sk";
    private static final String JUMP_FUNCTION = "bu";
    private static final String SHOW_SUM = "at";
    private static final String INTERVAL = "al";
    private static final String WP = "wp";
    private static final String OP = "op";
    private static final String SF="sf";


    private static final String C ="c";
    private static final String O ="o";
    private static final String N ="n";
    private static final String KT ="kt";
    private static final String AT ="at";
    private static final String CE ="ce";
    private static final String CZ ="cz";
    private static final String KSF ="ksf";
    private static final String XSF ="xsf";
    private static final String BSF ="bsf";
    private static final String CSF ="csf";
    private static final String SK ="sk";






    public static SDKConfigModel parseConfig(String result) {


        SDKConfigModel sdk = null;
        JSONObject object = null;
        JSONObject object_data = null;
        JSONObject object_c;
        JSONObject object_ksf;
        JSONObject object_xsf;
        JSONObject object_bsf;
        JSONObject object_csf;
        try {
            object = new JSONObject(result);
            if (object.getInt(RESULTCODE) == MConstant.SUC_CODE) {
                sdk = new SDKConfigModel();
                object_data = object.optJSONObject(DATA);


                //下次初始化时间段
                int n = object_data.optInt(String.valueOf(N));
                //是否能做广告
                String o = object_data.optString(O);
                //广告配置
                object_c = object_data.optJSONObject(C);


                //开屏广告展示时间
                int kt = object_c.optInt(KT);
                //广告一天可以展示的总次数
                int at = object_c.optInt(AT);
                //点击广告后的行为
                int ce = object_c.optInt(CE);
                //插屏图片宽度占屏幕的百分比
                int cz = object_c.optInt(CZ);

                //SDK KEY（SDK申请的aped，广告位ID）,广点通的
                String sk = object_c.optString(SK);

                //各种广告类型的SDK来源配置
                object_ksf = object_c.optJSONObject(KSF);
                object_xsf = object_c.optJSONObject(XSF);
                object_bsf = object_c.optJSONObject(BSF);
                object_csf = object_c.optJSONObject(CSF);

                //开屏SDK来源配置
                int ksf_mg=object_ksf.optInt("1");
                int ksf_gdt=object_ksf.optInt("2");
                //信息流SDK来源配置
                int xsf_mg=object_xsf.optInt("1");
                int xsf_gdt=object_xsf.optInt("2");
                //bannerSDK来源配置
                int bsf_mg=object_bsf.optInt("1");
                int bsf_gdt=object_bsf.optInt("2");
                //插屏SDK来源配置
                int csf_mg=object_csf.optInt("1");
                int csf_gdt=object_csf.optInt("2");

                sdk.setAdShow(o);
                sdk.setNext(n);
                sdk.setSplash_time(kt);
                sdk.setShow_sum(at);
                sdk.setCe(ce);
                sdk.setCz(cz);
                sdk.setSk(sk);
                sdk.setKsf_mg(ksf_mg);
                sdk.setKsf_gdt(ksf_gdt);
                sdk.setXsf_mg(xsf_mg);
                sdk.setXsf_gdt(xsf_gdt);
                sdk.setBsf_mg(bsf_mg);
                sdk.setBsf_gdt(bsf_gdt);
                sdk.setCsf_mg(csf_mg);
                sdk.setCsf_gdt(csf_gdt);
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            sdk = null;
        }
        return sdk;
    }


}
