/**
 * @author 
 * @Version
 * @DateTime
 */
var IQB = {
	/*信息提示框*/
	alert: function(msg){
		$('#alert-msg').html(msg);
		$('#alert-win').modal({backdrop: 'static', keyboard: false, show: true});
		$('#alert-win').find('#btn-alertWin-confirm').off('click').on('click', function(){$('#alert-win').modal('hide');});
	},
	/*信息确认框*/
	confirm: function(msg, confirmCallback, cancelCallback){
		$('#confirm-msg').html(msg);
		$('#confirm-win').modal({backdrop: 'static', keyboard: false, show: true});
		$('#confirm-win').find('#btn-confirmWin-confirm').off('click').on('click', function(){$('#confirm-win').modal('hide');if($.isFunction(confirmCallback)){confirmCallback();}});
		$('#confirm-win').find('#btn-confirmWin-cancel').off('click').on('click', function(){$('#confirm-win').modal('hide');if($.isFunction(cancelCallback)){cancelCallback();}});
	},
	/**
	 * 判断返回成功失败
	 */
	isSucc: function(data){
		if(data.success == 1){
			return true;
		}
		return false;
	},
	/**
	 * 获取用户展示的信息
	 */
	getRetUserInfo: function(data){
		return data.retUserInfo;
	},
	/**
	 * 下拉框字典表渲染
	 */
	getDictListByDictType: function(selectId, dictType){
		var req_data = {'dictTypeCode': dictType};
		IQB.post(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
			$('#' + selectId).prepend("<option value=''>请选择</option>");
			$('#' + selectId).select2({theme: 'bootstrap', data: result.iqbResult.result});
			return result.iqbResult.result;
		})
	},
	/**
	 * 下拉框字典表渲染
	 */
	formatterDictType: function(val, dictType){
		var req_data = {'dictTypeCode': dictType};
		IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
			var retArr = result.iqbResult.result;
			$.each(retArr, function(key, retVal) {
				if(val == retVal.id){
					return retVal.text;
				}
			});
		})
		return '';
	},
	/*重新登录*/
	toLogin: function(){
		//跳转首页
		window.top.location = urls['webUrl'] + '/index.html';
	},
	/*页面回退*/
	goBack: function(){
		window.history.go(-1);
	},
	/*登录检查*/
	checkLogin: function(data){
		//未登录或者登录超时处理
		if(data == 'unlogin'){
			return false;
		}
		return true;
	},
	/*权限检查*/
	checkAuth: function(data){
		if(IQB.isSucc(data)){
			return true;
		}
		if(data.retCode != '01090004'){
			return true;
		}
		IQB.alert(IQB.getRetUserInfo(data));
		return false;
	},
	/*业务和系统异常检查*/
	checkSuccess: function(data){
		//业务和系统异常处理
		if(data.success == 2){
			IQB.alert(data.retUserInfo);
			return false;
		}
		return true;
	},
	/*AJAX*/
	ajax: function(url, type, async, option, callback){
		$.ajax({
			url: url,
			type: type,
			async: async,
			cache: false,
			data: option,
			dataType: 'json',
			contentType: 'application/json',
			beforeSend: function(){//发送前
				
			},
			complete: function(){//发送后
				
			},
		 	success: function(data){//请求成功
		 		if(!IQB.checkLogin(data)){
		 			IQB.toLogin();
		 			return false;
		 		}	
		 		if(!IQB.checkAuth(data)){
		 			IQB.goBack();
	 				return false;
	 			}
		 		if(!IQB.checkSuccess(data)){
		 			return false;
		 		}	
		 		if($.isFunction(callback)){
		 			callback(data);
		 		}
		 	},
		 	error: function(response, textStatus, errorThrown){//请求失败
		 		try{		 			
		 			if(!IQB.checkLogin(response.responseText)){
			 			IQB.toLogin();
			 			return false;
			 		}	
		 			if(!IQB.checkAuth(response.responseText)){
		 				IQB.goBack();
		 				return false;
		 			}	
		 			if(response.readyState == 0){
		 				IQB.alert('网络连接异常');
		 				return false;
		 			}	
		 			if(response.readyState == 4 && response.status == 404){
		 				IQB.alert('网络连接异常');
		 			}else{
		 				IQB.alert('系统异常，请联系管理员');
		 			}
		 		}catch(e){
		 			IQB.alert('系统异常，请联系管理员');
		 		}
		 	}
		});
	},
	/*AJAX提交JSON(RESTful风格)*/
	ajaxJson: function(url, type, async, option, callback){
		$.ajax({
			url: url,
			type: type,
			async: async,
			cache: false,
			data: JSON.stringify(option),
			dataType: 'json',
			contentType: 'application/json',
			beforeSend: function(){//发送前
				
			},
			complete: function(){//发送后
				
			},
		 	success: function(data){//请求成功
		 		if(!IQB.checkLogin(data)){
		 			IQB.toLogin();
		 			return false;
		 		}	
		 		if(!IQB.checkAuth(data)){
		 			IQB.goBack();
	 				return false;
	 			}
		 		if(!IQB.checkSuccess(data)){
		 			return false;
		 		}	
		 		if($.isFunction(callback)){
		 			callback(data);
		 		}
		 	},
		 	error: function(response, textStatus, errorThrown){//请求失败
		 		try{		 			
		 			if(!IQB.checkLogin(response.responseText)){
			 			IQB.toLogin();
			 			return false;
			 		}	
		 			if(!IQB.checkAuth(response.responseText)){
		 				IQB.goBack();
		 				return false;
		 			}	
		 			if(response.readyState == 0){
		 				IQB.alert('网络连接异常');
		 				return false;
		 			}	
		 			if(response.readyState == 4 && response.status == 404){
		 				IQB.alert('网络连接异常');
		 			}else{
		 				IQB.alert('系统异常，请联系管理员');
		 			}
		 		}catch(e){
		 			IQB.alert('系统异常，请联系管理员');
		 		}
		 	}
		});
	},
	/*AJAX提交表单(图片等文件)*/
	ajaxForm: function(form, url, callback){
		form.ajaxSubmit({
			url: url,
			type: 'post',
			async: true,
			cache: false,
			dataType: 'json',
			contentType: 'multipart/form-data',
			beforeSend: function(){//发送前
				
			},
			complete: function(){//发送后
				
			},
		 	success: function(data){//请求成功
		 		if(!IQB.checkLogin(data)){
		 			IQB.toLogin();
		 			return false;
		 		}	
		 		if(!IQB.checkAuth(data)){
		 			IQB.goBack();
	 				return false;
	 			}
		 		if(!IQB.checkSuccess(data)){
		 			return false;
		 		}	
		 		if($.isFunction(callback)){
		 			callback(data);
		 		}
		 	},
		 	error: function(response, textStatus, errorThrown){//请求失败
		 		try{		 			
		 			if(!IQB.checkLogin(response.responseText)){
			 			IQB.toLogin();
			 			return false;
			 		}	
		 			if(!IQB.checkAuth(response.responseText)){
		 				IQB.goBack();
		 				return false;
		 			}	
		 			if(response.readyState == 0){
		 				IQB.alert('网络连接异常');
		 				return false;
		 			}	
		 			if(response.readyState == 4 && response.status == 404){
		 				IQB.alert('网络连接异常');
		 			}else{
		 				IQB.alert('系统异常，请联系管理员');
		 			}
		 		}catch(e){
		 			IQB.alert('系统异常，请联系管理员');
		 		}
		 	}
		});
	},
	/*通用get异步请求*/
	get: function(url, option, callback){
		//TODO(项目上线前需要改成 get方式提交数据)
		IQB.ajaxJson(url, 'post', true, option, function(data){
			if($.isFunction(callback)){
	 			callback(data);
	 		}		
		});
	},
	/*通用post异步请求*/
	post: function(url, option, callback){
		IQB.ajaxJson(url, 'post', true, option, function(data){
			if($.isFunction(callback)){
	 			callback(data);
	 		}		
		});
	},
	/*通用get同步请求*/
	getAsync: function(url, option, callback){
		IQB.ajaxJson(url, 'get', false, option, function(data){
			if($.isFunction(callback)){
				callback(data);
			}		
		});
	},
	/*通用post同步请求*/
	postAsync: function(url, option, callback){
		IQB.ajaxJson(url, 'post', false, option, function(data){
			if($.isFunction(callback)){
				callback(data);
			}		
		});
	},
	/*通用表单(图片等文件)请求*/
	postForm: function(form, callback){
		var url = form.prop('action');	
		IQB.ajaxForm(form, url, function(data) {
			if($.isFunction(callback)){
				callback(data);
			}	
		});
	},
	/*通用表单(图片等文件)请求*/
	postForm2: function(form, callback){
		$.each(form.find('input[type = "text"]'), function(i, n){
			var value = $.trim($(n).val());
			$(n).val(value);
		});
		if(form.form('validate')){
			var url = form.prop('action');	
			IQB.ajaxForm(form, url, function(data) {
				if($.isFunction(callback)){
					callback(data);
				}	
			});
		}		
	},
	/*保存*/
	save: function(form, callback){	
		$.each(form.find('input[type = "text"]'), function(i, n){
			var value = $.trim($(n).val());
			$(n).val(value);
		});
		var url = form.prop('action');	
		var option = form.serializeObject();
		if(form.form('validate')){
			IQB.post(url, option, function(data){         
				if($.isFunction(callback)){
		 			callback(data);
		 		}		
			});
		}		
	},
	/*根据ID查询*/
	getById: function(url, option, callback){
		IQB.post(url, option, function(data){        
			if($.isFunction(callback)){
	 			callback(data);
	 		}		
			
		});
	},
	/*删除*/
	remove: function(url, option, callback){
		IQB.post(url, option, function(data){        
			if($.isFunction(callback)){
	 			callback(data);
	 		}	
		});
	}
}  

