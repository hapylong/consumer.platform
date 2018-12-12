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
function Subtr(arg1,arg2){ 
	  var r1,r2,m,n; 
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	  m=Math.pow(10,Math.max(r1,r2)); 
	  n=(r1>=r2)?r1:r2; 
	  return ((arg1*m-arg2*m)/m).toFixed(n); 
	}
/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.operativeApproval');
IQB.operativeApproval = function() {
	var _this = {
		cache: {
			viewer: {},
			cutOverdueFlag:'',
			curItems:''
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
				variableData.cutOverdueFlag = _this.cache.cutOverdueFlag;
				variableData.curItems = _this.cache.curItems;
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.cutOverdueFlag = _this.cache.cutOverdueFlag;
				bizData.curItems = _this.cache.curItems;
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
		deleteProcess: function() {
			var authData= {}
			authData.procAuthType = "2";
			var variableData={}
			var bizData={}
			bizData.procBizId=procBizId;
			var procData={}
			procData.procTaskId = procTaskId;
			var option = {};
			option.authData=authData;
			option.variableData=variableData;
			option.bizData=bizData;
			option.procData=procData;
			IQB.getById(urls['rootUrl'] + '/WfTask/deleteProcess', option, function(result) {
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
		initApprovalTask: function(){//初始化详情
			IQB.post(urls['cfm'] + '/settle/getSettleBean', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				//基本信息
				$('#realName').text(result.realName);
				$('#orderItems').text(result.orderItems);
				$('#haspayItem').text(result.curItems);
				_this.cache.curItems = result.curItems;
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#monthCapital').text(Formatter.money(result.monthPrincipal));
				
			    //提前还款申请
				$('#hasMarginAmt').text(Formatter.money(result.margin));
				$('#hasTakePaymentAmt').text(Formatter.money(result.feeAmount));
				$('#hasRepayAmt').text(Formatter.money(result.payPrincipal));
				$('#hasnotRepayAmt').text(Formatter.money(result.surplusPrincipal));
				$('#dedit').text(Formatter.money(result.overdueAmt));
				$('#totalDefaultInterest').text(Formatter.money(result.totalOverdueInterest));
				$('#hasnotInterest').text(Formatter.money(result.remainInterest));
				$('#returnTakePaymentAmt').text(Formatter.money(result.refundAmt));
				
				$('#repayAmtAll').text(Formatter.money(result.totalRepayAmt));
				$('#hideFlag').text(Formatter.yOrn(result.hiddenFee));
				$('#payReason').text(result.reason);
				$('#remark').text(Formatter.ifNull(result.remark));
				//是否减免违约金
				$('#derateFlag').text(Formatter.yOrn(result.cutOverdueFlag));
				_this.cache.cutOverdueFlag = result.cutOverdueFlag;
				if(result.cutOverdueFlag == 1){
					$('.derateAbout').show();
					//回显
					$('#derateAmt').text(Formatter.money(result.cutOverdueAmt));
					$('#amtFinal').text(Formatter.money(result.finalOverdueAmt));
					$('#derateReason').text(result.cutOverdueRemark);
				}else{
					$('.derateAbout').hide();
				}
				$('#payway').text(Formatter.yOrn(result.payMethod));
				$('#paymentAmt').text(Formatter.money(result.receiveAmt));
				$('#paymentTime').text(result.recieveDate);
				//支付状态
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.amtStatus));
				//处理流水号
				if(result.amtStatus == 1){
					$('.outOrderId-div').show();	
				}else{
					$('.outOrderId-div').hide();	
				}
			});
			IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId}, function(result){
				if(result){
					if(result != '' && result != null && result.length > 1){
						$('#outOrderIdMore').show();
						_this.cache.outOrderId = result;
					}else{
						$('#outOrderId').text(result[0].outOrderId);	
					}
				}				
			});
		},
		outOrderIdMore : function(){
			$('#open-win').modal('show');
			var result = _this.cache.outOrderId;
			//赋值
			var tableHtml = '';
			for(var i=0;i<result.length;i++){
				tableHtml += "<tr><td>"+Formatter.moneyCent(result[i].amount)+
				"</td><td>"+Formatter.timeCfm2(result[i].tranTime)+"</td><td>"+
				result[i].outOrderId+"</td></tr>";
			}
			$(".outOrderIdTable").find('tbody').find('tr').remove();
			$(".outOrderIdTable").append(tableHtml);
			$("#btn-close2").click(function(){
        	    $('#open-win').modal('hide');
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
			$('#btn-delete').on('click', function(){_this.deleteProcess()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function() {
	IQB.operativeApproval.init();
});


