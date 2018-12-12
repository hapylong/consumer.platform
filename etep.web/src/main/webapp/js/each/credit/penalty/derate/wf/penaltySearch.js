$package('IQB.reliefSearch');
IQB.reliefSearch = function() {
	var _box = null;
	var _this = {
		config:{
			action:{
				getById: urls['rootUrl'] + '/unIntcpt-penaltyDerate/getPenaltyDerateById',
			},
			dataGrid: {
				url: urls['rootUrl'] + '/unIntcpt-penaltyDerate/listPenaltyDerateApply',
				queryParams:{
					"bizId":""
				},
				singleCheck: true
			}
		},
		penaltyWin:function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var option = {};
				option['id'] = records[0].id;
		    	IQB.getById(_this.config.action.getById, option, function(result){	
		    		$("#updateForm")[0].reset();  
		    		$("#updateForm").form('load',result.iqbResult.result);
		    		var onlinePayFailImg = result.iqbResult.result.onlinePayFailImg;
		    		var debitBankWaterImg = result.iqbResult.result.debitBankWaterImg;
		    		var otherImg = result.iqbResult.result.otherImg;
		    		if(isNaN(onlinePayFailImg)&&""!=onlinePayFailImg&&"undefined"!=onlinePayFailImg){
		    			_this.imgShow("onlinePayFailImg",onlinePayFailImg);
		    		}
		    		if(isNaN(debitBankWaterImg)&&""!=debitBankWaterImg&&"undefined"!=debitBankWaterImg){
		    			_this.imgShow("debitBankWaterImg",debitBankWaterImg);
		    		}
		    		if(isNaN(otherImg)&&""!=otherImg&&"undefined"!=otherImg){
		    			_this.imgShow("otherImg",otherImg);
		    		}
		    		
					$('#penalty-win').modal({backdrop: 'static', keyboard: false, show: true});
		    	});
		    	$("input").attr("disabled",true);
		    	$("select").attr("disabled",true);
		    	$("#btn-save").css({ "display": "none" });
		    	$("#btn-close").click(function(){
		    		$("input").removeAttr("disabled");
		    		$("select").removeAttr("disabled");
		    		$("#btn-save").css({ "display": "initial" });
		    	});
			}
		},
		imgShow : function(key,value){
			var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
			      			'<a href="javascript:void(0)"><img src="' + urls.imgUrl+value + '" style="width: 135px; height: 135px;"></a>' +
			      		'</div>';
			$('#td-'+ key).append(html);
		},
		closePenaltyWin : function() {
			$('#penalty-win').modal('hide');
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			$('#btn-penalty').on('click', function(){
				_this.penaltyWin();
			});
			$('#btn-close').click(_this.closePenaltyWin);
		}
	}
   return _this;
}();


$(function() {
	IQB.reliefSearch.init();
	datepicker(createDateStart);
	datepicker(createDateEnd);
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