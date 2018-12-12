$package('IQB.postLoanManagement');
IQB.postLoanManagement = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        skipNewWindow : function() {
            var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows.length > 0){			
				IQB.confirm('确定拖回入库该车辆吗？',function(){_this.startPro();},function(){});
			}else{
				IQB.alert('未选中行');
			}
		},
        skipNewWindow2 : function() {
            var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows.length > 0){			
				IQB.confirm('确定失联该车辆吗？',function(){_this.startPro2();},function(){});
			}else{
				IQB.alert('未选中行');
			}
		},
        startPro : function() {
			var bizData = {}
        	var rows = $("#datagrid").datagrid2('getCheckedRows')
			bizData.procBizId=rows[0].orderId;//orderID
			bizData.procBizMemo=rows[0].realName +';'+ rows[0].merchantShortName +';'+ rows[0].orderName+';'+ '拖车入库';
			bizData.procBizType='';
			bizData.procOrgCode=rows[0].merchantId;
			var authData= {}
			authData.procAuthType = "2";
			var procData = {}
			procData.procDefKey = 'car_storage';
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
		startPro2 : function() {
			var bizData = {}
			var rows = $("#datagrid").datagrid2('getCheckedRows')
			bizData.procBizId=rows[0].orderId;//orderID
			bizData.procBizMemo=rows[0].realName +';'+ rows[0].merchantShortName +';'+ rows[0].orderName+';'+ '车辆失联';
			bizData.procBizType='';
			bizData.procOrgCode=rows[0].merchantId;
			var authData= {}
			authData.procAuthType = "2";
			var procData = {}
			procData.procDefKey = 'car_out_of_contact';
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
			console.log('car--ssale');
			$("#datagrid").datagrid2({url: urls['cfm'] + '/carstatus/cget_info_list',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,channal: 2,status:10,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})});
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
										 {'orgCode': orgCode,'merchantNo':merchantNo,channal: 2,status:10})	
							});
				}
            },
            dataGrid: {//表格参数               
            	url: urls['cfm'] + '/carstatus/cget_info_list',
                singleCheck: true,
                queryParams: {
                	channal: 2,
                	status:10,
       			}
            }   
        },
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            $("#btn-skip").click(function(){_this.skipNewWindow()});
            $("#btn-skip2").click(function(){_this.skipNewWindow2()});
        },
    }
    return _this;
}();
$(function(){
    //页面初始化
    datepicker(search_expireDate);
    IQB.postLoanManagement.init();
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
