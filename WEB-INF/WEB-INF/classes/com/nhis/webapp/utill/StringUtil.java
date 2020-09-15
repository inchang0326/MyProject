package com.nhis.webapp.utill;

public class StringUtil {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	// String ��ȿ�� �˻�
	public static boolean isValid(String str) {
		if (null != str && !str.isEmpty()) {
			return true;
		}
		return false;
	}

	// ���� ��ū ����
	public static String randomToken(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}
