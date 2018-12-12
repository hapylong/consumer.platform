function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

//var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('orderId');
var caseId = getQueryString('caseId');
//var procInstId = getQueryString('procInstId');

$package('IQB.caseQueryDetail');
IQB.caseQueryDetail = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				//共用信息
				$('.merchantName').text(Formatter.isNull(result.merchantName));
				$('.realName').text(Formatter.isNull(result.realName));
				$('.regId').text(Formatter.isNull(result.regId));
				$('.orderId').text(Formatter.isNull(result.orderId));
				_this.cache.orderId = result.orderId;
				_this.cache.realName = result.realName;
				$('.orderAmt').text(Formatter.money(result.orderAmt));
				$('.monthInterest').text(Formatter.money(result.monthInterest));
				$('.orderName').text(Formatter.isNull(result.orderName));
				$('.plate').text(Formatter.isNull(result.plate));
				$('.carNo').text(Formatter.isNull(result.carNo));
				
				//贷后信息
				$('#lostContactFlag').text(Formatter.yOrn(result.lostContactFlag));
				$('#repaymentFlag').text(Formatter.yOrn(result.repaymentFlag));
				$('#disposeWay').text(Formatter.disposeWay(result.processMethod));
				$('#collectFlag').text(Formatter.yOrn(result.collectFlag));
				
				$('#intoGarageDate').text(Formatter.isNull(result.intoGarageDate));
				$('#intoGarageName').text(Formatter.isNull(result.intoGarageName));
				$('#trailerReason').text(Formatter.trailerReason(result.trailerReason));
				$('#carColor').text(Formatter.isNull(result.carColor));
				$('#key').text(_this.yOrn(result.carKeyFlag));
				$('#mileage').text(Formatter.isNull(result.mileage));
				
				$('#lostContactReason').text(Formatter.isNull(result.lostContactReason));//失联原因
				$('#checkOpinion').text(Formatter.isNull(result.checkOpinion));//核实意见
				$('#afterLoanOpinion').text(Formatter.isNull(result.afterLoanOpinion));//贷后意见
				$('#gpsArea').text(Formatter.isNull(result.gpsArea));//GPS信号区域
				$('#collectOpinion').text(Formatter.isNull(result.collectOpinion));//收车情况说明
				$('#collectOpinionConfirm').text(Formatter.isNull(result.collectOpinionConfirm));//收车情况确认
				
				
				//回款信息（外包）
				$('.repayNo').text(Formatter.isNull(result.hasRepayNo));
				$('.orderItems').text(Formatter.isNull(result.orderItems));
				$('#enNo').text(Formatter.isNull(result.engine));
				$('#collectionDate').text(Formatter.isNull(result.shouldReceivedDate));
				$('#collectionMoney').text(Formatter.moneyZero(result.shouldReceivedAmt));
				$('#commision').text(Formatter.moneyZero(result.commision));
				$('#towingFee').text(Formatter.moneyZero(result.trailerAmt));
				$('#parkingFee').text(Formatter.moneyZero(result.stopAmt));
				$('#otherFee').text(Formatter.moneyZero(result.otherAmt));
				$('#allAmt').text(Formatter.moneyZero(result.totalAmt));
				$('#remark').text(Formatter.isNull(result.receivedRemark));
				//到帐信息
				$('#arriveDate').text(Formatter.isNull(result.receivedDate));
				$('#arriveMoney').text(Formatter.moneyZero(result.receivedAmt));
				$('#surplusPrincipal').text(Formatter.moneyZero(result.remainPrincipal));
				$('#allCollection').text(Formatter.yOrn(result.receivedFlag));
			});
			//进度历史
			$("#datagrid3").datagrid2({url: urls['cfm'] + '/afterLoan/processList', 'paginator':'paginator3', queryParams : $.extend({}, {orderId: window.procBizId})	
			});
			//gps
			IQB.post(urls['cfm'] + '/afterLoan/getGpsInfoByOrderId', {orderId: window.procBizId}, function(result){
				var tableHtml = '';
				var result =result.iqbResult.result;
				for(i=0;i<result.length;i++){
					var j= i+1;
					tableHtml +="<tr><td>"+[j]+"</td><td>"+result[i].createTime+
						"</td><td>"+Formatter.gpsStatusShow(result[i].gpsStatus)+"</td><td>"
						+(result[i].remark)+"</td><td>"+(result[i].disposalScheme)+"</td></tr>";
				}
				$("#datagrid").append(tableHtml);
			});
		},
		yOrn: function(val){
			if(val != null){
				if(val == 1){
					return '是';
				}else if(val == 2){
					return '否';
				}
			}
			return '';
		},
		forBill : function(){
			IQB.get(urls['cfm'] + '/instRemindPhone/queryBillIfoByOId', {'orderId':window.procBizId}, function(result) {
				var result =result.iqbResult.result;
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win').modal('show');
					//赋值
					$("#billRealName").val(_this.cache.realName);
					$("#billOrderId").val(_this.cache.orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td><td>"+Formatter.isMobileCollection(result[i].mobileCollection)+"</td><td>"+Formatter.isDealReason(result[i].mobileDealOpinion)+"</td><td>"+Formatter.isNull(result[i].remark)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [101,35,36,102,103]}, function(result){
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
		showDocument : function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [104, 105, 106]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"></a>' +
						      			'<div class="caption">' +
						      			'<a url="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" onclick="IQB.caseExecution.showPdf('+n.imgPath+');">'+ n.imgName + '</a><a filePath="' + n.imgPath + '" onclick="IQB.caseExecution.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
			});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.showFile();
			_this.showDocument();
			_this.initApprovalTask();
			//账单详情
			$('#btn-check').on('click',function(){_this.forBill()});
		}
	}
	return _this;
}();

$(function(){
	IQB.caseQueryDetail.init();
});		
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};