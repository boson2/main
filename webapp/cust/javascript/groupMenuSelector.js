
var aGroupMenuSelector = null;
GroupMenuSelector = function(isTree)
{
	this.m_ajax = new enview.util.Ajax();
	if( portalPage == null) portalPage = new enview.portal.Page();
	this.m_selectRowIndex = 0;
	this.m_contextPath = portalPage.getContextPath();
	enviewMessageBox = new enview.portal.MessageBox(200, 100, 1000); 
	this.m_isTree = isTree;
	
	$("#MenuSelectDialog").dialog({
		autoOpen: false,
		resizable: false,
		modal: true,
		width: 450,
		buttons: {
			Apply: function() {
		aGroupMenuSelector.doApply();
			},
			Cancel: function() {
				$(this).dialog('close');
			}
		}
	});
	
	//this.init();
}

GroupMenuSelector.prototype =
{
	m_domElement : null,
	m_tree : null,
	m_sourceElement : null,
	m_isTree : false,
	
	init : function() {
		if(this.m_isTree == true ) {
			this.m_tree = new dhtmlXTreeObject(document.getElementById('MenuSelector_TreeTabPage'),"100%","100%",0);
			this.m_tree.setImagePath(this.m_contextPath + "/face/images/tree/");
			this.m_tree.setOnClickHandler( this.onNodeSelect );
			this.m_tree.enableAutoTooltips(true);
			this.m_tree.setXMLAutoLoading(this.m_contextPath + "/userMenu/retrieveGroupTreeForAjax.face"); 
			this.m_tree.loadXML(this.m_contextPath + "/userMenu/retrieveGroupTreeForAjax.face?");
			this.m_tree.enableCheckBoxes(true);
			//this.m_tree.clearSelection(id);
			//this.m_tree.getAllChecked();
			//this.m_tree.focusItem(id);
		}
	},
	doShow : function (source)
	{
		if( source ) {
			this.m_sourceElement = source;
		}
		$('#MenuSelectDialog').dialog('open');
	},
	onNodeSelect : function (id) {
		if( id != '/' ) {
			//aGroupMenuSelector.doApply( id );
		}
	},
	doApply : function(id) {
		if( this.m_isTree == true ) {
			var checkIds = this.m_tree.getAllChecked();
			if( checkIds != null && checkIds.length > 0 ) {
				$('#MenuSelectDialog').dialog('close');
				var checkArray = checkIds.split(",");
				var param = "pageIds=";
				for(var i=0; i< checkArray.length; i++) {
					var pageId = this.m_tree.getItem(checkArray[i]).getAttribute("userData");
					if(i > 0) param += ",";
					param += pageId;
				}
				
				if( i > 0 ) {
					$('#MenuSelectDialog').dialog('close');
					//alert(param);
					
					this.m_ajax.send("POST", portalPage.getContextPath() + "/userMenu/addGroupMenusOnlyForAjax.face", param, false, {
						success: function(data){
							location.reload();
							//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
						}});
				}
			}
		}
		else {
			var form = document.getElementById("MenuSelectListForm");
			var param = "pageIds=";
			var field = "";
			var ix = 0;
			for(var i=0; i<form.elements.length; i++) {
				field = form.elements[i];
				if(field.type == "checkbox" && field.checked == true ) {
					if(ix > 0) param += ",";
					param += field.id;
					ix++;
				}
			}
			
			if( ix > 0 ) {
				$('#MenuSelectDialog').dialog('close');
				//alert(param);
				
				this.m_ajax.send("POST", portalPage.getContextPath() + "/userMenu/addGroupMenusForAjax.face", param, false, {
					success: function(data){
						location.reload();
						//enviewMessageBox.doShow( portalPage.getMessageResource('pt.ev.message.success.add') );
					}});
			}
		}
	}
}

