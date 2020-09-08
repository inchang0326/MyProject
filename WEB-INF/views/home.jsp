<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>타행계좌 확인</title>
</head>
<body>
	<form action='/transferauth' method='post'>
		<div>
			<select name="bkNm" id="selBk" style="width:200px; height:30px;">
				<option value="" selected disabled hidden>은행 선택</option>
				<option value="004">국민은행</option>
				<option value="003">기업은행</option>
				<option value="011">농협은행</option>
				<option value="088">신한은행</option>
				<option value="020">우리은행</option>
				<option value="081">하나은행</option>
			</select> 
		</div>
		<div>
			<input name="accNo" type="text" placeholder="계좌 번호" style="width:200px; height:30px;"/>
		</div>
		<button type="submit">다음</button>		
		<br/>
		<br/>
		※ 테스트 용 계좌번호를 기입해주시기 바랍니다 : 국민은행, 1000000432004
	</form>
</body>
</html>
