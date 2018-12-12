package com.iqb.consumer.manage.front.user;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.user.UserRiskInfo;
import com.iqb.consumer.data.layer.bean.user.pojo.GetUserRiskInfoResponsePojo;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.riskinfo.RiskInfoService;
import com.iqb.consumer.service.layer.user.UserService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/user")
public class UserController extends BasicService {

    private static final int SERVICE_CODE_GET_USER_RISK_INFO = 1;

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SysUserSession sysUserSession;
    @Autowired
    private RiskInfoService riskInfoServiceImpl;

    /*
     * Description: 删除客户信息
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = {RequestMethod.POST, RequestMethod.GET})
    public Map delUserInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---删除客户{}信息...", objs);
            userService.deleteUserByID(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", "succ");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 修改客户信息
     */
    @ResponseBody
    @RequestMapping(value = "/mod", method = {RequestMethod.POST, RequestMethod.GET})
    public Map modUserInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---修改客户{}信息...", objs);
            int res = userService.updateUserInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            if (res == 0) {
                linkedHashMap.put("result", "succ");
            } else {
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取客户信息
     */
    @ResponseBody
    @RequestMapping(value = "/queryById", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getUserInfoById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            logger.info("IQB信息---获取客户信息...");
            String id = objs.getString("id");
            if (id == null || id.equals("")) {
                linkedHashMap.put("retCode", "fail");
            }
            UserRiskInfo lb = userService.getUserById(objs);
            logger.info("IQB信息---获取客户信息...");

            linkedHashMap.put("result", lb);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取客户信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getUserInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            PageInfo<UserRiskInfo> list = userService.getUserByMerNos(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-939 以租代购业务，人工风控节点，页面增加借款人客户基础信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月11日 上午11:48:30
     */
    @ResponseBody
    @RequestMapping(value = "/user_risk_info", method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_USER_RISK_INFO)
    public Object getUserRiskInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            GetUserRiskInfoResponsePojo result = riskInfoServiceImpl.getUserRiskInfoByRT(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_USER_RISK_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_USER_RISK_INFO);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_USER_CENTER;
    }
}
