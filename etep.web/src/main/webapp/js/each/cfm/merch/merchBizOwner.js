	function beforeClickModel(treeId, treeNode) {
		var check = (treeNode && !treeNode.isParent);
		if (!check) IQB.alert("请选择具体商户！");
		return check;
	}
	
	function onClickModel(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemoModel"),
		nodes = zTree.getSelectedNodes(),
		v = "";
		merchantNo = "";
		orgCode = "";
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].merchantShortName + ",";
			merchantNo += nodes[i].merchantNo + ",";
			orgCode += nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (merchantNo.length > 0 ) merchantNo = merchantNo.substring(0, merchantNo.length-1);
		if (orgCode.length > 0 ) orgCode = orgCode.substring(0, orgCode.length-1);
		var cityObj = $(".merchModel");
		cityObj.val(v);
		cityObj.attr('orgCode', String(orgCode));
		cityObj.attr('merchantNo', String(merchantNo));
		$('.merchantNo').val(String(merchantNo));
		$("#menuContentModel").hide();
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
					beforeClick: beforeClickModel,
					onClick: onClickModel
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

