/**
 * @author Van
 * @Version: 1.0
 * @DateTime: 2012-11-11
 */
var DataGrid2 = function(config){
	
		// 工具函数
		var Util = {
			getCheckedRows: function(){	
				return Grid.datagrid2('getCheckedRows');		
			},
			getSelectedRows: function(){	
				return Util.getCheckedRows();		
			},
			checkSelect: function(rows){
				if(rows && rows.length > 0){
					return true;
				}else{
					IQB.alert('未选中行');
					return false;	
				}						
			},
			checkSelectOne: function(rows){
				if(!Util.checkSelect(rows)){
					return false;
				}
				if(rows.length == 1){
					return true;
				}
				IQB.alert('请选中单行');
				return false;
			}		
		};			
		
		// 自定义函数
		var Handler = {
			// 刷新
			refresh: function(callback){				
				if($.isFunction(callback)){
					callback();// 回调
				}else{
					Grid.datagrid2(dataGrid);
				}				
			},
			// 查询
			search: function(callback){				
				if($.isFunction(callback)){
					callback();// 回调
				}else{
					if(Form.search.form('validate')){
						$.each(Form.search.find('input[type = "text"]'), function(i, n){
							var value = $.trim($(n).val());
							$(n).val(value);
						});
						$.extend(dataGrid.queryParams, Form.search.serializeObject(), {pageNum: 1});
						Evt.refresh();
					}	
				}
			},		
			// 重置
			reset: function(callback){			
				if($.isFunction(callback)){
					callback();// 回调
				}else{
					Form.search.form('reset');
				}
			},		
			// 新增
			insert: function(callback){					
				if($.isFunction(callback)){
					callback();// 回调
				}else{					
					Form.update.prop('action', Action.insert);
					Form.update.form('reset');
					Win.update.modal({backdrop: 'static', keyboard: false, show: true});
				}
			},
			// 修改
			update: function(callback){
				var rows = Util.getSelectedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idField = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idField];
			    	//option[idField] = parseInt(idValue, 10);
			    	option[idField] = idValue;
			    	IQB.getById(Action.getById, option, function(result){							
						if($.isFunction(callback)){
							callback(result);// 回调
						}else{
							Form.update.prop('action', Action.update);
							Form.update.form('load', result.iqbResult.result);							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});
						}
					});
				}
			},
			// 修改二
			updateForm: function(callback){
				var rows = Util.getSelectedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idField = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idField];
			    	//option[idField] = parseInt(idValue, 10);
			    	option[idField] = idValue;
			    	IQB.getById(Action.getById, option, function(result){	
						Form.update.prop('action', Action.update);
						Form.update.form('load', result.iqbResult.result);	
						if($.isFunction(callback)){
							callback(result);// 回调
						}
						Win.update.modal({backdrop: 'static', keyboard: false, show: true});
					});
				}
			},
			// 修改三
			updateAsset: function(callback){
				var rows = Util.getSelectedRows();
				$('#assetDueDate').val(rows[0].assetDueDate);
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idField = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idField];
			    	//option[idField] = parseInt(idValue, 10);
			    	option[idField] = idValue;
			    	IQB.getById(Action.getById, option, function(result){
							Form.update.prop('action', Action.getById);
							
							Form.update.form('load', result.iqbResult.result);							
							Form.proForm.form('load', result.iqbResult.result);
							Form.orderForm.form('load', result.iqbResult.result);
							var arr = Object.keys(result.iqbResult.customerInfo);
							var arrObject = result.iqbResult.customerInfo;
							if(arr.length>0){
								for(i=0;i<arr.length;i++){
									$("#creditName").append("<option value='"+result.iqbResult.customerInfo[i].flagId+"'>"+result.iqbResult.customerInfo[i].creditorName+"</option>");
								}
								$('#creditCardNo').val(arrObject[0].creditorIdNo);
								$('#creditBankCard').val(arrObject[0].creditorBankNo);
								$('#creditBank').val(arrObject[0].creditorBankName);
								$('#creditPhone').val(arrObject[0].creditorPhone);
								$("#creditName").unbind('change').on('change',function(){
									var key = $("#creditName").val();
									$('#creditCardNo').val(arrObject[key].creditorIdNo);
									$('#creditBankCard').val(arrObject[key].creditorBankNo);
									$('#creditBank').val(arrObject[key].creditorBankName);
									$('#creditPhone').val(arrObject[key].creditorPhone);
								});
								var tableResult = result.iqbResult.result;
								$('#preAmount').text(Formatter.money(tableResult.preAmount));
								$('#downPayment').text(Formatter.money(tableResult.downPayment));
								$('#margin').text(Formatter.money(tableResult.margin));
								$('#serviceFee').text(Formatter.money(tableResult.serviceFee));
								$('#feeAmount').text(Formatter.money(tableResult.feeAmount));
								$('#orderItems').text(tableResult.orderItems);
								$('#monthInterest').text(Formatter.money(tableResult.monthInterest));
								$('#orderTimes').val(Formatter.time(tableResult.orderTime));
							};
							var tableResult = result.iqbResult.result;
							$('#preAmount').text(Formatter.money(tableResult.preAmount));
							$('#downPayment').text(Formatter.money(tableResult.downPayment));
							$('#margin').text(Formatter.money(tableResult.margin));
							$('#serviceFee').text(Formatter.money(tableResult.serviceFee));
							$('#feeAmount').text(Formatter.money(tableResult.feeAmount));
							$('#orderItems').text(tableResult.orderItems);
							$('#monthInterest').text(Formatter.money(tableResult.monthInterest));
							$('#orderTimes').val(Formatter.time(tableResult.orderTime));
							//回显推标方式及其他附带
							/*$('#amtWay').val(tableResult.pushMode);
							if(tableResult.pushMode != '' && tableResult.pushMode != undefined && tableResult.pushMode != null){
								if(tableResult.pushMode == '1'){
									//按订单金额
									$('.amtWayShow').hide();
									$('#currentItem').val('');
									$('.sborderAmt').val($('#orderAmt').val());
								}else if(tableResult.pushMode == '2'){
									//按剩余未还本金
									$('.amtWayShow').show();
									$('#currentItem').val(tableResult.curRepayNo);
									$('.sborderAmt').val(tableResult.sbAmt);
								}
							}*/
							if($.isFunction(callback)){
								callback(result.iqbResult.result.orderId, result.iqbResult.result.leftitems);// 回调
							}					
							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});						
					});
				}
			},
			// 修改四
			updateAsset1: function(callback){
				var rows = Util.getSelectedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idField = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idField];
			    	//option[idField] = parseInt(idValue, 10);
			    	option[idField] = idValue;
			    	IQB.getById(Action.getById, option, function(result){		
							Form.update.prop('action', Action.getById);
							Form.update.form('load', result.iqbResult.result);
							Form.proForm.form('load', result.iqbResult.result);
							Form.orderForm.form('load', result.iqbResult.result);
							$('#orderDate').val(Formatter.timeCfm2(result.iqbResult.result.orderDate));
							if($.isFunction(callback)){
								callback(result.iqbResult.result.orderNo, result.iqbResult.result.leftitems);// 回调
							}					
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});						
					});
				}
			},
			// 修改7
			updateAsset4: function(callback){
				var rows = Util.getSelectedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idField = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idField];
			    	//option[idField] = parseInt(idValue, 10);
			    	option[idField] = idValue;
			    	IQB.getById(Action.getById, option, function(result){
							Form.update.prop('action', Action.getById);
							
							Form.update.form('load', result.iqbResult.result);							
							Form.proForm.form('load', result.iqbResult.result);
							Form.orderForm.form('load', result.iqbResult.result);
							$('#creditCardNo').val(result.iqbResult.result.creditCardNo);
							$('#creditBankCard').val(result.iqbResult.result.creditBankCard);
							$('#creditBank').val(result.iqbResult.result.creditBank);
							$('#creditPhone').val(result.iqbResult.result.creditPhone);
							var tableResult = result.iqbResult.result;
							$('#preAmount').text(Formatter.money(tableResult.preAmount));
							$('#downPayment').text(Formatter.money(tableResult.downPayment));
							$('#margin').text(Formatter.money(tableResult.margin));
							$('#serviceFee').text(Formatter.money(tableResult.serviceFee));
							$('#feeAmount').text(Formatter.money(tableResult.feeAmount));
							$('#orderItems').text(tableResult.orderItems);
							$('#monthInterest').text(Formatter.money(tableResult.monthInterest));
							$('#orderTimes').val(Formatter.time(tableResult.orderTime));
							if($.isFunction(callback)){
								callback(result.iqbResult.result.orderId, result.iqbResult.result.leftitems);// 回调
							}					
							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});						
					});
				}
			},
			// 删除
			remove: function(callback){
				var rows = Util.getSelectedRows();				
				if(Util.checkSelectOne(rows)){
					IQB.confirm('确认要删除吗？', function(){
						var option = {};
				    	var idField = Grid.datagrid2('getIdField'); 
				    	var idValue = rows[0][idField];
				    	//option[idField] = parseInt(idValue, 10);
				    	option[idField] = idValue;
				   		IQB.remove(Action.remove, option, function(result){								
							if($.isFunction(callback)){
								callback(result);// 回调
							}else{
								Evt.refresh();
							}
				   		});
					}, function(){});										
				}
			},
			// 保存
			save: function(callback){
				IQB.save(Form.update, function(result){					
					if($.isFunction(callback)){
						callback(result);// 回调
					}else{
						// 禁用表单验证,处理页面遗留toolTip
						Form.update.form('disableValidation');						
						Evt.close();
						Form.update.form('reset');
						// 启用表单验证
					    Form.update.form('enableValidation');					    
					    Evt.refresh();					    
					}								    
				});
			},
			// 关闭
			close: function (callback){					
				if($.isFunction(callback)){
					callback();// 回调
				}else{
					Win.update.modal('hide');
				}			
			}
		};		
		
		config = config || {};	
		var actionUrl = config.action || {};
		// URL
		var Action = {
				'insert': actionUrl.insert || 'insert',
				'update': actionUrl.update || 'update',
				'getById': actionUrl.getById || 'getById',
				'remove': actionUrl.remove || 'remove'
		};
		var evt = config.event || {};
		// 函数
		var Evt = {
			refresh: evt.refresh || Handler.refresh,
			search: evt.search || Handler.search,
			reset: evt.reset || Handler.reset,
			insert: evt.insert || Handler.insert,
			update: evt.update || Handler.update,		
			remove: evt.remove || Handler.remove,
			save: evt.save || Handler.save,
			close: evt.close ||  Handler.close,
			updateForm:evt.updateForm || Handler.updateForm,
			updateAsset:evt.updateAsset || Handler.updateAsset
		};		
		var dataGrid = config.dataGrid || {};	
		// 表格(TODO)
		var Grid = $('#datagrid');			
		// 表单	
		var Form = {
			search: $('#searchForm'),
			list: $('#listForm'),
			update: $('#updateForm'),
			treeFrom: $('#treeFrom'),
			orderForm: $('#orderForm'),
			proForm: $('#proForm')
		};
		// 窗口
		var Win = {
			update: $('#update-win')
		};	
		
		// 初始化表格
		var initGrid = function(){	
			dataGrid.queryParams = dataGrid.queryParams || {};
			Grid.datagrid2(dataGrid);	
		}		
		
		// 初始化表单
		var initForm = function(){			
			if(Form.search && Form.search[0]){				
				Form.search.find('#btn-search').off('click').on('click', Evt.search);//查询
				Form.search.find('#btn-reset').off('click').on('click', Evt.reset);//重置
			}			
			if(Form.list && Form.list[0]){
				Form.list.find('#btn-insert').off('click').on('click', Evt.insert);//添加
				Form.list.find('#btn-update').off('click').on('click', Evt.update);//修改
				Form.list.find('#btn-remove').off('click').on('click', Evt.remove);//删除
			}
		};
		
		// 初始化窗口
		var initWin = function(){
			if(Win.update && Win.update[0]){
				Win.update.find('#btn-save').off('click').on('click', Evt.save); //保存
				Win.update.find('#btn-close').off('click').on('click', Evt.close);//关闭
			}
		}
		
		// this	
		this.util = Util;
		this.handler = Handler;		
		this.grid = Grid;
		this.form = Form;
		this.win = Win;
		
		this.init = function(){
			initGrid();
			initForm();
			initWin();
		};
		
		return this;
};



