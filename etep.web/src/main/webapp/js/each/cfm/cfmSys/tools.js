$package('IQB.tools');
IQB.tools = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				unbind: urls['cfm'] + '/business/unBindBankCard',
				query: urls['cfm'] + '/business/getBindBankCard',
  			},
			event: {//按钮绑定函数，不定义的话采用默认

			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '',
			}
		},
		//解绑
		unbind:function(){
			var bankCard = $('#bankCard').val();
			var regId = $('#regId').val();
			var payOwnerId = $('#payOwnerId').val();
			var option = {
					'bankCardNo':bankCard,
					'userId':regId,
					'payOwnerId':payOwnerId
			}
			if(bankCard != '' && regId != ''){
				IQB.confirm('确定要解绑吗？', function(){
					IQB.post(_this.config.action.unbind, option, function(result) {
						if(result.status == 00){
							IQB.alert('解绑成功');
						}else{
							if(result.respMsg != ''){
								IQB.alert(result.respMsg);
							}else{
								IQB.alert('解绑失败');
							}
						}
					})
				});
			}else{
				IQB.alert('请输入解绑相关信息');
			}
		},
		//查询用户绑卡信息
		query:function(){
			var regId = $('#regId').val();
			var payOwnerId = $('#payOwnerId').val();
			IQB.post(_this.config.action.query, {'userId':regId,'payOwnerId':payOwnerId}, function(result) {
				if(result.status == 00){
					$('#bankCard').val(result.bankList[0].bankCardNo);
				}else{
					IQB.alert(result.respMsg);
				}
			})
		},
		init: function(){ 
			//_grid = new DataGrid2(_this.config); 
			//_grid.init();//初始化按钮、表格
			$('#btn-unbind').click(function(){
				_this.unbind();
			});
			$('#btn-query').click(function(){
				_this.query();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.tools.init();
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:true,
	    format:'Y-m-d H:i:00',
		allowBlank: true
	});
};
