$package('IQB.violationMark');
IQB.violationMark = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{

		},
		config: {
			action: {//页面请求参数
  				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,pageSize:50})	
							});
				},
				reset: function(){
					_grid.handler.reset();
					$('select[name="carStatus"]').val(null).trigger('change');
				},
			},
  			dataGrid: {//表格参数  				
  				url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyList',
  				queryParams: {
  					pageSize:50
  				}
			} 
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,pageSize:50})	
			});
	    },
		dispose : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				if(records.length > 1){
					IQB.alert('请选中单行');
				}else{
					if(records[0].status != '2'){
						IQB.alert('车辆未违章，请确认');
					}else if(records[0].status == '2' && records[0].processStatus == '2'){
						IQB.alert('违章已处理');
					}else{
						$('#open-win').modal('show');
						$('#processDate').val('');
						$('#remark').val('');
						//datagrid2
						$("#datagrid2").datagrid2({url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyDetailList',paginator: 'paginator1',queryParams : $.extend({}, {'vin': records[0].vin})});
						$("#btn-save").unbind("click").click(function(){
							  if($("#updateForm").form('validate')){
								  //保存违章处理
								  IQB.post(urls['cfm'] + '/carPeccancy/updateInstCarPeccancy', {'orderId': records[0].orderId,'processDate':$('#processDate').val(),'remark':$('#remark').val(),'vin': records[0].vin}, function(result){
										if(result.success=="1"){
											$('#open-win').modal('hide');
											_this.refresh();
										}
									}) 
							  }; 
					    });
					}
				}
			}else{
				IQB.alert('未选中行');
			}
            $("#btn-close").click(function(){
      	       $('#open-win').modal('hide');
		    });
		},
		detail : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				if(records.length > 1){
					IQB.alert('请选中单行');
				}else{
				   $('#open-win2').modal('show');
				   //datagrid3
				   $("#datagrid3").datagrid2({url: urls['cfm'] + '/carPeccancy/selectInstCarPeccancyDetailList',paginator: 'paginator2',queryParams : $.extend({}, {'vin': records[0].vin})});
				}
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close2").click(function(){
	      	       $('#open-win2').modal('hide');
			});
		},
		send : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				var orderList = [];
				$.each(records, function(i,n){
					orderList.push({
						 "orderId": n.orderId,
						 "vin": n.vin,
				         "engineNumber":n.engineNumber,
				         "licenseNo":n.licenseNo
					});
				});
				IQB.post(urls['cfm'] + '/carPeccancy/doSendAndGetInstCarPeccancyDetail', {'orderList':orderList}, function(result){
					if(result.success=="1"){
						IQB.alert('发送成功');
					}
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
        initSelect : function(){
			IQB.getDictListByDictType2('carStatus', 'carStatus');
			$('select[name="carStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.checkAll();
			_this.initSelect();
			//违章处理
			$('#btn-dispose').unbind('click').on('click',function(){
				_this.dispose();
			});
			//违章处理
			$('#btn-detail').unbind('click').on('click',function(){
				_this.detail();
			});
			/*//违章处理
			$('#btn-send').unbind('click').on('click',function(){
				_this.send();
			});*/
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.violationMark.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(startProcessDate);
	datepicker(endProcessDate);
	datepicker(startSendTime);
	datepicker(endSendTime);
	datepicker(processDate);
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