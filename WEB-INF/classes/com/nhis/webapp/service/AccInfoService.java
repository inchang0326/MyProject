package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ServiceException;

public interface AccInfoService {
	// �ӽ� ���°��� ���� ����
	public HashMap<String, Object> tempAccInfoProcess(HashMap<String, Object> input) throws ServiceException, DaoException;
}
