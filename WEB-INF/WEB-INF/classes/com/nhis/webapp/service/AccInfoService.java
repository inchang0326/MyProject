package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.exception.ServiceException;

public interface AccInfoService {
	// �ӽ� ���°��� ���� ����
	public HashMap<String, Object> tempAccInfoProcess(HashMap<String, Object> input) throws ServiceException, DaoException;
}
