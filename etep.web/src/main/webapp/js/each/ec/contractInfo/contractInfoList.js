$package('IQB.contractInfoList');
IQB.contractInfoList = function() {
	var _box = null;
	var _this = {
		config : {
			action:{
				getById: urls['rootUrl'] + '/contract/unIntcpt-judgeSign',
			},
			dataGrid: {
				url: urls['rootUrl'] + '/contract/init',
				singleCheck: true
			}
		},
		contractInfoWin:function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var option = {};
				option['orderId'] = records[0].orderId;
				option['bizType'] = records[0].bizType;
				option['orgCode'] = records[0].orgCode;
				IQB.getById(_this.config.action.getById, option, function(result){	
					if("00000000" == result.retCode){
				    	window.parent.IQB.main2.openTab("contract_info", "合同信息补录", '/etep.web/view/ec/contractInfo/contractInfoInput.html?orderId='+records[0].orderId+'&orgCode='+records[0].orgCode, true, true, null);
					}else{
						IQB.alert("判断数据");
					}
		    	});
			}
		},
		contractInfoWin1:function(){
			$.ajax({
				url: urls['rootUrl'] + '/contract/unIntcpt-judgeSign',
				type: 'post',
				async: true,
			 	success: function(result){//请求成功
			 	},
			});
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			
			$('#btn-contractInfo').on('click', function(){
				_this.contractInfoWin();
			});
			
			$('#btn-contractInfo1').on('click', function(){
				_this.contractInfoWin1();
			});
		}
	}
	return _this;
}();

$(function() {
	IQB.contractInfoList.init();
	datepicker(startDateStart);
	datepicker(startDateEnd);
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