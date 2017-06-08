package com.mg.splash;

import android.app.Activity;
import android.os.Handler;

import com.mg.others.message.ISender;
import com.mg.others.model.DeviceInfo;
import com.mg.others.ooa.MAdSDK;
import com.mg.others.ooa.MConstant;
import com.mg.others.task.DeviceInfoTask;
import com.mg.others.task.IDeviceInfoListener;
import com.mg.others.utils.CommonUtils;

import static com.mg.others.ooa.MAdSDK.sInstance;


/**
 * Created by wuqiyan on 17/6/8.
 */

public class MSplashAD implements IDeviceInfoListener{

    private Activity mActivity;
    private DeviceInfo mDeviceInfo = null;


    public MSplashAD(Activity mActivity){

        this.mActivity = mActivity;
        mDeviceInfo = CommonUtils.readParcel(mActivity.getApplicationContext(), MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){
            new DeviceInfoTask(sInstance,mActivity.getApplicationContext()).execute();
        }
    }

    @Override
    public void deviceInfoLoaded(DeviceInfo deviceInfo) {

    }


//    static int width;
//    static int height;
//    public void openDamaiDialog(Activity mActivity, String txt, int type) {
//        // 生成对话框
//        AlertDialog dlg = new AlertDialog.Builder(mActivity).create();
//        //显示对框框
//        dlg.show();
//
//
//        WindowManager wm = mActivity.getWindowManager();
//        width = wm.getDefaultDisplay().getWidth();
//        height=wm.getDefaultDisplay().getHeight();
//
//
//
////        dlg.getWindow().setContentView(R.layout.com.mg.splash);
//        Window window=dlg.getWindow();
//        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
//        params1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//
//        params1.height = (int)(height);
//        params1.width=(int) (width);
//        window.setAttributes(params1);
//
//        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//
//
//        //添加自定义的Layout以及布局方式，注意传入dlg对象本身方便关闭该提示框
//        window.setContentView(new MyDialog(mActivity,dlg,txt,type),params);
//
//    }
//    public class MyDialog extends RelativeLayout {
//
//        private AlertDialog dlg;
//        private Context context;
//        private String txt;
//        private int type;
//
//        public MyDialog(Context context,AlertDialog dlg,String txt,int type) {
//            super(context);
//            this.context=context;
//            this.dlg=dlg;
//            this.txt=txt;
//            this.type=type;
//            init();
//        }
//
//        private void init() {
//
//
//
//            RelativeLayout.LayoutParams pParams=new RelativeLayout.LayoutParams(ViewGroup
//                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            RelativeLayout  pLayout=new RelativeLayout(context);
//            pLayout.setLayoutParams(pParams);
//            pLayout.setBackgroundColor(Color.parseColor("#EBEBEB"));
//
//
//            TextView textView=new TextView(context);
//            textView.setText(txt);
//            RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            textParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            textParam.setMargins(40,(int) ((height*0.35)*0.15),40,0);
//            textView.setLayoutParams(textParam);
//            textView.setTextSize(18);
//            textView.setTextColor(Color.parseColor("#636363"));
//            textView.setGravity(Gravity.CENTER);
//            pLayout.addView(textView);
//
//
//            LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//            addView(pLayout, lp);
//
//
//
//
//        }
//
//    }
}
