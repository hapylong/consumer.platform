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
/*procBizId = 'CDHTC2001170315116';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';
procTaskId = '9d5e190f-0976-11e7-88a3-f48e3882dc6a';*/

$package('IQB.assetImport');
IQB.assetImport = function(){
	var _this = { 
		cache: {
			
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
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
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
				$("#orderId").html(result.iqbResult.result.orderId);
				$("#creditName").html(result.iqbResult.result.creditName);
				$("#creditCardNo").html(result.iqbResult.result.creditCardNo);
				$("#creditBankCard").html(result.iqbResult.result.creditBankCard);
				$("#creditBank").html(result.iqbResult.result.creditBank);
				$("#creditPhone").html(result.iqbResult.result.creditPhone);
				$("#bizType").html(Formatter.bizType(result.iqbResult.result.bizType));
				$("#merchantNo").html(result.iqbResult.result.merchantNo);
				$("#userName").html(result.iqbResult.result.realName);
				$("#regId").html(result.iqbResult.result.regId);
				$("#orderName").html(result.iqbResult.result.orderName);
				$("#orderAmt").html(result.iqbResult.result.orderAmt);
				$("#orderItems").html(result.iqbResult.result.orderItems);
				$("#monthInterest").html(result.iqbResult.result.monthInterest);
				$("#planId").html(result.iqbResult.result.planShortName);
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
