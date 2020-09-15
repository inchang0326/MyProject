package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;

public interface AuthDao {
	// ��ü���� �ֱ� �Ͻ� ��ȸ
	public String getLatestTransferAuthDate(String custId) throws DaoException;
	// ��ü���� �غ�
	public int readyForTransferAuth(String custId) throws DaoException;
	// API �� ������ �ѹ� ��ȸ
	public String getSqnoOfApi() throws DaoException;
	// ��ü����
	public int transferAuth(String custId) throws DaoException;
	// �����ܾ� ����
	public int storeRanWord(HashMap<String, Object> input) throws DaoException;
}
