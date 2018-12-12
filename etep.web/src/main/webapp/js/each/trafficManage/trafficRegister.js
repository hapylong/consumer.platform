$package('IQB.trafficRegister');
IQB.trafficRegister = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('.merchantNo').val('');
					$(".merch").attr('merchantNo','');
					$(".merch").attr('orgCode','');
					$('select[name="carStatus"]').val(null).trigger('change');
					$('select[name="processStatus"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/trafficManage/selectTrafficManageOrderList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
								 loadSuccess:function(date){
						   				//笔数
						   				$("#allTerms").val(date.iqbResult.resultTotal.count);
						   			}
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/trafficManage/selectTrafficManageOrderList',
	   			singleCheck: true,
	   			loadSuccess:function(date){
	   				//笔数
	   				$("#allTerms").val(date.iqbResult.resultTotal.count);
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/trafficManage/selectTrafficManageOrderList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
				loadSuccess:function(date){
	   				//笔数
	   				$("#allTerms").val(date.iqbResult.resultTotal.count);
	   			}
			});
	    },
		register : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
                //跳转
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab("register", "补充资料", '/etep.web/view/trafficManage/registerInput.html?orderId=' + records[0].orderId + '&checkStatus=' + records[0].checkStatus, true, true, {lastTab: param});
			}else{
				IQB.alert('未选中行');
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('carStatus', 'carStatus');
			$('select[name="carStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('processStatus', 'CASEPROGRESS');
			$('select[name="processStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//补充资料
			$('#btn-register').unbind('click').on('click',function(){
				_this.register();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.trafficRegister.init();
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