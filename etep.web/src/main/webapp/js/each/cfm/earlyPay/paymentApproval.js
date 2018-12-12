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
function Subtr(arg1,arg2){ 
	  var r1,r2,m,n; 
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	  m=Math.pow(10,Math.max(r1,r2)); 
	  n=(r1>=r2)?r1:r2; 
	  return ((arg1*m-arg2*m)/m).toFixed(n); 
	}
/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.paymentApproval');
IQB.paymentApproval = function() {
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
				variableData.payMethod = $('#payway').val();
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.payMethod = $('#payway').val();
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
		deleteProcess: function() {
			var authData= {}
			authData.procAuthType = "2";
			var variableData={}
			var bizData={}
			bizData.procBizId=procBizId;
			var procData={}
			procData.procTaskId = procTaskId;
			var option = {};
			option.authData=authData;
			option.variableData=variableData;
			option.bizData=bizData;
			option.procData=procData;
			IQB.getById(urls['rootUrl'] + '/WfTask/deleteProcess', option, function(result) {
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
			if($('#updateForm').form('validate')){
				if($('#payway').val() == 2){
					//线下支付截图必传
					if($('#td-2').find('div').length <= 0){
						IQB.alert('请上传支付截图！');
						return false;
					}
				}
				var data = {
						'orderId':window.procBizId,
						'payMethod':$('#payway').val()
				};
				IQB.post(urls['cfm'] + '/settle/saveOtherInfo', data, function(result){
					if(result.success == 1){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
			}
		},
		closeApproveWin: function() {
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化详情
			IQB.post(urls['cfm'] + '/settle/getSettleBean', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				//基本信息
				$('#realName').text(Formatter.ifNull(result.realName));
				$('#orderItems').text(result.orderItems);
				$('#haspayItem').text(result.curItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#monthCapital').text(Formatter.money(result.monthPrincipal));
				
			    //提前还款申请
				$('#hasMarginAmt').text(Formatter.money(result.margin));
				$('#hasTakePaymentAmt').text(Formatter.money(result.feeAmount));
				$('#hasRepayAmt').text(Formatter.money(result.payPrincipal));
				$('#hasnotRepayAmt').text(Formatter.money(result.surplusPrincipal));
				$('#dedit').text(Formatter.money(result.overdueAmt));
				$('#totalDefaultInterest').text(Formatter.money(result.totalOverdueInterest));
				$('#hasnotInterest').text(Formatter.money(result.remainInterest));
				$('#returnTakePaymentAmt').text(Formatter.money(result.refundAmt));
				
				$('#repayAmtAll').text(Formatter.money(result.totalRepayAmt));
				$('#hideFlag').text(Formatter.yOrn(result.hiddenFee));
				$('#payReason').text(Formatter.ifNull(result.reason));
				$('#remark').text(Formatter.ifNull(result.remark));
				//是否减免违约金
				$('#derateFlag').text(Formatter.yOrn(result.cutOverdueFlag));
				if(result.cutOverdueFlag == 1){
					$('.derateAbout').show();
					//回显
					$('#derateAmt').text(Formatter.money(result.cutOverdueAmt));
					$('#amtFinal').text(Formatter.money(result.finalOverdueAmt));
					$('#derateReason').text(Formatter.ifNull(result.cutOverdueRemark));
				}else{
					$('.derateAbout').hide();
				}
				$('#payway').val(result.payMethod);
				if(result.payMethod == 2){
					$('.btn-group').show();
					$('#viewerOne').show();
					_this.showFile();
				}
				_this.cache.payMethod = result.payMethod;
				$('#paymentAmt').text(Formatter.money(result.receiveAmt));
				$('#paymentTime').text(result.recieveDate);
				//支付状态
				$('#preAmountStatus').text(Formatter.preAmountStatus(result.amtStatus));
				//处理流水号
				if(result.amtStatus == 1){
					$('.outOrderId-div').show();	
				}else{
					$('.outOrderId-div').hide();	
				}
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [2]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      			'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.paymentApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.paymentApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		payway : function(){
			var payway = $('#payway').val();
			if(payway == 2){
				//线下支付需要上传支付截图
				$('.btn-group').show();
				$('#viewerOne').show();
			}else{
				$('.btn-group').hide();
				$('#viewerOne').hide();
			}
		},
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			$('#payway').on('change',function(){_this.payway();});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-delete').on('click', function(){_this.deleteProcess()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
            $('#btn-uploadTypeTwo').on('click', function(){$('#file').attr('fileType', 2);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});
		}
	}
	return _this;
}();

$(function() {
	IQB.paymentApproval.init();
});


