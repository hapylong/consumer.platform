$package('IQB.enterprisecustomers');
IQB.enterprisecustomers = function(){
	var _box = null;
    var _list = null;
    var imgUrl = '';
	var _this = {
		cache:{},
		config: {
			action: {
				insert: urls['sysmanegeUrl'] + '/customer/insertCustomerInfo',
  				update: urls['sysmanegeUrl'] + '/customer/updateCustomerInfo',
  				getById: urls['sysmanegeUrl'] + '/customer/getCustomerInfoByCustomerCode',
  				remove: urls['sysmanegeUrl'] + '/customer/deleteCustomerInfo',
  				pushToXFJR: urls['sysmanegeUrl'] + '/customer/pushCustomerInfoToXFJR'
  			},
			event: {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#query-customerType').val(null).trigger('change');
					$('#query-customerStatus').val(null).trigger('change');
				},
				update: function(){//重写update
					_this.extFunc.updateCustomerInfo();
				},
				insert: function(){//重写insert
					$("#imgUl1").find("li").remove();
					$("#imgUl2").find("li").remove();
					$("#updateForm")[0].reset();  
					$('#select-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择客户类型"});
					$('#select-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择业务类型"});
					_box.handler.insert();
				},
				close:function(){
					_box.handler.close();
					$('.btn-upload-div').show();
					$('.btn-upload-remove').show();
				}
			},
  			dataGrid: {
  				url: urls['cfm'] + '/customer/queryCustomerList',
  				singleCheck: true
			}
		},
		extFunc:{
			checkCustomerInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){		
			    		$("#updateForm")[0].reset(); 
			    		$("#updateForm").form('load',result.iqbResult.result);
			    		
			    		$('#query-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#query-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#query-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-customerType').select2({theme: 'bootstrap', placeholder: "请选择客户类型"});
						$('#select-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-holdWeixin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-businessType').select2({theme: 'bootstrap', placeholder: "请选择业务类型"});
						$('#select-province').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-city').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-riskManageType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-isFather').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-layer').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-overdueInterestModel').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-isVirtualMerc').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#riskFlag').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#risk_code').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-belongsArea').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$("#select-province").trigger("change"); 
						$("#select-city").val(result.iqbResult.result.city).trigger('change');
						
						if(result.iqbResult.result.customerType != undefined){
			    			var arr = eval(result.iqbResult.result.customerType);
			    			$('#select-customerType').select2("val", [arr]);  
			    		}
						

						if(result.iqbResult.result.businessType != undefined){
			    			var arr = eval(result.iqbResult.result.businessType);
			    			$('#select-businessType').select2("val", [arr]);  
			    		}
						
						$('.btn-upload-div').hide();
						
						_this.extFunc.renderCustomerImgCheck(result.iqbResult.result);
						
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
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
			updateCustomerInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){	
			    		$("#updateForm")[0].reset();  
			    		$("#updateForm").form('load',result.iqbResult.result);
			    		
			    		$('#query-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#query-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#query-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择客户类型"});
						$('#select-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-holdWeixin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-province').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-city').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-riskManageType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-isFather').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-layer').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-overdueInterestModel').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-corporateCertificateType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-isVirtualMerc').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#riskFlag').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#risk_code').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-belongsArea').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$("#select-province").trigger("change"); 
						$("#select-city").val(result.iqbResult.result.city).trigger('change');
			    		$("#updateForm").prop('action',_this.config.action.update);
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
			    		
			    		if(result.iqbResult.result.customerType != undefined){
			    			var arr = eval(result.iqbResult.result.customerType);
			    			$('#select-customerType').select2("val", [arr]);  
			    		}
			    		
			    		if(result.iqbResult.result.businessType != undefined){
			    			var arr = eval(result.iqbResult.result.businessType);
			    			$('#select-businessType').select2("val", [arr]);  
			    		}
			    		
			    		if(result.iqbResult.result.customerShortNameCode != undefined){
			    			$("#customerShortNameCode").prop("readonly",true);
			    		}
			    		
			    		_this.extFunc.renderCustomerImg(result.iqbResult.result);
			    		
					});
					$("#customerName").prop("readonly",true);
					$("#customerCode").prop("readonly",true);
					$("#customerShortName").prop("readonly",true);
					$("#btn-close").click(function(){
						$("input").removeAttr("readonly");
					});
				}
			},
			renderCustomerImg: function(result){
				$("#imgUl1").find("li").remove();
				$("#imgUl2").find("li").remove();
				if(result.componyImgUrl != undefined){
					var option = {};
					option.imgName = Formatter.getImgName(result.componyImgUrl);
					option.imgPath = result.componyImgUrl;
					option.imgUrl = urls['imgUrl'] + option.imgPath;
					option.imgType = 1; 
					var html = 
						'<li class="qq-file-id-0 qq-upload-success" qq-file-id="0">' +
							'<div class="thumbnail float-left" style="width: 145px;">' + 
				      			'<a href="' + option.imgUrl + '" data-lightbox="group" data-title="' + option.imgName + '"><img src="' + option.imgUrl + '" style="width: 135px; height: 135px;"></a>' +
				      			'<div class="caption">' +		
				      				'<button type="button" class="btn btn-danger btn-xs btn-upload-remove" data-loading-text="正在移除图片" autocomplete="off" imgPath="' + option.imgPath + '" onclick="IQB.enterprisecustomers.extFunc.removeImg(event,' + option.imgType + ');"><span class="glyphicon glyphicon-trash"></span> 移除</button>' + 	
				      			'</div>' + 
				      		'</div>';
						'</li>';
					$('#imgUl1').append(html);
				}
				if(result.corporateImgUrl != undefined){
					var option = {};
					option.imgName = Formatter.getImgName(result.corporateImgUrl);
					option.imgPath = result.corporateImgUrl;
					option.imgUrl = urls['imgUrl'] + option.imgPath;
					option.imgType = 2;
					var html = 
						'<li class="qq-file-id-0 qq-upload-success" qq-file-id="0">' +
						'<div class="thumbnail float-left" style="width: 145px;">' + 
						'<a href="' + option.imgUrl + '" data-lightbox="group" data-title="' + option.imgName + '"><img src="' + option.imgUrl + '" style="width: 135px; height: 135px;"></a>' +
						'<div class="caption">' +		
						'<button type="button" class="btn btn-danger btn-xs btn-upload-remove" data-loading-text="正在移除图片" autocomplete="off" imgPath="' + option.imgPath + '" onclick="IQB.enterprisecustomers.extFunc.removeImg(event,' + option.imgType + ');"><span class="glyphicon glyphicon-trash"></span> 移除</button>' + 	
						'</div>' + 
						'</div>';
					'</li>';
					$('#imgUl2').append(html);
				}
			},
			renderCustomerImgCheck: function(result){
				$("#imgUl1").find("li").remove();
				$("#imgUl2").find("li").remove();
				if(result.componyImgUrl != undefined){
					var option = {};
					option.imgName = Formatter.getImgName(result.componyImgUrl);
					option.imgPath = result.componyImgUrl;
					option.imgUrl = urls['imgUrl'] + option.imgPath;
					option.imgType = 1; 
					var html = 
						'<li class="qq-file-id-0 qq-upload-success" qq-file-id="0">' +
						'<div class="thumbnail float-left" style="width: 145px;">' + 
						'<a href="' + option.imgUrl + '" data-lightbox="group" data-title="' + option.imgName + '"><img src="' + option.imgUrl + '" style="width: 135px; height: 135px;"></a>' +
						'<div class="caption">' +		
						'<button type="button" disabled class="btn btn-danger btn-xs btn-upload-remove" data-loading-text="正在移除图片" autocomplete="off" imgPath="' + option.imgPath + '" onclick="IQB.enterprisecustomers.extFunc.removeImg(event,' + option.imgType + ');"><span class="glyphicon glyphicon-trash"></span> 移除</button>' + 	
						'</div>' + 
						'</div>';
					'</li>';
					$('#imgUl1').append(html);
				}
				if(result.corporateImgUrl != undefined){
					var option = {};
					option.imgName = Formatter.getImgName(result.corporateImgUrl);
					option.imgPath = result.corporateImgUrl;
					option.imgUrl = urls['imgUrl'] + option.imgPath;
					option.imgType = 2;
					var html = 
						'<li class="qq-file-id-0 qq-upload-success" qq-file-id="0">' +
						'<div class="thumbnail float-left" style="width: 145px;">' + 
						'<a href="' + option.imgUrl + '" data-lightbox="group" data-title="' + option.imgName + '"><img src="' + option.imgUrl + '" style="width: 135px; height: 135px;"></a>' +
						'<div class="caption">' +		
						'<button type="button" disabled class="btn btn-danger btn-xs btn-upload-remove" data-loading-text="正在移除图片" autocomplete="off" imgPath="' + option.imgPath + '" onclick="IQB.enterprisecustomers.extFunc.removeImg(event,' + option.imgType + ');"><span class="glyphicon glyphicon-trash"></span> 移除</button>' + 	
						'</div>' + 
						'</div>';
					'</li>';
					$('#imgUl2').append(html);
				}
			},
			pushToXFJR: function(){
				var records = _box.util.getCheckedRows();
				var option = {};
				option['customerCode'] = records[0].customerCode;
				IQB.getById(_this.config.action.pushToXFJR, option, function(result){	
					if(result.success == 1){
						IQB.alert('数据已推送！');
					}
				});
			},
			deletePush: function(callback){
				var records = _box.util.getCheckedRows();				
				if (_box.util.checkSelectOne(records)){
					IQB.confirm('是否删除？', function(){
						var option = {};
						option['customerCode'] = records[0].customerCode;
						IQB.remove(_this.config.action.remove, option, function(result){								
							_box.handler.refresh();
//							IQB.confirm('是否推送删除操作至消费金融？', function(){alert(1)}, function(){});
						});
					}, function(){});	
				}
			},
			provinceSelect: function(){
				$('#select-city').empty();
				var req_data = {'provinceName': $("#select-province").val()};
				IQB.postAsync(urls['rootUrl'] + '/customer/getCustomerCityListByProvince', req_data, function(result){
					$('#select-city').prepend("<option value=''>请选择</option>");
					$('#select-city').select2({theme: 'bootstrap', data: result.iqbResult.result});
					return result.iqbResult.result;
				})
			},
			updateFormSub: function(){//移除图片
				if($('#updateForm').form('validate')){
					var url = $('#updateForm').prop('action');
					var option = $('#updateForm').serializeObject();
					IQB.postAsync(url, option, function(result){ 
						$('#img').click();
						$('#updateForm').prop('action', urls['sysmanegeUrl'] + '/customer/updateCustomerInfo');
					});
				}	
			},
			removeImg: function(e, imgType){//移除图片
				var tarent = e.currentTarget;
				var imgPath = $(tarent).attr('imgPath');
				var option = {};			
				option.imgPath = imgPath;			
				IQB.post(urls['cfm'] + '/fileUpload/remove', option, function(result){
					option.filePath = imgPath;
					option.imgType = imgType;
					option.customerCode = $("#customerCode").val();
					IQB.post(urls['cfm'] + '/customer/deleteCustomerImg', option, function(resultInfo){
						$(tarent).parent().parent().remove();
		 			});
				});			
			},
			uploadImg: function(){// 上传图片
				$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/upload/pic/crm');
				IQB.postForm($('#uploadForm'), function(response){
					var option = {};
					option.imgType = $('#imgType').val();
					option.imgName = Formatter.getImgName(response.iqbResult.result);
					option.imgPath = response.iqbResult.result;
					option.imgUrl = urls['imgUrl'] + option.imgPath;
					option.customerCode = $("#customerCode").val();
					IQB.post(urls['cfm'] + '/customer/uploadCustomerImg', option, function(resultInfo){
						var html = 
							'<li class="qq-file-id-0 qq-upload-success" qq-file-id="0">' +
								'<div class="thumbnail float-left" style="width: 145px;">' + 
					      			'<a href="' + option.imgUrl + '" data-lightbox="group" data-title="' + option.imgName + '"><img src="' + option.imgUrl + '" style="width: 135px; height: 135px;"></a>' +
					      			'<div class="caption">' +		
					      				'<button type="button" class="btn btn-danger btn-xs btn-upload-remove" data-loading-text="正在移除图片" autocomplete="off" imgPath="' + option.imgPath + '" onclick="IQB.enterprisecustomers.extFunc.removeImg(event,' + option.imgType + ');"><span class="glyphicon glyphicon-trash"></span> 移除</button>' + 	
					      			'</div>' + 
					      		'</div>';
							'</li>';
						$('#imgUl' + option.imgType).find("li").remove();
						$('#imgUl' + option.imgType).append(html);
		 			});
					
	 			});
			}
		},
		init: function(){
			_box = new DataGrid2(_this.config); 
            _list = new Tree(_this.config); 
			_box.init();
			this.initSelect();
			this.initBtnClick();
			this.initLevelSelect();
			this.initOthers();
		},
		initSelect: function(){
			IQB.getDictListByDictType('query-customerStatus', 'CUSTOMER_STATUS');
			IQB.getDictListByDictType('query-customerType', 'CUSTOMER_TYPE');
			IQB.getDictListByDictType('select-customerType', 'CUSTOMER_TYPE');
			IQB.getDictListByDictType('select-customerStatus', 'CUSTOMER_STATUS');
			IQB.getDictListByDictType('select-holdWeixin', 'HOLD_WEIXIN');
			IQB.getDictListByDictType('select-businessType', 'BUSINESS_TYPE');  // add businessType 业务类型（帮帮手）
			IQB.getDictListByDictType('select-province', 'province');
//			IQB.getDictListByDictType('select-city', 'CUSTOMER_CITY');
			IQB.getDictListByDictType('select-riskManageType', 'RISK_TYPE');
			IQB.getDictListByDictType('select-isFather', 'IS_FATHER_CORP');
			IQB.getDictListByDictType('select-layer', 'LAYER');
			IQB.getDictListByDictType('select-overdueInterestModel', 'OVERDUE_MODEL');
			IQB.getDictListByDictType('select-corporateCertificateType', 'CORP_CERT_TYPE');
			IQB.getDictListByDictType('select-isVirtualMerc', 'IS_VIRTUAL_MERC');
			IQB.getDictListByDictType('select-belongsArea', 'BELONGS_AREA');
			//新增字段
			IQB.getDictListByDictType('riskFlag', 'yerOrno');
			IQB.getDictListByDictType('riskCode', 'risk_code');
			$('#query-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "请选择客户类型"});
			$('#select-customerStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-holdWeixin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-businessType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-province').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-city').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-riskManageType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-isFather').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-layer').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-overdueInterestModel').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-corporateCertificateType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-isVirtualMerc').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#riskFlag').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#risk_code').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		initBtnClick: function(){
			$('#btn-check').click(_this.extFunc.checkCustomerInfo);
			//$('#btn-update').off('click').on('click', function(){_this.extFunc.updateCustomerInfo();});
			$('#btn-pushToXFJR').click(_this.extFunc.pushToXFJR);
			$('#btn-deletePush').click(_this.extFunc.deletePush);
			$('#img').on('change', function() {
				var imgName = $('#img').val();
				if (imgName) {
					_this.extFunc.uploadImg(1);
				}
			});
			$('#btn-uploadCompony').on('click', function(){
				$('#imgType').val(1);
				_this.extFunc.updateFormSub();	
			});
			$('#btn-uploadCorporate').on('click', function(){
				$('#imgType').val(2);
				_this.extFunc.updateFormSub();	
			});
			
		},
		initOthers: function(){
//			_this.extFunc.imgUpload();
//			_this.extFunc.uploadImg();
		},
		parseCustomerType : function(val, dictType) {
			var req_data = {'dictTypeCode': dictType};
			var ret = '';
			if(_this.cache.customerTypeArr == undefined){
				IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
					var customerTypeArr = result.iqbResult.result;
					_this.cache.customerTypeArr = result.iqbResult.result;
				})
			}
			$.each(_this.cache.customerTypeArr, function(key, retVal) {
				if(val.indexOf(retVal.id)>=0){
					if(ret != ''){
						ret = ret + '，';
					}
					ret = ret + retVal.text;
				}
			});
			return ret;
		},
		parseCustomerStatus : function(val, dictType) {
			var req_data = {'dictTypeCode': dictType};
			var ret = '';
			if(_this.cache.customerStatusArr == undefined){
				IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
					var customerTypeArr = result.iqbResult.result;
					_this.cache.customerStatusArr = result.iqbResult.result;
				})
			}
			$.each(_this.cache.customerStatusArr, function(key, retVal) {
				if(val == retVal.id){
					ret = retVal.text;
				}
			});
			return ret;
		},
		initLevelSelect: function(){
			$('#select-province').change(_this.extFunc.provinceSelect);
		}
	}
	return _this;
}();

$(function(){
	/** 初始化表格  **/
	IQB.enterprisecustomers.init();
	//为模态框添加滚动条
	$('#update-win').find('.modal-form').mCustomScrollbar({setHeight: 500});
});	


