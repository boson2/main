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

	function login() {
		var form = document.getElementById("LoginForm");
		if( form["agree"].checked == true ) {
			// 로컬 스토리지에 동의내역 기록
			form.submit();
		}
		else {
			alert("동의를 하셔야 합니다");
		}
		return false;
	}

	function initTalk() {
		//로컬 스토리리지에서 동의내역을 읽고 동의한 내역이 있으면 바로 submit한다.
		var isAgree = true;
		
		document.getElementById("pn").value = "01097704868";
		document.getElementById("imei").value = "1234567890";
		
		if( isAgree == true ) {
			var form = document.getElementById("LoginForm");
			form.submit();
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
<body>
	<div id="menu_news_feed">
		<div id="menu_news_feed_wrap">
			<h3>사용동의</h3>
			<header>
				
			</header>
			<section>
				<h3>사용동의</h3>
				<ul>
					<li><a href="">개인정보를 사용해도 좋을까요 ?</a></li>
				</ul>
			</section>
			<footer>
				<form id="LoginForm" action="">
					<input type="hidden" id="pn" name="pn"/>
					<input type="hidden" id="imei" name="imei"/>
					<div class="content-make-group-btn-box">
						<input type="checkbox" value="사용동의" name="agree" id="agree"/>
						<input type="button" value="들어가기" onclick="javascript:login();"/>						
					</div>
				</form>
			</footer>
		</div>
	</div>
</body>
</html>
