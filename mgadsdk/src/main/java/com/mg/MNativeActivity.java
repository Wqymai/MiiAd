package com.mg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.mg.interf.MiiCpClickListener;
import com.mg.interf.MiiCpTouchListener;
import com.mg.interf.MiiNativeADDataRef;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MNativeActivity extends Activity {

    ImageView imageView;
    Button closeBtn;
    String imgurl;
    WebView webView;
    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_layout);


        final MiiNativeADDataRef ref = (MiiNativeADDataRef) getIntent().getSerializableExtra("AdData");




        imageView = (ImageView) findViewById(R.id.img);

        webView = (WebView) findViewById(R.id.webview);

        if (ref.getType() == 0){

            imgurl = ref.getImg();
            webView.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection conn = null;
                        URL mURL = new URL(imgurl);
                        conn = (HttpURLConnection) mURL.openConnection();
                        conn.setRequestMethod("GET"); //设置请求方法
                        conn.setConnectTimeout(10000); //设置连接服务器超时时间
                        conn.setReadTimeout(5000);  //设置读取数据超时时间
                        conn.connect(); //开始连接
                        if(conn.getResponseCode() == 200){

                            InputStream inputStream = conn.getInputStream();
                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ref.onExposured(MNativeActivity.this);
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            ref.setNormalClick(MNativeActivity.this, imageView, new MiiCpClickListener() {
                @Override
                public void click() {
                    System.out.println("我是cp的数据1");
                    finish();
                }
            }, new MiiCpTouchListener() {
                @Override
                public void touch() {

                }
            });

        }
        else {
            page=ref.getPage();

            imageView.setVisibility(View.GONE);
            webView.loadDataWithBaseURL("",page , "text/html", "utf-8", "");
            ref.onExposured(MNativeActivity.this);
            ref.setWVClick(MNativeActivity.this, webView, new MiiCpClickListener() {
                @Override
                public void click() {
                    System.out.println("我是cp的数据2");
                    finish();
                }
            });

        }


        closeBtn= (Button) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
