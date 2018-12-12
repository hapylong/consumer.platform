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

/*procBizId = '20170103-267322';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/
function floatSub(arg1,arg2){ 
	  var r1,r2,m,n; 
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	  m=Math.pow(10,Math.max(r1,r2)); 
	  n=(r1>=r2)?r1:r2; 
	  return ((arg1*m-arg2*m)/m).toFixed(n); 
}
$package('IQB.firstCheckApprovalCredit');
IQB.firstCheckApprovalCredit = function(){
	var _this = {
		cache: {			
			viewer: {},
			preAmount:''
		},	
		viewerShow: function(event){
			$('#img').click();
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
			IQB.post(urls['cfm'] + '/credit_pro/over_first_trial', {orderId: window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				if(result.iqbResult.result == true){ 
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}else{
					IQB.alert('请先审核车贷订单');
				}
			})
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text(Formatter.groupName(result.bizType));
				//处理车辆估价
				if(result.bizType == '2001'){
					$('.assessPrice-div').hide();
				}else{
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				IQB.post(urls['cfm'] + '/business/selOrderInfo', {orderId: window.procBizId.substring(0,window.procBizId.length-1)}, function(resultChild){
					$('#carAmt').text(Formatter.money(resultChild.iqbResult.result.carAmt));
					$('#gpsAmt').text(Formatter.money(resultChild.iqbResult.result.gpsAmt));
					$("#jqInsurance").html(Formatter.money(resultChild.iqbResult.result.insAmt));
					$("#syInsurance").html(Formatter.money(resultChild.iqbResult.result.businessTaxAmt));
					$('#taxAmt').text(Formatter.money(resultChild.iqbResult.result.taxAmt));	
					$('#otherAmt').text(Formatter.money(resultChild.iqbResult.result.otherAmt));
				});
				var useCreditLoan = result.useCreditLoan;
				if(useCreditLoan =='1'||useCreditLoan==1){
					_this.initCredit();
					$('#creditLoan-t').show();
				}else{
					$('#creditLoan-t').hide();
				}
				//处理支付结果
				if(result.chargeWay == 1){
					$('.isPay-table').show();
					$('.isPay-div').hide();	
					$('.outOrderId-div').hide();	
				}else{
					$('.isPay-table').hide();
					$('.isPay-div').show();	
					$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
					//处理流水号
					if(result.preAmtStatus == 1){
						$('.outOrderId-div').show();	
					}else{
						$('.outOrderId-div').hide();	
					}
				}
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
			});
			IQB.post(urls['cfm'] + '/creditorInfo/getCreditorInfo', {orderId: window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				if(result.iqbResult.result){
					$('#creditName').text(Formatter.ifNull(result.iqbResult.result.creditName));
					$('#creditCardNo').text(Formatter.ifNull(result.iqbResult.result.creditCardNo));
					$('#creditBankCard').text(Formatter.ifNull(result.iqbResult.result.creditBankCard));
					$('#creditBank').text(Formatter.ifNull(result.iqbResult.result.creditBank));
					$('#creditPhone').text(Formatter.ifNull(result.iqbResult.result.creditPhone));
				}				
			});
			IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				if(result){
					$('#outOrderId').text(Formatter.ifNull(result.outOrderId));				
				}				
			});
		},
		initCredit: function(){
			IQB.post(urls['cfm'] + '/credit_pro/get_info_details_x', {orderId:window.procBizId}, function(result){
				var resl = result.iqbResult.result;
				$('#merchantShortName').text(resl.merchantShortName);
				$('#realName_c').text(resl.realName);
				$('#regId_c').text(resl.regId);
				$('#orderId_c').text(resl.orderId);
				$('#orderAmt_c').text(Formatter.money(resl.orderAmt));
//				$('#planFullName_c').text(resl.planFullName);
//				$('#orderItems_c').text(resl.orderItems);
//				$('#fee').text(Formatter.getPercent(resl.fee));
//				$('#monthInterest_c').text(resl.monthInterest);
				$('#creditLoan-t').show();	
				$('#planFullName_c').text(resl.planShortName);
				$('#orderItems_c').text(resl.orderItems);
				$('#margin_c').text(Formatter.money(resl.margin));
				$('#serviceFee_c').text(Formatter.money(resl.serviceFee));
				$('#sbAmt_c').text(Formatter.money(resl.sbAmt));
				$('#contractAmt_c').text(Formatter.money(resl.contractAmt));
				$('#loanAmt_c').text(Formatter.money(resl.loanAmt));
				$('#monthMake_c').text(Formatter.money(resl.monthInterest));
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
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4, 5, 6, 7, 8,9,10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]}, function(result){
				$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				var is = false;
				var is2 = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						if(n.imgType != 5){
							var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
							      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;"></a>' +
							      			'<div class="caption">' +
							      				'<h5>' + n.imgName + '</h5>' +
							      			'</div>' + 
							      		'</div>';
							$('#td-' + n.imgType).append(html);
							is = true;
						}else{
							$('#printScreen').append('<a onclick="IQB.firstCheckApprovalCredit.viewerShow(event);">' + n.imgName +'</a>');
							$('#img').prop('src', urls['imgUrl'] + n.imgPath);
							$('#img').prop('alt', n.imgName);
							is2 = true;
						}						
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
				}
				if(is2){
					_this.cache.viewer.img = new Viewer(document.getElementById('img'), {});
				}
			});
		},
		showText : function(){
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				$("#VIN").html(result.iqbResult.AuthorityCardInfo.carNo);
				$("#engineNo").html(result.iqbResult.AuthorityCardInfo.engine);
				$("#licenseNumber").html(result.iqbResult.AuthorityCardInfo.plate);
				if(result.iqbResult.AuthorityCardInfo.plateType == '01'){
					$("#carType").html('大型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '02'){
					$("#carType").html('小型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '15'){
					$("#carType").html('挂车');
				}
			})
			IQB.post(urls['cfm'] + '/business/selOrderInfo', {'orderId': window.procBizId.substring(0,window.procBizId.length-1)}, function(result){
				$("#vehiclePrice").html(Formatter.money(result.iqbResult.result.carAmt));
				$("#GPSPrice").html(Formatter.money(result.iqbResult.result.gpsAmt));
				$("#insurancePrice").html(Formatter.money(result.iqbResult.result.insAmt));
				$("#purchasePrice").html(Formatter.money(result.iqbResult.result.taxAmt));
				$("#otherPrice").html(Formatter.money(result.iqbResult.result.otherAmt));
			})
		},
		showContract : function(){
			IQB.post(urls['cfm'] + '/admin/contract/get_ioce_by_oid', {'orderId': window.procBizId}, function(result){
				var result = result.iqbResult.result;
				if(result != null){
					$('#loanContractNo').text(result.loanContractNo);
					$('#guarantyContractNo').text(result.guarantyContractNo);
					$('#leaseContractNo').text(result.leaseContractNo);
					$('#contractDate').text(Formatter.timeCfm5(result.contractDate));
					$('#contractEndDate').text(Formatter.timeCfm5(result.contractEndDate));
				}
			});
		},
		showPreFeeX : function(){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': window.procBizId}, function(result){
				var result = result.iqbResult.result[0];
				if(result != '' && result != null){
					$('#gpsAmt3').text(Formatter.money(result.gpsAmt));
					$('#payJqInsuranceC').text(Formatter.money(result.riskAmt));
					$('#paySyInsuranceC').text(Formatter.money(result.preBusinessTaxAmt));
					$('#taxAmt3').text(Formatter.money(result.taxAmt));
					$('#serverAmt3').text(Formatter.money(result.serverAmt));
					$('#preOtherAmt3').text(Formatter.money(result.otherAmt));
				}
			})
		},
		showPreFee : function(){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': window.procBizId.substring(0, window.procBizId.length-1)}, function(result){
				var result = result.iqbResult.result[0];
				if(result != '' && result != null){
					if(result.preAmtFlag == 1){
						$('#isPreAmt').text('是');
						$('#online').text(Formatter.isSuperadmin(result.online));
						$('#totalCost').text(Formatter.money(result.totalCost));
						$('#gpsAmt2').text(Formatter.money(result.gpsAmt));
						$('#payJqInsurance').text(Formatter.money(result.riskAmt));
						$('#paySyInsurance').text(Formatter.money(result.preBusinessTaxAmt));
						$('#taxAmt2').text(Formatter.money(result.taxAmt));
						$('#serverAmt').text(Formatter.money(result.serverAmt));
						$('#preOtherAmt').text(Formatter.money(result.otherAmt));
						$('#preAmtAll').text(Formatter.money(_this.cache.preAmount));
					}else{
						$('#isPreAmt').text('否');
						$('.isPreAmtShow').hide();
					}
				}else{
					$('#isPreAmt').text('否');
					$('.isPreAmtShow').hide();
				}
			})
		},
		init: function(){ 				
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.showText();
			_this.showContract();
			_this.showPreFee();
			_this.showPreFeeX();
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function(){
	IQB.firstCheckApprovalCredit.init();
});		