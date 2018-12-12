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

/*procBizId = 'HLJLD2002170320002';
procInstId = '5c938d40-d163-11e6-8f6e-00163e10ea56';*/

$package('IQB.serEnter');
IQB.serEnter = function(){
	var _this = {
		cache: {
			viewer: {},
			flag:1
		},
		config: {
			//页面请求参数
			action: {

  			}
		},
		saveInfo:function(){
			$('#btn-save').attr('disabled',true);
			if($('#updateForm').form('validate')){
				var option = {
						'orderNo' : window.procBizId,
						"merchantId": $('#merchName').attr('merchantno'), //商户
						"bizType": $('#bizType').val(),          //业务类型
						"proType": "周转贷",                      //产品名称
						"realName": $('#realName').val(),       //姓名
						"regId": $('#regId').val(),             //手机号码
						"idCard": $('#idCard').val(),            //身份照吗
						"bankName": $('#bankCardName').val(),  //开户行
						"bankCard": $('#bankCard').val(),    //银行卡号
						"bankMobile": $('#bankMobile').val(),  //银行预留手机号码
						"address": $('#address').val(),        //常驻地址
						"companyAddress": $('#firmAddress').val(),	//公司地址
					    "companyName": $('#firmName').val(),		//公司名称
					    "companyPhone": $('#firmPhone').val(),	    //公司电话
						"orderAmt": $('#orderAmt').val(),           //申请金额
						"orderItems": $('#orderItems').val(),       //申请期限
						"creditNo": $('#creditNumber').val(),		//征信号
					    "creditPasswd": $('#creditPass').val(),		//征信密码
						"creditCode":$('#creditCode').val(),		//征信验证码
						"rName1": $('#reNameOne').val(),			//姓名1
					    "rName2": $('#reNameTwo').val(),			//姓名2
					    "rName3": $('#reNameThree').val(),			//姓名3
					    "phone1": $('#mobileOne').val(),			//手机号1
					    "phone2": $('#mobileTwo').val(),			//手机号2
					    "phone3": $('#mobileThree').val(),			//手机号3
						"relation1": $('#relationOne').val(),		//借款人关系1
					    "relation2": $('#relationTwo').val(),		//借款人关系2
					    "relation3": $('#relationThree').val(),		//借款人关系3
						"sex1": $('#sexOne').val(),					//性别1
					    "sex2": $('#sexTwo').val(),					//性别2
					    "sex3": $('#sexThree').val(),				//性别3
					    "colleagues1": $('#workmateOne').val(),		//同事1
					    "colleagues2": $('#workmateTwo').val(),		//同事2
					    "tel1": $('#workmateOneTel').val(),			//手机1
					    "tel2": $('#workmateTwoTel').val()			//手机2
				}
				IQB.post(urls['cfm'] + '/huayi/startHuaYiLoanWF', option, function(result){
					var result = result.iqbResult.result;
					if(result.retCode == '1'){
						var url = window.location.pathname;
						var param = window.parent.IQB.main2.fetchCache(url);
						var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
						window.parent.IQB.main2.call(callback);
					}else if(result.retCode == '2'){
						IQB.alert(result.retMsg);	
						$('#btn-save').removeAttr('disabled');
					}
				})
			}else{
				$('#btn-save').removeAttr('disabled');
				return false;
			}
		},
		bizType : function(){
	    	var merchantNo = $(".merch").val();
        	if(merchantNo == '全部商户'){
                $('#bizType').blur();
                IQB.alert('请选择商户');
        	}else{
        		if(_this.cache.flag){
        			_this.cache.flag = 0;
        			var showDataHtml = '';
                    IQB.get(urls['cfm'] + '/merchantBizType/getMerchantBizTypeList', {'merchantNo':merchantNo}, function(result){
                    	if (result.success=="1") {
                    		for(var i=0;i<result.iqbResult.result.length;i++){                      			
        	        			 showDataHtml +="<option value='"+result.iqbResult.result[i].bizType+"'>"+result.iqbResult.result[i].name+"</option>"
        	    			}
                    		$("#bizType").find('option').remove();
        		 			$("#bizType").append(showDataHtml);
        		 		};
    		        });
        		}
        	}
	    },
		//初始化处理人下拉框
		initSelect: function(){
			IQB.getDictListByDictType('bankCardName', 'BANK_NAME');
			IQB.getDictListByDictType('sexOne', 'sexN');
			IQB.getDictListByDictType('sexTwo', 'sexN');
			IQB.getDictListByDictType('sexThree', 'sexN');
			IQB.getDictListByDictType('relationOne', 'SECTORS');
			IQB.getDictListByDictType('relationTwo', 'SECTORS');
			IQB.getDictListByDictType('relationThree', 'SECTORS');
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
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.serEnter.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
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
		init: function() {
			//标签页样式动态处理
			$('.panel').addClass('special-panel');
			$('a[data-toggle="tab"]').on('show.bs.tab', function(e){var target = e.target;var href = $(target).prop('href');if(href.indexOf('#order-tab') != -1){$('.panel').addClass('special-panel');}else{$('.panel').removeClass('special-panel');}});
			
			_this.initSelect();//初始下拉框

			$('#btn-save').on('click', function(){_this.saveInfo()});
			$('#bizType').on('click',function(){_this.bizType()});
            $(".merch").on('click',function(){_this.cache.flag = 1});
            
			$('#btn-uploadTypeOne').on('click', function(){$('#file').attr('fileType', 1);$('#file').click();});
			$('#btn-uploadTypeTwo').on('click', function(){$('#file').attr('fileType', 2);$('#file').click();});
			$('#btn-uploadTypeThree').on('click', function(){$('#file').attr('fileType', 3);$('#file').click();});
			$('#btn-uploadTypeFour').on('click', function(){$('#file').attr('fileType', 4);$('#file').click();});
			$('#btn-uploadType101').on('click', function(){$('#file').attr('fileType', 101);$('#file').click();});
			$('#btn-uploadType102').on('click', function(){$('#file').attr('fileType', 102);$('#file').click();});
			
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});	
		}
	}
	return _this;
}();

$(function() {
	IQB.serEnter.init();
});
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
};
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};


