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

$package('IQB.preApproval');
IQB.preApproval = function(){
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
		//初始化客户渠道名称
		initCustChannels: function(){
			IQB.post(urls['rootUrl'] + '/car_dealer/cget_info', {'type':0}, function(result) {
				$('#customerName').typeahead({
			        source:  result.iqbResult.result,
			        //display:'text'
			    });
			})
			
		},
		//初始化车商名称
		initSourceCarName: function(){
			IQB.post(urls['rootUrl'] + '/car_dealer/cget_info', {'type':1}, function(result) {
				$('#carName').typeahead({
			        source:  result.iqbResult.result,
			        //display:'text'
			    });		
			})
		},
		carChannels : function(){
			var carChannels = $('#carChannels').val();
			if(carChannels == 1 || carChannels == 2){
				$('.CTD').show();
			}else{
				$('.CTD').hide();
			}
		},
		carName :function(){
			//是否黑名单
			IQB.post(urls['cfm'] + '/dealer/judgeBlack', {'sourceCarName':$('#carName').val()}, function(result){
				var result = result.iqbResult.result;
				if(result == 'Y'){
					IQB.alert('此商户为黑名单商户，不可输入');
					$('#carName').val('');
				}
			})
		},
		//客户渠道为车商/4s店时，输入名称，车来源/名称自动回显
		customerName : function(){
			var customerName = $('#customerName').val();
			var customerChannels = $('#customerChannels').val();
			if(customerChannels == 3 || customerChannels == 4){
				$('#carChannels').val(customerChannels).attr('disabled','disabled');
				$('#carName').val(customerName).attr('disabled','disabled');
			}else{
				$('#carChannels').val('').removeAttr('disabled');
				$('#carName').val('').removeAttr('disabled');
			}
		},
		customerChannels : function(){
			var customerChannels = $('#customerChannels').val();
			if(customerChannels == 3 || customerChannels == 4){
				var customerName = $('#customerName').val();
				$('#carChannels').val(customerChannels).attr('disabled','disabled');
				$('#carName').val(customerName).attr('disabled','disabled');
				$('.CTD').hide();
			}else{
				$('#carChannels').val('').removeAttr('disabled');
				$('#carName').val('').removeAttr('disabled');
			}
		},
		openApproveWin: function(){
			if($('#checkForm').form('validate')){
				if($('#td-12').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-27').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if($('#td-27').find('div').length == 0){
					IQB.alert('风控信息不完善，无法审核');
					return false;
				};
				if(false){
					if($('#td-23').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
				}else{
					if($('#td-15').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
					if($('#td-16').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
					if($('#td-17').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
					if($('#td-18').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
					if($('#td-19').find('div').length == 0){
						IQB.alert('风控信息不完善，无法审核');
						return false;
					};
					if($("#VIN").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
					}
					if($("#engineNo").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
					}
					if($("#licenseNumber").val() == ''){
					IQB.alert('风控信息不完善，无法审核');
					return false;
					}
				}
				var VIN = $("#VIN").val();
				var engineNo = $("#engineNo").val();
				var licenseNumber = $("#licenseNumber").val();
				var carType = $("#carType").val();
				console.log(carType);
				var data = {
					'carNo' : VIN,
					'engine' : engineNo,
					'plate' : licenseNumber,
					'plateType' : carType,
					'orderId' : window.procBizId
				}
				IQB.post(urls['cfm'] + '/authoritycard/save', data, function(result){
					if(result.success == 1){
						_this.saveCarInfo();
					}
				})
			}
		},
		//保存车商信息
		saveCarInfo: function(){
			var option = {
					"orderId": $('#orderId').text(),
				    "custChannels": $('#customerChannels').val(),
				    "custChannelsName": $('#customerName').val(),
				    "sourceCar": $('#carChannels').val(),
				    "sourceCarName": $('#carName').val(),
				    "address": $('#address').val(),
				    "contactMethod": $('#phone').val(),
				    "maritalStatus": $('#marriedStatus').val(),
				    "contactName": $('#coName').val(),
				    "contactMobile": $('#coPhone').val(),
				    "contactAddr": $('#coAddress').val()
			}
			IQB.post(urls['cfm'] + '/dealer/saveDealerInfo', option, function(result){
				if(result.iqbResult.result == 'success'){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}
			})
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: window.procBizId}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text('爱抵贷');
				//处理业务类型
				if(false){
					$('.new-li').show();
					$('.new-tr').show();
					$('.old-li').hide();
					$('.old-tr').hide();
				}else{
					$('.old-li').show();
					$('.old-tr').show();
					$('.new-li').hide();
					$('.new-tr').hide();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
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
			});
		},
		initApprovalHistory: function(){//初始化订单流程历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.preApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.preApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		showText : function(){
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
				$("#VIN").val(result.iqbResult.AuthorityCardInfo.carNo);
				$("#engineNo").val(result.iqbResult.AuthorityCardInfo.engine);
				$("#licenseNumber").val(result.iqbResult.AuthorityCardInfo.plate);
				$("#carType").val(result.iqbResult.AuthorityCardInfo.plateType);
			})
		},
		showCarInfo:function(){
			//回显车商信息
			IQB.post(urls['cfm'] + '/dealer/getDealerInfo', {'orderId':window.procBizId}, function(result){
				var result = result.iqbResult.result;
				if(result != null){
					$('#customerChannels').val(result.custChannels);
					$('#customerName').val(result.custChannelsName);
					$('#carChannels').val(result.sourceCar);
					$('#carName').val(result.sourceCarName);
					$('#address').val(result.address);
					$('#phone').val(result.contactMethod);
					if(result.sourceCar == 1 || result.sourceCar == 2){
						$('.CTD').show();
						$('#marriedStatus').val(result.maritalStatus);
						$('#coName').val(result.contactName);
						$('#coPhone').val(result.contactMobile);
						$('#coAddress').val(result.contactAddr);
					}
				}
			})
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			//回退到门店预处理节点信息回显
			_this.showText();
			_this.initCustChannels();
			_this.initSourceCarName();
			_this.showCarInfo();
			$('#carChannels').on('change',function(){_this.carChannels()});
			$('#customerName').on('change',function(){_this.customerName()});
			$('#customerChannels').on('change',function(){_this.customerChannels()});
			//$('#carName').on('blur',function(){_this.carName()});车商黑名单判断
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			$('#btn-uploadTypeTen').on('click', function(){$('#file').attr('fileType', 10);$('#file').click();});
			$('#btn-uploadTypeEleven').on('click', function(){$('#file').attr('fileType', 11);$('#file').click();});
			$('#btn-uploadTypeTwelve').on('click', function(){$('#file').attr('fileType', 12);$('#file').click();});
			$('#btn-uploadTypeThirteen').on('click', function(){$('#file').attr('fileType', 13);$('#file').click();});
			$('#btn-uploadTypeFourteen').on('click', function(){$('#file').attr('fileType', 14);$('#file').click();});
			$('#btn-uploadTypeFifteen').on('click', function(){$('#file').attr('fileType', 15);$('#file').click();});
			$('#btn-uploadTypeSixteen').on('click', function(){$('#file').attr('fileType', 16);$('#file').click();});
			$('#btn-uploadTypeSeventeen').on('click', function(){$('#file').attr('fileType', 17);$('#file').click();});
			$('#btn-uploadTypeEighteen').on('click', function(){$('#file').attr('fileType', 18);$('#file').click();});
			$('#btn-uploadTypeNineteen').on('click', function(){$('#file').attr('fileType', 19);$('#file').click();});
			$('#btn-uploadTypeTwenty').on('click', function(){$('#file').attr('fileType', 20);$('#file').click();});
			$('#btn-uploadTypeTwentyOne').on('click', function(){$('#file').attr('fileType', 21);$('#file').click();});
			$('#btn-uploadTypeTwentyTwo').on('click', function(){$('#file').attr('fileType', 22);$('#file').click();});
			$('#btn-uploadTypeTwentyThree').on('click', function(){$('#file').attr('fileType', 23);$('#file').click();});
			$('#btn-uploadTypeTwentyFour').on('click', function(){$('#file').attr('fileType', 24);$('#file').click();});
			$('#btn-uploadTypeTwentyFive').on('click', function(){$('#file').attr('fileType', 25);$('#file').click();});
			$('#btn-uploadTypeTwentySix').on('click', function(){$('#file').attr('fileType', 26);$('#file').click();});
			$('#btn-uploadTypeTwentySeven').on('click', function(){$('#file').attr('fileType', 27);$('#file').click();});
			$('#btn-uploadTypeTwentyEight').on('click', function(){$('#file').attr('fileType', 28);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	//页面初始化
	IQB.preApproval.init();
	//禁用浏览器右击菜单,可选
	document.oncontextmenu = function(){return false;}
});


