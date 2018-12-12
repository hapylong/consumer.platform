$package('IQB.wfDesign');
IQB.wfDesign = function() {
	var _grid = null;
	var _this = {
		wfdel : function() {
			var url = _this.config.action.wfDel;
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				var option = {};
				option.deploymentId = records[0]['deploymentId'];
				IQB.getById(url, option, function(result) {
					_grid.handler.refresh();

				});
			}
		},
		// 挂起功能
		wfsuspend : function() {
			var url = _this.config.action.wfSuspend;
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				if (records[0]['suspensionState'] == "1") {
					var option = {};
					option.procDefId = records[0]['id'];
					IQB.getById(url, option, function(result) {
						_grid.handler.refresh();

					});
				} else {
					IQB.alert('选中该行不允许挂起');
				}
			}
		},

		// 恢复功能
		wfactive : function() {
			var url = _this.config.action.wfActive;
			var records = _grid.util.getCheckedRows();
			if (_grid.util.checkSelectOne(records)) {
				if (records[0]['suspensionState'] == "2") {
					var option = {};
					option.procDefId = records[0]['id'];
					IQB.getById(url, option, function(result) {
						_grid.handler.refresh();
					});
				} else {
					IQB.alert('选中该行不是挂起状态,不允许恢复');
				}
			}
		},

		config : {
			action : {
				wfDel : urls['rootUrl'] + '/WfDesign/wfdel',
				wfSuspend : urls['rootUrl'] + '/WfDesign/wfsuspend',
				wfActive : urls['rootUrl'] + '/WfDesign/wfactive',
				wfOpenImage : urls['rootUrl'] + '/WfDesign/openimage',
			},
			dataGrid : {
				url : urls['rootUrl'] + '/WfDesign/processlist',
				singleCheck : true
			}
		},
		init : function() {
			_grid = new DataGrid(_this.config);
			_grid.init();

			$("#btn-wfdel").on('click', function() {
				_this.wfdel();
			});
			$("#btn-wfsuspend").on('click', function() {
				_this.wfsuspend();
			});// 流程挂起
			$("#btn-wfactive").on('click', function() {
				_this.wfactive();
			});// 流程恢复

			/*
			 * $("#wf-insert-btn").on('click', function(){_this.insertOpen();});
			 * $("#wf-insert-close").on('click',
			 * function(){_this.insertClose();});
			 * $("#wf-insert-save").on('click',
			 * function(){_this.insertSave();});
			 */
		}
	}
	return _this;
}();

$(function() {
	IQB.wfDesign.init();
});

$.getImage = function(val, row, rowIndex) {
	/*	var id = row.id;
	return '<button type="button" class="btn btn-link" onclick ="openImage(\''
			+ id + '\')">' + val + '</button>';*/
	
	return '<a href="' + urls['rootUrl'] + '/WfDesign/openimage?procDefId='+ row.id + '" data-lightbox="' + rowIndex + '" data-title="' + val + '">' + val + '</a>';
}
