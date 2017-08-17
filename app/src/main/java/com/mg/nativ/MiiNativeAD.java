package com.mg.nativ;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.mg.asyn.HbReturn;
import com.mg.asyn.RaNoReturn;
import com.mg.asyn.RaReturn;
import com.mg.asyn.ReqAsyncModel;
import com.mg.comm.MiiBaseAD;
import com.mg.interf.MiiNativeListener;
import com.mg.others.model.AdModel;

import java.util.List;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class MiiNativeAD extends MiiBaseAD {

    private NativeImpl ref = null;
    private Context mContext;
    private Activity mActivity;
    private MiiNativeListener mListener;
    private AdModel adModel;
    private TTAdNative mTTAdNative;
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
                        mListener.onMiiNoAD(3002);
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    openTTAD(true);
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

    public MiiNativeAD(Activity activity,String appid,String lid, MiiNativeListener listener){
      try {
          this.mContext = activity.getApplicationContext();
          this.mListener = listener;
          this.mActivity = activity;

          reqAsyncModel = new ReqAsyncModel();
          reqAsyncModel.context = this.mContext;
          reqAsyncModel.handler = this.mainHandler;
          reqAsyncModel.listener = this.mListener;
          reqAsyncModel.pt = 4;//信息流
          reqAsyncModel.appid = appid;
          reqAsyncModel.lid = lid;

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              check23AbovePermission(mActivity, mainHandler);
              return;
          }
          new HbReturn(reqAsyncModel).fetchMGAD();

      }catch (Exception e){
          mListener.onMiiNoAD(2001);
          e.printStackTrace();
      }
    }

    private void startupAD(){

         try {

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

             sdk = checkSdkConfig(sdk,mContext);
             int x = sdk.getX();
             int xsf_mg = sdk.getXsf_mg();
             int xsf_tt = sdk.getXsf_tt();

             if (x == 1){//按比例
                 if (xsf_mg > xsf_tt){

                     new RaReturn(reqAsyncModel).fetchMGAD();

                 }else {
                     openTTAD(true);
                 }

             }else {//按权重
                 if (xsf_mg > xsf_tt){

                     new RaNoReturn(reqAsyncModel).fetchMGAD();
                 }
                 else {
                     openTTAD(false);
                 }

             }
             

         }catch (Exception e){
             mListener.onMiiNoAD(3012);
             e.printStackTrace();
         }

    }

    String AID ="5000834";
    String NPID ="900834967";
    private void openTTAD(final boolean shouldReturn) {
//        String AID = "";
//        String NPID = "";
//        try {
//            OtherInfoModel model = getOtherIds(mContext);
//            AID = model.getTtAPPID();
//            NPID = model.getTtNativePosID();
//
//        }catch (Exception e){
//
//            mListener.onMiiNoAD(3007);
//            e.printStackTrace();
//
//        }

        TTAdManager ttAdManager = TTAdManagerHolder.getInstance(mActivity,AID);
        mTTAdNative = ttAdManager.createAdNative(mActivity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(NPID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(1)
                .build();

        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int i, String s) {
                if (shouldReturn){
                    mListener.onMiiNoAD(i);
                }else {
                    new RaReturn(reqAsyncModel).fetchMGAD();
                }

            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> list) {
                if (list == null ){
                   if (shouldReturn){
                      mListener.onMiiNoAD(3006);
                   }
                   else {
                      new RaReturn(reqAsyncModel).fetchMGAD();
                   }
                }else {
                    ref = new NativeImpl();
                    adModel = new AdModel();
                    adModel.setTt(true);
                    adModel.setTtFeedAd(list.get(0));
                    ref.setAdModel(adModel);
                    mListener.onADLoaded(ref);
                }
            }
        });
    }

    private  void setAdModel(){
        if (adModel == null){
            mListener.onMiiNoAD(3006);
        }
        sdk = checkSdkConfig(sdk,mContext);

        ref = new NativeImpl();
        adModel.setTt(false);
        adModel.setAutoRatio(sdk.getXc());
        ref.setAdModel(adModel);
        //回调
        mListener.onADLoaded(ref);
    }

    @Override
    public void recycle() {

    }
}
