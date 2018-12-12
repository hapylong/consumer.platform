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

$package('IQB.fwQueryDetail');
IQB.fwQueryDetail = function(){
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
			});
			IQB.post(urls['cfm'] + '/afterLoan/getCaseInfoByOrderId', {'orderId': window.procBizId,'caseId':window.caseId}, function(result){
				//表格回显
				var backInfo = result.iqbResult.receivedPaymentList;
				var backInfoHtml = '';
				if(backInfo != '' && backInfo != null){
					for(var i=0;i<backInfo.length;i++){
						backInfoHtml += "<tr>"+
						"<td><span class='text-muted'>"+Formatter.isNull(backInfo[i].receivedPaymentDate)+"</span></td>"+
						"<td><span class='text-muted'>"+Formatter.moneyZero(backInfo[i].receivedPayment)+"</span></td>"+
						"</tr>"
					}
				}
				$('#backTable').append(backInfoHtml);
				var result = result.iqbResult.result;
	
				$('#repayNo').text(Formatter.isNull(result.hasRepayNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				$('#register').text(Formatter.register(result.register));
				if(result.register == '1'){
					$('.registerAbout').hide();
				}else{
					$('.registerAbout').show();
				}
				$('#associatedAgency').text(Formatter.isNull(result.associatedAgency));
				$('#mandatoryLawyer').text(Formatter.isNull(result.mandatoryLawyer));
				$('#acceptingInstitution').text(Formatter.isNull(result.acceptOrg));
				$('#legalFare').text(Formatter.moneyZero(result.legalCost));
				$('#counselFee').text(Formatter.moneyZero(result.barFee));
				$('#filingTime').text(Formatter.isNull(result.registerDate));
				$('#caseNo').text(Formatter.isNull(result.caseNo));
				$('#filingRemark').text(result.registerRemark);
				
				//庭前调解信息回显
				$('#mediateFlag').text(Formatter.yOrn(result.composeFlag));
				$('#reconciliationFlag').text(Formatter.yOrn(result.compromiseFlag));
				$('#filingRemark').text(Formatter.isNull(result.registerRemark));
				$('#adjustRemark').text(Formatter.isNull(result.composeRemark));
				if(result.composeFlag != '1'){
					$('.reconciliationAbout').hide();
				}else{
					$('.reconciliationAbout').show();
				}
				if(result.compromiseFlag != '1'){
					$('.addOrDelete').hide();
				}else{
					$('.addOrDelete').show();
				}
				
				//到帐信息
				$('#arriveDate').text(Formatter.isNull(result.receivedDate));
				$('#arriveMoney').text(Formatter.moneyZero(result.receivedAmt));
			})
			//进度历史
			$("#datagrid4").datagrid2({url: urls['cfm'] + '/afterLoan/selectInstOrderLawResultByCaseId', 'paginator':'paginator4', queryParams : $.extend({}, {'caseId':window.caseId})	
			});
			$("#datagrid5").datagrid2({url: urls['cfm'] + '/afterLoan/selectInstOrderCaseExecuteByCaseId', 'paginator':'paginator5', queryParams : $.extend({}, {'caseId':window.caseId})	
			});
			IQB.post(urls['cfm'] + '/afterLoan/getInstOrderLawnInfoByCaseId', {"orderId":window.procBizId,'caseId':window.caseId}, function(result){
				  if(result.success == '1'){
					    var result = result.iqbResult.result;
					    $('#cause').val(Formatter.isNull(result.reason));
						$('#accuser').text(Formatter.accuserFlag(result.accuserFlag));
						$('#claims').val(Formatter.isNull(result.claimRequest));
						$('#createTime').text(result.createTime);
						$('#record').text(Formatter.yOrn(result.registerFlag));
				  }
			})
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
		showDocument : function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.caseId, imgType: [104, 105, 106]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"></a>' +
						      			'<div class="caption">' +
						      			'<a href="javascript:;" target="_blank" path="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" orderId="' + n.orderId + '" imgType="'+ n.imgType +'" onclick="IQB.fwQueryDetail.previewFile(event);">'+ n.imgName + '</a><a filePath="' + n.imgPath + '" alt="' + n.imgName + '" orderId="' + n.orderId + '" imgType="'+ n.imgType +'" onclick="IQB.fwQueryDetail.downLoadFile(event);" style="margin-left:8px;color:#7d7d7d;font-weight:bold;cursor:pointer">'+ '下载' + '</a>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
			});
		},
		previewFile : function(event){
			var tarent = event.currentTarget;
			var option = {
				'imgName': $(tarent).attr('alt'),
				'orderId': $(tarent).attr('orderId'),
				'imgType':$(tarent).attr('imgType')
			};
			var docType = option.imgName.split('.');
			if(docType[1] == 'pdf'){
				var url = $(tarent).attr('path');
				//window.parent.IQB.main2.openTab("case-pdf", "查看预览", '/etep.web/view/afterLoan/caseExecution/pdfShow.html?url=' + encodeURI(url), true, true, null);
				$(tarent).attr('href',url);
			}else{
				IQB.post(urls['cfm'] + '/image/convertDocToHtml', option, function(result){
					window.open(urls['baseUrl'] + result.iqbResult.result);
				});
			}
			
		},
		downLoadFile : function(){
			var tarent = event.currentTarget;
			var imgName = $(tarent).attr('alt');
			var orderId = $(tarent).attr('orderId');
			var imgType = $(tarent).attr('imgType');
			var datas = "?imgName=" + imgName + "&orderId=" + orderId + "&imgType=" + imgType;
			var urlOpen = encodeURI(urls['cfm'] + '/image/downLoadImage' + datas);
			$(tarent).attr("href",urlOpen);
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.showDocument();
			_this.initApprovalTask();
			//账单详情
			$('#btn-check').on('click',function(){_this.forBill()});
		}
	}
	return _this;
}();

$(function(){
	IQB.fwQueryDetail.init();
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