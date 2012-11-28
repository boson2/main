
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dymicMenuManager.js"></script>

<div class="webformpanel" style="width:100%;">
	<form id="DymicMenuManager_EditForm" style="display:inline" action="" method="post">
		<table cellpadding=0 cellspacing=0 border=0 width='100%'>
		<input type="hidden" id="DymicMenuManager_isCreate">
		
		<input type="hidden" id="DymicMenuManager_sortOrder" name="sortOrder">	
		<input type="hidden" id="DymicMenuManager_menuId" name="menuId" value="<c:out value="${aDymicMenuVO.menuId}"/>" />
		<tr> 
			<td colspan="2" width="100%" class="webformheaderline"></td>    
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 메뉴명 *</td>
			<td class="webformfield">
				<input type="text" id="DymicMenuManager_menuName" name="menuName" value="<c:out value="${aDymicMenuVO.menuName}"/>" size="50" maxLength="300" label="메뉴명" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 설명 *</td>
			<td class="webformfield">
				<input type="text" id="DymicMenuManager_desc" name="desc" value="<c:out value="${aDymicMenuVO.desc}"/>" size="100" maxLength="300" label="설명" class="webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 피드선택</td>
			<td class="webformfield">
				<select id="DymicMenuManager_applyFeed" name="applyFeed" class='webdropdownlist'>
					<c:forEach items="${applyFeedList}" var="applyFeed">
					<option value="<c:out value="${applyFeed.code}"/>"><c:out value="${applyFeed.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 작성권한</td>
			<td class="webformfield">
				<select id="DymicMenuManager_writeAuth" name="writeAuth" class='webdropdownlist'>
					<c:forEach items="${writeAuthList}" var="writeAuth">
					<option value="<c:out value="${writeAuth.code}"/>"><c:out value="${writeAuth.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 읽기권한</td>
			<td class="webformfield">
				<select id="DymicMenuManager_readAuth" name="readAuth" class='webdropdownlist'>
					<c:forEach items="${readAuthList}" var="readAuth">
					<option value="<c:out value="${readAuth.code}"/>"><c:out value="${readAuth.codeName}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="display:none">
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 프로그램 URL *</td>
			<td class="webformfield">
				<input type="text" id="DymicMenuManager_url" name="url" value="<c:out value="${aDymicMenuVO.url}"/>" maxLength="255" label="<util:message key='pt.ev.property.url'/>" class="full_webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 서비스상태</td>
			<td class="webformfield">
				<input type="radio" id="DymicMenuManager_isService_on" name="isService" value="1" label="서비스상태" checked /><label for="radio">On &nbsp;&nbsp;
				<input type="radio" id="DymicMenuManager_isService_off" name="isService" value="0" label="서비스상태" /><label for="radio">Off
			</td>
		</tr>
	</table>
	<table style="width:100%;" class="webbuttonpanel">
	<tr>
		<td align="right">  
			<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doMoveUp()">위로이동</a></span>
			<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doMoveDown()">아래로이동</a></span>
			<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doUpdate()">저장</a></span>
		</td>
	</tr>
	</table>
	</form>
</div>

