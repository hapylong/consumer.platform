function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('orderId');
var merchantId = getQueryString('merchantId');

$package('IQB.carCollection');
IQB.carCollection = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		approve: function() {
			if($('#updateForm').form('validate')){
				if($('#td-102').find('div').length == 0){
					IQB.alert('付款截图信息不完善，无法审核');
					return false;
				};
				var option = {
						'orderId':window.procBizId,
						'shouldReceivedDate':$('#collectionDate').val(),
						'shouldReceivedAmt':$('#collectionMoney').val(),
						'commision':$('#commision').val(),
						'trailerAmt':$('#towingFee').val(),
						'stopAmt':$('#parkingFee').val(),
						'otherAmt':$('#otherFee').val(),
						'totalAmt':_this.cache.allAmt,
						'receivedRemark':$('#remark').val(),
						'processMethod':'1'
				}
				IQB.post(urls['cfm'] + '/afterLoan/saveInstManageCarReceive', option, function(result){
					if(result.success == '1'){
						//拖回流程
						var bizData = {}
						bizData.procBizId='HK' + window.procBizId;//orderID
						bizData.procBizMemo='车辆回款'+';'+_this.cache.merchantNo +';'+ _this.cache.realName +';'+ _this.cache.regId;
						bizData.procBizType='';
						bizData.procOrgCode=window.merchantId;
						var authData= {}
						authData.procAuthType = "2";
						var procData = {}
						procData.procDefKey = 'outsource_received_payments';
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
							if(result.success != "1") {
								IQB.alert(result.retUserInfo);
							}else{
								IQB.post(urls['cfm'] + '/afterLoan/updateManagerCarInfoForCarBack', {orderId: window.procBizId}, function(result){
									var url = window.location.pathname;
				            		var param = window.parent.IQB.main2.fetchCache(url);
				            		var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
									var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + true + ',' + true + ',' + null + ')';
									window.parent.IQB.main2.call(callback, callback2);
								})
							}
						});
					}
				})
			}		
		},
		unassign: function() {
			var url = window.location.pathname;
    		var param = window.parent.IQB.main2.fetchCache(url);
    		var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
			//var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + true + ',' + true + ',' + null + ')';
			window.parent.IQB.main2.call(callback);
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/afterLoan/getOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				_this.cache.merchantNo = result.merchantNo;
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				$('#orderId').text(Formatter.isNull(result.orderId));
				_this.cache.regId = result.regId;
				_this.cache.orderId = result.orderId;
				_this.cache.realName = result.realName;
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#orderName').text(Formatter.isNull(result.orderName));
				_this.cache.orderName = result.orderName;
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				
				//回款信息
				$('#repayNo').text(Formatter.isNull(result.hasRepayNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				$('#enNo').text(Formatter.isNull(result.engine));
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
		cal : function(){
			var commision = $('#commision').val();
			var towingFee = $('#towingFee').val();
			var parkingFee = $('#parkingFee').val();
			var otherFee = $('#otherFee').val();
			if(commision != '' && towingFee != '' && parkingFee != '' && otherFee != ''){
				_this.cache.allAmt = Number(commision)+Number(towingFee)+Number(parkingFee)+Number(otherFee);
				$('#allAmt').val(_this.cache.allAmt);
			}
		},
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [102,103]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      				'<h5>' + n.imgName + '</h5><h6><a filePath="' + n.imgPath + '" onclick="IQB.carCollection.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.carCollection.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.showFile();
			_this.initApprovalTask();
			//账单详情
			$('#btn-uploadType102').on('click', function(){$('#file').attr('fileType', 102);$('#file').click();});
			$('#btn-uploadType103').on('click', function(){$('#file').attr('fileType', 103);$('#file').click();});
			$('#btn-check').on('click',function(){_this.forBill()});
			
			$('#btn-approve').on('click', function(){_this.approve()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function(){
	IQB.carCollection.init();
	datepicker(collectionDate);
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