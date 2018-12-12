$package('IQB.dept');
IQB.dept = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/sysOrganizationDeptRest/insertSysOrganizationDept',
  				update: urls['rootUrl'] + '/sysOrganizationDeptRest/updateSysOrganizationDept',
  				getById: urls['rootUrl'] + '/sysOrganizationDeptRest/getSysOrganizationDeptById',
  				remove: urls['rootUrl'] + '/sysOrganizationDeptRest/deleteSysOrganizationDept'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){//重写reset
						var orgId = $('#search-orgId').val();
						_grid.handler.reset();
						if(orgId){
							$('#search-orgId').val(orgId);
						}
				},
				insert: function(){//重写add
						var treeNodes = _tree.getTreeObj().getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){	
							_grid.form.update.form('reset');
							_grid.handler.insert();
							$('#update-win-label').html('添加部门信息');
							$('#update-orgId').val(treeNodes[0].id);							
						}else{
							IQB.alert('未选中机构');
						}					
				},
				update: function(){//重写update
					$('#update-win-label').html('修改部门信息');
					_grid.handler.update();
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/sysOrganizationDeptRest/getSysOrganizationDept'
			},
			tree: {//树参数
				url: urls['rootUrl'] + '/sysOrganizationRest/getSysOrganization',
				setting: {	data: {simpleData: {enable: true}},//启用简单数据渲染
							callback: {onClick: function(event, treeId, treeNode){//事件回调
													_grid.handler.reset();	
													$('#search-orgId').val(treeNode.id);
													_grid.handler.search();
												}
									  }
						 }				
			}
		},
		init: function(){
			_grid = new DataGrid(_this.config); 
			_tree = new Tree(_this.config.tree);//注意参数和表格传参不同
			_grid.init();//初始化表格相关
			_tree.init();//初始化树
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.dept.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		