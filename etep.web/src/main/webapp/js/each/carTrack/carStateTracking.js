$package('IQB.carStateTracking');
IQB.carStateTracking = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        refreshWindow : function() {
        	var rows = $("#datagrid").datagrid2('getCheckedRows');
            if(rows && rows.length > 0){
            	IQB.confirm('您确定要走贷后处置吗？',function(){
            		console.log(rows[0].orderId)
            		IQB.get(_this.config.action.carStateTracking, {'orderId':rows[0].orderId}, function(result) {
            			if(result.iqbResult.result == 'success'){
            					console.log(2),
                    		  _this.refresh(_this.cache.page);
							  IQB.alert('贷后处置成功');
							  $('#open-win').modal('hide');
						  }else{
							  console.log(result.iqbResult.result),
							  IQB.alert('贷后处置失败');
							  $('#open-win').modal('hide');
						  }
                	})
            	},function(){});
            }else{
                IQB.alert('未选中行');
                return false;   
            }                 
        },
        config: {
            action: {//页面请求参数
            	carStateTracking:urls['cfm'] + '/carstatus/persisit_imci'
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					_grid.handler.reset();
					$('select[name="gpsStatus"]').val(null).trigger('change');
				},
            },
            dataGrid: {//表格参数               
            	url: urls['cfm'] + '/carstatus/cget_info_list',
                singleCheck: true,
                queryParams: {
                	channal: 1,
	   			}
            }
        },
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/carstatus/cget_info_list',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,channal: 1})});
        },
        initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.initSelect();
            $("#btn-skip").click(function(){_this.refreshWindow()});
        },
    }
    return _this;
}();
$(function(){
    //页面初始化
    datepicker(search_expireDate);
    IQB.carStateTracking.init();
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
