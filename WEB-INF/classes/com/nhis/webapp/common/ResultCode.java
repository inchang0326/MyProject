package com.nhis.webapp.common;

public class ResultCode {

	public static final String ERR_0000 = "0000"; // (공통) 성공	
	public static final String ERR_0001 = "0001"; // (공통) 시스템 오류
	public static final String ERR_0002 = "0002"; // (공통) 컨트롤러 오류
	public static final String ERR_0003 = "0003"; // (공통) 서비스 오류
	public static final String ERR_0004 = "0004"; // (공통) DAO 오류
	public static final String ERR_0005 = "0005"; // 세션 없음
	public static final String ERR_0006 = "0006"; // 필수값 누락
	public static final String ERR_0007 = "0007"; // 동시거래 검증
	public static final String ERR_0008 = "0008"; // Http 통신 실패
	public static final String ERR_0009 = "0009"; // 계좌정보 유효성
	public static final String ERR_0010 = "0010"; // 이체인증
	
	public static final String ERR_00000 = "00000"; // NH Developers API(개발계) 파싱 성공
	
	public static final String ERR_0000_MSG = "SUCCESS";
	public static final String ERR_0001_MSG = "Exception";
	public static final String ERR_0002_MSG = "Controller Exception";
	public static final String ERR_0003_MSG = "Service Exception";
	public static final String ERR_0004_MSG = "Dao Exception";
	public static final String ERR_0005_MSG = "No Session";
	public static final String ERR_0006_MSG = "Invalid Parameters";
	public static final String ERR_0007_MSG = "Concurrent Auth Problem";
	public static final String ERR_0008_MSG = "Http TelCom Problem";
	public static final String ERR_0009_MSG = "Invalid Account Information";
	public static final String ERR_0010_MSG = "Transfer Auth Failed";
}
