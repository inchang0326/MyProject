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
public class LogDaoimpl implements LogDao {

	private static final Logger logger = LoggerFactory.getLogger(LogDaoimpl.class);

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int logging(HashMap<String, Object> input) throws DaoException {
		logger.debug(this.getClass().getName().toString());
		int ret = 0;
		try {
			ret = sqlSession.insert("log.logging", input);			
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new DaoException(EnumResultCode.E0004.toString(), EnumResultCode.E0004.getMsg());
		}
		return ret;
	}
}