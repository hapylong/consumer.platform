$package('IQB.marginClearing');
IQB.marginClearing = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		check : function(){
			$("#btn-clearing").on('click',function(){
				var records = _grid.util.getCheckedRows();
				if(records.length > 0){	
					IQB.confirm('您确定要发起保证金结算流程吗？',function(){
					      //启动结清工作流(启动一条工作流)
						  var orderIds = '';
						  $.each(records,function(i,n){
							  orderIds += n.orderId + ',';
						  });
						  orderIds = orderIds.substring(0, orderIds.length - 1);  
						  IQB.post(_this.config.action.startOverdueInfoStartWF, {'orderIds':orderIds}, function(result) {
								if(result.iqbResult.result > 0){
									IQB.alert('流程启动成功');
									_this.refresh();
								}
							})
					},function(){});
				}else{
					IQB.alert('未选中行');
				}
			})
		},
		config: {
			action: {//页面请求参数
				startOverdueInfoStartWF : urls['cfm'] + '/overdueInfo/startOverdueInfoStartWF'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/overdueInfo/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'pageSize':20,'status':1})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/overdueInfo/query',
	   			queryParams :{
	   				'pageSize':20,
	   				'status':1
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/overdueInfo/query',queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'pageSize':20,'status':1})	
			});
			$('#checkAll').prop('checked',false);
	    },
	    initSelect : function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		$('#datagrid').datagrid2('checkAll');
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.check();
			_this.initSelect();
			_this.checkAll();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.marginClearing.init();
	datepicker(startTime);
	datepicker(endTime);
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