$package('IQB.organization');
IQB.organization = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache: {//用于缓存页面对象
			
		},
		initOrgTypeSelect: function(){
			IQB.post(urls['rootUrl'] + '/sysOrganizationRest/getOrgTpye', {}, function(result){
				_this.cache.orgTypeSelect = result.iqbResult.result;
				$('#orgType').select2({theme: 'bootstrap', data: _this.cache.orgTypeSelect});
			})
		},
		showMenu: function(event, treeId, treeNode){
			if(treeNode) {				
				_tree.getTreeObj().selectNode(treeNode);
				$('#menu').menu('show', {left: event.clientX, top: event.clientY});
			}
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/sysOrganizationRest/insertSysOrganization', 
  				update: urls['rootUrl'] + '/sysOrganizationRest/updateSysOrganization',
  				getById: urls['rootUrl'] + '/sysOrganizationRest/getSysOrganizationById',
  				remove: urls['rootUrl'] + '/sysOrganizationRest/deleteSysOrganization',
  				pushToCRM: urls['sysmanegeUrl'] + '/sysOrganizationRest/pushOrgInfoToCRM'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){//重写insert
						var treeNodes = _tree.getTreeObj().getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){
							_grid.form.update.form('reset');
							$('#update-parentId').val(treeNodes[0].id);
							$('#parentOrgName').val(treeNodes[0].name);
							$('#orgType').select2({theme: 'bootstrap', data: _this.cache.orgTypeSelect});
							_grid.form.update.attr('action',_this.config.action.insert);
						}			
				},
				save: function(){//重写save
						var treeNodes = _tree.getTreeObj().getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){
							IQB.save(_grid.form.update, function(result){
								_grid.form.update.form('reset');
								_tree.init();//回调								
							});
						}else{
							IQB.alert('未选中机构');
						}				
				},
				remove: function(){//重写remove
					var treeNodes = $.fn.zTree.getZTreeObj('tree').getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){
							IQB.confirm('确认要执行吗？', function(){
								var option = {};
								option.id = treeNodes[0].id;
								IQB.remove(_this.config.action.remove, option, function(result){
									_grid.form.update.form('reset');
									_tree.init();//回调								
								});
							}, function(){
								
							});
						}				
				}
			},
  			dataGrid: {//表格参数(页面没有表格,不用配置参数,默认为空即可)  	
			},
			tree: {//树参数
				url: urls['rootUrl'] + '/sysOrganizationRest/getSysOrganization',
				setting: {	data: {simpleData: {enable: true}},//启用简单数据渲染
							callback: {onClick: function(event, treeId, treeNode){//回调事件
														var data = {id: treeNode.id};
												    	IQB.getById(_this.config.action.getById, data, function(result){	
												    		_grid.form.update.form('load', result.iqbResult.result);
												    		_this.cache.orgType.val(result.iqbResult.result.orgType).trigger('change');
												    		_grid.form.update.attr('action', _this.config.action.update);
														});
												},
									   onRightClick: function(event, treeId, treeNode){_this.showMenu(event, treeId, treeNode);}
									  }
						 }				
			}
		},
		extFunc:{
			pushToCRM: function(){
				var option = {};
				var orgCode = $('#orgCode').val();
				if(orgCode == ''){
					IQB.alert('机构编码为空，推送失败');
				}
				option['orgCode'] = orgCode;
				IQB.getById(_this.config.action.pushToCRM, option, function(result){		
					if(result.success == 1){
						IQB.alert('推送成功！');
					}
				});
			}
		},
		init: function(){
			_grid = new DataGrid(_this.config); 
			_tree = new Tree(_this.config.tree);//注意参数和表格组件传参不同 
			_grid.init();//初始化表格相关
			_tree.init();//初始化树
			_this.initOrgTypeSelect();
			this.initBtnClick();
			//菜单按钮初始化
			$('#men-insert').on('click', function(){$('#btn-insert').click();});
			$('#men-remove').on('click', function(){$('#btn-remove').click();});
			//初始化select2组件并缓存
			_this.cache.orgType = $('#orgType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		initBtnClick: function(){
			$('#btn-pushToCRM').click(_this.extFunc.pushToCRM);
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.organization.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});			