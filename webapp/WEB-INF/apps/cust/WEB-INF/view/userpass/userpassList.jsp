
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/userpassManager.js"></script>
<script type="text/javascript">
function initUserpassManager() {
	if( aUserpassManager == null ) {
		aUserpassManager = new UserpassManager("<c:out value="${evSecurityCode}"/>");
		aUserpassManager.init();
		aUserpassManager.doDefaultSelect(0);
	}
}
function finishUserpassManager() {
	
}
// Attach to the onload event
if (window.attachEvent)
{
    window.attachEvent ( "onload", initUserpassManager );
	window.attachEvent ( "onunload", finishUserpassManager );
}
else if (window.addEventListener )
{
    window.addEventListener ( "load", initUserpassManager, false );
	window.addEventListener ( "unload", finishUserpassManager, false );
}
else
{
    window.onload = initUserpassManager;
	window.onunload = finishUserpassManager;
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
<tr>
	<td width="100%" valign="top">
		<div class="webpanel" id="UserpassManager_mainTabs">
			<ul>
				<li><a href="#UserpassManager_UserpassTabPage" onclick="javascript:aUserpassManager.doSelectTab(0)">성도</a></li>
				<li><a href="#UserpassManager_UserpassNewTabPage" onclick="javascript:aUserpassManager.doSelectTab(1)">새가족</a></li>
			</ul>
			<div id="UserpassManager_UserpassTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="UserpassManager_SearchForm" name="UserpassManager_SearchForm" style="display:inline" action="javascript:aUserpassManager.doSearch('UserpassManager_SearchForm')" onkeydown="if(event.keyCode==13) aUserpassManager.doSearch('UserpassManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aUserpassManager.doPage'/>
						<input type='hidden' name='formName' value='UserpassManager_SearchForm'/>
					  <tr>
						<td class="area_note">성도 검색 및 성도에 대해서 접속 차단 및 성도에 대한 명부 출력기능(SMS 연동)</td>
						<td align="right">
							<select name="gradeCdCond" class='webdropdownlist'>
								<c:forEach items="${gradeCdList}" var="gradeCd">
								<option value="<c:out value="${gradeCd.code}"/>"><c:out value="${gradeCd.codeName}"/></option>
								</c:forEach>
							</select>
							<!-- <input type="text" name="userIdCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.userId'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.userId'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.userId'/>*');"/> --> 
							<input type="text" name="nmKorCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.nmKor'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.nmKor'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.nmKor'/>*');"/> 
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doSearch('UserpassManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="UserpassManager_ListForm" style="display:inline" name="UserpassManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aUserpassManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" ch="0" width="200px" onclick="aUserpassManager.doSort(this, 'NM_KOR');" >
								<span >이름</span>
							</th>	
							<th class="webgridheader" ch="0" width="100px" onclick="aUserpassManager.doSort(this, 'GRADE_CD');" >
								<span >직분</span>
							</th>	
							<th class="webgridheader" ch="0" width="50px" onclick="aUserpassManager.doSort(this, 'IS_ACTIVE');" >
								<span >활동여부</span>
							</th>	
							<th class="webgridheaderlast" ch="0" onclick="aUserpassManager.doSort(this, 'MOBILE_TEL');" >
								<span >전화번호</span>
							</th>	
						</tr>
					</thead>
					<tbody id="UserpassManager_ListBody">
					<c:forEach items="${results}" var="userpass" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="UserpassManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
							
								<input type="hidden" id="UserpassManager[<c:out value="${status.index}"/>].userId" value="<c:out value='${userpass.userId}'/>"/>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassManager[<c:out value="${status.index}"/>].nmKor.label">&nbsp;<c:out value="${userpass.nmKor}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassManager[<c:out value="${status.index}"/>].gradeCd.label">&nbsp;<c:out value="${userpass.gradeCd}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassManager[<c:out value="${status.index}"/>].isActive.label">&nbsp;<c:out value="${userpass.isActive}"/></span>
							</td>
							<td align="left" class="webgridbodylast" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassManager[<c:out value="${status.index}"/>].mobileTel.label">&nbsp;<c:out value="${userpass.mobileTel}"/></span>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				  </table>
				  </form>
				  <div id="UserpassManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="UserpassManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doRemove()">삭제</a></span>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doAccessDeny()">접속차단</a></span>
					    </td>
					</tr>
					</table>
				</div>
			</div>
			
			<div id="UserpassManager_UserpassNewTabPage">
				<br style='line-height:5px;'>
				<div>
					<form id="UserpassNewManager_SearchForm" name="UserpassNewManager_SearchForm" style="display:inline" action="javascript:aUserpassManager.doSearch('UserpassNewManager_SearchForm')" onkeydown="if(event.keyCode==13) aUserpassManager.doSearch('UserpassNewManager_SearchForm')" method="post">
					  <table width="100%">
						<input type='hidden' name='sortMethod' value='ASC'/>                    
						<input type='hidden' name='sortColumn' value=''/>  
						<input type='hidden' name='pageNo' value='1'/>
						<!--input type='hidden' name='pageSize' value='10'/-->
						<input type='hidden' name='pageFunction' value='aUserpassManager.doPage'/>
						<input type='hidden' name='formName' value='UserpassNewManager_SearchForm'/>
					  <tr>
						<td class="area_note">성도 검색 및 성도에 대해서 접속 차단 및 성도에 대한 명부 출력기능(SMS 연동)</td>
						<td align="right">
							<select name="gradeCdCond" class='webdropdownlist'>
								<c:forEach items="${gradeCdList}" var="gradeCd">
								<option value="<c:out value="${gradeCd.code}"/>"><c:out value="${gradeCd.codeName}"/></option>
								</c:forEach>
							</select>
							<!-- <input type="text" name="userIdCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.userId'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.userId'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.userId'/>*');"/>  --> 
							<input type="text" name="nmKorCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.nmKor'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.nmKor'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.nmKor'/>*');"/> 
							<select name='pageSize'>
								<option value="5">5</option>
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doSearch('UserpassNewManager_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
						</td>
					  </tr>
					  </table>    
					</form>
				</div>
				<div class="webgridpanel">
				  <form id="UserpassNewManager_ListForm" style="display:inline" name="UserpassNewManager_ListForm" action="" method="post">
				  <table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr> 
							<td colspan="100" class="webgridheaderline"></td>
						</tr>
						<tr style="cursor: pointer;">
							<th class="webgridheader" align="center" width="30px">
								<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aUserpassManager.m_checkBox.chkAll(this)"/>
							</th>
							<th class="webgridheader" ch="0" width="200px" onclick="aUserpassManager.doSort(this, 'NM_KOR');" >
								<span >이름</span>
							</th>	
							<th class="webgridheader" ch="0" width="200px" onclick="aUserpassManager.doSort(this, 'REG_DATIM');" >
								<span >등록일</span>
							</th>	
							<th class="webgridheader" ch="0" width="40px" onclick="aUserpassManager.doSort(this, 'SEX_FLAG');" >
								<span >성별</span>
							</th>	
							<th class="webgridheader" ch="0" width="50px" onclick="aUserpassManager.doSort(this, 'AGE');" >
								<span >나이</span>
							</th>		
							<th class="webgridheaderlast" ch="0" onclick="aUserpassManager.doSort(this, 'MOBILE_TEL');" >
								<span >전화번호</span>
							</th>	
						</tr>
					</thead>
					<tbody id="UserpassNewManager_ListBody">
			
					
					<c:forEach items="${results}" var="userpass" varStatus="status">
						<tr ch="<c:out value="${status.index}"/>" style="cursor:pointer;" class="<c:choose><c:when test="{$status.index%2 == 0}">even</c:when><c:otherwise>odd</c:otherwise></c:choose>" onmouseover="whenListMouseOver(this)" onmouseout="whenListMouseOut(this)">
							<td align="center" class="webgridbody">
								<input type="checkbox" id="UserpassNewManager[<c:out value="${status.index}"/>].checkRow" class="webcheckbox"/>
							
								<input type="hidden" id="UserpassNewManager[<c:out value="${status.index}"/>].userId" value="<c:out value='${userpass.userId}'/>"/>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassNewManager[<c:out value="${status.index}"/>].nmKor.label">&nbsp;<c:out value="${userpass.nmKor}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassNewManager[<c:out value="${status.index}"/>].regDatim.label">&nbsp;<c:out value="${userpass.regDatim}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassNewManager[<c:out value="${status.index}"/>].sexFlag.label">&nbsp;<c:out value="${userpass.sexFlag}"/></span>
							</td>
							<td align="left" class="webgridbody" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassNewManager[<c:out value="${status.index}"/>].age.label">&nbsp;<c:out value="${userpass.age}"/></span>
							</td>
							<td align="left" class="webgridbodylast" onclick="aUserpassManager.doSelect(this)">
								<span class="webgridrowlabel" id="UserpassNewManager[<c:out value="${status.index}"/>].mobileTel.label">&nbsp;<c:out value="${userpass.mobileTel}"/></span>
							</td>
						</tr>
					</c:forEach>
				
					</tbody>
				  </table>
				  </form>
				  <div id="UserpassNewManager_ListMessage">
					<c:out value='${resultMessage}'/>
				  </div>
				</div>
				<div>
					<table style="width:100%;" class="webbuttonpanel">
					<tr>
					    <td align="center">
					    <div id="UserpassNewManager_PAGE_ITERATOR" class="webnavigationpanel">
							<c:out escapeXml='false' value='${pageIterator}'/>
					    </div>
					    </td>    
					</tr>
					<tr>
					    <td align="right">
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doRemove()">삭제</a></span>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doCreate()">새가족등록</a></span>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doApprove()">새가족승인</a></span>
							<span class="btn_pack small"><a href="javascript:aUserpassManager.doAccessDeny()">접속차단</a></span>
					    </td>
					</tr>
					</table>
				</div>
			</div>
				
			<div id="UserpassManager_EditFormPanel" class="webformpanel" >  
				<div id="UserpassManager_propertyTabs">
					<ul>
						<li><a href="#UserpassManager_DetailTabPage"><util:message key='pt.ev.property.detailTab'/></a></li>   
						
					</ul>
					<div id="UserpassManager_DetailTabPage" style="width:99%;">
						<%@include file="userpassDetail.jsp"%>
					</div> <!--xsl:value-of select="@name"/>Manager_DetailTabPage -->
				
				</div>
			</div> <!-- End UserpassManager_EditFormPanel -->
		</div>
	</td>
<tr>
</table>

