package com.mg.others.utils;

import java.lang.reflect.Method;


public class SystemProperties {
    static {
        try {
            initOnce();
        } catch (Exception e) {

        }
    }

    private static Class<?> clsSystemProperties;
    private static Method metGet;
    private static Method metGetWithDef;
    private static Method metGetIntWithDef;
    private static Method metGetLongWithDef;
    private static Method metGetBooleanWithDef;
    private static Method metSet;

    private static void initOnce() throws Exception {
        clsSystemProperties = Class.forName("android.os.SystemProperties");
        metGet = clsSystemProperties.getDeclaredMethod("get", String.class);
        metGet.setAccessible(true);
        metGetWithDef = clsSystemProperties.getDeclaredMethod("get", String.class, String.class);
        metGetWithDef.setAccessible(true);
        metGetIntWithDef = clsSystemProperties.getDeclaredMethod("getInt", String.class, int.class);
        metGetIntWithDef.setAccessible(true);
        metGetLongWithDef = clsSystemProperties.getDeclaredMethod("getLong", String.class, long.class);
        metGetLongWithDef.setAccessible(true);
        metGetBooleanWithDef = clsSystemProperties.getDeclaredMethod("getBoolean", String.class, boolean.class);
        metGetBooleanWithDef.setAccessible(true);
        metSet = clsSystemProperties.getDeclaredMethod("set", String.class, String.class);
        metSet.setAccessible(true);
    }

    public static String get(String key) {
        try {
            return (String)metGet.invoke(null, key);
        } catch (Exception e) {
            return null;
        }
    }

    public static String get(String key, String def) {
        try {
            return (String)metGetWithDef.invoke(null, key, def);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getInt(String key, int def) {
        try {
            return (Integer)metGetIntWithDef.invoke(null, key, def);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    public static long getLong(String key, long def) {
        try {
            return (Long)metGetLongWithDef.invoke(null, key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean)metGetBooleanWithDef.invoke(null, key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public static void set(String key, String val) {
        try {
            metSet.invoke(null, key, val);
        } catch (Exception e) {
        }
    }
}
