$package('IQB.assetInto');
IQB.assetInto = function(){
	var _grid = null;
	var _tree = null;
	var _this = {		
		cache :{
			amount : '',
			procBizId : '',
		    procBizMemo : '', 
		    procOrgCode : '',
		    eq:0,
		    eq2:0,
		    urlPostfix : '/business/listJYSOrderInfo',
		    count : -1
		},
		config: {
			action: {//页面请求参数
			    query: urls['cfm'] + '/business/listJYSOrderInfo',
  				update: urls['cfm'] + '/business/updateJYSOrderInfo',
  				getById: urls['cfm'] + '/business/getJysOrderInfo',
  				remove: urls['cfm'] + '/business/deleteJYSOrderInfo',
  				cal : urls['cfm'] + '/calOrderInfo',
  				exports : urls['cfm'] + '/assetXlsDownload'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/listJYSOrderInfo',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'status' : 1})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/listJYSOrderInfo',
	   			//singleCheck: true,
	   			queryParams: {
	   				'status' : 1
	   			},
	   			onPageChanged : function(page){
	   				_this.refresh(page);
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/business/listJYSOrderInfo',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page,'status':1,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
				onPageChanged : function(page){
	   				_this.refresh(page);
	   			}	
			});
		},
		plan : function(eq,eq2) {
			var totalAmount = $("#orderAmt").val();
			if(totalAmount != "" && totalAmount != 0){
				//期数
				var orderTerms = _this.cache.prodList[eq2].installPeriods ;
				$("#orderItems").val(orderTerms);
			}
			var option = {
					'planId' : eq,
					'orderAmt' : $('#orderAmt').val()
			}
			IQB.get(_this.config.action.cal, option, function(result){
				$("#monthInterest").val(result.iqbResult.result.monthInterest);
			})
		},
		update: function(){
			var records = _grid.util.getCheckedRows();
			if(_grid.util.checkSelectOne(records)){
				var option = {'orderId': records[0].orderId,'id':records[0].id};	
				//var option = {'orderId': 'HLJLD2002170313001'};
		    	IQB.get(_this.config.action.getById, option, function(result){	
		    		//计划
		    		_this.cache.prodList = result.iqbResult.prodList;
		    		/*回显方案*/
		    		$("#planId").find('option').remove();
		    		if(_this.cache.prodList.length > 0){
		    			for(var i=0;i<_this.cache.prodList.length;i++){
			    			$("#planId").append("<option  id='"+i+"' value='" + _this.cache.prodList[i].id + "'>"+ _this.cache.prodList[i].planFullName+ "</option>")
			    		}
		    		}
		    		//赋值
		    		$("#id").val(result.iqbResult.result.id);
		    		$("#userId").val(result.iqbResult.result.userId);
		    		$("#orderId").val(result.iqbResult.result.orderId);
		    		$("#batchNo").val(result.iqbResult.result.batchNo);
					var bizType = result.iqbResult.result.bizType;
					if(bizType == '2001'){
						$("#bizType").val('以租代售新车');
					}else if(bizType == '2002'){
						$("#bizType").val('以租代售二手车');
					}else if(bizType == '2100'){
						$("#bizType").val('抵押车');
					}else if(bizType == '2200'){
						$("#bizType").val('质押车');
					}else if(bizType == '1100'){
						$("#bizType").val('易安家');
					}else if(bizType == '1000'){
						$("#bizType").val('医美');
					}else if(bizType == '1200'){
						$("#bizType").val('旅游');
					}
					$("#merchantNo").val(result.iqbResult.result.merchantNo);
					$("#userName").val(result.iqbResult.result.realName);
					$("#regId").val(result.iqbResult.result.regId);
					$("#orderName").val(result.iqbResult.result.orderName);
					$("#orderAmt").val(result.iqbResult.result.orderAmt);
					$("#orderItems").val(result.iqbResult.result.orderItems);
					$("#monthInterest").val(result.iqbResult.result.monthInterest);
					$("#planId").val(result.iqbResult.result.planId);
		    		$('#update-win-label').text('修改订单');
		    		$('#update-win').modal({backdrop: 'static', keyboard: false, show: true});		
		    		//修改分期方案
			    	$("#planId").change(function(){
			    		_this.cache.eq = $('#planId').val();
			    		_this.cache.eq2 = $('#planId option:selected').attr('id');
			    		_this.plan(_this.cache.eq,_this.cache.eq2);
			    	});
		    		//重写订单修改的保存方法
		    		$("#btn-saveOrder").unbind().click(function(){
		    			var userName = $("#userName").val();
		    			var regId = $("#regId").val();
		    			var orderName = $("#orderName").val();
		    			if(userName == '' || regId == '' || orderName == ''){
		    				IQB.alert('订单信息不完善！');
		    			}
		    			var id = $("#id").val();
		    			var userId = $("#userId").val();
		    			var orderId = $("#orderId").val();
		    			var batchNo = $("#batchNo").val();
		    			var bizType = $("#bizType").val();
		    			var merchantNo = $("#merchantNo").val();
		    			var orderAmt = $("#orderAmt").val();
		    			var orderItems = $("#orderItems").val();
		    			var monthInterest = $("#monthInterest").val();
		    			var planId = $("#planId").val();
		    			var option = {};
		    			option.id = id;
		    			option.userId = userId;
		    			option.orderId = orderId;
		    			option.realName = userName;
		    			option.regId = regId;
		    			option.orderName = orderName;
		    			option.batchNo = batchNo;
		    			//option.bizType = bizType;
		    			option.merchantNo = merchantNo;
		    			option.orderName = orderName;
		    			option.orderAmt = orderAmt;
		    			option.orderItems = orderItems;
		    			option.monthInterest = monthInterest;
		    			option.planId = planId;
		    			IQB.post(_this.config.action.update, option, function(result){
		    				//返回信息
		    				if(result.success == 1){
		    					$('#update-win').modal('hide');
		    					_this.refresh();
		    				}else{
		    					IQB.alert(result.msg);
		    				}
		    			})	
		    		});
				});
			}					
		},
		remove: function(){
			var rows = _grid.util.getCheckedRows();				
			if (_grid.util.checkSelect(rows)){
				IQB.confirm('确认要删除吗？', function(){
					var orderIdArr = [];
					for(var i=0;i<rows.length;i++){
						orderIdArr.push(rows[i]['id']);
					}
					var option = {
							'ids' : orderIdArr
					}
			   		IQB.remove(_this.config.action.remove, option, function(result){		
			   		    //表格url
			   			_this.refresh();
			   		});
				}, function(){});										
			}
		},
		ajaxFileUpload : function() {
			$.ajaxFileUpload
            (
                {
                    url:urls['cfm'] + '/assetXlsImport', //用于文件上传的服务器端请求地址
                    secureuri: true, //是否需要安全协议，一般设置为false
                    fileElementId: 'file', //文件上传域的ID
                    dataType: 'text', //返回值类型 一般设置为json
                    success: function (data, status)  //服务器成功响应处理函数
                    {
                    	data = jQuery.parseJSON(jQuery(data).text()); 
                    	var statu = data.iqbResult.result.retCode;
                    	//导入成功
                    	if(statu == 'success'){
                    		$('#import-win').modal({backdrop: 'static', keyboard: false, show: true});	
                    		$("#import-win").find('.import-body').html('导入成功,共导入'+data.iqbResult.result.totalCount + '条数据');
                    	//导入失败
                    	}else if(statu == 'error'){
                    		var errorHtml = data.iqbResult.result.retMsg.split(";");
                    		$('#import-win').modal({backdrop: 'static', keyboard: false, show: true});	
                    		$("#import-win").find('.import-title').html('导入失败,明细如下：');
                    		var importHtml = '';
                    		for(var i=0;i<errorHtml.length;i++){
                    		    importHtml += '<p class="errorInfo">'+errorHtml[i]+'</p>'
                    		}
                    		$("#import-win").find('.import-body').find('.errorInfo').remove();
                    		$("#import-win").find('.import-body').append(importHtml);
                    	}
                    	$('#file').off('change').on('change', function(){
                        	var fileName = $('#file').val();
                        	if(fileName){
                        		_this.ajaxFileUpload();
                        	}
                        });
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                        
                    }
                }
            )
            return false;
        },
        submit : function(){
        	var rows = _grid.util.getCheckedRows();				
			if (_grid.util.checkSelect(rows)){
				//$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				//$("#btn-approve-save").click(function(){
				   $('#btn-submit').attr('disabled',true);
				   for(var i=0;i<rows.length;i++){
						_this.cache.amount = rows[i].orderAmt;
						_this.cache.procBizId = rows[i].orderId + '-' + rows[i].id;
						_this.cache.procBizMemo = rows[i].createTime + '导入资产审批' + rows[i].createTime;
						_this.cache.procOrgCode = rows[i].procOrgCode;
						_this.approve();	
					}
				//});							
			}
        },
        downLoad : function(){
        	$("#btn-downLoad").attr('href', _this.config.action.exports);
        },
		approve: function() {
			/*var approveForm = $('#approveForm').serializeObject();
			if (approveForm.approveStatus) {
				if (approveForm.approveStatus == "1") {
					if($.trim(approveForm.approveOpinion) == '') {
						approveForm.approveOpinion = "同意";
					}
				} else {
					if($.trim(approveForm.approveOpinion) == '') {
						IQB.alert('审批意见必填');
						exit;
					}
				}
			} else {
				IQB.alert("请选择审批结果.");
			}*/
			var bizData = {}
			bizData.amount=_this.cache.amount;
			bizData.procBizId=_this.cache.procBizId;
			bizData.procBizMemo=_this.cache.procBizMemo;
			bizData.procBizType='';
			bizData.procOrgCode=_this.cache.procOrgCode;
			//bizData.procOrgCode='1006003';
			var authData= {}
			authData.procAuthType = "2";
			var procData = {}
			procData.procDefKey = 'fae_asset_import';
			var variableData = {}
			variableData.procApprStatus = '1';
			variableData.procApprOpinion = '同意';
			variableData.procAssignee = '';
			variableData.procAppointTask = '';
			var option = {};
			option.bizData = bizData;
			option.authData=authData;
			option.procData = procData;
			option.variableData = variableData;
			IQB.post(urls['rootUrl'] + '/WfTask/startAndCommitProcess', option, function(result){
				if(result.success != "1") {
					IQB.alert(result.retUserInfo);
					$('#btn-submit').removeAttr('disabled');
				}else{
					IQB.alert('提交成功！');
					$('#btn-alertWin-confirm').click(function(){
						//刷新表格
						$('#btn-submit').removeAttr('disabled');
						_this.refresh();
					});
				}
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config);
			_grid.init();
			//导入
            $("#btn-import").click(function(){
            	var fileName = $('#file').val();
            	$('#file').click();
		    });
            $('#file').off('change').on('change', function(){
            	var fileName = $('#file').val();
            	if(fileName){
            		_this.ajaxFileUpload();
            	}
            });
            //表格赋值
        	$("#btn-confirm").click(function(){
        		$('#import-win').modal('hide');
        		_this.config.dataGrid['url'] = urls['cfm'] + _this.cache.urlPostfix;
        		_grid.init();
        	});
            //提交工作流
            $("#btn-submit").unbind("click").click(function(){
				_this.submit();
		    });
            //删除
            $("#btn-removes").click(function(){
            	_this.remove();
            });
            //修改
            $("#btn-updates").click(function(){
            	_this.update();
            });
            //下载导出模板
            $("#btn-downLoad").click(function(){
            	_this.downLoad();
            });
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.assetInto.init();
});	