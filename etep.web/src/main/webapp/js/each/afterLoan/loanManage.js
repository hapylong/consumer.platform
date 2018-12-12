$package('IQB.loanManage');
IQB.loanManage = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		check : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win3').modal('show');
				$('#afterLoanOpinion').val('');
				$('#payOrNot').val('');
				$('.tip').text('');
				$("#btn-sure3").unbind("click").click(function(){
					  if($("#updateForm3").form('validate')){
						  //启动贷后核查流程
						  IQB.post(urls['cfm'] + '/instRemindPhone/saveAfterRiskAndStartWf', {orderId: records[0].orderId,'afterLoanOpinion':$('#afterLoanOpinion').val(),'repaymentFlag':$('#payOrNot').val()}, function(result){
								if(result.success=="1"){
									$('#open-win3').modal('hide');
									_this.refresh();
									IQB.alert('提交成功');
								}
							}) 
					  }; 
			    });
			}else{
				IQB.alert('未选中行');
			}
            $("#btn-close3").click(function(){
      	       $('#open-win3').modal('hide');
		    });
		},
		blur : function(){
		if($("#updateForm2").form('validate')){
			var orderIdAdd = $('#orderIdAdd').val();
			IQB.get(urls['cfm'] + '/instRemindPhone/queryOrderInfoByOrderId', {'orderId':orderIdAdd}, function(result) {
				if(result.iqbResult.code=="0"){
					var result=result.iqbResult.result
					$("#realNameAdd").val(result.realName);
					$("#regIdAdd").val(result.regId);
				}
				else if(result.iqbResult.code=="1"){
					IQB.alert('输入的订单号不是已分期或已放款订单');
					_this.refresh();
					$('#updateForm2')[0].reset()
					$('#open-win2').modal('hide');
				}else if(result.iqbResult.code=="2"){
					IQB.alert('输入的订单号不是以租代购订单');
					_this.refresh();
					$('#updateForm2')[0].reset()
					$('#open-win2').modal('hide');
				}
			})
		}
		},
		add : function(){
			$('#open-win2').modal('show');
			$("#btn-sure2").unbind("click").click(function(){
				  if($("#updateForm2").form('validate')){
					  //增加
					  IQB.post(urls['cfm'] + '/instRemindPhone/queryManagecarInfo', {orderId: $('#orderIdAdd').val()}, function(result){
							if(result.iqbResult.code=="1"){
								IQB.post(urls['cfm'] + '/carstatus/persisit_imci', {orderId: $('#orderIdAdd').val()}, function(result){
									if(result.iqbResult.result=="success"){
										$('#open-win2').modal('hide');
										IQB.alert('增加成功');
										_this.refresh();
										$('#updateForm2')[0].reset()
									}
								}) 
							}else{
								IQB.alert('该订单已经进入贷后无需重复申请');
								_this.refresh();
								$('#updateForm2')[0].reset()
								$('#open-win2').modal('hide');
							}
						}) 
				  }; 
		    });
			$("#btn-close2").click(function(){
      	      $('#open-win2').modal('hide');
      	      $('#updateForm2')[0].reset()
		    });
		},
		detail : function(){
			var records = _grid.util.getCheckedRows();
			var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			window.parent.IQB.main2.openTab("loanDetail", "贷后管理详情", encodeURI('/etep.web/view/afterLoan/loanManageDetail.html?orderId=' + records[0].orderId+ '&merchantName=' + records[0].merchantShortName+ '&realName=' + records[0].realName+ '&regId=' + records[0].regId), true, true, {lastTab: param});  
		},
		forBillDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			IQB.post(urls['cfm'] + '/instRemindPhone/queryBillIfoByOId', {'orderId':orderId}, function(result) {
				var result =result.iqbResult.result
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win').modal('show');
					//赋值
					$("#billRealName").val(row.realName);
					$("#billOrderId").val(row.orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td><td>"+Formatter.isMobileCollection(result[i].mobileCollection)+"</td><td>"+Formatter.isDealReason(result[i].mobileDealOpinion)+"</td><td>"+Formatter.isNull(result[i].remark)+"</td></tr>";
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
			$("#datagrid").datagrid2({url: urls['cfm'] + '/instRemindPhone/afterLoanList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
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
							 url: urls['cfm'] + '/instRemindPhone/afterLoanList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/instRemindPhone/afterLoanList',
	   			singleCheck: true,
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//贷后核实
			$('#btn-check').unbind('click').on('click',function(){
				_this.check();
			});
			//增加
			$('#btn-add').unbind('click').on('click',function(){
				_this.add();
			});
			//详情
			$('#btn-detail').unbind('click').on('click',function(){
				_this.detail();
			});
			//
			$('#orderIdAdd').on('blur',function(){
				_this.blur();
			});
			$('#payOrNot').on('change',function(){
				if($(this).val() == '1'){
					$('.tip').show().text('选择此操作后，客户还款状态会变为正常，将无法进行贷后处理，请确认客户是否还款！');
				}else if($(this).val() == '2'){
					$('.tip').show().text('选择此操作后，将启动贷后流程，请确认！');
				}else{
					$('.tip').hide();
				}
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.loanManage.init();
	datepicker(startDate);
	datepicker(endDate);
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