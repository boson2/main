var portletManager = null;
enview.portal.PortletManager = function()
{
	
}
enview.portal.PortletManager.prototype =
{
	getPortletInfo: function ()
	{
		var portletInfo = null;
		var param = "path=" + portalPage.getPath();
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/getPortletInfoForAjax.face", param, false, {
			success: function(data){
				portletInfo = data;
			}});
			
		return portletInfo;
	},
	savePortlet : function (param)
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/saveFragmentInfoForAjax.face", param, false, {
			success: function(data){
				portalPage.m_modified = false;
				location.reload(false);
			}});
	},
	saveSelectPortlet : function (param)
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/saveSelectPortletForAjax.face", param, false, {
			success: function(data){
				portalPage.m_modified = false;
				location.reload(false);
			}});
	},
	getPortletSelector: function ()
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/listForPortletSelector.face", "", false, {
			success: function(data){
				var dialogTag = document.createElement("div");
				dialogTag.id = "PortletSelectorDialog";
				dialogTag.title = portalPage.getMessageResource("pt.ev.portletSelectorName");
				document.body.appendChild( dialogTag );
				document.getElementById("PortletSelectorDialog").innerHTML = data;
				portalPortletSelector = new enview.portal.PortletSelector();
				portalPortletSelector.init();
			}});
		
		return portalPortletSelector;
	},
	getLayoutPortletEditor: function ()
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/listForPortletEditor.face?type=layout", "", false, {
			success: function(data){
				portalLayoutPortletEditor = new enview.portal.LayoutPortletEditor(data);
				portalLayoutPortletEditor.init();
			}});
		return portalLayoutPortletEditor;
	},
	getPortletEditor: function ()
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/listForPortletEditor.face?type=portlet", "", false, {
			success: function(data){
				portalPortletEditor = new enview.portal.PortletEditor(data);
			}});
		return portalPortletEditor;
	},
	getMyPagetEditor: function ()
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/listForPortletEditor.face?type=myPage", "", false, {
			success: function(data){
				var dialogTag = document.createElement("div");
				dialogTag.id = "MyPageEditorDialog";
				dialogTag.title = "MyPage Editor"; //portalPage.getMessageResource("pt.ev.myPageEditorName");
				//alert("dialogTag.title=" + dialogTag.title);
				document.body.appendChild( dialogTag );
				document.getElementById("MyPageEditorDialog").innerHTML = data;
				portalMyPageEditor = new enview.portal.MyPageEditor();
				portalMyPageEditor.init();
			}});
		return portalMyPageEditor;
	},
	getGroupPagetEditor: function ()
	{
		portalPage.getAjax().send("POST", portalPage.getContextPath() + "/page/listForPortletEditor.face?type=groupPage", "", false, {
			success: function(data){
				var dialogTag = document.createElement("div");
				dialogTag.id = "GroupPageEditorDialog";
				dialogTag.title = "GroupPage Editor"; //portalPage.getMessageResource("pt.ev.myPageEditorName");
				//alert("dialogTag.title=" + dialogTag.title);
				document.body.appendChild( dialogTag );
				document.getElementById("GroupPageEditorDialog").innerHTML = data;
				portalGroupPageEditor = new enview.portal.GroupPageEditor();
				portalGroupPageEditor.init();
			}});
		return portalGroupPageEditor;
	}
}

