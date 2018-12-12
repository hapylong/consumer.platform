$package('IQB.assetSplitDetail');
IQB.assetSplitDetail = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        config: {
            action: {//页面请求参数
            	exports: urls['cfm'] + '/assetallocine/export_xlsx_asset_break',
            	forBill: urls['cfm'] + '/assetallocine/getAllBillByOrderId'
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/assetallocine/cget_asset_break_details',
							 loadSuccess:function(date){
					   			//支付金额赋值
					   			$("#allAmt").val(Formatter.money(date.iqbResult.amtTotal));
					   		},
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
            },
            dataGrid: {//表格参数               
            	url: urls['cfm'] + '/assetallocine/cget_asset_break_details',
                //singleCheck: true
            	loadSuccess:function(date){
	   				//支付金额赋值
	   				$("#allAmt").val(Formatter.money(date.iqbResult.amtTotal));
	   			}
            }
        },
        exports : function(){
			$("#btn-export").click(function(){
				var merchName = $("#merchNames").val();
				var raiseInstitutions = $("#raiseInstitutions").val();
				var guaranteeInstitution = $("#guaranteeInstitution").val();
				var bakOrgan = $("#bakOrgan").val();
				var realName = $("#realName").val();
				var orderId = $("#orderId").val();
				var upperTime = $("#upperTime").val();
				var datas = "?merchNames=" + merchName + "&raiseInstitutions=" + raiseInstitutions + "&guaranteeInstitution=" + guaranteeInstitution + "&bakOrgan=" + bakOrgan + "&realName=" + realName+ "&orderId=" + orderId+ "&upperTime=" + upperTime;
	            var urls = _this.config.action.exports + datas;
	            $("#btn-export").attr("href",urls);
			});
		},
		alert: function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var url = row.url;
			if(url ==  ''){
				IQB.alert('挂牌链接为空！');
			}else{
				IQB.alert(url);
			}
		},
		forBill : function(data){
			var rowIndex = data;
			var row = $("#datagrid").datagrid2('getRow', rowIndex);
			var orderId = row.orderId;
			var regId = row.regId;
			var realName = row.realName;
			IQB.get(_this.config.action.forBill, {'orderId':orderId,'regId':regId}, function(result) {
				if(result.length > 0){
					var tableHtml = '';
					$('#open-win2').modal('show');
					//赋值
					$("#billRealName").val(realName);
					$("#billOrderId").val(orderId);
					for(var i=0;i<result.length;i++){
						var overdueInterest = result[i].curRepayOverdueInterest; 
						if(!isNaN(result[i].cutOverdueInterest)){
							overdueInterest = parseFloat(overdueInterest)-parseFloat(result[i].cutOverdueInterest);
						}
						tableHtml += "<tr><td>"+result[i].repayNo+"</td><td>"+Formatter.money(result[i].curRepayAmt)+
						"</td><td>"+Formatter.timeCfm2(result[i].lastRepayDate)+"</td><td>"
						+Formatter.money(result[i].curRealRepayamt)+"</td><td>"+Formatter.money(overdueInterest)+
						"</td><td>"+result[i].overdueDays+"</td><td>"+Formatter.status(result[i].status)+"</td></tr>";
					}
					$(".forBill").find('tbody').find('tr').remove();
					$(".forBill").append(tableHtml);
				}else if(result == null){
					IQB.alert('账单查询失败，请确认订单状态');
				}
			});
			$("#btn-close2").click(function(){
        	    $('#open-win2').modal('hide');
			});
		},
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.exports();//导出
        },
    }
    return _this;
}();
$(function(){
    //页面初始化
    IQB.assetSplitDetail.init();
    datepicker(upperTime);
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
