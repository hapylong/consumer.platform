package com.iqb.consumer.manage.front.order;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.service.layer.orderinfo.UserInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:用户信息导入Controller
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年12月11日上午11:41:26 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
@RequestMapping("/userImport")
public class UserInfoImportController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(UserInfoImportController.class);
    @Resource
    private UserInfoService userInfoService;

    /**
     * 
     * Description:订单信息导入接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/import"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map assetXlsImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            logger.debug("---用户信息导入---开始上传xls文件...");
            Map<String, Object> result = userInfoService.userInfoXlsImport(file);
            logger.debug("---用户信息导入---上传xls文件结束...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("ZXBIT错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:分页查询用户数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/query"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map selectInstUserInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---用户信息查询开始...");
            PageInfo<UserBean> pageInfo = userInfoService.selectInstUserInfo(objs);
            logger.debug("---用户信息查询结束...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("ZXBIT错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
