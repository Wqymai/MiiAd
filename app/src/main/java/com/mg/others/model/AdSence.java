package com.mg.others.model;

import java.io.Serializable;


public class AdSence implements Serializable {
    public static final int INSTALL = 2;
    public static final int UNINSTALL = 3;          //下载？？？
    public static final int SCREEN_ON = 7;
    public static final int CONNECTCHANGE = 4;
    public static final int NORMAL = -1;
    public static final int BATTERY = 5;
    public static final int SCREEN_OFF=6;
    public static final int USER_PRESENT=1;


    private int sence_install;
    private int sence_uninstall;
    private int sence_screenOn;
    private int sence_connectChange;
    private int sence_battery;

    public int getSence_battery() {
        return sence_battery;
    }

    public void setSence_battery(int sence_battery) {
        this.sence_battery = sence_battery;
    }

    public int getSence_install() {
        return sence_install;
    }

    public void setSence_install(int sence_install) {
        this.sence_install = sence_install;
    }

    public int getSence_uninstall() {
        return sence_uninstall;
    }

    public void setSence_uninstall(int sence_uninstall) {
        this.sence_uninstall = sence_uninstall;
    }

    public int getSence_screenOn() {
        return sence_screenOn;
    }

    public void setSence_screenOn(int sence_screenOn) {
        this.sence_screenOn = sence_screenOn;
    }

    public int getSence_connectChange() {
        return sence_connectChange;
    }

    public void setSence_connectChange(int sence_connectChange) {
        this.sence_connectChange = sence_connectChange;
    }

    public int choseAdBySence(int sence){
        int adType = 0;
        switch(sence){
            case SCREEN_ON:
                adType = getSence_screenOn();
            break;

            case INSTALL:
                adType = getSence_install();
                break;

            case UNINSTALL:
                adType = getSence_uninstall();
                break;
        }
        return adType;
    }
}
