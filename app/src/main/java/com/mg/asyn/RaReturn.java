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
 * 执行过程中需要返回结果码
 */

public class RaReturn extends RequestAsync {


    public RaReturn(ReqAsyncModel model) {
        super(model);
    }

    @Override
    public  void startRequest( ){

                requestRa();

    }


    @Override
    public void requestRa(){

      try{
        if (!CommonUtils.isNetworkAvailable(mContext)){

            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }

        if (httpManager == null){
            httpManager = HttpManager.getInstance(mContext);
        }


        final  String url=httpManager.getRaUrl();
        if (url == null || url.equals("")){

            listener.onMiiNoAD(3002);
            return;
        }

        Map<String,String> params = httpManager.getSraParams(pt,appid,lid);
        if (params == null){

            listener.onMiiNoAD(3002);
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {

                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealRaSuc(response);
            }
            @Override
            public void onFail(Exception e) {

                listener.onMiiNoAD(3002);
            }
        },params);
      }catch (Exception e){
          listener.onMiiNoAD(3002);
          e.printStackTrace();
      }
    }
    @Override
    public  void dealRaSuc(HttpResponse response){
        try {
            List<AdModel> ads;
            String temp = response.entity();
            if (temp == null){

                listener.onMiiNoAD(3002);
                return;
            }

            ads = AdParser.parseAd(temp);

            if (ads == null || ads.size() <= 0){

                listener.onMiiNoAD(3002);
                return;
            }


            AdModel ad = ads.get(0);
            if (ad == null){

                listener.onMiiNoAD(3002);
                return;
            }

            Message msg=new Message();
            msg.obj = ad;
            msg.what=200;
            mainHandler.sendMessage(msg);

        }
        catch (Exception e){

            listener.onMiiNoAD(3002);//ra 解析失败
            e.printStackTrace();
        }
    }


}
