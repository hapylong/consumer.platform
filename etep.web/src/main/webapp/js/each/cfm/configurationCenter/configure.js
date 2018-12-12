$package('IQB.configure');
IQB.configure = function(){
	var _grid = null;
	var _tree = null;
    var flag= 1;
    var showDataHtml="";
	var _this = {
		cache :{
			page:0,
			i:0,
			orgCode:'',
			merchantNo:''
		},
		config: {
			action: {//页面请求参数
  				insert: urls['cfm'] + '/product/add',
  				update: urls['cfm'] + '/product/mod',
  				getById: urls['cfm'] + '/product/queryById',
  				remove: urls['cfm'] + '/product/del',
  				move : urls['cfm'] + '/merchantBizType/getMerchantBizTypeList',
  				selDictItem : urls['cfm'] + '/product/finance_planid/selDictItem',
  				start : urls['cfm'] + '/product/1/update_status',
  				forbidden : urls['cfm'] + '/product/2/update_status'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改方案');
					$('#update-win #merchantNo').attr("disabled",true);
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/product/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/product/query',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   				$('#checkAll').prop('checked',false)
	   			}
			} 
		},
		addPlan : function(){
			$("#btn-insert").click(function(){
				_this.cache.orgCode = $(".merchModel").attr('orgCode');
				_this.cache.merchantNo = $(".merchModel").attr('merchantNo');
                $('#update-win').addClass("z-index");
				  $('#update-win').show();
				  $('.Newtr').remove();
                  $(".modal-backdrop").show();
				  $(".modal-backdrop").addClass("z-index2");
				  $("#btn-save").unbind().click(function(){
                        var bizType = $("#bizType").val();
                        var merchantNo = $(".merchModel").val();
                  
                        if(merchantNo == ''){
                            IQB.alert('请选择商户');
                        }else if(bizType == null){
                            IQB.alert('请选择业务类型');
                        }else if($('#updateForm').form('validate')){
				  			var trLength = $("#tableDialog").find('tr').length-1;
                        	var list = [];
							for (var i = 0; i<trLength; i++) {         
							list.push({});
							}
							for (var i = 1; i<list.length+1; i++) {
							list[i-1].installPeriods = tableDialog.rows[i].cells[1].getElementsByTagName("INPUT")[0].value;
							list[i-1].downPaymentRatio = tableDialog.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;;
							list[i-1].marginRatio = tableDialog.rows[i].cells[3].getElementsByTagName("INPUT")[0].value;
							list[i-1].floatMarginRatio = tableDialog.rows[i].cells[4].getElementsByTagName("INPUT")[0].value;
							list[i-1].serviceFeeRatio = tableDialog.rows[i].cells[5].getElementsByTagName("INPUT")[0].value;
							list[i-1].feeYear = tableDialog.rows[i].cells[6].getElementsByTagName("INPUT")[0].value;
							list[i-1].upInterestFee = tableDialog.rows[i].cells[7].getElementsByTagName("INPUT")[0].value;
							list[i-1].takePayment = tableDialog.rows[i].cells[8].getElementsByTagName("SELECT")[0].value;
							list[i-1].feeRatio = tableDialog.rows[i].cells[9].getElementsByTagName("INPUT")[0].value;
							list[i-1].planId = tableDialog.rows[i].cells[10].getElementsByTagName("SELECT")[0].value;
							list[i-1].remark = tableDialog.rows[i].cells[11].getElementsByTagName("INPUT")[0].value;
							list[i-1].reCharge = tableDialog.rows[i].cells[12].getElementsByTagName("INPUT")[0].value;
							list[i-1].greenChannel = tableDialog.rows[i].cells[13].getElementsByTagName("SELECT")[0].value;
							}
                        	var data = {
                        		list : list,
                        		orgCode: $(".merchModel").attr('orgCode'),
                        		merchantNo: $(".merchModel").attr('merchantNo'),
                        		merchNames : $('.merchModel').val(),
                        		bizType : $('#bizType').val(),
                        	};
                        	IQB.post(_this.config.action.insert, data, function(result) {
				            	if (result.iqbResult.result=="succ") {
                            		IQB.alert('方案添加成功');
						        	$("#update-win").hide();
						        	$(".modal-backdrop").hide();
						        	$("#menuContentModel").hide();
						        	_this.refresh();
						    	};
			            	})
				  		};  
				  });

                  $(".merchModel").click(function(){
                        flag=1;
                        showDataHtml="";
                        $('#bizType option').remove();
                  });

				  $("#btn-close").click(function(){
                        $(".Newtr").remove();
                        $("#bizType").find('option').remove();
					   $("#menuContentModel").hide();
					   $("#update-win").hide();
				  });
			});
		},
		move : function(){
            $("#bizType").click(function(){       
            	var merchantNo = $(".merchModel").val();
            	if(merchantNo == ''){
                    console.log(merchantNo)
                      IQB.alert('请选择商户');
            	}else if (flag){
                    IQB.get(_this.config.action.move, {'merchantNo':merchantNo}, function(result){
                        flag=0;
                    	if (result.success=="1") {
                    		for(var i=0;i<result.iqbResult.result.length;i++){                      			
        	        			 showDataHtml +="<option value='"+result.iqbResult.result[i].bizType+"'>"+result.iqbResult.result[i].name+"</option>"
        	    			}
        		 			$("#bizType").append(showDataHtml);
        		 			var bizType = $("#bizType").val();
        		 			
        		 			if((bizType=="8002")||(bizType=="8003")||(bizType=="8004")){
                            	$("#installPeriods").validatebox({validType:['intz']});
                            }else{
                            	$("#installPeriods").validatebox({validType:['int']});
                            };
        		 		};
			        });
            	}
            });  
		},
        selDictItem : function(){
        	IQB.get(_this.config.action.selDictItem, {}, function(result){
        		var result = result.iqbResult.result;
        		var option = '';
        		for(var i=0;i<result.length;i++){
        			option += '<option value='+result[i].dictValue+'>'+result[i].dictName+'</option>'
        		}
        		$('#planId'+_this.cache.i).find('option').remove();
        		$('#planId'+_this.cache.i).append(option);
        	})
        },
        morePlan : function(){
            $(".removePlanBox").click(function(){
                $("input[name='test']:checked").each(function() {
                    n= $(this).parents("tr").index();
                    m=n+1;
                    $("#tableDialog").find("tr:eq("+m+")").remove();
                });
            });
        	$(".addPlanBox").click(function(){
				    	var bizType = $("#bizType").val();
						if((bizType=="8002")||(bizType=="8003")||(bizType=="8004")){
							_this.cache.i = _this.cache.i+1;
							appendTr= "";
							appendTr="<tr class='Newtr'><td><input type='checkbox' name='test'></td><td><input id='installPeriods' name='installPeriods' type='text' class='form-control input-sm easyui-validatebox' placeholder='期数' maxlength='2' data-options=\"required:true, validType:['intz']\"></td><td><input id='downPaymentRatio' name='downPaymentRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='首付比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='marginRatio' name='marginRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='保证金比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td class='floatMarginRatioBox'><input id='floatMarginRatio' name='floatMarginRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='上浮保证金比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='serviceFeeRatio'   name='serviceFeeRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='服务费比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='feeYear' name='feeYear' type='text' class='form-control input-sm easyui-validatebox' placeholder='上收息月数' maxlength='2' data-options=\"required:true, validType:['intz','maxLength[2]']\"></td><td><input id='upInterestFee' name='upInterestFee' type='text' class='form-control input-sm easyui-validatebox' placeholder='上收息利率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='takePayment' name='takePayment' type='text' class='form-control'><option value='1' selected>是</option><option value='0'>否</option></select></td><td><input id='feeRatio' name='feeRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='月息率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='planId"+_this.cache.i+"' name='planId' type='text' class='form-control'></select></td><td><input id='remark' name='remark' type='text' class='form-control input-sm' placeholder='请输入描述' maxlength='20'></td><td><input id='reCharge' name='reCharge' type='text' class='form-control input-sm easyui-validatebox' placeholder='充值费率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='greenChannel' name='greenChannel' type='text' class='form-control'><option value='1' selected>非绿色通道</option><option value='2'>绿色通道</option></select></td></tr>"
								$(".modal-form table").append(appendTr);
							    	_this.selDictItem();
							    	$.parser.parse();
	                    }else{
	                    	_this.cache.i = _this.cache.i+1;
	        				appendTr= "";
	        				appendTr="<tr class='Newtr'><td><input type='checkbox' name='test'></td><td><input id='installPeriods' name='installPeriods' type='text' class='form-control input-sm easyui-validatebox' placeholder='期数' maxlength='2' data-options=\"required:true, validType:['int']\"></td><td><input id='downPaymentRatio' name='downPaymentRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='首付比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='marginRatio' name='marginRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='保证金比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td class='floatMarginRatioBox'><input id='floatMarginRatio' name='floatMarginRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='上浮保证金比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='serviceFeeRatio'   name='serviceFeeRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='服务费比例' maxlength='5' data-options=\"required:true, validType:['moneyTwo','maxLength[5]']\"></td><td><input id='feeYear' name='feeYear' type='text' class='form-control input-sm easyui-validatebox' placeholder='上收息月数' maxlength='2' data-options=\"required:true, validType:['intz','maxLength[2]']\"></td><td><input id='upInterestFee' name='upInterestFee' type='text' class='form-control input-sm easyui-validatebox' placeholder='上收息利率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='takePayment' name='takePayment' type='text' class='form-control'><option value='1' selected>是</option><option value='0'>否</option></select></td><td><input id='feeRatio' name='feeRatio' type='text' class='form-control input-sm easyui-validatebox' placeholder='月息率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='planId"+_this.cache.i+"' name='planId' type='text' class='form-control'></select></td><td><input id='remark' name='remark' type='text' class='form-control input-sm' placeholder='请输入描述' maxlength='20'></td><td><input id='reCharge' name='reCharge' type='text' class='form-control input-sm easyui-validatebox' placeholder='充值费率' maxlength='6' data-options=\"required:true, validType:['moneyThree','maxLength[6]']\"></td><td><select id='greenChannel' name='greenChannel' type='text' class='form-control'><option value='1' selected>非绿色通道</option><option value='2'>绿色通道</option></select></td></tr>"
	        					$(".modal-form table").append(appendTr);
	        				    	_this.selDictItem();
	        				    	$.parser.parse();
	                    };
					}); 
        },
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/product/query',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
			$('#checkAll').prop('checked',false);
	    },
        btnStart : function(){
        	var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				var idArr = [];
				var data = {'id':idArr};
				for(var i=0;i<rows.length;i++){
					idArr.push(rows[i].id);
				}
				IQB.confirm('确定要启用这些产品方案吗？', function(){
					IQB.post(_this.config.action.start, data, function(result) {
						if(result.iqbResult.result == 'success'){
							IQB.alert('产品方案已启用！');
							_this.refresh();
						}
					})
				});
			}else{				
				IQB.alert("未选中行");
			}
        },
        btnForbidden : function(){
        	var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				var idArr = [];
				var data = {'id':idArr};
				for(var i=0;i<rows.length;i++){
					idArr.push(rows[i].id);
				}
				IQB.confirm('确定要禁用这些产品方案吗？', function(){
					IQB.get(_this.config.action.forbidden, data, function(result) {
						if(result.iqbResult.result == 'success'){
							IQB.alert('产品方案已禁用！');
							_this.refresh();
						}
					})
				});
			}else{				
				IQB.alert("未选中行");
			}
        },
        checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		$('#datagrid').datagrid2('checkAll');
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
        feeYearBlur:function(){
        	$("#tableDialog").delegate("input[name='feeYear']","blur",function(){
        		var feeYear = $(this).val();
        		var _that = $(this);
            	if(feeYear == 0){
            		_that.parents('tr').find("#upInterestFee").attr('readonly','readonly').val('0');
            	}else{
            		_that.parents('tr').find("input[name='upInterestFee']").removeAttr('readonly');
            	}
        	});
        },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.morePlan();
			_this.addPlan();
			_this.move();
			_this.selDictItem();
			_this.checkAll();
			_this.feeYearBlur();
			$("input[name='feeYear']").on('blur',function(){_this.feeYearBlur()});
			//启用
			$('#btn-start').click(function(){
				_this.btnStart();
			});
			//禁用
			$('#btn-forbidden').click(function(){
				_this.btnForbidden();
			});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.configure.init();
});		