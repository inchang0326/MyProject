<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>이체인증 완료</title>
</head>
<body>
<p>	결과코드 : ${output.rtCd} </p>
<p> 결과메시지 : ${output.rtMsg}</p> 
   <br/>
   <br/>
※ 주의사항
   <br/>
#1. 정확한 계좌번호를 기입했더라도 결과코드(0009), 결과메시지(Invalid Account Information) 오류가 종종 발생할 수 있습니다.
   <br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;해당 오류는 API 요청 일련번호가 중복되었을 때 발생하므로, 다시 요청해주시기 바랍니다.
   <br/>
#2. 동시거래 검증을 위해 요청과 요청 사이 5초 제한을 두고 있습니다.
   <br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;따라서 빠르게 거래를 요청할 경우, 결과코드(0007), 결과메시지(Concurrent Auth Caught) 예외가 발생할 수 있습니다.
</body>
</html>
