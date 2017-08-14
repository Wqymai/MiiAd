package com.mg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mg.headup.MgHeadupAD;
import com.mg.interf.MiiADListener;

/**
 * Created by wuqiyan on 17/8/11.
 */

public class ListActivity extends Activity {
    ListView listView;
    MgHeadupAD headupAD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        headupAD =  new MgHeadupAD(this,false, Contants.APPID, Contants.BID, new MiiADListener() {
            @Override
            public void onMiiADDismissed() {
                Log.i("ad_demo","横幅广告 onMiiADDismissed");
            }

            @Override
            public void onMiiADPresent() {
                Log.i("ad_demo","横幅广告 onMiiADPresent");
            }

            @Override
            public void onMiiADClicked() {
                Log.i("ad_demo","横幅广告 onMiiADClicked");
                headupAD.loadHeadupAD();
            }

            @Override
            public void onMiiADTouched() {
                Log.i("ad_demo","横幅广告 onMiiADTouched");
            }

             @Override
             public void onMiiADTick(long millisUntilFinished) {
                //不回调
             }

             @Override
            public void onMiiNoAD(int errCode) {
                Log.i("ad_demo","横幅广告 "+errCode);
            }
        });

        headupAD.loadHeadupAD();




        listView = (ListView) findViewById(R.id.listview);
        String[] strs =new String[50];
        for (int i=0;i<50;i++){
            strs[i]=i+" 测试数据";
        }
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ad_demo","横幅广告 onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ad_demo","横幅广告 onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ad_demo","横幅广告 onDestroy");
        headupAD.recycle();
    }
}
