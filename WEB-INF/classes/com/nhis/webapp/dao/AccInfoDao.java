package com.nhis.webapp.dao;

import com.nhis.webapp.common.DaoException;

public interface AccInfoDao {
	// 임시 계좌개설 정보 저장
	public void tempAccInfoProcess(String custId) throws DaoException;
}
