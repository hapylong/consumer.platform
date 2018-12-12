$package('IQB.paySuccess');
IQB.paySuccess = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		inits : function(){
			var data = {};
			IQB.get(_this.config.action.success, data, function(result) {
				var orderName = result.productName;
				var orderId = result.orderNo;
				var orderAmount = result.amount;
				var orderMethod = result.method;
				$(".orderName span").html(orderName);
				$(".orderId span").html(orderId);
				$(".orderAmount span").html(orderAmount);
				$(".orderMethod span").html(orderMethod);
		    })
		},
		//刷新页面
		config: {
			action: {//页面请求参数
				success: urls['cfm'] + '/nr/getPayReturn',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
	   			
			}
		},
		init: function(){ 
            _this.inits();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.paySuccess.init();
});	