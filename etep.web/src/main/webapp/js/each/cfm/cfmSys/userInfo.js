$package('IQB.userInfo');
IQB.userInfo = function(){
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
		    urlPostfix : '/userImport/query',
		    count : -1
		},
		config: {
			action: {//页面请求参数
			    query: urls['cfm'] + '/userImport/query',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/userImport/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'status' : 1})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/userImport/query',
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
			$("#datagrid").datagrid2({url: urls['cfm'] + '/userImport/query',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page,'status':1,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
				onPageChanged : function(page){
	   				_this.refresh(page);
	   			}	
			});
		},
		ajaxFileUpload : function() {
			$.ajaxFileUpload
            (
                {
                    url:urls['cfm'] + '/userImport/import', //用于文件上传的服务器端请求地址
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
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.userInfo.init();
});	