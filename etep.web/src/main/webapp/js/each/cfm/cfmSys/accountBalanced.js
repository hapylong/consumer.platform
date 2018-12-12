$package('IQB.accountBalanced');
IQB.accountBalanced = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			'accountList':''
		},
		overdueInterest: function(val, row, rowIndex){
			var overdueInterest = val; 
			if(!isNaN(row.cutOverdueInterest)){
				overdueInterest = parseFloat(overdueInterest)-parseFloat(row.cutOverdueInterest);
			}
			return Formatter.money(overdueInterest);
		},
		config: {
			action: {//页面请求参数
				refund: urls['cfm'] + '/back/refund',
				verifyPayment:urls['cfm'] + '/pay/verifyPayment'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					$('#checkAll').prop('checked',false)
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/credit/repayment/All',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
							   onPageChanged : function(page){
								   $('#checkAll').prop('checked',false)
					   				_this.cache.page = page;
					   				console.log(_this.cache.page);
					   			}
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/credit/repayment/All',
	   			//singleCheck: true,
	   			queryParams: {
	   				type: 1
	   			},
	   			onPageChanged : function(page){
	   				$('#checkAll').prop('checked',false)
	   				_this.cache.page = page;
	   				console.log(_this.cache.page);
	   			}
			}
		},
		refresh : function(page){
				$("#datagrid").datagrid2({url: urls['cfm'] + '/credit/repayment/All',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
				});
				$('#checkAll').prop('checked',false)
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
	          var currentdate = year + seperator1 + month + seperator1 + strDate;
	          return currentdate;
	    },
	    checkAll: function(){
        	//全选
			$('#checkAll').click(function(){
	        	if($('#checkAll').prop('checked')){
	        		 $('#datagrid').find('tbody').find('tr').each(function(i){
						  $(this).find('.datagrid-row-checkbox:not(:disabled)').prop('checked', true); 
					 }); 
	        	}else{
	        		$('#datagrid').datagrid2('uncheckAll');
	        	}
			});
        },
        verifyPayment : function(){
        	rows = $("#datagrid").datagrid2('getCheckedRows');
        	_this.cache.length = rows.length;
        	if(_this.cache.length > 0){
        		var rows,orderId,curRepayAmt,repayNo,length,lastRepayDate;
    			_this.cache.accountList = [];//通过验证需平账的账单列表
    			var lastRepayDateFlag = true;
    			lastRepayDate = rows[0].lastRepayDate;
    			//判断最迟还款日为同一天的账单
    			$.each(rows,function(i,n){
    				if(rows[i].lastRepayDate != lastRepayDate){
    					IQB.alert('请选择最迟还款日为同一天的账单批量平账');
    					lastRepayDateFlag = false;
    				}
    			});
    			//同一还款日
    			if(lastRepayDateFlag){
    				$.each(rows,function(i,n){
						orderId = n.orderId;
						curRepayAmt = n.curRepayAmt;
						repayNo = n.repayNo;
						IQB.get(_this.config.action.verifyPayment, {'orderId':orderId,'repayNo':repayNo}, function(result) {
							if(result.retCode == 'success'){
								_this.cache.accountList.push(n);
							}else if(result.retCode == 'error'){
					    	      IQB.alert(result.retMsg);
						    }
							if(i == _this.cache.length-1 && _this.cache.accountList.length > 0){
								_this.accountBalance();
							}
						})
					});
    		   }
        	}else{ 
			    IQB.alert('未选中行');
    		}
		},
		accountBalance : function(){
			  //去平账
			  $('#open-win').modal('show');
		      $("#btn-accountBalanced-sure").removeAttr('disabled');
		      //表单值初始化
		      $("#repayDate").val(_this.getNowFormatDate());
		      $("#tradeNo").val('');
		      $("#reason").val('');
		      //是否显示并赋值
		      if(_this.cache.length == 1){
				  //单个平账
		    	  $("#curRepayAmt").validatebox({ required:true });
		    	  $("#tradeNo").validatebox({ required:true });
		    	  $('.curRepayAmtAbout').show();
		    	  $('.tradeNoAbout').show();
		    	  $("#curRepayAmt").val(_this.cache.accountList[0].curRepayAmt);
			  }else{
				  //批量平账
				  $("#curRepayAmt").validatebox({ required:false });
		    	  $("#tradeNo").validatebox({ required:false });
		    	  $('.curRepayAmtAbout').hide();
		    	  $('.tradeNoAbout').hide();
			  }
			  $("#btn-accountBalanced-sure").unbind("click").click(function(){
					  if($("#updateForm").form('validate')){
						  //平账按钮置灰
						  $("#btn-accountBalanced-sure").attr('disabled',true);
						  //循环平账
						  $.each(_this.cache.accountList,function(i,n){
							  var option = {
									  'orderId':n.orderId,
									  'repayDate':$('#repayDate').val(),
									  'tradeNo':$("#tradeNo").val(),
									  'reason':$("#reason").val(),
									  'repayNo':n.repayNo
							  };
							  IQB.post(_this.config.action.refund, option, function(result) {
								  if(result.iqbResult.result.retCode == 'success'){
									  IQB.alert('平账成功');
									  $('#open-win').modal('hide');
									  _this.refresh(_this.cache.page);
								  }else{
									  IQB.alert('平账失败');
									  $('#open-win').modal('hide');
								  }
							  })
						  });
					  }; 
			  });
	          $("#btn-close").click(function(){
	        	 $('#open-win').modal('hide');
			  });
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.checkAll();
			//单个平账
			$("#btn-accountBalanced").unbind('click').on('click',function(){
				_this.verifyPayment();
			});
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.accountBalanced.init();
	datepicker(startDate);
	datepicker(endDate);
	datepicker(repayDate);
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
