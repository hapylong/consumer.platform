$package('IQB.customerstoreinfo');
IQB.customerstoreinfo = function(){
	var _box = null;
    var _list = null;
    var imgUrl = '';
	var _this = {
		cache:{
			i:1,
			customerCode:'',
			requestMessage:'',
			valArr : []
		},
		config: {
			action: {
  				update: urls['sysmanegeUrl'] + '/customerStore/updateCustomerStoreInfo',
  				getById: urls['sysmanegeUrl'] + '/customerStore/getCustomerStoreInfoByCode',
  				getCustomerInfoByCustomerType: urls['sysmanegeUrl'] + '/customer/getCustomerInfoByCustomerType',
  			},
			event: {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#query-belongsArea').val(null).trigger('change');
				},
				update: function(){//重写update
					//_this.extFunc.update();
				},
			},
  			dataGrid: {
  				url: urls['cfm'] + '/customerStore/getCustomerStoreInfoList',
  				singleCheck: true
			}
		},
		extFunc:{
			checkCustomerStoreInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	_this.cache.customerCode = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){	
			    		//$("#updateForm")[0].reset();  
			    		$("#updateForm").form('load',result.iqbResult.result.cb);
			    		//展示债权人信息
			    		var requestMessage = result.iqbResult.result.icsi;
			    		_this.cache.requestMessage = result.iqbResult.result.icsi;
		    		    $('.newTr').remove();
		    		    appendTr= "";
		    			for(var i=0;i<requestMessage.length;i++){
		    				//第一行信息
		    				if(i == 0){
		    					//$('#creditorName').val(requestMessage[i].creditorName);
		    					$('#creditorIdNo').val(requestMessage[i].creditorIdNo);
		    					$('#select-creditorBankName').val(requestMessage[i].creditorBankName);
		    					$('#creditorBankNo').val(requestMessage[i].creditorBankNo);
		    					$('#creditorPhone').val(requestMessage[i].creditorPhone);
		    					$('#flag').find('option').remove();
		    					$('#flag').append("<option value='1' selected>是</option><option value='0'>否</option>");
		    					//隐藏
		    				    $('.PlanBox').hide();
		    				    $("input").attr("disabled",true);
		    			    	$("select").attr("disabled",true);
		    			    	$("#btn-save2").hide();
		    			    	$('.checkTd').hide();
		    				}else{
		    					appendTr="<tr class='newTr'><td class='checkTd'></td>"+
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
		    				    //隐藏
		    				    $('.PlanBox').hide();
		    				    $("input").attr("disabled",true);
		    			    	$("select").attr("disabled",true);
		    			    	$("#btn-save2").hide();
		    			    	$('.checkTd').hide();
		    				}
		    			} 
			    		$('#select-guaranteeCorporationName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$('#select-creditorBankName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    	    $("#select-creditorBankName"+(i)).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
					})
			    	$("#btn-close").click(function(){
			    		$('.PlanBox').show()
			    		$("input").removeAttr("disabled");
			    		$("select").removeAttr("disabled");
			    		$("#btn-save2").show();
			    		$('.checkTd').show();
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
					var obj = {};
					obj.id = '';
					obj.text = '请选择';
					arry.push(obj);
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
					var option = {};
			    	option['customerCode'] = records[0].customerCode;
			    	_this.cache.customerCode = records[0].customerCode;
			    	IQB.getById(_this.config.action.getById, option, function(result){	
			    		//$("#updateForm")[0].reset();  
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
		    					//$('#creditorName').val(requestMessage[i].creditorName);
		    					$('#creditorIdNo').val(requestMessage[i].creditorIdNo);
		    					$('#select-creditorBankName').val(requestMessage[i].creditorBankName);
		    					$('#creditorBankNo').val(requestMessage[i].creditorBankNo);
		    					$('#creditorPhone').val(requestMessage[i].creditorPhone);
		    					$('#flag').find('option').remove();
		    					$('#flag').append("<option value='1' selected>是</option><option value='0'>否</option>");
		    				}else{
		    					appendTr="<tr class='newTr'><td><input type='checkbox' name='test'></td>"+
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
					});
					$("#btn-close").click(function(){
					});
					$("#btn-save2").unbind('click').click(function(){
						//判断有几个默认债权人
						_this.cache.valArr = [];
						for(var i=0;i<$("select[option='flag']").length;i++){
							if($($("select[option='flag']")[i]).val() == '1'){
								_this.cache.valArr.push($($("select[option='flag']")[i]).val());
							}
						}
						if(_this.cache.valArr.length > 1){
							IQB.alert('只能选择一个默认债权人！');
							return false;
						}else if(_this.cache.valArr.length ==  0){
							IQB.alert('请至少选择一个默认债权人！');
							return false;
						}
						//判断必填
						for(var i=0;i<$('.validatebox').length;i++){
							if($($('.validatebox')[i]).val() == ''){
								IQB.alert('债权人信息不完善，无法保存！');
								return false;
							}
						}
						var trLength = $("#tableDialog").find('tr').length-1;
			        	var list = [];
						for (var i = 0; i<trLength; i++) {         
						list.push({});
						}
						for (var i = 1; i<list.length+1; i++) {
						list[i-1].creditorName = tableDialog.rows[i].cells[1].getElementsByTagName("INPUT")[0].value;
						list[i-1].creditorIdNo = tableDialog.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;
						list[i-1].creditorBankName = tableDialog.rows[i].cells[3].getElementsByTagName("SELECT")[0].value;
						list[i-1].creditorBankNo = tableDialog.rows[i].cells[4].getElementsByTagName("INPUT")[0].value;
						list[i-1].creditorPhone = tableDialog.rows[i].cells[5].getElementsByTagName("INPUT")[0].value;
						list[i-1].flag = tableDialog.rows[i].cells[6].getElementsByTagName("SELECT")[0].value;
						}
						if($('#updateForm').form('validate')){
							var data = {};
							data.customerCode = _this.cache.customerCode;
							data.customerName = $("input[name='customerName']").val();
							data.customerShortName = $("input[name='customerShortName']").val();
							data.province = $("input[name='province']").val();
							data.city = $("input[name='city']").val();
							data.higherOrgName = $("input[name='higherOrgName']").val();
							data.belongsArea = $("input[name='belongsArea']").val();
							data.guaranteeCorporationName = $("select[name='guaranteeCorporationName']").val();
							data.guaranteeCorporationCode = $("input[name='guaranteeCorporationCode']").val();
							data.requestMessage = list;
							IQB.post(urls['sysmanegeUrl'] + '/customerStore/updateCustomerStoreInfo', data, function(result) {
						       $("#update-win").modal('hide');
			            	})
						}
					});
				}
			},
		},
		morePlan : function(){
            $(".removePlanBox").click(function(){
            	var length = $("input[name='test']:checked").length;
            	if(length > 0){
            		IQB.confirm('确定要移除该行吗？',function(){
                		$("input[name='test']:checked").parents("tr").remove();
                	});
            	}else{
            		IQB.alert('未选中');
            	}
            });
        	$(".addPlanBox").click(function(){
        		IQB.confirm('确定要添加一行吗？',function(){
        			_this.cache.i = _this.cache.i+1;
    				appendTr= "";
    				appendTr="<tr class='newTr'><td><input type='checkbox' name='test'></td>"+
    				              "<td><input name='creditorName' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人姓名' maxlength='50'/></td>"+
    				              "<td><input name='creditorIdNo' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人身份证号' maxlength='18' /></td>"+
    				              "<td><select id='select-creditorBankName"+_this.cache.i+"' name='creditorBankName' required='required' class='form-control input-sm easyui-validatebox validatebox'></select></td>"+
    				              "<td><input name='creditorBankNo' type='text' class='form-control input-sm easyui-validatebox validatebox' required='required' placeholder='请输入债权人银行卡号' maxlength='20' /></td>"+
    				              "<td><input name='creditorPhone' type='text' class='form-control input-sm easyui-validatebox validatebox' placeholder='请输入债权人手机号' required='required' maxlength='11' /></td>"+
    				              "<td><select id='creditorFlag"+_this.cache.i+"' name='creditorFlag"+_this.cache.i+"' option='flag' class='form-control input-sm easyui-validatebox'></select></td>"
    				         +"</tr>"
    					$(".modal-form table").append(appendTr);
    				    IQB.getDictListByDictType('select-creditorBankName'+_this.cache.i, 'BANK_NAME');    
    				    $("#select-creditorBankName"+_this.cache.i).select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
    				    _this.initFlag('creditorFlag'+_this.cache.i);
        		})
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
			this.morePlan();
			//修改
			$('#btn-update').click(function(){
				_this.extFunc.update();
			});
		},
		initSelect: function(){
			IQB.getDictListByDictType('query-province', 'CUSTOMER_PROVINCE');
			IQB.getDictListByDictType('query-belongsArea', 'BELONGS_AREA');
			IQB.getDictListByDictType('select-creditorBankName', 'BANK_NAME');
			$('#query-province').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query-belongsArea').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-creditorBankName').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.initFlag('flag');
			_this.extFunc.initGuaranteeCorporationNameSelect();	
			_this.extFunc.initGuaranteeCorporationNameIpt();	
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
		initBtnClick: function(){
			$('#btn-check').click(_this.extFunc.checkCustomerStoreInfo);
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
	IQB.customerstoreinfo.init();
});	


