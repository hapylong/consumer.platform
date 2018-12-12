/**
 * @author
 * @Version
 * @DateTime
 */
$package('WF');
var WF = {

	// 时间戳转日期字符串
	time : function(val) {
		if (val) {
			val = parseInt(val, 10) * 1000;
			var date = new Date(val / 1000);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	// 时间戳转日期字符串
	exportxml : function(val,row) {
		return '<button type="button" class="btn btn-link" onclick="IQB.wfDesign.exporturl(\'' + row.id + '\')">'+row.key+'</button>';
	},

	// 时间戳转日期字符串
	timeno : function(val) {
		if (val) {
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	// 格式化函数
	formatter : {
		
		wfStatus : function(val) {
			if (val == "1") {
				return "激活";
			} else if (val == "2") {
				return "挂起";
			}  else{
				return "";
			}
		},
		
		taskStatus : function(val) {
			if (val == "1") {
				return "待签收";
			} else if (val == "2") {
				return "待处理";
			} else if (val == "3") {
				return "已处理";
			} else if (val == "4") {
				return "已终止";
			}
		},

		procStatus : function(val) {
			if (val == "0") {
				return "审批中";
			} else if (val == "3") {
				return "已暂停";
			} else if (val == "4") {
				return "已终止";
			} else if (val == "5") {
				return "已删除";
			} else if (val == "6") {
				return "已完成";
			}
		},
		approve : function(val) {
			if (val == "1") {
				return "通过";
			} else if (val == "0") {
				return "拒绝";
			} else {
				return "";
			}
		}
	},

}
