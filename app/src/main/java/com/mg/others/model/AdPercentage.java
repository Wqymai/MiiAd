package com.mg.others.model;

import java.io.Serializable;

public class AdPercentage implements Serializable {

    public static final int BANNER = 1;
    public static final int SPLASH = 2;
    public static final int INTERSTITIAL =3;
    public static final int HTML5 =4;

    private int banner_p;
    private int interstitial_p;
    private int splash_p;
    private int choseAdType;

    public int getBanner_p() {
        return banner_p;
    }

    public void setBanner_p(int banner_p) {
        this.banner_p = banner_p;
    }

    public int getInterstitial_p() {
        return interstitial_p;
    }

    public void setInterstitial_p(int interstitial_p) {
        this.interstitial_p = interstitial_p;
    }

    public int getSplash_p() {
        return splash_p;
    }

    public void setSplash_p(int splash_p) {
        this.splash_p = splash_p;
    }

    public int getChoseAdType() {
        return choseAdType;
    }

    public void setChoseAdType(int choseAdType) {
        this.choseAdType = choseAdType;
    }
}
