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

$package('IQB.carLoanReceipt');
IQB.carLoanReceipt = function() {
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
					      		 	'<h5>' + option.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + option.imgPath + '" onclick="IQB.carLoanReceipt.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(_this.cache.viewer.viewerFour){
						_this.cache.viewer.viewerFour.update();
					}else{
						_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
					}
				});
			});	
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [8,9,10,11,12,13,14,15]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6 style="float: right;margin-top:-21px"></h6>' +
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
        // 回显赋值
        showHtml: function() {
        	var data = {
					"orderId": procBizId,
			}
        	IQB.post(urls['cfm'] + '/ownerloan/getBaseInfo' , data,function(result){
        		var result = result.iqbResult.result;
            	$('#merchantName').text(Formatter.isNull(result.merchantName)); 
            	$('#bizTypeName').text(Formatter.isNull(result.bizTypeName)); 
            	$('#orderId').text(Formatter.isNull(procBizId)); 
            	$('#status').text(Formatter.riskStatus(result.status)); 
            	$('#realName').text(Formatter.isNull(result.realName)); 
            	$('#regId').text(Formatter.isNull(result.regId)); 
            });
        	IQB.post(urls['cfm'] + '/ownerloan/getMortgageInfo' , data,function(result){
        		var result = result.iqbResult.result;
            	$('#suggestAmt').text(Formatter.isNull(result.suggestAmt)); 
            	$('#suggestItems').text(Formatter.isNull(result.suggestItems)); 
            	$('#storeRiskAdvice').text(Formatter.isNull(result.storeRiskAdvice));
            	$('#trialRiskAdvice').text(Formatter.isNull(result.trialRiskAdvice));  
            });
        	IQB.post(urls['cfm'] + '/ownerloan/getDeptSignInfo' , data,function(result){
        		var result = result.iqbResult.result;
        		$('#orderAmt2').text(Formatter.money(result.orderAmt));
				$('#gpsAmt').text(Formatter.money(result.gpsAmt));
				$('#planId').text(Formatter.isNull(result.planName));
        		$('#gpsTrafficFee').text(Formatter.money(result.gpsTrafficFee));
				$('#preAmt').text(Formatter.money(result.preAmt));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#sbAmt').text(Formatter.money(result.sbAmt));
				$('#monthMake').text(Formatter.money(result.monthInterest));
				$('#contractAmt').text(Formatter.money(result.contractAmt));
				$('#finalRiskAdvice').text(Formatter.isNull(result.finalRiskAdvice));
				$('#preAmtStatus').text(Formatter.preAmtStatus(result.preAmtStatus));
				if(result.preAmtStatus == 1){
					$('.outOrderId-div').show();	
					IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: window.procBizId}, function(result){
						if(result != '' && result != null && result.length > 1){
							$('#outOrderIdMore').show();
							_this.cache.outOrderId = result;
						}else{
							$('#outOrderId').text(result[0].outOrderId);	
						}	
				   });
				}else{
					$('.outOrderId-div').hide();	
				}
            });
        },
        outOrderIdMore : function(){
			$('#open-win').modal('show');
			var result = _this.cache.outOrderId;
			//赋值
			var tableHtml = '';
			for(var i=0;i<result.length;i++){
				tableHtml += "<tr><td>"+Formatter.moneyCent(result[i].amount)+
				"</td><td>"+Formatter.timeCfm2(result[i].tranTime)+"</td><td>"+
				result[i].outOrderId+"</td></tr>";
			}
			$(".outOrderIdTable").find('tbody').find('tr').remove();
			$(".outOrderIdTable").append(tableHtml);
			$("#btn-close2").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
        openApproveWin: function(){
        	if($('#updateForm').form('validate')){
        		$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
			}
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/WfTask/procApproveHistory',
				pagination: false,
				queryParams: {
					procInstId: window.procInstId,
				}
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
        init: function(){ 
        	_this.showHtml(); 
        	$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			$('#btn-uploadType41').on('click', function(){$('#file').attr('fileType', 41);$('#file').click();});	
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
            _this.initApprovalHistory();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.carLoanReceipt.init();
});