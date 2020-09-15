package com.nhis.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nhis.webapp.common.SessionCommon;

@Controller
public class HomeController extends SessionCommon {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest req) {
		logger.debug(this.getClass().getName().toString());

		ModelAndView mav = new ModelAndView("home");

		// 技记 积己
		HttpSession httpSession = newSession(req); 

		// 绊按侥喊蔼 历厘
		httpSession.setAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID, "0000000001");		
		logger.info("Welcome home! >> " + httpSession.getAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID));

		return mav;
	}
}
