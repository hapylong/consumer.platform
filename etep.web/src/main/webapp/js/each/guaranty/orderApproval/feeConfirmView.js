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

/*procBizId = '20170103-267322';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.feeConfirmView');
IQB.feeConfirmView = function(){
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
			var projectName = $('#projectName').text();
			//项目信息完整性校验
			if(!projectName){
				IQB.alert('项目信息不完善，无法审核');
				return false;
			}
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin : function() {
			$('#approve-win').modal('hide');
		},
		openUpdateWin: function(){//打开对话框	
			//禁用表单验证
			$('#updateForm').form('disableValidation');			
			var orderId = $('#orderId').text();
			var orderAmt = Formatter.removeMoneyPrefix($('#orderAmt').text());
			var planId = $('#planId').text();
			var chargeWay = Formatter.chargeWayReverse($('#chargeWay').text());
			$('#update-orderId').val(orderId);
			$('#update-orderAmt').val(orderAmt);
			$('#update-planId').val(planId).trigger('change');
			$('#update-chargeWay').val(chargeWay).trigger('change');
			//启用表单验证
			$('#updateForm').form('enableValidation');
			$('#update-win').modal({backdrop: 'static', keyboard: false, show: true});				
		},
		update: function(){//修改订单信息
			//表单校验
			if($('#updateForm').form('validate')){
				$('#updateForm').prop('action', urls['cfm'] + '/workFlow/modifyOrderInfo');
				IQB.save($('#updateForm'), function(result){
					_this.closeUpdateWin();
					_this.initApprovalTask();				
				})
			}			
		},
		closeUpdateWin: function(){//关闭对话框			
			$('#update-win').modal('hide');			
		},
		openUpdateProjectNameWin: function(){//打开对话框	
			//禁用表单验证
			$('#updateProjectNameForm').form('disableValidation');			
			var projectName = $('#projectName').text();
			var guarantee = $('#guarantee').text();
			if(projectName){
				$('#updateProjectName-projectName').val(projectName);
			}
			if(guarantee){
				$('#updateProjectName-guarantee').val(guarantee).trigger('change');
			}
			//启用表单验证
			$('#updateProjectNameForm').form('enableValidation');
			$('#updateProjectName-win').modal({backdrop: 'static', keyboard: false, show: true});			
		},
		updateProjectName: function(){//修改项目信息
			//表单校验
			if($('#updateProjectNameForm').form('validate')){
				var projectName = $('#updateProjectName-projectName').val();
				var guarantee = $('#updateProjectName-guarantee').val();
				
				var option = {};
				option.orderId = $('#orderId').text();
				option.projectName = projectName;
				option.guarantee = guarantee;			
				$.each(_this.cache.guaranteeArry, function(i, n){
					if(option.guarantee == n.customerName){
						option.guaranteeName =  n.corporateName;
					}
				});
				IQB.post(urls['cfm'] + '/business/newModifyOrder', option, function(resultInfo){
					_this.showFile();
					_this.closeUpdateProjectNameWin();
	 			});
			};		
		},
		closeUpdateProjectNameWin: function(){//关闭对话框			
			$('#updateProjectName-win').modal('hide');			
		},
		initApprovalTask: function(){//初始化流程任务
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: window.procBizId}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text('爱抵贷');
				//处理车辆估价
				if(false){
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
				}else{
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));		
				
				$('#gpsPrice').text(Formatter.money(result.gpsAmt));	
				$('#total').text(Formatter.money(result.orderAmt));
				
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
			});
		},
		initApprovalHistory: function(){//初始化流程历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		initSelect: function(){//初始化select组件
			IQB.post(urls['cfm'] + '/workFlow/getMerAllPlan', {orderId: window.procBizId}, function(result){
				var arry = [];
				$.each(result, function(i, n){
					var obj = {};
					obj.id = n.id;
					obj.text = n.planFullName;
					arry.push(obj);
				});
				$('#update-planId').select2({theme: 'bootstrap', data: arry});
			});
			$('#update-chargeWay').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			IQB.post(urls['cfm'] + '/customer/getCustomerInfoByCustomerType', {customerType: 5}, function(result){
				var arry = [];
				$.each(result.iqbResult.result, function(i, n){
					var obj = {};
					obj.id = n.customerName;
					obj.text = n.customerName;
					arry.push(obj);
				});
				$('#updateProjectName-guarantee').select2({theme: 'bootstrap', data: arry});
				//缓存
				_this.cache.guaranteeArry = result.iqbResult.result;
			});			
		},
		showFile: function(){			
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4, 6, 7, 8, 9, 29]}, function(result){
				$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
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
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/guaranty');
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.businessApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.initSelect();	
			_this.showFile();
			
			$('#btn-update').on('click', function(){_this.openUpdateWin()});
			$('#btn-save').on('click', function(){_this.update()});
			$('#btn-close').on('click', function(){_this.closeUpdateWin()});
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			$('#btn-updateProjectName').on('click', function(){_this.openUpdateProjectNameWin();});
			$('#btn-updateProjectName-save').on('click', function(){_this.updateProjectName();});		
			$('#btn-updateProjectName-close').on('click', function(){_this.closeUpdateProjectNameWin();});
			
			$('#btn-uploadTypeOne').on('click', function(){$('#file').attr('fileType', 1);$('#file').click();});
			$('#btn-uploadTypeTwo').on('click', function(){$('#file').attr('fileType', 2);$('#file').click();});
			$('#btn-uploadTypeThree').on('click', function(){$('#file').attr('fileType', 3);$('#file').click();});
			$('#btn-uploadTypeFour').on('click', function(){$('#file').attr('fileType', 4);$('#file').click();});
			$('#btn-uploadTypeSix').on('click', function(){$('#file').attr('fileType', 6);$('#file').click();});
			$('#btn-uploadTypeSeven').on('click', function(){$('#file').attr('fileType', 7);$('#file').click();});
			$('#btn-uploadTypeEight').on('click', function(){$('#file').attr('fileType', 8);$('#file').click();});
			$('#btn-uploadTypeNine').on('click', function(){$('#file').attr('fileType', 9);$('#file').click();});
			$('#btn-uploadTypeTwentyNine').on('click', function(){$('#file').attr('fileType', 29);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.feeConfirmView.init();
	//禁用浏览器右击菜单,可选
	document.oncontextmenu = function(){return false;}
});		