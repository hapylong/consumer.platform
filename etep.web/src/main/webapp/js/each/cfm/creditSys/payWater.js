$package('IQB.payWater');
IQB.payWater = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/pay/getMersPaymentLogListForSave'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/pay/getMersPaymentLogList',
							 loadSuccess:function(date){
					   				//支付笔数与支付金额赋值
					   				$("#allTerms").val(date.iqbResult.totalMap.num);
					   				$("#allAmt").val(Formatter.moneyCent(date.iqbResult.totalMap.amt));
					   		 },
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/pay/getMersPaymentLogList',
	   			queryParams: {
	   				type: 1
	   			},
	   			loadSuccess:function(date){
	   				//支付笔数与支付金额赋值
	   				$("#allTerms").val(date.iqbResult.totalMap.num);
	   				$("#allAmt").val(Formatter.moneyCent(date.iqbResult.totalMap.amt));
	   			}
			}
		},
		toExport : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var orderId = $("#orderId").val();
				var regId = $("#regId").val();
				var startAmount = $("#startAmount").val();
				var endAmount = $("#endAmount").val();
				var datas = "?merchNames=" + merchName + "&startDate=" + startDate + "&endDate=" + endDate + "&orderId=" + orderId + "&regId=" + regId + "&startAmount=" + startAmount + "&endAmount=" + endAmount;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
		    _this.toExport();//导出
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.payWater.init();
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
