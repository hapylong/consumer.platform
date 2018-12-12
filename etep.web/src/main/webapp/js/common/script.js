/**
 * 自定义javascript
 */
var error_code = 0; //错误消息代码
var success_code = 1; //成功消息代码


/**
 * 序列化form表单为json
 */
$.fn.serializeObjectToJson = function() {
	var params = new Object();
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	params.data = JSON.stringify(o);
	return params;
};

/**
 * 判断返回成功失败
 */
$.isSucc = function(data){
	if(data.success == 1){
		return true;
	}else{
		return false;
	}
}

/**
 * 获取用户展示的信息
 */
$.getRetUserInfo = function(data){
	return data.retUserInfo;
}

/**
 * 用于datagrid中根据行号获取选中行的数据
 */
$.fn.getSelectedRow = function(index){
	$(this).datagrid("selectRow",index);// 关键在这里
	var row = $(this).datagrid('getSelected');
	return row;
}

//检查是否选中一条纪录，如果有则返回当前纪录数据如果没有，返回false
$.fn.checkRow = function(index){
	var row = $(tt).datagrid('getSelected');
	if(row){
		return row;
	}
	$.messager.alert('提示','请选择一条纪录！','warning');
	return false;
}

/**
 * 初始化富文本框
 */
function initEditor(args){
	if(args instanceof Array){
		var arr = new Array();
		$.each(args,function(i,item){
			var ue = UM.getEditor(item);
			arr.push(ue);
		})
		return arr;
	}
	
	if(typeof(args) == 'string'){
		var ue = UM.getEditor(args);
		return ue;
	}
}

function disableEditor(args){
	if(args instanceof Array){
		var arr = new Array();
		$.each(args,function(i,item){
			UM.getEditor(item).setDisabled('fullscreen');
		})
		return;
	}
	
	if(typeof(args) == 'string'){
		UM.getEditor(args).setDisabled('fullscreen');
		return;
	}
}


function getQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}


/**
 * 将select标签转成json
 * @param selectObj
 * @returns {___anonymous1154_1155}
 */
$.fn.selectToJSON = function(){
	 var data  = {};
	 $(this).find('option').each(function(i,item){
		var k = $(item).val();
		var v = $(item).text();
		data[k] = v;
	 });
	 return data;
}


/**
 * 关闭当前窗口
 */
function CloseWebPage(){
	 if (navigator.userAgent.indexOf("MSIE") > 0) {
	  if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
	   window.opener = null;
	   window.close();
	  } else {
	   window.open('', '_top');
	   window.top.close();
	  }
	 }
	 else if (navigator.userAgent.indexOf("Firefox") > 0) {
	  window.location.href = 'about:blank ';
	 } else {
	  window.opener = null;
	  window.open('', '_self', '');
	  window.close();
	 }
	}
//获取查询条件并放到param中
function putVal(id, obj) {
	var v = $("#" + id).val();
	if ($.trim(v).length > 0) {
		obj[id] = v;
	}
}


(function ($) {
	$.postData = function (options) {
		var url = options.url;
		var data = options.data;
		var before = eval(options.before)
		var success = eval(options.success);
		var after = eval(options.after);
		var error = eval(options.error);
		
		$.ajax({
			url : url,
			data : data,
			dataType:'json',
			type : 'POST',
			beforeSend :function(xhr){
				if(before != undefined){
					before();
				}
			},
			success:function(data){
				if (data == '' || data == undefined) {
					return alert('网络异常！');
				}
				var code = data.code;
				if (code == success_code) {
					if(success != undefined){
						return success(data);
					}else{
						return showOkMsg(data.msg)
					}
					
				} else if (code == error_code) {
					if (error != undefined) {
						return error(data);
					} else {
						return showErrMsg(data.msg);
					}
				}else{
					return alert(data);
				}
			},
			complete :function(data){
				if(after != undefined){
					after();
				}
			}
		});
		
	}
})(jQuery);


function showOkMsg (text , callback) {
	$.messager.alert("提示", text,"info",callback);
}

function showErrMsg (text , callback) {
	$.messager.alert("提示", text,"error",callback);
}


$.fn.getHeader = function() {
	var dt = $(this);
	var fields = $(dt).datagrid('getColumnFields');
	var header = {};
	$.each(fields, function(i,item){
		header[item] = $(dt).datagrid('getColumnOption', item).title;
	});
	return header;
};

function exportExcel(url, filter ,header){
	filter.exp = true;
	filter.header =  JSON.stringify(header);
	$.postData({
		url : url,
		data : filter,
		before:function(){
			$.messager.progress({
				title:'Exporting Data...',
			});
		},
		success:function(data){
			window.open(data.msg)
		},
		after : function(){
			$.messager.progress('close');
		}
	});
}

/**
 * 退出登录
 */
$.user_log_out = function(){
	$.ajax({
		type: "POST",
		url : urls['sysmanegeUrl'] + '/sysLogin/logout',
		success : function(data){
		}
	});
	top.location.href = '/etep.web/index.html';
}
