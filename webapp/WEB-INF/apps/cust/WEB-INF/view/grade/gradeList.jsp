
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/gradeManager.js"></script>
<script type="text/javascript">
function initGradeManager() {
	if( aGradeManager == null ) {
		aGradeManager = new GradeManager("<c:out value="${evSecurityCode}"/>");
		aGradeManager.init();
		aGradeManager.doDefaultSelect();
	}
}
function finishGradeManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initGradeManager );
	window.attachEvent ( "onunload", finishGradeManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initGradeManager, false );
	window.addEventListener ( "unload", finishGradeManager, false );
}
else
{
    window.onload = initGradeManager;
	window.onunload = finishGradeManager;
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div class="webpanel" id="GradeManager_mainTabs">
			<ul>
				<li><a href="#GradeManager_GradeTabPage" >등급관리</a></li>
			</ul>
			<div id="GradeManager_GradeTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="GradeManager_SearchForm" name="GradeManager_SearchForm" style="display:inline" action="javascript:aGradeManager.doSearch('GradeManager_SearchForm')" onkeydown="if(event.keyCode==13) aGradeManager.doSearch('GradeManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aGradeManager.doPage'/>
						<input type='hidden' name='formName' value='GradeManager_SearchForm'/>
					
					  <tr>
						<td class="area_sub_t">교회에서 사용하는 직분을 등록하세요</td>
						<td align="right">
						
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aGradeManager.doSearch('GradeManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="GradeManager_ListForm" style="display:inline" name="GradeManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aGradeManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" ch="0" width="50px" >
								<span >등급</span>
							</th>
							<th class="webgridheader" ch="0" width="200px" onclick="aGradeManager.doSort(this, 'CODE_NAME');" >
								<span >권한등급명</span>
							</th>	
							<th class="webgridheaderlast" ch="0" onclick="aGradeManager.doSort(this, 'REMARK');" >
								<span >설명</span>
							</th>		
						</tr>
					</thead>
					<tbody id="GradeManager_ListBody">
			
					
					<c:forEach items="${results}" var="grade" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="GradeManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
							
								<input type="hidden" id="GradeManager[<c:out value="${status.index}"/>].code" value="<c:out value='${grade.code}'/>"/>
							</td>
							<td align="left" class="webgridbody" onclick="aGradeManager.doSelect(this)">
								<span class="webgridrowlabel" id="GradeManager[<c:out value="${status.index}"/>].code.label">&nbsp;<c:out value="${grade.sortOrder}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aGradeManager.doSelect(this)">
								<span class="webgridrowlabel" id="GradeManager[<c:out value="${status.index}"/>].codeName.label">&nbsp;<c:out value="${grade.codeName}"/></span>
							</td>
							<td align="left" class="webgridbodylast" onclick="aGradeManager.doSelect(this)">
								<span class="webgridrowlabel" id="GradeManager[<c:out value="${status.index}"/>].remark.label">&nbsp;<c:out value="${grade.remark}"/></span>
							</td>
						</tr>
					</c:forEach>
				
					</tbody>
				  </table>
				  </form>
				  <div id="GradeManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="GradeManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aGradeManager.doCreate()"><util:message key='pt.ev.button.new'/></a></span>
							<span class="btn_pack small"><a href="javascript:aGradeManager.doRemove()"><util:message key='pt.ev.button.remove'/></a></span>
					    </td>
					</tr>
					</table>
				
					<div id="GradeManager_EditFormPanel" class="webformpanel" >  
					<div id="GradeManager_propertyTabs">
						<ul>
							<li><a href="#GradeManager_DetailTabPage"><util:message key='pt.ev.property.detailTab'/></a></li>   
							
						</ul>
						<div id="GradeManager_DetailTabPage" style="width:99%;">
							<%@include file="gradeDetail.jsp"%>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					
					</div>
					</div> <!-- End GradeManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End GradeManager_GradeTabPage -->
		</div>
	</td>
<tr>
</table>

