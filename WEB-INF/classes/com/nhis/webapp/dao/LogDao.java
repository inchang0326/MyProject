package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;

public interface LogDao {
	// �ŷ� �α�
	public void logging(HashMap<String, Object> input) throws DaoException;
}
