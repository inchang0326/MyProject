package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;

public interface LogDao {
	// °Å·¡ ·Î±ë
	public int logging(HashMap<String, Object> input) throws DaoException;
}
