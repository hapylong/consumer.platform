$package('IQB.penaltyRelief');
IQB.penaltyRelief = function() {
	var _box = null;
	var _this = {
		config:{
			dataGrid: {
				url: urls['rootUrl'] + '/unIntcpt-penaltyDerate/listPenaltyDerateApply',
				queryParams:{
					"bizId":""
				},
				singleCheck: true
			}
		},
		//减免申请页面跳转
		apply : function() {
			window.parent.IQB.main2.openTab("relief_apply", "减免申请", '/etep.web/view/credit/penalty/derate/wf/penaltyDerateApply.html', true, true, null);
		},	
		init:function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			//跳转到减免申请页面
			$('#btn-suspend').click(function() {
				_this.apply();
			});
		},
	}
   return _this;
}();


$(function() {
	IQB.penaltyRelief.init();
	datepicker(createDateStart);
	datepicker(createDateEnd);
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