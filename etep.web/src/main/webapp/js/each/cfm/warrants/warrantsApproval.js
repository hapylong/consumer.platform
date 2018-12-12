function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procTaskId = getQueryString('procTaskId');
var procBizId = getQueryString('procBizId');
var procInstId = getQueryString('procInstId');

/*procBizId = '20170103-267322';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.warrantsApproval');
IQB.warrantsApproval = function(){
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
				bizData.procBizId=window.procBizId;
				var procData = {}
				procData.procTaskId = window.procTaskId;
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
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {'orderId': window.procBizId}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text(Formatter.groupName(result.bizType));
				//处理业务类型
				if(result.bizType == '2001'){
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [28,29,30,31,32,35,36,37]}, function(result){
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.warrantsInput.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		inCarChange : function(){
			if($("#inCar").val() == 0){
				$("#inCarTime").attr('disabled',true);
			}
			$("#inCar").change(function(){
				if($("#inCar").val() == 0){
					$("#inCarTime").attr('disabled',true);
					$("#inCarTime").val('');
				}else if($("#inCar").val() == 1){
					$("#inCarTime").attr('disabled',false);
				}
			});
			
		},
		showText : function(){
			var groupName = $('#groupName').text();
			IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
				if(result.iqbResult.AuthorityCardInfo != null){
					$("#VIN").html(result.iqbResult.AuthorityCardInfo.carNo);
					$("#engineNo").html(result.iqbResult.AuthorityCardInfo.engine);
					$("#plate").html(result.iqbResult.AuthorityCardInfo.plate);
					$("#inCarTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.getCarDate));
					$("#annualInspectionTtime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.checkDate));
					$("#GPSTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.gpsInstDate));
					$("#insuranceStartTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceStart));
					$("#insuranceEndTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceEnd));
					$("#identifyNumber").html(result.iqbResult.AuthorityCardInfo.lineGpsNo);	
					$("#noIdentifyNumber").html(result.iqbResult.AuthorityCardInfo.nolineGpsNo);
					$("#commercialInsuranceStartTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksStart));
					$("#commercialInsuranceEndTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksEnd));
					$("#identifyNumberLocation").html(result.iqbResult.AuthorityCardInfo.lineGpsInstAdd);
					$("#noIdentifyNumberLocation").html(result.iqbResult.AuthorityCardInfo.noLineGpsInstAdd);
					$("#mortgageFlag").html(Formatter.isSuperadmin(result.iqbResult.AuthorityCardInfo.mortgageFlag));
					$("#mortgageDate").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.mortgageDate));
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
			_this.showText();
			_this.inCarChange();
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			$('#btn-uploadTypeTwentyeight').on('click', function(){$('#file').attr('fileType', 28);$('#file').click();});
			$('#btn-uploadTypeTwentynine').on('click', function(){$('#file').attr('fileType', 29);$('#file').click();});
			$('#btn-uploadTypeThirty').on('click', function(){$('#file').attr('fileType', 30);$('#file').click();});
			$('#btn-uploadTypeThirtyone').on('click', function(){$('#file').attr('fileType', 31);$('#file').click();});
			$('#btn-uploadTypeThirtytwo').on('click', function(){$('#file').attr('fileType', 32);$('#file').click();});
			$('#btn-uploadTypeThirtythree').on('click', function(){$('#file').attr('fileType', 33);$('#file').click();});
			$('#btn-uploadTypeThirtyfour').on('click', function(){$('#file').attr('fileType', 34);$('#file').click();});
			$('#btn-uploadTypeThirtyfive').on('click', function(){$('#file').attr('fileType', 35);$('#file').click();});
			$('#btn-uploadTypeThirtysix').on('click', function(){$('#file').attr('fileType', 36);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.warrantsApproval.init();
	/*datepicker(inCarTime);//交车时间
	datepicker(annualInspectionTtime);//年检时间
	datepicker(GPSTime);//GPS安装时间
	datepicker(insuranceStartTime);//交强险
	datepicker(insuranceEndTime);
	datepicker(commercialInsuranceStartTime);//商业险
	datepicker(commercialInsuranceEndTime);*/
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


