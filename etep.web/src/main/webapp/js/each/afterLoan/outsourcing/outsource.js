$package('IQB.outsource');
IQB.outsource = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				approveDetail: urls['cfm'] + '/carstatus/getApproveHtml'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				reset: function(){
					_grid.handler.reset();
					$('select[name="gpsStatus"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/afterLoan/selectOutSourcetList',
							 singleCheck: true,
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectOutSourcetList',
	   			singleCheck: true,
			}
		},
		process : function(){
			var records = _grid.util.getCheckedRows();
			if(records.length > 0){
				$('#open-win').modal('show');
				$('#turnLegal').val('');
				$('#process').val('');
				//进度历史
				$("#datagrid2").datagrid2({url: urls['cfm'] + '/afterLoan/processList',paginator: 'paginator2',queryParams : $.extend({}, {orderId: records[0].orderId})	
				});
				$("#btn-sure").unbind("click").click(function(){
					  if($("#updateForm3").form('validate')){
						  //更新外包进度
						  IQB.post(urls['cfm'] + '/afterLoan/saveOutSource', {orderId: records[0].orderId,'operateFlag':$('#turnLegal').val(),'operatorRemark':$('#process').val()}, function(result){
								if(result.success=="1"){
									$('#open-win').modal('hide');
									_this.refresh();
									IQB.alert('进度已更新');
								}
							}) 
					  }; 
			    });
			}else{
				IQB.alert('未选中行');
			}
            $("#btn-close").click(function(){
      	       $('#open-win').modal('hide');
		    });
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/afterLoan/selectOutSourcetList',singleCheck: true,queryParams : $.extend({}, $("#searchForm").serializeObject(), {'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		backhaul : function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows')
			if(rows.length > 0){
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab("backhaul", "拖车入库", '/etep.web/view/afterLoan/outsourcing/carStorage.html?orderId=' + rows[0].orderId+ '&merchantId=' + rows[0].merchantId, true, true, {lastTab: param});
			}else{
				IQB.alert('未选中行');
			}
		},
		collection : function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows')
			if(rows.length > 0){
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab("collection", "车辆回款", '/etep.web/view/afterLoan/outsourcing/carCollection.html?orderId=' + rows[0].orderId+ '&merchantId=' + rows[0].merchantId, true, true, {lastTab: param});
			}else{
				IQB.alert('未选中行');
			}
		},
		approveDetail : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			IQB.get(_this.config.action.approveDetail, {'orderId':orderId,'procKey':'post_loan_processing'}, function(result) {
				var procBizId = result.iqbResult.result.procBizId;
				var procDisplayurl = result.iqbResult.result.procDisplayurl;
				var procInstId = result.iqbResult.result.procInstId;
				_this.skipPage(procBizId,procInstId,procDisplayurl);
			})
		},
		//跳转订单详情页面
		skipPage: function(procBizId, procInstId,procDisplayurl){
			window.parent.IQB.main2.openTab("activiti-seedetail", "流程详情",procDisplayurl+'?procBizId=' + procBizId + '&procInstId=' + procInstId, true, true, null);			
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.initSelect();
			//贷后核实
			$('#btn-process').unbind('click').on('click',function(){
				_this.process();
			});
			//拖回流程
			$('#btn-backhaul').unbind('click').on('click',function(){
				_this.backhaul();
			});
			//回款流程
			$('#btn-collection').unbind('click').on('click',function(){
				_this.collection();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.outsource.init();
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