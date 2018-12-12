function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var orderId = getQueryString('orderId');
$package('IQB.gpsStatusTraceDetail');
IQB.gpsStatusTraceDetail = function() {
    var _this = {
        cache: {
        	viewer: {}
        },
        config: {
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					$('select[name="gpsStatus"]').val(null).trigger('change');
				}
            }
        },
        save : function(){
        	if($('#updateForm').form('validate')){
        		var option = {
        				'orderId':window.orderId,
        				'gpsStatus':$('#gpsStatus').val(),
        				'remark':$('#remark').val(),
        				'disposalScheme':$('#scheme').val()
        		};
        		IQB.post(urls['cfm'] + '/carstatus/saveGPSInfo', option, function(result) {
        			if(result.retCode == 0){
        				//关闭窗口
        				var url = window.location.pathname;
						var param = window.parent.IQB.main2.fetchCache(url);
						var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
						window.parent.IQB.main2.call(callback);
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
        	var data = {
					"orderId": window.orderId,
			}
            IQB.post(urls['cfm'] + '/carstatus/getOrderInfoAndGPSInfoByOrderId' , data,function(result){
                $('#orderId').text(Formatter.isNull(result.iqbResult.result.orderId));
                $('#realName').text(Formatter.isNull(result.iqbResult.result.realName));
                $('#regId').text(Formatter.isNull(result.iqbResult.result.regId));
                $('#orderName').text(Formatter.isNull(result.iqbResult.result.orderName));
                $('#carNo').text(Formatter.isNull(result.iqbResult.result.carNo));
                $('#plate').text(Formatter.isNull(result.iqbResult.result.plate));
                //回显页面显示最近一条gps记录
                $('#gpsStatusView').text(Formatter.gpsStatusShow(result.iqbResult.result.gpsStatus));
                $('#remarkView').text(Formatter.isNull(result.iqbResult.result.remark));
                $('#schemeView').text(Formatter.isNull(result.iqbResult.result.disposalScheme));
            });
        },
		initApprovalHistory: function(){//初始化订单历史
			$('#datagrid').datagrid2({
				url: urls['rootUrl'] + '/carstatus/selectGPSInfoListByOrderId',
				pagination: true,
				queryParams: {
					orderId: window.orderId,
				}
			});
		},
		initSelect : function(){
			IQB.getDictListByDictType2('gpsStatus', 'GPS_STATUS');
			$('select[name="gpsStatus"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
        init: function(){       
            _this.showHtml(); 
            _this.initApprovalHistory();
            $('#btn-save').on('click', function(){_this.save()});
			$('#btn-unsave').on('click', function(){_this.unsave()});
			_this.initSelect();
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.gpsStatusTraceDetail.init();
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