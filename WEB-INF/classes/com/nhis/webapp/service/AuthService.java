package com.nhis.webapp.service;

import java.util.HashMap;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.exception.HttpException;

public interface AuthService {
	// 동시거래 탐지
	public HashMap<String, Object> detectConcurrentAuth(HashMap<String, Object> input) throws ServiceException, DaoException;
	// 계좌정보 조회(with NH Developers API)
	public HashMap<String, Object> inquiryAccountInfo(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException;
	// 1원 이체(with NH Developers API)
	public HashMap<String, Object> requestTransferAuth(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException;
	// 인증단어 저장
	public HashMap<String, Object> storeRanWord(HashMap<String, Object> input) throws ServiceException, DaoException;
}
