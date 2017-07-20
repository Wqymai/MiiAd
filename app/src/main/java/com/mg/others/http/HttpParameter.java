package com.mg.others.http;

public class HttpParameter {
	public static final String DEFAULT_AGENT = "Mozilla/5.0 (Linux; Android 7.0; EVA-AL10 Build/HUAWEIEVA-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36";

	public final int soTimeOut;
	public final int connectTimeOut;

	public String userAgent = DEFAULT_AGENT;

	public HttpParameter(int soTimeOut, int connectTimeOut) {
		if (soTimeOut < 0 || connectTimeOut < 0)
			throw new IllegalArgumentException();
		this.soTimeOut = soTimeOut;
		this.connectTimeOut = connectTimeOut;
	}
}
