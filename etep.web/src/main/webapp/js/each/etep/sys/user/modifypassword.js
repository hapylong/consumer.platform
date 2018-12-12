$package('IQB.modifypassword');
IQB.modifypassword = function() {
	var _box = null;
	var _this = {
	    clopseUpdatePwdWin: function(){
	    	
	    },
		editPwd: function(){		
	          if($('#editPwdForm').form('validate')){
				IQB.get(_this.config.action.editPwd, $('#editPwdForm').serializeObject(), function(result) {
					if ($.isSucc(result)) {
						alert("密码修改成功");
						$('#editPwdForm').form('reset');
					}
				});			
	          }
		},
		config : {
			action : {
				editPwd : urls['rootUrl'] + '/SysUserRest/updateModifyUserPassword'
			},
			event : {

			},
			dataGrid : {
				
			}
		},
		init : function() {
			_box = new DataGrid(_this.config);
			
			$('#btn-editPwd').click(_this.editPwd);
		
		}
	}
	return _this;
}();

$(function() {
	IQB.modifypassword.init();
});
