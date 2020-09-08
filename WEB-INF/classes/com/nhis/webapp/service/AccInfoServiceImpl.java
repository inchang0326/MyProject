package com.nhis.webapp.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;
import com.nhis.webapp.common.ServiceException;
import com.nhis.webapp.dao.AccInfoDao;

@Service
public class AccInfoServiceImpl implements AccInfoService {

	private static final Logger logger = LoggerFactory.getLogger(AccInfoServiceImpl.class);

	@Autowired
	private AccInfoDao accInfoDao;
	
	@Override
	public HashMap<String, Object> tempAccInfoProcess(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.info(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		
		try {
			String custId = (String) input.get("custId");			
			accInfoDao.tempAccInfoProcess(custId);
			
			retMap.put("rtCd", ResultCode.ERR_0000);
			retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}
		
		return retMap;
	}	
}
