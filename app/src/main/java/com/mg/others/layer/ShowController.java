package com.mg.others.layer;

import android.content.Context;
import android.view.WindowManager;

import com.mg.others.model.AdPercentage;

/**
 * Created by wuqiyan on 17/4/20.
 */

public class ShowController {

    AdShowBase ad=null;
    private ShowController(){}
    public static ShowController getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder{
        private static final ShowController instance=new ShowController();
    }

    public void setAdType(int type){
        switch (type){
            case AdPercentage.SPLASH:
                this.ad=new SelfSplash();
                break;
            case AdPercentage.BANNER:
                this.ad=new SelfBanner();
                break;
            case AdPercentage.INTERSTITIAL:
                this.ad=new SelfInterstitial();
                break;
            default:
                break;
        }
    }
    public WindowManager.LayoutParams showSelfAd(String adLoc, int adSource, Context context, WindowManager.LayoutParams params){
        return ad.showSelfAd(adLoc,adSource,context,params);
    }

}
