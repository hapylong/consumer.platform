$package('IQB.commonparam');
IQB.commonparam = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
  				insert: urls['rootUrl'] + '/sysParamRest/insertSysParam',
  				update: urls['rootUrl'] + '/sysParamRest/updateSysParam',
  				getById: urls['rootUrl'] + '/sysParamRest/getSysParamById',
  				remove: urls['rootUrl'] + '/sysParamRest/deleteSysParam'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){
					_grid.handler.insert();
					_this.cache.readonly.val(1).trigger('change');
					_this.cache.isEnable.val(1).trigger('change');
					$('#update-win-label').text('添加业务参数');					
				},
				update: function(){
					_grid.handler.update(function(result){
						_grid.form.update.attr('action',_this.config.action.update);
						_grid.form.update.form('load',result.iqbResult.result);		
						_this.cache.readonly.val(result.iqbResult.result.readonly).trigger('change');
						_this.cache.isEnable.val(result.iqbResult.result.isEnable).trigger('change');
						$('#update-win-label').text('修改业务参数');
						_grid.win.update.modal({backdrop: 'static', keyboard: false, show: true});						
					});					
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/sysParamRest/getSysParam',
	   			queryParams: {
	   				type: 2
	   			}
			}
		},
		init: function(){
			_grid = new DataGrid(_this.config); 
			_grid.init();//初始化表格相关
			//slect2组件初始化并缓存
			_this.cache.readonly = $('#readonly').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.cache.isEnable = $('#isEnable').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.commonparam.init();
	//禁用浏览器右击菜单
	document.oncontextmenu = function(){return false;}
});		