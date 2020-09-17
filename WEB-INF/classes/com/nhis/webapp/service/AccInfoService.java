package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.exception.ServiceException;

public interface AccInfoService {
	// 임시 계좌개설 정보 저장
	public HashMap<String, Object> tempAccInfoProcess(HashMap<String, Object> input) throws ServiceException, DaoException;
}
