package com.mg.demo;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.mg.comm.MConstant;
import com.mg.comm.MiiADListener;
import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.model.AdModel;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
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
 * Created by wuqiyan on 2017/6/14.
 */

public class ParentAsync {

    protected Handler mainHandler;
    protected Context mContext;
    protected HttpManager httpManager=null;
    protected MiiADListener listener=null;
    protected int pt;
    public ParentAsync(Context context, Handler handler, int pt, MiiADListener listener){
        this.mainHandler=handler;
        this.mContext=context;
        this.listener=listener;
        this.pt=pt;
    }

    public void fetchMGAD(final boolean isFirst){

        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {


                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);

                    startRequest(isFirst);
                }
            }, mContext).execute();
        }
        else {
            startRequest(isFirst);
        }

    }
    protected  SDKConfigModel sdkConfigModel;
    protected boolean checkSDKConfigModel(){
         sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

        if (sdkConfigModel == null){
            return false;
        }
        return true;
    }
    protected boolean checkHbTime(){
        long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);

        long currTime = System.currentTimeMillis();

        int next = sdkConfigModel.getNext();

        if (((currTime - hbTime)/1000) < next){
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean checkNumber(){
        int real_num = (int) SP.getParam(SP.CONFIG, mContext, SP.FOT, 0);//广告已展示的次数
        int sdk_num = sdkConfigModel.getShow_sum();
        if (real_num >= sdk_num){

            return true;
        }
        return false;

    }
    protected boolean checkADShow(){
        if (!sdkConfigModel.isAdShow()){
            return true;
        }
        return false;
    }
    protected void checkShowCount(){
        long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);
        long currentTime = System.currentTimeMillis();
        if (httpManager.DateCompare(writeTime) || writeTime == 0l){
            SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
        }
    }
    protected  void startRequest(final boolean isFirst){

    }
    protected void requestHb(final boolean isFirst){

    }
    protected void dealHbSuc(HttpResponse response, boolean isFirst){

    }

    protected void requestRa(boolean shouldReturn){

    }
    protected  void dealRaSuc(HttpResponse response,boolean shouldReturn){

    }

    class child1 extends ParentAsync{

        public child1(Context context, Handler handler, int pt, MiiADListener listener) {
            super(context, handler, pt, listener);
        }


        public  void startRequest(final boolean isFirst){//isFirst 参数无用
            if (! checkSDKConfigModel()){

                requestHb(isFirst);

            }else {

                if(! checkHbTime()){
                    LogUtils.i(MConstant.TAG,"need hb");
                    requestHb(isFirst);
                }

            }
        }
        public void requestHb(final boolean isFirst){//isFirst 参数无用
            if (!CommonUtils.isNetworkAvailable(mContext)){
                LogUtils.i(MConstant.TAG,"network error");
                return;
            }
            if (httpManager == null){
                httpManager= HttpManager.getInstance(mContext);
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = httpManager.getParams(NI, 0, 0);

            if (url == null||url.equals("")){
                LogUtils.i(MConstant.TAG,"url="+url);
                return;
            }
            httpUtils.get(url, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealHbSuc(response,false);
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        }
        public void dealHbSuc(HttpResponse response, boolean isFirst){//isFirst 参数无用
            try {
                SDKConfigModel sdk = null;

                String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

                if (data == null){
                    return;
                }
                sdk = ConfigParser.parseConfig(data);

                if (sdk == null){
                    return;
                }
                LogUtils.i(MConstant.TAG,"hb res："+sdk.toString());

                CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

                checkShowCount();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public void requestRa(boolean shouldReturn){

        }
        public  void dealRaSuc(HttpResponse response){

        }

    }


    class child2 extends ParentAsync{


        public child2(Context context, Handler handler, int pt, MiiADListener listener) {
            super(context, handler, pt, listener);
        }


        public  void startRequest(final boolean isFirst){

            if (! checkSDKConfigModel()){
                requestHb(isFirst);
            }else {
                if (isFirst){
                    return;
                }

                if(checkHbTime()) {

                    if (checkNumber()){

                        LogUtils.i(MConstant.TAG,"startRequest real_num > sdk_num");
                        listener.onMiiNoAD(3004);
                        return;
                    }
                    if (checkADShow()){

                        listener.onMiiNoAD(3003);
                        return;
                    }
                    requestRa(false);

                    return;
                }
                else {
                    LogUtils.i(MConstant.TAG,"need hb");
                    requestHb(false);
                }
            }

        }
        public void requestHb(final boolean isFirst){

            if (!CommonUtils.isNetworkAvailable(mContext)){
                LogUtils.i(MConstant.TAG,"network error");
                listener.onMiiNoAD(3000);//未检测到网络
                return;
            }
            if (httpManager == null){
                httpManager= HttpManager.getInstance(mContext);
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = httpManager.getParams(NI, 0, 0);
            if (url == null||url.equals("")){
                Log.i(Constants.TAG,"url="+url);
                listener.onMiiNoAD(3001);
                return;
            }
            httpUtils.get(url, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealHbSuc(response,isFirst);
                }

                @Override
                public void onFail(Exception e) {

                    listener.onMiiNoAD(3001);
                }
            });

        }
        public void dealHbSuc(HttpResponse response, boolean isFirst){
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
                LogUtils.i(MConstant.TAG,"hb res："+sdk.toString());

                CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

                checkShowCount();

                if (isFirst){
                    mainHandler.sendEmptyMessage(100);
                    return;
                }

                if (checkNumber()){

                    listener.onMiiNoAD(3004);
                    return;
                }
                if (checkADShow()){

                    listener.onMiiNoAD(3003);
                    return;
                }

                requestRa(false);
            }
            catch (Exception e){
                listener.onMiiNoAD(3001);//hb解析失败
                e.printStackTrace();
            }
        }

        public void requestRa(boolean shouldReturn){
            if (!CommonUtils.isNetworkAvailable(mContext)){

                listener.onMiiNoAD(3000);//未检测到网络
                return;
            }

            if (httpManager==null){
                httpManager = HttpManager.getInstance(mContext);
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final  String url=httpManager.getRaUrl(RA);
            if (url == null || url.equals("")){
                listener.onMiiNoAD(3002);
                return;
            }

            Map<String,String> params=httpManager.getParams2(RA,pt,0);//暂时写的

            httpUtils.post(url.trim(), new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {

                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                    dealRaSuc(response,false);
                }
                @Override
                public void onFail(Exception e) {

                    listener.onMiiNoAD(3002);
                }
            },params);
        }
        public  void dealRaSuc(HttpResponse response,boolean shouldReturn){
            try {
                List<AdModel> ads;
                String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
                if (temp==null){
                    return;
                }

                ads = AdParser.parseAd(temp);

                if (ads == null || ads.size() <= 0){
                    listener.onMiiNoAD(3002);//ra 解析失败
                    return;
                }


                AdModel ad = ads.get(0);
                if (ad == null){
                    listener.onMiiNoAD(3002);//ra 解析失败
                    return;
                }

                Message msg=new Message();
                msg.obj = ad;
                msg.what=200;//ra访问成功
                mainHandler.sendMessage(msg);

            }
            catch (Exception e){
                listener.onMiiNoAD(3002);//ra 解析失败
                e.printStackTrace();
            }
        }

    }
    class child3 extends ParentAsync{

        public child3(Context context, Handler handler, int pt, MiiADListener listener) {
            super(context, handler, pt, listener);
        }


        public  void startRequest(final boolean shouldReturn){
            SDKConfigModel sdkConfigModel = CommonUtils.readParcel(mContext,MConstant.CONFIG_FILE_NAME);

            if (!checkSDKConfigModel()){
                requestHb(shouldReturn);
            }
            else {


                if (checkHbTime()){



                    if (checkNumber()){


                        if (shouldReturn){
                            listener.onMiiNoAD(3004);
                        }
                        else {
                            mainHandler.sendEmptyMessage(400);
                        }
                        return;
                    }
                    if (checkADShow()){

                        if (shouldReturn){
                            listener.onMiiNoAD(3003);
                        }
                        else {
                            mainHandler.sendEmptyMessage(400);
                        }
                        return;

                    }
                    requestRa(shouldReturn);
                    return;
                }
                requestHb(shouldReturn);
            }
        }
        public void requestHb(final boolean shouldReturn){
            if (!CommonUtils.isNetworkAvailable(mContext)){

                if (shouldReturn){
                    listener.onMiiNoAD(3000);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }
            if (httpManager==null){
                httpManager= HttpManager.getInstance(mContext);
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = httpManager.getParams(NI, 0, 0);
            if (url == null||url.equals("")){
                if (shouldReturn){
                    listener.onMiiNoAD(3001);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }
            httpUtils.get(url, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealHbSuc(response,shouldReturn);
                }

                @Override
                public void onFail(Exception e) {

                    if (shouldReturn){

                        listener.onMiiNoAD(3001);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                }
            });
        }
        public void dealHbSuc(HttpResponse response, boolean shouldReturn){
            try {
                SDKConfigModel sdk = null;

                String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));

                if (data == null){
                    if (shouldReturn){
                        listener.onMiiNoAD(3001);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }
                sdk = ConfigParser.parseConfig(data);

                if (sdk == null){
                    if (shouldReturn) {
                        listener.onMiiNoAD(3001);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }


                CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

                checkShowCount();


                if (checkNumber()){


                    if (shouldReturn){

                        listener.onMiiNoAD(3004);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }
                if (checkADShow()){

                    if (shouldReturn){
                        listener.onMiiNoAD(3003);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;

                }

                requestRa(shouldReturn);
            }
            catch (Exception e){
                if (shouldReturn) {
                    listener.onMiiNoAD(3001);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                e.printStackTrace();
            }
        }

        public void requestRa(final boolean shouldReturn){
            if (!CommonUtils.isNetworkAvailable(mContext)){


                if (shouldReturn) {
                    listener.onMiiNoAD(3000);
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }

            if (httpManager==null){
                httpManager = HttpManager.getInstance(mContext);
            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final  String url=httpManager.getRaUrl(RA);
            if (url == null || url.equals("")){
                if (!shouldReturn){
                    mainHandler.sendEmptyMessage(400);
                }
                return;
            }

            Map<String,String> params=httpManager.getParams2(RA,pt,0);

            httpUtils.post(url.trim(), new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {

                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                    dealRaSuc(response,shouldReturn);
                }
                @Override
                public void onFail(Exception e) {

                    if (shouldReturn){

                        listener.onMiiNoAD(3002);
                    }else {
                        mainHandler.sendEmptyMessage(400);
                    }
                }
            },params);
        }
        public  void dealRaSuc(HttpResponse response,boolean shouldReturn){
            try {
                List<AdModel> ads;
                String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
                if (temp == null){
                    if (shouldReturn) {
                        listener.onMiiNoAD(3002);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }

                ads = AdParser.parseAd(temp);

                if (ads == null || ads.size() <= 0){
                    if (shouldReturn) {
                        listener.onMiiNoAD(3002);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }


                AdModel ad = ads.get(0);
                if (ad == null){
                    if (shouldReturn) {
                        listener.onMiiNoAD(3002);
                    }
                    else {
                        mainHandler.sendEmptyMessage(400);
                    }
                    return;
                }

                Message msg=new Message();
                msg.obj = ad;
                msg.what=200;//ra访问成功
                mainHandler.sendMessage(msg);

            }
            catch (Exception e){
                if (shouldReturn) {
                    listener.onMiiNoAD(3002);//ra 解析失败
                }
                else {
                    mainHandler.sendEmptyMessage(400);
                }
                e.printStackTrace();
            }
        }

    }

}



