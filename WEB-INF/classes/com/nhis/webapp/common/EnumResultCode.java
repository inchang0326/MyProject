package com.nhis.webapp.common;

//(2020.09.15) ���� >> EnumResultCode Ŭ���� �߰�(����ڵ� �� �޽��� ������ ���)
public enum EnumResultCode {
	E0000 { // (����) �ý��� ����
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
	E0001 { // (����) �ý��� ����
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
	E0002 { // (����) ��Ʈ�ѷ� ����
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
	E0003 { // (����) ���� ����
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
	E0004 { // (����) DAO ����
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
	E0005 { // ���� ����
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
	E0006 { // �ʼ��� ����
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
	E0007 { // ���ðŷ� Ž��
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
	E0008 { // Http ��� ����
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
	E0009 { // �������� ��ȿ��
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
	E0010 { // ��ü����
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
	E00000 { // NH Developers API(�׽�Ʈ��) �Ľ� ����
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
	
	// �����޽���
	public abstract String getMsg();
	// ����ڵ� ���� ����
	public abstract boolean equalTo(String rtCd);
	// ���� ��ȸ
	public static EnumResultCode valueOf(int n){
		return EnumResultCode.values()[n];
	}
}