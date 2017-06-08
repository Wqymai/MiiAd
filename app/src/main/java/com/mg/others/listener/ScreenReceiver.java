package com.mg.others.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mg.others.ooa.MConstant;
import com.mg.others.ooa.MiiService;


public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MiiService.class);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            i.putExtra(MConstant.sence.screen_on, true);
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            i.putExtra(MConstant.sence.screen_off, true);
        }
        context.startService(i);
    }
}
