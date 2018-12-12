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

/*procBizId = '20160530-625226';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/
$package('IQB.contractInfoView');
IQB.contractInfoView = function() {
	var _this = {
		cache :{
			data:{},
			merchCode : '',
			resultInfo : ''
		},
		config : {
			action : {
				showInfo : urls['rootUrl'] + '/contract/selContractInfo',
				bizMerch : urls['rootUrl'] + '/contract/justMerchantType'
			}
		},
		bizMerch : function(){
			//显示信息
			IQB.get(_this.config.action.showInfo, {'orderId':window.procBizId}, function(result) {
				_this.cache.resultInfo = result.iqbResult.contractInfo;
				var result = result.iqbResult.contractInfo;
				//赋值
				$("#supply").html(result.provider);
				$("#manufacturer").html(result.vendor);
				$("#factoryPlateModel").html(result.vendorNo);
				$("#seat").html(result.seatNum);
				$("#motorcycleType").html(result.carType);
				$("#fuelOilType").html(result.fuelForm);
				$("#fuelOilNumber").html(result.fuelNo);
				$("#engineType").html(result.engine);
				$("#VIN").html(result.carNo);
				$("#color").html(result.carColor);
			})
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
					$("#accountName").html(_this.cache.resultInfo.openAcct);
					$("#accountBank").html(_this.cache.resultInfo.openBankName);
					$("#bankNumber").html(_this.cache.resultInfo.openBankNo);
				}else if(_this.cache.merchCode == 1006){
					//轮动及其下属商户时
					$('.panelDisplay').show();
					$(".FeeDisplay").show();
					//
					$("#assessFee").html(Formatter.money(_this.cache.resultInfo.evalManageFee));
					$("#inspectFee").html(Formatter.money(_this.cache.resultInfo.kaochaFee));
					$("#enterFee").html(Formatter.money(_this.cache.resultInfo.intoFee));
					$("#other").html(Formatter.money(_this.cache.resultInfo.otherFee));
				}else if(_this.cache.merchCode == 1020){
					//惠淘车
					$('.panelDisplay').show();
					$('.lessorFeeDisplay').show();
					$("#lessorFee").html(Formatter.money(_this.cache.resultInfo.lessorManageFee));
				}
			})
		},
		init : function() {
			_this.bizMerch();
		}
	}
	return _this;
}();

$(function() {
	IQB.contractInfoView.init();
});