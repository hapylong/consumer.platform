$package('IQB.assetAllocation');
IQB.assetAllocation = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            't':''
        },
        skipNewWindow : function() {
        	var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows && rows.length > 0){
            	var id = rows[0]['id'];
            	var url = window.location.pathname;
				//var param = window.parent.IQB.main2.fetchCache(url);
            	var param = {tabId: 192, tabTitle: "资产分配", tabUrl: "/etep.web/view/cfm/requestFunds/assetAllocation.html"}
                window.parent.IQB.main2.openTab('assetAllocationDetail', '资产分配详情', '/etep.web/view/cfm/requestFunds/assetAllocationDetail.html?id=' + id, true, true, {lastTab: param});
            }else{
                IQB.alert('未选中行');
                return false;   
            }                 
        },
        config: {
            action: {//页面请求参数
                
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					_grid.handler.reset();
					$('select[name="bizType"]').val(null).trigger('change');
				},
            },
            dataGrid: {//表格参数               
                url: urls['cfm'] + '/assetallocine/get_division_assets_details',
                singleCheck: true,
                onPageChanged : function(page){
	   				console.log(page);
	   				_this.refresh(page);
	   			}
            }
        },
        initSelect: function(){
			IQB.getDictListByDictType2('bizType', 'business_type');
			$('select[name="bizType"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		query : function(){
			$("#btn-search").click(function(){
				_this.refresh(1);
			})
		},
		refresh : function(page){
			clearInterval(_this.cache.t);
			_this.cache.t = setInterval(function(){
				$("#datagrid").datagrid2({url: urls['cfm'] + '/assetallocine/get_division_assets_details',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: page}),
					onPageChanged : function(page){
		   				console.log(page);
		   				_this.refresh(page);
		   			}	
				});
			},10000);
		},
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.initSelect();
            $("#btn-skip").click(function(){_this.skipNewWindow()});
            _this.query();
            _this.refresh(1);
        }
    }
    return _this;
}();
$(function(){
    //页面初始化
    datepicker(search_expireDate);
    IQB.assetAllocation.init();
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
