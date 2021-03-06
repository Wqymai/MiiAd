package com.mg.asyn;

import android.os.Message;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.SP;

import java.util.List;
import java.util.Map;


/**
 * Created by wuqiyan on 17/6/15.
 * 执行过程中不需要返回结果码,如果发生错误发送消息400,如果成功就发送消息200
 */

public class RaNoReturn extends RequestAsync {


    public RaNoReturn(ReqAsyncModel model) {
        super(model);
    }

    @Override
    public  void startRequest( ){
      try {

//        if (!checkSDKConfigModel()){
//            requestHb();
//        }
//        else {
//
//            if (checkHbTime()){
//
//                if (checkNumber()){
//
//                    mainHandler.sendEmptyMessage(400);
//
//                    return;
//                }
//                if (checkADShow()){
//
//                    mainHandler.sendEmptyMessage(400);
//
//                    return;
//
//                }
                requestRa();
//                return;
//            }
//            requestHb();
//        }
      }catch (Exception e){
          mainHandler.sendEmptyMessage(400);
          e.printStackTrace();
      }
    }
//    @Override
//    public void requestHb( ){
//      try {
//
//
//        if (!CommonUtils.isNetworkAvailable(mContext)){
//
//            mainHandler.sendEmptyMessage(400);
//
//            return;
//        }
//        if (httpManager==null){
//            httpManager= HttpManager.getInstance(mContext);
//        }
//
//        HttpUtils httpUtils = new HttpUtils(mContext);
//        final String url = httpManager.getParams(NI, 0, 0,appid);
//        if (url == null||url.equals("")){
//
//            mainHandler.sendEmptyMessage(400);
//
//            return;
//        }
//        httpUtils.get(url, new HttpListener() {
//            @Override
//            public void onSuccess(HttpResponse response) {
//                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
//                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
//                dealHbSuc(response);
//            }
//
//            @Override
//            public void onFail(Exception e) {
//
//                mainHandler.sendEmptyMessage(400);
//            }
//        });
//      }catch (Exception e){
//          mainHandler.sendEmptyMessage(400);
//          e.printStackTrace();
//      }
//    }
//
//    @Override
//    public void dealHbSuc(HttpResponse response ){
//        try {
//            SDKConfigModel sdk = null;
//
//            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
//
//            if (data == null){
//
//                mainHandler.sendEmptyMessage(400);
//
//                return;
//            }
//            sdk = ConfigParser.parseConfig(data);
//
//            if (sdk == null){
//
//                mainHandler.sendEmptyMessage(400);
//
//                return;
//            }
//
//            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);
//
//            checkReShowCount();
//
//            if (checkNumber()){
//
//                mainHandler.sendEmptyMessage(400);
//
//                return;
//            }
//            if (checkADShow()){
//
//                mainHandler.sendEmptyMessage(400);
//
//                return;
//
//            }
//
//            requestRa();
//        }
//        catch (Exception e){
//
//            mainHandler.sendEmptyMessage(400);
//            e.printStackTrace();
//        }
//    }

    @Override
    public void requestRa( ){

      try {


        if (!CommonUtils.isNetworkAvailable(mContext)){

            mainHandler.sendEmptyMessage(400);

            return;
        }

        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext);
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl();
        if (url == null || url.equals("")){

            mainHandler.sendEmptyMessage(400);

            return;
        }

        Map<String,String> params=httpManager.getSraParams(pt,appid,lid);

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {

                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealRaSuc(response);
            }
            @Override
            public void onFail(Exception e) {

               mainHandler.sendEmptyMessage(400);

            }
        },params);
      }catch (Exception e){
          mainHandler.sendEmptyMessage(400);
          e.printStackTrace();
      }
    }

    @Override
    public  void dealRaSuc(HttpResponse response ){
        try {
            List<AdModel> ads;
            String temp = response.entity();
            if (temp == null){

                mainHandler.sendEmptyMessage(400);

                return;
            }

            ads = AdParser.parseAd(temp);

            if (ads == null || ads.size() <= 0){

                mainHandler.sendEmptyMessage(400);

                return;
            }

            AdModel ad = ads.get(0);
            if (ad == null){

                mainHandler.sendEmptyMessage(400);

                return;
            }

            Message msg=new Message();
            msg.obj = ad;
            msg.what = 200;
            mainHandler.sendMessage(msg);

        }
        catch (Exception e){

            mainHandler.sendEmptyMessage(400);

            e.printStackTrace();
        }
    }

}


