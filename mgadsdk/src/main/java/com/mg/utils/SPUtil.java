package com.mg.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference工具类
 */
public class SPUtil {
	public static final String CONFIG = "CONFIG";				//FILE NAME

	public static final String UPDATE="update";

	public static final String FIRSTHB="firsthb";

	public static final String FOT = "fot";						//广告在一天可以展示的总次数

	public static final String FOS = "fos";						//广告展示总次数写入时间

	public static final String LASTSHOW = "lastShow";			//上次展示时间

	public static final String HOST = "host";					//服务器域名

	public static final String LAST_REQUEST_NI = "last_request_ni";	//上次请求ni时间

	public static final String LAST_REQUEST_RA = "last_request_ra"; //上次请求hb时间

	public static final String SET_PKN = "";					//已安装的程序包名

	public static final String AD_ACTIVE="fire_active";					//广告SDK是否激活： 0  不激活     激活

	public static final String AD_SILENT_TIME="ad_silent_time";				//广告的沉默时间

	public static final String HB_TIME="hb_time";//心跳时间间隔

	public static final String APPACTIVE="time0";//应用外

	public static final String SCREENUNLOCK="time1";//解锁屏幕

	public static final String INSTALL="time2";//安装卸载

	public static final String DOWNLOAD="time3";//下载

	public static final String NETWORKCHANGE="time4";

	public static final String COMMON_CODE_TIME="common_code_time";

	public static final String SILENT_FIRST_TIME="silent_firstime";

	public static final String IS_SILENT_FIRST="isSilentFirst";

	public static final String PUBLIC_COLD_FIRST_TIME="public_cold_firstime";

	public static final String GDT_ST="gdtst";


//	/**
//	 *
//	 * @param context
//	 * @param key
//	 * @param object
//	 */
//	public static void setParam(String fileName, Context context, String key,
//			Object object) {
//
//		String type = object.getClass().getSimpleName();
//		SharedPreferences sp = context.getSharedPreferences(fileName,
//				Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = sp.edit();
//
//		if ("String".equals(type)) {
//			editor.putString(key, (String) object);
//		} else if ("Integer".equals(type)) {
//			editor.putInt(key, (Integer) object);
//		} else if ("Boolean".equals(type)) {
//			editor.putBoolean(key, (Boolean) object);
//		} else if ("Float".equals(type)) {
//			editor.putFloat(key, (Float) object);
//		} else if ("Long".equals(type)) {
//			editor.putLong(key, (Long) object);
//		}
//
//		editor.commit();
////		editor.apply();
//	}


	/**
	 *
	 * @param context
	 * @param key
	 * @param object
	 */
	public static boolean setParam(String fileName, Context context, String key,
								   Object object) {

		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if ("String".equals(type)) {
			editor.putString(key, (String) object);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		}

		return editor.commit();
//		editor.apply();
	}

	public static Object getParam(String fileName, Context context, String key,
								  Object defaultObject) {
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);

		if ("String".equals(type)) {
			return sp.getString(key, (String) defaultObject);
		} else if ("Integer".equals(type)) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if ("Boolean".equals(type)) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if ("Float".equals(type)) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if ("Long".equals(type)) {
			return sp.getLong(key, (Long) defaultObject);
		}
		else if ("long".equals(type)){
			return sp.getLong(key, (long) defaultObject);
		}

		return null;
	}

	public static void removeParams(String name, Context context, String key){
		SharedPreferences.Editor data = context.getSharedPreferences(name,
				Context.MODE_PRIVATE).edit();
		data.remove(key);
		data.commit();
	}
}
