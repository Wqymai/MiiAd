package com.mg.interf;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import java.io.Serializable;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface MiiNativeADDataRef extends Serializable {
    static final long serialVersionUID = -7060210544600464481L;

    String getImg();//图片地址
    int getType();//1.html5的 0.非html5的
    String getName();//广告名称
    String getTitle();//广告语
    String getDesc();//广告描述
    String getPage();//h5代码，当getType=1,有值
    String getIcon();//图标
    void setNormalClick(Activity context, View view,MiiCpClickListener cpClickListener,MiiCpTouchListener cpTouchListener);//针对可设置setOnclickListener的
    void onExposured(Context context);
    void setWVClick(Activity context,WebView webView,MiiCpClickListener cpClickListener);//设置webview的点击事件

}
