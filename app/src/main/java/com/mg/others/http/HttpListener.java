package com.mg.others.http;



public interface HttpListener {
	public void onSuccess(HttpResponse response);

	public void onFail(Exception e);

	public static final int HTTP_OK = 200;
}
