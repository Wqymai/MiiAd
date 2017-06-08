package com.mg.others.model;


public enum  MmAdType {
    Banner(1),
    Interstitial(2),
    Splash(3);
    private int type;
    private MmAdType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
