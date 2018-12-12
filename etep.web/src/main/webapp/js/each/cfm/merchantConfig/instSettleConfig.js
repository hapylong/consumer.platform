$package('IQB.instSettleConfigure');
IQB.instSettleConfigure = function(){
	var _grid = null;
	var _tree = null;
    var merchantFlag= 1;
    var showDataHtml="";
	var _this = {
		cache :{
			page:0,
			i:0
		},
		config: {
			action: {//页面请求参数
  				insert: urls['cfm'] + '/instSettleConfig/add',
  				update: urls['cfm'] + '/instSettleConfig/mod',
  				getById: urls['cfm'] + '/instSettleConfig/queryById',
  				move : urls['cfm'] + '/merchantBizType/getMerchantBizTypeList',
  				selDictItem : urls['cfm'] + '/product/finance_planid/selDictItem',
  				start : urls['cfm'] + '/instSettleConfig/1/update_status',
  				forbidden : urls['cfm'] + '/instSettleConfig/2/update_status'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				close:function(){
					$("#menuContentModel").hide();
					_grid.handler.close();
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/instSettleConfig/query',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   				$('#checkAll').prop('checked',false);
	   			}
			} 
		},
		addPlan : function(){
			$("#btn-insert").click(function(){
				  $('#update-win').modal({backdrop: 'static', keyboard: false, show: true});	
				  $('.modal-backdrop').addClass('z-index2');
				  $('#update-win').addClass("z-index");
				  $("#btn-save").unbind().click(function(){
                        var merchantNo = $(".merchModel").val();
                        var startDate = $("#startDate2").val();
                        var description = $('#description').val();
                        var bizType = $("#bizType").val();
                        var orderDate = $('#orderDate').val();
                        var flag = $('#flag').val();
                        if(merchantNo == ''){
                            IQB.alert('请选择商户');
                            return false;
                        }else if(bizType == null){
                            IQB.alert('请选择业务类型');
                            return false;
                        }else if(startDate == '' && orderDate == ''){
                            IQB.alert('请选择订单时间或者订单分期时间');
                            return false;
                        }else if(flag == ''){
                        	 IQB.alert('请选择是否手动划扣');
                             return false;
				  		}; 
				  		var data = {
                        		'startDate' : $("#startDate2").val(),
                        		'merchantNo' : $('.merchModel').val(),
                        		'description' : description,
                        		'bizType':bizType,
                        		'orderDate':orderDate,
                        		'flag':flag
                        	};
                        	IQB.post(_this.config.action.insert, data, function(result) {
				            	if (result.success=="1") {
                            		IQB.alert('商户代扣添加成功');
                            		_grid.handler.close();
						        	_this.refresh();
						    	};
			            	})
				  });
			});
		},
		updatePlan:function(){
			$("#btn-update").click(function(){
				var rows = $("#datagrid").datagrid2('getCheckedRows');
				if(rows.length > 0){
					if(rows.length > 1){
						  IQB.alert('请选中单行');
					}else{
						  IQB.get(_this.config.action.getById, {'id':rows[0].id}, function(result){
							  $('#update-win').modal({backdrop: 'static', keyboard: false, show: true});
							  $('.modal-backdrop').addClass('z-index2');
							  $('#update-win').addClass("z-index");
							  $('#updateForm').form('load', result.iqbResult.result);
							  _this.cache.bizType = result.iqbResult.result.bizType;
							  $('#startDate2').val(result.iqbResult.result.startDate);
							  $('#merchantNo').val(result.iqbResult.result.merchantName);
							  //该商户的业务类型
							  IQB.get(_this.config.action.move, {'merchantNo':result.iqbResult.result.merchantName}, function(result){
								    merchantFlag=0;
			                    	if (result.success=="1") {
			                    		var showDataHtmlUpdate = '';
			                    		for(var i=0;i<result.iqbResult.result.length;i++){                      			
			                    			showDataHtmlUpdate +="<option value='"+result.iqbResult.result[i].bizType+"'>"+result.iqbResult.result[i].name+"</option>"
			        	    			}
			                    		$('#bizType option').remove();
			        		 			$("#bizType").append(showDataHtmlUpdate);
										$("#bizType").val(_this.cache.bizType);
			        		 		};
							  })
						  })
						  $("#btn-save").unbind().click(function(){
							  var merchantNo = $(".merchModel").val();
		                      var startDate = $("#startDate2").val();
		                      var description = $('#description').val();
		                      var bizType = $("#bizType").val();
		                      var orderDate = $('#orderDate').val();
		                      var flag = $('#flag').val();
							  if(merchantNo == ''){
		                            IQB.alert('请选择商户');
		                            return false;
		                        }else if(bizType == null){
		                            IQB.alert('请选择业务类型');
		                            return false;
		                        }else if(startDate == '' && orderDate == ''){
		                            IQB.alert('请选择订单时间或者订单分期时间');
		                            return false;
		                        }else if(flag == ''){
		                        	 IQB.alert('请选择是否手动划扣');
		                             return false;
						  		}; 
							    var option = {
							    		'merchantNo' : $('.merchModel').val(),
			                    		'startDate' : $("#startDate2").val(),
			                      		'description' : $('#description').val(),
			                      		'id' : rows[0].id,
			                      		'bizType':$('#bizType').val(),
		                        		'orderDate':$('#orderDate').val(),
		                        		'flag':$('#flag').val()
			                      };
			                      IQB.post(_this.config.action.update, option, function(result) {
						            	if (result.success=="1") {
				                  		    IQB.alert('修改成功');
				                  		    _grid.handler.close();
								        	_this.refresh();
								    	};
					              });
						  })
					}
				}else{
					IQB.alert('未选中行');
				}
			})
		},
		move : function(){
            $("#bizType").click(function(){       
            	var merchantNo = $(".merchModel").val();
            	if(merchantNo == ''){
                      IQB.alert('请选择商户');
            	}else if (merchantFlag){
                    IQB.get(_this.config.action.move, {'merchantNo':merchantNo}, function(result){
                    	merchantFlag=0;
                    	if (result.success=="1") {
                    		for(var i=0;i<result.iqbResult.result.length;i++){                      			
        	        			 showDataHtml +="<option value='"+result.iqbResult.result[i].bizType+"'>"+result.iqbResult.result[i].name+"</option>"
        	    			}
        		 			$("#bizType").append(showDataHtml);
        		 		};
			        });
            	}
            });  
		},
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/instSettleConfig/query',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page})	
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
				IQB.confirm('确定要启用这些商户代扣配置吗？', function(){
					IQB.post(_this.config.action.start, data, function(result) {
						if(result.success == '1'){
							IQB.alert('商户代扣配置已启用！');
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
				IQB.confirm('确定要禁用这些商户代扣配置吗？', function(){
					IQB.get(_this.config.action.forbidden, data, function(result) {
						if(result.success == '1'){
							IQB.alert('商户代扣配置已禁用！');
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
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.addPlan();
			_this.updatePlan();
			_this.move();
			_this.checkAll();
			$(".merchModel").click(function(){
        	    merchantFlag=1;
                showDataHtml="";
                $('#bizType option').remove();
            });
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
	IQB.instSettleConfigure.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(startDate2);
	datepicker(orderDate);
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