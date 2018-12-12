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
$package('IQB.businessApprovalView');
IQB.businessApprovalView = function(){
	var _this = {
		cache: {			
			viewer: {},
			preAmount:''
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
				IQB.post(urls['rootUrl'] + '/workFlow/saveWfInfo', {orderId: window.procBizId, receiveAmt: $('#receiveAmt').val(), remark: $.trim($('#remark').val())}, function(){
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
			if($('#form').form('validate')){
				$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
			}			
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId.substring(0,window.procBizId.length-1);
			}else{
				orderId = window.procBizId;
			}
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: orderId}, function(result){
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
				_this.cache.preAmount = result.preAmt;
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#greenChannel').text(Formatter.greenChannel(result.greenChannel));
				IQB.post(urls['cfm'] + '/business/selOrderInfo', {orderId: orderId}, function(resultChild){
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
					_this.showPreFeeX();
					$('#creditLoan-t').show();
				}else{
					$('#creditLoan-t').hide();
				}
			});
		},
		showPreFeeX : function(){
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId;
			}else{
				orderId = window.procBizId+'X';
			}
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': orderId}, function(result){
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
		initCredit: function(){
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId;
			}else{
				orderId = window.procBizId+'X';
			}
			IQB.post(urls['cfm'] + '/credit_pro/get_info_details_x', {orderId:orderId}, function(result){
				var resl = result.iqbResult.result;
				$('#merchantShortName').text(resl.merchantShortName);
				$('#realName_c').text(resl.realName);
				$('#regId_c').text(resl.regId);
				$('#orderId_c').text(resl.orderId);
				$('#orderAmt_c').text(Formatter.money(resl.orderAmt));
//				$('#planFullName_c').text(resl.planFullName);
//				$('#orderItems_c').text(resl.orderItems);
//				$('#fee').text(Formatter.getPercent(resl.fee));
//				$('#monthInterest_c').text(resl.monthInterest);\
				
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
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		showFile: function(){			
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4, 6, 7, 8, 9, 100]}, function(result){
				var orderId;
				if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
					//车秒贷
					$('.projectAbout').hide();
				}else{
					//非车秒贷
					$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
					$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
					$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				}
				
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5>' +
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
		showText : function(){
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId.substring(0,window.procBizId.length-1);
			}else{
				orderId = window.procBizId;
			}
			IQB.post(urls['cfm'] + '/business/selOrderInfo', {'orderId': orderId}, function(result){
				$("#vehiclePrice").html(Formatter.money(result.iqbResult.result.carAmt));
				$("#GPSPrice").html(Formatter.money(result.iqbResult.result.gpsAmt));
				$("#insurancePrice").html(Formatter.money(result.iqbResult.result.insAmt));
				$("#purchasePrice").html(Formatter.money(result.iqbResult.result.taxAmt));
				$("#otherPrice").html(Formatter.money(result.iqbResult.result.otherAmt));
			})
		},
		sublet : function(){
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId.substring(0,window.procBizId.length-1);
			}else{
				orderId = window.procBizId;
			}
			IQB.post(urls['cfm'] + '/sublet/get_sublet_record', {'orderId': orderId}, function(result){
				if(result.iqbResult.result != null){
					var result = result.iqbResult.result;
					$('.noSublet').show();
					//赋值
					$('#sublet').html('是');
					$('#oldOrderId').html(result.subletOrderId);
					$('#oldRegId').html(result.subletRegId);
					$('#oldRealName').html(result.subletRealName);
					$('#planShortName').html(result.planShortName);
					$('#oldMonthInterest').html(result.monthInterest);
					$('#oldOrderItems').html(result.orderItems);
					$('#overItems').html(result.overItems);
					var rollOverFlag = result.rollOverFlag;
					if(rollOverFlag == 1){
						$('#rollOverFlag').html('是');
						$('.noRollOver').show();
						$('.noManageFee').hide();
						//赋值
						$('#rollOverItems').html(result.rollOverItems);
						var manageFeeFlag = result.manageFeeFlag;
						if(manageFeeFlag == 1){
							$('#manageFeeFlag').html('是');
							$('.noManageFee').show();
							$('#manageFee').html(Formatter.money(result.manageFee));
							//赋值
						}else{
							$('#manageFeeFlag').html('否');
						}
					}else{
						$('#rollOverFlag').html('否');
					}
				}else{
					$('#sublet').html('否');
				}
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
		showPreFee : function(){
			var orderId;
			if(window.procBizId.charAt(window.procBizId.length-1) == 'X'){
				orderId = window.procBizId.substring(0,window.procBizId.length-1);
			}else{
				orderId = window.procBizId;
			}
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': orderId}, function(result){
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
						//$('#assessMsgAmt').text(Formatter.money(result.assessMsgAmt));
						//$('#inspectionAmt').text(Formatter.money(result.inspectionAmt));
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
			_this.sublet();
			_this.showContract();
			_this.showPreFee();
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function(){
	IQB.businessApprovalView.init();
});		