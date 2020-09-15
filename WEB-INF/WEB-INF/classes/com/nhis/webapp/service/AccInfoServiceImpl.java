package com.nhis.webapp.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.dao.AccInfoDao;

@Service
public class AccInfoServiceImpl implements AccInfoService {

	private static final Logger logger = LoggerFactory.getLogger(AccInfoServiceImpl.class);

	@Autowired
	private AccInfoDao accInfoDao;
	
	@Override
	public HashMap<String, Object> tempAccInfoProcess(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.debug(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			String custId = (String) input.get("custId");			
			
			// (2020.09.13) 수정 >> insert 결과에 따른 예외처리 코드 추가
			ret = accInfoDao.tempAccInfoProcess(custId);
			if(ret != 1) {
				logger.debug("insert에 실패했습니다.");
			}	
			retMap.put("rtCd", EnumResultCode.E0000.toString());
			retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}
		return retMap;
	}	
}
