function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('procBizId');
var procTaskId = getQueryString('procTaskId');
var procInstId = getQueryString('procInstId');

$package('IQB.processApprova');
IQB.processApprova = function() {
	var _this = {
		cache:{
			viewer: {}
		},
		config:{
			getById: urls['rootUrl'] + '/unIntcpt-penaltyDerate/getPenaltyDerateByUUId',
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
				variableData.procAuthType="2";
				variableData.procApprStatus = approveForm.approveStatus;
				variableData.procApprOpinion = approveForm.approveOpinion;
				variableData.derateType=$('#derateType option:selected').val();
				var cutAmt = $('#cutAmt').val();
				if(isNaN(cutAmt)){
					variableData.cutAmt=0;
				}else{
					variableData.cutAmt=cutAmt;
				}
				
				var bizData = {}
				bizData.procBizId=procBizId;
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
		openApproveWin: function() {
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		initFrom : function(){
			var option = {};
			option['uuid'] = window.procBizId;
			IQB.post(_this.config.getById, option, function(result){
				$("#updateForm")[0].reset();  
	    		$("#updateForm").form('load',result.iqbResult.result);
	    		$("input[name='monthInterest']").val(parseFloat(result.iqbResult.result.monthInterest).toFixed(2));
	    		var onlinePayFailImg = result.iqbResult.result.onlinePayFailImg;
	    		var debitBankWaterImg = result.iqbResult.result.debitBankWaterImg;
	    		var otherImg = result.iqbResult.result.otherImg;
	    		if(isNaN(onlinePayFailImg)&&""!=onlinePayFailImg&&"undefined"!=onlinePayFailImg){
	    			_this.imgShow("onlinePayFailImg",onlinePayFailImg);
	    		}
	    		if(isNaN(debitBankWaterImg)&&""!=debitBankWaterImg&&"undefined"!=debitBankWaterImg){
	    			_this.imgShow("debitBankWaterImg",debitBankWaterImg);
	    		}
	    		if(isNaN(otherImg)&&""!=otherImg&&"undefined"!=otherImg){
	    			_this.imgShow("otherImg",otherImg);
	    		}
			});
		},
		imgShow : function(key,value){
			if(value.indexOf(',') >= 0){
				value = value.substring(1,value.length-1).split(',');
				var html = '';
				$.each(value, function(i, n){
					n = n.substring(1,n.length-1);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
	      			'<a href="javascript:void(0)"><img src="' + urls.imgUrl+n + '" style="width: 135px; height: 135px;"></a>' +
	      		    '</div>';
				})
				$('#td-'+ key).append(html);
				if(_this.cache.viewer.viewerOne){
					_this.cache.viewer.viewerOne.update();
				}else{
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			}else{
				var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
      			'<a href="javascript:void(0)"><img src="' + urls.imgUrl+value + '" style="width: 135px; height: 135px;"></a>' +
      		    '</div>';
                $('#td-'+ key).append(html);
                if(_this.cache.viewer.viewerOne){
					_this.cache.viewer.viewerOne.update();
				}else{
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			}
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
		init : function() {
			_this.initFrom();
			_this.initApprovalHistory();
			$("input").attr("readonly","readonly");
	    	$("select").attr("readonly","readonly");
	    	$('#derateType').attr('disabled',true);
	    	$("textarea").attr("readonly","readonly");
	    	
	    	$("#approveOpinion").attr("readonly",false);
	    	$("#approveStatus").attr("readonly",false);
	    	
	    	$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
		}
	}
	return _this;
}();

$(function() {
	IQB.processApprova.init();
});
