$package('IQB.toPayment');
IQB.toPayment = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			 t:''
		},
		//还款
		turnTo : function(rowIndex){
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId=row.orderId,amount=row.caculateAmt,orderItem=row.repayNo;
			var url = '/etep.web/view/cfm/creditSys/payWay.html?orderId='+orderId+'&amount='+amount+'&orderItem='+'&type=1';
			url = encodeURI(url);
			window.open(url);
		},
		query : function(){
			$("#btn-search").click(function(){
				_this.refresh(1);
			})
		},
		refresh : function(page){
			clearInterval(_this.cache.t);
			_this.cache.t = setInterval(function(){
				$("#datagrid").datagrid2({url: urls['cfm'] + '/business/getPreOrderList',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo}),
					onPageChanged : function(page){
		   				console.log(page);
		   				_this.refresh(page);
		   			}	
				});
			},10000);
		},
		//催收
		collection:function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				var data = {
						"flag" : "2",
						"list" : rows
				};
				IQB.confirm('确认要催收吗？', function(){
					IQB.get(_this.config.action.collectionUrl, data, function(result) {
						var fails = result.iqbResult;
						if(fails.count == 0){
							IQB.alert("催收成功！");
						}else{
							var str = '';
							for(var i=0;i<fails.result.length;i++){
								str += fails.result[i]+",";
							}
							str = str.substring(0,str.length-1);
							IQB.alert(str +"未成功催收！");
						}
					})
					console.log(rows);
				});
			}else{				
				IQB.alert("未选中行");
			}
		},
		//刷新页面
		config: {
			action: {//页面请求参数
  				collectionUrl : urls['cfm']+ '/SMS/sendSms',
  				verifyPayment : urls['cfm']+ '/pay/verifyPayment'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/business/getPreOrderList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/business/getPreOrderList',
	   			onPageChanged : function(page){
	   				console.log(page);
	   				_this.refresh(page);
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.query();
			_this.refresh(1);
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.toPayment.init();
	datepicker(startTime);
	datepicker(endTime);
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:true,
	    format:'Y-m-d H:i:00',
		allowBlank: true
	});
};