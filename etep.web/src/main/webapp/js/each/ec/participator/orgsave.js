$package('IQB.orgsave');
IQB.orgsave = function(){
	var _grid = null;
	var _isUpload = "FALSE";
	var _this = {
		cache: {},
		initOrgSelect: function() {
			IQB.post(_this.config.action.getOrgInfo, {}, function(result){
				_this.cache.result = result.iqbResult.result;
				$('#search-orgId').select2({theme: 'bootstrap', placeholder: '请选择机构', allowClear: true, data: _this.cache.result}).val(null).trigger("change");
				$('#update-orgId').select2({theme: 'bootstrap', data: _this.cache.result})
			})
		},
		messageSuccess : function(msg) {
			$("#ec_signer_update_message").html("<div style='color:green'>" + msg + "</div>");
			$("#message").show().fadeOut(3000);
		},
		messageFail : function(msg) {
			$("#ec_signer_update_message").html("<div style='color:red'>" + msg + "</div>");
			$("#message").show().fadeOut(3000);
		},
		updateSignatureStamp : function() {	
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			$("#ec_signer_name_update").val(rows[0].customerName);
			$('#signature-update-label').text('上传签名图章');
			$('#signature-update').modal({backdrop: 'static', keyboard: false, show: true});
			
			$("#btn-signature-update-label").click(function(){
				if(_isUpload != "TRUE") {
					_this.messageFail("请先上传图片");
					return;
				}
				var data = {
						"ec_signer_img_type_update" : $("#ec_signer_img_type_update").val(),
						"is_default_signer_img_update" : $("#is_default_signer_img_update").val(),
						"ec_signer_img_name_update" : $("#ec_signer_img_name_update").val(),
						"ec_signer_pic_name_update" : $("#ec_signer_pic_name_update").val(),
						"sign_type" : "2,3",
						"ecSignerCode" : rows[0].ecSignerCode
				}
				IQB.post(urls['rootUrl'] + '/ec_participant/update_group_by_type', data, function(result,status){
					if(result.success == 1) {
						_this.signatureUpdateLabelClose();
					} else {
						_this.messageFail(result.retUserInfo);
					}
				})
			});
		},
		signatureUpdateLabelClose : function() {
			$('#signature-update').modal('hide');
			$("#ec_signer_img_type_update").val("");
			$("#is_default_signer_img_update").val("");
			$("#ec_signer_img_name_update").val("");
			$("#ec_signer_pic_name_update").val("");
			_grid.handler.refresh();
			//$("#ec_signer_img_data_blob_update").html("");
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
		initPicStyle : function() {
			/*******   开始加载上传图片的配置     ********/
			_isUpload = "FALSE";
			var uploadExtraDataIdCard = {
			        "ecSignerCode" : $('#datagrid').datagrid2('getCheckedRows')[0].ecSignerCode,
			        "size": 1024
			};
			$("#ec_signer_img_data_blob_update").fileinput('refresh', {
			    uploadUrl: urls['rootUrl'] + '/ec_participant/persist_org_signature_stamp',
			    allowedFileExtensions : ['jpg', 'png', 'gif'],
			    overwriteInitial: false,
			    maxFileSize: 2097152,
			    maxFilesNum: 1,
			    showUpload:true,
			    showCaption:false,
			    dropZoneEnabled: false,
			    uploadExtraData: uploadExtraDataIdCard
			}).on("fileuploaded", function(event, data) {
				if(data.response.Status == "OK"){
			    	_isUpload = "TRUE";
			    	_this.messageSuccess("上传成功");
			    } else if(data.response.success == 2) {
			    	IQB.alert(data.response.retUserInfo);
			    }else {
					_this.messageFail("上传失败");
				};
			});
			/*******    结束加载上传图片的配置    ********/
		},
		saveUpdateSignatureStamp : function() {
		},
		config: {
			action: {//页面请求参数
				updateSignatureStamp : urls['rootUrl'] + '/ec_participant/persist_org_signature_stamp',
				getOrgInfo : urls['rootUrl'] + '/sysOrganizationRest/getAllOrgInfo'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				insert: function(){				
				},
				reset: function(){//重写save	
					_grid.handler.reset();
					$('#search-orgId').val(null).trigger('change');
					$('#query-customerType').val(null).trigger('change');
				}
			},
  			dataGrid: {//表格参数  				
  				url: urls['rootUrl'] + '/ec_participant/get_group_by_type',
				singleCheck: true,
	   			queryParams: {
	   				"sign_type": "2,3",
	   				"sign_state":"0"
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化表格相关
			_this.initOrgSelect();//初始化机构下拉	
			$('#name').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择状态', allowClear: true}).val(null).trigger("change");		
			$("#btn-signature-update").click(function(){
				_this.initPicStyle();
				_this.updateSignatureStamp()});
			$('#btn-signature-update-label-close').on('click', function(){
				_this.signatureUpdateLabelClose();
				window.location.reload(); 
			});
			IQB.getDictListByDictType('query-customerType', 'CUSTOMER_TYPE');
			$('#query-customerType').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.orgsave.init();
	//禁用浏览器右击菜单
	//document.oncontextmenu = function(){return false;}
});		


























