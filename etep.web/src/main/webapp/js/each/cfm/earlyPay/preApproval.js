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

$package('IQB.preApproval');
IQB.preApproval = function(){
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
				variableData.reduceFlag = $('#derateFlag').val();
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
				if(!($("#amtFinal").val() < 0)){
					//保存信息 
					var derateFlag = $('#derateFlag').val();//是否减免	
					var derateAmt = $('#derateAmt').val();//减免金额
					var amtFinal = $('#amtFinal').val();//减免后违约金
					var repayAmtAll = Formatter.removeMoneyPrefix($('#repayAmtAll').text());//累计应还
					var hideFlag = $('#hideFlag').val();//是否隐藏
					var derateReason = $('#derateReason').val();//减免原因
					var payReason = $("#payReason").val();//提前还款原因
					var remark = $("#remark").val();//备注
					var data = {
							'orderId':window.procBizId,
							'reason' : payReason,
							'remark' : remark,
                            'cutOverdueFlag':derateFlag,
							'totalRepayAmt':repayAmtAll,
							'cutOverdueRemark':derateReason,
							'hiddenFee':hideFlag
					}
					if(derateFlag == 1){
						data.cutOverdueAmt = derateAmt;
						data.finalOverdueAmt = amtFinal;
					}else{
						data.cutOverdueAmt = 0;
						data.finalOverdueAmt = _this.cache.overdueAmt;
					}
					IQB.post(urls['cfm'] + '/settle/saveOtherInfo', data, function(result){
						if(result.success == 1){
							$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
						}
					})
				}else{
					IQB.alert('金额有误，请确认！');
				}
			}
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化详情
			IQB.post(urls['cfm'] + '/settle/getSettleBean', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				//基本信息
				$('#realName').text(Formatter.ifNull(result.realName));
				$('#orderItems').text(Formatter.ifNull(result.orderItems));
				$('#haspayItem').text(result.curItems);
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
				$('#hideFlag').val(result.hiddenFee);
				$('#payReason').val(result.reason);
				$('#remark').val(result.remark);
				//是否减免违约金
				$('#derateFlag').val(result.cutOverdueFlag);
				if(result.cutOverdueFlag == 1){
					$('.derateAbout').show();
					$('#derateAmt').validatebox({ required:true });
					$('#derateReason').validatebox({ required:true });
					//回显
					$('#derateAmt').val(result.cutOverdueAmt);
					$('#amtFinal').val(result.finalOverdueAmt);
					$('#derateReason').val(result.cutOverdueRemark);
				}else{
					$('.derateAbout').hide();
					$('#derateAmt').validatebox({ required:false });
					$('#derateReason').validatebox({ required:false });
				}
				//应还违约金
				_this.cache.overdueAmt = result.overdueAmt;
				//累计应还金额
				_this.cache.totalRepayAmt = result.totalRepayAmt;
				//原始累计应还金额
				_this.cache.totalRepayAmtOriginal = result.totalRepayAmtOriginal;
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
		derateFlag : function(){
			var derateFlag = $('#derateFlag').val();
			if(derateFlag == 1){
				//减免违约金
				$('#repayAmtAll').text(Formatter.money(_this.cache.totalRepayAmt));
				$('.derateAbout').show();
				$('#derateAmt').validatebox({ required:true });
				$('#derateReason').validatebox({ required:true });
			}else{
				//不减免
				$('#repayAmtAll').text(Formatter.money(_this.cache.totalRepayAmtOriginal));
				$('.derateAbout').hide();
				$('#derateAmt').validatebox({ required:false });
				$('#derateReason').validatebox({ required:false });
			}
		},
		cal : function(){
			var derateAmt = $('#derateAmt').val();
			var amtFinal;//减免后金额(应还违约金-减免金额)
			var repayAmtAll;//累计应还
			if(_this.cache.overdueAmt != 0 && derateAmt != ''){
	            $('#amtFinal').val(window.Subtr(_this.cache.overdueAmt , derateAmt));
	            repayAmtAll = window.Subtr(Number(_this.cache.totalRepayAmtOriginal), Number(derateAmt));
	            $('#repayAmtAll').text(Formatter.money(repayAmtAll));
			}else{
				$('#amtFinal').val('');
			}
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			$('#derateFlag').on('change',function(){_this.derateFlag();});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});		
		}
	}
	return _this;
}();

$(function() {
	IQB.preApproval.init();
});


