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

$package('IQB.pledgeFinalJudgment');
IQB.pledgeFinalJudgment = function(){
	var _this = {
		cache: {			
			viewer: {},
			selectRZ:''
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
			if($('#selectRZ').val() == ''){
				IQB.alert('是否线下融资未选择');
				return false;
			}
			_this.saveFinalJudgmentInfo();
		},
		saveFinalJudgmentInfo: function(){
			/** 保存质押车车辆入库信息 **/
			var selectRZ = $('#selectRZ').val();
			_this.cache.selectRZ = selectRZ;
			var option = {
					'selectRZ' : selectRZ,
					'orderId' : window.procBizId
			}
			IQB.post(urls['cfm'] + '/pledge/saveVehicleStorageInfo', option, function(result){
				if(result.success == 1){
					if(_this.cache.selectRZ == 2){
						_this.saveCreditorName();
					}else{
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				}
			})
		},
		saveCreditorName: function(){
			var option = {
					'orderId':window.procBizId,
					'creditName':$('#creditName').val(),
					'creditCardNo':$('#creditCardNo').val(),
					'creditBankCard':$('#creditBankCard').val(),
					'creditBank':$('#creditBank').val(),
					'creditPhone':$('#creditPhone').val()
			}
			IQB.post(urls['cfm'] + '/customerStore/update_customer_info', option, function(result){
				if(result.iqbResult.result == 'success'){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}
			})
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		 // 拆分规则
		selectRZ: function(){
            var checkValue=$("#selectRZ").val();
            if(checkValue==2){
                $('.guaranteeInfo').show();
            }else{
                $('.guaranteeInfo').hide();
            }
        },
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/pledge/getPledgeInfo', {orderId: window.procBizId}, function(ret){
				result = ret.iqbResult.result;
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#plate').text(result.plate);
				$('#rfid').text(result.rfid);
				$('#groupName').text(Formatter.groupName(result.bizType));
				if(result.bizType == '2001'){
					$('.new-li').show();
					$('.old-li').hide();
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{
					$('.old-li').show();
					$('.new-li').hide();
					$('.old-tr').show();
					$('.new-tr').hide();
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
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				$('#riskRetRemark').text(result.riskRetRemark);	
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#ipiRemark').text((Formatter.ifNull(result.ipiRemark)));
				$('#isLoan').text((Formatter.isUseCreditLoan(result.isLoan)));
				$('#selectRZ').val(result.selectRZ);
				var checkValue=$("#selectRZ").val();
	            if(checkValue==2){
	                $('.guaranteeInfo').show();
	            }else{
	                $('.guaranteeInfo').hide();
	            }
			});
		},
		//选择债权人信息
		initGuaranteeCorporationNameSelect: function() {
			IQB.post(urls['cfm'] + '/customerStore/get_csi_by_oid', {'orderId': window.procBizId}, function(result){
				var arry = [];
				/*var obj = {};
				obj.id = '';
				obj.text = '请选择';
				arry.push(obj);*/
				$.each(result.iqbResult.result.icsi, function(i, n){
					var obj = {};
					obj.id = n.creditorName;
					obj.text = n.creditorName;
					arry.push(obj);
				});
				$('#creditName').select2({theme: 'bootstrap', data: arry});
				//缓存
				var guaranteeCorporationNameArr = result.iqbResult.result.icsi;
				_this.cache.guaranteeCorporationName = guaranteeCorporationNameArr;
				$('#creditName').val(Formatter.ifNull(guaranteeCorporationNameArr[0].creditorName));
				$('#creditCardNo').val(Formatter.ifNull(guaranteeCorporationNameArr[0].creditorIdNo));
				$('#creditBankCard').val(Formatter.ifNull(guaranteeCorporationNameArr[0].creditorBankNo));
				$('#creditBank').val(Formatter.ifNull(guaranteeCorporationNameArr[0].creditorBankName));
				$('#creditPhone').val(Formatter.ifNull(guaranteeCorporationNameArr[0].creditorPhone));
			});	
		},
		initGuaranteeCorporationNameIpt: function() {
			$('#creditName').change(function(e){
				var target = e.target;
				$.each(_this.cache.guaranteeCorporationName, function(i, n){
					if($(target).val() == n.creditorName){
						$('#creditCardNo').val(n.creditorIdNo);
						$('#creditBankCard').val(n.creditorBankNo);
						$('#creditBank').val(n.creditorBankName);
						$('#creditPhone').val(n.creditorPhone);
					}
				});
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [0,2,3,4,5,6,7,8,9,10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,28]}, function(result){
				$('.projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('.guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('.guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
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
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
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
		},
		init: function(){ 				
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.showText();
			_this.initGuaranteeCorporationNameSelect();
			_this.initGuaranteeCorporationNameIpt();
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			$('#selectRZ').on('click', function(){_this.selectRZ()});
		}
	}
	return _this;
}();

$(function(){
	IQB.pledgeFinalJudgment.init();
});		