// 创建闭包    
(function($){    
  // 插件定义    
  $.fn.datagrid2 = function(){ 
	  var gird = $(this);
	  if(arguments.length > 0){
		  if(typeof(arguments[0]) == 'object'){
			  var options = $.extend({}, $.fn.datagrid2.defaults, arguments[0]); 
			  if(options.pagination){
				  options.queryParams.pageNum = options.queryParams.pageNum || 1;
				  options.queryParams.pageSize = options.queryParams.pageSize || 10;
			  }
			  // 分页
			  IQB.post(options.url, options.queryParams, function(result){
				  // 结果集
				  if(result.iqbResult.result != '' && result.iqbResult.result != null){
					  var list = result.iqbResult.result.list;
					  if(list != null && list.length > 0){
						  var _html = '';		
						  var total = result.iqbResult.result.total || list.length;
						  var startRowNum = result.iqbResult.result.startRow || 1;
						  var cols = gird.find('thead').find('th');	
						  $.each(list, function(rowIndex, row){
							  var tr_html = '<tr>', td_html = '';
							  var rowNum = startRowNum + rowIndex;						  				
							  $.each(cols, function(colIndex, col){	
								  var field = $(col).attr('field');
								  var html = '', hiddenClass = '', alignClass = '', style = '';								
								  var isHidden = $(col).attr('hidden') || false;
								  if(isHidden){hiddenClass = 'hidden';}	
								  alignClass = $(col).attr('align') || 'text-left';		
								  var formatter = $(col).attr('formatter');	
								  var styler = $(col).attr('styler');
								  if(field == 'rn'){
									  var val = rowNum;
									  html = val;								  							
									  if(formatter){html = eval(formatter);}
									  if(styler){style = eval(styler);}
									  html = '<td class="' + hiddenClass + ' ' + alignClass + '">' + html + '</td>';
									  tr_html = '<tr style="' + style + '">';
								  }else if(field == 'ck'){
									  var val = '<input class="datagrid-row-checkbox" type="checkbox" />';
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
					  	  });
						  gird.find('tbody').empty().append(_html);	
						  if(options.singleCheck){
							  gird.find('tbody').find('.datagrid-row-checkbox').each(function(i){
								  $(this).off('mousedown').on('mousedown', function(e){
									  if(e.which == 1){
										  gird.datagrid2('uncheckAll');
									  }									  
								  });								  
							  });								 
						  }		
						  if(options.isExport){
							  var exportBtn = $('body').find('#btn-export');
							  if(exportBtn && exportBtn[0]){
								  exportBtn.first().prop('disabled', false);
								  exportBtn.first().off('click').on('click', function(e){
									  gird.gridExport(options);
								  });
							  }
						  }
						  if(options.pagination){
							  var paginationOption = {						
									 totalPages: result.iqbResult.result.pages,
									 currentPage: result.iqbResult.result.pageNum,
									 numberOfPages: 5,
									 itemTexts: function(type, page, current){
										  if(type == 'first'){return '首页';}else if(type == 'prev'){return '上一页';}else if(type == 'next'){return '下一页';}else if(type == 'last'){return '尾页';}else{return page;}
									 },
									 itemContainerClass: function(type, page, current) {
										 return (page === current) ? 'active' : 'pointer-cursor';		            	
									 },
									 onPageClicked: function(e, originalEvent, type, page){
										 e.stopImmediatePropagation();
										 options.queryParams.pageNum = page;
										 gird.datagrid2(options);
										 if($.isFunction(options.onPageChanged)){
											 options.onPageChanged(page);
										 }
									 }		          								
							  };				  
							  $('#' + options.paginator).bootstrapPaginator(paginationOption);
							  $('#' + options.paginator).removeClass('pagination').find('ul').addClass('pagination pagination-sm');
							  if(options.isExport){
								  var exportAllBtn = $('body').find('#btn-export-all');
								  if(exportAllBtn && exportAllBtn[0]){
									  exportAllBtn.first().prop('disabled', false);
									  exportAllBtn.first().off('click').on('click', function(e){
										  gird.gridExportAll(options, total);
									  });
								  }
							  }
						  }
						  if($.isFunction(options.loadSuccess)){
							  options.loadSuccess(result);
					  	  }
					  }else{
						  if(result.iqbResult.result.total){
							  options.queryParams.pageNum = options.queryParams.pageNum - 1;
							  gird.datagrid2(options);
						  }else{
							  gird.find('tbody').empty();
							  $('#' + options.paginator).empty();
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
				  }else{
					  gird.find('tbody').empty()
				  }
		  	  });			
		  }else if(typeof(arguments[0]) == 'string'){
			  if(arguments[0] == 'getIdField'){
				  var idField = 'id';
				  gird.find('thead').find('tr').find('th').each(function(i){
					  var isIdField = $(this).attr('idField') || false;
					  if(isIdField){
						  idField = $(this).attr('field');
					  }
				  });
				  return idField;
			  }else if(arguments[0] == 'getRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];					  
					  var row = {rn: arguments[1]};
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('font').each(function(i){
						  var field = $(this).parent().attr('field');
						  var val = $(this).text();
						  row[field] = val;		
					  }); 
					  return row;
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'getCheckedRows'){
				  var arry = new Array();	
				  var trs = gird.find('tbody').find('tr');
				  $.each(trs, function(i, m){
					  if($(m).find('.datagrid-row-checkbox').prop('checked')){
						  var fonts = $(m).find('font');
						  var row = {rn: i};
				    	  $.each(fonts, function(j, n){
				    			var field = $(n).parent().attr('field');
				    			var val = $(n).text();
				    			row[field] = val;						
						  });		    		
				    	  arry.push(row);
					  }; 
				  });	
				  return arry;
			  }else if(arguments[0] == 'checkRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', true); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'checkAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', true); 
				  }); 				 
			  }else if(arguments[0] == 'uncheckRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', false); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'uncheckAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', false); 
				  }); 
			  }// 继续扩展	  
		  }
	  }else{
		  console.error('参数类型错误');
	  }  
  };  
  // 插件默认选项 
  $.fn.datagrid2.defaults = {    
		  queryParams: {},// 参数
		  singleCheck: false,// 是否单选,默认false
		  pagination: true,// 是否分页,默认true
		  paginator: 'paginator',// 前端分页插件容器(仅在pagination为false,否则无效)
		  onPageChanged: null,
		  loadSuccess: null,
		  isExport: false, // 是否导出电子表格,默认false
		  filename: '%YY%-%MM%-%DD%-%hh%-%mm%-%ss%',// 导出电子表格名称
		  cols: ''// 指定导出的表格列(从1开始,如'1,2,5'表示第一列、第二列、第五列),默认导出表格全部列
		  
  };    
  //插件定义
  $.fn.datagrid3 = function(){ 
	  var gird = $(this);
	  if(arguments.length > 0){
		  if(typeof(arguments[0]) == 'object'){
			  var options = $.extend({}, $.fn.datagrid3.defaults, arguments[0]); 
			  if(options.pagination){
				  options.queryParams.pageNum = options.queryParams.pageNum || 1;
				  options.queryParams.pageSize = options.queryParams.pageSize || 10;
			  }
			  // 分页
			  IQB.postIfFail(options.url, options.queryParams, function(result){
				  if(result.iqbResult.result != null && result.iqbResult.result != ''){
				  // 结果集
				  var list = result.iqbResult.result.list;
				  if(list != null && list.length > 0){
					  var _html = '';		
					  var total = result.iqbResult.result.total || list.length;
					  var startRowNum = result.iqbResult.result.startRow || 1;
					  var cols = gird.find('thead').find('th');	
					  $.each(list, function(rowIndex, row){
						  var tr_html = '<tr>', td_html = '';
						  var rowNum = startRowNum + rowIndex;						  				
						  $.each(cols, function(colIndex, col){	
							  var field = $(col).attr('field');
							  var html = '', hiddenClass = '', alignClass = '', style = '';								
							  var isHidden = $(col).attr('hidden') || false;
							  if(isHidden){hiddenClass = 'hidden';}	
							  alignClass = $(col).attr('align') || 'text-left';		
							  var formatter = $(col).attr('formatter');	
							  var styler = $(col).attr('styler');
							  if(field == 'rn'){
								  var val = rowNum;
								  html = val;								  							
								  if(formatter){html = eval(formatter);}
								  if(styler){style = eval(styler);}
								  html = '<td class="' + hiddenClass + ' ' + alignClass + '">' + html + '</td>';
								  tr_html = '<tr style="' + style + '">';
							  }else if(field == 'ck'){
								  var val = '<input class="datagrid-row-checkbox" type="checkbox" />';
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
				  	  });
					  gird.find('tbody').empty().append(_html);	
					  if(options.singleCheck){
						  gird.find('tbody').find('.datagrid-row-checkbox').each(function(i){
							  $(this).off('mousedown').on('mousedown', function(e){
								  if(e.which == 1){
									  gird.datagrid3('uncheckAll');
								  }									  
							  });								  
						  });								 
					  }		
					  if(options.isExport){
						  var exportBtn = $('body').find('#btn-export');
						  if(exportBtn && exportBtn[0]){
							  exportBtn.first().prop('disabled', false);
							  exportBtn.first().off('click').on('click', function(e){
								  gird.gridExport(options);
							  });
						  }
					  }
					  if(options.pagination){
						  var paginationOption = {						
								 totalPages: result.iqbResult.result.pages,
								 currentPage: result.iqbResult.result.pageNum,
								 numberOfPages: 5,
								 itemTexts: function(type, page, current){
									  if(type == 'first'){return '首页';}else if(type == 'prev'){return '上一页';}else if(type == 'next'){return '下一页';}else if(type == 'last'){return '尾页';}else{return page;}
								 },
								 itemContainerClass: function(type, page, current) {
									 return (page === current) ? 'active' : 'pointer-cursor';		            	
								 },
								 onPageClicked: function(e, originalEvent, type, page){
									 e.stopImmediatePropagation();
									 options.queryParams.pageNum = page;
									 gird.datagrid3(options);
									 if($.isFunction(options.onPageChanged)){
										 options.onPageChanged(page);
									 }
								 }		          								
						  };				  
						  $('#' + options.paginator).bootstrapPaginator(paginationOption);
						  $('#' + options.paginator).removeClass('pagination').find('ul').addClass('pagination pagination-sm');
						  if(options.isExport){
							  var exportAllBtn = $('body').find('#btn-export-all');
							  if(exportAllBtn && exportAllBtn[0]){
								  exportAllBtn.first().prop('disabled', false);
								  exportAllBtn.first().off('click').on('click', function(e){
									  gird.gridExportAll(options, total);
								  });
							  }
						  }
					  }
					  if($.isFunction(options.loadSuccess)){
						  options.loadSuccess(result);
				  	  }
				  }else{
					  if(result.iqbResult.result.total){
						  options.queryParams.pageNum = options.queryParams.pageNum - 1;
						  gird.datagrid3(options);
					  }else{
						  gird.find('tbody').empty();
						  $('#' + options.paginator).empty();
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
			  }
		  	  });			
		  }else if(typeof(arguments[0]) == 'string'){
			  if(arguments[0] == 'getIdField'){
				  var idField = 'id';
				  gird.find('thead').find('tr').find('th').each(function(i){
					  var isIdField = $(this).attr('idField') || false;
					  if(isIdField){
						  idField = $(this).attr('field');
					  }
				  });
				  return idField;
			  }else if(arguments[0] == 'getRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];					  
					  var row = {rn: arguments[1]};
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('font').each(function(i){
						  var field = $(this).parent().attr('field');
						  var val = $(this).text();
						  row[field] = val;		
					  }); 
					  return row;
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'getCheckedRows'){
				  var arry = new Array();	
				  var trs = gird.find('tbody').find('tr');
				  $.each(trs, function(i, m){
					  if($(m).find('.datagrid-row-checkbox').prop('checked')){
						  var fonts = $(m).find('font');
						  var row = {rn: i};
				    	  $.each(fonts, function(j, n){
				    			var field = $(n).parent().attr('field');
				    			var val = $(n).text();
				    			row[field] = val;						
						  });		    		
				    	  arry.push(row);
					  }; 
				  });	
				  return arry;
			  }else if(arguments[0] == 'checkRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', true); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'checkAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', true); 
				  }); 				 
			  }else if(arguments[0] == 'uncheckRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', false); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'uncheckAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', false); 
				  }); 
			  }// 继续扩展	  
		  }
	  }else{
		  console.error('参数类型错误');
	  }  
  };  
  // 插件默认选项 
  $.fn.datagrid3.defaults = {    
		  queryParams: {},// 参数
		  singleCheck: false,// 是否单选,默认false
		  pagination: true,// 是否分页,默认true
		  paginator: 'paginator',// 前端分页插件容器(仅在pagination为false,否则无效)
		  onPageChanged: null,
		  loadSuccess: null,
		  isExport: false, // 是否导出电子表格,默认false
		  filename: '%YY%-%MM%-%DD%-%hh%-%mm%-%ss%',// 导出电子表格名称
		  cols: ''// 指定导出的表格列(从1开始,如'1,2,5'表示第一列、第二列、第五列),默认导出表格全部列
		  
  };
