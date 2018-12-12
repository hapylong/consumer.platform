$package('IQB.violationQuery');
IQB.violationQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/carPeccancy/exportInstCarPeccancyList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,pageSize:50})	
							});
				},
				reset: function(){
					_grid.handler.reset();
					$('select[name="carStatus"]').val(null).trigger('change');
				},
			},
  			dataGrid: {//表格参数  				
  				url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyList',
  				queryParams: {
  					pageSize:50
  				}
			} 
		},
		violationDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			$('#open-win').modal('show');
			$("#datagrid2").datagrid2({url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyDetailList',paginator: 'paginator1',queryParams : $.extend({}, {'vin': row.vin})});
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
		exports : function(){
			var merchantNames = $("#merchNames").val();
			var orderId = $("#orderId").val();
			var realName = $("#realName").val();
			var regId = $("#regId").val();
			var licenseNo = $("#licenseNo").val();
			var vin = $("#vin").val();
			var engineNumber = $("#engineNumber").val();
			var carStatus = $("#carStatus").val();
			var status = $("#status").val();
			var processStatus = $("#processStatus").val();
			var totalDegree = $("#totalDegree").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var startProcessDate = $("#startProcessDate").val();
			var endProcessDate = $("#endProcessDate").val();
			var startSendTime = $("#startSendTime").val();
			var endSendTime = $("#endSendTime").val();
			
			var datas = "?merchantNames=" + merchantNames + "&orderId=" + orderId + "&regId=" + regId + "&realName=" + realName 
						+ "&licenseNo=" + licenseNo + "&vin=" + vin + "&engineNumber=" + engineNumber + "&carStatus=" + carStatus + "&status=" + status
						+ "&processStatus=" + processStatus + "&totalDegree=" + totalDegree + "&startDate=" + startDate + "&endDate=" + endDate
						+ "&startProcessDate=" + startProcessDate + "&endProcessDate=" + endProcessDate + "&startSendTime=" + startSendTime + "&endSendTime=" + endSendTime;
            var urls = _this.config.action.exports + datas;
            $("#btn-export").attr("href",urls);
		},
		initSelect : function(){
			IQB.getDictListByDictType2('carStatus', 'carStatus');
			$('select[name="carStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//导出
			$('#btn-export').unbind('click').on('click',function(){
				_this.exports();
			});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.violationQuery.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(startProcessDate);
	datepicker(endProcessDate);
	datepicker(startSendTime);
	datepicker(endSendTime);
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