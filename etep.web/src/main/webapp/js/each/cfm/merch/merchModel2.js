	function beforeClickModel(treeId, treeNode) {
		var check = (treeNode && !treeNode.isParent);
		if (!check) IQB.alert("请选择具体商户！");
		return check;
	}
	
	function onClickModel(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemoModel"),
		nodes = zTree.getSelectedNodes(),
		v = "";
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].merchantShortName + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		var cityObj = $(".merchModel");
		/*cityObj.attr("value", v);*/
		cityObj.val(v);
		$("#menuContentModel").hide();
	}
	function onCheckModel(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemoModel"),
		nodes = zTree.getCheckedNodes(true),
		v = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].merchantShortName + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		var cityObj = $(".merchModel");
		//cityObj.attr("value", v);
		cityObj.val(v);
	}
	function showMenuModel() {
		var cityObj = $(".merchModel");
		var cityOffset = $(".merchModel").offset();
		var width = cityObj.outerWidth();
		$("#menuContentModel").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px", "z-index":99, width:width + "px"}).toggle();
		if($("#update-win")){$("#update-win").addClass("z-index")};
		if($(".modal-backdrop")){$(".modal-backdrop").addClass("z-index2")};
		$("body").bind("mousedown", onBodyDown);
	}
	function hideMenuModel() {
		$("#menuContentModel").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDownModel);
	}
	function onBodyDownModel(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "menuContentModel" || $(event.target).parents("#menuContentModel").length>0)) {
			hideMenuModel();
		}
	}
	$(function(){
		  var setting = {
			    check: {
					enable: true,
					chkboxType: {"Y":"s", "N":"s"},
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
					beforeClick: beforeClickModel,
					//onClick: onClickModel,
					onCheck: onCheckModel
				}
			};
		    var zNodes;
		    var data = {};
			IQB.get(urls['cfm']+ '/merchant/getMerList', data, function(result) {
				zNodes = result.iqbResult.result;
				//页面初始化
				$.fn.zTree.init($("#treeDemoModel"), setting, zNodes);
			})
	});

