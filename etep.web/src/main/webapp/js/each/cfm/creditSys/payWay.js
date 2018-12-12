$package('IQB.payWay');
IQB.payWay = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{

		},
		//点击tab
		tab : function(){
			$('#tabs a').click(function() {
				$(".content-son").hide();
				$("#tabs li a").attr("id","");
				$(this).attr("id","current");
				$('#' + $(this).attr('title')).fadeIn();
				$(".addDiv").hide();
				if($(this).html() == "快捷支付"){
					   //银行卡信息
					   var data = {};
					   IQB.get(_this.config.action.bankCard, data, function(result) {
							var bankList = result;
							if(bankList.length > 0){
							var str = '';
							for(var i=0;i<bankList.length;i++){
								str += "<div class='bankcard' id='"+bankList[i].id+"'><p class='userName'>"+bankList[i].realName+"</p><p class='bankType'>"+bankList[i].bankType+"</p><p class='bankCard'>"+bankList[i].bankNo+"</p><input type='hidden' class='hiddenId' value='"+bankList[i].cardNo+"'><input type='hidden' class='mobileNo' value='"+bankList[i].mobileNo+"'><img src='../../../img/cfmImg/check.png' class='check'>" +
										"<div class='cardManage'><span>管理</span><img src='../../../img/cfmImg/arrow.png' class='manageImg'><div class='ManageSon'><p class='ManageSonItem cardRevise'>修改</p><p class='ManageSonItem cardDelete'>删除</p></div></div><div class='reviseMask'>修改中</div></div>"
							}
							str += "<div class='addbankcard'><span class='addText'>添加其他银行卡</span><img src='../../../img/cfmImg/add.png'></div>";
							$(".bankBox").html(str);
							$(".bankcard .check").eq(0).show();
							$("#bankId").val($(".bankcard").attr("id"));
						 }else{
							var str = '';
							str += "<div class='addbankcard' style='margin:0 auto;float:none;'><span class='addText'>添加其他银行卡</span><img src='../../../img/cfmImg/add.png'></div>";
							$(".bankBox").html(str);
							$("#fastButton").attr("disabled",true);
						}
					})
				}
			})
		},
		//管理银行卡
		manageCard : function(){
			$(".bankBox").delegate(".cardManage","click",function(){
				$(this).find(".ManageSon").toggle();
				$(this).toggleClass('ManageSonToggle');
				$(this).find(".manageImg").toggle();
			});
			//修改
			$(".bankBox").delegate(".cardRevise","click",function(){
				var that = $(this);
				var thisBankCard = that.parent().parent().parent();
				thisBankCard.find(".reviseMask").show();
				$(".reviseDiv").show();
				//修改银行卡信息显示
				$(".reviseDiv #reviseRealName").val(thisBankCard.find(".userName").html());
				$(".reviseDiv #reviseIDCard").val(thisBankCard.find(".hiddenId").val());
				$(".reviseDiv #reviseBankType").append("<option>"+thisBankCard.find(".bankType").html()+"</option>");
				$(".reviseDiv #reviseBankCard").val(thisBankCard.find(".bankCard").html());
				$(".reviseDiv #revisePhoneNum").val(thisBankCard.find(".mobileNo").val());
				$("#fastButton").hide();
				//取消修改
				$("#cancelRevise").click(function(){
					$(".reviseDiv").hide();
					$("#fastButton").show();
					thisBankCard.find(".reviseMask").hide();
				});
				//保存修改
				$("#saveRevise").click(function(){
					//表单验证
					if($("#reviseBankForm").form('validate')){
						var realName = $("#reviseRealName").val();
						var cardNo = $("#reviseIDCard").val();
						var bankNo = $("#reviseBankCard").val();
						var mobileNo = $("#revisePhoneNum").val();
						var id = thisBankCard.attr("id");
						console.log(id);
						var data = {
								"realName":realName,
								"cardNo":cardNo,
								"bankNo":bankNo,
								"mobileNo":mobileNo,
								"bankId":id
						};
						IQB.get(_this.config.action.reviseBankCard, data, function(result) {
							if(result.retCode == 'success'){
								$(".reviseDiv").hide();
								thisBankCard.find(".reviseMask").hide();
								$("#fastButton").show();
								thisBankCard.find(".userName").html(realName);
								thisBankCard.find(".bankCard").html(bankNo);
							}else if(result.retCode == 'failed'){
								IQB.alert('修改银行卡失败，请重新修改！');
								$(".reviseDiv").hide();
								thisBankCard.find(".reviseMask").hide();
								$("#fastButton").show();
							}
						})
					}
				})
			});
			//删除
			$(".bankBox").delegate(".cardDelete","click",function(){
				var thisCard = $(this).parent().parent().parent();
				var id = thisCard.attr("id");
				var number = thisCard.find(".bankCard").html();
				var type = thisCard.find(".bankType").html();
				var number2 = number.substring(number.length-4,number.length);
				IQB.confirm("确认删除您尾号为"+number2+"的"+type+"银行卡吗？",function(){
					var data = {"bankId":id};
					IQB.get(_this.config.action.deleteBankCard, data, function(result) {
						if(result.retCode == 'success'){
							thisCard.remove();
						}else if(result.retCode == 'failed'){
							IQB.alert('删除银行卡失败，请重新删除！');
						}
					})
				},function(){});
			});
		},
		//选择银行卡
		chooseCard : function(){
			$(".bankBox").delegate(".bankcard","click",function(){ 
				$(".bankcard .check").hide();
				$(this).find('.check').show();
				var id=$(this).attr("id");
				$("#bankId").val(id);
			})
		},
		//获取信息
		getValue : function(){
			var currentUrl = window.location.href;
			currentUrl = decodeURI(currentUrl);
			/*var productName = decodeURI(location.search.match(new RegExp("[\?\&]productName=([^\&]+)","i"))[1]);
			$("input[name='productName']").val(productName);*/
			var type = location.search.match(new RegExp("[\?\&]type=([^\&]+)","i"))[1];
			if(type == '1'){
				//首付款代偿
				var orderId = location.search.match(new RegExp("[\?\&]orderId=([^\&]+)","i"))[1];
				$("input[name='orderId']").val(orderId);
				var amount = location.search.match(new RegExp("[\?\&]amount=([^\&]+)","i"))[1];
				$("input[name='amount']").val(amount);
				$("input[name='orderItem']").attr('disabled',true);
			}else{
				var orderId = location.search.match(new RegExp("[\?\&]orderId=([^\&]+)","i"))[1];
				$("input[name='orderId']").val(orderId);
				var amount = location.search.match(new RegExp("[\?\&]amount=([^\&]+)","i"))[1];
				$("input[name='amount']").val(amount);
				var orderItem = location.search.match(new RegExp("[\?\&]orderItem=([^\&]+)","i"))[1];
				$("input[name='orderItem']").removeAttr('disabled');
				$("input[name='orderItem']").val(orderItem);
			}
			
		},
		//网银
		toPayOnline : function(){
			$('#payForm').attr('action', _this.config.action.onlinePay);
			$("#payForm").submit();
		},
		toPayFast : function(){
			$('#payForm').attr('action', _this.config.action.fastPay);
			$("#payForm").submit();
		},
		//添加银行卡
		addBankCard : function(){
			$(".bankBox").delegate(".addbankcard","click",function(){ 
				//5张最多
				if($(".bankcard").length >= 5){
					IQB.alert("最多添加5张银行卡！");	
				}else{
					$(".addDiv").show();
					$(".addDiv input").val("");
					$("#fastButton").hide();
					//获取银行卡类型
					var data = {};
					IQB.get(_this.config.action.getBankType, data, function(result) {
						var BankType = result;
						$("#bankType option").remove();
						$("#bankType").append("<option value=''>请选择卡类型</option>");
						if(BankType.length > 0){
		                    for(var i=0;i<BankType.length;i++){
		                    	$("#bankType").append("<option value='"+BankType[i].id+"'>"+BankType[i].bankType+"</option>")
		                    } 
						}
				    })
				}
			}); 
			$("#cancelAdd").click(function(){
				$(".addDiv").hide();
				$("#fastButton").show();
			});
			$("#saveCard").click(function(){
				//表单验证
				if($("#addBankForm").form('validate')){
					var realName = $("#realName").val();
					var cardNo = $("#IDCard").val();
					var txt = $("#bankType").find("option:selected").text();
					var bankType = txt;
					var bankNo = $("#bankCard").val();
					var mobileNo = $("#phoneNum").val();
					var data = {
							"realName":realName,
							"cardNo":cardNo,
							"bankType":bankType,
							"bankNo":bankNo,
							"mobileNo":mobileNo
					};
					IQB.get(_this.config.action.addBankCard, data, function(result) {
						if(result.retCode == 'success'){
							$(".addDiv").hide();
							//$(".addbankcard").before("<div class='bankcard'><p class='userName'>"+realName+"</p><p class='bankType'>"+bankType+"<p class='bankCard'>"+bankNo+"<img src='../../../img/cfmImg/check.png' class='check'></div>");
							//银行卡信息
							var data = {};
							IQB.get(_this.config.action.bankCard, data, function(result) {
									var bankList = result;
									var str = '';
									for(var i=0;i<bankList.length;i++){
										str += "<div class='bankcard' id='"+bankList[i].id+"'><p class='userName'>"+bankList[i].realName+"</p><p class='bankType'>"+bankList[i].bankType+"</p><p class='bankCard'>"+bankList[i].bankNo+"</p><input type='hidden' class='hiddenId' value='"+bankList[i].cardNo+"'><input type='hidden' class='mobileNo' value='"+bankList[i].mobileNo+"'><img src='../../../img/cfmImg/check.png' class='check'>" +
												"<div class='cardManage'><span>管理</span><img src='../../../img/cfmImg/arrow.png' class='manageImg'><div class='ManageSon'><p class='ManageSonItem cardRevise'>修改</p><p class='ManageSonItem cardDelete'>删除</p></div></div><div class='reviseMask'>修改中</div></div>"
									}
									str += "<div class='addbankcard'><span class='addText'>添加其他银行卡</span><img src='../../../img/cfmImg/add.png'></div>";
									$(".bankcard").remove(); 
									$(".bankBox").html(str);
									$(".bankcard .check").eq(0).show();
									$("#bankId").val($(".bankcard").attr("id")); 
							})
							$("#fastButton").show();
							$("#fastButton").attr("disabled",false);
						}else if(result.retCode == 'failed'){
							IQB.alert('添加银行卡失败，请重新添加！');
						}
					})
				}
			})
		},
		config: {
			action: {//页面请求参数
				addBankCard: urls['cfm'] + '/pay/addBindBankCard',
				bankCard: urls['cfm'] + '/pay/getBindBankCard',
				getBankType:urls['cfm'] + '/pay/getBankType',
				deleteBankCard: urls['cfm'] + '/pay/delBindBankCard',
				reviseBankCard: urls['cfm'] + '/pay/updateBindBankCard',
				onlinePay: urls['cfm'] + '/pay/payByCashierDesk',
				fastPay: urls['cfm'] + '/pay/payByQuick'
			},
			event: {//按钮绑定函数，不定义的话采用默认

			}
		},
		init: function(){
			_grid = new DataGrid(_this.config);
			_grid.init();//初始化按钮、表格
			_this.tab();
			_this.getValue();
			_this.addBankCard();
			_this.chooseCard();
			_this.manageCard();
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.payWay.init();
});
