package com.mg.others.utils;

/**
 * Created by wuqiyan on 17/6/7.
 */

public class MiiLocalStrEncrypt {
    public static String deCodeStringToString(String string, String key) {
        if(!"".equals(string)&&!"".equals(key)) {
            String deStr = "";

            try {
                deStr = Base64.decodeStringToString(string);
                deStr = EncrypAES.decrypt(deStr, key);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return deStr;
        } else {
            return string;
        }
    }
}
