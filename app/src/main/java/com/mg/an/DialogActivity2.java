//package com.mg.an;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.android.others.R;
//import com.mg.comm.MConstant;
//import com.mg.interf.MiiNativeADDataRef;
//import com.mg.others.utils.LogUtils;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by wuqiyan on 17/6/22.
// */
//
//public class DialogActivity2 extends Activity {
//    ImageView imageView;
//    Button closeBtn;
//    String imgurl;
//    WebView webView;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog2);
//        final MiiNativeADDataRef ref = (MiiNativeADDataRef) getIntent().getSerializableExtra("AdData");
//
//
//        LogUtils.i(MConstant.TAG, "imgurl="+ref.getImg()+" type="+ref.getType());
//
//
//        imgurl = ref.getImg();
//        imageView = (ImageView) findViewById(R.id.img);
//
//        webView= (WebView) findViewById(R.id.webview);
//
//        if (ref.getType() == 0){
//
//            LogUtils.i(MConstant.TAG,"不是h5广告");
//            webView.setVisibility(View.GONE);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        HttpURLConnection conn = null;
//                        URL mURL = new URL(imgurl);
//                        conn = (HttpURLConnection) mURL.openConnection();
//                        conn.setRequestMethod("GET"); //设置请求方法
//                        conn.setConnectTimeout(10000); //设置连接服务器超时时间
//                        conn.setReadTimeout(5000);  //设置读取数据超时时间
//                        conn.connect(); //开始连接
//                        if(conn.getResponseCode() == 200){
//                            LogUtils.i(MConstant.TAG, "图片下载成功");
//                            InputStream inputStream = conn.getInputStream();
//                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ref.onExposured(DialogActivity2.this);
//                                    imageView.setImageBitmap(bitmap);
//                                }
//                            });
//                        }
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//            ref.setNormalClick(DialogActivity2.this,imageView);
//
//        }
//        else {
//            LogUtils.i(MConstant.TAG,"h5广告");
//            imageView.setVisibility(View.GONE);
//
//            webView.loadDataWithBaseURL("",imgurl , "text/html", "utf-8", "");
//            ref.onExposured(DialogActivity2.this);
//            ref.setWVClick(DialogActivity2.this,webView);
//
//        }
//
//
//        closeBtn= (Button) findViewById(R.id.closeBtn);
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        LogUtils.i(MConstant.TAG,"onPause");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LogUtils.i(MConstant.TAG,"onResume");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        LogUtils.i(MConstant.TAG,"onRestart");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LogUtils.i(MConstant.TAG,"onDestroy");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LogUtils.i(MConstant.TAG,"onStart");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        LogUtils.i(MConstant.TAG,"onStop");
//    }
//
//}
