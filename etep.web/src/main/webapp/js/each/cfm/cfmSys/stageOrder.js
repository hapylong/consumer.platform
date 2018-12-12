$package('IQB.stageOrder');
IQB.stageOrder = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/business/exportOrderList',
				stage: urls['cfm'] + '/back/singleInstall'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改订单');
					$('#update-win #merchantNo').attr("disabled",true);
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'type':2})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getOrderList',
	   			queryParams: {
	   				type: 2
	   			}
			}
		},
		stage : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			$('#open-win').modal('show');
			$("#beginDate").val(_this.getNowFormatDate());
			$("#btn-install-sure").unbind("click").click(function(){
				  if($("#updateForm").form('validate')){
					  var beginDate = $("#beginDate").val();
					  var data = {
								'orderId':orderId,
								'beginDate':beginDate
					  };
					  IQB.get(_this.config.action.stage, data, function(result) {
						if(result.iqbResult.result.retCode == 'success'){
							IQB.alert('分期成功');
							$('#open-win').modal('hide');
						}else{
							if(result.iqbResult.result.retCode == '10001'){
								IQB.alert('重复分期');
								$('#open-win').modal('hide');
							}else{
								IQB.alert('分期失败');
								$('#open-win').modal('hide');
							}
						}
					})
				  }; 
		    });
            $("#btn-close").click(function(){
      	       $('#open-win').modal('hide');
		    });
		},
		getNowFormatDate : function() {
	          var date = new Date();
	          var seperator1 = "-";
	          var year = date.getFullYear();
	          var month = date.getMonth() + 1;
	          var strDate = date.getDate();
	          if (month >= 1 && month <= 9) {
	              month = "0" + month;
	          }
	          if (strDate >= 0 && strDate <= 9) {
	              strDate = "0" + strDate;
	          }
	          var currentdate = year.toString() + month.toString() + strDate.toString();
	          return currentdate;
	    },
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var userName = $("#userName").val();
				/*var riskStatus = $("#riskStatus").val();*/
				var wfStatus = $("#wfStatus").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var orderId = $("#orderId").val();
				var datas = "?merchNames=" + merchName + "&orderId=" + orderId + "&regId=" + regId + "&userName=" + userName + "&wfStatus=" + wfStatus + "&startTime=" + startTime + "&endTime=" + endTime + "&type=" + 2;
	            var urls = _this.config.action.exports + datas;
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
	IQB.stageOrder.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker2(beginDate);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:true,
	    format:'Y-m-d H:i:00',
		allowBlank: true
	});
};
/*显示日历*/ 
function datepicker2(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Ymd',
		allowBlank: true
	});
};