/*页面公共JS*/
$(function(){
	$.initAlertWin();
	$.initConfirmWin();
});

/*扩展EasyUi表单校验规则*/
if($.fn.validatebox){	
	$.fn.validatebox.defaults.missingMessage = '该项为必输项';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的邮箱地址';
	$.fn.validatebox.defaults.rules.length.message = '输入字符长度必须介于{0}和{1}之间';
	$.extend($.fn.validatebox.defaults.rules, {	
		url: {
			validator:function(value, param){
	    		var pattern = /^[a-zA-Z0-9//._]*$/;
	    		return pattern.test(value);
	    	},
	    	message: '请输入有效的URL'
		},
	    minLength: {
			validator: function(value, param){
				return value.length >= param[0];
			},
			message: '最少需要输入 {0} 个字符'
	    },
	    maxLength: {
			validator: function(value, param){
				return value.length <= param[0];
			},
			message: '最多可以输入 {0} 个字符'
	    },
	    equals: {
			validator: function(value, param){
				return value == $('#userPassword').val();
			},
			message: '密码不一致'
	    },
	    newequals: {
			validator: function(value, param){
				return value == $('#newPwd').val();
			},
			message: '密码不一致'
	    },
	    zh: {
	    	validator: function(value, param){
	    		var pattern = /^[\u0391-\uFFE5]*$/g;
	    	    return pattern.test(value);
			},
			message: '请输入中文字符'
	    },
	    num: {
			validator: function(value, param){
				return !isNaN(value);
			},
			message: '请输入数值'
	    },
	    int: {
	    	validator: function(value, param){
	    		var pattern = /^[1-9][0-9]*$/ ;//整数
	            return pattern.test(value);
			},
			message: '请输入有效的非零整数'
	    },
	    intz: {
	    	validator: function(value, param){
	    		var pattern = /^[0-9]{1,2}$/ ;//整数
	            return pattern.test(value);
			},
			message: '请输入有效的整数'
	    },
	    moneyZero: {
	    	validator: function(value, param){
	    		if(parseInt(value));
	    		var pattern = /^([1-9][\d]{0,7})(\.[\d]{1,2})?$/ ;//金额
	            return pattern.test(value) || parseInt(value) == 0;
			},
			message: '请输入有效的非零金额'
	    },
	    money: {
	    	validator: function(value, param){
	    		var pattern = /^([1-9][\d]{0,7})(\.[\d]{1,2})?$/ ;//金额
	            return pattern.test(value);
			},
			message: '请输入有效的金额'
	    },	    
	    moneyTwo: {
	    	validator: function(value, param){
	    		var pattern = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/ ;//金额(2位小数,用作比例限制位数)
	            return pattern.test(value);
			},
			message: '请输入有效的比例'
	    },
	    moneyThree: {
	    	validator: function(value, param){
	    		var pattern = /^([1-9][\d]{0,7}|0)(\.[\d]{1,3})?$/ ;//金额(3位小数,用作比例限制位数)
	            return pattern.test(value);
			},
			message: '请输入有效的比例'
	    },
	    moneyFour: {
	    	validator: function(value, param){
	    		var pattern = /^([0-9][\d]{0,7})(\.[\d]{1,2})?$/ ;//金额
	            return pattern.test(value);
			},
			message: '请输入有效的金额范围'
	    },
	    maxNum: {
			validator: function(value, param){
				return value <= param[0];
			},
			message: '最大值为 {0}'
	    },
	    minNum: {
			validator: function(value, param){
				return value >= param[0];
			},
			message: '最小值为 {0}'
	    },
	    name: {
	    	validator: function(value, param){
	    		var pattern = /^([A-Za-z]|[\u4E00-\u9FA5]||[0-9]|[·])+$/ ;//姓名
	            return pattern.test(value);
			},
			message: '请输入真实姓名'
	    },
	    safeName: {
	    	validator: function(value, param){
	    		var pattern = /^([A-Za-z]|[\u4E00-\u9FA5]|[0-9]|[·]|[*])+$/ ;//姓名带*
	            return pattern.test(value);
			},
			message: '请输入真实姓名'
	    },
	    idCard: {
	    	validator: function(value, param){
	    		var pattern = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/ ;//身份证号
	            return pattern.test(value);
			},
			message: '请输入真实的身份证号'
	    },
	    safeIdCard: {
	    	validator: function(value, param){
	    		var pattern = /(^\d{18}$)|(^\d{17}(\d|X|x)$)|(^\d{4}(\*){10}\d{4}$)|(^\d{4}(\*){10}\d{3}(\d|X|x)$)/ ;//身份证号中间10位带*
	            return pattern.test(value);
			},
			message: '请输入真实的身份证号'
	    },
	    bankCard: {
	    	validator: function(value, param){
	    		var pattern = /^(\d{16}|\d{17}|\d{18}|\d{19})$/ ;//银行卡号
	            return pattern.test(value);
			},
			message: '请输入真实的银行卡号'
	    },
	    safeBankCard: {
	    	validator: function(value, param){
	    		var pattern = /^(\d{16}|\d{17}|\d{18}|\d{19}|\d{4}(\*){8}\d{4}|\d{4}(\*){9}\d{4}|\d{4}(\*){10}\d{4}|\d{4}(\*){11}\d{4})$/ ;//银行卡号中间带*
	            return pattern.test(value);
			},
			message: '请输入真实的银行卡号'
	    },
	    phone: {
	    	validator: function(value, param){
	    		var telPattern =  /^\d{3,4}-\d{7,8}(-\d{3,4})?$/;//固定电话,支持分机号
	    		var mobPattern =  /^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])\d{8}?$/;//手机号
	            return telPattern.test(value) || mobPattern.test(value);
			},
			message: '请输入有效的电话号码'
	    },
	    mobilePhone: {
	    	validator: function(value, param){
	    		var pattern = /^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])\d{8}?$/ ;//手机号
	            return pattern.test(value);
			},
			message: '请输入真实的手机号'
	    },
	    safeMobilePhone: {
	    	validator: function(value, param){
	    		var pattern = /^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])\d{8}|(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])(\*){4}\d{4}?$/ ;//手机号中间4位*
	            return pattern.test(value);
			},
			message: '请输入真实的手机号'
	    },
	    address: {
	    	validator: function(value, param){
	    		var pattern = /^([A-Za-z]|[\u4E00-\u9FA5]|[0-9]|[#])+$/ ;//地址
	            return pattern.test(value);
			},
			message: '请输入真实的地址'
	    },
	    post: {
	    	validator: function(value, param){
	    		var pattern = /^[0-8]\d{5}?$/;
	            return pattern.test(value);
			},
			message: '请输入有效的邮政编码'
	    },
	    code:{//编码类字段
	    	validator:function(value, param){
	    		var pattern = /^[a-zA-Z0-9_]*$/;
	    		return pattern.test(value);
	    	},
	    	message: '请输入字母、数字、下划线'
	    },
	
	   codeField:{
	    	validator:function(value, param){
	    		var pattern = /^[a-zA-Z0-9]*$/;
	    		return pattern.test(value);
	    	},
	    	message: '请输入字母、数字'    
	    },
	 
	    rulePassword:{
	    	validator:function testPass(str){
	    	    var rC = {
	    	            lW:'[a-z]',
	    	            uW:'[A-Z]',
	    	            nW:'[0-9]',
	    	           
	    	        };
	    	        function Reg(param, value){
	    	            var reg = new RegExp(value);
	    	            return reg.test(str); 
	    	          
	    	        }
	    	        var tR = {
	    	                l:Reg(str, rC.lW),
	    	                u:Reg(str, rC.uW),
	    	                n:Reg(str, rC.nW),
	    	               
	    	            };
	    	            if((tR.u && tR.n)||(tR.l && tR.n)||(tR.l && tR.n && tR.u) ){
	    	            	
	    	                return true;
	    	            }else{
	    	            	
	    	                return false;
	    	            }
	    	       
	    	    },
	         message: '请输入数字或者字母组合'
	    	
	    },
	   
	
	   
	    codename:{
	    	validator:function (value,param){
	        var pattern = /^[\u4E00-\u9FA5\w\d]+$/;
	    	  return pattern.test(value);
	    	},
	    	   message: '请输入字母、数字、下划线、中文字符'   
	        },
	   

	    //请继续扩展
	    codepassword:{
	    	validator:function testPass(str){
	    	    var rC = {
	    	            lW:'[a-z]',
	    	            uW:'[A-Z]',
	    	            nW:'[0-9]',
	    	            sW:'[\\u0020-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007E]'
	    	        };
	    	        function Reg(param, value){
	    	            var reg = new RegExp(value);
	    	            return reg.test(str); 
	    	          
	    	        }
	    	        var tR = {
	    	                l:Reg(str, rC.lW),
	    	                u:Reg(str, rC.uW),
	    	                n:Reg(str, rC.nW),
	    	                s:Reg(str, rC.sW)
	    	            };
	    	            if((tR.l && tR.u && tR.n) || (tR.l && tR.u && tR.s) || (tR.s && tR.u && tR.n) || (tR.s && tR.l && tR.n)){
	    	            	
	    	                return true;
	    	            }else{
	    	            	
	    	                return false;
	    	            }
	    	       
	    	    },
	         message: '您的密码必须含有“小写字母”、“大写字母”、“数字”、“特殊符号”中的至少三种'
	    	
	    },
	   
	    	
	    
	});
}

