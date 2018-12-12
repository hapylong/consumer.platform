$package('IQB.houseAccountBalanced');
IQB.houseAccountBalanced = function(){
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
				refund: urls['cfm'] + '/finance/billHandler/refund',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/finance/billHandler/houseQuery',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'channal':2})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/finance/billHandler/houseQuery',
	   			singleCheck: true,
	   			queryParams: {
	   				'channal': 2,
	   				'type':6
	   			},
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   			}
			}
		},
		refresh : function(page){
				$("#datagrid").datagrid2({url: urls['cfm'] + '/finance/billHandler/houseQuery',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'channal': 2})	
				});
		},
		getNowFormatDate : function() {
	          var date = new Date();
	          var seperator1 = "-";
	          var year = date.getFullYear();
	          var month = date.getMonth() + 1;
	          var strDate = date.getDate();
	          if (month >= 1 && month <= 9) {
	              month = "0" + month;
	          }
	          if (strDate >= 0 && strDate <= 9) {
	              strDate = "0" + strDate;
	          }
	          var currentdate = year + seperator1 + month + seperator1 + strDate;
	          return currentdate;
	    },
		accountBalance : function(){
			var rows;
			var orderId;
			var curRepayAmt;
			var repayNo;
			$("#btn-accountBalanced").click(function(){
				rows = $("#datagrid").datagrid2('getCheckedRows');
				if(rows.length > 0){
					orderId = rows[0].orderId;
					subOrderId = rows[0].subOrderId;
					curRepayAmt = rows[0].curRepayAmt;
					repayNo = rows[0].repayNo;
		    	      $('#open-win').modal('show');
		    	      $("#btn-accountBalanced-sure").removeAttr('disabled');
		    	      //赋值
				      $("#repayDate").val(_this.getNowFormatDate());
				      $("#curRepayAmt").val(curRepayAmt);
				      $("#tradeNo").val('');
				      $("#reason").val('');
					  $("#btn-accountBalanced-sure").unbind("click").click(function(){
							  if($("#updateForm").form('validate')){
								  //平账按钮置灰
								  $("#btn-accountBalanced-sure").attr('disabled',true);
								  var option = {
										  'orderId':orderId,
										  'subOrderId':subOrderId,
										  'repayDate':$('#repayDate').val(),
										  'tradeNo':$("#tradeNo").val(),
										  'reason':$("#reason").val(),
										  'repayNo':repayNo
								  };
								  IQB.post(_this.config.action.refund, option, function(result) {
									  if(result.iqbResult.result == 'success'){
										  IQB.alert('平账成功');
										  $('#open-win').modal('hide');
										  _this.refresh(_this.cache.page);
									  }else{
										  IQB.alert('平账失败');
										  $('#open-win').modal('hide');
									  }
								  })
							  }; 
					  });
	                  $("#btn-close").click(function(){
	                	 $('#open-win').modal('hide');
					  });
				}else{ 
					IQB.alert('未选中行');
				}
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.accountBalance();
			$('#btn-reset').click(function(){console.log(00);$('#type').val('0');});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.houseAccountBalanced.init();
	datepicker(createTime);
	datepicker(endTime);
	datepicker(repayDate);
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