// 闭包结束    
})(jQuery);     


(function ($) {

    $.fn.gridExport = function(){

    	var $this = $(this);
    	
        var defaults = {
          filename: 'grid',
          format: 'xls',
          cols: ''
        };

        var opts = $.extend(defaults, arguments[0]);
        var cols = opts.cols ? opts.cols.split(',') : []; 
        var data_type = {xls: 'application/vnd.ms-excel'};
        var result = '';

        function getHeaders(){
            var tr = $this.find('thead').first().find('tr').first();
            var arr = [];
            if(cols.length){
            	$.each(cols, function(i, m){
        			arr.push($(tr).find('th:nth-child(' + m + ')').text());
        		});
            }else{
            	var ths = $(tr).find('th');
        		$.each(ths, function(i, m){
        			arr.push($(m).text());
        		});        		
            }
            return arr;
        }

        function getItems(){
            var trs = $this.find('tbody').first().find('tr');
            var arr = [];
            $.each(trs, function(i, m){
            	var s = [];
            	if(cols.length){
            		$.each(cols, function(j, n){
            			var val = $(m).find('td:nth-child(' + n + ')').find('font').first().text();
            			var text = $(m).find('td:nth-child(' + n + ')').text();
            			if(val === ''){
            				s.push(text);
            			}else{
            				val += '';
            				text += '';
            				text = text.substring(val.length);
            				s.push(text);
            			}
            			
            		});
            	}else{
            		var tds = $(m).find('td');
            		$.each(tds, function(j, n){
            			var val = $(n).find('font').first().text();
            			var text = $(n).text();
            			if(val === ''){
            				s.push(text);
            			}else{
            				val += '';
            				text += '';
            				text = text.substring(val.length);
            				s.push(text);
            			}            			
            		});
            	}
            	arr.push(s);
            });
            return arr;
        }

        function download(data, filename, format){
            var a = document.createElement('a');
            a.href = URL.createObjectURL(new Blob([data], {type: data_type[format]}));

            var now = new Date();
            var time_arr = [
                'DD:'+now.getDate(),
                'MM:'+ (now.getMonth() + 1),
                'YY:'+now.getFullYear(),
                'hh:'+now.getHours(),
                'mm:'+now.getMinutes(),
                'ss:'+now.getSeconds()
            ];

            for(var i = 0; i < time_arr.length; i++){
                var key = time_arr[i].split(':')[0];
                var val = time_arr[i].split(':')[1];
                filename = filename.replace( '%' + key + '%', val);
            }

            a.download = filename + '.' + format;
            a.click();
            a.remove();
        }
        
        switch (opts.format) {
            case 'xls':
                var headers = getHeaders();
                var items  = getItems();
                var template = '<table><thead>%thead%</thead><tbody>%tbody%</tbody></table>';

                var res = '';
                $.each(headers, function(i, m){
                	res += '<th>' + m + '</th>' ;
                });
                template = template.replace('%thead%', res);

                res = '';
                $.each(items, function(i, item){
                	res += '<tr>';
                	$.each(item, function(j, td){
                		 res += '<td>' + td + '</td>' ;
                	});
                    res += '</tr>';
                });
                template = template.replace('%tbody%', res);

                result = template;
            break;  
        }

        download(result, opts.filename, opts.format);

    }

}(jQuery));

