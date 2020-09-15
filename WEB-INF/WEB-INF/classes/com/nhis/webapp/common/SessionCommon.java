package com.nhis.webapp.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionCommon {
	protected static final String SESSION_ATTRIBUTE_CUSOMTER_ID = "CUSTOMER_ID"; // 绊按侥喊蔼
	
	// 技记 积己
	public HttpSession newSession(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		return session;
	}
	
	// 扁粮 技记
	public HttpSession getSession(HttpServletRequest req) {
		return req.getSession(false);
	}
}
