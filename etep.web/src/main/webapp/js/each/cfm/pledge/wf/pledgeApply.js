$package('IQB.pledgeApply');
IQB.pledgeApply = function(){
	var _grid = null;
	var _tree = null;
    var flag= 1;
	var _this = {
		cache :{
			page:0,
			id:'',
			merchantNo:'',
			procOrgCode:'',
			carBrand:'',
			carDetail:'',
			plate:''
		},
		config: {
			action: {//页面请求参数
				insert : urls['rootUrl'] + '/pledge/persist_car_info'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/pledge/cget_car_info',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['rootUrl'] + '/pledge/cget_car_info',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   			}
			} 
		},
		addPlan : function(){
			  $("#btn-insert").click(function(){
	              $('#update-win').addClass("z-index");
				  $('#update-win').show();
				  $('#btn-save').removeAttr('disabled');
	              $(".modal-backdrop").show();
				  $(".modal-backdrop").addClass("z-index2");
				  $("#btn-save").unbind("click").click(function(){
					  if($('#updateForm').form('validate')){
						    $('#btn-save').attr('disabled',true);
						    _this.cache.id = guid();
						    _this.cache.merchantNo = $('#merchantNo').val();
						    _this.cache.carBrand = $('#carBrand').val();
						    _this.cache.carDetail = $('#carDetail').val();
						    _this.cache.plate = $('#plate').val();
					        var data = {
	                    		'id':_this.cache.id,
	                    		'merchantNo':$('#merchantNo').val(),
	                    		'carBrand':$('#carBrand').val(),
	                    		'carDetail':$('#carDetail').val(),
	                    		'carType':$('#carType').val(),
	                    		'plate':$('#plate').val(),
	                    		'carNo':$('#carNo').val(),
	                    		'engine':$('#engine').val(),
	                    		'registDate':$('#registDate').val(),
	                    		'registAdd':$('#registAdd').val(),
	                    		'mileage':$('#mileage').val(),
	                    		'remark':$('#remark').val()
	                    	};
	                    	IQB.post(_this.config.action.insert, data, function(result) {
                                if(result.success == 1){
                                	if (result.iqbResult.result=="success") {
					            		_this.queryProcOrgCode();
							    	}else{
							    		$('#btn-save').removeAttr('disabled');
							    	}
                                }else{
                                	$('#btn-save').removeAttr('disabled');
                                }
			            	}) 
					  }
				  });
				  $("#btn-close").click(function(){
					   $("#menuContentModel").hide();
					   $("#update-win").hide();
				  });
			});
		},
		queryProcOrgCode : function(){
			IQB.post(urls['rootUrl'] + '/merchant/getMerByMerNo', {'merchantName':_this.cache.merchantNo}, function(result) {
            	if (result.iqbResult.result != "fail") {
            		_this.cache.procOrgCode = result.iqbResult.result;
            		_this.startPro();
		    	}else{
		    		IQB.alert('商户不存在！');
		    	}
        	}) 
		},
		startPro : function() {
			var bizData = {}
			bizData.amount='';
			bizData.procBizId=_this.cache.id;
			bizData.procBizMemo=_this.cache.merchantNo + ';' + _this.cache.carBrand + ';' + _this.cache.carDetail + ';' + _this.cache.plate;
			bizData.procBizType='';
			bizData.procOrgCode=String(_this.cache.procOrgCode);
			var authData= {}
			authData.procAuthType = "2";
			var procData = {}
			procData.procDefKey = 'pledge_inquiry_order';
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
					IQB.alert('增加成功');
		        	$("#update-win").hide();
		        	$(".modal-backdrop").hide();
		        	$("#menuContentModel").hide();
					_this.refresh();
				}
			});
		},
		carType : function(){
			var carType = $('#carType').val();
			if(carType == 2){
				$('.plateVINs').hide();
				$('.registAddVINs').hide();
				$('#plate').validatebox({required:false});
				$('#registAdd').validatebox({required:false});
			}else{
				$('.plateVINs').show();
				$('.registAddVINs').show();
				$('#plate').validatebox({required:true});
				$('#registAdd').validatebox({required:true});
			}
		},
        refresh : function(page){
        	console.log('refresh');
			$("#datagrid").datagrid2({url: urls['cfm'] + '/pledge/cget_car_info',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.addPlan();
			$('#carType').on('change',function(){_this.carType();});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.pledgeApply.init();
	datepicker(registDate);
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
//生成UUID
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
};