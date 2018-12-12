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

$package('IQB.outUpload');
IQB.outUpload = function(){
	var _this = {
		cache: {
			viewer: {},
			i : 1,
			d :1,
			backFlag:'',//是否拒绝订单
			orderFlag:''//是否历史订单
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
				variableData.backFlag  = _this.cache.backFlag;
				variableData.orderFlag = "2";
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.backFlag=_this.cache.backFlag;
				bizData.orderFlag = "2";
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
			for(var i=0;i<$('.basicInfo').length;i++){
				if($($('.basicInfo')[i]).val() == ''){
					IQB.alert('基本信息不完善，无法审核');
					return false;
				}
			}
			for(var i=0;i<$('.applyInfo').length;i++){
				if($($('.applyInfo')[i]).val() == ''){
					IQB.alert('信息不完善，无法审核');
					return false;
				}
			}
			if($('#bankCardName').val() == ''){
				IQB.alert('请选择开户银行');
				return false;
			}
			if($('#borrow').val() == ''){
				IQB.alert('请选择是否共借人');
				return false;
			}
			if($('#borrow').val() == 1){
				if($('#borrowName').val() == ''){
					IQB.alert('请输入共借人姓名');
					return false;
				}
			}
			//保存派单处理人
			/*var option = {};
			option.id = 'liuxiaonan';
			option.text = '刘肖楠';
			option.orderId = window.procBizId;
			IQB.post(urls['rootUrl'] + '/dandelion/persist_design_person', option, function(result){
				if(result.success=="1") {
					
				} else {
					IQB.alert(result.retUserInfo);
				}
			});*/
			_this.saveCredit();
		},
		saveCredit:function(){
			var trLength = $("#bondsTable").find('tr').length-1;
        	var list = [];
			for (var i = 0; i<trLength; i++) {         
			list.push({});
			}
			for (var i = 1; i<list.length+1; i++) {
			list[i-1].userType = bondsTable.rows[i].cells[1].getElementsByTagName("SELECT")[0].value;
			list[i-1].name = bondsTable.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;
			list[i-1].idCardNo = bondsTable.rows[i].cells[3].getElementsByTagName("INPUT")[0].value;
			list[i-1].mobile = bondsTable.rows[i].cells[4].getElementsByTagName("INPUT")[0].value;
			list[i-1].bankCardNo = bondsTable.rows[i].cells[5].getElementsByTagName("INPUT")[0].value;
			list[i-1].companyName = bondsTable.rows[i].cells[6].getElementsByTagName("INPUT")[0].value;
			list[i-1].companyAddress = bondsTable.rows[i].cells[7].getElementsByTagName("INPUT")[0].value;
			list[i-1].companyPhone = bondsTable.rows[i].cells[8].getElementsByTagName("INPUT")[0].value;
			}
			//保存信贷类型
			var option = {
					'orderId' : window.procBizId,
					'creditType':$('#creditType').val(),
					'type':'1',
					'amtAdvice':$('#limitAdvise').val(),
					'remark':$('#remark').val(),
					'signGuarant':list
			}
			IQB.post(urls['cfm'] + '/dandelion/update_credit_type', option, function(result){
				var result = result.iqbResult.result;
				if(result == 'success'){
					_this.saveAmtAndTerms();
				}
			})
		},
		saveAmtAndTerms:function(){
			//保存订单金额 期数以及银行卡号
			var bankCard = $('#bankCard').val();
			var orderAmt = $('#orderAmt').val();
			var orderItems = $('#orderItems').val();
			var bankCardName = $('#bankCardName').val();
			var option = {
					'orderId' : window.procBizId,
					'bankCard':bankCard,
					'orderAmt':orderAmt,
					'orderItems':orderItems,
					'bankCardName':bankCardName
			}
			IQB.post(urls['cfm'] + '/dandelion/saveAmtAndItems', option, function(result){
				var result = result.iqbResult.result;
				if(result == 'success'){
					_this.saveRiskInfo();
				}
			})
		},
		saveRiskInfo:function(){
			//保存风控信息
			var option = {
					"realName": $('#realName').val(),			//姓名
					"regId": $('#regId').text(),		            //手机号码
					"idCard": $('#idCard').val(),	            //身份证号
				    "address": $('#address').val(),			    //常驻地址
				    "bankCard": $('#bankCard').val(),           //银行卡
				    "companyAddress": $('#firmAddress').val(),		//公司地址
				    "companyName": $('#firmName').val(),		//公司名称
				    "companyPhone": $('#firmPhone').val(),	    //公司电话
				    "creditNo": $('#creditNumber').val(),		//征信号
				    "creditPasswd": $('#creditPass').val(),		//征信密码
					"creditCode":$('#creditCode').val(),		//征信验证码
				    "merchantId": $('#merchName').text(),		//机构编码
					"proType":$('#bizType').text(),			    //产品类型
					"rName1": $('#reNameOne').val(),			//姓名1
				    "rName2": $('#reNameTwo').val(),			//姓名2
				    "rName3": $('#reNameThree').val(),			//姓名3
				    "phone1": $('#mobileOne').val(),			//手机号1
				    "phone2": $('#mobileTwo').val(),			//手机号2
				    "phone3": $('#mobileThree').val(),			//手机号3
					"relation1": $('#relationOne').val(),		//借款人关系1
				    "relation2": $('#relationTwo').val(),		//借款人关系2
				    "relation3": $('#relationThree').val(),		//借款人关系3
					"sex1": $('#sexOne').val(),					//性别1
				    "sex2": $('#sexTwo').val(),					//性别2
				    "sex3": $('#sexThree').val(),				//性别3
				    "colleagues1": $('#workmateOne').val(),		//同事1
				    "colleagues2": $('#workmateTwo').val(),		//同事2
				    "tel1": $('#workmateOneTel').val(),			//手机1
				    "tel2": $('#workmateTwoTel').val()			//手机2
			}
			IQB.post(urls['cfm'] + '/dandelion/saveRiskInfo', option, function(result){
				var result = result.iqbResult.result;
				if(result == 'success'){
					_this.saveCreAmt();
				}
			})
		},
		saveCreAmt:function(){
			//保存项目信息中的信贷类型
			var trLength = $("#loanTable").find('tr').length-1;
        	var list = [];
			for (var i = 0; i<trLength; i++) {         
			list.push({});
			}
			for (var i = 1; i<list.length+1; i++) {
			list[i-1].creditType = loanTable.rows[i].cells[1].getElementsByTagName("INPUT")[0].value;
			list[i-1].loanAmt = loanTable.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;;
			}
        	var data = {
        		'orderId':window.procBizId,
        		'creditType' : $('#creditType').val(),
        		'borrowTogether':$('#borrow').val(),
        		'borrTogetherName':$('#borrowName').val(),
        		'creditInfoX' : list,
        		'type':2
        	};
			IQB.post(urls['rootUrl'] + '/dandelion/update_credit_type', data, function(result){
				if(result.success == 1){
					_this.saveOrderInfo();
				}
			})
		},
		saveOrderInfo:function(){
			//保存基本信息
			var option = {
					'orderId' : window.procBizId,
					'regId':$('#regId').text(),
					'realName':$('#realName').val(),
					'idCard':$('#idCard').val()
			}
			IQB.post(urls['cfm'] + '/dandelion/saveOrderInfo', option, function(result){
				var result = result.iqbResult.result;
				if(result == 'success'){
					_this.auth();
				}
			})
		},
		auth:function(){
			//鉴权
			var option = {
					"bankCard": $('#bankCard').val(),
					"realName": $('#realName').val(),
					"idCard": $('#idCard').val()
			}
			IQB.post(urls['cfm'] + '/house/userInfoAuth/three', option, function(result){
				var result = result.iqbResult.result;
				if(result.retCode == '1'){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}else{
					IQB.alert(result.retMsg);
					return false;
				}
			})
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			//基本信息
			IQB.post(urls['cfm'] + '/dandelion/get_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchName').text(result.merchantNo);
				$('#realName').val(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				//订单状态
				$('#orderStatus').text(Formatter.orderStatus(result.riskStatus));
				
				$('#bizType').text(result.proType);
				$('#groupName').text(Formatter.groupName(result.bizType));
				$('#idCard').val(result.idCard);
				$('#bankCard').val(result.bankCard);
				if(result.orderAmt == 0){
					$('#orderAmt').val('');
				}else{
					$('#orderAmt').val(result.orderAmt);
				}
				if(result.orderItems == 0){
					$('#orderItems').val('');
				}else{
					$('#orderItems').val(result.orderItems);
				}
				$('#bankCardName').val(result.bankName);
				/*var proInfo = result.proInfo.split(';');
				_this.cache.orderFlag = proInfo[0];
				$('#orderFlag').text(_this.formatterOrderFlag(proInfo[0]));
				$('#startTime').text(proInfo[1]);
				$('#endTime').text(proInfo[2]);*/
			});
			//紧急联系人信息
			IQB.post(urls['cfm'] + '/dandelion/get_risk', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#address').val(Formatter.ifNull(result.address));
				$('#firmName').val(Formatter.ifNull(result.companyName));
				$('#firmAddress').val(Formatter.ifNull(result.companyAddress));
				$('#firmPhone').val(Formatter.ifNull(result.companyPhone));
				$('#workmateOne').val(Formatter.ifNull(result.colleagues1));
				$('#workmateOneTel').val(Formatter.ifNull(result.tel1));
				$('#workmateTwo').val(Formatter.ifNull(result.colleagues2));
				$('#workmateTwoTel').val(Formatter.ifNull(result.tel2));
				
				$('#relationOne').val(Formatter.ifNull(result.relation1));
				$('#reNameOne').val(Formatter.ifNull(result.rname1));
				$('#sexOne').val(Formatter.ifNull(result.sex1));
				$('#mobileOne').val(Formatter.ifNull(result.phone1));
				
				$('#relationTwo').val(Formatter.ifNull(result.relation2));
				$('#reNameTwo').val(Formatter.ifNull(result.rname2));
				$('#sexTwo').val(Formatter.ifNull(result.sex2));
				$('#mobileTwo').val(Formatter.ifNull(result.phone2));
				
				$('#relationThree').val(Formatter.ifNull(result.relation3));
				$('#reNameThree').val(Formatter.ifNull(result.rname3));
				$('#sexThree').val(Formatter.ifNull(result.sex3));
				$('#mobileThree').val(Formatter.ifNull(result.phone3));
				
				$('#creditNumber').val(Formatter.ifNull(result.creditNo));
				$('#creditPass').val(Formatter.ifNull(result.creditPasswd));
				$('#creditCode').val(Formatter.ifNull(result.creditCode));
			});
			//查询是否历史订单
			IQB.post(urls['cfm'] + '/dandelion/getInstOrderBackFlag', {orderId: window.procBizId}, function(result){
				_this.cache.backFlag = result.iqbResult.result;
			})
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1,2,3,4,101,102,11,12,13,14,15,16,17,18,19,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,
			                                                                                    21,22,23,24,25,26,27,28,29,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,251,252,253,230,231,232,233,234,235,236,237,238,239,240,
			                                                                                    31,32,33,34,35,36,37,38,39,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,351,352,353,330,331,332,333,334,335,336,337,338,339,340,
			                                                                                    41,42,43,44,45,46,47,48,49,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,451,452,453,430,431,432,433,434,435,436,437,438,439,440,
			                                                                                    51,52,53,54,55,56,57,58,59,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,551,552,553,530,531,532,533,534,535,536,537,538,539,540,
			                                                                                    61,62,63,64,65,66,67,68,69,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,651,652,653,630,631,632,633,634,635,636,637,638,639,640,
			                                                                                    71,72,73,74,75,76,77,78,79,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,751,752,753,730,731,732,733,734,735,736,737,738,739,740,
			                                                                                    ]}, function(result){
				var is = false;is2 = false;is3 = false;is4 = false;is5 = false;is6 = false;is7 = false;is8 = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5>'
						      			'</div>' + 
						      		'</div>';
						var html2 = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      			'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.outUpload.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
						      			'</div>' + 
						      		'</div>';
						if(String(n.imgType).length == 1){
							$('#td-' + n.imgType).append(html);
							is = true;
						}else if(String(n.imgType).charAt(0) == 1 && String(n.imgType).length > 1 && String(n.imgType).charAt(1) != 0){
							$('#td-' + n.imgType).append(html2);
							is2 = true;
						}else if(String(n.imgType).charAt(0) == 2 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is3 = true;
						}else if(String(n.imgType).charAt(0) == 3 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is4 = true;
						}else if(String(n.imgType).charAt(0) == 4 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is5 = true;
						}else if(String(n.imgType).charAt(0) == 5 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is6 = true;
						}else if(String(n.imgType).charAt(0) == 6 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is7 = true;
						}else if(String(n.imgType).charAt(0) == 7 && String(n.imgType).length > 1){
							$('#td-' + n.imgType).append(html2);
							is8 = true;
						}
					}
				});
				if(is){
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
				if(is2){
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
				}
				if(is3){
					_this.cache.viewer.viewerThree = new Viewer(document.getElementById('viewerThree'), {});
				}
				if(is4){
					_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
				}
				if(is5){
					_this.cache.viewer.viewerFive = new Viewer(document.getElementById('viewerFive'), {});
				}
				if(is6){
					_this.cache.viewer.viewerSix = new Viewer(document.getElementById('viewerSix'), {});
				}
				if(is7){
					_this.cache.viewer.viewerSeven = new Viewer(document.getElementById('viewerSeven'), {});
				}
				if(is8){
					_this.cache.viewer.viewerEight = new Viewer(document.getElementById('viewerEight'), {});
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
			
			$('.btn-group').find('button').prop('disabled', true);
			$('.btn-group').find('button').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.outUpload.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('.btn-group').find('button').prop('disabled', false);
					$('.btn-group').find('button').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(fileType.charAt(0) == 1){
						if(_this.cache.viewer.viewerTwo){
							_this.cache.viewer.viewerTwo.update();
						}else{
							_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
						}
					}else if(fileType.charAt(0) == 2){
						if(_this.cache.viewer.viewerThree){   
							_this.cache.viewer.viewerThree.update();
						}else{
							_this.cache.viewer.viewerThree = new Viewer(document.getElementById('viewerThree'), {});
						}
					}else if(fileType.charAt(0) == 3){
						if(_this.cache.viewer.viewerFour){
							_this.cache.viewer.viewerFour.update();
						}else{
							_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
						}
					}else if(fileType.charAt(0) == 4){
						if(_this.cache.viewer.viewerFive){
							_this.cache.viewer.viewerFive.update();
						}else{
							_this.cache.viewer.viewerFive = new Viewer(document.getElementById('viewerFive'), {});
						}
					}else if(fileType.charAt(0) == 5){
						if(_this.cache.viewer.viewerSix){
							_this.cache.viewer.viewerSix.update();
						}else{
							_this.cache.viewer.viewerSix = new Viewer(document.getElementById('viewerSix'), {});
						}
					}else if(fileType.charAt(0) == 6){
						if(_this.cache.viewer.viewerSeven){
							_this.cache.viewer.viewerSeven.update();
						}else{
							_this.cache.viewer.viewerSeven = new Viewer(document.getElementById('viewerSeven'), {});
						}
					}else if(fileType.charAt(0) == 7){
						if(_this.cache.viewer.viewerEight){
							_this.cache.viewer.viewerEight.update();
						}else{
							_this.cache.viewer.viewerEight = new Viewer(document.getElementById('viewerEight'), {});
						}
					}
					console.log(_this.cache.viewer);
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
			$('.infos').hide().css('display','none');
			$('.borrowerInfo').show();
			$('.forInfo').click(function(){
				var href = $(this).attr('href');
				$('.infos').hide();
				$('.'+href).show();
			});
		},
		addBondsman : function(){
			IQB.post(urls['cfm'] + '/dandelion/add_guarantee_no', {'orderId':window.procBizId}, function(result){
				if(result.iqbResult.result == 'success'){
					var addBondsman = _this.cache.d;
					IQB.confirm('确定要增加担保人信息吗？',function(){
						_this.cache.d = _this.cache.d+1;
						//$('.bondsmanInfo-' + _this.cache.d).show();
						$('#btn-bondsmanInfo' + _this.cache.d).show();
					},null);
				}
			})
		},
		addTr : function(){
			IQB.confirm('确定要增加一行吗？',function(){
				_this.cache.i = _this.cache.i + 1;
				var trrHtml =   "<tr>"+	
				                    "<td><input type='checkbox' id='checkbox"+_this.cache.i+"' name='checkbox'></td>"+
						    		"<td><select id='bondsmanType"+_this.cache.i+"' name='bondsmanType"+_this.cache.i+"' class='form-control input-sm'></select></td>"+				    		
							    	"<td><input type='text' id='bondsmaName"+_this.cache.i+"' name='bondsmaName"+_this.cache.i+"' class='form-control input-sm'></td>"+
							    	"<td><input type='text' id='bondsmanIdNo"+_this.cache.i+"' name='bondsmanIdNo"+_this.cache.i+"' class='form-control input-sm' maxlength='18'></td>"+				    		
							    	"<td><input type='text' id='bondsmanPhone"+_this.cache.i+"' name='bondsmanPhone"+_this.cache.i+"' class='form-control input-sm' maxlength='11'></td>"+
							    	"<td><input type='text' id='bondsmanBankNo"+_this.cache.i+"' name='bondsmanBankNo"+_this.cache.i+"' class='form-control input-sm' maxlength='20'></td>"+				    		
							    	"<td><input type='text' id='bondsmanCo"+_this.cache.i+"' name='bondsmanCo"+_this.cache.i+"' class='form-control input-sm'></td>"+
							    	"<td><input type='text' id='bondsmanCa"+_this.cache.i+"' name='bondsmanCa"+_this.cache.i+"' class='form-control input-sm'></td>"+				    		
							    	"<td><input type='text' id='bondsmanCp"+_this.cache.i+"' name='bondsmanCp"+_this.cache.i+"' class='form-control input-sm'></td>"+
					    		"</tr>"
				$('#bondsTable').append(trrHtml);
				_this.initSelect('bondsmanType'+_this.cache.i);
			},null);
		},
		deleteTr : function(){
			if($("input[name='checkbox']:checked").length > 0){
				IQB.confirm('确定要删除本行吗？',function(){
					$("input[name='checkbox']:checked").each(function() {
	                    n= $(this).parents("tr").index();
	                    m=n+1;
	                    $("#bondsTable").find("tr:eq("+n+")").remove();
	                });
				},null);
			}else{
				IQB.alert('未选中行');
			}
		},
		addTr2 : function(){
			IQB.confirm('确定要增加一行吗？',function(){
				_this.cache.i = _this.cache.i + 1;
				var trrHtml =   "<tr>"+	
				                    "<td><input type='checkbox' id='checkbox"+_this.cache.i+"' name='checkbox'></td>"+
						    		"<td><input type='text' id='creditType"+_this.cache.i+"' name='creditType"+_this.cache.i+"' class='form-control input-sm'></td>"+				    		
							    	"<td><input type='text' id='LoanAmt"+_this.cache.i+"' name='LoanAmt"+_this.cache.i+"' class='form-control input-sm'></td>"+						    	
					    		"</tr>"
				$('#loanTable').append(trrHtml);
			},null);
		},
		deleteTr2 : function(){
			if($("input[name='checkbox']:checked").length > 0){
				IQB.confirm('确定要删除本行吗？',function(){
					$("input[name='checkbox']:checked").each(function() {
	                    n= $(this).parents("tr").index();
	                    m=n+1;
	                    $("#loanTable").find("tr:eq("+n+")").remove();
	                });
				},null);
			}else{
				IQB.alert('未选中行');
			}
		},
        showInfo : function(){
			IQB.post(urls['cfm'] + '/dandelion/get_designated_person_info', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#projectName').val(result.icie.projectName);
				//信贷类型
				$('.creditType').text(Formatter.creditType(result.icie.creditType));
				_this.cache.creditType = result.icie.creditType;
				//额度建议及备注
				$('#limitAdvise').val(result.icie.amtAdvice);
				$('#remark').val(result.icie.remark);
				//项目名称及其他信息
				$('#projectName').text(result.icie.projectName);
				$('#borrow').val(result.icie.borrowTogether);
				$('#borrowName').val(Formatter.ifNull(result.icie.borrTogetherName));
				//担保人个数
				var num = result.icie.guarantorNum;
				_this.cache.d = num;
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
				if(creditInfo != '' && creditInfo != null){
					$('#creditType1').val(creditInfo[0].creditType);
					$('#loanAmt1').val(creditInfo[0].loanAmt);
					var creditHtml = '';
					for(var i=1;i<creditInfo.length;i++){
						creditHtml += "<tr><td><input type='checkbox' name='checkbox'></td><td><input type='text' class='form-control input-sm' value='"+Formatter.ifNull(creditInfo[i].creditType)+"'></td><td><input type='text' class='form-control input-sm' value='"+Formatter.ifNull(creditInfo[i].loanAmt)+"'></td></tr>"
					}
				}
				$('#loanTable').append(creditHtml);
				//担保人信息
				var signGuarant = result.icie.signGuarantCopy;
				var creditHtml = '';
				if(signGuarant != '' && signGuarant != null){
					$('#bondsmaName1').val(signGuarant[0].name);
					$('#bondsmanIdNo1').val(signGuarant[0].idCardNo);
					$('#bondsmanPhone1').val(signGuarant[0].mobile);
					$('#bondsmanBankNo1').val(signGuarant[0].bankCardNo);
					$('#bondsmanCo1').val(signGuarant[0].companyName);
					$('#bondsmanCa1').val(signGuarant[0].companyAddress);
					$('#bondsmanCp1').val(signGuarant[0].companyPhone);
					$('#bondsmanType1').val(signGuarant[0].userType).select2({theme: 'bootstrap'});
					var creditHtml = '';
					for(var i=1;i<signGuarant.length;i++){
						_this.cache.i = i + 1;
						creditHtml += "<tr>"+
						"<td><input type='checkbox' name='checkbox'></td>"+
						"<td><select class='form-control input-sm' id='bondsmanType"+_this.cache.i+"'><option  value='"+signGuarant[i].userType+"'>"+Formatter.userType(signGuarant[i].userType)+"</option></select></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].name+"'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].idCardNo+"' maxlength='18'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].mobile+"' maxlength='11'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].bankCardNo+"' maxlength='20'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].companyName+"'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].companyAddress+"'></td>"+
						"<td><input type='text' class='form-control input-sm' value='"+signGuarant[i].companyPhone+"'></td>"+
						"</tr>"
					}
					$('#bondsTable').append(creditHtml);
					for(var i=1;i<signGuarant.length;i++){
						_this.initSelect('bondsmanType'+(i + 1));
					}
				}
			})
		},
		getDictListByDictType: function(selectId, dictType){
			var req_data = {'dictTypeCode': dictType};
			IQB.post(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
				$('#' + selectId).prepend("<option value=''>请选择</option>");
				for(i=0;i<result.iqbResult.result.length;i++){
					$('#' + selectId).append("<option value='"+result.iqbResult.result[i].id+"'>"+result.iqbResult.result[i].text+"</option>");
				}
				_this.initApprovalTask();
				//showHtml
			})
		},
		initSelect: function(selectId){
			$('#' + selectId).select2({theme: 'bootstrap', data: JSON.parse('[{"id":"1","text":"共签人"},{"id":"2","text":"担保人"},{"id":"3","text":"互保人"}]')});
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			_this.getDictListByDictType('bankCardName', 'BANK_NAME'); 
			_this.initApprovalHistory();
			_this.showFile();
			_this.showInfo();
			/*$('#bondsmanType1').select2({theme: 'bootstrap'});*/
            //增加担保人信息
			$('#btn-addBondsman').click(function(){_this.addBondsman();});
			$('.bondsmanInfoBtn').hide();
			//tab页相关
			$('#tab li').click(function(){
				_this.forInfo();
			});
			//担保人信息增加一行
			$('#btn-addTr').click(function(){_this.addTr()});
			//担保人信息删除一行
			$('#btn-deleteTr').click(function(){_this.deleteTr()});
			//担保人信贷类型增加一行
			$('#btn-addTr2').click(function(){_this.addTr2()});
			//担保人信贷类型删除一行
			$('#btn-deleteTr2').click(function(){_this.deleteTr2()});
			
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
			$('#btn-uploadType1-32').on('click', function(){$('#file').attr('fileType', 132);$('#file').click();});
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
			$('#btn-uploadType2-51').on('click', function(){$('#file').attr('fileType', 251);$('#file').click();});
			$('#btn-uploadType2-52').on('click', function(){$('#file').attr('fileType', 252);$('#file').click();});
			$('#btn-uploadType2-53').on('click', function(){$('#file').attr('fileType', 253);$('#file').click();});
			$('#btn-uploadType2-30').on('click', function(){$('#file').attr('fileType', 230);$('#file').click();});
			$('#btn-uploadType2-31').on('click', function(){$('#file').attr('fileType', 231);$('#file').click();});
			$('#btn-uploadType2-32').on('click', function(){$('#file').attr('fileType', 232);$('#file').click();});
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
			$('#btn-uploadType3-51').on('click', function(){$('#file').attr('fileType', 351);$('#file').click();});
			$('#btn-uploadType3-52').on('click', function(){$('#file').attr('fileType', 352);$('#file').click();});
			$('#btn-uploadType3-53').on('click', function(){$('#file').attr('fileType', 353);$('#file').click();});
			$('#btn-uploadType3-30').on('click', function(){$('#file').attr('fileType', 330);$('#file').click();});
			$('#btn-uploadType3-31').on('click', function(){$('#file').attr('fileType', 331);$('#file').click();});
			$('#btn-uploadType3-32').on('click', function(){$('#file').attr('fileType', 332);$('#file').click();});
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
			$('#btn-uploadType4-51').on('click', function(){$('#file').attr('fileType', 451);$('#file').click();});
			$('#btn-uploadType4-52').on('click', function(){$('#file').attr('fileType', 452);$('#file').click();});
			$('#btn-uploadType4-53').on('click', function(){$('#file').attr('fileType', 453);$('#file').click();});
			$('#btn-uploadType4-30').on('click', function(){$('#file').attr('fileType', 430);$('#file').click();});
			$('#btn-uploadType4-31').on('click', function(){$('#file').attr('fileType', 431);$('#file').click();});
			$('#btn-uploadType4-32').on('click', function(){$('#file').attr('fileType', 432);$('#file').click();});
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
			$('#btn-uploadType5-51').on('click', function(){$('#file').attr('fileType', 551);$('#file').click();});
			$('#btn-uploadType5-52').on('click', function(){$('#file').attr('fileType', 552);$('#file').click();});
			$('#btn-uploadType5-53').on('click', function(){$('#file').attr('fileType', 553);$('#file').click();});
			$('#btn-uploadType5-30').on('click', function(){$('#file').attr('fileType', 530);$('#file').click();});
			$('#btn-uploadType5-31').on('click', function(){$('#file').attr('fileType', 531);$('#file').click();});
			$('#btn-uploadType5-32').on('click', function(){$('#file').attr('fileType', 532);$('#file').click();});
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
			$('#btn-uploadType6-51').on('click', function(){$('#file').attr('fileType', 651);$('#file').click();});
			$('#btn-uploadType6-52').on('click', function(){$('#file').attr('fileType', 652);$('#file').click();});
			$('#btn-uploadType6-53').on('click', function(){$('#file').attr('fileType', 653);$('#file').click();});
			$('#btn-uploadType6-30').on('click', function(){$('#file').attr('fileType', 630);$('#file').click();});
			$('#btn-uploadType6-31').on('click', function(){$('#file').attr('fileType', 631);$('#file').click();});
			$('#btn-uploadType6-32').on('click', function(){$('#file').attr('fileType', 632);$('#file').click();});
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
			$('#btn-uploadType7-51').on('click', function(){$('#file').attr('fileType', 751);$('#file').click();});
			$('#btn-uploadType7-52').on('click', function(){$('#file').attr('fileType', 752);$('#file').click();});
			$('#btn-uploadType7-53').on('click', function(){$('#file').attr('fileType', 753);$('#file').click();});
			$('#btn-uploadType7-30').on('click', function(){$('#file').attr('fileType', 730);$('#file').click();});
			$('#btn-uploadType7-31').on('click', function(){$('#file').attr('fileType', 731);$('#file').click();});
			$('#btn-uploadType7-32').on('click', function(){$('#file').attr('fileType', 732);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.outUpload.init();
});