var portalPortletSelector = null;
enview.portal.PortletSelector = function()
{
	this.m_checkBoxUtil = new enview.util.CheckBoxUtil();
	this.m_ajax = new enview.util.Ajax();
	this.m_ajax.setContextPath( portalPage.getContextPath() );
	this.m_utility = new enview.util.Utility();
	this.m_contextPath = this.m_utility.getContextPath();
	
	$("#PortletSelectorDialog").dialog({
			autoOpen: false,
			resizable: false,
			width:440, 
			//height:400,
			modal: false,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				},
				"Apply": function() {
					//$(this).dialog('close');
					portalPortletSelector.doApply();
				}
			}
		});
	
}
enview.portal.PortletSelector.prototype =
{
	m_checkBoxUtil: null,
	m_domElement: null,
	m_ajax: null,
	m_object: null,
	m_selectPortletMap : null,
	m_selectPortletArray : null,
	m_currentPortletMap : null,
	
	init: function ()
	{
		this.m_selectPortletMap = new Map();
		
		var data = portalPage.getPortletManager().getPortletInfo();
		if( data.column != null && data.column.length > 0 ) {
			for(var idx=0; idx<data.column.length; idx++) {
				var column = data.column[idx];
				if( column.row.length > 0 ) {
					for(var idy=0; idy<column.row.length; idy++) {
						var row = column.row[idy];
						if( row.attribute.name != null &&  row.attribute.name.length > 0 ) {
							this.m_selectPortletMap.put(row.attribute.name, "T");
						}
					}
				}
			}
		}
		
		this.doPortletRetrieve();
	},
	doShow: function (obj)
	{
		this.m_object = obj;
		//this.m_domElement.style.display = "";
		$('#PortletSelectorDialog').dialog('open');
	},
	
	doHide: function ()
	{
		$('#PortletSelectorDialog').dialog('close');
	},
	
	getDomElement: function ()
	{
		return this.m_domElement;
	},
	
	changeParentId: function (obj)
	{
		var el = document.getElementById("PortletSelectorParentId");
		el.value = obj.options[obj.selectedIndex].value;
	},
	doPortletRetrieve: function ()
	{
		var param = "";
		var formElem = document.forms[ "PortletSelectorSearchForm" ];
	    for (var i=0; i < formElem.elements.length; i++) {
			var tmp = formElem.elements[ i ].value;
			if( tmp.indexOf("*") != 0 ) {
				param += formElem.elements[ i ].name + "=" + encodeURIComponent( tmp ) + "&";
			}
	    }
		
		this.m_ajax.send("POST", this.m_contextPath + "/page/listPortletForAjax.face", param, false, {success: function(data) { portalPortletSelector.doPortletRetrieveHandler(data); }});
	},
	doPortletRetrieveHandler: function (data)
	{
		var bodyElem = document.getElementById('PortletSelectorListBody');
	    var tr_tag = null;
		var div_tag = null;
	    var td_tag = null;
		var chkUtil = new enview.util.CheckBoxUtil();
		var listForm = document.getElementById("PortletSelectorListForm");
		if( listForm != null ) {
			chkUtil.unChkAll( listForm );
		}
		for(; bodyElem.hasChildNodes(); )
			bodyElem.removeChild( bodyElem.childNodes[0] );

		var formElem = document.getElementById("PortletSelectorSearchForm");
		var uniqueId = document.getElementById("PortletSelector_categoryType").value;
		var key = "";
		
		for(i=0; i<data.Data.length; i++) {
			tr_tag = document.createElement('tr');
			tr_tag.id = "PortletSelector:Portlet[" + i + "]";
			//tr_tag.onmouseover = new Function( "portletSelector.m_checkBoxUtil.rowOver(this)" );
			//tr_tag.onmouseout = new Function( "portletSelector.m_checkBoxUtil.rowOut(this)" );
			tr_tag.setAttribute("ch", i);
			if( i % 4 == 0 ) {
				tr_tag.setAttribute("class", "even");
				tr_tag.setAttribute("className", "even");
			}
			else {
				tr_tag.setAttribute("class", "odd");
				tr_tag.setAttribute("className", "odd");
			}
			
			td_tag = document.createElement('td');
			td_tag.align = "center";
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			
			key = data.Data[ i ].appName + "::" + data.Data[ i ].name;
			//alert("id=" + data.Data[ i ].id + ", contains=" + portletIdMap.containsKey(data.Data[ i ].id));
			if( this.m_selectPortletMap.containsKey(key) ) {
				td_tag.innerHTML = "<input type=\"checkbox\" id=\"PortletSelector[" + i + "].checkRow\" ch=\"" + i + "\" checked=\"true\" class=\"webcheckbox\" onclick=\"portalPortletSelector.doChangeStatus(this);\"/>";
			}
			else {
				td_tag.innerHTML = "<input type=\"checkbox\" id=\"PortletSelector[" + i + "].checkRow\" ch=\"" + i + "\" class=\"webcheckbox\" onclick=\"portalPortletSelector.doChangeStatus(this);\"/>";
			}
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletId\" value=\"" + data.Data[ i ].id + "\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletAppName\" value=\"" + data.Data[ i ].appName + "\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletName\" value=\"" + data.Data[ i ].name + "\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletTitle\" value=\"" + data.Data[ i ].displayName + "\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].title\" value=\"" + data.Data[ i ].description + "\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].uniqueId\" value=\"" + uniqueId + "\"/>";
			tr_tag.appendChild( td_tag );
			
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.onclick = new Function( "portalPortletSelector.doPortletSelect(" + i + ")" );
			td_tag.align = "center";
			td_tag.innerHTML = "<img src='" + portalPage.getContextPath() + "/images/portlets/" + data.Data[ i ].icon + "'>";
			tr_tag.appendChild( td_tag );
			
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.onclick = new Function( "portalPortletSelector.doPortletSelect(" + i + ")" );
			td_tag.align = "left";
			td_tag.innerHTML = "<span style=\"width:100%;\" class=\"webgridrowlabel\" id=\"PortletSelector[" + i + "].name.label\" dragCopy='true' " + 
								" portletId='" + data.Data[ i ].id + "'" +
								" portletAppName='" + data.Data[ i ].appName + "'" +  
								" portletName='" + data.Data[ i ].name + "'" +
								" portletTitle='" + data.Data[ i ].displayName + "'" + 
								" uniqueId='" + uniqueId + "'" +
								" addValue1='" + data.Data[ i ].addValue1 + "'" + 
								" addValue2='" + data.Data[ i ].addValue2 + "'" + 
								" addValue3='" + data.Data[ i ].addValue3 + "'" + 
								" title='" + data.Data[ i ].description + "'>" + 
								data.Data[ i ].displayName + 
								"</span>";
			td_tag.setAttribute("style", "cursor: pointer;");
			td_tag.style.cursor = "pointer";
			tr_tag.appendChild( td_tag );
			
			i++;
			if( data.Data[i] ) {
				td_tag = document.createElement('td');
				td_tag.align = "center";
				td_tag.setAttribute("class", "webgridbody");
				td_tag.setAttribute("className", "webgridbody");
				
				key = data.Data[ i ].appName + "::" + data.Data[ i ].name;
				if( this.m_selectPortletMap.containsKey(key) ) {
					td_tag.innerHTML = "<input type=\"checkbox\" id=\"PortletSelector[" + i + "].checkRow\" ch=\"" + i + "\" checked=\"true\" class=\"webcheckbox\" onclick=\"portalPortletSelector.doChangeStatus(this);\"/>";
				}
				else {
					td_tag.innerHTML = "<input type=\"checkbox\" id=\"PortletSelector[" + i + "].checkRow\" ch=\"" + i + "\" class=\"webcheckbox\" onclick=\"portalPortletSelector.doChangeStatus(this);\"/>";
				}
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletId\" value=\"" + data.Data[ i ].id + "\"/>";
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletAppName\" value=\"" + data.Data[ i ].appName + "\"/>";
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletName\" value=\"" + data.Data[ i ].name + "\"/>";
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].portletTitle\" value=\"" + data.Data[ i ].displayName + "\"/>";
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].title\" value=\"" + data.Data[ i ].description + "\"/>";
				td_tag.innerHTML += "<input type=\"hidden\" id=\"PortletSelector[" + i + "].uniqueId\" value=\"" + uniqueId + "\"/>";
				
				tr_tag.appendChild( td_tag );
				
				td_tag = document.createElement('td');
				td_tag.setAttribute("class", "webgridbody");
				td_tag.setAttribute("className", "webgridbody");
				td_tag.onclick = new Function( "portalPortletSelector.doPortletSelect(" + i + ")" );
				td_tag.align = "center";
				td_tag.innerHTML = "<img src='" + portalPage.getContextPath() + "/images/portlets/" + data.Data[ i ].icon + "'>";
				tr_tag.appendChild( td_tag );
				
				td_tag = document.createElement('td');
				td_tag.setAttribute("class", "webgridbodylast");
				td_tag.setAttribute("className", "webgridbodylast");
				td_tag.onclick = new Function( "portalPortletSelector.doPortletSelect(" + i + ")" );
				td_tag.align = "left";
				td_tag.innerHTML = "<span style=\"width:100%;\" class=\"webgridrowlabel\" id=\"PortletSelector[" + i + "].name.label\" dragCopy='true' " + 
									" portletId='" + data.Data[ i ].id + "'" +
									" portletAppName='" + data.Data[ i ].appName + "'" +  
									" portletName='" + data.Data[ i ].name + "'" +
									" portletTitle='" + data.Data[ i ].displayName + "'" + 
									" uniqueId='" + uniqueId + "'" +
									" addValue1='" + data.Data[ i ].addValue1 + "'" + 
									" addValue2='" + data.Data[ i ].addValue2 + "'" + 
									" addValue3='" + data.Data[ i ].addValue3 + "'" + 
									" title='" + data.Data[ i ].description + "'>" + 
									data.Data[ i ].displayName + 
									"</span>";

				td_tag.setAttribute("style", "cursor: pointer;");
				td_tag.style.cursor = "pointer";
				tr_tag.appendChild( td_tag );
			}
			else {
				td_tag = document.createElement('td');
				td_tag.innerHTML = "&nbsp;";
				tr_tag.appendChild( td_tag );
				td_tag = document.createElement('td');
				td_tag.innerHTML = "&nbsp;";
				tr_tag.appendChild( td_tag );
				td_tag = document.createElement('td');
				td_tag.innerHTML = "&nbsp;";
				tr_tag.appendChild( td_tag );
			}

			bodyElem.appendChild( tr_tag );
		}
		
		if(data.TotalSize > 30) {
			if(data.Data.length == 0) {
				//alert("not found");
				tr_tag = document.createElement('tr');
				tr_tag.setAttribute("class", "row-empty");
				tr_tag.setAttribute("className", "row-empty");
				td_tag = document.createElement('td');
				td_tag.colSpan = "100";
				td_tag.innerHTML = "<span>" + portalPage.getMessageResource("pt.ev.messageNotFoundData") + "</span>";
				tr_tag.appendChild( td_tag );
				bodyElem.appendChild( tr_tag );
			}
			else {
				tr_tag = document.createElement('tr');
				tr_tag.setAttribute("class", "row-empty");
				tr_tag.setAttribute("className", "row-empty");
				td_tag = document.createElement('td');
				td_tag.colSpan = "100";
				td_tag.innerHTML = "<span>" + portalPage.getMessageResourceByParam('pt.ev.message.ResultSize', data.ResultSize) + "<br></span>";
				tr_tag.appendChild( td_tag );
				bodyElem.appendChild( tr_tag );
			}
		
			var formElem = document.forms[ "PortletSelectorSearchForm" ];
			var pageNo = formElem.elements[ "pageNo" ].value;
			var pageSize = formElem.elements[ "pageSize" ].value;
			var pageFunction = formElem.elements[ "pageFunction" ].value;
			var formName = formElem.elements[ "formName" ].value;
			document.getElementById("PORTLETSELECTOR_PAGE_ITERATOR").innerHTML = 
					(new enview.util.PageNavigationUtil()).getPageIteratorHtmlString(pageNo, pageSize, data.TotalSize, formName, pageFunction);
		}

	},
	doPortletPage: function (formName, pageNo)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = pageNo;
		
		this.doPortletRetrieve();
	},

	doPortletSearch: function (formName)
	{
		var formElem = document.forms[ formName ];
	    formElem.elements[ "pageNo" ].value = 1;
		
		this.doPortletRetrieve();
	},

	doPortletSort: function (obj, sortColumn)
	{
		var formElem = document.forms[ "PortletSelectorSearchForm" ];
	    formElem.elements[ "sortColumn" ].value = sortColumn;
	    if( obj.ch % 2 == 0 ) {
			formElem.elements[ "sortMethod" ].value = "ASC";
	        obj.ch = 1;
	    }
	    else {
	        formElem.elements[ "sortMethod" ].value = "DESC";
	        obj.ch = 0;
	    }
		
		this.doPortletRetrieve();
	},
	doPortletSelect: function (idx)
	{
		//alert("idx=" + idx + ", checked=" + document.getElementById('PortletSelector[' + idx + '].checkRow').checked);
		if( document.getElementById('PortletSelector[' + idx + '].checkRow').checked == true ) {
			document.getElementById('PortletSelector[' + idx + '].checkRow').checked = false;
		}
		else {
			document.getElementById('PortletSelector[' + idx + '].checkRow').checked = true;
		}
		this.doChangeStatus( document.getElementById('PortletSelector[' + idx + '].checkRow') );
	},
	doChangeStatus : function(obj)
	{
		var ch = obj.getAttribute("ch");
		var key = document.getElementById('PortletSelector[' + ch + '].portletAppName').value + "::" +
				  document.getElementById('PortletSelector[' + ch + '].portletName').value;
				  
		if( obj.checked == true ) {
			this.m_selectPortletMap.put(key, "T");
		}
		else {
			this.m_selectPortletMap.remove(key);
		}
	},
	doApply : function()
	{
		var param = "path=" + portalPage.getPath() + "&selectPortletIds=";
		var keySet = this.m_selectPortletMap.getKeySet();
		for(var i=0; i<this.m_selectPortletMap.getSize(); i++) {
			if( i > 0 ) param += ",";
			param += keySet[i];
		}
		//alert(param);
		portalPage.getPortletManager().saveSelectPortlet( param);
	}
};

