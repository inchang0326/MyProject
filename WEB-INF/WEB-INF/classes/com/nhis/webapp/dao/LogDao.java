package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;

public interface LogDao {
	// �ŷ� �α�
	public int logging(HashMap<String, Object> input) throws DaoException;
}
