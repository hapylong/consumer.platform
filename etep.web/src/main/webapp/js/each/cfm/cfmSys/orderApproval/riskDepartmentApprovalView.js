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
$package('IQB.riskDepartmentApprovalView');
IQB.riskDepartmentApprovalView = function() {
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
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: window.procBizId}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text(Formatter.groupName(result.bizType));
				//处理业务类型
				if(result.bizType == '2001'){
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{
					$('.old-tr').show();
					$('.new-tr').hide();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]}, function(result){
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
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
				$("#VIN").html(result.iqbResult.AuthorityCardInfo.carNo);
				$("#engineNo").html(result.iqbResult.AuthorityCardInfo.engine);
				$("#licenseNumber").html(result.iqbResult.AuthorityCardInfo.plate);
				$("#fileNumber").html(result.iqbResult.AuthorityCardInfo.driverLicenseNum);
				if(result.iqbResult.AuthorityCardInfo.plateType == '01'){
					$("#carType").html('大型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '02'){
					$("#carType").html('小型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '15'){
					$("#carType").html('挂车');
				}
			})
			IQB.post(urls['cfm'] + '/business/selOrderInfo', {'orderId': window.procBizId}, function(result){
				$("#vehiclePrice").html(Formatter.money(result.iqbResult.result.carAmt));
				$("#GPSPrice").html(Formatter.money(result.iqbResult.result.gpsAmt));
				$("#jqInsurance").html(Formatter.money(result.iqbResult.result.insAmt));
				$("#syInsurance").html(Formatter.money(result.iqbResult.result.businessTaxAmt));
				$("#purchasePrice").html(Formatter.money(result.iqbResult.result.taxAmt));
				$("#otherPrice").html(Formatter.money(result.iqbResult.result.otherAmt));
			})
		},
		sublet : function(){
			IQB.post(urls['cfm'] + '/sublet/get_sublet_record', {'orderId': window.procBizId}, function(result){
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
		showCarInfo:function(){
			//回显车商信息
			IQB.post(urls['cfm'] + '/dealer/getDealerInfo', {'orderId':window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#customerChannels').text(Formatter.custChannels(result.custChannels));
				$('#customerName').text(result.custChannelsName);
				$('#carChannels').text(Formatter.SourceCar(result.sourceCar));
				$('#carName').text(result.sourceCarName);
				
				$('#address').text(result.address);
				$('#phone').text(result.contactMethod);
				if(result.sourceCar == 1 || result.sourceCar == 2){
					$('.CTD').show();
					$('#marriedStatus').text(Formatter.maritalStatus(result.maritalStatus));
					$('#coName').text(result.contactName);
					$('#coPhone').text(result.contactMobile);
					$('#coAddress').text(result.contactAddr);
				}
				var overdueFlag = result.overdueFlag;
				if(overdueFlag == 'Y'){
					$('#carName').css('color','red');
				}
			})
		},
		showPreFee : function(){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': window.procBizId}, function(result){
				var result = result.iqbResult.result[0];
				if(result != '' && result != null){
					if(result.preAmtFlag == 1){
						$('#isPreAmt').text('是');
						$('#online').text(Formatter.isSuperadmin(result.online));
						$('#totalCost').text(Formatter.money(result.totalCost));
						$('#gpsAmt').text(Formatter.money(result.gpsAmt));
						$('#payJqInsurance').text(Formatter.money(result.riskAmt));
						$('#paySyInsurance').text(Formatter.money(result.preBusinessTaxAmt));
						$('#taxAmt').text(Formatter.money(result.taxAmt));
						$('#serverAmt').text(Formatter.money(result.serverAmt));
						$('#assessMsgAmt').text(Formatter.money(result.assessMsgAmt));
						$('#inspectionAmt').text(Formatter.money(result.inspectionAmt));
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
		riskReport : function(){
			IQB.post(urls['cfm'] + '/afterLoan/getReportList', {'orderId': window.procBizId}, function(result){
			    //加载报告列表
				var riskHtml = '';
				if(result.success == 1){
					$.each(result.iqbResult.result,function(i,n){
						riskHtml += '<a class="riskReport" onclick="IQB.riskDepartmentApprovalView.showReport('+n.reportType+')">'+n.reportName+'</a>';
					});
				}else{
					riskHtml = result.result;
				}
				$('.riskReports').find('a').remove();
				$('.riskReports').append(riskHtml);
			});
		},
		showReport : function(type){
			var url;
			if(type == '1'){
				//个人风控信息
				IQB.post(urls['cfm'] + '/afterLoan/getReportByReprotNo', {'orderId': window.procBizId,'reportType':1}, function(result){
					if(result.pojo.result == null){
						IQB.alert('未生成报告,稍后再试');
						return false;
					}else{
						url = '/etep.web/view/riskReport/reports/html/info_report.html?orderId='+window.procBizId+'&reportType=1';
						url = encodeURI(url);
						window.open(url);
					}
				})
			}else{
				//贷前反欺诈
				IQB.post(urls['cfm'] + '/afterLoan/getReportByReprotNo', {'orderId': window.procBizId,'reportType':2}, function(result){
					if(result.pojo.result == null){
						IQB.alert('未生成报告,稍后再试');
						return false;
					}else{
						url = '/etep.web/view/riskReport/reports/html/loanfraud_report.html?orderId='+window.procBizId+'&reportType=2';
						url = encodeURI(url);
						window.open(url);
					}
				})
			}
		},
		getOption : function(){
			IQB.post(urls['cfm'] + '/business/getApprovalOpinion', {'orderId': window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#telOption').text(result.telApprovalOpinion);
				$('#riskOption').text(result.riskApprovalOpinion);
			})
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.showText();
			_this.sublet();
			_this.showCarInfo();
			_this.showPreFee();
			_this.getOption();
			//监听风控报告是否生成
			_this.riskReport();
			
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
	IQB.riskDepartmentApprovalView.init();
});


