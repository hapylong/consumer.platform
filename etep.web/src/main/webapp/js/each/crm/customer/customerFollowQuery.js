$package('IQB.customerFollow');
IQB.customerFollow = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		check : function(){
			$("#btn-supplement").click(function(){
				var records = _grid.util.getCheckedRows();
				if(_grid.util.checkSelectOne(records)){			    	
			    	//跳转窗口
					window.parent.IQB.main2.openTab("activiti-customerFollow", "补充资料", encodeURI('/etep.web/view/crm/customer/customerFollowDetail.html?merchantShortName=' + records[0].merchantShortName + '&realName=' + records[0].realName + '&regId=' + records[0].regId + '&orderId=' + records[0].orderId +'&type=' + 2 + '&smsMobile=' + records[0].smsMobile), true, true, null);
				}
			})
		},
		forBillDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var regId = row.regId;
			var realName = row.realName;
			IQB.get(_this.config.action.forBill, {'orderId':orderId,'regId':regId}, function(result) {
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win').modal('show');
					//赋值
					$("#billRealName").val(realName);
					$("#billOrderId").val(orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
		config: {
			action: {//页面请求参数
				forBill: urls['cfm'] + '/afterLoan/getAllBillByOrderId'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="gpsStatus"]').val(null).trigger('change');
				},
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/afterLoanList',
	   			singleCheck: true
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.check();
			_this.initSelect();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.customerFollow.init();
});		