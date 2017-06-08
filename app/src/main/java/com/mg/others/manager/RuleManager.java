package com.mg.others.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import com.mg.others.http.HttpUtils;
import com.mg.others.layer.AdLayer;
import com.mg.others.layer.LayerManager;
import com.mg.others.message.Handler;
import com.mg.others.message.ISender;
import com.mg.others.message.MessageObjects;
import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.model.AdSence;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.ooa.MConstant;
import com.mg.others.process.AndroidProcesses;
import com.mg.others.process.models.AndroidAppProcess;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.SP;
import com.mg.others.utils.SystemProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mg.others.ooa.MConstant.adClickType.app;


public class RuleManager implements Handler {
    private LayerManager mLayerManger;
    private Application mApplication;
    private SDKConfigModel sdkConfig;
    private final Object obj = new Object();
    private List<String> appList;
    private  String mPackageName = "";
    private ISender mSender = ISender.Factory.newMainThreadSender(this);
    private List<String> systemApp = null;
    private String tempPkn = null;
    private long lastShowAdTime = 0l;
    private HttpManager httpManager;
    public static RuleManager instance = null;
    private boolean isInclude=true;


    public static RuleManager getInstance(Application mApplication, LayerManager mLayerManger) {

        if (instance == null) {
            synchronized (RuleManager.class) {
                if (instance == null) {
                    instance = new RuleManager(mApplication, mLayerManger);
                }
            }
        }
        return instance;
    }

    public RuleManager(Application mApplication, LayerManager mLayerManger) {
        this.mLayerManger = mLayerManger;
        this.mApplication = mApplication;
        synchronized (obj) {
            if (sdkConfig == null) {
                sdkConfig = CommonUtils.readParcel(mApplication, MConstant.CONFIG_FILE_NAME);
            }
        }
        LoadSystemApp loadThread = new LoadSystemApp();
        loadThread.start();
        appList = new ArrayList<>();
        httpManager = HttpManager.getInstance(mApplication, mSender);
    }


    public boolean isReachCommonCodeTime() {
        SharedPreferences sp = mApplication.getSharedPreferences(SP.CONFIG, Activity.MODE_PRIVATE);
        long timeCur = System.currentTimeMillis();
        long commonStartTime = sp.getLong("commonStartTime", timeCur);
        long commonTime=sp.getLong("timeComm",600000);
        if (commonTime==0){
            commonTime=600000;
        }
        LogUtils.i(MConstant.TAG, "common==时间差==："+(timeCur-commonStartTime));
        if (timeCur == commonStartTime || (timeCur - commonStartTime > commonTime)) {
            LogUtils.i(MConstant.TAG, "common time up");
            return true;
        }
        return false;
    }


