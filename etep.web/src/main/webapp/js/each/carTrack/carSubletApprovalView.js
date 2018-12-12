function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('procBizid');
var procInstId = getQueryString('procInstId');

/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.carSaleApproval');
IQB.carSaleApproval = function(){
	var _this = {
		cache: {
			viewer: {},
			i : 1,
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
		openApproveWin: function(){
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/carstatus/selOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				if(result != '' && result != null){
					$('#merchName').text(result.merchantNo);
					$('#orderId').text(result.orderId);
					$('#realName').text(result.realName);
					$('#borrowAmt').text(Formatter.money(result.orderAmt));
					$('#preAmt').text(Formatter.money(result.preAmt));
					$('#monthInterest').text(Formatter.money(result.monthInterest));
					$('#orderItems').text(result.orderItems);
					$('#planName').text(result.planName);
					$('#orderName').text(result.orderName);
					$('#VIN').text(result.carNo);
					$('#plate').text(result.plate);
					//填写的信息回显
					var subleaseInfo = result.subleaseInfoMap;
					if(subleaseInfo != '' && subleaseInfo != null){
						$('#orderId2').text(subleaseInfo.subleaseOrderId);
						$('#orderPerson').text(subleaseInfo.subleaseRealName);
						$('#orderItems2').text(subleaseInfo.subleaseOrderItems);
						$('#orderPhone').text(subleaseInfo.subleaseRegId);
						$('#financeAmt').text(Formatter.money(subleaseInfo.subleaseAmount));
						$('#planName2').text(subleaseInfo.subleasePlan);
						$('#preAmt2').text(Formatter.money(subleaseInfo.subleasePreAmount));
						$('#monthInterest2').text(Formatter.money(subleaseInfo.subleaseMonthInterest));
						$('#remark').text(subleaseInfo.subleaseRemark);
					}
				}
			});
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
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
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
	IQB.carSaleApproval.init();
});


