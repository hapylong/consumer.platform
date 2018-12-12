$package('IQB.wfProcDelegateCancel');
IQB.wfProcDelegateCancel = function(){
	var _box = null;
	var _this = {
		formatterOfSkipPage: function(val, row, rowIndex){
			return '<button type="button" class="btn btn-link" onclick="IQB.wfProcDelegateCancel.skipPage(\'' + row.procBizId + '\',\'' + row.procInstId + '\',\'' + row.procDisplayurl + '\')">' + val + '</button>';
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		//取消流程委托
		cancelDelegate : function() {
			var url = _this.config.action.cancelDelegate;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var authData= {}
				authData.procAuthType = "2";
				var variableData={}
				var procData={}
				procData.procInstId = records[0]['procInstId'];
				var option = {};
				option.authData=authData;
				option.variableData=variableData;
				option.procData=procData;
				IQB.getById(url, option, function(result) {
					if(result.success=="1") {
						IQB.alert('流程委托取消成功！');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}
		},
		config: {
			action: {//页面请求参数
				cancelDelegate : urls['rootUrl'] + '/WfTask/cancelProcDelegate',// 取消流程委托
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/WfMonitor/myProcDelegate',
	   			singleCheck : true,
	   			queryParams: {
					procAuthType : '2'
				}
			},
		},
		init: function(){
			_box = new DataGrid2(_this.config);
			_box.init();
			$('#btn-cancelDelegate').click(function() {
				_this.cancelDelegate()
			});// 取消流程委托
		}
	}
	return _this;
}();

$(function() {
	IQB.wfProcDelegateCancel.init();
});