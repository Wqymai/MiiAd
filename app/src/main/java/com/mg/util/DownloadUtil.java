package com.mg.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.mg.demo.Constants;
import com.mg.others.utils.imager.DownloadImgUtils;
import com.mg.others.utils.imager.ImageSizeUtil;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wuqiyan on 2017/6/8.
 */

public class DownloadUtil {



    public static void downloadShowImage(Context context, final String url, final ImageView imageView, final Handler mainHandler){
        Log.i(Constants.TAG,"url="+url);
        final File file = getDiskCacheDir(context, md5(url));

//        if (file.exists())// 如果在缓存文件中发现
//        {
//            Log.i(Constants.TAG,"url=1");
//            Bitmap bm = loadImageFromLocal(file.getAbsolutePath(), imageView.getWidth(),imageView.getHeight());//暂时写的
//            Message msg = new Message();
//            msg.obj = bm;
//            msg.what=300;
//            mainHandler.sendMessage(msg);
//        }
//        else {
            Log.i(Constants.TAG,"url=2");
            HandlerThread handlerThread=new HandlerThread("downHt"+System.currentTimeMillis());
            handlerThread.start();
            Handler handler=new Handler(handlerThread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    boolean downloadState = DownloadImgUtils.downloadImgByUrl(url, file);
                    if (downloadState)// 如果下载成功
                    {
                        Bitmap bm = loadImageFromLocal(file.getAbsolutePath(), imageView.getWidth(),imageView.getHeight());//暂时写的
                        Message msg = new Message();
                        msg.obj = bm;
                        msg.what=300;
                        mainHandler.sendMessage(msg);
                    }
                }
            });


//        }

    }
    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
    public static   Bitmap loadImageFromLocal( String path,int width,int height)
    {
        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
//        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        // 2、压缩图片
        bm = decodeSampledBitmapFromPath(path, width, height);
        return bm;
    }
    public static   Bitmap decodeSampledBitmapFromPath(String path, int width,
                                                 int height)
    {
        // 获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
                width, height);
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
