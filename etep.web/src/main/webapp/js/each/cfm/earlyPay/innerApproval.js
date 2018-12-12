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

$package('IQB.innerApproval');
IQB.innerApproval = function(){
	var _this = {
		cache: {
			viewer: {},
			orderAmt : 0
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
		initApprovalTask: function(){//初始化详情
			IQB.post(urls['cfm'] + '/settle/getSettleBean', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#realName').text(result.realName);
				$('#orderItems').text(result.orderItems);
				$('#currentItem').text(result.curItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				
				$('#applyTime').text(Formatter.time3(result.createTime));
				$('#dedit').text(Formatter.money(result.overdueAmt));
				$('#hasRepayAmt').text(Formatter.money(result.payAmt));
				$('#hasMarginAmt').text(Formatter.money(result.margin));
				
				$('#hasTakePaymentAmt').text(Formatter.money(result.feeAmount));
				$('#repayAmt').text(Formatter.money(result.shouldRepayAmt));
				$('#returnMarginAmt').text(Formatter.money(result.refundMargin));
				$('#returnTakePaymentAmt').text(Formatter.money(result.refundAmt));
				_this.cache.repayAmt = result.shouldRepayAmt;
				_this.cache.returnMarginAmt = result.refundMargin;
				_this.cache.returnTakePaymentAmt = result.refundAmt;
				$('#repayAmtAll').text(Formatter.money(result.totalRepayAmt));
				$('#payReason').text(result.reason);
				$('#remark').text(result.remark);
				//查询新加字段
				var cutOverdue = result.cutOverdueAmt;
				var amtFinal;//减免后金额
				var repayAmtAll;//累计应还
				if(cutOverdue != null && cutOverdue != '' && cutOverdue != 0){
					$('#derateFlag').text('是');
					$('.derateAbout').show();
					$('#derateAmt').text(Formatter.money(result.cutOverdueAmt));
					$('#amtFinal').text(Formatter.money(window.Subtr(result.overdueAmt , result.cutOverdueAmt)));
					amtFinal = window.Subtr(result.overdueAmt , result.cutOverdueAmt);
					$('#derateReason').text(Formatter.ifNull(result.cutOverdueRemark));
					//计算累计应还
					repayAmtAll = window.Subtr(Number(_this.cache.repayAmt) + Number(amtFinal) , Number(_this.cache.returnMarginAmt) + Number(_this.cache.returnTakePaymentAmt));
		            $('#repayAmtAll').text(Formatter.money(repayAmtAll));
				}else{
					$('#derateFlag').text('否');
					$('.derateAbout').hide();
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
	IQB.innerApproval.init();
});


