$package('IQB.orderQuery');
IQB.orderQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/carOwner/exportCarOwnerOrderInfo',
				forBill : urls['cfm'] + '/business/getAllBillByOrderId'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改订单');
					$('#update-win #merchantNo').attr("disabled",true);
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/carOwner/getCarOwnerOrderInfo',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/carOwner/getCarOwnerOrderInfo',
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var orderId = $("#orderId").val();
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var userName = $("#userName").val();
				var riskStatus = $("#riskStatus").val();
				var wfStatus = $("#wfStatus").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var sbStartTime = $("#sbStartTime").val();
				var sbEndTime = $("#sbEndTime").val();
				var loanStartTime = $("#loanStartTime").val();
				var loanEndTime = $("#loanEndTime").val();
				//var	type = $("#type").val();
				//var applyTime = $("#applyTime").val();
				var datas = "?&merchNames=" + merchName + "&regId=" + regId + "&userName=" + userName 
							+ "&riskStatus=" + riskStatus + "&wfStatus=" + wfStatus + "&startTime=" + startTime + "&loanStartTime=" + loanStartTime 
							+ "&endTime=" + endTime + "&loanEndTime=" + loanEndTime + "&orderId=" + orderId + "&type=1"+ "&sbStartTime="+sbStartTime+ "&sbEndTime="+sbEndTime;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		forBill : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var regId = row.regId;
			var realName = row.realName;
			IQB.get(_this.config.action.forBill, {'orderId':orderId,'regId':regId}, function(result) {
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win').modal('show');
					//赋值
					$("#billRealName").val(realName);
					$("#billOrderId").val(orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+Formatter.money(result[i].fixedOverdueAmt)+"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
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
	IQB.orderQuery.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker(loanStartTime);
	datepicker(loanEndTime);
	datepicker(sbStartTime);
	datepicker(sbEndTime);
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
