package com.nhis.webapp.common;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// (2020.09.14) 수정 >> SessionInterceptor 클래스 추가(세션 체크 용도)
public class SessionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response
							, Object handle) throws Exception {
		logger.debug(this.getClass().getName().toString());
		logger.info("=======================Interceptor preHandle() START=======================");
		HttpSession session = request.getSession(false);
		
		// 세션 체크
		if(session == null) {
			
			logger.error("No Session");

			ModelAndView mav = new ModelAndView("auth/transfer_auth_result");
			
			HashMap<String, Object> output = new HashMap<String, Object>();
			output.put("rtCd", EnumResultCode.E0005);
			output.put("rtMsg", EnumResultCode.E0005.getMsg());
			
			mav.addObject("output", output);
			
			throw new ModelAndViewDefiningException(mav);
		} else {
			logger.info("Session is alive");
		}
		logger.info("===========================================================================");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
							Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		logger.info("=======================Interceptor postHandle() END========================");
	}
}
