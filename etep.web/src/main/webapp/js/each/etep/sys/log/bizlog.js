$package('IQB.bizlog');
IQB.bizlog = function(){
	var _box = null;
    var _list = null;
	var _this = {
		config: {
			action: {
  			},
			event: {
				
			},
  			dataGrid: {
  				url: urls['sysmanegeUrl'] + '/log/queryBizLog',
	   			idField: 'operType',
	   			//TODO
	   			queryParams: {	   				
	   				pageNo: 1,
	   				pageSize: 10,
	   				operType: 0
	   			}
			},
            tree: {
            }
		},
		init: function(){
			_box = new DataGrid(_this.config); 
            _list = new Tree(_this.config); 
			_box.init();
		}
	}
	return _this;
}();

$(function(){
	IQB.bizlog.init();
	datepicker(dateStart);
	datepicker(dateEnd);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:true,
	    format:'Y-m-d H:i:00',
		allowBlank: true
	});
};


$.getOperType = function(val){
	if(val == '1'){
		return '用户登录';
	}
	if(val == '0'){
		return '菜单访问';
	}
	return '';
}