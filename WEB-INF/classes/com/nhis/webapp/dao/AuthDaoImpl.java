package com.nhis.webapp.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;

@Repository
public class AuthDaoImpl implements AuthDao {

	private static final Logger logger = LoggerFactory.getLogger(AuthDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;

	@Override
	public String getLatestTransferAuthDate(String custId) throws DaoException {
		logger.info(this.getClass().getName().toString());
		String date = "";
		try {
			date = sqlSession.selectOne("auth.getLatestTransferAuthDate", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}	
		return date;
	}

	@Override
	public void readyForTransferAuth(String custId) throws DaoException {
		logger.info(this.getClass().getName().toString());
		try {
			sqlSession.update("auth.readyForTransferAuth", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}
	}

	@Override
	public String getSqnoOfApi() throws DaoException {
		logger.info(this.getClass().getName().toString());
		String sqno = "";
		try {
			sqno = sqlSession.selectOne("auth.getSqnoOfApi");			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}	
		return sqno;
	}

	@Override
	public void transferAuth(String custId) throws DaoException {
		logger.info(this.getClass().getName().toString());
		try {
			sqlSession.update("auth.transferAuth", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}
	}

	@Override
	public void storeRanWord(HashMap<String, Object> input) throws DaoException {
		logger.info(this.getClass().getName().toString());
		try {
			sqlSession.update("auth.storeRanWord", input);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}
	}
}
