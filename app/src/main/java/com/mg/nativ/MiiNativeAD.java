package com.mg.nativ;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.mg.asyn.FirstEnter;
import com.mg.asyn.HbRaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiNativeListener;
import com.mg.others.model.AdModel;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class MiiNativeAD extends MiiBaseAD {

    private NativeImpl ref = null;
    private Context mContext;
    private Activity mActivity;
    private MiiNativeListener mListener;
    private AdModel adModel;
    private ReqAsyncModel reqAsyncModel = new ReqAsyncModel();


    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    startupAD();
                    break;
                case 200:
                    try {
                        adModel = (AdModel) msg.obj;
                        setAdModel();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    mListener.onMiiNoAD(1000);
                    break;
                case 600:
                    Init();
                    break;
            }

        }
    };

    public MiiNativeAD(Activity activity, MiiNativeListener listener){

        this.mContext = activity.getApplicationContext();
        this.mListener = listener;
        this.mActivity = activity;

        reqAsyncModel.context = this.mContext;
        reqAsyncModel.handler = this.mainHandler;
        reqAsyncModel.listener = this.mListener;
        reqAsyncModel.pt = 4;//信息流

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            check23AbovePermission(mActivity,mainHandler);
            return;
        }
        Init();

    }
    private void Init(){
        if (isFirstEnter(mContext)){
            new FirstEnter(reqAsyncModel).fetchMGAD();
            return;
        }
        startupAD();
    }

    private void startupAD(){

        SourceAssignModel saModel = checkADSource(mContext);

        if (saModel == null){
            new FirstEnter(reqAsyncModel).fetchMGAD();
            return;
        }

        if (saModel.type == 1){
            mListener.onMiiNoAD(3005);
            return;
        }

        new HbRaReturn(reqAsyncModel).fetchMGAD();

    }
    private  void setAdModel(){
        if (adModel == null){
            mListener.onMiiNoAD(3006);
        }
        ref = new NativeImpl();
        ref.setAdModel(adModel);

//        if (adModel.getType() == 4){
//            String html5="<!DOCTYPE html><html><head><meta name='viewport' " +
//                    "content='width=device-width,initial-scale=1,maximum-scale=1," +
//                    "user-scalable=no'><meta charset='utf-8'><title>Insert title " +
//                    "here</title><style type='text/css'>*{margin:0;padding:0}html," +
//                    "body{width:100%;height:100%;background-color:#FFF;" +
//                    "overflow:hidden}img{border:0}</style></head><body style=\"height: 100%;" +
//                    "width: 100%;\"><a href=\"http://www.baidu.com\"><img " +
//                    "src=\"http://192.168.199.191:8080/TestDemo/image/xfzg.jpg\" height=\"100%\" " +
//                    "width=\"100%\" /></a></body></body></html>";
//            ref.setAdType(1);
//            ref.setImgContent(adModel.getPage());//是h5广告的话 设置是h5代码
//
//        }
//        else {
//            ref.setAdType(0);
//            ref.setImgContent(adModel.getImage());//如果不是h5广告的话 设置广告图片链接
//        }
        //回调
        mListener.onADLoaded(ref);
    }

    @Override
    public void recycle() {

    }
}
