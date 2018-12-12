$package('IQB.user');
IQB.user = function() {
	var _box = null;
	var _this = {
		cache : {
		},
		extFunc:{
	    	initStationRoleName: function(){
	    		if(_this.cache.stationNameArr == undefined){
					IQB.postAsync(urls['rootUrl'] + '/hqSysStationRoleRest/getAllStationInfoForSelect', {}, function(result){
						_this.cache.stationNameArr = result.iqbResult.result;
					})
				}
	    		$('#query-stationCode').prepend("<option value=''>请选择</option>");
	    		$('#query-stationCode').select2({theme : 'bootstrap',data : _this.cache.stationNameArr,placeholder: "请选择"});
	    	}
	    },
		openUpdatePwdWin : function() {
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				$('#update-pwd-win').modal('show');
				var passwordId = records[0].id;
				$('#pwdId').val(passwordId);
			}
		},
		openUpdateRoleWin : function() {
			$('#roleUpdate').empty();
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				if (records[0].status == 1) {
					$('#role-update-win').modal('show');
					var stationCode = records[0].stationCode;
					var arr = stationCode;
					if(stationCode.indexOf('[') >= 0){
						arr = eval(stationCode);
					}
					var userCodeIpt = records[0].userCode;
					var orgIdv = records[0].orgId;
					var isHq =records[0].isHq;
				    var data = {
			    		orgId : orgIdv,
		    			isHq : isHq
					};
					$('#userCode').val(userCodeIpt);
					IQB.get(_this.config.action.getRoleInfoByOrgIdAndHq, data, function(result) {
						$('#roleUpdate').select2({theme : 'bootstrap',data : result.iqbResult.result,maximumSelectionLength: 6,placeholder: "请选择"});
						$('#roleUpdate').val([arr]);
						$('#roleUpdate').change();
					})
				} else {
					IQB.alert("对不起，您目前状态冻结不能指定角色");
				}
			}
		},
		initOrgSelect: function(){//初始化机构下拉
			IQB.post(_this.config.action.getOrgInfo, {}, function(result) {
				_this.cache.result = result.iqbResult.result;
				$('#orgIdSelectAdd').select2({theme: 'bootstrap', data: result.iqbResult.result}).on('change', function(){
					_this.initDeptSelect($('#orgIdSelectAdd').val());//调用部门下拉初始化
				});		
			})
		},
		initDeptSelect: function(orgId){// 初始化部门下拉		
			IQB.get(_this.config.action.selectDeptId, {orgId: orgId}, function(result){
				$('#deptIdSelectAdd').empty().select2({
					theme: 'bootstrap',
					data: result.iqbResult.result
				});
				if(_this.cache.deptId){
					$('#deptIdSelectAdd').val(_this.cache.deptId).trigger('change');
					_this.cache.deptId = null;
				}
			})
		},
		closeUpdateRoleWin : function() {
			$('#role-update-win').modal('hide');
		},
		saveUpdateRoleWin : function() {
			$("#roleFrom").attr("action", _this.config.action.saveRoleInfo);
			IQB.save($("#roleFrom"), function(result) {
				_this.closeUpdateRoleWin();
				_box.handler.refresh();
			});
		},
		closeUpdatePwdWin : function() {
			$('#update-pwd-win').modal('hide');
		},
		parseUserStationName : function(val) {
			var ret = '';
			if(val == undefined || val == ''){
				return ret;
			}
			var arr = val;
			if(val.indexOf('[') >= 0){
				arr = eval(val);
			}else{
				arr = [arr];
			}
			if(_this.cache.stationNameArr == undefined){
				IQB.postAsync(urls['rootUrl'] + '/hqSysStationRoleRest/getAllStationInfoForSelect', {}, function(result){
					var customerTypeArr = result.iqbResult.result;
					_this.cache.stationNameArr = result.iqbResult.result;
				})
			}
			$.each(_this.cache.stationNameArr, function(key, retVal) {
				if($.inArray(retVal.id, arr)>=0){
					if(ret != ''){
						ret = ret + '，';
					}
					ret = ret + retVal.text;
				}
			});
			return ret;
		},
		saveUpdatePwdWin : function() {
			var userPassword = $('#userPassword').val();
			var realUserPassword = $('#realUserPassword').val();
			if (userPassword == realUserPassword) {
				$('#btn-pwd-save').click(_this.saveUpdatePwdWin);
				$('#updatePwdForm').attr('action',
						_this.config.action.updatePwd);
				IQB.save($('#updatePwdForm'), function(result) {
					$('#update-pwd-win').modal('hide');
					$('#updatePwdForm').form('reset');
					_box.handler.refresh();
				});
			}
		},
		config : {
			action : {
				insert : urls['rootUrl'] + '/SysUserRest/insertSysUser',
				update : urls['rootUrl'] + '/SysUserRest/updateSysUserAll',
				getById : urls['rootUrl'] + '/SysUserRest/getSysUserById',
				remove : urls['rootUrl'] + '/SysUserRest/deleteSysUser',
				updatePwd : urls['rootUrl'] + '/SysUserRest/updateUserPassword',
				getRoleInfo : urls['rootUrl']+ '/sysStationRoleRest/getUserEnableRole',
				getRoleInfoByOrgIdAndHq : urls['rootUrl']+ '/hqSysStationRoleRest/getRoleInfoByOrgIdAndHq',
				saveRoleInfo : urls['rootUrl']+ '/hqSysStationRoleRest/saveUserHqRoleInfo',
				getOrgInfo : urls['rootUrl']+ '/sysOrganizationRest/getAllOrgInfo',
				selectDeptId : urls['rootUrl']+ '/sysOrganizationRest/selectOrganizationDept',
				},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#query-stationCode').val(null).trigger('change');
				},
				insert: function(){// 重写insert
					_this.cache.deptId = null;
					_box.handler.insert();
					$('#update-win-label').text('添加用户');					
					$('#orgIdSelectAdd').val(_this.cache.result[0].id).trigger('change');
					$('#update-status').val(1).trigger('change');
				},
				update: function(){//重写update
				 	_box.handler.update(function(result){
				 		$('#update-win-label').text('修改用户');
				 		  $('#update-status').val(result.iqbResult.result.status).trigger('change');
				 		_box.form.update.form('load', result.iqbResult.result);
				 		_this.cache.deptId = result.iqbResult.result.deptId;	
				 		$('#orgIdSelectAdd').val(result.iqbResult.result.orgId).trigger('change');				 							
						_box.form.update.prop('action', _this.config.action.update);
						_box.win.update.modal({backdrop: 'static', keyboard: false,show: true});
					});
				}
			},
			dataGrid: {
				url: urls['rootUrl'] + '/SysUserRest/getSysUser',
				singleCheck: true
			}
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			this.initSelect();
			_this.initOrgSelect();//调用机构下拉初始化
            $('#update-status').select2({minimumResultsForSearch : 'Infinity',theme : 'bootstrap'});
			$('#btn-pwd-update').click(_this.openUpdatePwdWin);
			$('#btn-role-update').click(_this.openUpdateRoleWin);
			$('#btn-role-close').click(_this.closeUpdateRoleWin);
			$('#btn-role-save').click(_this.saveUpdateRoleWin);
			$('#btn-pwd-close').click(_this.closeUpdatePwdWin);
			$('#btn-pwd-save').click(_this.saveUpdatePwdWin);
			$('#orgIdSelectAdd').change(_this.orgIdSelectChange);
			$('#btn-update').click(_this.deptIdSelectAdd);

		},
		initSelect: function(){
			_this.extFunc.initStationRoleName();	
		}
	}
	return _this;
}();

$(function() {
	IQB.user.init();
});

$.getStatusType = function(val) {
	if (val == '1') {

		return '<span class="label label-primary">正常</span>';
	} else {
		return '<span class="label label-default">冻结</span>';
	}
	return '';
}

