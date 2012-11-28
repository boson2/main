<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>회원 정보 변경</title>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/user/change.css" type="text/css">
		<c:if test="${windowId == null}" var="result">
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.4.2.js"></script>
		<script type='text/javascript' src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui-1.8.2.custom.min.js"></script>
		</c:if>
		<script type="text/javascript">
			//contextPath 전역 변수 설정
			var contextPath = "<%= request.getContextPath()%>";
		</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/main.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/user/change.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/user/utility.js"></script>
	</head>
	<body onload="initPage('ChangeInfo', 'start')">
		<center>
			<br><input type="hidden" id="work" value="ChangeInfo"/>
			<div id="contents"></div>
		</center>
	</body>
</html>
