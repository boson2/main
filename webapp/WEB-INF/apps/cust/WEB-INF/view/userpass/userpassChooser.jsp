
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/userpassManager.js"></script>


<table width="100%" height="400px" border="0" cellspacing="0" cellpadding="0" >
	<tr>
		<td valign='top'>
			<table width="100%" border="0" cellspacing="1" cellpadding="0"> 
			<tr>
				<td valign="top">
					<div class="webpanel">
						<div id="UserpassChooser_ListTabPage" style="width:100%; height:300px;"> 
							<div class="webformpanel">
								<form id="UserpassChooser_SearchForm" name="UserpassChooser_SearchForm" style="display:inline" action="javascript:aUserpassChooser.doSearch('UserpassChooser_SearchForm')" onkeydown="if(event.keyCode==13) aUserpassChooser.doSearch('UserpassChooser_SearchForm')" method="post">
									<div>
									  <table width="100%">
										<input type='hidden' name='sortMethod' value='ASC'/>                    
										<input type='hidden' name='sortColumn' value=''/>  
										<input type='hidden' name='pageNo' value='1'/>
										<!--input type='hidden' name='pageSize' value='10'/-->
										<input type='hidden' name='pageFunction' value='aUserpassChooser.doPage'/>
										<input type='hidden' name='formName' value='UserpassChooser_SearchForm'/>
										
									  <tr>
										<td align="right">
										
											<input type="text" name="userIdCond" size="30" class="webtextfield" value="*<util:message key='pt.ev.property.userId'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.userId'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.userId'/>*');"/> 
														
											<input type="text" name="nmKorCond" size="20" class="webtextfield" value="*<util:message key='pt.ev.property.nmKor'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.nmKor'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.nmKor'/>*');"/> 
														
											<select name='pageSize'>
												<option value="5">5</option>
												<option value="10">10</option>
												<option value="20">20</option>
												<option value="50">50</option>
												<option value="100">100</option>
											</select>
											<span class="btn_pack small"><a href="javascript:aUserpassChooser.doSearch('UserpassChooser_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
										</td>
									  </tr>
									  </table>    
									</div>
								</form>
								<form id="UserpassChooser_ListForm" style="display:inline" name="ListForm" action="" method="post">
									<div class="webgridpanel" style="overflow:auto; ">
										<table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
											<thead>
												<tr> 
													<td colspan="100" class="webgridheaderline"></td>
												</tr>
												<tr style="cursor: pointer;">
													<th class="webgridheader" align="center" width="30px">
														<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aUserpassChooser.m_checkBox.chkAll(this)"/>
													</th>
													<th class="webgridheader" align="center" width="30px">No</th>
												
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'NM_KOR');" >
														<span ><util:message key='pt.ev.property.nmKor'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'GRADE_CD');" >
														<span ><util:message key='pt.ev.property.gradeCd'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'MOBILE_TEL');" >
														<span ><util:message key='pt.ev.property.mobileTel'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'IS_ACTIVE');" >
														<span ><util:message key='pt.ev.property.isActive'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'SEX_FLAG');" >
														<span ><util:message key='pt.ev.property.sexFlag'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aUserpassChooser.doSort(this, 'REG_DATIM');" >
														<span ><util:message key='pt.ev.property.regDatim'/></span>
													</th>	
													<th class="webgridheaderlast" ch="0" onclick="aUserpassChooser.doSort(this, 'AGE');" >
														<span ><util:message key='pt.ev.property.age'/></span>
													</th>				
												</tr>
											</thead>
											<tbody id="UserpassChooser_ListBody"></tbody>
										</table>
										<div id="UserpassChooser_ListMessage"/>
									</div>
									<table style="width:100%;" class="webbuttonpanel">
										<tr>
											<td align="center">
											<div id="UserpassChooser_PAGE_ITERATOR" class="webnavigationpanel"></div>
											</td>   
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>

