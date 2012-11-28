
var aTodayWordManager = null;
TodayWordManager = function(evSecurityCode)
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
TodayWordManager.prototype =
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
			{"fieldName":"wordId", "validation":""}
			,{"fieldName":"title", "validation":"Required,MaxLength", "maxLength":"80"}
			,{"fieldName":"content", "validation":"Required,MaxLength", "maxLength":"300"}
			,{"fieldName":"startDate", "validation":""}
			
		]
		
		$(function() {
			$("#TodayWordManager_mainTabs").tabs();
		});
		$(function() {
			$("#TodayWordManager_propertyTabs").tabs();
		});
		
		$.datepicker.setDefaults({
			showMonthAfterYear: true,
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			showAnim : 'slideDown',
			dateFormat : 'yy-mm-dd'
		});
		
		
		$('#TodayWordManager_startDate').datepicker();	
		
	},
	initSearch : function () {
		var formElem = document.forms[ "TodayWordManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
	},
	doRetrieve : function () {
		this.doCreate();
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = document.forms[ "TodayWordManager_SearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		
		this.m_ajax.send("POST", this.m_contextPath + "/todayWord/listForAjax.cust", param, false, {success: function(data) { aTodayWordManager.doRetrieveHandler(data); }});
	},
	doRetrieveHandler : function( resultObject ) {
		
		this.m_utility.setListData2(
			"TodayWordManager",
			new Array('wordId'),
			new Array('title', 'startDate'),
			new Function( "aTodayWordManager.doSelect(this)" ),
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
		var formElem = document.forms[ "TodayWordManager_SearchForm" ];
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
		if( document.getElementById('TodayWordManager[' + this.m_selectRowIndex + '].checkRow') ) {
			document.getElementById('TodayWordManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
			
			var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		
			param += "wordId=";
			param += document.getElementById('TodayWordManager[' + this.m_selectRowIndex + '].wordId').value;
			
			this.m_ajax.send("POST", this.m_contextPath + "/todayWord/detailForAjax.cust", param, false, {success: function(data) { aTodayWordManager.doSelectHandler(data); }});
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
		
	    this.m_checkBox.unChkAll( document.getElementById("TodayWordManager_ListForm") );
	    document.getElementById('TodayWordManager[' + rowSeq + '].checkRow').checked = true;
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		param += "wordId=";
		param += document.getElementById('TodayWordManager[' + rowSeq + '].wordId').value;
			
		this.m_ajax.send("POST", this.m_contextPath + "/todayWord/detailForAjax.cust", param, false, {success: function(data) { aTodayWordManager.doSelectHandler(data); }});
	},
	doSelectHandler : function(resultObject)
	{
		this.m_utility.setFormDataFromXML(this.m_dataStructure, "TodayWordManager", resultObject);
		
	
		document.getElementById("TodayWordManager_wordId").readOnly = true;
		
		document.getElementById("TodayWordManager_EditFormPanel").style.display = '';
	},
	doCreate : function() 
	{
		this.m_utility.initFormData(this.m_dataStructure, "TodayWordManager");
		
		var propertyTabs = $("#TodayWordManager_propertyTabs").tabs();
		propertyTabs.tabs('select', 0);
	
		document.getElementById("TodayWordManager_wordId").readOnly = true;
		document.getElementById("TodayWordManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var isCreate = document.getElementById("TodayWordManager_isCreate").value;
		var form = document.getElementById("TodayWordManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		var param = this.m_utility.getFormData(this.m_dataStructure, form);
		param += "evSecurityCode=" + this.m_evSecurityCode + "&";
	
		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/todayWord/addForAjax.cust", param, false, {
				success: function(data){
					aTodayWordManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/todayWord/updateForAjax.cust", param, false, {
				success: function(data){
					aTodayWordManager.doRetrieve();
					
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
		}
	},
	doRemove : function () {
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById("TodayWordManager_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('pt.ev.message.remove') );
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById('TodayWordManager[' + rowCntArray[i] + '].wordId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/todayWord/removeForAjax.cust", param, false, {
				success: function(data){
					aTodayWordManager.m_selectRowIndex = 0;
					aTodayWordManager.doRetrieve();
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
				}});
			
		}
	}
	
}


