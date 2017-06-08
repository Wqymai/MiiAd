package com.mg.others.model;

import java.io.Serializable;


public class SDKConfigModel implements Serializable {
    public static final int AD_OPEN = 1;
    public static final int JUMP_SHOW = 1;
    public static final int JUMP_FUNCTION_NORMAL = 1;
    public static final int JMMP_FUNCTION_CLICK = 0;
    public static final int BLACK_LIST = 1;
    public static final int WHITE_LIST = 2;

    private boolean adShow;                     //是否允许展示
    private int show_percentage;                //广告展示几率
    private int splash_time;                    //开屏广告消失时间
    private int interstitial_time;
    private int banner_time;
    private int interstitial_delay_time;
    private boolean jump;                       //跳过按钮是否显示
    private int jump_function;                  //1 jump    2 click
    private int show_sum;                       //每日展示次数
    private int interval;                       //广告展示间隔时间
    private int next;                           //下次请求初始化时间
    private int auto_show_percentage;           //模拟点击概率
    private int listType;                       //1 黑名单   2 白名单
    private String list;                        //名单列表
    private AdPercentage percentage;            //广告权重
    private int ce;
    private long time0;
    private long time1;
    private long time2;
    private long time3;
    private long timeComm;

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    private String bp;

    public long getTimeComm() {
        return timeComm;
    }

    public void setTimeComm(long timeComm) {
        this.timeComm = timeComm;
    }

    public int getAuto_show_percentage() {
        return auto_show_percentage;
    }

    public void setAuto_show_percentage(int auto_show_percentage) {
        this.auto_show_percentage = auto_show_percentage;
    }
    public long getTime2() {
        return time2;
    }

    public void setTime2(long time2) {
        this.time2 = time2;
    }

    public long getTime3() {
        return time3;
    }

    public void setTime3(long time3) {
        this.time3 = time3;
    }

    public long getTime4() {
        return time4;
    }

    public void setTime4(long time4) {
        this.time4 = time4;
    }

    private long time4;

    public long getTime6() {
        return time6;
    }

    public void setTime6(long time6) {
        this.time6 = time6;
    }

    private long time6;

    public long getTime0() {
        return time0;
    }

    public void setTime0(long time0) {
        this.time0 = time0;
    }

    public long getTime1() {
        return time1;
    }

    public void setTime1(long time1) {
        this.time1 = time1;
    }

    public int getCe() {
        return ce;
    }

    public void setCe(int ce) {
        this.ce = ce;
    }

    public long getCold_time() {
        return cold_time;
    }

    public void setCold_time(long cold_time) {
        this.cold_time = cold_time;
    }

    private AdSence sence;                      //广告展示场景
    private long cold_time;                     //冷却时间


    private long updateTime;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isAdShow() {
        return adShow;
    }

    public void setAdShow(boolean adShow) {
        this.adShow = adShow;
    }

    public int getShow_percentage() {
        return show_percentage;
    }

    public void setShow_percentage(int show_percentage) {
        this.show_percentage = show_percentage;
    }

    public int getSplash_time() {
        return splash_time;
    }

    public void setSplash_time(int splash_time) {
        this.splash_time = splash_time;
    }

    public int getInterstitial_time() {
        return interstitial_time;
    }

    public void setInterstitial_time(int interstitial_time) {
        this.interstitial_time = interstitial_time;
    }

    public int getBanner_time() {
        return banner_time;
    }

    public void setBanner_time(int banner_time) {
        this.banner_time = banner_time;
    }

    public int getInterstitial_delay_time() {
        return interstitial_delay_time;
    }

    public void setInterstitial_delay_time(int interstitial_delay_time) {
        this.interstitial_delay_time = interstitial_delay_time;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public int getJump_function() {
        return jump_function;
    }

    public void setJump_function(int jump_function) {
        this.jump_function = jump_function;
    }

    public int getShow_sum() {
        return show_sum;
    }

    public void setShow_sum(int show_sum) {
        this.show_sum = show_sum;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public AdPercentage getPercentage() {
        return percentage;
    }

    public void setPercentage(AdPercentage percentage) {
        this.percentage = percentage;
    }

    public AdSence getSence() {
        return sence;
    }

    public void setSence(AdSence sence) {
        this.sence = sence;
    }

//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("\n"+"adShow = " +adShow + "\n");
//        sb.append("show_percentage = " +show_percentage + "\n");
//        sb.append("splash_time = " +splash_time + "\n");
//        sb.append("interstitial_time = " +interstitial_time + "\n");
//        sb.append("banner_time = " +banner_time + "\n");
//        sb.append("interstitial_delay_time = " +interstitial_delay_time+ "\n");
//        sb.append("jump = " +jump + "\n");
//        sb.append("jump_function = " +jump_function+ "\n");
//        sb.append("show_sum = " +show_sum+ "\n");
//        sb.append("interval = " +interval+ "\n");
//        sb.append("next = " +next+ "\n");
//        sb.append("banner_p = " + getPercentage().getBanner_p() + "\n");
//        sb.append("splash_p = " + getPercentage().getSplash_p() + "\n");
//        sb.append("interstitial_p = " + getPercentage().getInterstitial_p() + "\n");
//        sb.append("updateTime = " + updateTime + "\n");
//        sb.append("white/black-List = " + list + "\n");
//        sb.append("listType = " +listType+"\n");
//        sb.append("ce= "+ce);
//
//        return sb.toString();
//    }

    /**
     * 根据各类型广告权重选择广告
     * @return
     */
    public int choseAdType(){
        AdPercentage percentage = getPercentage();
        int r = (int) (Math.random() * 100);
        int banner = percentage.getBanner_p();
        int interstitial = percentage.getInterstitial_p();
        int splash = percentage.getSplash_p();
        Integer [] ads = new Integer[]{banner, splash, interstitial};
        int temp = 0;
        for (int i = 0; i < ads.length; i++) {
            temp += ads[i];
            if (temp > r){
                percentage.setChoseAdType(i+1);
                break;
            }
        }
        return percentage.getChoseAdType();
    }

    /**
     * 根据场景选择广告
     * @param sence
     * @return
     */
    public int choseAdTypeBySence(int sence){
        int adType = 0;
        switch(sence){
            case AdSence.INSTALL:
                adType = 3;
                break;
            case AdSence.UNINSTALL:
                adType = 3;
                break;
            case AdSence.USER_PRESENT:
                adType = 2;
                break;
        }
        return adType;
    }

    /**
     * 根据广告种类不同得到不同的广告消失时间
     * @param type
     * @return
     */
    public int getDisplayTime(int type) {
        int time = 0;
        switch (type) {
            case AdPercentage.BANNER:
                time = getBanner_time();
                break;

            case AdPercentage.INTERSTITIAL:
                time = getInterstitial_time();
                break;

            case AdPercentage.SPLASH:
                time = getSplash_time();
                break;
        }
        return time;
    }
}
