$package('IQB.hyjfStageOrder');
IQB.hyjfStageOrder = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{ 
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/business/exportOrderList',
				stage: urls['cfm'] + '/order/houseHandler/createBill'
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
							 url: urls['cfm'] + '/order/houseHandler/2/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
						     isExport : true,
				   			 cols : '1,2,3,6,7,8,9,10,11,12,13,14,15,16',
				   			 filename: 'order_overdue_%YY%-%MM%-%DD%-%hh%-%mm%-%ss%'
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/order/houseHandler/2/query',
	   			isExport : true,
	   			cols : '1,2,3,6,7,8,9,10,11,12,13,14,15,16',
	   			filename: 'order_overdue_%YY%-%MM%-%DD%-%hh%-%mm%-%ss%'
			}
		},
		stage : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderNo;
			var approvalAmt=row.approvalAmt;
			var realInstAmt=row.realInstAmt;
			var remain=approvalAmt-realInstAmt;
			$('#open-win').modal('show');
			$("#beginDate").val(_this.getNowFormatDate());
			$("#btn-install-sure").unbind("click").click(function(){
				  if($("#updateForm").form('validate')){
					  var beginDate = $("#beginDate").val();
					  var installAmt = $("#installAmt").val();
					  var data = {
								'orderId':orderId,
								'beginDate':beginDate,
								'installAmt':installAmt
					  };
					  if(installAmt>remain){
						  IQB.alert('分期金额大于剩余金额');
					  }else{
						  IQB.get(_this.config.action.stage, data, function(result) {
							  if(result.success == 1){
								  IQB.alert('分期成功'); 
								  $('#open-win').modal('hide');
								  _this.refresh();
							  }else{
								  IQB.alert('分期失败');
								  $('#open-win').modal('hide');
							  };
						  })
					  }
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
	          var currentdate = year + month + strDate;
	          return currentdate;
	    },
	    refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/order/houseHandler/2/query', queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.hyjfStageOrder.init();
	datepicker2(startTime);
	datepicker2(endTime);
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