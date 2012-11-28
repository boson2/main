<html>

<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<!--%@ taglib prefix="util" uri="/tld/utility.tld" %-->

	<head>
		<title></title>
		<meta http-equiv="Content-type" content="text/html" />  
		<meta http-equiv="Content-style-type" content="text/css" />
		<meta name="version" content="3.2.4" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
	
		<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/styles.css" type="text/css">
		
		<script language="JavaScript" src="<%=request.getContextPath()%>/javascript/common_new.js"></script>
		<script language="JavaScript">
		
			function enterLogin(event){
				if(event.keyCode == 13){
					tokLogin();
				}
			}
			function tokLogin() {
				var form = document.getElementById("LoginForm");
				var id = form["userId"].value;
				var pass = form["password"].value;
				var isSaveLoginID = form["saveLoginID"].checked;
				
				if( id==null || id.length==0 ) {
					alert( "사용자 ID를 입력하시기 바랍니다." );
					form["userId"].focus();
					return false;
				}
				else if( pass==null || pass.length==0 ) {
					alert( "비밀번호를 입력하시기 바랍니다." );
					form["password"].focus();
					return false;
				}
				form["password"].value = pass;
				
				if( isSaveLoginID == true ) {
					var today = new Date();
					var expires = new Date();
					expires.setTime(today.getTime() + 1000*60*60*24*365);
					SetCookie('TalkLoginID', id+";1", expires, '/');
				}
				else {
					//DeleteCookie('TalkLoginID', '/');
					var today = new Date();
					var expires = new Date();
					expires.setTime(today.getTime() + 1000*60*60*24*365);
					SetCookie('TalkLoginID', ";0", expires, '/');
				}

				form["username"].value = id;
				form.submit();
				return false;
			}
		
			function initTalk() {
				var errorMessage = "<c:out value="${errorMessage}"/>";
				if( errorMessage != "null" && errorMessage.length > 0 ) {
					alert( errorMessage );
				}
			
				var form = document.getElementById("LoginForm");
				if( form ) {
					var userinfo = GetCookie('TalkLoginID');
					if( userinfo ) {
						var userinfoArray = userinfo.split(";");
						//var id = GetCookie('TalkLoginID');
						//if( id ) {
						if( userinfoArray[0] ) {
							form["userId"].value = userinfoArray[0];
							//form["userId"].value = id;
							form["password"].focus();
						}
						else {
							form["userId"].focus();
						}
						
						if( userinfoArray[1] == "1" ) {
							form["saveLoginID"].checked = true;
						}
						else {
							form["saveLoginID"].checked = false;
						}
					}
				}
			}
			
			// Attach to the onload event
			if (window.attachEvent)
			{
			    window.attachEvent ( "onload", initTalk );
			}
			else if (window.addEventListener )
			{
			    window.addEventListener ( "load", initTalk, false );
			}
			else
			{
			    window.onload = initTalk;
			}
		</script>
	</head>
	<body marginwidth="0" marginheight="0" class="#PageBaseCSSClass()" style="overflow: auto;">
		<table width="100%"  height="100%"border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
			<td class="login_out1"></td>
		  </tr>
		  <tr>
			<td class="login_out">   
			
			<table width="450px"  border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
				<td rowspan="3"><img src="<%=request.getContextPath()%>/cust/images/img_login1.gif" width="407" height="350" /></td>
				<td><img src="<%=request.getContextPath()%>/cust/images/img_login2.gif" width="387" height="207" /></td>
			  </tr>
			  <tr>
				<td class="login_bg">
				  <table width="100%"  height="100%" border="0" cellspacing="0" cellpadding="0">
				  <form id="LoginForm" name="LoginForm" method="POST" style="display:inline" action="<c:out value="${loginUrl}"/>">
				  <tr>
					<td height="5" colspan="5"></td>
				  </tr>
				  <tr>
					<td width="22%" >&nbsp;</td>
					<td width="19%" class="login_t">아이디</td>
					<td width="29%"><label for="userId"></label>
						<input type="hidden" name="username" >
						<input name="userId" type="text" id="userId" value="" size="15" tabindex="1"/>
					</td>
					<td width="21%" rowspan="2">
						<input type="image" name="imageField" id="imageField" src="<%=request.getContextPath()%>/cust/images/btn_login.gif" onclick="javascript:tokLogin()"/>
					</td>
					<!--td width="9%">&nbsp;</td-->
				  </tr>
				  <tr>
					<td>&nbsp;</td>
					<td class="login_t">패스워드</td>
					<td><input name="password" type="password" id="password" value="" size="15" tabindex="2" onkeyup="enterLogin(event)"/></td>
					<td>&nbsp;</td>
				  </tr>
				  <tr>
					<td height="36">&nbsp;</td>
					<td colspan="2" class="login_tb">
						<input type="checkbox" name="saveLoginID" id="saveLoginID" /><label for="checkbox"></label>Save ID</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				  </tr>
				  </form>
				  </table>
				</td>
			  </tr>
			  <tr>
				<td height="50"><img src="<%=request.getContextPath()%>/cust/images/img_login2-03.gif" width="387" height="50" /></td>
			  </tr>
			</table>
			</td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
		  </tr>
		</table>
	</body>
</html>
