$package('IQB.configPayWay');
IQB.configPayWay = function(){
	var _box = null;
    var _list = null;
    var imgUrl = '';
	var _this = {
		cache:{},
		config: {
			action: {
				insert: urls['sysmanegeUrl'] + '/product/addPayConf',
  				update: urls['sysmanegeUrl'] + '/product/updatePayConf',
  				getById: urls['sysmanegeUrl'] + '/product/getPayConfByMno',
  				remove: urls['sysmanegeUrl'] + '/product/delPayConf',
  			},
			event: {
				reset: function(){//重写save	
					_box.handler.reset();
					$('select[name="bizOwner"]').val(null).trigger('change');
					$('select[name="payWay"]').val(null).trigger('change');
				},
				update: function(){//重写update
					_this.extFunc.updateInfo();
				},
				insert: function(){//重写insert
					_this.extFunc.insertInfo();
				},
				close:function(){
					$("#menuContentModel").hide();
					$('.merchModel').removeAttr('disabled');
					_box.handler.close();
				}
			},
  			dataGrid: {
  				url: urls['cfm'] + '/product/queryPayConfList',
  				singleCheck: true
			}
		},
		extFunc:{
			updateInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['id'] = records[0].id;
			    	IQB.getById(_this.config.action.getById, option, function(result){	
			    		$("#updateForm")[0].reset();  
			    		$("#updateForm").form('load',result.iqbResult.result);
			    		$('#bizOwner').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择支付主体"});
			    		$('#payWay').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择支付通道"});
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
			    		$('.modal-backdrop').addClass('z-index2');
			    		if(result.iqbResult.result.bizOwner != null && result.iqbResult.result.bizOwner != ''){
			    			$('#bizOwner').select2("val", result.iqbResult.result.bizOwner);
			    		}
			    		if(result.iqbResult.result.payWay != null && result.iqbResult.result.payWay != ''){
			    			$('#payWay').select2("val", result.iqbResult.result.payWay);
			    		}
			    		$('.merchModel').attr('disabled',true).val(result.iqbResult.result.merchantName);
			    		$('#btn-save').unbind('click').on('click',function(){
			    			var data = {};
			    			data['id'] = records[0].id;
					    	data['merchantNo'] = $('.merchantNo').val();
					    	data['bizOwner'] = $('#bizOwner').val();
					    	data['payWay'] = $('#payWay').val();
					    	data['gateWay'] = $('#gateWay').val();
					    	data['service'] = $('#service').val();
					    	data['vSon'] = $('#vSon').val();
					    	data['merchantId'] = $('#merchantId').val();
					    	data['key'] = $('#key').val();
					    	data['secId'] = $('#secId').val();
					    	data['certPath'] = $('#certPath').val();
					    	data['prikeyPath'] = $('#prikeyPath').val();
					    	IQB.post(_this.config.action.update, data, function(result) {
					    		if(result.iqbResult.result == 'success'){
					    			$("#update-win").modal('hide');
					    			_box.handler.refresh();
					    		}
					    	})
			    		});
					});
				}
			},
			insertInfo : function(){
				$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
				$("#updateForm")[0].reset();  
				$('#bizOwner').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择支付主体"});
				$('.modal-backdrop').css('z-index','97');
				$('#updateForm').css('z-index','98');
				$('.modal-backdrop').addClass('z-index2');
				$('.merchModel').removeAttr('disabled');
				$('#btn-save').unbind('click').on('click',function(){
	    			var data = {};
			    	data['merchantNo'] = $('.merchantNo').val();
			    	data['bizOwner'] = $('#bizOwner').val();
			    	data['payWay'] = $('#payWay').val();
			    	data['gateWay'] = $('#gateWay').val();
			    	data['service'] = $('#service').val();
			    	data['vSon'] = $('#vSon').val();
			    	data['merchantId'] = $('#merchantId').val();
			    	data['key'] = $('#key').val();
			    	data['secId'] = $('#secId').val();
			    	data['certPath'] = $('#certPath').val();
			    	data['prikeyPath'] = $('#prikeyPath').val();
			    	IQB.post(_this.config.action.insert, data, function(result) {
			    		if(result.iqbResult.result == 'succ'){
			    			$("#update-win").modal('hide');
			    			_box.handler.refresh();
			    		}else{
			    			IQB.alert(result.iqbResult.result);
			    			$("#update-win").modal('hide');
			    			_box.handler.refresh();
			    		}
			    	})
	    		});
			},
			deletePush: function(callback){
				var records = _box.util.getCheckedRows();				
				if (_box.util.checkSelectOne(records)){
					IQB.confirm('是否删除？', function(){
						var option = {};
						option['id'] = records[0].id;
						IQB.remove(_this.config.action.remove, option, function(result){								
							_box.handler.refresh();
						});
					}, function(){});	
				}
			},
		},
		init: function(){
			_box = new DataGrid2(_this.config); 
            _list = new Tree(_this.config); 
			_box.init();
			this.initSelect();
			this.initBtnClick();
		},
		initSelect: function(){
			IQB.getDictListByDictType2('bizOwner', 'BIZ_OWNER');
			IQB.getDictListByDictType2('payWay', 'PAY_WAY');
			$('select[name="bizOwner"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('select[name="payWay"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		initBtnClick: function(){
			$('#btn-deletePush').click(_this.extFunc.deletePush);
		}
	}
	return _this;
}();

$(function(){
	/** 初始化表格  **/
	IQB.configPayWay.init();
});	


