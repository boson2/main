<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<html xmlns="http://www.w3.org/1999/xhtml"><head>

	<title></title>
    <meta http-equiv="Content-type" content="text/html" />  
    <meta http-equiv="Content-style-type" content="text/css" />
    <meta name="version" content="3.21" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/styles.css" type="text/css">
	<script language="JavaScript" src="<%=request.getContextPath()%>/javascript/common_new.js"></script>
    <script type="text/JavaScript">
		function changePasswd() {
			var form = document.getElementById("PasswdChanageForm");
			var password = form["password"].value;
			var passwordNew = form["passwordNew"].value;
			var passwordConfirm = form["passwordConfirm"].value;
			
			if ( password=='' ) {
				alert('비밀번호를 입력하세요.');
				form["password"].focus();
			} else if ( document.frm.passwordNew.value=='' ) {
				alert('새 비밀번호를 입력하세요.');
				form["passwordNew"].focus();
			} else if ( password==passwordNew ) {
				alert('현재 비밀번호와 같습니다.');
				form["passwordNew"].value="";
				form["passwordConfirm"].value="";
				form["passwordNew"].focus();
				form["passwordNew"].select();
			} else if ( passwordNew!=passwordConfirm ) {
				alert('새 비밀번호를 확인하세요.');
				form["passwordConfirm"].value="";
				form["passwordConfirm"].value = '';
				form["passwordConfirm"].focus();
			} else {
				form["password"].value = password; //getMovieName("top_left_bg").epPassEncode(password); 
				form["passwordNew"].value = passwordNew; //getMovieName("top_left_bg").epPassEncode(passwordNew); 
				form["passwordConfirm"].value = passwordConfirm; //getMovieName("top_left_bg").epPassEncode(passwordConfirm); 
				return true;
			}
			return false;
		}
		function reset() {
			var form = document.getElementById("PasswdChanageForm");
			form["password"].value='';
			form["passwordNew"].value='';
			form["passwordConfirm"].value='';
		}
		function setNoticeLayer(target){
			var obj = document.getElementById(target);

			var top = 31, left = 2;

			while(obj && obj.offsetParent) {
				top += obj.offsetTop;
				left += obj.offsetLeft;
				obj = obj.offsetParent;
			}

			var target= document.getElementById('help3_1');
			target.style.left = left+"px";
			target.style.top = top+"px";

			//showCapslock();
		}
		function inFocus2(i) {
			(i).style.borderColor='#59a509';
		}
		function outFocus2(i) {
			(i).style.borderColor='#cccccc'
		}
		
		function init() {
			var errorMessage = "<c:out value="${errorMessage}"/>";
			if( errorMessage != "null" && errorMessage.length > 0 ) {
				alert( errorMessage );
			}
			
			document.frm.password.focus();
		}

		// Attach to the onload event
		if (window.attachEvent)
		{
		    window.attachEvent ( "onload", init );
		}
		else if (window.addEventListener )
		{
		    window.addEventListener ( "load", init, false );
		}
		else
		{
		    window.onload = init;
		}
	</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center"> 
	<table width="800px" border="0" cellspacing="0" cellpadding="0">
		<tr>
          <td width="8"></td>
          <td >
          <!-- BEGIN : flash module for password encoding -->
	          <div>
	          <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="1" height="1" id="top_left_bg" align="middle">
              <param name="allowScriptAccess" value="always" />
              <param name="movie" value="<%=request.getContextPath()%>/cust/images/user/top_left_bg.swf" />
              <embed src="<%=request.getContextPath()%>/cust/images/user/top_left_bg.swf" width="1" height="1" name="top_left_bg" align="middle" allowScriptAccess="always" swLiveConnect="true"  type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
            </object>
            </div>	
	      <!-- END : flash module for password encoding --></td>
          <td width="8"></td>
        </tr>
	</table>
	<table width="800px" border="0" cellspacing="0" cellpadding="0">
        <tr> 
			<td>
				<h2><img src="<%=request.getContextPath()%>/cust/images/user/modify_pw.gif" alt="비밀번호 변경" height="16" width="84"></h2>
			</td>
        </tr>
		<tr> 
            <td>
				<p class="content_summary"><img src="<%=request.getContextPath()%>/cust/images/user/modify_pw_sum.gif" alt="현재 비밀번호를 입력한 후 새로 사용할 비밀번호를 입력하세요." height="12" width="286"></p>
				<br>
			</td>
        </tr>
		<tr> 
          <td>
			<form name="frm" id="PasswdChanageForm" method="post" action="<%=request.getContextPath()%>/user/changePassword.face" onsubmit="return changePasswd()" style="width: 333px; float: left; border-right: 1px solid rgb(230, 230, 230);">
			<input name="token_help" value="" type="hidden">
			<div style="border:1px solid black; padding: 10px; width:600px"> 
				<table width="100%" border="0" cellspacing="0" cellpadding="0" >
					<tr> 
						<td style="width: 15%; height: 43px;">
							<img src="<%=request.getContextPath()%>/cust/images/user/passwd_old.gif" alt="현재 비밀번호" height="13" width="73">
						</td>
						<td>
							<input name="password" id="password" maxlength="16" class="input_text" style="width: 138px; border-color: rgb(204, 204, 204);" type="password">
						</td>
						<td rowspan=4>
							<ul style="letter-spacing: -1px; margin: 8px; padding-left: 26px; text-align: left; line-height: 18px;">
								<li><strong>영문, 숫자, 특수문자를 혼용(6자 이상 16자 이하)<br>하시면 보다 안전합니다.</strong></li>
								<li>아이디와 같은 비밀번호나 주민등록번호, 생일, 학번,<br>전화번호 등 개인정보와 관련된 숫자, 연속된 숫자, 동일<br>하게 반복된 숫자 등 다른 사람이 쉽게 알아낼 수 있는<br>비밀번호는 유출 위험이 높아 사용하지 않는 것이 좋습니다.</li>
								<li>이전에 사용한 비밀번호를 재사용할 경우 도용의 우려가<br>있으니 가급적 새로운 비밀번호를 사용해 주십시오.</li>
							</ul>
						</td>
					</tr>
					<tr> 
						<td colspan=2>
							<hr>
						</td>
					</tr>
					<tr> 
						<td style="height: 43px;">
							<img src="<%=request.getContextPath()%>/cust/images/user/passwd_new1.gif" alt="새 비밀번호" height="13" width="73">
						</td>
						<td>
							<input name="passwordNew" id="passwordNew" maxlength="16" class="input_text" style="width: 138px; border-color: rgb(204, 204, 204);" type="password">
						</td>
					</tr>
					<tr> 
						<td style="height: 43px;">
							<img src="<%=request.getContextPath()%>/cust/images/user/passwd_new2.gif" alt="새 비밀번호 확인" height="13" width="73">
						</td>
						<td>
							<input name="passwordConfirm" id="passwordConfirm" maxlength="16" class="input_text" style="width: 138px; border-color: rgb(204, 204, 204);" type="password">
						</td>
					</tr>
					<tr> 
						<td colspan=3>
							<hr>
						</td>
					</tr>
				</table>
				<table width="571" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="right">
							<input name="" src="<%=request.getContextPath()%>/cust/images/user/btn_confirm.gif" alt="확인" style="vertical-align: top;" type="image">
							<button name="" type="reset" class="reset" style="vertical-align: top;" onclick="javascript:document.frm.password.focus();"><span>다시입력</span></button>
						</td>
					</tr>
				</table>
			</div>
			</form>
		  </td>
		</tr>
	</table>
	</td>
  </tr>
</table>
</body>
</html>