var portalLayoutPortletEditor = null;
enview.portal.LayoutPortletEditor = function(data)
{
	this.m_ajax = new enview.util.Ajax();
	this.m_fragmentPropDialog = document.createElement("div");
	this.m_fragmentPropDialog.id = "LayoutPortletEditorDialog";
	this.m_fragmentPropDialog.title = portalPage.getMessageResource("pt.ev.portal.page.editing.portlet");
	this.m_fragmentPropDialog.innerHTML = data;
	document.body.appendChild( this.m_fragmentPropDialog );
	
	this.m_isSizeFieldSelect = false;
		
	$("#LayoutPortletEditorDialog").dialog({
			autoOpen: false,
			resizable: true,
			width:350, 
			//height:400,
			modal: true,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				},
				/*
				"Remove": function() {
					$(this).dialog('close');
					portalLayoutPortletEditor.remove();
				},
				*/
				"Apply": function() {
					$(this).dialog('close');
					portalLayoutPortletEditor.changeLayout();
				}
			}
		});
}
enview.portal.LayoutPortletEditor.prototype =
{
	m_object : null,
	m_fragmentPropDialog : null,
	m_isChangeLayout : false,
	m_isSizeFieldSelect : false,
	m_attributeMap : null,
	
	init : function()
	{
		//var tmp = "[{k : {\"name\" : \"zzz\", \"attr\" : {\"a\" : 1, \"b\" : 2}}},{k : {\"name\" : \"zzz\", \"attr\" : {\"a\" : 1, \"b\" : 2}}}]";
		//var tmps = eval("(" + tmp + ")");
		//alert(tmps.length);
		this.m_attributeMap = new Map();
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/getLayoutPortletInfoForAjax.face", "", false, {
			success: function(resultObject){
				//alert(resultObject.data.length);
				for(var i=0; i<resultObject.data.length; i++) {
					portalLayoutPortletEditor.m_attributeMap.put(resultObject.data[i].name, resultObject.data[i].attribute);
				}
			}});
	},
	
	show : function(obj)
	{
		//alert("layout type=" + obj.getType());
		this.m_object = obj;
		document.getElementById("LayoutPortletEditor.SELECT_LAYOUT").value = obj.getName();
		//document.getElementById("LayoutPortletEditor.SELECT_ALIGN").value = obj.getAlign();
		document.getElementById("LayoutPortletEditorDialog.Fragment").value = obj.getFragmentId();
		document.getElementById("LayoutPortletEditorDialog.Name").innerHTML = obj.getName();
		if( portalPage.getServletPath() == "/contentonly" ) {
			document.getElementById("LayoutPortletEditor.ACTIONMASK_PANE").style.display = "";
		}
		else {
			document.getElementById("LayoutPortletEditor.ACTIONMASK_PANE").style.display = "none";
		}
		
		var actionMask = obj.getActionMask();
		if( (actionMask & 0x00001000) == 0x00001000 ) {
			document.getElementById("LayoutPortletEditor.ADD_ENABLE").checked = true;
		}
		else {
			document.getElementById("LayoutPortletEditor.ADD_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000100) == 0x00000100 ) {
			document.getElementById("LayoutPortletEditor.EDIT_ENABLE").checked = true;
		}
		else {
			document.getElementById("LayoutPortletEditor.EDIT_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000010) == 0x00000010 ) {
			document.getElementById("LayoutPortletEditor.MOVE_ENABLE").checked = true;
		}
		else {
			document.getElementById("LayoutPortletEditor.MOVE_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000001) == 0x00000001 ) {
			document.getElementById("LayoutPortletEditor.DELETE_ENABLE").checked = true;
		}
		else {
			document.getElementById("LayoutPortletEditor.DELETE_ENABLE").checked = false;
		}

		if(obj.getColumns() != null) {
			var htmlString = "";
			var columns = obj.getColumns();
			if( columns.length == 1 ) {
				htmlString += "<input type='text' maxlength='3' size='3' id='column0' name='column0' used='false' value='100' >"; 
				//htmlString += "<input type='text' maxlength='3' size='3' id='column0' name='column0' used='false' value='100' onselect='javascript:return portalLayoutPortletEditor.onselect(0)' onkeypress='javascript:return portalLayoutPortletEditor.onkeypress(event, this, " + columns.length + ");'>"; 
			}
			else {
				for(var idx=0; idx<columns.length; idx++) {
					htmlString += "<input type='text' maxlength='4' size='3' id='column" + idx + "' name='column" + idx + "' used='false' value='" + columns[idx] + "' >"; 
					//htmlString += "<input type='text' maxlength='4' size='3' id='column" + idx + "' name='column" + idx + "' used='false' value='" + columns[idx] + "' onselect='javascript:return portalLayoutPortletEditor.onselect(" + idx + ")' onkeypress='javascript:return portalLayoutPortletEditor.onkeypress(event, this, " + columns.length + ");'>"; 
				}
			}
			document.getElementById("LayoutPortletEditorDialog.ColumnSize").innerHTML = htmlString;
		}
		
		var unitElem = document.getElementById("LayoutPortletEditor.ColumnSizeUnit");
		if( unitElem != null ) {
			unitElem.value = obj.getColumnUnit();
		}
		
		$('#LayoutPortletEditorDialog').dialog('open');
	},
	selectLayoutChanage : function()
	{
		this.m_isChangeLayout = true;
		if(this.m_object.getColumns() != null) {
			var formElem = document.forms[ "PortalLayoutFragmentEditForm" ];
			var columns = this.m_object.getColumns();
			for(var idx=0; idx<columns.length; idx++) {
			
				//alert("element=" + formElem.elements);    
				formElem.elements["column" + idx].disabled = "true";
			}
		}
	},
	onselect : function(id)
	{
		//alert(id);
		this.m_isSizeFieldSelect = true;
	},
	onkeypress : function(a_event, elm, columnLength)
	{
		//alert("columnLength=" + columnLength + ", value=" + elm.value);
		var keyCode = 0;
		if (document.all)	// it is IE
		{
			keyCode = event.keyCode;
		}
		else {
			keyCode = a_event.which;
		}
		
		var unitElem = document.getElementById("LayoutPortletEditor.ColumnSizeUnit");
		var unit = unitElem.options[ unitElem.selectedIndex ].value;
		//alert( unit);
		var maxUnit = 100;
		if( unit == 'px' ) {
			maxUnit = enview.portal.LAYOUT_MAX_SIZE;
		}
		
		//alert("keyCode=" + keyCode);
		if(keyCode==8 || (48<=keyCode && keyCode<=57) ) {
			elm.setAttribute("used", true);
			
			var value = keyCode-48;
			if( this.m_isSizeFieldSelect == true ) {
				this.m_isSizeFieldSelect = false;
				elm.value = "";
			}
			else {
				value = elm.value + (keyCode-48);
				if( columnLength > 1 && value > maxUnit ) {
					return false;
				}
			}
			
			//alert("value=" + value);
			var changeElemArray = new Array();
			var i = 0;
			var j = 0;
			var usedValue = 0;
			for( i=0; i<columnLength; i++) {
				var columnElm = document.getElementById( "column" + i );
				//alert("usedValue=" + usedValue + ", value=" + value + ", columnElm.value=" + columnElm.value);
				var used = columnElm.getAttribute("used");
				if( used == "false" ) {
					changeElemArray[j++] = columnElm;
				}
				else if( columnElm == elm ) {
					usedValue += Number( value );
				}
				else {
					usedValue += Number( columnElm.value );
				}
				//alert("usedValue=" + usedValue);
			}
			//alert("usedValue=" + usedValue);
			 
			var asignVal = 0;
			if( changeElemArray.length > 0 ) {
				asignVal = Math.floor( (maxUnit - usedValue) / changeElemArray.length );
			}
			else {
				asignVal = maxUnit - usedValue;
			}
			
			//alert("changeElemArray=" + changeElemArray.length + ", usedValue=" + usedValue + ", asignValue=" + asignVal);
			
			if( asignVal < 0 ) {
				alert( portalPage.getMessageResource("pt.ev.messageColumnSizeOver") );
				return false;
			}
			
			for(i=0; i<changeElemArray.length; i++) {
				changeElemArray[i].value = asignVal;
			}
			
			return true;
		}
		else {
			return false;
		}
	},
	changeLayout : function()
	{
		var name = document.getElementById("LayoutPortletEditor.SELECT_LAYOUT").value;
		//alert("name=" + name);
		var align = ""; //document.getElementById("LayoutPortletEditor.SELECT_ALIGN").value;
		var attribute = this.m_attributeMap.get(name);
		var layoutSize = attribute.sizes;
		var unit = document.getElementById("LayoutPortletEditor.ColumnSizeUnit").value;
		//alert(unit);
		var formElem = document.forms[ "PortalLayoutFragmentEditForm" ];
		if(this.m_isChangeLayout==false && this.m_object.getColumns()!=null) {
			layoutSize = "";
			var columns = this.m_object.getColumns();
			for(var idx=0; idx<columns.length; idx++) {
				//columns[idx] = formElem.elements["column" + idx].value;
				if( idx > 0 ) {
					layoutSize += ",";
				}
				layoutSize += formElem.elements["column" + idx].value + unit;
			}
		}
		//alert(layoutSize);
		
		var actionMask = 0;
		if( document.getElementById("LayoutPortletEditor.ADD_ENABLE").checked == true ) {
			actionMask |= 0x00001000;
		}
		if( document.getElementById("LayoutPortletEditor.EDIT_ENABLE").checked == true ) {
			actionMask |= 0x00000100;
		}
		if( document.getElementById("LayoutPortletEditor.MOVE_ENABLE").checked == true ) {
			actionMask |= 0x00000010;
		}
		if( document.getElementById("LayoutPortletEditor.DELETE_ENABLE").checked == true ) {
			actionMask |= 0x00000001;
		}
		
		//alert( attribute.displayName );
		this.m_object.changeLayout(name, layoutSize, align, actionMask, attribute);
	},
	remove : function()
	{
		this.m_object.remove();
	}
}

var portalPortletEditor = null;
enview.portal.PortletEditor = function(data)
{
	this.m_fragmentPropDialog = document.createElement("div");
	this.m_fragmentPropDialog.id = "PortletEditorDialog";
	this.m_fragmentPropDialog.title = portalPage.getMessageResource("pt.ev.portal.page.editing.portlet");
	this.m_fragmentPropDialog.innerHTML = data;
	document.body.appendChild( this.m_fragmentPropDialog );
					
	$("#PortletEditorDialog").dialog({
			autoOpen: false,
			resizable: true,
			width:420, 
			//height:400,
			modal: true,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				},
				"Apply": function() {
					$(this).dialog('close');
					portalPortletEditor.changeDecorator();
				}
			}
		});
}
enview.portal.PortletEditor.prototype =
{
	m_object : null,
	m_fragmentPropDialog : null,
	
	show : function(obj)
	{
		this.m_object = obj;
		
		var contentType = obj.m_contentType;
		//document.getElementById("PortletEditor.SELECT_CONTENT_TYPE").value = contentType;
		//document.getElementById("PortletEditor.SELECT_SYSTEM_CODE").value = (obj.m_preference["SYSTEM_CODE"] != null) ? obj.m_preference["SYSTEM_CODE"] : "";
		document.getElementById("PortletEditor.AUTO_RESIZE").checked = (obj.m_preference["AUTO-RESIZE"] == "true") ? true : false;
		document.getElementById("PortletEditor.SCROLLING").checked = (obj.m_preference["SCROLLING"] == "yes") ? true : false;
		document.getElementById("PortletEditor.TITLE_SHOW").checked = (obj.m_preference["TITLE-SHOW"] != null && obj.m_preference["TITLE-SHOW"] == "true") ? true : false;
		document.getElementById("PortletEditor.CONTENT_TITLE").value = (obj.m_preference["TITLE"] != null) ? obj.m_preference["TITLE"] : "";
		document.getElementById("PortletEditor.CONTENT_CLASS").value = (obj.m_preference["CLASS"] != null) ? obj.m_preference["CLASS"] : "";
		document.getElementById("PortletEditor.WIDTH").value = (obj.m_preference["WIDTH"] != null) ? obj.m_preference["WIDTH"] : "";
		document.getElementById("PortletEditor.HEIGHT").value = (obj.m_preference["HEIGHT"] != null) ? obj.m_preference["HEIGHT"] : "";
		//document.getElementById("PortletEditor.MAX_HEIGHT").value = (obj.m_preference["MAX-HEIGHT"] != null) ? obj.m_preference["MAX-HEIGHT"] : "";
		document.getElementById("PortletEditor.CONTENT_STYLE").value = (obj.m_preference["STYLE"] != null) ? obj.m_preference["STYLE"] : "";
		//document.getElementById("PortletEditor.CONTENT_SOURCE").value = (obj.m_preference["SRC"] != null) ? obj.m_preference["SRC"] : "";
		//document.getElementById("PortletEditor.MORE_SRC").value = (obj.m_preference["MORE_SRC"] != null) ? obj.m_preference["MORE_SRC"] : "";
		//document.getElementById("PortletEditor.MORE_SRC_TARGET").value = (obj.m_preference["MORE_SRC_TARGET"] != null) ? obj.m_preference["MORE_SRC_TARGET"] : "";
		/*
		if( portalPage.getServletPath() == "/contentonly" ) {
			document.getElementById("PortletEditor.ACTIONMASK_PANE").style.display = "";
		}
		else {
			document.getElementById("PortletEditor.ACTIONMASK_PANE").style.display = "none";
		}
		
		var actionMask = obj.getActionMask();
		if( (actionMask & 0x00001000) == 0x00001000 ) {
			document.getElementById("PortletEditor.ADD_ENABLE").checked = true;
		}
		else {
			document.getElementById("PortletEditor.ADD_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000100) == 0x00000100 ) {
			document.getElementById("PortletEditor.EDIT_ENABLE").checked = true;
		}
		else {
			document.getElementById("PortletEditor.EDIT_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000010) == 0x00000010 ) {
			document.getElementById("PortletEditor.MOVE_ENABLE").checked = true;
		}
		else {
			document.getElementById("PortletEditor.MOVE_ENABLE").checked = false;
		}
		if( (actionMask & 0x00000001) == 0x00000001 ) {
			document.getElementById("PortletEditor.DELETE_ENABLE").checked = true;
		}
		else {
			document.getElementById("PortletEditor.DELETE_ENABLE").checked = false;
		}
		*/

		document.getElementById("PortletEditorDialog.Fragment").value = obj.getFragmentId();
		document.getElementById("PortletEditorDialog.Name").innerHTML = obj.getName();
		
		$('#PortletEditorDialog').dialog('open');
	},
	changeDecorator : function()
	{
		//this.m_object.m_preference["SYSTEM_CODE"] = document.getElementById("PortletEditor.SELECT_SYSTEM_CODE").value;
		this.m_object.m_preference["AUTO-RESIZE"] = (document.getElementById("PortletEditor.AUTO_RESIZE").checked == true) ? "true" : "false";
		this.m_object.m_preference["SCROLLING"] = (document.getElementById("PortletEditor.SCROLLING").checked == true) ? "true" : "false";
		this.m_object.m_preference["TITLE-SHOW"] = (document.getElementById("PortletEditor.TITLE_SHOW").checked == true) ? "true" : "false";
		this.m_object.m_preference["TITLE"] = document.getElementById("PortletEditor.CONTENT_TITLE").value;
		this.m_object.m_preference["CLASS"] = document.getElementById("PortletEditor.CONTENT_CLASS").value;
		this.m_object.m_preference["WIDTH"] = document.getElementById("PortletEditor.WIDTH").value;
		this.m_object.m_preference["HEIGHT"] = document.getElementById("PortletEditor.HEIGHT").value;
		//this.m_object.m_preference["MAX-HEIGHT"] = document.getElementById("PortletEditor.MAX_HEIGHT").value;
		this.m_object.m_preference["STYLE"] = document.getElementById("PortletEditor.CONTENT_STYLE").value;
		//this.m_object.m_preference["SRC"] = document.getElementById("PortletEditor.CONTENT_SOURCE").value;
		//this.m_object.m_preference["MORE_SRC"] = document.getElementById("PortletEditor.MORE_SRC").value;
		//this.m_object.m_preference["MORE_SRC_TARGET"] = document.getElementById("PortletEditor.MORE_SRC_TARGET").value;
		/*
		var actionMask = 0;
		if( document.getElementById("PortletEditor.ADD_ENABLE").checked == true ) {
			actionMask |= 0x00001000;
		}
		if( document.getElementById("PortletEditor.EDIT_ENABLE").checked == true ) {
			actionMask |= 0x00000100;
		}
		if( document.getElementById("PortletEditor.MOVE_ENABLE").checked == true ) {
			actionMask |= 0x00000010;
		}
		if( document.getElementById("PortletEditor.DELETE_ENABLE").checked == true ) {
			actionMask |= 0x00000001;
		}
		this.m_object.setActionMask( actionMask );
		*/
		this.m_object.changeDecorator();
	},
	remove : function()
	{
		this.m_object.remove();
	}
}

