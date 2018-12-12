$package('IQB.wfProcessStop');
IQB.wfProcessStop = function() {
	var _box = null;
	var _this = {
		formatterOfSkipPage: function(val, row, rowIndex){
			return '<button type="button" class="btn btn-link" onclick="IQB.wfProcessStop.skipPage(\'' + row.procBizId + '\',\'' + row.procInstId + '\',\'' + row.procDisplayurl + '\')">' + val + '</button>';
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
			
		stop : function() {
			if ($('#stopForm').form('validate')) {
				var url = _this.config.action.stop;
				var records = _box.util.getCheckedRows();
				var procSpecialDesc = $('#stopForm').find('[name="procSpecialDesc"]').val();
				if (_box.util.checkSelectOne(records)) {
					var authData = {}
					authData.procAuthType = "2";
					
					var variableData = {}
					variableData.procSpecialDesc = procSpecialDesc;
					
					var procData={}
					procData.procInstId = records[0]['procInstId'];
					
					var bizData = {}
					bizData.procBizId = records[0]['procBizId'];
					bizData.procBizType = records[0]['procBiztype'];
					bizData.procOrgCode = records[0]['procOrgcode'];
					
					var option = {};
					option.variableData=variableData;
					option.authData=authData;
					option.procData=procData;
					option.bizData = bizData;
					
					IQB.post(url, option, function(result){
						if(result.success=="1") {
							IQB.alert('流程终止处理成功！');
							$('#stop-win').modal('hide');
							$('#stopForm').form('reset');
							_box.handler.refresh();
						} else {
							IQB.alert(result.retUserInfo);
						}
					});
				}
			}
		},
		// 打开终止流程原因输入窗口
		stopOpen : function() {
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				$('#stop-win').modal('show');
			}
		},
		// 关闭终止流程原因输入窗口
		stopClose : function() {
			$('#stop-win').modal('hide');

		},
		config : {
			action : {
				stop : urls['rootUrl'] + '/WfTask/endProcess',// 流程终止
			},
			dataGrid : {
				url : urls['rootUrl'] + '/WfMonitor/orgProcessList',
				singleCheck : true,
				queryParams: {
					procAuthType : '2'
				}
			}
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			$('#btn-stop').click(function() {
				_this.stopOpen();
			});
			$('#btn-stop-close').click(function() {
				_this.stopClose();
			});
			$('#btn-stop-save').click(function() {
				_this.stop();
			});
			$('#search-procStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择流程状态', allowClear: true}).val(null).trigger("change");		
		}
	}
	return _this;
}();

$(function() {
	IQB.wfProcessStop.init();
});