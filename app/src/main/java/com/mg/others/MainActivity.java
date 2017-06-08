package com.mg.others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mg.others.ooa.MiiService;
import com.mg.others.utils.AppManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, SecondActivity.class));
        startService(new Intent(this, MiiService.class));
    }
}