var portalMyPageEditor = null;
enview.portal.MyPageEditor = function()
{
	this.m_ajax = new enview.util.Ajax();
	this.m_ajax.setContextPath( portalPage.getContextPath() );
	
	$("#MyPageEditorDialog").dialog({
			autoOpen: false,
			resizable: false,
			width:380, 
			//height:400,
			modal: true,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				},
				"Apply": function() {
					portalMyPageEditor.doUpdate();
				}
			}
		});
}
enview.portal.MyPageEditor.prototype =
{
	m_myPageEditDialog : null,
	m_removeHandler : null,
	m_updateHandler : null,
	m_addHandler : null,
	m_invokeHandler : null,
	m_pageId : null,
	m_myPageLength : null,
	m_principalId : null,
	m_contextMenu : null,
	m_currentTabId : 0,
	m_currentMakeType : "1",
	
	init : function()
	{
		this.m_contextMenu = aMyPageMenu = new MyPageMenu(portalPage.getContextPath());
		this.doRetrieveTemplate();
		
		document.getElementById("MyPageEditorDialog_Layout_select").style.display = "";
		document.getElementById("MyPageEditorDialog_Template_select").style.display = "none";
	},
	setPages : function(myPageids)
	{
		//alert(myPageids);
		var pageArray = myPageids.split(",");
		this.m_myPageLength =  0;
		var currentPageId = portalPage.getId();
		for(var i=0; i<pageArray.length; i++) {
			if( pageArray[i] != null && pageArray[i].length > 0 ) {
				this.m_myPageLength++;
			}
			
			if( pageArray[i] != null && pageArray[i].length > 0 && currentPageId != pageArray[i] ) {
				if( document.getElementById("MyTabMenu_" + pageArray[i]) ) {
					document.getElementById("MyTabMenu_" + pageArray[i]).style.display = "none";
				}
			}
			else if( currentPageId == pageArray[i] ) {
				this.m_currentTabId = i;
			}
		}
	},
	setPrincipalId : function(principalId)
	{
		this.m_principalId = principalId;
	},
	onSelectPropertyTab : function(pagePath, tabId)
	{
		if( this.m_currentTabId == tabId ) return;
		
		this.m_currentTabId = tabId;
		location.href = pagePath;
	},
	doShowContextMenu : function(id) {
		var item = document.getElementById("MyTabPageTab_" + id);
		var pos = (new enview.util.Utility()).getAbsolutePosition( item );
		//alert(id + "," + pos.getX() + "," + pos.getY());
		this.m_contextMenu.setItemId( id );
		this.m_contextMenu.show( pos.getX()+3, pos.getY()+25 );
	},
	doShow : function(elm)
	{
		portalMyPageEditor.m_contextMenu.hide();
		
		if( elm != null ) {
			var pos = (new enview.util.Utility()).getAbsolutePosition( elm );
			var left = pos.getX() - 380;
			var top = pos.getY() + 20;
			$('#MyPageEditorDialog').dialog( "option", "position", [left,top] );
		}

		$('#MyPageEditorDialog').dialog('open');
		document.getElementById("MyPageEditorDialog.title").focus();
	},
	doChangeMakeType : function(obj) {
		this.m_currentMakeType = obj.options[ obj.selectedIndex ].value;
		
		if( this.m_currentMakeType == "1" ) {
			document.getElementById("MyPageEditorDialog_Layout_select").style.display = "";
			document.getElementById("MyPageEditorDialog_Template_select").style.display = "none";
		}
		else {
			document.getElementById("MyPageEditorDialog_Layout_select").style.display = "none";
			document.getElementById("MyPageEditorDialog_Template_select").style.display = "";
		}
	},
	doRetrieveTemplate: function ()
	{
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/getMyPageTemplateListForAjax.face", "", false, {success: function(data) { portalMyPageEditor.doRetrieveTemplateHandler(data); }});
	},
	doRetrieveTemplateHandler: function (data)
	{
		var bodyElem = document.getElementById('MyPageEditorDialog_ListBody');
		//alert("bodyElem=" + bodyElem);
	    var tr_tag = null;
	    var td_tag = null;
		for(; bodyElem.hasChildNodes(); )
			bodyElem.removeChild( bodyElem.childNodes[0] );
			
	    var tr_tag = null;
	    var td_tag = null;

		for(i=0; i<data.Data.length; i++) {
			tr_tag = document.createElement('tr');
			if( i % 2 == 0 ) {
				tr_tag.setAttribute("class", "even");
				tr_tag.setAttribute("className", "even");
			}
			else {
				tr_tag.setAttribute("class", "odd");
				tr_tag.setAttribute("className", "odd");
			}
			
			td_tag = document.createElement('td');
			td_tag.align = "center";
			td_tag.width = "20%";
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.innerHTML = "<input type=\"radio\" id=\"MyPageEditorDialog[" + i + "].checkRow\" ch=\"" + i + "\" name=\"templetCheck\" value=\"" + data.Data[ i ].path + "\" class=\"webcheckbox\"/>";
			tr_tag.appendChild( td_tag );
			
			/*
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.align = "left";
			td_tag.innerHTML = "<span>" + data.Data[ i ].title + "</span>";
			tr_tag.appendChild( td_tag );
			*/
			
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.align = "center";
			td_tag.innerHTML = "<img title='" + data.Data[ i ].desc + "' src='" + portalPage.getContextPath() + "/images/template/mypage/" + data.Data[ i ].name + ".jpg'>";
			tr_tag.appendChild( td_tag );

			td_tag.setAttribute("style", "cursor: pointer;");
			td_tag.style.cursor = "pointer";
			//td_tag.appendChild( div_tag );
			tr_tag.appendChild( td_tag );

			bodyElem.appendChild( tr_tag );
		}

		if(data.Data.length == 0) {
			//alert("not found");
			tr_tag = document.createElement('tr');
			tr_tag.setAttribute("class", "row-empty");
			tr_tag.setAttribute("className", "row-empty");
			td_tag = document.createElement('td');
			td_tag.colSpan = "100";
			td_tag.innerHTML = "<span>" + portalPage.getMessageResource("pt.ev.messageNotFoundData") + "</span>";
			tr_tag.appendChild( td_tag );
			bodyElem.appendChild( tr_tag );
		}
	},
	doSelect : function(obj)
	{
		document.getElementById("MyPageEditorDialog.title").value = portalPage.getTitle();
		var layout = portalPage.getLayout();
		var form = document.getElementById("MyPageEditorDialog.EditForm");
		for(var i=0; i<form.elements.length; i++) {
			var field = form.elements[i];
			if(field.type == "radio" && field.name == "layoutCheck") {
				if( field.value == layout ) {
					field.checked = true;
					break;
				}
			}
		}
		document.getElementById("MyPageEditorDialog.isCreate").value = "false";
		document.getElementById("MyPageEditorDialog_changeMakeType").disabled = true;
		document.getElementById("MyPageEditorDialog_Layout_select").style.display = "";
		document.getElementById("MyPageEditorDialog_Template_select").style.display = "none";
		this.doShow(obj);
	},
	doCreate : function(obj)
	{
		document.getElementById("MyPageEditorDialog.title").value = "";
		document.getElementById("MyPageEditorDialog.isCreate").value = "true";
		document.getElementById("MyPageEditorDialog_changeMakeType").disabled = false;
		if( this.m_currentMakeType == "1" ) {
			document.getElementById("MyPageEditorDialog_Layout_select").style.display = "";
			document.getElementById("MyPageEditorDialog_Template_select").style.display = "none";
		}
		else {
			document.getElementById("MyPageEditorDialog_Layout_select").style.display = "none";
			document.getElementById("MyPageEditorDialog_Template_select").style.display = "";
		}
		this.doShow(obj);
	},
	doUpdate : function()
	{
		var param = "";
		var isCreate = document.getElementById("MyPageEditorDialog.isCreate").value;
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		var pageNameElem = document.getElementById("MyPageEditorDialog.title");
		if( pageNameElem.value==null || pageNameElem.value.trim().length==0 ) {
			var fieldName = portalPage.getMessageResource('pt.ev.property.title');
			var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
			alert( msg );
			pageNameElem.focus();
			return;
		}
		
		param += "&title=" + encodeURIComponent( filterTag(document.getElementById("MyPageEditorDialog.title").value) );
		param += "&theme=" + document.getElementById("MyPageEditorDialog.theme").value;
		
		var isFound = false;
		if( isCreate == "true" ) {
			var form = document.getElementById("MyPageEditorDialog.EditForm");
			if( this.m_currentMakeType == "1" ) {
				for(var i=0; i<form.elements.length; i++) {
					var field = form.elements[i];
					if(field.type == "radio" && field.name == "layoutCheck") {
						if( field.checked == true ) {
							param += "&pageLayout=" + field.value;
							isFound = true;
							break;
						}
					}
				}
				if( isFound == false ) {
					var fieldName = portalPage.getMessageResource('pt.ev.property.layout');
					var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
					alert( msg );
					return;
				}
			}
			else {
				for(var i=0; i<form.elements.length; i++) {
					var field = form.elements[i];
					if(field.type == "radio" && field.name == "templetCheck") {
						if( field.checked == true ) {
							param += "&templatePath=" + field.value;
							isFound = true;
							break;
						}
					}
				}
				if( isFound == false ) {
					var fieldName = portalPage.getMessageResource('pt.ev.property.template');
					var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
					alert( msg );
					return;
				}
			}
		}
		else {
			isFound = false;
			var form = document.getElementById("MyPageEditorDialog.EditForm");
			for(var i=0; i<form.elements.length; i++) {
				var field = form.elements[i];
				if(field.type == "radio" && field.name == "layoutCheck") {
					if( field.checked == true ) {
						param += "&pageLayout=" + field.value;
						isFound = true;
						break;
					}
				}
			}
			if( isFound == false ) {
				var fieldName = portalPage.getMessageResource('pt.ev.property.layout');
				var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
				alert( msg );
				return;
			}
		}
		
		$("#MyPageEditorDialog").dialog('close');
		if( isCreate == "true" ) {
			//alert(param);
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/addMyPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
					location.href = portalPage.getContextPath() + "/portal" + data.Path;
				}});
		}
		else {
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/updateMyPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
					location.reload();
				}});
		}
	},
	doRemove : function()
	{
		if( this.m_myPageLength == 1 ) {
			alert( portalPage.getMessageResource("pt.ev.error.NotAccept") );
			return;
		}
		
		if( confirm(portalPage.getMessageResource("pt.ev.messageRemove")) ) {
			var param = "";
			param += "userId=" + portalPage.getUserId();
			param += "&pageId=" + portalPage.getId();
			param += "&path=" + portalPage.getPath();
			
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/removeMyPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
					location.href = portalPage.getContextPath() + "/portal/_user/~" + portalMyPageEditor.m_principalId; 
				}});
		}
	},
	setDefaultMyPage : function() 
	{
		var param = "";
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/setMyPageToHomeForAjax.face", param, false, {
			success: function(data){
				location.reload();
			}});
	},
	setDefaultPage : function() 
	{
		var param = "";
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/setDefaultPageToHomeForAjax.face", param, false, {
			success: function(data){
				location.reload();
			}});
	}
}

