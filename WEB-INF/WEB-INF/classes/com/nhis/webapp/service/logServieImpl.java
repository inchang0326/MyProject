package com.nhis.webapp.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.dao.LogDao;

@Service
public class logServieImpl implements LogService {

	private static final Logger logger = LoggerFactory.getLogger(logServieImpl.class);

	@Autowired
	private LogDao logDao;
	
	@Override
	public void logging(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			// (2020.09.13) 수정 >> insert 결과에 따른 예외처리 코드 추가
			ret = logDao.logging(input);
			if(ret != 1) {
				logger.debug("insert에 실패했습니다.");
			}
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}		
	}	
}
