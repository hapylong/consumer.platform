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
//  判断学历
	function isEducation(val){
		if(val == '2'){
			return '本科学历';
		}
		else if (val == '1'){
			return '高中及以下';
		}else if (val == '3'){
			return '硕士学历';
		}else if (val == '4'){
			return '博士学历';
		}else if (val == '0'){
			return '';
		}else{
			return val;
		}
	}
//  判断交易金额稳定性
	function isTransAmountStability(val){
		if(val == '0'){
			return '无交易';
		}
		else if (val == '1'){
			return '稳定性高（近6个月交易金额的标准差<=300）';
		}else if (val == '2'){
			return '稳定性中（300<近6个月交易金额的标准差<=1500）';
		}else if (val == '3'){
			return '稳定性低（1500<近6个月交易金额的标准差）';
		}else{
			return val;
		}
	}
//  判断高中低消费人群标记
	function isHmlConsumGroupsMark(val){
		if(val == '0'){
			return '无交易';
		}
		else if (val == '1'){
			return '低（笔均消费<300）';
		}else if (val == '2'){
			return '中（300<=笔均消费<1000）';
		}else if (val == '3'){
			return '高（笔均消费>=1000）';
		}else{
			return val;
		}
	}
//  判断交易金额稳定性
	function isTransAmountStability(val){
		if(val == '0'){
			return '无交易';
		}
		else if (val == '1'){
			return '稳定性高（近6个月交易金额的标准差<=300） ';
		}else if (val == '2'){
			return '稳定性中（300<近6个月交易金额的标准差<=1500）';
		}else if (val == '3'){
			return '稳定性低（1500<近6个月交易金额的标准差）';
		}else{
			return val;
		}
	}
//  判断消费强度标志实际月
	function isConsumptionStrengthMark(val){
		if(val == '0'){
			return '低（实际发生交易月份的月均交易金额<500）';
		}
		else if (val == '1'){
			return '中低（500<=实际发生交易月份的月均交易金额<1500）';
		}else if (val == '2'){
			return '中高（1500<=实际发生交易月份的月均交易金额<5000）';
		}else if (val == '3'){
			return '高（5000<=实际发生交易月份的月均交易金额）';
		}else{
			return val;
		}
	}
//  卡状态得分
	function isCardStatusScore(val){
		if(val == 'null'){
			return '近12个月无交易';
		}
		else if (val == '1'){
			return '不活跃客户';
		}else if (val == '2'){
			return '长期忠诚客户';
		}else if (val == '3'){
			return '活跃上升客户';
		}else if (val == '4'){
			return '活跃下降客户';
		}else if (val == '5'){
			return '自激活或新客户';
		}else if (val == '6'){
			return '睡眠客户';
		}else{
			return val;
		}
	}
//  消费能力得分
	function isConsumerAbilityScore(val){
		if(val == 'null'){
			return '近12个月无交易';
		}
		else if (val == '1'){
			return '（与地域相比）显著低';
		}else if (val == '2'){
			return '（与地域相比）低';
		}else if (val == '3'){
			return '（与地域相比）相当';
		}else if (val == '4'){
			return '（与地域相比）高';
		}else if (val == '5'){
			return '（与地域相比）显著高';
		}else if (val == '6'){
			return '高额消费';
		}else if (val == '7'){
			return '近3个月无交易';
		}else{
			return val;
		}
	}
//  判断婚姻
	function isMarried(val){
		if(val == '2'){
			return '已婚';
		}
		else if (val == '1'){
			return '未婚';
		}else if (val == '0'){
			return '';
		}else{
			return val;
		}
	}
//  判断匹配度
	function isMatch(val){
		if(val == '0'){
			return '一般匹配';
		}
		else if (val == '1'){
			return '高度匹配';
		}else if (val == '2'){
			return '完全匹配';
		}else{
			return val;
		}
	}
