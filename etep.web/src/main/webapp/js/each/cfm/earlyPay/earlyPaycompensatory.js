$package('IQB.earlyPaycompensatory');
IQB.earlyPaycompensatory = function(){
	var _grid = null;
	var _tree = null;
	var _box = null;
	var _list = null;
	var _this = {
		cache :{
			page : 1
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
							 url: urls['cfm'] + '/settle/selectPrepaymentList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/settle/selectPrepaymentList',
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   			},
	   			singleCheck: true
			}
		},
		turnTo : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId=row.orderId,amount=row.needPayAmt,id=row.id,totalRepayAmt=row.totalRepayAmt,regId=row.regId;
			amount = Number(amount).toFixed(2);
			//本地缓存
			var object = {
					'orderId':orderId,
					'amount':amount,
					'id':id,
					'sumAmt':totalRepayAmt,
					'regId':regId
			}
			localStorage.setItem("amountObject",JSON.stringify(object));
			var url = '/etep.web/view/cfm/earlyPay/payWay.html';
			window.open(url);
		},
		turnToSplit : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId=row.orderId,amount=row.needPayAmt,id=row.id,totalRepayAmt=row.totalRepayAmt,regId=row.regId;
			amount = Number(amount).toFixed(2);
			//本地缓存
			var objectSplit = {
					'orderId':orderId,
					'amount':amount,
					'id':id,
					'sumAmt':totalRepayAmt,
					'regId':regId
			}
			console.log(objectSplit);
			localStorage.setItem("objectSplit",JSON.stringify(objectSplit));
			var url = '/etep.web/view/cfm/earlyPay/paySplit.html';
			window.open(url);
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_box = new DataGrid2(_this.config);
			_box.init();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.earlyPaycompensatory.init();
	datepicker(beginTime);
	datepicker(endTime);
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