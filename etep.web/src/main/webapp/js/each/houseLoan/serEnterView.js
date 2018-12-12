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

/*procBizId = 'CDHTC3001170517007';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.serEnterView');
IQB.serEnterView = function(){
	var _this = {
		cache: {
			viewer: {},
			i : 1
		},
		initApprovalTask: function(){//初始化订单详情
			//基本信息
			IQB.post(urls['cfm'] + '/dandelion/get_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchName').text(Formatter.ifNull(result.merchantName));
				$('#bizType').text(Formatter.bizType(result.bizType));
				$('#realName').text(Formatter.ifNull(result.realName));
				$('#regId').text(Formatter.ifNull(result.regId));
				$('#idCard').text(Formatter.ifNull(result.idCard));
				$('#bankMobile').text(Formatter.ifNull(result.bankMobile));
			});
			//紧急联系人信息
			IQB.post(urls['cfm'] + '/dandelion/get_risk', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#address').text(Formatter.ifNull(result.address));
				$('#firmAddress').text(Formatter.ifNull(result.companyAddress));
				$('#firmName').text(Formatter.ifNull(result.companyName));
				$('#firmPhone').text(Formatter.ifNull(result.companyPhone));
				$('#creditNumber').text(Formatter.ifNull(result.creditNo));
				$('#creditPass').text(Formatter.ifNull(result.creditPasswd));
				$('#creditCode').text(Formatter.ifNull(result.creditCode));
				$('#operatorPass').text(Formatter.ifNull(result.optPasswd));
				$('#creditScore').text(Formatter.ifNull(result.zmCreditScore));
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1,2,3,4,101,102]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName +
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.serEnterView.init();
});


