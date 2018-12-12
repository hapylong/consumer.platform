function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}
var flag = 3;
var id = getQueryString('id');
function Subtr(arg1,arg2){ 
	  var r1,r2,m,n; 
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	  m=Math.pow(10,Math.max(r1,r2)); 
	  n=(r1>=r2)?r1:r2; 
	  return ((arg1*m-arg2*m)/m).toFixed(n); 
	}
/*orderId = '20170103-267322';*/
$package('IQB.splitCommit');
IQB.splitCommit = function() {
    var _this = {
        cache: {
            viewer: {}
        },	
        // 数据字典渲染
        initSelect: function(delistingMechanism){
    		/*var req_data = {'key': "delistingMechanism" };
    		IQB.post(urls['rootUrl'] + '/delistingMechanism/selDictItem', req_data, function(result){
    			$('#' + "delistingMechanism").prepend("<option value=''>请选择</option><option value='"+result.iqbResult.result[0].value+"'>"+result.iqbResult.result[0].value+"</option><option value='"+result.iqbResult.result[1].value+"'>"+result.iqbResult.result[1].value+"</option>");
    			return result.iqbResult.result;
    		})*/
        	var req_data = {'key': "delistingMechanism" };
    		IQB.post(urls['rootUrl'] + '/product/delistingMechanism/selDictItem', req_data, function(result){
    			var result = result.iqbResult.result;
    			var optionHtml = '';
    			for(var i=0;i<result.length;i++){
    				optionHtml += "<option value='"+result[i].dictValue+"'>"+result[i].dictValue+"</option>"
    			}
    			$('#delistingMechanism').append(optionHtml);
    		})
    	},
        // 回显赋值 
        showHtml: function() {
            IQB.post(urls['cfm'] + '/assetallocine/get_division_assets_prepare/' + id, {}, function(result){
                $('#updateForm').form('load', result.iqbResult.result);   
                $('#beginInterestDate').val(Formatter.timeCfm2(result.iqbResult.result.beginInterestDate));
                $('#expireDate').val(Formatter.timeCfm2(result.iqbResult.result.expireDate));
            });
        },
        // 拆分规则
        breakType: function(){
            var checkValue=$("#breakType").val();
            if(checkValue==1){
                $('.copies').show();
                $('.money').hide();
                $("#splitShare").validatebox('enableValidation');
                $("#amountPerServing").validatebox('disableValidation');
            }else if(checkValue==2){
                $('.copies').hide();
                $('.money').show();
                $("#splitShare").validatebox('disableValidation');
                $("#amountPerServing").validatebox('enableValidation');
            }else{
                $('.copies').hide();
                $('.money').hide();
            }
        },
        // 拆分
        split: function() {
            var checkValue=$("#breakType").val();
            // 按金额分配 金额余数 num 份数 splitShares
            if($('#updateForm').form('validate') && checkValue==2){
                var orderAmt=$('#orderAmt').val();
                var amountPerServing = $('#amountPerServing').val();
                var splitShare=orderAmt / amountPerServing;
                var splitShares = Math.floor(splitShare);
                var amtpre = parseFloat(amountPerServing)*parseFloat(splitShares);
                var num = window.Subtr(orderAmt, amtpre);
                $("#btn-split").attr("disabled",true);
                if(num == 0){
                	for (var i=1; i<=splitShares;i++) {
                        appendTr= "";
                        appendTr="<tr class='Newtr'><td class='number'>"+ i +"</td><td class='bOrderId'>"+ $('#orderId').val() +" - "+$('#packageId').val()+" - "+ 0 +""+ i +"</td><td class='orderId'>"+ $('#orderId').val() +"</td><td class='raiseInstitutions'>"+ $('#raiseInstitutions').val() +"</td><td class='bOrderAmt'>"+ $('#amountPerServing').val() +"</td><td class='orderItems'>"+ $('#orderItems').val() +"</td><td class='fee'>"+ $('#fee').val() +"</td><td class='delistingMechanism'>"+ $('#delistingMechanism').val() +"</td><td class='expireDate'>"+ $('#expireDate').val() +"</td><td class='recordNum'>"+ $('#recordNum').val() +"</td></tr>"
                            $("#datagrid thead").append(appendTr);
                            $.parser.parse();
                    }
                	$("#datagrid").find("tr:last").children("td").eq(4).text($('#amountPerServing').val())
                }else {
                	for (var i=1; i<=splitShares+1;i++) {
                        appendTr= "";
                        appendTr="<tr class='Newtr'><td class='number'>"+ i +"</td><td class='bOrderId'>"+ $('#orderId').val() +" - "+$('#packageId').val()+" - "+ 0 +""+ i +"</td><td class='orderId'>"+ $('#orderId').val() +"</td><td class='raiseInstitutions'>"+ $('#raiseInstitutions').val() +"</td><td class='bOrderAmt'>"+ $('#amountPerServing').val() +"</td><td class='orderItems'>"+ $('#orderItems').val() +"</td><td class='fee'>"+ $('#fee').val() +"</td><td class='delistingMechanism'>"+ $('#delistingMechanism').val() +"</td><td class='expireDate'>"+ $('#expireDate').val() +"</td><td class='recordNum'>"+ $('#recordNum').val() +"</td></tr>"
                            $("#datagrid thead").append(appendTr);
                            $.parser.parse();
                    }
                	$("#datagrid").find("tr:last").children("td").eq(4).text(num)
                }        
            }else if($('#updateForm').form('validate') && checkValue==1){ // 按份数分配  金额 amountPerServing
                var orderAmt=$('#orderAmt').val();
                var amountPerServing= Math.floor(orderAmt / $('#splitShare').val());
                var amtpre = parseFloat(amountPerServing)*parseFloat($('#splitShare').val());
                var num = window.Subtr(orderAmt, amtpre);
                $("#btn-split").attr("disabled",true);
                for (var i=1; i<=$('#splitShare').val();i++) {
                    appendTr= "";
                    appendTr="<tr class='Newtr'><td class='number'>"+ i +"</td><td class='bOrderId'>"+ $('#orderId').val() +" - "+$('#packageId').val()+" - "+ 0 +""+ i +"</td><td class='orderId'>"+ $('#orderId').val() +"</td><td class='raiseInstitutions'>"+ $('#raiseInstitutions').val() +"</td><td class='bOrderAmt'>"+ amountPerServing +"</td><td class='orderItems'>"+ $('#orderItems').val() +"</td><td class='fee'>"+ $('#fee').val() +"</td><td class='delistingMechanism'>"+ $('#delistingMechanism').val() +"</td><td class='expireDate'>"+ $('#expireDate').val() +"</td><td class='recordNum'>"+ $('#recordNum').val() +"</td></tr>"
                        $("#datagrid thead").append(appendTr);
                        $.parser.parse();
                }
                $("#datagrid").find("tr:last").children("td").eq(4).text(parseFloat(parseFloat(amountPerServing)+parseFloat(num)));
            }  
            flag = 1; 
        },
        // 保存
        save: function(){
        	$('#btn-save').attr('disabled',true);
            if($('#updateForm').form('validate') && flag==1){
            	var list = [];
            	var trLength = $("#datagrid").find('tr').length-1;
				for (var i = 0; i<trLength; i++) {         
					list.push({});
				}
				for (var i = 1; i<list.length+1; i++) {
					list[i-1].bOrderId = datagrid.rows[i].cells[1].innerHTML;
					list[i-1].orderId = $('#orderId').val();
					list[i-1].bOrderAmt = datagrid.rows[i].cells[4].innerHTML;
					list[i-1].orderItems = datagrid.rows[i].cells[5].innerHTML;
					list[i-1].recordNum = datagrid.rows[i].cells[9].innerHTML;
					list[i-1].breakType = $("#breakType").val();
					list[i-1].delistingMechanism = $('#delistingMechanism').val();
					list[i-1].status = 1;
					list[i-1].remark = $('#remark').val();
					list[i-1].breakPackNum = trLength;
					list[i-1].jysOrderId = $('#id').val();
				}
				
				var data={
						'children' : list,
						'id': id,
						'bakOrgan':$('#bakOrgan').val(),
						'url':$('#url').val(),
						'proDetail':$('#proDetail').val(),
						'tranCondition':$('#tranCondition').val(),
						'safeWay':$('#safeWay').val()
				}	
               IQB.postIfFail(urls['cfm'] + '/assetallocine/persist_child_assets', data, function(result){
            	   if(result.success=="1"){
            		    var url = window.location.pathname;
	   					var param = window.parent.IQB.main2.fetchCache(url);
	   					var callback = '_this.clickCloseTab(\'' + param.tabId + '\')';
	   					var callback2 = '';
	   					//var callback2 = '_this.openTab(\'' + param.lastTab.tabId + '\', \'' + param.lastTab.tabTitle + '\', \'' + param.lastTab.tabUrl + '\',' + false + ',' + true + ',' + null + ')';
	   					window.parent.IQB.main2.call(callback, callback2);
            	   }else{
   					   IQB.alert(result.retUserInfo);
   					   $('#btn-save').removeAttr('disabled');
            	   }
               })
            }else{
            	$('#btn-save').removeAttr('disabled');
            }
        },
        // 删除
        remove: function(){
            $("#btn-split").attr("disabled",false);
            $('.Newtr').remove();
            flag = 0;
        },
        // 关闭
        close: function(){
            window.parent.IQB.main2.closeTab(window.parent.IQB.main2.tab)
        },
        recordNum:function(){
        	IQB.post(urls['cfm'] + '/ex/selRecordAssets',{'assetNumber':$('#recordNum').val()} , function(result){
        		var result = result.iqbResult.result;
        		if(result != null){
        			//$('#proName').val(result.assetName);
        			$('#bakOrgan').val(result.assetFax);
        			$('#url').val(result.assetUrl);
        		}
        	})
        },
        init: function() {       
            _this.showHtml();
            _this.initSelect();
            $('#breakType').on('click', function(){_this.breakType()});
            $('#btn-split').on('click', function(){_this.split()}); 
            $('#btn-save').unbind("click").on('click', function(){_this.save()});
            $('#btn-remove').on('click', function(){_this.remove()});
            $('#btn-close').on('click', function(){_this.close()});
            $('#recordNum').on('blur',function(){_this.recordNum()});
        }
    }
    return _this;
}();
$(function() {
    IQB.splitCommit.init();
});