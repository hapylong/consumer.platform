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

$package('IQB.orderApply');
IQB.orderApply = function() {
    var _this = {
        cache: {
        	viewer: {}
        }, 
        Save: function(){
        	$('#btn-save').attr('disabled',true);
        	if($('#merchNames').val()=="全部商户" || $('#merchNames').val()==""){
				alert("请选择商户");
				$('#btn-save').removeAttr('disabled');
				return false;
			}
			if($('#updateForm').form('validate')){
				for(var i=0;i<$('.personalInfo').length;i++){
					if($($('.personalInfo')[i]).find('div').length == 0){
						IQB.alert('个人信息不完善！');
						$('#btn-save').removeAttr('disabled');
						return false;
					}
				}
				for(var i=0;i<$('.carInfo').length;i++){
					if($($('.carInfo')[i]).find('div').length == 0){
						IQB.alert('车辆信息不完善！');
						$('#btn-save').removeAttr('disabled');
						return false;
					}
				}
				for(var i=0;i<$('.faceInfo').length;i++){
					if($($('.faceInfo')[i]).find('div').length == 0){
						IQB.alert('面审资料不完善！');
						$('#btn-save').removeAttr('disabled');
						return false;
					}
				}
				var option = {
	        			'userName':$('#realName').val(),
	        			'idNo':$('#idNo').val(),
	        			'bankCardNum':$('#bankNo').val(),
	        			'mobile':''
	        	};
				IQB.postIfFail(urls['cfm'] + '/business/authInfo', option, function(result){
	        		if(result.result_code == 1){
	        			var data = {
								'uuid':procBizId,
								"merchantNo":$('#merchNames').attr('merchantNo'),
							    "realName": $('#realName').val(),
							    "regId": $('#regId').val(),
							    "idNo": $('#idNo').val(),
							    "applyAmt": Formatter.removeMoneyFormatter($('#applyAmt').val()),
							    "orderItems": $('#applyItems').val(),
							    "address": $('#address').val(),
							    "job": $('#job').val(),
							    "bankName": $('#bankName').val(),
							    "bankNo": $('#bankNo').val(),
							    "userName1": $('#userName1').val(),
							    "mobile1": $('#mobile1').val(),
							    "relation1": $('#relation1').val(),
							    "userName2": $('#userName2').val(),
							    "mobile2": $('#mobile2').val(),
							    "relation2": $('#relation2').val(),
							    "userName3": $('#userName3').val(),
							    "mobile3": $('#mobile3').val(),
							    "relation3": $('#relation3').val(),
							    "userName4": $('#userName4').val(),
							    "mobile4": $('#mobile4').val(),
							    "relation4": $('#relation4').val(),
							    "plate": $('#plate').val(),
							    "carAge": $('#carAge').val(),
							    "orderName": $('#carbrand').val()+'-'+ $('#carDetail').val()
							}
							IQB.postIfFail(urls['cfm'] + '/carOwner/carOwnerBasicInfo', data, function(result){
								if(result.success == 1 && result.iqbResult.result == 'success'){
									var url = window.location.pathname;
									var param = window.parent.IQB.main2.fetchCache(url);
									var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
									window.parent.IQB.main2.call(callback);
								}else {
									IQB.alert(result.retUserInfo);	
									$('#btn-save').removeAttr('disabled');
								}
							})
	        		}else{
	        			IQB.alert(result.result_msg);
	        			$('#btn-save').removeAttr('disabled');
	        		}
	        	})
			}else{
				$('#btn-save').removeAttr('disabled');
			}
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
						      				'<h5>' + n.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + n.imgPath + '" onclick="IQB.orderApply.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
					      		 	'<h5>' + option.imgName + '</h5><h6 style="float: right;margin-top:-21px"><a filePath="' + option.imgPath + '" onclick="IQB.orderApply.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		formatterMoney : function(){
		    var applyAmt = $('#applyAmt').val();
		    $('#applyAmt').val(Formatter.moneyTs(applyAmt));
		},
        init: function(){     
        	//_this.showFile();
        	IQB.getDictListByDictType('relation1','SECTORS'); 
        	IQB.getDictListByDictType('relation2','SECTORS');
        	IQB.getDictListByDictType('relation3','SECTORS');
        	IQB.getDictListByDictType('relation4','SECTORS');
            $('#btn-save').unbind('click').on('click', function(){_this.Save()});
            $('#applyAmt').on('blur',function(){_this.formatterMoney()});
            $('#idNo').on('blur',function(){_this.IdCard($('#idNo').val())});
            $('#btn-uploadType1').on('click', function(){$('#file').attr('fileType', 1);$('#file').click();});
			$('#btn-uploadType2').on('click', function(){$('#file').attr('fileType', 2);$('#file').click();});
			$('#btn-uploadType3').on('click', function(){$('#file').attr('fileType', 3);$('#file').click();});
			$('#btn-uploadType4').on('click', function(){$('#file').attr('fileType', 4);$('#file').click();});
			$('#btn-uploadType5').on('click', function(){$('#file').attr('fileType', 5);$('#file').click();});
			$('#btn-uploadType6').on('click', function(){$('#file').attr('fileType', 6);$('#file').click();});
			$('#btn-uploadType7').on('click', function(){$('#file').attr('fileType', 7);$('#file').click();});
			$('#btn-uploadType8').on('click', function(){$('#file').attr('fileType', 8);$('#file').click();});
			$('#btn-uploadType9').on('click', function(){$('#file').attr('fileType', 9);$('#file').click();});
			$('#btn-uploadType10').on('click', function(){$('#file').attr('fileType', 10);$('#file').click();});
			$('#btn-uploadType11').on('click', function(){$('#file').attr('fileType', 11);$('#file').click();});
			$('#btn-uploadType12').on('click', function(){$('#file').attr('fileType', 12);$('#file').click();});
			$('#btn-uploadType13').on('click', function(){$('#file').attr('fileType', 13);$('#file').click();});
			$('#btn-uploadType14').on('click', function(){$('#file').attr('fileType', 14);$('#file').click();});
			$('#btn-uploadType15').on('click', function(){$('#file').attr('fileType', 15);$('#file').click();});
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.orderApply.init();
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
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
};