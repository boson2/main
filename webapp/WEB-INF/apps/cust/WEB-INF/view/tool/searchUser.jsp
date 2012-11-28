<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>사용자 검색</title>
		<c:if test="${windowId == null}" var="result">
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.4.2.js"></script>
		<script type='text/javascript' src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui-1.8.2.custom.min.js"></script>
		</c:if>
		<script type="text/javascript">
			var contextPath = "<%= request.getContextPath()%>";
			var errorMessage = "<c:out value="${errorMessage}"/>";
		</script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/main.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/tool/searchUser.js"></script>
		
		<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/tool/search_user.css" type="text/css">
	</head>
	<body onload="initPage()">
		<div class="user_search">
			<select id="searchType" class="normal_select">
				<option id="name_search" value="0">이름</option>
				<option id="id_search"   value="1">ID</option>
			</select>
			<input id="searchName" type="text"   class=user_id	title="이름/아이디 입력" onkeyup="userSearch()">				
			<input id="searchBtn"  type="button" class="search_button" value="검색"/>
		</div>
		<div class="user_list">
			<div class="group_list">
				<span class="list_label">그룹원 리스트</span>
				<select id="userListBox" multiple="multiple" class="list_select">
				</select>
			</div>
			<div class="select_control">
				<input type="button" class="add_button" title="받는 사람 목록으로 추가하기" onclick="addReceiver()" value="추가"/>
				<input type="button" class="del_button" title="받는 사람 목록에서 삭제하기" onclick="removeReceiver()" value="삭제"/>
			</div>
			<div class="receiver_list">
				<span class="list_label">받는 사람</span>
				<select id="receiverListBox" multiple="multiple" class="list_select">
				</select>
			</div>
		</div>
		<div class="search_control">
			<span><input type="button" class="ok_button"	 onclick="selectUser()" 			value="확인"/></span>
			<span><input type="button" class="cancel_button" onclick="javascript:self.close();" value="취소"/></span>
		</div>
	</body>
</html>
