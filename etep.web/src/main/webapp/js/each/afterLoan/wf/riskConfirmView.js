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

$package('IQB.riskConfirmView');
IQB.riskConfirmView = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				$('#orderId').text(Formatter.isNull(result.orderId));
				_this.cache.orderId = result.orderId;
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				$('#lostContactFlag').text(Formatter.yOrn(result.lostContactFlag));
				$('#repaymentFlag').text(Formatter.yOrn(result.repaymentFlag));
				$('#lostContactReason').text(Formatter.isNull(result.lostContactReason));
				$('#afterLoanOpinion').text(Formatter.isNull(result.afterLoanOpinion));
				$('#riskRemark').text(Formatter.isNull(result.riskRemark));
			});
			IQB.post(urls['cfm'] + '/afterLoan/getGpsInfoByOrderId', {orderId: window.procBizId}, function(result){
				var tableHtml = '';
				var result =result.iqbResult.result;
				for(i=0;i<result.length;i++){
					var j= i+1;
					tableHtml +="<tr><td>"+[j]+"</td><td>"+result[i].createTime+
						"</td><td>"+Formatter.gpsStatusShow(result[i].gpsStatus)+"</td><td>"
						+(result[i].remark)+"</td><td>"+(result[i].disposalScheme)+"</td></tr>";
				}
				$("#datagrid2").append(tableHtml);
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
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});


			_this.initApprovalTask();
			_this.initApprovalHistory();
			//账单详情
		}
	}
	return _this;
}();

$(function(){
	IQB.riskConfirmView.init();
});		