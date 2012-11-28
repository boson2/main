
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/todayWordManager.js"></script>
<script type="text/javascript">
function initTodayWordManager() {
	if( aTodayWordManager == null ) {
		aTodayWordManager = new TodayWordManager("<c:out value="${evSecurityCode}"/>");
		aTodayWordManager.init();
		aTodayWordManager.doDefaultSelect();
	}
}
function finishTodayWordManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initTodayWordManager );
	window.attachEvent ( "onunload", finishTodayWordManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initTodayWordManager, false );
	window.addEventListener ( "unload", finishTodayWordManager, false );
}
else
{
    window.onload = initTodayWordManager;
	window.onunload = finishTodayWordManager;
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div class="webpanel" id="TodayWordManager_mainTabs">
			<ul>
				<li><a href="#TodayWordManager_TodayWordTabPage" >오늘의 말씀</a></li>
			</ul>
			<div id="TodayWordManager_TodayWordTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="TodayWordManager_SearchForm" name="TodayWordManager_SearchForm" style="display:inline" action="javascript:aTodayWordManager.doSearch('TodayWordManager_SearchForm')" onkeydown="if(event.keyCode==13) aTodayWordManager.doSearch('TodayWordManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aTodayWordManager.doPage'/>
						<input type='hidden' name='formName' value='TodayWordManager_SearchForm'/>
					
					  <tr>
						<td align="right">
						
							<input type="text" name="titleCond" size="20" class="webtextfield" value="*구절*" onfocus="whenSrchFocus(this,'*구절*');" onblur="whenSrchBlur(this,'*구절*');"/> 
							<input type="text" name="titleCond2" size="30" class="webtextfield" value="*말씀*" onfocus="whenSrchFocus(this,'*말씀*');" onblur="whenSrchBlur(this,'*말씀*');"/>
										
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aTodayWordManager.doSearch('TodayWordManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="TodayWordManager_ListForm" style="display:inline" name="TodayWordManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aTodayWordManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" ch="0" onclick="aTodayWordManager.doSort(this, 'TITLE');" >
								<span >구절</span>
							</th>	
							<th class="webgridheaderlast" ch="0" onclick="aTodayWordManager.doSort(this, 'START_DATE');" >
								<span >게시일</span>
							</th>		
						</tr>
					</thead>
					<tbody id="TodayWordManager_ListBody">
					<c:forEach items="${results}" var="todayword" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="TodayWordManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
								<input type="hidden" id="TodayWordManager[<c:out value="${status.index}"/>].wordId" value="<c:out value='${todayword.wordId}'/>"/>
							</td>
							<td align="left" class="webgridbody" onclick="aTodayWordManager.doSelect(this)">
								<span class="webgridrowlabel" id="TodayWordManager[<c:out value="${status.index}"/>].title.label">&nbsp;<c:out value="${todayword.title}"/></span>
							</td>
							<td align="left" class="webgridbodylast" onclick="aTodayWordManager.doSelect(this)">
								<span class="webgridrowlabel" id="TodayWordManager[<c:out value="${status.index}"/>].startDate.label">&nbsp;<c:out value="${todayword.startDate}"/></span>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				  </table>
				  </form>
				  <div id="TodayWordManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="TodayWordManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aTodayWordManager.doCreate()"><util:message key='pt.ev.button.new'/></a></span>
							<span class="btn_pack small"><a href="javascript:aTodayWordManager.doRemove()"><util:message key='pt.ev.button.remove'/></a></span>
					    </td>
					</tr>
					</table>
				
					<div id="TodayWordManager_EditFormPanel" class="webformpanel" >  
					<div id="TodayWordManager_propertyTabs">
						<ul>
							<li><a href="#TodayWordManager_DetailTabPage"><util:message key='pt.ev.property.detailTab'/></a></li>   
							
						</ul>
						<div id="TodayWordManager_DetailTabPage" style="width:99%;">
							<%@include file="todayWordDetail.jsp"%>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					
					</div>
					</div> <!-- End TodayWordManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End TodayWordManager_TodayWordTabPage -->
		</div>
	</td>
<tr>
</table>

