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

$package('IQB.afterLoanCar');
IQB.afterLoanCar = function(){
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
				console.log($('#repaymentFlag').val())
				variableData.isRepayment = $('#repaymentFlag').val();
				variableData.isRetrieve = $('#collectFlag').val();
				var bizData = {}
				bizData.procBizId=procBizId;
				bizData.isRepayment = $('#repaymentFlag').val();
				bizData.isRetrieve = $('#collectFlag').val();
				var procData = {}
				procData.procTaskId = procTaskId;
				var option = {};
				option.authData=authData;
				option.variableData = variableData;
				option.bizData = bizData;
				option.procData = procData;
				IQB.post(urls['rootUrl'] + '/workFlow/saveWfInfo', {orderId: window.procBizId, receiveAmt: $('#receiveAmt').val(), remark: $.trim($('#remark').val())}, function(){
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
				if($("#repaymentFlag").val()==""){
					IQB.alert("请选是否还款");
					  return false;
				}
				if($("#repaymentFlag").val()!="1"){
					if($("#collectFlag").val()==""){
						IQB.alert("请选车辆是否收回");
						  return false;
					}
					if($("#collectFlag").val()=="1"){
						if($("#intoGarageDate").val()==""){
							IQB.alert("请选入库时间");
							  return false;
						}
						if($("#trailerReason").val()==""){
							IQB.alert("请选拖车原因");
							  return false;
						}
						if($('#td-35').find('div').length == 0){
							IQB.alert('入库截图信息不完善，无法审核');
							return false;
						};
					}
				}
				var option = {
						'orderId':_this.cache.orderId,
						'repaymentFlag':$('#repaymentFlag').val(),
						'collectFlag':$('#collectFlag').val(),
						'intoGarageDate':$('#intoGarageDate').val(),
						'intoGarageName':$('#intoGarageName').val(),
						'trailerReason':$('#trailerReason').val(),
						'collectOpinion':$('#collectOpinion').val(),
					}
				IQB.post(urls['cfm'] + '/afterLoan/updateAfterLoanInfo', option, function(result){
					if(result.success == '1'){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})		
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				$('#orderId').text(Formatter.isNull(result.orderId));
				_this.cache.orderId = result.orderId;
				_this.cache.realName = result.realName;
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				$('#lostContactFlag').text(Formatter.yOrn(result.lostContactFlag));
				$('#checkOpinion').text(Formatter.isNull(result.checkOpinion));
				$('#afterLoanOpinion').text(Formatter.isNull(result.afterLoanOpinion));
				$('#gpsArea').text(Formatter.isNull(result.gpsArea));
			});
			IQB.post(urls['cfm'] + '/afterLoan/getGpsInfoByOrderId', {orderId: window.procBizId}, function(result){
				var tableHtml = '';
				var result =result.iqbResult.result;
				for(i=0;i<result.length;i++){
					var j= i+1;
					tableHtml +="<tr><td>"+[j]+"</td><td>"+result[i].createTime+
						"</td><td>"+Formatter.gpsStatusShow(result[i].gpsStatus)+"</td><td>"
						+(result[i].remark)+"</td><td>"+(result[i].disposalScheme)+"</td></tr>";
				}
				$("#datagrid2").append(tableHtml);
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
		forBill : function(){
			IQB.get(urls['cfm'] + '/instRemindPhone/queryBillIfoByOId', {'orderId':_this.cache.orderId}, function(result) {
				var result =result.iqbResult.result;
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win').modal('show');
					//赋值
					$("#billRealName").val(_this.cache.realName);
					$("#billOrderId").val(_this.cache.orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td><td>"+Formatter.isMobileCollection(result[i].mobileCollection)+"</td><td>"+Formatter.isDealReason(result[i].mobileDealOpinion)+"</td><td>"+Formatter.isNull(result[i].remark)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close").click(function(){
        	    $('#open-win').modal('hide');
			});
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [35,36]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.afterLoanCar.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.afterLoanCar.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		rule : function(){
			$("#repaymentFlag").change(function(){
				if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()==''){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()=='1'){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()=='2'){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()==''){
					$(".collectBox").show();
					$(".reBox").show();
					$(".cageBox").show();
				}else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()=='1'){
					$(".collectBox").show();
					$(".reBox").show();
					$(".cageBox").show();
				}
				else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()=='2'){
					$(".collectBox").show();
					$(".reBox").hide();
					$(".cageBox").hide();
				}
			})
			$("#collectFlag").change(function(){
				if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()==''){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()=='1'){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()=='1'&&$('#collectFlag').val()=='2'){
					$(".collectBox").hide();
					$(".reBox").hide();
					$(".cageBox").hide();
				}else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()==''){
					$(".collectBox").show();
					$(".reBox").show();
					$(".cageBox").show();
				}else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()=='1'){
					$(".collectBox").show();
					$(".reBox").show();
					$(".cageBox").show();
				}
				else if($('#repaymentFlag').val()!='1'&&$('#collectFlag').val()=='2'){
					$(".collectBox").show();
					$(".reBox").hide();
					$(".cageBox").hide();
				}
			})
		},
		initSelect : function(){
			IQB.getDictListByDictType2('trailerReason', 'car_back_reason');
			$('select[name="trailerReason"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.showFile();
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.initSelect();
			_this.rule();
			//账单详情
			$('#btn-uploadType35').on('click', function(){$('#file').attr('fileType', 35);$('#file').click();});
			$('#btn-uploadType36').on('click', function(){$('#file').attr('fileType', 36);$('#file').click();});
			$('#btn-check').on('click',function(){_this.forBill()});
			
			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function(){
	IQB.afterLoanCar.init();
	datepicker(intoGarageDate);
});		
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};