$package('IQB.wfMonitor');
IQB.wfMonitor = function() {
	var _box = null;
	var _this = {
		formatterOfSkipPage: function(val, row, rowIndex){
			return '<button type="button" class="btn btn-link" onclick="IQB.wfMonitor.skipPage(\'' + row.procBizId + '\',\'' + row.procInstId + '\',\'' + row.procDisplayurl + '\')">' + val + '</button>';
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		// 流程挂起
		suspend : function() {
			var url = _this.config.action.suspend;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var authData = {}
				authData.procAuthType = "2";
				
				var variableData = {}
				
				var procData={}
				procData.procInstId = records[0]['procInstId'];
				
				var option = {};
				option.variableData=variableData;
				option.authData=authData;
				option.procData=procData;
				
				IQB.post(url, option, function(result){
					if(result.success=="1") {
						IQB.alert('流程实例暂停处理成功！');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}
		},
		// 流程激活
		active : function() {
			var url = _this.config.action.active;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var authData = {}
				authData.procAuthType = "2";
				
				var variableData = {}
				
				var procData={}
				procData.procInstId = records[0]['procInstId'];
				
				var option = {};
				option.variableData=variableData;
				option.authData=authData;
				option.procData=procData;
				
				IQB.post(url, option, function(result){
					if(result.success=="1") {
						IQB.alert('流程实例激活成功！');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}
		},
		// 流程终止
		endProcess : function() {
			if ($('#stopForm').form('validate')) {
				var url = _this.config.action.endProcess;
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

		// 打开处理人选择窗口
		assignOpen : function() {
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				$('#assign-win').modal('show');
			}
		},

		// 保存流程任务处理人
		assignSave : function() {
			var procAssignee = $('#btn-ProcAssignee').val();
			if(procAssignee){
				var records = _box.util.getCheckedRows();
				var authData= {}
				authData.procAuthType = "2";
				var variableData={}
				variableData.procAssignee = procAssignee;
				var procData={}
				procData.procTaskId = records[0]['procTaskid'];
				var option = {};
				option.variableData=variableData;
				option.authData=authData;
				option.procData=procData;
				
				IQB.post(_this.config.action.assign, option, function(result){
					if(result.success=="1") {
						IQB.alert('指定业务流程任务处理人成功！');
						$('#assign-win').modal('hide');
						$('#assignForm').form('reset');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}else{
				IQB.alert('指定的业务流程任务处理人不能为空');
			}
		},
		// 关闭处理人选择窗口
		assignClose : function() {
			$('#assign-win').modal('hide');

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
		initSelectProcAssignee: function(){//初始化处理人下拉框
			IQB.post(_this.config.action.getSelectProcAssignee, {}, function(result) {
				$('#btn-ProcAssignee').select2({theme: 'bootstrap', data: result.iqbResult.result}).on('change', function(){
				});		
			})
		},
		config : {
			action : {
				assign : urls['rootUrl'] + '/WfTask/assign',// 指定处理人
				suspend : urls['rootUrl'] + '/WfMonitor/suspend',// 挂起
				active : urls['rootUrl'] + '/WfMonitor/active',// 激活
				endProcess : urls['rootUrl'] + '/WfTask/endProcess',// 终止
				getSelectProcAssignee : urls['rootUrl'] + '/SysUserRest/getSysUserAll'//获取当前机构用户下拉框 

			},
			dataGrid : {
				url : urls['rootUrl'] + '/WfMonitor/activeProcessList',
				singleCheck : true,
	   			isExport : true,
	   			cols : '2,5,8,9,10,11,12,13,14,15,16,17',
				queryParams: {
					procAuthType : '2'
				}
			}
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			_this.initSelectProcAssignee();//调用用户下拉初始化
			$('#btn-suspend').click(function() {
				_this.suspend()
			});// 流程实例暂停
			$('#btn-active').click(function() {
				_this.active()
			});// 流程实例激活
			$('#btn-assign').click(function() {
				_this.assignOpen();
			});
			$('#btn-assign-close').click(function() {
				_this.assignClose();
			});
			$('#btn-assign-save').click(function() {
				_this.assignSave();
			});
			$('#btn-stop').click(function() {
				_this.stopOpen();
			});
			$('#btn-stop-close').click(function() {
				_this.stopClose();
			});
			$('#btn-stop-save').click(function() {
				_this.endProcess();
			});
			$('#btn-text').click(function() {
				IQB.confirm('保存成功',function(){alert("确定");},function(){alert("取消");});
				
			});
		}
	}
	return _this;
}();

$(function() {
	IQB.wfMonitor.init();
	datepicker(createDateStart);
	datepicker(createDateEnd);
	datepicker(commitDateStart);
	datepicker(commitDateEnd);
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