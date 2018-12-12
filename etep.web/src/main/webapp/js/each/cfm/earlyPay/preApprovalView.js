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

$package('IQB.preApprovalView');
IQB.preApprovalView = function() {
	var _this = {
		cache: {
			viewer: {}
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
				$('#hideFlag').text(Formatter.yOrn(result.hiddenFee));
				$('#payReason').text(Formatter.ifNull(result.reason));
				$('#remark').text(Formatter.ifNull(result.remark));
				//是否减免违约金
				$('#derateFlag').text(Formatter.yOrn(result.cutOverdueFlag));
				if(result.cutOverdueFlag == 1){
					$('.derateAbout').show();
					//回显
					$('#derateAmt').text(Formatter.money(result.cutOverdueAmt));
					$('#amtFinal').text(Formatter.money(result.finalOverdueAmt));
					$('#derateReason').text(Formatter.ifNull(result.cutOverdueRemark));
				}else{
					$('.derateAbout').hide();
				}
				$('#payway').text(Formatter.payMethod(result.payMethod));
				if(result.payMethod == 2){
					$('.btn-group').show();
					$('#viewerOne').show();
					_this.showFile();
				}
				$('#paymentAmt').text(Formatter.money(result.receiveAmt));
				$('#paymentTime').text(Formatter.ifNull(result.recieveDate));
				//支付状态
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.amtStatus));
				//处理流水号
				if(result.amtStatus == 1){
					$('.outOrderId-div').show();	
					var orderS = window.procBizId.split('-');
					IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: orderS[0],'flag':'32'}, function(result){
						if(result){
							if(result != '' && result != null && result.length > 1){
								$('#outOrderIdMore').show();
								_this.cache.outOrderId = result;
							}else{
								$('#outOrderId').text(result[0].outOrderId);	
							}
						}				
					});
				}else{
					$('.outOrderId-div').hide();	
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
		showFile: function(){			
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [2]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      			'<h5>' + n.imgName + '</h5>'+
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
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
			$('#btn-delete').on('click', function(){_this.deleteProcess()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function() {
	IQB.preApprovalView.init();
});


