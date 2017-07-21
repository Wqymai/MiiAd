package com.mg.others.utils.imager;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mg.others.utils.CommonUtils;

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
	public static int caculateInSampleSize(Options options, Context context)
	{
		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;
//		LogUtils.i(MConstant.TAG,"width="+width+" height="+height);

		int reqWidth= CommonUtils.getScreenW(context);
		int reqHeight=CommonUtils.getScreenH(context);

//		LogUtils.i(MConstant.TAG,"reqWidth="+reqWidth+" reqHeight="+reqHeight);

		if (width > reqWidth || height > reqHeight)
		{
//			int widthRadio = Math.round(width * 1.0f / reqWidth);
//			int heightRadio = Math.round(height * 1.0f / reqHeight);
//
//			inSampleSize = Math.max(widthRadio, heightRadio);

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
			{
				inSampleSize *= 2;
			}

		}
//		LogUtils.i(MConstant.TAG,"inSampleSize="+inSampleSize);
		return inSampleSize;
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
