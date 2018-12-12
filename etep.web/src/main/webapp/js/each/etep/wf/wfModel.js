$package('IQB.wfDesign');
IQB.wfDesign = function() {
	var _grid = null;
	var _this = {

		insertModelOpen : function() {
			$('#insert-model-win').modal('show');
		},
		insertModelClose : function() {
			$('#insert-model-win').modal('hide');
		},
		insertModelSave : function() {
			var url = _this.config.action.insertWf;
			var option = $('#insertModelForm').serializeObject();
			IQB.get(url, option, function(result){
				$('#insert-model-win').modal('hide');
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab('wf_edit', '模型编辑', '/etep.web/modeler.html?modelId=' + result.iqbResult.result.id, true, true, {lastTab: param});
			});
		},

		updateModel : function() {
			var records = _grid.util.getCheckedRows();
			if(_grid.util.checkSelectOne(records)){
				var url = window.location.pathname;
				var param = window.parent.IQB.main2.fetchCache(url);
				window.parent.IQB.main2.openTab('wf_edit', '模型编辑', '/etep.web/modeler.html?modelId=' + records[0]['id'], true, true, {lastTab: param});
			}
		},
		delModel : function() {
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				IQB.confirm('确认要执行吗？', function(){
					var option = {modelId: records[0]['id']};
					IQB.getById(_this.config.action.delWf, option, function(result) {
						_grid.handler.refresh();
					});
				}, function(){});
			}
		},
		
		exporturl: function(param){
				IQB.confirm('确认要导出吗？', function(){
					var option = {modelId: param};
					IQB.getById(urls['rootUrl'] + '/WfDesign/export', option, function(result) {
						IQB.alert("导出成功到：C:\export\模型Key.bpmn20.xml");
					});
				}, function(){});
			
			/*$('#btn-export').prop('href', urls['rootUrl'] + '/WfDesign/export?param=' + param);//modelId
			IQB.alert("导出到：C:\export\模型Key.bpmn20.xml");
			document.getElementById('btn-export').click();
			window.location.href = urls['webUrl'] + '/view/etep/wf/wfModel.html';*/
		},
		
		//部署
		deployModel : function() {
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				IQB.confirm('确认要部署吗？', function(){
					var option = {modelId: records[0]['id']};
					IQB.getById(_this.config.action.deployWf, option, function(result) {
						if(result.success=="1"){
							IQB.alert("流程部署成功！");
						}
					});
				}, function(){});
			}
		},
		config : {
			action : {
				insertWf : urls['rootUrl'] + '/workflow/model/create',
				delWf : urls['rootUrl'] + '/WfDesign/modeldelete',
				deployWf : urls['rootUrl'] + '/WfDesign/modeldeploy',
			},
			dataGrid : {
				url : urls['rootUrl'] + '/WfDesign/modellist',
				singleCheck : true
			}
		},
		init : function() {
			_grid = new DataGrid(_this.config);
			_grid.init();

			/*
			 * <button id="insert-model-btn" type="button" class="btn
			 * btn-primary btn-sm">新增(A)</button> <button id="update-model-btn"
			 * type="button" class="btn btn-primary btn-sm">编辑(M)</button>
			 * <button id="del-model-btn" type="button" class="btn btn-primary
			 * btn-sm">删除(D)</button> <button id="remove-model-btn"
			 * type="button" class="btn btn-danger btn-sm">发布(P)</button>
			 */

			$("#insert-model-btn").click(function() {
				_this.insertModelOpen();
			});
			$("#insert-model-close").click(function() {
				_this.insertModelClose();
			});
			$("#insert-model-save").click(function() {
				_this.insertModelSave();
			});
			$("#update-model-btn").click(function() {
				_this.updateModel();
			});
			$("#del-model-btn").click(function() {
				_this.delModel();
			});
			$("#deploy-model-btn").click(function() {
				_this.deployModel();
			});
		}
	}
	return _this;
}();

$(function() {
	IQB.wfDesign.init();
});