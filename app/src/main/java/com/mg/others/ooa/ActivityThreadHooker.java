package com.mg.others.ooa;

import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wuqiyan on 16/12/7.
 */
public class ActivityThreadHooker {
    private static final String TAG = "ci";

    private static ActivityThreadHooker instance = null;

    private HookInstrumentation mHookInstr = null;

    private Instrumentation mOldInstr = null;

    private static boolean mHooked = false;

    public static boolean getHooked()
    {
        return mHooked;
    }

    public ActivityThreadHooker() throws Throwable
    {
        synchronized (ActivityThreadHooker.class)
        {
            if (instance != null)
                throw new Exception("Only one ActivityThreadHooker instance can be created.");
                        instance = this;
        }
        mHookInstr = new HookInstrumentation();

        try
        {
            initHook();
        } catch (NoSuchFieldException ensf)
        {
            ensf.printStackTrace();
            return;
        } catch (IllegalArgumentException eilar)
        {
            eilar.printStackTrace();
            return;
        } catch (IllegalAccessException eilac)
        {
            eilac.printStackTrace();
            return;
        } catch (ClassNotFoundException ecnf)
        {
            ecnf.printStackTrace();
            return;
        } catch (NoSuchMethodException ensm)
        {
            ensm.printStackTrace();
            return;
        }

        mHooked = true;
    }

    private void initHook() throws NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, ClassNotFoundException, NoSuchMethodException
    {
        Class<?> atClass = Class.forName("android.app.ActivityThread");
        Method cat = atClass.getMethod("currentActivityThread");

        Object at = null;
        try
        {
            at = cat.invoke(atClass, new Object[0]);
        }
        catch (InvocationTargetException eit)
        {
            return;
        }

        if (at == null)
            return;

        Field instrField = atClass.getDeclaredField("mInstrumentation");
        instrField.setAccessible(true);
        mOldInstr = (Instrumentation) instrField.get(at);

        boolean res = mHookInstr.initOldInstr(mOldInstr);
        if (!res)
        {
            Log.e(TAG, "Error process Old Instrumentation.");
            return;
        }

        instrField.set(at, mHookInstr);

        Log.d(TAG, "Hook success!");
    }
}
