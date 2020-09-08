package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ServiceException;
import com.nhis.webapp.http.HttpException;

public interface AuthService {
	// ���ðŷ� Ž��
	public HashMap<String, Object> detectConcurrentAuth(HashMap<String, Object> input) throws ServiceException, DaoException;
	// �ŷ����� ��ȸ(with NH Developers API)
	public HashMap<String, Object> inquiryAccountInfo(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException;
	// 1�� ��ü(with NH Developers API - ���߰�)
	public HashMap<String, Object> requestTransferAuth(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException;
	// �����ܾ� ����
	public HashMap<String, Object> storeRanWord(HashMap<String, Object> input) throws ServiceException, DaoException;
}
