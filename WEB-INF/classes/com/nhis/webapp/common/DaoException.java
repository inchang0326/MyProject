package com.nhis.webapp.common;

@SuppressWarnings("serial")
public class DaoException extends Exception {
	
	private String rtCd;
	private String rtMsg;
	
	public DaoException() {}

	public DaoException(String rtCd, String rtMsg) {
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
