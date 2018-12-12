$package('IQB.myTask');
IQB.myTask = function(){
	var _box = null;
	var _this = {
		formatterOfSkipPage: function(val, row, rowIndex){
			return '<button type="button" class="btn btn-link" onclick="IQB.myTask.skipPage(\'' + row.procBizId + '\',\'' + row.procInstId + '\',\'' + row.procDisplayurl + '\')">' + val + '</button>';
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		//取消流程
		cancelProcess : function() {
			var url = _this.config.action.cancelProcess;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if(records[0]['procTaskstatus']=="1") {
					var authData= {}
					authData.procAuthType = "2";
					var variableData={}
					var bizData={}
					bizData.procBizId=records[0]['procBizid'];
					bizData.procBizType =records[0]['procBiztype'];
					bizData.procOrgCode =records[0]['procOrgcode'];
					var procData={}
					procData.procInstId = records[0]['procInstId'];
					var option = {};
					option.authData=authData;
					option.variableData=variableData;
					option.bizData=bizData;
					option.procData=procData;
					IQB.getById(url, option, function(result) {
						if(result.success=="1") {
							IQB.alert('业务流程取消成功！');
							_box.handler.refresh();
						} else {
							IQB.alert(result.retUserInfo);
						}
					});
				} else {
					IQB.alert('流程任务已被签收或处理，不能取消流程.');
				}
			}
		},
		//撤回流程
		retrieveProcess : function() {
			var url = _this.config.action.retrieveProcess;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var authData= {}
				authData.procAuthType = "2";
				var variableData={}
				var bizData={}
				bizData.procBizId=records[0]['procBizId'];
				bizData.procBizType =records[0]['procBiztype'];
				bizData.procOrgCode =records[0]['procOrgcode'];
				var procData={}
				procData.procTaskId = records[0]['procTaskid'];
				var option = {};
				option.authData=authData;
				option.variableData=variableData;
				option.bizData=bizData;
				option.procData=procData;
				IQB.getById(url, option, function(result) {
					if(result.success=="1") {
						IQB.alert('业务流程任务撤回成功！');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}
		},
		config: {
			action: {//页面请求参数
				cancelProcess : urls['rootUrl'] + '/WfTask/cancelProcess',// 取消流程
				retrieveProcess : urls['rootUrl'] + '/WfTask/retrieveProcess',// 撤回流程
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/WfMonitor/userDoneTasks',
	   			singleCheck : true,
	   			isExport : true,
	   			cols : '2,5,8,9,10,11,12,13,14,15,16,17,18,19',
	   			queryParams: {
					procAuthType : '2'
				}
			},
		},
		init: function(){
			_box = new DataGrid2(_this.config);
			_box.init();
			$('#btn-cancel').click(function() {
				_this.cancelProcess()
			});// 流程实例暂停
			$('#btn-retrieve').click(function() {
				_this.retrieveProcess()
			});// 流程实例暂停
			$('#search-procStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择流程状态', allowClear: true}).val(null).trigger("change");		
		}
	}
	return _this;
}();


$(function(){
	IQB.myTask.init();
	datepicker(createDateStart);
	datepicker(createDateEnd);
	datepicker(finishedDateStart);
	datepicker(finishedDateEnd);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};
