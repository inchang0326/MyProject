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
			
			// Date 클래스를 통한 현재일시 조회
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
			Date nowDate = new Date();

			// 동시거래 검증을 위한 최근 이체인증일시 조회
			Date latestAuthDate = null;
			
			// (2020.09.13) 수정 >> 최근이체인증일시 1회 조회 후 변수에 저장함으로써 불필요한 DB Call 제거
			String tmpLatestAuthDate = authDao.getLatestTransferAuthDate(custId);
			if(StringUtil.isValid(tmpLatestAuthDate)) {
				latestAuthDate = sdf.parse(tmpLatestAuthDate);
			}
			
			if(latestAuthDate != null) { // 동시거래 탐지 
				logger.debug("AuthConcurrency >>> " + ((nowDate.getTime() - latestAuthDate.getTime()) / 1000) + "초 차이");				

				// (2020.09.13) 수정 >> [latestAuthDate == null] 조건은 바깥 if문 else에서 처리해주기 때문에 실행될 일이 없으므로 제거
				if(((nowDate.getTime() - latestAuthDate.getTime()) / 1000) > 5 ) { // 현재일시 - 최근 이체인증일시 > 5초일 경우 동시거래검증
					
					// 이체인증 준비
					// (2020.09.13) 수정 >> insert 결과에 따른 예외처리 코드 추가
					int ret = authDao.readyForTransferAuth(custId);
					if(ret != 1) {
						logger.debug( "insert에 실패했습니다." );
					}
					
				} else {
					logger.error("Concurrent Auth Caught");
					throw new ServiceException(EnumResultCode.E0007.toString(), EnumResultCode.E0007.getMsg());
				}
			} else { // 동시거래가 탐지 되지 않았다면 이체인증 준비
				authDao.readyForTransferAuth(custId);				
			}
			retMap.put("rtCd", EnumResultCode.E0000.toString());
			retMap.put("rtMsg", EnumResultCode.E0000.getMsg());
		} catch(DaoException e) {
			logger.error(e.getMessage());
			throw e;
		} catch(ServiceException e) { // (2020.09.13) 수정 >> 위에서 throw한 ServiceException의 결과코드와 메시지를 throw하기 위해 ServiceException을 catch하는 코드 추가
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
		
		// 이체인증을 위해 NH Developers API(테스트용) 활용
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// INPUT 데이터 설정
			String url = "https://developers.nonghyup.com/InquireDepositorOtherBank.nh";
			input.put("apiNm", "InquireDepositorOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "DrawingTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());
			
			// 데이터 송수신
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
		
		// 이체인증을 위해 NH Developers API(테스트용) 활용
		try {
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");					
			Date date = new Date();
			String nowDate = sdf.format(date);
			
			// INPUT 데이터 설정
			String url = "https://developers.nonghyup.com/ReceivedTransferOtherBank.nh";
			input.put("apiNm", "ReceivedTransferOtherBank");
			input.put("tsymd", nowDate.substring(0, 8));
			input.put("trtm", nowDate.substring(8));
			input.put("apiSvcCd", "ReceivedTransferA");
			input.put("sqnoOfApi", authDao.getSqnoOfApi());
			input.put("dractOtlt" , input.get("word").toString());
			input.put("mractOtlt" , input.get("word").toString());
			
			// 데이터 송수신
			String rtCd = http.sendPost(url, input);			
			if(EnumResultCode.E00000.equalTo(rtCd)) {
				
				String custId = input.get("custId").toString();

				// 이체인증 성공 시 성공여부 기록				
				// (2020.09.13) 수정 >> update 결과에 따른 예외처리 코드 추가
				int ret = authDao.transferAuth(custId);
				if(ret != 1) {
					logger.debug( "update에 실패했습니다." );
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
			// (2020.09.13) 수정 >> insert 결과에 따른 예외처리 코드 추가
			int ret = authDao.storeRanWord(input);
			if(ret != 1) {
				logger.debug("insert에 실패했습니다.");
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