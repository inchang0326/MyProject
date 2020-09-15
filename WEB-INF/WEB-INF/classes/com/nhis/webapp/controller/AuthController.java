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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nhis.webapp.exception.ControllerException;
import com.nhis.webapp.exception.DaoException;
import com.nhis.webapp.common.EnumResultCode;
import com.nhis.webapp.exception.ServiceException;
import com.nhis.webapp.common.SessionCommon;
import com.nhis.webapp.service.AccInfoService;
import com.nhis.webapp.service.AuthService;
import com.nhis.webapp.utill.StringUtil;

@Controller
public class AuthController extends SessionCommon {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AccInfoService accInfoService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@RequestMapping(value = "/transferauth.do", method = RequestMethod.POST)
	public ModelAndView transferAuth(@RequestParam(value = "bkNm", required = true, defaultValue="") String reqBkNm
									, @RequestParam(value = "accNo", required = true, defaultValue="") String reqAccNo
									, HttpServletRequest req) throws Exception {
		logger.debug(this.getClass().getName().toString());
		ModelAndView mav = new ModelAndView("auth/transfer_auth_result");
		
		HashMap<String, Object> input = new HashMap<String, Object>();
		HashMap<String, Object> output = new HashMap<String, Object>();
		HashMap<String, Object> log = new HashMap<String, Object>();
		
		// 고객식별값 조회
		HttpSession session = getSession(req);
		String custId = (String) session.getAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID);
		try {
			// (2020.09.13) 수정 >> RequestParam을 통한 변수 할당
			String bkNm = reqBkNm;
			String accNo = reqAccNo;
			
			// 필수값 체크
			// (2020.09.13) 수정 >> 둘중 하나라도 유효하지 않을 때 작동해야 하므로 && 연산에서 || 연산으로 변경
			if(!StringUtil.isValid(bkNm) || !StringUtil.isValid(accNo)) { 
				logger.error("Invalid Parameters");
				throw new ControllerException(EnumResultCode.E0006.toString(), EnumResultCode.E0006.getMsg());
			}
			
			// 동시거래 검증
			input.put("custId", custId);			
			authService.detectConcurrentAuth(input); 
			
			// 계좌정보 조회
			input.put("bkNm", bkNm);
			input.put("accNo", accNo);		
			output = authService.inquiryAccountInfo(input);
			String rtCd = (String) output.get("rtCd");

			// 계좌정보가 유효한 경우
			if(EnumResultCode.E0000.equalTo(rtCd)) { 
				
				// 수신개발 AP로 1원출금을 요청했다고 가정한다.
				
				// 1원이체 요청
				String word = StringUtil.randomToken(10);
				logger.debug("Random Word >> : " + word);
				input.put("word", word);
				output = authService.requestTransferAuth(input);
				String rtCd2 = (String) output.get("rtCd");
				
				if(EnumResultCode.E0000.equalTo(rtCd2)) { // 1원이체 성공
					
					// 인증단어 저장 >> 타행이체 API(ReceivedTransferOtherBank)가 인증단어를 생성하지 않아 서버에서 생성한 토큰으로 인증단어 대체
					authService.storeRanWord(input);
					
					// 임시 계좌개설 정보 갱신 >> update 해야 하지만 이전 단계가 없는 상황이기 때문에 데이터 insert로 대체
					accInfoService.tempAccInfoProcess(input);
					
				} else { // 1원 이체 실패
					
					// 수신개발 AP로 1원출금 취소를 요청했다고 가정한다.
					
					logger.error("Transfer Auth Failed");
					throw new ControllerException(EnumResultCode.E0010.toString(), EnumResultCode.E0010.getMsg());
				}
			} else {
				logger.error("Invalid Account Information");
				throw new ControllerException(EnumResultCode.E0009.toString(), EnumResultCode.E0009.getMsg());
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
			output.put("rtCd", EnumResultCode.E0001.toString());	
			output.put("rtMsg", EnumResultCode.E0001.getMsg());	
		} finally {
			// 거래 로깅
			log.put("svcId", "transferauth");
			log.put("custId", custId);
			log.put("rtCd", (String) output.get("rtCd"));
			log.put("rtMsg", (String) output.get("rtMsg"));

			req.setAttribute("logMap", log);
		}
		
		mav.addObject("output", output);
		
		logger.debug("output >> : " + output);
		
		return mav;
	}	
}