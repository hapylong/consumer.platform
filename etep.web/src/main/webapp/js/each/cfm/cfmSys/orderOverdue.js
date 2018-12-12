$package('IQB.orderOverdue');
IQB.orderOverdue = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		overdueInterest: function(val, row, rowIndex){
			var overdueInterest = val; 
			if(!isNaN(row.cutOverdueInterest)){
				overdueInterest = parseFloat(overdueInterest)-parseFloat(row.cutOverdueInterest);
			}
			return Formatter.money(overdueInterest);
		},
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/credit/export/All',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/credit/repayment/All',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/credit/repayment/All',
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var status = $("#status").val();
				var orderId = $("#orderId").val();
				var regId = $("#regId").val();
				var realName = $("#realName").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var curRepayDateBegin = $("#curRepayDateBegin").val();
				var curRepayDateEnd = $("#curRepayDateEnd").val();
				var datas = "?merchNames=" + merchName + "&status=" + status + "&orderId=" + orderId + "&regId=" + regId + "&startDate=" + startDate + "&endDate=" + endDate+ "&curRepayDateBegin=" + curRepayDateBegin + "&curRepayDateEnd=" + curRepayDateEnd + "&realName=" + realName; 
	            var urls = _this.config.action.exports + datas;
	            console.log(urls)
	            $("#btn-export").attr("href",urls);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.exports();//导出
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.orderOverdue.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(curRepayDateBegin);
	datepicker(curRepayDateEnd);
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