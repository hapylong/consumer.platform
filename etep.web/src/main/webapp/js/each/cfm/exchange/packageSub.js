function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
var orderId = getQueryString('orderId');
var id=getQueryString('id');
$package('IQB.packageSub');
IQB.packageSub = function(){
	var _grid = null;
	var _tree = null;
	var _this = {		
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				queryDetail : urls['cfm'] + '/business/getJysOrderInfo',
				initSelect :urls['cfm'] + '/getGuaranteeInfo',
				savePackInfo : urls['cfm'] + '/savePackInfo'
  			},
			event: {//按钮绑定函数，不定义的话采用默认

			},
  			dataGrid: {//表格参数  				
  				
			}
		},
		initSelect: function(){//初始化担保机构下拉框
			IQB.post(_this.config.action.initSelect, {'guaranteeInstitution':''}, function(result) {
				$('#guaranteeInstitution').select2({theme: 'bootstrap', data: result.iqbResult.result}).on('change', function(){
				});		
			})
		},
		queryDetail : function(){
			IQB.get(_this.config.action.queryDetail, {'orderId':window.orderId,'id':window.id}, function(result){
				//赋值
				$(".orderId").html(result.iqbResult.result.orderId);
				$(".orderId").val(result.iqbResult.result.orderId);
				var bizType = result.iqbResult.result.bizType;
				if(bizType == '2001'){
					$(".bizType").html('以租代售新车');
				}else if(bizType == '2002'){
					$(".bizType").html('以租代售二手车');
				}else if(bizType == '2100'){
					$(".bizType").html('抵押车');
				}else if(bizType == '2200'){
					$(".bizType").html('质押车');
				}else if(bizType == '1100'){
					$(".bizType").html('易安家');
				}else if(bizType == '1000'){
					$(".bizType").html('医美');
				}else if(bizType == '1200'){
					$(".bizType").html('旅游');
				}else if(bizType == '8002'){
					$(".bizType").html('过桥垫资');
				}else if(bizType == '8003'){
					$(".bizType").html('房屋质押一抵');
				}else if(bizType == '8004'){
					$(".bizType").html('房屋质押二抵');
				}else if(bizType == '3002'){
					$(".bizType").html('周转贷');
				}else if(bizType == '4001'){
					$(".bizType").html('融资租赁');
				}
				$("#packAmt").val(result.iqbResult.result.orderAmt);
				$(".merchantNo").html(result.iqbResult.result.merchantNo);
				$(".userName").html(result.iqbResult.result.realName);
				$(".regId").html(result.iqbResult.result.regId);
				$(".orderName").html(result.iqbResult.result.orderName);
				$(".orderAmt").html(result.iqbResult.result.orderAmt);
				$(".orderItems").html(result.iqbResult.result.orderItems);
				$(".orderItems").val(result.iqbResult.result.orderItems);
				$(".fee").html(result.iqbResult.result.fee);
				$(".fee").val(result.iqbResult.result.fee);
				$(".monthInterest").html(result.iqbResult.result.monthInterest);
				$(".expireDate").html(Formatter.timeCfm2(result.iqbResult.result.expireDate));
				$(".expireDate").val(Formatter.timeCfm2(result.iqbResult.result.expireDate));
				$(".createTime").html(Formatter.timeCfm2(result.iqbResult.result.createTime));
				$(".planId").html(result.iqbResult.result.planId);
				$(".planShortName").html(result.iqbResult.result.planShortName);
				
				$("#creditorName").val(result.iqbResult.result.creditName);
				$("#creditorPhone").val(result.iqbResult.result.creditPhone);
				$("#creditorIdNo").val(result.iqbResult.result.creditCardNo);
				$("#creditorBankName").val(result.iqbResult.result.creditBank);
				$("#creditorBankNo").val(result.iqbResult.result.creditBankCard);
			})
		},
		submit : function(){
			$('#btn-submit').attr('disabled',true);
			var beginInterestDate = $("#beginInterestDate").val();
			var raiseInstitutions = $("#raiseInstitutions").val();
			var expireDate = $("#expireDate").val();
			var guaranteeName= $("#guaranteeInstitution").val();//id
			var guaranteeInstitution = $('#select2-guaranteeInstitution-container').html();//text
			var orderItems = $("#orderItems").val();
			var fee = $("#fee").val();
			var packNum = $("#packNum").val();
			var packAmt = $("#packAmt").val();
			var remark = $("#remark").val();
			if(raiseInstitutions == '' || guaranteeInstitution == ''){
				IQB.alert('信息不完善，无法提交！');
				$('#btn-submit').removeAttr('disabled');
				return false;
			}
			var option = {
					'orderId': window.orderId,
					'id':window.id,
					'beginInterestDate' : beginInterestDate,
					'raiseInstitutions' : raiseInstitutions,
					'guaranteeName' : guaranteeName,
					'guaranteeInstitution' : guaranteeInstitution,
					'packNum':packNum,
					'packAmt':packAmt,
					'remark':remark
			};
			IQB.postIfFail(_this.config.action.savePackInfo, option, function(result) {
				if(result.iqbResult.success=="1") {
					var url = window.location.pathname;
					var param = window.parent.IQB.main2.fetchCache(url);
					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
					var callback2 = '';
					//var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + true + ',' + null + ')';
					window.parent.IQB.main2.call(callback, callback2);
				} else if(result.iqbResult.success=="-2"){
					IQB.alert("该订单对应的资产已打包");
					$('#btn-submit').removeAttr('disabled');
				} else {
					IQB.alert(result.retUserInfo);
					$('#btn-submit').removeAttr('disabled');
				}
			});
		},
		init: function(){ 
			_this.queryDetail();
			_this.initSelect();
			$("#btn-submit").unbind("click").click(function(){
				_this.submit();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.packageSub.init();
	datepicker(beginInterestDate);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};
