$package('IQB.vehicleOutbound');
IQB.vehicleOutbound = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/carstatus/cget_info_list',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,channal: 2,status:30})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/carstatus/cget_info_list',
	   			singleCheck: true,
	   			queryParams: {
	   				"channal":"2",
	   				"status":"30"
	   			}
			}
		},
		carSale: function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
			    //启动流程
				var orderId = rows[0].orderId;
                var procOrgCode = rows[0].merchantId;
                var procDefKey = 'car_sale';
                //流程名称
                var realName = rows[0].realName;
                var merchantShortName = rows[0].merchantShortName;
                var orderName = rows[0].orderName;
                var proName = realName +';'+ merchantShortName +';'+ orderName+';'+ '车辆出售';
				IQB.confirm('确定出售该车辆吗？',function(){_this.startPro(orderId,procOrgCode,procDefKey,proName);},function(){});
			}else{
				IQB.alert('未选中行');
			}
		},
		carSublet:function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
			    //启动流程
				var orderId = rows[0].orderId;
                var procOrgCode = rows[0].merchantId;
                var procDefKey = 'car_sublet';
                //流程名称
                var realName = rows[0].realName;
                var merchantShortName = rows[0].merchantShortName;
                var orderName = rows[0].orderName;
                var proName = realName +';'+ merchantShortName +';'+ orderName+';'+ '车辆转租';
				IQB.confirm('确定转租该车辆吗？',function(){_this.startPro(orderId,procOrgCode,procDefKey,proName);},function(){});
			}else{
				IQB.alert('未选中行');
			}
		},
		carReturn:function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
			    //启动流程
                var orderId = rows[0].orderId;
                var procOrgCode = rows[0].merchantId;
                var procDefKey = 'car_return';
                //流程名称
                var realName = rows[0].realName;
                var merchantShortName = rows[0].merchantShortName;
                var orderName = rows[0].orderName;
                var proName = realName +';'+ merchantShortName +';'+ orderName+';'+ '车辆返还';
				IQB.confirm('确定返还该车辆吗？',function(){_this.startPro(orderId,procOrgCode,procDefKey,proName);},function(){});
			}else{
				IQB.alert('未选中行');
			}
		},
		startPro : function(orderId,procOrgCode,procDefKey,proName) {
			var id = orderId;
			var code = procOrgCode;
			var key = procDefKey;
			var name = proName;
			var bizData = {}
			bizData.procBizId=id;
			bizData.procBizMemo=name;
			bizData.procBizType='';
			bizData.procOrgCode=String(code);
			var authData= {}
			authData.procAuthType = "2";
			var procData = {}
			procData.procDefKey = key;
			var variableData = {}
			variableData.procApprStatus = '1';
			variableData.procApprOpinion = '同意';
			variableData.procAssignee = '';
			variableData.procAppointTask = '';
			var option = {};
			option.bizData = bizData;
			option.authData=authData;
			option.procData = procData;
			option.variableData = variableData;
			IQB.post(urls['rootUrl'] + '/WfTask/startAndCommitProcess', option, function(result){
				if(result.success != "1") {
					IQB.alert(result.retUserInfo);
				}else{
					IQB.alert('流程启动成功');
					_this.refresh();
				}
			});
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/carstatus/cget_info_list',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,channal: 2,status:30,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})});
        },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			$('#btn-sale').on('click',function(){_this.carSale()});
			$('#btn-sublet').on('click',function(){_this.carSublet()});
			$('#btn-return').on('click',function(){_this.carReturn()});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.vehicleOutbound.init();
	datepicker(lintoGarageDate);
	datepicker(hintoGarageDate);
});	
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