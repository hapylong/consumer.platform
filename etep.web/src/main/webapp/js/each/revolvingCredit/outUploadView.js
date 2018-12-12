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

$package('IQB.outUploadView');
IQB.outUploadView = function(){
	var _this = {
		cache: {
			viewer: {},
			i : 1
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
				$('#merchName').text(Formatter.ifNull(result.merchantName));
				$('#bizType').text(Formatter.bizType(result.bizType));
				$('#realName').text(Formatter.ifNull(result.realName));
				$('#regId').text(Formatter.ifNull(result.regId));
				$('#idCard').text(Formatter.ifNull(result.idCard));
				$('#bankCardName').text(IQB.formatterDictTypeT(result.bankName,'BANK_NAME'));
				$('#bankCard').text(Formatter.ifNull(result.bankCard));
				$('#bankMobile').text(Formatter.ifNull(result.bankMobile));
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#orderItems').text(Formatter.ifNull(result.orderItems));
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
				
				$('#relationOne').text(IQB.formatterDictTypeT(result.relation1,'SECTORS'));
				$('#reNameOne').text(Formatter.ifNull(result.rname1));
				$('#sexOne').text(IQB.formatterDictTypeT(result.sex1,'sexN'));
				$('#mobileOne').text(Formatter.ifNull(result.phone1));
				
				$('#relationTwo').text(IQB.formatterDictTypeT(result.relation2,'SECTORS'));
				$('#reNameTwo').text(Formatter.ifNull(result.rname2));
				$('#sexTwo').text(IQB.formatterDictTypeT(result.sex2,'sexN'));
				$('#mobileTwo').text(Formatter.ifNull(result.phone2));
				
				$('#relationThree').text(IQB.formatterDictTypeT(result.relation3,'SECTORS'));
				$('#reNameThree').text(Formatter.ifNull(result.rname3));
				$('#sexThree').text(IQB.formatterDictTypeT(result.sex3,'sexN'));
				$('#mobileThree').text(Formatter.ifNull(result.phone3));
				
				$('#address').text(Formatter.ifNull(result.address));
				$('#firmAddress').text(Formatter.ifNull(result.companyAddress));
				$('#firmName').text(Formatter.ifNull(result.companyName));
				$('#firmPhone').text(Formatter.ifNull(result.companyPhone));
				$('#creditNumber').text(Formatter.ifNull(result.creditNo));
				$('#creditPass').text(Formatter.ifNull(result.creditPasswd));
				$('#creditCode').text(Formatter.ifNull(result.creditCode));
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
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1,2,3,4,101,102,103,104,105,106,11,12,13,14,15,16,17,18,19,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,
			                                                                                    21,22,23,24,25,26,27,28,29,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,251,252,253,230,231,232,232,233,234,235,236,237,238,239,240,
			                                                                                    31,32,33,34,35,36,37,38,39,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,351,352,353,330,331,332,332,333,334,335,336,337,338,339,340,
			                                                                                    41,42,43,44,45,46,47,48,49,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,451,452,453,430,431,432,432,433,434,435,436,437,438,439,440,
			                                                                                    51,52,53,54,55,56,57,58,59,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,551,552,553,530,531,532,532,533,534,535,536,537,538,539,540,
			                                                                                    61,62,63,64,65,66,67,68,69,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,651,652,653,630,631,632,632,633,634,635,636,637,638,639,640,
			                                                                                    71,72,73,74,75,76,77,78,79,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,751,752,753,730,731,732,732,733,734,735,736,737,738,739,740,
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.outUploadView.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		addBondsman : function(){
			var addBondsman = _this.cache.i;
			if(addBondsman < 5){
				IQB.confirm('确定要增加担保人信息吗？',function(){
					_this.cache.i = _this.cache.i+1;
					$('.bondsmanInfo-' + _this.cache.i).show();
				},null);
			}else {
				IQB.alert('最多可填写5个担保人信息！');
			}
		},
		showInfo : function(){
			IQB.post(urls['cfm'] + '/dandelion/get_designated_person_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#projectName').val(result.icie.projectName);
				//信贷类型
				$('#creditType').text(Formatter.creditType(result.icie.creditType));
				_this.cache.creditType = result.icie.creditType;
				//额度建议及备注
				$('#limitAdvise').val(result.icie.amtAdvice);
				$('#remark').val(result.icie.remark);
				//项目名称及其他信息
				$('#projectName').text(result.icie.projectName);
				$('#borrow').text(_this.formatterOrderFlag(result.icie.borrowTogether));
				//押品信息
				$('#totalValue').text(Formatter.ifNull(result.icie.mortgageTotalAmt));
        		$('#financingLimit').text(Formatter.ifNull(result.icie.adviceAmt));
				if(result.icie.borrowTogether == 1){
					$('.borrowNameAbout').show();
					$('#borrowName').text(Formatter.ifNull(result.icie.borrTogetherName));
				}else{
					$('.borrowNameAbout').hide();
				}
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
				//押品总额与可融资额度
				
				//信贷类型及借款信息
				var creditInfo = eval(result.icie.mortgageInfo);
				var creditHtml = '';
				if(creditInfo != '' && creditInfo != null){
					for(var i=0;i<creditInfo.length;i++){
						creditHtml += "<tr>"+
										"<td><span class='text-muted'>"+IQB.formatterDictTypeT(creditInfo[i].kind,'KIND')+"</span></td>"+
										"<td><span class='text-muted'>"+creditInfo[i].num+"</span></td>"+
										"<td><span class='text-muted'>"+creditInfo[i].name+"</span></td>"+
										"<td><span class='text-muted'>"+creditInfo[i].remark+"</span></td>"+
										"<td><span class='text-muted'>"+creditInfo[i].unitPrice+"</span></td>"+
										"<td><span class='text-muted'>"+creditInfo[i].allPrice+"</span></td>"+
										"</tr>"
					}
				}
				$('#loanTable').append(creditHtml);
				//担保人信息
				var signGuarant = result.icie.signGuarantCopy;
				var creditHtml = '';
				if(signGuarant != '' && signGuarant != null){
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
				}
				$('#bondsTable').append(creditHtml);
			})
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
            //增加担保人信息
			$('#btn-addBondsman').click(function(){_this.addBondsman();});
			$('.bondsmanInfoBtn').hide();
			//担保人信息查询
			_this.showInfo();
			//tab页相关
			$('#tab li').click(function(){
				_this.forInfo();
			});

			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			//借款人信息
			$('#btn-uploadType1-1').on('click', function(){$('#file').attr('fileType', 11);$('#file').click();});
			$('#btn-uploadType1-2').on('click', function(){$('#file').attr('fileType', 12);$('#file').click();});
			$('#btn-uploadType1-3').on('click', function(){$('#file').attr('fileType', 13);$('#file').click();});
			$('#btn-uploadType1-4').on('click', function(){$('#file').attr('fileType', 14);$('#file').click();});
			$('#btn-uploadType1-5').on('click', function(){$('#file').attr('fileType', 15);$('#file').click();});
			$('#btn-uploadType1-6').on('click', function(){$('#file').attr('fileType', 16);$('#file').click();});
			$('#btn-uploadType1-7').on('click', function(){$('#file').attr('fileType', 17);$('#file').click();});
			$('#btn-uploadType1-8').on('click', function(){$('#file').attr('fileType', 18);$('#file').click();});
			$('#btn-uploadType1-9').on('click', function(){$('#file').attr('fileType', 19);$('#file').click();});
			$('#btn-uploadType1-10').on('click', function(){$('#file').attr('fileType', 110);$('#file').click();});
			$('#btn-uploadType1-11').on('click', function(){$('#file').attr('fileType', 111);$('#file').click();});
			$('#btn-uploadType1-12').on('click', function(){$('#file').attr('fileType', 112);$('#file').click();});
			$('#btn-uploadType1-13').on('click', function(){$('#file').attr('fileType', 113);$('#file').click();});
			$('#btn-uploadType1-14').on('click', function(){$('#file').attr('fileType', 114);$('#file').click();});
			$('#btn-uploadType1-15').on('click', function(){$('#file').attr('fileType', 115);$('#file').click();});
			$('#btn-uploadType1-16').on('click', function(){$('#file').attr('fileType', 116);$('#file').click();});
			$('#btn-uploadType1-17').on('click', function(){$('#file').attr('fileType', 117);$('#file').click();});
			$('#btn-uploadType1-18').on('click', function(){$('#file').attr('fileType', 118);$('#file').click();});
			$('#btn-uploadType1-19').on('click', function(){$('#file').attr('fileType', 119);$('#file').click();});
			$('#btn-uploadType1-20').on('click', function(){$('#file').attr('fileType', 120);$('#file').click();});
			$('#btn-uploadType1-21').on('click', function(){$('#file').attr('fileType', 121);$('#file').click();});
			$('#btn-uploadType1-22').on('click', function(){$('#file').attr('fileType', 122);$('#file').click();});
			$('#btn-uploadType1-23').on('click', function(){$('#file').attr('fileType', 123);$('#file').click();});
			$('#btn-uploadType1-24').on('click', function(){$('#file').attr('fileType', 124);$('#file').click();});
			$('#btn-uploadType1-25').on('click', function(){$('#file').attr('fileType', 125);$('#file').click();});
			$('#btn-uploadType1-26').on('click', function(){$('#file').attr('fileType', 126);$('#file').click();});
			$('#btn-uploadType1-27').on('click', function(){$('#file').attr('fileType', 127);$('#file').click();});
			$('#btn-uploadType1-28').on('click', function(){$('#file').attr('fileType', 128);$('#file').click();});
			$('#btn-uploadType1-29').on('click', function(){$('#file').attr('fileType', 129);$('#file').click();});
			$('#btn-uploadType1-30').on('click', function(){$('#file').attr('fileType', 130);$('#file').click();});
			$('#btn-uploadType1-31').on('click', function(){$('#file').attr('fileType', 131);$('#file').click();});
			//共签人信息
			$('#btn-uploadType2-1').on('click', function(){$('#file').attr('fileType', 21);$('#file').click();});
			$('#btn-uploadType2-2').on('click', function(){$('#file').attr('fileType', 22);$('#file').click();});
			$('#btn-uploadType2-3').on('click', function(){$('#file').attr('fileType', 23);$('#file').click();});
			$('#btn-uploadType2-4').on('click', function(){$('#file').attr('fileType', 24);$('#file').click();});
			$('#btn-uploadType2-5').on('click', function(){$('#file').attr('fileType', 25);$('#file').click();});
			$('#btn-uploadType2-6').on('click', function(){$('#file').attr('fileType', 26);$('#file').click();});
			$('#btn-uploadType2-7').on('click', function(){$('#file').attr('fileType', 27);$('#file').click();});
			$('#btn-uploadType2-8').on('click', function(){$('#file').attr('fileType', 28);$('#file').click();});
			$('#btn-uploadType2-9').on('click', function(){$('#file').attr('fileType', 29);$('#file').click();});
			$('#btn-uploadType2-10').on('click', function(){$('#file').attr('fileType', 210);$('#file').click();});
			$('#btn-uploadType2-11').on('click', function(){$('#file').attr('fileType', 211);$('#file').click();});
			$('#btn-uploadType2-12').on('click', function(){$('#file').attr('fileType', 212);$('#file').click();});
			$('#btn-uploadType2-13').on('click', function(){$('#file').attr('fileType', 213);$('#file').click();});
			$('#btn-uploadType2-14').on('click', function(){$('#file').attr('fileType', 214);$('#file').click();});
			$('#btn-uploadType2-15').on('click', function(){$('#file').attr('fileType', 215);$('#file').click();});
			$('#btn-uploadType2-16').on('click', function(){$('#file').attr('fileType', 216);$('#file').click();});
			$('#btn-uploadType2-17').on('click', function(){$('#file').attr('fileType', 217);$('#file').click();});
			$('#btn-uploadType2-18').on('click', function(){$('#file').attr('fileType', 218);$('#file').click();});
			$('#btn-uploadType2-19').on('click', function(){$('#file').attr('fileType', 219);$('#file').click();});
			$('#btn-uploadType2-20').on('click', function(){$('#file').attr('fileType', 220);$('#file').click();});
			$('#btn-uploadType2-21').on('click', function(){$('#file').attr('fileType', 221);$('#file').click();});
			$('#btn-uploadType2-22').on('click', function(){$('#file').attr('fileType', 222);$('#file').click();});
			$('#btn-uploadType2-23').on('click', function(){$('#file').attr('fileType', 223);$('#file').click();});
			$('#btn-uploadType2-24').on('click', function(){$('#file').attr('fileType', 224);$('#file').click();});
			$('#btn-uploadType2-25').on('click', function(){$('#file').attr('fileType', 225);$('#file').click();});
			$('#btn-uploadType2-26').on('click', function(){$('#file').attr('fileType', 226);$('#file').click();});
			$('#btn-uploadType2-27').on('click', function(){$('#file').attr('fileType', 227);$('#file').click();});
			$('#btn-uploadType2-28').on('click', function(){$('#file').attr('fileType', 228);$('#file').click();});
			$('#btn-uploadType2-29').on('click', function(){$('#file').attr('fileType', 229);$('#file').click();});
			$('#btn-uploadType2-30').on('click', function(){$('#file').attr('fileType', 230);$('#file').click();});
			$('#btn-uploadType2-31').on('click', function(){$('#file').attr('fileType', 231);$('#file').click();});
			//担保人信息1
			$('#btn-uploadType3-1').on('click', function(){$('#file').attr('fileType', 31);$('#file').click();});
			$('#btn-uploadType3-2').on('click', function(){$('#file').attr('fileType', 32);$('#file').click();});
			$('#btn-uploadType3-3').on('click', function(){$('#file').attr('fileType', 33);$('#file').click();});
			$('#btn-uploadType3-4').on('click', function(){$('#file').attr('fileType', 34);$('#file').click();});
			$('#btn-uploadType3-5').on('click', function(){$('#file').attr('fileType', 35);$('#file').click();});
			$('#btn-uploadType3-6').on('click', function(){$('#file').attr('fileType', 36);$('#file').click();});
			$('#btn-uploadType3-7').on('click', function(){$('#file').attr('fileType', 37);$('#file').click();});
			$('#btn-uploadType3-8').on('click', function(){$('#file').attr('fileType', 38);$('#file').click();});
			$('#btn-uploadType3-9').on('click', function(){$('#file').attr('fileType', 39);$('#file').click();});
			$('#btn-uploadType3-10').on('click', function(){$('#file').attr('fileType', 310);$('#file').click();});
			$('#btn-uploadType3-11').on('click', function(){$('#file').attr('fileType', 311);$('#file').click();});
			$('#btn-uploadType3-12').on('click', function(){$('#file').attr('fileType', 312);$('#file').click();});
			$('#btn-uploadType3-13').on('click', function(){$('#file').attr('fileType', 313);$('#file').click();});
			$('#btn-uploadType3-14').on('click', function(){$('#file').attr('fileType', 314);$('#file').click();});
			$('#btn-uploadType3-15').on('click', function(){$('#file').attr('fileType', 315);$('#file').click();});
			$('#btn-uploadType3-16').on('click', function(){$('#file').attr('fileType', 316);$('#file').click();});
			$('#btn-uploadType3-17').on('click', function(){$('#file').attr('fileType', 317);$('#file').click();});
			$('#btn-uploadType3-18').on('click', function(){$('#file').attr('fileType', 318);$('#file').click();});
			$('#btn-uploadType3-19').on('click', function(){$('#file').attr('fileType', 319);$('#file').click();});
			$('#btn-uploadType3-20').on('click', function(){$('#file').attr('fileType', 320);$('#file').click();});
			$('#btn-uploadType3-21').on('click', function(){$('#file').attr('fileType', 321);$('#file').click();});
			$('#btn-uploadType3-22').on('click', function(){$('#file').attr('fileType', 322);$('#file').click();});
			$('#btn-uploadType3-23').on('click', function(){$('#file').attr('fileType', 323);$('#file').click();});
			$('#btn-uploadType3-24').on('click', function(){$('#file').attr('fileType', 324);$('#file').click();});
			$('#btn-uploadType3-25').on('click', function(){$('#file').attr('fileType', 325);$('#file').click();});
			$('#btn-uploadType3-26').on('click', function(){$('#file').attr('fileType', 326);$('#file').click();});
			$('#btn-uploadType3-27').on('click', function(){$('#file').attr('fileType', 327);$('#file').click();});
			$('#btn-uploadType3-28').on('click', function(){$('#file').attr('fileType', 328);$('#file').click();});
			$('#btn-uploadType3-29').on('click', function(){$('#file').attr('fileType', 329);$('#file').click();});
			$('#btn-uploadType3-30').on('click', function(){$('#file').attr('fileType', 330);$('#file').click();});
			$('#btn-uploadType3-31').on('click', function(){$('#file').attr('fileType', 331);$('#file').click();});
			//担保人信息2
			$('#btn-uploadType4-1').on('click', function(){$('#file').attr('fileType', 41);$('#file').click();});
			$('#btn-uploadType4-2').on('click', function(){$('#file').attr('fileType', 42);$('#file').click();});
			$('#btn-uploadType4-3').on('click', function(){$('#file').attr('fileType', 43);$('#file').click();});
			$('#btn-uploadType4-4').on('click', function(){$('#file').attr('fileType', 44);$('#file').click();});
			$('#btn-uploadType4-5').on('click', function(){$('#file').attr('fileType', 45);$('#file').click();});
			$('#btn-uploadType4-6').on('click', function(){$('#file').attr('fileType', 46);$('#file').click();});
			$('#btn-uploadType4-7').on('click', function(){$('#file').attr('fileType', 47);$('#file').click();});
			$('#btn-uploadType4-8').on('click', function(){$('#file').attr('fileType', 48);$('#file').click();});
			$('#btn-uploadType4-9').on('click', function(){$('#file').attr('fileType', 49);$('#file').click();});
			$('#btn-uploadType4-10').on('click', function(){$('#file').attr('fileType', 410);$('#file').click();});
			$('#btn-uploadType4-11').on('click', function(){$('#file').attr('fileType', 411);$('#file').click();});
			$('#btn-uploadType4-12').on('click', function(){$('#file').attr('fileType', 412);$('#file').click();});
			$('#btn-uploadType4-13').on('click', function(){$('#file').attr('fileType', 413);$('#file').click();});
			$('#btn-uploadType4-14').on('click', function(){$('#file').attr('fileType', 414);$('#file').click();});
			$('#btn-uploadType4-15').on('click', function(){$('#file').attr('fileType', 415);$('#file').click();});
			$('#btn-uploadType4-16').on('click', function(){$('#file').attr('fileType', 416);$('#file').click();});
			$('#btn-uploadType4-17').on('click', function(){$('#file').attr('fileType', 417);$('#file').click();});
			$('#btn-uploadType4-18').on('click', function(){$('#file').attr('fileType', 418);$('#file').click();});
			$('#btn-uploadType4-19').on('click', function(){$('#file').attr('fileType', 419);$('#file').click();});
			$('#btn-uploadType4-20').on('click', function(){$('#file').attr('fileType', 420);$('#file').click();});
			$('#btn-uploadType4-21').on('click', function(){$('#file').attr('fileType', 421);$('#file').click();});
			$('#btn-uploadType4-22').on('click', function(){$('#file').attr('fileType', 422);$('#file').click();});
			$('#btn-uploadType4-23').on('click', function(){$('#file').attr('fileType', 423);$('#file').click();});
			$('#btn-uploadType4-24').on('click', function(){$('#file').attr('fileType', 424);$('#file').click();});
			$('#btn-uploadType4-25').on('click', function(){$('#file').attr('fileType', 425);$('#file').click();});
			$('#btn-uploadType4-26').on('click', function(){$('#file').attr('fileType', 426);$('#file').click();});
			$('#btn-uploadType4-27').on('click', function(){$('#file').attr('fileType', 427);$('#file').click();});
			$('#btn-uploadType4-28').on('click', function(){$('#file').attr('fileType', 428);$('#file').click();});
			$('#btn-uploadType4-29').on('click', function(){$('#file').attr('fileType', 429);$('#file').click();});
			$('#btn-uploadType4-30').on('click', function(){$('#file').attr('fileType', 430);$('#file').click();});
			$('#btn-uploadType4-31').on('click', function(){$('#file').attr('fileType', 431);$('#file').click();});
			//担保人信息3
			$('#btn-uploadType5-1').on('click', function(){$('#file').attr('fileType', 51);$('#file').click();});
			$('#btn-uploadType5-2').on('click', function(){$('#file').attr('fileType', 52);$('#file').click();});
			$('#btn-uploadType5-3').on('click', function(){$('#file').attr('fileType', 53);$('#file').click();});
			$('#btn-uploadType5-4').on('click', function(){$('#file').attr('fileType', 54);$('#file').click();});
			$('#btn-uploadType5-5').on('click', function(){$('#file').attr('fileType', 55);$('#file').click();});
			$('#btn-uploadType5-6').on('click', function(){$('#file').attr('fileType', 56);$('#file').click();});
			$('#btn-uploadType5-7').on('click', function(){$('#file').attr('fileType', 57);$('#file').click();});
			$('#btn-uploadType5-8').on('click', function(){$('#file').attr('fileType', 58);$('#file').click();});
			$('#btn-uploadType5-9').on('click', function(){$('#file').attr('fileType', 59);$('#file').click();});
			$('#btn-uploadType5-10').on('click', function(){$('#file').attr('fileType', 510);$('#file').click();});
			$('#btn-uploadType5-11').on('click', function(){$('#file').attr('fileType', 511);$('#file').click();});
			$('#btn-uploadType5-12').on('click', function(){$('#file').attr('fileType', 512);$('#file').click();});
			$('#btn-uploadType5-13').on('click', function(){$('#file').attr('fileType', 513);$('#file').click();});
			$('#btn-uploadType5-14').on('click', function(){$('#file').attr('fileType', 514);$('#file').click();});
			$('#btn-uploadType5-15').on('click', function(){$('#file').attr('fileType', 515);$('#file').click();});
			$('#btn-uploadType5-16').on('click', function(){$('#file').attr('fileType', 516);$('#file').click();});
			$('#btn-uploadType5-17').on('click', function(){$('#file').attr('fileType', 517);$('#file').click();});
			$('#btn-uploadType5-18').on('click', function(){$('#file').attr('fileType', 518);$('#file').click();});
			$('#btn-uploadType5-19').on('click', function(){$('#file').attr('fileType', 519);$('#file').click();});
			$('#btn-uploadType5-20').on('click', function(){$('#file').attr('fileType', 520);$('#file').click();});
			$('#btn-uploadType5-21').on('click', function(){$('#file').attr('fileType', 521);$('#file').click();});
			$('#btn-uploadType5-22').on('click', function(){$('#file').attr('fileType', 522);$('#file').click();});
			$('#btn-uploadType5-23').on('click', function(){$('#file').attr('fileType', 523);$('#file').click();});
			$('#btn-uploadType5-24').on('click', function(){$('#file').attr('fileType', 524);$('#file').click();});
			$('#btn-uploadType5-25').on('click', function(){$('#file').attr('fileType', 525);$('#file').click();});
			$('#btn-uploadType5-26').on('click', function(){$('#file').attr('fileType', 526);$('#file').click();});
			$('#btn-uploadType5-27').on('click', function(){$('#file').attr('fileType', 527);$('#file').click();});
			$('#btn-uploadType5-28').on('click', function(){$('#file').attr('fileType', 528);$('#file').click();});
			$('#btn-uploadType5-29').on('click', function(){$('#file').attr('fileType', 529);$('#file').click();});
			$('#btn-uploadType5-30').on('click', function(){$('#file').attr('fileType', 530);$('#file').click();});
			$('#btn-uploadType5-31').on('click', function(){$('#file').attr('fileType', 531);$('#file').click();});
			//担保人信息4
			$('#btn-uploadType6-1').on('click', function(){$('#file').attr('fileType', 61);$('#file').click();});
			$('#btn-uploadType6-2').on('click', function(){$('#file').attr('fileType', 62);$('#file').click();});
			$('#btn-uploadType6-3').on('click', function(){$('#file').attr('fileType', 63);$('#file').click();});
			$('#btn-uploadType6-4').on('click', function(){$('#file').attr('fileType', 64);$('#file').click();});
			$('#btn-uploadType6-5').on('click', function(){$('#file').attr('fileType', 65);$('#file').click();});
			$('#btn-uploadType6-6').on('click', function(){$('#file').attr('fileType', 66);$('#file').click();});
			$('#btn-uploadType6-7').on('click', function(){$('#file').attr('fileType', 67);$('#file').click();});
			$('#btn-uploadType6-8').on('click', function(){$('#file').attr('fileType', 68);$('#file').click();});
			$('#btn-uploadType6-9').on('click', function(){$('#file').attr('fileType', 69);$('#file').click();});
			$('#btn-uploadType6-10').on('click', function(){$('#file').attr('fileType', 610);$('#file').click();});
			$('#btn-uploadType6-11').on('click', function(){$('#file').attr('fileType', 611);$('#file').click();});
			$('#btn-uploadType6-12').on('click', function(){$('#file').attr('fileType', 612);$('#file').click();});
			$('#btn-uploadType6-13').on('click', function(){$('#file').attr('fileType', 613);$('#file').click();});
			$('#btn-uploadType6-14').on('click', function(){$('#file').attr('fileType', 614);$('#file').click();});
			$('#btn-uploadType6-15').on('click', function(){$('#file').attr('fileType', 615);$('#file').click();});
			$('#btn-uploadType6-16').on('click', function(){$('#file').attr('fileType', 616);$('#file').click();});
			$('#btn-uploadType6-17').on('click', function(){$('#file').attr('fileType', 617);$('#file').click();});
			$('#btn-uploadType6-18').on('click', function(){$('#file').attr('fileType', 618);$('#file').click();});
			$('#btn-uploadType6-19').on('click', function(){$('#file').attr('fileType', 619);$('#file').click();});
			$('#btn-uploadType6-20').on('click', function(){$('#file').attr('fileType', 620);$('#file').click();});
			$('#btn-uploadType6-21').on('click', function(){$('#file').attr('fileType', 621);$('#file').click();});
			$('#btn-uploadType6-22').on('click', function(){$('#file').attr('fileType', 622);$('#file').click();});
			$('#btn-uploadType6-23').on('click', function(){$('#file').attr('fileType', 623);$('#file').click();});
			$('#btn-uploadType6-24').on('click', function(){$('#file').attr('fileType', 624);$('#file').click();});
			$('#btn-uploadType6-25').on('click', function(){$('#file').attr('fileType', 625);$('#file').click();});
			$('#btn-uploadType6-26').on('click', function(){$('#file').attr('fileType', 626);$('#file').click();});
			$('#btn-uploadType6-27').on('click', function(){$('#file').attr('fileType', 627);$('#file').click();});
			$('#btn-uploadType6-28').on('click', function(){$('#file').attr('fileType', 628);$('#file').click();});
			$('#btn-uploadType6-29').on('click', function(){$('#file').attr('fileType', 629);$('#file').click();});
			$('#btn-uploadType6-30').on('click', function(){$('#file').attr('fileType', 630);$('#file').click();});
			$('#btn-uploadType6-31').on('click', function(){$('#file').attr('fileType', 631);$('#file').click();});
			//担保人信息5
			$('#btn-uploadType7-1').on('click', function(){$('#file').attr('fileType', 71);$('#file').click();});
			$('#btn-uploadType7-2').on('click', function(){$('#file').attr('fileType', 72);$('#file').click();});
			$('#btn-uploadType7-3').on('click', function(){$('#file').attr('fileType', 73);$('#file').click();});
			$('#btn-uploadType7-4').on('click', function(){$('#file').attr('fileType', 74);$('#file').click();});
			$('#btn-uploadType7-5').on('click', function(){$('#file').attr('fileType', 75);$('#file').click();});
			$('#btn-uploadType7-6').on('click', function(){$('#file').attr('fileType', 76);$('#file').click();});
			$('#btn-uploadType7-7').on('click', function(){$('#file').attr('fileType', 77);$('#file').click();});
			$('#btn-uploadType7-8').on('click', function(){$('#file').attr('fileType', 78);$('#file').click();});
			$('#btn-uploadType7-9').on('click', function(){$('#file').attr('fileType', 79);$('#file').click();});
			$('#btn-uploadType7-10').on('click', function(){$('#file').attr('fileType', 710);$('#file').click();});
			$('#btn-uploadType7-11').on('click', function(){$('#file').attr('fileType', 711);$('#file').click();});
			$('#btn-uploadType7-12').on('click', function(){$('#file').attr('fileType', 712);$('#file').click();});
			$('#btn-uploadType7-13').on('click', function(){$('#file').attr('fileType', 713);$('#file').click();});
			$('#btn-uploadType7-14').on('click', function(){$('#file').attr('fileType', 714);$('#file').click();});
			$('#btn-uploadType7-15').on('click', function(){$('#file').attr('fileType', 715);$('#file').click();});
			$('#btn-uploadType7-16').on('click', function(){$('#file').attr('fileType', 716);$('#file').click();});
			$('#btn-uploadType7-17').on('click', function(){$('#file').attr('fileType', 717);$('#file').click();});
			$('#btn-uploadType7-18').on('click', function(){$('#file').attr('fileType', 718);$('#file').click();});
			$('#btn-uploadType7-19').on('click', function(){$('#file').attr('fileType', 719);$('#file').click();});
			$('#btn-uploadType7-20').on('click', function(){$('#file').attr('fileType', 720);$('#file').click();});
			$('#btn-uploadType7-21').on('click', function(){$('#file').attr('fileType', 721);$('#file').click();});
			$('#btn-uploadType7-22').on('click', function(){$('#file').attr('fileType', 722);$('#file').click();});
			$('#btn-uploadType7-23').on('click', function(){$('#file').attr('fileType', 723);$('#file').click();});
			$('#btn-uploadType7-24').on('click', function(){$('#file').attr('fileType', 724);$('#file').click();});
			$('#btn-uploadType7-25').on('click', function(){$('#file').attr('fileType', 725);$('#file').click();});
			$('#btn-uploadType7-26').on('click', function(){$('#file').attr('fileType', 726);$('#file').click();});
			$('#btn-uploadType7-27').on('click', function(){$('#file').attr('fileType', 727);$('#file').click();});
			$('#btn-uploadType7-28').on('click', function(){$('#file').attr('fileType', 728);$('#file').click();});
			$('#btn-uploadType7-29').on('click', function(){$('#file').attr('fileType', 729);$('#file').click();});
			$('#btn-uploadType7-30').on('click', function(){$('#file').attr('fileType', 730);$('#file').click();});
			$('#btn-uploadType7-31').on('click', function(){$('#file').attr('fileType', 731);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.outUploadView.init();
});


