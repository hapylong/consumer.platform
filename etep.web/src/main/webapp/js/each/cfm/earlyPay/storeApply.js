$package('IQB.storeApply');
IQB.storeApply = function(){
	var _grid = null;
	var _tree = null;
	var _box = null;
	var _list = null;
	var _this = {
		cache :{
			page : 1
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/settle/selectSettleOrderListExle',
				approveDetail: urls['cfm'] + '/carstatus/getApproveHtml'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/settle/selectSettleOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/settle/selectSettleOrderList',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   			},
	   			singleCheck: true
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchantShortName = $("#merchantShortName").val();
				var orderId = $("#orderId").val();
				var realName = $("#realName").val();
				var regId = $("#regId").val();
				var beginTime = $("#beginTime").val();
				var endTime = $("#endTime").val();
				var settleStatus = $("#settleStatus").val();
				var datas = "?merchantShortName=" + merchantShortName + "&orderId=" + orderId+ "&realName=" + realName + "&regId=" + regId + "&beginTime=" + beginTime + "&endTime=" + endTime+ "&settleStatus=" + settleStatus; 
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		approveDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var id = row.id;
			orderId = orderId + '-' + id;
			IQB.get(_this.config.action.approveDetail, {'orderId':orderId,"procKey": "prepayment_order"}, function(result) {
				if(result.iqbResult.result != null && result.iqbResult.result != ''){
					var procBizId = result.iqbResult.result.procBizId;
					var procDisplayurl = result.iqbResult.result.procDisplayurl;
					var procInstId = result.iqbResult.result.procInstId;
					_this.skipPage(procBizId,procInstId,procDisplayurl);
				}else{
					IQB.alert('未启动提前退租流程');
				}
			})
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		apply : function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				IQB.post(urls['cfm'] + '/settle/validateIPenaltyDerate', {orderId: records[0].orderId}, function(result){
					if(result.iqbResult.result == 'success'){
						//允许提前还款
						window.parent.IQB.main2.openTab("apply-detail", "门店申请", '/etep.web/view/cfm/earlyPay/applyDetail.html?procTaskId='+records[0]['procTaskId']+"&procBizid="+records[0]['orderId']+"&procInstId="+records[0]['procInstId']+"&procOrgCode="+records[0]['procOrgCode'], true, true, {lastTab: param});
					}
				})
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_box = new DataGrid2(_this.config);
			_box.init();
			$("#btn-apply").click(function(){
				_this.apply();
			});
			_this.exports();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.storeApply.init();
	datepicker(beginTime);
	datepicker(endTime);
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