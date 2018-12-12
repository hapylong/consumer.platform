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

$package('IQB.carLoanDeptRisk');
IQB.carLoanDeptRisk = function() {
    var _this = {
        cache: {
        	viewer: {}
        }, 
		showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]}, function(result){
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
						      				'<h5>' + n.imgName + '</h5><h6 style="float: right;margin-top:-21px"></h6>' +
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
		showFile2: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [24,25,26,27,28,29,30]}, function(result){
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
					_this.cache.viewer.viewerThree = new Viewer(document.getElementById('viewerThree'), {});
				}
			});
		},
		showFile3: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [39]}, function(result){
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
					_this.cache.viewer.viewerFour = new Viewer(document.getElementById('viewerFour'), {});
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
            	$('#riskStatus').text(Formatter.isNull(result.riskStatus)); 
            	$('#riskMessage').text(Formatter.isNull(result.riskMessage));
            });
        	IQB.post(urls['cfm'] + '/ownerloan/getCarinfo' , data,function(result){
        		var result = result.iqbResult.result;
        		$('#carConfig').text(Formatter.isNull(result.carConfig)); 
            	$('#carColor').text(Formatter.isNull(result.carColor)); 
            	$('#carNo').text(Formatter.isNull(result.carNo)); 
            	$('#carEmissions').text(Formatter.isNull(result.carEmissions)); 
            	$('#passengerNum').text(Formatter.isNull(result.passengerNum)); 
            	$('#mileage').text(Formatter.isNull(result.mileage)); 
            	$('#firstBuyAmt').text(Formatter.money(result.firstBuyAmt)); 
            	$('#regOrg').text(Formatter.isNull(result.regOrg)); 
            	$('#firstRegDate').text(Formatter.isNull(result.firstRegDate)); 
            	$('#transferNum').text(Formatter.isNull(result.transferNum)); 
            	$('#mortgageType').text(Formatter.isNull(result.mortgageType)); 
            	$('#mortgageCompany').text(Formatter.isNull(result.mortgageCompany)); 
            	$('#instAmt').text(Formatter.money(result.instAmt)); 
            	$('#instMonthInterest').text(Formatter.money(result.instMonthInterest)); 
            	$('#instItems').text(Formatter.isNull(result.instItems)); 
            	$('#instRepayedItems').text(Formatter.isNull(result.instRepayedItems)); 
            	$('#assessPrice').text(Formatter.money(result.assessPrice)); 
            	$('#mortgageFlag').text(Formatter.yOrn(result.mortgageFlag)); 
            	$('#assessSuggest').text(Formatter.isNull(result.assessSuggest));
            	var key = result.mortgageFlag;
				if (key==1){
					$(".inst").show();
				}else if (key==2){
					$(".inst").hide();
				}else{
					$(".inst").hide();
				}
            });
        	IQB.post(urls['cfm'] + '/ownerloan/getMortgageInfo' , data,function(result){
        		var result = result.iqbResult.result;
        		$('#suggestAmt').text(Formatter.money(result.suggestAmt)); 
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
            });
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
        	_this.showFile();
        	_this.showFile1();
        	_this.showFile2();
        	_this.showFile3();
            _this.initApprovalHistory();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.carLoanDeptRisk.init();
});