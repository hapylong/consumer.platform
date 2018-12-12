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

$package('IQB.financeApproval');
IQB.financeApproval = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		viewerShow: function(event){
			$('#img').click();
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
		openApproveWin: function() {
			var creditName = $('#creditName').text();
			//债权人信息完整性校验
			if(!creditName){
				IQB.alert('债权人信息不完善，无法审核');
				return false;
			}
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		openUpdateWin: function(){
			//禁用表单验证
			$('#updateForm').form('disableValidation');
			
			var orderId = $('#orderId').text();
			var creditName =$('#creditName').text();
			var creditCardNo = $('#creditCardNo').text();
			var creditBankCard = $('#creditBankCard').text();
			var creditBank = $('#creditBank').text();
			var creditPhone = $('#creditPhone').text();
			
			$('#update-orderId').val(orderId);
			$('#update-creditName').val(creditName);
			$('#update-creditCardNo').val(creditCardNo);
			$('#update-creditBankCard').val(creditBankCard);
			if(creditBank){
				$('#update-creditBank').val(creditBank).trigger('change');
			}			
			$('#update-creditPhone').val(creditPhone);
			$('#update-win').modal({backdrop: 'static', keyboard: false, show: true});	
			
			//启用表单验证
			$('#updateForm').form('enableValidation');
		},
		update: function(){	
			var creditName = $('#creditName').text();
			if(creditName){
				$('#updateForm').prop('action', urls['cfm'] + '/creditorInfo/updateCreditorInfo');
			}else{
				$('#updateForm').prop('action', urls['cfm'] + '/creditorInfo/saveCreditorInfo');				
			}			
			IQB.save($('#updateForm'), function(result){
				_this.closeUpdateWin();
				_this.initApprovalTask();				
			})
		},
		closeUpdateWin: function(){	
			$('#update-win').modal('hide');			
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
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));
				//处理支付结果
				if(result.chargeWay == 1){
					$('.isPay-table').show();
					$('.isPay-div').hide();	
					$('.outOrderId-div').hide();					
				}else{
					$('.isPay-table').hide();
					$('.isPay-div').show();	
					$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
					//处理流水号
					if(result.preAmtStatus == 1){
						$('.outOrderId-div').show();	
					}else{
						$('.outOrderId-div').hide();	
					}
				}
				
				$('#gpsPrice').text(Formatter.money(result.gpsAmt));	
				$('#total').text(Formatter.money(result.orderAmt));
				
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
			});
			IQB.post(urls['cfm'] + '/creditorInfo/getCreditorInfo', {orderId: window.procBizId}, function(result){
				if(result.iqbResult.result){
					$('#creditName').text(Formatter.ifNull(result.iqbResult.result.creditName));
					$('#creditCardNo').text(Formatter.ifNull(result.iqbResult.result.creditCardNo));
					$('#creditBankCard').text(Formatter.ifNull(result.iqbResult.result.creditBankCard));
					$('#creditBank').text(Formatter.ifNull(result.iqbResult.result.creditBank));
					$('#creditPhone').text(Formatter.ifNull(result.iqbResult.result.creditPhone));
				}				
			});
			IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId}, function(result){
				if(result){
					$('#outOrderId').text(Formatter.ifNull(result.outOrderId));				
				}				
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
		initSelect: function(){
			$('#update-creditBank').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});	
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1, 2, 3, 4, 5, 6, 7, 8, 9, 29]}, function(result){
				$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				var is = false;
				var is2 = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						if(n.imgType != 5){
							var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
							      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
							      			'<div class="caption">' +
							      				'<h5>' + n.imgName + '</h5>' +
							      			'</div>' + 
							      		'</div>';
							$('#td-' + n.imgType).append(html);
							is = true;
						}else{
							$('#btn-upload').hide();
							$('#printScreen').append('<a onclick="IQB.financeApproval.viewerShow(event);">' + n.imgName +'</a>');
							$('#img').prop('src', urls['imgUrl'] + n.imgPath);
							$('#img').prop('alt', n.imgName);
							$('#btn-remove').append('<a filePath="' + n.imgPath + '" onclick="IQB.financeApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a>');
							is2 = true;
						}						
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
				if(is2){
					_this.cache.viewer.img = new Viewer(document.getElementById('img'), {});
				}
			});
		},
		uploadFile: function(){
			var extensionName = Formatter.getExtensionName($('#file').val());
			if(Formatter.extensionName.doc.contain(extensionName)){
				$('#file').val('');
				IQB.alert('格式不支持');
			}else if(Formatter.extensionName.pic.contain(extensionName)){
				$('#btn-upload').prop('disabled', true);
				$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
				$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/upload/pic/guaranty');
				IQB.postForm($('#uploadForm'), function(result){
					var fileType = $('#file').attr('fileType');		
					var option = {};
					option.orderId = window.procBizId;
					option.imgType = fileType;
					option.imgName = Formatter.getFileName($('#file').val());
					option.imgPath = result.iqbResult.result;
					IQB.post(urls['cfm'] + '/image/uploadImage', option, function(resultInfo){
						$('#file').val('');
						$('#btn-upload').prop('disabled', false).hide();
						$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');		 
						$('#printScreen').append('<a onclick="IQB.financeApproval.viewerShow(event);">' + option.imgName +'</a>');
						$('#img').prop('src', urls['imgUrl'] + option.imgPath);
						$('#img').prop('alt', option.imgName);
						$('#btn-remove').append('<a filePath="' + option.imgPath + '" onclick="IQB.financeApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a>');
						if(_this.cache.viewer.img){
							_this.cache.viewer.img.update();
						}else{
							_this.cache.viewer.img = new Viewer(document.getElementById('img'), {});
						}
					});
				});
			}else{
				$('#file').val('');
				IQB.alert('格式不支持');
			}
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
					$('#btn-upload').show();
					$('#printScreen').html('');
					$('#btn-remove').html('');
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
			
			$('#btn-upload').on('click', function(){$('#file').attr('fileType', 5);$('#file').click();});			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.financeApproval.init();
	//禁用浏览器右击菜单,可选
	document.oncontextmenu = function(){return false;}
});		