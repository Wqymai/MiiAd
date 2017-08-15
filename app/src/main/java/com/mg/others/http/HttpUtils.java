package com.mg.others.http;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mg.comm.MConstant;
import com.mg.others.manager.HttpManager;
import com.mg.others.utils.LogUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * HTTP tools for Android application
 *
 * @author hsllany
 *
 */
public class HttpUtils {
	/**
	 * Executor parameters, reference of {@code AsyncTask}
	 */
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

	private static final int GET = 0x01;
	private static final int POST = 0x02;
	private static final int DOWNLOAD_FILE = 0x05;
	private static final int UPLOAD_FILE = 0x06;

	/**
	 * for only debug
	 */
	private static final boolean DEBUG = false;
	private static final String DEBUG_TAG = "HttpUtils";

	/**
	 * Post data chars
	 */
	private static final String CRLF = "\r\n";
	private final static String BOUNDARY = "MOZZEEDN9JOFxnp3Q8_2lXBfEexEE_Rnsey";
	private final static String PREFIX = "--";
	private final static String MUTIPART_FORMDATA = "multipart/form-data";
	private final static String CHARSET = "UTF-8";
	private final static String CONTENTTYPE = "application/octet-stream";

	private static AtomicInteger mInstanceCount = new AtomicInteger(0);

	/**
	 * The executor for run http request
	 */
	protected static ExecutorService httpExecutor;

	/**
	 * global HttpParameter
	 */
	protected static HttpParameter globleParameters;

	/**
	 * default HttpParameter
	 */
	protected static final HttpParameter defaultParamter;

	private Context mContext;

	static {
		defaultParamter = new HttpParameter(5000, 5000);
		globleParameters = defaultParamter;
	}

	public HttpUtils(Context ctx) {
		if (httpExecutor == null) {
			httpExecutor = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
//			httpExecutor = Executors.newSingleThreadExecutor();
			mInstanceCount.addAndGet(1);
		}
		if (mContext == null){
			this.mContext = ctx;
		}
	}
	public void downloadAdImage(Runnable runnable){
		httpExecutor.execute(runnable);
	}
	/**
	 * update global http parameter
	 *
	 * @param parameter
	 */
	public static void updateGlobalParameter(HttpParameter parameter) {
		if (parameter != null)
			globleParameters = parameter;
		else
			throw new NullPointerException("HttpParameter can't be null.");
	}

	/**
	 * Get method using global parameter.
	 *
	 * @see #updateGlobalParameter(HttpParameter)
	 * @see #get(String, HttpListener, HttpParameter)
	 */
	public void get(String url, HttpListener l) {

		httpExecutor.execute(new HttpGet(url, l, globleParameters,mContext));
	}

	/**
	 * get method, using specific HttpParameter.
	 *
	 * @param url
	 * @param l
	 * @param parameter
	 */
	public void get(String url, HttpListener l, HttpParameter parameter) {
		httpExecutor.execute(new HttpGet(url, l, parameter,mContext));
	}

	/**
	 * Post method using specific HttpParameter
	 *
	 * @see #post(String, HttpListener, Map)
	 * @param postData
	 *            , Map<String, String> that contains name field and value field
	 *            of post data;
	 */
	public void post(String url, HttpListener l, Map<String, String> postData, HttpParameter parameter) {

		if (postData != null) {
//			StringBuilder sb = new StringBuilder();
//			Iterator<Entry<String, String>> itr = postData.entrySet().iterator();
//
//			int i = 0;
//
//			while (itr.hasNext()) {
//				Entry<String, String> entry = itr.next();
//				String key = entry.getKey();
//				String value = entry.getValue();
//				if (i == 0)
//					sb.append(key + "=" + value);
//				else
//					sb.append("&" + key + "=" + value);
//
//				i++;
//			}
			Iterator<Entry<String, String>> itr = postData.entrySet().iterator();
			JSONObject object = new JSONObject();
			try
			{
				while (itr.hasNext()) {
					Entry<String, String> entry = itr.next();
					String key = entry.getKey();
					String value = entry.getValue();
					object.put(key,value);
				}

			}catch (Exception e){
				e.printStackTrace();
			}

			httpExecutor.execute(new HttpPost(url, l,object.toString(), parameter,mContext));
		} else {
			httpExecutor.execute(new HttpPost(url, l, null, parameter,mContext));
		}
	}

