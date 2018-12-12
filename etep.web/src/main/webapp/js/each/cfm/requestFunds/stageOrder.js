$package('IQB.stageOrder');
IQB.stageOrder = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/assetallocine/export_xlsx',
				stage: urls['cfm'] + '/assetallocine/order_break',
				forBill: urls['cfm'] + '/assetallocine/getAllBillByOrderId'
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
							 url: urls['cfm'] + '/assetallocine/cget_order_break_details',
							 onPageChanged : function(page){
					   				_this.cache.page = page;
					   				console.log(_this.cache.page);
					   		 },
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/assetallocine/cget_order_break_details',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/assetallocine/cget_order_break_details',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		stage : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var applyTime = row.sbTime;
			$('#open-win').modal('show');
			$("#beginDate").val(Formatter.timeCfm5(applyTime));
			$("#btn-install-sure").unbind("click").click(function(){
				  if($("#updateForm").form('validate')){
					  var beginDate = $("#beginDate").val();
					  var replaceStr = "-";
					  beginDate = beginDate.replace(new RegExp(replaceStr,'gm'),'');
					  var data = {
								'orderId':orderId,
								'beginDate':beginDate
					  };
					  IQB.get(_this.config.action.stage, data, function(result) {
						if(result.iqbResult.result.retCode == 'success'){
							IQB.alert('分期成功');
							$('#open-win').modal('hide');
							_this.refresh();
						}else{
							if(result.iqbResult.result.retCode == '10001'){
								IQB.alert('重复分期');
								$('#open-win').modal('hide');
								_this.refresh();
							}else{
								IQB.alert('分期失败');
								$('#open-win').modal('hide');
								_this.refresh();
							}
						}
					})
				  }; 
		    });
            $("#btn-close").click(function(){
      	       $('#open-win').modal('hide');
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
					$('#open-win2').modal('show');
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
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close2").click(function(){
        	    $('#open-win2').modal('hide');
			});
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var realName = $("#realName").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var datas = "?merchNames=" + merchName + "&regId=" + regId + "&realName=" + realName + "&startTime=" + startTime + "&endTime=" + endTime;
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
	IQB.stageOrder.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker(beginDate);
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