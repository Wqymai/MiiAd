package com.mg.nativ;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.mg.asyn.HbReturn;
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
    private MiiNativeListener listener;
    private AdModel adModel;
    private ReqAsyncModel reqAsyncModel;


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
                        listener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    listener.onMiiNoAD(1000);
                    break;
                case 600:
                    new HbReturn(reqAsyncModel).fetchMGAD();
                    break;
            }

        }
    };

    public MiiNativeAD(Activity activity,String appid,String lid, MiiNativeListener adListener){
      try {

          this.listener = adListener;
          super.context = activity.getApplicationContext();
          super.listener = adListener;
          super.activity = activity;
          super.reqAsyncModel = new ReqAsyncModel();

          reqAsyncModel.context = super.context;
          reqAsyncModel.handler = mainHandler;
          reqAsyncModel.listener = adListener;
          reqAsyncModel.pt = 4;//信息流
          reqAsyncModel.appid = appid;
          reqAsyncModel.lid = lid;

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              check23AbovePermission(mActivity, mainHandler);
              return;
          }
          new HbReturn(reqAsyncModel).fetchMGAD();

      }catch (Exception e){
          listener.onMiiNoAD(2001);
          e.printStackTrace();
      }
    }

//    private void startupAD(){
//
//         try {
//
//            SourceAssignModel saModel = checkADSource(mContext,4);
//            if (saModel == null){
//                new HbReturn(reqAsyncModel).fetchMGAD();
//                return;
//            }
//            if (saModel.type == 1){
//                mListener.onMiiNoAD(3005);
//                return;
//            }
//            new RaReturn(reqAsyncModel).fetchMGAD();
//
//         }catch (Exception e){
//             mListener.onMiiNoAD(3012);
//             e.printStackTrace();
//         }
//
//    }
    private  void setAdModel(){
        if (adModel == null){
            listener.onMiiNoAD(3006);
        }
        ref = new NativeImpl();
        ref.setAdModel(adModel);
        //回调
        listener.onADLoaded(ref);
    }

    @Override
    public void recycle() {

    }
}
