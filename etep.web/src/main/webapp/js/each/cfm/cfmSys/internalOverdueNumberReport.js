$package('IQB.internalOverdueNumberReport');
IQB.internalOverdueNumberReport = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/admin/riskManagementXlsx/int',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/admin/riskManagement/int',
							 loadSuccess:function(data){
								 var data = data.iqbResult.resultInside;
								 if(data==null){
									 $(".totalOverdueNum").text("");
						   				$(".totalOverdueStockNum").text("");
						   				$(".totalOverdueNumRate").text("");
						   				$(".totalOverdueNumMb5").text("");
						   				$(".totalOverdueNumRateMb5").text("");
						   				$(".totalOverdueNumMb1").text("");
						   				$(".totalOverdueNumhRateMb1").text("");
						   				$(".totalOverdueNumMb2").text("");
						   				$(".totalOverdueNumRateMb2").text("");
						   				$(".totalOverdueNumMb3").text("");
						   				$(".totalOverdueNumRateMb3").text("");
						   				$(".totalOverdueNumMb4").text("");
						   				$(".totalOverdueNumRateMb4").text("");
								 }else{
					   				$(".totalOverdueNum").text(data.totalOverdueNum);
					   				$(".totalOverdueStockNum").text(data.totalOverdueStockNum);
					   				$(".totalOverdueNumRate").text(data.totalOverdueNumRate);
					   				$(".totalOverdueNumMb5").text(data.totalOverdueNumMb5);
					   				$(".totalOverdueNumRateMb5").text(data.totalOverdueNumRateMb5);
					   				$(".totalOverdueNumMb1").text(data.totalOverdueNumMb1);
					   				$(".totalOverdueNumhRateMb1").text(data.totalOverdueNumhRateMb1);
					   				$(".totalOverdueNumMb2").text(data.totalOverdueNumMb2);
					   				$(".totalOverdueNumRateMb2").text(data.totalOverdueNumRateMb2);
					   				$(".totalOverdueNumMb3").text(data.totalOverdueNumMb3);
					   				$(".totalOverdueNumRateMb3").text(data.totalOverdueNumRateMb3);
					   				$(".totalOverdueNumMb4").text(data.totalOverdueNumMb4);
					   				$(".totalOverdueNumRateMb4").text(data.totalOverdueNumRateMb4);
								 }
					   			},
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/admin/riskManagement/int',
	   			singleCheck: true,
	   			queryParams: {
	   			},
	   			loadSuccess:function(data){
	   				var data = data.iqbResult.resultInside;
	   				$(".totalOverdueNum").text(data.totalOverdueNum);
	   				$(".totalOverdueStockNum").text(data.totalOverdueStockNum);
	   				$(".totalOverdueNumRate").text(data.totalOverdueNumRate);
	   				$(".totalOverdueNumMb5").text(data.totalOverdueNumMb5);
	   				$(".totalOverdueNumRateMb5").text(data.totalOverdueNumRateMb5);
	   				$(".totalOverdueNumMb1").text(data.totalOverdueNumMb1);
	   				$(".totalOverdueNumhRateMb1").text(data.totalOverdueNumhRateMb1);
	   				$(".totalOverdueNumMb2").text(data.totalOverdueNumMb2);
	   				$(".totalOverdueNumRateMb2").text(data.totalOverdueNumRateMb2);
	   				$(".totalOverdueNumMb3").text(data.totalOverdueNumMb3);
	   				$(".totalOverdueNumRateMb3").text(data.totalOverdueNumRateMb3);
	   				$(".totalOverdueNumMb4").text(data.totalOverdueNumMb4);
	   				$(".totalOverdueNumRateMb4").text(data.totalOverdueNumRateMb4);
	   			}
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var schedueDate = $("#schedueDate").val();
				var datas = "?merchNames=" + merchName + "&schedueDate=" + schedueDate;
	            var urls = encodeURI(_this.config.action.exports + datas);
	            $("#btn-export").attr("href",urls);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格 
			_this.exports();//导出
		}
	}
	return _this; 
}();

$(function(){
	//页面初始化
	IQB.internalOverdueNumberReport.init();
	datepicker(schedueDate);
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