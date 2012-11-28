<%@ page contentType="text/html; charset=utf-8"%>

<!DOCTYPE html>
<!--[if lt IE 7 ]>
    <html class="ie6 no-js oldie">
<![endif]-->
<!--[if IE 7 ]>
    <html class="ie7 no-js oldie">
<![endif]-->
<!--[if IE 8 ]>
    <html class="ie8 no-js oldie">
<![endif]-->
<!--[if IE 9 ]>
    <html class="ie9 no-js">
<![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html class="no-js" lang="ko">
<!--<![endif]-->
<head>

<!-- meta -->
<meta charset="utf-8" content="text/html">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="user-scalable=no,height=device-height,width=device-width">

<!-- css -->
<link rel="stylesheet" href="css/style.css" />

<!-- script -->
<script type="text/javascript" src="js/dist/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/dist/modernizr.js"></script>
<script type="text/javascript" src="js/dist/jquery.isotope.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script language="JavaScript">

	function confirmGuest() {
		var form = document.getElementById("ConfirmForm");
		form["confirm"].value = "guest";
		form.submit();
		return false;
	}
	
	function confirmNormal() {
		var form = document.getElementById("ConfirmForm");
		form["confirm"].value = "normal";
		form.submit();
		return false;
	}
	
	function finish() {
		alert("앱 종료");
	}

	function initTalk() {
		document.getElementById("pn").value = "01097704868";
		document.getElementById("imei").value = "1234567890";
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
<body>
	<div id="menu_news_feed">
		<div id="menu_news_feed_wrap">
			<h3>사용정보 확인</h3>
			<header>
				
			</header>
			<section>
				<h3>사용정보 확인</h3>
				<ul>
					<li>ID : <c:out value="${userInfoMap.user_id}"/></li>
					<li>이름 : <c:out value="${userInfoMap.nm_kor}"/></li>
				</ul>
			</section>
			<footer>
				<form id="ConfirmForm" action="">
					<input type="hidden" id="pn" name="pn"/>
					<input type="hidden" id="imei" name="imei"/>
					<input type="hidden" id="confirm" name="confirm"/>
					<div class="content-make-group-btn-box">
						<input type="button" value="Guest로 사용" onclick="javascript:confirmGuest();"/>
						<input type="button" value="종료하기" onclick="javascript:finish();"/>
					</div>
					<input type="button" value="들어가기" onclick="javascript:confirmNormal();"/>						
				</form>
			</footer>
		</div>
	</div>
</body>
</html>
