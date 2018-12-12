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
var procOrgCode = getQueryString('procOrgCode');
var orderAmt = getQueryString('orderAmt');
var orderName = decodeURI(getQueryString('orderName'));
var procBizMemo = procBizId +';'+ orderAmt;
var queryCondition = queryCondition;


/*procBizId = '20170103-267322';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';
procOrgCode = '10001';
orderAmt = '2000';
orderName = '以租代售';
var procBizMemo = procBizId + orderName;*/
	
	
$package('IQB.warrantsUpdate');
IQB.warrantsUpdate = function(){
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
				
				var bizData = {}
				bizData.amount=window.amount;
				bizData.procBizId=window.procBizId;
				bizData.procBizMemo=window.procBizMemo;
				bizData.procBizType='';
				bizData.procOrgCode=window.procOrgCode;
				var authData= {}
				authData.procAuthType = "2";
				var procData = {}
				procData.procDefKey = 'warrants_document';
				var variableData = {}
				variableData.procApprStatus = '1';
				variableData.procApprOpinion = '同意';
				variableData.procAssignee = '';
				variableData.procAppointTask = '';
				var option = {};
				option.bizData = bizData;
				option.authData=authData;
				option.procData = procData;
				option.variableData = variableData;
				
				IQB.post(urls['rootUrl'] + '/WfTask/startAndCommitProcess', option, function(result){
					if(result.success=="1") {
						$('#approve-win').modal('hide');
						var url = window.location.pathname;
						var param = window.parent.IQB.main2.fetchCache(url);
						var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
						var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + true + ',' + null + ')';
						window.parent.IQB.main2.call(callback, callback2);
					} else {
						IQB.alert(result.retUserInfo);
					}
				});
				
			} else {
				IQB.alert("请选择审批结果.");
			}
		},
		openApproveWin: function(){
			if($('#td-28').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			if($('#td-29').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			if($('#td-30').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			if($('#td-31').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			if($('#td-32').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			/*if($('#td-36').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};*/
			if($('#td-37').find('div').length == 0){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			};
			var groupName = $('#groupName').text();
			if(groupName == '以租代售新车'){
				//车架号、发动机号
				if($("#VIN").val() == ''){
					IQB.alert('权证信息不完善，无法审核');
					return false;
				}
				if($("#engineNo").val() == ''){
					IQB.alert('权证信息不完善，无法审核');
					return false;
				}
			}
			if($("#plate").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}	
			if($("#inCarTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#annualInspectionTtime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#GPSTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#insuranceStartTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#insuranceEndTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#commercialInsuranceStartTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#commercialInsuranceEndTime").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#identifyNumber").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#noIdentifyNumber").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#mortgageFlag").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			//gps安装位置去掉必填校验
			/*if($("#identifyNumberLocation").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}
			if($("#noIdentifyNumberLocation").val() == ''){
				IQB.alert('权证信息不完善，无法审核');
				return false;
			}*/
			var carNo = $("#VIN").val();
			var engine = $("#engineNo").val();
			var plate = $("#plate").val();
			var getCarDate = $("#inCarTime").val();
			var checkDate = $("#annualInspectionTtime").val();
			var gpsInstDate = $("#GPSTime").val();
			var insuranceStart = $("#insuranceStartTime").val();
			var insuranceEnd = $("#insuranceEndTime").val();
			var lineGpsNo = $("#identifyNumber").val();	
			var nolineGpsNo = $("#noIdentifyNumber").val();
			var bizRisksStart = $("#commercialInsuranceStartTime").val();
			var bizRisksEnd = $("#commercialInsuranceEndTime").val();
			var lineGpsInstAdd = $("#identifyNumberLocation").val();
			var noLineGpsInstAdd = $("#noIdentifyNumberLocation").val();
			var mortgageFlag = $('#mortgageFlag').val();
			var mortgageDate = $('#mortgageDate').val();
			var data = {
					'orderId': window.procBizId,
					'carNo' : carNo,
					'engine': engine,
					'plate' : plate,
					'getCarDate' : getCarDate,
					'checkDate' : checkDate,
					'gpsInstDate' : gpsInstDate,
					'insuranceStart' : insuranceStart,
					'insuranceEnd' : insuranceEnd,
					'lineGpsNo' : lineGpsNo,
					'nolineGpsNo' : nolineGpsNo,
					'bizRisksStart' : bizRisksStart,
					'bizRisksEnd' : bizRisksEnd,
					'lineGpsInstAdd' : lineGpsInstAdd,
					'noLineGpsInstAdd' : noLineGpsInstAdd,
					'mortgageFlag' : mortgageFlag,
					'mortgageDate' : mortgageDate
			}; 
			//保存权证信息
			IQB.post(urls['cfm'] + '/authoritycard/save', data, function(result){
				var result = result.iqbResult.result;
				if(result == 000000){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}
			})
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
				var groupName = $('#groupName').text();
				if(groupName == '以租代售二手车'){
					$(".VINs").hide();
					$(".engineNos").hide();
				}
				IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': window.procBizId}, function(result){
					if(result.iqbResult.AuthorityCardInfo != null){
						$("#VIN").val(result.iqbResult.AuthorityCardInfo.carNo);
						$("#engineNo").val(result.iqbResult.AuthorityCardInfo.engine);
						if(result.iqbResult.AuthorityCardInfo.carNo != '' && result.iqbResult.AuthorityCardInfo.carNo != null){
							$('#VIN').attr('disabled',true);
						}else{
							$('#VIN').removeAttr('disabled');
						}
						if(result.iqbResult.AuthorityCardInfo.engine != '' && result.iqbResult.AuthorityCardInfo.engine != null){
							$('#engineNo').attr('disabled',true);
						}else{
							$('#engineNo').removeAttr('disabled');
						}
						$("#plate").val(result.iqbResult.AuthorityCardInfo.plate);
						$("#inCarTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.getCarDate));
						$("#annualInspectionTtime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.checkDate));
						$("#GPSTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.gpsInstDate));
						$("#insuranceStartTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceStart));
						$("#insuranceEndTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceEnd));
						$("#identifyNumber").val(result.iqbResult.AuthorityCardInfo.lineGpsNo);	
						$("#noIdentifyNumber").val(result.iqbResult.AuthorityCardInfo.nolineGpsNo);
						$("#commercialInsuranceStartTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksStart));
						$("#commercialInsuranceEndTime").val(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksEnd));
						$("#identifyNumberLocation").val(result.iqbResult.AuthorityCardInfo.lineGpsInstAdd);	
						$("#noIdentifyNumberLocation").val(result.iqbResult.AuthorityCardInfo.noLineGpsInstAdd);
						$("#mortgageFlag").val(result.iqbResult.AuthorityCardInfo.mortgageFlag);
						$("#mortgageDate").val(Formatter.timeCfm5(result.iqbResult.AuthorityCardInfo.mortgageDate));
					}
				})
			});
		},
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid3({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId
				}
			});
		},
		showFile: function(){
			//图片
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [28,29,30,31,32,35,36,37]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.warrantsUpdate.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
			//文字信息
			
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.warrantsUpdate.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
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
			$('#btn-uploadTypeThirtyfive').on('click', function(){$('#file').attr('fileType', 35);$('#file').click();});
			$('#btn-uploadTypeThirtysix').on('click', function(){$('#file').attr('fileType', 36);$('#file').click();});
			$('#btn-uploadTypeThirtyseven').on('click', function(){$('#file').attr('fileType', 37);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.warrantsUpdate.init();
	datepicker(inCarTime);//年检时间
	datepicker(annualInspectionTtime);//年检时间
	datepicker(GPSTime);//GPS安装时间
	datepicker(insuranceStartTime);//交强险
	datepicker(insuranceEndTime);
	datepicker(commercialInsuranceStartTime);//商业险
	datepicker(commercialInsuranceEndTime);
	datepicker(mortgageDate);//抵押登记日期
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


