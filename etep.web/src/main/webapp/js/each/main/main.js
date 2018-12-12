$package('IQB.main');
IQB.main = function(){
	var _tree = null;
	var _this = {
		config: {
			tree : {// 组织机构树参数
				url : urls['rootUrl'] + '/sysMenuRest/getSysMenuListByRole',
				setting : {
					data : {
						simpleData : {
							enable : true
						}
					}, // 启用简单数据渲染
					callback : {
						onClick : function(event, treeId, treeNode) {// 节点单击回调
							if (event && event.preventDefault) {
								event.preventDefault();
							} else {
								window.event.returnValue = false;
							}
							if(treeNode.url != '' && treeNode.url.indexOf('.html') > 0){
								IQB.main2.menuClick(treeNode);
							}
						}
					}
				}
			}
		},
		init: function(){
			_tree = new Tree(_this.config.tree);//注意参数和表格传参不同 
			_tree.init();//初始化组织机构树
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.main.init();
	var reqUrl = urls['rootUrl'] + '/sysLogin/getSysLoginInfo';
	$.ajax({
		url: reqUrl,
		type: 'post',
		async: true,
	 	success: function(result){//请求成功
			$('#userNameShow').html(result.iqbResult.result.userCode);	
			$('#orgName').html(result.iqbResult.result.orgShortName);	
	 	},
	});
});		
function log_out(){
	$.ajax({
		type: "POST",
		url : urls['sysmanegeUrl'] + '/sysLogin/logout',
		success : function(data){
		}
	});
	top.location.href = '/etep.web/index.html';
}
function reset_pwd(){
	IQB.main2.openTab('modifypassword', '修改密码', '/etep.web/view/etep/sys/user/modifypassword.html', true, false, null);
}
$(function(){
    $(".tree > li > a").click(function(){
		$(this).parents().siblings().find(".er").hide(300);
		$(this).siblings(".er").toggle(300);
        $(this).siblings().find(".thr").hide(300);
        $(this).siblings().find(".change").attr("src","imgs/li8.png");
	})	 
    $(".er > li > a").click(function(){
        $(this).siblings().find(".change").attr("src","imgs/li7.png");
        $(this).parents().siblings().find(".change").attr("src","imgs/li8.png");
        $(this).parents().siblings().find(".thr").hide(300);	
	    $(this).siblings(".thr").toggle(300);
    	})
     $(".arrow").click(function(){
        $(this).parents().siblings().find(".tanchunag").hide(300);
        $(this).siblings(".er").toggle(300);
    }) 

    $(".arrow").click(function(){   
    if($(".tanchuang").css("display")=="none"){
    $(".tanchuang").show();
    $(".arrow img").attr("src","../../img/arrow1.png");
    }else{
    $(".tanchuang").hide();
    }
    });
     // 隐藏树
    $(".jinfu").click(function(){   
    if($(".left-tree").css("display")=="none"){
    $(".left-tree").show();
    $(".bg").width("90%");
    }else{
    $(".left-tree").hide();
    $(".bg").width("100%");
    }
    });
     // 改变时间
    $(".TD").click(function(){      
    $(".DT").val("2013-06-15 14:45" )
    });
    $(".SEVEN").click(function(){      
    $(".DT").val("2013-06-15 14:45" )
    });
    $(".MONTH").click(function(){      
    $(".DT").val("2013-06-15 14:45" )
    });
    /*$(".tianjia").click(function(){   
    if($(".tanform").css("display")=="none"){
    $(".tanform").show();}
    if($(".zhezhao").css("display")=="none"){
    $(".zhezhao").show();}
    });*/
    /* $(".guanbi").click(function(){   
    if($(".tanform").css("display")=="block"){
    $(".tanform").hide();}
    if($(".zhezhao").css("display")=="block"){
    $(".zhezhao").hide();}
    });*/
})  