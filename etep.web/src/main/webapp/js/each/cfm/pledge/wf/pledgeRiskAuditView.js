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

//procBizId = '20170103-267322';
//procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';

$package('IQB.pledgeRiskAudit');
IQB.pledgeRiskAudit = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		viewerShow: function(event){
			$('#img').click();
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/pledge/getPledgeInfo', {orderId: window.procBizId}, function(ret){
				result = ret.iqbResult.result;
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#plate').text(result.plate);
				$('#groupName').text(Formatter.groupName(result.bizType));
				if(result.bizType == '2001'){
					$('.assessPrice-div').hide();
				}else{
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
				}
				$('#applyAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
//				$('#applyAmt').text(Formatter.money(result.applyAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));
				var riskRetRemark = result.riskRetRemark;
				if(riskRetRemark != null && riskRetRemark != ''){
					var riskRetArr = riskRetRemark.split(':');
					if(riskRetArr[0] == '1'){
						$('#riskRetStatus').text('通过');	
					}else{
						$('#riskRetStatus').text('拒绝');	
					}
					$('#riskRetRemark').text(riskRetArr[1]);	
				}
			});
			IQB.post(urls['cfm'] + '/creditorInfo/getCreditorInfo', {orderId: window.procBizId}, function(result){
				if(result.iqbResult.result){
					$('#creditName').text(Formatter.ifNull(result.iqbResult.result.creditName));
					$('#creditCardNo').text(Formatter.ifNull(result.iqbResult.result.creditCardNo));
					$('#creditBankCard').text(Formatter.ifNull(result.iqbResult.result.creditBankCard));
					$('#creditBank').text(Formatter.ifNull(result.iqbResult.result.creditBank));
					$('#creditPhone').text(Formatter.ifNull(result.iqbResult.result.creditPhone));
				}				
			});
			IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId}, function(result){
				if(result){
					$('#outOrderId').text(Formatter.ifNull(result.outOrderId));				
				}				
			});
		},
		initApprovalHistory: function(){//初始化流程历史
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
			
		}
	}
	return _this;
}();

$(function(){
	IQB.pledgeRiskAudit.init();
});		