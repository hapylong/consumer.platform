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

$package('IQB.carLoanAppraise');
IQB.carLoanAppraise = function() {
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
					      		 	'<h5>' + option.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + option.imgPath + '" onclick="IQB.carLoanAppraise.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				IQB.post(urls['cfm'] + '/image/multiUploadImage', {imgs: arr}, function(resultInfo){
					$('#file').val('');
					$('#btn-upload').prop('disabled', false);
					$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
					$('#td-' + fileType).append(html);
					if(_this.cache.viewer.viewerTwo){
						_this.cache.viewer.viewerTwo.update();
					}else{
						_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
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
		showFile1: function(){	
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [17,18,19,20,21,22,23]}, function(result){
				var is = false;
				$.each(result.iqbResult.result, function(i, n){
					var extensionName = Formatter.getExtensionName(n.imgName);
					if(Formatter.extensionName.doc.contain(extensionName)){
						
					}else{
						var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
						      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
						      			'<div class="caption">' +
						      			'<h5>' + n.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + n.imgPath + '" onclick="IQB.carLoanAppraise.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
						      			'</div>' + 
						      		'</div>';
						$('#td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('viewerTwo'), {});
				}
			});
		},
        // 回显赋值
        showHtml: function() {
        	$(".inst").hide();
        	var data = {
					"orderId": procBizId,
			}
        	IQB.post(urls['cfm'] + '/ownerloan/getBaseInfo' , data,function(result){
        		var result = result.iqbResult.result;
        		$('#orderId').text(Formatter.isNull(procBizId)); 
            	$('#status').text(Formatter.orderStatus(result.status));
        		$('#merchantName').text(result.merchantName);
        		$('#bizType').text(result.bizTypeName);
        		$('#realName').text(result.realName);
        		$('#regId').text(result.regId);
        		$('#idNo').text(result.idNo);
        		if (parseInt(result.idNo.substr(16, 1)) % 2 == 1) {
        			$('#sex').text('男');
        		    } else {
        		    $('#sex').text('女');
        		    }
        			//获取年龄
        			var myDate = new Date();
        			var month = myDate.getMonth() + 1;
        			var day = myDate.getDate();
        			var age = myDate.getFullYear() - result.idNo.substring(6, 10) - 1;
        			if (result.idNo.substring(10, 12) < month || result.idNo.substring(10, 12) == month && result.idNo.substring(12, 14) <= day) {
        			age++;
        			}
        		$('#age').text(age);
        		$('#applyAmt').text(Formatter.money(result.orderAmt));
        		$('#applyItems').text(result.orderItems);
        		$('#bankName').text(result.bankName);
        		$('#bankNo').text(result.bankCardNo);
        		var orderName = result.orderName.split('-');
        		$('#carbrand').text(orderName[0]);
        		$('#carDetail').text(orderName[1]);
        		var checkInfoBean = result.checkInfoBean;
        		$('#address').text(checkInfoBean.address);
        		$('#job').text(checkInfoBean.companyName);
        		$('#userName1').text(checkInfoBean.rName1);
        		$('#mobile1').text(checkInfoBean.phone1);
        		$('#relation1').text(checkInfoBean.relation1);
        		$('#userName2').text(checkInfoBean.rName2);
        		$('#mobile2').text(checkInfoBean.phone2);
        		$('#relation2').text(checkInfoBean.relation2);
        		$('#userName3').text(checkInfoBean.rName3);
        		$('#mobile3').text(checkInfoBean.phone3);
        		$('#relation3').text(checkInfoBean.relation3);
        		$('#userName4').text(checkInfoBean.rName4);
        		$('#mobile4').text(checkInfoBean.phone4);
        		$('#relation4').text(checkInfoBean.relation4);
        		$('#plate').text(Formatter.isNull(result.plate)); 
            	$('#carAge').text(Formatter.isNull(result.carAge)); 
            	$('#carBrand').text(Formatter.isNull(result.carBrand)); 
            	$('#carDetail').text(Formatter.isNull(result.carDetail)); 
            });
        	IQB.post(urls['cfm'] + '/ownerloan/getCarinfo' , data,function(result){
        		var result = result.iqbResult.result;
        		$('#carConfig').val(Formatter.isNull1(result.carConfig)); 
            	$('#carColor').val(Formatter.isNull1(result.carColor)); 
            	$('#carNo').val(Formatter.isNull1(result.carNo)); 
            	$('#carEmissions').val(Formatter.isNull1(result.carEmissions)); 
            	$('#passengerNum').val(Formatter.isNull1(result.passengerNum)); 
            	$('#mileage').val(Formatter.isNull1(result.mileage)); 
            	$('#firstBuyAmt').val(Formatter.isNull1(Formatter.moneyTs(result.firstBuyAmt))); 
            	$('#regOrg').val(Formatter.isNull1(result.regOrg)); 
            	if(result.firstRegDate==null){
                	$("#firstRegDate").val(_this.getNowFormatDate());
            	}else{
            		$('#firstRegDate').val(Formatter.isNull1(result.firstRegDate)); 
            	}
            	$('#transferNum').val(Formatter.isNull1(result.transferNum)); 
            	$('#mortgageType').val(Formatter.isNull1(result.mortgageType)); 
            	$('#mortgageCompany').val(Formatter.isNull1(result.mortgageCompany)); 
            	$('#instAmt').val(Formatter.isNull1(Formatter.moneyTs(result.instAmt))); 
            	$('#instMonthInterest').val(Formatter.isNull1(Formatter.moneyTs(result.instMonthInterest))); 
            	$('#instItems').val(Formatter.isNull1(result.instItems)); 
            	$('#instRepayedItems').val(Formatter.isNull1(result.instRepayedItems)); 
            	$('#assessPrice').val(Formatter.isNull1(Formatter.moneyTs(result.assessPrice))); 
            	$('#mortgageFlag').val(result.mortgageFlag); 
            	$('#assessSuggest').val(Formatter.isNull1(result.assessSuggest));
            	var key = result.mortgageFlag;
				if (key==1){
					$(".inst").show();
					$("#mortgageType").validatebox({required:true });
					$("#mortgageCompany").validatebox({required:true });
					$("#instAmt").validatebox({required:true });
					$("#instMonthInterest").validatebox({required:true });
					$("#instItems").validatebox({required:true });
					$("#instRepayedItems").validatebox({required:true });
				}else if (key==2){
					$(".inst").hide();
					$("#mortgageType").validatebox({required:false });
					$("#mortgageCompany").validatebox({required:false });
					$("#instAmt").validatebox({required:false });
					$("#instMonthInterest").validatebox({required:false });
					$("#instItems").validatebox({required:false });
					$("#instRepayedItems").validatebox({required:false });
				}else{
					$(".inst").hide();
					$("#mortgageType").validatebox({required:false });
					$("#mortgageCompany").validatebox({required:false });
					$("#instAmt").validatebox({required:false });
					$("#instMonthInterest").validatebox({required:false });
					$("#instItems").validatebox({required:false });
					$("#instRepayedItems").validatebox({required:false });
				}
            });
        },
        //    时间格式化
        getNowFormatDate : function() {
	          var date = new Date();
	          var seperator1 = "-";
	          var year = date.getFullYear();
	          var month = date.getMonth() + 1;
	          var strDate = date.getDate();
	          if (month >= 1 && month <= 9) {
	              month = "0" + month;
	          }
	          if (strDate >= 0 && strDate <= 9) {
	              strDate = "0" + strDate;
	          }
	          var currentdate = year.toString() +-+ + month.toString() +-+ + strDate.toString();
	          return currentdate;
	    },
        openApproveWin: function(){
        	if($('#updateForm').form('validate')){
				if($('#td-17').find('div').length == 0){
					IQB.alert('车辆车架号未上传图片，无法审核');
					return false;
				};
				if($('#td-18').find('div').length == 0){
					IQB.alert('车辆外观（正面、侧面、后面）未上传图片，无法审核');
					return false;
				};
				if($('#td-19').find('div').length == 0){
					IQB.alert('车辆室内（驾驶室、操控台、前排座位、后排座位）未上传图片，无法审核');
					return false;
				};
				if($('#td-20').find('div').length == 0){
					IQB.alert('车辆发动机舱未上传图片，无法审核');
					return false;
				};
				if($('#td-21').find('div').length == 0){
					IQB.alert('车主与车辆合影未上传图片，无法审核');
					return false;
				};
				if($('#td-22').find('div').length == 0){
					IQB.alert('车辆行驶里程未上传图片，无法审核');
					return false;
				};
				if($('#td-23').find('div').length == 0){
					IQB.alert('车辆基本信息表未上传图片，无法审核');
					return false;
				};
				var data = {
					'orderId':procBizId,	
					'carConfig':$('#carConfig').val(),
					'carColor':$('#carColor').val(),
					'carNo':$('#carNo').val(),
					'carEmissions':$('#carEmissions').val(),
					'passengerNum':$('#passengerNum').val(),
					'mileage':$('#mileage').val(),
					'firstBuyAmt':Formatter.removeMoneyFormatter($('#firstBuyAmt').val()),
					'firstRegDate':$('#firstRegDate').val(),
					'transferNum':$('#transferNum').val(),
					'mortgageType':$('#mortgageType').val(),
					'mortgageCompany':$('#mortgageCompany').val(),
					'instAmt':Formatter.removeMoneyFormatter($('#instAmt').val()),
					'instMonthInterest':Formatter.removeMoneyFormatter($('#instMonthInterest').val()),
					'instItems':$('#instItems').val(),
					'instRepayedItems':$('#instRepayedItems').val(),
					'regOrg':$('#regOrg').val(),
					'assessSuggest':$('#assessSuggest').val(),
					'assessPrice':Formatter.removeMoneyFormatter($('#assessPrice').val()),
				}
				IQB.post(urls['cfm'] + '/ownerloan/updateCarInfo', data, function(result){
					if(result.success == 1){
						$('#approve-win').modal({backdrop: 'static', keyboard: false, show: true});
					}
				})
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
		getDictListByDictType: function(selectId, dictType){
			var req_data = {'dictTypeCode': dictType};
			IQB.post(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
				$('#' + selectId).prepend("<option value=''>请选择</option>");
				for(i=0;i<result.iqbResult.result.length;i++){
					$('#' + selectId).append("<option value='"+result.iqbResult.result[i].id+"'>"+result.iqbResult.result[i].text+"</option>");
				}
				//showHtml
			})
		},
		check: function(){
			$("#mortgageFlag").change(function(){
				var key = $('#mortgageFlag').val();
				if (key==1){
					$(".inst").show();
					$("#mortgageType").validatebox({required:true });
					$("#mortgageCompany").validatebox({required:true });
					$("#instAmt").validatebox({required:true });
					$("#instMonthInterest").validatebox({required:true });
					$("#instItems").validatebox({required:true });
					$("#instRepayedItems").validatebox({required:true });
				}else if (key==2){
					$(".inst").hide();
					$("#mortgageType").validatebox({required:false });
					$("#mortgageCompany").validatebox({required:false });
					$("#instAmt").validatebox({required:false });
					$("#instMonthInterest").validatebox({required:false });
					$("#instItems").validatebox({required:false });
					$("#instRepayedItems").validatebox({required:false });
				}else{
					$(".inst").hide();
					$("#mortgageType").validatebox({required:false });
					$("#mortgageCompany").validatebox({required:false });
					$("#instAmt").validatebox({required:false });
					$("#instMonthInterest").validatebox({required:false });
					$("#instItems").validatebox({required:false });
					$("#instRepayedItems").validatebox({required:false });
				}
			});
		},
		formatterMoney : function(){
		    var firstBuyAmt = $('#firstBuyAmt').val();
		    var instAmt = $('#instAmt').val();
		    var instMonthInterest = $('#instMonthInterest').val();
		    var assessPrice = $('#assessPrice').val();
		    $('#firstBuyAmt').val(Formatter.moneyTs(firstBuyAmt));
		    $('#instAmt').val(Formatter.moneyTs(instAmt));
		    $('#instMonthInterest').val(Formatter.moneyTs(instMonthInterest));
		    $('#assessPrice').val(Formatter.moneyTs(assessPrice));
		},
        init: function(){ 
        	_this.getDictListByDictType('mortgageType', 'mortgageType'); 
        	_this.showHtml();
        	_this.check(); 
        	_this.showFile();
        	_this.showFile1();
            _this.initApprovalHistory();
            $('#firstBuyAmt').on('blur',function(){_this.formatterMoney()});
            $('#instAmt').on('blur',function(){_this.formatterMoney()});
            $('#instMonthInterest').on('blur',function(){_this.formatterMoney()});
            $('#assessPrice').on('blur',function(){_this.formatterMoney()});
            $('#btn-approve').on('click', function(){_this.openApproveWin()});
			$('#btn-unassign').on('click', function(){_this.unassign()});
			$('#btn-approve-save').on('click', function(){_this.approve()});
			$('#btn-approve-close').on('click', function(){_this.closeApproveWin()});
			$('#btn-uploadType17').on('click', function(){$('#file').attr('fileType', 17);$('#file').click();});	
			$('#btn-uploadType18').on('click', function(){$('#file').attr('fileType', 18);$('#file').click();});
			$('#btn-uploadType19').on('click', function(){$('#file').attr('fileType', 19);$('#file').click();});
			$('#btn-uploadType20').on('click', function(){$('#file').attr('fileType', 20);$('#file').click();});
			$('#btn-uploadType21').on('click', function(){$('#file').attr('fileType', 21);$('#file').click();});
			$('#btn-uploadType22').on('click', function(){$('#file').attr('fileType', 22);$('#file').click();});
			$('#btn-uploadType23').on('click', function(){$('#file').attr('fileType', 23);$('#file').click();});
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
	datepicker(firstRegDate);
    IQB.carLoanAppraise.init();
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