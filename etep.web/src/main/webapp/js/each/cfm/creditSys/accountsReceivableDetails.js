$package('IQB.accountsReceivableDetails');
IQB.accountsReceivableDetails = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		overdueInterest: function(val, row, rowIndex){
			var overdueInterest = val; 
			if(!isNaN(row.cutOverdueInterest)){
				overdueInterest = parseFloat(overdueInterest)-parseFloat(row.cutOverdueInterest);
			}
			return Formatter.money(overdueInterest);
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/credit/exportShouldDebtDetailList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/credit/getShouldDebtDetail',
							 loadSuccess:function(date){
					   				//支付金额赋值
					   				$("#allAmt").val(Formatter.money(date.iqbResult.totalAmt));
					   				$("#allTerms").val(date.iqbResult.totalCount);
					   		 },
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/credit/getShouldDebtDetail',
	   			queryParams: {
	   				type: 1
	   			},
	   			loadSuccess:function(date){
	   				//支付金额赋值
	   				$("#allAmt").val(Formatter.money(date.iqbResult.totalAmt));
	   				$("#allTerms").val(date.iqbResult.totalCount);
	   			}
			}
		},
		toExport : function(){
			$("#btn-export").click(function(){
				var orderId = $("#orderId").val();
				var realName = $("#realName").val();
				var regId = $("#regId").val();
				var merchCode = $("#merchNames").val();
				var sourcesFunding = $("#sourcesFunding").val();
				var fundId = $("#fundId").val();
				var status = $("#status").val();		
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var datas = "?orderId=" + orderId + "&realName=" + realName + "&regId=" + regId + "&merchNames=" + merchCode + "&sourcesFunding=" + sourcesFunding + "&fundId=" + fundId + "&status=" + status+ 
				"&startDate=" + startDate + "&endDate=" + endDate;
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
	IQB.accountsReceivableDetails.init();
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
