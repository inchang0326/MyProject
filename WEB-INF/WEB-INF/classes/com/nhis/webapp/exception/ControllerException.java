package com.nhis.webapp.exception;

@SuppressWarnings("serial")
public class ControllerException extends Exception {
	private String rtCd;
	private String rtMsg;
	
	public ControllerException() {}

	public ControllerException(String rtCd, String rtMsg) {
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
