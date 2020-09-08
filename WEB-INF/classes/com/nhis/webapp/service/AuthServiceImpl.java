package com.nhis.webapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;
import com.nhis.webapp.common.ServiceException;
import com.nhis.webapp.dao.AuthDao;
import com.nhis.webapp.http.HttpException;
import com.nhis.webapp.http.HttpRequest;
import com.nhis.webapp.utill.StringUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");

	@Autowired
	private AuthDao authDao;
	
	@Override
	public HashMap<String, Object> detectConcurrentAuth(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.info(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		try {
			String custId = (String) input.get("custId");
			
			// Date Ŭ������ ���� �����Ͻ� ��ȸ
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
			sdf.setTimeZone(tz);
			Date nowDate = new Date();

			// ���ðŷ� ������ ���� �ֱ� ��ü�����Ͻ� ��ȸ
			Date latestAuthDate = null;
			if(StringUtil.isValid(authDao.getLatestTransferAuthDate(custId))) {
				latestAuthDate = sdf.parse(authDao.getLatestTransferAuthDate(custId));
			}
			
			if(latestAuthDate != null) {
				// ���ðŷ��� Ž�� �Ǿ��ٸ�, "�����Ͻ� - �ֱ� ��ü�����Ͻ� > 5��"
				logger.info("AuthConcurrency >>> " + ((nowDate.getTime() - latestAuthDate.getTime()) / 1000) + "�� ����");				
				if(((nowDate.getTime() - latestAuthDate.getTime()) / 1000) > 5 
						|| (latestAuthDate == null)) {
					// ��ü���� �غ�
					authDao.readyForTransferAuth(custId);
					retMap.put("rtCd", ResultCode.ERR_0000);
					retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
				} else {
					logger.error("Concurrent Auth Problem");
					throw new ServiceException(ResultCode.ERR_0007, ResultCode.ERR_0007_MSG);
				}
				// ���ðŷ� ������ ���� �ʾҴٸ� ��ü���� �غ�
			} else {
				authDao.readyForTransferAuth(custId);				
			}
			retMap.put("rtCd", ResultCode.ERR_0000);
			retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}
		
		return retMap;
	}

	@Override
	public HashMap<String, Object> inquiryAccountInfo(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException {
		logger.info(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		HttpRequest http = new HttpRequest();
		
		// ��ü������ ���� NH Developers �� REST API(���߰�) Ȱ��
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			sdf.setTimeZone(tz);
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// ��ǲ ������ �Է�
			String url = "https://developers.nonghyup.com/InquireDepositorOtherBank.nh";
			input.put("apiNm", "InquireDepositorOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "DrawingTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());

			// �۽� �� ����
			String rtCd = http.sendPost(url, input);
			
			if(ResultCode.ERR_00000.equals(rtCd)) {
				retMap.put("rtCd", ResultCode.ERR_0000);
				retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
			} else {
//				retMap.put("rtCd", rtCd);
			}
		} catch(HttpException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}
		
		return retMap;
	}

	@Override
	public HashMap<String, Object> requestTransferAuth(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException {
		logger.info(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		HttpRequest http = new HttpRequest();
		
		// ��ü������ ���� NH Developers �� REST API(���߰�) Ȱ��
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			sdf.setTimeZone(tz);
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// ��ǲ ������ �Է�
			String url = "https://developers.nonghyup.com/ReceivedTransferOtherBank.nh";
			input.put("apiNm", "ReceivedTransferOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "ReceivedTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());
			input.put("dractOtlt" , input.get("word").toString());
			input.put("mractOtlt" , input.get("word").toString());
			
			// �۽� �� ����
			String rtCd = http.sendPost(url, input);			
			if(ResultCode.ERR_00000.equals(rtCd)) {
				
				// ��ü���� ���� �� �������� ���
				String custId = input.get("custId").toString();
				authDao.transferAuth(custId);
				
				retMap.put("rtCd", ResultCode.ERR_0000);
				retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
			} else {
//				retMap.put("rtCd", rtCd);
			}
		} catch(HttpException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}
		
		return retMap;	
	}

	@Override
	public HashMap<String, Object> storeRanWord(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.info(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		try {
			authDao.storeRanWord(input);
			
			retMap.put("rtCd", ResultCode.ERR_0000);
			retMap.put("rtMsg", ResultCode.ERR_0000_MSG);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(ResultCode.ERR_0003, ResultCode.ERR_0003_MSG);
		}

		return retMap;
	}
}