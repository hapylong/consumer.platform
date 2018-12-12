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
	   			queryParams: {'orderId':window.orderId,'curItems':window.curItems,'flag':'2'},  
			}
        },
        save : function(){
        	if($("#select2-mobileCollection1-container").text()=="请选择"){
				  IQB.alert("请选择电催结果");
				  return false;
			  }
			  if($("#select2-mobileDealOpinion1-container").text()=="请选择"){
				  IQB.alert("请选择处理意见");
				  return false;
			  }
			  var data={};
			  data.orderId = window.orderId;
			  data.flag = 2;
			  data.curItems = window.curItems;
			  data.mobileCollection = $("#mobileCollection1").val();
			  data.mobileDealOpinion = $('#mobileDealOpinion1').val();
			  data.remark = $('#remark').val();
              IQB.post(_this.config.action.insert, data, function(result) {
		            if (result.success=="1") {
                		IQB.alert('电催成功');
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
                IQB.getDictListByDictType2('mobileCollection', 'telephone_urging');
                IQB.getDictListByDictType2('mobileDealOpinion', 'treatment_suggestion');
                $('select[name="mobileCollection"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
                $('select[name="mobileDealOpinion"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
        },
        init: function(){       
        	_grid = new DataGrid2(_this.config); 
        	_grid.init();//初始化按钮、表格
        	_this.initSelect();
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