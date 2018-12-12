$package('IQB.loanManagement');
IQB.loanManagement = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		config: {
			action: {//页面请求参数
				exports: urls['cfm'] + '/business/exportLoanOrderList',
				stage: urls['cfm'] + '/business/updateLoanDateByOrderIds'
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
							 url: urls['cfm'] + '/business/getLoanOrderList',
							 loadSuccess:function(date){
					   				//支付金额赋值
								    $("#allAmt").val(Formatter.money(date.iqbResult.resultTotal.orderAmtTotal));
					   				$("#allTerms").val(date.iqbResult.resultTotal.orderAmtCount);
					   		},
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getLoanOrderList',
	   			loadSuccess:function(date){
	   				//支付金额赋值
	   				$("#allAmt").val(Formatter.money(date.iqbResult.resultTotal.orderAmtTotal));
	   				$("#allTerms").val(date.iqbResult.resultTotal.orderAmtCount);
	   			}
			}
		},
		checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		$('#datagrid').datagrid2('checkAll');
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
		loan : function(data){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				var orderId = [];
				for(var i=0;i<rows.length;i++){
					orderId.push(rows[i].orderId);
				}
				$('#open-win').modal('show');
				$('select[name="lendersSubject"]').val(null).trigger('change');
				$("#beginDate").val(_this.getNowFormatDate());
				$("#btn-install-sure").unbind("click").click(function(){
					if($("#updateForm").form('validate')){
						var loanDate = $("#loanDate1").val();
						var lendersSubject = $("#lendersSubject").val();
						var data = {
								'orderIds':orderId,
								'loanDate':loanDate,
								'lendersSubject':lendersSubject
							};
						IQB.get(_this.config.action.stage, data, function(result) {
							if(result.success == 1){
								IQB.alert('放款成功');
								$('#open-win').modal('hide');
								_this.refresh();
								//车300灰名单发送
								IQB.post(urls['cfm'] + '/cheThreeHunder/registerPostLoanMonitorCar', {'orderIds':orderId}, function(result){
									if(result.iqbResult.result.status == "1"){
										IQB.alert('发送成功');
									}else{
										IQB.alert(result.iqbResult.result.message);
									}
								})
							}else{
								IQB.alert('放款失败');
								$('#open-win').modal('hide');
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
		getNowFormatDate : function() {
	          var date = new Date();
	          var seperator1 = "-";
	          var year = date.getFullYear();
	          var month = date.getMonth() + 1;
	          var strDate = date.getDate();
	          if (month >= 1 && month <= 9) {
	              month = "0" + month;
	          }
	          if (strDate >= 0 && strDate <= 9) {
	              strDate = "0" + strDate;
	          }
	          var currentdate = year.toString() + month.toString() + strDate.toString();
	          return currentdate;
	    },
		exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var regId = $("#regId").val();
				var realName = $("#realName").val();
				var orderId = $("#orderId").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var loanStartTime = $("#loanStartTime").val();
				var loanEndTime = $("#loanEndTime").val();
				var datas = "?merchNames=" + merchName + "&regId=" + regId + "&realName=" + realName + "&orderId=" + orderId + "&startTime=" + startTime + "&endTime=" + endTime+ "&loanStartTime=" + loanStartTime+ "&loanEndTime=" + loanEndTime;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/business/getLoanOrderList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})});
        },
        initSelect : function(){
        	IQB.getDictListByDictType2('lendersSubject', 'Lenders_Subject');
			$('select[name="lendersSubject"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
        },
		init: function(){ 
			$('#btn-loan').on('click',function(){_this.loan()});
			_grid = new DataGrid2(_this.config); 
			_this.checkAll();
			_grid.init();//初始化按钮、表格
			_this.exports();//导出
			_this.initSelect();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.loanManagement.init();
	datepicker(startTime);
	datepicker(endTime);
	datepicker(loanStartTime);
	datepicker(loanEndTime);
	datepicker(loanDate1);
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
