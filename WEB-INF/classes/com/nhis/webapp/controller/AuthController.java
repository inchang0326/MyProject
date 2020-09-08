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
			// ���� üũ
			if(session == null) { 
				logger.error("No Session");
				throw new ControllerException(ResultCode.ERR_0005, ResultCode.ERR_0006_MSG);
			}
			// ���ĺ��� ��ȸ
			custId = String.valueOf(session.getAttribute(SESSION_ATTRIBUTE_CUSOMTER_ID));
			String bkNm = req.getParameter("bkNm");
			String accNo = req.getParameter("accNo");
			
			// �ʼ��� üũ
			if(!StringUtil.isValid(bkNm) && !StringUtil.isValid(accNo)) { 
				logger.error("Invalid Parameters");
				throw new ControllerException(ResultCode.ERR_0006, ResultCode.ERR_0006_MSG);
			}
			
			input.put("custId", custId);
			
			// ���ðŷ� ����
			authService.detectConcurrentAuth(input); 
			
			// �������� ��ȸ
			input.put("bkNm", bkNm);
			input.put("accNo", accNo);		
			output = authService.inquiryAccountInfo(input);
			String rtCd = (String) output.get("rtCd");

			// ���������� ��ȿ�� ���
			if(StringUtil.isValid(rtCd)) { 
				// ���Ű��� AP�� 1�� ��� ��û�� �ߴٰ� ����. 
				
				// 1�� ��ü ��û
				String word = StringUtil.randomToken(10);
				input.put("word", word);
				logger.info("Get Word >> : " + word);
				output = authService.requestTransferAuth(input);
				String rtCd2 = (String) output.get("rtCd");
				if(StringUtil.isValid(rtCd2)) { // 1�� ��ü ����
					// �����ܾ� ���� >> Ÿ����ü API(ReceivedTransferOtherBank)�� �����ܾ �������� �ʾ� �������� ������ ��ū���� �����ܾ� ��ü
					authService.storeRanWord(input);
					
					// �ӽ� ���°��� ���� ���� >> update �ؾ� ������ ���� �ܰ谡 ���� ��Ȳ�̱� ������ ������ insert�� ��ü
					accInfoService.tempAccInfoProcess(input);
				} else { // 1�� ��ü ����
					// ���Ű��� AP�� 1�� ��� ��û�� ����ߴٰ� ����.
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
			
			// �ŷ� �α�
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