package com.mg.others.model;

import java.io.Serializable;

public class SDKConfigModel implements Serializable {

    public static final int JUMP_SHOW = 1;


    private String adShow;                     //是否允许展示
    private int splash_time;                    //开屏广告展示时间
    private int show_sum;                       //每日展示次数
    private int next;                           //下次请求初始化时间
    private int ce;
    private int cz;
    private String sk;

    private int ksf_mg;
    private int ksf_gdt;
    private int xsf_mg;
    private int xsf_gdt;
    private int bsf_mg;
    private int bsf_gdt;
    private int csf_mg;
    private int csf_gdt;

    public int getCz() {
        return cz;
    }

    public void setCz(int cz) {
        this.cz = cz;
    }
    public int getKsf_mg() {
        return ksf_mg;
    }

    public void setKsf_mg(int ksf_mg) {
        this.ksf_mg = ksf_mg;
    }

    public int getKsf_gdt() {
        return ksf_gdt;
    }

    public void setKsf_gdt(int ksf_gdt) {
        this.ksf_gdt = ksf_gdt;
    }

    public int getXsf_mg() {
        return xsf_mg;
    }

    public void setXsf_mg(int xsf_mg) {
        this.xsf_mg = xsf_mg;
    }

    public int getXsf_gdt() {
        return xsf_gdt;
    }

    public void setXsf_gdt(int xsf_gdt) {
        this.xsf_gdt = xsf_gdt;
    }

    public int getBsf_mg() {
        return bsf_mg;
    }

    public void setBsf_mg(int bsf_mg) {
        this.bsf_mg = bsf_mg;
    }

    public int getBsf_gdt() {
        return bsf_gdt;
    }

    public void setBsf_gdt(int bsf_gdt) {
        this.bsf_gdt = bsf_gdt;
    }

    public int getCsf_mg() {
        return csf_mg;
    }

    public void setCsf_mg(int csf_mg) {
        this.csf_mg = csf_mg;
    }

    public int getCsf_gdt() {
        return csf_gdt;
    }

    public void setCsf_gdt(int csf_gdt) {
        this.csf_gdt = csf_gdt;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }



    public String getAdShow() {
        return adShow;
    }

    public void setAdShow(String adShow) {
        this.adShow = adShow;
    }



    public int getCe() {
        return ce;
    }

    public void setCe(int ce) {
        this.ce = ce;
    }

    public int getSplash_time() {
        return splash_time;
    }

    public void setSplash_time(int splash_time) {
        this.splash_time = splash_time;
    }

    public int getShow_sum() {
        return show_sum;
    }

    public void setShow_sum(int show_sum) {
        this.show_sum = show_sum;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n"+"adShow = " +adShow + "\n");
        sb.append("splash_time = " +splash_time + "\n");
        sb.append("show_sum = " +show_sum+ "\n");
        sb.append("next = " +next+ "\n");
        sb.append("ce= "+ce);
        return sb.toString();
    }





//    /**
//     * 根据广告种类不同得到不同的广告消失时间
//     * @param type
//     * @return
//     */
//    public int getDisplayTime(int type) {
//        int time = 0;
//        switch (type) {
//            case 1:
//                time = getBanner_time();
//                break;
//
//            case 3:
//                time = getInterstitial_time();
//                break;
//
//            case 2:
//                time = getSplash_time();
//                break;
//        }
//        return time;
//    }
}
