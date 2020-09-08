package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;

public interface AuthDao {
	// ��ü���� �ֱ� �Ͻ� ��ȸ
	public String getLatestTransferAuthDate(String custId) throws DaoException;
	// ��ü���� �غ�
	public void readyForTransferAuth(String custId) throws DaoException;
	// API �� ������ �ѹ� ��ȸ
	public String getSqnoOfApi() throws DaoException;
	// ��ü����
	public void transferAuth(String custId) throws DaoException;
	// �����ܾ� ����
	public void storeRanWord(HashMap<String, Object> input) throws DaoException;
}
