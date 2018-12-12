$package('IQB.sysmenu');
IQB.sysmenu = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache: {
			
		},
		showMenu: function(flag, event, treeId, treeNode){
			if(flag){
				_tree.getTreeObj().selectNode(treeNode);
				if(treeNode.menuType == 1){
					$('#men-cancel').addClass('hidden');
					$('#men-insert').removeClass('hidden');
					$('#men-remove').removeClass('hidden');
				}else{
					$('#men-cancel').addClass('hidden');
					$('#men-insert').addClass('hidden');
					$('#men-remove').removeClass('hidden');
				}
			}else{
				_tree.getTreeObj().cancelSelectedNode();
				$('#men-cancel').removeClass('hidden');
				$('#men-insert').addClass('hidden');
				$('#men-remove').addClass('hidden');
			}
			$('#menu').menu('show', {left: event.clientX, top: event.clientY});
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/sysMenuRest/insertSysMenu', 
  				update: urls['rootUrl'] + '/sysMenuRest/updateSysMenu',
  				getById: urls['rootUrl'] + '/sysMenuRest/getSysMenuById',
  				remove: urls['rootUrl'] + '/sysMenuRest/deleteSysMenu'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){//重写insert
						_grid.form.update.form('reset');						
						var treeNodes = _tree.getTreeObj().getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){							
							$('#update-parentId').val(treeNodes[0].id);
							$('#parentMenuName').val(treeNodes[0].name);
						}else{
							$('#update-parentId').val(0);							
						}	
						$('#menuType').attr('disabled', false);
						_grid.form.update.attr('action', _this.config.action.insert);
				},
				save: function(){//重写save
						var parentId = $('#update-parentId').val();
						if(parentId >= 0){											
							IQB.save(_grid.form.update, function(result){
								_tree.init();
								_grid.form.update.form('reset');												
							});							
						}else{
							IQB.alert('请右击菜单目录或者右击菜单树空白区域添加菜单');
						}									
				},
				remove: function(){//重写remove					
						var treeNodes = _tree.getTreeObj().getSelectedNodes();
						if(treeNodes && treeNodes.length > 0){
							IQB.confirm('确认要执行吗？',function(){
								var option = {};
								option.id = treeNodes[0].id;
								IQB.remove(_this.config.action.remove, option, function(result){
									_tree.init();
									_grid.form.update.form('reset');					
								});
							},function(){
								return false;
							});
						}				
				}
			},
  			dataGrid: {//表格参数(页面没有表格，不用配置参数，默认为空即可)  	
			},
			tree: {//组织机构树参数
				url: urls['rootUrl'] + '/sysMenuRest/getSysMenu',
				setting: {	data: {simpleData: {enable: true}}, //启用简单数据渲染
							callback: {onClick: function(event, treeId, treeNode){//节点单击回调
														var option = {id: treeNode.id};
												    	IQB.getById(_this.config.action.getById, option, function(result){
												    		_grid.form.update.form('load', result.iqbResult.result);
												    		_this.cache.menuType.val(result.iqbResult.result.menuType).trigger('change');
												    		$('#menuType').attr('disabled', true);
												    		_grid.form.update.attr('action', _this.config.action.update);
														});
												},
									   onRightClick: function(event, treeId, treeNode){//节点右击回调
								   						_this.showMenu(true, event, treeId, treeNode);
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
			//菜单树空白区域右击
			$('.left-panel').on('mousedown', function(event){if(event.which == 3){var element = event.target;if(element.tagName == 'DIV'){_this.showMenu(false, event);if(event){if(event.stopPropagation){event.stopPropagation();}else{event.cancelBubble = true;}}};}});
			//select2组件初始化并缓存
			_this.cache.menuType = $('#menuType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#men-cancel').on('click', function(){$('#btn-insert').click();});
			$('#men-insert').on('click', function(){$('#btn-insert').click();});
			$('#men-remove').on('click', function(){$('#btn-remove').click();});
		}
	}
	return _this;
}();

$(function(){	
	//页面初始化
	IQB.sysmenu.init();
	//禁止浏览器右击
	document.oncontextmenu = function(){return false;}
});		