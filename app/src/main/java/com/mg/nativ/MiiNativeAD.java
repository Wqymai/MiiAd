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
import com.mg.others.model.AdModel;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class MiiNativeAD extends MiiBaseAD {

    private NativeModel ref;
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
        reqAsyncModel.pt = 3;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            check23AbovePermission(mActivity,mainHandler);
            return;
        }

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
        ref = new NativeModel(mContext);
        ref.setAdModel(adModel);
        ref.setAdType(adModel.getType());//设置是不是h5广告
        ref.setImgUrl(adModel.getImage());//设置广告图片链接
    }
}
