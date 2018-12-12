function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var orderId = getQueryString('orderId');
var realName = getQueryString('realName');
var regId = getQueryString('regId');
var merchantName = getQueryString('merchantName');

IQB.loanManageDetail = function() {
    var _this = {
        cache: {
        	viewer: {}
        },
        config: {
            event: {//按钮绑定函数，不定义的话采用默认
            },
            dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/afterLoan/selectCustomerInfoListByOrderId',
	   			queryParams: {'orderId':window.orderId},  
			}
        },
      // 回显赋值
        showHtml: function() {
        	 $('#realName').text(Formatter.isNull(window.realName));
        	 $('#regId').text(Formatter.isNull(window.regId));
             $('#merchantName').text(Formatter.isNull(window.merchantName));
        },
        unsave : function(){
        	var url = window.location.pathname;
    		var param = window.parent.IQB.main2.fetchCache(url);
    		var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
			var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + true + ',' + true + ',' + null + ')';
			window.parent.IQB.main2.call(callback, callback2);
        },
        init: function(){       
        	_grid = new DataGrid2(_this.config); 
        	_grid.init();//初始化按钮、表格
        	_this.showHtml();
			$('#btn-unsave').on('click', function(){_this.unsave()});
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.loanManageDetail.init();
});