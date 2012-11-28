<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>우편번호 검색</title>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/tool/searchZip.css" type="text/css">
		<c:if test="${windowId == null}" var="result">
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/message/messageResource_<c:out value="${langKnd}"/>.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.4.2.js"></script>
		<script type='text/javascript' src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui-1.8.2.custom.min.js"></script>
		</c:if>
		<script type="text/javascript">
			var contextPath = "<%= request.getContextPath()%>";
		</script> 
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/main.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/user/utility.js"></script>
		<script type="text/javascript">
			var errorMessage = "<c:out value="${errorMessage}"/>";
			var warnings = {
				"homeAddr1" : {
					"required": messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress'),
					"err"     : 0
				}
			}
		
			function requestSearch(data){
				$.ajax({
					type: 'POST',
					url: contextPath + '/tool/ajaxSearchZip.face', 
					data:'m=search&homeAddr1=' + data,
					dataType: 'html',
					success: function(html, textStatus){
						initResults(html);
					},
					error: function(xhr, textStatus, errorThrown){
						alert('잠시 후에 다시 시도 해주십시오.');
					}
				});
			}
		
			function checkAddress(e){
				var keyCode = e.which;

				//keyup event
				if(keyCode == 13){
					homeAddr1 = $(this);
				}

				//click event
				else if(keyCode == 0){
					homeAddr1 = $('#homeAddr1');
				}
				else return false;
				
				if( homeAddr1.val() == ""){
					alert(messageResource.getMessage('pt.ev.user.label.EmptyHomeAddress'));
					homeAddr1.select();
					return false;
				}
				else {
					requestSearch(homeAddr1.val());
					homeAddr1.select();
					return false;
				}
			}

			function initResults(responseHTML){
				$('div#result').html(responseHTML);
			}

			function initPage(){
				$('#search').click(checkAddress);

				$('#homeAddr1').keyup(fieldIsFilled);
				$('#homeAddr1').keyup(checkAddress);
				$('#homeAddr1').blur(fieldIsFilled);
				
				$('#homeAddr1').select();
			}
		</script>
	</head>
	<body onload="initPage()">
		<center>
			<form id="saerchForm" method="post">
				<table id="searchTable">
					<tr id="searchTableRow">
						<td>
						&nbsp;<label class="normal">지역명</label>&nbsp;
						<input type="text" id="homeAddr1" class="text" name="homeAddr1" style="ime-mode:active"/>
						<input type="button" id="search" class="submit" name="search" value="검색"/>
						</td>
					</tr>
					<tr>
						<td>
						&nbsp;<input type="text" style="display: none;" /><label class="example">예) 구로동, 교하읍, 장유면</label>
						</td>
					</tr>
				</table>
				<div id="result"></div>
			</form>
		</center>
		<script type="text/javascript">
			function selectAddress(zipCode, simpleAddress){
				$('#homeZip1', opener.document).val(zipCode.split("-")[0]);
				$('#homeZip2', opener.document).val(zipCode.split("-")[1]);
				$('#homeAddr1', opener.document).val(simpleAddress);
				$('#homeAddr2', opener.document).select();
				self.close();
			}
		</script>
	</body>
</html>
