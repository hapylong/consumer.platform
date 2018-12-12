/**
 * @author
 * @Version
 * @DateTime
 */
$package('Formatter');
var Formatter = {
	//审核结果格式化
	approve: function(val){
		if(val == '1'){
			return '通过';
		}else if (val == '0'){
			return '拒绝';
		}else if (val == '2'){
			return '退回';
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
			return '待手工签约';
		}else if (val == '2'){
			return '签约完毕';
		}else if (val == '3'){
			return '需电子签约';
		}else{
			return '';
		}
	},
	showContractTime: function(val, row, rowIndex){
		var status = row.contractStatus;
		var val = row.updateTime;
		if(val != null){
			if(status == 2){
				return val;
			}else{
				return '';
			}
		}
		return '';
	},
	trailerReason: function(val){
		if(val == '1'){
			return '还款逾期';
		}else if (val == '2'){
			return '车辆异常';
		}else if (val == '3'){
			return '其他';
		}else if (val == '4'){
			return '用户异常';
		}
		else{
			return '';
		}
	},
	//是否为空
	isNull: function(val){
		if(val != null){
			return val;
		}
		else{
			return '';
		}
	},
	//是否为空
	isNull1: function(val){
		if(val != null&&val != 0){
			return val;
		}
		else{
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
	isMobileCollection: function(val){
		if(val == '1'){
			return '空号';
		}else if (val == '2'){
			return '停机';
		}else if (val == '3'){
			return '关机';
		}else if (val == '4'){
			return '无法接通';
		}else if (val == '5'){
			return '无人接听';
		}else if (val == '6'){
			return '不是本人';
		}else if (val == '7'){
			return '拒绝还款';
		}else if (val == '8'){
			return '其他';
		}else if (val == '9'){
			return '正常';
		}else{
			return val;
		}
	},
	isTelRecord: function(val){
		if(val == '1'){
			return '正常';
		}else if (val == '2'){
			return '失败';
		}else{
			return val;
		}
	},
	isDealReason: function(val){
		if(val == '1'){
			return '保留任务';
		}else if (val == '2'){
			return '转外催';
		}else if (val == '3'){
			return '补充资料';
		}else{
			return val;
		}
	},
	isFailReason: function(val){
		if(val == '1'){
			return '空号';
		}else if (val == '2'){
			return '停机';
		}else if (val == '3'){
			return '关机';
		}else if (val == '4'){
			return '无法接通';
		}else if (val == '5'){
			return '无人接听';
		}else if (val == '6'){
			return '不是本人';
		}else if (val == '7'){
			return '拒绝还款';
		}else if (val == '8'){
			return '其他';
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
	time3: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日 ');
		}
		return '';
	},
	//是否的判断
	yOrn: function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
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
	ecType: function(val){
		if(val != null) {
			if(val == 'dz'){
				return '<span class="label label-success">电子合同</span>';
			}
			if(val == 'zz'){
				return '<span class="label label-success">纸质合同</span>';
			}
		}
		return '';
	},
//	房贷业务类型
	businessType: function(val){
		if(val != null){
			if(val == 8003){
				return '房屋质押一抵';
			}else if(val == 8004){
				return '房屋质押二抵';
			}else if(val == 8002){
				return '过桥垫资';
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
	datePart: function(val){
		if(val) {
			var s = val.split(" ")[0];
			var data = s.split("-");
			return data[0] + "年" + data[1] + "月" + data[2] + "日";
		}
		return "";
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
			}else if(val == 3001){
				return '交易权质押类';
			}else if(val == 3002){
				return '周转贷';
			}else if(val == 4001){
				return '融资租赁';
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
	//货币格式化(¥200,500.00)---为空时返回空
	moneyZero: function(val){
		val = String(val);
		if(((val != 'null') && (val != ''))){
			if(val != 0){
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
			}else{
				return '0';
			}
		}
		return '';
	},
	//货币格式化(200,500.00 ---- 返回万元)
	moneyTs: function(val){
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
			return t.split('').reverse().join('') + '.' + r;
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
	//货币格式化(¥200,500.00;后台返回元    按照万元展示)
	moneyMi: function(val){
		if(((val != null) && (val != '')) && (val != 0)){
			var val = val/10000;
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
	removeMoneyFormatter: function(val){
		return val.replace(',', '').replace(',', '').replace(',', '');
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
		val = Number(val);
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
			}else if(val == 7){
				return '已放款';
			}
		}
		return '';
	},
	//订单状态格式化(蒲公英)
	orderStatus: function(val){	
        if(val != null){
			if(val == 1){
				return '审核拒绝';
			}else if(val == 2){
				return '审核中';
			}else if(val == 3){
				return '已分期';
			}else if(val == 4){
				return '待支付';
			}else if(val == 0){  
				return '审核通过';
			}else if(val == 5){  
				return '待确认';
			}else if(val == 7){
				return '已放款';
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
			if(val == 3 || val == 7 || val == 10){
				return '<a class="addLink" onclick="IQB.orderQuery.forBill(' + rowIndex + ')">'+row.orderId+'</a>';
			}else{
				return row.orderId;
			}
		}else{
			return row.orderId;
		}		
		return '';
	},
	//增加超链接
	addLink4: function(val, row, rowIndex){			
		var val = row.riskStatus;
		var valWf = row.wfStatus;
		if(val != null){
			if(val == 3 || val == 10){
				return '<a class="addLink" onclick="IQB.orderQuery.forBill(' + rowIndex + ')">'+row.orderId+'</a>';
			}else{
				return row.orderId;
			}
		}else{
			return row.orderId;
		}		
		return '';
	},
	//增加超链接
	addLink2: function(val, row, rowIndex){			
		var val = row.riskStatus;
		if(val != null){
			if(val == 3){
				return '<a class="addLink" onclick="IQB.stageOrder.forBill(' + rowIndex + ')">'+row.orderId+'</a>';
			}else{
				return row.orderId;
			}
		}else{
			return row.orderId;
		}		
		return '';
	},
	//增加超链接
	addLink3: function(val, row, rowIndex){
		var val = row.riskStatus;
		if(val != null){
			if(val == 3){
				return '<a class="addLink" onclick="IQB.assetSplitDetail.forBill(' + rowIndex + ')">'+row.orderId+'</a>';
			}else{
				return row.orderId;
			}
		}else{
			return row.orderId;
		}		
		return '';
	},
	orderId : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.carStatusQuery.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	orderIdProcess : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.outsource.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	cusFoOrderId : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null){
			if(row.riskStatus == 3 || row.riskStatus == 7){
				return '<a class="addLink" onclick="IQB.customerFollow.forBillDetail(' + rowIndex + ')">'+val+'</a>';
			}else{
				return val;
			}
		}else{
			return val;
		}	
		return '';
	},
	loanOrderId : function(val,row,rowIndex){
		if(val != null){
			return '<a class="addLink" onclick="IQB.loanManage.forBillDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	billRecord : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null){
			return '<a class="addLink" onclick="IQB.breakRecord.forBillDetail(' + rowIndex + ')">查看</a>';
		}else{
			return '查看';
		}	
		return '';
	},
	orderIdGps : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.gpsStatusTrace.approveDetail(' + rowIndex + ')">'+val+'</a>';
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
			}else if(val == 1){
				return '待客服派单';
			}else if(val == 11){
				return '待车价评估';
			}else if(val == 12){
				return '待车价复评';
			}else if(val == 2){
				return '待门店预处理';
			}else if(val == 3){
				return '待风控审批';
			}else if(val == 30){
				return '待外访审批';
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
			}else if(val == 21){
				return '待客服录入';
			}else if(val == 42){
				return '待外访资料上传';
			}else if(val == 43){
				return '待总部信审';
			}else if(val == 44){
				return '待门店额度确认';
			}else if(val == 51){
				return '待签订合同';
			}else if(val == 62){
				return '待合同审核';
			}else if(val == 45){
				return '待门店签约';
			}else if(val == 63){
				return '待门店放款';
			}else if(val == 32){
				return '待风控经理审核';
			}else if(val == 22){
				return '待电核审批';
			}else if(val == 71){
				return '待线下费用确认';
			}else if(val == 83){
				return '待GPS确认';
			}else if(val == 33){
				return '待并行审批结束';
			}
		}else{   
			return '待门店预处理';
		}		 
		return '';
	},
	//车主贷工作流格式化
	wfStatu: function(val){
		val = val.toString();
		if(val != '' && val != 'null'){
			if(val == 100){
				return '流程结束';
			}else if(val == 0){
				return '流程拒绝';
			}else if(val == 10){
				return '待车辆勘察评估';
			}else if(val == 15){
				return '待门店风控审批';
			}else if(val == 20){
				return '待读脉审核';
			}else if(val == 25){
				return '待客服电核';
			}else if(val == 30){
				return '待风控初审';
			}else if(val == 35){
				return '待风控终审';
			}else if(val == 40){
				return '待门店签约';
			}else if(val == 45){
				return '待确认gps安装';
			}else if(val == 50){
				return '待合同审核';
			}else if(val == 55){
				return '待gps信号确认';
			}else if(val == 60){
				return '待结算岗';
			}else if(val == 65){
				return '待放款确认';
			}
		}else{   
			return '待门店预处理';
		}		 
		return '';
	},
	//账单状态格式化
	status: function(val){
		val = val.toString();
		if(val != 'null' && val != ''){
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
			}else if(val == 32){
				return '退租';
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
			}else if(val == 3){
				return '交易所';
			}else if(val == 4){
				return '神仙有财';
			}else if(val == 41){
				return '京金所';
			}
		}		
		return '';
	},
	sourcesFundingJys: function(val){
		if(val != null){
			if(val == 1){
				return '饭团';
			}else if(val == 2){
				return '金鳞';
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
			}else if(val == 2400){
				return '车主贷';
			}else if(val == 3002){
				return '华益周转贷';
			}else if(val == 4001){
				return '融资租赁';
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
			}else if (val == 2){
				return '待审核';
			}else if(val == 3){
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
		if(row.riskStatus == "0" && row.wfStatus == "9"){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}
	},
	option5: function(val, row, rowIndex){
		if(row.riskStatus == 3 || row.riskStatus == 7){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.changePlan.resetPlan(' + rowIndex + ')">重置分期</button>';// + '<button type="button" class="btn btn-default btn-sm btn-success" style="margin-top:10px" onclick="IQB.changePlan.stopPlan(' + rowIndex + ')">停止分期</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success">重置分期</button>';// + '<button type="button" disabled style="margin-top:10px" class="btn btn-default btn-sm btn-success">停止分期</button>';
		}
	},
	//交易所订单分期按钮格式化
	option6: function(val, row, rowIndex){
		if(row.riskStatus == 9){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.stageOrder.stage(' + rowIndex + ')">分期</button>';
		}
	},
	//房贷分期按钮格式化
	option7: function(val, row, rowIndex){
		if(row.riskStatus != 0){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.hyjfStageOrder.stage(' + rowIndex + ')">分期</button>';
		}else{
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.hyjfStageOrder.stage(' + rowIndex + ')">分期</button>';
		}
	},
	//资产分配按钮格式化
	option8: function(val, row, rowIndex){
		if(row.status == 2){
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.requestFundsDetail.openUpdateWin(' + rowIndex + ')">失败</button>';
		}else if(row.status == 1){
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success">成功</button>';
		}
	},
	//车主贷分期按钮格式化
	option9: function(val, row, rowIndex){
		if(row.riskStatus == "7"){
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
	//复选框格式化
	checkbox1: function(val, row, rowIndex){
		if(row.riskStatus == 3){
			return '<input type="checkbox" class="datagrid-row-checkbox" >';
		}else{
			return '';
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
			}else if(val == 7){
				return '已放款';
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
	creditType : function(val){
		val = String(val);
		if(val != null){
			if(val == '1'){
				return '单签类';
			}else if(val == '2'){
				return '担保类';
			}else if(val == '3'){
				return '双签类';
			}else if(val == '4'){
				return '互保类';
			}else if(val == '5'){
				return '主借人互换类';
			}
		}
		return '';
	},
	borrowTogether : function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else if(val == 0){
				return '否';
			}
		}
		return '';
	},
	carStatu : function(val){
		if(val != null){
			if(val == 1){
				return '抵押';
			}else if(val == 0){
				return '无';
			}else if(val == 2){
				return '质押';
			}
		}
		return '';
	},
	buyWay : function(val){
		if(val != null){
			if(val == 1){
				return '全款';
			}else if(val == 2){
				return '按揭';
			}
		}
		return '';
	},
	key : function(val){
		if(val != null){
			return '<span style="word-break:break-all">'+ val +'</span>';
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
		doc: ['doc', 'docx', 'txt', 'pdf', 'xls', 'xlsx'],
		wP:['doc', 'pdf']
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
	},
	greenChannel :function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '2'){
				return '绿色通道';
			}else{
				return '非绿色通道';
			}
		}
		return '';
	},
	//汽车类型
	carType :function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '二手车';
			}else if(val == '2'){
				return '新车';
			}
		}
		return '';
	},
	//抵押撤评估状态
	pledgeStatus :function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '评估中';
			}else if(val == '2'){
				return '已估价';
			}else if(val == '3'){
				return '已退回';
			}else if(val == '4'){
				return '已使用';
			}else if(val == '5'){
				return '已拒绝';
			}
		}
		return '';
	},
	custChannels :function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '业务员';
			}else if(val == '2'){
				return '朋友推荐';
			}else if(val == '3'){
				return '车商管理';
			}else if(val == '4'){
				return '4S店';
			}
		}
		return '';
	},
	SourceCar :function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '3'){
				return '车商';
			}else if(val == '4'){
				return '4S店';
			}else if(val == '1'){
				return '个人';
			}else if(val == '2'){
				return '自己';
			}
		}
		return '';
	},
	maritalStatus:function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '未婚';
			}else if(val == '2'){
				return '已婚';
			}
		}
		return '';
	},
	settleApply : function(val){
		val = String(val);
		if(val == '1'){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled/>';
		}else if(val == '2'){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled/>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox"/>';
		}
	},
	settleStatus : function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '审核中';
			}else if(val == '2'){
				return '审核通过';
			}else if(val == '3'){
				return '审核拒绝';
			}else if(val == '4'){
				return '流程终止';
			}
		}
		return '';
	},
	payMethod : function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '线上支付';
			}else if(val == '2'){
				return '线下支付';
			}
		}
		return '';
	},
	earlyPay : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.storeApply.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	earlyPayOption : function(val, row, rowIndex){
		return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.earlyPaycompensatory.turnTo(' + rowIndex + ')">代偿</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.earlyPaycompensatory.turnToSplit(' + rowIndex + ')">拆分支付</button>';
	},
	deductStatusCheckbox : function(val){
		val = String(val);
		if(val == '1' || val == '4' || val == '5'){
			return '<input type="checkbox" class="datagrid-row-checkbox"/>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled/>';
		}
	},
	deductStatus : function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '未发送';
			}else if(val == '2'){
				return '发送成功';
			}else if(val == '3'){
				return '划扣成功';
			}else if(val == '4'){
				return '划扣部分成功';
			}else if(val == '5'){
				return '划扣失败';
			}
		}
		return '';
	},
	feeratio:function(val){
		if(val != null && val != ''){
			val = Formatter.accMul(val,12);
			return val;
		}
		return '';
	},
	accMul: function(arg1,arg2){    
		var m=0,s1=arg1.toString(),  
		s2=arg2.toString();    
		try{  
		m+=s1.split(".")[1].length}catch(e){}    
		try{  
		m+=s2.split(".")[1].length}catch(e){}    
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	},
	url : function(val, row, rowIndex){
		return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.assetSplitDetail.alert(' + rowIndex + ')">获取</button>';
	},
	userType:function(val){
		if(val != null && val != ''){
			val = String(val);
			if(val == '1'){
				return '共签人';
			}else if(val == '2'){
				return '担保人';
			}else if(val == '3'){
				return '互保人';
			}
		}
		return '';
	},
	fixedOverdueAmt:function(val, row, rowIndex){
		var val = row.status;
		var valx = row.fixedOverdueAmt;
		if(val != null){
			if(val == 0){
				return Formatter.money(valx);
			}else{
				return '';
			}
		}else{
			return '';
		}		
	},
	//订单状态格式化(蒲公英)
	carStatus: function(val){	
        if(val != null){
        	if(val == 20){
				return '已失联';
			}else if(val == 25){
				return '待入库';
			}else if(val == 30){
				return '已入库';
			}else if(val == 35){
				return '待出售';
			}else if(val == 40){
				return '已出售';
			}else if(val == 45){
				return '待转租';
			}else if(val == 50){  
				return '已转租';
			}else if(val == 55){
				return '待返还';
			}else if(val == 60){
				return '已返还';
			}else if(val == 65){
				return '未拖回';
			}else if(val == 70){
				return '正常';
			}
		}
		return '正常';
	},
	//gps状态
	gpsStatus:function(val, row, rowIndex){
		var val = row.gpsStatus;
		if(val != null && val != ''){
			if(val == '1'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'正常</a>';
			}else if(val == '2'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'有线离线</a>';
			}else if(val == '3'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'无线离线</a>';
			}else if(val == '4'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'断电报警</a>';
			}else if(val == '5'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'拆除报警</a>';
			}else if(val == '6'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'其它</a>';
			}else if(val == '7'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'全部离线</a>';
			}else if(val == '8'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'出省</a>';
			}else if(val == '0'){
				return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'未标记</a>';
			}
		}
		return '<a class="addLink" onclick="IQB.carStatusQuery.approveGpsDetail(' + rowIndex + ')">'+'未标记</a>';
	},
	gpsStatusShow : function(val){
		if(val != null && val != ''){
			if(val == '1'){
				return '正常';
			}else if(val == '2'){
				return '有线离线';
			}else if(val == '3'){
				return '无线离线';
			}else if(val == '4'){
				return '断电报警';
			}else if(val == '5'){
				return '拆除报警';
			}else if(val == '6'){
				return '其它';
			}else if(val == '7'){
				return '全部离线';
			}else if(val == '8'){
				return '出省';
			}else if(val == '0'){
				return '未标记';
			}
		}
		return '未标记';
	},
	billStatus : function(val){
		if(val != null && val != ''){
			if(val == '0'){
				return '逾期';
			}else if(val == '1'){
				return '待还款';
			}else if(val == '2'){
				return '部分还款';
			}else if(val == '3'){
				return '已还款';
			}
		}
		return '';
	},
	billInfoStatus : function(val){
		if(val == '0'){
			return '已逾期';
		}else if(val == '1'){
			return '未还款';
		}else if(val == '2'){
			return '部分还款';
		}else if(val == '3'){
			return '全部还款';
		}else if(val == '4'){
			return '失效账单';
		}
	},
	checkRedemption: function(val, row, rowIndex){
		var timestamp = new Date();
		var deadline = row.deadline;
		deadline = new Date(deadline.replace(/-/g, '/'));
		deadline = deadline.getTime();
		if((timestamp >= deadline) || row.redemptionDate != null){
			//当系统当前时间大于或等于标的到期日或标的已赎回过  不可赎回
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	checkDeleteRecord: function(val, row, rowIndex){
		var redemptionDate = row.redemptionDate;
		var applyInstIDay = row.applyInstIDay; 
		if(redemptionDate != null || (applyInstIDay != '0' && applyInstIDay != null)){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	clearingStatus : function(val){
		if(val != null){
			if(val == 1){
				return '未结算';
			}else if(val == 2){
				return '待结算';
			}else if(val == 3){
				return '已结算';
			}
		}		
		return '';
	},
	lendersSubject : function(val){
		if(val != null){
			return IQB.formatterDictTypeT(val, 'Lenders_Subject');
		}		
		return '';
	},
	violationStatus : function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '未违章';
			}else{
				return '已违章';
			}
		}		
		return '';
	},
	disposeStatus : function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '未处理';
			}else{
				return '已处理';
			}
		}		
		return '';
	},
	violationDetail : function(val, row, rowIndex){
		if(val != null){
			return '<a class="violationDetail" onclick="IQB.violationQuery.violationDetail(' + rowIndex + ')">'+row.orderId+'</a>';
		}		
		return '';
	},
	checkboxAfterLoan : function(val, row, rowIndex){
		if(row.status == '处理中' && row.dealOpinion == '转外催'){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	checkboxAfterLoan2 : function(val, row, rowIndex){
		if(row.status == '处理中' && row.mobileDealOpinion == '转外催'){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	disposeWay : function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '转外包';
			}else{
				return '转法务';
			}
		}		
		return '';
	},
	register : function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '我方立案';
			}else{
				return '委托机构';
			}
		}		
		return '';
	},
	orderIdCase : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.caseAcceptance.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	orderIdCasePro : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.caseQuery.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	orderIdCaseFw : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.fwQuery.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	orderIdInfo : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.recordApplication.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	caseProgress : function(val){
		if(val != null){
			if(val == 5){
				return '待贷后处理';
			}else if(val == 10){
				return '贷后处理中';
			}else if(val == 15){
				return '待外包处理';
			}else if(val == 20){
				return '外包处理中';
			}else if(val == 25){
				return '法务处理中';
			}else if(val == 30){
				return '处理结束';
			}
		}		
		return '';
	},
	caseStatus : function(val){
		if(val != null){
			if(val == 5){
				return '待法务受理';
			}else if(val == 10){
				return '资料准备中';
			}else if(val == 15){
				return '立案申请中';
			}else if(val == 20){
				return '已立案';
			}else if(val == 25){
				return '庭审登记中';
			}else if(val == 30){
				return '执行中';
			}else if(val == 35){
				return '结束';
			}
		}		
		return '';
	},
	caseSource : function(val){
		if(val != null){
			if(val == 1){
				return '贷后转入';
			}else if(val == 2){
				return '法务新增';
			}
		}		
		return '';
	},
	executeResult : function(val){
		if(val != null){
			if(val == 1){
				return '全部执行';
			}else if(val == 2){
				return '部分执行';
			}else if(val == 3){
				return '未执行';
			}else if(val == 4){
				return '失信被执行人';
			}
		}		
		return '';
	},
	//是否为原告
	accuserFlag: function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '是';
			}else if(val == 2){
				return '否';
			}else{
				return '';
			}
		}
		return '';
	},
	//车务审核状态
	checkStatus: function(val){
		if(val != null && val != ''){
			if(val == 3){
				return '未提交';
			}else if(val == 2){
				return '已审核';
			}else if(val == 1){
				return '审核中';
			}else{
				return '';
			}
		}
		return '';
	},
	//勾选可处理的车务
	checkTraffic : function(val, row, rowIndex){
		var val = row.checkStatus;
		//1--审核中
		if(val == 1){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	forDetail : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.trafficQuery.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	forRiskDetail : function(val, row, rowIndex){
		var val = row.orderId;
		if(val != null && val != ''){
			return '<a class="addLink" onclick="IQB.monitorQuery.approveDetail(' + rowIndex + ')">'+val+'</a>';
		}
		return '';
	},
	riskLevel : function(val){
		if(val != null && val != ''){
			if(val == 'CHE300PLM030'){
				return '高风险';
			}else if(val == 'CHE300PLM020'){
				return '中风险';
			}else if(val == 'CHE300PLM010'){
				return '低风险';
			}else{
				return '';
			}
		}
		return '';
	},
	sendStatus : function(val){
		if(val != null && val != ''){
			if(val == 1){
				return '未发送';
			}else if(val == 3){
				return '已发送';
			}else if(val == 9){
				return '发送失败';
			}else{
				return '';
			}
		}
		return '';
	},
	//推标方式格式化
	pushMode: function(val){	
		if(val != null){
			if(val == 1){
				return '按订单金额';
			}else if(val == 2){
				return '按剩余未还本金';
			}else{
				return '';
			}
		}
		return '';
	},
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
} ;