	/**
	 * Post method using global http parameter
	 *
	 * @see #post(String, HttpListener, Map, HttpParameter)
	 * @see #updateGlobalParameter(HttpParameter)
	 */
	public void post(String url, HttpListener l, Map<String, String> postData) {
		this.post(url, l, postData, globleParameters);
	}

	/**
	 * Download file and store it at 'path/fileName', using specific http
	 * parameter
	 *
	 * @see #download(String, HttpDownloadListener, String, String,boolean)
	 *
	 */
	public void download(String url, HttpDownloadListener l, String path, String fileName,
						 HttpParameter parameter,boolean isSameOnce) {
		httpExecutor.execute(new HttpDownloaderTask(url, l, path, fileName, parameter, mContext,isSameOnce));
	}

	/**
	 * Download file and store it at 'path/fileName', using specific http
	 * parameter
	 *
	 * @see #download(String, HttpDownloadListener, String, String,
	 *      HttpParameter,boolean)
	 * @see #updateGlobalParameter(HttpParameter)
	 */
	public void download(String url, HttpDownloadListener l, String path, String fileName,boolean isSameOnce) {
		this.download(url, l, path, fileName, globleParameters,isSameOnce);
	}

	/**
	 * Post file and data to URL with fileList
	 *
	 * @see #upload(String, HttpUploadFileListener, Map, Map)
	 * @see #upload(String, HttpUploadFileListener, File)
	 * @see #upload(String, HttpUploadFileListener, File, HttpParameter)
	 *
	 */
	public void upload(String url, HttpUploadFileListener l, Map<String, File> fileList, Map<String, String> postData, HttpParameter parameter) {
		httpExecutor.execute(new HttpPostFile(url, fileList, postData, l, parameter));
	}

	/**
	 * Post single file to URL
	 *
	 * @see #upload(String, HttpUploadFileListener, Map, Map, HttpParameter)
	 * @see #upload(String, HttpUploadFileListener, Map, Map)
	 * @see #upload(String, HttpUploadFileListener, File)
	 */
	public void upload(String url, HttpUploadFileListener l, File file, HttpParameter parameter) {
		Map<String, File> fileList = new HashMap<String, File>();
		fileList.put("file", file);
		upload(url, l, fileList, null, parameter);
	}

	/**
	 * Post file and data to URL using global http parameter
	 *
	 * @see #upload(String, HttpUploadFileListener, Map, Map, HttpParameter)
	 * @see #upload(String, HttpUploadFileListener, File, HttpParameter)
	 * @see #upload(String, HttpUploadFileListener, File)
	 */
	public void upload(String url, HttpUploadFileListener l, Map<String, File> fileList, Map<String, String> postData) {
		this.upload(url, l, fileList, postData, globleParameters);
	}

	/**
	 * Post single file to URL using global http parameter
	 *
	 * @see #upload(String, HttpUploadFileListener, File, HttpParameter)
	 * @see #upload(String, HttpUploadFileListener, Map, Map)
	 * @see #upload(String, HttpUploadFileListener, Map, Map, HttpParameter)
	 */
	public void upload(String url, HttpUploadFileListener l, File file) {
		this.upload(url, l, file, globleParameters);
	}

	/**
	 * If you met any situation that should use full compacity of CPU, you
	 * should {@code shutdown()}
	 */
	private static void release() {
		if (mInstanceCount.decrementAndGet() == 0) {
			httpExecutor.shutdown();
			httpExecutor = null;
		}
	}

	/**
	 * Release all resources.
	 */
	public void shutdown() {
		release();
	}

	private static class HttpPost implements Runnable {
		public HttpPost(String address, HttpListener l, String postData, HttpParameter httpParameter,Context context) {
			this.mListener = l;
			this.mURL = address;
			this.postData = postData;
			this.parameter = httpParameter;
			this.context = context;
		}

