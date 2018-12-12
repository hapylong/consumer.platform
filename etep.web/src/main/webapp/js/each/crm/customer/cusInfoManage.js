$package('IQB.cusInfoManage');
IQB.cusInfoManage = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		setMerCodeSelect : function() {
			var data = {};
			IQB.get(_this.config.action.getMerCodeInfo, data, function(result) {
				jQuery("select[name='merchantNo']").prepend(
						"<option value=''>全部</option>");
				var arr = result.iqbResult.result;
				$(arr).each(
						function(index) {
							jQuery("select[name='merchantNo']").append(
									"<option value='" + this.merchantNo + "'>"
											+ this.merchantShortName
											+ "</option>");
						});
			})
		},
		check : function(){
			$("#btn-check").click(function(){
				var records = _grid.util.getCheckedRows();
				if(_grid.util.checkSelectOne(records)){
					var option = {reg_id: records[0].reg_id};				    	
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
  				update: urls['cfm'] + '/user/mod', 
  				getById: urls['cfm'] + '/user/queryById',
  				remove: urls['cfm'] + '/user/del',
  				getMerCodeInfo : urls['cfm']+ '/merchant/getMerList',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				/*update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改车型');
					$('#update-win #merchantNo').attr("disabled",true);
				}*/
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/user/query',
	   			singleCheck: true,
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.setMerCodeSelect(); 
			_this.check();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.cusInfoManage.init();
});		