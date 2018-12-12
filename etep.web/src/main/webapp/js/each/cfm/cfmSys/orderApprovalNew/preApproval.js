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

/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/
function floatSub(arg1,arg2){ 
	  var r1,r2,m,n; 
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	  m=Math.pow(10,Math.max(r1,r2)); 
	  n=(r1>=r2)?r1:r2; 
	  return ((arg1*m-arg2*m)/m).toFixed(n); 
}
$package('IQB.preApproval');
IQB.preApproval = function(){
	var _this = {
		cache: {
			viewer: {},
			orderAmt : 0,
			preAmount : '',
			result : '',
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
				variableData.isSublet = $('#sublet').val();
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.isSublet = $('#sublet').val();
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
		//初始化客户渠道名称
		initCustChannels: function(){
			IQB.post(urls['rootUrl'] + '/car_dealer/cget_info', {'type':0}, function(result) {
				$('#customerName').typeahead({
			        source:  result.iqbResult.result,
			        //display:'text'
			    });
			})
			
		},
		//初始化车商名称
		initSourceCarName: function(){
			IQB.post(urls['rootUrl'] + '/car_dealer/cget_info', {'type':1}, function(result) {
				$('#carName').typeahead({
			        source:  result.iqbResult.result,
			        //display:'text'
			    });		
			})
		},
		carChannels : function(){
			var carChannels = $('#carChannels').val();
			if(carChannels == 1 || carChannels == 2){
				$('.CTD').show();
			}else{
				$('.CTD').hide();
			}
		},
		carName :function(){
			//是否黑名单
			IQB.post(urls['cfm'] + '/dealer/judgeBlack', {'sourceCarName':$('#carName').val()}, function(result){
				var result = result.iqbResult.result;
				if(result == 'Y'){
					IQB.alert('此商户为黑名单商户，不可输入');
					$('#carName').val('');
				}
			})
		},
		//客户渠道为车商/4s店时，输入名称，车来源/名称自动回显
		customerName : function(){
			var customerName = $('#customerName').val();
			var customerChannels = $('#customerChannels').val();
			if(customerChannels == 3 || customerChannels == 4){
				$('#carChannels').val(customerChannels).attr('disabled','disabled');
				$('#carName').val(customerName).attr('disabled','disabled');
			}else{
				$('#carChannels').val('').removeAttr('disabled');
				$('#carName').val('').removeAttr('disabled');
			}
		},
		customerChannels : function(){
			var customerChannels = $('#customerChannels').val();
			if(customerChannels == 3 || customerChannels == 4){
				var customerName = $('#customerName').val();
				$('#carChannels').val(customerChannels).attr('disabled','disabled');
				$('#carName').val(customerName).attr('disabled','disabled');
				$('.CTD').hide();
			}else{
				$('#carChannels').val('').removeAttr('disabled');
				$('#carName').val('').removeAttr('disabled');
			}
		},
		openApproveWin: function(){
			if($('#vehiclePrice').val() == ''){
				IQB.alert('订单金额不完善，无法审核');
				return false;
			}
			if($('#GPSPrice').val() == ''){
				IQB.alert('订单金额不完善，无法审核');
				return false;
			}
			if($('#jqInsurance').val() == ''){
				IQB.alert('订单金额不完善，无法审核');
				return false;
			}
			if($('#syInsurance').val() == ''){
				IQB.alert('订单金额不完善，无法审核');
				return false;
			}
			if($('#otherPrice').val() < 0 ){
				IQB.alert('订单金额拆分有误，无法审核');
				return false;
			}
			//驾驶证档案编号去掉必填
			/*if($("#fileNumber").val().length != 12){
				IQB.alert('请输入12位驾驶证档案编号');
				return false;
			}*/
			if($('#td-12').find('div').length == 0){
				IQB.alert('风控信息不完善，无法审核');
				return false;
			};
			if($('#td-27').find('div').length == 0){
				IQB.alert('风控信息不完善，无法审核');
				return false;
			};
			var groupName = $('#groupName').text();
			if(groupName == '以租代售新车'){
				if($('#td-23').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
			}else{
				if($('#td-15').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-16').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-17').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-18').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-19').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($("#VIN").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				}
				if($("#engineNo").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				}
				if($("#licenseNumber").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				}
			}
			//车商必填项校验
			/*if($('#customerName').val() == '' || $('#carName').val()=='' || $('#address').val()=='' || $('#phone').val()==''){
				IQB.alert('车商信息不完善，无法审核');
				return false;
			}*/
			
			//判断是否转租
			var sublet = $('#sublet').val();
			if(sublet == 1){
				if($('#oldOrderId').val() == ''){
					IQB.alert('转租订单号为空，无法审核');
					return false;
				}
			}
			//保存车辆信息
			var VIN = $("#VIN").val();
			var engineNo = $("#engineNo").val();
			var licenseNumber = $("#licenseNumber").val();
			var carType = $("#carType").val();
			var fileNumber = $('#fileNumber').val();
			var data = {
					'carNo' : VIN,
					'engine' : engineNo,
					'plate' : licenseNumber,
					'plateType' : carType,
					'orderId' : window.procBizId,
					'driverLicenseNum':fileNumber
			}
			IQB.post(urls['cfm'] + '/authoritycard/save', data, function(result){
				if(result.success == 1){
					_this.saveAmt();
				}
			});
		},
		saveAmt: function(){
			//保存订单金额信息
			var carAmt = $("#vehiclePrice").val();
			var gpsAmt = $("#GPSPrice").val();
			var jqInsurance = $("#jqInsurance").val();
			var syInsurance = $("#syInsurance").val();
			var taxAmt = $("#purchasePrice").val();
			var otherAmt = $("#otherPrice").val();
			if(carAmt == ''){
				carAmt = 0;
			}
			if(gpsAmt == ''){
				gpsAmt = 0;
			}
			if(jqInsurance == ''){
				jqInsurance = 0;
			}
			if(syInsurance == ''){
				syInsurance = 0;
			}
			if(taxAmt == ''){
				taxAmt = 0;
			}
			if(otherAmt == ''){
				otherAmt = 0;
			}
			if(_this.cache.backFlag == 1){
				var json = {
						'carAmt' : carAmt,
						'gpsAmt' : gpsAmt,
						'insAmt' : jqInsurance,
						'businessTaxAmt': syInsurance,
						'taxAmt' : taxAmt,
						'otherAmt' : otherAmt,
						'orderId' : window.procBizId
				};
				IQB.post(urls['cfm'] + '/car_dealer/resetAmt', json, function(result){
					if(result.success == 1) {
						var option = {
								'orderAmt':$('#orderAmt').text(),
								'carAmt' : carAmt,
								'gpsAmt' : gpsAmt,
								'insAmt' : jqInsurance,
								'businessTaxAmt': syInsurance,
								'taxAmt' : taxAmt,
								'otherAmt' : otherAmt,
								'orderId' : window.procBizId,
								'employeeID':$('#employeeID').val(),
						        'employeeName':$('#employeeName').val()
						}
						IQB.post(urls['cfm'] + '/business/saveBrkOrderInfo', option, function(result){
							if(result.success == 1){
								_this.saveCarInfo();
							}
						})
					}
				});
			}else{
				var option = {
						'orderAmt':$('#orderAmt').text(),
						'carAmt' : carAmt,
						'gpsAmt' : gpsAmt,
						'insAmt' : jqInsurance,
						'businessTaxAmt': syInsurance,
						'taxAmt' : taxAmt,
						'otherAmt' : otherAmt,
						'orderId' : window.procBizId,
						'employeeID':$('#employeeID').val(),
				        'employeeName':$('#employeeName').val()
				}
				IQB.post(urls['cfm'] + '/business/saveBrkOrderInfo', option, function(result){
					if(result.success == 1){
						_this.saveCarInfo();
					}
				})
			}
		},
		//保存车商信息
		saveCarInfo: function(){
			var option = {
					"orderId": $('#orderId').text(),
				    "custChannels": $('#customerChannels').val(),
				    "custChannelsName": $('#customerName').val(),
				    "sourceCar": $('#carChannels').val(),
				    "sourceCarName": $('#carName').val(),
				    "address": $('#address').val(),
				    "contactMethod": $('#phone').val(),
				    "maritalStatus": $('#marriedStatus').val(),
				    "contactName": $('#coName').val(),
				    "contactMobile": $('#coPhone').val(),
				    "contactAddr": $('#coAddress').val()
			}
			IQB.post(urls['cfm'] + '/dealer/saveDealerInfo', option, function(result){
				if(result.iqbResult.result == 'success'){
					_this.saveSubletInfo();
				}
			})
		},
		saveSubletInfo : function(){
			var sublet = $('#sublet').val();
			if(sublet == 1){
				var rollOverItems;
				var manageFee;
				if($('#rollOverFlag').val() == 0){
					//不展期
					rollOverItems = '0';
				}else{
					rollOverItems = $('#rollOverItems').val();
					if(rollOverItems == ''){
						rollOverItems = '0';
					}
				}
				//管理费
				if($('#manageFeeFlag').val() == 0){
					//无管理费
					manageFee = '0';
				}else{
					manageFee = $('#manageFee').val();
					if(manageFee == ''){
						manageFee = '0';
					}
				}
				var //展期
				option = {
						'orderId' : $('#orderId').html(),
						'regId' : $('#regId').html(),
						'subletOrderId' : $('#oldOrderId').val(),
						'subletRegId' : $('#oldRegId').val(),
						'rollOverItems' : rollOverItems,
						'rollOverFlag' : $('#rollOverFlag').val(),
						'manageFeeFlag' : $('#manageFeeFlag').val(),
						'manageFee' : manageFee,
						'overItems':$('#overItems').val()
				}
				IQB.post(urls['cfm'] + '/sublet/persist_sublet_entity', option, function(result){
					if(result.iqbResult.result == 'success'){
						_this.validateIsPreFee();
					}
				})
			}else{
				//不转租
				_this.validateIsPreFee();
			}
		},
		validateIsPreFee : function(){
			//是否前期费用相关
			if($('#isPreAmt').val() == ''){
				IQB.alert('请选择是否收取前期费用！');
				return false;
			}
			if($('#isPreAmt').val() == 1){
				//有前期费用
				if($('#online').val() == ''){
					IQB.alert('请选择是否线上收取！');
					return false;
				}
				if($('#totalCost').val() == '' || $('#gpsAmt').val() == '' || $('#payJqInsurance').val() == '' || $('#paySyInsurance').val() == '' || $('#taxAmt').val() == '' || $('#serverAmt').val() == ''){
					IQB.alert('请输入相关费用！');
					return false;
				}
				if(Number(Formatter.removeMoneyPrefix($('#preOtherAmt').text())) < 0){
					IQB.alert('其它支付费用为负！无法提交！');
					return false;
				}
				_this.savePreFee();
			}else{
				_this.savePreFee();
			}
		},
		savePreFee : function(){
			var online = $('#online').val();
			var totalCost = $('#totalCost').val();
			var gpsAmt = $('#gpsAmt').val();
			//var riskAmt = $('#riskAmt').val();
			var payJqInsurance = $('#payJqInsurance').val();
			var paySyInsurance = $('#paySyInsurance').val();
			var taxAmt = $('#taxAmt').val();
			var serverAmt = $('#serverAmt').val();
			var assessMsgAmt = $('#assessMsgAmt').val();
			var inspectionAmt = $('#inspectionAmt').val();
			var preOtherAmt = $('#preOtherAmt').val();
			var isPreAmt = $('#isPreAmt').val();
			var option = {
					'orderId': window.procBizId,
					'online':online,
					'totalCost':totalCost,
					'gpsAmt':gpsAmt,
					'riskAmt':payJqInsurance,
					'preBusinessTaxAmt':paySyInsurance,
					'taxAmt':taxAmt,
					'serverAmt':serverAmt,
					'assessMsgAmt':assessMsgAmt,
					'inspectionAmt':inspectionAmt,
					'otherAmt':preOtherAmt,
					'preAmtFlag':isPreAmt
			};
			if(_this.cache.result != '' && _this.cache.result != null){
				//update
				IQB.post(urls['cfm'] + '/order/otherAmt/saveOrUpdate/false', option, function(result){
					if(result.iqbResult.result == 'success'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}else{
				//insert
				IQB.post(urls['cfm'] + '/order/otherAmt/saveOrUpdate/true', option, function(result){
					if(result.iqbResult.result == 'success'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}
		},
		cal: function(){
			var vehiclePrice = $('#vehiclePrice').val();
			var GPSPrice = $('#GPSPrice').val();
			var jqInsurance = $('#jqInsurance').val();
			var syInsurance = $('#syInsurance').val();
			var purchasePrice = $('#purchasePrice').val();
			if(_this.cache.backFlag == 1) {
				var otherPrice = $('#otherPrice').val();
				var orderAmt = 0;
				if(vehiclePrice == '') {
					vehiclePrice = 0;
				}
				orderAmt = _this.floatAdd(vehiclePrice, orderAmt);
				if(GPSPrice == '') {
					GPSPrice = 0;
				}
				orderAmt = _this.floatAdd(GPSPrice, orderAmt);
				if(jqInsurance == '') {
					jqInsurance = 0;
				}
				orderAmt = _this.floatAdd(jqInsurance, orderAmt);
				if(syInsurance == '') {
					syInsurance = 0;
				}
				orderAmt = _this.floatAdd(syInsurance, orderAmt);
				if(purchasePrice == '') {
					purchasePrice = 0;
				}
				orderAmt = _this.floatAdd(purchasePrice, orderAmt);
				if(otherPrice == '') {
					otherPrice = 0;
				}
				orderAmt = _this.floatAdd(otherPrice, orderAmt);
				$('#orderAmt').text(orderAmt);
				
				IQB.post(urls['cfm'] + '/car_dealer/reculateAmt', {'orderId': window.procBizId, 'orderAmt':orderAmt}, function(result){
					if(result.success == 1) {
						result = result.iqbResult.result;
						$("#preAmount").text(result.preAmount);
						_this.cache.preAmount = result.preAmount;
						$("#downPayment").text(result.downPayment);
						$("#margin").text(result.margin);
						$("#serviceFee").text(result.serviceFee);
						$("#feeAmount").text(result.feeAmount);
						$("#orderItems").text(result.orderItems);
						$("#monthInterest").text(result.monthMake);
					}
				});
			} else {
				if(_this.cache.orderAmt != 0 && vehiclePrice != '' && GPSPrice != '' && jqInsurance != '' && syInsurance != ''){
		            $('#otherPrice').val(_this.cache.orderAmt - vehiclePrice - GPSPrice - jqInsurance - syInsurance - purchasePrice);
				}else{
					$('#otherPrice').val('');
				}
			}
		},
		floatAdd: function (f1,f2){
			var r1,r2,m;
			try{r1=f1.toString().split(".")[1].length}catch(e){r1=0}
			try{r2=f2.toString().split(".")[1].length}catch(e){r2=0}
			m=Math.pow(10,Math.max(r1,r2))
			return (f1*m + f2*m)/m
		}, 
		closeApproveWin: function(){
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
					$('.new-li').show();
					$('.new-tr').show();
					$('.old-li').hide();
					$('.old-tr').hide();
				}else{
					$('.old-li').show();
					$('.old-tr').show();
					$('.new-li').hide();
					$('.new-tr').hide();
				}
				/*判断风控资料中的信息*/
				if(result.bizType == '2001'){
					$(".VINs").hide();
				}
				$('#orderAmt').text(result.orderAmt);
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
				$('#greenChannel').val(Formatter.greenChannel(result.greenChannel));
				if(result.backFlag == 1) {
					//$('#btn-resetAmt').show();
					$('#otherPrice').removeAttr("readonly");
				}
				_this.cache.backFlag = result.backFlag;
			});
		},
		sublet : function(){
           var sublet = $('#sublet').val();
           if(sublet == 1){
        	   $('.noSublet').show();
        	   $('.noRollOver').hide();
        	   $('.noManageFee').hide();
        	   
           }else{
        	   $('.noSublet').hide();
           }
		},
		rollOverFlag : function(){
			var rollOverFlag = $('#rollOverFlag').val();
	           if(rollOverFlag == 1){
	        	   $('.noRollOver').show();
	           }else{
	        	   $('.noRollOver').hide();
	           }
		},
		manageFeeFlag : function(){
			var manageFeeFlag = $('#manageFeeFlag').val();
	           if(manageFeeFlag == 1){
	        	   $('.noManageFee').show();
	           }else{
	        	   $('.noManageFee').hide();
	           }
		},
		subletInfo : function(){
			IQB.get(urls['cfm'] + '/sublet/get_sublet_info', {orderId: $('#oldOrderId').val()}, function(result){
				var result = result.iqbResult.result;
				//回显项赋值
				$('#oldRegId').val(result.regId);
				$('#oldRealName').val(result.realName);
				$('#planShortName').val(result.planShortName);
				$('#oldMonthInterest').val(result.monthInterest);
				$('#oldOrderItems').val(result.orderItems);
				$('#overItems').val(result.overItems);
			})
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
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.preApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		uploadFile: function(){
			var files = $('#file').get(0).files;
			var mark = false;
			$.each(files, function(i, n){
				var extensionName = Formatter.getExtensionName(n.name);
				if(Formatter.extensionName.doc.contain(extensionName)){
					mark = true;				
					return false;
				}else if(Formatter.extensionName.pic.contain(extensionName)){
					
				}else{
					mark = true;						
					return false;
				}
			});			
			if(mark){
				$('#file').val('');
				IQB.alert('格式不支持');
				return false;
			}
			
			$('#btn-upload').prop('disabled', true);
			$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/cfm');
			IQB.postForm($('#uploadForm'), function(result){
				var fileType = $('#file').attr('fileType');	
				var arr = [];
				var html = '';
				$.each(files, function(i, n){
					var option = {};
					option.orderId = window.procBizId;
					option.imgType = fileType;
					option.imgName = n.name;
					option.imgPath = result.iqbResult.result[i];
					arr.push(option);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
					      		'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + option.imgPath + '" alt="' + option.imgName + '" style="width: 135px; height: 135px;" /></a>' +
					      		 '<div class="caption">' +
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.preApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(_this.cache.viewer.viewerOne){
						_this.cache.viewer.viewerOne.update();
					}else{
						_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					}
				});
			});	
		},
		removeFile: function(event){
			if(event.stopPropagation){//W3C阻止冒泡方法  
				event.stopPropagation();  
			}else{//IE阻止冒泡方法   
				event.cancelBubble = true;
			}  	
			var tarent = event.currentTarget;
			$(tarent).prop('disabled', true);
			$(tarent).find('span').first().removeClass('glyphicon glyphicon-trash').addClass('fa fa-spinner fa-pulse');
			var filePath = $(tarent).attr('filePath');		
			IQB.post(urls['cfm'] + '/fileUpload/remove', {filePath: filePath}, function(result){
				IQB.post(urls['cfm'] + '/image/deleteImage', {imgPath: filePath}, function(resultInfo){
					$(tarent).parent().parent().parent().remove();
		 		});
			});		
		},
		showText : function(){
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
				$("#VIN").val(result.iqbResult.AuthorityCardInfo.carNo);
				$("#engineNo").val(result.iqbResult.AuthorityCardInfo.engine);
				$("#licenseNumber").val(result.iqbResult.AuthorityCardInfo.plate);
				$("#carType").val(result.iqbResult.AuthorityCardInfo.plateType);
				$("#fileNumber").val(result.iqbResult.AuthorityCardInfo.driverLicenseNum);
			})
			IQB.post(urls['cfm'] + '/business/selOrderInfo', {'orderId': window.procBizId}, function(result){
				$("#vehiclePrice").val(result.iqbResult.result.carAmt);
				$("#GPSPrice").val(result.iqbResult.result.gpsAmt);
				$("#jqInsurance").val(result.iqbResult.result.insAmt);
				$("#syInsurance").val(result.iqbResult.result.businessTaxAmt);
				$("#purchasePrice").val(result.iqbResult.result.taxAmt);
				$("#otherPrice").val(result.iqbResult.result.otherAmt);
				$('#employeeID').val(Formatter.ifNull(result.iqbResult.result.employeeID));
		        $('#employeeName').val(Formatter.ifNull(result.iqbResult.result.employeeName));
			})
		},
		showCarInfo:function(){
			//回显车商信息
			IQB.post(urls['cfm'] + '/dealer/getDealerInfo', {'orderId':window.procBizId}, function(result){
				var result = result.iqbResult.result;
				if(result != null){
					$('#customerChannels').val(result.custChannels);
					$('#customerName').val(result.custChannelsName);
					$('#carChannels').val(result.sourceCar);
					$('#carName').val(result.sourceCarName);
					$('#address').val(result.address);
					$('#phone').val(result.contactMethod);
					if(result.sourceCar == 1 || result.sourceCar == 2){
						$('.CTD').show();
						$('#marriedStatus').val(result.maritalStatus);
						$('#coName').val(result.contactName);
						$('#coPhone').val(result.contactMobile);
						$('#coAddress').val(result.contactAddr);
					}
				}
			})
		},
		showSubletInfo : function(){
			IQB.post(urls['cfm'] + '/sublet/get_sublet_record', {'orderId': window.procBizId}, function(result){
				if(result.iqbResult.result != null){
					var result = result.iqbResult.result;
					$('.noSublet').show();
					//赋值
					$('#sublet').val('1');
					$('#oldOrderId').val(result.subletOrderId);
					$('#oldRegId').val(result.subletRegId);
					$('#oldRealName').val(result.subletRealName);
					$('#planShortName').val(result.planShortName);
					$('#oldMonthInterest').val(result.monthInterest);
					$('#oldOrderItems').val(result.orderItems);
					$('#overItems').val(result.overItems);
					var rollOverFlag = result.rollOverFlag;
					if(rollOverFlag == 1){
						$('#rollOverFlag').val('1');
						$('.noRollOver').show();
						$('.noManageFee').hide();
						//赋值
						$('#rollOverItems').val(result.rollOverItems);
						var manageFeeFlag = result.manageFeeFlag;
						if(manageFeeFlag == 1){
							$('#manageFeeFlag').val('1');
							$('.noManageFee').show();
							$('#manageFee').val(result.manageFee);
						}
					}else{
						$('.noRollOver').hide();
						$('.noManageFee').hide();
					}
				}
			})
		},
		//是否有前期费用
		isPreAmt:function(){
			var val = $('#isPreAmt').val();
			if(val == 2){
				//无前期费用
				$('.isPreAmtShow').hide();
			}else{
				$('.isPreAmtShow').show();
			}
		},
		calPreFee:function(){
			var preOtherAmt = $('#preOtherAmt').val();
			var gpsAmt = $('#gpsAmt').val();
			var payJqInsurance = $('#payJqInsurance').val();
			var paySyInsurance = $('#paySyInsurance').val();
			var taxAmt = $('#taxAmt').val();
			var serverAmt = $('#serverAmt').val();
			var assessMsgAmt = $('#assessMsgAmt').val();
			var inspectionAmt = $('#inspectionAmt').val();
			var totalCost;//费用合计
			if(gpsAmt != '' && payJqInsurance != '' && paySyInsurance != '' && taxAmt != '' && serverAmt != ''){
				totalCost = Number(preOtherAmt) + Number(gpsAmt) + Number(payJqInsurance) + Number(paySyInsurance) + Number(taxAmt) + Number(serverAmt) + Number(assessMsgAmt) + Number(inspectionAmt); 
				$('#totalCost').val(totalCost);
				/*var online = $('#online').val();
				if(online == 1){
					//前期费用
					$('#preAmtAll').text(Formatter.money(Number(_this.cache.preAmount) + Number(totalCost)));
				}else{
					$('#preAmtAll').text(Formatter.money(_this.cache.preAmount));
				}*/
			}
			
		},
		showPreFee : function(){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': window.procBizId}, function(result){
				var result = result.iqbResult.result[0];
				_this.cache.result = result;
				if(result != '' && result != null){
					if(result.preAmtFlag == 1){
						$('#isPreAmt').val(1);
						$('#online').val(result.online);
						$('#totalCost').val(result.totalCost);
						$('#gpsAmt').val(result.gpsAmt);
						$('#payJqInsurance').val(result.riskAmt);
						$('#paySyInsurance').val(result.preBusinessTaxAmt);
						$('#taxAmt').val(result.taxAmt);
						$('#serverAmt').val(result.serverAmt);
						$('#assessMsgAmt').val(result.assessMsgAmt);
						$('#inspectionAmt').val(result.inspectionAmt);
						$('#preOtherAmt').val(result.otherAmt);
						//$('#preAmtAll').text(Formatter.money(_this.cache.preAmount));
					}else{
						$('#isPreAmt').val(2);
						$('.isPreAmtShow').hide();
					}
				}else{
					$('#isPreAmt').val(2);
					$('.isPreAmtShow').hide();
				}
			})
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			//回退到门店预处理节点信息回显
			_this.showText();
			_this.initCustChannels();
			_this.initSourceCarName();
			_this.showCarInfo();
			$('#carChannels').on('change',function(){_this.carChannels()});
			$('#customerName').on('change',function(){_this.customerName()});
			$('#customerChannels').on('change',function(){_this.customerChannels()});
			//$('#carName').on('blur',function(){_this.carName()});车商黑名单校验
			//转租相关
			$('#sublet').on('change', function(){_this.sublet()});
			$('#rollOverFlag').on('change', function(){_this.rollOverFlag()});
			$('#manageFeeFlag').on('change', function(){_this.manageFeeFlag()});
			$('#oldOrderId').on('change',function(){_this.subletInfo()});
			_this.showSubletInfo();
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			//是否有前期费用相关
			$('#isPreAmt').on('change',function(){_this.isPreAmt();});
			$('#online').on('change',function(){_this.calPreFee()});
			_this.showPreFee();
			
			$('#btn-uploadTypeTen').on('click', function(){$('#file').attr('fileType', 10);$('#file').click();});
			$('#btn-uploadTypeEleven').on('click', function(){$('#file').attr('fileType', 11);$('#file').click();});
			$('#btn-uploadTypeTwelve').on('click', function(){$('#file').attr('fileType', 12);$('#file').click();});
			$('#btn-uploadTypeThirteen').on('click', function(){$('#file').attr('fileType', 13);$('#file').click();});
			$('#btn-uploadTypeFourteen').on('click', function(){$('#file').attr('fileType', 14);$('#file').click();});
			$('#btn-uploadTypeFifteen').on('click', function(){$('#file').attr('fileType', 15);$('#file').click();});
			$('#btn-uploadTypeSixteen').on('click', function(){$('#file').attr('fileType', 16);$('#file').click();});
			$('#btn-uploadTypeSeventeen').on('click', function(){$('#file').attr('fileType', 17);$('#file').click();});
			$('#btn-uploadTypeEighteen').on('click', function(){$('#file').attr('fileType', 18);$('#file').click();});
			$('#btn-uploadTypeNineteen').on('click', function(){$('#file').attr('fileType', 19);$('#file').click();});
			$('#btn-uploadTypeTwenty').on('click', function(){$('#file').attr('fileType', 20);$('#file').click();});
			$('#btn-uploadTypeTwentyOne').on('click', function(){$('#file').attr('fileType', 21);$('#file').click();});
			$('#btn-uploadTypeTwentyTwo').on('click', function(){$('#file').attr('fileType', 22);$('#file').click();});
			$('#btn-uploadTypeTwentyThree').on('click', function(){$('#file').attr('fileType', 23);$('#file').click();});
			$('#btn-uploadTypeTwentyFour').on('click', function(){$('#file').attr('fileType', 24);$('#file').click();});
			$('#btn-uploadTypeTwentyFive').on('click', function(){$('#file').attr('fileType', 25);$('#file').click();});
			$('#btn-uploadTypeTwentySix').on('click', function(){$('#file').attr('fileType', 26);$('#file').click();});
			$('#btn-uploadTypeTwentySeven').on('click', function(){$('#file').attr('fileType', 27);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.preApproval.init();
});


