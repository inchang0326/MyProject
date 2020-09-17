package com.nhis.webapp.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;

@Repository
public class AuthDaoImpl implements AuthDao {

	private static final Logger logger = LoggerFactory.getLogger(AuthDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;

	@Override
	public String getLatestTransferAuthDate(String custId) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		String date = "";
		try {
			date = sqlSession.selectOne("auth.getLatestTransferAuthDate", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}	
		return date;
	}

	@Override
	public int readyForTransferAuth(String custId) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			ret = sqlSession.update("auth.readyForTransferAuth", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}
		return ret;
	}

	@Override
	public String getSqnoOfApi() throws DaoException {
		logger.debug(this.getClass().getName().toString());
		String sqno = "";
		try {
			sqno = sqlSession.selectOne("auth.getSqnoOfApi");			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}	
		return sqno;
	}

	@Override
	public int transferAuth(String custId) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			ret = sqlSession.update("auth.transferAuth", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}
		return ret;
	}

	@Override
	public int storeRanWord(HashMap<String, Object> input) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			ret = sqlSession.update("auth.storeRanWord", input);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}
		return ret;
	}
}
