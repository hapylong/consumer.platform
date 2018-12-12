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
			if($('#creditType').val() == ''){
				IQB.alert('请选择信贷类型');
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
			if($('#td-103').find('div').length == 0){
				IQB.alert('请上传押品图片');
				return false;
			}
			if($('#updateForm').form('validate')){
				_this.saveCredit();
			}else{
				IQB.alert('押品信息不完善，无法审核');
				return false;
			}
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
			list[i-1].kind = loanTable.rows[i].cells[1].getElementsByTagName("SELECT")[0].value;
			list[i-1].num = loanTable.rows[i].cells[2].getElementsByTagName("INPUT")[0].value;
			list[i-1].name = loanTable.rows[i].cells[3].getElementsByTagName("INPUT")[0].value;
			list[i-1].remark = loanTable.rows[i].cells[4].getElementsByTagName("INPUT")[0].value;
			list[i-1].unitPrice = loanTable.rows[i].cells[5].getElementsByTagName("INPUT")[0].value;
			list[i-1].allPrice = loanTable.rows[i].cells[6].getElementsByTagName("INPUT")[0].value;
			}
        	var data = {
        		'orderId':window.procBizId,
        		'creditType' : $('#creditType').val(),
        		'borrowTogether':$('#borrow').val(),
        		'borrTogetherName':$('#borrowName').val(),
        		'mortgageTotalAmt':$('#totalValue').text(),
        		'adviceAmt':$('#financingLimit').text(),
        		'mortgageInfo' : list,
        		'type':3
        	};
			IQB.post(urls['rootUrl'] + '/dandelion/update_credit_type', data, function(result){
				if(result.success == 1){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
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
			                                                                                    21,22,23,24,25,26,27,28,29,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,251,252,253,230,231,232,233,234,235,236,237,238,239,240,
			                                                                                    31,32,33,34,35,36,37,38,39,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,351,352,353,330,331,332,333,334,335,336,337,338,339,340,
			                                                                                    41,42,43,44,45,46,47,48,49,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,451,452,453,430,431,432,433,434,435,436,437,438,439,440,
			                                                                                    51,52,53,54,55,56,57,58,59,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,551,552,553,530,531,532,533,534,535,536,537,538,539,540,
			                                                                                    61,62,63,64,65,66,67,68,69,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,651,652,653,630,631,632,633,634,635,636,637,638,639,640,
			                                                                                    71,72,73,74,75,76,77,78,79,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,751,752,753,730,731,732,733,734,735,736,737,738,739,740,
			                                                                                    ]}, function(result){
				var is = false;is2 = false;is3 = false;is4 = false;is5 = false;is6 = false;is7 = false;is8 = false,is9 = false;
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
						if(String(n.imgType).length == 1 || n.imgType == 101 || n.imgType == 102){
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
						}else if(String(n.imgType).length == 3 || n.imgType == 103 || n.imgType == 104 || n.imgType == 105 || n.imgType == 106){
							$('#td-' + n.imgType).append(html2);
							is9 = true;
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
				if(is9){
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
						if(fileType.length == 3){
							if(_this.cache.viewer.viewerNine){
								_this.cache.viewer.viewerNine.update();
							}else{
								_this.cache.viewer.viewerNine = new Viewer(document.getElementById('viewerNine'), {});
							}
						}else{
							if(_this.cache.viewer.viewerTwo){
								_this.cache.viewer.viewerTwo.update();
							}else{
								_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
							}
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
						    		"<td><select class='form-control input-sm easyui-validatebox' id='kind"+_this.cache.i+"' required='required'></select></td>"+				    		
							    	"<td><input type='text' class='form-control input-sm easyui-validatebox calculateN' id='num"+_this.cache.i+"' data-options='required:true, validType:['int']'></td>"+
							    	"<td><input type='text' class='form-control input-sm easyui-validatebox' id='name"+_this.cache.i+"' required='required'></td>"+
							    	"<td><input type='text' class='form-control input-sm easyui-validatebox' id='remark"+_this.cache.i+"' required='required'></td>"+
							    	"<td><input type='text' class='form-control input-sm easyui-validatebox calculateP' id='unitPrice"+_this.cache.i+"' data-options='required:true, validType:['money']'></td>"+
							    	"<td><input type='text' class='form-control input-sm calculateA' id='allPrice"+_this.cache.i+"' readonly></td>"+
					    		"</tr>"
				$('#loanTable').append(trrHtml);
				_this.getDictListByDictType('kind'+_this.cache.i,'KIND');
			},null);
		},
		deleteTr2 : function(){
			if($("input[name='checkbox']:checked").length > 0){
				IQB.confirm('确定要删除本行吗？',function(){
					var totalPrice = 0;
					$("input[name='checkbox']:checked").each(function() {
	                    n= $(this).parents("tr").index();
	                    m=n+1;
	                    $("#loanTable").find("tr:eq("+n+")").remove();
	                    $.each($('.calculateA'),function(i,n){
	                    	totalPrice += Number($(n).val());
					    }); 
					    $('#totalValue').text(totalPrice);
					    var limitVal = parseFloat(totalPrice*0.2);
					    $('#financingLimit').text(limitVal.toFixed(2));
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
				$('#creditType').val(result.icie.creditType);
				_this.cache.creditType = result.icie.creditType;
				//额度建议及备注
				$('#limitAdvise').val(result.icie.amtAdvice);
				$('#remark').val(result.icie.remark);
				//项目名称及其他信息
				$('#projectName').text(result.icie.projectName);
				$('#borrow').val(result.icie.borrowTogether);
				//押品信息
				$('#totalValue').text(Formatter.ifNull(result.icie.mortgageTotalAmt));
        		$('#financingLimit').text(Formatter.ifNull(result.icie.adviceAmt));
				if(result.icie.borrowTogether == 1){
					$('.borrowNameAbout').show();
					$('#borrowName').val(Formatter.ifNull(result.icie.borrTogetherName));
				}
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
				var creditInfo = eval(result.icie.mortgageInfo);
				var creditHtml = '';
				if(creditInfo != '' && creditInfo != null){
					$('#kind1').val(creditInfo[0].kind);
					$('#num1').val(creditInfo[0].num);
					$('#name1').val(creditInfo[0].name);
					$('#remark1').val(creditInfo[0].remark);
					$('#unitPrice1').val(creditInfo[0].unitPrice);
					$('#allPrice1').val(creditInfo[0].allPrice);
					var creditHtml = '';
					for(var i=1;i<creditInfo.length;i++){
						_this.cache.i = i + 1;
						creditHtml += "<tr>"+	
					                    "<td><input type='checkbox' id='checkbox"+_this.cache.i+"' name='checkbox'></td>"+
							    		"<td><select class='form-control input-sm easyui-validatebox' id='kind"+_this.cache.i+"' required='required'><option  value='"+creditInfo[i].kind+"'>"+creditInfo[i].kind+"</option></select></td>"+				    		
								    	"<td><input type='text' class='form-control input-sm easyui-validatebox calculateN' id='num"+_this.cache.i+"' data-options='required:true, validType:['int']' value='"+creditInfo[i].num+"'></td>"+
								    	"<td><input type='text' class='form-control input-sm easyui-validatebox' id='name"+_this.cache.i+"' required='required' value='"+creditInfo[i].name+"'></td>"+
								    	"<td><input type='text' class='form-control input-sm easyui-validatebox' id='remark"+_this.cache.i+"' required='required' value='"+creditInfo[i].remark+"'></td>"+
								    	"<td><input type='text' class='form-control input-sm easyui-validatebox calculateP' id='unitPrice"+_this.cache.i+"' data-options='required:true, validType:['money']' value='"+creditInfo[i].unitPrice+"'></td>"+
								    	"<td><input type='text' class='form-control input-sm calculateA' id='allPrice"+_this.cache.i+"' readonly value='"+creditInfo[i].allPrice+"'></td>"+
						    		"</tr>"
					}
					$('#loanTable').append(creditHtml);
					for(var i=1;i<creditInfo.length;i++){
						_this.getDictListByDictType('kind'+(i + 1),'KIND');
					}
				}
				
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
				for(var k=0;k<result.iqbResult.result.length;k++){
					$('#' + selectId).append("<option value='"+result.iqbResult.result[k].id+"'>"+result.iqbResult.result[k].text+"</option>");
				}
			})
		},
		initSelect: function(selectId){
			$('#' + selectId).select2({theme: 'bootstrap', data: JSON.parse('[{"id":"1","text":"共签人"},{"id":"2","text":"担保人"},{"id":"3","text":"互保人"}]')});
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}}); 
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			_this.showInfo();
			$('#bondsmanType1').select2({theme: 'bootstrap'});
			_this.getDictListByDictType('kind1','KIND');
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
			$('#borrow').on('change',function(){
				if($(this).val() == 1){
					$('.borrowNameAbout').show();
				}else{
					$('.borrowNameAbout').hide();
				}
			});
			//担保人信贷类型增加一行
			$('#btn-addTr2').click(function(){_this.addTr2()});
			//担保人信贷类型删除一行
			$('#btn-deleteTr2').click(function(){_this.deleteTr2()});
			
			//计算押品总额
			$("#loanTable").delegate(".calculateN","blur",function(){
				var totalPrice = 0;
				var num = $(this).val();
				var price = $(this).parents("tr").find('.calculateP').val();
				var priceAll = $(this).parents("tr").find('.calculateA');
				if($('#loanTable').form('validate')){
					$(priceAll).val(parseFloat(num*price).toFixed(2));
				    $.each($('.calculateA'),function(i,n){
				    	totalPrice += Number($(n).val());
				    }); 
				    $('#totalValue').text(totalPrice);
				    var limitVal = parseFloat(totalPrice*0.2);
				    $('#financingLimit').text(limitVal.toFixed(2));
				}
			});
			$("#loanTable").delegate(".calculateP","blur",function(){
            	var totalPrice = 0;
            	var num = $(this).val();
				var price = $(this).parents("tr").find('.calculateN').val();
				var priceAll = $(this).parents("tr").find('.calculateA');
				if($('#loanTable').form('validate')){
					$(priceAll).val(num*price);
					$.each($('.calculateA'),function(i,n){
				    	totalPrice += Number($(n).val());
				    }); 
				    $('#totalValue').text(totalPrice);
				    var limitVal = parseFloat(totalPrice*0.2);
				    $('#financingLimit').text(limitVal.toFixed(2));
				}
			});
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			//押品信息
			$('#btn-uploadType103').on('click', function(){$('#file').attr('fileType', 103);$('#file').click();});
			$('#btn-uploadType104').on('click', function(){$('#file').attr('fileType', 104);$('#file').click();});
			$('#btn-uploadType105').on('click', function(){$('#file').attr('fileType', 105);$('#file').click();});
			$('#btn-uploadType106').on('click', function(){$('#file').attr('fileType', 106);$('#file').click();});
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
			$('#btn-uploadType1-33').on('click', function(){$('#file').attr('fileType', 133);$('#file').click();});
			$('#btn-uploadType1-34').on('click', function(){$('#file').attr('fileType', 134);$('#file').click();});
			$('#btn-uploadType1-35').on('click', function(){$('#file').attr('fileType', 135);$('#file').click();});
			$('#btn-uploadType1-36').on('click', function(){$('#file').attr('fileType', 136);$('#file').click();});
			$('#btn-uploadType1-37').on('click', function(){$('#file').attr('fileType', 137);$('#file').click();});
			$('#btn-uploadType1-38').on('click', function(){$('#file').attr('fileType', 138);$('#file').click();});
			$('#btn-uploadType1-39').on('click', function(){$('#file').attr('fileType', 139);$('#file').click();});
			$('#btn-uploadType1-40').on('click', function(){$('#file').attr('fileType', 140);$('#file').click();});
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
			$('#btn-uploadType2-33').on('click', function(){$('#file').attr('fileType', 233);$('#file').click();});
			$('#btn-uploadType2-34').on('click', function(){$('#file').attr('fileType', 234);$('#file').click();});
			$('#btn-uploadType2-35').on('click', function(){$('#file').attr('fileType', 235);$('#file').click();});
			$('#btn-uploadType2-36').on('click', function(){$('#file').attr('fileType', 236);$('#file').click();});
			$('#btn-uploadType2-37').on('click', function(){$('#file').attr('fileType', 237);$('#file').click();});
			$('#btn-uploadType2-38').on('click', function(){$('#file').attr('fileType', 238);$('#file').click();});
			$('#btn-uploadType2-39').on('click', function(){$('#file').attr('fileType', 239);$('#file').click();});
			$('#btn-uploadType2-40').on('click', function(){$('#file').attr('fileType', 240);$('#file').click();});
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
			$('#btn-uploadType3-33').on('click', function(){$('#file').attr('fileType', 333);$('#file').click();});
			$('#btn-uploadType3-34').on('click', function(){$('#file').attr('fileType', 334);$('#file').click();});
			$('#btn-uploadType3-35').on('click', function(){$('#file').attr('fileType', 335);$('#file').click();});
			$('#btn-uploadType3-36').on('click', function(){$('#file').attr('fileType', 336);$('#file').click();});
			$('#btn-uploadType3-37').on('click', function(){$('#file').attr('fileType', 337);$('#file').click();});
			$('#btn-uploadType3-38').on('click', function(){$('#file').attr('fileType', 338);$('#file').click();});
			$('#btn-uploadType3-39').on('click', function(){$('#file').attr('fileType', 339);$('#file').click();});
			$('#btn-uploadType3-40').on('click', function(){$('#file').attr('fileType', 340);$('#file').click();});
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
			$('#btn-uploadType4-33').on('click', function(){$('#file').attr('fileType', 433);$('#file').click();});
			$('#btn-uploadType4-34').on('click', function(){$('#file').attr('fileType', 434);$('#file').click();});
			$('#btn-uploadType4-35').on('click', function(){$('#file').attr('fileType', 435);$('#file').click();});
			$('#btn-uploadType4-36').on('click', function(){$('#file').attr('fileType', 436);$('#file').click();});
			$('#btn-uploadType4-37').on('click', function(){$('#file').attr('fileType', 437);$('#file').click();});
			$('#btn-uploadType4-38').on('click', function(){$('#file').attr('fileType', 438);$('#file').click();});
			$('#btn-uploadType4-39').on('click', function(){$('#file').attr('fileType', 439);$('#file').click();});
			$('#btn-uploadType4-40').on('click', function(){$('#file').attr('fileType', 440);$('#file').click();});
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
			$('#btn-uploadType5-33').on('click', function(){$('#file').attr('fileType', 533);$('#file').click();});
			$('#btn-uploadType5-34').on('click', function(){$('#file').attr('fileType', 534);$('#file').click();});
			$('#btn-uploadType5-35').on('click', function(){$('#file').attr('fileType', 535);$('#file').click();});
			$('#btn-uploadType5-36').on('click', function(){$('#file').attr('fileType', 536);$('#file').click();});
			$('#btn-uploadType5-37').on('click', function(){$('#file').attr('fileType', 537);$('#file').click();});
			$('#btn-uploadType5-38').on('click', function(){$('#file').attr('fileType', 538);$('#file').click();});
			$('#btn-uploadType5-39').on('click', function(){$('#file').attr('fileType', 539);$('#file').click();});
			$('#btn-uploadType5-40').on('click', function(){$('#file').attr('fileType', 540);$('#file').click();});
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
			$('#btn-uploadType6-33').on('click', function(){$('#file').attr('fileType', 633);$('#file').click();});
			$('#btn-uploadType6-34').on('click', function(){$('#file').attr('fileType', 634);$('#file').click();});
			$('#btn-uploadType6-35').on('click', function(){$('#file').attr('fileType', 635);$('#file').click();});
			$('#btn-uploadType6-36').on('click', function(){$('#file').attr('fileType', 636);$('#file').click();});
			$('#btn-uploadType6-37').on('click', function(){$('#file').attr('fileType', 637);$('#file').click();});
			$('#btn-uploadType6-38').on('click', function(){$('#file').attr('fileType', 638);$('#file').click();});
			$('#btn-uploadType6-39').on('click', function(){$('#file').attr('fileType', 639);$('#file').click();});
			$('#btn-uploadType6-40').on('click', function(){$('#file').attr('fileType', 640);$('#file').click();});
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
			$('#btn-uploadType7-33').on('click', function(){$('#file').attr('fileType', 733);$('#file').click();});
			$('#btn-uploadType7-34').on('click', function(){$('#file').attr('fileType', 734);$('#file').click();});
			$('#btn-uploadType7-35').on('click', function(){$('#file').attr('fileType', 735);$('#file').click();});
			$('#btn-uploadType7-36').on('click', function(){$('#file').attr('fileType', 736);$('#file').click();});
			$('#btn-uploadType7-37').on('click', function(){$('#file').attr('fileType', 737);$('#file').click();});
			$('#btn-uploadType7-38').on('click', function(){$('#file').attr('fileType', 738);$('#file').click();});
			$('#btn-uploadType7-39').on('click', function(){$('#file').attr('fileType', 739);$('#file').click();});
			$('#btn-uploadType7-40').on('click', function(){$('#file').attr('fileType', 740);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.outUpload.init();
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

