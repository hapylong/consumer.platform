$package('IQB.borrowersonline');
IQB.borrowersonline = function() {
	var _box = null;
	var _tree=null;
	var _this = {
		cache: {
			page:0
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
			IQB.get(_this.config.action.getOrgInfo, {}, function(result){
				_this.cache.result = result.iqbResult.result;
				$('#update-orgId').select2({theme: 'bootstrap', data: _this.cache.result})
			})
		},
		createSecurityKey : function() {
			var data = {
				"merchNames":$("#merchNames").val()	
			}
			data.borrowTogether = $('#borrow').val();
			IQB.post(urls['rootUrl'] + '/admin/generate_key', data, function(result){
				if(result.success == 1){
					alert(result.iqbResult.result);
				} else {
					IQB.alert(result.retUserInfo);
				}
				window.location.reload();
			})
		},
		askIfUpdateSecurityKey : function() {
			var msg = "是否更新商户秘钥 【";
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var data ;
			if(rows.length > 0) {
				var data = {
						"merchNames":rows[0].merchantNo
					}
				IQB.confirm(msg + rows[0].merchantNo + "】", 
						function(){_this.updateSecurityKey(data)},
						function(){});
			} else {
				var array = $("#merchNames").val();
				var merchants = "";
				data = {
						"merchNames":$("#merchNames").val()	
					}
				IQB.confirm(msg + array + "】", 
						function(){_this.updateSecurityKey(data)},
						function(){});
			}
			
		},
		updateSecurityKey : function(data) {
			data.borrowTogether = $('#borrow').val();
			IQB.post(urls['rootUrl'] + '/admin/update_key', data, function(result){
				if(result.success == 1){
					alert(result.iqbResult.result);
				} else {
					IQB.alert(result.retUserInfo);
				}
				window.location.reload();
			})
		},
		signatureSelectLabelClose : function() {
			$('#signature-select').modal('hide');
			$("#ec_signer_img_type_select").val("");
			$("#is_default_signer_img_select").val("");
			$("#ec_signer_img_name_select").val("");
			$("#ec_signer_pic_name_select").val("");
			$("#ec_signer_img_data_blob_select").html("");
		},
		securitykeyDetailsClose : function() {
			$('#securitykey-details').modal('hide');
			$("#merchantNo").val("");
			$("#publicKey").val("");
			$("#privateKey").val("");
			$("#whiteList").val("");
		},
		securitykeyDetails : function(data){
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			if(rows.length > 1) {
				IQB.alert("请选中单条数据.");
				return;
			} else if(rows.length == 0){
				IQB.alert("请选中行.");
				return;
			} else {
				$("#merchantNo").val(rows[0].merchantNo),
				$("#publicKey").val(rows[0].publicKey),
				$("#privateKey").val(rows[0].privateKey),
				$("#whiteList").val(rows[0].whiteList)
			}
			$('#securitykey-details').modal({backdrop: 'static', keyboard: false, show: true});
			$("#btn-securitykey-update-label").unbind("click").click(function(){
				var data = {
						"id" : rows[0].id,
						"whiteList" : $("#whiteList").val()
				}
				IQB.post(urls['rootUrl'] + '/admin/update_ips_by_id', data, function(result,status){
					if(result.iqbResult.result == "success") {
						IQB.alert("操作成功");
						_this.securitykeyDetailsClose();
						_this.refresh();
					}
				})
			});
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/admin/cget_key',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page})	
			});
	    },
		config : {
			action : {
			    insert: urls['rootUrl'] + '/sysStationRoleRest/insertSysStationRole',
				update: urls['rootUrl'] + '/sysStationRoleRest/updateSysStationRole',
			    getById: urls['rootUrl'] + '/sysStationRoleRest/getSysStationRoleById',
				remove: urls['rootUrl'] + '/sysStationRoleRest/deleteSysStationRole',
				updateRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/insertPurview',
				getRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/getSysRolePurview',
				getOrgInfo : urls['rootUrl'] + '/bizConfig/getOrg',
				updateStationId:urls['rootUrl']+'/sysStationRoleRest/deleteSysUserStationId'
			},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
				}
			},
			dataGrid: {//表格参数
				url: urls['rootUrl'] + '/admin/cget_key',
				singleCheck: true,
				onPageChanged : function(page){
	   				_this.cache.page = page;
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
			$('#btn-create').click(function(){_this.createSecurityKey()});
			$('#btn-update-key').click(function(){_this.askIfUpdateSecurityKey()});
			
			$("#info-details").click(function(){_this.securitykeyDetails();})
			$("#btn-securitykey-details-label-close").click(function(){_this.securitykeyDetailsClose()})
		
		}
	}
	return _this;
}();


$(function(){
	IQB.borrowersonline.init();
});

	
	
