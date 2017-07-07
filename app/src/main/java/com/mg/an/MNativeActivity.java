//package com.mg.an;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.android.others.R;
//import com.mg.comm.MConstant;
//import com.mg.interf.AbsADData;
//import com.mg.interf.MiiCpClickListener;
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
//public class MNativeActivity extends Activity {
//    ImageView imageView;
//    Button closeBtn;
//    String imgurl;
//    WebView webView;
//    String page;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog2);
//        final AbsADData ref = (AbsADData) getIntent().getSerializableExtra("AdData");
//
//
//        LogUtils.i(MConstant.TAG, "imgurl="+ref.getImg()+" type="+ref.getType());
//
//
//
//        imageView = (ImageView) findViewById(R.id.img);
//
//        webView= (WebView) findViewById(R.id.webview);
//
//        if (ref.getType() == 0){
//            Log.i(MConstant.TAG,"不是h5广告");
//            imgurl = ref.getImg();
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
//                                    ref.onExposured(MNativeActivity.this);
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
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            ref.setNormalClick(MNativeActivity.this, imageView, new MiiCpClickListener() {
//                @Override
//                public void click() {
//                    System.out.println("我是cp的数据1");
//                    finish();
//                }
//            });
//
//        }
//        else {
//            page=ref.getPage();
//            Log.i(MConstant.TAG,"h5广告" + page);
//
//            imageView.setVisibility(View.GONE);
//            webView.loadDataWithBaseURL("",page , "text/html", "utf-8", "");
//            ref.onExposured(MNativeActivity.this);
//            ref.setWVClick(MNativeActivity.this, webView, new MiiCpClickListener() {
//                @Override
//                public void click() {
//                    System.out.println("我是cp的数据2");
//                    finish();
//                }
//            });
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
