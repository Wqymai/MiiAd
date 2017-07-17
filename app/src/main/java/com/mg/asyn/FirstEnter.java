//package com.mg.asyn;
//
//import com.mg.comm.MConstant;
//import com.mg.others.http.HttpListener;
//import com.mg.others.http.HttpResponse;
//import com.mg.others.http.HttpUtils;
//import com.mg.others.manager.HttpManager;
//import com.mg.others.model.SDKConfigModel;
//import com.mg.others.utils.CommonUtils;
//import com.mg.others.utils.ConfigParser;
//import com.mg.others.utils.LocalKeyConstants;
//import com.mg.others.utils.MiiLocalStrEncrypt;
//import com.mg.others.utils.SP;
//
//import java.util.Map;
//
///**
// * Created by wuqiyan on 17/6/15.
// * 第一次心跳,执行过程中发送错误需要返回结果码,如果成功就发送消息100
// */
//
//public class FirstEnter extends RequestAsync {
//
//
//    public FirstEnter(ReqAsyncModel model) {
//        super(model);
//    }
//
//    @Override
//    protected void startRequest() {
//        requestHb();
//    }
//
//    @Override
//    protected void requestHb( ) {
//       try {
//
//            if (!CommonUtils.isNetworkAvailable(mContext)){
//
//                listener.onMiiNoAD(3000);
//                return;
//            }
//            if (httpManager == null){
//                httpManager = HttpManager.getInstance(mContext);
//            }
//
//            final String url = httpManager.getHbUrl();
//            if (url == null||url.equals("")){
//
//                listener.onMiiNoAD(3001);
//                return;
//            }
//            Map<String,String> params = httpManager.getHbParams(appid,lid);
//            HttpUtils httpUtils = new HttpUtils(mContext);
//            httpUtils.post(url, new HttpListener() {
//                @Override
//                public void onSuccess(HttpResponse response) {
//                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
//                    MConstant.HB_HOST = MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS);
//                    dealHbSuc(response);
//                }
//
//                @Override
//                public void onFail(Exception e) {
//
//                    listener.onMiiNoAD(3001);
//                }
//            },params);
//       }catch (Exception e){
//           e.printStackTrace();
//           listener.onMiiNoAD(3001);
//       }
//    }
//
//    @Override
//    protected void dealHbSuc(HttpResponse response) {
//        try {
//
//            String data = response.entity();
//
//            if (data == null){
//                listener.onMiiNoAD(3001);
//                return;
//            }
//            SDKConfigModel sdk = ConfigParser.parseConfig(data);
//
//            if (sdk == null){
//                listener.onMiiNoAD(3001);
//                return;
//            }
//
//            CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);
//
//            checkReShowCount();
//
//            mainHandler.sendEmptyMessage(100);
//
//        }
//        catch (Exception e){
//            listener.onMiiNoAD(3001);
//            e.printStackTrace();
//        }
//    }
//}
