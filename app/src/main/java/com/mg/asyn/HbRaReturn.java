package com.mg.asyn;

import android.os.Message;
import android.util.Base64;

import com.mg.comm.MConstant;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;

import java.util.List;
import java.util.Map;

import static com.mg.others.manager.HttpManager.NI;
import static com.mg.others.manager.HttpManager.RA;

/**
 * Created by wuqiyan on 17/6/15.
 * 执行过程中需要返回结果码
 */

public class HbRaReturn extends RequestAsync {


    public HbRaReturn(ReqAsyncModel model) {
        super(model);
    }

    @Override
    public  void startRequest( ){

        if (! checkSDKConfigModel()){//缓存中sdk为空

            requestHb();

        }else {//缓存中sdk不为空

            if(checkHbTime()) {

                if (checkNumber()){

                    listener.onMiiNoAD(3004);
                    return;
                }
                if (checkADShow()){

                    listener.onMiiNoAD(3003);
                    return;
                }
                requestRa();

            }
            else {
                requestHb();
            }
        }

    }
    @Override
    public void requestHb(){

        if (!CommonUtils.isNetworkAvailable(mContext)){

            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }
        if (httpManager == null){
            httpManager= HttpManager.getInstance(mContext);
        }


        final String url = httpManager.getParams(NI, 0, 0,appid);

        if (url == null||url.equals("")){

            listener.onMiiNoAD(3001);
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                dealHbSuc(response);
            }

            @Override
            public void onFail(Exception e) {

                listener.onMiiNoAD(3001);
            }
        });

    }
    @Override
    public void dealHbSuc(HttpResponse response ){
        try {
            SDKConfigModel sdk = null;

            String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

            if (data == null){
                listener.onMiiNoAD(3001);
                return;
            }
            sdk = ConfigParser.parseConfig(data);

            if (sdk == null){
                listener.onMiiNoAD(3001);
                return;
            }


            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

            checkReShowCount();

            LogUtils.i(MConstant.TAG,"come here...1");

            if (checkNumber()){
                LogUtils.i(MConstant.TAG,"checkNumber...");
                listener.onMiiNoAD(3004);
                return;
            }
            if (checkADShow()){
                LogUtils.i(MConstant.TAG,"checkADShow...");
                listener.onMiiNoAD(3003);
                return;
            }
            LogUtils.i(MConstant.TAG,"come here...2");
            requestRa();
        }
        catch (Exception e){
            listener.onMiiNoAD(3001);//hb解析失败
            e.printStackTrace();
        }
    }

    @Override
    public void requestRa(){
        if (!CommonUtils.isNetworkAvailable(mContext)){

            listener.onMiiNoAD(3000);//未检测到网络
            return;
        }

        if (httpManager==null){
            httpManager = HttpManager.getInstance(mContext);
        }


        final  String url=httpManager.getRaUrl(RA);
        if (url == null || url.equals("")){
            listener.onMiiNoAD(3002);
            return;
        }

        Map<String,String> params=httpManager.getParams2(RA,pt,0,appid);
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
    }
    @Override
    public  void dealRaSuc(HttpResponse response){
        try {
            List<AdModel> ads;
            String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
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
