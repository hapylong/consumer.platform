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

$package('IQB.carCheck');
IQB.carCheck = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		approve: function() {
			$('#btn-approve-save').attr('disabled','disabled');
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
				console.log($('#repaymentFlag').val())
				variableData.isRepayment = $('#repaymentFlag').val();
				variableData.isLost = $('#lostContactFlag').val();
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.isRepayment = $('#repaymentFlag').val();
				bizData.isLost = $('#lostContactFlag').val();
				bizData.processMethod = $('#disposeWay').val();
				var procData = {}
				procData.procTaskId = procTaskId;
				var option = {};
				option.authData=authData;
				option.variableData = variableData;
				option.bizData = bizData;
				option.procData = procData;
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
						$('#btn-approve-save').removeAttr('disabled');
					}
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
				if($("#repaymentFlag").val()==""){
					IQB.alert("请选是否还款");
					  return false;
				}
				if($('#repaymentFlag').val()!='1'){
					if($("#lostContactFlag").val()==""){
						IQB.alert("请选择车辆是否失联");
						return false;
					}
					if($("#lostContactFlag").val()=="1" && ($("#lostContactReason").val()=="" || $('#disposeWay').val() == '')){
						IQB.alert("请输入失联原因和处理途径");
						return false;
					}
				}
				var option = {
						'orderId':_this.cache.orderId,
						'lostContactFlag':$('#lostContactFlag').val(),
						'processMethod':$('#disposeWay').val(),
						'lostContactReason':$('#lostContactReason').val(),
						'repaymentFlag':$('#repaymentFlag').val(),
						'checkOpinion':$('#checkOpinion').val()
					}
				IQB.post(urls['cfm'] + '/afterLoan/updateAfterLoanInfo', option, function(result){
					if(result.success == '1'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
						$('#btn-approve-save').removeAttr('disabled');
					}
				})		
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
				_this.cache.realName = result.realName;
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				
				$('#afterLoanOpinion').text(Formatter.isNull(result.afterLoanOpinion));
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
		forBill : function(){
			IQB.get(urls['cfm'] + '/instRemindPhone/queryBillIfoByOId', {'orderId':_this.cache.orderId}, function(result) {
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
		rule : function(){
			$("#repaymentFlag").change(function(){
				if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()==''){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()=='1'){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()=='2'){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()==''){
					$(".sugBox").show();
					$(".reasonBox").show();
					$(".missBox").show();
				}else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()=='1'){
					$(".sugBox").hide();
					$(".reasonBox").show();
					$(".missBox").show();
				}
				else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()=='2'){
					$(".sugBox").show();
					$(".reasonBox").hide();
					$(".missBox").show();
				}
			})
			$("#lostContactFlag").change(function(){
				if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()==''){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()=='1'){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#lostContactFlag').val()=='2'){
					$(".sugBox").hide();
					$(".reasonBox").hide();
					$(".missBox").hide();
				}else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()==''){
					$(".sugBox").show();
					$(".reasonBox").show();
					$(".missBox").show();
				}else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()=='1'){
					$(".sugBox").hide();
					$(".reasonBox").show();
					$(".missBox").show();
					$(".disposeAbout").show();
				}
				else if($('#repaymentFlag').val()!='1'&&$('#lostContactFlag').val()=='2'){
					$(".sugBox").show();
					$(".reasonBox").hide();
					$(".missBox").show();
					$(".disposeAbout").hide();
				}
			})
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});


			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.rule();
			//账单详情
			$('#btn-check').on('click',function(){_this.forBill()});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function(){
	IQB.carCheck.init();
});		