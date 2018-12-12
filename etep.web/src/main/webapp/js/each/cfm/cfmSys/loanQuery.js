$package('IQB.loanQuery');
IQB.loanQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/business/exportSelectLoanOrderList',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getSelectLoanOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				},
				reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
					$('select[name="lendersSubject"]').val(null).trigger('change');
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getSelectLoanOrderList',
	   			queryParams: {
	   			}
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var realName = $("#realName").val();
				var orderId = $("#orderId").val();
				var bizType = $("#bizType").val();
				var lendersSubject = $("#lendersSubject").val();
				var loanStartTime = $("#loanStartTime").val();
				var loanEndTime = $("#loanEndTime").val();
				var datas = "?merchNames=" + merchName + "&regId=" + regId + "&realName=" + realName + "&orderId=" + orderId + "&bizType=" + bizType + "&lendersSubject=" + lendersSubject+ "&loanStartTime=" + loanStartTime+ "&loanEndTime=" + loanEndTime;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		initSelect : function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('lendersSubject', 'Lenders_Subject');
			$('select[name="lendersSubject"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			_this.exports();//导出
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.loanQuery.init();
	datepicker(loanStartTime);
	datepicker(loanEndTime);
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
