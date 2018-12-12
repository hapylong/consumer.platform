function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('procBizId');
var procInstId = getQueryString('procInstId');

/*procBizId = '20170103-267322';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.orderView');
IQB.orderView = function(){
	var _this = {
		cache: {
			viewer: {}
		},	
		viewerShow: function(event){
			$('#img').click();
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
					$('.assessPrice-div').hide();
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
					$('.old-tr').show();
					$('.new-tr').hide();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				//处理支付结果
				if(result.chargeWay == 1){
					$('.isPay-table').show();
					$('.isPay-div').hide();	
					/*$('.outOrderId-div').hide();	*/
				}else{
					$('.isPay-table').hide();
					$('.isPay-div').show();	
					$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
					//处理流水号
//					if(result.preAmtStatus == 1){
//						$('.outOrderId-div').show();	
//					}else{
//						$('.outOrderId-div').hide();	
//					}
				}
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
			});
			IQB.post(urls['cfm'] + '/creditorInfo/getCreditorInfo', {orderId: window.procBizId}, function(result){
				if(result.iqbResult.result){
					$('#creditName').text(Formatter.ifNull(result.iqbResult.result.creditName));
					$('#creditCardNo').text(Formatter.ifNull(result.iqbResult.result.creditCardNo));
					$('#creditBankCard').text(Formatter.ifNull(result.iqbResult.result.creditBankCard));
					$('#creditBank').text(Formatter.ifNull(result.iqbResult.result.creditBank));
					$('#creditPhone').text(Formatter.ifNull(result.iqbResult.result.creditPhone));
				}				
			});
			/*IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId}, function(result){
				if(result){
					$('#outOrderId').text(Formatter.ifNull(result.outOrderId));				
				}				
			});*/
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]}, function(result){
				$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				var is = false;
				var is2 = false;
				var is3 = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						if(n.imgType != 5){
							var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
							      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;"></a>' +
							      			'<div class="caption">' +
							      				'<h5>' + n.imgName + '</h5>' +
							      			'</div>' + 
							      		'</div>';
							$('#td-' + n.imgType).append(html);
							if(n.imgType < 10){
								is = true;
							}
							if(n.imgType >= 10){
								is3 = true;
							}							
						}else{
							$('#printScreen').append('<a onclick="IQB.orderView.viewerShow(event);">' + n.imgName +'</a>');
							$('#img').prop('src', urls['imgUrl'] + n.imgPath);
							$('#img').prop('alt', n.imgName);
							is2 = true;
						}						
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
				if(is2){
					_this.cache.viewer.img = new Viewer(document.getElementById('img'), {});
				}
				if(is3){
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
				}
			});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
		}
	}
	return _this;
}();

$(function(){
	IQB.orderView.init();
});		