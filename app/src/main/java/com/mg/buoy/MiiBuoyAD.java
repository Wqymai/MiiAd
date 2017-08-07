package com.mg.buoy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.mg.asyn.HbReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.others.model.AdModel;

/**
 * Created by wuqiyan on 17/8/7.
 */

public class MiiBuoyAD extends MiiBaseAD {

    private Context mContext;
    private Activity mActivity;
    private String mAppid;
    private String mLid;
    private AdModel adModel;
    private MiiADListener mListener;
    private ReqAsyncModel reqAsyncModel;

    Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    startupAD();
                    break;
                case 200:
                    try {
                        adModel= (AdModel) msg.obj;
                        if (adModel == null){
                            mListener.onMiiNoAD(3002);
                            return;
                        }
                        checkADType(adModel);
                    }
                    catch (Exception e){
                        mListener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;
                case 300:
                    try {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        if (bitmap == null){
                            mListener.onMiiNoAD(3011);
                            return;
                        }
                        showBuoyAD(bitmap);
                    }
                    catch (Exception e){
                        mListener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    mListener.onMiiNoAD(1000);
                    break;
                case 600:
                    new HbReturn(reqAsyncModel).fetchMGAD();
                    break;
            }
        }
    };

    private void showBuoyAD(Bitmap bitmap) {

    }

    private void checkADType(final AdModel model) {
        if (adModel.getType() == 4){//H5广告
            try {

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {

        }
    }

    private void startupAD() {
        try {

            SourceAssignModel saModel = checkADSource(mContext,1);

            if (saModel == null) {
                new HbReturn(reqAsyncModel).fetchMGAD();
                return;
            }

            if (saModel.type == 1) {

                mListener.onMiiNoAD(3005);
                return;

            }
            new RaReturn(reqAsyncModel).fetchMGAD();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public MiiBuoyAD(Activity activity, String appid, String lid, MiiADListener listener){
        try{
            this.mActivity = activity;
            this.mContext = activity.getApplicationContext();
            this.mAppid = appid;
            this.mLid = lid;
            this.mListener = listener;
            reqAsyncModel = new ReqAsyncModel();
            reqAsyncModel.context = mContext;
            reqAsyncModel.handler = mainHandler;
            reqAsyncModel.listener = listener;
            reqAsyncModel.pt = 0;
            reqAsyncModel.appid = appid;
            reqAsyncModel.lid = lid;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                check23AbovePermission(activity, mainHandler);
                return;
            }

            new HbReturn(reqAsyncModel).fetchMGAD();

        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public void recycle() {

    }
}
