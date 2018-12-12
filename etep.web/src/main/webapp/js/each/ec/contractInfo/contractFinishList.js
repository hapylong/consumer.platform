$package('IQB.contractFinishList');
IQB.contractFinishList = function() {
	var _box = null;
	var _this = {
		config : {
			dataGrid: {
				url: urls['rootUrl'] + '/contract/contractFinish',
				singleCheck: true
			},
			action : {
				selContract : urls['rootUrl'] + '/contract/unIntcpt-selContracts',
			}
		},
		contractInfoWin:function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				_this.girdWin('#contractInfoid','#contractInfoShow-win','#contractPaginator','dz');
			}
		},
		contractInfoDown:function(){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				_this.girdWin('#contractDownid','#contractInfoDown-win','#contractDownPaginator','zz');
			}
		},
		girdWin:function(org1,org2,paginator,type){
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				var gird = $(org1);
				var option = {};
				option['bizId'] = records[0].orderId;
				option['bizType'] = records[0].bizType;
				option['orgCode'] = records[0].orgCode;
				// 分页
				  IQB.post(_this.config.action.selContract, option, function(result){
					  // 结果集
					  var list = result.iqbResult.result.ecList;
					  if(list != null && list.length > 0){
						  var _html = '';		
						  var total = result.iqbResult.result.total || list.length;
						  var startRowNum = result.iqbResult.result.startRow || 1;
						  var cols = gird.find('thead').find('th');	
						  var num=0
						  $.each(list, function(rowIndex, row){
							  var rowNum = startRowNum+rowIndex;	
							  if(row.ecType==type){
								  num=num+1;
								  var tr_html = '<tr>', td_html = '';
								  $.each(cols, function(colIndex, col){	
									  var field = $(col).attr('field');
									  var html = '', hiddenClass = '', alignClass = '', style = '';								
									  var isHidden = $(col).attr('hidden') || false;
									  if(isHidden){hiddenClass = 'hidden';}	
									  alignClass = $(col).attr('align') || 'text-left';		
									  var formatter = $(col).attr('formatter');	
									  var styler = $(col).attr('styler');
									  if(field == 'rn'){
										  var val = num;
										  html = val;								  							
										  if(formatter){html = eval(formatter);}
										  if(styler){style = eval(styler);}
										  html = '<td class="' + hiddenClass + ' ' + alignClass + '">' + html + '</td>';
										  tr_html = '<tr style="' + style + '">';
									  }else if(field == 'ck'){
										  var val = "";
										  if("zz" == row.ecType){
											  val = '<input class="datagrid-row-checkbox" type="checkbox" name="'+row.ecCode+'" value="/uploadData'+row.ecDownloadUrl+'" />';
										  }else{
											  val = '<input class="datagrid-row-checkbox" type="checkbox"/>';
										  }
										  html = val;
										  if(formatter){html = eval(formatter);}	
										  if(styler){style = eval(styler);}
										  html = '<td class="' + hiddenClass + ' ' + alignClass + '">' + html + '</td>';
										  tr_html = '<tr style="' + style + '">';
									  }else{								
										  var val = row[field];
										  // TODO(屏蔽了空值)
										  if(val == null){val = '';}
										  html = val;
										  if(formatter){html = eval(formatter);}
										  if(styler){style = eval(styler);}
										  html = '<td field="' + field + '" class="' + hiddenClass + ' ' + alignClass + '"><font class="hidden">' + val + '</font>' + html + '</td>';
										  tr_html = '<tr style="' + style + '">';
									  }
									  td_html = td_html + html;
								  });
								  _html = _html + tr_html + td_html + '</tr>';
							  }
						  });
						  gird.find('tbody').empty().append(_html);	
					  }else{
						  if(result.iqbResult.result.total){
							  options.queryParams.pageNum = options.queryParams.pageNum - 1;
							  gird.datagrid2(options);
						  }else{
							  gird.find('tbody').empty();
							  $(paginator).empty();
							  if(options.isExport){
								  var exportBtn = $('body').find('#btn-export');							  
								  if(exportBtn && exportBtn[0]){
									  exportBtn.first().prop('disabled', true);
								  }						
								  var exportAllBtn = $('body').find('#btn-export-all');
								  if(exportAllBtn && exportAllBtn[0]){
									  exportAllBtn.first().prop('disabled', true);
								  }
							  }
							  if($.isFunction(options.loadSuccess)){
								  options.loadSuccess(result);
						  	  }
						  }
					  }
			  	  });
				
				$(org2).modal({backdrop: 'static', keyboard: false, show: true});
			}
		},
		showButton: function(val, row, rowIndex){
			var ecType = row.ecType;
			if("zz" == ecType){
				var ecDownloadUrl = row.ecDownloadUrl;
				if(ecDownloadUrl!=null&&ecDownloadUrl!=''){
					return '<a class="text-stress" href="/uploadData'+ecDownloadUrl+'" target="_blank">下载</a>';
				}
			}else{
				var ecViewUrl = row.ecViewUrl;
				if(ecViewUrl!=null&&ecViewUrl!=''){
					return '<a class="text-stress" href="'+ecViewUrl+'" target="_blank">预览</a>';
				}else{
					return '';
				}
			}
		},
		downWin:function(){
			var form = document.getElementById("downForm");
			var elements = new Array();
			var tagElements = form.getElementsByTagName('input');
			for (var j = 0; j < tagElements.length; j++) {
				if(tagElements[j].checked==true){
					elements.push(tagElements[j]);
				}
			}
			if(elements.length>0){
				for (var i = 0; i < elements.length; i++){ 
					var element = elements[i];
					switch (element.type.toLowerCase()) {
					case 'submit':
					case 'hidden':
					case 'password':
					case 'text':
					case 'radio':
					case 'checkbox':window.open (element.value,element.name,'_blank');
					}
				}
			}else{
				IQB.alert('未选中行');
			}
		},
		close: function(){
			$('#contractInfoShow-win').modal('hide');
		},
		closeDow: function(){
			$('#contractInfoDown-win').modal('hide');
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			
			$('#btn-contractInfoShow').on('click', function(){
				_this.contractInfoWin();
			});
			$('#btn-close').on('click', function(e){_this.close();});
			$('#btn-close-down').on('click', function(e){_this.closeDow();});
			$('#btn-contractInfoDown').on('click', function(){
				_this.contractInfoDown();
			});
			
			$('#btn-down').on('click', function(){
				_this.downWin();
			});
		}
		
	}
	return _this;
}();

$(function() {
	IQB.contractFinishList.init();
	datepicker(startDateStart);
	datepicker(startDateEnd);
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