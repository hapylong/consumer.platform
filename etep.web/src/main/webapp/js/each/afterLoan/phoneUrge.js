$package('IQB.phoneRemember');
IQB.phoneRemember = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/instRemindPhone/exportInstRemindPhoneList4'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var startDate = $('#startDate').val();
					var endDate = $('#endDate').val();
					if(startDate == '' && endDate == ''){
						$('#code').val('1');
					}else{
						$('#code').val('');
					}
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/instRemindPhone/queryTotalList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
							 singleCheck: true,
							 loadSuccess:function(date){
					   		 		//合计笔数
					   				$("#allTerms").val(date.iqbResult.totalNum);
					   		 }
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/instRemindPhone/queryTotalList',
	   			singleCheck: true,
	   			loadSuccess:function(date){
	   				//合计笔数
	   				$("#allTerms").val(date.iqbResult.totalNum);
	   			}
			}
		},
		exports : function(){
			var merchName = $("#merchNames").val();
			var orderId = $("#orderId").val();
			var realName = $("#realName").val();
			var regId = $("#regId").val();
			var billInfoStatus = $("#billInfoStatus").val();
			var status = $("#urgeStatus").val();
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var processStartDate = $("#processStartDate").val();
			var processEndDate = $("#processEndDate").val();
			var overdueDays = $("#overdueDays").val();
			var code = $('#code').val();
			var datas = "?merchNames=" + merchName + "&orderId=" + orderId + "&realName=" + realName
			            + "&regId=" + regId + "&billInfoStatus=" + billInfoStatus + "&status=" + status 
			            + "&startDate=" + startDate + "&endDate=" + endDate + "&code=" + code + "&flag=" + 2
			            + "&processStartDate=" + processStartDate + "&processEndDate=" + processEndDate
			            + "&overdueDays=" + overdueDays;
			var urls = _this.config.action.exports + datas;
            $("#btn-export").attr("href",urls);
		},
		detail : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				//datagrid2
				$("#datagrid2").datagrid2({url: urls['cfm'] + '/instRemindPhone/queryRecordList',paginator:'paginator2',queryParams : $.extend({}, {'orderId':records[0].orderId,'flag':2,'curItems':records[0].curItems})});
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
	      	       $('#open-win').modal('hide');
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			//导出
			$('#btn-export').unbind('click').on('click',function(){
				_this.exports();
			});
			//详情
			$('#btn-detail').unbind('click').on('click',function(){
				_this.detail();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.phoneRemember.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(processStartDate);
	datepicker(processEndDate);
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