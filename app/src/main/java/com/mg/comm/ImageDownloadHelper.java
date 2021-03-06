package com.mg.comm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.mg.others.http.HttpUtils;
import com.mg.others.utils.LogUtils;
import com.mg.others.utils.imager.DownloadImgUtils;
import com.mg.others.utils.imager.ImageSizeUtil;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by wuqiyan on 2017/6/8.
 */

public class ImageDownloadHelper {

    public  void downloadShowImage(final Context context, final String url, final Handler mainHandler){
        final File file = getDiskCacheDir(context, md5(url));
        if (file.exists())
        {
            LogUtils.i(MConstant.TAG,"load cache img");
            Bitmap bm;
            bm = loadImageFromLocal(file.getAbsolutePath(),context);
            if (mainHandler != null){
                Message msg = new Message();
                msg.obj = bm;
                msg.what = 300;
                mainHandler.sendMessage(msg);
            }
        }
        else {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    boolean downloadState = DownloadImgUtils.downloadImgByUrl(url, file,mainHandler);
                    if (downloadState)// 如果下载成功
                    {
                        LogUtils.i(MConstant.TAG,"img download suc");
                        Bitmap bm = loadImageFromLocal(file.getAbsolutePath(),context);
                        if (mainHandler !=null){
                            Message msg = new Message();
                            msg.obj = bm;
                            msg.what = 300;
                            mainHandler.sendMessage(msg);
                        }
                    }
                    else {
                       if (mainHandler!=null){
                           mainHandler.sendEmptyMessage(700);
                       }
                    }
                }
            };
            new HttpUtils(context).downloadAdImage(runnable);
        }

    }
    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
    public static   Bitmap loadImageFromLocal( String path,Context context)
    {
        Bitmap bm;

        bm = decodeSampledBitmapFromPath(path,context);
        return bm;
    }
    public static   Bitmap decodeSampledBitmapFromPath(String path,Context context)
    {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);


        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,context);

        // 使用获得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
    /**
     * 利用签名辅助类，将字符串字节数组
     *
     * @param str
     * @return
     */
    public static String md5(String str)
    {
        byte[] digest = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 方式二
     *
     * @param bytes
     * @return
     */
    public static String bytes2hex02(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes)
        {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString();

    }
}
