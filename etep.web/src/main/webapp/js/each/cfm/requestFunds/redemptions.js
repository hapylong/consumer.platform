$package('IQB.deleteRecord');
IQB.deleteRecord = function(){
    var _grid = null;
    var _tree = null;
    var _this = {
        cache :{
            
        },
        redeem : function() {
        	var rows = $('#datagrid').datagrid2('getCheckedRows');
            if(rows && rows.length > 0){
            	//赎回
				$('#open-win').modal('show');
				//$("#redemptionDate").val(_this.getNowFormatDate());
				$("#redemptionDate").val('');
				$('#redemptionReason').val('');
				$("#btn-sure").unbind("click").click(function(){
					  var option = {
							  "id": rows[0].id,
							  "redemptionReason": $('#redemptionReason').val(),
							  'redemptionDate':$('#redemptionDate').val()
					  }
					  if($("#updateForm").form('validate')){
						  IQB.post(_this.config.action.redeemUrl, option , function(result) {
							  if(result.iqbResult.result == 'success'){
								  $('#open-win').modal('hide');
								  IQB.alert('资产赎回成功');
								  _this.refresh();
							  }else if(result.iqbResult.result == '-1'){
								  $('#open-win').modal('hide');
								  IQB.alert('赎回时间不能大于标的到期日');
							  }else if(result.iqbResult.result == '-2'){
								  $('#open-win').modal('hide');
								  IQB.alert('赎回时间不能小于资产分配时间');
							  }
						  })
					  }
				})
				$('#btn-close').on('click',function(){
					$('#open-win').modal('hide');
				});
            }else{
                IQB.alert('未选中行');
                return false;   
            }                 
        },
        refresh : function(page){
			$("#datagrid").datagrid2({url: urls['cfm'] + '/workFlow/getAllotRedemptionInfo',queryParams : $.extend({}, $("#searchForm").serializeObject(),{'orgCode': _this.cache.orgCode,'merchantNo':_this.cache.merchantNo,'flag':1})	
			});
	    },
        config: {
            action: {//页面请求参数
            	redeemUrl: urls['cfm'] + '/workFlow/allotRedemption'
            },
            event: {//按钮绑定函数，不定义的话采用默认
            	reset: function(){
					_grid.handler.reset();
					$('select[name="sourcesFunding"]').val(null).trigger('change');
				},
				search: function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2({
					 url: urls['cfm'] + '/workFlow/getAllotRedemptionInfo',
					 queryParams : 
						 $.extend({}, $("#searchForm").serializeObject(), 
								 {'orgCode': orgCode,'merchantNo':merchantNo,'flag':1})
					});
				}
            },
            dataGrid: {//表格参数               
                url: urls['cfm'] + '/workFlow/getAllotRedemptionInfo',
                queryParams:{
                	'flag':2
                },
                singleCheck: true
            }
        },
        getNowFormatDate : function() {
	          var date = new Date();
	          var seperator1 = "-";
	          var year = date.getFullYear();
	          var month = date.getMonth() + 1;
	          var strDate = date.getDate();
	          if (month >= 1 && month <= 9) {
	              month = "0" + month;
	          }
	          if (strDate >= 0 && strDate <= 9) {
	              strDate = "0" + strDate;
	          }
	          var currentdate = year.toString() +'-'+ month.toString() +'-'+ strDate.toString();
	          return currentdate;
	    },
	    initSelect : function(){
			IQB.getDictListByDictType2('sourcesFunding', 'fund_source');
			$('select[name="sourcesFunding"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
		},
        init: function(){ 
            _grid = new DataGrid2(_this.config); 
            _grid.init();//初始化按钮、表格
            _this.initSelect();
            $("#btn-redeem").click(function(){_this.redeem()});
        }
    }
    return _this;
}();
$(function(){
    //页面初始化
    IQB.deleteRecord.init();
    datepicker(startTime);
    datepicker(endTime);
    datepicker(deadLineStartTime);
    datepicker(deadLineEndTime);
    datepicker(redemptionDate);
}); 
/*显示日历*/ 
function datepicker(id){
    var date_ipt = $(id)
    $(id).datetimepicker({
        lang:'ch',
        timepicker:false,
        format:'Y-m-d',
        allowBlank: true
    });
};
