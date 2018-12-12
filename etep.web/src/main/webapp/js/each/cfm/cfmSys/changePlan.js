$package('IQB.changePlan');
IQB.changePlan = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/business/exportOrderList',
				plan: urls['cfm'] + '/admin/change_plan'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.update();
					$('#update-win-label').text('修改订单');
					$('#update-win #merchantNo').attr("disabled",true);
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getOrderList',
	   			queryParams: {
	   				type: 1
	   			}
			}
		},
		updateSignatureStamp : function() {	
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			$("#ec_signer_name_update").val(rows[0].ecSignerName);
			$('#msg-create-label').text('上传签名图章');
			$('#msg-create').modal({backdrop: 'static', keyboard: false, show: true});
			
			$("#btn-msg-create-label").click(function(){
				if(_isUpload != "TRUE") {
					_this.messageFail("请先上传图片");
					return;
				}
				var data = {
						"ec_signer_img_type_update" : $("#ec_signer_img_type_update").val(),
						"is_default_signer_img_update" : $("#is_default_signer_img_update").val(),
						"ec_signer_img_name_update" : $("#ec_signer_img_name_update").val(),
						"ec_signer_pic_name_update" : $("#ec_signer_pic_name_update").val(),
						"sign_type" : 91,
						"ecSignerCertNo" : rows[0].ecSignerCertNo
				}
				IQB.post(urls['rootUrl'] + '/ec_participant/update_group_by_type', data, function(result,status){
					if(result.success == 1) {
						_this.signatureUpdateLabelClose();
					} else {
						_this.messageFail(result.retUserInfo);
					}
				})
			});
		},
		resetPlan : function(data){
			var rowIndex = data;
			$("#beginDate").val("");
			$("#showOrNot").show();
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			$('#msg-create').modal({backdrop: 'static', keyboard: false, show: true});
			$("#btn-msg-create-label").unbind("click").click(function(){
				var data = {
						"target" : "RESET",
						"orderId" : row.orderId,
						"describe" : $("#msg").val(),
						"beginDate" : $("#beginDate").val()
				}
				IQB.post(urls['rootUrl'] + '/admin/change_plan', data, function(result,status){
					if(result.success == 1) {
						alert(result.iqbResult.result);
						_this.msgCreateClose();
					} else {
						_this.messageFail(result.retUserInfo);
					}
				})
			});
		},
		stopPlan : function(data){
			$("#beginDate").val("");
			$("#showOrNot").hide();
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			$('#msg-create').modal({backdrop: 'static', keyboard: false, show: true});
			$("#btn-msg-create-label").unbind("click").click(function(){
				var data = {
						"target" : "STOP",
						"orderId" : row.orderId,
						"describe" : $("#msg").val()
				}
				IQB.post(urls['rootUrl'] + '/admin/change_plan', data, function(result,status){
					if(result.success == 1) {
						alert(result.iqbResult.result);
						_this.msgCreateClose();
					} else {
						_this.messageFail(result.retUserInfo);
					}
				})
			});
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/business/getOrderList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		msgCreateClose : function() {
			$('#msg-create').modal('hide');
			$("#msg").val("");
			_this.refresh();
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var userName = $("#userName").val();
				/*var riskStatus = $("#riskStatus").val();*/
				var wfStatus = $("#wfStatus").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var datas = "?merchNames=" + merchName + "&regId=" + regId + "&userName=" + userName + "&wfStatus=" + wfStatus + "&startTime=" + startTime + "&endTime=" + endTime;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.exports();//导出
			$("#btn-msg-create-label-close").click(function() {
				_this.msgCreateClose();
			})
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.changePlan.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker2(beginDate);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:true,
	    format:'Y-m-d',
		allowBlank: true
	});
};
function datepicker2(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
		allowBlank: true
	});
};