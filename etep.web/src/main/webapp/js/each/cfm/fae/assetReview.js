function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('procBizId');
var procInstId = getQueryString('procInstId');
/*procBizId = 'CDHTC2001170317015';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';
procTaskId = '9d5e190f-0976-11e7-88a3-f48e3882dc6a';*/

$package('IQB.assetImport');
IQB.assetImport = function(){
	var _this = { 
		cache: {
			eq:0,
		    eq2:0,
		},
		config: {
			action: {//页面请求参数
  				update: urls['cfm'] + '/business/updateJYSOrderInfo',
  				getById: urls['cfm'] + '/business/getJysOrderInfo',
  				cal : urls['cfm'] + '/calOrderInfo',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
                
			},
  			dataGrid: {//表格参数  				
	   			
			}
		},
		plan : function(eq,eq2) {
			var totalAmount = $("#orderAmt").val();
			if(totalAmount != "" && totalAmount != 0){
				//期数
				var orderTerms = _this.cache.prodList[eq2].installPeriods ;
				$("#orderItems").val(orderTerms);
			}
			var option = {
					'planId' : eq,
					'orderAmt' : $('#orderAmt').val()
			}
			IQB.get(_this.config.action.cal, option, function(result){
				$("#monthInterest").val(result.iqbResult.result.monthMake);
			})
		},
		approve: function() {
			var approveForm = $('#approveForm').serializeObject();
			
			if (approveForm.approveStatus) {
				if (approveForm.approveStatus == "1") {
					if($.trim(approveForm.approveOpinion) == '') {
						approveForm.approveOpinion = "同意";
					}
				} else {
					if($.trim(approveForm.approveOpinion) == '') {
						IQB.alert('审批意见必填');
						exit;
					}
				}
				
				var authData= {}
				authData.procAuthType = "2";
				var variableData = {}
				variableData.procApprStatus = approveForm.approveStatus;
				variableData.procApprOpinion = approveForm.approveOpinion;
				var bizData = {}
				bizData.procBizId=window.procBizId;
				var procData = {}
				procData.procTaskId = window.procTaskId;
				var option = {};
				option.authData=authData;
				option.variableData = variableData;
				option.bizData = bizData;
				option.procData = procData;
				
				IQB.post(urls['rootUrl'] + '/WfTask/completeProcess', option, function(result){
					if(result.success=="1") {
						$('#approve-win').modal('hide');
						var url = window.location.pathname;
						var param = window.parent.IQB.main2.fetchCache(url);
						var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
						var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + false + ',' + null + ')';
						window.parent.IQB.main2.call(callback, callback2);
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
				
			} else {
				IQB.alert("请选择审批结果.");
			}
		},
		openApproveWin: function(){
			//修改保存
			var userName = $("#userName").val();
			var regId = $("#regId").val();
			var orderName = $("#orderName").val();
			var planId = $('#planId').val();
			if(userName == '' || regId == '' || orderName == '' || planId == ''){
				IQB.alert('订单信息不完善！');
			}
			var id = $("#id").val();
			var userId = $("#userId").val();
			var orderId = $("#orderId").html();
			var batchNo = $("#batchNo").html();
			var bizType = $("#bizType").val();
			var merchantNo = $("#merchantNo").val();
			var orderAmt = $("#orderAmt").val();
			var orderItems = $("#orderItems").val();
			var monthInterest = $("#monthInterest").val();
			//var planId = $("#planId").val();
			var option = {};
			option.id = id;
			option.userId = userId;
			option.orderId = orderId;
			option.realName = userName;
			option.regId = regId;
			option.orderName = orderName;
			option.batchNo = batchNo;
			//option.bizType = bizType;
			option.merchantNo = merchantNo;
			option.orderName = orderName;
			option.orderAmt = orderAmt;
			option.orderItems = orderItems;
			option.monthInterest = monthInterest;
			option.planId = planId;
			IQB.post(_this.config.action.update, option, function(result){
				//返回信息
				if(result.success == 1){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}else{
					IQB.alert(result.msg);
				}
			})
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		showText : function(){
			var orderId = window.procBizId;
			var option = {};
			if(orderId.indexOf("-") > -1){
				var orderIdArr = orderId.split('-');
				orderId = orderIdArr[0];
				id = orderIdArr[1];
				option.orderId = orderId;
				option.id = id;
			}else{
				option.orderId = orderId;
				option.id = '';
			}
			IQB.post(urls['cfm'] + '/business/getJysOrderInfo', option, function(result){
				//计划
	    		_this.cache.prodList = result.iqbResult.prodList;
	    		/*回显方案*/
	    		$("#planId").find('option').remove();
	    		if(_this.cache.prodList.length > 0){
	    			for(var i=0;i<_this.cache.prodList.length;i++){
		    			$("#planId").append("<option  id='"+i+"' value='" + _this.cache.prodList[i].id + "'>"+ _this.cache.prodList[i].planFullName+ "</option>")
		    		}
	    		}
	    		//赋值
	    		$("#id").val(result.iqbResult.result.id);
	    		$("#userId").val(result.iqbResult.result.userId);
	    		$("#orderId").html(result.iqbResult.result.orderId);
				$("#creditName").html(result.iqbResult.result.creditName);
				$("#creditCardNo").html(result.iqbResult.result.creditCardNo);
				$("#creditBankCard").html(result.iqbResult.result.creditBankCard);
				$("#creditBank").html(result.iqbResult.result.creditBank);
				$("#creditPhone").html(result.iqbResult.result.creditPhone);
	    		$("#batchNo").html(result.iqbResult.result.batchNo);
	    		$("#bizType").val(Formatter.bizType(result.iqbResult.result.bizType));
				$("#merchantNo").val(result.iqbResult.result.merchantNo);
				$("#userName").val(result.iqbResult.result.realName);
				$("#regId").val(result.iqbResult.result.regId);
				$("#orderName").val(result.iqbResult.result.orderName);
				$("#orderAmt").val(result.iqbResult.result.orderAmt);
				$("#orderItems").val(result.iqbResult.result.orderItems);
				$("#monthInterest").val(result.iqbResult.result.monthInterest);
				$("#planId").val(result.iqbResult.result.planId);
				//修改分期方案
		    	$("#planId").change(function(){
		    		_this.cache.eq = $('#planId').val();
		    		_this.cache.eq2 = $('#planId option:selected').attr('id');
		    		_this.plan(_this.cache.eq,_this.cache.eq2);
		    	});
		    	//提交
			})
		},
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		init: function() {
			//标签页样式动态处理
			_this.showText();
			_this.initApprovalHistory();
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});			
		}
	}
	return _this;
}();

$(function() {
	IQB.assetImport.init();
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


