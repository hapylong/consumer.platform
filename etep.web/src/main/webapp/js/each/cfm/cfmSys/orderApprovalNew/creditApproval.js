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
$package('IQB.creditApproval');
IQB.creditApproval = function(){
	var _this = {
		cache: {
			viewer: {}
		},	
		approve: function() {
			var approveForm = $('#approveForm').serializeObject();
			var saveOption = {};
			var useCreditLoan = $('#useCreditLoan_c').val();
			if(useCreditLoan =='1'||useCreditLoan==1){
				saveOption.orderId = window.procBizId+'X';
				var orderAmt = parseFloat($('#orderAmt_c').val());
				saveOption.orderAmt = orderAmt;
				var planId = $('#planId_c option:selected').val();
				saveOption.planId = planId;
				if(orderAmt>0){
					if(planId_c == "1"||planId_c == 1){
						IQB.alert("金额大于0时，产品方案必选");
						return false;
					}
					IQB.post(urls['cfm'] + '/credit_pro/persist_plan_details_x', saveOption, function(resultPlan){
						_this.approveSubmit();
					});
				}else{
					_this.approveSubmit();
				}
			}else{
				_this.approveSubmit();
			}
		},
		approveSubmit: function() {
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
			if(_this.cache.useCreditLoan =='1'||_this.cache.useCreditLoan==1){
				if($('#orderAmt_c').val() == '' || $('#gpsAmt2').val() == '' || $('#payJqInsurance').val() == '' || $('#paySyInsurance').val() == '' || $('#taxAmt2').val() == '' || $('#serverAmt').val() == ''){
					IQB.alert('请输入相关费用！');
					return false;
				}else if(Number(Formatter.removeMoneyPrefix($('#preOtherAmt').text())) < 0){
					IQB.alert('其它支付费用为负！无法提交！');
					return false;
				}else{
					_this.savePreFee();
				}
			}else{
				$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
			}
		},
		savePreFee : function(){
			var totalCost = $('#orderAmt_c').val();
			var gpsAmt = $('#gpsAmt2').val();
			var payJqInsurance = $('#payJqInsurance').val();
			var paySyInsurance = $('#paySyInsurance').val();
			var taxAmt = $('#taxAmt2').val();
			var serverAmt = $('#serverAmt').val();
			var otherAmt = window.floatSub(totalCost , (Number(gpsAmt) + Number(payJqInsurance) + Number(paySyInsurance) + Number(taxAmt) + Number(serverAmt))); 
			var option = {
					'orderId': window.procBizId+'X',
					'otherAmt':otherAmt,
					'gpsAmt':gpsAmt,
					'riskAmt':payJqInsurance,
					'preBusinessTaxAmt':paySyInsurance,
					'taxAmt':taxAmt,
					'serverAmt':serverAmt
			};
			//insert
			IQB.post(urls['cfm'] + '/order/otherAmt/saveOrUpdateCmd/true', option, function(result){
				if(result.iqbResult.result == 'success'){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}
			})
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
				//处理车辆估价
				if(result.bizType == '2001'){
					$('#oldCar').hide();
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{					
					$('#oldCar').show();
					$('.old-tr').show();
					$('.new-tr').hide();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				$('#merchantNo').val(result.merchantNo);
				$('#assessPrice').text(result.assessPrice);
				
				var useCreditLoan = result.useCreditLoan;
				_this.cache.useCreditLoan = result.useCreditLoan;
				$('#useCreditLoan').text(Formatter.isUseCreditLoan(useCreditLoan));
				$('#useCreditLoan_c').val(useCreditLoan);
				
				if(useCreditLoan =='1'||useCreditLoan==1){
					$('#creditInfo').show();
				}else{
					$('#creditInfo').hide();
				}
				//$('#amtLines').text(result.amtLines);
				
				IQB.post(urls['cfm'] + '/business/selOrderInfo', {orderId: window.procBizId}, function(resultChild){
					$('#carAmt').text(Formatter.money(resultChild.iqbResult.result.carAmt));
					$('#gpsAmt').text(Formatter.money(resultChild.iqbResult.result.gpsAmt));
					$("#jqInsurance").html(Formatter.money(resultChild.iqbResult.result.insAmt));
					$("#syInsurance").html(Formatter.money(resultChild.iqbResult.result.businessTaxAmt));
					$('#taxAmt').text(Formatter.money(resultChild.iqbResult.result.taxAmt));	
					$('#otherAmt').text(Formatter.money(resultChild.iqbResult.result.otherAmt));
				});
				IQB.post(urls['cfm'] + '/credit_pro/plan_details_by_criteria', {merchantNo: result.merchantNo,bizType:'2300'}, function(resultPlan){
					var arry = [];
					$.each(resultPlan.iqbResult.result, function(i, n){
						var obj = {};
						obj.id = n.id;
						obj.text = n.planShortName;
						arry.push(obj);
					});
					$('#planId_c').prepend("<option value='1'>请选择</option>");
					$('#planId_c').select2({theme: 'bootstrap', data: arry});
				});
				
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
		selectEvent:function(){
			$("#planId_c").change(function(){
				var key = $(this).children('option:selected').val();
				var orderAmt = $('#orderAmt_c').val();
				if(key =="1"){
					$('#orderItems_c').text("");
					$('#fee').text("");
					$('#monthInterest_c').text("");
					return false;
				}
				if(key!="1"&&parseFloat(orderAmt)<=0){
					IQB.alert("金额必须大于0");
					$("#planId_c").val("1");
					$('#orderItems_c').text("");
					$('#fee').text("");
					$('#monthInterest_c').text("");
					return false;
				}
				var option  = {}
				option.planId = key;
				option.orderAmt = orderAmt;
				IQB.post(urls['cfm'] + '/credit_pro/SpeResetOrderAmt', option, function(resultChild){
					$('#orderItems_c').text(resultChild.iqbResult.result.orderItems);
					$('#margin_c').text(Formatter.money(resultChild.iqbResult.result.margin));
					$('#serviceFee_c').text(Formatter.money(resultChild.iqbResult.result.serviceFee));
					$('#sbAmt_c').text(Formatter.money(resultChild.iqbResult.result.sbAmt));
					$('#contractAmt_c').text(Formatter.money(resultChild.iqbResult.result.contractAmt));
					$('#loanAmt_c').text(Formatter.money(resultChild.iqbResult.result.loanAmt));
					$('#monthMake_c').text(Formatter.money(resultChild.iqbResult.result.monthInterest));
				});
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
				$("#insurancePrice").html(Formatter.money(result.iqbResult.result.insAmt));
				$("#purchasePrice").html(Formatter.money(result.iqbResult.result.taxAmt));
				$("#otherPrice").html(Formatter.money(result.iqbResult.result.otherAmt));
			})
		},
		confirm: function(msg){
			$('#confirm-msg').html(msg);
			$('#confirm-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		calPreFee : function(){
			var totalCost = $('#orderAmt_c').val();
			var gpsAmt = $('#gpsAmt2').val();
			var payJqInsurance = $('#payJqInsurance').val();
			var paySyInsurance = $('#paySyInsurance').val();
			var taxAmt = $('#taxAmt2').val();
			var serverAmt = $('#serverAmt').val();
			var preOtherAmt;
			if(totalCost != 0 && gpsAmt != '' && payJqInsurance != '' && paySyInsurance != '' && taxAmt != '' && serverAmt != ''){
				preOtherAmt = window.floatSub(totalCost , (Number(gpsAmt) + Number(payJqInsurance) + Number(paySyInsurance) + Number(taxAmt) + Number(serverAmt))); 
				$('#preOtherAmt').text(Formatter.money(preOtherAmt));
			}
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.selectEvent();
			_this.showFile();
			_this.showText();
			
			$('#btn-approve').unbind('click').on('click', function(){
				var useCreditLoan = $('#useCreditLoan_c').val();
				if('1'==useCreditLoan||useCreditLoan==1){
					var orderAmt_c = parseFloat($('#orderAmt_c').val());
					if(orderAmt_c <= 0){
						_this.confirm("借款金额为0，是否继续？");
					}else{
						var planId_c = $("#planId_c").val();
						if(planId_c == "1"||planId_c == 1){
							IQB.alert("金额大于0时，产品方案必选");
							return false;
						}
						var amtLines = $('#amtLines').text();
						var amtLine =  amtLines.split("-");
						/*var amtLineStart =amtLine[0];
						var amtLineEnd = amtLine[1];
						if(orderAmt_c< parseFloat(amtLineStart)||orderAmt_c > parseFloat(amtLineEnd)){
							IQB.alert("借款金额必须在授权额度范围内");
							return false;
						}*/
						_this.openApproveWin();
					}
				}else{
					_this.openApproveWin();
				}
			});
			$('#btn-confirmWin-confirm').on('click', function(){$('#confirm-win').modal('hide');_this.approve()});
			$('#btn-confirmWin-cancel').on('click', function(){$('#confirm-win').modal('hide');_this.init()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
		}
	}
	return _this;
}();

$(function(){
	IQB.creditApproval.init();
});		