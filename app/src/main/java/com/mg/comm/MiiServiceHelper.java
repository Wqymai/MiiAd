//package com.mg.comm;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.os.Build;
//import android.os.Handler;
//import android.os.HandlerThread;
//
//import com.mg.others.manager.HttpManager;
//import com.mg.others.model.AdModel;
//import com.mg.others.model.AdReport;
//
//import com.mg.others.process.AndroidProcesses;
//import com.mg.others.process.models.AndroidAppProcess;
//import com.mg.others.utils.LogUtils;
//import com.mg.others.utils.SystemProperties;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by wuqiyan on 17/6/13.
// */
//
//public class MiiServiceHelper {
//
//    public void  checkActive(final Context mContext,final AdModel ad){
//
//        //激活上报暂时没写
//        HandlerThread handlerThread=new HandlerThread("cs");
//        final Handler handler=new Handler(handlerThread.getLooper());
//        final long startTime=System.currentTimeMillis();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long currTime=System.currentTimeMillis();
//                if (currTime - startTime > 5000){
//                    handler.removeCallbacks(this);
//                    return;
//                }
//                String currPN=getCurrApkName(mContext);
//                if (currPN.equals(ad.getPkName())){
//                    //激活上报
//                    HttpManager.reportEvent(ad, AdReport.EVENT_OPEN, mContext);
//                }
//            }
//        });
//
//
//
//    }
//    private String getCurrApkName(Context context) {
//        if (Build.VERSION.SDK_INT <= 20) {
//            return getRunningTask(context);
//        } else {
//            String app;
//            String brand= SystemProperties.get(MConstant.PRODUCT_BRAND).toLowerCase();
//            LogUtils.i(MConstant.TAG,"brand="+brand);
//            if (brand.contains("meizu")){
//                app=getRunningAppOther(context);
//            }
//            else if((brand.contains("xiaomi") || brand.contains("redmi")) && Build.VERSION.SDK_INT >= 23){
//                app = getRunningAppOther(context);
//            }
//            else {
//                app=getForegroundApp();
//            }
//            return app;
//        }
//    }
//    private String getRunningTask(Context pContext) {
//        ActivityManager am = (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);
//        String pn = am.getRunningTasks(1).get(0).topActivity.getPackageName();
//        return pn;
//
//    }
//
//    List<String> mList = new ArrayList<>();
//
//    private String getRunningAppOther(Context pContext) {
//        String pn = "";
//        List<String> list = new ArrayList<>();
//        List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(pContext);
//        for (int i = 0; i < processes.size(); i++) {
//            LogUtils.i(MConstant.TAG, "==running app==" + processes.get(i).name);
//            list.add(processes.get(i).name);
//        }
//
//        for (String str : list) {
//            boolean isFind = mList.contains(str);
//            if (isFind) {
//            } else {
//                pn = str;
//            }
//        }
//        mList = list;
//        return pn;
//    }
//    /** first app user */
//    private static final int AID_APP = 10000;
//    /** offset for uid ranges for each user */
//    private static final int AID_USER = 100000;
//
//    private  String getForegroundApp() {
//        File[] files = new File("/proc").listFiles();
//        int lowestOomScore = Integer.MAX_VALUE;
//        String foregroundProcess = null;
//        try {
//            for (File file : files) {
//                if (!file.isDirectory()) {
//                    continue;
//                }
//                int pid;
//                try {
//                    pid = Integer.parseInt(file.getName());
//                } catch (NumberFormatException e) {
//                    continue;
//                }
//                String cgroup = read(String.format("/proc/%d/cgroup", pid));
//                String[] lines = cgroup.split("\n");
//                String cpuSubsystem;
//                String cpuaccctSubsystem;
//                if (lines.length == 2) {//有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
//                    cpuSubsystem = lines[0];
//                    cpuaccctSubsystem = lines[1];
//                }else if(lines.length==3){
//                    cpuSubsystem = lines[0];
//                    cpuaccctSubsystem = lines[2];
//                }else if(lines.length == 5){//6.0系统
//                    cpuSubsystem = lines[2];
//                    cpuaccctSubsystem = lines[4];
//                }else {
//                    continue;
//                }
//                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
//                    // not an application process
//                    continue;
//                }
//                if (cpuSubsystem.endsWith("bg_non_interactive")) {
//                    // background policy
//                    continue;
//                }
//                String cmdline = read(String.format("/proc/%d/cmdline", pid));
//                if (cmdline.contains("com.android.systemui")) {
//                    continue;
//                }
//                int uid = Integer.parseInt(
//                        cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
//                if (uid >= 1000 && uid <= 1038) {
//                    // system process
//                    continue;
//                }
//                int appId = uid - AID_APP;
//                int userId = 0;
//                // loop until we get the correct user id.
//                // 100000 is the offset for each user.
//                while (appId > AID_USER) {
//                    appId -= AID_USER;
//                    userId++;
//                }
//                if (appId < 0) {
//                    continue;
//                }
//                // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
//                // String uidName = String.format("u%d_a%d", userId, appId);
//                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
//                if (oomScoreAdj.canRead()) {
//                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
//                    if (oomAdj != 0) {
//                        continue;
//                    }
//                }
//                int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
//                if (oomscore < lowestOomScore) {
//                    lowestOomScore = oomscore;
//                    foregroundProcess = cmdline;
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return foregroundProcess;
//    }
//    private static String read(String path) throws IOException {
//        StringBuilder output = new StringBuilder();
//        BufferedReader reader = new BufferedReader(new FileReader(path));
//        output.append(reader.readLine());
//        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
//            output.append('\n').append(line);
//        }
//        reader.close();
//        return output.toString().trim();
//    }
//}
