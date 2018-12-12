$package('IQB.fwQuery');
IQB.fwQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/afterLoan/exportAfterCaseOrderLawList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('.merchantNo').val('');
					$(".merch").attr('merchantNo','');
					$(".merch").attr('orgCode','');
					$('select[name="carStatus"]').val(null).trigger('change');
					$('select[name="caseStatus"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectAfterCaseOrderLawList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
								 loadSuccess:function(date){
						   				//笔数
						   				$("#allTerms").val(date.iqbResult.resultTotal.caseLawCount);
						   			}
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectAfterCaseOrderLawList',
	   			singleCheck: true,
	   			loadSuccess:function(date){
	   				//笔数
	   				$("#allTerms").val(date.iqbResult.resultTotal.caseLawCount);
	   			}
			}
		},
		approveDetail : function(data){
			var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var caseId = row.caseId;
			window.parent.IQB.main2.openTab("fw-progress", "法务查询", '/etep.web/view/afterLoan/caseQuery/fwQueryDetail.html?orderId=' + orderId + '&caseId=' + caseId, true, true, {lastTab: param});
		},
		exports : function(){
			var merchNames = $('#merchNames').val();
			var merchantNo = $('.merchantNo').val();
			var orgCode = $(".merch").attr('orgCode');
			var orderId = $("input[name='orderId']").val();
			var realName = $("input[name='realName']").val();
			var regId = $("input[name='regId']").val();
			var carStatus = $("select[name='carStatus']").val();
			var caseStatus = $("select[name='caseStatus']").val();
			var startTime = $("input[name='startTime']").val();
			var endTime = $("input[name='endTime']").val();
			var caseNo = $("input[name='caseNo']").val();
			var datas = "?merchNames= "+ merchNames +" &merchantNo=" + merchantNo +" &orgCode=" + orgCode + '&orderId=' + orderId + '&realName=' + realName + '&regId=' + regId +
			            '&carStatus=' + carStatus + '&caseStatus=' + caseStatus + '&startTime=' + startTime + '&endTime=' + endTime + '&caseNo=' + caseNo;
			var urls = encodeURI(_this.config.action.exports + datas);
            $("#btn-export").attr("href",urls);
		},
		initSelect : function(){
			IQB.getDictListByDictType2('carStatus', 'carStatus');
			$('select[name="carStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.getDictListByDictType2('caseStatus', 'fwStatus');
			$('select[name="caseStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
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
	IQB.fwQuery.init();
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