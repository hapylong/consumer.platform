/**
 * 后台访问url
 */
//var req_ip = 'http://47.94.240.53:8088';//本地
//var req_ip = 'http://47.94.240.53:8088';//consumer开发
//var req_ip = 'http://123.56.186.143:8088';//etep测试
//var req_ip = 'https://tst.zhongezc.com';//consumer测试
//var req_ip = 'https://112.126.81.154';//consumer测试https
var req_ip = 'https://www.zhongezc.com';//consumer生产
//var req_ip = 'https://114.215.252.24';//consumer生产https
//var project_name = '/etep.front';
var project_name = '/consumer.manage.front';
var urls= {
	
	rootUrl: req_ip + project_name,
	baseUrl: req_ip,
	webUrl: req_ip + '/etep.web',	
	imgUrl: req_ip + '/uploadData',
	sysmanegeUrl: req_ip + project_name,
	workflowUrl: req_ip + project_name,
	cfm: req_ip + project_name,
	cmf: req_ip + project_name,

}
