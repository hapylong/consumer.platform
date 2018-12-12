$package('IQB.deleteRecordJys');
IQB.deleteRecordJys = function(){ 
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        deleteRecord : function() {
        	var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows && rows.length > 0){
            	IQB.confirm('是否要删除已推动标的，重新推送？',function(){
            		IQB.post(_this.config.action.deleteRecord, {'id':rows[0].id,'breakId':rows[0].breakId}, function(result) {
						  if(result.iqbResult.result == 'success'){
							  IQB.alert('删除成功');
							  $('#open-win').modal('hide');
							  _this.refresh();
						  }else if(result.iqbResult.result == '-1'){
							  IQB.alert('历史订单不可做删除操作');
							  $('#open-win').modal('hide');
							  _this.refresh(); 
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
            	deleteRecord: urls['cfm'] + '/assetallocine/deleteAssetAllocationInfo',
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
					$('select[name="fund_source"]').val(null).trigger('change');
				},
            },
            dataGrid: {//表格参数               
                url: urls['cfm'] + '/assetallocine/assetAllocationList',
                singleCheck: true
            }
        },
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/assetallocine/assetAllocationList',queryParams : $.extend({}, $("#searchForm").serializeObject())	
			});
	    },
        initSelect: function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			IQB.getDictListByDictType2('fund_source', 'fund_sourceJys');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('select[name="fund_source"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.initSelect();
            $("#btn-delete").unbind('click').click(function(){_this.deleteRecord()});
        }
    }
    return _this;
}();
$(function(){
    //页面初始化
    datepicker(search_expireDate);
    datepicker(proBeginDateBegin);
    datepicker(proBeginDateEnd);
    IQB.deleteRecordJys.init();
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
