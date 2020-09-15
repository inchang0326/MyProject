package com.nhis.webapp.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;

@Repository
public class AccInfoDaoImpl implements AccInfoDao {
	
	private static final Logger logger = LoggerFactory.getLogger(AccInfoDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int tempAccInfoProcess(String custId) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			ret = sqlSession.insert("acc.tempAccInfoProcess", custId);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}
		return ret;
	}
}
