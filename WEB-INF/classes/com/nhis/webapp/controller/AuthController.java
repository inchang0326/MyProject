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
		
		// ���ĺ��� ��ȸ
		HttpSession session = getSession(req);
		String custId = (String) session.getAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID);
		try {
			// (2020.09.13) ���� >> RequestParam�� ���� ���� �Ҵ�
			String bkNm = reqBkNm;
			String accNo = reqAccNo;
			
			// �ʼ��� üũ
			// (2020.09.13) ���� >> ���� �ϳ��� ��ȿ���� ���� �� �۵��ؾ� �ϹǷ� && ���꿡�� || �������� ����
			if(!StringUtil.isValid(bkNm) || !StringUtil.isValid(accNo)) { 
				logger.error("Invalid Parameters");
				throw new ControllerException(EnumResultCode.E0006.toString(), EnumResultCode.E0006.getMsg());
			}
			
			// ���ðŷ� ����
			input.put("custId", custId);			
			authService.detectConcurrentAuth(input); 
			
			// �������� ��ȸ
			input.put("bkNm", bkNm);
			input.put("accNo", accNo);		
			output = authService.inquiryAccountInfo(input);
			String rtCd = (String) output.get("rtCd");

			// ���������� ��ȿ�� ���
			if(EnumResultCode.E0000.equalTo(rtCd)) { 
				
				// ���Ű��� AP�� 1������� ��û�ߴٰ� �����Ѵ�.
				
				// 1����ü ��û
				String word = StringUtil.randomToken(10);
				logger.debug("Random Word >> : " + word);
				input.put("word", word);
				output = authService.requestTransferAuth(input);
				String rtCd2 = (String) output.get("rtCd");
				
				if(EnumResultCode.E0000.equalTo(rtCd2)) { // 1����ü ����
					
					// �����ܾ� ���� >> Ÿ����ü API(ReceivedTransferOtherBank)�� �����ܾ �������� �ʾ� �������� ������ ��ū���� �����ܾ� ��ü
					authService.storeRanWord(input);
					
					// �ӽ� ���°��� ���� ���� >> update �ؾ� ������ ���� �ܰ谡 ���� ��Ȳ�̱� ������ ������ insert�� ��ü
					accInfoService.tempAccInfoProcess(input);
					
				} else { // 1�� ��ü ����
					
					// ���Ű��� AP�� 1����� ��Ҹ� ��û�ߴٰ� �����Ѵ�.
					
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
			// �ŷ� �α�
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