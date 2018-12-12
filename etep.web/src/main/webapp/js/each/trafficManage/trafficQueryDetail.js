function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var procBizId = getQueryString('orderId');

$package('IQB.trafficQueryDetail');
IQB.trafficQueryDetail = function(){
	var _this = {
		cache: {			
			viewer: {}
		},	
		initApprovalTask: function(){//初始化订单详情
			IQB.post(urls['cfm'] + '/trafficManage/getTrafficManageDetail', {orderId: window.procBizId}, function(result){
				var result = result.iqbResult.result;
				$('#merchantName').text(Formatter.isNull(result.merchantName));
				$('#realName').text(Formatter.isNull(result.realName));
				$('#regId').text(Formatter.isNull(result.regId));
				$('#orderId').text(Formatter.isNull(result.orderId));		
				
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#bizType').text(Formatter.bizType(result.bizType));
				$('#plate').text(Formatter.isNull(result.plate));
				$('#carNo').text(Formatter.isNull(result.carNo));
				$('#orderItems').text(Formatter.isNull(result.orderItems));
				//填写的信息回显
				$('#registerDate').text(Formatter.isNull(result.registerDate));
				$('#carOwner').text(Formatter.isNull(result.currentMaster));
				$('#carColor').text(Formatter.isNull(result.color));

			});
		},
		showHistoryList : function(){
			IQB.post(urls['cfm'] + '/trafficManage/selectTrafManaAdditionalhistory', {orderId: window.procBizId,'checkStatus':'2'}, function(result){
				//渲染列表
				var tableHtml = '';
				var result =result.iqbResult.result;
				for(i=0;i<result.length;i++){
					var j= i+1;
					tableHtml +="<tr><td>"+[j]+"</td><td><a href='javascript:;' uuid='"+ result[i].additionalNo +"' onclick='IQB.trafficQueryDetail.previewImg(event);'>"+(result[i].carInspectTime)+
						"</a></td><td>"+result[i].comInsOrg+"</td><td>"+result[i].comInsOverTime+"</td><td>"
						+(result[i].comTraAccOrg)+"</td><td>"+(result[i].comTraAccOverTime)+"</td><td>"+(result[i].operatorTime)+"</td><td>"+(result[i].operatorUser)+"</td></tr>";
				}
				$("#datagrid2").append(tableHtml);
			})
		},
		previewImg: function(event){
			var tarent = event.currentTarget;//当前操作对象
			$('#open-win').modal('show');
			//清空列表里的图片
			$('#viewerTwo').find('.thumbnail').remove();
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId:$(tarent).attr('uuid'), imgType: [107,108,109,110]}, function(result){
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
						$('.td-' + n.imgType).append(html);
						is = true;
					}
				});
				if(is){
					_this.cache.viewer.viewerTwo = new Viewer(document.getElementById('open-win'), {});
				}
			});
			$("#btn-close").click(function(){
      	        $('#open-win').modal('hide');
		    });
		},
		init: function(){ 	
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});

			_this.initApprovalTask();
			_this.showHistoryList();
		}
	}
	return _this;
}();

$(function(){
	IQB.trafficQueryDetail.init();
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