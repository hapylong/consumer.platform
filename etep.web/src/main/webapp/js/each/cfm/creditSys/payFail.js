$package('IQB.payFail');
IQB.payFail = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		inits : function(){
			var data = {};
			IQB.get(_this.config.action.fail, data, function(result) {
				
		    })
		},
		//刷新页面
		config: {
			action: {//页面请求参数
				fail: urls['cfm'] + '',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			
			}
		},
		init: function(){ 
			 /*_this.inits();*/
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.payFail.init();
});	