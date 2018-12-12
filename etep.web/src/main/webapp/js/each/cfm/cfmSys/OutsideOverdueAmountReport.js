$package('IQB.OutsideOverdueAmountReport');
IQB.OutsideOverdueAmountReport = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/admin/riskManagementXlsx/noInside',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/admin/riskManagement/noInside',
							 loadSuccess:function(data){
								 var data = data.iqbResult.resultInside;
								 if(data==null){
									 $(".totalOverdueMonth").text("");
						   				$(".totalStockAmt").text("");
						   				$(".totalOverdueMonthRate").text("");
						   				$(".totalOverdueMonthMb5").text("");
						   				$(".totalOverdueMonthRateMb5").text("");
						   				$(".totalOverdueMonthMb1").text("");
						   				$(".totalOverdueMonthRateMb1").text("");
						   				$(".totalOverdueMonthMb2").text("");
						   				$(".totalOverdueMonthRateMb2").text("");
						   				$(".totalOverdueMonthMb3").text("");
						   				$(".totalOverdueMonthRateMb3").text("");
						   				$(".totalOverdueMonthMb4").text("");
						   				$(".totalOverdueMonthRateMb4").text("");
								 }else{
					   				$(".totalOverdueMonth").text(Formatter.money(data.totalOverdueMonth));
					   				$(".totalStockAmt").text(Formatter.money(data.totalStockAmt));
					   				$(".totalOverdueMonthRate").text(data.totalOverdueMonthRate);
					   				$(".totalOverdueMonthMb5").text(Formatter.money(data.totalOverdueMonthMb5));
					   				$(".totalOverdueMonthRateMb5").text(data.totalOverdueMonthRateMb5);
					   				$(".totalOverdueMonthMb1").text(Formatter.money(data.totalOverdueMonthMb1));
					   				$(".totalOverdueMonthRateMb1").text(data.totalOverdueMonthRateMb1);
					   				$(".totalOverdueMonthMb2").text(Formatter.money(data.totalOverdueMonthMb2));
					   				$(".totalOverdueMonthRateMb2").text(data.totalOverdueMonthRateMb2);
					   				$(".totalOverdueMonthMb3").text(Formatter.money(data.totalOverdueMonthMb3));
					   				$(".totalOverdueMonthRateMb3").text(data.totalOverdueMonthRateMb3);
					   				$(".totalOverdueMonthMb4").text(Formatter.money(data.totalOverdueMonthMb4));
					   				$(".totalOverdueMonthRateMb4").text(data.totalOverdueMonthRateMb4);
								 }
					   			},
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/admin/riskManagement/noInside',
	   			singleCheck: true,
	   			queryParams: {
	   			},
	   			loadSuccess:function(data){
	   				var data = data.iqbResult.resultInside;
	   				$(".totalOverdueMonth").text(Formatter.money(data.totalOverdueMonth));
	   				$(".totalStockAmt").text(Formatter.money(data.totalStockAmt));
	   				$(".totalOverdueMonthRate").text(data.totalOverdueMonthRate);
	   				$(".totalOverdueMonthMb5").text(Formatter.money(data.totalOverdueMonthMb5));
	   				$(".totalOverdueMonthRateMb5").text(data.totalOverdueMonthRateMb5);
	   				$(".totalOverdueMonthMb1").text(Formatter.money(data.totalOverdueMonthMb1));
	   				$(".totalOverdueMonthRateMb1").text(data.totalOverdueMonthRateMb1);
	   				$(".totalOverdueMonthMb2").text(Formatter.money(data.totalOverdueMonthMb2));
	   				$(".totalOverdueMonthRateMb2").text(data.totalOverdueMonthRateMb2);
	   				$(".totalOverdueMonthMb3").text(Formatter.money(data.totalOverdueMonthMb3));
	   				$(".totalOverdueMonthRateMb3").text(data.totalOverdueMonthRateMb3);
	   				$(".totalOverdueMonthMb4").text(Formatter.money(data.totalOverdueMonthMb4));
	   				$(".totalOverdueMonthRateMb4").text(data.totalOverdueMonthRateMb4);
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
	IQB.OutsideOverdueAmountReport.init();
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