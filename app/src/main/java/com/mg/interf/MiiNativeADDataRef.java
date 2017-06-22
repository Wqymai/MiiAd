package com.mg.interf;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import java.io.Serializable;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface MiiNativeADDataRef extends Serializable {
    static final long serialVersionUID = -7060210544600464481L;

    String getImgContent();
    int getADType();//1.html5的 0.非html5的
    void onClick(Context context,View view);//针对可设置setOnclickListener的
    void onExposured(Context context);
    void setWVClick(Context context,WebView webView);//设置webview的点击事件

}