		@Override
		public void run() {
			HttpResponse response = null;
			StringBuilder sb = new StringBuilder();
			HttpURLConnection urlConnection = null;
			URL url = null;
			DataOutputStream osw = null;
			InputStream in = null;
			BufferedReader br = null;
			try {
				url = new URL(mURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				setURLConnectionParameters(POST, urlConnection, this.parameter,context);

				if (postData != null && postData.length() > 512)
					urlConnection.setChunkedStreamingMode(5);
				urlConnection.connect();

				if (postData != null) {
					osw = new DataOutputStream(urlConnection.getOutputStream());
					osw.writeBytes(postData);
					osw.flush();
				}

				int status = urlConnection.getResponseCode();

				if (status >= 400)
					in = urlConnection.getErrorStream();
				else
					in = urlConnection.getInputStream();

				br = new BufferedReader(new InputStreamReader(in));

				String line = null;

				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				response = new HttpResponse();
				response.setEntity(sb.toString());
				response.setStatus(urlConnection.getResponseCode());

				mListener.onSuccess(response);
			} catch (Exception e) {
				mListener.onFail(e);
				e.printStackTrace();
			} finally {
				try {
					if (osw != null) {
						osw.close();
					}
				} catch (IOException e) {
					mListener.onFail(e);
					e.printStackTrace();
				} finally {
					try {
						if (br != null)
							br.close();
					} catch (IOException e) {
						mListener.onFail(e);
						e.printStackTrace();
					} finally {
						try {
							if (in != null)
								in.close();
						} catch (IOException e) {
							mListener.onFail(e);
							e.printStackTrace();
						} finally {
							if (urlConnection != null)
								urlConnection.disconnect();
						}
					}
				}

			}
		}

		private String mURL;
		private HttpListener mListener;
		private String postData;
		private HttpParameter parameter;
		private Context context;

	}

	/**
	 * Get
	 *
	 * @author shitupublic
	 *
	 */
	private static class HttpGet implements Runnable {

		public HttpGet(String address, HttpListener l, HttpParameter parameter, Context mContext) {

			this.mURL = address;
			this.mListener = l;
			this.parameter = parameter;
			this.mContext = mContext;
		}

		@Override
		public void run() {

			HttpResponse response = null;
			StringBuilder sb = new StringBuilder();
			HttpURLConnection urlConnection = null;
			URL url = null;
			InputStream in = null;
			BufferedReader br = null;

			try {
				url = new URL(mURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				setURLConnectionParameters(GET, urlConnection, this.parameter, mContext);

				urlConnection.connect();
				int status = urlConnection.getResponseCode();
				if (status >= 400)
					in = urlConnection.getErrorStream();
				else
					in = urlConnection.getInputStream();

				br = new BufferedReader(new InputStreamReader(in));

				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				in.close();

				response = new HttpResponse();
				response.setEntity(sb.toString());
				response.setStatus(urlConnection.getResponseCode());
				if (mListener != null)
					mListener.onSuccess(response);
			} catch (Exception e) {
				e.printStackTrace();
				if (mListener != null)
					mListener.onFail(e);
			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (in != null)
							in.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (urlConnection != null)
							urlConnection.disconnect();
					}
				}

			}
		}

		private String mURL;
		private HttpListener mListener;
		private HttpParameter parameter;
		private Context mContext;

	}

	public static boolean isDownloadIng = false;
	/**
	 * download file task
	 *
	 * @author YangTao
	 *
	 */
	private static String realKey="";

	private static int redirctCount = 0;
	private static class HttpDownloaderTask implements Runnable {

