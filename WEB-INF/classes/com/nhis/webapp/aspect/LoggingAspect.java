package com.nhis.webapp.aspect;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.service.LogService;

//(2020.09.15) 수정 >> LogginAspect 클래스 추가(거래 로깅 용도)
@Component
@Aspect
public class LoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Autowired
	private LogService logService;
	
	@Pointcut("execution(* com.nhis.webapp.controller.AuthController.*(..))")
	public void loggingPointcut() {}

	@SuppressWarnings("unchecked")
	@After("loggingPointcut()")
	public void logging(JoinPoint jp) throws Exception {
		logger.debug(this.getClass().getName().toString());
		logger.info("=======================LoggingAspect logging() START========================");
		
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			HashMap<String, Object> logMap = new HashMap<String, Object>();
		
			logMap = (HashMap<String, Object>) request.getAttribute("logMap");

			logService.logging(logMap);
		
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		logger.info("=======================LoggingAspect logging() END========================");
	}	
}
