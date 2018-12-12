
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
$package('IQB.manualApproval');
IQB.manualApproval = function(){
	var _this = {
		cache: {
			viewer: {},
			preAmount:''
		},	
		approve: function() {
			var approveForm = $('#approveForm').serializeObject();
			var assessPrice = parseFloat($('#assessPrice').val());
			var carAmt = parseFloat( _this.cache.carAmt);
			var useCreditLoan =  parseInt($('#useCreditLoan option:selected').val());
			if (approveForm.approveStatus) {
				if (approveForm.approveStatus == "1") {
					if($.trim(approveForm.approveOpinion) == '') {
						approveForm.approveOpinion = "同意";
					}
					var saveOption = {}
					saveOption.orderId = $('#orderId').text();
					saveOption.assessPrice = assessPrice;
					saveOption.orderAmt = parseFloat(_this.cache.orderAmt);
					saveOption.carAmt = assessPrice;
					saveOption.planId = $('#planId').val();
					saveOption.useCreditLoan = useCreditLoan;
					if(useCreditLoan =='1'||useCreditLoan==1){
						saveOption.amtLines = parseFloat(1000)+'-'+parseFloat(10000);
					}
					IQB.post(urls['cfm'] + '/credit_pro/GPI_manager', saveOption, function(result){});
				}else {
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
				variableData.carAmt = carAmt;
				variableData.orderAmt = assessPrice;
				variableData.useCreditLoan = $("#useCreditLoan").val();
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.useCreditLoan = $("#useCreditLoan").val();
				bizData.carAmt = carAmt;
				bizData.orderAmt = assessPrice;
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
				_this.cache.bizType = result.bizType;
				_this.showText();
				//处理车辆估价
				if(result.bizType == '2001'){
					$('.new-tr').show();
					$('.old-tr').hide();
					$('#evaluate-t').hide();
				}else{					
					$('.old-tr').show();
					$('.new-tr').hide();
					$('#evaluate-t').show();
				}
				$('#hasUseCreditLoan').show();
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				_this.cache.orderAmt = result.orderAmt;
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
				$('#merchantNo').val(result.merchantNo);
				$('#planId').val(result.planId);
				_this.hasUseCreditLoan();
				IQB.post(urls['cfm'] + '/business/selOrderInfo', {orderId: window.procBizId}, function(resultChild){
					$('#carAmt').text(Formatter.money(resultChild.iqbResult.result.carAmt));
					_this.cache.carAmt = resultChild.iqbResult.result.carAmt;
					$('#assessPrice').val(resultChild.iqbResult.result.carAmt);
					$('#gpsAmt').text(Formatter.money(resultChild.iqbResult.result.gpsAmt));
					$("#jqInsurance").html(Formatter.money(resultChild.iqbResult.result.insAmt));
					$("#syInsurance").html(Formatter.money(resultChild.iqbResult.result.businessTaxAmt));
					$('#taxAmt').text(Formatter.money(resultChild.iqbResult.result.taxAmt));	
					$('#otherAmt').text(Formatter.money(resultChild.iqbResult.result.otherAmt));
				});
			});
		},
		hasUseCreditLoan: function(){//判断用户是否已经存在车秒贷
			var option = {}
			option.merchantNo=$('#merchantNo').val();
			option.biztype='2300';
			IQB.post(urls['cfm'] + '/credit_pro/merchant_certificate', option, function(result){
				if(result.iqbResult.result != 1){
					$('#hasUseCreditLoan').hide();
					$("#useCreditLoan").find("option[value='0']").attr("selected",true); 
					$("#useCreditLoan").attr("disabled",true);
				}else{
					$('#hasUseCreditLoan').hide();
					$("#useCreditLoan").attr("disabled",false);
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
				_this.cache.carNo = result.iqbResult.AuthorityCardInfo.carNo;
				$("#engineNo").html(result.iqbResult.AuthorityCardInfo.engine);
				$("#licenseNumber").html(result.iqbResult.AuthorityCardInfo.plate);
				if(result.iqbResult.AuthorityCardInfo.plateType == '01'){
					$("#carType").html('大型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '02'){
					$("#carType").html('小型汽车');
				}else if(result.iqbResult.AuthorityCardInfo.plateType == '15'){
					$("#carType").html('挂车');
				}
				if(_this.cache.bizType == '2002'){
					//监听车e估报告和照片是否生成
					_this.report();
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
		selectEvent:function(){
			$("#useCreditLoan").change(function(){
				var key = $(this).children('option:selected').val();
				if(key!=null&&key!=""){
					if('0'==key){
						$('#hasUseCreditLoan').hide();
					}else{
						$('#hasUseCreditLoan').show();
					}
				}
			});
		},
		assessPriceEvent:function(){
			$('#assessPrice').on('blur',function(){
				var value = $('#assessPrice').val();
				var carAmt = _this.cache.carAmt;
				if(parseFloat(value)>parseFloat(carAmt)){
					IQB.alert("评估金额大于购车价格");
					return;
				}
				_this.calculate(value);
			});
		},
		changeAssessPrice :function (){
			 location.reload();
		},
		calculate : function (value){
			var gpsAmt = Formatter.removeMoneyPrefix($('#gpsAmt').text());
			var insAmt = Formatter.removeMoneyPrefix($('#jqInsurance').text());
			var businessTaxAmt = Formatter.removeMoneyPrefix($('#syInsurance').text());
			var taxAmt = Formatter.removeMoneyPrefix($('#taxAmt').text());	
			var otherAmt = Formatter.removeMoneyPrefix($('#otherAmt').text());
			if(value == '') {
				value = 0;
			}
			if(gpsAmt == '') {
				gpsAmt = 0;
			}
			if(insAmt == '') {
				insAmt = 0;
			}
			if(businessTaxAmt == '') {
				businessTaxAmt = 0;
			}
			if(taxAmt == '') {
				taxAmt = 0;
			}
			if(otherAmt == '') {
				otherAmt = 0;
			}
			var orderAmt = parseFloat(value)+parseFloat(gpsAmt)+parseFloat(insAmt)+parseFloat(businessTaxAmt)+parseFloat(taxAmt)+parseFloat(otherAmt)
			if(orderAmt == 0 || orderAmt == null || orderAmt == 'null') {
				return;
			}
			var option = {};
			option.orderAmt = parseFloat(orderAmt)
			option.planId = $('#planId').val();
			
			IQB.post(urls['cfm'] + '/credit_pro/reset_order_amt', option, function(result){
				var resl = result.iqbResult.result;
				$('#preAmount').text(Formatter.money(resl.preAmt));
				$('#downPayment').text(Formatter.money(resl.downPayment));
				$('#margin').text(Formatter.money(resl.margin));
				$('#serviceFee').text(Formatter.money(resl.serviceFee));
				$('#feeAmount').text(Formatter.money(resl.feeAmount));
				$('#orderItems').text(resl.orderItems);
				$('#monthInterest').text(Formatter.money(resl.monthInterest));
				$('#orderAmt').text(parseFloat(orderAmt));
			});
		},
		showPreFee : function(){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': window.procBizId}, function(result){
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
						$('#assessMsgAmt').text(Formatter.money(result.assessMsgAmt));
						$('#inspectionAmt').text(Formatter.money(result.inspectionAmt));
						$('#preOtherAmt').text(Formatter.money(result.otherAmt))
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
		report : function(){
			IQB.post(urls['cfm'] + '/business/getCarAssessReport', {'orderId': window.procBizId,'vin':_this.cache.carNo}, function(result){
			    if(result.iqbResult.result.msg == 'success'){
			    	_this.cache.carReportUrl = result.iqbResult.result.carReportUrl;
			    	if(_this.cache.carReportUrl == ''){
			    		$('.reportAbout').hide();
			    		$('.evaluateReason').show();
						$('#evaluateReason').val(result.iqbResult.result.refuseMsg);
			    	}
					//加载图片
					var is2 = false;
					$.each(result.iqbResult.result.imageList, function(i, n){
						var html = '<div class="thumbnail float-left" style="width: 145px;margin-top:8px;">' + 
									      			'<a href="javascript:void(0)"><img src="' + n.url + '" alt="' + n.tag + '" style="width: 135px; height: 135px;" /></a>' +
									      			'<div class="caption">' +
									      				'<h5>' + n.tag + '</h5>' +
									      			'</div>' + 
									      		'</div>';
									$('#td-30').append(html);
									is2 = true;
					});
					if(is2){
						_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
					}
					//加载视频
					if(result.iqbResult.result.videolist && result.iqbResult.result.videolist.length > 0){
						$('.videoInfo').show();
						$.each(result.iqbResult.result.videolist, function(i, n){
							var videoHtml = '<video src="'+n.url+'" controls="controls" width="250px;" height="300px">您的浏览器不支持。</video>';
							$('#td-31').append(videoHtml);
						});
					}else{
						$('.videoInfo').hide();
					}
			    }else{
			    	IQB.alert(result.iqbResult.result.msg);
			    }
			});
		},
		showReport : function(reportUrl){
			if(_this.cache.carReportUrl == '' || _this.cache.carReportUrl == undefined){
				IQB.alert('未返回评估报告，请核实');
			}else{
				$('.riskReport').attr('href',_this.cache.carReportUrl);
			}
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.selectEvent();
			_this.assessPriceEvent();
			_this.showPreFee();
			
			$('#btn-approve').on('click', function(){
				if($("#assessPrice").val() == '') {
					IQB.alert("车辆估价不为空");
					return;
				}
				var useCreditLoan =  parseInt($('#useCreditLoan option:selected').val());
				if(useCreditLoan =='1'||useCreditLoan==1){
					/*var amtLinestart = parseFloat($('#amtLines').val());
					var amtLines_end = parseFloat($('#amtLines_end').val());*/
					var carAmt = parseFloat( _this.cache.carAmt);
					if(parseFloat($('#assessPrice').val())>parseFloat(carAmt)){
						IQB.alert("评估金额大于购车价格");
						return;
					}
					/*if(amtLinestart<1000){
						IQB.alert('授权额度不低于1000');
						return false;
					}
					if(amtLines_end>10000){
						IQB.alert('授权额度不高于10000');
						return false;
					}
					if(amtLinestart>amtLines_end){
						IQB.alert('授权额度最小范围不能高于最大范围');
						return false;
					}*/
				}
				_this.openApproveWin();
			});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function(){
	IQB.manualApproval.init();
});		