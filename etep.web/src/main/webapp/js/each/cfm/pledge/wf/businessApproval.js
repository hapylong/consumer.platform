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

$package('IQB.businessApproval');
IQB.businessApproval = function(){
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
			if($('#td-0').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-2').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-3').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-4').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-5').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-6').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-28').find('div').length < 2){
				IQB.alert('车辆资料信息不完善，无法审核');
				return false;
			};
			if($('#td-7').find('div').length < 2){
				IQB.alert('个人资料不完善，无法审核');
				return false;
			};
			var type = $('#type').val();
			if(type == '2001'){
				if($('#td-23').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-24').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-25').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-26').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
			}else{
				if($('#td-9').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-10').find('div').length == 0){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-11').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-14').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-15').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-16').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-17').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
				if($('#td-18').find('div').length < 2){
					IQB.alert('合同资料信息不完善，无法审核');
					return false;
				};
			}
			var option = {}
			option.orderId = window.procBizId;
			option.isLoan = parseInt($('#isLoan option:selected').val());
			IQB.post(urls['cfm'] + '/pledge/savePledgeInfo', option, function(ret){
				$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
			});
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/pledge/getPledgeInfo', {orderId: window.procBizId}, function(ret){
				result = ret.iqbResult.result;
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#plate').text(result.plate);
				$('#groupName').text(Formatter.groupName(result.bizType));
				$('#type').val(result.bizType);
				if(result.bizType == '2001'){
					$('.new-li').show();
					$('.old-li').hide();
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{
					$('.old-li').show();
					$('.new-li').hide();
					$('.old-tr').show();
					$('.new-tr').hide();
				}
				$('#applyAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
//				$('#applyAmt').text(Formatter.money(result.applyAmt));
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				$('#riskRetRemark').text(result.riskRetRemark);	
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.ipiRemark)));
				$('#isLoan').val(result.isLoan);
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [0,2,3,4,5,6,28]}, function(result){
				$('.projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('.guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('.guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.businessApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					//_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
				}
			});
		},
		showFile2: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [7,8,9,10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]}, function(result){
				$('.projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('.guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('.guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				var is2 = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.businessApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is2 = true;
					}
				});
				if(is2){
					//_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
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
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/pledge');
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
					if(_this.cache.viewer.viewerTwo){
						_this.cache.viewer.viewerTwo.update();
					}else{  
						_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
					};
				});
			});	 
		},
		uploadFile2: function(){
			var files = $('#file2').get(0).files;
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
				$('#file2').val('');
				IQB.alert('格式不支持');
				return false;
			}
			
			$('#btn-upload').prop('disabled', true);
			$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
			$('#uploadForm2').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/pledge');
			IQB.postForm($('#uploadForm2'), function(result){
				var fileType = $('#file2').attr('fileType');	
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
					$('#file2').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(_this.cache.viewer.viewerOne){
						_this.cache.viewer.viewerOne.update();
					}else{  
						_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					};
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.showFile2();
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			$('#btn-uploadTypezero').on('click', function(){$('#file2').attr('fileType', 0);$('#file2').click();});
			$('#btn-uploadTypeTwo').on('click', function(){$('#file2').attr('fileType', 2);$('#file2').click();});
			$('#btn-uploadTypeThree').on('click', function(){$('#file2').attr('fileType', 3);$('#file2').click();});
			$('#btn-uploadTypeFour').on('click', function(){$('#file2').attr('fileType', 4);$('#file2').click();});
			$('#btn-uploadTypeFive').on('click', function(){$('#file2').attr('fileType', 5);$('#file2').click();});
			$('#btn-uploadTypeSix').on('click', function(){$('#file2').attr('fileType', 6);$('#file2').click();});
			$('#btn-uploadTypeSeven').on('click', function(){$('#file').attr('fileType', 7);$('#file').click();});
			$('#btn-uploadTypeEight').on('click', function(){$('#file').attr('fileType', 8);$('#file').click();});
			$('#btn-uploadTypeNine').on('click', function(){$('#file').attr('fileType', 9);$('#file').click();});
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
			$('#btn-uploadTypeTwentyEight').on('click', function(){$('#file2').attr('fileType', 28);$('#file2').click();});

			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});
			$('#file2').on('change', function(){var fileName = $('#file2').val();if(fileName){_this.uploadFile2();}});
		}
	}
	return _this;
}();

$(function() {
	//页面初始化
	IQB.businessApproval.init();
	//禁用浏览器右击菜单,可选
	document.oncontextmenu = function(){return false;}
});


