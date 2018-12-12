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

/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.carSaleApproval');
IQB.carSaleApproval = function(){
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
		openApproveWin: function(){
			//验证
			if($('#arriveAmt').val() == ''){
				IQB.alert('到帐金额为空，无法审核');
				return false;
			}
			
			//保存信息
			var arriveAmt = $("#arriveAmt").val();
			var transNo = $("#transNo").val();
			
			var data = {
					"receivedAmt": arriveAmt,
				    "serialNumber": transNo,
				    "orderId": window.procBizId,
			}
			IQB.post(urls['cfm'] + '/carstatus/saveAssessInfo', data, function(result){
				if(result.success == 1){
					$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
				}
			})
		},
		closeApproveWin: function(){
			$('#approve-win').modal('hide');
		},
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/carstatus/selOrderInfo', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				if(result != '' && result != null){
					$('#merchName').text(result.merchantNo);
					$('#orderId').text(result.orderId);
					$('#realName').text(result.realName);
					$('#borrowAmt').text(Formatter.money(result.orderAmt));
					$('#orderItems').text(result.orderItems);
					$('#orderName').text(result.orderName);
					$('#VIN').text(result.carNo);
					$('#plate').text(result.plate);
					$('#inTime').text(Formatter.timeCfm2(result.intoGarageDate));
					//填写的信息回显
					$('#assessAmt').text(Formatter.money(result.assessAmt));
					$('#remark').text(result.saleRemark);
					$("#hqAssessAmt").text(Formatter.money(result.hqEvaluatesAmt));
					$("#hqCheckAmt").text(Formatter.money(result.hqCheckAmt));
					$("#hqRemark").text(result.evaluatesRemark);
					$("#shouldRepay").text(Formatter.money(result.shouldPayAmt));
					$("#disposeAmt").text(Formatter.money(result.dealAmt));
					$("#arriveAmt").text(Formatter.money(result.receivedAmt));
					$("#transNo").text(result.serialNumber);
					//信息
					var dealerEvaluatesInfo = result.dealerEvaluatesInfoList;
					if(dealerEvaluatesInfo != null){
						$('#price1').text(Formatter.money(dealerEvaluatesInfo[0].dealerAmt));
						$('#name1').text(dealerEvaluatesInfo[0].dealerName);
						$('#phone1').text(dealerEvaluatesInfo[0].dealerMobile);
						var creditHtml = '';
						for(var i=1;i<dealerEvaluatesInfo.length;i++){
							_this.cache.i = i + 1;
							creditHtml += "<tr>"+
							"<td><span class='text-muted'>"+Formatter.money(dealerEvaluatesInfo[i].dealerAmt)+"</span></td>"+
							"<td><span class='text-muted'>"+dealerEvaluatesInfo[i].dealerName+"</span></td>"+
							"<td><span class='text-muted'>"+dealerEvaluatesInfo[i].dealerMobile+"</span></td>"+
							"</tr>"
						}
						$('#bondsTable').append(creditHtml);
					}
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
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [30,31,32]}, function(result){
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.carSaleApproval.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		addTr : function(){
			IQB.confirm('确定要增加一行吗？',function(){
				_this.cache.i = _this.cache.i + 1;
				var trrHtml =   "<tr>"+	
				                    "<td><input type='checkbox' id='checkbox"+_this.cache.i+"' name='checkbox'></td>"+
						    		"<td><input type='text' id='price"+_this.cache.i+"' name='bondsmaName"+_this.cache.i+"' class='form-control input-sm'></td>"+				    		
							    	"<td><input type='text' id='name"+_this.cache.i+"' name='bondsmaName"+_this.cache.i+"' class='form-control input-sm'></td>"+
							    	"<td><input type='text' id='phone"+_this.cache.i+"' name='bondsmanIdNo"+_this.cache.i+"' class='form-control input-sm' maxlength='11'></td>"+				    		
					    		"</tr>"
				$('#bondsTable').append(trrHtml);
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initApprovalTask();
			_this.initApprovalHistory();
			_this.showFile();
			//增加一行
			$('#btn-addTr').click(function(){_this.addTr()});
			//删除一行
			$('#btn-deleteTr').click(function(){_this.deleteTr()});

			$('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			
			$('#btn-uploadTypeThirty').on('click', function(){$('#file').attr('fileType', 30);$('#file').click();});
			$('#btn-uploadTypeThirtyone').on('click', function(){$('#file').attr('fileType', 31);$('#file').click();});
			$('#btn-uploadTypeThirtyTwo').on('click', function(){$('#file').attr('fileType', 32);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});			
		}
	}
	return _this;
}();

$(function() {
	IQB.carSaleApproval.init();
});


