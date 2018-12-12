$package('IQB.borrowersonline');
IQB.borrowersonline = function() {
	var _box = null;
	var _tree=null;
	var _this = {
		cache: {
			
		},
		expandAll: function(flag){//自定义函数(全部展开、收起)
			$.fn.zTree.getZTreeObj().expandAll(flag);
		   },
		checkAll: function(flag){//自定义函数(全部选中、取消)
			_tree.getZTreeObj().checkAllNodes(flag);
		},
		check: function(treeNodes){//自定义函数(部分选中)
			var treeObj = _tree.getTreeObj();
        	if(treeNodes && treeNodes.length > 0){
				treeObj.expandAll(true);
				treeObj.checkAllNodes(false);
				$.each(treeNodes, function(i, m){		
					var treeNode = treeObj.getNodeByParam('id', m.id);
					treeObj.checkNode(treeNode, true, false, false);
				});
			}else{
				treeObj.expandAll(true);
				treeObj.checkAllNodes(false);
			}			
		},
		initOrgSelect: function() {
			IQB.post(_this.config.action.getOrgInfo, {}, function(result){
				_this.cache.result = result.iqbResult.result;
				$('#update-orgId').select2({theme: 'bootstrap', data: _this.cache.result})
			})
		},
		getImageGroupByType : function() {
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var data = {
					"id" : parseInt(rows[0].id),
					"sign_type" : "x"
			}
			IQB.post(urls['rootUrl'] + '/ec_participant/get_image_group_by_type', data, function(result,status){
				if(result.success == 1) {
					result = result.iqbResult.result;
					$("#ec_signer_name_select").val($('#datagrid').datagrid2('getCheckedRows')[0].ecSignerName)
				    $('#signature-select-label').text('查看签名图章');
					$('#signature-select').modal({backdrop: 'static', keyboard: false, show: true});
					$("#ec_signer_img_type_select").val(result.ecSignerImgType);
					$("#is_default_signer_img_select").val(result.isDefaultSignerImg);
					$("#ec_signer_img_name_select").val(result.ecSignerImgName);
					$("#ec_signer_pic_name_select").val(result.ecSignerPicName);
					$("#ec_signer_img_data_blob_select").html('<img src=' + result.ecSignerImgDataBlobStr+ ' style="width:90%">');
				}
			})
		},
		skipNewWindow : function() {
			var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			window.parent.IQB.main2.openTab('wf_edit', '添加线上借款人', '/etep.web/view/ec/participator/borrowersave.html', true, true, {lastTab: param});
		},
		signatureSelectLabelClose : function() {
			$('#signature-select').modal('hide');
			$("#ec_signer_img_type_select").val("");
			$("#is_default_signer_img_select").val("");
			$("#ec_signer_img_name_select").val("");
			$("#ec_signer_pic_name_select").val("");
			$("#ec_signer_img_data_blob_select").html("");
		},
		config : {
			action : {
			    insert: urls['rootUrl'] + '/sysStationRoleRest/insertSysStationRole',
				update: urls['rootUrl'] + '/sysStationRoleRest/updateSysStationRole',
			    getById: urls['rootUrl'] + '/sysStationRoleRest/getSysStationRoleById',
				remove: urls['rootUrl'] + '/ec_participant/deleteSignFactor',
				updateRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/insertPurview',
				getRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/getSysRolePurview',
				getOrgInfo : urls['rootUrl'] + '/sysOrganizationRest/getAllOrgInfo',
				updateStationId:urls['rootUrl']+'/sysStationRoleRest/deleteSysUserStationId'
			},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
				}
			},
			dataGrid: {//表格参数
				url: urls['rootUrl'] + '/ec_participant/get_group_by_type',
				singleCheck: true,
	   			queryParams: {
	   				"sign_type": "x",
	   				"sign_state": "1"
	   			}
			},
	     	tree: {//树参数
				url: urls['rootUrl'] + '/sysMenuRest/getSysOrganationMenu',
				queryParams: {},
		        setting: {
					data: {simpleData: {enable: true}},// 启用简单数据渲染
					check: {enable: true, chkStyle: 'checkbox'}//启用复选框				     
		        }			
			}
		},
		init : function() {
			_box = new DataGrid2(_this.config);			
			_box.init();//初始化表格相关	
			_this.initOrgSelect();//初始化机构下拉			
			$('#name').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择状态', allowClear: true}).val(null).trigger("change");		
			$('#update-isSuperadmin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#update-stationStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});			
			//$("#btn-signature-update").click(function(){_this.updateSignatureStamp()});
			$("#btn-skip").click(function(){_this.skipNewWindow()});
			$("#btn-signature-select").click(function() {_this.getImageGroupByType();})
			$("#btn-signature-select-label-close").click(function(){_this.signatureSelectLabelClose()})
		
		}
	}
	return _this;
}();


$(function(){
	IQB.borrowersonline.init();
});

	
	
