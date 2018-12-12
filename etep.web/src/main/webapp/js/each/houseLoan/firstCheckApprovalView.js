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

/*procBizId = 'CDHTC3001170517007';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.firstCheckApprovalView');
IQB.firstCheckApprovalView = function(){
	var _this = {
		cache: {
			viewer: {},
			i:1,
			creditType:''
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
			$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
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
				$('#bankCard').text(result.bankCard);
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#orderItems').text(result.orderItems);
				$('#loanDate').text(Formatter.ifNull(result.loanDate));
				$('#loanAmt1').text(Formatter.money(result.loanAmt));
				/*var proInfo = result.proInfo.split(';');
				$('#orderFlag').text(_this.formatterOrderFlag(proInfo[0]));
				$('#startTime').text(proInfo[1]);
				$('#endTime').text(proInfo[2]);*/
			});
			//紧急联系人信息
			IQB.post(urls['cfm'] + '/dandelion/get_risk', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#address').text(Formatter.ifNull(result.address));
				$('#firmName').text(Formatter.ifNull(result.companyName));
				$('#firmAddress').text(Formatter.ifNull(result.companyAddress));
				$('#firmPhone').text(Formatter.ifNull(result.companyPhone));
				$('#workmateOne').text(Formatter.ifNull(result.colleagues1));
				$('#workmateOneTel').text(Formatter.ifNull(result.tel1));
				$('#workmateTwo').text(Formatter.ifNull(result.colleagues2));
				$('#workmateTwoTel').text(Formatter.ifNull(result.tel2));
				
				$('#relationOne').text(Formatter.ifNull(result.relation1));
				$('#reNameOne').text(Formatter.ifNull(result.rname1));
				$('#sexOne').text(Formatter.ifNull(result.sex1));
				$('#mobileOne').text(Formatter.ifNull(result.phone1));
				
				$('#relationTwo').text(Formatter.ifNull(result.relation2));
				$('#reNameTwo').text(Formatter.ifNull(result.rname2));
				$('#sexTwo').text(Formatter.ifNull(result.sex2));
				$('#mobileTwo').text(Formatter.ifNull(result.phone2));
				
				$('#relationThree').text(Formatter.ifNull(result.relation3));
				$('#reNameThree').text(Formatter.ifNull(result.rname3));
				$('#sexThree').text(Formatter.ifNull(result.sex3));
				$('#mobileThree').text(Formatter.ifNull(result.phone3));
				
				$('#creditNumber').text(Formatter.ifNull(result.creditNo));
				$('#creditPass').text(Formatter.ifNull(result.creditPasswd));
				$('#creditCode').text(Formatter.ifNull(result.creditCode));
			});
		},
		formatterOrderFlag:function(val){
			if(val != null){
				if(val == 1){
					return '是';
				}else{
					return '否';
				}
			}
			return '';
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
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1,2,3,4,101,102,5,6,7,8,9,11,12,13,14,15,16,17,18,19,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,
			                                                                                    21,22,23,24,25,26,27,28,29,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,251,252,253,230,231,232,
			                                                                                    31,32,33,34,35,36,37,38,39,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,351,352,353,330,331,332,
			                                                                                    41,42,43,44,45,46,47,48,49,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,451,452,453,430,431,432,
			                                                                                    51,52,53,54,55,56,57,58,59,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,551,552,553,530,531,532,
			                                                                                    61,62,63,64,65,66,67,68,69,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,651,652,653,630,631,632,
			                                                                                    71,72,73,74,75,76,77,78,79,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,751,752,753,730,731,732,
			                                                                                    ]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
					_this.cache.viewer.viewerThree = new Viewer(document.getElementById('viewerThree'), {});
					_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
					_this.cache.viewer.viewerFive = new Viewer(document.getElementById('viewerFive'), {});
					_this.cache.viewer.viewerSix = new Viewer(document.getElementById('viewerSix'), {});
					_this.cache.viewer.viewerSeven = new Viewer(document.getElementById('viewerSeven'), {});
					_this.cache.viewer.viewerEight = new Viewer(document.getElementById('viewerEight'), {});
					_this.cache.viewer.viewerNine = new Viewer(document.getElementById('viewerNine'), {});
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.firstCheckApprovalView.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					if(_this.cache.viewer.viewerTwo){
						_this.cache.viewer.viewerTwo.update();
					}else{
						_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
					}
					if(_this.cache.viewer.viewerThree){   
						_this.cache.viewer.viewerThree.update();
					}else{
						_this.cache.viewer.viewerThree = new Viewer(document.getElementById('viewerThree'), {});
					}
					if(_this.cache.viewer.viewerFour){
						_this.cache.viewer.viewerFour.update();
					}else{
						_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
					}
					if(_this.cache.viewer.viewerFive){
						_this.cache.viewer.viewerFive.update();
					}else{
						_this.cache.viewer.viewerFive = new Viewer(document.getElementById('viewerFive'), {});
					}
					if(_this.cache.viewer.viewerSix){
						_this.cache.viewer.viewerSix.update();
					}else{
						_this.cache.viewer.viewerSix = new Viewer(document.getElementById('viewerSix'), {});
					}
					if(_this.cache.viewer.viewerSeven){
						_this.cache.viewer.viewerSeven.update();
					}else{
						_this.cache.viewer.viewerSeven = new Viewer(document.getElementById('viewerSeven'), {});
					}
					if(_this.cache.viewer.viewerEight){
						_this.cache.viewer.viewerEight.update();
					}else{
						_this.cache.viewer.viewerEight = new Viewer(document.getElementById('viewerEight'), {});
					}
					if(_this.cache.viewer.viewerNine){
						_this.cache.viewer.viewerNine.update();
					}else{
						_this.cache.viewer.viewerNine = new Viewer(document.getElementById('viewerEight'), {});
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
		forInfo: function(){
			$('.infos').hide();
			$('.borrowerInfo').show();
			$('.forInfo').click(function(){
				var href = $(this).attr('href');
				$('.infos').hide();
				$('.'+href).show();
			});
		},
		showInfo : function(){
			IQB.post(urls['cfm'] + '/dandelion/get_designated_person_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				//$('#projectName').val(result.icie.projectName);
				//信贷类型
				$('.creditType').text(Formatter.creditType(result.icie.creditType));
				_this.cache.creditType = result.icie.creditType;
				//额度建议及备注
				$('#limitAdvise').val(result.icie.amtAdvice);
				$('#remark').val(result.icie.remark);
				//项目名称及其他信息
				$('#projectName').text(Formatter.ifNull(result.icie.projectName));
				$('#borrow').text(Formatter.borrowTogether(result.icie.borrowTogether));
				$('#borrowName').text(Formatter.ifNull(result.icie.borrTogetherName));
				//担保人个数
				var num = result.icie.guarantorNum;
				switch (num)
				{
				case 2:
					$('.bondsmanInfo-2').show();
				  break;
				case 3:
					$('.bondsmanInfo-2').show();
					$('.bondsmanInfo-3').show();
				  break;
				case 4:
					$('.bondsmanInfo-2').show();
					$('.bondsmanInfo-3').show();
					$('.bondsmanInfo-4').show();
				  break;
				case 5:
					$('.bondsmanInfo-2').show();
					$('.bondsmanInfo-3').show();
					$('.bondsmanInfo-4').show();
					$('.bondsmanInfo-5').show();
				  break;
				default:
				}
				//信贷类型及借款信息
				var creditInfo = result.icie.creditInfoX;
				var creditHtml = '';
				for(var i=0;i<creditInfo.length;i++){
					creditHtml += "<tr><td><span class='text-muted'>"+creditInfo[i].creditType+"</span></td><td><span class='text-muted'>"+Formatter.ifNull(creditInfo[i].loanAmt)+"</span></td></tr>"
				}
				$('#loanTable').append(creditHtml);
				//担保人信息
				var signGuarant = result.icie.signGuarantCopy;
				var creditHtml = '';
				for(var i=0;i<signGuarant.length;i++){
					creditHtml += "<tr>"+
					"<td><span class='text-muted'>"+Formatter.userType(signGuarant[i].userType)+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].name+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].idCardNo+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].mobile+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].bankCardNo+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].companyName+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].companyAddress+"</span></td>"+
					"<td><span class='text-muted'>"+signGuarant[i].companyPhone+"</span></td>"+
					"</tr>"
				}
				$('#bondsTable').append(creditHtml);
				//借款金额和产品方案
				$('#lendAmt').text(Formatter.money(result.orderAmt));
				$('#plan').text(result.planShortName);
				
				$('#preAmount').text(Formatter.money(result.rap.sfkTotalAmt));
				$('#downPayment').text(Formatter.money(result.rap.sfkAmt));
				$('#margin').text(Formatter.money(result.rap.bzAmt));
				$('#serviceFee').text(Formatter.money(result.rap.fwAmt));
				$('#feeAmount').text(Formatter.money(result.rap.ssxAmt));
				$('#orderItems2').text(result.rap.qs);
				$('#monthInterest').text(Formatter.money(result.rap.ygAmt));
				$('#superAmt').text(Formatter.money(result.rap.sbAmt));
				$('#loanAmt').text(Formatter.money(result.rap.fkAmt));
			})
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			//回显项目信息（查询指派人担保人个数,信贷类型，共借人信息）
			_this.showInfo();
			
            //担保人信息
			$('.bondsmanInfoBtn').hide();
			
			//tab页相关
			$('#tab li').click(function(){
				_this.forInfo();
			});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			//借款人信息
			$('#btn-uploadTypeFive').on('click', function(){$('#file').attr('fileType', 5);$('#file').click();});
			$('#btn-uploadTypeSix').on('click', function(){$('#file').attr('fileType', 6);$('#file').click();});
			$('#btn-uploadTypeSeven').on('click', function(){$('#file').attr('fileType', 7);$('#file').click();});
			$('#btn-uploadTypeEight').on('click', function(){$('#file').attr('fileType', 8);$('#file').click();});
			
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.firstCheckApprovalView.init();
});


