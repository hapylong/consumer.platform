$package('IQB.marginClearingQuery');
IQB.marginClearingQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/overdueInfo/exportMarginQuery'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/overdueInfo/marginQuery',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/overdueInfo/marginQuery'
			}
		},
		exports : function(){
			var merchName = $("#merchNames").val();
			var batchId = $("#batchId").val();
			var orderId = $("#orderId").val();
			var realName = $("#realName").val();
			var regId = $("#regId").val();
			var bizType = $("#bizType").val();
			var status = $("#status").val();
			var startTime = $("#startTimeQuery").val();
			var endTime = $("#endTimeQuery").val();
			var marginRatio = $("#marginRatio").val();
			var downPaymentRatio = $("#downPaymentRatio").val();
			var serviceFeeRatio = $("#serviceFeeRatio").val();
			var upInterestFee = $("#upInterestFee").val();
			var takePayment = $("#takePayment").val();
			var datas = "?merchNames=" + merchName + "&batchId=" + batchId + "&orderId=" + orderId + "&realName=" + realName + 
			            "&regId=" + regId  + "&bizType=" + bizType + "&status=" + status + "&startTimeQuery=" + startTime + "&endTimeQuery=" + endTime + 
			            "&marginRatio=" + marginRatio + "&downPaymentRatio=" + downPaymentRatio + "&serviceFeeRatio=" + serviceFeeRatio + 
			            "&upInterestFee=" + upInterestFee + "&takePayment=" + takePayment;
			console.log(datas);
			var urls = _this.config.action.exports + datas;
            $("#btn-export").attr("href",urls);
		},
		initSelect : function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			$('#btn-export').unbind('click').on('click',function(){_this.exports();});
			_this.initSelect();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.marginClearingQuery.init();
	datepicker(startTimeQuery);
	datepicker(endTimeQuery);
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