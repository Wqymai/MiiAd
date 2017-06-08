package com.mg.others.http;

public interface HttpDownloadListener {
	public void onDownloadStart(long fileSize);

	public void onDownloading(long downloadSize, long incrementSize,
							  float percentage);

	public void onDownloadSuccess(String key);

	public void onDownloadFailed(Exception e);
}
