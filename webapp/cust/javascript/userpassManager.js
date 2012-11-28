
var aUserpassManager = null;
UserpassManager = function(evSecurityCode)
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
UserpassManager.prototype =
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
			{"fieldName":"nmKor", "validation":"Required,MaxLength", "maxLength":"30"}
			,{"fieldName":"userId", "validation":"Required,MaxLength", "maxLength":"30"}
			,{"fieldName":"columnValue", "validation":"Required,MaxLength", "maxLength":"30"}
			,{"fieldName":"gradeCd", "validation":"", "maxLength":"50"}
			,{"fieldName":"emailAddr", "validation":"MaxLength", "maxLength":"20"}
			,{"fieldName":"homeTel", "validation":"MaxLength", "maxLength":"16"}
			,{"fieldName":"homeZip", "validation":""}
			,{"fieldName":"homeAddr1", "validation":"MaxLength", "maxLength":"20"}
			,{"fieldName":"homeAddr2", "validation":"MaxLength", "maxLength":"20"}
			,{"fieldName":"birthYmd", "validation":""}
			,{"fieldName":"intro", "validation":"MaxLength", "maxLength":"200"}
			,{"fieldName":"sexFlag", "validation":""}
			,{"fieldName":"accessFlag", "validation":""}
			,{"fieldName":"regDatim", "validation":""}
			,{"fieldName":"guide", "validation":""}
			
		]
		
		$(function() {
			$("#UserpassManager_mainTabs").tabs();
		});
		$(function() {
			$("#UserpassManager_propertyTabs").tabs();
		});
		
		$.datepicker.setDefaults({
			showMonthAfterYear: true,
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			showAnim : 'slideDown',
			dateFormat : 'yy-mm-dd'
		});
		
		
		$('#UserpassManager_birthYmd').datepicker();	
		
	},
	initSearch : function () {
		var formElem = document.forms[ "UserpassManager_SearchForm" ];
		formElem.elements[ "pageNo" ].value = 1;
		//formElem.elements[ "pageSize" ].value = 10;
	},
	doSelectTab : function(tabId) {
		this.m_selectRowIndex = 0;
		switch(tabId) {
		case 0 :
			aUserpassManager.doRetrieve(0);
			break;
		case 1 :
			aUserpassManager.doRetrieve(1); 
			break;
		}
	},
	doRetrieve : function (tabId) {
		this.doCreate();
		
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var formElem = null;
		if( tabId == 0 ) {
			formElem = document.forms[ "UserpassManager_SearchForm" ];
		    for (var i=0; i < formElem.elements.length; i++) {
				var tmp = formElem.elements[ i ].value;
				if( tmp.indexOf("*") != 0 ) {
					param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
				}
		    }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/listForAjax.cust", param, false, {success: function(data) { aUserpassManager.doRetrieveHandler(data); }});
		}
		else {
			formElem = document.forms[ "UserpassNewManager_SearchForm" ];
		    for (var i=0; i < formElem.elements.length; i++) {
				var tmp = formElem.elements[ i ].value;
				if( tmp.indexOf("*") != 0 ) {
					param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
				}
		    }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/listNewForAjax.cust", param, false, {success: function(data) { aUserpassManager.doRetrieveNewHandler(data); }});
		}
	},
	doRetrieveHandler : function( resultObject ) {
		
		this.m_utility.setListData2(
			"UserpassManager",
			new Array('userId'),
			new Array('nmKor', 'gradeCd', 'isActive', 'mobileTel'),
			new Function( "aUserpassManager.doSelect(this)" ),
			this.m_contextPath,
			resultObject);
				
		if(resultObject.Data.length != 0) {
			this.doDefaultSelect(0); 
		} 
	},
	doRetrieveNewHandler : function( resultObject ) {
		
		this.m_utility.setListData2(
			"UserpassNewManager",
			new Array('userId'),
			new Array('nmKor', 'regDatim', 'sexFlag', 'age', 'mobileTel'),
			new Function( "aUserpassManager.doSelect(this)" ),
			this.m_contextPath,
			resultObject);
				
		if(resultObject.Data.length != 0) {
			this.doDefaultSelect(1); 
		}
	},
	doPage : function (formName, pageNo)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.m_selectRowIndex = 0;
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		this.doRetrieve( mainTabs.tabs('option', 'selected') );
	},
	doSearch : function (formName)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.m_selectRowIndex = 0;
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		this.doRetrieve( mainTabs.tabs('option', 'selected') );
	},
	doSort : function (obj, sortColumn)
	{
		var formElem = null;
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		var mainSelectedTabId = mainTabs.tabs('option', 'selected');
		if( mainSelectedTabId == 0 ) {
			formElem = document.forms[ "UserpassManager_SearchForm" ];
		}
		else {
			formElem = document.forms[ "UserpassNewManager_SearchForm" ];
		}
		
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
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		this.doRetrieve( mainTabs.tabs('option', 'selected') );
	},
	doDefaultSelect : function (tabId)
	{
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&userId=";
		if( tabId == 0 ) {
			if( document.getElementById('UserpassManager[' + this.m_selectRowIndex + '].checkRow') ) {
				document.getElementById('UserpassManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
				param += document.getElementById('UserpassManager[' + this.m_selectRowIndex + '].userId').value;
			}
		}
		else {
			if( document.getElementById('UserpassNewManager[' + this.m_selectRowIndex + '].checkRow') ) {
				document.getElementById('UserpassNewManager[' + this.m_selectRowIndex + '].checkRow').checked = true;
				param += document.getElementById('UserpassNewManager[' + this.m_selectRowIndex + '].userId').value;
			}
		}
		
		this.m_ajax.send("POST", this.m_contextPath + "/userpass/detailForAjax.cust", param, false, {success: function(data) { aUserpassManager.doSelectHandler(data); }});
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
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		var mainSelectedTabId = mainTabs.tabs('option', 'selected');
		if( mainSelectedTabId == 0 ) {
		    this.m_checkBox.unChkAll( document.getElementById("UserpassManager_ListForm") );
		    document.getElementById('UserpassManager[' + rowSeq + '].checkRow').checked = true;
			param += "userId=";
			param += document.getElementById('UserpassManager[' + rowSeq + '].userId').value;
		}
		else {
			this.m_checkBox.unChkAll( document.getElementById("UserpassNewManager_ListForm") );
		    document.getElementById('UserpassNewManager[' + rowSeq + '].checkRow').checked = true;
			param += "userId=";
			param += document.getElementById('UserpassNewManager[' + rowSeq + '].userId').value;
		}
			
		this.m_ajax.send("POST", this.m_contextPath + "/userpass/detailForAjax.cust", param, false, {success: function(data) { aUserpassManager.doSelectHandler(data); }});
	},
	doSelectHandler : function(resultObject)
	{
		//this.m_utility.setFormDataFromXML(this.m_dataStructure, "UserpassManager", resultObject);
		document.getElementById("UserpassManager_isCreate").value = "false";
		document.getElementById("UserpassManager_userId").value = portalPage.getAjax().retrieveElementValue("userId", resultObject);
		document.getElementById("UserpassManager_nmKor").value = portalPage.getAjax().retrieveElementValue("nmKor", resultObject);
		document.getElementById("UserpassManager_gradeCd").value = portalPage.getAjax().retrieveElementValue("gradeCd", resultObject);
		document.getElementById("UserpassManager_birthYmd").value = portalPage.getAjax().retrieveElementValue("birthYmd", resultObject);
		var emailAddr = portalPage.getAjax().retrieveElementValue("emailAddr", resultObject);
		var atPos = emailAddr.indexOf("@");
		document.getElementById("UserpassManager_emailAddr1").value = emailAddr.substring(0, atPos);
		document.getElementById("UserpassManager_emailAddr2").value = emailAddr.substring(atPos+1);
		
		var homeTel = portalPage.getAjax().retrieveElementValue("homeTel", resultObject);
		if( homeTel != null ) {
			var homeTelArray = homeTel.split("-")
			if( homeTelArray[0] )
				document.getElementById("UserpassManager_homeTel1").value = homeTelArray[0];
			if( homeTelArray[1] )
				document.getElementById("UserpassManager_homeTel2").value = homeTelArray[1];
			if( homeTelArray[2] )
				document.getElementById("UserpassManager_homeTel3").value = homeTelArray[2];
		}
		else {
			document.getElementById("UserpassManager_homeTel1").value = "";
			document.getElementById("UserpassManager_homeTel2").value = "";
			document.getElementById("UserpassManager_homeTel3").value = "";
		}
		var homeZip = portalPage.getAjax().retrieveElementValue("homeZip", resultObject);
		document.getElementById("UserpassManager_homeZip1").value = homeZip.substring(0, 3);
		document.getElementById("UserpassManager_homeZip2").value = homeZip.substring(3);
		document.getElementById("UserpassManager_homeAddr1").value = portalPage.getAjax().retrieveElementValue("homeAddr1", resultObject);
		document.getElementById("UserpassManager_homeAddr2").value = portalPage.getAjax().retrieveElementValue("homeAddr2", resultObject);
		document.getElementById("UserpassManager_regDatim").value = portalPage.getAjax().retrieveElementValue("regDatim", resultObject);
		
		var sexFlag = portalPage.getAjax().retrieveElementValue("sexFlag", resultObject);
		if( sexFlag == 1 ) {
			document.getElementById("UserpassManager_sexFlag_male").checked = true;
		}
		else if( sexFlag == 2 ) {
			document.getElementById("UserpassManager_sexFlag_female").checked = true;
		}
		
		document.getElementById("UserpassManager_intro").value = portalPage.getAjax().retrieveElementValue("intro", resultObject);
		document.getElementById("UserpassManager_guide").value = portalPage.getAjax().retrieveElementValue("guide", resultObject);
		
		var accessFlag = portalPage.getAjax().retrieveElementValue("accessFlag", resultObject);
		if( accessFlag == 0 ) {
			document.getElementById("UserpassManager_accessFlag_0").checked = true;
		}
		else {
			document.getElementById("UserpassManager_accessFlag_1").checked = true;
		}
	
		document.getElementById("UserpassManager_userId").readOnly = true;
		document.getElementById("UserpassManager_regDatim").readOnly = true;
		
		document.getElementById("UserpassManager_EditFormPanel").style.display = '';
	},
	doCreate : function() 
	{
		//this.m_utility.initFormData(this.m_dataStructure, "UserpassManager");
		document.getElementById("UserpassManager_userId").value = "";
		document.getElementById("UserpassManager_nmKor").value = "";
		document.getElementById("UserpassManager_gradeCd").value = "beliver";
		document.getElementById("UserpassManager_birthYmd").value = "";
		document.getElementById("UserpassManager_emailAddr1").value = "";
		document.getElementById("UserpassManager_emailAddr2").value = "";
		
		document.getElementById("UserpassManager_homeTel1").value = "";
		document.getElementById("UserpassManager_homeTel2").value = "";
		document.getElementById("UserpassManager_homeTel3").value = "";
		document.getElementById("UserpassManager_homeZip1").value = "";
		document.getElementById("UserpassManager_homeZip2").value = "";
		document.getElementById("UserpassManager_homeAddr1").value = "";
		document.getElementById("UserpassManager_homeAddr2").value = "";
		document.getElementById("UserpassManager_regDatim").value = "";
		
		document.getElementById("UserpassManager_sexFlag_male").checked = true;
		document.getElementById("UserpassManager_accessFlag_1").checked = true;
	
		document.getElementById("UserpassManager_userId").readOnly = false;
		document.getElementById("UserpassManager_EditFormPanel").style.display = '';
	},
	doUpdate : function (forDetail)
	{
		var elm = null;
		var val = null;
		var param = "evSecurityCode=" + this.m_evSecurityCode + "&";
		var isCreate = document.getElementById("UserpassManager_isCreate").value;
		var form = document.getElementById("UserpassManager_EditForm");

		var isValid = this.m_validator.validate(this.m_dataStructure, form);
		if( isValid == false ) return;
		
		var password = document.getElementById("UserpassManager_columnValue").value;
		var confirmPassword = document.getElementById("UserpassManager_columnValue2").value;
		if( password != null && password != confirmPassword ) {
			alert( portalPage.getMessageResource('pt.ev.errors.password') );
			document.getElementById("UserpassManager_columnValue").focus();
			return;
		}
		
		//var param = this.m_utility.getFormData(this.m_dataStructure, form);
		param = "userId=" + document.getElementById("UserpassManager_userId").value;
		param += "&nmKor=" + encodeURIComponent( document.getElementById("UserpassManager_nmKor").value );
		param += "&gradeCd=" + document.getElementById("UserpassManager_gradeCd").value;
		param += "&emailAddr=" + document.getElementById("UserpassManager_emailAddr1").value + "@" +
		 						 document.getElementById("UserpassManager_emailAddr2").value;
		param += "&homeTel=" + document.getElementById("UserpassManager_homeTel1").value + "-" +
								document.getElementById("UserpassManager_homeTel2").value + "-" +
								document.getElementById("UserpassManager_homeTel3").value;
		param += "&homeZip=" + document.getElementById("UserpassManager_homeZip1").value + document.getElementById("UserpassManager_homeZip2").value;
		param += "&homeAddr1=" + encodeURIComponent( document.getElementById("UserpassManager_homeAddr1").value );
		param += "&homeAddr2=" + encodeURIComponent( document.getElementById("UserpassManager_homeAddr2").value );
		param += "&birthYmd=" + document.getElementById("UserpassManager_birthYmd").value;
		param += "&intro=" + encodeURIComponent( document.getElementById("UserpassManager_intro").value );
		param += "&guide=" + encodeURIComponent( document.getElementById("UserpassManager_guide").value );
		param += "&sexFlag=" + ((document.getElementById("UserpassManager_sexFlag_male").checked == true) ? document.getElementById("UserpassManager_sexFlag_male").value : document.getElementById("UserpassManager_sexFlag_female").value);
		param += "&accessFlag=" + ((document.getElementById("UserpassManager_accessFlag_0").checked == true) ? document.getElementById("UserpassManager_accessFlag_0").value : document.getElementById("UserpassManager_accessFlag_1").value);
		
		//alert("param=" + param);
	
		if( isCreate == "true" ) {
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/addForAjax.cust", param, false, {
				success: function(data){
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
					var mainTabs = $("#UserpassManager_mainTabs").tabs();
					aUserpassManager.doRetrieve( mainTabs.tabs('option', 'selected') );
					
				}});
		}
		else {
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/updateForAjax.cust", param, false, {
				success: function(data){
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
					var mainTabs = $("#UserpassManager_mainTabs").tabs();
					aUserpassManager.doRetrieve( mainTabs.tabs('option', 'selected') );
				}});
		}
	},
	doRemove : function () {
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		var tabId = mainTabs.tabs('option', 'selected');
		var mngName = null;
		if( tabId == 0) {
			mngName = "UserpassManager";
		}
		else {
			mngName = "UserpassNewManager";
		}
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById(mngName + "_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm( portalPage.getMessageResource('pt.ev.message.remove') );
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById(mngName + '[' + rowCntArray[i] + '].userId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/removeForAjax.cust", param, false, {
				success: function(data){
					aUserpassManager.m_selectRowIndex = 0;
					aUserpassManager.doRetrieve( tabId );
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
				}});
			
		}
	},
	doAccessDeny : function () {
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		var tabId = mainTabs.tabs('option', 'selected');
		var mngName = null;
		if( tabId == 0) {
			mngName = "UserpassManager";
		}
		else {
			mngName = "UserpassNewManager";
		}
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById(mngName + "_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm("선택한 사용자를 접속차단 하시겠습니까 ?");
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById(mngName + '[' + rowCntArray[i] + '].userId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/accessDenyForAjax.cust", param, false, {
				success: function(data){
					aUserpassManager.m_selectRowIndex = 0;
					aUserpassManager.doRetrieve( tabId );
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
			
		}
	},
	doApprove : function () {
		var mainTabs = $("#UserpassManager_mainTabs").tabs();
		var tabId = mainTabs.tabs('option', 'selected');
		var mngName = null;
		if( tabId == 0) {
			mngName = "UserpassManager";
		}
		else {
			mngName = "UserpassNewManager";
		}
		var rowCnts = this.m_checkBox.getCheckedElements( document.getElementById(mngName + "_ListForm") );
		if( rowCnts == "" ) return;

		var ret = confirm("선택한 사용자를 새 가족으로 승인 하시겠습니까 ?");
	    if( ret == true ) {
	        var formData = "";
	        var rowCntArray = rowCnts.split(",");
			
			var param = "evSecurityCode=" + this.m_evSecurityCode;
	        for(i=0; i<rowCntArray.length; i++) {
				param += "&pks=";
				param += 
					document.getElementById(mngName + '[' + rowCntArray[i] + '].userId').value;
	        }
			
			this.m_ajax.send("POST", this.m_contextPath + "/userpass/approveForAjax.cust", param, false, {
				success: function(data){
					aUserpassManager.m_selectRowIndex = 0;
					aUserpassManager.doRetrieve( tabId );
					enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
				}});
			
		}
	}
	
}

