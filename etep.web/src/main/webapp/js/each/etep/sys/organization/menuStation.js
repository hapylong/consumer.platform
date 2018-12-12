$package('IQB.menu');
IQB.menu = function(){
	var _grid = null;
	var _organizationTree = null;
	var _menuTree = null;
	var _this = {
		expandAll: function(flag){//自定义函数(全部展开、收起)
			_menuTree.getTreeObj().expandAll(flag);
		},
		checkAll: function(flag){//自定义函数(全部勾选、取消勾选)
			_menuTree.getTreeObj().checkAllNodes(flag);			
		},
		check: function(treeNodes){//自定义函数(部分勾选)
			var treeObj = _menuTree.getTreeObj();
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
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/hqSysStationRoleRest/insertStationRole', 
  				get: urls['rootUrl'] + '/hqSysStationRoleRest/getHqSysStation'
  			},
			event: {//按钮绑定函数
				save: function(){//重写save
					var organizationTreeNodes = _organizationTree.getTreeObj().getSelectedNodes();
					if(organizationTreeNodes && organizationTreeNodes.length > 0){
						var menuTreeNodes = _menuTree.getTreeObj().getCheckedNodes();
						_grid.form.update.attr('action', _this.config.action.insert);
						$('#orgId').val(organizationTreeNodes[0].id);
						$('.menuIds').remove();
						$.each(menuTreeNodes, function(i, m){
							var $input = $('<input type="text" class="menuIds hidden" name="newMenuIds" />');							
							$input.val(m.id);
							_grid.form.update.append($input);								
						});		
						IQB.save(_grid.form.update, function(result) {
							IQB.alert('保存成功');
						});	
					}else{
						IQB.alert('未选中机构');
					}		
				}
			},
  			dataGrid: {//表格参数(页面没有表格，不用配置参数，默认为空即可)
			},
			organizationTree: {//组织机构树参数
				container: 'organizationTree',//容器
				url: urls['rootUrl'] + '/sysOrganizationRest/getSysOrganization',
				setting: {	data: {simpleData: {enable: true}},//启用简单数据渲染
							callback: {onClick: function(event, treeId, treeNode){//事件回调
														var data = {orgId: treeNode.id};
												    	IQB.get(_this.config.action.get, data, function(result){			    		
												    		_this.check(result.iqbResult.result);
														});
												}
									  }
						 }				
			},
			menuTree: {//菜单树参数
				container: 'menuTree',//容器
		        url: urls['rootUrl'] + '/hqSysStationRoleRest/selectHqStation',
				setting: {	data: {simpleData: {enable: true}},//启用简单数据渲染
							check: {enable: true,chkStyle: 'checkbox'}//启用复选框
									
						 }				
			}
		},
		init: function(){
			_grid = new DataGrid(_this.config); 
			_organizationTree = new Tree(_this.config.organizationTree); 
			_menuTree = new Tree(_this.config.menuTree);//注意参数和表格传参不同
			_grid.init();//初始化表格相关
			_organizationTree.init();//初始化树
			_menuTree.init();//初始化树		
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.menu.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		