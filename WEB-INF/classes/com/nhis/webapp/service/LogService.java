package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ServiceException;

public interface LogService {
	// �ŷ� �α�
	public void logging(HashMap<String, Object> input) throws ServiceException, DaoException;
}
