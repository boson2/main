
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/userpassManager.js"></script>

<div class="webformpanel" style="width:100%;">
	<form id="UserpassManager_EditForm" style="display:inline" action="" method="post">
		<table cellpadding=0 cellspacing=0 border=0 width='100%'>
		<input type="hidden" id="UserpassManager_isCreate">
		<tr> 
			<td colspan="2" width="100%" class="webformheaderline"></td>    
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 전화번호(ID) *</td>
			<td class="webformfield">
				<input type="text" id="UserpassManager_userId" name="userId" value="<c:out value="${aUserpassVO.userId}"/>" size="20" maxLength="30" label="전화번호" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 이름 *</td>
			<td class="webformfield">
				<input type="text" id="UserpassManager_nmKor" name="nmKor" value="<c:out value="${aUserpassVO.nmKor}"/>" size="30" maxLength="30" label="이름" class="webtextfield" />
			</td>
		</tr>
		<tr style="display:none;">
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 비밀번호 *</td>
			<td class="webformfield">
				<input type="password" id="UserpassManager_columnValue" name="columnValue" value="<c:out value="${aUserpassVO.columnValue}"/>" size="20" maxLength="30" label="비밀번호" class="webtextfield" />
				&nbsp;&nbsp;비밀번호확인 <input type="password" id="UserpassManager_columnValue2" name="columnValue" value="<c:out value="${aUserpassVO.columnValue}"/>" size="20" maxLength="30" label="비밀번호확인" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 직분</td>
			<td class="webformfield">
				<select id="UserpassManager_gradeCd" name="gradeCd" class='webdropdownlist'>
					<c:forEach items="${gradeCdList}" var="gradeCd">
					<option value="<c:out value="${gradeCd.code}"/>"><c:out value="${gradeCd.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 메일주소</td>
			<td class="webformfield">
				<input type="hidden" id="UserpassManager_emailAddr" name="emailAddr" label="메일주소"/>
				<input type="text" id="UserpassManager_emailAddr1" name="emailAddr1" value="" size="15" maxLength="20" class="webtextfield" />@
				<select id="UserpassManager_emailAddr2" class='webdropdownlist'>
					<c:forEach items="${emailAddrList}" var="emailAddr">
					<option value="<c:out value="${emailAddr.code}"/>"><c:out value="${emailAddr.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 집전화번호</td>
			<td class="webformfield">
				<input type="hidden" id="UserpassManager_homeTel" name="homeTel" label="<util:message key='pt.ev.property.homeTel'/>"/>
				<select id="UserpassManager_homeTel1" class='webdropdownlist'>
					<c:forEach items="${homeTelList}" var="homeTel">
					<option value="<c:out value="${homeTel.code}"/>"><c:out value="${homeTel.codeName}"/></option>
					</c:forEach>
				</select>&nbsp;
				<input type="text" id="UserpassManager_homeTel2" name="homeTel2" value="" size="15" maxLength="4" class="webtextfield" />-
				<input type="text" id="UserpassManager_homeTel3" name="homeTel3" value="" size="15" maxLength="4" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 집주소</td>
			<td class="webformfield">
				<input type="hidden" id="UserpassManager_homeZip" name="homeZip" value="<c:out value="${aUserpassVO.homeZip}"/>" size="5" maxLength="" label="우편번호" class="webtextfield" />
				<input type="text" id="UserpassManager_homeZip1" name="homeZip1" value="" size="5" maxLength="" label="우편번호1" class="webtextfield" /> - 
				<input type="text" id="UserpassManager_homeZip2" name="homeZip2" value="" size="5" maxLength="" label="우편번호2" class="webtextfield" />&nbsp;
				<input type="text" id="UserpassManager_homeAddr1" name="homeAddr1" value="<c:out value="${aUserpassVO.homeAddr1}"/>" size="80" maxLength="20" label="집주소" class="webtextfield" />
				<span class="btn_pack small"><a href=" ">주소찾기</a></span>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 상세주소</td>
			<td class="webformfield">
				<input type="text" id="UserpassManager_homeAddr2" name="homeAddr2" value="<c:out value="${aUserpassVO.homeAddr2}"/>" size="80" maxLength="20" label="상세주소" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 생년월일</td>
			<td class="webformfield"><input type="text" id="UserpassManager_birthYmd" name="birthYmd" size="20" value="<c:out value="${aUserpassVO.birthYmd}"/>" label="생년월일" class="webtextfield" /></td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 자기소개</td>
			<td class="webformfield">
				<input type="text" id="UserpassManager_intro" name="intro" value="<c:out value="${aUserpassVO.intro}"/>" maxLength="200" label="자기소개" class="full_webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 성별</td>
			<td class="webformfield">
				<input type="radio" id="UserpassManager_sexFlag_male" name="sexFlag" value="1" label="성별" checked /><label for="radio">남자 &nbsp;&nbsp;
				<input type="radio" id="UserpassManager_sexFlag_female" name="sexFlag" value="2" label="성별" /><label for="radio">여자
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 접속차단여부</td>
			<td class="webformfield">
				<input type="radio" id="UserpassManager_accessFlag_0" name="accessFlag" value="0" label="접속차단여부" checked /><label for="radio">접속차단 &nbsp;&nbsp;
				<input type="radio" id="UserpassManager_accessFlag_1" name="accessFlag" value="1" label="접속차단여부" /><label for="radio">접속허용
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 인도자</td>
			<td class="webformfield">
				<input type="text" id="UserpassManager_guide" name="guide" value="<c:out value="${aUserpassVO.guide}"/>" size="30" maxLength="200" label="인도자" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 등록일</td>
			<td class="webformfield"><input type="text" id="UserpassManager_regDatim" name="regDatim" size="20" value="<c:out value="${aUserpassVO.regDatim}"/>" label="등록일" class="webtextfield" /></td>
		</tr>
	</table>
	<table style="width:100%;" class="webbuttonpanel">
	<tr>
		<td align="right">  
			<span class="btn_pack small"><a href="javascript:aUserpassManager.doUpdate()"><util:message key='pt.ev.button.save'/></a></span>
		</td>
	</tr>
	</table>
	</form>
</div>

<div id="UserpassManager_UserpassChooser"></div>

