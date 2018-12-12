function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('procBizid');
var procInstId = getQueryString('procInstId');

$package('IQB.registerUpdate');
IQB.registerUpdate = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		approve: function() {
			var approveForm = $('#approveForm').serializeObject();
			
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
				
				var authData= {}
				authData.procAuthType = "2";
				var variableData = {}
				variableData.procApprStatus = approveForm.approveStatus;
				variableData.procApprOpinion = approveForm.approveOpinion;
				var bizData = {}
				bizData.procBizId=procBizId;
				//后台要求
				bizData.orderId=_this.cache.orderId;
				bizData.additionalNo=_this.cache.uuid;
				var procData = {}
				procData.procTaskId = procTaskId;
				var option = {};
				option.authData=authData;
				option.variableData = variableData;
				option.bizData = bizData;
				option.procData = procData;
				IQB.post(urls['rootUrl'] + '/WfTask/completeProcess', option, function(result){
					if(result.success=="1") {
						$('#approve-win').modal('hide');
						var url = window.location.pathname;
						var param = window.parent.IQB.main2.fetchCache(url);
						var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
						var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + false + ',' + null + ')';
						window.parent.IQB.main2.call(callback, callback2);
					} else {
						IQB.alert(result.retUserInfo);
					}
				});			
			} else {
				IQB.alert("请选择审批结果.");
			}
		},
		unassign: function() {
			var authData= {}
			authData.procAuthType = "2";
			var variableData={}
			var bizData={}
			var procData={}
			procData.procTaskId = procTaskId;
			var option = {};
			option.authData=authData;
			option.variableData=variableData;
			option.bizData=bizData;
			option.procData=procData;
			IQB.getById(urls['rootUrl'] + '/WfTask/unclaimProcess', option, function(result) {
				if(result.success=="1") {
					var url = window.location.pathname;
					var param = window.parent.IQB.main2.fetchCache(url);
					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
					var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + false + ',' + null + ')';
					window.parent.IQB.main2.call(callback, callback2);
				} else {
					IQB.alert(result.retUserInfo);
				}
			});
		},
		openApproveWin: function(){
			if($('#updateForm').form('validate')){
				if($('#td-107').find('div').length == 0 || $('#td-108').find('div').length == 0 || $('#td-109').find('div').length == 0){
					IQB.alert('保险资料不完善，无法审核');
					return false;
				};
				var option = {
						'orderId':_this.cache.orderId,
						'additionalNo':_this.cache.uuid,
						'registerDate':$('#registerDate').val(),//
						'currentMaster':$('#carOwner').val(),//
						'color':$('#carColor').val(),//
						'carInspectTime':$('#annualDate').val(),
						'comInsOrg':$('#busiRiskName').val(),
						'comInsOverTime':$('#busiRiskDate').val(),
						'comTraAccOrg':$('#jqRiskName').val(),
						'comTraAccOverTime':$('#jqRiskDate').val(),
				}
				IQB.post(urls['cfm'] + '/trafficManage/saveTrafficManageAdditional', option, function(result){
					if(result.success == '1'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}

		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/trafficManage/getTrafficManageDetail', {procBizId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				//返回uuid取图片信息
				_this.cache.uuid = result.additionalNo;
				$('#orderId').text(Formatter.isNull(result.orderId));	
				_this.cache.orderId = result.orderId;
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#bizType').text(Formatter.bizType(result.bizType));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				//填写的信息回显
				$('#registerDate').val(result.registerDate);
				$('#carOwner').val(result.currentMaster);
				$('#carColor').val(result.color);
				
				$('#annualDate').val(result.carInspectTime);
				$('#busiRiskName').val(result.comInsOrg);
				$('#busiRiskDate').val(result.comInsOverTime);
				$('#jqRiskName').val(result.comTraAccOrg);
				$('#jqRiskDate').val(result.comTraAccOverTime);
				//接口返回flag
				if(result.readOnlyFlag == 1){
					//有记录回显不可修改
					$('.onlyOne').validatebox({ required: false }).attr('disabled','disabled');
				}else{
					//无记录要输入
					$('.onlyOne').validatebox({ required: true }).removeAttr('disabled');
					$('#registerDate').val(result.registerDate);
					$('#carOwner').val(result.currentMaster);
					$('#carColor').val(result.color);
				}
				_this.showFile();
			});
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId:_this.cache.uuid, imgType: [107,108,109,110]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.registerUpdate.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			});
		},
		showHistoryList : function(){
			var procBizIdArr = window.procBizId.slice(2).split('-');
			IQB.post(urls['cfm'] + '/trafficManage/selectTrafManaAdditionalhistory', {orderId: procBizIdArr[0],'checkStatus':'2'}, function(result){
				//渲染列表
				var tableHtml = '';
				var result =result.iqbResult.result;
				for(i=0;i<result.length;i++){
					var j= i+1;
					tableHtml +="<tr><td>"+[j]+"</td><td><a href='javascript:;' uuid='"+ result[i].additionalNo +"' onclick='IQB.registerUpdate.previewImg(event);'>"+(result[i].carInspectTime)+
						"</a></td><td>"+result[i].comInsOrg+"</td><td>"+result[i].comInsOverTime+"</td><td>"
						+(result[i].comTraAccOrg)+"</td><td>"+(result[i].comTraAccOverTime)+"</td><td>"+(result[i].operatorTime)+"</td><td>"+(result[i].operatorUser)+"</td></tr>";
				}
				$("#datagrid2").append(tableHtml);
			})
		},
		previewImg: function(event){
			var tarent = event.currentTarget;//当前操作对象
			$('#open-win').modal('show');
			//清空列表里的图片
			$('#viewerTwo').find('.thumbnail').remove();
			IQB.post(urls['cfm'] + '/image/getImageList', {'orderId':$(tarent).attr('uuid'), imgType: [107,108,109,110]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5>' +
						      			'</div>' + 
						      		'</div>';
						$('.td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('open-win'), {});
				}
			});
			$("#btn-close").click(function(){
      	        $('#open-win').modal('hide');
		    });
		},
		uploadFile: function(){
			var files = $('#file').get(0).files;
			var mark = false;
			$.each(files, function(i, n){
				var extensionName = Formatter.getExtensionName(n.name);
				if(Formatter.extensionName.doc.contain(extensionName)){
					mark = true;				
					return false;
				}else if(Formatter.extensionName.pic.contain(extensionName)){
					
				}else{
					mark = true;						
					return false;
				}
			});			
			if(mark){
				$('#file').val('');
				IQB.alert('格式不支持');
				return false;
			}
			
			$('#btn-upload').prop('disabled', true);
			$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/cfm');
			IQB.postForm($('#uploadForm'), function(result){
				var fileType = $('#file').attr('fileType');	
				var arr = [];
				var html = '';
				$.each(files, function(i, n){
					var option = {};
					option.orderId = _this.cache.uuid;
					option.imgType = fileType;
					option.imgName = n.name;
					option.imgPath = result.iqbResult.result[i];
					arr.push(option);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
					      		'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + option.imgPath + '" alt="' + option.imgName + '" style="width: 135px; height: 135px;" /></a>' +
					      		 '<div class="caption">' +
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.registerUpdate.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(_this.cache.viewer.viewerOne){
						_this.cache.viewer.viewerOne.update();
					}else{
						_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					}
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
					$(tarent).parent().parent().parent().remove();
		 		});
			});		
		},
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
            
			_this.initApprovalTask();
			_this.showHistoryList();
			_this.initApprovalHistory();
			//账单详情
			$('#btn-uploadType107').on('click', function(){$('#file').attr('fileType', 107);$('#file').click();});
			$('#btn-uploadType108').on('click', function(){$('#file').attr('fileType', 108);$('#file').click();});
			$('#btn-uploadType109').on('click', function(){$('#file').attr('fileType', 109);$('#file').click();});
			$('#btn-uploadType110').on('click', function(){$('#file').attr('fileType', 110);$('#file').click();});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function(){
	IQB.registerUpdate.init();
	datepicker(registerDate);
	datepicker(annualDate);
	datepicker(busiRiskDate);
	datepicker(jqRiskDate);
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