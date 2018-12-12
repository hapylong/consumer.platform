$package('IQB.listSend');
IQB.listSend = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/cheThreeHunder/selectCheThreeHWaitSendList'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('.merchantNo').val('');
					$(".merch").attr('merchantNo','');
					$(".merch").attr('orgCode','');
					$('select[name="carStatus"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/cheThreeHunder/selectCheThreeHWaitSendList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
								 loadSuccess:function(date){
						   				//笔数
						   				$("#allTerms").val(date.iqbResult.resultTotal.count);
						   			},
						   			onPageChanged:function(){
						   		    	$('#checkAll').prop('checked',false);
						   		    },
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/cheThreeHunder/selectCheThreeHWaitSendList',
	   			loadSuccess:function(date){
	   				//笔数
	   				$("#allTerms").val(date.iqbResult.resultTotal.count);
	   			},
	   			onPageChanged:function(){
	   		    	$('#checkAll').prop('checked',false);
	   		    },
			}
		},
		initSelect : function(){
			IQB.getDictListByDictType2('carStatus', 'carStatus');
			$('select[name="carStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/cheThreeHunder/selectCheThreeHWaitSendList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
				loadSuccess:function(date){
	   				//笔数
	   				$("#allTerms").val(date.iqbResult.resultTotal.count);
   			    },
   			    onPageChanged:function(){
	   		    	$('#checkAll').prop('checked',false);
	   		    },
			});
			$('#checkAll').prop('checked',false);
	    },
		send : function(){
			//车300灰名单发送
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				var orderList = [];
				$.each(records, function(i,n){
					orderList.push(
						 n.orderId
					);
				});
				IQB.post(urls['cfm'] + '/cheThreeHunder/registerPostLoanMonitorCar', {'orderIds':orderList}, function(result){
					if(result.success == '1'){
						IQB.alert('已发送');
						_this.refresh();
					}
					/*if(result.iqbResult.result.status == "1"){	
					}else{
						IQB.alert(result.iqbResult.result.message);
					}*/
				})
			}else{
				IQB.alert('未选中行');
			}
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
			_this.initSelect();
			_this.checkAll();
			//导出
			$('#btn-send').unbind('click').on('click',function(){
				_this.send();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.listSend.init();
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