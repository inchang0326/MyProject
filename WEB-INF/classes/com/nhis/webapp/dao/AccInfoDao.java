package com.nhis.webapp.dao;

import com.nhis.webapp.common.DaoException;

public interface AccInfoDao {
	// �ӽ� ���°��� ���� ����
	public void tempAccInfoProcess(String custId) throws DaoException;
}
