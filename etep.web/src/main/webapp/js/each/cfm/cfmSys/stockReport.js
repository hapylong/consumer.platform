$package('IQB.orderQuery');
IQB.orderQuery = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        config: {
            action: {//页面请求参数
                exports: urls['cfm'] + '/credit/exportStockStatisticsList',
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
							 url: urls['cfm'] + '/credit/listStockStatisticsPage',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
            },
            dataGrid: {//表格参数               
                url: urls['cfm'] + '/credit/listStockStatisticsPage',
                queryParams: {
                    type: 1
                }
            }
        },
        exports : function(){
            $("#btn-export").click(function(){
                var merchNames = $("#merchNames").val();
                var orderId = $("#orderId").val();
                var regId = $("#regId").val();
                var curRepayDate = $("#curRepayDate").val();
                var datas = "?merchNames=" + merchNames + "&orderId=" + orderId + "&regId=" + regId+"&curRepayDate="+curRepayDate;
                var urls = _this.config.action.exports + datas;
                $("#btn-export").attr("href",urls);
            });
        },
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.exports();//导出
        }
    }
    return _this;
}();

$(function(){
    //页面初始化
    IQB.orderQuery.init();
    datepicker(curRepayDate);
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
