$package('IQB.forgetpassword');
IQB.forgetpassword = function() {
	var _box = null;
	var _this = {

	/*	openMessageWin : function() {
			$("#update-win").modal('show');
		},*/

		closeMessPwd : function() {

			$("#update-win").modal('hide');
		},

		editPwd : function() {
			var option = $('#forgetPwdForm').serializeObject();
			 if($('#forgetPwdForm').form('validate')){
			IQB.get(_this.config.action.editPwd, option, function(result) {
				if ($.isSucc(result)) {
					alert("邮件已发送");
					$('#forgetPwdForm').form('reset');
				} else {
					$("#update-win").modal('show');
					//alert("aaaa");
					//_this.openMessageWin();
				}
			});
			 }

		},
		config : {
			action : {
				editPwd : urls['rootUrl']
						+ '/SysUserRest/updateForgetUserPassword'
			},
			event : {

			},
			dataGrid : {

			}
		},
		init : function() {
			_box = new DataGrid(_this.config);
			
			$('#btn-confirm').click(_this.editPwd);
			$('#btn-close').click(_this.closeMessPwd)
		}
	}
	return _this;
}();

$(function() {
	IQB.forgetpassword.init();
});
