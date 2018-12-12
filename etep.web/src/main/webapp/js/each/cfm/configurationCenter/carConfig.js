$package('IQB.carConfig');
IQB.carConfig = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
  				insert: urls['cfm'] + '/car/add',
  				update: urls['cfm'] + '/car/mod',
  				getById: urls['cfm'] + '/car/queryById',
  				remove: urls['cfm'] + '/car/del',
  				getMerCodeInfo : urls['cfm']+ '/merchant/getMerList',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){
					_grid.handler.insert();
					$('#update-win-label').text('添加车型');
					$('#update-win #merchantNo').addClass("merchModel");
					$('#update-win #merchantNo').attr("onclick","showMenuModel();return false;");
					$("#menuContentModel").css("max-height","105px");
					if($("#update-win")){$("#update-win").addClass("z-index")};
					if($(".modal-backdrop")){$(".modal-backdrop").addClass("z-index2")};
					//orgCode&&merchantNo
					$('.orgCode').val($(".merchModel").attr('orgCode'));
					$('.merchantNo').val($(".merchModel").attr('merchantNo'));
					$("#btn-save").click(function(){
						$("#update-win").removeClass("z-index");
						$("#menuContentModel").hide();
						});
					$("#btn-close").click(function(){
						$("#update-win").removeClass("z-index");
						$("#menuContentModel").hide();
						});
				}, 
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改车型');
					$('#update-win #merchantNo').removeClass("merchModel");
					$('#update-win #merchantNo').removeAttr("onclick");
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/car/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/car/query',
	   			singleCheck: true,
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
	IQB.carConfig.init();
});		