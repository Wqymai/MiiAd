package com.mg.others.utils.imager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mg.others.message.ISender;
import com.mg.others.ooa.MConstant;
import com.mg.others.utils.CommonUtils;
import com.mg.others.utils.LogUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 * @author zhy
 *
 */
public class ImageLoader
{
	private static ImageLoader mInstance;

	/**
	 * 图片缓存的核心对象
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	private static final int DEAFULT_THREAD_COUNT = 1;
	/**
	 * 队列的调度方式
	 */
	private Type mType = Type.LIFO;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	/**
	 * 后台轮询线程
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	/**
	 * UI线程中的Handler
	 */
	private Handler mUIHandler;

	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
	private Semaphore mSemaphoreThreadPool;

	private boolean isDiskCacheEnable = true;

	private static final String TAG = "ImageLoader";

	public enum Type
	{
		FIFO, LIFO;
	}

	private ImageLoader(int threadCount, Type type)
	{
		init(threadCount, type);
	}

	private ImageloadListener imageloadListener;

	private ISender mSender;

	/**
	 * 初始化
	 *
	 * @param threadCount
	 * @param type
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private void init(int threadCount, Type type)
	{
		initBackThread();

		// 获取我们应用的最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheMemory)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			}

		};

		// 创建线程池
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}

	/**
	 * 初始化后台轮询线程
	 */
	private void initBackThread()
	{
		// 后台轮询线程
		mPoolThread = new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				mPoolThreadHandler = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						// 线程池去取出一个任务进行执行
						mThreadPool.execute(getTask());
						try
						{
							mSemaphoreThreadPool.acquire();
						} catch (InterruptedException e)
						{
						}
					}
				};
				// 释放一个信号量
				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			};
		};

		mPoolThread.start();
	}

	public static ImageLoader getInstance()
	{
		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	public static ImageLoader getInstance(int threadCount, Type type)
	{
		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}


	public void loadImageviewBitmap(String path, ImageView imageView, boolean isFromNet, ISender mSender){
		this.mSender = mSender;
//		Bitmap bm = getBitmapFromLruCache(path);
        Bitmap bm=null;
		if (bm != null)
		{
			LogUtils.i(MConstant.TAG,"bm 不为空");
			refreashBitmap(path, imageView, bm);
		} else
		{
			LogUtils.i(MConstant.TAG,"bm 为空");
			addTask(buildTask(path, imageView, isFromNet));
		}

//		addTask(buildTask(path, imageView, isFromNet));
	}

	/**
	 * 根据path为imageview设置图片
	 *
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView,
						  final boolean isFromNet, ImageloadListener loadListener)
	{
		this.imageloadListener = loadListener;
		imageView.setTag(path);
		if (mUIHandler == null)
		{
			mUIHandler = new Handler()
			{
				public void handleMessage(Message msg)
				{
					// 获取得到图片，为imageview回调设置图片
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageview = holder.imageView;
					String path = holder.path;
					// 将path与getTag存储路径进行比较
					if (imageview.getTag().toString().equals(path))
					{
						int maxHeight = dip2px(imageView, CommonUtils.getScreenH(imageview.getContext()));

						Drawable drawable = new BitmapDrawable(imageview.getContext().getResources(),bm);

						int height = (int) ((float) imageview.getWidth()/drawable.getMinimumWidth() * drawable.getMinimumHeight());
						if (height > maxHeight) height = maxHeight;
						imageview.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
						imageview.setImageBitmap(bm);
						if (imageloadListener != null){
							imageloadListener.onloadSuc();
						}
					}
				}
			};
		}

		// 根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(path);

		if (bm != null)
		{
			refreashBitmap(path, imageView, bm);
		} else
		{
//			addTask(buildTask(path, imageView, isFromNet));
		}


	}

	private int dip2px(ImageView imageView , float dpValue) {
		final float scale = imageView.getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据传入的参数，新建一个任务
	 *
	 * @param path
	 * @param imageView
	 * @param isFromNet
	 * @return
	 */
	private Runnable buildTask(final String path, final ImageView imageView,
							   final boolean isFromNet)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				Bitmap bm = null;
				if (isFromNet)
				{
					File file = getDiskCacheDir(imageView.getContext(),
							md5(path));
					if (file.exists())// 如果在缓存文件中发现
					{
						LogUtils.i(MConstant.TAG, "find :" + path );
						bm = loadImageFromLocal(file.getAbsolutePath(),
								imageView);
					} else
					{
//						file.mkdir();
						if (isDiskCacheEnable)// 检测是否开启硬盘缓存
						{
							boolean downloadState = DownloadImgUtils
									.downloadImgByUrl(path, file);
							if (downloadState)// 如果下载成功
							{
								LogUtils.i(MConstant.TAG,
										"suc :" + path
												+ "  path is "
												+ file.getAbsolutePath());
								bm = loadImageFromLocal(file.getAbsolutePath(),
										imageView);

							}
						} else
						// 直接从网络加载
						{
							LogUtils.i(MConstant.TAG, "load  :" + path + " to memory.");
							bm = DownloadImgUtils.downloadImgByUrl(path,
									imageView);
						}
					}
				} else
				{
					bm = loadImageFromLocal(path, imageView);
				}
				// 3、把图片加入到缓存
//				addBitmapToLruCache(path, bm);
				refreashBitmap(path, imageView, bm);
				mSemaphoreThreadPool.release();
			}


		};
	}

	private Bitmap loadImageFromLocal(final String path,
									  final ImageView imageView)
	{
		Bitmap bm;
		// 加载图片
		// 图片的压缩
		// 1、获得图片需要显示的大小
		ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图片
		bm = decodeSampledBitmapFromPath(path, imageSize.width,
				imageSize.height);
		return bm;
	}

	/**
	 * 从任务队列取出一个方法
	 *
	 * @return
	 */
	private Runnable getTask()
	{
		if (mType == Type.FIFO)
		{
			return mTaskQueue.removeFirst();
		} else if (mType == Type.LIFO)
		{
			return mTaskQueue.removeLast();
		}
		return null;
	}

	/**
	 * 利用签名辅助类，将字符串字节数组
	 *
	 * @param str
	 * @return
	 */
	public String md5(String str)
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
	public String bytes2hex02(byte[] bytes)
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

	private void refreashBitmap(final String path, final ImageView imageView,
								Bitmap bm)
	{
//		Message message = Message.obtain();
//		ImgBeanHolder holder = new ImgBeanHolder();
//		holder.bitmap = bm;
//		holder.path = path;
//		holder.imageView = imageView;
//		message.obj = holder;
//		mUIHandler.sendMessage(message);
		mSender.sendMessage(mSender.obtainMessage(MConstant.what.image_loaded,bm));
	}

	/**
	 * 将图片加入LruCache
	 *
	 * @param path
	 * @param bm
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	protected void addBitmapToLruCache(String path, Bitmap bm)
	{
		if (getBitmapFromLruCache(path) == null)
		{
			if (bm != null)
				mLruCache.put(path, bm);
		}
	}

	/**
	 * 根据图片需要显示的宽和高对图片进行压缩
	 *
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	protected Bitmap decodeSampledBitmapFromPath(String path, int width,
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

	private synchronized void addTask(Runnable runnable)
	{
		mTaskQueue.add(runnable);
		// if(mPoolThreadHandler==null)wait();
		try
		{
			if (mPoolThreadHandler == null)
				mSemaphorePoolThreadHandler.acquire();
		} catch (InterruptedException e)
		{
		}
		mPoolThreadHandler.sendEmptyMessage(0x110);
	}

	/**
	 * 获得缓存图片的地址
	 *
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(Context context, String uniqueName)
	{
		String cachePath;
//		if (Environment.getExternalStorageState() != null &&
//				Environment.MEDIA_MOUNTED.equals(Environment
//				.getExternalStorageState()))
//		{
//			StringBuilder sb = new StringBuilder();
//			File file = context.getExternalCacheDir();
//			if (file != null){
//				cachePath = file.getPath();
//			}else {
//				cachePath = sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/")
//						.append(context.getPackageName()).append("/cache/").toString();
//			}
////			cachePath = context.getExternalCacheDir().getPath();
//
//		} else
//		{
			cachePath = context.getCacheDir().getPath();
//		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 根据path在缓存中获取bitmap
	 *
	 * @param key
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
}
