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
			$("#datagrid").datagrid2({url: urls['cfm'] + '/workFlow/getAllReqAllotMoney',queryParams : $.extend({}, $("#searchForm").serializeObject(), {pageNum: _this.cache.page,'orgCode': _this.cache.orgCode,'merchantNo': _this.cache.merchantNo})	
			});
	    },
		update:function(){
			if(!$('#updateForm').form('validate')){
				return false;
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
			$('#update-win').modal('hide');
		},
		openUpdateWin:function(){
			var row = $("#datagrid").datagrid2("getCheckedRows");
			var orderId=row[0].orderId
			IQB.get(urls['cfm'] + '/workFlow/getAllotDetailByOrderId',{'orderId': orderId}, function(result){
        		var result = result.iqbResult.result;
        		var appendTr= "";
        		for(var i=0;i<result.length;i++){
        			
            		appendTr +="<tr class='NewTr' ><td>"+(i+1)+"</td><td>"+Formatter.timeCfm5(result[i].createTime)+"</td><td>"+Formatter.money(result[i].applyAmt)+"</td><td>"+result[i].deadline+"</td><td>"+result[i].applyItems+"</td><td>"+result[i].sourcesFunding+"</td><td>"+result[i].creditName+"</td><td>"+Formatter.isNull(result[i].realName)+"</td><td>"+Formatter.isNull(result[i].redemptionDate)+"</td><td>"+Formatter.isNull(result[i].redemptionReason)+"</td><td>"+Formatter.pushMode(result[i].pushMode)+"</td></tr>"
        		}
        		$("#datagrid1").find('.NewTr').remove();
				$("#datagrid1").append(appendTr);
	

        	})
			$('.iqbRequire').hide();
			$('#updateForm')[0].reset();
			this.renderSelect();
		},
		closeUpdateWin:function(){
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
			var req_data = {'dictTypeCode': 'STAGE_NUMBER'};
			$("select[name='requestTimes']").empty();
			IQB.post(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
				jQuery("select[name='requestTimes']").prepend("<option value=''>请选择</option>");
				var requestItems = result.iqbResult.result;
				$(requestItems).each(
						function(index) {
							if(parseInt(requestItems[index].id) <= parseInt(leftitems)){
								jQuery("select[name='requestTimes']").append("<option value='" + requestItems[index].id + "'>"+ requestItems[index].text+ "</option>");
							}
						}
						);
				
			})
			$('#chargeWay').attr("disabled",true);
			$("#riskType").attr("disabled",true);
			 var mydate = new Date();  
				var str = "" + mydate.getFullYear() + "-";  
				str += (mydate.getMonth()+1) + "-";  
				str += mydate.getDate() + " ";  
				str += mydate.getHours()+":";
				str += mydate.getMinutes()+":00";
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
		exportXlsx : function(){
			var orgCode = $(".merch").attr('orgCode');
			_this.cache.orgCode = orgCode;
			var merchantNo = $(".merch").attr('merchantNo');
			_this.cache.merchantNo = merchantNo;
			var data = $.extend({}, $("#searchForm").serializeObject(), {'orgCode': orgCode,'merchantNo':merchantNo});
			window.location.href=urls['cfm'] + '/workFlow/exportAllReqAllotMoney?' + IQB.jsonToUrlParams(data);
		},
		config: {
			action: {//页面请求参数
  				getMerCodeInfo : urls['cfm']+ '/merchant/getMerList',
  				getById: urls['cfm'] +'/assetAllocation/getAssetDetails',
  				save: urls['cfm']+'/workFlow/saveReqParams'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.updateAsset4(_this.showImg);
					$('#update-win-label').text('新建资产分配');
				},
				reset: function(){//重写save	
					_grid.handler.reset();
					$('#query_fund_source').val(null).trigger('change');
					$('#query_stage_number').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/workFlow/getAllReqAllotMoney',
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
	   			url: urls['cfm'] + '/workFlow/getAllReqAllotMoney',
	   			singleCheck: true,//'/assetAllocation/getAssetAllocationList',
	   			loadSuccess:function(date){
	   				//支付笔数与支付金额赋值
	   				$("#allTerms").val(date.iqbResult.resultTotal.orderAmtCount);
	   				$("#allAmt").val(Formatter.money(date.iqbResult.resultTotal.orderAmtTotal));
	   			}
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			this.initSelect();
			this.initBtnClick();
		},
		initSelect: function(){
			IQB.getDictListByDictType('query_fund_source', 'FUND_SOURCE');
			IQB.getDictListByDictType('query_stage_number', 'STAGE_NUMBER');
			this.renderSelect();
			$('#query2_fund_source').on('change', function(){_this.query2_fund_source()});
		},
		renderSelect: function(){
			$('#query_fund_source').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#query_stage_number').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isWithholding').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPublic').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPushff').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
		initBtnClick: function(){
			$('#btn-save_req').on('click', function(){_this.update()});
			$('#btn-update').on('click', function(){_this.openUpdateWin()});
			$('#btn-export').on('click', function(){_this.exportXlsx()});
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
	datepicker(applyTime);
	datepicker(planLendingTime);
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
		allowBlank: true
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