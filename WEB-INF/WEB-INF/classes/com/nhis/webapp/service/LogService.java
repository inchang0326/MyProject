package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.exception.ServiceException;

public interface LogService {
	// °Å·¡ ·Î±ë
	public void logging(HashMap<String, Object> input) throws ServiceException, DaoException;
}
