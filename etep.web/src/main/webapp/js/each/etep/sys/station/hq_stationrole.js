$package('IQB.hq_stationrole');
IQB.user = function() {
	var _box = null;
	var _tree=null;
	var _organizationTree=null;
	var _this = {
			cache: {
				
			},
		expandAll: function(flag){//自定义函数(全部展开、收起)
	        _tree.getZTreeObj().expandAll(flag);
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
		

	expandOrganAll: function(flag){//自定义函数(全部展开、收起)
		_organizationTree.getZTreeObj().expandAll(flag);
		},
		checkOrganAll: function(flag){//自定义函数(全部选中、取消)
		_organizationTree.getZTreeObj().checkAllNodes(flag);		
		},
		checkOrgan: function(treeNodes){//自定义函数(部分选中)
			var treeObj =  _organizationTree.getTreeObj();
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

		openRoleAuthWin: function(){		
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)){
				 if(records[0].stationStatus == 1){
					 _this.config.menuTree.queryParams.orgId = records[0].orgId;
					 _tree = new Tree(_this.config.menuTree);	
					 _tree.init();
					 var data = {roleId: records[0].id};
					 IQB.get(_this.config.action.getRoleAuth, data, function(result){	
						 var timer = setInterval(function(){
							 if(_tree.getTreeObj()){
							 	_this.check(result.iqbResult.result);							 	
							 	$('#role-auth-win').modal({backdrop: 'static', keyboard: false, show: true});
							 	clearInterval(timer);
							}
						 }, 100);						
					 })
					 
				 }else{
					 IQB.alert("对不起，您目前状态为非激活不能进行角色授权");
				 }
			}			
		},
		
		openOrganAuthWin:function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {	
			_organizationTree= new Tree(_this.config.organTree);	
		    _organizationTree.init();
		   var option = {roleId: records[0].id};
		    IQB.get(_this.config.action.getOrganAuth,option,function(result){
				var timer = setInterval(function(){
					if(_organizationTree.getTreeObj()){
						_this.checkOrgan(result.iqbResult.result);
						  $('#update-organ-win').modal({backdrop: 'static', keyboard: false, show: true});
						  clearInterval(timer);
					}
				},100)
				
			})
			}
		},
		closeRoleAuthWin : function() {
			$('#role-auth-win').modal('hide');
		},
		btnAuthSave : function() {
	       _box.form.treeFrom.attr('action',_this.config.action.updateRoleAuth);
			var records = _box.util.getCheckedRows();
			var roleId = records[0].id
			$('#roleId').val(roleId);
			var checkedtreeNodes = $.fn.zTree.getZTreeObj("menuTree").getCheckedNodes();	
		    $('.menuIds').remove();
			$.each(checkedtreeNodes, function(i, m){
			 var $input = $('<input type="text" class="menuIds hidden" name="menuIds" />');
		     $input.val(m.id);
				_box.form.treeFrom.append($input);								
			});		
			IQB.save(_box.form.treeFrom, function(result) {
				_this.closeRoleAuthWin();
			});
		
		},
		
		closeUpdateOrganWin:function(){
			$('#update-organ-win').modal('hide');
		},
		
		saveUpdateOrganWin: function(){
			_box.form.treeFrom.attr('action',_this.config.action.updateOrganAuth);
			var records = _box.util.getCheckedRows();
			var roleId = records[0].id
			$('#roleId').val(roleId);
			var checkedtreeNodes = $.fn.zTree.getZTreeObj("organtree").getCheckedNodes();	
		    $('.menuIds').remove();
			$.each(checkedtreeNodes, function(i, m){
			 var $input = $('<input type="text" class="menuIds hidden" name="menuIds" />');
		     $input.val(m.id);
				_box.form.treeFrom.append($input);								
			});		
			IQB.save(_box.form.treeFrom, function(result) {
				_this.closeUpdateOrganWin();
			});
		},
		config : {
			action : {
			    insert : urls['rootUrl'] + '/hqSysStationRoleRest/insertSysStationRole',
				update : urls['rootUrl'] + '/hqSysStationRoleRest/updateHqSysStationRole',
			    getById : urls['rootUrl'] + '/sysStationRoleRest/getSysStationRoleById',
				remove : urls['rootUrl'] + '/hqSysStationRoleRest/deleteSysStationRole',
				updateRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/insertPurview',
				getRoleAuth : urls['rootUrl'] + '/stationRolePurviewRest/getSysRolePurview',
				getOrgInfo : urls['rootUrl'] + '/sysOrganizationRest/getAllOrgInfo',
				updateStationId:urls['rootUrl']+'/sysStationRoleRest/deleteSysUserStationId',
			    updateOrganAuth:urls['rootUrl']+'/hqSysStationOrganRest/insertSysStationOrgan',
			    getOrganAuth:urls['rootUrl']+'/hqSysStationOrganRest/getStationOrgan'
			
			},
			event : {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#search-stationStatus').val(null).trigger('change');
				},		
			   insert: function(){
				
				   $('#update-win-label').text('添加通用角色');
				   _box.handler.insert();
				   $('#update-isSuperadmin').val(1).trigger('change');
				   $('#update-stationStatus').val(1).trigger('change');
			   },
			   update:function(){
				       _box.handler.update(function(result){
					   _box.form.update.attr('action', _this.config.action.update);
						_box.form.update.form('load', result.iqbResult.result);
						$('#update-win-label').text('修改通用角色');
						
						$('#update-isSuperadmin').val(result.iqbResult.result.stationIsSuperadmin).trigger('change');
						
						$('#update-stationStatus').val(result.iqbResult.result.stationStatus).trigger('change');
						_box.win.update.modal({backdrop: 'static', keyboard: false, show: true});
				  
				     });
			   },
			},
			dataGrid : {
				url : urls['rootUrl'] + '/hqSysStationRoleRest/getHqStationRole'
			},			
			organTree: {// 菜单树参数
				container:'organtree',
				url: urls['rootUrl'] + '/sysOrganizationRest/getSysOrganization',
				setting : {
					data : {simpleData : {enable : true}},// 启用简单数据渲染
					check : {enable : true, chkStyle : 'checkbox'},//启用复选框
				     
		        }			
			},			
	     	menuTree : {// 菜单树参数
	     		container:'menuTree',
				url: urls['rootUrl'] + '/sysMenuRest/getSysOrganationMenu',
				queryParams: {},       
				setting : {
					data : {simpleData : {enable : true}},// 启用简单数据渲染
					check : {enable : true, chkStyle : 'checkbox'},//启用复选框
				     
		        }			
			}
		},
		
		
		init : function() {
			_box = new DataGrid(_this.config);
			_box.init();
		    $('#search-stationStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: '请选择状态', allowClear: true}).val(null).trigger("change");		
			$('#update-isSuperadmin').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#update-stationStatus').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});			
	
			$('#btn-role-auth').on('click', function(){_this.openRoleAuthWin();});
			$('#btn-auth-close').click(_this.closeRoleAuthWin);
			$('#btn-auth-save').click(_this.btnAuthSave);
			
			$('#btn-organ-auth').click(_this.openOrganAuthWin);
	    	$('#btn-organ-save').on('click',function(){_this.saveUpdateOrganWin();});
			$('#btn-organ-close').on('click',function(){_this.closeUpdateOrganWin();});
			
		}
	}
	   return _this;
   }();

    $(function() {
	IQB.user.init();
    });

