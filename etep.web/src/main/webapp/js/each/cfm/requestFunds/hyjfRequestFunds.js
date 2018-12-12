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
		},
		update:function(){
			if($('#updateForm').form('validate')){
				//判断剩余金额及本次请款金额
				var restAmt = $('#restAmt').val();
				var applyAmt = $('#applyAmt').val();
				if(restAmt <= 0){
					IQB.alert('剩余金额为零,无法分配');
					return false;
				}
				if(applyAmt > restAmt){
					IQB.alert('本次请款金额必须小于剩余金额');
					return false;
				}
				var option = {};
				option.orderNo = $("#orderNo").val();
				option.applyTerm = $("#applyTerm").val();
				option.fundSourceId = $("#query2_fund_source  option:selected").val();
				option.deadline = $("#deadline").val();
				option.plantime = $("#plantime").val();
				option.applyAmt = $("#applyAmt").val();
				option.isWithholding = $("#isWithholding").val();
				option.isPublic = $("#isPublic").val();
				option.isPushff = $("#isPushff").val();
				option.desc =  $("#desc").val();
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
				IQB.post(urls['cfm'] + '/houseAsset/houseAssetAllot', option, function(resultInfo){
					if( resultInfo && "success" == resultInfo.retCode){
						IQB.alert('推送成功！');
						_grid.handler.refresh();
					}else if(resultInfo && "success" != resultInfo.retCode){
						IQB.alert('推送失败！');
					}
	 			});
			}	
		},
		closeUpdateWinWithRender: function(){
			$('#update-win').modal('hide');
		},
		openUpdateWin:function(){
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
		config: {
			action: {//页面请求参数
  				getMerCodeInfo : urls['cfm']+ '/merchant/getMerList',
  				getById: urls['cfm'] +'/houseAsset/queryDetail ',
  				save: urls['cfm']+'/workFlow/saveReqParams'
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				update: function(){
					_grid.handler.updateAsset1(_this.showImg);
					$('#update-win-label').text('资产分配信息');
				},
				reset: function(){//重写save	
					_grid.handler.reset();
					$('#query_fund_source').val(null).trigger('change');
				},
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/houseAsset/query',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/houseAsset/query',
	   			singleCheck: true,
	   			isExport:true,
		    	filename: '%YY%%MM%%DD%%hh%%mm%%ss%',
		    	cols: '3,4,5,6,7,8,9,10,11,12,13,14'
			}
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			this.initSelect();
			this.initBtnClick();
			$('#btn-reset').click(function(){_this.reset()});
		},
		initSelect: function(){
			IQB.getDictListByDictType('query2_fund_source', 'FUND_SOURCE');
			this.renderSelect();
			$('#query2_fund_source').on('change', function(){_this.query2_fund_source()});
		},
		renderSelect: function(){
			$('#query_fund_source').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isWithholding').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPublic').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#isPushff').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
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
	datepicker(plantime);
	datepicker(deadline);
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
		allowBlank: false
	});
};