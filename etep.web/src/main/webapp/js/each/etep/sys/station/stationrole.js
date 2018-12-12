$package('IQB.stationrole');
IQB.stationrole = function() {
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
			IQB.get(_this.config.action.getOrgInfo, {}, function(result){
				_this.cache.result = result.iqbResult.result;
				$('#search-orgId').select2({theme: 'bootstrap', placeholder: '请选择机构', allowClear: true, data: _this.cache.result}).val(null).trigger("change");
				$('#update-orgId').select2({theme: 'bootstrap', data: _this.cache.result})
			})
		},
		 openUpdateAuthWin : function() {		
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
			    if (records[0].stationStatus == 1) {
		        _this.config.tree.queryParams.orgId = records[0].orgId;
				_tree = new Tree(_this.config.tree);	
				_tree.init();		
				$('#update-auth-win-label').text('角色授权');
				var option = {roleId: records[0].id};
            	IQB.get(_this.config.action.getRoleAuth, option, function(result){
						var timer = setInterval(function(){
				        if( _tree.getTreeObj()){
				        	_this.check(result.iqbResult.result);
				        	$('#update-auth-win').modal({backdrop: 'static', keyboard: false, show: true});
				        	clearInterval(timer);
				        }			
						},100)
						_this.check(result.iqbResult.result);
						})
                   }else{
				IQB.alert("对不起，您目前状态为非激活不能进行角色授权");
			}
		}			
	},
		closeUpdateAuthWin : function() {
			$('#update-auth-win').modal('hide');
		},
		saveUpdateAuthWin : function() {
			_box.form.treeFrom.attr('action',_this.config.action.updateRoleAuth);
			var records = _box.util.getCheckedRows();
			var roleId = records[0].id
			$('#roleId').val(roleId);
			var checkedtreeNodes = $.fn.zTree.getZTreeObj("tree").getCheckedNodes();			
			$('.menuIds').remove();
			$.each(checkedtreeNodes, function(i, m){
				var $input = $('<input type="text" class="menuIds hidden" name="menuIds" />');							
				$input.val(m.id);
				_box.form.treeFrom.append($input);								
			});		
			IQB.save(_box.form.treeFrom, function(result) {
				_this.closeUpdateAuthWin();
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
				getOrgInfo : urls['rootUrl'] + '/sysOrganizationRest/getAllOrgInfo',
				updateStationId:urls['rootUrl']+'/sysStationRoleRest/deleteSysUserStationId'
			},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#search-orgId').val(null).trigger('change');
					$('#search-stationStatus').val(null).trigger('change');
				},
				insert: function(){//重写insert
					$('#update-win-label').text('添加角色');
					_box.handler.insert();					
					$('#update-isSuperadmin').val(1).trigger('change');
					$('#update-orgId').select2({theme: 'bootstrap', data: _this.cache.result})
					$('#update-stationStatus').val(1).trigger('change');
				},
				update: function(){//重写update
					_box.handler.update(function(result){
						$('#update-win-label').text('修改角色');
						_box.form.update.attr('action', _this.config.action.update);
						_box.form.update.form('load', result.iqbResult.result);
						$('#update-isSuperadmin').val(result.iqbResult.result.stationIsSuperadmin).trigger('change');
						$('#update-orgId').val(result.iqbResult.result.orgId).trigger('change');
						$('#update-stationStatus').val(result.iqbResult.result.stationStatus).trigger('change');
						_box.win.update.modal({backdrop: 'static', keyboard: false, show: true});
					});
				}
			},
			dataGrid: {//表格参数
				url: urls['rootUrl'] + '/sysStationRoleRest/getStationRole'
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
			_box = new DataGrid(_this.config);			
			_box.init();//初始化表格相关	
			_this.initOrgSelect();//初始化机构下拉			
			$('#search-stationStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择状态', allowClear: true}).val(null).trigger("change");		
			$('#update-isSuperadmin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#update-stationStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});			
			$('#btn-update-auth').on('click', function(){_this.openUpdateAuthWin();});
			$('#btn-save-auth').on('click', function(){_this.saveUpdateAuthWin();});
			$('#btn-close-auth').on('click', function(){_this.closeUpdateAuthWin();});			
		
		}
	}
	return _this;
}();


$(function(){
	//页面初始化
	IQB.stationrole.init();
});



	
