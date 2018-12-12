function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('procBizId');
var procTaskId = getQueryString('procTaskId');
var procInstId = getQueryString('procInstId');

$package('IQB.phoneApprove');
IQB.phoneApprove = function() {
	var _this = {
		cache:{
			viewer: {}
		},
		config:{
			
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
				bizData.procBizId=procBizId;
				var procData = {}
				procData.procTaskId = procTaskId;
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
		unassign: function() {
			var authData= {}
			authData.procAuthType = "2";
			var variableData={}
			var bizData={}
			var procData={}
			procData.procTaskId = procTaskId;
			var option = {};
			option.authData=authData;
			option.variableData=variableData;
			option.bizData=bizData;
			option.procData=procData;
			IQB.getById(urls['rootUrl'] + '/WfTask/unclaimProcess', option, function(result) {
				if(result.success=="1") {
					var url = window.location.pathname;
					var param = window.parent.IQB.main2.fetchCache(url);
					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
					var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + false + ',' + null + ')';
					window.parent.IQB.main2.call(callback, callback2);
				} else {
					IQB.alert(result.retUserInfo);
				}
			});
		},
		openApproveWin: function() {
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){
			var orderIdArr = window.procBizId.split('-');
			IQB.post(urls['cfm'] + '/instRemindPhone/getRemindInfoByOrderId', {orderId: orderIdArr[0],'curItems':orderIdArr[1],'flag':'2'}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(result.merchantName);
				$('#orderId').text(result.orderId);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				
				$('#curItems').text(result.curItems);
				_this.cache.curItems = result.curItems;
				$('#orderItems').text(result.orderItems);
				$('#billInfoStatus').text(result.billInfoStatus);
				$('#lastRepayDate').text(result.lastRepayDate);
				
				$('#overdueDays').text(result.overdueDays);
				$('#perOverdueAmt').text(Formatter.money(result.perOverdueAmt));
				$('#overdueAmt').text(Formatter.money(result.overdueAmt));
				$('#curRepayAmt').text(Formatter.money(result.curRepayAmt));
			})
			$("#datagrid2").datagrid2({url: urls['cfm'] + '/instRemindPhone/queryRecordList',queryParams : $.extend({}, {'orderId':orderIdArr[0],'flag':2,'curItems':_this.cache.curItems})});
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
		init : function() {
			_this.initApprovalTask();
			_this.initApprovalHistory();

	    	
	    	$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function() {
	IQB.phoneApprove.init();
});
