package com.nhis.webapp.dao;

import java.util.HashMap;

import com.nhis.webapp.common.DaoException;

public interface AuthDao {
	// 이체인증 최근 일시 조회
	public String getLatestTransferAuthDate(String custId) throws DaoException;
	// 이체인증 준비
	public void readyForTransferAuth(String custId) throws DaoException;
	// API 용 시퀀스 넘버 조회
	public String getSqnoOfApi() throws DaoException;
	// 이체인증
	public void transferAuth(String custId) throws DaoException;
	// 인증단어 저장
	public void storeRanWord(HashMap<String, Object> input) throws DaoException;
}
