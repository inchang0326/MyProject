package com.nhis.webapp.dao;

import com.nhis.webapp.exception.DaoException;

public interface AccInfoDao {
	// �ӽ� ���°��� ���� ����
	public int tempAccInfoProcess(String custId) throws DaoException;
}
