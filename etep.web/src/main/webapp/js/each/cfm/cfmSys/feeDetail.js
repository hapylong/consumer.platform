$package('IQB.feeDetail');
IQB.feeDetail = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/orderImport/exportOrderAmtDetailList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/orderImport/selectOrderAmtDetail',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/orderImport/selectOrderAmtDetail'
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchNames = $("#merchNames").val();
				var realName = $("#realName").val();
				var regId = $("#regId").val();
				var orderId = $("#orderId").val();
				var riskStatus = $("#riskStatus").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var datas = "?merchNames=" + merchNames + "&regId=" + regId + "&realName=" + realName 
							+ "&riskStatus=" + riskStatus + "&startDate=" + startDate + "&endDate=" + endDate + "&orderId=" + orderId;
	            var urls = _this.config.action.exports + datas;
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
	IQB.feeDetail.init();
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
