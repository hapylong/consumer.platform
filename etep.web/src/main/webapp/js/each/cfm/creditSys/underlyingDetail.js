$package('IQB.underlyingDetail');
IQB.underlyingDetail = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/assetAllocation/exportAssetObjectInfo'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset:function(){
					$('select[name="sourcesFunding"]').val(null).trigger('change');
					$('select[name="lendersSubject"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/assetAllocation/selectAssetObjectInfo',
							 loadSuccess:function(date){
					   				//合计笔数
					   				$("#allTerms").val(date.iqbResult.totalCount);
					   		 },
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/assetAllocation/selectAssetObjectInfo',
	   			loadSuccess:function(date){
	   				//合计笔数
	   				$("#allTerms").val(date.iqbResult.totalCount);
	   			}
			}
		},
		toExport : function(){
			$("#btn-export").click(function(){
				var merchNames = $("#merchNames").val();
				var orderId = $("#orderId").val();
				var realName = $("#realName").val();
				var regId = $("#regId").val();
				
				var projectName = $("#projectName").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				
				var sourcesFunding = $("#sourcesFunding").val();
				var deadLineStartTime = $("#deadLineStartTime").val();
				var assetDueDate = $("#assetDueDate").val();
				var lendersSubject = $("#lendersSubject").val();	//放款主体	
				
				var datas = "?orderId=" + orderId + "&realName=" + realName + "&regId=" + regId + "&merchNames=" + merchNames + 
				"&projectName=" + projectName + "&startTime=" + startTime + "&endTime=" + endTime+
				"&sourcesFunding=" + sourcesFunding + "&deadLineStartTime=" + deadLineStartTime + "&assetDueDate=" + assetDueDate + "&lendersSubject=" + lendersSubject;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		initSelect: function(){
			IQB.getDictListByDictType2('sourcesFunding', 'FUND_SOURCE');
			$('select[name="sourcesFunding"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('lendersSubject', 'Lenders_Subject');
			$('select[name="lendersSubject"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
		    _this.toExport();//导出
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.underlyingDetail.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker(deadLineStartTime);
	datepicker(assetDueDate);
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
