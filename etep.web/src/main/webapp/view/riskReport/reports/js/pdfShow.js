
function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

var url = getQueryString('url');

$package('IQB.pdfShow');
IQB.pdfShow = function(){
	var _this = {
		cache: {
		},	
		init: function(){ 	
            $('iframe').attr('src',window.url);
		}
	}
	return _this;
}();

$(function(){
	IQB.pdfShow.init();
});		