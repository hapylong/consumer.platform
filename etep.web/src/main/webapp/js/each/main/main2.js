$package('IQB.main2');
IQB.main2 = function(){
	var _this = {
		isMain: function(id){
			if(id == '0'){
				return true;
			}
			return false;
		},
		tab: '0'
		,
		history:[
		    '0'
		],
		//更新Tab历史
		refreshHistory: function(id){
			var arry = [];
			$.each(_this.history, function(i, n){
				  if(n != id){
					  arry.push(n);
				  }
			});
			if(arry.length == 0){
				arry.push('0');
			}
			_this.tab = arry[arry.length - 1];
			_this.history = arry;
		},
		//用于首页缓存变量
		cache: {
			'/etep.web/view/etep/wf/waitHandleForHome.html': {
				tabId: '0',
				tabTitle: '首页',
				tabUrl: '../etep/wf/waitHandleForHome.html'
			}
		},
		//获取首页缓存变量
		fetchCache: function(url){
			if(url){
				return _this.cache[url]; 
			}
			return _this.cache;
		},	
		//执行回调
		call: function(callback, callback2){
			eval(callback);
			eval(callback2);
		},	
		//执行回调
		call2: function(callback, callback2, callback3){
			eval(callback);
			eval(callback2);
			eval(callback3);
		},
		//菜单单击回调
		menuClick: function(menu){
			var id = menu.id;
			var title = menu.name;
			var url = menu.url;
			IQB.main2.openTab(id, title, url, true, true, null);
		},
		//打开页签
		openTab: function(id, title, url, open, refresh, param){
			param = param || {};
			var tab = $('.tab-content').find('#' + id);
			if(tab && tab[0]){//已存在
				_this.showTab(id, title, url, refresh, param);
			}else{//不存在
				if(open){
					_this.tab = id;//刷新当前页签
					_this.history.push(id);								
					$.extend(param, {tabId: id, tabTitle: title, tabUrl: url});		
					var index = url.indexOf('?');
					//缓存变量
					if(index == -1){
						_this.cache[url] = param;
					}else{
						var shortUrl = url.substring(0, index);
						_this.cache[shortUrl] = param;						
					}					
					url = urls['baseUrl'] + url;
					var caption = '<li role="presentation"><a href="#' + id + '" role="tab" data-toggle="tab" onclick="IQB.main2.switchTab(\'' + id + '\')"><span class="glyphicon glyphicon-list-alt"></span> ' + title + ' <span class="glyphicon glyphicon-remove" onclick="IQB.main2.closeTab(\'' + id + '\', event)"></span></a></li>';				
					var content = '<div role="tabpanel" class="tab-pane fade" id="' + id + '" style="width: 100%; height: 100%;">' + 
									'<iframe scrolling="auto" frameborder="0" src="' + url + '" style="width: 100%; height: 100%;"></iframe>' +
								  '</div>';
					$('.nav').append(caption);
					$('.tab-content').append(content);
					$('.nav').tabdrop('layout');
					$('.nav a[href="#' + id + '"]').tab('show');
				}				
			}	
		},
		//展示已存在页签
		showTab: function(id, title, url, refresh, param){
			_this.switchTab(id);
			if(!_this.isMain(id)){
				if(refresh){
					$.extend(param, {tabId: id, tabTitle: title, tabUrl: url});
					var index = url.indexOf('?');
					//缓存变量
					if(index == -1){
						_this.cache[url] = param;
					}else{
						var shortUrl = url.substring(0, index);
						_this.cache[shortUrl] = param;					
					}	
					url = urls['baseUrl'] + url;
					$('#' + id).find('iframe').attr('src', url);//强制重新加载框架
				}
			}
			$('.nav a[href="#' + id + '"]').tab('show');
		},
		//切换页签
		switchTab: function(id){
			if(_this.tab != id){	
				_this.tab = id;//刷新当前页签
				_this.history.push(id);						
			}
			if(_this.isMain(id)){
				var url = $('#0').find('iframe').attr('src');
				$('#0').find('iframe').attr('src', url);//强制重新加载框架
			}
		},
		//打开Tab
		clickCloseTab: function(id){
			$('.nav a[href="#' + id + '"]').find('.glyphicon-remove').click();
		},
		//关闭页签
		closeTab: function(id, event){		
			if(_this.tab == id){
				_this.refreshHistory(id);
				$('.nav a[href="#' + id + '"]').remove();
				$('.tab-content').find('#' + id).remove();
				$('.nav').tabdrop('layout');
				if(_this.tab == '0'){
					var url = $('#0').find('iframe').attr('src');
					$('#0').find('iframe').attr('src', url);//强制重新加载
				}
				$('.nav a[href="#' + _this.tab + '"]').tab('show');
			}else{
				_this.refreshHistory(id);
				$('.nav a[href="#' + id + '"]').remove();
				$('.tab-content').find('#' + id).remove();
				$('.nav').tabdrop('layout');
			}
			if(event){
			    if(event.stopPropagation){//W3C阻止冒泡方法  
			    	event.stopPropagation();  
			    }else{//IE阻止冒泡方法   
			    	event.cancelBubble = true;
			    }  
			}	
		},	
		//初始化页签
		initTab: function(){	
			$('.nav').tabdrop({text: '<span class="glyphicon glyphicon-list"></span> 更多'});
		}
	};
	return _this;
}();

$(function(){
	//页面提示
	$('#btn-exit').tooltip({animation: true, placement: 'left', title: '退出系统'});
	$('#btn-editPwd').tooltip({animation: true, placement: 'left', title: '修改密码'});
	//页面初始化
	IQB.main2.initTab();	
});		