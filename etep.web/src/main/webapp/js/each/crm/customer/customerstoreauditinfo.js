$package('IQB.customerstoreauditinfo');
IQB.customerstoreauditinfo = function(){
	var _box = null;
    var _list = null;
    var imgUrl = '';
	var _this = {
		cache:{},
		config: {
			action: {
  				update: urls['sysmanegeUrl'] + '/customerStore/updateCustomerStoreAuditInfo',
  				getById: urls['sysmanegeUrl'] + '/customerStore/getCustomerStoreInfoByCode',
  				getCustomerInfoByCustomerType: urls['sysmanegeUrl'] + '/customer/getCustomerInfoByCustomerType',
  			},
			event: {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#query-belongsArea').val(null).trigger('change');
					$('#query-status').val(null).trigger('change');
				},
				update: function(){//重写update
					_this.extFunc.update();
				},
			},
  			dataGrid: {
  				url: urls['cfm'] + '/customerStore/getCustomerStoreInfoList',
  				singleCheck: true,
  				onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   			}
			}
		},
		extFunc:{
			checkCustomerStoreAuditInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){		
			    		$("#updateForm").form('load',result.iqbResult.result.cb);
			    		//展示债权人信息
			    		var requestMessage = result.iqbResult.result.icsi;
			    		_this.cache.requestMessage = result.iqbResult.result.icsi;
		    		    $('.newTr').remove();
		    		    appendTr= "";
		    			for(var i=0;i<requestMessage.length;i++){
		    				//第一行信息
		    				if(i == 0){
		    					$('#creditorIdNo').val(requestMessage[i].creditorIdNo);
		    					$('#select-creditorBankName').val(requestMessage[i].creditorBankName);
		    					$('#creditorBankNo').val(requestMessage[i].creditorBankNo);
		    					$('#creditorPhone').val(requestMessage[i].creditorPhone);
		    					$('#flag').find('option').remove();
		    					$('#flag').append("<option value='1' selected>是</option><option value='0'>否</option>");
		    				}else{
		    					appendTr="<tr class='newTr'>"+
    				              "<td><input id='creditorName"+(i)+"' type='text' class='form-control input-sm easyui-validatebox' required='required' placeholder='请输入债权人姓名' validType='maxLength[50]' value='"+requestMessage[i].creditorName+"'/></td>"+
    				              "<td><input id='creditorIdNo"+(i)+"' type='text' class='form-control input-sm easyui-validatebox' required='required' placeholder='请输入债权人身份证号' validType='maxLength[50]' value='"+requestMessage[i].creditorIdNo+"'/></td>"+
    				              "<td><select id='select-creditorBankName"+(i)+"' required='required' class='form-control input-sm easyui-validatebox'><option value='"+requestMessage[i].creditorBankName+"' selected>"+requestMessage[i].creditorBankName+"</option></select></td>"+
    				              "<td><input id='creditorBankNo"+(i)+"' type='text' class='form-control input-sm easyui-validatebox' required='required' placeholder='请输入债权人银行卡号' validType='maxLength[50]' value='"+requestMessage[i].creditorBankNo+"'/></td>"+
    				              "<td><input id='creditorPhone"+(i)+"' type='text' class='form-control input-sm easyui-validatebox' placeholder='请输入债权人手机号' required='required' validType='maxLength[50]' value='"+requestMessage[i].creditorPhone+"'/></td>"+
    				              "<td><select id='creditorFlag"+(i)+"' name='creditorFlag"+(i)+"' option='flag'><option value='"+requestMessage[i].flag+"' selected>"+_this.formaterFlag(requestMessage[i].flag)+"</option></select></td>"
	    				         +"</tr>"
		    					$(".modal-form table").append(appendTr);
		    				    IQB.getDictListByDictType('select-creditorBankName'+(i), 'BANK_NAME'); 
		    				    $("#select-creditorBankName"+(i)).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		    				    _this.initFlag('creditorFlag'+(i));
		    				}
		    			} 
			    		$('#select-guaranteeCorporationName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$('#select-creditorBankName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    	    $("#select-creditorBankName"+(i)).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
			    		
			    		$("input").attr("disabled",true);
    			    	$("select").attr("disabled",true);
    			    	$("#btn-save2").hide();
			    		if($('#select-hasAuthed').val() == '1'){
			    			$('.ipt-authChannel').show();
			    		}
					});
			    	
			    	$("input").attr("disabled",true);
			    	$("select").attr("disabled",true);
			    	$("#btn-save2").css({ "display": "none" });
			    	$("#btn-close").click(function(){
			    		$("input").removeAttr("disabled");
			    		$("select").removeAttr("disabled");
			    		$("#btn-save").css({ "display": "initial" });
			    	});
				}
			},
			provinceSelect: function(){
				$("#select-city").empty();
				var val = '';
				if($("#select-province").val() == '北京市'){
					val = '[{"id":"请选择","text":"请选择"},{"id":"北京市","text":"北京市"}]';
					$('#select-city').select2({theme: 'bootstrap', data: JSON.parse(val)});
				}
				if($("#select-province").val() == '湖南省'){
					val = '[{"id":"请选择","text":"请选择"},{"id":"长沙市","text":"长沙市"}]';
					$('#select-city').select2({theme: 'bootstrap', data: JSON.parse(val)});
				}
			},
			initGuaranteeCorporationNameSelect: function() {
				IQB.post(urls['cfm'] + '/customer/getCustomerInfoByCustomerType', {customerType: 5}, function(result){
					var arry = [];
					$.each(result.iqbResult.result, function(i, n){
						var obj = {};
						obj.id = n.customerName;
						obj.text = n.customerName;
						arry.push(obj);
					});
					$('#select-guaranteeCorporationName').select2({theme: 'bootstrap', data: arry});
					//缓存
					_this.cache.guaranteeCorporationName = result.iqbResult.result;
				});	
			},
			initGuaranteeCorporationNameIpt: function() {
				$('#select-guaranteeCorporationName').change(function(e){
					var target = e.target;
					$.each(_this.cache.guaranteeCorporationName, function(i, n){
						if($(target).val() == n.customerName){
							$('#guaranteeCorporationCorName').val(n.corporateName);
							$('#guaranteeCorporationCode').val(n.customerCode);
						}
					});
				});
			},
			initSelectHasAuthedForIpt: function() {
				$('#select-hasAuthed').change(function(e){
					var target = e.target;
					if($(target).val() == '2'){
						$('.ipt-authChannel').hide();
					}
					if($(target).val() == '1'){
						$('.ipt-authChannel').show();
					}
				});
			},
			formaterStatus: function(val) {
				if(val == '1'){
					return '已审核';
				}
				if(val == '2'){
					return '未审核';
				}
				return '未审核';
			},
			update: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					if(records[0].creditorStatus == '1'){
						IQB.alert('此条记录已被审核,不允许操作');
						return false;
					}
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){	 
			    		$("#updateForm").form('load',result.iqbResult.result.cb);
			    		//展示债权人信息
			    		var requestMessage = result.iqbResult.result.icsi;
			    		_this.cache.requestMessage = result.iqbResult.result.icsi;
		    		    $('.newTr').remove();
		    		    appendTr= "";
		    		    _this.cache.i = requestMessage.length-1;
		    			for(var i=0;i<requestMessage.length;i++){
		    				//第一行信息
		    				if(i == 0){
		    					$('#creditorIdNo').val(requestMessage[i].creditorIdNo);
		    					$('#select-creditorBankName').val(requestMessage[i].creditorBankName);
		    					$('#creditorBankNo').val(requestMessage[i].creditorBankNo);
		    					$('#creditorPhone').val(requestMessage[i].creditorPhone);
		    					$('#flag').find('option').remove();
		    					$('#flag').append("<option value='1' selected>是</option><option value='0'>否</option>");
		    				}else{
		    					appendTr="<tr class='newTr'>"+
    				              "<td><input id='creditorName"+(i)+"' name='creditorName' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人姓名' maxlength='50' value='"+requestMessage[i].creditorName+"'/></td>"+
    				              "<td><input id='creditorIdNo"+(i)+"' name='creditorIdNo' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人身份证号' maxlength='18' value='"+requestMessage[i].creditorIdNo+"'/></td>"+
    				              "<td><select id='select-creditorBankName"+(i)+"' name='creditorBankName' required='required' class='form-control input-sm easyui-validatebox validatebox'><option value='"+requestMessage[i].creditorBankName+"' selected>"+requestMessage[i].creditorBankName+"</option></select></td>"+
    				              "<td><input id='creditorBankNo"+(i)+"' name='creditorBankNo' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人银行卡号' maxlength='20' value='"+requestMessage[i].creditorBankNo+"'/></td>"+
    				              "<td><input id='creditorPhone"+(i)+"' name='creditorPhone' type='text' class='form-control input-sm easyui-validatebox validatebox' placeholder='请输入债权人手机号' required='required' maxlength='11' value='"+requestMessage[i].creditorPhone+"'/></td>"+
    				              "<td><select id='creditorFlag"+(i)+"' name='creditorFlag"+(i)+"' option='flag'><option value='"+requestMessage[i].flag+"' selected>"+_this.formaterFlag(requestMessage[i].flag)+"</option></select></td>"
	    				         +"</tr>"
		    					$(".modal-form table").append(appendTr);
		    				    IQB.getDictListByDictType('select-creditorBankName'+(i), 'BANK_NAME'); 
		    				    $("#select-creditorBankName"+(i)).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		    				    _this.initFlag('creditorFlag'+(i));
		    				}
		    			} 
			    		$('#select-guaranteeCorporationName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$('#select-creditorBankName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    	    $("#select-creditorBankName"+(i)).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
			    		//隐藏
    				    $("input").attr("readonly",true);
    			    	$("select").attr("disabled",true);
    			    	$('#select-hasAuthed').removeAttr('disabled');
    			    	$('#authChannel').removeAttr('readonly');
			    		if($('#select-hasAuthed').val() == '1'){
			    			$('.ipt-authChannel').show();
			    		}
					});
					$("#btn-close").click(function(){
			    		$("input").removeAttr("disabled");
			    		$("select").removeAttr("disabled");
			    		$("#btn-save2").show();
			    		$('.ipt-authChannel').hide();
					});
					$("#btn-save2").unbind('click').click(function(){
						if($('#select-hasAuthed').val() == '1'){
							$('#authChannel').validatebox({ 
								required:true 
							});
						}
						if($('#updateForm').form('validate')){
							var data = {
							    'hasAuthed':$('#select-hasAuthed').val(),
							    'authChannel':$('#authChannel').val(),
							    'belongsArea' : $("input[name='belongsArea']").val(),
								'city' : $("input[name='city']").val(),
								'creditorBankName' : $("#select-creditorBankName").val(),
								'creditorBankNo' : $("#creditorBankNo").val(),
								'creditorIdNo' :$("#creditorIdNo").val(),
								'creditorName' : $("#creditorName").val(),
								'creditorPhone' : $("#creditorPhone").val(),
								'customerCode' : $("input[name='customerCode']").val(),
								'customerName' : $("input[name='customerName']").val(),
								'customerShortName' : $("input[name='customerShortName']").val(),
								'guaranteeCorporationCorName' : $("input[name='guaranteeCorporationCorName']").val(),
								'guaranteeCorporationName' : $("input[name='guaranteeCorporationName']").val(),
								'higherOrgName' : $("input[name='higherOrgName']").val(),
								'province' : $("input[name='province']").val(),
							};
							IQB.post(urls['sysmanegeUrl'] + '/customerStore/updateCustomerStoreAuditInfo', data, function(result) {
						       $("#update-win").modal('hide');
						       _this.refresh();
			            	})
						}
					})
				}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/customerStore/getCustomerStoreInfoList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page})	
			});
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
			IQB.getDictListByDictType('query-province', 'CUSTOMER_PROVINCE');
			IQB.getDictListByDictType('query-belongsArea', 'BELONGS_AREA');
			IQB.getDictListByDictType('select-creditorBankName', 'BANK_NAME');
			$('#query-province').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query-belongsArea').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-creditorBankName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			//$('#select-hasAuthed').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-authResult').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query-status').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.extFunc.initGuaranteeCorporationNameSelect();	
			_this.extFunc.initGuaranteeCorporationNameIpt();
			_this.extFunc.initSelectHasAuthedForIpt();
			_this.initFlag('flag');
		},
		initBtnClick: function(){
			$('#btn-check').click(_this.extFunc.checkCustomerStoreAuditInfo);
		},
		initFlag: function(selectId){
			val = '[{"id":"0","text":"否"},{"id":"1","text":"是"}]';
			$('#' + selectId).select2({theme: 'bootstrap', data: JSON.parse(val)});
		},
		formaterFlag: function(val) {
			if(val == '0'){
				return '否';
			}
			if(val == '1'){
				return '是';
			}
		},
		initOthers: function(){
		},
		parseCustomerType : function(val, dictType) {
		},initLevelSelect: function(){
			$('#query-province').change(_this.extFunc.provinceSelect);
		}
	}
	return _this;
}();

$(function(){
	/** 初始化表格  **/
	IQB.customerstoreauditinfo.init();
});	


