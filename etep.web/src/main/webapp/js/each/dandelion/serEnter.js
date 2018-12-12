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

/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.serEnter');
IQB.serEnter = function(){
	var _this = {
		cache: {
			viewer: {},
			id:'',
			text:'',
			//orderFlag:''
		},
		config: {
			//页面请求参数
			action: {
				getSelectProcAssignee: urls['cfm'] + '/SysUserRest/getSysUserAll',
				assign :urls['cfm'] + '/save/persist_design_person',
				saveAmtAndItems:urls['cfm'] + '/dandelion/saveHistoryOrder'
  			}
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
				//variableData.orderFlag = _this.cache.orderFlag;
				variableData.procAssignee = _this.cache.id;
				var bizData = {}
				bizData.procBizId=procBizId;
				//bizData.orderFlag = _this.cache.orderFlag;
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
		//保存信息----客户录入去掉相应字段
		/*saveInfo:function(){
			var orderFlag = $('#orderFlag').val();
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
			_this.cache.orderFlag = orderFlag;
			var data = {
					'orderId':window.procBizId,
					'flag':orderFlag,
					'startDate':startTime,
					'endDate':endTime
			}
			IQB.post(_this.config.action.saveAmtAndItems, data, function(result) {
				if(result.iqbResult.result == 'success'){
					_this.approve();
				}	
			})
		},
		orderFlag:function(){
			var orderFlag = $('#orderFlag').val();
			if(orderFlag == 2){
				$('.time').hide();
			}else{
				$('.time').show();
			}
		},*/
		//初始化处理人下拉框
		initSelectProcAssignee: function(){
			IQB.post(_this.config.action.getSelectProcAssignee, {}, function(result) {
				$('#btn-ProcAssignee').select2({theme: 'bootstrap', data: result.iqbResult.result}).on('change', function(){
                    _this.cache.id=$(this).val();
                    _this.cache.text=$('#select2-btn-ProcAssignee-container').html();
				});		
			})
		},
		//  打开派单人选择窗口
		sendOrderOpen : function(){
			$('#sendOrder-win').modal('show');
		},
		// 关闭派单人选择窗口
		sendOrderClose : function() {
			$('#sendOrder-win').modal('hide');

		},
		// 保存流程任务处理人
		sendOrderSave : function() {
			var procAssignee = $('#btn-ProcAssignee').val();
			if(procAssignee){
				var option = {};
				option.id = _this.cache.id;
				option.text = _this.cache.text;
				option.orderId = window.procBizId;
				IQB.post(urls['rootUrl'] + '/dandelion/persist_design_person', option, function(result){
					if(result.success=="1") {
						IQB.alert('指定派单处理人成功！');
						$('#sendOrder-win').modal('hide');
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
			}else{
				IQB.alert('派单处理人不能为空');
			}
		},
		openApproveWin: function(){
			if(_this.cache.id == ''){
				IQB.alert('派单处理人不能为空');
				return false;
			}
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化信息详情
			//基本信息
			IQB.post(urls['cfm'] + '/dandelion/get_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchName').text(result.merchantNo);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				//订单状态
				$('#orderStatus').text(Formatter.orderStatus(result.riskStatus));
				
				$('#bizType').text(result.proType);
				$('#groupName').text(Formatter.groupName(result.bizType));
				$('#idCard').text(result.idCard);
				//$('#bankCard').text(result.bankCard);
				//紧急联系人信息
				IQB.post(urls['cfm'] + '/dandelion/get_risk', {orderId: window.procBizId}, function(result){
					var result = result.iqbResult.result;
					$('#address').text(Formatter.ifNull(result.address));
					$('#firmName').text(Formatter.ifNull(result.companyName));
					$('#firmAddress').text(Formatter.ifNull(result.companyAddress));
					$('#firmPhone').text(Formatter.ifNull(result.companyPhone));
					$('#creditNumber').text(Formatter.ifNull(result.creditNo));
					$('#creditPass').text(Formatter.ifNull(result.creditPasswd));
					$('#creditCode').text(Formatter.ifNull(result.creditCode));
				});
			});
		},
		showFile: function(){			
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4,101,102]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      			'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.serEnter.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					option.orderId = window.procBizId;
					option.imgType = fileType;
					option.imgName = n.name;
					option.imgPath = result.iqbResult.result[i];
					arr.push(option);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
					      		'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + option.imgPath + '" alt="' + option.imgName + '" style="width: 135px; height: 135px;" /></a>' +
					      		 '<div class="caption">' +
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.serEnter.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.initSelectProcAssignee();//初始化处理人下拉框

			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});	
			$('#btn-sendOrder').on('click', function(){_this.sendOrderOpen()});
			$('#btn-sendOrder-close').click(function() {
				_this.sendOrderClose();
			});
			$('#btn-sendOrder-save').click(function() {
				_this.sendOrderSave();
			});
			//$('#orderFlag').on('change',function(){_this.orderFlag();});
			
			$('#btn-uploadTypeOne').on('click', function(){$('#file').attr('fileType', 1);$('#file').click();});
			$('#btn-uploadTypeTwo').on('click', function(){$('#file').attr('fileType', 2);$('#file').click();});
			$('#btn-uploadTypeThree').on('click', function(){$('#file').attr('fileType', 3);$('#file').click();});
			$('#btn-uploadTypeFour').on('click', function(){$('#file').attr('fileType', 4);$('#file').click();});
			$('#btn-uploadType101').on('click', function(){$('#file').attr('fileType', 101);$('#file').click();});
			$('#btn-uploadType102').on('click', function(){$('#file').attr('fileType', 102);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function() {
	IQB.serEnter.init();
	/*datepicker(startTime);
	datepicker(endTime);*/
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


