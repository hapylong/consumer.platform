$package('IQB.recordApplication');
IQB.recordApplication = function(){
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
										 {'orgCode': orgCode,'merchantNo':merchantNo,"flag":"2"})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectCasetList',
	   			singleCheck: true,
	   			queryParams: {
	   				"flag":"2"
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectCasetList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo,"flag":"2"})	
			});
	    },
		register : function(){
			$("#btn-sure").attr('disabled','disabled');
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				$("#btn-sure").removeAttr('disabled');
				$('#updateForm')[0].reset();
				$('.payFlagAbout').show();
				$('.registerAbout').show();
				$('.payFlagAbout').find('.payFlagAboutInput').validatebox({ required: true }); 
				$('.payFlagAbout').find('.payFlagAboutInput').validatebox({ required: true });
				
				$("#btn-sure").unbind("click").click(function(){
					  if($("#updateForm").form('validate')){
						  //保存申请立案信息
						  var option = {
								  'caseId':records[0].caseId,
								  'orderId':records[0].orderId,
								  'payFlag':$('#payFlag').val(),
								  'register':$('#register').val(),
								  'associatedAgency':$('#associatedAgency').val(),
								  'mandatoryLawyer':$('#mandatoryLawyer').val(),
								  'acceptOrg':$('#acceptingInstitution').val(),
								  'legalCost':$('#legalFare').val(),
								  'barFee':$('#counselFee').val(),
								  'registerDate':$('#filingTime').val(),
								  'caseNo':$('#caseNo').val(),
								  'registerRemark':$('#filingRemark').val(),
						  };
						  if($('#payFlag').val() == '2'){
							  option.register = '';
							  option.acceptOrg = '';
							  option.associatedAgency = '';
							  option.mandatoryLawyer = '';
							  option.legalCost = '';
							  option.barFee = '';
							  option.registerDate = '';
							  option.caseNo = '';
							  option.registerRemark = '';
						  }
						  if($('#register').val() == '1'){
							  option.associatedAgency = '';
							  option.mandatoryLawyer = '';
						  }
						  IQB.post(urls['cfm'] + '/afterLoan/applyCase', option, function(result){
							  if(result.success == '1'){
								  IQB.alert('申请成功');
								  $('#open-win').modal('hide');
								  _this.refresh();
							  }else{
								  $("#btn-sure").removeAttr('disabled');
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
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			$('#open-win2').modal('show');
			//弹框赋值                                                                    
			IQB.post(urls['cfm'] + '/afterLoan/getInstOrderLawnInfoByCaseId', {"orderId":row.orderId,'caseId':row.caseId}, function(result){
				  if(result.success == '1'){
					    var result = result.iqbResult.result;
					    $('#cause').val(Formatter.isNull(result.reason));
						$('#accuser').text(Formatter.accuserFlag(result.accuserFlag));
						$('#claims').val(Formatter.isNull(result.claimRequest));
						$('#record').text(Formatter.yOrn(result.registerFlag));
				  }
			})
			$("#btn-close2").click(function(){
      	        $('#open-win2').modal('hide');
      	        $('#open-win2').find('span').html('');
      	        $('#open-win2').find('textarea').val('');
		    });
		},
		validateCaseNo : function(){
			var caseNo = $('#caseNo').val();
			IQB.post(urls['cfm'] + '/afterLoan/validateCaseNo', {'caseNo':caseNo}, function(result){
				if(result.iqbResult.result == '1'){
					//编号重复
					IQB.alert('案件编号已存在');
					$("#btn-sure").attr('disabled','disabled');
				}else{
					$("#btn-sure").removeAttr('disabled');
				}
			})
		},
		initSelect : function(){
			IQB.getDictListByDictType2('status', 'carStatus');
			$('select[name="status"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//申请立案
			$('#btn-register').unbind('click').on('click',function(){
				_this.register();
			});
			//
			$('#payFlag').on('change',function(){
				if($(this).val() == 1){
					$('.payFlagAbout').show();
					$('.payFlagAbout').find('.payFlagAboutInput').validatebox({ required: true }); 
				}else{
					$('.payFlagAbout').find('.payFlagAboutInput').validatebox({ required: false }); 
					$('.payFlagAbout').hide();
				}
			});
			//
            $('#register').on('change',function(){
            	if($(this).val() == 1){
            		$('.registerAbout').hide();
				}else{
					$('.registerAbout').show();
				}
			});
            //校验案件编号是否重复
            $('#caseNo').on('blur',function(){
            	_this.validateCaseNo();
            });
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.recordApplication.init();
	datepicker(filingTime );
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