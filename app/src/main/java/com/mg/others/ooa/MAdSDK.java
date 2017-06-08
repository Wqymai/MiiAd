package com.mg.others.ooa;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mg.others.layer.LayerManager;
import com.mg.others.listener.BootReceiver;
import com.mg.others.listener.ScreenReceiver;
import com.mg.others.listener.UserPresentReceiver;
import com.mg.others.manager.RuleManager;
import com.mg.others.model.AdSence;
import com.mg.others.model.DeviceInfo;
import com.mg.others.model.SDKConfigModel;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;

import static com.mg.others.utils.SystemUtils.checkPasswordToUnLock;


public class MAdSDK implements IDeviceInfoListener {

    private Context mContext;
    public static MAdSDK sInstance = null;
    private DeviceInfo mDeviceInfo = null;
    private SDKConfigModel config = null;

    public static MAdSDK getInstance() {
        if (sInstance == null){
            synchronized (MAdSDK.class){
                if (sInstance == null){
                    sInstance = new MAdSDK();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();

//        initReceivers(mContext);
        initConfig();
    }

    private void initConfig(){
        mDeviceInfo = CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){
            new DeviceInfoTask(sInstance,mContext).execute();
        }
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public void deviceInfoLoaded(DeviceInfo deviceInfo) {
        LogUtils.i(MConstant.TAG,"init load device info over");
        CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);
    }





    private void initReceivers(Context pContext) {


        UserPresentReceiver userPresentReceiver=new UserPresentReceiver();
        IntentFilter if_userpresent = new IntentFilter();
        if_userpresent.addAction(Intent.ACTION_USER_PRESENT);
        pContext.registerReceiver(userPresentReceiver,if_userpresent);

        BootReceiver bootReceiver=new BootReceiver();
        IntentFilter if_boot=new IntentFilter();
        if_boot.addAction(Intent.ACTION_PACKAGE_ADDED);
        if_boot.addAction(Intent.ACTION_PACKAGE_REMOVED);
        if_boot.addDataScheme("package");
        pContext.registerReceiver(bootReceiver,if_boot);

        ScreenReceiver screenReceiver=new ScreenReceiver();
        IntentFilter screen=new IntentFilter();
        screen.addAction(Intent.ACTION_SCREEN_OFF);
        screen.addAction(Intent.ACTION_SCREEN_ON);
        pContext.registerReceiver(screenReceiver,screen);

    }

    public void startAd(Intent intent, Application application){
        LogUtils.i(MConstant.TAG,"startAd");
        int triggerPoint = 0;
        RuleManager rule = RuleManager.getInstance(application, LayerManager.obtainLocal(application));
        boolean needListen = true;
        if (intent != null) {
            if (intent.getBooleanExtra(MConstant.sence.user_present, false)) {
                MConstant.isBlack=false;
                triggerPoint = AdSence.USER_PRESENT;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.screen_off, false)) {
                MConstant.isBlack=true;
                needListen=false;
            }
            if (intent.getBooleanExtra(MConstant.sence.screen_on, false)) {
                if (!checkPasswordToUnLock(application)){
                    MConstant.isBlack=false;
                }
            }
            if (intent.getBooleanExtra(MConstant.sence.install, false)){
                MConstant.isBlack=false;
                triggerPoint = AdSence.INSTALL;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.uninstall, false)){
                MConstant.isBlack=false;
                triggerPoint = AdSence.UNINSTALL;
                needListen = true;
            }
            if (needListen) {
                rule.listen(triggerPoint);
            }

        }
    }

}
