/**
 * @author Van
 * @Version: 1.0
 * @DateTime: 2012-11-11
 */
var DataGrid = function(config){
	
		//工具函数
		var Util = {
			getCheckedRows: function(){	
				var rows = $('td input:checked');
				var columns = $('thead th');
				var rowArr = new Array();					
		    	$.each(rows, function(i, m){
		    		var row = {};
		    		$.each(columns, function(j, n){
		    			if(j > 0){
		    				var field = $(n).attr('field');
		    				var val = $(m).parent().parent().find("[field = '" + field + "']").find('.original-value').text();
				    		row[field] = val;
		    			}						
					});		    		
		    		rowArr.push(row);
		    	});			
				return rowArr;			
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
			},			
		};			
		
		//自定义函数
		var Handler = {
			//刷新
			refresh: function(callback){				
				if($.isFunction(callback)){
					callback();//回调
				}else{
					initGrid();
				}				
			},
			//查询
			search: function(callback){				
				if($.isFunction(callback)){
					callback();//回调
				}else{
					$.each(Form.search.find('input[type = "text"]'), function(index, input){
						var value = $.trim($(input).val());
						$(input).val(value);
					});
					var param = Form.search.serializeObject();
					$.extend(dataGrid.queryParams, param, {pageNum: 1});
					Evt.refresh();
				}	
			},		
			//重置
			reset: function(callback){			
				if($.isFunction(callback)){
					callback();//回调
				}else{
					Form.search.form('reset');
				}
			},		
			//新增
			insert: function(callback){					
				if($.isFunction(callback)){
					callback();//回调
				}else{					
					Form.update.attr('action',Action.insert);
					Form.update.form('reset');
					Win.update.modal({backdrop: 'static', keyboard: false, show: true});
				}
			},
			//修改
			update: function(callback){
				var records = Util.getCheckedRows();
				if (Util.checkSelectOne(records)){
					var option = {};
			    	var idKey = dataGrid.idField || 'id'; 
			    	var idValue = records[0][idKey];
			    	option[idKey] = parseInt(idValue, 10);
			    	IQB.getById(Action.getById, option, function(result){							
						if($.isFunction(callback)){
							callback(result);//回调
						}else{
							Form.update.attr('action',Action.update);
							Form.update.form('load',result.iqbResult.result);							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});
						}
					});
				}
			},
			//删除函数
			remove: function(callback){
				var records = Util.getCheckedRows();				
				if (Util.checkSelectOne(records)){
					IQB.confirm('确认要删除吗？', function(){
						var option = {};
				    	var idKey = dataGrid.idField || 'id'; 
				    	var idValue = records[0][idKey];
				    	option[idKey] = parseInt(idValue, 10);
				   		IQB.remove(Action.remove, option, function(result){								
							if($.isFunction(callback)){
								callback(result);//回调
							}else{
								if(dataGrid.queryParams.pageNum > 1 && dataGrid.queryParams.pageRowNum == 0){
					   				dataGrid.queryParams.pageNum = dataGrid.queryParams.pageNum - 1;
					   			}
								Evt.refresh();
							}
				   		});
					}, function(){});										
				}
			},
			//保存函数
			save: function(callback){
				IQB.save(Form.update, function(result){					
					if($.isFunction(callback)){
						callback(result);//回调
					}else{						
						Evt.close();
					    Evt.refresh();
					    Form.update.form('disableValidation');
					    Form.update.form('reset');
					    Form.update.form('enableValidation');
					}								    
				});
			},
			//关闭函数
			close: function (callback){					
				if($.isFunction(callback)){
					callback();//回调
				}else{
					Win.update.modal('hide');
				}			
			}
		};		
		
		//传入参数
		config = config || {};	
		//URL参数
		var actionUrl = config.action || {};
		//URL
		var Action = {
				'insert': actionUrl.insert || 'insert',
				'update': actionUrl.update || 'update',
				'getById': actionUrl.getById || 'getById',
				'remove': actionUrl.remove || 'remove'
		};
		//函数参数
		var evt = config.event || {};
		//函数
		var Evt = {
			refresh: evt.refresh || Handler.refresh,
			search: evt.search || Handler.search,
			reset: evt.reset || Handler.reset,
			insert: evt.insert || Handler.insert,
			update: evt.update || Handler.update,		
			remove: evt.remove || Handler.remove,
			save: evt.save || Handler.save,
			close: evt.close ||  Handler.close
		};		
		//表格参数
		var dataGrid = config.dataGrid || {};	
		//表格
		var Grid = null;			
		//表单	
		var Form = {
				search: $('#searchForm'),
				list: $('#listForm'),
				update: $('#updateForm'),
				treeFrom: $('#treeFrom')
		};
		//窗口
		var Win = {
				update: $('#update-win')
		};	
		
		//初始化表格
		var initGrid = function(){	
			dataGrid.queryParams =  dataGrid.queryParams || {};	
			dataGrid.queryParams.pageNum = dataGrid.queryParams.pageNum || 1;
			dataGrid.queryParams.pageSize = dataGrid.queryParams.pageSize || 10;			
			if(dataGrid.url){
				IQB.get(dataGrid.url, dataGrid.queryParams, function(result){
					var list = result.iqbResult.result.list;
					if(list.length > 0){	
						var startRow = result.iqbResult.result.startRow;
						var endRow = result.iqbResult.result.endRow;
						dataGrid.queryParams.pageRowNum = endRow - startRow;
						var _html = '';
						$.each(list, function(rowIndex, row){
							_html = _html + '<tr>';
							var columns = $('.datagrid thead th');							
							var rowNum = startRow + rowIndex;
							$.each(columns, function(colIndex, col){	
								var html = '', hiddenClass = '';								
								var isHidden = $(col).attr('hidden') || false;
								if(isHidden){hiddenClass = 'hidden';}	
								var property = $(col).attr('field');
								if(property == 'rn'){
									html = '<td field="' + property + '" class="' + hiddenClass + '">' + rowNum + '</td>';
								}else if(property == 'ck'){
									html = '<td field="' + property + '" class="' + hiddenClass + '"><input type="checkbox" /></td>';
								}else{
									var isIdField = $(col).attr('idField') || false;
									if(isIdField){dataGrid.idField = property}
									var val = row[property];
									if(val == null){val = '';}
									html = val;
									var formatter = $(col).attr('formatter');								
									if(formatter){html = eval(formatter);}	
									html = '<td field="' + property + '" class="' + hiddenClass + '"><span class="hidden original-value">' + val + '</span>' + html + '</td>';							
								}
								_html = _html + html;
								
							});
							_html = _html + '</tr>';
						});
						$('.datagrid tbody').empty();
						$('.datagrid tbody').append(_html);
						var totalPages = result.iqbResult.result.pages;
						var currentPage = result.iqbResult.result.pageNum;
						initPaginator(totalPages, currentPage);
					}else{
						$('.datagrid tbody').empty();
						$('#paginator').empty();
					}			
				});
			}
		}
		
		//初始化分页
		var initPaginator = function(totalPages, currentPage){
			var paginationOption = {						
					totalPages: totalPages,
					currentPage: currentPage,
					numberOfPages: 5,
					itemTexts: function(type, page, current){
						if(type == 'first'){
							return '首页';
						}else if(type == 'prev'){
							return '上一页';
						}else if(type == 'next'){
							return '下一页';
						}else if(type == 'last'){
							return '尾页';
						}else{
							return page;
						}
		            },
		            itemContainerClass: function (type, page, current) {
		            	return (page === current) ? 'active' : 'pointer-cursor';		            	
		            },
		            onPageClicked: function(e, originalEvent, type, page){
		                e.stopImmediatePropagation();
		                dataGrid.queryParams.pageNum = page;
		                initGrid();
		            }		          								
            };
			$('#paginator').bootstrapPaginator(paginationOption);
			$('#paginator').removeClass('pagination');
			$('#paginator ul').addClass('pagination pagination-sm');
		}
		
		//初始化表单
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
		
		//初始化窗口
		var initWin = function(){
			if(Win.update && Win.update[0]){
				Win.update.find('#btn-save').off('click').on('click', Evt.save); //保存
				Win.update.find('#btn-close').off('click').on('click', Evt.close);//关闭
			}
		}
		
		//this 返回属性		
		this.util = Util;
		this.handler = Handler;		
		this.grid = Grid;
		this.form = Form;
		this.win = Win;
		
		//初始化方法
		this.init = function(){
			initGrid();
			initForm();
			initWin();
		};
		
		return this;
};