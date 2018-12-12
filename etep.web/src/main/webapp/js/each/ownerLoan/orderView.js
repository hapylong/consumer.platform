function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var procTaskId = getQueryString('procTaskId');
var procBizId = guid();
var procInstId = getQueryString('procInstId');

$package('IQB.orderView');
IQB.orderView = function() {
    var _this = {
        cache: {
        	viewer: {}
        }, 
        showText : function(){
        	IQB.get(urls['cfm'] + '/ownerloan/getbaseinfo', {orderId:window.procBizId}, function(result){
        		var result = result.iqbResult.result;
        		$('#merchantName').text(result.merchantName);
        		$('#bizType').text(result.bizTypeName);
        		$('#realName').text(result.realName);
        		$('#regId').text(result.regId);
        		$('#idNo').text(result.idNo);
        		IdCard(result.idNo);
        		$('#applyAmt').text(Formatter.money(result.orderAmt));
        		$('#applyItems').text(result.orderItems);
        		$('#bankName').text(result.bankCard);
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
        	})
        	IQB.get(urls['cfm'] + '/ownerloan/getcarinfo', orderId:window.procBizId, function(result){
        		var result = result.iqbResult.result;
        		$('#plate').text(result.plate);
        		$('#carAge').text(result.carAge);
        	})
        },
        IdCard : function (UUserCard){
			//获取性别
			if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) {
			$('#sex').text('男');
		    } else {
		    $('#sex').text('女');
		    }
			//获取年龄
			var myDate = new Date();
			var month = myDate.getMonth() + 1;
			var day = myDate.getDate();
			var age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1;
			if (UUserCard.substring(10, 12) < month || UUserCard.substring(10, 12) == month && UUserCard.substring(12, 14) <= day) {
			age++;
			}
			$('#age').text(age);
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
						      				'<h5>' + n.imgName + '</h5>'+
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
					      		 	'<h5>' + option.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + option.imgPath + '" onclick="IQB.orderView.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
        	_this.showFile(); 
        	_this.showText();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    datepicker(registDate);
    IQB.orderView.init();
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