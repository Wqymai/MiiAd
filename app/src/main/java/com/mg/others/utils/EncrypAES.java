package com.mg.others.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wuqiyan on 17/6/7.
 */

public class EncrypAES {
    private static final String IV = "qws871bz73msl9x8";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public EncrypAES() {
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16];

        for(int skeySpec = 0; skeySpec < arrBTmp.length && skeySpec < arrB.length; ++skeySpec) {
            arrB[skeySpec] = arrBTmp[skeySpec];
        }

        SecretKeySpec var4 = new SecretKeySpec(arrB, "AES");
        return var4;
    }

    public static String encrypt(String data, String KEY) {
        try {
            SecretKeySpec skeySpec = getKey(KEY);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("qws871bz73msl9x8".getBytes());
            cipher.init(1, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return parseByte2HexStr(encrypted);
        } catch (Exception var7) {
            return "";
        }
    }

    public static String decrypt(String data, String KEY) {
        try {
            SecretKeySpec skeySpec = getKey(KEY);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("qws871bz73msl9x8".getBytes());
            cipher.init(2, skeySpec, iv);
            byte[] encrypted1 = parseHexStr2Byte(data);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception var9) {
            return "";
        }
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if(hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if(hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for(int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte)(high * 16 + low);
            }

            return result;
        }
    }
}
