$package('IQB.caseExecution');
IQB.caseExecution = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectTrialRegistList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,'flag':2})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectTrialRegistList',
	   			singleCheck: true,
	   			queryParams:{
	   				'flag':2
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectTrialRegistList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,'flag':2})	
			});
	    },
		register : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				$('#updateForm')[0].reset();
				$('.thumbnail').remove();
				_this.cache.orderId = records[0].orderId;
				_this.cache.caseId = records[0].caseId;
				_this.showFile();
			    //案件执行历史
				$("#datagrid2").datagrid2({url: urls['cfm'] + '/afterLoan/selectInstOrderCaseExecuteByCaseId',paginator: 'paginator2',queryParams : $.extend({}, {'caseId': records[0].caseId})	
				});
				$("#btn-sure").unbind("click").click(function(){
					  if($("#updateForm").form('validate')){
						  //保存案件执行信息
						  var option = {
								  'orderId':records[0].orderId,
								  'caseId':records[0].caseId,
								  'executeResult':$('#postCondition').val(),
								  'executeRemark':$('#schedule').val(),
						  };
						  IQB.post(urls['cfm'] + '/afterLoan/saveInstOrderCaseExecute', option, function(result){
							  if(result.success == '1'){
								  IQB.alert('登记成功');
								  $('#open-win').modal('hide');
								  _this.refresh();
							  }
						  })
					  }
				})
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
      	        $('#open-win').modal('hide');
      	        $('#updateForm')[0].reset();
		    });
		},
		previewFile : function(event){
			var tarent = event.currentTarget;
			var option = {
				'imgName': $(tarent).attr('alt'),
				'orderId': $(tarent).attr('orderId'),
				'imgType':$(tarent).attr('imgType')
			};
			var docType = option.imgName.split('.');
			if(docType[1] == 'pdf'){
				var url = $(tarent).attr('path');
				//window.parent.IQB.main2.openTab("case-pdf", "查看预览", '/etep.web/view/afterLoan/caseExecution/pdfShow.html?url=' + encodeURI(url), true, true, null);
				$(tarent).attr('href',url);
			}else{
				IQB.post(urls['cfm'] + '/image/convertDocToHtml', option, function(result){
					window.open(urls['baseUrl'] + result.iqbResult.result);
				});
			}
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: _this.cache.caseId, imgType: [104, 105, 106]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.wP.contain(extensionName)){
						var html = '<div class="thumbnail float-left" style="width: 145px;float:left;margin:5px;">' + 
						      			'<a href="javascript:void(0)"></a>' +
						      			'<div class="caption">' +
						      			'<a href="javascript:;" target="_blank" path="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" orderId="' + n.orderId + '" imgType="'+ n.imgType +'" onclick="IQB.caseExecution.previewFile(event);">'+ n.imgName + '</a><a filePath="' + n.imgPath + '" onclick="IQB.caseExecution.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
			});
		},
		uploadFile: function(){
			var files = $('#file').get(0).files;
			var mark = false;
			$.each(files, function(i, n){
				var extensionName = Formatter.getExtensionName(n.name);
				if(Formatter.extensionName.wP.contain(extensionName)){
					//mark = true;				
					//return false;
				}else{
					mark = true;						
					return false;
				}
			});			
			if(mark){
				$('#file').val('');
				IQB.alert('上传格式不对，请重新上传。');
				return false;
			}
			
			$('#btn-upload').prop('disabled', true);
			$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/doc/cfmdoc');
			IQB.postForm($('#uploadForm'), function(result){
				var fileType = $('#file').attr('fileType');	
				var arr = [];
				var html = '';
				$.each(files, function(i, n){
					var option = {};
					option.orderId = _this.cache.caseId;
					option.imgType = fileType;
					option.imgName = n.name;
					option.imgPath = result.iqbResult.result[i];
					arr.push(option);
					html += '<div class="thumbnail float-left" style="width: 145px;float:left;margin:5px;">' + 
					      		 '<div class="caption">' +
					      		 	'<a href="javascript:;" target="_blank" path="' + urls['imgUrl'] + option.imgPath + '" alt="' + option.imgName + '" orderId="' + option.orderId + '" imgType="'+ option.imgType +'" onclick="IQB.caseExecution.previewFile(event);">' + option.imgName + '</a><a filePath="' + option.imgPath + '" onclick="IQB.caseExecution.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
				});
			});	
		},
		removeFile: function(event){
			if(event.stopPropagation){//W3C阻止冒泡方法  
				event.stopPropagation();  
			}else{//IE阻止冒泡方法   
				event.cancelBubble = true;
			}  	
			var tarent = event.currentTarget;
			$(tarent).prop('disabled', true);
			$(tarent).find('span').first().removeClass('glyphicon glyphicon-trash').addClass('fa fa-spinner fa-pulse');
			var filePath = $(tarent).attr('filePath');		
			IQB.post(urls['cfm'] + '/fileUpload/remove', {filePath: filePath}, function(result){
				IQB.post(urls['cfm'] + '/image/deleteImage', {imgPath: filePath}, function(resultInfo){
					$(tarent).parent().parent().remove();
		 		});
			});		
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			//案件执行
			$('#btn-register').unbind('click').on('click',function(){
				_this.register();
			});
			$('#btn-104').on('click', function(){$('#file').attr('fileType', 104);$('#file').click();});
			$('#btn-105').on('click', function(){$('#file').attr('fileType', 105);$('#file').click();});
			$('#btn-106').on('click', function(){$('#file').attr('fileType', 106);$('#file').click();});
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.caseExecution.init();
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