(function ($) {

    $.fn.gridExportAll = function(){
    	
    	
    	var $this = $(this);
    	
    	var defaults = {
          filename: 'grid',
          format: 'xls',
          cols: ''
        };
    	
    	var opts = $.extend(defaults, arguments[0]);	
    	opts.queryParams.pageSize = arguments[1];
    	var cols = opts.cols ? opts.cols.split(',') : []; 
        var data_type = {xls: 'application/vnd.ms-excel'};
        var data = '';
		
		function getHeaders(){
            var tr = $this.find('thead').first().find('tr').first();
            var arr = [];
            if(cols.length){
            	$.each(cols, function(i, m){
        			arr.push($(tr).find('th:nth-child(' + m + ')').text());
        		});
            }else{
            	var ths = $(tr).find('th');
        		$.each(ths, function(i, m){
        			arr.push($(m).text());
        		});        		
            }
            return arr;
        }
		
		function getColumns(){
            var tr = $this.find('thead').first().find('tr').first();
            var arr = [];
            if(cols.length){
            	$.each(cols, function(i, m){
        			arr.push($(tr).find('th:nth-child(' + m + ')'));
        		});
            }else{
            	var ths = $(tr).find('th');
        		$.each(ths, function(i, m){
        			arr.push($(m));
        		});        		
            }
            return arr;
        }

        function getItems(result){
        	var columns = getColumns();
        	var arr = [];
            if(result.iqbResult.result.list != null && result.iqbResult.result.list.length > 0){            	
            	$.each(result.iqbResult.result.list, function(rowIndex, row){
  				  var s = [];
  				  var rowNum = result.iqbResult.result.startRow + rowIndex;						  				
  				  $.each(columns, function(colIndex, col){	
  					  var html = '';
  					  var property = $(col).attr('field');
  					  if(property == 'rn'){
  						  var val = rowNum;
  						  html = val;
  						  var formatter = $(col).attr('formatter');								
  						  if(formatter){html = eval(formatter);}	
  						  s.push(html);
  					  }else if(property == 'ck'){
  						  s.push('');
  					  }else{								
  						  var val = row[property];
  						  if(val == null){val = '';}
  						  html = val;
  						  var formatter = $(col).attr('formatter');								
  						  if(formatter){html = eval(formatter);}	
  						  s.push(html);										
  					  }
  				  });
  				  arr.push(s);
  		  	  	});
            	return arr;
            }else{
            	return arr;
            }
        }
        
        function download(data, filename, format){
            var a = document.createElement('a');
            a.href = URL.createObjectURL(new Blob([data], {type: data_type[format]}));

            var now = new Date();
            var time_arr = [
                'DD:'+now.getDate(),
                'MM:'+ (now.getMonth() + 1),
                'YY:'+now.getFullYear(),
                'hh:'+now.getHours(),
                'mm:'+now.getMinutes(),
                'ss:'+now.getSeconds()
            ];

            for(var i = 0; i < time_arr.length; i++){
                var key = time_arr[i].split(':')[0];
                var val = time_arr[i].split(':')[1];
                filename = filename.replace( '%' + key + '%', val);
            }

            a.download = filename + '.' + format;
            a.click();
            a.remove();
        }
        
        

        // 分页
		IQB.post(opts.url, opts.queryParams, function(result){
			
			opts.queryParams.pageSize = null;
			
			switch (opts.format) {
	            case 'xls':
	                var headers = getHeaders();
	                var items  = getItems(result);
	                var template = '<table><thead>%thead%</thead><tbody>%tbody%</tbody></table>';
	
	                var res = '';
	                $.each(headers, function(i, m){
	                	res += '<th>' + m + '</th>' ;
	                });
	                template = template.replace('%thead%', res);
	
	                res = '';
	                $.each(items, function(i, item){
	                	res += '<tr>';
	                	$.each(item, function(j, td){
	                		 res += '<td>' + td + '</td>' ;
	                	});
	                    res += '</tr>';
	                });
	                template = template.replace('%tbody%', res);
	
	                data = template;
	            break;  
	        }
			
			download(data, opts.filename, opts.format);
			
		});

    }

}(jQuery));