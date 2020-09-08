package com.nhis.webapp.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;
import com.nhis.webapp.common.ServiceException;
import com.nhis.webapp.dao.LogDao;

@Service
public class logServieImpl implements LogService {

	private static final Logger logger = LoggerFactory.getLogger(logServieImpl.class);

	@Autowired
	private LogDao logDao;
	
	@Override
	public void logging(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.info(this.getClass().getName().toString());

		try {
			logDao.logging(input);			
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}
	}	
}
