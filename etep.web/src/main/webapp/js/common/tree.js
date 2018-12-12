/**
 * @author Van
 * @Version: 1.0
 * @DateTime: 2012-11-11
 */
var Tree = function(config){
	
		//传入参数
		config = config || {};		
		//容器
		var container = config.container || 'tree';		
		//设置
		var setting = config.setting || {	data: {simpleData: {enable: true}}, 
											callback: {onClick: function(event, treeId, treeNode) {}}
									     };				
		
		//初始化树
		var initTree = function(){
			var option = config.queryParams || {};
			IQB.get(config.url, option, function(result){
				var treeNodes = result.iqbResult.result;		 
				$.fn.zTree.init($('#' + container), setting, treeNodes);
			});
		}
		
		//this 返回 tree 对象(此为jQuery对象，此对象具体方法、事件参考 http://www.treejs.cn/v3/api.php)		
		this.getTreeObj = function(){return $.fn.zTree.getZTreeObj(container)};
		
		//初始化方法
		this.init = function(){
			initTree();
		};
		
		return this;
};