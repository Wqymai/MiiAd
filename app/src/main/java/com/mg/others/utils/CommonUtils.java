package com.mg.others.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.android.others.BuildConfig;
import com.mg.comm.MConstant;
import com.mg.others.model.AdModel;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CommonUtils {
    public static boolean copyFileFromAssetsToSd(Context context, String fileName, String path){
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while((i = is.read(temp)) > 0){
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static void CopyAssertJarToFile(Context context, String filename,
                                           String des) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .toString() + File.separator + des);
            if (file.exists()) {
                file.delete();
            }

            InputStream inputStream = context.getAssets().open(filename);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static synchronized <T> T readParcel(Context context, String fileName) {
        if(context==null){
            return null;
        }
        File file = context.getFileStreamPath(fileName);
        if (null == file || !file.exists()) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            @SuppressWarnings("unchecked")
            T t = (T) ois.readObject();
            return t;
        } catch (FileNotFoundException e) {
            LogUtils.i(MConstant.TAG,"read parcel error : " + e);
        } catch (IOException e) {
            LogUtils.i(MConstant.TAG,"read parcel error : " + e);
        } catch (ClassNotFoundException e) {
            LogUtils.i(MConstant.TAG, "read parcel error : " + e);
        } finally {
            if (null != ois) {
                try {
                    ois.close();
                } catch (IOException e) {
                    LogUtils.i(MConstant.TAG, "read parcel close ObjectOutputStream error : " + e);
                }
            }
        }
        return null;
    }

    public static synchronized <T> boolean writeParcel(Context context, String fileName, T t) {
        if(context==null){
            return false;
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(context.getFileStreamPath(fileName)));
            oos.writeObject(t);
            LogUtils.i(MConstant.TAG, "write parcel success"+context.getFileStreamPath(fileName).toString());
            return true;
        } catch (FileNotFoundException e) {
            LogUtils.i(MConstant.TAG, "write parcel error : " + e);
        } catch (IOException e) {
            LogUtils.i(MConstant.TAG, "write parcel error : " + e);
        } finally {
            if (null != oos) {
                try {
                    oos.close();
                } catch (IOException e) {
                    LogUtils.i(MConstant.TAG, "write parcel close ObjectOutputStream error : " + e);
                }
            }
        }
        return false;
    }
