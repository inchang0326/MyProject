package com.nhis.webapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.dao.AuthDao;
import com.nhis.webapp.exception.HttpException;
import com.nhis.webapp.http.HttpRequest;
import com.nhis.webapp.utill.StringUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private AuthDao authDao;
	
	@Override
	public HashMap<String, Object> detectConcurrentAuth(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.debug(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		try {
			String custId = (String) input.get("custId");
			
			// Date Ŭ������ ���� �����Ͻ� ��ȸ
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
			Date nowDate = new Date();

			// ���ðŷ� ������ ���� �ֱ� ��ü�����Ͻ� ��ȸ
			Date latestAuthDate = null;
			
			// (2020.09.13) ���� >> �ֱ���ü�����Ͻ� 1ȸ ��ȸ �� ������ ���������ν� ���ʿ��� DB Call ����
			String tmpLatestAuthDate = authDao.getLatestTransferAuthDate(custId);
			if(StringUtil.isValid(tmpLatestAuthDate)) {
				latestAuthDate = sdf.parse(tmpLatestAuthDate);
			}
			
			if(latestAuthDate != null) { // ���ðŷ� Ž�� 
				logger.debug("AuthConcurrency >>> " + ((nowDate.getTime() - latestAuthDate.getTime()) / 1000) + "�� ����");				

				// (2020.09.13) ���� >> [latestAuthDate == null] ������ �ٱ� if�� else���� ó�����ֱ� ������ ����� ���� �����Ƿ� ����
				if(((nowDate.getTime() - latestAuthDate.getTime()) / 1000) > 5 ) { // �����Ͻ� - �ֱ� ��ü�����Ͻ� > 5���� ��� ���ðŷ�����
					
					// ��ü���� �غ�
					// (2020.09.13) ���� >> insert ����� ���� ����ó�� �ڵ� �߰�
					int ret = authDao.readyForTransferAuth(custId);
					if(ret != 1) {
						logger.debug( "insert�� �����߽��ϴ�." );
					}
					
				} else {
					logger.error("Concurrent Auth Caught");
					throw new ServiceException(EnumResultCode.E0007.toString(), EnumResultCode.E0007.getMsg());
				}
			} else { // ���ðŷ��� Ž�� ���� �ʾҴٸ� ��ü���� �غ�
				authDao.readyForTransferAuth(custId);				
			}
			retMap.put("rtCd", EnumResultCode.E0000.toString());
			retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch(ServiceException e) { // (2020.09.13) ���� >> ������ throw�� ServiceException�� ����ڵ�� �޽����� throw�ϱ� ���� ServiceException�� catch�ϴ� �ڵ� �߰�
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}
		
		return retMap;
	}

	@Override
	public HashMap<String, Object> inquiryAccountInfo(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException {
		logger.debug(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		HttpRequest http = new HttpRequest();
		
		// ��ü������ ���� NH Developers API(�׽�Ʈ��) Ȱ��
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// INPUT ������ ����
			String url = "https://developers.nonghyup.com/InquireDepositorOtherBank.nh";
			input.put("apiNm", "InquireDepositorOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "DrawingTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());
			
			// ������ �ۼ���
			String rtCd = http.sendPost(url, input);
			
			if(EnumResultCode.E00000.equalTo(rtCd)) {
				retMap.put("rtCd", EnumResultCode.E0000.toString());
				retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
			}
		} catch (HttpException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}
		
		return retMap;
	}

	@Override
	public HashMap<String, Object> requestTransferAuth(HashMap<String, Object> input) throws ServiceException, DaoException, HttpException {
		logger.debug(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		HttpRequest http = new HttpRequest();
		
		// ��ü������ ���� NH Developers API(�׽�Ʈ��) Ȱ��
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// INPUT ������ ����
			String url = "https://developers.nonghyup.com/ReceivedTransferOtherBank.nh";
			input.put("apiNm", "ReceivedTransferOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "ReceivedTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());
			input.put("dractOtlt" , input.get("word").toString());
			input.put("mractOtlt" , input.get("word").toString());
			
			// ������ �ۼ���
			String rtCd = http.sendPost(url, input);			
			if(EnumResultCode.E00000.equalTo(rtCd)) {
				
				String custId = input.get("custId").toString();

				// ��ü���� ���� �� �������� ���				
				// (2020.09.13) ���� >> update ����� ���� ����ó�� �ڵ� �߰�
				int ret = authDao.transferAuth(custId);
				if(ret != 1) {
					logger.debug( "update�� �����߽��ϴ�." );
				}
				
				retMap.put("rtCd", EnumResultCode.E0000.toString());
				retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
			}
		} catch(HttpException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}
		
		return retMap;	
	}

	@Override
	public HashMap<String, Object> storeRanWord(HashMap<String, Object> input) throws ServiceException, DaoException {
		logger.debug(this.getClass().getName().toString());
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		try {
			// (2020.09.13) ���� >> insert ����� ���� ����ó�� �ڵ� �߰�
			int ret = authDao.storeRanWord(input);
			if(ret != 1) {
				logger.debug("insert�� �����߽��ϴ�.");
			}
			
			retMap.put("rtCd", EnumResultCode.E0000.toString());
			retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(EnumResultCode.E0003.toString(), EnumResultCode.E0003.getMsg());
		}

		return retMap;
	}
}