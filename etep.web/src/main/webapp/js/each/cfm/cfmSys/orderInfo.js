$package('IQB.orderInfo');
IQB.orderInfo = function(){
	var _grid = null;
	var _tree = null;
	var flag= 1;
    var showDataHtml="";
	var _this = {		
		cache :{
			amount : '',
			procBizId : '',
		    procBizMemo : '', 
		    procOrgCode : '',
		    eq:0,
		    eq2:0,
		    urlPostfix : '/orderImport/query',
		    count : -1
		},
		config: {
			action: {//页面请求参数
  				remove: urls['cfm'] + '/orderImport/batchDel',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/orderImport/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'status' : 1})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/orderImport/query',
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
			$("#datagrid").datagrid2({url: urls['cfm'] + '/orderImport/query',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page,'status':1,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
				onPageChanged : function(page){
	   				_this.refresh(page);
	   			}	
			});
		},
		remove: function(){
			var rows = _grid.util.getCheckedRows();				
			if (_grid.util.checkSelect(rows)){
				IQB.confirm('确认要删除吗？', function(){
					var orderIdArr = [];
					for(var i=0;i<rows.length;i++){
						orderIdArr.push(rows[i]['orderId']);
					}
					var option = {
							'orderIds' : orderIdArr
					}
			   		IQB.remove(_this.config.action.remove, option, function(result){		
			   		    //表格url
			   			_this.refresh();
			   		});
				}, function(){});										
			}
		},
		getDictListByDictType: function(selectId, dictType){
			var req_data = {'dictTypeCode': dictType};
			IQB.post(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
				$('#' + selectId).prepend("<option value=''>请选择</option>");
				for(i=0;i<result.iqbResult.result.length;i++){
					$('#' + selectId).append("<option value='"+result.iqbResult.result[i].id+"'>"+result.iqbResult.result[i].text+"</option>");
				}
				//showHtml
			})
		},
		ajaxFileUpload : function() {
			$.ajaxFileUpload
            (
                {
                    url:urls['cfm'] + '/orderImport/import', //用于文件上传的服务器端请求地址
                    data: { merchantNo: $("#merchNames").attr("merchantno"), bizType: $("#bizType").val(),projectPrefix: $("#projectProfix").val() },
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
        bizType: function(){
        	var merchantNo = $(".merch").val();
        	if(merchantNo == ''){
                  IQB.alert('请选择商户');
        	}else if(merchantNo == '全部商户'){
        		showDataHtml="";
                $('#bizType option').remove();
        	}else if (flag){
                IQB.get(urls['cfm'] + '/merchantBizType/getMerchantBizTypeList', {'merchantNo':merchantNo}, function(result){
                    flag=0;
                	if (result.success=="1") {
                		for(var i=0;i<result.iqbResult.result.length;i++){                      			
    	        			 showDataHtml +="<option value='"+result.iqbResult.result[i].bizType+"'>"+result.iqbResult.result[i].name+"</option>"
    	    			}
    		 			$("#bizType").append(showDataHtml);
    		 			var bizType = $("#bizType").val();
    		 		};
		        });
        	}
        },
		init: function(){ 
			_this.getDictListByDictType('projectProfix', 'projectProfix'); 
			_grid = new DataGrid2(_this.config);
			_grid.init();
			//导入
            $("#btn-import").click(function(){
            	var fileName = $('#file').val();
            	if($("#bizType").val()=="" || $("#bizType").val()==null){
            		alert("请选择业务类型");
            		return false;
            	};
            	if($("#merchNames").val()=="全部商户"){
            		alert("请选择商户");
            		return false;
            	};
            	if($("#projectProfix").val()==""){
            		alert("请选择项目前缀");
            		return false;
            	};
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
            //删除
            $("#btn-removes").click(function(){
            	_this.remove();
            });
            $('#bizType').unbind('click').on('click',function(){
            	_this.bizType();
            });
            $(".merch").click(function(){
                flag=1;
                showDataHtml="";
                $('#bizType option').remove();
          });
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.orderInfo.init();
	datepicker(startTime);
	datepicker(endTime);
});	
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};
