    function beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	
    function onCheck(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
		nodes = zTree.getCheckedNodes(true),
		v = "";
		merchantNo = "";
		orgCode = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].merchantShortName + ",";
			merchantNo += nodes[i].merchantNo + ",";
			orgCode += nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (merchantNo.length > 0 ) merchantNo = merchantNo.substring(0, merchantNo.length-1);
		if (orgCode.length > 0 ) orgCode = orgCode.substring(0, orgCode.length-1);
		var cityObj = $(".merch");
		cityObj.val(v);
		cityObj.attr('orgCode', String(orgCode));
		cityObj.attr('merchantNo', String(merchantNo));
	}

	function showMenu() {
		var cityObj = $(".merch");
		var cityOffset;
		var cityWidth;
		var timer = setInterval(function(){
			cityOffset = $(".merch").offset();
			cityWidth = $(".merch").outerWidth();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px" , width:cityWidth + "px"});
		 }, 100);
		$("#menuContent").slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	}
	function hideMenu() {
		$("#menuContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "citySel" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
			hideMenu();
		}
	}
	$(function(){
		  var setting = {
				check: {
					enable: true,
					chkboxType: {"Y":"", "N":""},//父子不关联
					chkStyle: "radio",  //单选框
		            radioType: "all"   //对所有节点设置单选
				},
				view: {
					dblClickExpand: false
				},
				data: {
					key: {
						name:"merchantShortName"
					},
					simpleData: {
						enable: true,
						pIdKey: "parentId",
						idKey:"id",
						nameKey:"merchantShortName"
					}
				},
				callback: {
					beforeClick: beforeClick,
					onCheck: onCheck
				}
			};
		    var zNodes;
		    var data = {};
			IQB.get(urls['cfm']+ '/merchant/getMerList', data, function(result) {
				zNodes = result.iqbResult.result;
				//页面初始化
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			})
			//重置
			$("#btn-reset").click(function(){
				$("#searchForm input").val("");
				$("#searchForm select").val("");
				$("#searchForm .merch").val('全部商户');
				$("#searchForm .merch").attr('orgCode','');
				$("#searchForm .merch").attr('merchantNo','');
				//取消勾选全部节点
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				treeObj.checkAllNodes(false);
			})
	});

