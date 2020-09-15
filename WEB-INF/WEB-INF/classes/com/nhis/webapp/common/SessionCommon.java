package com.nhis.webapp.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionCommon {
	protected static final String SESSION_ATTRIBUTE_CUSOMTER_ID = "CUSTOMER_ID"; // ���ĺ���
	
	// ���� ����
	public HttpSession newSession(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		return session;
	}
	
	// ���� ����
	public HttpSession getSession(HttpServletRequest req) {
		return req.getSession(false);
	}
}
