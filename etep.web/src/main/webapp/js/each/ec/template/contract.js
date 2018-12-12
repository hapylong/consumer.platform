$package('IQB.contract');
IQB.contract = function(){
	var _grid = null;
	var _this = {
		cache: {},
		getEcTypeName: function(value){
			var name = '';
			$.each(_this.cache.ecType, function(i, n){
				if(n.id == value){
					name = n.text;
				}
			});
			return name;
		},
		getSignerName: function(value){
			var name = '';
			$.each(_this.cache.signer, function(i, n){
				if(n.id == value){
					name = n.text;
				}
			});
			return name;
		},
		selectInint: function(){
			IQB.postAsync(urls['rootUrl'] + '/contractTemplate/getEcType', {}, function(result){	
				_this.cache.ecType = result.iqbResult.result;
			});
			IQB.post(urls['rootUrl'] + '/contractTemplateSigner/getSigner', {}, function(result){	
				_this.cache.signer = result.iqbResult.result;
			});
		},
		insert: function(){
			$('#maintainForm').prop('action', urls['rootUrl'] + '/contractTemplateSigner/insert');
			$('#maintainForm').form('reset');
			$('#ecSignerType').select2({theme: 'bootstrap', data: _this.cache.signer});
			$('.maintainDatagrid').hide();
			$('.maintainForm').show();			
		},
		updateAndRemoveOperation: function(val, row, rowIndex){
			var id = row.id;
			return '<button type="button" onClick="IQB.contract.update(' + id + ')" class="btn btn-link">修改</button><button type="button" onClick="IQB.contract.remove(' + id + ')" class="btn btn-link">删除</button>'
		},
		update: function(id){
			IQB.post(urls['rootUrl'] + '/contractTemplateSigner/getById', {id: id}, function(result){							
				$('#maintainForm').prop('action', urls['rootUrl'] + '/contractTemplateSigner/update');
				$('#maintainForm').form('load', result.iqbResult.result);	
				$('#ecSignerType').select2({theme: 'bootstrap', data: _this.cache.signer});
				$('#ecSignerType').val(result.iqbResult.result.ecSignerType).trigger('change');
				$('.maintainDatagrid').hide();
				$('.maintainForm').show();
			});
		},
		remove: function(id){
			IQB.confirm('确认要删除吗？', function(){
				var rows = $('#maintainDatagrid').datagrid2('getCheckedRows');
				var idField = $('#maintainDatagrid').datagrid2('getIdField'); 
		   		IQB.post(urls['rootUrl'] + '/contractTemplateSigner/remove', {id: id}, function(result){
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
			option.ecTplId = rows[0][idField];
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
   			$('#maintainDatagrid').datagrid2({url: urls['rootUrl'] + '/contractTemplateSigner/paging', singleCheck: true, queryParams: {ecTplId: rows[0][idField]}, pagination: false, paginator: 'maintainPaginator'});
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/contractTemplate/insert',
  				update: urls['rootUrl'] + '/contractTemplate/update',
  				getById: urls['rootUrl'] + '/contractTemplate/getById',
  				remove: urls['rootUrl'] + '/sysParamRest/deleteSysParam'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('#searchForm').find('select').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
				},
				insert: function(){
					_grid.handler.insert();		
					$('#ecTplType').select2({theme: 'bootstrap', data: _this.cache.ecType});
					$('#isSenderPartSign').val(1).trigger('change');
					$('#status').val(1).trigger('change');	
					$('#file').parent().parent().show();
					$('file').val('');
					$('#update-win-label').text('添加合同模板');
				},
				update: function(){
					_grid.handler.update(function(result){
						_grid.form.update.attr('action', _this.config.action.update);
						_grid.form.update.form('load', result.iqbResult.result);		
						$('#ecTplType').select2({theme: 'bootstrap', data: _this.cache.ecType});
						$('#ecTplType').val(result.iqbResult.result.ecTplType).trigger('change');
						$('#isSenderPartSign').val(result.iqbResult.result.isSenderPartSign).trigger('change');
						$('#status').val(result.iqbResult.result.status).trigger('change');	
						$('#ecType').val(result.iqbResult.result.ecType).trigger('change');	
						$('#file').parent().parent().hide();
						$('file').val('');
						$('#update-win-label').text('修改合同模板');
						_grid.win.update.modal({backdrop: 'static', keyboard: false, show: true});		
					});					
				},
				save: function(){
					if($('#file').val()){
						var url = _grid.form.update.prop('action') + 'WithFile';
						_grid.form.update.prop('action', url);
						IQB.postForm2(_grid.form.update, function(result){
							//禁用表单验证,处理页面遗留toolTip
							_grid.form.update.form('disableValidation');						
							_grid.handler.close();
							_grid.form.update.form('reset');
							//启用表单验证
							_grid.form.update.form('enableValidation');					    
							_grid.handler.refresh();		
						});
					}else{
						IQB.save(_grid.form.update, function(result){
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
	   			url: urls['rootUrl'] + '/contractTemplate/paging',
	   			singleCheck: true
			}
		},
		init: function(){ 
			_this.selectInint();
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化表格相关
			//slect2组件初始化
			$('#searchForm').find('select').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isSenderPartSign').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#ecType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#status').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#ecSignType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#ecSignerVideoType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#btn-upload').on('click', function(e){
				var rows = $('#datagrid').datagrid2('getCheckedRows');
				var idField = $('#datagrid').datagrid2('getIdField'); 
				if(rows != null && rows.length > 0){
					$('#uploadForm').find('input[name="id"]').val(rows[0][idField]);
					$('#uploadForm').find('input[name="file"]').click();
				}else{
					IQB.alert('未选中行');
				}
			});
			$('#btn-download').on('click', function(e){
				var rows = $('#datagrid').datagrid2('getCheckedRows');
				var idField = $('#datagrid').datagrid2('getIdField'); 
				if(rows != null && rows.length > 0){
					console.info();
					if(rows[0]['ecTplStorageForm']){
						var id = rows[0][idField];
						$('#downloadForm').prop('action', urls['rootUrl'] + '/contractTemplate/downloadFile/' + id);
						$('#downloadForm').submit();
					}else{
						IQB.alert('请先上传文件');
					}					
				}else{
					IQB.alert('未选中行');
				}
			});
			$('#btn-preview').on('click', function(e){
				var rows = $('#datagrid').datagrid2('getCheckedRows');
				var idField = $('#datagrid').datagrid2('getIdField'); 
				if(rows != null && rows.length > 0){
					if(rows[0]['ecTplContentDataBlob']){
						var id = rows[0][idField];
						$('#downloadForm').prop('action', urls['rootUrl'] + '/contractTemplate/previewFile/' + id);
						$('#downloadForm').submit();
					}else{
						IQB.alert('请先上传文件');
					}					
				}else{
					IQB.alert('未选中行');
				}
			});
			$('#btn-maintain').on('click', function(e){
				var rows = $('#datagrid').datagrid2('getCheckedRows');
				var idField = $('#datagrid').datagrid2('getIdField'); 
				if(rows != null && rows.length > 0){
					$('.maintainForm').hide();
					$('#maintainDatagrid').datagrid2({url: urls['rootUrl'] + '/contractTemplateSigner/paging', singleCheck: true, queryParams: {ecTplId: rows[0][idField]}, pagination: false, paginator: 'maintainPaginator'});
					$('#maintain-win').modal({backdrop: 'static', keyboard: false, show: true});
				}else{
					IQB.alert('未选中行');
				}
			});
			$('input[type="file"]').on('change', function(e){
				var target = e.target;
				var fileName = $(target).val();
				var id = $(target).prop('id');
				if(fileName){
					var ext = fileName.substr(fileName.lastIndexOf('.')).toLowerCase();
					if(ext == '.doc' || ext == '.docx'){
						if(!id){
							$('#uploadForm').prop('action', _this.config.action.update + 'WithFile');
							IQB.postForm2($('#uploadForm'), function(result){
								$(target).val('');
								IQB.alert('上传成功');	
								_grid.handler.refresh();
							});
						}
					}else{
						IQB.alert('附件格式错误');
						$(target).val('');
					}
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
	IQB.contract.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		