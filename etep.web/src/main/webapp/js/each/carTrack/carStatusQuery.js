$package('IQB.carStatusQuery');
IQB.carStatusQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				approveDetail: urls['cfm'] + '/carstatus/getApproveHtml',exports:urls['cfm'] + '/carstatus/export_cget_info_list'
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
					$('select[name="gpsStatus"]').val(null).trigger('change');
					$('select[name="caseProgress"]').val(null).trigger('change');
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
			IQB.get(_this.config.action.approveDetail, {'orderId':orderId}, function(result) {
				var procBizId = result.iqbResult.result.procBizId;
				var procDisplayurl = result.iqbResult.result.procDisplayurl;
				var procInstId = result.iqbResult.result.procInstId;
				_this.skipPage(procBizId,procInstId,procDisplayurl);
			})
		},
		approveGpsDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			window.parent.IQB.main2.openTab("activiti-seeGpsDetail", "gps详情",'/etep.web/view/carTrack/gpsStatusTraceDetailView.html?orderId=' + orderId, true, true, null);
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		}, exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var orderId = $("#orderId").val();
				var realName = $("#realName").val();
				var plate = $("#plate").val();
				var startDate = $("#lafterLoanDate").val();
				var endDate = $("#hafterLoanDate").val();
				//车辆状态
				var status = $("select[name='status']").val();
				//贷后状态
				var processStatus = $("select[name='processStatus']").val();
				//GPS状态获取
				var gpsStatus = $("select[name='gpsStatus']").val();
				var regId = $("#regId").val();
				var datas = "?merchNames=" + merchName + "&orderId=" + orderId + "&realName=" + realName + "&plate=" + plate + "&startDate=" + startDate+ "&endDate=" + endDate+ "&status=" + status + "&processStatus=" + processStatus + "&gpsStatus=" + gpsStatus;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('processStatus', 'CASEPROGRESS');
			$('select[name="processStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('status', 'carStatus');
			$('select[name="status"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.exports();//导出
			_this.initSelect();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.carStatusQuery.init();
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