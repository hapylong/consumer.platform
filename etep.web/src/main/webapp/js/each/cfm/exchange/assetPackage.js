$package('IQB.assetPackage');
IQB.assetPackage = function(){
	var _grid = null;
	var _tree = null;
	var _this = {		
		cache :{
			't':''
		},
		config: {
			action: {//页面请求参数
			    
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/listJYSOrderInfo',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'status':3})	
							});
				},
				reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
				},
			},
  			dataGrid: {//表格参数  				
  				url: urls['cfm'] + '/business/listJYSOrderInfo',
	   			singleCheck: true,
	   			queryParams: {
	   				'status':3
	   			},
	   			onPageChanged : function(page){
	   				console.log(page);
	   				_this.refresh(page);
	   			}
			}
		},
		packages : function(){
			var rows = _grid.util.getCheckedRows();	
			if (_grid.util.checkSelect(rows)){
			    //跳页打包资产
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab("packageSub", "资产打包提交", '/etep.web/view/cfm/exchange/packageSub.html?orderId='+rows[0]['orderId']+'&id='+rows[0]['id'], true, true, {lastTab: param});
			}
		},
		initSelect: function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		query : function(){
			$("#btn-search").click(function(){
				_this.refresh(1);
			})
		},
		refresh : function(page){
			clearInterval(_this.cache.t);
			_this.cache.t = setInterval(function(){
				$("#datagrid").datagrid2({url: urls['cfm'] + '/business/listJYSOrderInfo',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'status':3}),
					onPageChanged : function(page){
		   				console.log(page);
		   				_this.refresh(page);
		   			}	
				});
			},10000);
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config);
			_grid.init();
			_this.initSelect();
			$("#btn-package").click(function(){
				_this.packages();
			});
			_this.query();
            _this.refresh(1);
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.assetPackage.init();
	datepicker(startDate);
	datepicker(endDate);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};
