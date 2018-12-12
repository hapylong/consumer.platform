$package('IQB.caseAcceptance');
IQB.caseAcceptance = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="status"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectCasetList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo,"flag":"1"})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectCasetList',
	   			singleCheck: true,
	   			queryParams : {
	   				"flag":"1"
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectCasetList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,"flag":"1"})	
			});
	    },
	    blur : function(){
			if($("#orderIdAdd").val() != ''){
				var orderIdAdd = $('#orderIdAdd').val();
				IQB.get(urls['cfm'] + '/instRemindPhone/queryOrderInfoByOrderId', {'orderId':orderIdAdd}, function(result) {
					if(result.iqbResult.code=="0"){
						var result=result.iqbResult.result
						$("#realNameAdd").val(result.realName);
						$("#regIdAdd").val(result.regId);
					}
					else if(result.iqbResult.code=="1"){
						IQB.alert('输入的订单号不是已分期或已放款订单');
						_this.refresh();
						$('#updateForm2')[0].reset()
						$('#open-win2').modal('hide');
					}else if(result.iqbResult.code=="2"){
						IQB.alert('输入的订单号不是以租代购订单');
						_this.refresh();
						$('#updateForm2')[0].reset()
						$('#open-win2').modal('hide');
					}
				})
			}
		},
		add : function(){
			$('#open-win2').modal('show');
			$("#btn-sure2").removeAttr('disabled');
			$("#btn-sure2").unbind("click").click(function(){
				  if($("#updateForm2").form('validate')){
					  $("#btn-sure2").attr('disabled','disabled');
					  //增加
					  var option = {
							  'orderId':$('#orderIdAdd').val(),
							  'reason':$('#cause1').val(),
							  'accuserFlag':$('#accuser1').val(),
							  'claimRequest':$('#claims1').val(),
							  'registerFlag':$('#record1').val()
					  };
					  IQB.post(urls['cfm'] + '/afterLoan/newCase', option, function(result){
							if(result.success == "1"){
								$('#open-win2').modal('hide');
								IQB.alert('增加成功');
								_this.refresh();
								$('#updateForm2')[0].reset()
							}else{
								IQB.alert('该订单已经进入委案受理无需重复申请');
								_this.refresh();
								$('#updateForm2')[0].reset();
								$("#btn-sure2").removeAttr('disabled');
								$('#open-win2').modal('hide');
							}
						}) 
				  }; 
		    });
			$("#btn-close2").click(function(){
      	      $('#open-win2').modal('hide');
      	      $('#updateForm2')[0].reset()
		    });
		},
		register : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				$('#updateForm')[0].reset();
				//回显已有的信息（回显可修改）                                                               
				IQB.post(urls['cfm'] + '/afterLoan/getInstOrderLawnInfoByCaseId', {"orderId":records[0].orderId,'caseId':records[0].caseId}, function(result){
					  if(result.success == '1'){
						    var result = result.iqbResult.result;
						    if(result != '' && result != null){
						    	$('#cause').val(result.reason);
								$('#accuser').val(result.accuserFlag);
								$('#claims').val(result.claimRequest);
								$('#record').val(result.registerFlag);
						    }
					  }
				})
				$("#btn-sure").unbind("click").click(function(){
					  if($("#updateForm").form('validate')){
						  //保存登记信息
						  var option = {
								  'caseId':records[0].caseId,
								  'reason':$('#cause').val(),
								  'accuserFlag':$('#accuser').val(),
								  'claimRequest':$('#claims').val(),
								  'registerFlag':$('#record').val()
						  };
						  IQB.post(urls['cfm'] + '/afterLoan/registerCase', option, function(result){
							  if(result.success == '1'){
								  IQB.alert('登记成功');
								  $('#open-win').modal('hide');
								  _this.refresh();
							  }
						  })
					  }
				})
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
	      	      $('#open-win').modal('hide');
	      	      $('#updateForm')[0].reset()
			});
		},
		approveDetail : function(data){
			var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			window.parent.IQB.main2.openTab("case-detail", "委案受理", '/etep.web/view/afterLoan/caseAcceptance/caseAcceptanceDetail.html?orderId=' + orderId, true, true, {lastTab: param});
		},
		initSelect : function(){
			IQB.getDictListByDictType2('status', 'carStatus');
			$('select[name="status"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//增加
			$('#btn-add').unbind('click').on('click',function(){
				_this.add();
			});
			//登记注册
			$('#btn-register').unbind('click').on('click',function(){
				_this.register();
			});
			$('#orderIdAdd').on('blur',function(){
				_this.blur();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.caseAcceptance.init();
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