<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<html>
<head>
<title>우편번호 검색</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/tool/searchZip.css" type="text/css">
<script language="JavaScript"
	src="<%=request.getContextPath()%>/enface/javascript/main.js"></script>
<script language="JavaScript">
	function mOver(value){
		value.style = "font-size: 13;font-family: 굴림;color: #0033AA";
	}
	
	function mOut(value){
		value.style = "font-size: 13;font-family: 굴림;color: #000000";
	}

	function selectAddress(zipCode, simpleAddress)
	{
		var openerForm = opener.document.getElementById("<c:out value="${formName}"/>");
		openerForm["homeZip1"].value = zipCode.split("-")[0];
		openerForm["homeZip2"].value = zipCode.split("-")[1];
		openerForm["homeAddr1"].value = simpleAddress;
		openerForm["homeAddr2"].focus();
		self.close();
	}
	
	function checkAddress()
	{
		var form = document.getElementById("saerchForm");
	    if( form["homeAddr1"].value == ""){
	        alert('<util:message key="pt.ev.user.label.EmptyHomeAddress"/>');
	        form["homeAddr1"].focus();
	        return false;
	    }
	}
	
	function initPage(){
		var errorMessage = "<c:out value="${errorMessage}"/>";
		if( errorMessage != "null" && errorMessage.length > 0 ) {
			alert( errorMessage );
		}
		
		var form = document.getElementById("saerchForm");
		form["homeAddr1"].value = "";
		form["homeAddr1"].focus();
	}

</script>
</head>
	<body onload="initPage()">
		<center>
		<form id="saerchForm" name="saerchForm" method="post" onsubmit="return checkAddress()">
			<table bordercolor="gray" bgcolor="#CCCCEE" border="0" width="450">
				<tr>
					<td>
					&nbsp;<label>지역명</label>&nbsp;
					<input type="text" name="homeAddr1" style="ime-mode:active"/>
					<input type="submit" name="search" value="검색"/><br>
					&nbsp;<label class="textStyle">예) 구로동, 교하읍, 장유면</label>
					</td>
				</tr>
			</table> 
		<c:if test="${empty zipCodes }" var="result" >
			<c:if test="${isFirst == false}">
				<table bordercolor="gray" bgcolor="#EEEEFF" border="0" width="450" cellpadding="0">
					<colgroup>
						<col width="450">
						<col>
					</colgroup>
					<thead>
						<tr>
						<th class="textStyle2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;'<c:out value="${dong}"/>' 검색 결과가 없습니다. 다시 입력해주세요.</th>
						</tr>
						<tr></tr>
					</thead>
				</table>
			</c:if>
		</c:if>
		<c:if test="${result == false}">
			<table bordercolor="gray" bgcolor="#EEEEFF" border="0" width="450" cellpadding="0">
				<colgroup>
					<col width="450">
					<col>
				</colgroup>
				<thead>
					<tr>
					<th class="textStyle2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;검색 결과(해당되는 주소를 선택해 주세요.)</th>
					</tr>
					<tr></tr>
				</thead>
			</table>
			<div style="overflow-x:hidden;overflow-y:auto;width:450;height:300;padding:0px;">		
			<table bordercolor="gray" bgcolor="#EEEEFF" border="0" width="450" cellpadding="0">
				<tbody>
		<c:forEach items="${zipCodes}" var="zips">
					<tr>
						<td align="left">
						<div id="zipList">
							<a href="#" onclick='selectAddress("<c:out value="${zips.zipCode}"/>", "<c:out value="${zips.simpleAddress}"/>")'>
								<c:out value="${zips.zipCode}"/>	<c:out value="${zips.address}"/>
							</a>			
						</div>			
						</td>
						<td></td>
					</tr>
		</c:forEach>
				</tbody>
			</table>
			</div>
		</c:if>	
		</form>
		</center>
	</body>
</html>
