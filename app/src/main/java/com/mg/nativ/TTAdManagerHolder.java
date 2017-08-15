package com.mg.nativ;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdManagerFactory;

/**
 * 可以用一个单例来保存TTAdManager实例
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    public static TTAdManager getInstance(Context context,String appid) {
        TTAdManager ttAdManager = TTAdManagerFactory.getInstance(context);
        if (!sInit) {
            synchronized (TTAdManagerHolder.class) {
                if (!sInit) {
                    doInit(context,ttAdManager,appid);
                    sInit = true;
                }
            }
        }
        return ttAdManager;
    }

    private static void doInit(Context context,TTAdManager ttAdManager,String appid) {
        ttAdManager.setAppId(appid)
                .setName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString())
                .setTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_LIGHT);
    }
}