/*扩展Bootstrap模态框*/
if($.fn.modal){
	$.fn.modal.Constructor.prototype.enforceFocus = function(){};  
	$.extend({      
		  initAlertWin: function(){
			  var html = '<div id="confirm-win" class="modal fade" role="dialog" aria-labelledby="confirm-win-label">' + 
								'<div class="modal-dialog modal-sm" role="document">' +
									'<div class="modal-content">' +
								  		'<div class="modal-header"><h7 class="modal-title" id="confirm-win-label">信息确认</h7></div>' +
								  		'<div id="confirm-msg" style="padding:12px;"></div>' +
								  		'<div class="modal-footer">' +
									        '<button id="btn-confirmWin-confirm" type="button" class="btn btn-danger btn-sm">确定</button>' +
									        '<button id="btn-confirmWin-cancel" type="button" class="btn btn-default btn-sm">取消</button>'	+	        
								  		'</div>' +
									'</div>' +
								'</div>' +
						 '</div>';
			  $('body').append(html);
		  },
		  initConfirmWin: function(){
			  var html = '<div id="alert-win" class="modal fade" role="dialog" aria-labelledby="alert-win-label">' +
								'<div class="modal-dialog modal-sm" role="document">' +
									'<div class="modal-content">' +
								  		'<div class="modal-header"><h7 class="modal-title" id="alert-win-label">信息提示</h7></div>'	+
								  		'<div id="alert-msg" style="padding:12px;"></div>' +
								  		'<div class="modal-footer">' +
									        '<button id="btn-alertWin-confirm" type="button" class="btn btn-primary btn-sm">确定</button>' +	        
								  		'</div>' +
									'</div>' +
								'</div>' +
						 '</div>';
			  $('body').append(html);
		  }
	});
}

/*将表单数据转换为JSON对象*/
$.fn.extend({
	serializeObject: function(){
		var o = {};
	    var a = this.serializeArray();
	    $.each(a, function(){
	        if(o[this.name]){
	            if(!o[this.name].push){
	                o[this.name] = [ o[this.name] ];
	            }
	            o[this.name].push(this.value || '');
	        }else{
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	}

}); 