		public HttpDownloaderTask(String url, HttpDownloadListener l, String path, String fileName,
								  HttpParameter parameter, Context mContext,boolean isSameOnce) {
			mListener = l;
			key = url;
			mfileName=fileName;
			orginPath=path;
			if (! isSameOnce){
				realKey=key;
			}

			if (!path.endsWith(File.separator))
				path = path + File.separator;
			mPath = path + fileName;
			buffer = new byte[8 * 1024];
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			mFile = new File(mPath);
			this.paramter = parameter;
			this.mContext = mContext;
		}

//		static class MytmArray implements X509TrustManager {
//			public X509Certificate[] getAcceptedIssuers() {
//				// return null;
//				return new X509Certificate[] {};
//			}
//
//			@Override
//			public void checkClientTrusted(X509Certificate[] chain, String authType)
//					throws CertificateException {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void checkServerTrusted(X509Certificate[] chain, String authType)
//					throws CertificateException {
//				// TODO Auto-generated method stub
//				// System.out.println("cert: " + chain[0].toString() + ", authType: "
//				// + authType);
//			}
//		}
//
//		static TrustManager[] xtmArray = new MytmArray[] {
//				new MytmArray()
//		};
//
//		/**
//		 * 信任所有主机-对于任何证书都不做检查
//		 */
//		private static void trustAllHosts() {
//			// Create a trust manager that does not validate certificate chains
//			// Android 采用X509的证书信息机制
//			// Install the all-trusting trust manager
//			try {
//				SSLContext sc = SSLContext.getInstance("TLS");
//				sc.init(null, xtmArray, new java.security.SecureRandom());
//				HttpsURLConnection
//						.setDefaultSSLSocketFactory(sc.getSocketFactory());
//				// HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
//				// 不进行主机名确认
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				// TODO Auto-generated method stub
//				// System.out.println("Warning: URL Host: " + hostname + " vs. "
//				// + session.getPeerHost());
//				return true;
//			}
//		};

		final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		/**
		 * Trust every server - dont check for any certificate
		 */
		private static void trustAllHosts() {
			final String TAG = "trustAllHosts";
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[] {};
						}

						public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							Log.i(TAG, "checkClientTrusted");
						}

