$package('IQB.breakRecord');
IQB.breakRecord = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		check : function(){
			$("#btn-break").click(function(){
				var records = _grid.util.getCheckedRows();
				if(records.length > 0){
					//标记违约行为
					$('#open-win2').modal('show');
					$('#overdueDate').val('');
					$('#overdueRemark').val('');
					$("#btn-install-sure").unbind("click").click(function(){
						  if($("#updateForm2").form('validate')){
							  var programmers = [];
							  $.each(records,function(i,n){
								  programmers.push({
									    "orderId": n.orderId,
										"overdueDate": $('#overdueDate').val(),
										"overdueRemark": $('#overdueRemark').val()
								  });
							  });
							  IQB.get(_this.config.action.overdueRemark, {'programmers':programmers}, function(result) {
									if(result.success == 1){
										IQB.alert('标记成功');
										$('#open-win2').modal('hide');
										_this.refresh();
									}
									if(result.success == 110){
										IQB.alert(result.result);
										$('#open-win2').modal('hide');
									}
								})
						  }; 
				    });
				}else{
					IQB.alert('未选中行');
				}
	            $("#btn-close2").click(function(){
	      	       $('#open-win2').modal('hide');
			    });
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
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectBreachContractList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'pageSize':20})	
			});
			$('#checkAll').prop('checked',false);
	    },
		config: {
			action: {//页面请求参数
				forBill: urls['cfm'] + '/afterLoan/getAllBillByOrderId',
				overdueRemark : urls['cfm'] + '/afterLoan/batchListToMark'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="gpsStatus"]').val(null).trigger('change');
					$('select[name="bizType"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectBreachContractList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'pageSize':20})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectBreachContractList',
	   			queryParams :{
	   				'pageSize':20
	   			}
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		$('#datagrid').datagrid2('checkAll');
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.check();
			_this.initSelect();
			_this.checkAll();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.breakRecord.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker(overdueDate);
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