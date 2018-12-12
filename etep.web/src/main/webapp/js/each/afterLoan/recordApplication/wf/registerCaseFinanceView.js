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

$package('IQB.registerCaseFinanceView');
IQB.registerCaseFinanceView = function(){
	var _this = {
		cache: {			
			viewer: {},
			i: 1
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
			if($('#updateForm').form('validate')){
				var option = {
						'arriveDate': $('#arriveDate').val(),
						'arriveMoney': $('#arriveMoney').val()
				}
				//保存
				IQB.post(urls['cfm'] + '', option, function(result){
					if(result.success == '1'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getCaseInfoByOrderId', {'caseId': window.procBizId}, function(result){
				//表格回显
				var backInfo = result.iqbResult.receivedPaymentList;
				var backInfoHtml = '';
				if(backInfo != '' && backInfo != null){
					for(var i=0;i<backInfo.length;i++){
						backInfoHtml += "<tr>"+
						"<td><span class='text-muted'>"+Formatter.isNull(backInfo[i].receivedPaymentDate)+"</span></td>"+
						"<td><span class='text-muted'>"+Formatter.money(backInfo[i].receivedPayment)+"</span></td>"+
						"</tr>"
					}
				}
				$('#backTable').append(backInfoHtml);
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				$('#orderId').text(Formatter.isNull(result.orderId));
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				
				//
				$('#repayNo').text(Formatter.isNull(result.hasRepayNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				$('#enNo').text(Formatter.isNull(result.engine));
				
				//诉讼信息回显
				$('#register').text(Formatter.register(result.register));
				if(result.register == '1'){
					$('.registerAbout').hide();
				}else{
					$('.registerAbout').show();
				}
				$('#associatedAgency').text(Formatter.isNull(result.associatedAgency));
				$('#mandatoryLawyer').text(Formatter.isNull(result.mandatoryLawyer));
				$('#acceptingInstitution').text(Formatter.isNull(result.acceptOrg));
				$('#legalFare').text(Formatter.money(result.legalCost));
				$('#counselFee').text(Formatter.money(result.barFee));
				$('#filingTime').text(Formatter.isNull(result.registerDate));
				$('#caseNo').text(Formatter.isNull(result.caseNo));
				$('#filingRemark').text(Formatter.isNull(result.registerRemark));
				
				//庭前调解信息回显
				$('#mediateFlag').text(Formatter.yOrn(result.composeFlag));
				$('#reconciliationFlag').text(Formatter.yOrn(result.compromiseFlag));
				$('#filingRemark').text(Formatter.isNull(result.registerRemark));
				$('#adjustRemark').text(Formatter.isNull(result.composeRemark));
				if(result.composeFlag != '1'){
					$('.reconciliationAbout').hide();
				}else{
					$('.reconciliationAbout').show();
				}
				if(result.compromiseFlag != '1'){
					$('.addOrDelete').hide();
				}else{
					$('.addOrDelete').show();
				}
				
				//到帐信息
				$('#arriveDate').text(Formatter.isNull(result.receivedDate));
				$('#arriveMoney').text(Formatter.money(result.receivedAmt));
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
		init: function(){ 	
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

$(function(){
	IQB.registerCaseFinanceView.init();
});		
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};