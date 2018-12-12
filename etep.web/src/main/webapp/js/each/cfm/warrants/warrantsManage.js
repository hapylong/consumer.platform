$package('IQB.warrantsManage');
IQB.warrantsManage = function(){
	var _grid = null;
	var _tree = null;
	var _box = null;
	var _list = null;
	var _this = {
		cache :{
			page : 1
		},
		config: {
			action: {//页面请求参数
  				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getAuthorityOrderList',
							 loadSuccess:function(date){
					   				//支付金额赋值
					   				$("#allTerms").val(date.iqbResult.totalCount);
					   		 },
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getAuthorityOrderList',
	   			loadSuccess:function(date){
	   				//支付金额赋值
	   				$("#allTerms").val(date.iqbResult.totalCount);
	   			},
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   			},
	   			singleCheck: true
			}
		},
		forInfo : function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				var queryCondition = {
						page : _this.cache.page,
						merchNames : $("#merchNames").val(),
						userName : $("#userName").val(),
						regId : $("#regId").val(),
						uploadStatus : $("#uploadStatus").val(),
						startTime : $("#startTime").val(),
						startTime : $("#startTime").val(),
						endTime : $("#endTime").val()
				}
				var orderName = encodeURI(records[0]['orderName']);
				window.parent.IQB.main2.openTab("warrents-update", "权证资料管理", '/etep.web/view/cfm/warrants/wf/warrantsUpdate.html?procTaskId='+records[0]['procTaskId']+"&procBizid="+records[0]['orderId']+"&procInstId="+records[0]['procInstId']+"&procOrgCode="+records[0]['procOrgCode']+"&orderAmt="+records[0]['orderAmt']+"&orderName="+orderName+"&queryCondition="+queryCondition, true, true, {lastTab: param});
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_box = new DataGrid2(_this.config);
			_box.init();
			$("#btn-forInfo").click(function(){
				_this.forInfo();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.warrantsManage.init();
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