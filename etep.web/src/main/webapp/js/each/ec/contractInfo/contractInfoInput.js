function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
var orderId = getQueryString('orderId');
var orgCode = getQueryString('orgCode');
$package('IQB.contractInfoInput');
IQB.contractInfoInput = function() {
	var _this = {
		cache :{
			data:{},
			merchCode : '',
			bizType : ''
		},
		config : {
			action : {
				saveContract : urls['rootUrl'] + '/contract/insertContractInfo',
				toBeContract : urls['rootUrl'] + '/contract/makeContract',
				signContract : urls['rootUrl'] + '/contract/submitContract',
				selContract : urls['rootUrl'] + '/contract/unIntcpt-selContracts',
				bizMerch : urls['rootUrl'] + '/contract/justMerchantType'
			}
		},
		bizMerch : function(){
			IQB.get(_this.config.action.bizMerch, {'orderId':orderId}, function(result) {
				merchCode = result.iqbResult.result.parentId;
				bizType=result.iqbResult.result.bizType;
				var contractInfo = result.iqbResult.result.contractInfo;
				if(contractInfo != null){//存在订单，允许回显（添加状态判断，正在签约，不允许操作）
					$("#updateForm")[0].reset();  
		    		$("#updateForm").form('load',result.iqbResult.result.contractInfo);
		    		if(contractInfo.status == 'waiting'||contractInfo.status == 'process'||contractInfo.status == 'complete'){
		    			$("#signContract").remove();
		    			$("#toBeContract").remove();
		    			$('input').attr('readonly',true);
		    			IQB.get(_this.config.action.selContract, {'bizId':orderId,'orgCode':orgCode,'bizType':bizType}, function(res) {
		    				var recode = res.iqbResult.result.recode
		    				if("1"==recode){
		    					var relist = res.iqbResult.result.ecList
		    					//合同列表
		    					var listHtml = '<div class="panel panel-info"><div class="panel-heading"><h4 class="panel-title">合同列表</h4></div><ul class="list-group contactList">'+
		    					'</ul></div>';
		    					$(".outBox").append(listHtml);
		    					var list = '';
		    					if(relist.length > 0){
		    						for(var i=0;i<relist.length;i++){
		    							if("zz" != relist[i].ecType){
		    								list += '<li class="list-group-item"><div class="row"><div class="col-md-1"></div><div class="col-md-8"><a class="text-stress" href="'+relist[i].ecViewUrl+'" target="_blank">'+relist[i].ecName+'</a></div></div></li>'
		    							}
		    						}
		    						$(".contactList").append(list);
		    					}
		    				}
		    			});
		    		}
		    		$('#id').val(contractInfo.id);
				}else{
					//回显车架号
					IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': orderId}, function(result){
						$("#carNo").val(result.iqbResult.AuthorityCardInfo.carNo);
					})
				}
				merchCode = merchCode.toString().substring(0,4);
				if(bizType == "2001"){//以租代购2001新车
					$('.car_lease').hide();
				}else if(bizType == "2002"){//2002二手车
					$('.car_lease').show();
				}
				$('#orderId').val(orderId);
				$('#orgCode').val(orgCode);
			})
		},
		saveContract : function(){
			if(bizType == "2002"){//以租代购2002二手车
				if($("#provider").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#vendor").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#vendorNo").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#seatNum").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#carType").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#fuelForm").val() == ''){
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
				if($("#carNo").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}
				if($("#carColor").val() == ''){
					IQB.alert('请完整填写合同所需信息！');
					return false;
				}else{
					$("#toBeContract").attr('disabled',true);
				}
			}else{
				$("#toBeContract").attr('disabled',true);
			}
			var orderId = $('#orderId').val();
			var provider = $("#provider").val();
			var vendor = $("#vendor").val();
			var vendorNo = $("#vendorNo").val();
			var seatNum = $("#seatNum").val();
			var carType = $("#carType").val();
			var fuelForm = $("#fuelForm").val();
			var fuelOilNumber = $("#fuelOilNumber").val();
			var engineType = $("#engineType").val();
			var carNo = $("#carNo").val();
			var carColor  = $("#carColor").val();
			var registrationNo  = $("#registrationNo").val();
			_this.cache.data = {
					'orderId':orderId,
					'provider' : provider,
					'vendor' : vendor,
					'vendorNo' : vendorNo,
					'seatNum' : seatNum,
					'carType' : carType,
					'fuelForm' : fuelForm,
					'fuelOilNumber' : fuelOilNumber,
					'engineType' : engineType,
					'carNo' : carNo,
					'carColor' : carColor,
					'registrationNo':registrationNo,
					'id':$('#id').val(),
					'orgCode':$('#orgCode').val()
			};
			IQB.get(_this.config.action.saveContract, _this.cache.data, function(result) {
				if(result.retCode == 0){
					$("#toBeContract").attr('disabled',true);
					_this.toBeContract();
				}else{
					$("#toBeContract").removeAttr('disabled');
					IQB.alert(result.iqbResult.msg);
				}
			})
		},
		toBeContract : function(){
			IQB.get(_this.config.action.toBeContract, _this.cache.data, function(result) {
				var retCode = result.iqbResult.result;
				if(retCode == '000000'){
					IQB.alert('合同生成成功！');
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
					$("#signContract").removeAttr('disabled');
					$("#toBeContract").attr('disabled',true);
					$('input').attr('readonly',true);
				}else{
					IQB.alert(result.iqbResult.msg);
				}
			})
		},
		signContract:function(){
			$("#signContract").attr('disabled',true);
			//签署合同
			IQB.get(_this.config.action.signContract, _this.cache.data, function(result) {
				var retCode = result.iqbResult.result;
				if(retCode == '000000'){
					IQB.alert('提交成功！');
					$("#signContract").attr('disabled',true);
					$("#toBeContract").attr('disabled',true);
					$('input').attr('readonly',true);
				}else{
					$("#signContract").removeAttr('disabled');
					IQB.alert(result.iqbResult.msg);
				}
			})
		},
		init : function() {
			_this.bizMerch();
			//生成合同
			$('#toBeContract').unbind('click').on('click',function(){
				_this.saveContract();
			});
			//立即签约
			$('#signContract').unbind('click').on('click',function(){
				_this.signContract();
			});
		}
	}
	return _this;
}();

$(function() {
	IQB.contractInfoInput.init();
});