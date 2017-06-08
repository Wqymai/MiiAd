package com.mg.others.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mg.others.ooa.MConstant;

import com.mg.others.process.ProcessManager;
import com.mg.others.process.models.AndroidAppProcess;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SystemUtils {

    /**
     * {@link #checkOperation} 的返回值
     */
    public static final int OPRES_DISALLOWED = 0;
    /**
     * {@link #checkOperation} 的返回值
     */
    public static final int OPRES_ALLOWED = 1;
    /**
     * {@link #checkOperation} 的返回值
     */
    public static final int OPRES_UNSUPPORT = 2;

    public static final String OP_NONE = "OP_NONE";
    public static final String OP_COARSE_LOCATION = "OP_COARSE_LOCATION";
    public static final String OP_FINE_LOCATION = "OP_FINE_LOCATION";
    public static final String OP_GPS = "OP_GPS";
    public static final String OP_VIBRATE = "OP_VIBRATE";
    public static final String OP_READ_CONTACTS = "OP_READ_CONTACTS";
    public static final String OP_WRITE_CONTACTS = "OP_WRITE_CONTACTS";
    public static final String OP_READ_CALL_LOG = "OP_READ_CALL_LOG";
    public static final String OP_WRITE_CALL_LOG = "OP_WRITE_CALL_LOG";
    public static final String OP_READ_CALENDAR = "OP_READ_CALENDAR";
    public static final String OP_WRITE_CALENDAR = "OP_WRITE_CALENDAR";
    public static final String OP_WIFI_SCAN = "OP_WIFI_SCAN";
    public static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    public static final String OP_NEIGHBORING_CELLS = "OP_NEIGHBORING_CELLS";
    public static final String OP_CALL_PHONE = "OP_CALL_PHONE";
    public static final String OP_READ_SMS = "OP_READ_SMS";
    public static final String OP_WRITE_SMS = "OP_WRITE_SMS";
    public static final String OP_RECEIVE_SMS = "OP_RECEIVE_SMS";
    public static final String OP_RECEIVE_EMERGECY_SMS = "OP_RECEIVE_EMERGECY_SMS";
    public static final String OP_RECEIVE_MMS = "OP_RECEIVE_MMS";
    public static final String OP_RECEIVE_WAP_PUSH = "OP_RECEIVE_WAP_PUSH";
    public static final String OP_SEND_SMS = "OP_SEND_SMS";
    public static final String OP_READ_ICC_SMS = "OP_READ_ICC_SMS";
    public static final String OP_WRITE_ICC_SMS = "OP_WRITE_ICC_SMS";
    public static final String OP_WRITE_SETTINGS = "OP_WRITE_SETTINGS";
    public static final String OP_SYSTEM_ALERT_WINDOW = "OP_SYSTEM_ALERT_WINDOW";
    public static final String OP_ACCESS_NOTIFICATIONS = "OP_ACCESS_NOTIFICATIONS";
    public static final String OP_CAMERA = "OP_CAMERA";
    public static final String OP_RECORD_AUDIO = "OP_RECORD_AUDIO";
    public static final String OP_PLAY_AUDIO = "OP_PLAY_AUDIO";
    public static final String OP_READ_CLIPBOARD = "OP_READ_CLIPBOARD";
    public static final String OP_WRITE_CLIPBOARD = "OP_WRITE_CLIPBOARD";
    public static final String OP_TAKE_MEDIA_BUTTONS = "OP_TAKE_MEDIA_BUTTONS";
    public static final String OP_TAKE_AUDIO_FOCUS = "OP_TAKE_AUDIO_FOCUS";
    public static final String OP_AUDIO_MASTER_VOLUME = "OP_AUDIO_MASTER_VOLUME";
    public static final String OP_AUDIO_VOICE_VOLUME = "OP_AUDIO_VOICE_VOLUME";
    public static final String OP_AUDIO_RING_VOLUME = "OP_AUDIO_RING_VOLUME";
    public static final String OP_AUDIO_MEDIA_VOLUME = "OP_AUDIO_MEDIA_VOLUME";
    public static final String OP_AUDIO_ALARM_VOLUME = "OP_AUDIO_ALARM_VOLUME";
    public static final String OP_AUDIO_NOTIFICATION_VOLUME = "OP_AUDIO_NOTIFICATION_VOLUME";
    public static final String OP_AUDIO_BLUETOOTH_VOLUME = "OP_AUDIO_BLUETOOTH_VOLUME";
    public static final String OP_WAKE_LOCK = "OP_WAKE_LOCK";
    public static final String OP_MONITOR_LOCATION = "OP_MONITOR_LOCATION";
    public static final String OP_MONITOR_HIGH_POWER_LOCATION = "OP_MONITOR_HIGH_POWER_LOCATION";
    public static final String OP_GET_USAGE_STATS = "OP_GET_USAGE_STATS";
    public static final String OP_MUTE_MICROPHONE = "OP_MUTE_MICROPHONE";
    public static final String OP_TOAST_WINDOW = "OP_TOAST_WINDOW";
    public static final String OP_PROJECT_MEDIA = "OP_PROJECT_MEDIA";
    public static final String OP_ACTIVATE_VPN = "OP_ACTIVATE_VPN";

    public static String mLastComponentName = null;//上一个TopActivity包名
    public static ComponentName mComponentName = null;
    public static int sMiuiVersion = -1;
    public static int mCachedReleaseVersion = 0;
    public static int mCachedHas_USAGE_ACCESS_SETTING = -1;

    /**
     * 获取锁屏时间
     *
     * @param resolver     ContentResolver
     * @param defaultValue (毫秒)
     * @return 锁屏时间(毫秒)
     */
    public static int getScreenOffTimeout(ContentResolver resolver, int defaultValue) {
        int time = defaultValue;
        try {
            time = Settings.System.getInt(resolver,
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Throwable tr) {
        }
        return time;
    }

    /**
     * 设置锁屏时间
     *
     * @param resolver ContentResolver
     * @param time     (毫秒)
     */
    public static void setScreenOffTimeout(ContentResolver resolver, int time) {
        if (resolver != null) {
            try {
                Settings.System.putInt(resolver,
                        Settings.System.SCREEN_OFF_TIMEOUT, time);
            } catch (Throwable tr) {
            }
        }
    }

    private static class RecentUseComparator implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }

    //topActivity需不要需要申请权限
    public static boolean isNeedPermissionForGetTopPackage(Context context) {
        if (getAndroidReleaseVersion() == 500) {
            if (!hasUsageAccess(context)) {
                return false;
            }
            //检查权限
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                return (mode != AppOpsManager.MODE_ALLOWED);
            } catch (PackageManager.NameNotFoundException e) {
                return true;
            }
        }
        return false;
    }

    //检查软件锁是否需要用这种方式实现 0:不使用这种 1:使用但是不能申请权限　２:使用,且有权限　３:使用,没权限
    public static int getPermissionForUsageStates(Context context) {
        int res = 0;
        if (getAndroidReleaseVersion() == 500) {
            //有些5.1系统手机rom里面这个activity被删除了 这里判断一下, 如果没有,就不检查了
            if (!hasUsageAccess(context)) {
                res = 1;
                return res;
            }
            //检查权限
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    res = 2;
                } else {
                    res = 3;
                }
            } catch (PackageManager.NameNotFoundException e) {
                res = 4;
            }
        }
        return res;
    }
    public static String getForegroundApp(Context context) {
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        UsageEvents usageEvents = usageStatsManager.queryEvents(true ? 0 : ts-5000, ts);
        if (usageEvents == null) {
            return null;
        }


        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents.Event lastEvent = null;
        while (usageEvents.getNextEvent(event)) {
            // if from notification bar, class name will be null
            if (event.getPackageName() == null || event.getClassName() == null) {
                continue;
            }

            if (lastEvent == null || lastEvent.getTimeStamp() < event.getTimeStamp()) {
                lastEvent = event;
            }
        }

        if (lastEvent == null) {
            return null;
        }
        return lastEvent.getPackageName();
    }

    //获取TopActivity入口
    public static String getTopActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String componentName = null;
        if (getAndroidReleaseVersion() >= 500) {
            componentName = getTopPackage500(context);
        }
        else
        {

            if (componentName == null) {
                componentName = getTopPackage2(context, activityManager);
            }

            if (componentName == null) {
                componentName = getTopPackage3(context, activityManager);
            }
        }

        return componentName;
    }