var portalGroupPageEditor = null;
enview.portal.GroupPageEditor = function()
{
	this.m_ajax = new enview.util.Ajax();
	this.m_ajax.setContextPath( portalPage.getContextPath() );
	
	$("#GroupPageEditorDialog").dialog({
			autoOpen: false,
			resizable: false,
			width:370, 
			//height:400,
			modal: true,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				},
				"Apply": function() {
					portalGroupPageEditor.doUpdate();
				}
			}
		});
}
enview.portal.GroupPageEditor.prototype =
{
	m_groupPageEditDialog : null,
	m_removeHandler : null,
	m_updateHandler : null,
	m_addHandler : null,
	m_invokeHandler : null,
	m_pageId : null,
	m_groupPageIds : null,
	m_principalId : null,
	m_contextMenu : null,
	m_currentTabId : 0,
	m_currentMakeType : "1",
	
	init : function()
	{
		this.m_contextMenu = aGroupPageMenu = new GroupPageMenu(portalPage.getContextPath());
		this.doRetrieveTemplate();
		
		document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "";
		document.getElementById("GroupPageEditorDialog_Template_select").style.display = "none";
	},
	setPages : function(groupPageIds)
	{
		this.m_groupPageIds = new Array();
		var pageArray = groupPageIds.split(",");
		var currentPageId = portalPage.getId();
		for(var i=0; i<pageArray.length; i++) {
			if( pageArray[i] != null && pageArray[i].length > 0 && currentPageId != pageArray[i] ) {
				document.getElementById("GroupTabMenu_" + pageArray[i]).style.display = "none";
			}
			else if( currentPageId == pageArray[i] ) {
				this.m_currentTabId = i;
			}
		}
	},
	setPrincipalId : function(principalId)
	{
		this.m_principalId = principalId;
	},
	onSelectPropertyTab : function(pagePath, tabId)
	{
		if( this.m_currentTabId == tabId ) return;
		
		this.m_currentTabId = tabId;
		location.href = pagePath;
	},
	doShowContextMenu : function(id) {
		var item = document.getElementById("GroupTabPageTab_" + id);
		var pos = (new enview.util.Utility()).getAbsolutePosition( item );
		//alert(id + "," + pos.getX() + "," + pos.getY());
		this.m_contextMenu.setItemId( id );
		this.m_contextMenu.show( pos.getX()+3, pos.getY()+25 );
	},
	doShow : function(elm)
	{
		portalGroupPageEditor.m_contextMenu.hide();
		
		if( elm != null ) {
			var pos = (new enview.util.Utility()).getAbsolutePosition( elm );
			var left = pos.getX() - 380;
			var top = pos.getY() + 20;
			$('#GroupPageEditorDialog').dialog( "option", "position", [left,top] );
		}
		
		$('#GroupPageEditorDialog').dialog('open');
		document.getElementById("GroupPageEditorDialog.title").focus();
	},
	doChangeMakeType : function(obj) {
		this.m_currentMakeType = obj.options[ obj.selectedIndex ].value;
		
		if( this.m_currentMakeType == "1" ) {
			document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "";
			document.getElementById("GroupPageEditorDialog_Template_select").style.display = "none";
		}
		else {
			document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "none";
			document.getElementById("GroupPageEditorDialog_Template_select").style.display = "";
		}
	},
	doRetrieveTemplate: function ()
	{
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/getGroupPageTemplateListForAjax.face", "", false, {success: function(data) { portalGroupPageEditor.doRetrieveTemplateHandler(data); }});
	},
	doRetrieveTemplateHandler: function (data)
	{
		var bodyElem = document.getElementById('GroupPageEditorDialog_ListBody');
		//alert("bodyElem=" + bodyElem);
	    var tr_tag = null;
	    var td_tag = null;
		for(; bodyElem.hasChildNodes(); )
			bodyElem.removeChild( bodyElem.childNodes[0] );
			
	    var tr_tag = null;
	    var td_tag = null;

		for(i=0; i<data.Data.length; i++) {
			tr_tag = document.createElement('tr');
			if( i % 2 == 0 ) {
				tr_tag.setAttribute("class", "even");
				tr_tag.setAttribute("className", "even");
			}
			else {
				tr_tag.setAttribute("class", "odd");
				tr_tag.setAttribute("className", "odd");
			}
			
			td_tag = document.createElement('td');
			td_tag.align = "center";
			td_tag.width = "20%";
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.innerHTML = "<input type=\"radio\" id=\"GroupPageEditorDialog[" + i + "].checkRow\" ch=\"" + i + "\" name=\"templetCheck\" class=\"webcheckbox\"/>";
			td_tag.innerHTML += "<input type=\"hidden\" id=\"GroupPageEditorDialog[" + i + "].templatePath\" value=\"" + data.Data[ i ].path + "\"/>";
			tr_tag.appendChild( td_tag );
			
			/*
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.align = "left";
			td_tag.innerHTML = "<span>" + data.Data[ i ].title + "</span>";
			tr_tag.appendChild( td_tag );
			*/
			
			td_tag = document.createElement('td');
			td_tag.setAttribute("class", "webgridbody");
			td_tag.setAttribute("className", "webgridbody");
			td_tag.align = "center";
			td_tag.innerHTML = "<img title='" + data.Data[ i ].desc + "' src='" + portalPage.getContextPath() + "/images/template/grouppage/" + data.Data[ i ].name + ".jpg'>";
			tr_tag.appendChild( td_tag );

			td_tag.setAttribute("style", "cursor: pointer;");
			td_tag.style.cursor = "pointer";
			//td_tag.appendChild( div_tag );
			tr_tag.appendChild( td_tag );

			bodyElem.appendChild( tr_tag );
		}

		if(data.Data.length == 0) {
			//alert("not found");
			tr_tag = document.createElement('tr');
			tr_tag.setAttribute("class", "row-empty");
			tr_tag.setAttribute("className", "row-empty");
			td_tag = document.createElement('td');
			td_tag.colSpan = "100";
			td_tag.innerHTML = "<span>" + portalPage.getMessageResource("pt.ev.messageNotFoundData") + "</span>";
			tr_tag.appendChild( td_tag );
			bodyElem.appendChild( tr_tag );
		}
	},
	doSelect : function(obj)
	{
		document.getElementById("GroupPageEditorDialog.title").value = portalPage.getTitle();
		var layout = portalPage.getLayout();
		var form = document.getElementById("GroupPageEditorDialog.EditForm");
		for(var i=0; i<form.elements.length; i++) {
			var field = form.elements[i];
			if(field.type == "radio" && field.name == "layoutCheck") {
				if( field.value == layout ) {
					field.checked = true;
					break;
				}
			}
		}
		document.getElementById("GroupPageEditorDialog.isCreate").value = "false";
		document.getElementById("GroupPageEditorDialog_changeMakeType").disabled = true;
		document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "";
		document.getElementById("GroupPageEditorDialog_Template_select").style.display = "none";
		this.doShow(obj);
	},
	doCreate : function(obj)
	{
		document.getElementById("GroupPageEditorDialog.title").value = "";
		document.getElementById("GroupPageEditorDialog.isCreate").value = "true";
		document.getElementById("GroupPageEditorDialog_changeMakeType").disabled = false;
		if( this.m_currentMakeType == "1" ) {
			document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "";
			document.getElementById("GroupPageEditorDialog_Template_select").style.display = "none";
		}
		else {
			document.getElementById("GroupPageEditorDialog_Layout_select").style.display = "none";
			document.getElementById("GroupPageEditorDialog_Template_select").style.display = "";
		}
		this.doShow(obj);
	},
	doUpdate : function()
	{
		var param = "";
		var isCreate = document.getElementById("GroupPageEditorDialog.isCreate").value;
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		var pageNameElem = document.getElementById("GroupPageEditorDialog.title");
		if( pageNameElem.value==null || pageNameElem.value.trim().length==0 ) {
			var fieldName = portalPage.getMessageResource('pt.ev.property.title');
			var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
			alert( msg );
			pageNameElem.focus();
			return;
		}
		param += "&title=" + encodeURIComponent( filterTag(document.getElementById("GroupPageEditorDialog.title").value) );
		param += "&theme=" + document.getElementById("GroupPageEditorDialog.theme").value;
		
		var isFound = false;
		if( isCreate == "true" ) {
			var form = document.getElementById("GroupPageEditorDialog.EditForm");
			if( this.m_currentMakeType == "1" ) {
				for(var i=0; i<form.elements.length; i++) {
					var field = form.elements[i];
					if(field.type == "radio" && field.name == "layoutCheck") {
						if( field.checked == true ) {
							param += "&pageLayout=" + field.value;
							isFound = true;
							break;
						}
					}
				}
				if( isFound == false ) {
					var fieldName = portalPage.getMessageResource('pt.ev.property.layout');
					var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
					alert( msg );
					return;
				}
			}
			else {
				for(var i=0; i<form.elements.length; i++) {
					var field = form.elements[i];
					if(field.type == "radio" && field.name == "templetCheck") {
						if( field.checked == true ) {
							param += "&templatePath=" + field.value;
							isFound = true;
							break;
						}
					}
				}
				if( isFound == false ) {
					var fieldName = portalPage.getMessageResource('pt.ev.property.template');
					var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
					alert( msg );
					return;
				}
			}
		}
		else {
			isFound = false;
			var form = document.getElementById("GroupPageEditorDialog.EditForm");
			for(var i=0; i<form.elements.length; i++) {
				var field = form.elements[i];
				if(field.type == "radio" && field.name == "layoutCheck") {
					if( field.checked == true ) {
						param += "&pageLayout=" + field.value;
						isFound = true;
						break;
					}
				}
			}
			if( isFound == false ) {
				var fieldName = portalPage.getMessageResource('pt.ev.property.layout');
				var msg = portalPage.getMessageResourceByParam('pt.ev.error.validation.required', fieldName);
				alert( msg );
				return;
			}
		}
		
		$("#GroupPageEditorDialog").dialog('close');
		
		if( isCreate == "true" ) {
			//alert(param);
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/addGroupPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
					//location.href = portalPage.getContextPath() + "/portal/_group/~" + portalGroupPageEditor.m_principalId; 
					location.href = portalPage.getContextPath() + "/portal" + data.Path;
				}});
		}
		else {
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/updateGroupPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.update') );
					location.reload();
				}});
		}
		
	},
	doRemove : function()
	{
		if( confirm(portalPage.getMessageResource("pt.ev.messageRemove")) ) {
			var param = "";
			param += "userId=" + portalPage.getUserId();
			param += "&pageId=" + portalPage.getId();
			param += "&path=" + portalPage.getPath();
			
			this.m_ajax.send("POST", portalPage.getContextPath() + "/page/removeGroupPageForAjax.face", param, false, {
				success: function(data){
					//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.remove') );
					location.href = portalPage.getContextPath() + "/portal/_group/~" + portalGroupPageEditor.m_principalId; 
				}});
		}
	},
	setDefaultGroupPage : function() 
	{
		var param = "";
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/setGroupPageToHomeForAjax.face", param, false, {
			success: function(data){
				location.reload();
			}});
	},
	setDefaultPage : function() 
	{
		var param = "";
		param += "userId=" + portalPage.getUserId();
		param += "&pageId=" + portalPage.getId();
		param += "&path=" + portalPage.getPath();
		
		this.m_ajax.send("POST", portalPage.getContextPath() + "/page/setDefaultPageToHomeForAjax.face", param, false, {
			success: function(data){
				location.reload();
			}});
	}
}

