package com.mg.notify;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiADListener;
import com.mg.others.model.AdModel;

/**
 * Created by wuqiyan on 17/8/8.
 */

public class MiiNotifyAD extends MiiBaseAD {



    private AdModel adModel;
    private MiiADListener mListener;



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
                        showNotifyAD(bitmap);
                    }
                    catch (Exception e){
                        mListener.onMiiNoAD(3011);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    mListener.onMiiNoAD(1000);
                    break;
                case 700:
                    mListener.onMiiNoAD(3011);
                    break;
            }
        }
    };





    public  MiiNotifyAD(Activity activity, String appid, String lid, MiiADListener listener){
        try{
            this.mListener = listener;
            super.listener = listener;
            super.activity = activity;
            super.context = activity.getApplicationContext();
            super.reqAsyncModel = new ReqAsyncModel();

            reqAsyncModel.context = super.context;
            reqAsyncModel.handler = mainHandler;
            reqAsyncModel.listener = listener;
            reqAsyncModel.pt = 0;
            reqAsyncModel.appid = appid;
            reqAsyncModel.lid = lid;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                check23AbovePermission(activity, mainHandler);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showNotifyAD(Bitmap bitmap){

    }

    private void checkADType(AdModel model) {

        try {
            if (model.getImage() == null || model.getImage().equals("") || model.getImage().equals("null")){

                if (model.getIcon() == null || model.getIcon().equals("") || model.getIcon().equals("null")){

                    mListener.onMiiNoAD(3011);
                }
                else {

                    new ImageDownloadHelper().downloadShowImage(context,model.getIcon(),mainHandler);
                }

            }
            else {

                new ImageDownloadHelper().downloadShowImage(context,model.getImage(),mainHandler);

            }

        }catch (Exception e){

            mListener.onMiiNoAD(3011);
            e.printStackTrace();

        }

    }


    @Override
    public void recycle() {

    }
}
