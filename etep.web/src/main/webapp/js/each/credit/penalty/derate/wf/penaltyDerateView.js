function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('procBizId');
var procTaskId = getQueryString('procTaskId');
var procInstId = getQueryString('procInstId');

$package('IQB.penaltyDerateView');
IQB.penaltyDerateView = function() {
	var _this = {
		cache:{
			viewer: {}
		},
		config:{
			getById: urls['rootUrl'] + '/unIntcpt-penaltyDerate/getPenaltyDerateByUUId',
		},
		initFrom : function(){
			var option = {};
			option['uuid'] = window.procBizId;
			IQB.post(_this.config.getById, option, function(result){
				$("#updateForm")[0].reset();  
	    		$("#updateForm").form('load',result.iqbResult.result);
	    		$("input[name='monthInterest']").val(parseFloat(result.iqbResult.result.monthInterest).toFixed(2));
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
			});
		},
		imgShow : function(key,value){
			if(value.indexOf(',') >= 0){
				value = value.substring(1,value.length-1).split(',');
				var html = '';
				$.each(value, function(i, n){
					n = n.substring(1,n.length-1);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
	      			'<a href="javascript:void(0)"><img src="' + urls.imgUrl+n + '" style="width: 135px; height: 135px;"></a>' +
	      		    '</div>';
				})
				$('#td-'+ key).append(html);
				if(_this.cache.viewer.viewerOne){
					_this.cache.viewer.viewerOne.update();
				}else{
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			}else{
				var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
      			'<a href="javascript:void(0)"><img src="' + urls.imgUrl+value + '" style="width: 135px; height: 135px;"></a>' +
      		    '</div>';
                $('#td-'+ key).append(html);
                if(_this.cache.viewer.viewerOne){
					_this.cache.viewer.viewerOne.update();
				}else{
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			}
		},
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		init : function() {
			_this.initFrom();
			_this.initApprovalHistory();
			$("input").attr("disabled",true);
	    	$("select").attr("disabled",true);
	    	$("textarea").attr("disabled",true);
		}
	}
   return _this;
}();

$(function() {
	IQB.penaltyDerateView.init();
});

