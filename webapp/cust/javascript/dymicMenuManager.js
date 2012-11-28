
var aDymicMenuManager = null;
DymicMenuManager = function(evSecurityCode)
{
	this.m_ajax = new enview.util.Ajax();
	this.m_checkBox = new enview.util.CheckBoxUtil();
	this.m_validator = new enview.util.Validator();
	this.m_utility = new enview.util.Utility();
	this.m_contextPath = portalPage.getContextPath();
	this.m_selectRowIndex = 0;
	this.m_evSecurityCode = evSecurityCode;
	if( portalPage == null) portalPage = new enview.portal.Page();
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
}
DymicMenuManager.prototype =
{
	m_ajax : null,
	m_checkBox : null,
	m_validator : null,
	m_utility : null,
	m_contextPath : null,
	m_selectRowIndex : 0,
	m_dataStructure : null,
	m_evSecurityCode : null,

	
	init : function() { 
		
		this.m_dataStructure = [
			{"fieldName":"menuName", "validation":"Required,MaxLength", "maxLength":"300"}
			,{"fieldName":"menuId", "validation":""}
			,{"fieldName":"desc", "validation":"Required,MaxLength", "maxLength":"300"}
			,{"fieldName":"applyFeed", "validation":""}
			,{"fieldName":"writeAuth", "validation":""}
			,{"fieldName":"readAuth", "validation":""}
			,{"fieldName":"url", "validation":"MaxLength", "maxLength":"255"}
			,{"fieldName":"isService", "validation":""}
			
		]
		
		$(function() {
			$("#DymicMenuManager_mainTabs").tabs();
		});
		
		$(function() {
			$("#DymicMenuManager_propertyTabs").tabs();
		});
		
	},
	initSearch : function () {
		var formElem = document.forms[ "DymicMenuManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
	},
	doRetrieve : function () {
		this.doCreate();
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = document.forms[ "DymicMenuManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
	    
		this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/listForAjax.cust", param, false, {success: function(data) { aDymicMenuManager.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		
		this.m_utility.setListData2(
			"DymicMenuManager",
			new Array('menuId'),
			new Array('menuName', 'applyFeed', 'writeAuth', 'readAuth', 'desc', 'serviceStatus'),
			new Function( "aDymicMenuManager.doSelect(this)" ),
			this.m_contextPath,
			resultObject);
				
		if(resultObject.Data.length != 0) {
			this.doDefaultSelect(); 
		}
	},
	doPage : function (formName, pageNo)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSearch : function (formName)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doSort : function (obj, sortColumn)
	{
		var formElem = document.forms[ "DymicMenuManager_SearchForm" ];
	    formElem.elements[ "sortColumn" ].value = sortColumn;
	    if( obj.ch % 2 == 0 ) {
			formElem.elements[ "sortMethod" ].value = "ASC";
	        obj.ch = 1;
	    }
	    else {
	        formElem.elements[ "sortMethod" ].value = "DESC";
	        obj.ch = 0;
	    }
		
		this.m_selectRowIndex = 0;
		this.doRetrieve();
	},
	doDefaultSelect : function ()
	{
		if( document.getElementById('DymicMenuManager[' + this.m_selectRowIndex + '].checkRow') ) {
			document.getElementById('DymicMenuManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
			
			var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		
			param += "menuId=";
			param += document.getElementById('DymicMenuManager[' + this.m_selectRowIndex + '].menuId').value;
			
			this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/detailForAjax.cust", param, false, {success: function(data) { aDymicMenuManager.doSelectHandler(data); }});
		}
	},
	doSelect : function (obj)
	{
		var rowSeq = 0;
	    if(obj.nodeName=="SPAN") {
	        rowSeq = obj.parentNode.parentNode.getAttribute("ch");
	    }
	    else if(obj.nodeName=="TD") {
	        rowSeq = obj.parentNode.getAttribute("ch");
	    }
	    else if(obj.nodeName=="TR") {
	        rowSeq = obj.getAttribute("ch");
	    }
		
		this.m_selectRowIndex = rowSeq;
		
	    this.m_checkBox.unChkAll( document.getElementById("DymicMenuManager_ListForm") );
	    document.getElementById('DymicMenuManager[' + rowSeq + '].checkRow').checked = true;
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		param += "menuId=";
		param += document.getElementById('DymicMenuManager[' + rowSeq + '].menuId').value;
			
		this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/detailForAjax.cust", param, false, {success: function(data) { aDymicMenuManager.doSelectHandler(data); }});
	},
	doSelectHandler : function(resultObject)
	{
		this.m_utility.setFormDataFromXML(this.m_dataStructure, "DymicMenuManager", resultObject);
		
	
		document.getElementById("DymicMenuManager_menuId").readOnly = true;
		
		document.getElementById("DymicMenuManager_EditFormPanel").style.display = '';
	},
	doCreate : function() 
	{
		this.m_utility.initFormData(this.m_dataStructure, "DymicMenuManager");
		
		var propertyTabs = $("#DymicMenuManager_propertyTabs").tabs();
		propertyTabs.tabs('select', 0);
	
		document.getElementById("DymicMenuManager_menuId").readOnly = true;
		document.getElementById("DymicMenuManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var isCreate = document.getElementById("DymicMenuManager_isCreate").value;
		var form = document.getElementById("DymicMenuManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		var param = this.m_utility.getFormData(this.m_dataStructure, form);
		param += "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/addForAjax.cust", param, false, {
				success: function(data){
					aDymicMenuManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/updateForAjax.cust", param, false, {
				success: function(data){
					aDymicMenuManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
		}
	},
	doRemove : function () {
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("DymicMenuManager_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('pt.ev.message.remove') );
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById('DymicMenuManager[' + rowCntArray[i] + '].menuId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/removeForAjax.cust", param, false, {
				success: function(data){
					aDymicMenuManager.m_selectRowIndex = 0;
					aDymicMenuManager.doRetrieve();
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
				}});
			
		}
	},
	doMoveUp : function () {
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&id=" + document.getElementById("DymicMenuManager_menuId").value + "&toDown=false";
		this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/changeOrderForAjax.cust", param, false, { 
			success: function(data){ 
				aDymicMenuManager.doRetrieve();
				enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
			}});
	},
	doMoveDown : function () {
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&id=" + document.getElementById("DymicMenuManager_menuId").value + "&toDown=true";
		this.m_ajax.send("POST", this.m_contextPath + "/dymicMenu/changeOrderForAjax.cust", param, false, {  
			success: function(data){ 
				aDymicMenuManager.doRetrieve();
				enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') ); 
			}});
	}
	
}