aMyPageMenu = null;
MyPageMenu = function(contextPath)
{
	if( portalPage == null) portalPage = new enview.portal.Page();
	
	this.m_domElement = document.createElement('div');
	this.m_domElement.id = "MyPageMenu_PageMenu";
	this.m_domElement.title = "MyPage Menu";
	this.m_domElement.style.display = "none";
	var htmlStr = "";
	htmlStr += "<div onmouseover='aMyPageMenu.onMouseOver(this)' onmouseout='aMyPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalMyPageEditor.doCreate()' ><img src='" + contextPath + "/decorations/layout/images/select.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.add') + "</a></div>";
	htmlStr += "<div onmouseover='aMyPageMenu.onMouseOver(this)' onmouseout='aMyPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalMyPageEditor.doSelect()' ><img src='" + contextPath + "/decorations/layout/images/edit.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.edit') + "</a></div>";
	htmlStr += "<div onmouseover='aMyPageMenu.onMouseOver(this)' onmouseout='aMyPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalMyPageEditor.doRemove()' ><img src='" + contextPath + "/decorations/layout/images/icon_del.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.remove') + "</a></div>";
	htmlStr += "<hr>";
	htmlStr += "<div onmouseover='aMyPageMenu.onMouseOver(this)' onmouseout='aMyPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalMyPageEditor.setDefaultMyPage()' ><img src='" + contextPath + "/decorations/layout/images/home_small.png' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.homepage') + "</a></div>";
	htmlStr += "<div onmouseover='aMyPageMenu.onMouseOver(this)' onmouseout='aMyPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalMyPageEditor.setDefaultPage()' ><img src='" + contextPath + "/decorations/layout/images/home_small.png' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.grouppage') + "</a></div>";
	this.m_domElement.innerHTML = htmlStr;
	//alert(htmlStr);

	document.body.appendChild( this.m_domElement );
		
	$("#MyPageMenu_PageMenu").dialog({
		autoOpen: false,
		width: "140px",
		resizable: false,
		draggable: false,
		modal: true,
		open: function(event, ui) { 
			$(".ui-dialog-titlebar-close").hide(); 
			//$("#PageManager_PageMenu").attr("style", "left:" + left);
			//$("#PageManager_PageMenu").attr("style", "top:" + top);
		}
	});
	
	this.init();
}

