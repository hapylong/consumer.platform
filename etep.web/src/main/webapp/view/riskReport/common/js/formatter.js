/**
 * @author
 * @Version
 * @DateTime
 */
var Formatter = {
	commonYN: function(val){
		if(val == '1'){
			return '<span class="label label-success">是</span>';
		}else if (val == '0'){
			return '<span class="label label-danger">否</span>';
		}else{
			return '';
		}
	},
	channelOrderStatus: function(val){
		if(val == '1'){
			return '<span class="label label-success">终态</span>';
		}else if (val == '0'){
			return '<span class="label label-danger">非终态</span>';
		}else{
			return '';
		}
	},
	channelTradeStatus: function(val){
		if(val == '1'){
			return '<span class="label label-success">终态</span>';
		}else if (val == '0'){
			return '<span class="label label-danger">非终态</span>';
		}else{
			return '';
		}
	},
	jsRechargeType: function(val){
		var retStr = '';
		if(val.indexOf('sk') >= 0){
			retStr += '<span class="label label-success">收款</span> ';
		}
		if(val.indexOf('fk') >= 0){
			retStr += '<span class="label label-success">付款</span> ';
		}
		return retStr;
	},
	jsPayType: function(val){
		var retStr = '';
		if(val.indexOf('dk') >= 0){
			retStr += '<span class="label label-success">代扣</span> ';
		}
		if(val.indexOf('df') >= 0){
			retStr += '<span class="label label-success">代付</span> ';
		}
		if(val.indexOf('kj') >= 0){
			retStr += '<span class="label label-success">快捷</span> ';
		}
		if(val.indexOf('wg') >= 0){
			retStr += '<span class="label label-success">网关</span> ';
		}
		return retStr;
	},
	/** 交易类型(0还款，1划扣，2放款) **/
	tradeType: function(value){
		if(value === 0){
			return '<span class="label label-success">还款</span>';
		}
		if(value === 1){
			return '<span class="label label-success">首付款划扣</span>';
		}
		if(value === 2){
			return '<span class="label label-success">放款</span>';
		}else{
			return '';
		}
	},
	/** 划扣类型（0实时，1批量） **/
	debitType: function(val){
		if(val == '1'){
			return '<span class="label label-success">实时</span>';
		}else if (val == '2'){
			return '<span class="label label-success">批量</span>';
		}else{
			return '';
		}
	},
	/** 状态(1.未扣款 2.扣款成功 3.扣款失败 4.已合并 5.扣款失败-非终态) **/
	tradeStatus: function(val){
		if(val == '1'){
			return '<span class="label label-success">未扣款</span>';
		}else if (val == '2'){
			return '<span class="label label-success">扣款成功</span>';
		}else if (val == '3'){
			return '<span class="label label-success">扣款失败</span>';
		}else if (val == '4'){
			return '<span class="label label-success">已合并</span>';
		}else if (val == '5'){
			return '<span class="label label-success">扣款失败-非终态</span>';
		}else if (val == '6'){
			return '<span class="label label-success">处理中</span>';
		}else{
			return '';
		}
	},
	approve: function(val){
		if(val == '1'){
			return '通过';
		}else if (val == '0'){
			return '拒绝';
		}else{
			return '';
		}
	},
	isRegistered: function(val){
		if(val == '1'){
			return '已注册';
		}else if (val == '0'){
			return '未注册';
		}else{
			return val;
		}
	},
	contractStatus: function(val){
		if(val == '1'){
			return '待手工签约的订单';
		}else if (val == '2'){
			return '手工签约完毕的订单';
		}else if (val == '3'){
			return '需电子签约订单';
		}else{
			return '';
		}
	},
	isOwn: function(val){
		if(val == '1'){
			return '有';
		}else if (val == '0'){
			return '无';
		}else{
			return val;
		}
	},
	time2: function(val){
		if(val){
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日 ');
		}
		return '';
	},
	//是否可用(标签格式)
	isSenderPartSign: function(val){
		if(val != null) {
			if(val == 1){
				return '<span class="label label-success">参与签署</span>';
			}else{
				return '<span class="label label-warning">不参与签署</span>';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	ecSignType: function(val){
		if(val != null) {
			if(val == 2){
				return '<span class="label label-success">自动签署</span>';
			}else{
				return '<span class="label label-warning">手动签署</span>';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	ecSignType: function(val){
		if(val != null) {
			if(val == 2){
				return '<span class="label label-success">自动签署</span>';
			}else{
				return '<span class="label label-warning">手动签署</span>';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	ecSignerVideoType: function(val){
		if(val != null) {
			if(val == 0){
				return '<span class="label label-success">无视频</span>';
			}else if(val == 1){
				return '<span class="label label-warning">单向视频</span>';
			}else{
				return '<span class="label label-danger">双向视频</span>';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	isIncludeJunior: function(val){
		if(val != null) {
			if(val == 1){
				return '<span class="label label-success">可用</span>';
			}else{
				return '<span class="label label-danger">不可用</span>';
			}
		}
		return '';
	},
	opinion: function(val){
		return '<span style="display: inline-block; width: 260px;">' + val + '</span>';
	},
	//时间戳转时间字符串
	time: function(val){
		if(val){
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	//时间戳转日期字符串
	date: function(val){
		if(val){
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日');
		}
		return '';
	},
	//时间转换 for requestFund
	dateFund: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日');
		}
		return '';
	},
	//是否可用
	isEnable: function(val){
		if(val != null){
			if(val == 1){
				return '启用';
			}else{
				return '停用';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	isEnableHtml: function(val){
		if(val != null) {
			if(val == 1){
				return '<span class="label label-primary">启用</span>';
			}else{
				return '<span class="label label-default">停用</span>';
			}
		}
		return '';
	},
	//是否通过
	ispass: function(val){
		if(val != null){
			if(val == 1){
				return '通过';
			}else{
				return '不通过';
			}
		}
		return '';
	},
	//是否通过(标签格式)
	ispassHtml: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">通过</span>';
			}else{
				return '<span class="label label-default">不通过</span>';
			}
		}
		return '';
	},
	//是否只读
	readonly: function(val){
		if(val != null){
			if(val == 1){
				return '只读';
			}else{
				return '可写';
			}
		}
		return '';
	},
	//是否只读(标签格式)
	readonlyHtml: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-default">只读</span>';
			}else{
				return '<span class="label label-primary">可写</span>';
			}
		}
		return '';
	},
	//是否是管理员
	isSuperadmin: function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
		}
		return '';
	},
	//角色状态格式化
	stationStatus: function(val) {
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">激活</span>';
			}else{
				return '<span class="label label-default">非激活</span>';
			}
		}
		return '';
	},
	//用户状态格式化
	UserStatus: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">正常</span>';
			}else{
				return '<span class="label label-default">冻结</span>';
			}
		}
		return '';
	},
	//业务类型格式化
	groupName: function(val){
		if(val != null){
			if(val == 2001){
				return '以租代售新车';
			}else if(val == 2002){
				return '以租代售二手车';
			}else if(val == 2100){
				return '抵押车';
			}else if(val == 2200){
				return '质押车';
			}else if(val == 1100){
				return '易安家';
			}else if(val == 1000){
				return '医美';
			}else if(val == 1200){
				return '旅游';
			}else if(val == 2300){
				return '车秒贷';
			}
		}		
		return '';
	},
	/*CFM*/
	//是否上收月供
	upPayment: function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
		}
		return '';
	},
	//时间戳转时间字符串
	timeCfm: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	}, 
	//时间戳转时间字符串
	timeCfm2: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日');
		}
		return '';
	},
	//时间字符串去掉毫秒
	timeCfm3: function(val){
		var val=val.substring(0,val.length-2);
		return val;
	},
	//时间戳转时间字符串
	timeCfm4: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy/M/d');
		}
		return '';
	},
	timeCfm5: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy-MM-dd');
		}
		return '';
	},
	//收取方式格式化
	chargeWay: function(val){	
		if(val != null){
			if(val == 1){
				return '线下收取';
			}else{
				return '线上收取';
			}
		}
		return '';
	},
	//货币格式化(¥200,500.00)
	money: function(val){
		if(((val != null) && (val != '')) && (val != 0)){
			var n = 2;
			val = parseFloat((val + '').replace(/[^\d\.-]/g, '')).toFixed(n)
					+ '';
			var l = val.split('.')[0].split('').reverse();
			var r = val.split('.')[1];
			var t = '';
			for(var i = 0; i < l.length; i++){
				t += l[i]
						+ ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',' : '');
			}
			return '¥' + t.split('').reverse().join('') + '.' + r;
		}
		return 0;
	},
	// 已还本金保留两位小数
	double: function(val){
		console.log(val)
		if(val=='' || val == null || val == 'null'){
			return '';
		}else{
		num = val.toFixed(2);
		return num; 
		}
	},
	//货币格式化(¥200,500.00;后台返回分)
	moneyCent: function(val){
		if(((val != null) && (val != '')) && (val != 0)){
			var val = val/100;
			var n = 2;
			val = parseFloat((val + '').replace(/[^\d\.-]/g, '')).toFixed(n)
					+ '';
			var l = val.split('.')[0].split('').reverse();
			var r = val.split('.')[1];
			var t = '';
			for(var i = 0; i < l.length; i++){
				t += l[i]
						+ ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',' : '');
			}
			return '¥' + t.split('').reverse().join('') + '.' + r;
		}
		return 0;
	},
	//比例
	percent: function(val){
		if(val != null && val != '' && val > 0){
			return val;
		}
		return 0;
	},
	//还原货币格式(200500.00)
	removeMoneyPrefix: function(val){
		return val.replace('¥', '').replace(',', '').replace(',', '').replace(',', '');
	},
	//还原收取方式
	chargeWayReverse: function(val){
		if(val){
			if(val == '线上收取'){
				return 0;
			}else{
				return 1;
			}
		}
		return '';
	},
	//支付结果格式化
	preAmountStatus: function(val){
		if(val != null){
			if(val == 1){
				return '已支付';
			}else{
				return '未支付';
			}
		}
		return '';
	},
	//订单状态格式化
	orderStatu: function(val, row, rowIndex){	
		var val = row.riskStatus;
        if(val != null){
			if(val == 1){
				return '审核拒绝';
			}else if(val == 2){
				return '审核中';
			}else if(val == 3){
				return '已分期';
			}else if(val == 5){  
				return '待确认';
			}else if(val == 0){
				return '审核通过';
			}else if(val == 10){
				return '已结清';
			}else if(val == 11){
				return '已终止';
			}else if(val == 6){
				return '已取消';
			}else if(val == 21){
				return '待估价';
			}else if(val == 22){
				return '已估价';
			}
		}
		return '';
	},
	//支付结果格式化
	preAmtStatus: function(val){
		if(val != null){
			if(val == 1){
				return '已支付';
			}else if(val == 2){
				return '待支付';
			}else{
				return '';
			}
		}
		return '';
	},
	//增加超链接
	addLink: function(val, row, rowIndex){			
		var val = row.riskStatus;
		var valWf = row.wfStatus;
		if(val != null){
			if(val == 3){
				return '<a class="addLink">'+row.orderId+'</a>';
			}else{
				return row.orderId;
			}
		}else{
			return row.orderId;
		}		
		return '';
	},
	
	//工作流格式化
	wfStatus: function(val){
		val = val.toString();
		if(val != '' && val != 'null'){
			if(val == 9){
				return '流程结束';
			}else if(val == 0){
				return '流程拒绝';
			}else if(val == 11){
				return '待车价评估';
			}else if(val == 12){
				return '待车价复评';
			}else if(val == 2){
				return '待门店预处理';
			}else if(val == 3){
				return '待风控审批';
			}else if(val == 31){
				return '待人工风控';
			}else if(val == 4){
				return '待抵质押物估价';
			}else if(val == 41){
				return '待车秒贷审核';
			}else if(val == 5){
				return '待项目信息维护';
			}else if(val == 6){
				return '待内控审批';
			}else if(val == 7){
				return '待财务收款确认';
			}else if(val == 8){
				return '待项目初审';
			}else if(val == 81){
				return '待确认入库';
			}else if(val == 82){
				return '待终审';
			}
		}else{   
			return '待门店预处理';
		}		 
		return '';
	},
	//账单状态格式化
	status: function(val){
		if(val != null){
			if(val == 0){
				return '已逾期';
			}else if(val == 1){
				return '待还款';
			}else if(val == 2){
				return '部分还款';
			}else if(val == 3){
				return '已还款';
			}
		}		
		return '';
	},
	//支付类型
	payType: function(val){
		if(val != null){
			if(val == 11){
				return '用户支付预付款';
			}else if(val == 12){
				return '商户代偿预付款';
			}else if(val == 13){
				return '线下平账预付款';
			}else if(val == 21){
				return '用户支付分期还款';
			}else if(val == 22){
				return '商户代偿分期还款';
			}else if(val == 23){
				return '线下平账分期付款';
			}
		}		
		return '';
	},
	//资金来源
	sourcesFunding: function(val){
		if(val != null){
			if(val == 1){
				return '爱钱帮';
			}else if(val == 2){
				return '饭饭金服';
			}
		}		
		return '';
	},
	//业务类型
	bizType: function(val){
		if(val != null){
			if(val == 2001){
				return '以租代售新车';
			}else if(val == 2002){
				return '以租代售二手车';
			}else if(val == 2100){
				return '抵押车';
			}else if(val == 2200){
				return '质押车';
			}else if(val == 1100){
				return '易安家';
			}else if(val == 1000){
				return '医美';
			}else if(val == 1200){
				return '旅游';
			}else if(val == 2300){
				return '车秒贷';
			}
		}		
		return '';
	},
	//充值费
	cZF: function(val){
		 return '0';
	},
	//还款类型
	repayType: function(val){
		if(val != null){
			if(val == 22){
				return '门店代偿';
			}else if(val == 21){
				return '客户自还';
			}else if(val == 23){
				return '线下平账';
			}
		}		
		return '';
	},
	//权证资料上传状态
	uploadStatus: function(val){
		if(val != null){
			if(val == 1){
				return '待上传';
			}else if(val == 2){
				return '已上传';
			}
		}		
		return '';
	},
	//还款按钮格式化
	option: function(val, row, rowIndex){
		var val = row.preAmtStatus;
		if(val == 2){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.toPayment.turnTo(' + rowIndex + ')">代偿</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.toPayment.turnTo(' + rowIndex + ')">代偿</button>';
		}
	},
	//还款按钮格式化2
	option2: function(val, row, rowIndex){
		if(row.status <= 2){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.overdue.turnTo(' + rowIndex + ')">代偿</button>';
		}else if(row.status == 3) {
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.overdue.turnTo(' + rowIndex + ')">代偿</button>';
		}
	},
	//请款按钮格式化
	option3: function(val, row, rowIndex){
		return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.requestFunds.turnTo(' + rowIndex + ')">请款</button>';
	},
	//分期按钮格式化
	option4: function(val, row, rowIndex){
		if(row.riskStatus == 0){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}
	},
	//复选框格式化
	checkbox: function(val, row, rowIndex){
		if(row.status <= 2){
			return '<input type="checkbox" class="datagrid-row-checkbox" >';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}
	},
	//权证资料上传状态
	ck2: function(val, row, rowIndex){
		if(row.uploadStatus < 2){
			return '<input type="checkbox" class="datagrid-row-checkbox" >';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}
	},
	//暂时无效
	isPay: function(val){
		if(val < 4){
			 return true;
		}else{
			 return false;
		}
		return '';
	},
	//***
	fundSource: function(val){
		if(val != null){
			if(val == 1){
				return '爱钱帮';
			}else if(val == 2){
				return '饭饭金服';
			}
		}
		return '';
	},
	toLead: function(val){
		if(val != null){
			if(val == 1){
				return '已导入';
			}
		}
		return '';
	},
	//订单状态格式化
	riskStatus: function(val){
		if(val != null){
			if(val == 0){
				return '审核通过';
			}else if(val == 1){
				return '审核拒绝';
			}else if(val == 2){
				return '审核中';
			}else if(val == 3){
				return '已分期';
			}else if(val == 4){
				return '人工风控';
			}else if(val == 5){
				return '业务门店审核中';
			}else if(val == 6){
				return '内审审核中';
			}else if(val == 7){
				return '财务审核中';
			}else{
				return '信贷';
			}
		}
		return '';
	},
	isUseCreditLoan: function(val){	
		if(val == '0'){
			return '否';
		}else if(val == '1'){
			return '是';
		}else {
			return '';
		}
	},
	//忽略空值
	ifNull: function(val){	
		if(val != null && val != ''){
			return val;
		}
		return '';
	},
	planStatus: function(val){
		if(val != null){
			if(val == 1){
				return '启用';
			}else if(val == 2){
				return '禁用';
			}
		}
		return '';
	},
	//页面高度
	fitHeight: function(percent){
		percent = percent || 1;
		return document.body.scrollHeight * percent;
	},
	//页面宽度
	fitWidth: function(percent){
		percent = percent || 1;
		return document.body.scrollWidth * percent;
	},
	//媒体类型及其支持扩展名
	extensionName: {
		pic: ['jpg', 'JPG', 'jepg', 'JEPG', 'png', 'PNG'],
		doc: ['doc', 'docx', 'txt', 'pdf', 'xls', 'xlsx']
	},
	//根据图片路径获取图片名
	getImgName: function(val){	
		var arry = val.split('\\').reverse();
		return arry[0];
	},
	//获取文件名
	getFileName: function(val){	
		if(val.indexOf('\\') == -1){
			return val;
		}
		return val.substring(val.lastIndexOf('\\') + 1) ;
	},
	//获取文件扩展名
	getExtensionName: function(val){	
		return val.substring(val.lastIndexOf('.') + 1) ;
	},
	parseCustomerType: function(val, dictType){
		var req_data = {'dictTypeCode': dictType};
		var ret = '';
		IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
			var customerTypeArr = result.iqbResult.result;
			$.each(customerTypeArr, function(key, retVal) {
				if(val == retVal.id){
					ret = retVal.text;
				}
			});
		})
		return ret;
	},
	review: function(val){	
		if(val != null){
			val += '';
			if(val.length > 0){
				if(val == '1'){
					return '是';
				}else{
					return '否';
				}
			}			
		}
		return '';
	},
	planId:function(val){
		if(val != null){
			if(val == 1){
				return '月供3%';
			}else if(val == 2){
				return '剩余本金3‰';
			}else if(val == 3){
				return '剩余本金2‰';
			}
		}
		return '';
	},
	getPercent:function(num){
		num = parseFloat(num); 
		if (isNaN(num)) { 
			return ""; 
		} 
		return num <= 0 ? "0.00%" : (Math.round(num * 100) / 100.00 + "%"); 
	},
	carType :function(val){
		if(val != null){
			val += '';
			if(val == '1'){
				return '二手车';
			}else if(val == '2'){
				return '新车';
			}else{
				return ''; 
			}
		}
	}
};
//重写javaScript日期对象原型
Date.prototype.format = function(fmt){
	var o = {
		'M+': this.getMonth() + 1,//月份
		'd+': this.getDate(),//日
		'h+': this.getHours(),//小时
		'm+': this.getMinutes(),//分
		's+': this.getSeconds(),//秒
		'q+': Math.floor((this.getMonth() + 3) / 3),//季度
		'S': this.getMilliseconds()//毫秒
	};
	if(/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	for( var k in o)
		if(new RegExp('(' + k + ')').test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
	return fmt;
}

//重写javaScript数组对象原型
Array.prototype.contain = function(e){  
	for(i = 0; i < this.length; i ++){  
		if(this[i] == e){
			return true;
		} 			  
	}  
	return false;  
}  