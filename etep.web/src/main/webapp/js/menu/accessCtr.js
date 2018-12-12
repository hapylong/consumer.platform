$package('IQB.cardealer');
IQB.accessCtr = function() {
	var _this = {
		cache :{
			page:0
		},
		change : function() {	
			var codeA = $('#codeA').val();
			var codeB =	$('#codeB').val();
			var data = {
					"codeA" : codeA,
					"codeB" : codeB
			}
			if((codeA=='') || (codeB=='')){
				alert('请输入机构代码')
			}else{
			IQB.post(urls['rootUrl'] + '/admin/CAS_orgcode', data, function(result){
				if(result.iqbResult.result == "success") {
					alert("操作成功");
				}
			})
			}
		},
		init : function() {
			$("#change").click(function(){_this.change();})
		}
	}
	return _this;
}();


$(function(){
	IQB.accessCtr.init();
});
