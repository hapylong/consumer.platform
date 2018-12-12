function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var id = getQueryString('id');
$package('IQB.assetAllocationDetail');
IQB.assetAllocationDetail = function() {
    var _this = {
        cache: {
        	
        },
        // 回显赋值
        showHtml: function() {
            IQB.post(urls['cfm'] + '/assetallocine/get_division_assets_details/' + id, {}, function(result){
            	$('#updateForm').form('load', result.iqbResult.result); 
                $('#expireDate').val(Formatter.timeCfm2(result.iqbResult.result.expireDate));
                $('#proName').val(result.iqbResult.result.proName);
                $('#bakOrgan').val(result.iqbResult.result.bakOrgan);
                $('#url').val(result.iqbResult.result.url);
                $('#proDetail').val(result.iqbResult.result.proDetail);
                $('#tranCondition').val(result.iqbResult.result.tranCondition);
                $('#safeWay').val(result.iqbResult.result.safeWay);
            });
        },
        // 保存 
        save: function(){
        	$('#btn-save').attr('disabled',true);
        	if($('#updateForm').form('validate')){
        		IQB.postIfFail(urls['cfm'] + '/assetallocine/persist_details', $('#updateForm').serializeObject(), function(result){
        			if(result.success=="1") {
        				var url = window.location.pathname;
    					var param = window.parent.IQB.main2.fetchCache(url);
    					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
    					var callback2 = '';
    					//var callback2 = '_this.closeTab(\'' + param.lastTab.tabId + '\')';
    					//var callback3 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\', \'' + false + '\', \'' + false + '\', \'' + null + '\')';
    					window.parent.IQB.main2.call(callback , callback2);
        			}else{
        				IQB.alert(result.retUserInfo);
        				$('#btn-save').removeAttr('disabled');
        			}
                });
        	}else{
        		$('#btn-save').removeAttr('disabled');
        	}
        },
        //关闭
        close:function(){
        	var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
			var callback2 = '_this.closeTab(\'' + param.lastTab.tabId + '\')';
			var callback3 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\', \'' + false + '\', \'' + false + '\', \'' + null + '\')';
			window.parent.IQB.main2.call2(callback , callback2 , callback3);
        },
        init: function(){       
            _this.showHtml(); 
            $('#btn-save').unbind("click").on('click', function(){_this.save()});
            $('#btn-close').on('click', function(){_this.close()});
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    datepicker(proBeginDate);
    IQB.assetAllocationDetail.init();
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