//    //Android5.1 获取方法
//    private static ComponentName getTopPackage1(final Context context) {
//        String pkg = null;
//        long ts = System.currentTimeMillis();
//        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
//        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000, ts);
//        if (usageStats == null || usageStats.size() == 0) {
//            pkg = "null";
//        } else {
//            Collections.sort(usageStats, new RecentUseComparator());
//            pkg = usageStats.get(0).getPackageName();
//        }
//        mLastComponentName = "null".equals(pkg) ? mLastComponentName : pkg;
//        if ("null".equals(mLastComponentName) || mLastComponentName == null) {
//            return null;
//        } else {
//            mComponentName = new ComponentName(mLastComponentName, "");
//            return mComponentName;
//        }
//    }

    public static List<String> RuningAppList = new ArrayList<String>();

    //Android5以上 获取方法
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getTopPackage500(final Context context) {
        List<String> locaRuningAppList = new ArrayList<String>();
        String mPackageName = null;
        List<AndroidAppProcess> processes = ProcessManager.getRunningForegroundApps(context);

        for (AndroidAppProcess appProcess : processes) {
            String nPackageName = appProcess.getPackageName();
            LogUtils.i(MConstant.TAG,"PN:"+nPackageName);
            locaRuningAppList.add(nPackageName);
        }

//        Log.i("TAG", "processes:"+processes.size()+" "+locaRuningAppList.size()+"      "+(locaRuningAppList.size() - RuningAppList.size()));
        LogUtils.i(MConstant.TAG,"locaRuningAppList:"+locaRuningAppList.toString());
        LogUtils.i(MConstant.TAG,"RuningAppList:"+RuningAppList.toString());

        if(RuningAppList == null)
        {
            LogUtils.i(MConstant.TAG,"RuningAppList:null");
        }
        else if(locaRuningAppList.size() - RuningAppList.size() >= 1)
        {
            for(String nPackgeName:locaRuningAppList)
            {
                LogUtils.i(MConstant.TAG,"nPackgeName:"+nPackgeName);
                boolean isfind = RuningAppList.contains(nPackgeName);

                if(isfind)
                {
                    RuningAppList.remove(nPackgeName);
                }
                else
                {
                    mPackageName = nPackgeName;
                    break;

                }

            }

        }


        RuningAppList = locaRuningAppList;

        return mPackageName;

    }


    //Android5.0 获取方法
    private static String getTopPackage2(final Context context, final ActivityManager activityManager) {
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list != null) {
            for (ActivityManager.RunningAppProcessInfo info : list) {
                if (info != null && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    mComponentName = new ComponentName(info.pkgList[0], "");
                    return mComponentName.getPackageName();
                }
            }
        }
        return null;
    }

    //较低版本获取方法
    private static String getTopPackage3(final Context context, final ActivityManager activityManager) {
        List<ActivityManager.RunningTaskInfo> tasksInfos = activityManager.getRunningTasks(1);
        if (tasksInfos != null && tasksInfos.size() > 0) {
            ActivityManager.RunningTaskInfo info = tasksInfos.get(0);
            if (info != null) {
                return info.topActivity.getPackageName();
            }
        }
        return null;
    }

    //获得android release版本,格式为 ddd,d为数字;如　5.0.2 为 502
    public static int getAndroidReleaseVersion() {
        int version = 0;
        String release = SystemProperties.get("ro.build.version.release", "unknown");
        if (mCachedReleaseVersion == 0 && !"unknown".equals(release)) {
            try {
                String[] nums = release.split("\\.");
                for (int i = 0; i < nums.length; i++) {
                    int n = Integer.parseInt(nums[i]);
                    switch (i) {
                        case 0: {
                            version += n * 100;
                            break;
                        }
                        case 1: {
                            version += n * 10;
                            break;
                        }
                        case 2: {
                            version += n;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCachedReleaseVersion = version;
        } else {
            version = mCachedReleaseVersion;
        }
        return version;
    }

    //判断系统有没有允许topActivity的弹窗
    public static boolean hasUsageAccess(final Context context) {
        boolean res = false;
        switch (mCachedHas_USAGE_ACCESS_SETTING) {
            case -1: {
                try {
                    Intent intent = new Intent("android.settings.USAGE_ACCESS_SETTINGS");
                    res = intent.resolveActivity(context.getPackageManager()) != null;
                } catch (Exception e) {
                }
                mCachedHas_USAGE_ACCESS_SETTING = res ? 1 : 0;
                break;
            }
            case 0: {
                res = false;
                break;
            }
            case 1: {
                res = true;
                break;
            }
        }
        return res;
    }

    public static boolean isAirPlaneMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isAirPlaneModeInternal(context);
        } else {
            return isAirPlaneModeInternalOld(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean isAirPlaneModeInternal(Context context) {
        return (Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1);
    }

    private static boolean isAirPlaneModeInternalOld(Context context) {
        return (Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1);
    }


    public static boolean isServiceRunning(Context context, Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo aServiceList : serviceList) {
            if (aServiceList.service != null && clazz.getName().equals(aServiceList.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

//    /**
//     * 检查一些系统权限
//     *
//     * @param context Context
//     * @param opstr   the field name of {@link AppOpsManager#OP_*}
//     * @return {@link #OPRES_ALLOWED},  {@link #OPRES_DISALLOWED} or  {@link #OPRES_UNSUPPORT}
//     */
//    public static int checkOperation(Context context, String opstr) {
//        try {
//            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            int uid = android.os.Process.myUid();
//            String pkName = context.getPackageName();
//            Object res = ReflectUtils.getFieldValue(AppOpsManager.class, opstr);
//            if (res != null && (res instanceof Integer)) {
//                res = ReflectUtils.invoke(appOpsManager, "checkOp", Integer.TYPE, res, Integer.TYPE, uid, String.class, pkName);
//                if (res != null && (res instanceof Integer)) {
//                    return ((int) res) == AppOpsManager.MODE_ALLOWED ? OPRES_ALLOWED : OPRES_DISALLOWED;
//                }
//            }
//        } catch (Throwable tr) {
//            DebugLog.w("test_float", tr);
//        }
//        return OPRES_UNSUPPORT;
//    }
//
//    /**
//     * 检查是否有悬浮窗权限
//     */
//    public static int checkFloatWindow(Context context) {
//        return checkOperation(context, OP_SYSTEM_ALERT_WINDOW);
//    }

    /**
     * 判断能不能联网
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                try {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    return networkInfo.isAvailable() && networkInfo.isConnected();
                } catch (Exception e) {
                    return false;
                }

            }
            return false;
        }
        return false;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
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


    public static boolean isSecured(Context context){
        boolean isSecured = false;
        String classPath = "com.android.internal.widget.LockPatternUtils";
        try{
            Class<?> lockPatternClass = Class.forName(classPath);
            Object lockPatternObject = lockPatternClass.getConstructor(Context.class).newInstance(context);
            Method method = lockPatternClass.getMethod("isSecure");
            isSecured = (boolean) method.invoke(lockPatternObject);
        }catch (Exception e){
            isSecured = false;
        }
        return isSecured;
    }
    public static boolean checkPasswordToUnLock(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return keyguardManager.inKeyguardRestrictedInputMode();
        }else{
            return isSecured(context);
        }
    }

}
