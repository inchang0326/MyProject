package com.nhis.webapp.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nhis.webapp.common.ControllerException;
import com.nhis.webapp.common.DaoException;
import com.nhis.webapp.common.ResultCode;
import com.nhis.webapp.common.ServiceException;
import com.nhis.webapp.common.SessionCommon;
import com.nhis.webapp.service.AccInfoService;
import com.nhis.webapp.service.AuthService;
import com.nhis.webapp.service.LogService;
import com.nhis.webapp.utill.StringUtil;

@Controller
public class AuthController extends SessionCommon {

	@Autowired
	private LogService logService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AccInfoService accInfoService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@RequestMapping(value = "/transferauth", method = RequestMethod.POST)
	public ModelAndView transferAuth(HttpServletRequest req) throws Exception {
		logger.info(this.getClass().getName().toString());
		ModelAndView mav = new ModelAndView("auth/transfer_auth_result");
		
		HashMap<String, Object> input = new HashMap<String, Object>();
		HashMap<String, Object> output = new HashMap<String, Object>();
		HashMap<String, Object> log = new HashMap<String, Object>();
		
		HttpSession session = getSession(req);
		String custId = "";
		try {
			// 세션 체크
			if(session == null) { 
				logger.error("No Session");
				throw new ControllerException(ResultCode.ERR_0005, ResultCode.ERR_0006_MSG);
			}
			// 고객식별값 조회
			custId = String.valueOf(session.getAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID));
			String bkNm = req.getParameter("bkNm");
			String accNo = req.getParameter("accNo");
			
			// 필수값 체크
			if(!StringUtil.isValid(bkNm) && !StringUtil.isValid(accNo)) { 
				logger.error("Invalid Parameters");
				throw new ControllerException(ResultCode.ERR_0006, ResultCode.ERR_0006_MSG);
			}
			
			input.put("custId", custId);
			
			// 동시거래 검증
			authService.detectConcurrentAuth(input); 
			
			// 계좌정보 조회
			input.put("bkNm", bkNm);
			input.put("accNo", accNo);		
			output = authService.inquiryAccountInfo(input);
			String rtCd = (String) output.get("rtCd");

			// 계좌정보가 유효한 경우
			if(StringUtil.isValid(rtCd)) { 
				// 수신개발 AP로 1원 출금 요청을 했다고 가정. 
				
				// 1원 이체 요청
				String word = StringUtil.randomToken(10);
				input.put("word", word);
				logger.info("Get Word >> : " + word);
				output = authService.requestTransferAuth(input);
				String rtCd2 = (String) output.get("rtCd");
				if(StringUtil.isValid(rtCd2)) { // 1원 이체 성공
					// 인증단어 저장 >> 타행이체 API(ReceivedTransferOtherBank)가 인증단어를 생성하지 않아 서버에서 생성한 토큰으로 인증단어 대체
					authService.storeRanWord(input);
					
					// 임시 계좌개설 정보 갱신 >> update 해야 하지만 이전 단계가 없는 상황이기 때문에 데이터 insert로 대체
					accInfoService.tempAccInfoProcess(input);
				} else { // 1원 이체 실패
					// 수신개발 AP로 1원 출금 요청을 취소했다고 가정.
					logger.error("Transfer Auth Failed");
					throw new ControllerException(ResultCode.ERR_0010, ResultCode.ERR_0010_MSG);
				}
			} else {
				logger.error("Invalid Account Information");
				throw new ControllerException(ResultCode.ERR_0009, ResultCode.ERR_0009_MSG);
			}
		} catch(DaoException e) {
			output.put("rtCd", e.getRtCd());	
			output.put("rtMsg", e.getRtMsg());	
		} catch(ServiceException e) {
			output.put("rtCd", e.getRtCd());	
			output.put("rtMsg", e.getRtMsg());	
		} catch (ControllerException e) {
			output.put("rtCd", e.getRtCd());	
			output.put("rtMsg", e.getRtMsg());	
		} catch (Exception e) {
			logger.error(e.getMessage());
			output.put("rtCd", ResultCode.ERR_0001);	
			output.put("rtMsg", ResultCode.ERR_0001_MSG);	
		} finally {
			
			// 거래 로깅
			log.put("svcId", "transferauth");
			log.put("custId", custId);
			log.put("rtCd", String.valueOf(output.get("rtCd")));
			log.put("rtMsg", String.valueOf(output.get("rtMsg")));
			logService.logging(log);
		}
		
		mav.addObject("output", output);
		
		logger.info("output >> : " + output);
		
		return mav;
	}	
}