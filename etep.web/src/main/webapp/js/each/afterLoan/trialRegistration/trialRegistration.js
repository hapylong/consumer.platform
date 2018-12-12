$package('IQB.trialRegistration');
IQB.trialRegistration = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectTrialRegistList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'flag':1})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectTrialRegistList',
	   			singleCheck: true,
	   			queryParams:{
	   				'flag':1
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectTrialRegistList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'flag':1})	
			});
	    },
		register : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				$('#updateForm')[0].reset();
			    //庭审登记备注历史
				$("#datagrid2").datagrid2({url: urls['cfm'] + '/afterLoan/selectInstOrderLawResultByCaseId',paginator: 'paginator2',queryParams : $.extend({}, {'caseId': records[0].caseId})	
				});
				$("#btn-sure").unbind("click").click(function(){
					  if($("#updateForm").form('validate')){
						  //保存申请立案信息
						  var option = {
								  'orderId':records[0].orderId,
								  'caseId':records[0].caseId,
								  'caseResult':$('#trialResults').val(),
								  'executeFlag':$('#performFlag').val(),
								  'courtRemark':$('#trialRemark').val(),
								  
						  };
						  IQB.post(urls['cfm'] + '/afterLoan/saveInstOrderLawResult', option, function(result){
							  if(result.success == '1'){
								  IQB.alert('登记成功');
								  $('#open-win').modal('hide');
								  _this.refresh();
							  }
						  })
					  }
				})
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
      	        $('#open-win').modal('hide');
      	        $('#updateForm')[0].reset()
		    });
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			//庭审登记
			$('#btn-register').unbind('click').on('click',function(){
				_this.register();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.trialRegistration.init();
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