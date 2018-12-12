function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var orderId = getQueryString('orderId');
var merchantShortName = getQueryString('merchantShortName');
var realName = getQueryString('realName');
var regId = getQueryString('regId');
var type = getQueryString('type');
var smsMobile = getQueryString('smsMobile');
$package('IQB.customerFollowDetail');
IQB.customerFollowDetail = function() {
    var _this = {
        cache: {
        	viewer: {}
        },
        config: {
            event: {//按钮绑定函数，不定义的话采用默认
            },
            dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectCustomerInfoListByOrderId',
	   			queryParams:{
                	'orderId':window.orderId
                },
	   			singleCheck: true
			}
        },
        save : function(){
        	if($('#updateForm').form('validate')){
        		var option = {
        				'orderId':window.orderId,
        				'remark':$('#remark').val(),
        				'smsMobile':$('#smsMobile').val()
        		};
        		IQB.post(urls['cfm'] + '/afterLoan/saveAfterLoanCustomer', option, function(result) {
        			if(result.success == 1){
        				//关闭窗口
        				var url = window.location.pathname;
	   					var param = window.parent.IQB.main2.fetchCache(url);
	   					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
	   					var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + true + ',' + null + ')';
	   					window.parent.IQB.main2.call(callback, callback2);
        			}
        		})
        	}
        },
        unsave : function(){
        	var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
			window.parent.IQB.main2.call(callback);
        },
        // 回显赋值
        showHtml: function() {
        	 $('#realName').text(Formatter.isNull(window.realName));
        	 $('#regId').text(Formatter.isNull(window.regId));
             $('#merchantShortName').text(Formatter.isNull(window.merchantShortName));
             $('#smsMobile').val(window.smsMobile);
             if(window.type == 2){
            	 //查看功能
            	 $('.panel-heading').hide();
            	 $('#updateForm').hide();
             }else{
            	 $('.panel-heading').show();
            	 $('#updateForm').show();
             }
        },
        init: function(){       
        	_grid = new DataGrid2(_this.config); 
        	_grid.init();//初始化按钮、表格
            $('#btn-save').on('click', function(){_this.save()});
			$('#btn-unsave').on('click', function(){_this.unsave()});
			_this.showHtml();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.customerFollowDetail.init();
});
function datepicker(id){
    var date_ipt = $(id)
    $(id).datetimepicker({
        lang:'ch',
        timepicker:false,
        format:'Y-m-d',
        allowBlank: true
    });
};