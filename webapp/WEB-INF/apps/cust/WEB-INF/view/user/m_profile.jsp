<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@page import="java.util.Map"%>
<%@page import="java.lang.StringBuffer"%>
<%
	String userIconPath = "";
	Map userInfoMap = (Map)request.getAttribute("userInfoMap");
	if( userInfoMap != null ) {
		String userId = (String)userInfoMap.get("user_id");
		if(userId != null) {
			StringBuffer buff = new StringBuffer(userId);
			for(int i=0, j=0; i<buff.length(); i+=2) {
				if( (i+2) < buff.length() && j < 3 ) {
					userIconPath += buff.substring(i, i+2) + "/";
					j++;
				}
			}
			
			userIconPath += "T050" + userId;
		}
	}
%>
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
<link rel="stylesheet" href="<%=request.getContextPath()%>/cust/css/style.css" />

<!-- script -->
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dist/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dist/modernizr.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dist/jquery.isotope.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/Launcher.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/board/javascript/board_util.js"></script>
<script>
	function save(update) {
		var form = document.getElementById("ConfirmForm");
		form["update"].value = update;
		
		form.submit();
		return false;
	}
	
	function selectBirth() {
		$('#birthChoose').live('click', function() {
			$('#birth').datebox('open');
		});
	}
	
	function uploadFile() {
		getFile();
		upload ();
	}
	function getFile() {
		LCamera.getPicture (upload, function (errMsg) {
			 alert (errMsg);
		}, {
			 quality : 50,
			 destinationType : LCamera.TYPE.FILE_URL,
			 sourceType : LCamera.PictureSourceType.SAVEDPHOTOALBUM
		});
	}
	function upload (imgURL) {
		
		alert("in upload()()() imgURL=["+imgURL+"]");
		
		if (imgURL == null || imgURL.length == 0) return;
		
		document.getElementById("profilePic").src = imgURL;
		
		var options = new FileUploadOptions();
		options.fileKey  = "file";
		options.fileName = imgURL.substr (imgURL.lastIndexOf('/') + 1);
		options.mimeType = "image/jpeg";

		var params = new Object();
		params.value1 = "key";
		params.value2 = "value";

		options.params = params;

		var ft = new FileTransfer();
		/**
		 * @file_path 업로드 파일 경로
		 * @server 서버 주소
		 * @successCallback
		 * @errorCallback
		 */
		var offset = location.href.indexOf(location.host) + location.host.length;
		var uriStr = location.href.substring(0,offset);

		var reqURI = uriStr + "<%=request.getContextPath()%>" + "/board/fileMngr?mode=mobile&cmd=upload&subId=sub21&userId=" + document.getElementById("pn").value;
		alert("reqURI=" + reqURI);
		
		ft.upload (imgURL, encodeURI (reqURI), function (rslt) {
			//alert ("rslt.responseCode=["+rslt.responseCode+"]"); 
			//alert ("rslt.bytesSent=["+rslt.bytesSent+"]");
			//alert ("rslt.response=["+rslt.response+"]");
			var data = eval("("+rslt.response+")");
			if (data.Status == "fail") {
				alert (data.Reason);
			} else {
				document.getElementById("cmntImgFileMask").value = data.FileMask;
			}
		}, function (errMsg) {
			alert ("errMsg.code=["+errMsg.code+"]");
			alert ("errMsg.source=["+errMsg.source+"]");
			alert ("errMsg.target=["+errMsg.target+"]");
		}, options);
	}
	
	function imageResize (elm,w,h) {
		if (navigator.appName.indexOf("Microsoft") == 0) {
			// IE는 이미지 로딩 속도가 느려서 time delay를 약간 주어야 안정적으로 동작한다.
			setTimeout("imageResizeOnIE('"+elm.id+"',"+w+","+h+")", 1);
			return;
		}
		if (elm.width > w || elm.height > h) {
			var scaleX = w / elm.width;
			var scaleY = h / elm.height;
			var scale = scaleX;
			if (scaleX > scaleY) scale = scaleY;
			
			var x = Math.ceil(elm.width  * scale);
			var y = Math.ceil(elm.height  * scale)
			elm.width  = x; // Math.ceil()의 결과값을 바로 할당하면 에러난다!
			elm.height = y; // Math.ceil()의 결과값을 바로 할당하면 에러난다!
		}
	}
	function imageResizeOnIE (elmId,w,h) {
		var elm = document.getElementById(elmId);
		if (elm.width > w || elm.height > h) {
			var scaleX = w / elm.width;
			var scaleY = h / elm.height;
			var scale = scaleX;
			if (scaleX > scaleY) scale = scaleY;
			//alert("width=["+elm.width+"],height=["+elm.height+"],scale=["+scale+"],x=["+Math.ceil(elm.width  * scale)+"],y=["+Math.ceil(elm.height  * scale)+"]");
			var x = Math.ceil(elm.width  * scale);
			var y = Math.ceil(elm.height  * scale)
			elm.width  = x; // Math.ceil()의 결과값을 바로 할당하면 에러난다!
			elm.height = y; // Math.ceil()의 결과값을 바로 할당하면 에러난다!
		}
	}	
