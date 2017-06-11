package com.mg.Interstitial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.others.R;
import com.mg.comm.ADClickHelper;
import com.mg.comm.MiiADListener;
import com.mg.demo.Constants;
import com.mg.others.model.AdModel;
import com.mg.comm.ImageDownloadHelper;
import com.mg.comm.MhttpRequestHelper;


/**
 * Created by wuqiyan on 2017/6/11.
 */

public class MiiInterstitialAD {

    Context mContext;
    Activity mActivity;
    AdModel adModel;
    MiiADListener listener;

    Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    Log.i(Constants.TAG,"收到RA请求成功的消息");
                    try {
                        adModel= (AdModel) msg.obj;
                        ImageDownloadHelper.downloadShowImage(mContext,adModel.getImage(),null,mainHandler);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 300:
                    try {
                        Bitmap bitmap= (Bitmap) msg.obj;
                        if (bitmap == null){
                            return;
                        }
                        showShade(bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    public MiiInterstitialAD(Activity mActivity, MiiADListener listener){
        this.mContext=mActivity.getApplicationContext();
        this.mActivity=mActivity;
        this.listener=listener;
        if (false){

        }else {
            Log.i(Constants.TAG,"加载麦广广告...");
            MhttpRequestHelper mhttpRequest = new MhttpRequestHelper(mContext,mainHandler,3,listener);
            mhttpRequest.fetchMGAD();
        }

    }
    //有遮罩效果
    private void showShade(Bitmap bitmap){
        // 生成对话框
        final AlertDialog dlg = new AlertDialog.Builder(mActivity).setCancelable(false).create();
        //显示对框框
        dlg.show();
        Window window = dlg.getWindow();


        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(600, 900);

        RelativeLayout relativeLayout=new RelativeLayout(mActivity);

        relativeLayout.setBackgroundColor(Color.parseColor("#EBEBEB"));


        ImageView imageView=new ImageView(mActivity);
        RelativeLayout.LayoutParams ivParam=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(ivParam);
//        imageView.setImageBitmap(bitmap);
        imageView.setImageResource(R.mipmap.splash_holder);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        relativeLayout.addView(imageView);

        CircleTextView cancel=new CircleTextView(mActivity);
        cancel.setGravity(Gravity.CENTER);
        cancel.setText("X");
        cancel.setWidth(50);
        cancel.setHeight(50);
        cancel.setBackgroundColor(Color.argb(50, 41, 36, 33));
        cancel.setTextColor(Color.WHITE);


        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(cancel, lp);





        //广告成功展示
        listener.onMiiADPresent();

        //添加自定义的Layout以及布局方式，注意传入dlg对象本身方便关闭该提示框
        window.addContentView(relativeLayout,layoutParams);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                //广告关闭
                listener.onMiiADDismissed();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击广告后相关行为
                new ADClickHelper(mContext).AdClick(adModel);

                //广告点击
                listener.onMiiADClicked();
                //广告关闭
                listener.onMiiADDismissed();
            }
        });

    }

    private class CircleTextView extends TextView {

        private Paint mBgPaint = new Paint();


        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            init(context);
        }

        public CircleTextView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public CircleTextView(Context context) {
            super(context);

            init(context);
        }

        public void init(Context context) {
            mContext = context;
            mBgPaint.setAntiAlias(true);
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int max = Math.max(measuredWidth, measuredHeight);
            setMeasuredDimension(max, max);
        }
        @Override
        public void setBackgroundColor(int color) {

            mBgPaint.setColor(color);
        }

        @Override
        public void draw(Canvas canvas) {
            // TODO Auto-generated method stub
            canvas.setDrawFilter(pfd);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
            super.draw(canvas);
        }
    }
}
