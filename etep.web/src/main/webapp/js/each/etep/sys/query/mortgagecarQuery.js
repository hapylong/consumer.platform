$package('IQB.mortgagecarQuery');
IQB.mortgagecarQuery = function() {
	var _box = null;
    var _list = null;
	var _this = {
			cache :{
				
			},
			initguarnteeSelection:function(){
				var data = {customerType:"5"};
				IQB.get(_this.config.action.getGuarnteeSelection,data,function(result){
					jQuery("select[name='guarntee']").prepend("<option value=''>全部</option>");
					var arr = result.iqbResult.result.list;
					$(arr).each(
							function(index) {
								jQuery("select[name='guarntee']").append("<option value='" + this.customerName + "'>"+ this.customerName+ "</option>");
							});
				}) ;
			},
			config: {
				action: {
					exports: urls['cmf'] + '/business/exportOrderList',
					getGuarnteeSelection:urls['cfm'] + '/customer/queryCustomerList'
	  			},
	  			dataGrid: {
	  				url: urls['cmf'] + '/mortgagecarQuery/getAllMortgagecars',
	  				isExport: true,
	  				cols: '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17'
				},
				event:{
					reset: function(){//重写save	
						_box.handler.reset();
						$('#query_stage_number').val(null).trigger('change');
					},
					search:function(){
						var orgCode = $(".merch").attr('orgCode');
						_this.cache.orgCode = orgCode;
						var merchantNo = $(".merch").attr('merchantNo');
						_this.cache.merchantNo = merchantNo;
						$("#datagrid").datagrid2
								({
								 url: urls['cfm'] + '/mortgagecarQuery/getAllMortgagecars',
								 queryParams : 
									 $.extend({}, $("#searchForm").serializeObject(), 
											 {'orgCode': orgCode,'merchantNo':merchantNo})	
								});
					}
				}
			},
			exports : function(){
				$("#btn-export").click(function(){
					var merchName = $("#merchNames").val();
					var orderId = $("#orderId").val();
					var regId = $("#regId").val();
					var realName = $("#realName").val();
					var status = $("#status").val();
					var lastRepayDate = $("#lastRepayDate").val();
					
					
					var	type = $("#type").val();
					var datas = "?merchNames=" + merchName + "&regId=" + regId + "&realName=" + realName 
								+ "&status=" + status +"&lastRepayDate=" + lastRepayDate+"&orderId=" + orderId;
		            var urls = _this.config.action.exports + datas;
		            $("#btn-export").attr("href",urls);
				});
			},
			
			init: function(){
				_box = new DataGrid2(_this.config); 
	            _list = new Tree(_this.config); 
				_box.init();
				_this.initguarnteeSelection();
				IQB.getDictListByDictType('query_stage_number', 'STAGE_NUMBER');
				$('#query_stage_number').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.mortgagecarQuery.init();
	datepicker(lastRepayDate);
});
$.getRiskstatus = function(val){
	if(val == 0){
		return '逾期';
	}
	if(val == 1){
		return '未还款';
	}
	if(val == 2){
		return '部分还款';
	}
	if(val == 3){
		return '已还款';
	}
	return '';
  }
$.formatId = function(val){
	if(val == 'undefined' || val ==0){
		return "";
	}else{
		return val;
	}
}
$.getTotal = function(val, row, rowIndex){
	
	if(row.margin == '' || row.margin == null){
		row.margin = 0;
	}else if(row.downPayment == '' || row.downPayment == null){
		row.downPayment = 0;
	}else if(row.serviceFee == '' || row.serviceFee == null){
		row.feeAmount = 0;
	}
	
	var total = (parseInt(row.margin) + parseInt(row.downPayment) + parseInt(row.serviceFee));
	return Formatter.money(total);
}
$.formatReqId = function(val){
	var result =  parseInt(val) + 1;
	return	result;
	
}
$.formatMonthRatio = function(val){
	var result = 0;
	if(val !='0'){
		result = val+"%";	
	}
	return result;
}
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