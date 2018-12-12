$package('IQB.wfProcessDelegate');
IQB.wfProcessDelegate = function(){
	var _box = null;
	var _this = {
		formatterOfSkipPage: function(val, row, rowIndex){
			return '<button type="button" class="btn btn-link" onclick="IQB.wfProcessDelegate.skipPage(\'' + row.procBizId + '\',\'' + row.procInstId + '\',\'' + row.procDisplayurl + '\')">' + val + '</button>';
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		config: {
			action: {//页面请求参数
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/WfMonitor/processDelegate',
	   			singleCheck : true,
	   			isExport : true,
	   			cols : '2,5,7,8,9,10,11,12,13,14,15',
	   			queryParams: {
					procAuthType : '2'
				}
			},
		},
		init: function(){
			_box = new DataGrid2(_this.config);
			_box.init();
			$('#search-procStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择流程状态', allowClear: true}).val(null).trigger("change");		
		}
	}
	return _this;
}();


$(function(){
	IQB.wfProcessDelegate.init();
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
