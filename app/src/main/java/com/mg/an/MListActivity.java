package com.mg.an;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mg.R;
import com.mg.comm.MConstant;
import com.mg.headup.MiiHeadupAD;
import com.mg.interf.MiiADListener;
import com.mg.others.utils.LogUtils;

/**
 * Created by wuqiyan on 17/8/11.
 */

public class MListActivity extends Activity {
    ListView listView;
    MiiHeadupAD notifyAD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

         notifyAD =  new MiiHeadupAD(this,true, MConstant.APPID, MConstant.BID, new MiiADListener() {
            @Override
            public void onMiiADDismissed() {

            }

            @Override
            public void onMiiADPresent() {

            }

            @Override
            public void onMiiADClicked() {
//                notifyAD.loadHeadupAD();
            }

            @Override
            public void onMiiADTouched() {

            }

             @Override
             public void onMiiADTick(long millisUntilFinished) {

             }

             @Override
            public void onMiiNoAD(int errCode) {
                LogUtils.i(MConstant.TAG,"横幅广告 "+errCode);
            }
        });

        notifyAD.loadHeadupAD();




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


    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notifyAD.recycle();
    }
}
