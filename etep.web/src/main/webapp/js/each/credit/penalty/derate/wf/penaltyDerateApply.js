$package('IQB.reliefApply');
IQB.reliefApply = function() {
	var _box = null;
	var _this = {
		cache:{
			viewer: {},
			curRepayAmt:'',
			seqNo : '',
			installTerms : '',
			repayNo : '',
			orderId : ''
		},
		config:{
			action:{
				getById: urls['rootUrl'] + '/unIntcpt-penaltyDerate/listPenaltyDeratable',
				getByDetailId: urls['rootUrl'] + '/unIntcpt-penaltyDerate/getPenaltyDerateByInstallDetailId'
			},
			dataGrid: {
				url: urls['rootUrl'] + '/unIntcpt-penaltyDerate/listPenaltyDeratable',
				singleCheck: true
			}
		},
		overdueInterest: function(val, row, rowIndex){
			var overdueInterest = val; 
			if(!isNaN(row.cutOverdueInterest)){
				overdueInterest = parseFloat(overdueInterest)-parseFloat(row.cutOverdueInterest);
			}
			return Formatter.money(overdueInterest);
		},
		penaltyWin:function(){
			$('#btn-save').removeAttr('disabled');
			$('.thumbnail').remove();
			var records = _box.util.getCheckedRows();
			if (_box.util.checkSelectOne(records)) {
				_this.cache.orderId = records[0].orderId;
				_this.cache.installTerms = records[0].installTerms;
				_this.cache.repayNo = records[0].repayNo;
				var option = {};
				option['bid'] = records[0].id;
				option['installDetailId'] = records[0].installDetailId; 
				option['orderId'] = records[0].orderId; 
				IQB.getById(_this.config.action.getByDetailId, option, function(result){
					if(result.iqbResult != null){
						_this.cache.seqNo = result.iqbResult.seqNo;
					}
					if("00000000" == result.retCode){
						IQB.getById(_this.config.action.getById, option, function(result){	
				    		$("#updateForm")[0].reset();  
				    		$("#updateForm").form('load',result.iqbResult.result.list[0]);
				    		
				    		//减免后罚息金额--减免后滞纳金  金额默认显示未减免的金额
				    		$('#cutOverdueInterest_after').val(result.iqbResult.result.list[0].curRepayOverdueInterest);
				    		$('#cutFixedOverdueAmt_after').val(result.iqbResult.result.list[0].fixedOverdueAmt);
				    		
				    		var curRepayOverdueInterest = result.iqbResult.result.list[0].curRepayOverdueInterest;
				    		$("#curRepayOverdueInterest").val(parseFloat(curRepayOverdueInterest).toFixed(2));
				    		
				    		var curRepayAmt = result.iqbResult.result.list[0].curRepayAmt - curRepayOverdueInterest - result.iqbResult.result.list[0].fixedOverdueAmt;
				    		var monthInterest = result.iqbResult.result.list[0].curRepayPrincipal+result.iqbResult.result.list[0].curRepayInterest+result.iqbResult.result.list[0].otherAmt;
				    		$("#monthInterest").val(parseFloat(monthInterest).toFixed(2));
				    		_this.cache.curRepayAmt = parseFloat(curRepayAmt).toFixed(2);
				    		
				    		var fixedOverdueAmt = result.iqbResult.result.list[0].fixedOverdueAmt;
				    		$("#fixedOverdueAmt").val(parseFloat(fixedOverdueAmt).toFixed(2));
				    		
				    		$("#cutFixedOverdueAmt_after").val("0");
				    		
				    		$('#penalty-win').modal({backdrop: 'static', keyboard: false, show: true});
				    	});
					}else{
						IQB.alert("已做过罚息减免申请");
						_box.handler.refresh();	
					}
		    	});
			}
		},
		savePanalty : function() {
			var panaltyForm = $('#updateForm').serializeObject();
			panaltyForm.curRepayAmt = _this.cache.curRepayAmt;
			
			var derateType = panaltyForm.derateType;
			if("2" == derateType){
				if(undefined == panaltyForm.onlinePayFailImg||"" == panaltyForm.onlinePayFailImg||"undefined" == panaltyForm.onlinePayFailImg){
					IQB.alert("无责减免必传线上支付失败截图");
					$('#btn-save').removeAttr('disabled');
					return ;
				}
				if(undefined == panaltyForm.debitBankWaterImg||"" == panaltyForm.debitBankWaterImg||"undefined" == panaltyForm.debitBankWaterImg){
					IQB.alert("无责减免必传扣款银行日流水");
					$('#btn-save').removeAttr('disabled');
					return ;
				}
			}
			var procData = {}
			procData.procDefKey = 'penalty_derate';
			
			var authData= {}
			authData.procAuthType = "2";
			
			var variableData = {}
			variableData.procAuthType = "2";
			variableData.procApprStatus = "1";
			
			var bizData = {}
			bizData.penaltyDerateInfo=JSON.stringify(panaltyForm);
			bizData.procBizMemo=panaltyForm.realName+";"+panaltyForm.orderId+";"+panaltyForm.repayNo;
			bizData.procBizId = _this.cache.orderId + '-' + _this.cache.repayNo + '-' + _this.cache.installTerms + '-' + _this.cache.seqNo;
			console.log(JSON.stringify(panaltyForm));
			var option = {};
			option.authData=authData;
			option.variableData = variableData;
			option.bizData = bizData;
			option.procData = procData;
			console.log(option);
			
			IQB.post(urls['rootUrl'] + '/WfTask/startAndCommitProcess', option, function(result){
				if(result.success=="1") {
					$('#penalty-win').modal('hide');
					$('#btn-save').removeAttr('disabled');
					_box.handler.refresh();	
				} else {
					IQB.alert(result.retUserInfo);
					_box.handler.refresh();	
					$('#btn-save').removeAttr('disabled');
				}
			});
			_box.handler.refresh();
			$(".modal-content").scrollTop(0);
		},
		closePenaltyWin : function() {
			$('#penalty-win').modal('hide');
			_box.handler.refresh();
			$(".modal-content").scrollTop(0);
		},
		uploadFile: function(){
			var files = $('#file').get(0).files;
			var mark = false;
			$.each(files, function(i, n){
				var extensionName = Formatter.getExtensionName(n.name);
				if(Formatter.extensionName.doc.contain(extensionName)){
					mark = true;				
					return false;
				}else if(Formatter.extensionName.pic.contain(extensionName)){
					
				}else{
					mark = true;						
					return false;
				}
			});			
			if(mark){
				$('#file').val('');
				IQB.alert('格式不支持');
				return false;
			}

			$('#btn-upload').prop('disabled', true);
			$('#btn-upload').find('span').first().removeClass('fa fa-folder-open-o').addClass('fa fa-spinner fa-pulse');
			$('#uploadForm').prop('action', urls['cfm'] + '/fileUpload/multiUpload/pic/penalty');
			
			IQB.postForm($('#uploadForm'), function(result){
				var fileType = $('#file').attr('fileType');	
				var arr = [];
				var html = '';
				$.each(files, function(i, n){
					var option = {};
					option.orderId = window.procBizId;
					option.imgType = fileType;
					option.imgName = n.name;
					option.imgPath = result.iqbResult.result[i];
					arr.push(option);
					html += '<div class="thumbnail float-left" style="width: 145px;">' + 
					      		'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + option.imgPath + '" alt="' + option.imgName + '" style="width: 135px; height: 135px;" /></a>' +
					      		'<input type="hidden" name="'+fileType+'" value="'+option.imgPath+'">' + 
					      		'<div class="caption">' +
					      		 	'<h5>' + option.imgName + '</h5><h6><a filePath="' + option.imgPath + '" onclick="IQB.reliefApply.removeFile(event);"><span class="glyphicon glyphicon-trash"></span></a></h6>' +
					      		 '</div>' + 
					      	 '</div>';
				});
				$('#file').val('');
				$('#btn-upload').prop('disabled', false);
				$('#btn-upload').find('span').first().removeClass('fa fa-spinner fa-pulse').addClass('fa fa-folder-open-o');
				$('#td-' + fileType).append(html);
				
				if(_this.cache.viewer.viewerOne){
					_this.cache.viewer.viewerOne.update();
				}else{
					_this.cache.viewer.viewerOne = new Viewer(document.getElementById('viewerOne'), {});
				}
			});	
		},
		removeFile: function(event){
			if(event.stopPropagation){//W3C阻止冒泡方法  
				event.stopPropagation();  
			}else{//IE阻止冒泡方法   
				event.cancelBubble = true;
			}  	
			var tarent = event.currentTarget;
			$(tarent).prop('disabled', true);
			$(tarent).find('span').first().removeClass('glyphicon glyphicon-trash').addClass('fa fa-spinner fa-pulse');
			$(tarent).parent().parent().parent().remove();
		},
		sumCutAmt: function(v1,v2,v3){
			var sumResult = 0;
			if(isNaN(v1)||""==v1){
				v1=0;
			}
			if(isNaN(v2)||""==v2){
				v2=0;
			}
			if(isNaN(v3)){
				v3=0;
			}
			sumResult = parseFloat(v1)+parseFloat(v2)+parseFloat(v3);
			return parseFloat(sumResult).toFixed(2);
		},
		init : function() {
			_box = new DataGrid2(_this.config);
			_box.init();
			
			
			$('#btn-penalty').on('click', function(){
				_this.penaltyWin();
			});
			
			//计算减免事件
			$('#cutOverdueInterest_on').on('blur',function(){//罚息
				var curRepayOverdueInterest = $('#curRepayOverdueInterest').val();
				if(parseFloat(curRepayOverdueInterest)>0){
					var cutOverdueInterest = this.value;
					var result=curRepayOverdueInterest;
					if(!isNaN(cutOverdueInterest)){
						result = parseFloat(curRepayOverdueInterest)-parseFloat(cutOverdueInterest);
					}
					if(result<0){
						IQB.alert("减免金额不能大于罚息金额");
						_box.handler.refresh();	
					}
					$('#cutOverdueInterest_after').val(parseFloat(result).toFixed(2));
					$('#cutAmt_sum').val(_this.sumCutAmt(cutOverdueInterest,$('#cutFixedOverdueAmt_on').val(),"0"));
					$('#curRepayAmt_sum').val(_this.sumCutAmt(result,$('#cutFixedOverdueAmt_after').val(),$('#monthInterest').val()));
				}else{
					this.value = "0.00";
					$('#cutOverdueInterest_after').val("0.00");
					$('#cutAmt_sum').val(_this.sumCutAmt(0,$('#cutFixedOverdueAmt_on').val(),0));
					$('#curRepayAmt_sum').val(_this.sumCutAmt(0,$('#cutFixedOverdueAmt_after').val(),$('#monthInterest').val()));
					}
			});
			
			$('#cutFixedOverdueAmt_on').on('blur',function(){//滞纳金
				var fixedOverdueAmt = $('#fixedOverdueAmt').val();
				if(parseFloat(fixedOverdueAmt)>0){
					var cutFixedOverdueAmt = this.value;
					var result=fixedOverdueAmt;
					if(!isNaN(cutFixedOverdueAmt)){
						result = parseFloat(fixedOverdueAmt)-parseFloat(cutFixedOverdueAmt);
					}
					if(result<0){
						IQB.alert("减免金额不能大于滞纳金金额");
					}
					$('#cutFixedOverdueAmt_after').val(parseFloat(result).toFixed(2));
					$('#cutAmt_sum').val(_this.sumCutAmt(cutFixedOverdueAmt,$('#cutOverdueInterest_on').val(),0));
					$('#curRepayAmt_sum').val(_this.sumCutAmt($('#cutOverdueInterest_after').val(),result,$('#monthInterest').val()));
				}else{
					this.value = "0.00";
					$('#cutFixedOverdueAmt_after').val("0.00");
					$('#cutAmt_sum').val(_this.sumCutAmt(0,$('#cutOverdueInterest_on').val(),0));
					$('#curRepayAmt_sum').val(_this.sumCutAmt(0,$('#cutOverdueInterest_after').val(),$('#monthInterest').val()));
				}
			});
			
			$('#btn-save').unbind('click').on('click', function(){
				if($('#updateForm').form("validate")){
					//FINANCE-3100  逾期罚息减免页面中，应还总金额大于等于月供金额；
				    if(Number($('#curRepayAmt_sum').val()) < Number(_this.cache.curRepayAmt)){
				    	IQB.alert('应还总额须大于等于月供金额');
				    	return false;
				    }else{
				    	$(this).attr('disabled','true');
						_this.savePanalty();
				    }
				}
			});
			$('#btn-close').click(_this.closePenaltyWin);
			
			$('#btn-uploadTypeOne').on('click', function(){$('#file').attr('fileType', "onlinePayFailImg");$('#file').click();});
			$('#btn-uploadTypeTwo').on('click', function(){$('#file').attr('fileType', "debitBankWaterImg");$('#file').click();});
			$('#btn-uploadTypeThree').on('click', function(){$('#file').attr('fileType', "otherImg");$('#file').click();});
			$('#file').on('change', function(){var fileName = $('#file').val();if(fileName){_this.uploadFile();}});		
		}
	}
   return _this;
}();

$(function() {
	IQB.reliefApply.init();
});

