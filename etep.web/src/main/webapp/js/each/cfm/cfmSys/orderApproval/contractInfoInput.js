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

/*procBizId = 'CDHTC2001170323001';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/
$package('IQB.contractInfoInput');
IQB.contractInfoInput = function() {
	var _this = {
		cache :{
			data:{},
			merchCode : ''
		},
		config : {
			action : {
				saveContract : urls['rootUrl'] + '/contract/insertContractInfo',
				toBeContract : urls['rootUrl'] + '/contract/makeContract',
				signContract : urls['rootUrl'] + '/contract/submitContract',
				WfTask : urls['rootUrl'] + '/WfTask/completeProcess',
				bizMerch : urls['rootUrl'] + '/contract/justMerchantType'
			}
		},
		bizMerch : function(){
			//页面内容显示与隐藏
			$('.panelDisplay').hide();
			$(".FeeDisplay").hide();
			$('.lessorFeeDisplay').hide();
			$('.panelDisplay1').hide();
			IQB.get(_this.config.action.bizMerch, {'orderId':window.procBizId}, function(result) {
				merchCode = result.iqbResult.parentId;
				_this.cache.merchCode = merchCode.substring(0,4);
				if(_this.cache.merchCode == 1021){
					//医美
					$('.panelDisplay1').show();
				}else if(_this.cache.merchCode == 1006){
					//轮动及其下属商户时
					$('.panelDisplay').show();
					$(".FeeDisplay").show();
				}else if(_this.cache.merchCode == 1020){
					//惠淘车
					$('.panelDisplay').show();
					$('.lessorFeeDisplay').show();
				}
			})
			//回显车架号
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
				$("#VIN").val(result.iqbResult.AuthorityCardInfo.carNo);
			})
		},
		saveContract : function(){
			//验证
			if($("#supply").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#manufacturer").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#factoryPlateModel").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#seat").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#motorcycleType").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#fuelOilType").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#engineType").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#fuelOilNumber").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#VIN").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			if($("#color").val() == ''){
				IQB.alert('请完整填写合同所需信息！');
				return false;
			}
			var orderId = window.procBizId ;
			var provider = $("#supply").val();
			var vendor = $("#manufacturer").val();
			var vendorNo = $("#factoryPlateModel").val();
			var seatNum = $("#seat").val();
			var carType = $("#motorcycleType").val();
			var fuelForm = $("#fuelOilType").val();
			var fuelNo = $("#fuelOilNumber").val();
			var engine = $("#engineType").val();
			var carNo = $("#VIN").val();
			var carColor = $("#color").val();
			var otherFee = '';
			var evalManageFee = '';
			var kaochaFee = '';
			var intoFee = '';
			var lessorManageFee = '';
			var openAcct = '';
			var openBankName = '';
			var openBankNo = '';
			//轮动
			if(_this.cache.merchCode == 1006){
				if($("#assessFee").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#inspectFee").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#enterFee").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				evalManageFee = $("#assessFee").val();
				kaochaFee = $("#inspectFee").val();
				intoFee = $("#enterFee").val();
				otherFee = $("#other").val();
			//惠淘车
			}else if(_this.cache.merchCode == 1020){
				if($("#lessorFee").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				lessorManageFee = $("#lessorFee").val();
			//医美	
			}else if(_this.cache.merchCode == 1021){
				if($("#accountName").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#accountName").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#bankNumber").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				openAcct = $("#accountName").val();
				openBankName = $("#accountBank").val();
				openBankNo = $("#bankNumber").val();
			}
				_this.cache.data = {
						'orderId':orderId,
						'provider' : provider,
						'vendor' : vendor,
						'vendorNo' : vendorNo,
						'seatNum' : seatNum,
						'carType' : carType,
						'fuelForm' : fuelForm,
						'fuelNo' : fuelNo,
						'engine' : engine,
						'carNo' : carNo,
						'carColor' : carColor,
						'evalManageFee' : evalManageFee,
						'kaochaFee' : kaochaFee,
						'intoFee' : intoFee,
						'otherFee' : otherFee,
						'lessorManageFee' : lessorManageFee,
						'openAcct' : openAcct,
						'openBankName' : openBankName,
						'openBankNo' : openBankNo
				};
				IQB.get(_this.config.action.saveContract, _this.cache.data, function(result) {
					if(result.retCode == 0){
						IQB.alert('保存成功！');
						$("#toBeContract").removeAttr('disabled');
						$('input').attr('readonly',true);
						$("#saveContract").attr('disabled',true);
					}else{
						IQB.alert(result.iqbResult.msg);
					}
				})
		},
		toBeContract : function(){
			IQB.get(_this.config.action.toBeContract, _this.cache.data, function(result) {
				if(result.retCode == 0){
					IQB.alert('合同生成成功！');
					$("#signContract").removeAttr('disabled');
					$("#toBeContract").attr('disabled',true);
					$("#btn-alertWin-confirm").click(function(){
						if(result.iqbResult.returnResult != null){
							result = result.iqbResult.returnResult;
							//合同列表
							var listHtml = '<div class="panel panel-info"><div class="panel-heading"><h4 class="panel-title">合同列表</h4></div><ul class="list-group contactList">'+
			     			'</ul></div>';
							$(".outBox").append(listHtml);
							var list = '';
							if(result.length > 0){
								for(var i=0;i<result.length;i++){
									list += '<li class="list-group-item"><div class="row"><div class="col-md-1"></div><div class="col-md-8"><a class="text-stress" href="'+result[i].ecUrl+'" target="_blank">'+result[i].ecName+'</a></div></div></li>'
								}
					     		$(".contactList").append(list);
							}
						}
					});
				}else{
					IQB.alert(result.iqbResult.msg);
				}
			})
		},
		signContract : function(){
			//签署合同
			IQB.get(_this.config.action.signContract, _this.cache.data, function(result) {
				if(result.retCode == 0){
					
					//工作流参数
					var approveForm= {};
					approveForm.approveStatus = '1';
					approveForm.approveOpinion = '同意';
					var authData= {}
					authData.procAuthType = "2";
					var variableData = {}
					variableData.procApprStatus = approveForm.approveStatus;
					variableData.procApprOpinion = approveForm.approveOpinion;
					var bizData = {}
					bizData.procBizId=window.procBizId;
					var procData = {}
					procData.procTaskId = window.procTaskId;
					var option = {};
					option.authData=authData;
					option.variableData = variableData;
					option.bizData = bizData;
					option.procData = procData;
					
					IQB.post(urls['rootUrl'] + '/WfTask/completeProcess', option, function(result){
						if(result.success!="1"){
							$('#approve-win').modal('hide');
						}
					});
				}else{
					IQB.alert(result.iqbResult.msg);
				}
			})
		},
		init : function() {
			_this.bizMerch();
            //保存合同
			$('#saveContract').click(function() {
				_this.saveContract();
			});
			//生成合同
			$('#toBeContract').click(function() {
				_this.toBeContract();
			});
			//提交
			$('#signContract').click(function() {
				_this.signContract();
			});	
		}
	}
	return _this;
}();

$(function() {
	IQB.contractInfoInput.init();
});