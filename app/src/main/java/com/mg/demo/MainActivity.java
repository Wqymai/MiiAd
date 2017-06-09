package com.mg.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.others.R;

/**
 * Created by wuqiyan on 17/6/9.
 */

public class MainActivity extends Activity {
    Button openSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openSplash= (Button) findViewById(R.id.open_splash_ad);
        openSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
            }
        });
    }
}
