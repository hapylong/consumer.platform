$package('IQB.configQuery');
IQB.configQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
  				insert: urls['cfm'] + '/product/add',
  				update: urls['cfm'] + '/product/mod',
  				getById: urls['cfm'] + '/product/queryById',
  				remove: urls['cfm'] + '/product/del'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){
					_grid.handler.insert();
					$('#update-win-label').text('添加方案');
					$('#update-win #merchantNo').attr("disabled",false);
				},
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改方案');
					$('#update-win #merchantNo').attr("disabled",true);
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/product/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/product/query',
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.configQuery.init();
});		