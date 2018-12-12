$package('IQB.houeOrderOverdue');
IQB.houeOrderOverdue = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		overdueInterest: function(val, row, rowIndex){
			var overdueInterest = val; 
			if(!isNaN(row.cutOverdueInterest)){
				overdueInterest = parseFloat(overdueInterest)-parseFloat(row.cutOverdueInterest);
			}
			return Formatter.money(overdueInterest);
		},
		cache :{
			orgCode:'',
			merchantNo:''
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/finance/billHandler/houseQuery/export',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/finance/billHandler/houseQuery',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'channal':2}),
							 isExport : true,
					   		 cols : '1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18',
					   		 filename: 'order_overdue_%YY%-%MM%-%DD%-%hh%-%mm%-%ss%'	 
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/finance/billHandler/houseQuery',
	   			queryParams: {
	   				'channal': 2,
	   				'type':6
	   			},
	   			isExport : true,
	   			cols : '1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18',
	   			filename: 'order_overdue_%YY%-%MM%-%DD%-%hh%-%mm%-%ss%'
			}
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchNames = $("#merchNames").val();
				var realName = $("#realName").val();
				var orderId = $("#orderId").val();
				var songsong = $("#songsong").val();
				if(songsong == undefined){
					songsong = '';
				}
				var createTime = $("#createTime").val();
				var endTime = $("#endTime").val();
				var type = $("#type").val();
				var repayTime = $("#repayTime").val();
				var datas = "?merchNames=" + merchNames + "&realName=" + realName + "&orderId=" + orderId 
							+ "&songsong=" + songsong + "&createTime=" + createTime + "&endTime=" + endTime 
							+ "&type=" + type + "&repayTime=" + repayTime;
				console.log(datas);
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			/*_this.exports();//导出
*/			$('#btn-reset').click(function(){$('#type').val('0');});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.houeOrderOverdue.init();
	datepicker(createTime);
	datepicker(endTime);
	datepicker(repayTime);
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