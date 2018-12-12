$package('IQB.cardealer');
IQB.borrowersonline = function() {
	var _box = null;
	var _tree=null;
	var _this = {
		cache :{
			page:0
		},
		expandAll: function(flag){//自定义函数(全部展开、收起)
			$.fn.zTree.getZTreeObj().expandAll(flag);
		   },
		checkAll: function(flag){//自定义函数(全部选中、取消)
			_tree.getZTreeObj().checkAllNodes(flag);
		},
		check: function(treeNodes){//自定义函数(部分选中)
			var treeObj = _tree.getTreeObj();
        	if(treeNodes && treeNodes.length > 0){
				treeObj.expandAll(true);
				treeObj.checkAllNodes(false);
				$.each(treeNodes, function(i, m){		
					var treeNode = treeObj.getNodeByParam('id', m.id);
					treeObj.checkNode(treeNode, true, false, false);
				});
			}else{
				treeObj.expandAll(true);
				treeObj.checkAllNodes(false);
			}			
		},
		addBlack : function() {
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var ids = "";
			for (var i=0; i<rows.length; i++){
			    ids += parseInt(rows[i].id) + ","
			}
			var data = {
					"ids" : ids
			}
			IQB.post(urls['rootUrl'] + '/dealer/joinBlack', data, function(result,status){
				if(result.iqbResult.result == "success") {
					alert("操作成功");
				}
			})
		},
		cancelBlack : function() {
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			var ids = "";
			for (var i=0; i<rows.length; i++){
			    ids += parseInt(rows[i].id) + ","
			}
			var data = {
					"ids" : ids
			}
			IQB.post(urls['rootUrl'] + '/dealer/removeBlack', data, function(result,status){
				if(result.iqbResult.result == "success") {
					alert("操作成功");
				}
			})
		},
		config : {
			action : {
			    
			},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
				}
			},
			dataGrid: {//表格参数
				url: urls['rootUrl'] + '/car_dealer/cget_car_dealer_list',
				onPageChanged : function(page){
	   				_this.cache.page = page;
	   			}
			}
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/car_dealer/cget_car_dealer_list',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page})	
			});
	    },
		updateInfoClose : function() {
			$('#cardealer-update').modal('hide');
			$("#sourceCarName").val("");
			$("#address").val("");
			$("#contactMethod").val("");
		},
		updateInfo : function(data){
			var rows = $('#datagrid').datagrid2('getCheckedRows');
			if(rows.length > 1) {
				alert("请选中单条数据.");
				return;
			} else {
				$("#sourceCarName").val(rows[0].sourceCarName),
				$("#address").val(rows[0].address),
				$("#contactMethod").val(rows[0].contactMethod)
			}
			$('#cardealer-update').modal({backdrop: 'static', keyboard: false, show: true});
			$("#btn-cardealer-update-label").unbind("click").click(function(){
				var data = {
						"orderId" : rows[0].orderId,
						"sourceCarName" : $("#sourceCarName").val(),
						"address" : $("#address").val(),
						"contactMethod" : $("#contactMethod").val()
				}
				IQB.post(urls['rootUrl'] + '/dealer/updateDealerInfo', data, function(result,status){
					if(result.iqbResult.result == "success") {
						alert("操作成功");
						_this.updateInfoClose();
						_this.refresh();
					}
				})
			});
		},
		init : function() {
			_box = new DataGrid2(_this.config);			
			_box.init();//初始化表格相关	
			$("#update-info").click(function(){_this.updateInfo();})
			$("#add-black").click(function(){_this.addBlack()});
			$("#cancel-black").click(function() {_this.cancelBlack();})
			$("#btn-cardealer-update-label-close").click(function(){_this.updateInfoClose()})
		}
	}
	return _this;
}();


$(function(){
	IQB.borrowersonline.init();
});
