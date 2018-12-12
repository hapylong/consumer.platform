$package('IQB.user');
IQB.user = function() {
	var _box = null;
	var _this = {
			unlockSysUser:function(){
				var records = _box.util.getCheckedRows();
			    if(records[0].count==5){
				IQB.get(_this.config.action.remLock,{usercode:records[0].userCode},function(result){
				IQB.alert("该用户可以正常登录");
					_box.handler.refresh();
				//刷新表格
				});
			   }else{
				  IQB.alert("该用户没有冻结");
			   }
			},

		config : {
			action : {
                remLock:urls['rootUrl']+'/SysUserRest/remSysUserLock'
			},
			event : {

			},
			dataGrid : {
				url : urls['rootUrl'] + '/SysUserRest/getSysUserLock',
				singleCheck : true
			}
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			 $('#btn-unlock').click(_this.unlockSysUser);

		},

	}
	return _this;
}();

$(function() {
	IQB.user.init();
});
