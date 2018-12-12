 $(function(){
//	获取id
	function GetQueryString(name){
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null)return  unescape(r[2]); return null;
	}
//  是否为空
	function isNull(val){
		if(val != null){
			return val;
		}
		else{
			return '';
		}
	}
//  判断社会关系
	function isContact(val){
		if(val == 'coworker'){
			return '同事';
		}
		else if (val == 'mother'){
			return '母亲';
		}else if (val == 'father'){
			return '父亲';
		}else if (val == 'other_relative'){
			return '其他亲属';
		}else if (val == 'friend'){
			return '朋友';
		}else if (val == 'spouse'){
			return '配偶';
		}else if (val == 'others'){
			return '其他';
		}else if (val == 'child'){
			return '子女';
		}else{
			return val;
		}
	}
		// 回显数据
		var data = {};
		data.orderId = GetQueryString("orderId");
		data.reportType = Number(GetQueryString("reportType"));
		//	数据显示
		$.ajax({
         type:"post",
         url:urls['cfm'] + '/afterLoan/getReportByReprotNo',
         dataType: 'json',
         contentType: "application/json",
         data : JSON.stringify(data),
         success : function(result){
        	 if(result.hasOwnProperty("success") && result.success == '2'){
        		 IQB.alert(result.retUserInfo);
        	 }
        	 if(result.hasOwnProperty("pojo") && result.pojo.result != null){ 
            	 var personal =result.pojo;
         		//  查询时间 报告时间            		
         		$("#createTime").text(personal.createTime);
         		$("#updateTime").text(personal.updateTime);
         		//  借款人信息   		
         		$("#name").text(personal.name);
         		$("#idNo").text(personal.idNo);
         		$("#phone").text(personal.phone);
        		$(".personal").show();
         		var together = result.pojo;
         		if(together){
         			$("#bankCard").text(together.bankNum);
         			if(together.contact1Name||together.contact1IdNumber||together.contact1Mobile||together.contact1Relation){
         				$("#contact1").show();
         				$("#contact1Name").text(together.contact1Name); 
         				$("#contact1IdNumber").text(together.contact1IdNumber);
         				$("#contact1Mobile").text(together.contact1Mobile);
         				$("#contact1Relation").text(isContact(together.contact1Relation));
         			}
         			if(together.contact2Name||together.contact2IdNumber||together.contact2Mobile||together.contact2Relation){
         				$("#contact2").show();
         				$("#contact2Name").text(together.contact2Name);
         				$("#contact2IdNumber").text(together.contact2IdNumber);
         				$("#contact2Mobile").text(together.contact2Mobile);
         				$("#contact2Relation").text(isContact(together.contact2Relation));
         			}
         			if(together.contact3Name||together.contact3IdNumber||together.contact3Mobile||together.contact3Relation){
         				$("#contact3").show();
         				$("#contact3Name").text(together.contact3Name);
         				$("#contact3IdNumber").text(together.contact3IdNumber);
         				$("#contact3Mobile").text(together.contact3Mobile);
         				$("#contact3Relation").text(isContact(together.contact3Relation));
         			}
         			if(together.contact4Name||together.contact4IdNumber||together.contact4Mobile||together.contact4Relation){
         				$("#contact4").show();
         				$("#contact4Name").text(together.contact4Name);
         				$("#contact4IdNumber").text(together.contact4IdNumber);
         				$("#contact4Mobile").text(together.contact4Mobile);
         				$("#contact4Relation").text(isContact(together.contact4Relation));
         			}
         			if(together.contact5Name||together.contact5IdNumber||together.contact5Mobile||together.contact5Relation){
         				$("#contact5").show();
         				$("#contact5Name").text(together.contact5Name);
         				$("#contact5IdNumber").text(together.contact5IdNumber);
         				$("#contact5Mobile").text(together.contact5Mobile);
         				$("#contact5Relation").text(isContact(together.contact5Relation));
         			}
         		}
         		//  生成CODE128二维码
         		$(".bcTarget").barcode(personal.reportNo, "code128",{barWidth:1, barHeight:20});
            	 if(personal){
         			$(".courtMissNull").show();
         			$(".courtHisNull").show();
         			$(".courtFinNull").show();
         			$(".greyDetailNull").show();
         			$(".fuzzyBoxNull").show();
         			$(".discreditBoxNull").show();
         			$(".frequencyBoxNull").show();
         			$(".crossFrequencyBoxNull").show();
         			$(".platformBoxNull").show();
         			$(".customBoxNull").show();
         			$(".deviceNull").show();
            		if(result.pojo.result==""){
            			
            		}
            		else{
            		var  personalResult=result.pojo.result;
                    if(personalResult.ANTIFRAUD){
                    	$(".infoMajor").show();
                    	$(".infoMajorL").show();
                    	//   风险分数 风险等级 风险建议                 	
                    	$("#final_score").text(personalResult.ANTIFRAUD.final_score);
                    	$("#final_decision_level").text(personalResult.ANTIFRAUD.final_decision_level);
                    	$("#final_decision").text(personalResult.ANTIFRAUD.final_decision);
                    	//    司法风险检测                	
                    	if(personalResult.ANTIFRAUD.black_list&&personalResult.ANTIFRAUD.black_list.length>0){
                    		//   身份证命中法院失信\执行\结案名单             		
                    		if(personalResult.ANTIFRAUD.black_list[0].risk_detail){
                    			$(".courtMissNull").hide();
                    			$(".courtMiss1").show();
                    			$(".courtMiss").show();
                    			$(".courtMissBoxTitle").text(personalResult.ANTIFRAUD.black_list[0].risk_name);
                    			var court = personalResult.ANTIFRAUD.black_list[0].risk_detail;
                    			var courtMissHtml="";
                    			for(i=0;i<court[0].court_details.length;i++){
                    				courtMissHtml +="<tr><td id='td-property'>被执行人姓名</td><td><span>"+isNull(court[0].court_details[i].executed_name)+"</span></td><td id='td-property'>案号</td><td><span>"+isNull(court[0].court_details[i].case_code)+"</span></td><td id='td-property'>执行依据文号</td><td><span>"+isNull(court[0].court_details[i].execute_code)+"</span></td></tr><tr><td id='td-property'>立案时间</td><td><span>"+isNull(court[0].court_details[i].case_date)+"</span></td><td id='td-property'>执行法院</td><td><span>"+isNull(court[0].court_details[i].execute_court)+"</span></td><td id='td-property'>执行状态</td><td><span>"+isNull(court[0].court_details[i].execute_status)+"</span></td></tr><tr><td id='td-property'>执行标的</td><td><span>"+isNull(court[0].court_details[i].execute_subject)+"</span></td><td id='td-property'>被执行人履行情况</td><td><span>"+isNull(court[0].court_details[i].carry_out)+"</span></td><td id='td-property'>风险类型</td><td><span>"+isNull(court[0].court_details[i].fraud_type_display_name)+"</span></td></tr><tr><td id='td-property'>被执行人具体情形</td><td colspan='5'><span>"+isNull(court[0].court_details[i].specific_circumstances)+"</span></td></tr><tr><td id='td-property'>生效法律文书确定的义务</td><td colspan='5'><span>"+isNull(court[0].court_details[i].term_duty)+"</span></td></tr>";
                    			}
                    			$("#courtMissBox").append(courtMissHtml);
                    		}
                    		if(personalResult.ANTIFRAUD.black_list[1].risk_detail){
                    			$(".courtHisNull").hide();
                    			$(".courtMiss1").show();
                    			$(".courtDone").show();                    			
                    			$(".courtDoneBoxTitle").text(personalResult.ANTIFRAUD.black_list[1].risk_name);
                    			var court1 = personalResult.ANTIFRAUD.black_list[1].risk_detail;
                    			var courtDoneHtml="";
                    			for(i=0;i<court1[0].court_details.length;i++){
                    				courtDoneHtml +="<tr><td id='td-property'>被执行人姓名</td><td><span>"+isNull(court1[0].court_details[i].executed_name)+"</span></td><td id='td-property'>案号</td><td><span>"+isNull(court1[0].court_details[i].case_code)+"</span></td><td id='td-property'>执行依据文号</td><td><span>"+isNull(court1[0].court_details[i].execute_code)+"</span></td></tr><tr><td id='td-property'>立案时间</td><td><span>"+isNull(court1[0].court_details[i].case_date)+"</span></td><td id='td-property'>执行法院</td><td><span>"+isNull(court1[0].court_details[i].execute_court)+"</span></td><td id='td-property'>执行状态</td><td><span>"+isNull(court1[0].court_details[i].execute_status)+"</span></td></tr><tr><td id='td-property'>执行标的</td><td><span>"+isNull(court1[0].court_details[i].execute_subject)+"</span></td><td id='td-property'>被执行人履行情况</td><td><span>"+isNull(court1[0].court_details[i].carry_out)+"</span></td><td id='td-property'>风险类型</td><td><span>"+isNull(court1[0].court_details[i].fraud_type_display_name)+"</span></td></tr><tr><td id='td-property'>被执行人具体情形</td><td colspan='5'><span>"+isNull(court1[0].court_details[i].specific_circumstances)+"</span></td></tr><tr><td id='td-property'>生效法律文书确定的义务</td><td colspan='5'><span>"+isNull(court1[0].court_details[i].term_duty)+"</span></td></tr>";
                    			}
                    			$("#courtDoneBox").append(courtDoneHtml);
                    		}
                    		if(personalResult.ANTIFRAUD.black_list[2].risk_detail){
                    			$(".courtFinNull").hide();
                    			$(".courtMiss1").show();
                    			$(".courtFin").show();
                    			$(".courtFinBoxTitle").text(personalResult.ANTIFRAUD.black_list[2].risk_name);
                    			var court2 = personalResult.ANTIFRAUD.black_list[2].risk_detail;
                    			var courtFinHtml="";
                    			for(i=0;i<court2[0].court_details.length;i++){
                    				courtFinHtml +="<tr><td id='td-property'>被执行人姓名</td><td><span>"+isNull(court2[0].court_details[i].executed_name)+"</span></td><td id='td-property'>案号</td><td><span>"+isNull(court2[0].court_details[i].case_code)+"</span></td><td id='td-property'>执行依据文号</td><td><span>"+isNull(court2[0].court_details[i].execute_code)+"</span></td></tr><tr><td id='td-property'>立案时间</td><td><span>"+isNull(court2[0].court_details[i].case_date)+"</span></td><td id='td-property'>执行法院</td><td><span>"+isNull(court2[0].court_details[i].execute_court)+"</span></td><td id='td-property'>执行状态</td><td><span>"+isNull(court2[0].court_details[i].execute_status)+"</span></td></tr><tr><td id='td-property'>执行标的</td><td><span>"+isNull(court2[0].court_details[i].execute_subject)+"</span></td><td id='td-property'>被执行人履行情况</td><td><span>"+isNull(court2[0].court_details[i].carry_out)+"</span></td><td id='td-property'>风险类型</td><td><span>"+isNull(court2[0].court_details[i].fraud_type_display_name)+"</span></td></tr><tr><td id='td-property'>被执行人具体情形</td><td colspan='5'><span>"+isNull(court2[0].court_details[i].specific_circumstances)+"</span></td></tr><tr><td id='td-property'>生效法律文书确定的义务</td><td colspan='5'><span>"+isNull(court2[0].court_details[i].term_duty)+"</span></td></tr>";
                    			}
                    			$("#courtFinBox").append(courtFinHtml);
                    		}
                    	};
                    	//  历史违约风险检测
                    	if(personalResult.ANTIFRAUD.black_list_history&&personalResult.ANTIFRAUD.black_list_history.length>0){
                    		$(".courtHis").show();    
                    		var courtHisHtml="";
                    		for(i=0;i<personalResult.ANTIFRAUD.black_list_history.length;i++){
                    			courtHisHtml +="<tr><td id='td-property'>"+isNull(personalResult.ANTIFRAUD.black_list_history[i].risk_name)+"</td><td><span>"+isNull(personalResult.ANTIFRAUD.black_list_history[i].risk_detail[0].hit_type_display_name)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.black_list_history[i].risk_detail[0].fraud_type_display_name)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.black_list_history[i].risk_detail[0].description)+"</span></td></tr>"
                    		}
                    		$("#courtHisBox").append(courtHisHtml);
                    	};
                        //  关注名单风险检测
                    	if(personalResult.ANTIFRAUD.grey_list&&personalResult.ANTIFRAUD.grey_list.length>0){
                    		$(".greyDetailNull").hide();
                    		$(".greyDetail").show(); 
                    		for(i=0;i<personalResult.ANTIFRAUD.grey_list.length;i++){
                    			var greyHtml="";
                    			var greyLength =personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details.length+1;
                    			greyHtml +="<tr><td rowspan="+greyLength+" style='text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.description)+"</span></td><td rowspan="+greyLength +" style='text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.hit_type_display_name)+"</span></td></tr>";
                    			$("#greyDetailBox").append(greyHtml);   
                    			var greyDetailhtml="";
                    			for(j=0;j<personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details.length;j++){
                    				greyDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details[j].value)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details[j].risk_level)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details[j].fraud_type_display_name)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.grey_list[i].risk_detail.grey_list_details[j].evidence_time)+"</span></td></tr>";
                    			}
                    			$("#greyDetailBox").append(greyDetailhtml);
                    			var greyDetailhtml="";
                    		}
                    	};
                    	//  模糊证据库风险检测
                    	if(personalResult.ANTIFRAUD.fuzzy_black_list&&personalResult.ANTIFRAUD.fuzzy_black_list.length>0){
                    		$(".fuzzyBoxNull").hide();
                    		$(".fuzzyBox").show(); 
                    		for(i=0;i<personalResult.ANTIFRAUD.fuzzy_black_list.length;i++){
                    			var fuzzyHtml="";
                    			var fuzzyLength =personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fuzzy_list_details.length+1;
                    			fuzzyHtml +="<tr><td rowspan="+fuzzyLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fraud_type_display_name)+"</span></td></tr>";
                    			$("#fuzzyBox").append(fuzzyHtml);   
                    			var fuzzyDetailhtml="";
                    			for(j=0;j<personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fuzzy_list_details.length;j++){  
                    				fuzzyDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fuzzy_list_details[j].fuzzy_name)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fuzzy_list_details[j].fuzzy_id_number)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.fuzzy_black_list[i].risk_detail[0].fuzzy_list_details[j].fraud_type_display_name)+"</span></td></tr>";
                    			}
                    			$("#fuzzyBox").append(fuzzyDetailhtml);
                    			var fuzzyDetailhtml="";
                    		}
                    	};
                    	//  信贷逾期风险检测
                    	if(personalResult.ANTIFRAUD.discredit_count&&personalResult.ANTIFRAUD.discredit_count.length>0){
                    		$(".discreditBoxNull").hide(); 
                    		$(".discreditBox").show(); 
                    		for(i=0;i<personalResult.ANTIFRAUD.discredit_count.length;i++){
                    			var discreditHtml="";
                    			var discreditLength =personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details.length+1;
                    			discreditHtml +="<tr><td rowspan="+discreditLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].description)+"</span></td><td rowspan="+discreditLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].discredit_times)+"</span></td></tr>";
                    			$("#discreditBox").append(discreditHtml);   
                    			var discreditDetailhtml="";
                    			for(j=0;j<personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details.length;j++){  
                    				discreditDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details[j].overdue_amount_range)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details[j].overdue_count)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details[j].overdue_day_range)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].overdue_details[j].overdue_time)+"</span></td></tr>";
                    			}
                    			$("#discreditBox").append(discreditDetailhtml);
                    			var discreditDetailhtml="";
                    		}
                    	};
                    	//  频度风险检测
                    	if(personalResult.ANTIFRAUD.frequency_detail&&personalResult.ANTIFRAUD.frequency_detail.length>0){
                    		for(i=0;i<personalResult.ANTIFRAUD.frequency_detail.length;i++){
                    			for(k=0;k<personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list.length;k++){
                    				if(personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list[k].data){
                                		$(".frequencyBoxNull").hide(); 
                                		$(".frequencyBox").show(); 
                    					var frequencyHtml="";
                    					var frequencyLength =personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list[k].data.length+1;
                    					frequencyHtml +="<tr><td rowspan="+frequencyLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.frequency_detail[i].risk_name)+"</span></td></tr>";
                    					var frequencyDetailhtml="";
                    					for(j=0;j<personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list[k].data.length;j++){  
                    						frequencyDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list[k].detail)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.frequency_detail[i].risk_detail[0].frequency_detail_list[k].data[j])+"</span></td></tr>";
                    					}
                    					$("#frequencyBox").append(frequencyHtml); 
                    					$("#frequencyBox").append(frequencyDetailhtml);
                    				}
                    			}
                    			var frequencyDetailhtml="";
                    		}
                    	};
                    	//  跨事件频度风险检测
                    	if(personalResult.ANTIFRAUD.cross_frequency_detail&&personalResult.ANTIFRAUD.cross_frequency_detail.length>0){
                    		for(i=0;i<personalResult.ANTIFRAUD.cross_frequency_detail.length;i++){
                    			for(k=0;k<personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list.length;k++){
                    				if(personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list[k].data){
                    					$(".crossFrequencyBoxNull").hide(); 
                    					$(".crossFrequencyBox").show();
                    					var crossFrequencyHtml="";
                    					var crossFrequencyLength =personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list[k].data.length+1;
                    					crossFrequencyHtml +="<tr><td rowspan="+crossFrequencyLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_name)+"</span></td></tr>";
                    					var crossFrequencyDetailhtml="";
                    					for(j=0;j<personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list[k].data.length;j++){  
                    						crossFrequencyDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list[k].detail)+"</span></td><td><span>"+isNull(personalResult.ANTIFRAUD.cross_frequency_detail[i].risk_detail[0].cross_frequency_detail_list[k].data[j])+"</span></td></tr>";
                    					}
                    					$("#crossFrequencyBox").append(crossFrequencyHtml); 
                    					$("#crossFrequencyBox").append(crossFrequencyDetailhtml);
                    				}
                    			}
                    			var crossFrequencyDetailhtml="";
                    		}
                    	};
                    	//  多平台风险检测
                    	if(personalResult.ANTIFRAUD.platform_detail&&personalResult.ANTIFRAUD.platform_detail.length>0){
                    		$(".platformBoxNull").hide(); 
                    		$(".platformBox").show(); 
                    		for(i=0;i<personalResult.ANTIFRAUD.platform_detail.length;i++){
                    			var platformHtml="";
                    			var platformLength=personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_detail_dimension.length+1;;
                    			platformHtml+="<tr><td style='text-align: center;vertical-align: middle;' rowspan="+platformLength +"><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_name)+"</span></td><td style='text-align: center;vertical-align: middle;' rowspan="+platformLength +"><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_count)+"</span></td><td style='text-align: center;vertical-align: middle;' rowspan="+platformLength +"><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_detail.detail_total)+"</span></td></tr>";
                    			$("#platformBox").append(platformHtml); 
                    			var morePlatformHtml="";
                    			for(j=0;j<personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_detail_dimension.length;j++){ 
                    				morePlatformHtml+="<tr><td style='text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_detail_dimension[j].dimension)+"</span></td><td style='text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_detail_dimension[j].count)+"</span></td><td style='text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.platform_detail[i].risk_detail.platform_detail_dimension[j].detail_dimension_total)+"</span></td></tr>";
                    			}
                    			$("#platformBox").append(morePlatformHtml);
                    			var morePlatformHtml="";
                    		}
                    	};
                    	//  自定义风险检测
                    	if(personalResult.ANTIFRAUD.custom_list&&personalResult.ANTIFRAUD.custom_list.length>0){
                    		$(".customBoxNull").hide(); 
                    		$(".customBox").show(); 
                    		for(i=0;i<personalResult.ANTIFRAUD.custom_list.length;i++){
                    			var customHtml="";
                    			var customLength =personalResult.ANTIFRAUD.custom_list[i].risk_detail[0].high_risk_areas.length+1;
                    			customHtml +="<tr><td rowspan="+customLength +" style='width:262px;text-align: center;vertical-align: middle;'><span>"+isNull(personalResult.ANTIFRAUD.discredit_count[i].risk_detail[0].description)+"</span></td></tr>";
                    			$("#customBox").append(customHtml);   
                    			var discreditDetailhtml="";
                    			for(j=0;j<personalResult.ANTIFRAUD.custom_list[i].risk_detail[0].high_risk_areas.length;j++){  
                    				customDetailhtml +="<tr><td><span>"+isNull(personalResult.ANTIFRAUD.custom_list[i].risk_detail[0].high_risk_areas[j])+"</span></td><td><span></span></td></tr>";
                    			}
                    			$("#customBox").append(customDetailhtml);
                    			var customDetailhtml="";
                    		}
                    	};
                    }
                    //  信息解析详情
                    if(personalResult.INFOANALYSIS){
                    	$(".deviceNull").hide(); 
                    	$(".device").show(); 
                    	//    归属地
                    	$("#id_card_address").text(personalResult.INFOANALYSIS.address_detect.id_card_address);
                    	$("#mobile_address").text(personalResult.INFOANALYSIS.address_detect.mobile_address);
                    	$("#bank_card_address").text(personalResult.INFOANALYSIS.address_detect.bank_card_address);
                    	$("#true_ip_address").text(personalResult.INFOANALYSIS.address_detect.true_ip_address);
                    	$("#wifi_address").text(personalResult.INFOANALYSIS.address_detect.wifi_address);
                    	$("#cell_address").text(personalResult.INFOANALYSIS.address_detect.cell_address);
                    	//  IP代理信息
                    	$("#isp").text(personalResult.INFOANALYSIS.geoip_info.isp);
                    	$("#longitude").text(personalResult.INFOANALYSIS.geoip_info.longitude);
                    	$("#latitude").text(personalResult.INFOANALYSIS.geoip_info.latitude);
                    	//  真实IP信息
                    	$("#isp1").text(personalResult.INFOANALYSIS.geotrueip_info.isp);
                    	$("#longitude1").text(personalResult.INFOANALYSIS.geotrueip_info.longitude);
                    	$("#latitude1").text(personalResult.INFOANALYSIS.geotrueip_info.latitude);
                    	// 设备解析
                    	if(!personalResult.INFOANALYSIS.device_info.error){
                    		$("#deviceId").text(personalResult.INFOANALYSIS.device_info.deviceId);
                    		$("#smartId").text(personalResult.INFOANALYSIS.device_info.smartId);
                    		$("#trueIp").text(personalResult.INFOANALYSIS.device_info.trueIp);
                    		$("#appOs").text(personalResult.INFOANALYSIS.device_info.appOs);
                    		$("#os").text(personalResult.INFOANALYSIS.device_info.os);
                    		$("#screen").text(personalResult.INFOANALYSIS.device_info.screen);
                    		$("#browserType").text(personalResult.INFOANALYSIS.device_info.browserType);
                    		$("#browserVersion").text(personalResult.INFOANALYSIS.device_info.browserVersion);
                    	}
                    }
                    }
            	 }
             }else{
            	 IQB.alert('用户未生成报告,稍后再试');
             }
         }
     });
});