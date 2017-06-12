package com.mg.others.utils.imager;

import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mg.demo.Constants;

import java.lang.reflect.Field;



/**
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 * @author zhy
 *
 */
public class ImageSizeUtil
{
	/**
	 * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
	 *
	 * @param options
	 * @return
	 */
	public static int caculateInSampleSize(Options options, int reqWidth,
										   int reqHeight)
	{
		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;

//		if (width > reqWidth || height > reqHeight)
//		{
//			int widthRadio = Math.round(width * 1.0f / reqWidth);
//			int heightRadio = Math.round(height * 1.0f / reqHeight);
//
//			inSampleSize = Math.max(widthRadio, heightRadio);
//		}
		return inSampleSize;

//		int inSampleSize = 1;
//		if(reqWidth == 0 || reqHeight == 0){
//			return inSampleSize;
//		}
//		int bitmapWidth = options.outWidth;
//		int bitmapHeight = options.outHeight;
////		Log.i(Constants.TAG,"before1 w="+reqWidth+" h="+reqHeight);
////		Log.i(Constants.TAG,"before2 w="+bitmapWidth+" h="+bitmapHeight);
//
//		//假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
//		if(bitmapWidth > reqWidth || bitmapHeight > reqHeight){
//
//
//			int widthScale = Math.round((float) bitmapWidth / (float) reqWidth);
//			int heightScale = Math.round((float) bitmapHeight / (float) reqWidth);
//
//			//为了保证图片不缩放变形，我们取宽高比例最小的那个
//			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
////			Log.i(Constants.TAG,"缩放。。。。");
//		}
//		return inSampleSize;
	}

	/**
	 * 根据ImageView获适当的压缩的宽和高
	 *
	 * @param imageView
	 * @return
	 */
	public static ImageSize getImageViewSize(ImageView imageView)
	{

		ImageSize imageSize = new ImageSize();
		DisplayMetrics displayMetrics = imageView.getContext().getResources()
				.getDisplayMetrics();


		LayoutParams lp = imageView.getLayoutParams();

		int width = imageView.getWidth();// 获取imageview的实际宽度
		if (width <= 0)
		{
			width = lp.width;// 获取imageview在layout中声明的宽度
		}
		if (width <= 0)
		{
			 //width = imageView.getMaxWidth();// 检查最大值
			width = getImageViewFieldValue(imageView, "mMaxWidth");
		}
		if (width <= 0)
		{
			width = displayMetrics.widthPixels;
		}

		int height = imageView.getHeight();// 获取imageview的实际高度
		if (height <= 0)
		{
			height = lp.height;// 获取imageview在layout中声明的宽度
		}
		if (height <= 0)
		{
			height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
		}
		if (height <= 0)
		{
			height = displayMetrics.heightPixels;
		}
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}

	public static class ImageSize
	{
		int width;
		int height;
	}

	/**
	 * 通过反射获取imageview的某个属性值
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		} catch (Exception e)
		{
		}
		return value;

	}


}
