$package('IQB.documentQuery');
IQB.documentQuery = function(){
	var _grid = null;
	var _tree = null;
	var _this = {
		cache :{

		},
		config: {
			action: {//页面请求参数
  				
  			},
			event: {//按钮绑定函数，不定义的话采用默认
				search:function(){
					var orgCode = $(".merch").attr('orgCode');
					_this.cache.orgCode = orgCode;
					var merchantNo = $(".merch").attr('merchantNo');
					_this.cache.merchantNo = merchantNo;
					$("#datagrid").datagrid2
							({
							 url: urls['cfm'] + '/overdueInfo/queryArchivesList',
							 queryParams : 
								 $.extend({}, $("#searchForm").serializeObject(), 
										 {'orgCode': orgCode,'merchantNo':merchantNo})	
							});
				}
			},
  			dataGrid: {//表格参数  				
  				url: urls['cfm'] + '/overdueInfo/queryArchivesList',
  				singleCheck:true
			} 
		},
		resetWin : function(){
			$('.text-muted').html('');
			$('.EReport').removeAttr('href');
			$('#evaluateReason').val('');
			$('.riskReports').html('');
			$('.riskReports').find('a').remove();
			$('.contracts').html('');
			$('.contracts').find('a').remove();
			$('td').find('div').remove();
			$('td').find('video').remove();
			//清楚图片缓存
		},
		detail : function(){
			var records = _grid.util.getCheckedRows();
			var orderId = records[0].orderId,bizType = records[0].bizType, orgCode = records[0].orgCode, vin = records[0].vin;
			_this.cache.orderId = records[0].orderId;
			//var orderId = 'TYLD2002180302002';
			//_this.cache.orderId = 'TYLD2002180302002';
			if(records.length > 0){
				if(records.length > 1){
					IQB.alert('请选中单行');
				}else{
				   $('#open-win').modal('show');
				   //对tab页数据进行处理--订单信息
				   _this.initApprovalTask(orderId);
				   _this.sublet(orderId);
				   _this.showPreFee(orderId);
				   //车辆信息
				   /*_this.report(orderId,vin);
				   IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': orderId}, function(result){
						$("#VIN").html(result.iqbResult.AuthorityCardInfo.carNo);
						$("#engineNo").html(result.iqbResult.AuthorityCardInfo.engine);
						$("#licenseNumber").html(result.iqbResult.AuthorityCardInfo.plate);
						if(result.iqbResult.AuthorityCardInfo.plateType == '01'){
							$("#carType").html('大型汽车');
						}else if(result.iqbResult.AuthorityCardInfo.plateType == '02'){
							$("#carType").html('小型汽车');
						}else if(result.iqbResult.AuthorityCardInfo.plateType == '15'){
							$("#carType").html('挂车');
						}
				    })*/
					//风控信息
					//_this.riskReport(orderId);
				    /*IQB.post(urls['cfm'] + '/business/getApprovalOpinion', {'orderId': orderId}, function(result){
						var result = result.iqbResult.result;
						//风控初审意见  电核意见
						$('#riskFirstOption').text(Formatter.ifNull(result.riskApprovalOpinion));
						$('#phoneCheckOption').text(Formatter.ifNull(result.telApprovalOpinion));
					})
					IQB.post(urls['cfm'] + '/image/getImageList', {orderId: orderId, imgType: [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]}, function(result){
						var is = false;
						$.each(result.iqbResult.result, function(i, n){
							var extensionName = Formatter.getExtensionName(n.imgName);
							if(Formatter.extensionName.doc.contain(extensionName)){
								
							}else{
								var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
								      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
								      			'<div class="caption">' +
								      				'<h5>' + n.imgName + '</h5>' +
								      			'</div>' + 
								      		'</div>';
								$('#td-' + n.imgType).append(html);
								is = true;
							}
						});
						if(is){
							_this.cache.viewer = new Viewer(document.getElementById('open-win'), {});
						}
					});*/
				    //合同信息
					//var orderId = "CDHTC2002171130001",bizType = '2002',orgCode='1020002';
					_this.contract(orderId, bizType, orgCode);
					var is = false;
					IQB.post(urls['cfm'] + '/image/getImageList', {orderId: orderId, imgType: [1, 2, 3, 4, 6, 7, 8, 9, 100]}, function(result){
						$('#projectName').text(Formatter.ifNull(result.iqbResult.projectName));
						$('#guarantee').text(Formatter.ifNull(result.iqbResult.guarantee));
						$('#guaranteeName').text(Formatter.ifNull(result.iqbResult.guaranteeName));
						
						
						$.each(result.iqbResult.result, function(i, n){
							var extensionName = Formatter.getExtensionName(n.imgName);
							if(Formatter.extensionName.doc.contain(extensionName)){
								
							}else{
								var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
								      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
								      			'<div class="caption">' +
								      				'<h5>' + n.imgName + '</h5>' +
								      			'</div>' + 
								      		'</div>';
								$('#td-' + n.imgType).append(html);
								is = true;
							}
						});
						if(is){
							_this.cache.viewer = new Viewer(document.getElementById('open-win'), {});
						}
					});
					IQB.post(urls['cfm'] + '/admin/contract/get_ioce_by_oid', {'orderId': orderId}, function(result){
						var result = result.iqbResult.result;
						if(result != null){
							$('#loanContractNo').text(result.loanContractNo);
							$('#guarantyContractNo').text(result.guarantyContractNo);
							$('#leaseContractNo').text(result.leaseContractNo);
							$('#contractDate').text(Formatter.timeCfm5(result.contractDate));
							$('#contractEndDate').text(Formatter.timeCfm5(result.contractEndDate));
						}
					});
					//权证资料
					IQB.post(urls['cfm'] + '/authoritycard/querryByOrderId', {'orderId': orderId}, function(result){
						if(result.iqbResult.AuthorityCardInfo != null){
							$("#VIN2").html(result.iqbResult.AuthorityCardInfo.carNo);
							$("#engineNo2").html(result.iqbResult.AuthorityCardInfo.engine);
							$("#plate").html(result.iqbResult.AuthorityCardInfo.plate);
							$("#inCarTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.getCarDate));
							$("#annualInspectionTtime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.checkDate));
							$("#GPSTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.gpsInstDate));
							$("#insuranceStartTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceStart));
							$("#insuranceEndTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.insuranceEnd));
							$("#identifyNumber").html(result.iqbResult.AuthorityCardInfo.lineGpsNo);	
							$("#noIdentifyNumber").html(result.iqbResult.AuthorityCardInfo.nolineGpsNo);
							$("#commercialInsuranceStartTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksStart));
							$("#commercialInsuranceEndTime").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.bizRisksEnd));
							$("#identifyNumberLocation").html(result.iqbResult.AuthorityCardInfo.lineGpsInstAdd);
							$("#noIdentifyNumberLocation").html(result.iqbResult.AuthorityCardInfo.noLineGpsInstAdd);
							$("#mortgageFlag").html(Formatter.isSuperadmin(result.iqbResult.AuthorityCardInfo.mortgageFlag));
							$("#mortgageDate").html(Formatter.timeCfm2(result.iqbResult.AuthorityCardInfo.mortgageDate));
						}
					})
					var is2 = false;
					IQB.post(urls['cfm'] + '/image/getImageList', {orderId: orderId, imgType: [28,29,30,31,32,35,36,37]}, function(result){
						
						$.each(result.iqbResult.result, function(i, n){
							var extensionName = Formatter.getExtensionName(n.imgName);
							if(Formatter.extensionName.doc.contain(extensionName)){
								
							}else{
								var html = '<div class="thumbnail float-left" style="width: 145px;">' + 
								      			'<a href="javascript:void(0)"><img src="' + urls['imgUrl'] + n.imgPath + '" alt="' + n.imgName + '" style="width: 135px; height: 135px;" /></a>' +
								      			'<div class="caption">' +
								      				'<h5>' + n.imgName + '</h5>' +
								      			'</div>' + 
								      		'</div>';
								$('.td-' + n.imgType).append(html);
								is2 = true;
							}
						});
						if(is2 && (!is)){
							_this.cache.viewer = new Viewer(document.getElementById('open-win'), {});
						}
						if(is && is2){
							_this.cache.viewer.update();
						}
					});
				}
			}else{
				IQB.alert('未选中行');
			}
			$("#btn-close").click(function(){
	      	     $('#open-win').modal('hide');
			});
		},
		initApprovalTask: function(orderId){//初始化订单详情
			IQB.post(urls['cfm'] + '/workFlow/getArtificialCheck', {orderId: orderId}, function(result){
				$('#merchName').text(result.merchName);
				$('#realName').text(result.realName);
				$('#regId').text(result.regId);
				$('#orderId').text(result.orderId);
				$('#groupName').text(Formatter.groupName(result.bizType));
				//处理车辆估价
				if(result.bizType == '2001'){
					$('.assessPrice-div').hide();
					$('.new-tr').show();
					$('.old-tr').hide();
				}else{
					$('#assessPrice').text(Formatter.money(result.assessPrice));
					$('.assessPrice-div').show();
					$('.old-tr').show();
					$('.new-tr').hide();
				}
				$('#orderAmt').text(Formatter.money(result.orderAmt));
				$('#planFullName').text(result.planFullName);
				$('#planId').text(result.planId);
				$('#chargeWay').text(Formatter.chargeWay(result.chargeWay));
				$('#preAmount').text(Formatter.money(result.preAmt));
				_this.cache.preAmount = result.preAmt;
				$('#downPayment').text(Formatter.money(result.downPayment));
				$('#margin').text(Formatter.money(result.margin));
				$('#serviceFee').text(Formatter.money(result.serviceFee));
				$('#feeAmount').text(Formatter.money(result.feeAmount));
				$('#orderItems').text(result.orderItems);
				$('#monthInterest').text(Formatter.money(result.monthInterest));
				$('#riskStatus').text(Formatter.riskStatus(result.riskStatus));	
				$('#greenChannel').text(Formatter.greenChannel(result.greenChannel));
				IQB.post(urls['cfm'] + '/business/selOrderInfo', {orderId: orderId}, function(resultChild){
					$('#carAmt').text(Formatter.money(resultChild.iqbResult.result.carAmt));
					$('#gpsAmt').text(Formatter.money(resultChild.iqbResult.result.gpsAmt));
					$("#jqInsurance").html(Formatter.money(resultChild.iqbResult.result.insAmt));
					$("#syInsurance").html(Formatter.money(resultChild.iqbResult.result.businessTaxAmt));
					$('#taxAmt').text(Formatter.money(resultChild.iqbResult.result.taxAmt));	
					$('#otherAmt').text(Formatter.money(resultChild.iqbResult.result.otherAmt));
				});
				//处理支付结果
				if(result.chargeWay == 1){
					$('.isPay-table').show();
					$('.isPay-div').hide();	
					$('.outOrderId-div').hide();	
				}else{
					$('.isPay-table').hide();
					$('.isPay-div').show();	
					$('#preAmountStatus').text(Formatter.preAmountStatus(result.preAmtStatus));
					//处理流水号
					if(result.preAmtStatus == 1){
						$('.outOrderId-div').show();	
					}else{
						$('.outOrderId-div').hide();	
					}
				}
				$('#gpsRemark').text((Formatter.ifNull(result.gpsRemark)));
				$('#receiveAmt').text((Formatter.money(result.receiveAmt)));
				$('#remark').text((Formatter.ifNull(result.remark)));
			});
			IQB.post(urls['cfm'] + '/creditorInfo/getCreditorInfo', {orderId: orderId}, function(result){
				if(result.iqbResult.result){
					$('#creditName').text(Formatter.ifNull(result.iqbResult.result.creditName));
					$('#creditCardNo').text(Formatter.ifNull(result.iqbResult.result.creditCardNo));
					$('#creditBankCard').text(Formatter.ifNull(result.iqbResult.result.creditBankCard));
					$('#creditBank').text(Formatter.ifNull(result.iqbResult.result.creditBank));
					$('#creditPhone').text(Formatter.ifNull(result.iqbResult.result.creditPhone));
				}				
			});
			IQB.post(urls['cfm'] + '/workFlow/getTranNoByOrderId', {orderId: orderId}, function(result){
				if(result){
					if(result != '' && result != null && result.length > 1){
						$('#outOrderIdMore').show();
						_this.cache.outOrderId = result;
					}else{
						$('#outOrderIdMore').hide();
						$('#outOrderId').text(result[0].outOrderId);	
					}
				}				
			});
		},
		sublet : function(orderId){
			IQB.post(urls['cfm'] + '/sublet/get_sublet_record', {'orderId': orderId}, function(result){
				if(result.iqbResult.result != null){
					var result = result.iqbResult.result;
					$('.noSublet').show();
					//赋值
					$('#sublet').html('是');
					$('#oldOrderId').html(result.subletOrderId);
					$('#oldRegId').html(result.subletRegId);
					$('#oldRealName').html(result.subletRealName);
					$('#planShortName').html(result.planShortName);
					$('#oldMonthInterest').html(result.monthInterest);
					$('#oldOrderItems').html(result.orderItems);
					$('#overItems').html(result.overItems);
					var rollOverFlag = result.rollOverFlag;
					if(rollOverFlag == 1){
						$('#rollOverFlag').html('是');
						$('.noRollOver').show();
						$('.noManageFee').hide();
						//赋值
						$('#rollOverItems').html(result.rollOverItems);
						var manageFeeFlag = result.manageFeeFlag;
						if(manageFeeFlag == 1){
							$('#manageFeeFlag').html('是');
							$('.noManageFee').show();
							$('#manageFee').html(Formatter.money(result.manageFee));
							//赋值
						}else{
							$('#manageFeeFlag').html('否');
							$('.noManageFee').hide();
						}
					}else{
						$('#rollOverFlag').html('否');
						$('.noRollOver').hide();
					}
				}else{
					$('#sublet').html('否');
					$('.noSublet').hide();
				}
			})
		},
		showPreFee : function(orderId){
			IQB.get(urls['cfm'] + '/order/otherAmt/getOtherAmtList/false', {'orderId': orderId}, function(result){
				var result = result.iqbResult.result[0];
				if(result != '' && result != null){
					if(result.preAmtFlag == 1){
						$('#isPreAmt').text('是');
						$('.isPreAmtShow').show();
						$('#online').text(Formatter.isSuperadmin(result.online));
						$('#totalCost').text(Formatter.money(result.totalCost));
						$('#gpsAmt2').text(Formatter.money(result.gpsAmt));
						$('#payJqInsurance').text(Formatter.money(result.riskAmt));
						$('#paySyInsurance').text(Formatter.money(result.preBusinessTaxAmt));
						$('#taxAmt2').text(Formatter.money(result.taxAmt));
						$('#serverAmt').text(Formatter.money(result.serverAmt));
						$('#assessMsgAmt').text(Formatter.money(result.assessMsgAmt));
						$('#inspectionAmt').text(Formatter.money(result.inspectionAmt));
						$('#preOtherAmt').text(Formatter.money(result.otherAmt));
						$('#preAmtAll').text(Formatter.money(_this.cache.preAmount));
					}else{
						$('#isPreAmt').text('否');
						$('.isPreAmtShow').hide();
					}
				}else{
					$('#isPreAmt').text('否');
					$('.isPreAmtShow').hide();
				}
			})
		},
		outOrderIdMore : function(){
			$('#open-win2').modal('show');
			var result = _this.cache.outOrderId;
			//赋值
			var tableHtml = '';
			for(var i=0;i<result.length;i++){
				tableHtml += "<tr><td>"+Formatter.moneyCent(result[i].amount)+
				"</td><td>"+Formatter.timeCfm2(result[i].tranTime)+"</td><td>"+
				result[i].outOrderId+"</td></tr>";
			}
			$(".outOrderIdTable").find('tbody').find('tr').remove();
			$(".outOrderIdTable").append(tableHtml);
			$("#btn-close2").click(function(){
        	    $('#open-win2').modal('hide');
			});
		},
		//车e估
		report : function(orderId, vin){
			IQB.post(urls['cfm'] + '/business/getCarAssessReport', {'orderId': orderId,'vin':vin}, function(result){
			    if(result.iqbResult.result.msg == 'success'){
			    	if(result.iqbResult.result.carReportUrl != ''){
			    		$('.EReport').attr('href',result.iqbResult.result.carReportUrl);
			    	}else{
			    		$('.EReport').removeAttr('href');
			    	}
			    	if($('.EReport').attr('href') == ''){
			    		$('.reportAbout').hide();
			    		$('.evaluateReason').show();
						$('#evaluateReason').val(result.iqbResult.result.refuseMsg);
			    	}else{
			    		$('.reportAbout').show();
			    		$('.evaluateReason').hide();
			    	}
			    	//加载视频
					if(result.iqbResult.result.videolist && result.iqbResult.result.videolist.length > 0){
						$('.videoInfo').show();
						$.each(result.iqbResult.result.videolist, function(i, n){
							var videoHtml = '<video src="'+n.url+'" controls="controls" width="250px;" height="300px">您的浏览器不支持。</video>';
							$('#td-31').append(videoHtml);
						});
					}else{
						$('.videoInfo').hide();
					}
					//加载图片
					var is2 = false;
					$.each(result.iqbResult.result.imageList, function(i, n){
						var html = '<div class="thumbnail float-left" style="width: 145px;margin-top:8px;">' + 
									      			'<a href="javascript:void(0)"><img src="' + n.url + '" alt="' + n.tag + '" style="width: 135px; height: 135px;" /></a>' +
									      			'<div class="caption">' +
									      				'<h5>' + n.tag + '</h5>' +
									      			'</div>' + 
									      		'</div>';
						            $('#td-30').append(html);
									is2 = true;
					});
					if(is2){
						_this.cache.viewer.update();
					}
			    }else{
			    	IQB.alert(result.iqbResult.result.msg);
			    }
			});
		},
		showReport : function(reportUrl){
			if($('.EReport').attr('href') == '' || $('.EReport').attr('href') == undefined){
				IQB.alert('未返回评估报告，请核实');
			}
		},
		//风控报告
		riskReport : function(orderId){
			IQB.post(urls['cfm'] + '/afterLoan/getReportList', {'orderId': orderId}, function(result){
			    //加载报告列表
				var riskHtml = '';
				if(result.success == 1){
					$.each(result.iqbResult.result,function(i,n){
						riskHtml += '<a class="riskReport" onclick="IQB.documentQuery.showRiskReport('+n.reportType+')">'+n.reportName+'</a>';
					});
				}else{
					riskHtml = result.result;
				}
				$('.riskReports').find('a').remove();
				$('.riskReports').html('');
				$('.riskReports').append(riskHtml);
			});
		},
		showRiskReport : function(type){
			var url;
			if(type == '1'){
				//个人风控信息
				IQB.post(urls['cfm'] + '/afterLoan/getReportByReprotNo', {'orderId': _this.cache.orderId,'reportType':1}, function(result){
					if(result.pojo.result == null){
						IQB.alert('未生成报告,稍后再试');
						return false;
					}else{
						url = '/etep.web/view/riskReport/reports/html/info_report.html?orderId='+_this.cache.orderId+'&reportType=1';
						url = encodeURI(url);
						window.open(url);
					}
				})
			}else{
				//贷前反欺诈
				IQB.post(urls['cfm'] + '/afterLoan/getReportByReprotNo', {'orderId': _this.cache.orderId,'reportType':2}, function(result){
					if(result.pojo.result == null){
						IQB.alert('未生成报告,稍后再试');
						return false;
					}else{
						url = '/etep.web/view/riskReport/reports/html/loanfraud_report.html?orderId='+_this.cache.orderId+'&reportType=2';
						url = encodeURI(url);
						window.open(url);
					}
				})
			}
		},
		contract: function(orderId, bizType, orgCode){
			IQB.post(urls['cfm'] + '/contract/unIntcpt-selContracts', {'bizId': orderId, 'bizType':bizType, 'orgCode':orgCode}, function(result){
			    //加载报告列表
				var contractHtml = '';
				if(result.success == 1){
					$.each(result.iqbResult.result.ecList,function(i,n){
						contractHtml += '<a class="contractReport" href="'+n.ecViewUrl+'" target="_blank">'+n.ecName+'</a>';
					});
				}else{
					contractHtml = result.result;
				}
				$('.contracts').find('a').remove();
				$('.contracts').html('');
				$('.contracts').append(contractHtml);
			});
		},
		init: function(){ 
			_grid = new DataGrid2(_this.config); 
			_grid.init();//初始化按钮、表格
			//违章详情
			$('#btn-detail').unbind('click').on('click',function(){
				_this.resetWin();
				_this.detail();
			});
		}
	}
	return _this;
}();
$(function(){
	//页面初始化
	IQB.documentQuery.init();
	datepicker(startTime);
	datepicker(endTime);
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