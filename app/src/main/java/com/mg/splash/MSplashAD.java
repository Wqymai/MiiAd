package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Base64;

import com.mg.others.http.HttpListener;
import com.mg.others.http.HttpResponse;
import com.mg.others.http.HttpUtils;
import com.mg.others.manager.HttpManager;
import com.mg.others.message.ISender;
import com.mg.others.message.MessageObjects;
import com.mg.others.model.AdModel;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MAdSDK;
import com.mg.others.ooa.MConstant;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.AdParser;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.ConfigParser;
import com.mg.others.utils.LocalKeyConstants;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.MiiLocalStrEncrypt;
import com.mg.others.utils.SP;
import com.mg.others.utils.imager.DownloadImgUtils;
import com.mg.util.DownloadUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.mg.others.manager.HttpManager.NI;
import static com.mg.others.manager.HttpManager.RA;
import static com.mg.others.ooa.MAdSDK.sInstance;


/**
 * Created by wuqiyan on 17/6/8.
 */

public class MSplashAD implements IDeviceInfoListener{

    private Activity mActivity;
    private DeviceInfo mDeviceInfo = null;
    private Context mContext;
    private HttpManager httpManager=null;
    private Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AdModel adModel= (AdModel) msg.obj;
            Bundle bundle=new Bundle();
            bundle.putSerializable("ad",adModel);
            Intent intent=new Intent(mActivity,SplashActivity.class);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    };


    public MSplashAD(Activity mActivity){

        this.mActivity = mActivity;
        this.mContext=mActivity.getApplicationContext();
        mDeviceInfo = CommonUtils.readParcel(mActivity.getApplicationContext(), MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){
            new DeviceInfoTask(sInstance,mActivity.getApplicationContext()).execute();
        }
        else {
            requestHb();
        }
    }

    @Override
    public void deviceInfoLoaded(DeviceInfo deviceInfo) {
            requestHb();
    }


    private void requestRa(){

        long currentTime = System.currentTimeMillis();
        long lastRa = (long) SP.getParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA,0l);
        long diffRa = (currentTime - lastRa)/1000;

        if (lastRa !=0l && diffRa < 20){
            return;
        }
        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url=httpManager.getRaUrl(RA);
        if (url==null || url.equals("")){
            return;
        }
        Map<String,String> params=httpManager.getParams2(RA,0,0);//暂时写的

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealHbSuc(response);
            }
            @Override
            public void onFail(Exception e) {
                LogUtils.i(MConstant.TAG,new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
            }
        },params);
    }
    private void requestHb(){

        if (httpManager==null){
            HttpManager.getInstance(mActivity.getApplicationContext(), null);
        }

        if (CommonUtils.isNetworkAvailable(mContext)){

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = httpManager.getParams(NI, 0, 0);
            if (url == null||url.equals("")){
                return;
            }
            httpUtils.get(url, new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST= MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealNiSuc(response);
                }

                @Override
                public void onFail(Exception e) {
                    LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                }
            });

        }else{
            LogUtils.i(MConstant.TAG,"ni 网络不可用......");
        }
    }

    private void dealNiSuc(HttpResponse response){
        SDKConfigModel sdk = null;
        String data = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (data==null){
            return;
        }
        sdk = ConfigParser.parseConfig(data);

        if (sdk == null){
            return;
        }

        CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

        requestRa();

    }
    private  void dealHbSuc(HttpResponse response){

        List<AdModel> ads;
        String temp = new String(Base64.decode(response.entity(),Base64.NO_WRAP));
        if (temp==null){
            return;
        }
        ads = AdParser.parseAd(temp);

        if (ads == null || ads.size()>0){
            return;
        }

        AdModel ad = ads.get(0);

        if (DownloadUtil.downloadShowImage(mContext,ad.getImage())){
            Message msg = new Message();
            msg.obj = ad;
            mainHandler.sendMessage(msg);
        }

    }


//    static int width;
//    static int height;
//    public void openDamaiDialog(Activity mActivity, String txt, int type) {
//        // 生成对话框
//        AlertDialog dlg = new AlertDialog.Builder(mActivity).create();
//        //显示对框框
//        dlg.show();
//
//
//        WindowManager wm = mActivity.getWindowManager();
//        width = wm.getDefaultDisplay().getWidth();
//        height=wm.getDefaultDisplay().getHeight();
//
//
//
////        dlg.getWindow().setContentView(R.layout.com.mg.splash);
//        Window window=dlg.getWindow();
//        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
//        params1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//
//        params1.height = (int)(height);
//        params1.width=(int) (width);
//        window.setAttributes(params1);
//
//        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//
//
//        //添加自定义的Layout以及布局方式，注意传入dlg对象本身方便关闭该提示框
//        window.setContentView(new MyDialog(mActivity,dlg,txt,type),params);
//
//    }
//    public class MyDialog extends RelativeLayout {
//
//        private AlertDialog dlg;
//        private Context context;
//        private String txt;
//        private int type;
//
//        public MyDialog(Context context,AlertDialog dlg,String txt,int type) {
//            super(context);
//            this.context=context;
//            this.dlg=dlg;
//            this.txt=txt;
//            this.type=type;
//            init();
//        }
//
//        private void init() {
//
//
//
//            RelativeLayout.LayoutParams pParams=new RelativeLayout.LayoutParams(ViewGroup
//                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            RelativeLayout  pLayout=new RelativeLayout(context);
//            pLayout.setLayoutParams(pParams);
//            pLayout.setBackgroundColor(Color.parseColor("#EBEBEB"));
//
//
//            TextView textView=new TextView(context);
//            textView.setText(txt);
//            RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            textParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            textParam.setMargins(40,(int) ((height*0.35)*0.15),40,0);
//            textView.setLayoutParams(textParam);
//            textView.setTextSize(18);
//            textView.setTextColor(Color.parseColor("#636363"));
//            textView.setGravity(Gravity.CENTER);
//            pLayout.addView(textView);
//
//
//            LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//            addView(pLayout, lp);
//
//
//
//
//        }
//
//    }
}
