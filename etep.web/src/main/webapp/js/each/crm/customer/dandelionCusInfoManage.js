$package('IQB.dandelionCusInfoManage');
IQB.dandelionCusInfoManage = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		check : function(){
			$("#btn-check").click(function(){
				var records = _grid.util.getCheckedRows();
				if(_grid.util.checkSelectOne(records)){
					var option = {regId: records[0].regId};				    	
			    	IQB.get(_this.config.action.getById, option, function(result){	
			    		$('#updateForm').form('load', result.iqbResult.result);
			    		$('#update-win').modal({backdrop: 'static', keyboard: false, show: true});			    		
					});
			    	$("input").attr("disabled",true);
			    	$("select").attr("disabled",true);
			    	$(".btn-success").css({ "display": "none" });
			    	$("#btn-close").click(function(){
			    		$("input").removeAttr("disabled");
			    		$("select").removeAttr("disabled");
			    		$("#btn-save").css({ "display": "initial" });
			    	});
				}
			})
		},
		config: {
			action: {//页面请求参数
  				getById: urls['cfm'] + '/customer/getDandelionCustomerDetail'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/customer/getDandelionCustomerList',
	   			singleCheck: true,
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.check();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.dandelionCusInfoManage.init();
});		