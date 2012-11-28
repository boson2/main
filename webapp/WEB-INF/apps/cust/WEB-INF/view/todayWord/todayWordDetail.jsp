
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/admin/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/admin/javascript/todayWordManager.js"></script>

<div class="webformpanel" style="width:100%;">
	<form id="TodayWordManager_EditForm" style="display:inline" action="" method="post">
		<table cellpadding=0 cellspacing=0 border=0 width='100%'>
		<input type="hidden" id="TodayWordManager_isCreate">
		<input type="hidden" id="TodayWordManager_wordId" name="wordId" value="" />
		<tr> 
			<td colspan="2" width="100%" class="webformheaderline"></td>    
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/admin/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 구절 *</td>
			<td class="webformfield">
				<input type="text" id="TodayWordManager_title" name="title" value="<c:out value="${aTodayWordVO.title}"/>" maxLength="80" label="<util:message key='pt.ev.property.title'/>" class="full_webtextfield" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/admin/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 오늘의 말씀 *</td>
			<td class="webformfield">
				<textarea id="TodayWordManager_content" name="content" value="<c:out value="${aTodayWordVO.content}"/>" cols="80" rows="5" maxLength="300" label="<util:message key='pt.ev.property.content'/>" class="webtextarea" >	</textarea>
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/admin/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 게시일</td>
			<td class="webformfield"><input type="text" id="TodayWordManager_startDate" name="startDate" size="20" alue="<c:out value="${aTodayWordVO.startDate}"/>" label="게시일" class="webtextfield" /></td>
		</tr>
	</table>
	<table style="width:100%;" class="webbuttonpanel">
	<tr>
		<td align="right">  
			<span class="btn_pack small"><a href="javascript:aTodayWordManager.doUpdate()"><util:message key='pt.ev.button.save'/></a></span>
		</td>
	</tr>
	</table>
	</form>
</div>

<div id="TodayWordManager_TodayWordChooser"></div>

