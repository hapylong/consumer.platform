$package('IQB.biz');
IQB.biz = function(){
	var _grid = null;
	var _this = {
		cache: {
			
		},		
		getBizName: function(value){
			var name = '';
			$.each(_this.cache.biz, function(i, n){
				if(n.id == value){
					name = n.text;
				}
			});
			return name;
		},
		getOrgName: function(value){
			var name = '';
			$.each(_this.cache.org, function(i, n){
				if(n.id == value){
					name = n.text;
				}
			});
			return name;
		},
		selectInint: function(){
			IQB.postAsync(urls['rootUrl'] + '/bizConfig/getBiz', {}, function(result){	
				_this.cache.biz = result.iqbResult.result;
			});
			IQB.post(urls['rootUrl'] + '/bizConfig/getOrg', {}, function(result){	
				_this.cache.org = result.iqbResult.result;
			});
			IQB.post(urls['rootUrl'] + '/bizConfigTemplate/getEc', {}, function(result){	
				_this.cache.ec = result.iqbResult.result;
			});
		},
		insert: function(){
			$('#maintainForm').prop('action', urls['rootUrl'] + '/bizConfigTemplate/insert');
			$('#maintainForm').form('reset');
			$('#templateId').select2({theme: 'bootstrap', data: _this.cache.ec});
			$('.maintainDatagrid').hide();
			$('.maintainForm').show();			
		},
		updateAndRemoveOperation: function(val, row, rowIndex){
			var id = row.id;
			return '<button type="button" onClick="IQB.biz.update(' + id + ')" class="btn btn-link">修改</button><button type="button" onClick="IQB.biz.remove(' + id + ')" class="btn btn-link">删除</button>'
		},
		update: function(id){
			IQB.post(urls['rootUrl'] + '/bizConfigTemplate/getById', {id: id}, function(result){							
				$('#maintainForm').prop('action', urls['rootUrl'] + '/bizConfigTemplate/update');
				$('#maintainForm').form('load', result.iqbResult.result);	
				$('#templateId').select2({theme: 'bootstrap', data: _this.cache.ec});
				$('#templateId').val(result.iqbResult.result.templateId).trigger('change');
				$('.maintainDatagrid').hide();
				$('.maintainForm').show();
			});
		},
		remove: function(id){
			IQB.confirm('确认要删除吗？', function(){
				var rows = $('#maintainDatagrid').datagrid2('getCheckedRows');
				var idField = $('#maintainDatagrid').datagrid2('getIdField'); 
		   		IQB.post(urls['rootUrl'] + '/bizConfigTemplate/remove', {id: id}, function(result){
		   			_this.refresh();
		   		});
			}, function(){});
		},
		close: function(){
			$('#maintain-win').modal('hide');
		},
		save: function(){
			$.each($('#maintainForm').find('input[type = "text"]'), function(i, n){
				var value = $.trim($(n).val());
				$(n).val(value);
			});
			var url = $('#maintainForm').prop('action');	
			var option = $('#maintainForm').serializeObject();
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var idField = $('#datagrid').datagrid2('getIdField'); 
			option.bizConfigId = rows[0][idField];
			if($('#maintainForm').form('validate')){
				IQB.post(url, option, function(result){  
					_this.back();
					_this.refresh();
				});
			}		
		},
		back: function(){
			$('.maintainDatagrid').show();
			$('.maintainForm').hide();
		},
		refresh: function(){
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var idField = $('#datagrid').datagrid2('getIdField'); 
   			$('#maintainDatagrid').datagrid2({url: urls['rootUrl'] + '/bizConfigTemplate/paging', singleCheck: true, queryParams: {bizConfigId: rows[0][idField]}, pagination: false, paginator: 'maintainPaginator'});
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/bizConfig/insert',
  				update: urls['rootUrl'] + '/bizConfig/update',
  				getById: urls['rootUrl'] + '/bizConfig/getById',
  				remove: urls['rootUrl'] + '/bizConfig/remove'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('#searchForm').find('select').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
				},
				insert: function(){
					_grid.handler.insert();
					$('#bizType').select2({theme: 'bootstrap', data: _this.cache.biz});
					$('#orgCode').select2({theme: 'bootstrap', data: _this.cache.org});
					$('#isIncludeJunior').val(1).trigger('change');
					$('#status').val(1).trigger('change');
					$('#update-win-label').text('添加业务模板');					
				},
				update: function(){
					_grid.handler.update(function(result){
						_grid.form.update.attr('action', _this.config.action.update);
						_grid.form.update.form('load', result.iqbResult.result);
						$('#bizType').select2({theme: 'bootstrap', data: _this.cache.biz});
						$('#orgCode').select2({theme: 'bootstrap', data: _this.cache.org});
						$('#bizType').val(result.iqbResult.result.bizType).trigger('change');
						$('#orgCode').val(result.iqbResult.result.orgCode).trigger('change');
						$('#isIncludeJunior').val(result.iqbResult.result.isIncludeJunior).trigger('change');
						$('#status').val(result.iqbResult.result.status).trigger('change');
						$('#update-win-label').text('修改业务模板');
						_grid.win.update.modal({backdrop: 'static', keyboard: false, show: true});		
					});					
				},
				save: function(){
					$.each(_grid.form.update.find('input[type = "text"]'), function(i, n){
						var value = $.trim($(n).val());
						$(n).val(value);
					});
					var url = _grid.form.update.prop('action');	
					var option = _grid.form.update.serializeObject();
					option.orgName = _this.getOrgName(option.orgCode);
					if(_grid.form.update.form('validate')){
						IQB.post(url, option, function(result){  
							//禁用表单验证,处理页面遗留toolTip
							_grid.form.update.form('disableValidation');						
							_grid.handler.close();
							_grid.form.update.form('reset');
							//启用表单验证
							_grid.form.update.form('enableValidation');					    
							_grid.handler.refresh();	
						});
					}		
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/bizConfig/paging',
	   			singleCheck: true
			}
		},
		init: function(){
			_this.selectInint();
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化表格相关
			//slect2组件初始化
			$('#searchForm').find('select').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isIncludeJunior').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#status').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});			
			$('#btn-maintain').on('click', function(e){
				var rows = $('#datagrid').datagrid2('getCheckedRows');
				var idField = $('#datagrid').datagrid2('getIdField'); 
				if(rows != null && rows.length > 0){
					$('.maintainForm').hide();
					$('#maintainDatagrid').datagrid2({url: urls['rootUrl'] + '/bizConfigTemplate/paging', singleCheck: true, queryParams: {bizConfigId: rows[0][idField]}, pagination: false, paginator: 'maintainPaginator'});
					$('#maintain-win').modal({backdrop: 'static', keyboard: false, show: true});
				}else{
					IQB.alert('未选中行');
				}
			});
			$('#btn-maintain-insert').on('click', function(e){_this.insert();});
			$('#btn-maintain-close').on('click', function(e){_this.close();});
			$('#btn-maintain-save').on('click', function(e){_this.save();});
			$('#btn-maintain-back').on('click', function(e){_this.back();});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.biz.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		