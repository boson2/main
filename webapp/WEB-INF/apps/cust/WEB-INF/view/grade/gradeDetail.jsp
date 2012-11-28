
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/gradeManager.js"></script>

<div class="webformpanel" style="width:100%;">
	<form id="GradeManager_EditForm" style="display:inline" action="" method="post">
		<table cellpadding=0 cellspacing=0 border=0 width='100%'>
		<input type="hidden" id="GradeManager_isCreate">
		<tr> 
			<td colspan="2" width="100%" class="webformheaderline"></td>    
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 권한등급코드 *</td>
			<td class="webformfield">
				<input type="text" id="GradeManager_code" name="code" value="<c:out value="${aGradeVO.code}"/>" size="10" maxLength="20" label="권한등급코드" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 권한등급명 *</td>
			<td class="webformfield">
				<input type="text" id="GradeManager_codeName" name="codeName" value="<c:out value="${aGradeVO.codeName}"/>" size="20" maxLength="20" label="권한등급명" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="webformlabel"><img src="<%=request.getContextPath()%>/cust/images/tb_icon.gif" width="5" height="5" align="absmiddle"> 설명 *</td>
			<td class="webformfield">
				<input type="text" id="GradeManager_remark" name="remark" value="<c:out value="${aGradeVO.remark}"/>" maxLength="60" label="설명" class="full_webtextfield" />
			</td>
		</tr>
	</table>
	<table style="width:100%;" class="webbuttonpanel">
	<tr>
		<td align="right">  
			<span class="btn_pack small"><a href="javascript:aGradeManager.doMoveUp()">위로이동</a></span>
			<span class="btn_pack small"><a href="javascript:aGradeManager.doMoveDown()">아래로이동</a></span>
			<span class="btn_pack small"><a href="javascript:aGradeManager.doUpdate()"><util:message key='pt.ev.button.save'/></a></span>
		</td>
	</tr>
	</table>
	</form>
</div>

