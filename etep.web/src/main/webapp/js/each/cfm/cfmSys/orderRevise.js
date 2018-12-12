$package('IQB.orderRevise');
IQB.orderRevise = function(){
	var _grid = null;
	var _tree = null;
	var _this = {		
		cache :{
			eq:0
		},
		plan : function(eq) {
			var totalAmount = $("#orderAmt").val();
			if(totalAmount != "" && totalAmount != 0){
				//首付
				var downPaymentRatio = parseFloat(_this.cache.prodList[eq].downPaymentRatio)*0.01;
				var downPayment = parseFloat(totalAmount*downPaymentRatio).toFixed(2);
				$("#downPayment").val(downPayment);
				//期数
				var orderTerms = _this.cache.prodList[eq].installPeriods ;
				$("#orderItems").val(orderTerms);
				//上收息（月）
				var feeYear =_this.cache.prodList[eq].feeYear;
				//$("#feeYear").val(feeYear);
				//是否上收月供
				var takePayment = _this.cache.prodList[eq].takePayment ;
				if(takePayment == 0){
					$("#takePayment").val(0);
				}else{
					$("#takePayment").val(1);
				}
 				//月利息
				var feeRatio = parseFloat(_this.cache.prodList[eq].feeRatio)*0.01;
				var fee = (totalAmount - downPayment)*feeRatio;
				//上收息
				var preFee = parseFloat(fee*(_this.cache.prodList[eq].feeYear)).toFixed(2);
				$("#feeAmount").val(preFee);
				//剩余利息
				var lateFee = fee*(_this.cache.prodList[eq].installPeriods-_this.cache.prodList[eq].feeYear);
				//月供金额
				var amountTotal = (totalAmount-downPayment)+lateFee;
				var amount = parseFloat(amountTotal/(_this.cache.prodList[eq].installPeriods)).toFixed(2);
				amount = Math.floor(amount);
				$("#monthInterest").val(amount);
				//保证金
				var marginRatio = parseFloat(_this.cache.prodList[eq].marginRatio)*0.01;
				var margin = parseFloat(totalAmount*marginRatio).toFixed(2);
				$("#margin").val(margin);
				//服务费
				var serviceFeeRatio = parseFloat(_this.cache.prodList[eq].serviceFeeRatio)*0.01;
				var serviceFee = parseFloat(totalAmount*serviceFeeRatio).toFixed(2);
				$("#serviceFee").val(serviceFee);
				//预付款
                var preAmount = parseFloat(downPayment + margin + serviceFee + preFee + takePayment*amount); 
				$("#preAmount").val(preAmount);
			}
		},
		cal : function(){
			var totalAmount = $("#orderAmt").val();
			if($("#planShortName").val() != ""){
				if(totalAmount != "" && totalAmount != 0 && totalAmount != 0.00 && totalAmount != 0.0 ){
					_this.plan(_this.cache.eq);
				}else if(totalAmount == 0 || totalAmount == ""){
					$("#downPayment").val('');
					$("#margin").val('');
					$("#serviceFee").val('');
					$("#orderItems").val('');
					$("#monthInterest").val('');
					$("#feeYear").val('');
					$("#takePayment").val('');
					$("#preAmount").val('');
					$("#feeAmount").val('');
				}
			}
		},
		config: {
			action: {//页面请求参数
  				update: urls['cfm'] + '/business/modifyOrder',
  				getById: urls['cfm'] + '/business/getOrderInfo',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					var records = _grid.util.getCheckedRows();
					if(_grid.util.checkSelectOne(records)){
						var option = {orderId: records[0].orderId};				    	
				    	IQB.get(_this.config.action.getById, option, function(result){	
				    		//计划
				    		_this.cache.prodList = result.iqbResult.prodList;
				    		/*回显方案*/
				    		if(_this.cache.prodList.length > 0){
				    			for(var i=0;i<_this.cache.prodList.length;i++){
					    			$("#planId").append("<option  id='"+i+"' value='" + _this.cache.prodList[i].id + "'>"+ _this.cache.prodList[i].planFullName+ "</option>")
					    		}
				    		}
				    		$('#updateForm').attr('action', _this.config.action.update);
				    		$('#updateForm').form('load', result.iqbResult.result);
				    		$("#planId").val(result.iqbResult.result.planId);
				    		$("#merchCode").val(result.iqbResult.result.merchName);
				    		$('#update-win-label').text('修改订单');
				    		$('#update-win').modal({backdrop: 'static', keyboard: false, show: true});			    		
						});
				    	//选择计划
				    	$("#planId").change(function(){
				    		_this.cache.eq = $('#planId option:selected').attr('id');
				    		_this.plan(_this.cache.eq);
				    	});
					}					
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getOrderList',
	   			singleCheck: true,
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.orderRevise.init();
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