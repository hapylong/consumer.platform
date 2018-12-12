$package('IQB.withholdPayment');
IQB.withholdPayment = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		//催收
		deduct:function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				var params = [];
				$.each(rows,function(i,n){
					params.push({});
					params[i].orderId = n.orderId;
					params[i].repayNo = n.repayNo;
				})
				IQB.confirm('确认要划扣吗？', function(){
					IQB.get(_this.config.action.deductUrl, {'params':params}, function(result) {
						if(result.iqbResult.result == 'success'){
							IQB.alert('已发送！');
							_grid.handler.refresh();
						}
					})
				});
			}else{				
				IQB.alert("未选中行");
			}
		},
		checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		 $('#datagrid').find('tbody').find('tr').each(function(i){
						  $(this).find('.datagrid-row-checkbox:not(:disabled)').prop('checked', true); 
					 }); 
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
        exports : function(){
        	var merchantNo = $("#merchantNo").val();
			var orderId = $("#orderId").val();
			var realName = $("#realName").val();
			var regId = $("#regId").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var billStatus = $("#billStatus").val();
			var status = $("#status").val();
			var datas = "?merchantNo=" + merchantNo + "&orderId=" + orderId+ "&realName=" + realName + "&regId=" + regId + "&startTime=" + startTime + "&endTime=" + endTime+ "&billStatus=" + billStatus + "&status=" + status; 
            var urls = _this.config.action.exports + datas;
            $("#btn-export").attr("href",urls);
        },
		config: {
			action: {//页面请求参数
				deductUrl : urls['cfm']+ '/settlementresultHold/billWithHold',
				exports : urls['cfm']+ '/settlementresultHold/exportBillList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/settlementresultHold/query'
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			$('#btn-deduct').unbind('click').on('click',function(){_this.deduct()});
			$('#btn-export').unbind('click').on('click',function(){_this.exports()});
			_this.checkAll();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.withholdPayment.init();
	datepicker(startTime);
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