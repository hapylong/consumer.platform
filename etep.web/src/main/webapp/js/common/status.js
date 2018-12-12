$package('STATUS');
var STATUS = {
	formatter:{
		derateType : function(val) {
			if (val == "1") {
				return "有责减免";
			} else if (val == "2") {
				return "无责减免";
			}  else{
				return "";
			}
		},
		applyStatus : function(val) {
			if (val == "1") {
				return "申请中";
			} else if (val == "2") {
				return "已通过";
			} else if (val == "3") {
				return "已拒绝";
			} else{
				return "";
			}
		}
	},	
}