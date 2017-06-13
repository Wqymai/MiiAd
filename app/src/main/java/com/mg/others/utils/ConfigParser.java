package com.mg.others.utils;

import com.mg.comm.MConstant;

import com.mg.others.model.SDKConfigModel;

import org.json.JSONArray;
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


    public static SDKConfigModel parseConfig(String result) {

        LogUtils.i(MConstant.TAG, "parse Config = " + result);
        SDKConfigModel sdk = null;
        JSONObject object = null;
        JSONObject object_ad = null;
        JSONObject object_config;
        JSONObject object_sf;
        try {
            object = new JSONObject(result);
            if (object.getInt(RESULTCODE) == MConstant.SUC_CODE) {
                sdk = new SDKConfigModel();
                object_ad = object.optJSONObject(DATA);
                String list = object_ad.optString(LIST);
                int listType = object_ad.optInt(LIST_TYPE);
                object_config = object_ad.optJSONObject(CONFIG);
                int next = object_ad.optInt(String.valueOf(NEXT));
                int autoShowPercentage = object_ad.optInt(AUTOPERCENTAGE);

                object_sf = object_ad.optJSONObject(SF);
                int sf_mg=object_sf.optInt("1");
                int sf_gdt=object_sf.optInt("2");


                int adshow = object_config.optInt(String.valueOf(ADSHOW));
                int showPercentage = object_config.optInt(String.valueOf(PERCENTAGE));
                int banner_time = object_config.optInt(String.valueOf(BANNER_TIME));
                int interstitial_time = object_config.optInt(String.valueOf(INTERSTITIAL_TIME));
                int splash_time = object_config.optInt(String.valueOf(SPLASH_TIME));
                int interstitial_delay_time = object_config.optInt(String.valueOf
                        (INTERSTITIAL_DELAY_TIME));
                int jump = object_config.optInt(String.valueOf(JUMP));
                int jump_function = object_config.optInt(JUMP_FUNCTION);
                int show_sum = object_config.optInt(String.valueOf(SHOW_SUM));
                String bp=object_config.optString("bp");

                int timeComm=object_config.optInt("ptl");
                JSONArray code_obj = new JSONArray(object_config.optString("al"));

                Long time0 =Long.parseLong(code_obj.get(0).toString());
                Long time1 = Long.parseLong(code_obj.get(1).toString());
                Long time2 = Long.parseLong(code_obj.get(1).toString());
                Long time3 = Long.parseLong(code_obj.get(1).toString());
                Long time4 = Long.parseLong(code_obj.get(1).toString());

                int ce = object_config.optInt("ce");



                sdk.setAdShow(adshow == SDKConfigModel.AD_OPEN);
                sdk.setShow_percentage(showPercentage);

                sdk.setJump(jump == SDKConfigModel.JUMP_SHOW);
                sdk.setJump_function(jump_function);

                sdk.setInterstitial_delay_time(interstitial_delay_time);
                sdk.setBanner_time(banner_time);
                sdk.setSplash_time(splash_time);
                sdk.setInterstitial_time(interstitial_time);
                sdk.setNext(next);
                sdk.setAuto_show_percentage(autoShowPercentage);
                sdk.setList(list);
                sdk.setListType(listType);
                sdk.setShow_sum(show_sum);
                //设置个场景冷却时间和点击动作
                sdk.setCe(ce);
                sdk.setTime0(time0);
                sdk.setTime1(time1);
                sdk.setTime2(time2);
                sdk.setTime3(time3);
                sdk.setTime4(time4);
                sdk.setTimeComm(timeComm);
                sdk.setBp(bp);

                sdk.setSf_mg(sf_mg);
                sdk.setSf_gdt(sf_gdt);

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            sdk = null;
        }
        return sdk;
    }


}
