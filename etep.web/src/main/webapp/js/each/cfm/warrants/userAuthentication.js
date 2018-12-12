$package('IQB.userAuthentication');
IQB.userAuthentication = function(){
	var _grid = null;
	var _tree = null;
	var _box = null;
	var _list = null;
	var _this = {
		cache :{
			page : 1
		},
		config: {
			action: {//页面请求参数
  				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				
			},
  			dataGrid: {//表格参数  				
			}
		},
		submit: function() {
			var uri = $("#factor").val();
			var json = {
					'owner': $("#owner").val(),
					'cert_no': $("#cert_no").val(),
					'card_no': $("#card_no").val(),
					'phone': $("#phone").val()
			}
			IQB.post(urls['rootUrl'] + '/auth/innerAuth' + uri, json, function(result){
				IQB.alert(result.retMsg);
			})
		},
		reset: function() {
			IQB.alert('reload');
		},
		init: function(){ 
			$("#factor").on('change', function() {
				_this.chooseType();
			});
			$("#submit").click(function() {
				_this.submit();
			});
			$("#reset").click(function() {
				_this.reset();
			});
		},
		chooseType : function() {
			var type = $("#factor").val();
			if(type == 'threeYards') {
				$("#phone").val("");
				$("#changeDiv").hide();
				return;
			}
			if (type == 'fourYards') {
				$("#phone").val("");
				$("#changeDiv").show();
				return;
			}
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.userAuthentication.init();
});	
/*显示日历*/ 
