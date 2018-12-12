function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('procBizid');//流程ID==caseId
var procInstId = getQueryString('procInstId');

$package('IQB.registerCaseMediate');
IQB.registerCaseMediate = function(){
	var _this = {
		cache: {			
			viewer: {},
			i: 1
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
				variableData.composeFlag=_this.cache.composeFlag;
				variableData.compromiseFlag=_this.cache.compromiseFlag;
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.composeFlag=_this.cache.composeFlag;
				bizData.compromiseFlag=_this.cache.compromiseFlag;
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
			if($('#updateForm2').form('validate')){
				var trLength = $("#backTable").find('tr').length-1;
	        	var list = [];
				for (var i = 0; i<trLength; i++) {         
				list.push({});
				}
				for (var i = 1; i<list.length+1; i++) {
				list[i-1].receivedPaymentDate = backTable.rows[i].cells[1].getElementsByTagName("INPUT")[0].value;
				list[i-1].receivedPayment = backTable.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;
				}
				var option = {
						"orderId": _this.cache.orderId,
						"caseId":window.procBizId,
						'receivedPaymentList':list,
						'composeFlag': $('#mediateFlag').val(),
						'compromiseFlag': $('#reconciliationFlag').val(),
						'composeRemark':$('#adjustRemark').val()
				}
				if($('#mediateFlag').val() != '1'){
					option.compromiseFlag = '';
					option.receivedPaymentList = '';
				}
				if($('#reconciliationFlag').val() != '1'){
					option.receivedPaymentList = '';
				}
				_this.cache.composeFlag = option.composeFlag;
				_this.cache.compromiseFlag = option.compromiseFlag;
				//保存
				IQB.post(urls['cfm'] + '/afterLoan/updateRegisterCase', option, function(result){
					if(result.success == '1'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getCaseInfoByOrderId', {'caseId': window.procBizId}, function(result){
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
				
				//
				$('#repayNo').text(Formatter.isNull(result.hasRepayNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				$('#enNo').text(Formatter.isNull(result.engine));
				
				//诉讼信息回显
				$('#register').text(Formatter.register(result.register));
				if(result.register == '1'){
					$('.registerAbout').hide();
				}else{
					$('.registerAbout').show();
				}
				$('#associatedAgency').text(Formatter.isNull(result.associatedAgency));
				$('#mandatoryLawyer').text(Formatter.isNull(result.mandatoryLawyer));
				$('#acceptingInstitution').text(Formatter.isNull(result.acceptOrg));
				$('#legalFare').text(Formatter.money(result.legalCost));
				$('#counselFee').text(Formatter.money(result.barFee));
				$('#filingTime').text(Formatter.isNull(result.registerDate));
				$('#caseNo').text(Formatter.isNull(result.caseNo));
				$('#filingRemark').text(Formatter.isNull(result.registerRemark));
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
		addTr : function(){
			IQB.confirm('确定要增加一行吗？',function(){
				_this.cache.i = _this.cache.i + 1;
				var trrHtml =   "<tr>"+	
				                    "<td><input type='checkbox' id='checkbox"+_this.cache.i+"' name='checkbox'></td>"+
							    	"<td><input type='text' id='backTime"+_this.cache.i+"' name='backTime"+_this.cache.i+"' class='form-control input-sm easyui-validatebox addInfo' required='required'></td>"+
							    	"<td><input type='text' id='backMoney"+_this.cache.i+"' name='backMoney"+_this.cache.i+"' class='form-control input-sm easyui-validatebox addInfo' required='required'></td>"+		
					    		"</tr>"
				$('#backTable').append(trrHtml);
				//时间插件
				$('.addInfo').validatebox({ required: true })
				datepicker($('#backTime'+_this.cache.i));
			},null);
		},
		deleteTr : function(){
			if($("input[name='checkbox']:checked").length > 0){
				IQB.confirm('确定要删除本行吗？',function(){
					$("input[name='checkbox']:checked").each(function() {
	                    n= $(this).parents("tr").index();
	                    m=n+1;
	                    $("#backTable").find("tr:eq("+n+")").remove();
	                });
				},null);
			}else{
				IQB.alert('未选中行');
			}
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.initApprovalTask();
			_this.initApprovalHistory();
			datepicker(backTime1);
			//担保人信息增加一行
			$('#btn-addTr').click(function(){_this.addTr()});
			//担保人信息删除一行
			$('#btn-deleteTr').click(function(){_this.deleteTr()});
			//是否庭前调解
			$('.reconciliationAbout').find('input').validatebox({ required: false }); 
			$('.reconciliationAbout').find('select').validatebox({ required: false });
			$('#mediateFlag').on('change',function(){
		    	if($(this).val() == '1'){
		    		$('.reconciliationAbout').show();
		    		$('.reconciliationAbout').find('input').validatebox({ required: true }); 
					$('.reconciliationAbout').find('select').validatebox({ required: true });
		    	}else{
		    		$('.reconciliationAbout').hide();
		    		$('.reconciliationAbout').find('input').validatebox({ required: false }); 
					$('.reconciliationAbout').find('select').validatebox({ required: false });
		    	}
		    });
			//是否和解
		    $('#reconciliationFlag').on('change',function(){
		    	if($(this).val() == '1'){
		    		$('.addOrDelete').show();
		    		$('.addOrDelete').find('input').validatebox({ required: true }); 
					$('.addOrDelete').find('select').validatebox({ required: true });
		    	}else{
		    		$('.addOrDelete').hide();
		    		$('.addOrDelete').find('input').validatebox({ required: false }); 
					$('.addOrDelete').find('select').validatebox({ required: false });
		    	}
		    });
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function(){
	IQB.registerCaseMediate.init();
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