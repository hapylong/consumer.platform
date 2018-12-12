$package('IQB.waitHandleForHome');
IQB.waitHandleForHome = function() {
	var _box = null;
	var _list = null;
	var _this = {
		//签收
		assign : function() {
			var url = _this.config.action.assign;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if(records[0]['procTaskStatus']=="1") {
					var authData= {}
					authData.procAuthType = "2";
					var variableData={}
					var bizData={}
					bizData.procBizId=records[0]['procBizid'];
					bizData.procBizType =records[0]['procBiztype'];
					bizData.procOrgCode =records[0]['procOrgcode'];
					var procData={}
					procData.procTaskId = records[0]['procCtaskId'];
					var option = {};
					option.authData=authData;
					option.variableData=variableData;
					option.bizData=bizData;
					option.procData=procData;
					IQB.getById(url, option, function(result) {
						if(result.success=="1") {
							IQB.alert('业务流程任务签收成功！');
							_box.handler.refresh();
						} else {
							IQB.alert(result.retUserInfo);
						}
					});
				} else {
					IQB.alert('请选择流程任务状态为【待签收】的流程任务');
				}
			}
		},
		//取消签收
		unassign : function() {
			var url = _this.config.action.unassign;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if(records[0]['procTaskStatus']=="2") {
					var authData= {}
					authData.procAuthType = "2";
					var variableData={}
					var bizData={}
					bizData.procBizId=records[0]['procBizid'];
					bizData.procBizType=records[0]['procBiztype'];
					bizData.procOrgCode=records[0]['procOrgcode'];
					var procData={}
					procData.procTaskId = records[0]['procCtaskId'];
					var option = {};
					option.authData=authData;
					option.variableData=variableData;
					option.bizData=bizData;
					option.procData=procData;
					IQB.getById(url, option, function(result) {
						if(result.success=="1") {
							IQB.alert('业务流程任务取消签收成功！');
							_box.handler.refresh();
						} else {
							IQB.alert(result.retUserInfo);
						}
					});
				} else {
					IQB.alert('请选择流程任务状态为【待处理】的流程任务');
				}
			}
		},
		//流程审批页面跳转
		approve : function() {
			var url = _this.config.action.assign;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if(records[0]['procTaskStatus']=="1") {
					var authData= {}
					authData.procAuthType = "2";
					var variableData={}
					var bizData={}
					bizData.procBizId=records[0]['procBizid'];
					bizData.procBizType=records[0]['procBiztype'];
					bizData.procOrgCode=records[0]['procOrgcode'];
					var procData={}
					procData.procTaskId = records[0]['procCtaskId'];
					var option = {};
					option.authData=authData;
					option.variableData=variableData;
					option.bizData=bizData;
					option.procData=procData;
					IQB.getById(url, option, function(result) {
						if(result.success=="1"){
							var url = window.location.pathname;
							var param = window.parent.IQB.main2.fetchCache(url);
							window.parent.IQB.main2.openTab("activiti-approve", "流程审批", records[0]['procApproveurl']+'?procTaskId='+records[0]['procCtaskId']+"&procBizid="+records[0]['procBizid']+"&procInstId="+records[0]['procInstId'], true, true, {lastTab: param});
						}
					});
				}else if(records[0]['procTaskStatus']=="2"){
					var url = window.location.pathname;
					var param = window.parent.IQB.main2.fetchCache(url);
					window.parent.IQB.main2.openTab("activiti-approve", "流程审批", records[0]['procApproveurl']+'?procTaskId='+records[0]['procCtaskId']+"&procBizid="+records[0]['procBizid']+"&procInstId="+records[0]['procInstId'], true, true, {lastTab: param});
				}else{
					IQB.alert('请选择流程任务状态为【待签收】或【待处理】的流程任务');
				}
			}
		},		
		delegateOpen : function() {
			var url = _this.config.action.unassign;
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if(records[0]['procTaskStatus'] == "1"){
					$('#btn-ProcAssignee').val(null).trigger('change');
					$('#delegate-win').modal('show');
				}else if(records[0]['procTaskStatus'] == "2"){
					IQB.alert('只有流程任务状态为【待签收】的流程才能被委托');
				}else{
					IQB.alert('请选择将要委托的流程任务');
				}
			}
		},
		delegateSave : function() {
			var procAssignee = $('#btn-ProcAssignee').val();
			if(procAssignee){
				var records = _box.util.getCheckedRows();
				var authData= {}
				authData.procAuthType = "2";
				var variableData={}
				variableData.procAssignee = procAssignee;
				var procData={}
				procData.procTaskId = records[0]['procCtaskId'];
				var option = {};
				option.variableData=variableData;
				option.authData=authData;
				option.procData=procData;
				
				IQB.post(_this.config.action.delegate, option, function(result){
					if(result.success=="1") {
						IQB.alert('业务流程任务委托成功！');
						$('#delegate-win').modal('hide');
						_box.handler.refresh();
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}else{
				IQB.alert('委托人不能为空');
			}
			
		},
		delegateClose : function() {
			$('#delegate-win').modal('hide');
		},
		
		initSelectProcAssignee: function(){//初始化委托处理人下拉框
			IQB.post(_this.config.action.getSelectProcAssignee, {}, function(result) {
				$('#btn-ProcAssignee').select2({theme: 'bootstrap', data: result.iqbResult.result}).val(null).trigger('change');
			})
		},
		config : {
			action : {
				assign : urls['rootUrl'] + '/WfTask/claimProcess',// 签收
				unassign : urls['rootUrl'] + '/WfTask/unclaimProcess',// 取消签收
				delegate : urls['rootUrl'] + '/WfTask/delegateProcess',// 委托
				approve : urls['rootUrl'] + '/WfTask/completeProcess',// 审批
				getSelectProcAssignee : urls['rootUrl'] + '/SysUserRest/getSysUserOrgan'//获取当前机构用户下拉框 
			},
			dataGrid : {
				url : urls['rootUrl'] + '/WfMonitor/userToDoTasks',
				singleCheck : true,
				queryParams: {
					procAuthType : '2'
				}
			}
		},
		
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			_this.initSelectProcAssignee();//调用用户下拉初始化
			$('#btn-assign').click(function() {
				_this.assign()
			});
			$('#btn-unassign').click(function() {
				_this.unassign()
			});
			$('#btn-delegate').click(function() {
				_this.delegateOpen()
			});
			$('#btn-delegate-close').click(function() {
				_this.delegateClose()
			});
			$('#btn-delegate-save').click(function() {
				_this.delegateSave()
			});
			$('#btn-handle').click(function() {
				_this.approve()
			});
			$('#btn-ref').click(function() {
				$('#btn-search').click();
			});
		}
	}
	return _this;
}();

$(function() {
	IQB.waitHandleForHome.init();
});