//  卡性质
	function isCardProperty(val){
		if(val == '00'){
			return '已婚';
		}
		else if (val == '01'){
			return '未知';
		}else if (val == '02'){
			return '借记卡';
		}else if (val == '03'){
			return '贷记卡';
		}else if (val == '04'){
			return '准贷记卡';
		}else if (val == '05'){
			return '预付费卡';
		}else{
			return val;
		}
	}
//  判断逾期时长
	function isYqtime(val){
		if(val == 'M0'){
			return '1~15天';
		}
		else if (val == 'M1'){
			return 'M1:16~30 天';
		}else if (val == 'M2'){
			return 'M2:31~60 天';
		}else if (val == 'M3'){
			return 'M3:61~90天';
		}else if (val == 'M4'){
			return 'M4:91~120天';
		}else if (val == 'M5'){
			return 'M5:121~150天';
		}else if (val == 'M6'){
			return 'M6:151~180天';
		}else if (val == 'M6+'){
			return 'M6+:180天以上';
		}else{
			return val;
		}
	}
//  判断匹配度
	function isQueryTyp(val){
		if(val == '1'){
			return '命中';
		}
		else if (val == '2'){
			$("#queryTyp").css({'color':'red','font-weight':'bold'});
			return '未命中';
		}else{
			return val;
		}
	}
		// 回显数据
		var data = {};
		data.orderId = GetQueryString("orderId");
		data.reportType = Number(GetQueryString("reportType"));
		// 图片显示
		var imgHtml='<img src="../report/getReportInfo/photo/'+data.id+'"/>';
		$(".personalPhoto").append(imgHtml);
		//	数据显示
		$.ajax({
         type:"post",
         url:urls['cfm'] + '/afterLoan/getReportByReprotNo',
         dataType: 'json',
         contentType: "application/json",
         data : JSON.stringify(data),
         success : function(result){
        	 /*if(result.hasOwnProperty("success") && result.success == '2'){
        		 IQB.alert(result.retUserInfo);
        	 }*/
        	 if(result.hasOwnProperty("pojo") && result.pojo.result != null){	 
            	var personal =result.pojo;
        		// 回显个人信息数据            
        		if(personal){
        			$(".personalBox").show();
        			$(".personalPhoto").show();
        			//暂无
            		if(personal.ruleStatus=="3"){
            			$(".infoMajor").show();
            			$(".infoMajorL").show();
            		}
            		//暂无
            		if(personal.photo==""||personal.photo=="null"||personal.photo==null){
            			$(".personalPhoto").hide();
            		}
            		$("#createTime").text(personal.createTime);
                 	$("#updateTime").text(personal.updateTime);
                 	$("#name").text(personal.name);
                 	$("#idNo").text(personal.idNo);
                 	$("#phone").text(personal.phone);
                 	//获取性别
            		var idNo = personal.idNo;
            		if (parseInt(idNo.substr(16, 1)) % 2 == 1) {
            		$('#sex').text('男');
            	    } else {
            	    $('#sex').text('女');
            	    }
            		//获取年龄
            		var myDate = new Date();
            		var month = myDate.getMonth() + 1;
            		var day = myDate.getDate();
            		var age = myDate.getFullYear() - idNo.substring(6, 10) - 1;
            		if (idNo.substring(10, 12) < month || idNo.substring(10, 12) == month && idNo.substring(12, 14) <= day) {
            		age++;
            		}
            		$('#age').text(age);
                 	$("#sex").text(personal.sex);
                 	$("#age").text(personal.age);
                 	$("#married").text(isMarried(personal.married));
                 	$("#bankNum").text(personal.bankNum);
                 	$("#bankName").text(personal.bankName);
                 	$("#income").text(personal.income);
                 	$("#education").text(isEducation(personal.education));
                 	$("#school").text(personal.school);
                 	$("#otherIncome").text(personal.otherIncome);
                 	$("#nativePlace").text(personal.nativePlace);
                 	//	生成CODE128二维码
                    $(".bcTarget").barcode(personal.reportNo, "code128",{barWidth:1, barHeight:20});
            	}
            	//  司法及不良信息 
                if((result.pojo.result)["br_negative_record"]){
                	$(".badInfoBox3").show();
                	$(".badInfoBoxNull").show();
                	var police = JSON.parse((result.pojo.result)["br_negative_record"]);
                	if(police.data){
                		$(".badInfoBox").show();
                		$(".badInfoBoxNull").hide();
                		if(police.data.checkResult.indexOf("W")>=0||police.data.checkResult==""){
                    		$("#crime").text("否");
                    		$("#pedigree").text("否");
                    		$("#trust").text("否");
                    		$("#drug").text("否");
                    		$("#case").text("否");
                    		$("#risk").text("是");
                    	}
                    	if(police.data.checkResult.indexOf("F")>=0||police.data.checkResult.indexOf("X")>=0||police.data.checkResult.indexOf("Q")>=0||police.data.checkResult.indexOf("T")>=0||police.data.checkResult.indexOf("S")>=0){
                    		$("#risk").text("否");
                    		$("#risk").css({'color':'red','font-weight':'bold'});
                    	}
                    	if(police.data.checkResult.indexOf("F")>=0){
                    		$("#crime").text("是");
                    		$("#crime").css({'color':'red','font-weight':'bold'});
                    	}else{
                    		$("#crime").text("否");	
                    	}
                    	if(police.data.checkResult.indexOf("Q")>=0){
                    		$("#pedigree").text("是");
                    		$("#pedigree").css({'color':'red','font-weight':'bold'});
                    	}else{
                    		$("#pedigree").text("否");
                    	}
                    	if(police.data.checkResult.indexOf("S")>=0){
                    		$("#trust").text("是");
                    		$("#trust").css({'color':'red','font-weight':'bold'});
                    	}else{
                    		$("#trust").text("否");
                    	}
                    	if(police.data.checkResult.indexOf("X")>=0){
                    		$("#drug").text("是");
                    		$("#drug").css({'color':'red','font-weight':'bold'});
                    	}else{
                    		$("#drug").text("否");
                    	}
                    	if(police.data.checkResult.indexOf("T")>=0){
                    		$("#case").text("是");
                    		$("#case").css({'color':'red','font-weight':'bold'});
                    	}else{
                    		$("#case").text("否");
                    	}
                	}
                }
                //  司法信息
                if((result.pojo.result)["br_judicial_case"]){
                	$(".badInfoBox3").show();
                	$(".badInfoBox2Null").show();
                	var involvedCase = JSON.parse((result.pojo.result)["br_judicial_case"]);
                    if(involvedCase.type=="0"&&involvedCase.data){
                    	$(".badInfoBox2").show();
                    	$(".badInfoBox2Null").hide();
                    	var involvedCaseHtml="<tr><td id='td-property' class='col-md-1'>案件标题</td><td class='col-md-2.5'><span id='title'>"+isNull(involvedCase.data.title)+"</span></td><td id='td-property' class='col-md-1'>当事人</td><td class='col-md-2.5'><span id='dangshiren'>"+isNull(involvedCase.data.dangshiren)+"</span></td><td id='td-property' class='col-md-1'>匹配度</td><td class='col-md-2.5'><span id='matchfit'>"+isMatch(isNull(involvedCase.data.match))+"</span></td></tr><tr><td id='td-property' style='width: 101px'>案件摘要</td><td colspan='5'><span id='jsummary'>"+isNull(involvedCase.data.abstract)+"</span><p style='display:none'>"+isNull(involvedCase.data.recordNo)+"</p><span class='infoDetail'>查看详情</span><p style='display:none'>infoDetail1</p></td></tr>";
                    	$(".involvedCaseTitle").append(involvedCaseHtml);
                    	//  查看详情
                        $(".infoDetail").click(function(){
                       	 	var divClass = $(this).next("p").text();
                       	 	var recordNo = $(this).prev("p").text();
                       	 	var request = {};
                       		request.name = $("#name").text();
                       		request.idNo = $("#idNo").text();
                       		request.recordNo = recordNo;
                       		request.orderId = GetQueryString("orderId");
                       		if (!$("#infoDetail1").length > 0 ){ 
                       			$.ajax({
                           			type : "POST",
                                	url : urls['cfm'] + '/afterLoan/unIntcpt-getPersonDetail',
                                	dataType: 'json',
                                	contentType: "application/json",
                                	data : JSON.stringify(request),
                                	success : function(data){
                                		if(data.hasOwnProperty("success")){
                                			IQB.alert(data.retUserInfo);
                                		}else{
                                			var data= data.data;
                                    		var involvedCaseHtmlLeft="<tr class='infoDetail1' id='infoDetail1'><td id='td-property'>法院名称</td><td><span id='court'>"+isNull(data.courtName)+"</span></td><td id='td-property'>案号</td><td><span id='jnum'>"+isNull(data.recordNo)+"</span></td><td id='td-property'>审结时间</td><td><span id='judge_date'>"+isNull(data.concludeDate)+"</span></td></tr><tr class='infoDetail1'<td id='td-property'>案件类别</td><td><span id='jtype'>"+isNull(data.caseType)+"</span></td><td id='td-property'>当事人</td><td><span id='danshiren'>"+isNull(data.danshiren)+"</span></td><td id='td-property'>当事人身份证</td><td><span id='idNo'>"+isNull(data.idNo)+"</span></td></tr><tr class='infoDetail1'><td id='td-property'>原告</td><td><span id='yuangao'>"+isNull(data.yuangao)+"</span></td><td id='td-property'>被告</td><td><span id='beigao'>"+isNull(data.beigao)+"</span></td><td id='td-property'>案由</td><td><span id='jcase'>"+isNull(data.caseReason)+"</span></td></tr><tr class='infoDetail1'><td id='td-property'>上诉人</td><td ><span id='shangshuren'>"+isNull(data.shangshuren)+"</span></td><td id='td-property'>被上诉人</td><td><span id='beishangshuren'>"+isNull(data.beishangshuren)+"</span></td><td id='td-property'>判决结果</td><td><span id='result_str'>"+isNull(data.trialResult)+"</span></td></tr>"
                                    		$(".involvedCaseTitle").append(involvedCaseHtmlLeft);
                                		}
                                	},
                           		}); 
                       		}
                       		if($("."+divClass).is(":hidden")){
                       		 	$("."+divClass).show();
                       	 	}else{
                       		 	$("."+divClass).hide();
                       	 	}
                       		var rgb = $(this).css('color'); 
                       		if(rgb=="rgb(68, 138, 202)"){
                       			$(this).css("color","#ababab")
                       		}else{
                       			$(this).css("color","#448aca")
                       		}
                    	}); 
                    }else if(involvedCase.type=="1"&&involvedCase.data.length>0){
                    	$(".badInfoBox2").show();
                    	$(".badInfoBox2Null").hide();
                    	 	involvedCaseHtmlList="";
                    	for(i=0;i<involvedCase.data.length;i++){
                    		 involvedCaseHtmlList +="<tr><td id='td-property' class='col-md-1'>案件标题</td><td class='col-md-2.5'><span id='title'>"+isNull(involvedCase.data[i].title)+"</span></td><td id='td-property' class='col-md-1'>当事人</td><td class='col-md-2.5'><span id='dangshiren'>"+isNull(involvedCase.data[i].dangshiren)+"</span></td><td id='td-property' class='col-md-1'>匹配度</td><td class='col-md-2.5'><span id='matchfit'>"+isNull(isMatch(involvedCase.data[i].match))+"</span></td></tr><tr id='major"+i+"'><td id='td-property' style='width: 101px'>案件摘要</td><td colspan='5'><span id='jsummary'>"+isNull(involvedCase.data[i].abstract)+"</span><p style='display:none'>"+isNull(involvedCase.data[i].recordNo)+"</p><span class='infoDetail'>查看详情</span><p style='display:none'>infoDetail"+i+"</p></td></tr>";
                        } 
                    	$(".involvedCaseTitle").append(involvedCaseHtmlList);
                    	//  查看详情
                        $(".infoDetail").click(function(){
                        	var divClass = $(this).next("p").text();
                       	 	var recordNo = $(this).prev("p").text();
                       	 	var major = $(this).parent().parent().attr('id');
                       	 	var request = {};
                       		request.name = $("#name").text();
                       		request.idNo = $("#idNo").text();
                       		request.recordNo = recordNo;
                       		request.orderId = GetQueryString("orderId");
                       		if (!$("#"+divClass).length > 0 ){ 
                       			$.ajax({
                           			type : "POST",
                                	url : urls['cfm'] + '/afterLoan/unIntcpt-getPersonDetail',
                                	dataType: 'json',
                                	contentType: "application/json",
                                	data : JSON.stringify(request),
                                	success : function(data){
                                		if(data.hasOwnProperty("success")){
                                			IQB.alert(data.retUserInfo);
                                		}else{
                                			var data= data.data;
                                    		var involvedCaseHtmlListLeft="<tr class='"+divClass+"' id='"+divClass+"'><td id='td-property'>法院名称</td><td><span id='court'>"+isNull(data.courtName)+"</span></td><td id='td-property'>案号</td><td><span id='jnum'>"+isNull(data.recordNo)+"</span></td><td id='td-property'>审结时间</td><td><span id='judge_date'>"+isNull(data.concludeDate)+"</span></td></tr><tr class='"+divClass+"'><td id='td-property'>案件类别</td><td><span id='jtype'>"+isNull(data.caseType)+"</span></td><td id='td-property'>当事人</td><td><span id='danshiren'>"+isNull(data.danshiren)+"</span></td><td id='td-property'>当事人身份证</td><td><span id='idNo'>"+isNull(data.idNo)+"</span></td></tr><tr class='"+divClass+"'><td id='td-property'>原告</td><td><span id='yuangao'>"+isNull(data.yuangao)+"</span></td><td id='td-property'>被告</td><td><span id='beigao'>"+isNull(data.beigao)+"</span></td><td id='td-property'>案由</td><td><span id='jcase'>"+isNull(data.caseReason)+"</span></td></tr><tr class='"+divClass+"'><td id='td-property'>上诉人</td><td ><span id='shangshuren'>"+isNull(data.shangshuren)+"</span></td><td id='td-property'>被上诉人</td><td><span id='beishangshuren'>"+isNull(data.beishangshuren)+"</span></td><td id='td-property'>判决结果</td><td><span id='result_str'>"+isNull(data.trialResult)+"</span></td></tr>"
                                    		$("#"+major).after(involvedCaseHtmlListLeft);
                                		}
                                	},
                           		}); 
                       		} 
                       		if($("."+divClass).is(":hidden")){
                       		 	$("."+divClass).show();
                       	 	}else{
                       		 	$("."+divClass).hide();
                       	 	}
                       		var rgb = $(this).css('color'); 
                       		if(rgb=="rgb(68, 138, 202)"){
                       			$(this).css("color","#ababab")
                       		}else{
                       			$(this).css("color","#448aca")
                       		}
                    	}); 
                    } 
                } 
               //  失信信息
                if((result.pojo.result)["br_niddering"]){
                	$(".badInfoBox3").show();
                	$(".badInfoBox1Null").show();
                	var promise = JSON.parse((result.pojo.result)["br_niddering"]);
                	if(promise.type=="0"&&promise.data){
                		$(".badInfoBox1").show();
                		$(".badInfoBox1Null").hide();
                    	var promiseHtml="<tr><td id='td-property'>ID</td><td><span id='no'>"+isNull(promise.data.no)+"</span></td><td id='td-property'>省份</td><td><span id='province'>"+isNull(promise.data.province)+"</span></td><td id='td-property'>立案时间</td><td><span id='filingTime'>"+isNull(promise.data.filingTime)+"</span></td></tr><tr><td id='td-property'>发布时间</td><td><span id='releaseTime'>"+isNull(promise.data.releaseTime)+"</span></td><td id='td-property'>执行法院</td><td><span id='executiveCourt'>"+isNull(promise.data.executiveCourt)+"</span></td><td id='td-property'>案号</td><td><span id='caseNo'>"+isNull(promise.data.caseNo)+"</span></td></tr><tr><td id='td-property'>执行依据文号</td><td><span id='executiveBaiscNo'>"+isNull(promise.data.executiveBaiscNo)+"</span></td><td id='td-property'>做出执行依据单位</td><td><span id='executiveArm'>"+isNull(promise.data.executiveArm)+"</span></td><td id='td-property'>被执行人履行情况</td><td><span id='executiveCase'>"+isNull(promise.data.executiveCase)+"</span></td></tr><tr><td id='td-property' style='width: 101px;text-align: center;'>生效法律文书确定的义务</td><td colspan='5'><span id='legalObligation'>"+isNull(promise.data.legalObligation)+"</span></td></tr>";    	 	
                    	$(".badInfoTitle").append(promiseHtml);
                    }else if(promise.type=="1"&&promise.data.length>0){
                    	$(".badInfoBox1").show();
                    	$(".badInfoBox1Null").hide();
                    	promiseHtmlList="";
                    	for(i=0;i<promise.data.length;i++){
                    		promiseHtmlList +="<tr><td id='td-property'>ID</td><td><span id='no'>"+isNull(promise.data[i].no)+"</span></td><td id='td-property'>省份</td><td><span id='province'>"+isNull(promise.data[i].province)+"</span></td><td id='td-property'>立案时间</td><td><span id='filingTime'>"+isNull(promise.data[i].filingTime)+"</span></td></tr><tr><td id='td-property'>发布时间</td><td><span id='releaseTime'>"+isNull(promise.data[i].releaseTime)+"</span></td><td id='td-property'>执行法院</td><td><span id='executiveCourt'>"+isNull(promise.data[i].executiveCourt)+"</span></td><td id='td-property'>案号</td><td><span id='caseNo'>"+isNull(promise.data[i].caseNo)+"</span></td></tr><tr><td id='td-property'>执行依据文号</td><td><span id='executiveBaiscNo'>"+isNull(promise.data[i].executiveBaiscNo)+"</span></td><td id='td-property'>做出执行依据单位</td><td><span id='executiveArm'>"+isNull(promise.data[i].executiveArm)+"</span></td><td id='td-property'>被执行人履行情况</td><td><span id='executiveCase'>"+isNull(promise.data[i].executiveCase)+"</span></td></tr><tr><td id='td-property' style='width: 101px;text-align: center;'>生效法律文书确定的义务</td><td colspan='5'><span id='legalObligation'>"+isNull(promise.data[i].legalObligation)+"</span></td></tr>";    	 	
                        } 
                    	$(".badInfoTitle").append(promiseHtmlList);
                    } 
                }
                //  金融机构黑名单信息
                if((result.pojo.result)["br_black_list"]){
                	$(".blacklistBoxTitle").show();
                	$(".blacklistBoxNull").show();
                	var blacklist = JSON.parse((result.pojo.result)["br_black_list"]);
                	var blacklistData = blacklist.data;
                	if(blacklistData){
                		$(".blacklistBox").show();
                		$(".blacklistBoxNull").hide();
                		$("#queryTyp").text(isQueryTyp(blacklistData.queryTyp));
                		$("#resultYqDqsc").text(isYqtime(blacklistData.queryInfo.resultYqDqsc));
                		$("#resultYqDqje").text(blacklistData.queryInfo.resultYqDqje);
                		$("#resultYqZdsc").text(isYqtime(blacklistData.queryInfo.resultYqZdsc));
                		$("#resultYqZdje").text(blacklistData.queryInfo.resultYqZdje);
                		$("#resultYqZzsj").text(blacklistData.queryInfo.resultYqZzsj);
                		$("#resultYqZjsj").text(blacklistData.queryInfo.resultYqZjsj);
                		$("#resultYqLjcs").text(blacklistData.queryInfo.resultYqLjcs);
                		$("#resultQzZzsj").text(blacklistData.queryInfo.resultQzZzsj);
                		$("#resultQzZjsj").text(blacklistData.queryInfo.resultQzZjsj);
                		$("#resultQzLjcs").text(blacklistData.queryInfo.resultQzLjcs);
                		$("#resultSxZzsj").text(blacklistData.queryInfo.resultSxZzsj);
                		$("#resultSxZjsj").text(blacklistData.queryInfo.resultSxZjsj);
                		$("#resultSxLjcs").text(blacklistData.queryInfo.resultSxLjcs);
                	}
                }
                //  银行卡信息result.hasOwnProperty("success")
                if(result.pojo.result.hasOwnProperty("br_finance_info")){
                	if(JSON.parse((result.pojo.result)["br_finance_info"])){
                    	$(".tradeBoxTitle").show();
                    	$(".tradeBoxNull").show();
                    	var bankCard = JSON.parse((result.pojo.result)["br_finance_info"]);
                    	var bankCardData = bankCard.data;
                    	if(bankCardData&&bankCardData.result!="查无此记录"){
                    		$(".tradeBox").show();
                    		$(".tradeBoxNull").hide();
                    		$("#idNumber").text($("#bankNum").text());
                        	$("#cardProperty").text(isCardProperty(bankCardData.cardProperty));
                        	$("#cardStatusScore").text(isCardStatusScore(bankCardData.cardStatusScore));
                        	$("#hmlConsumGroupsMark").text(isHmlConsumGroupsMark(bankCardData.hmlConsumGroupsMark));
                        	$("#transAmountStability").text(isTransAmountStability(bankCardData.transAmountStability));
                        	$("#commonCityName").text(bankCardData.commonCityName);
                        	$("#generalCityTotalAmt").text(bankCardData.generalCityTotalAmt);
                        	$("#consumptionStrengthMark").text(isConsumptionStrengthMark(bankCardData.consumptionStrengthMark));
                        	$("#monthActiveProvince").text(bankCardData.monthActiveProvince);
                        	$("#consumerAbilityScore").text(isConsumerAbilityScore(bankCardData.consumerAbilityScore));
                        	$("#transAount1").text(bankCardData.transAount1);
                        	$("#transAount3").text(bankCardData.transAount3);
                        	$("#transAountAverage3").text(bankCardData.transAountAverage3);
                        	$("#transAount6").text(bankCardData.transAount6);
                        	$("#transAountLaw6").text(bankCardData.transAountLaw6);
                        	$("#transAount12").text(bankCardData.transAount12);
                        	$("#transAountAverage12").text(bankCardData.transAountAverage12);
                        	$("#transNumber1").text(bankCardData.transNumber1);
                        	$("#transNumber3").text(bankCardData.transNumber3);
                        	$("#transNumberAverage3").text(bankCardData.transNumberAverage3);
                        	$("#transNumber6").text(bankCardData.transNumber6);
                        	$("#transNumberLaw6").text(bankCardData.transNumberLaw6);
                        	$("#transNumber12").text(bankCardData.transNumber12);
                        	$("#transNumberAverage12").text(bankCardData.transNumberAverage12);
                    	}
                    }
                }
             }else{
            	 IQB.alert(result.retUserInfo);
             }
         }
     });
});