//    public static synchronized boolean clearParcel(String fileName) {
//        File file = MAdSDK.getInstance().getContext().getFileStreamPath(fileName);
//        if (null != file && file.exists()) {
//            return file.delete();
//        }
//        return false;
//    }

    public static String hashSign(String key){
        String signKey;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            signKey = bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            signKey = String.valueOf(key.hashCode());
        }
        return signKey;
    }

    public static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static final int NET_TYPE_UNKNOWN = 0;
    public static final int NET_TYPE_WIFI = 1;
    public static final int NET_TYPE_2G = 2;
    public static final int NET_TYPE_3G = 3;
    public static final int NET_TYPE_4G = 4;

    public static int getNetworkSubType(Context context) {
        int netType = NET_TYPE_UNKNOWN;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_MOBILE) {
                int subtype = info.getSubtype();
                switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: {//api<8 : replace by 11
                        netType = NET_TYPE_2G;
                        break;
                    }
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: { //api<13 : replace by 15
                        netType = NET_TYPE_3G;
                        break;
                    }
                    case TelephonyManager.NETWORK_TYPE_LTE: {    //api<11 : replace by 13
                        netType = NET_TYPE_4G;
                    }
                    default: {
                        String subtypeName = info.getSubtypeName();
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (subtypeName != null && (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000"))) {
                            netType = NET_TYPE_3G;
                        }
                        break;
                    }
                }

            } else if (type == ConnectivityManager.TYPE_WIFI) {
                netType = NET_TYPE_WIFI;
            }
        }
        return netType;
    }

    public static String getInstalledSafeWare(Context context) {
        PackageManager pm = context.getPackageManager();
        StringBuilder apps = new StringBuilder();

        if (pm == null) {
            return apps.toString();
        }

        List<PackageInfo> installs = pm.getInstalledPackages(0);
        if (installs == null) {
            return apps.toString();
        }
        for (PackageInfo info : installs) {
            //用户安装的
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                apps.append(info.packageName);
                apps.append("|");
            }
        }
        String str = apps.toString();
        if (str.endsWith("|")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        ConnectivityManager cm = (ConnectivityManager) MAdSDK.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getScreenW(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getScreenH(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        // int h = aty.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }

    public static void openBrowser(Context mContext , String url) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //get请求参数拼接
    public static String getUrl(String url, Map<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    public static String MapToString(Map<String, String> params){
        // 添加url参数
        if (params != null) {
            StringBuffer sb = null;
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
//                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            return sb.toString();
        }
        return null;
    }

    public static boolean installNormal(Context context, String filePath){
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() < 0){
            return false;
        }
        LogUtils.i(MConstant.TAG,"last loc="+filePath);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+ ".fileProvider", file);
                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
            }
            else {
                String cmd = "chmod 777 " + filePath;
                Runtime.getRuntime().exec(cmd);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getFileDownloadLocation(Context context){
        if (context == null){
            return null;
        }
        String filePath;
        if (Environment.getExternalStorageState() != null &&
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            StringBuilder sb = new StringBuilder();
//            File file = context.getExternalFilesDir("");
//            if (file != null){
//                filePath = context.getExternalFilesDir("").getAbsolutePath();
//            }else {
//                filePath = sb.append(Environment.getExternalStorageDirectory()).append("/Android/data/")
//                        .append(context.getPackageName()).append("/file/").toString();
//            }
            filePath = MConstant.Dir_TEMP;
        }else {
            filePath = context.getFilesDir().getAbsolutePath();
        }
        return filePath;
    }

//    /**
//     * 获取屏幕宽度
//     *
//     * @param activity Context
//     * @return 屏幕宽度
//     */
//    public static int getScreenW(Context activity) {
//        DisplayMetrics dm ;
//        dm = activity.getResources().getDisplayMetrics();
//        int w = dm.widthPixels;
//        return w;
//    }
//
//    /**
//     * 获取屏幕高度
//     *
//     * @param activity Activity
//     * @return 屏幕高度
//     */
//    public static int getScreenH(Context activity) {
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = activity.getResources().getDisplayMetrics();
//        int h = dm.heightPixels;
//        return h;
//    }

    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static void writeLogToSdCard(String content){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String fileName = "mmLog.txt";
            File file = new File(MConstant.Dir,fileName);
            if (!file.exists()){
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = content.getBytes();
                    fos.write(buffer,0,buffer.length);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void writeTxt(String fileName, String content) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName,true),"UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content);
            bw.write("\r\n");
            bw.close();
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
//			FileWriter writer = new FileWriter(fileName, true);
//			writer.write(content);
//			writer.write("\r\n");
//			writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String MEncode(Map<String , String> params){
        try {
            return new StringBuffer(URLEncoder.encode(Base64.encodeToString(MapToString(params).getBytes()
                    , Base64.NO_WRAP),"UTF-8")).reverse().toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MDecode(String str){
        try {
            return new String(Base64.decode(URLDecoder.decode(new StringBuffer(str).reverse().toString(),"UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static void writeADToSP(Context context, AdModel ad){
        if (context == null){
            return;
        }

        if (ad == null){
            return;
        }
		SharedPreferences sp = context.getSharedPreferences(SP.CONFIG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(ad);
			String base64Config = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
			editor.putString(ad.getPkName(),base64Config);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static AdModel readADFromSP(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(SP.CONFIG, Context.MODE_PRIVATE);
		String base64Config = sp.getString(key,"");
		AdModel ad = null;
		if (!base64Config.equals("")){
			byte[] bytes = Base64.decode(base64Config, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream ois = new ObjectInputStream(bais);
				ad = (AdModel) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ad;
	}

}
