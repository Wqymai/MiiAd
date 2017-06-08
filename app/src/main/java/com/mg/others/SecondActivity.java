package com.mg.others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mg.others.ooa.MiiService;

/**
 * Created by wuqiyan on 17/6/8.
 */

public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondlayout);
        startService(new Intent(this, MiiService.class));
    }
}
