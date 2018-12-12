$package('IQB.deleteRecord');
IQB.deleteRecord = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        deleteRecord : function() {
        	var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows && rows.length > 0){
            	var orderId = rows[0]['orderId'];
            	//删除记录
				$('#open-win').modal('show');
				$('#reason').val('');
				$("#btn-sure").unbind("click").click(function(){
					  var option = {
							  "orderId": rows[0]['orderId'],
							  "id": rows[0].id,
							  "applyTime": rows[0].applyTime,
							  "applyItems": rows[0].applyItems,
							  "sourcesFunding": rows[0].sourcesFunding,
							  "creditName": rows[0].creditName,
							  "delRemark": $('#reason').val()
					  }
					  if($("#updateForm").form('validate')){
						  IQB.post(_this.config.action.deleteRecord, option , function(result) {
							  IQB.alert(result.iqbResult.result);
							  $('#open-win').modal('hide');
							  _this.refresh();
						  })
					  }
				})
				$('#btn-close').on('click',function(){
					$('#open-win').modal('hide');
				});
            }else{
                IQB.alert('未选中行');
                return false;   
            }                 
        },
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/pushRecord/pushRecordByOrderIdList',queryParams : $.extend({}, $("#searchForm").serializeObject())	
			});
	    },
        config: {
            action: {//页面请求参数
            	deleteRecord: urls['cfm'] + '/pushRecord/delPushRecordById'
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					_grid.handler.reset();
				},
				search: function(){
					if($('#searchForm').form('validate')){
						_grid.handler.search();
					}
				}
            },
            dataGrid: {//表格参数               
                url: urls['cfm'] + '/pushRecord/pushRecordByOrderIdList',
                singleCheck: true
            }
        },
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            $("#btn-delete").click(function(){_this.deleteRecord()});
        }
    }
    return _this;
}();
$(function(){
    //页面初始化
    IQB.deleteRecord.init();
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
