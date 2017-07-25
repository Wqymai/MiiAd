//package com.mg.an;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.TextView;
//
//import com.android.others.R;
//import com.mg.interf.MiiCustomAdListener;
//import com.mg.tuia.MiiTuiaAD;
//
///**
// * Created by wuqiyan on 17/7/24.
// */
//
//public class TuiaCustomActivity extends Activity {
//    private TextView textView;
//    private MiiTuiaAD tuiaAD;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_non_standar);
//        textView = (TextView)findViewById(R.id.content_text);
//        tuiaAD = new MiiTuiaAD();
//        tuiaAD.loadAd(this,465);
//        tuiaAD.setAdListener(new MiiCustomAdListener() {
//            @Override
//            public void onReceiveAd(String result) {
//                textView.setText(result);
//            }
//
//            @Override
//            public void onFailedToReceiveAd() {
//
//            }
//        });
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tuiaAD.adExposed();
//                tuiaAD.adClicked();
//            }
//        });
//    }
//}
