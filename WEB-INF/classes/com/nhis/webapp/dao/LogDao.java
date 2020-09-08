package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;

public interface LogDao {
	// °Å·¡ ·Î±ë
	public void logging(HashMap<String, Object> input) throws DaoException;
}
