$package('IQB.requestFunds');
IQB.requestFunds = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{
			
		},
		reset : function(){
			$('#searchForm').find('input').val('');
			$('#searchForm').find('select').select2('val','');
			$('#select2-query_merch_name-container').text('请选择');
		},
		refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/workFlow/getAllReqMoney',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		update:function(){
			if(!$('#updateForm').form('validate')){
				return false;
			}
			//当标的结束时间大于资产到期日时，标的结束时间默认为资产到期日；
			var deadline = $('#deadline').val();
			var assetDueDate = $('#assetDueDate').val();
			var leftItems = $("input[name='leftitems']").val();
			var orderItems = $("input[name='orderItems']").val();
			leftItems = leftItems.substring(0,leftItems.length-1);
			leftItems = leftItems.split('月');
			deadline = new Date(deadline.replace(/-/g, '/'));
			deadline = deadline.getTime();
			if(leftItems[0] != orderItems || leftItems[1] != 0){
				if(deadline > assetDueDate){
					IQB.alert('标的结束时间不能大于资产到期日');
					$('#deadline').val(Formatter.timeCfm5(assetDueDate));
					return false;
				}
			}
			var option = {};
			option.orderId = $("#orderId").val();
			option.requestTimes = $("#requestTimes  option:selected").val();
			option.fundSourceId = $("#query2_fund_source  option:selected").val();
			option.plantime =  $("#plantime").val();
			option.desc =  $("#desc").val();
			option.deadline = $("#deadline").val();
			option.isWithholding = $("#isWithholding").val();
			option.isPublic = $("#isPublic").val();
			option.isPushff = $("#isPushff").val();
			option.plantime =  $("#plantime").val();
			option.creditName =  $("#creditName").find("option:selected").text();
			option.creditCardNo = $("#creditCardNo").val();
			option.creditBankCard = $("#creditBankCard").val();
			option.creditBank = $("#creditBank").val();
			option.creditPhone = $("#creditPhone").val();
			//保存推标方式
			option.pushMode = $('#amtWay').val();
			option.applyAmt = $('.sborderAmt').val();//上标金额
			option.curRepayNo = $('#currentItem').val();
			if(option.fundSourceId == '1'){
				if(option.isWithholding == ''){
					$("#isWithholding").focus();
					return false;
				}else if(option.isPublic == ''){
					$("#isPublic").focus();
					return false;
				}else if(option.isPushff == ''){
					$("#isPushff").focus();
					return false;
				}
			}
			this.closeUpdateWinWithRender();
			IQB.post(urls['cfm'] + '/workFlow/saveReqParams', option, function(resultInfo){
				if( resultInfo && "error" != resultInfo.retCode){
					_this.refresh();
				}else if(resultInfo && "error" == resultInfo.retCode){
					IQB.alert('推送失败！');
				}
 			});
		},
		closeUpdateWinWithRender: function(){
			$("#creditName").children().remove();
			$('#update-win').modal('hide');
		},
		openUpdateWin:function(){
			$('.iqbRequire').hide();
			$('.amtWayShow').hide();
			datepicker(plantime);
			//去掉当前期数必填校验
			$('#currentItem').validatebox({ required: false });
			$('#updateForm')[0].reset();
			this.renderSelect();
		},
		closeUpdateWin:function(){
			$("#creditName").children().remove();
			$('#updateForm')[0].reset();
			this.renderSelect();
		},
		query2_fund_source: function(){
            var query2_fund_source = $("#query2_fund_source").val();
            if(query2_fund_source == '1'){
                $('.iqbRequire').show();
            }else{
                $('.iqbRequire').hide();
            }
        },
		showImg: function(orderId,leftitems){//初始化图片
			$("#query2_fund_source").empty();
			IQB.getDictListByDictType('query2_fund_source', 'FUND_SOURCE');
			var req_data = {'orderId': orderId};
			$("select[name='requestTimes']").empty();
			IQB.post(urls['rootUrl'] + '/afterLoan/selectPayMoneyListByOrderId', req_data, function(result){
				jQuery("select[name='requestTimes']").prepend("<option value=''>请选择</option>");
				var requestItems = result.iqbResult.result;
				$(requestItems).each(
						function(index) {
							jQuery("select[name='requestTimes']").append("<option value='" + requestItems[index].id + "'>"+ requestItems[index].text+ "</option>");
						}
						);
				
			})
			$('#chargeWay').attr("disabled",true);
			$("#riskType").attr("disabled",true);
			 var mydate = new Date();  
				var str = "" + mydate.getFullYear() + "-";  
				if(mydate.getMonth()+1<10) {
					str += "0" + (mydate.getMonth()+1) + "-";
				} else {
					str += (mydate.getMonth()+1) + "-";
				}
				if(mydate.getDate()<10) {
					str += "0" + mydate.getDate() + " ";
				} else {
					str += mydate.getDate() + "";  
				}  
				/*str += mydate.getHours()+":";
				str += mydate.getMinutes()+":00";*/
			$("#plantime").val(str);
			IQB.post(urls['cfm'] + '/image/getImageList', {orderId: orderId}, function(result){
				$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
				$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
				$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
				$('#td-1').empty();$('#td-2').empty();$('#td-3').empty();$('#td-4').empty();$('#td-6').empty();$('#td-7').empty();$('#td-8').empty();$('#td-9').empty();	
				$.each(result.iqbResult.result, function(i, n){
					var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
									'<a href="' + urls['imgUrl'] + n.imgPath + '" rel="prettyPhoto[one]" title="' + n.imgName + '"><img src="' + urls['imgUrl'] + n.imgPath + '" style="width: 135px; height: 135px;"></a>' +
							   '</div>';	
					$('#td-' + n.imgType).append(html);	
				});
				$('a[rel^="prettyPhoto"]').prettyPhoto();
			});
		},
		calDate : function(){
			//var plantime = $('#plantime').val().split(' ');
			var plantime = $('#plantime').val();
			plantime = plantime.replace("-","");
			plantime = plantime.replace("-","");
			var requestTimes = $('#requestTimes').val();
			if(plantime!='' && requestTimes!= ''){
				var option = {
						'startDate':plantime,
						'addMonth':requestTimes
				};
				IQB.post(urls['cfm'] + '/business/calDate', option, function(result){
					var newDate = result.iqbResult.result;
					newStr = newDate.substring(0,4);
					newStr1 = newDate.substring(4,6);
					newStr2 = newDate.substring(6,8);
					newDate = newStr + '-' + newStr1 + '-' + newStr2;
					$('#deadline').val(newDate);
				})
			}
		},
		config: {
			action: {//页面请求参数
  				getMerCodeInfo : urls['cfm']+ '/merchant/getMerList',
  				getById: urls['cfm'] +'/assetAllocation/getAssetDetails',
  				save: urls['cfm']+'/workFlow/saveReqParams'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.updateAsset(_this.showImg);
					$('#update-win-label').text('新建资产分配');
				},
				reset: function(){//重写save	
					_grid.handler.reset();
					$('#query_fund_source').val(null).trigger('change');
					$('#query_stage_number').val(null).trigger('change');
					$('select[name="lendersSubject"]').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/workFlow/getAllReqMoney',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo}),
								 loadSuccess:function(date){
						   				//支付笔数与支付金额赋值
						   				$("#allTerms").val(date.iqbResult.resultTotal.orderAmtCount);
						   				$("#allAmt").val(Formatter.money(date.iqbResult.resultTotal.orderAmtTotal));
						   			}
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/workFlow/getAllReqMoney',
	   			singleCheck: true,//'/assetAllocation/getAssetAllocationList',
	   			loadSuccess:function(date){
	   				//支付笔数与支付金额赋值
	   				$("#allTerms").val(date.iqbResult.resultTotal.orderAmtCount);
	   				$("#allAmt").val(Formatter.money(date.iqbResult.resultTotal.orderAmtTotal));
	   			}
			}
		},
		amtWayChoose : function(){
			/*if($('#requestTimes').val() == '' || $('#deadline').val() == ''){
				IQB.alert('请优先选择本次请款期数与标的结束时间！');
				$('#amtWay').val('');
				return false;
			}*/
			var val = $('#amtWay').val();
			if(val == '1'){
				//按订单金额
				$("#plantime").datetimepicker({ 
					lang:'ch',
				    timepicker:false,
				    format:'Y-m-d',
				    defaultDate : new Date(),
					allowBlank: true,
					minDate: false
				});
				$('.amtWayShow').hide();
				//去掉当前期数必填校验
				$('#currentItem').validatebox({ required: false });
				//回显上标金额=订单金额
				$('#currentItem').val('');
				$('.sborderAmt').val($('#orderAmt').val());
			}else if(val == '2'){
				//按剩余未还本金
				//datepickerP(plantime);//预计放款时间
				$("#plantime").datetimepicker({ 
					lang:'ch',
				    timepicker:false,
				    format:'Y-m-d',
				    defaultDate : new Date(),
					allowBlank: true,
					minDate: 0
				});
				$('.amtWayShow').show();
				//去掉当前期数必填校验
				$('#currentItem').validatebox({ required: true });
				//回显当前期数  和  上标金额
				IQB.post(urls['cfm'] + '/assetAllocation/getAssetInfo', {'orderId':$('#orderId').val(),'loanDate':$('#plantime').val()}, function(result){
					var result = result.iqbResult.result;
					if(result != null){
						$('#currentItem').val(result.repayNo);
						$('.sborderAmt').val(result.sbAmt);
					}
				})
				
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			this.initSelect();
			this.initBtnClick();
			$('#requestTimes').on('change',function(){
				_this.calDate();
			});
			$('#plantime').on('blur',function(){
				//计算标的结束时间
				_this.calDate();
				//计算上标金额和当前期数
				_this.amtWayChoose();
			});
			//选择推标金额方式
			$('#amtWay').on('change',function(){
				_this.amtWayChoose();
			});
		},
		initSelect: function(){
			IQB.getDictListByDictType('query_fund_source', 'FUND_SOURCE');
			IQB.getDictListByDictType('query_stage_number', 'STAGE_NUMBER');
			this.renderSelect();
			$('#query2_fund_source').on('change', function(){_this.query2_fund_source()});
			IQB.getDictListByDictType2('lendersSubject', 'Lenders_Subject');
			$('select[name="lendersSubject"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		renderSelect: function(){
			$('#query_fund_source').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query_stage_number').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isWithholding').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPublic').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPushff').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
            $('#amtWay').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		initBtnClick: function(){
			$('#btn-save_req').on('click', function(){_this.update()});
			$('#btn-update').on('click', function(){_this.openUpdateWin()});
			$('#btn-close').on('click', function(){_this.closeUpdateWin()});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.requestFunds.init();
	datepicker(query_start_order_time);
	datepicker(query_end_order_time);
    //datepicker(plantime);//预计放款时间
	datepicker(planLendingTime);
	datepicker(deadline);
	datepicker(applyTime);
	$('#update-win').find('.modal-form').mCustomScrollbar({setHeight: 500});
});	
/*显示日历*/ 
function datepicker(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
	    defaultDate : new Date(),
		allowBlank: true,
		minDate: false
	});
};
function datepickerP(id){
	var date_ipt = $(id)
	$(id).datetimepicker({
	    lang:'ch',
	    timepicker:false,
	    format:'Y-m-d',
	    defaultDate : new Date(),
		allowBlank: true,
		minDate: 0
	});
};
function show(val){  
	   var mydate = new Date();  
	   var str = "" + mydate.getFullYear() + "-";  
	   str += (mydate.getMonth()+1) + "-";  
	   str += mydate.getDate() + "";  
	   return str;  
}  
function initRequestPeriod (value){
	 $("[name='orderName']").val(value.orderName);
	
	var all = [6,12,24,36];
	var value = $("#orderItems")[0].value;
	jQuery("select[name='requestPeriod']").prepend("<option value=''>全部</option>");
	$(all).each(
			function(index) {
				jQuery("select[name='requestPeriod']").append("<option value='" + all[index] + "'>"+ all[index]+ "</option>");
			});
}
function insert_flg(str,flg,sn){
    var newstr="";
    for(var i=0;i<str.length;i+=sn){
        var tmp=str.substring(i, i+sn);
        newstr+=tmp+flg;
    }
    return newstr;
}