MyPageMenu.prototype =
{
	m_domElement : null,
	m_id : null,
	
	init : function() {
	
	},
	getItemId : function ()
	{
		return this.m_id;
	},
	setItemId : function (id)
	{
		this.m_id = id;
	},
	show : function (left, top)
	{
		//alert(left + "," + top);
		
		$('#MyPageMenu_PageMenu').dialog( "option", "position", [left,top] );
		$('#MyPageMenu_PageMenu').dialog('open');
	},
	hide : function()
	{
		$('#MyPageMenu_PageMenu').dialog('close');
	},
	onMouseOver : function(obj)
	{
		obj.setAttribute("class", "contextMenu_itemOver");
		obj.setAttribute("className", "contextMenu_itemOver");
	},
	onMouseOut : function(obj)
	{
		obj.setAttribute("class", "contextMenu_item");
		obj.setAttribute("className", "contextMenu_item");
	}
}

aGroupPageMenu = null;
GroupPageMenu = function(contextPath)
{
	if( portalPage == null) portalPage = new enview.portal.Page();
	
	this.m_domElement = document.createElement('div');
	this.m_domElement.id = "GroupPageMenu_PageMenu";
	this.m_domElement.title = "GroupPage Menu";
	this.m_domElement.style.display = "none";
	var htmlStr = "";
	htmlStr += "<div onmouseover='aGroupPageMenu.onMouseOver(this)' onmouseout='aGroupPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalGroupPageEditor.doCreate()' ><img src='" + contextPath + "/decorations/layout/images/select.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.add') + "</a></div>";
	htmlStr += "<div onmouseover='aGroupPageMenu.onMouseOver(this)' onmouseout='aGroupPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalGroupPageEditor.doSelect()' ><img src='" + contextPath + "/decorations/layout/images/edit.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.edit') + "</a></div>";
	htmlStr += "<div onmouseover='aGroupPageMenu.onMouseOver(this)' onmouseout='aGroupPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalGroupPageEditor.doRemove()' ><img src='" + contextPath + "/decorations/layout/images/icon_del.gif' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.remove') + "</a></div>";
	htmlStr += "<hr>";
	htmlStr += "<div onmouseover='aGroupPageMenu.onMouseOver(this)' onmouseout='aGroupPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalGroupPageEditor.setDefaultGroupPage()' ><img src='" + contextPath + "/decorations/layout/images/home_small.png' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.homepage') + "</a></div>";
	htmlStr += "<div onmouseover='aGroupPageMenu.onMouseOver(this)' onmouseout='aGroupPageMenu.onMouseOut(this)' style='border:none; cursor:hand;'><a onclick='portalGroupPageEditor.setDefaultPage()' ><img src='" + contextPath + "/decorations/layout/images/home_small.png' align='absmiddle'>&nbsp;" + portalPage.getMessageResource('pt.ev.mypage.grouppage') + "</a></div>";
	this.m_domElement.innerHTML = htmlStr;
	//alert(htmlStr);

	document.body.appendChild( this.m_domElement );
		
	$("#GroupPageMenu_PageMenu").dialog({
		autoOpen: false,
		width: "140px",
		resizable: false,
		draggable: false,
		modal: true,
		open: function(event, ui) { 
			$(".ui-dialog-titlebar-close").hide(); 
			//$("#PageManager_PageMenu").attr("style", "left:" + left);
			//$("#PageManager_PageMenu").attr("style", "top:" + top);
		}
	});
	
	this.init();
}

GroupPageMenu.prototype =
{
	m_domElement : null,
	m_id : null,
	
	init : function() {
	
	},
	getItemId : function ()
	{
		return this.m_id;
	},
	setItemId : function (id)
	{
		this.m_id = id;
	},
	show : function (left, top)
	{
		//alert(left + "," + top);
		
		$('#GroupPageMenu_PageMenu').dialog( "option", "position", [left,top] );
		$('#GroupPageMenu_PageMenu').dialog('open');
	},
	hide : function()
	{
		$('#GroupPageMenu_PageMenu').dialog('close');
	},
	onMouseOver : function(obj)
	{
		obj.setAttribute("class", "contextMenu_itemOver");
		obj.setAttribute("className", "contextMenu_itemOver");
	},
	onMouseOut : function(obj)
	{
		obj.setAttribute("class", "contextMenu_item");
		obj.setAttribute("className", "contextMenu_item");
	}
}
