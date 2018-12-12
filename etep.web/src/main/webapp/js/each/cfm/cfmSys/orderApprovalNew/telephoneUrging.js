$package('IQB.telephoneUrging');
IQB.telephoneUrging = function(){
	var _grid = null;
	var _tree = null;
    var merchantFlag= 1;
    var showDataHtml="";
	var _this = {
		cache :{
			page:0,
			i:0
		},
		reset : function(){
			$("#btn-reset").click(function(){
				$('#searchForm').find('input').val('');
				$('#searchForm').find('select').select2('val','');
				$('#select2-mobileCollection-container').text('请选择');
				$('#select2-mobileDealOpinion-container').text('请选择');
			})
		},
		config: {
			action: {//页面请求参数
  				exports: urls['cfm'] + '/instRemindPhone/exportInstRemindPhoneList2',
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				close:function(){
					$("#menuContentModel").hide();
					_grid.handler.close();
				}
			},
  			dataGrid: {//表格参数  				
	   			url: urls['cfm'] + '/instRemindPhone/queryList',
	   			queryParams: {flag: '2','merchNames':'全部商户'},
	   			singleCheck:true,
	   			onPageChanged : function(page){
	   				_this.cache.page = page;
	   			}
			} 
		},
		exports : function(){
			$("#btn-export").click(function(){
				var merchNames = $("#merchNames").val();
				var mobileCollection = $("#mobileCollection").val();
				var orderId = $("#orderId").val();
				var regId = $("#regId").val();
				var realName = $("#realName").val();
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				var overdueDays = $("#overdueDays").val();
				var mobileDealOpinion = $("#mobileDealOpinion").val();
				var datas = "?merchNames=" + merchNames + "&mobileCollection=" + mobileCollection + "&orderId=" + orderId + "&regId=" + regId + "&realName=" + realName + "&startDate=" + startDate + "&endDate=" + endDate+ "&mobileDealOpinion=" + mobileDealOpinion+ "&flag=" + 2+ "&overdueDays=" + overdueDays ; 
	            var urls = _this.config.action.exports + datas;
	            console.log(urls)
	            $("#btn-export").attr("href",urls);
			});
		},
		addPlan : function(){
			$("#btn-insert1").click(function(){
				  var rows = $("#datagrid").datagrid2('getCheckedRows');
				  if(rows.length > 0){
						if(rows.length > 1){
							  IQB.alert('请选中单行');
						}else{
							var url = window.location.pathname;
							var param = window.parent.IQB.main2.fetchCache(url);
							window.parent.IQB.main2.openTab("urging", "电催管理", encodeURI('/etep.web/view/cfm/cfmSys/orderApprovalNew/telephoneUrgingAct.html?orderId=' + rows[0].orderId+ '&curItems=' + rows[0].curItems), true, true, {lastTab: param});
						}
					}else{
						IQB.alert('未选中行');
					} 
			});
		},
		updatePlan:function(){
			$("#btn-update1").click(function(){
				var rows = $("#datagrid").datagrid2('getCheckedRows');
				if(rows.length > 0){
					if(rows.length > 1){
						  IQB.alert('请选中单行');
					}else{
						  var rows = $("#datagrid").datagrid2('getCheckedRows');
						  window.parent.IQB.main2.openTab("urgingDetail", "电催详情", encodeURI('/etep.web/view/cfm/cfmSys/orderApprovalNew/telephoneUrgingDetail.html?orderId=' + rows[0].orderId+ '&merchantName=' + rows[0].merchantName+ '&realName=' + rows[0].realName+ '&regId=' + rows[0].regId), true, true, null);  
					}
				}else{
					IQB.alert('未选中行');
				}
			})
		},
		riskInfoShow : function(){
			var rows = $("#datagrid").datagrid2('getCheckedRows');
			if(rows.length > 0){
				IQB.post(urls['cfm'] + '/instRemindPhone/getRiskInfoByOrderId', {"orderId":rows[0].orderId,"flag":2}, function(result){
					if(result.success == '1'){
						$('#update-win').modal('show');
						var result = result.iqbResult.result;
						if(result != null){
							$('#contactname1').text(Formatter.ifNull(result.contactname1));
							$('#contactphone1').text(Formatter.ifNull(result.contactphone1));
							$('#contactname2').text(Formatter.ifNull(result.contactname2));
							$('#contactphone2').text(Formatter.ifNull(result.contactphone2));
							$('#smsMobile').text(Formatter.ifNull(result.smsMobile));
							$('#phone').text(Formatter.ifNull(result.phone));
							$('#addprovince').text(Formatter.ifNull(result.addprovince));
							$('#marriedstatus').text(Formatter.ifNull(result.marriedstatus));
						}
					}
				})
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
	      	    $('#update-win').modal('hide');
			});
		},
        initSelect : function(){
            IQB.getDictListByDictType2('mobileCollection', 'telephone_urging');
            IQB.getDictListByDictType2('mobileDealOpinion', 'treatment_suggestion');
            $('select[name="mobileCollection"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
            $('select[name="mobileDealOpinion"]').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
        },
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			_this.exports();//导出
			_this.initSelect();
			_this.addPlan();
			_this.reset();
			_this.updatePlan();
			$(".merchModel").click(function(){
        	    merchantFlag=1;
                showDataHtml="";
                $('#bizType option').remove();
            });
			$('#btn-riskInfo').on('click',function(){
				_this.riskInfoShow();
			});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.telephoneUrging.init();
	datepicker(startDate);
	datepicker(endDate);
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