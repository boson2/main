
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/cust/css/styles.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/cust/javascript/dymicMenuManager.js"></script>


<table width="100%" height="400px" border="0" cellspacing="0" cellpadding="0" >
	<tr>
		<td valign='top'>
			<table width="100%" border="0" cellspacing="1" cellpadding="0"> 
			<tr>
				<td valign="top">
					<div class="webpanel">
						<div id="DymicMenuChooser_ListTabPage" style="width:100%; height:300px;"> 
							<div class="webformpanel">
								<form id="DymicMenuChooser_SearchForm" name="DymicMenuChooser_SearchForm" style="display:inline" action="javascript:aDymicMenuChooser.doSearch('DymicMenuChooser_SearchForm')" onkeydown="if(event.keyCode==13) aDymicMenuChooser.doSearch('DymicMenuChooser_SearchForm')" method="post">
									<div>
									  <table width="100%">
										<input type='hidden' name='sortMethod' value='ASC'/>                    
										<input type='hidden' name='sortColumn' value=''/>  
										<input type='hidden' name='pageNo' value='1'/>
										<!--input type='hidden' name='pageSize' value='10'/-->
										<input type='hidden' name='pageFunction' value='aDymicMenuChooser.doPage'/>
										<input type='hidden' name='formName' value='DymicMenuChooser_SearchForm'/>
										
									  <tr>
										<td align="right">
										
											<input type="text" name="titleCond" size="" class="webtextfield" value="*<util:message key='pt.ev.property.title'/>*" onfocus="whenSrchFocus(this,'*<util:message key='pt.ev.property.title'/>*');" onblur="whenSrchBlur(this,'*<util:message key='pt.ev.property.title'/>*');"/> 
														
											<select name='pageSize'>
												<option value="5">5</option>
												<option value="10">10</option>
												<option value="20">20</option>
												<option value="50">50</option>
												<option value="100">100</option>
											</select>
											<span class="btn_pack small"><a href="javascript:aDymicMenuChooser.doSearch('DymicMenuChooser_SearchForm')"><util:message key='pt.ev.button.search'/></a></span>
										</td>
									  </tr>
									  </table>    
									</div>
								</form>
								<form id="DymicMenuChooser_ListForm" style="display:inline" name="ListForm" action="" method="post">
									<div class="webgridpanel" style="overflow:auto; ">
										<table id="grid-table" width="100%" border="0" cellspacing="0" cellpadding="0">
											<thead>
												<tr> 
													<td colspan="100" class="webgridheaderline"></td>
												</tr>
												<tr style="cursor: pointer;">
													<th class="webgridheader" align="center" width="30px">
														<input type="checkbox" id="delCheck" class="webcheckbox" onclick="aDymicMenuChooser.m_checkBox.chkAll(this)"/>
													</th>
													<th class="webgridheader" align="center" width="30px">No</th>
												
													<th class="webgridheader" ch="0" onclick="aDymicMenuChooser.doSort(this, 'MENU_NAME');" >
														<span ><util:message key='pt.ev.property.menuName'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aDymicMenuChooser.doSort(this, 'APPLY_FEED');" >
														<span ><util:message key='pt.ev.property.applyFeed'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aDymicMenuChooser.doSort(this, 'WRITE_AUTH');" >
														<span ><util:message key='pt.ev.property.writeAuth'/></span>
													</th>	
													<th class="webgridheader" ch="0" onclick="aDymicMenuChooser.doSort(this, 'READ_AUTH');" >
														<span ><util:message key='pt.ev.property.readAuth'/></span>
													</th>	
													<th class="webgridheaderlast" ch="0" onclick="aDymicMenuChooser.doSort(this, 'IS_SERVICE');" >
														<span ><util:message key='pt.ev.property.isService'/></span>
													</th>				
												</tr>
											</thead>
											<tbody id="DymicMenuChooser_ListBody"></tbody>
										</table>
										<div id="DymicMenuChooser_ListMessage"/>
									</div>
									<table style="width:100%;" class="webbuttonpanel">
										<tr>
											<td align="center">
											<div id="DymicMenuChooser_PAGE_ITERATOR" class="webnavigationpanel"></div>
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

