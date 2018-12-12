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

$package('IQB.gpsConfirm');
IQB.gpsConfirm = function(){
	var _this = {
		cache: {			
			viewer: {}
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
				IQB.post(urls['rootUrl'] + '/workFlow/saveWfInfo', {orderId: window.procBizId, receiveAmt: $('#receiveAmt').val(), remark: $.trim($('#remark').val())}, function(){
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
		openApproveWin: function(){
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
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
				$('#collectOpinion').text(Formatter.isNull(result.collectOpinion));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				// $('#collectFlag').text(Formatter.yOrn(result.collectFlag));
				$('#intoGarageDate').text(Formatter.isNull(result.intoGarageDate));
				$('#intoGarageName').text(Formatter.isNull(result.intoGarageName));
				$('#trailerReason').text(Formatter.trailerReason(result.trailerReason));
				$('#lostContactFlag').text(Formatter.yOrn(result.lostContactFlag));
				$('#repaymentFlag').text(Formatter.yOrn(result.repaymentFlag));
				$('#checkOpinion').text(Formatter.isNull(result.checkOpinion));
				$('#afterLoanOpinion').text(Formatter.isNull(result.afterLoanOpinion));
				$('#collectOpinion').text(Formatter.isNull(result.collectOpinion));
				$('#gpsArea').text(Formatter.isNull(result.gpsArea));
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
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [35,36]}, function(result){
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
				}
			});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.showFile();
			_this.initApprovalTask();
			_this.initApprovalHistory();
			//账单详情
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});	
		}
	}
	return _this;
}();

$(function(){
	IQB.gpsConfirm.init();
});		