						public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							Log.i(TAG, "checkServerTrusted");
						}
					}

			};

			// Install the all-trusting trust manager
			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



		@Override
		public void run() {
			InputStream in = null;
			OutputStream out = null;
			HttpURLConnection conn = null;
			try {
				mFile.createNewFile();
				boolean isDownloadSuccess = false;
				long filesize2 = 0;
				int downloadSize = 0;
				out = new FileOutputStream(mFile);
				int whileTime = 0;
				while (!isDownloadSuccess)
				{
					whileTime++;
					HttpUtils.isDownloadIng = true;
					try {

						URL url = new URL(key);
						if(url.getProtocol().toLowerCase().contains("https")){
							LogUtils.i(MConstant.TAG,"https request...");
							trustAllHosts();
							conn = (HttpURLConnection) url.openConnection();
							((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);
						}
						else {
							conn = (HttpURLConnection) url.openConnection();
						}
						conn.setConnectTimeout(30000);
						conn.setReadTimeout(20000);
						if(downloadSize!=0)
						{
							int downloadLength = (int) Math.abs(filesize2 - downloadSize);
							conn.setRequestProperty("Range", "bytes=" + downloadSize
									+ "-" + (filesize2-1));

						}
						setURLConnectionParameters(DOWNLOAD_FILE, conn, this.paramter, mContext);
						conn.setInstanceFollowRedirects(true);
						int status = conn.getResponseCode();
						LogUtils.i(MConstant.TAG,"run status="+status);
						String realUrl=conn.getURL().toString();
						if (status==301 || status==302 || status==303 ||status==307){
							if (redirctCount < 8){
								redirctCount++;
								LogUtils.i(MConstant.TAG,"status="+status+" realUrl="+realUrl+" redirctCount="+redirctCount);
								httpExecutor.execute(new HttpDownloaderTask(realUrl, mListener, orginPath, mfileName, paramter, mContext,true));

							}
							else {
								redirctCount = 0;
							}
							return;
						}
						conn.connect();
						fileSize = conn.getHeaderFieldInt("Content-Length", -1);
						String filetype = conn.getHeaderField("Content-Type");
						if(filesize2 == 0)
							filesize2 = fileSize;

						if (mListener != null) {
							if (fileSize >= 0) {
								mListener.onDownloadStart(fileSize);
							} else {
								mListener.onDownloadStart(-1);
							}
						}
						if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_PARTIAL) {
							in = conn.getInputStream();
							for (; ; ) {
								int bytes = in.read(buffer);
								if (bytes == -1) {
									break;
								}
								downloadSize += bytes;
								out.write(buffer, 0, bytes);
								if (mListener != null) {
									mListener.onDownloading(downloadSize, bytes, (float) downloadSize / (float) filesize2);
								}
							}
							out.flush();
						}
						isDownloadSuccess = true;
						HttpUtils.isDownloadIng = false;
					}
					catch (Exception e)
					{
						e.printStackTrace();

					}finally
					{
						if(in != null) in.close();
						if(conn != null) conn.disconnect();
						if(fileSize == 0 || whileTime > 6) isDownloadSuccess = true;

					}
				}

				if (mListener != null) {
					LogUtils.i(MConstant.TAG,"download over++++++"+realKey);
					mListener.onDownloadSuccess(realKey);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (mFile != null && mFile.exists()) {
					mFile.delete();
				}
				if (mListener != null) {
					mListener.onDownloadFailed(e);
				}
			} finally {

				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
					} finally {
						if (conn != null)
							conn.disconnect();
					}
				}
			}
		}

		protected byte[] buffer;

		private String mUrl;
		private HttpDownloadListener mListener;
		private String mPath;

		private File mFile;

		private long fileSize;

		private HttpParameter paramter;
		private String key;
		private Context mContext;
		private String mfileName;
		private String orginPath;

	}




	private static class HttpPostFile implements Runnable {
		private Map<String, File> mFiles;
		private String mUrl;
		private Map<String, String> mPostdata;
		private HttpUploadFileListener mListener;
		private HttpParameter parameter;

		public HttpPostFile(String url, Map<String, File> files, Map<String, String> postData, HttpUploadFileListener listener,
							HttpParameter parameter) {
			mUrl = url;
			mFiles = files;
			mPostdata = postData;
			mListener = listener;
			this.parameter = parameter;
		}

		@Override
		public void run() {
			URL postURL;
			DataOutputStream dos = null;
			HttpResponse response = null;
			InputStream in = null;
			BufferedReader br = null;
			HttpURLConnection urlConnection = null;
			try {
				postURL = new URL(mUrl);
				urlConnection = (HttpURLConnection) postURL.openConnection();
				setURLConnectionParameters(UPLOAD_FILE, urlConnection, this.parameter,null);
				dos = new DataOutputStream(urlConnection.getOutputStream());

				String formdataNormal = buildDataformPostdata(mPostdata);
				if (formdataNormal != null) {
					dos.write(formdataNormal.getBytes());
				}

				long allFileSize = 0;

				Iterator<Entry<String, File>> itr = mFiles.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, File> entry = itr.next();
					File file = entry.getValue();
					allFileSize += file.length();
				}

				if (mListener != null) {
					mListener.onUploadStart(allFileSize);
				}

				byte[] writeBuffer = new byte[1024 * 2];

				long completeSize = 0;

				itr = mFiles.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, File> entry = itr.next();

					File file = entry.getValue();
					String key = entry.getKey();

					StringBuffer sb = new StringBuffer("");
					sb.append(PREFIX + BOUNDARY + CRLF)
							.append("Content-Disposition: form-data;" + " name=\"" + key + "\";" + "filename=\"" + file.getName() + "\"" + CRLF)
							.append("Content-Type:" + CONTENTTYPE).append(CRLF).append(CRLF);
					dos.write(sb.toString().getBytes());

					FileInputStream fis = new FileInputStream(file);

					int len = 0;
					int j = 0;
					while ((len = fis.read(writeBuffer)) != -1) {
						dos.write(writeBuffer, 0, len);
						dos.flush();
						completeSize += len;
						if (j++ % 5 == 0 && mListener != null)
							mListener.onUploading(completeSize, (float) completeSize / (float) allFileSize);
					}
					dos.write(CRLF.getBytes());

					fis.close();
				}

				dos.write((PREFIX + BOUNDARY + PREFIX + CRLF).getBytes());
				dos.flush();
				if (mListener != null)
					mListener.onUploading(completeSize, 1);
				StringBuffer sb = new StringBuffer();
				in = urlConnection.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));

				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				response = new HttpResponse();
				response.setEntity(sb.toString());
				response.setStatus(urlConnection.getResponseCode());

				mListener.onSuccess(response);

			} catch (Exception e) {
				if (mListener != null) {
					mListener.onFail(e);
				}
			} finally {
				try {
					if (dos != null)
						dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (in != null)
								in.close();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (urlConnection != null)
								urlConnection.disconnect();
						}
					}
				}
			}
		}

	}

	private static void setURLConnectionParameters(int methodHint, HttpURLConnection urlConnection,
												   HttpParameter parameter, Context mContext) {
		if (parameter.userAgent == HttpParameter.DEFAULT_AGENT){
			parameter = new HttpParameter(5000, 5000);
			if (mContext == null){
				parameter.userAgent = HttpParameter.DEFAULT_AGENT;
			}else {
				String userAgent = HttpManager.getInstance(mContext.getApplicationContext()).getUA();
				if (userAgent != null){
					parameter.userAgent = userAgent;
				}else{
					parameter.userAgent = HttpParameter.DEFAULT_AGENT;
				}
			}
		}

		urlConnection.setConnectTimeout(parameter.connectTimeOut);
		urlConnection.setReadTimeout(parameter.soTimeOut);
		urlConnection.setRequestProperty("User-Agent",  parameter.userAgent);

		switch (methodHint) {
			case GET:
				try {
					urlConnection.setRequestMethod("GET");
					urlConnection.setRequestProperty("Connection", "Keep-Alive");
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				urlConnection.setUseCaches(false);
				break;
			case POST:
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				try {
					urlConnection.setRequestMethod("POST");
					urlConnection.setRequestProperty("Connection", "Keep-Alive");
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				urlConnection.setUseCaches(false);
				break;
			case UPLOAD_FILE:
				urlConnection.setConnectTimeout(parameter.connectTimeOut);
				urlConnection.setReadTimeout(parameter.soTimeOut);

				try {
					urlConnection.setRequestMethod("POST");
				} catch (ProtocolException e) {
				}
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				urlConnection.setUseCaches(false);
				urlConnection.setChunkedStreamingMode(1024);

				urlConnection.setRequestProperty("Connection", "Keep-Alive");
				urlConnection.setRequestProperty("Charset", CHARSET);
				urlConnection.setRequestProperty("Content-Type", MUTIPART_FORMDATA + ";boundary=" + BOUNDARY);
				break;
			case DOWNLOAD_FILE:
		}
	}

	private static String urlEncording(String url) {
//		String resultUrl = url;
////		try {
//
//		int lastSlash = url.lastIndexOf("/");
//		String pureUrl = url.substring(0, lastSlash + 1);
//		String what = url.substring(lastSlash + 1);
////			String fileName = URLEncoder.encode(url.substring(lastSlash + 1), "UTF-8");
//		String fileName = Uri.encode(url.substring(lastSlash + 1), "UTF-8");
//		resultUrl = pureUrl + fileName;
//
////		}
////		catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}

		String resultUrl = url;
		try {
			int i = url.indexOf("?");
			String pureUrl = url.substring(0,i+1);
			String what = Uri.encode(url.substring(i + 1),"UTF-8");
			resultUrl = pureUrl + what;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return resultUrl;

	}

	private static String buildDataformPostdata(Map<String, String> postData) {
		if (postData != null) {
			Iterator<Entry<String, String>> itr = postData.entrySet().iterator();

			StringBuilder builder = new StringBuilder();

			while (itr.hasNext()) {
				Entry<String, String> entry = itr.next();

				builder.append(PREFIX + BOUNDARY + CRLF);
				builder.append("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + CRLF + CRLF);
				builder.append(entry.getValue() + CRLF);
			}
			return builder.toString();
		}
		return null;

	}

	public static void debug(String msg) {
		if (DEBUG) {
			Log.d(DEBUG_TAG, msg);
		}
	}
}
