package com.nhis.webapp.http;

@SuppressWarnings("serial")
public class HttpException extends Exception{
	private String rtCd;
	private String rtMsg;
	
	public HttpException() {}

	public HttpException(String rtCd, String rtMsg) {
		this.rtCd = rtCd;
		this.rtMsg = rtMsg;
	}
	
	public String getRtCd() {
		return rtCd;
	}
	public String getRtMsg() {
		return rtMsg;
	}
}
