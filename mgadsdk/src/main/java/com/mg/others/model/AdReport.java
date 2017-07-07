package com.mg.others.model;

import java.io.Serializable;


public class AdReport implements Serializable {
    public static final int EVENT_SHOW = 0;
    public static final int EVENT_CLICK = 1;
    public static final int EVENT_DOWNLOAD_START = 2;
    public static final int EVENT_DOWNLOAD_COMPLETE = 3;
    public static final int EVENT_INSTALL_COMLETE = 4;
    public static final int EVENT_OPEN = 5;

    public String[] urlShow;

    public String[] urlClick;

    public String[] urlDownloadStart;

    public String[] urlDownloadComplete;

    public String[] urlInstallComplete;

    public String[] urlOpen;

    public String[] getUrlShow() {
        return urlShow;
    }

    public void setUrlShow(String[] urlShow) {
        this.urlShow = urlShow;
    }

    public String[] getUrlClick() {
        return urlClick;
    }

    public void setUrlClick(String[] urlClick) {
        this.urlClick = urlClick;
    }

    public String[] getUrlDownloadStart() {
        return urlDownloadStart;
    }

    public void setUrlDownloadStart(String[] urlDownloadStart) {
        this.urlDownloadStart = urlDownloadStart;
    }

    public String[] getUrlDownloadComplete() {
        return urlDownloadComplete;
    }

    public void setUrlDownloadComplete(String[] urlDownloadComplete) {
        this.urlDownloadComplete = urlDownloadComplete;
    }

    public String[] getUrlInstallComplete() {
        return urlInstallComplete;
    }

    public void setUrlInstallComplete(String[] urlInstallComplete) {
        this.urlInstallComplete = urlInstallComplete;
    }

    public String[] getUrlOpen() {
        return urlOpen;
    }

    public void setUrlOpen(String[] urlOpen) {
        this.urlOpen = urlOpen;
    }
}
