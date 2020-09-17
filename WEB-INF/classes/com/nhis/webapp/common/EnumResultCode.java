package com.nhis.webapp.common;

//(2020.09.15) 수정 >> EnumResultCode 클래스 추가(결과코드 및 메시지 가독성 향상)
public enum EnumResultCode {
	E0000 { // (공통) 시스템 오류
		@Override
		public String toString(){ return "E0000";}
		@Override
		public String getMsg(){
			return "SUCCESS";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0001 { // (공통) 시스템 오류
		@Override
		public String toString(){ return "E0001";}		
		@Override
		public String getMsg(){
			return "Exception";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0002 { // (공통) 컨트롤러 오류
		@Override
		public String toString(){ return "E0002";}		
		@Override
		public String getMsg(){
			return "Controller Exception";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0003 { // (공통) 서비스 오류
		@Override
		public String toString(){ return "E0003";}		
		@Override
		public String getMsg(){
			return "Service Exception";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0004 { // (공통) DAO 오류
		@Override
		public String toString(){ return "E0004";}		
		@Override
		public String getMsg(){
			return "Dao Exception";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0005 { // 세션 없음
		@Override
		public String toString(){ return "E0005";}		
		@Override
		public String getMsg(){
			return "No Session";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0006 { // 필수값 누락
		@Override
		public String toString(){ return "E0006";}		
		@Override
		public String getMsg(){
			return "Invalid Parameters";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0007 { // 동시거래 탐지
		@Override
		public String toString(){ return "E0007";}		
		@Override
		public String getMsg(){
			return "Concurrent Auth Caught";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0008 { // Http 통신 실패
		@Override
		public String toString(){ return "E0008";}		
		@Override
		public String getMsg(){
			return "Http TelCom Problem";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0009 { // 계좌정보 유효성
		@Override
		public String toString(){ return "E0009";}		
		@Override
		public String getMsg(){
			return "Invalid Account Information";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	}, 
	E0010 { // 이체인증
		@Override
		public String toString(){ return "E0010";}
		@Override
		public String getMsg(){
			return "Transfer Auth Failed";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	},
	E00000 { // NH Developers API(테스트용) 파싱 성공
		@Override
		public String toString(){ return "00000";}		
		@Override
		public String getMsg(){
			return "API SUCCESS";
		}
		@Override
		public boolean equalTo(String rtCd) {
			return this.toString().equals(rtCd);
		}
	};
	
	// 에러메시지
	public abstract String getMsg();
	// 결과코드 동일 여부
	public abstract boolean equalTo(String rtCd);
	// 원소 조회
	public static EnumResultCode valueOf(int n){
		return EnumResultCode.values()[n];
	}
}