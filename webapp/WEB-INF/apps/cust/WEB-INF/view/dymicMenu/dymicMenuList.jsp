
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dymicMenuManager.js"></script>
<script type="text/javascript">
function initDymicMenuManager() {
	if( aDymicMenuManager == null ) {
		aDymicMenuManager = new DymicMenuManager("<c:out value="${evSecurityCode}"/>");
		aDymicMenuManager.init();
		aDymicMenuManager.doDefaultSelect();
	}
}
function finishDymicMenuManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initDymicMenuManager );
	window.attachEvent ( "onunload", finishDymicMenuManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initDymicMenuManager, false );
	window.addEventListener ( "unload", finishDymicMenuManager, false );
}
else
{
    window.onload = initDymicMenuManager;
	window.onunload = finishDymicMenuManager;
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div class="webpanel" id="DymicMenuManager_mainTabs">
			<ul>
				<li><a href="#DymicMenuManager_DymicMenuTabPage" >동적메뉴관리</a></li>
			</ul>
			<div id="DymicMenuManager_DymicMenuTabPage">
				<div>
					<form id="DymicMenuManager_SearchForm" name="DymicMenuManager_SearchForm" style="display:inline" action="javascript:aDymicMenuManager.doSearch('DymicMenuManager_SearchForm')" onkeydown="if(event.keyCode==13) aDymicMenuManager.doSearch('DymicMenuManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aDymicMenuManager.doPage'/>
						<input type='hidden' name='formName' value='DymicMenuManager_SearchForm'/>
					
					  <tr>
						<td align="right">
						
							<input type="text" name="titleCond" size="" class="webtextfield" value="*<util:message key='pt.ev.property.title'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.title'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.title'/>*');"/> 
										
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doSearch('DymicMenuManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="DymicMenuManager_ListForm" style="display:inline" name="DymicMenuManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aDymicMenuManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" ch="0" width="180px" onclick="aDymicMenuManager.doSort(this, 'MENU_NAME');" >
								<span >메뉴명</span>
							</th>	
							<th class="webgridheader" ch="0" width="80px" onclick="aDymicMenuManager.doSort(this, 'APPLY_FEED');" >
								<span >형태</span>
							</th>	
							<th class="webgridheader" ch="0" width="80px" onclick="aDymicMenuManager.doSort(this, 'WRITE_AUTH');" >
								<span >작성권한</span>
							</th>	
							<th class="webgridheader" ch="0" width="80px" onclick="aDymicMenuManager.doSort(this, 'READ_AUTH');" >
								<span >읽기권한</span>
							</th>	
							<th class="webgridheader" ch="0" onclick="aDymicMenuManager.doSort(this, 'DESC');" >
								<span >설명</span>
							</th>
							<th class="webgridheaderlast" ch="0" width="50px" onclick="aDymicMenuManager.doSort(this, 'IS_SERVICE');" >
								<span >상태</span>
							</th>		
						</tr>
					</thead>
					<tbody id="DymicMenuManager_ListBody">
			
					
					<c:forEach items="${results}" var="dymicmenu" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="DymicMenuManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
								<input type="hidden" id="DymicMenuManager[<c:out value="${status.index}"/>].menuId" value="<c:out value='${dymicmenu.menuId}'/>"/>
							</td>
							<td align="left" class="webgridbody" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].menuName.label">&nbsp;<c:out value="${dymicmenu.menuName}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].applyFeed.label">&nbsp;<c:out value="${dymicmenu.applyFeed}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].writeAuth.label">&nbsp;<c:out value="${dymicmenu.writeAuth}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].readAuth.label">&nbsp;<c:out value="${dymicmenu.readAuth}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].desc.label">&nbsp;<c:out value="${dymicmenu.desc}"/></span>
							</td>
							<td align="left" class="webgridbodylast" onclick="aDymicMenuManager.doSelect(this)">
								<span class="webgridrowlabel" id="DymicMenuManager[<c:out value="${status.index}"/>].isService.label">&nbsp;<c:out value="${dymicmenu.serviceStatus}"/></span>
							</td>
						</tr>
					</c:forEach>
				
					</tbody>
				  </table>
				  </form>
				  <div id="DymicMenuManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="DymicMenuManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doCreate()"><util:message key='pt.ev.button.new'/></a></span>
							<span class="btn_pack small"><a href="javascript:aDymicMenuManager.doRemove()"><util:message key='pt.ev.button.remove'/></a></span>
					    </td>
					</tr>
					</table>
				
					<div id="DymicMenuManager_EditFormPanel" class="webformpanel" >  
					<div id="DymicMenuManager_propertyTabs">
						<ul>
							<li><a href="#DymicMenuManager_DetailTabPage"><util:message key='pt.ev.property.detailTab'/></a></li>   
							
						</ul>
						<div id="DymicMenuManager_DetailTabPage" style="width:99%;">
							<%@include file="dymicMenuDetail.jsp"%>
						</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
					
					</div>
					</div> <!-- End DymicMenuManager_EditFormPanel -->
				</div> <!-- End webformpanel -->
			</div> <!-- End DymicMenuManager_DymicMenuTabPage -->
		</div>
	</td>
<tr>
</table>

