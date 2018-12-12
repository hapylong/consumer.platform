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

$package('IQB.carLoanInstallGpsView');
IQB.carLoanInstallGpsView = function() {
    var _this = {
        cache: {
        	viewer: {}
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
		//回显信息
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
        	IQB.post(urls['cfm'] + '/ownerloan/getDeptSignInfo' , data,function(result){
        		var result = result.iqbResult.result;
            	$('#gpsInstDate').text(Formatter.isNull(result.gpsInstDate)); 
            	$('#gpsDeviceNum').text(Formatter.isNull(result.gpsDeviceNum)); 
            	$('#gpsDeviceAddress').text(Formatter.isNull(result.gpsDeviceAddress)); 
            	$('#gpsNum').text(Formatter.isNull(result.gpsNum)); 
            });
        },
        showFile: function(){
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: window.procBizId, imgType: [40]}, function(result){
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
        	_this.initApprovalHistory();
        	_this.showFile(); 
        	_this.showHtml();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.carLoanInstallGpsView.init();
});
