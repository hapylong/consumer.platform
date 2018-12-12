$package('IQB.gpsStatusTrace');
IQB.gpsStatusTrace = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			approveDetail: urls['cfm'] + '/carstatus/getApproveHtml',
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
							 url: urls['cfm'] + '/carstatus/cget_info_list',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,channal: 2})	
							});
				},
				reset: function(){
					_grid.handler.reset();
					$('select[name="status"]').val(null).trigger('change');
				},
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/carstatus/cget_info_list',
	   			singleCheck: true,
	   			queryParams: {
	   				"channal":"2"
	   			}
			}
		},
		approveDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			IQB.get(urls['cfm'] + '/carstatus/getApproveHtml', {'orderId':orderId}, function(result) {
				var procBizId = result.iqbResult.result.procBizId;
				var procDisplayurl = result.iqbResult.result.procDisplayurl;
				var procInstId = result.iqbResult.result.procInstId;
				_this.skipPage(procBizId,procInstId,procDisplayurl);
			})
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		//跳转订单详情页面
		gotoGps: function(){
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				var orderId = records[0].orderId;
				window.parent.IQB.main2.openTab("activiti-seeGpsDetail", "GPS详情",'/etep.web/view/carTrack/gpsStatusTraceDetail.html?orderId=' + orderId, true, true, null);
			}else{
				IQB.alert('未选中行');
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('status', 'carStatus');
			$('select[name="status"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			$('#btn-gps').on('click',function(){_this.gotoGps()});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.gpsStatusTrace.init();
	datepicker(lafterLoanDate);
	datepicker(hafterLoanDate);
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