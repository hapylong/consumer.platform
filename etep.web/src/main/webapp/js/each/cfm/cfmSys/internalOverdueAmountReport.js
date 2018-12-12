$package('IQB.internalOverdueAmountReport');
IQB.internalOverdueAmountReport = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/admin/riskManagementXlsx/isInside',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/admin/riskManagement/isInside',
							 loadSuccess:function(data){
								 var data = data.iqbResult.resultInside;
								 if(data==null){
									 $(".totalOverduePrincipal").text("");
						   				$(".totalStockAmt").text("");
						   				$(".totalOverdueRate").text("");
						   				$(".totalOverduePrincipalMb5").text("");
						   				$(".totalOverdueRateMb5").text("");
						   				$(".totalOverduePrincipalMb1").text("");
						   				$(".totalOverdueRateMb1").text("");
						   				$(".totalOverduePrincipalMb2").text("");
						   				$(".totalOverdueRateMb2").text("");
						   				$(".totalOverduePrincipalMb3").text("");
						   				$(".totalOverdueRateMb3").text("");
						   				$(".totalOverduePrincipalMb4").text("");
						   				$(".totalOverdueRateMb4").text("");
								 }else{
					   				$(".totalOverduePrincipal").text(Formatter.money(data.totalOverduePrincipal));
					   				$(".totalStockAmt").text(Formatter.money(data.totalStockAmt));
					   				$(".totalOverdueRate").text(data.totalOverdueRate);
					   				$(".totalOverduePrincipalMb5").text(Formatter.money(data.totalOverduePrincipalMb5));
					   				$(".totalOverdueRateMb5").text(data.totalOverdueRateMb5);
					   				$(".totalOverduePrincipalMb1").text(Formatter.money(data.totalOverduePrincipalMb1));
					   				$(".totalOverdueRateMb1").text(data.totalOverdueRateMb1);
					   				$(".totalOverduePrincipalMb2").text(Formatter.money(data.totalOverduePrincipalMb2));
					   				$(".totalOverdueRateMb2").text(data.totalOverdueRateMb2);
					   				$(".totalOverduePrincipalMb3").text(Formatter.money(data.totalOverduePrincipalMb3));
					   				$(".totalOverdueRateMb3").text(data.totalOverdueRateMb3);
					   				$(".totalOverduePrincipalMb4").text(Formatter.money(data.totalOverduePrincipalMb4));
					   				$(".totalOverdueRateMb4").text(data.totalOverdueRateMb4);
								 }
					   			},
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/admin/riskManagement/isInside',
	   			singleCheck: true,
	   			queryParams: {
	   			},
	   			loadSuccess:function(data){
	   				var data = data.iqbResult.resultInside;
	   				$(".totalOverduePrincipal").text(Formatter.money(data.totalOverduePrincipal));
	   				$(".totalStockAmt").text(Formatter.money(data.totalStockAmt));
	   				$(".totalOverdueRate").text(data.totalOverdueRate);
	   				$(".totalOverduePrincipalMb5").text(Formatter.money(data.totalOverduePrincipalMb5));
	   				$(".totalOverdueRateMb5").text(data.totalOverdueRateMb5);
	   				$(".totalOverduePrincipalMb1").text(Formatter.money(data.totalOverduePrincipalMb1));
	   				$(".totalOverdueRateMb1").text(data.totalOverdueRateMb1);
	   				$(".totalOverduePrincipalMb2").text(Formatter.money(data.totalOverduePrincipalMb2));
	   				$(".totalOverdueRateMb2").text(data.totalOverdueRateMb2);
	   				$(".totalOverduePrincipalMb3").text(Formatter.money(data.totalOverduePrincipalMb3));
	   				$(".totalOverdueRateMb3").text(data.totalOverdueRateMb3);
	   				$(".totalOverduePrincipalMb4").text(Formatter.money(data.totalOverduePrincipalMb4));
	   				$(".totalOverdueRateMb4").text(data.totalOverdueRateMb4);
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
	IQB.internalOverdueAmountReport.init();
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