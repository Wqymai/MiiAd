package com.mg.others.utils;


import com.mg.comm.MConstant;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdParser {


    public static final String RESULTCODE = "resultCode";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    public static final String AID = "aid";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String IMG = "img";

    public static final String URL = "url";
    public static final String ICON = "icon";
    public static final String CATEGORY = "category";
    public static final String PK = "pk";
    public static final String FILESIZE = "fileSize";
    public static final String TYPE = "type";
    public static final String PAGE="page";
    public static final String PT = "pt";
    public static final String ET = "et";
    public static final String CB = "cb";
    public static final String PV = "pv";
    public static final String C = "c";
    public static final String DS = "ds";
    public static final String DE = "de";
    public static final String IS = "is";
    public static final String IE = "ie";
    public static final String A = "a";


    public static List<AdModel> parseAd(String response){
        List<AdModel> adModels = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int resultCode = jsonObject.optInt(RESULTCODE);
            if (resultCode == MConstant.SUC_CODE){
                adModels = new ArrayList<>();
                JSONArray array_data = jsonObject.optJSONArray(DATA);
                for (int i = 0; i < array_data.length(); i++) {
                    AdModel ad = new AdModel();
                    JSONObject object_ad = (JSONObject) array_data.opt(i);
                    String aid = (String) object_ad.opt(AID);
                    String name = (String) object_ad.opt(NAME);
                    String title = (String) object_ad.opt(TITLE);
                    String desc = object_ad.optString(DESC);
                    String img = object_ad.optString(IMG);
                    String pk = object_ad.optString(PK);
                    String category = object_ad.optString(CATEGORY);
                    int type = object_ad.optInt(TYPE);
                    String page=object_ad.optString(PAGE);
                    String icon = object_ad.optString(ICON);
                    String url = object_ad.optString(URL);
                    int pt = object_ad.optInt(PT);
                    int et = object_ad.optInt(ET);

                    JSONObject object_cb = object_ad.optJSONObject(CB);
                    JSONArray pv = object_cb.optJSONArray(PV);
                    JSONArray c  = object_cb.optJSONArray(C);
                    JSONArray ds = object_cb.optJSONArray(DS);
                    JSONArray de = object_cb.optJSONArray(DE);
                    JSONArray ie = object_cb.optJSONArray(IE);
                    JSONArray a  = object_cb.optJSONArray(A);

                    String[] array_pv = parserCb(pv);
                    String[] array_c  = parserCb(c);
                    String[] array_ds = parserCb(ds);
                    String[] array_de = parserCb(de);
                    String[] array_ie = parserCb(ie);
                    String[] array_a  = parserCb(a);

                    AdReport adReport = new AdReport();
                    adReport.setUrlClick(array_c);
                    adReport.setUrlDownloadComplete(array_de);
                    adReport.setUrlDownloadStart(array_ds);
                    adReport.setUrlInstallComplete(array_ie);
                    adReport.setUrlShow(array_pv);
                    adReport.setUrlOpen(array_a);

                    ad.setUrl(url);
                    ad.setCategory(category);
                    ad.setDesc(desc);
                    ad.setIcon(icon);
                    ad.setId(aid);
                    ad.setImage(img);
                    ad.setName(name);
                    ad.setPkName(pk);
                    ad.setType(type);
                    ad.setPage(page);
                    ad.setTitle(title);
                    ad.setReportBean(adReport);
                    ad.setPt(pt);
                    ad.setEt(et);
                    adModels.add(ad);
                }
            }else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            adModels = null;
        }
        return adModels;
    }

    public static String[] parserCb(JSONArray array){
        String[] strings = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            try {
                strings[i] = array.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }

}
