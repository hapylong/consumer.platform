function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var orderId = getQueryString('orderId');
var curItems = getQueryString('curItems');

IQB.telephoneRemindAct = function() {
    var _this = {
        cache: {
        	viewer: {}
        },
        config: {
        	action: {//页面请求参数
  				insert: urls['cfm'] + '/instRemindPhone/saveRecordInfo',
  			},
            event: {//按钮绑定函数，不定义的话采用默认
            },
            dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/instRemindPhone/queryRecordList',
	   			queryParams: {'orderId':window.orderId,'curItems':window.curItems,'flag':'1'},  
			}
        },
        rule : function(){
			$("#telRecord1").change(function(){
				if($('#telRecord1').val()=='1'){
					$(".rule").hide();
				}else
					$(".rule").show();
			})
		},
        save : function(){
				  if($("#select2-telRecord1-container").text()=="请选择"){
					  IQB.alert("请选择通话结果");
					  return false;
				  }
				  if($('#telRecord1').val()!='1'){
					  if($("#select2-failReason1-container").text()=="请选择"){
						  IQB.alert("请选择失败原因");
						  return false;
					  }
					  if($("#select2-mobileDealOpinion1-container").text()=="请选择"){
						  IQB.alert("请选择处理意见");
						  return false;
					  }
				  } 
				  var data={};
				  data.orderId = window.orderId;
				  data.flag = 1;
				  data.curItems = window.curItems;
				  data.telRecord = $("#telRecord1").val();
				  data.remark = $('#remark').val();
				  if($('#telRecord1').val()=='1'){
					  data.failReason = '';
					  data.dealReason = '';
				  }else{
					  data.failReason = $('#failReason1').val();
					  data.dealReason = $('#mobileDealOpinion1').val();
				  }
				  IQB.post(_this.config.action.insert, data, function(result) {
		            	if (result.success=="1") {
		            		IQB.alert('电话提醒成功');
		            		var url = window.location.pathname;
		            		var param = window.parent.IQB.main2.fetchCache(url);
		            		var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
							// var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + true + ',' + true + ',' + null + ')';
							window.parent.IQB.main2.call(callback);
		            	};
				  	})
        },
        unsave : function(){
        	var url = window.location.pathname;
			var param = window.parent.IQB.main2.fetchCache(url);
			var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
			window.parent.IQB.main2.call(callback);
        },
        initSelect : function(){
            IQB.getDictListByDictType2('telRecord', 'call_result');
            IQB.getDictListByDictType2('failReason', 'failure_reason');
            IQB.getDictListByDictType2('mobileDealOpinion', 'treatment_suggestion');
            $('select[name="telRecord"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
            $('select[name="failReason"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
            $('select[name="mobileDealOpinion"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
        },
        init: function(){       
        	_grid = new DataGrid2(_this.config); 
        	_grid.init();//初始化按钮、表格
        	_this.initSelect();
			_this.rule();
            $('#btn-save').on('click', function(){_this.save()});
			$('#btn-unsave').on('click', function(){_this.unsave()});
        }
    }
    return _this;
}();
$(function() {
	//页面初始化
    IQB.telephoneRemindAct.init();
});