</script>
<!--
<script type="text/javascript">
$('#page').live('pagecreate', function(event) {
	//$('#mydate').bind('change', function () {
	//    alert($(this).val());
	//});
	
	$('#birthChoose').live('click', function() {
		$('#birth').datebox('open');
	});
});
</script>
-->
</head> 
<body >
	<div id="menu_news_feed">
		<div id="menu_news_feed_wrap">
			<h3>뉴스 피드 메뉴</h3>
			<header>
				<dl>
					<dt>
						<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />
					</dt>
					<dd class="user_name">김주영</dd>
					<dd>
						<a href="">- 담벼락</a>
					</dd>
				</dl>
			</header>
			<section>
				<h3>오늘의 말씀</h3>
				<ul>
					<li><a href="">각종공기</a></li>
					<li><a href="">교회소식</a></li>
					<li><a href="">우리들이야기</a></li>
					<li><a href="">새가족소개</a></li>
					<li><a href="">오늘의 QT</a></li>
					<li><a href="">목장 나눔지</a></li>
				</ul>
			</section>
			<section>
				<h3>
					소그룹 <a href="">전체보기</a>
				</h3>
				<ul>
					<li><a href="">- 목장이름</a></li>
					<li><a href="">가입그룹명 1</a></li>
					<li><a href="">가입그룹명 2</a></li>
				</ul>
			</section>
			<section>
				<ul>
					<li><a href="">이벤트</a></li>
					<li><a href="">채팅</a></li>
					<li><a href="">요람</a></li>
				</ul>
			</section>
		</div>
	</div>
	<div id="page">
		<!-- header -->
		<header>
			<div id="btn_menu">메뉴 버튼</div>
			<ul id="btn_smart_setting">
				<li id="btn_smart">스마트 버튼</li>
			</ul>
			<div id="notify">
				<i class="top-arrow"></i>
				<h3>알림</h3>
				<ul>
					<li>
						<div class="user-photo">
							<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />	
						</div>
						<div class="user-contents">
							<p>홍길동 님이 새로운 사진을 추가하였습니다.</p>
							<p>2012년 10월 11일 오전 10:30</p>
						</div>
					</li>
					<li>
						<div class="user-photo">
							<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />	
						</div>
						<div class="user-contents">
							<p>홍길동 님이 새로운 사진을 추가하였습니다.</p>
							<p>2012년 10월 11일 오전 10:30</p>
						</div>
					</li>
					<li>
						<div class="user-photo">
							<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />	
						</div>
						<div class="user-contents">
							<p>홍길동 님이 새로운 사진을 추가하였습니다.</p>
							<p>2012년 10월 11일 오전 10:30</p>
						</div>
					</li>
					<li>
						<div class="user-photo">
							<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />	
						</div>
						<div class="user-contents">
							<p>홍길동 님이 새로운 사진을 추가하였습니다.</p>
							<p>2012년 10월 11일 오전 10:30</p>
						</div>
					</li>
					<li>
						<div class="user-photo">
							<img src="<%=request.getContextPath()%>/cust/images/ic_default_user.jpg" alt="" />	
						</div>
					<div class="user-contents">
							<p>홍길동 님이 새로운 사진을 추가하였습니다.</p>
							<p>2012년 10월 11일 오전 10:30</p>
						</div>
					</li>
				</ul>
			</div>
		</header>
		<style type="text/css">
			#content-make-profile.write-box dt{
				width:23%;
			}
			#content-make-profile.write-box dd{
				width:60%;
			}
		</style>
		<div id="contents">
			<section id="wr_title">
				<p class="bold title">
					성도님의 프로필을 작성하세요
				</p>
			</section>
			<section id="content-make-profile" class="write-box">
				<form id="ConfirmForm" method="post" action="/cust/m_index.page">
					<input type="hidden" id="update" name="update" value="true"/>
					<input type="hidden" id="pn" name="pn" value="<c:out value="${userInfoMap.user_id}"/>" />
					<input type="hidden" id="imei" name="imei"/>
					<dl>
						<dt>프로필 사진</dt>
						<dd class="user-photo">
							<img id="profilePic" src="<%=request.getContextPath()%>/board/upload/user/<%=userIconPath%>" alt="" onload="imageResize(this,50,50)"/>
							<span class="btn-user-set"><a href="javascript:uploadFile()">사진선택</a></span>
						</dd>
						<dt>이름</dt>
						<dd><input type="text" name="nm_kor" value="<c:out value="${userInfoMap.nm_kor}"/>"/></dd>
						<dt>성별</dt>
						<dd>
							<input type="radio" name="sex_flag" value="1" <c:if test="${userInfoMap.sex_flag == 1}">checked</c:if> /> 남자
							<input type="radio" name="sex_flag" value="2" <c:if test="${userInfoMap.sex_flag == 2}">checked</c:if> /> 여자
						</dd>
						<dt>생년월일</dt>
						<dd class="birth">
							<input type="hidden" name="birth_ymd" id="birth" value="<c:out value="${userInfoMap.birth_ymd}"/>">
							<!--input name="birth_ymd" id="birth" value="<c:out value="${userInfoMap.birth_ymd}"/>" type="date" data-role="datebox" data-options='{"mode": "flipbox"}'-->
						</dd>
						<dt>직분</dt>
						<dd>
							<select id="authrity" name="authrity" data-mini="true">
								<c:forEach items="${authList}" var="auth">
								<option value="<c:out value="${auth.code}"/>"><c:out value="${auth.codeName}"/></option>
								</c:forEach>
							</select>
						</dd>
						<dt>주소</dt>
						<dd>
							<input type="text" name="home_addr1" placeholder="기본주소" value="<c:out value="${userInfoMap.home_addr1}"/>" />
						</dd>
						<dt></dt>
						<dd class="address2">동과 번지를 정확하게 작성하세요</dd>
						<dt></dt>
						<dd >
							<input type="text" name="home_addr2" placeholder="상세주소" value="<c:out value="${userInfoMap.home_addr2}"/>" />
						</dd>
						<dt>자기소개</dt>
						<dd >
							<textarea name="intro" id="intro" cols="30" rows="8"><c:out value="${userInfoMap.intro}"/></textarea>
						</dd>
						<dt>공개/비공개</dt>
						<dd>
							<input type="radio" name="open_flag" value="1" <c:if test="${userInfoMap.open_flag == 1}">checked</c:if> /> 공개
							<input type="radio" name="open_flag" value="0" <c:if test="${userInfoMap.open_flag == 0}">checked</c:if> /> 비공개
						</dd>
						<dt></dt>
						<!--dd class="content-make-group-btn-box">
							<a href="javascript:save(true);" data-role="button" data-inline=true>저장하기</a>
							<a href="javascript:save(false);" data-role="button" data-inline=true>나중에 하기</a>
						</dd-->
					</dl>
				<footer>
					<div class="content-make-group-btn-box">
						<input type="button" value="저장하기" id="btn-make-group-submit" onclick="javascript:save(true);"/>
						<input type="button" value="나중에 하기" id="btn-make-group-cancel" onclick="javascript:save(false);"/>						
					</div>
				</footer>
				</form>
			</section>
		</div>
	</div>
</body>
</html>
