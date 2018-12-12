$package('IQB.achievementQuery');
IQB.achievementQuery = function() {
	var _box = null;
    var _list = null;
	var _this = {
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
					getGuarnteeSelection:urls['cfm'] + '/customer/queryCustomerList'
	  			},
	  			dataGrid: {
	  				url: urls['cmf'] + '/achievementQuery/getAchievements',
	  				isExport: true,
	  				cols: '1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18'
				},
				event:{
					reset: function(){//重写save	
						_box.handler.reset();
						$('#query_stage_number').val(null).trigger('change');
						
					}
				}
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
	IQB.achievementQuery.init();
	datepicker(dateStart);
	datepicker(dateEnd);
});
$.getRiskstatus = function(val){
	if(val == ''){
		return "待上标";
	}
	if(val == '0'){
		return '待上标';
	}
	if(val == 1){
		return '已上标';
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