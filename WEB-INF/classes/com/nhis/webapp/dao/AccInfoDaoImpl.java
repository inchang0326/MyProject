package com.nhis.webapp.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;

@Repository
public class AccInfoDaoImpl implements AccInfoDao {
	
	private static final Logger logger = LoggerFactory.getLogger(AccInfoDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void tempAccInfoProcess(String custId) throws DaoException {
		logger.info(this.getClass().getName().toString());

		try {
			sqlSession.insert("acc.tempAccInfoProcess", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(ResultCode.ERR_0004, ResultCode.ERR_0004_MSG);
		}
	}
}