    //按各个场景判断是否达到冷却时间 默认冷却时间1小时
    public boolean isReachCodeTime(int triggerPoint) {
        SharedPreferences mySharedPreferences = mApplication.getSharedPreferences(SP.CONFIG,
                Activity.MODE_PRIVATE);
        long timeCur = System.currentTimeMillis();
        long timeCold = mySharedPreferences.getLong("time" + triggerPoint, 3600000);//默认冷却时间1小时=3600 000毫秒
        long timeStart = mySharedPreferences.getLong("timeStart" + triggerPoint, timeCur);
        LogUtils.i(MConstant.TAG, "==时间差==："+(timeCur-timeStart));
        if (timeCur == timeStart || timeCur - timeStart > timeCold) {
            LogUtils.i(MConstant.TAG, "cold time up");
            return true;
        } else {
            return false;
        }
    }
    public static HandlerThread myHandlerThread=null;
    public static android.os.Handler mHandler=null;
    public void listen(int tiggleSence) {
        if (myHandlerThread == null) {
            myHandlerThread = new HandlerThread("htOthers");
            myHandlerThread.start();
        }
        if (mHandler == null) {
            mHandler = new android.os.Handler(myHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        boolean isOk;
                        int AdType;

                        int triggerPoint = msg.what;

                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                        if (!setSdkConfig(triggerPoint)){
                            mHandler.sendEmptyMessageDelayed(triggerPoint,2000);
                            return;
                        }

                        if (!sdkConfig.isAdShow()) {
                            LogUtils.i(MConstant.TAG, "show , don't require");
                            mHandler.sendEmptyMessageDelayed(triggerPoint,2000);
                            return;
                        }

                        mPackageName = getForegroundApp(mApplication);

                        LogUtils.i(MConstant.TAG,"mPackageName=="+mPackageName);

                        isOk = checkAbleScene(triggerPoint,mPackageName);
                        if (!isOk){
                            LogUtils.i(MConstant.TAG,"checkAbleScene false");
                            mHandler.sendEmptyMessageDelayed(triggerPoint,2000);
                            return;
                        }


                        isOk = checkSdkConfig();
                        if (!isOk){
                            LogUtils.i(MConstant.TAG,"checkSdkConfig false");
                            mHandler.sendEmptyMessageDelayed(triggerPoint,2000);
                            return;
                        }

                        isOk = checkColdShow(triggerPoint);
                        if (!isOk){
                            LogUtils.i(MConstant.TAG,"checkColdShow false");
                            mHandler.sendEmptyMessageDelayed(triggerPoint,2000);
                            return;
                        }

//                        Intent dialogIntent = new Intent(mApplication, FrameActivity.class);
//                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mApplication.startActivity(dialogIntent);
//                        AppManager.getAppManager().finishAllActivity();

                        checkActive();

                        AdType = sdkConfig.choseAdTypeBySence(triggerPoint);

                        httpManager.requestRa(AdType, triggerPoint);

                        mHandler.sendEmptyMessageDelayed(triggerPoint,2000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
       }
       mHandler.sendEmptyMessageDelayed(tiggleSence,0);
    }



    //检查冷却时间
    private boolean checkColdShow(int triggerPoint){
        boolean isReachTime = isReachCodeTime(triggerPoint);
        boolean isCommonTime = isReachCommonCodeTime();
        if (isReachTime && isCommonTime) {
            long timeCur = System.currentTimeMillis();
            SharedPreferences.Editor editor = mApplication.getSharedPreferences(SP.CONFIG, Context.MODE_PRIVATE).edit();
            editor.putLong("timeStart" + triggerPoint, timeCur);
            editor.putLong("commonStartTime", timeCur);
            editor.commit();
           return true;
        }
        return false;
    }
    //检查展示场景
    private boolean checkAbleScene(int triggerPoint,String mPackageName){
        boolean isInScene=false;

        if (mPackageName == null
            || mPackageName.equals("")
            || mPackageName.contains(mApplication.getPackageName())){

            return isInScene;
        }

        if (triggerPoint == 0) {
            boolean isSystemApp = CheckInstallAppList(mPackageName);
            if (isSystemApp) {
                isInScene = false;
            } else {
                isInScene = checkBlackWhiteList();
            }
            return isInScene;
        }

        isInScene = true;
        return isInScene;
    }
    //检查sdk配置
    private boolean setSdkConfig(int triggerPoint){

        if (httpManager == null) {
            httpManager = HttpManager.getInstance(mApplication, mSender);
        }
        sdkConfig = CommonUtils.readParcel(mApplication, MConstant.CONFIG_FILE_NAME);
        if (sdkConfig == null) {
            httpManager.requestNi(false);
            mHandler.sendEmptyMessageDelayed(triggerPoint,5000);
            return false;
        }
        if (httpManager.updateNi(sdkConfig)){
            mHandler.sendEmptyMessageDelayed(triggerPoint,5000);
            return true;
        }
        return true;
    }
    //是否符合展示广告的条件
    private boolean checkSdkConfig() {

        if (MConstant.isBlack){
            LogUtils.i(MConstant.TAG,"current black screen");
            return false;
        }
        int show_percentage = (int) ((Math.random() * 100)+1);

        if (show_percentage > sdkConfig.getShow_percentage()) {
            LogUtils.i(MConstant.TAG,"percentage , don't require");
            return false;
        }
        int show_num = (Integer) SP.getParam(SP.CONFIG, mApplication, SP.FOT, 0);//广告已展示的次数

        if (show_num >= sdkConfig.getShow_sum()) {
            LogUtils.i(MConstant.TAG,"number , don't require");
            return false;
        }
        return true;
    }


    private class LoadSystemApp extends Thread {
        @Override
        public void run() {
            if (systemApp == null || systemApp.size() <= 0) {
                systemApp = new ArrayList<>();
                PackageManager packageManager = mApplication.getPackageManager();
                final List<PackageInfo> installedList = packageManager.getInstalledPackages(0);
                //获取系统应用
                for (int i = 0; i < installedList.size(); i++) {
                    PackageInfo packageInfo = installedList.get(i);
                    String packageName = packageInfo.packageName;
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ) {
                        systemApp.add(packageName);
                    }
                }
                if (!systemApp.contains("android.process.acore")){
                    systemApp.add("android.process.acore");
                }
                if (!systemApp.contains("com.vivo.launcher.scene.comm")){
                    systemApp.add("com.vivo.launcher.scene.comm");
                }
                if (!systemApp.contains("com.qihoo.notification")){
                    systemApp.add("com.qihoo.notification");
                }
                if (!systemApp.contains(mApplication.getPackageName())){
                    systemApp.add(mApplication.getPackageName());//本应用不弹广告
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MConstant.what.update_config:
                MessageObjects messageObjects = (MessageObjects) msg.obj;
                SDKConfigModel temp = (SDKConfigModel) messageObjects.obj0;
                messageObjects.recycle();
                sdkConfig = temp;
                appList = null;
                break;

            case MConstant.what.show_notNormalAd_finish:
                this.listen(AdSence.NORMAL);
                break;

            case MConstant.what.ads_result:
                MessageObjects messageObjects2 = (MessageObjects) msg.obj;
                AdModel adModel = (AdModel) messageObjects2.obj0;
                int Adtype = messageObjects2.arg0;
                int toggleSence = messageObjects2.arg1;
                messageObjects2.recycle();
                if (sdkConfig != null && adModel != null) {
                    adModel.setDelayTime(sdkConfig.getInterstitial_delay_time());
                    if (Adtype==0){
                        Adtype=adModel.getPt();
                    }
                    adModel.setDisplayTime(sdkConfig.getDisplayTime(Adtype));
                    adModel.setHasJumpButton(sdkConfig.isJump());
                    adModel.setJumpFunction(sdkConfig.getJump_function());
                    adModel.setToggleSence(toggleSence);
                    adModel.setBp(sdkConfig.getBp());

                    int auto_percentage = sdkConfig.getAuto_show_percentage();
                    int show_percentage = (int) (Math.random() * 100);
                    if (adModel.getType() != 4 && show_percentage < auto_percentage) {
                        int show_num = (Integer) SP.getParam(SP.CONFIG, mApplication, SP.FOT, 0);
                        SP.setParam(SP.CONFIG, mApplication, SP.FOT, show_num + 1);
                        new AutoAdManager(mApplication,adModel).start();
                    }
                    else
                    {
                        Intent intent = new Intent(mApplication, AdLayer.class);
                        intent.putExtra(MConstant.key.ADS_DATA, adModel);
                        intent.putExtra(MConstant.key.ADTYPE, Adtype);
                        intent.putExtra("BP",sdkConfig.getBp());
                        intent.putExtra("Source",adModel.getType());
                        mLayerManger.addLayer(intent);
                    }
                    lastShowAdTime = System.currentTimeMillis();
                }
                break;

            default:

                break;
        }
    }

    //激活上报
    private void checkActive() {
        if (tempPkn == null || tempPkn != mPackageName) {
            AdModel ad = CommonUtils.readADFromSP(mApplication, mPackageName);
            if (ad != null) {
                HttpManager.reportEvent(ad, AdReport.EVENT_OPEN, mApplication);
                SP.removeParams(SP.CONFIG, mApplication, mPackageName);
            }
            tempPkn = mPackageName;
        }
    }




    public class AutoAdManager extends Thread {

        AdModel ad = null;
        Context context = null;
        public AutoAdManager(Context context, AdModel ad)
        {
            this.ad = ad;
            this.context = context;
        }
        @Override
        public void run() {
            try {
                int show_percentage = (int) (Math.random() * 5);
                sleep(show_percentage*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpManager.reportEvent(ad, AdReport.EVENT_SHOW, context);
            try {
                int show_percentage = (int) (Math.random() * 5);
                sleep(show_percentage*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpManager.reportEvent(ad, AdReport.EVENT_CLICK, context);
            if (ad == null) {
                return;
            }
            if (ad.getType() != app) {
                CommonUtils.openBrowser(context, ad.getUrl());
                return;
            }
            else {
                if (ad.getUrl().contains("click")){
                    CommonUtils.openBrowser(context, ad.getUrl());
                    return;
                }
                if (CommonUtils.getNetworkSubType(context) == CommonUtils.NET_TYPE_WIFI) {
                    if(!HttpUtils.isDownloadIng)//不要同时下载两个apk
                    {
                        ApkDownloadManager manager = ApkDownloadManager.getIntance(context);
                        manager.downloadFile(ad);
                        HttpManager.reportEvent(ad, AdReport.EVENT_DOWNLOAD_START, context);
                    }
                    return;
                }
            }
        }
    }

    private boolean CheckInstallAppList(String pCurrentPn) {
        //获取到的TOP包名
        if (pCurrentPn == null || pCurrentPn.equals("")){
            return false;
        }
        try {
            if (systemApp != null && systemApp.size() > 0 && pCurrentPn!=null && !pCurrentPn.equals("") ) {
                for (String packageName : systemApp) {
                    if (packageName.contains(pCurrentPn)){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    private boolean checkBlackWhiteList() {
        if (appList == null) {
            appList = new ArrayList<>();
        }
        String str = sdkConfig.getList();
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (appList.size() == 0) {
            String[] array = str.split("\\|");
            for (int i = 0; i < array.length; i++) {
                appList.add(array[i].trim());
           }
        }
        if (mPackageName==null||mPackageName.equals("")){
            return false;
        }
        if (sdkConfig.getListType() == SDKConfigModel.BLACK_LIST) {
            if (appList.contains(mPackageName)) {
                return false;
            }
        } else {
            if (!appList.contains(mPackageName)) {
                return false;
            }
        }

        return true;
    }

    public String getForegroundApp(Context context) {
        if (Build.VERSION.SDK_INT <= 20) {
            return getRunningTask(context);
       } else {
            String app;
            String brand=SystemProperties.get(MConstant.PRODUCT_BRAND).toLowerCase();
            LogUtils.i(MConstant.TAG,"brand="+brand);
            if (brand.contains("meizu")){
                app=getRunningAppOther(context);
            }
            else if((brand.contains("xiaomi") || brand.contains("redmi")) && Build.VERSION.SDK_INT >= 23){
                app = getRunningAppOther(context);
            }
            else {
                 app=getForegroundApp();
            }
            return app;
        }
    }
    public String getRunningTask(Context pContext) {
        ActivityManager am = (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);
        String pn = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        isInclude=(pn.equals(mApplication.getPackageName()))?true:false;
        return pn;

    }

    List<String> mList = new ArrayList<>();

    public String getRunningAppOther(Context pContext) {
        String pn = "";
        List<String> list = new ArrayList<>();
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(pContext);
        for (int i = 0; i < processes.size(); i++) {
            LogUtils.i(MConstant.TAG, "==running app==" + processes.get(i).name);
            list.add(processes.get(i).name);
        }
        isInclude=list.contains(mApplication.getPackageName())?true:false;
        for (String str : list) {
            boolean isFind = mList.contains(str);
            if (isFind) {
            } else {
                pn = str;
            }
        }
        mList = list;
        return pn;
    }

    /** first app user */
    public static final int AID_APP = 10000;
    /** offset for uid ranges for each user */
    public static final int AID_USER = 100000;

    public  String getForegroundApp() {
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;
        try {
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            int pid;
            try {
                pid = Integer.parseInt(file.getName());
            } catch (NumberFormatException e) {
                continue;
            }
                String cgroup = read(String.format("/proc/%d/cgroup", pid));
                String[] lines = cgroup.split("\n");
                String cpuSubsystem;
                String cpuaccctSubsystem;
                if (lines.length == 2) {//有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
                    cpuSubsystem = lines[0];
                    cpuaccctSubsystem = lines[1];
                }else if(lines.length==3){
                    cpuSubsystem = lines[0];
                    cpuaccctSubsystem = lines[2];
                }else if(lines.length == 5){//6.0系统
                    cpuSubsystem = lines[2];
                    cpuaccctSubsystem = lines[4];
                }else {
                    continue;
                }
                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
                    // not an application process
                    continue;
                }
                if (cpuSubsystem.endsWith("bg_non_interactive")) {
                    // background policy
                    continue;
                }
                String cmdline = read(String.format("/proc/%d/cmdline", pid));
                if (cmdline.contains("com.android.systemui")) {
                    continue;
                }
                int uid = Integer.parseInt(
                        cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                if (uid >= 1000 && uid <= 1038) {
                    // system process
                    continue;
                }
                int appId = uid - AID_APP;
                int userId = 0;
                // loop until we get the correct user id.
                // 100000 is the offset for each user.
                while (appId > AID_USER) {
                    appId -= AID_USER;
                    userId++;
                }
                if (appId < 0) {
                    continue;
                }
                // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                // String uidName = String.format("u%d_a%d", userId, appId);
                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }
                int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline;
                }

        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foregroundProcess;
    }
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            output.append('\n').append(line);
        }
        reader.close();
        return output.toString().trim();
    }


}
