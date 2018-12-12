$package('IQB.sysdict');
IQB.sysdict = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache: {
			
		},
		showMenu: function(event, treeId, treeNode){
			if(treeNode) {
				_tree.getTreeObj().selectNode(treeNode);
				if(treeNode.isContent == 1){
					$('#men-insert').removeClass('hidden');
					$('#men-insert2').removeClass('hidden');
					$('#men-start').addClass('hidden');
					$('#men-stop').addClass('hidden');
				}else{
					$('#men-insert').addClass('hidden');
					$('#men-insert2').addClass('hidden');
					if(treeNode.isEnable == 1){
						$('#men-start').addClass('hidden');
						$('#men-stop').removeClass('hidden');
					}else{
						$('#men-start').removeClass('hidden');
						$('#men-stop').addClass('hidden');
					}					
				}
				$('#menu').menu('show', {left: event.clientX, top: event.clientY});
			}
		},
		insertDictType: function(){
			var treeNodes = _tree.getTreeObj().getSelectedNodes();
			if(treeNodes && treeNodes.length > 0){	 
				$('#updateDictTypeForm').attr('action', _this.config.action.insertDictType);	
		    	$('#updateDictTypeForm').form('reset');			    	
				$('#update-parentId').val(treeNodes[0].id);
				$('#dictType').val(1);
				$('#isContent').val(1);
				$('#dictTypeCode').parent().parent().addClass('hidden');
				$('#cascadeCode').parent().parent().addClass('hidden');	
				$('#editable').parent().parent().addClass('hidden');
				$('#isEnable2').parent().parent().addClass('hidden');	
				$('#dictTypeCode').validatebox('disableValidation');
				$('#cascadeCode').validatebox('disableValidation');				
				_this.cache.editable.val(1).trigger('change');
				_this.cache.isEnable2.val(1).trigger('change');
				$('#update-dictType-label').html('添加字典类别');
			    $('#update-dictType-win').modal({backdrop: 'static', keyboard: false, show: true});	    	
			}
		},
		insertDictType2: function(){
			var treeNodes = _tree.getTreeObj().getSelectedNodes();
			if(treeNodes && treeNodes.length > 0){	
				$('#updateDictTypeForm').attr('action', _this.config.action.insertDictType);	
		    	$('#updateDictTypeForm').form('reset');			    	
				$('#update-parentId').val(treeNodes[0].id);
				$('#dictType').val(1);
				$('#isContent').val(2);
				$('#dictTypeCode').parent().parent().removeClass('hidden');
				$('#cascadeCode').parent().parent().removeClass('hidden');
				$('#editable').parent().parent().removeClass('hidden');
				$('#isEnable2').parent().parent().removeClass('hidden');
				$('#dictTypeCode').validatebox('enableValidation');
				$('#cascadeCode').validatebox('enableValidation');
				_this.cache.editable.val(1).trigger('change');
				_this.cache.isEnable2.val(1).trigger('change');
				$('#update-dictType-label').html('添加字典类别');
			    $('#update-dictType-win').modal({backdrop: 'static', keyboard: false, show: true});	 	    	
			}
		},
		updateDictType: function(){
			var treeNodes = _tree.getTreeObj().getSelectedNodes();
			if(treeNodes && treeNodes.length > 0){				
				if(treeNodes[0].isContent == 1){
					$('#dictTypeCode').parent().parent().addClass('hidden');
					$('#cascadeCode').parent().parent().addClass('hidden');
					$('#editable').parent().parent().addClass('hidden');
					$('#isEnable2').parent().parent().addClass('hidden');	
					$('#dictTypeCode').validatebox('disableValidation');
					$('#cascadeCode').validatebox('disableValidation');	
				}else{
					$('#dictTypeCode').parent().parent().removeClass('hidden');
					$('#cascadeCode').parent().parent().removeClass('hidden');
					$('#editable').parent().parent().removeClass('hidden');
					$('#isEnable2').parent().parent().removeClass('hidden');
					$('#dictTypeCode').validatebox('enableValidation');
					$('#cascadeCode').validatebox('enableValidation');
				}
				var option = {id: treeNodes[0].id};
		    	IQB.getById(_this.config.action.getDictTypeById, option, function(result){	
		    		$('#updateDictTypeForm').attr('action', _this.config.action.updateDictType);		
		    		$('#updateDictTypeForm').form('reset');	
		    		$('#updateDictTypeForm').form('load', result.iqbResult.result);
					_this.cache.editable.val(result.iqbResult.result.editable).trigger('change');
					_this.cache.isEnable2.val(result.iqbResult.result.isEnable).trigger('change');
			    	$('#update-dictType-label').html('修改字典类别');		    	
			    	$('#update-dictType-win').modal({backdrop: 'static', keyboard: false, show: true});
				});		    	
			}
		},
		saveDictType: function(){
			IQB.save($('#updateDictTypeForm'), function(result){
				_this.closeDictType();
				_tree.init();	
				_grid.handler.reset();
				_grid.handler.search();
			});
		},
		startDictType: function(){
			IQB.confirm('确认要执行吗？', function(){
				var treeNodes = _tree.getTreeObj().getSelectedNodes();
				if(treeNodes && treeNodes.length > 0){
					var option = {id: treeNodes[0].id, isEnable: 1};
					IQB.get(_this.config.action.removeDictType, option, function(result){
						_tree.init();	
						_grid.handler.reset();
						_grid.handler.search();
			   		});
				}		   		
			}, function(){
				return false;
			});				
		},
		stopDictType: function(){
			IQB.confirm('确认要执行吗？', function(){
				var treeNodes = _tree.getTreeObj().getSelectedNodes();
				if(treeNodes && treeNodes.length > 0){
					var option = {id: treeNodes[0].id, isEnable: 2};
					IQB.get(_this.config.action.removeDictType, option, function(result){
						_tree.init();	
						_grid.handler.reset();
						_grid.handler.search();
			   		});
				}		   		
			}, function(){
				return false;
			});				
		},
		closeDictType: function(){
			$('#update-dictType-win').modal('hide');
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/sysDictRest/insertSysDictItem',
  				update: urls['rootUrl'] + '/sysDictRest/updateSysDictItem',
  				getById: urls['rootUrl'] + '/sysDictRest/getSysDictItemById',
  				remove: urls['rootUrl'] + '/sysDictRest/deleteSysDictItem',
  				insertDictType: urls['rootUrl'] + '/sysDictRest/insertSysDictType',
  				updateDictType: urls['rootUrl'] + '/sysDictRest/updateSysDictType',
  				getDictTypeById: urls['rootUrl'] + '/sysDictRest/getSysDictTypeById',
  				removeDictType: urls['rootUrl'] + '/sysDictRest/deleteSysDictType'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){//重写reset
					var dictTypeCode = $('#search-dictTypeCode').val();
					_grid.handler.reset();
					if(dictTypeCode){
						$('#search-dictTypeCode').val(dictTypeCode);
					}
				},
				insert: function(){//重写insert
					var treeNodes = _tree.getTreeObj().getSelectedNodes();
					if(treeNodes && treeNodes.length > 0){	
						if(treeNodes[0].isContent == 2){
							_grid.handler.insert();						
							$('#update-dictTypeCode').val(treeNodes[0].dictTypeCode);
							_this.cache.isEnable.val(1).trigger('change');
							$('#update-win-label').html('添加系统字典项');
						}else{
							IQB.alert('字典类别为目录，无法添加字典项信息');
						}							
					}else{
						IQB.alert('未选中字典类别');
					}					
				},
				update: function(){//重写update					
					_grid.handler.update(function(result){
						_grid.form.update.attr('action',_this.config.action.update);
						_grid.form.update.form('load',result.iqbResult.result);		
						_this.cache.isEnable.val(result.iqbResult.result.isEnable).trigger('change');
						$('#update-win-label').text('修改系统字典项');
						_grid.win.update.modal({backdrop: 'static', keyboard: false, show: true});
					});					
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/sysDictRest/getSysDictItem',
	   			queryParams: {
	   				dictType: 1
	   			}
			},
			tree: {//树参数
				url: urls['rootUrl'] + '/sysDictRest/getSysDictType',
				queryParams: {
					dictType: 1
	   			},
				setting: {	data: {simpleData: {enable: true}},//启用简单数据渲染
							callback: {onClick: function(event, treeId, treeNode){//节点单击回调
													if(treeNode.isContent == 2){
														_grid.handler.reset();
														$('#search-dictTypeCode').val(treeNode.dictTypeCode);
														_grid.handler.search();	
													}																						
												},
									   onRightClick: function(event, treeId, treeNode){//节点右击回调
							   				_this.showMenu(event, treeId, treeNode);
									   			}
									  }
						 }				
			}
		},
		init: function(){
			_grid = new DataGrid(_this.config); 
			_tree = new Tree(_this.config.tree);//注意树参数和表格参数不同
			_grid.init();//初始化表格相关
			_tree.init();//初始化树
			//select2组件初始化并缓存对象
			_this.cache.isEnable = $('#isEnable').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.cache.editable = $('#editable').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.cache.isEnable2 = $('#isEnable2').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			//菜单初始化
			$('#men-insert').on('click', function(){_this.insertDictType();});
			$('#men-insert2').on('click', function(){_this.insertDictType2();});
			$('#men-update').on('click', function(){_this.updateDictType();});
			$('#men-start').on('click', function(){_this.startDictType();});
			$('#men-stop').on('click', function(){_this.stopDictType();});
			$('#btn-dictType-save').on('click', function(){_this.saveDictType();});
			$('#btn-dictType-close').on('click', function(){_this.closeDictType();});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.sysdict.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		