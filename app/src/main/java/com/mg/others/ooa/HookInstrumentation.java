package com.mg.others.ooa;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;

import com.mg.others.utils.AppManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wuqiyan on 16/12/7.
 */
public class HookInstrumentation extends Instrumentation {
    private static final String TAG = "ci";

    private Instrumentation mOldInstr = null;

    private Method methodResume = null;
    private Method methodPause = null;

    private void obtainMethods() throws NoSuchMethodException
    {
        if ( mOldInstr == null )
            return;
        Class<?> inClass = mOldInstr.getClass();
        methodResume = inClass.getMethod("callActivityOnResume", Activity.class);
        methodPause = inClass.getMethod("callActivityOnPause", Activity.class);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        Log.i(TAG, "oncreate Intercept here."+activity.getClass().getName());
        AppManager.getAppManager().addActivity(activity);
        super.callActivityOnCreate(activity, icicle);
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        Log.i(TAG, "OnPause Intercept here."+activity.getClass().getName());

        if ( methodPause != null )
        {
            try {
                methodPause.invoke(mOldInstr, activity);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return;
            }
        }
        else
            super.callActivityOnPause(activity);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        if ( methodResume != null )
        {
            try {
                methodResume.invoke(mOldInstr, activity);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return;
            }

            Log.i(TAG, "OnResume Intercept here."+activity.getClass().getName());
        }
        else
            super.callActivityOnResume(activity);
    }

    public boolean initOldInstr(Instrumentation oldInstr) {
        mOldInstr = oldInstr;
        try {
            obtainMethods();
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
    }
}
