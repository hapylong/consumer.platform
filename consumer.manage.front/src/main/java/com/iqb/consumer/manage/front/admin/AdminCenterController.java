package com.iqb.consumer.manage.front.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.RandomUtils;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.request.AuthoritySeekResponseMessage;
import com.iqb.consumer.data.layer.bean.contract.InstOrderContractEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.consumer.service.layer.dict.DictService;

import jodd.util.StringUtil;

/**
 * 
 * Description: 后台服务中，有一些通用功能，写在此模块中
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月19日    adam       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/admin")
public class AdminCenterController extends BasicService {

    protected static final Logger logger = LoggerFactory.getLogger(AdminCenterController.class);
    private static final int SERVICE_CODE_CONTRACT_GET_IOCE_BY_OID = 1;
    private static final int SERVICE_CODE_CONTRACT_SAVE_OR_UPDATE_IOCE = 2;
    private static final int SERVICE_CODE_SECURITY_GENERATE_KEY = 3;
    private static final int SERVICE_CODE_SECURITY_CGET_KEY = 4;
    private static final int SERVICE_CODE_SECURITY_UPDATE_KEY = 5;
    private static final int SERVICE_CODE_SECURITY_UPDATE_IP_LIST_BY_ID = 6;
    private static final int SERVICE_CODE_CHANGE_PLAN = 7;
    private static final int SERVICE_CODE_CHANGE_ORG_CODE = 8;
    private static final int SERVICE_CODE_MERCHANT_AUTHORITY_CHANGE = 9;
    private static final int SERVICE_CODE_MERCHANT_AUTHORITY_SEEK = 10;
    private static final int SERVICE_CODE_MERCHANT_AUTHORITY_TREE1 = 11;
    private static final int SERVICE_CODE_MERCHANT_AUTHORITY_TREE3 = 12;
    private static final int SERVICE_CODE_MERCHANT_AUTHORITY_QUERY_BY_ORG_CODE = 13;
    private static final int SERVICE_CODE_GET_2D_CAPTCHA = 14;
    private static final int SERVICE_CODE_ANALYSIS_IN = 15;

    @Autowired
    private AdminService adminServiceImpl;
    @Autowired
    private DictService dictServiceImpl;

    @Resource
    private AssetApiService assetApiService;

    /**
     * 
     * Description: ioce inst_ordercontract 表 ，对应 InstOrderContractEntity Description: FINANCE-1016
     * 项目信息维护节点项目信息页面增加录入合同编号/合同签订日期，合同编号系统可自动识别重复编号，如果重复设置提醒
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午2:03:21
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/contract/get_ioce_by_oid"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CONTRACT_GET_IOCE_BY_OID)
    public Object getIOCEByOid(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            String orderId = requestMessage.getString(KEY_ORDER_ID);
            if (StringUtil.isEmpty(orderId)) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            InstOrderContractEntity result = adminServiceImpl
                    .getIOCEByOid(orderId);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CONTRACT_GET_IOCE_BY_OID);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CONTRACT_GET_IOCE_BY_OID);
        }
    }

    /**
     * 
     * Description: ioce inst_ordercontract 表 ，对应 InstOrderContractEntity Description: FINANCE-1016
     * 项目信息维护节点项目信息页面增加录入合同编号/合同签订日期，合同编号系统可自动识别重复编号，如果重复设置提醒
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午2:03:21
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/contract/save_or_update_ioce"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CONTRACT_SAVE_OR_UPDATE_IOCE)
    public Object saveOrUpdateIOCE(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            InstOrderContractEntity ioce = JSONObject.toJavaObject(requestMessage, InstOrderContractEntity.class);
            if (ioce == null || !ioce.checkEntity()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            String result = adminServiceImpl
                    .saveOrUpdateIOCE(ioce);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if ("success".equals(result)) {
                linkedHashMap.put(KEY_RESULT, result);
                return returnSuccessInfo(linkedHashMap);
            } else {
                return generateFailResponseMessage(new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER,
                        Location.A), SERVICE_CODE_CONTRACT_SAVE_OR_UPDATE_IOCE, result);
            }
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CONTRACT_SAVE_OR_UPDATE_IOCE);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CONTRACT_SAVE_OR_UPDATE_IOCE);
        }
    }

    /**
     * 
     * Description:FINANCE-1058 密钥生成
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月24日 上午11:01:53
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/generate_key"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_SECURITY_GENERATE_KEY)
    public Object generateKey(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            String result = adminServiceImpl.generateKey(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_GENERATE_KEY);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_GENERATE_KEY);
        }
    }

    /**
     * 
     * Description: FINANCE-1058 密钥查询 cget means conditionsGet
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月24日 上午11:15:29
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/cget_key"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_SECURITY_CGET_KEY)
    public Object getKey(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            Object result = adminServiceImpl.getKeyPairsByCondition(requestMessage);
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_CGET_KEY);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_CGET_KEY);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/update_key"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_SECURITY_UPDATE_KEY)
    public Object updateKey(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            String result = adminServiceImpl.updateKey(requestMessage);
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_UPDATE_KEY);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_UPDATE_KEY);
        }
    }

    /**
     * 
     * Description: FINANCE-1250 白名单功能 FINANCE-1251 白名单IP维护
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月13日 下午3:53:42
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/update_ips_by_id"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_SECURITY_UPDATE_IP_LIST_BY_ID)
    public Object updateIPsById(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            adminServiceImpl.updateIpsById(requestMessage);
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_UPDATE_KEY);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SECURITY_UPDATE_KEY);
        }
    }

    /**
     * 
     * Description: FINANCE-1126 重置分期和停止分期页面
     * 
     * @param {"target":"STOP"/"RESET","orderId":"","describe":"", "beginDate":"重置分期必填" }
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月1日 下午3:54:23
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/change_plan"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CHANGE_PLAN)
    public Object changePlan(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            String result = adminServiceImpl.changePlan(requestMessage);
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHANGE_PLAN);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHANGE_PLAN);
        }
    }

    /**
     * 
     * Description: Compare And Swap Organization Code
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年9月1日 下午2:02:33
     */
    @ResponseBody
    @RequestMapping(value = {"/CAS_orgcode"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CHANGE_ORG_CODE)
    public Object changeOrgCode(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.casOrgCode(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHANGE_ORG_CODE);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_CHANGE_ORG_CODE, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHANGE_ORG_CODE);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/authority/seek"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_MERCHANT_AUTHORITY_SEEK)
    public Object authoritySeek(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            AuthoritySeekResponseMessage asrm = adminServiceImpl.authorityHandlerSeek(requestMessage);
            return returnSuccessInfo(getSuccResponse(asrm));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_SEEK);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_MERCHANT_AUTHORITY_SEEK, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_SEEK);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/authority/change"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_MERCHANT_AUTHORITY_CHANGE)
    public Object authorityChange(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.authorityHandlerChange(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_CHANGE);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_MERCHANT_AUTHORITY_CHANGE, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_CHANGE);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/authority/tree1"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_MERCHANT_AUTHORITY_TREE1)
    public Object merchantTree1() {
        try {
            return returnSuccessInfo(getSuccResponse(adminServiceImpl.authorityHandlerTree1()));
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_TREE1);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/authority/tree3"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_MERCHANT_AUTHORITY_TREE3)
    public Object merchantTree3(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            IqbCustomerPermissionEntity icpe = adminServiceImpl.authorityHandlerQuery(requestMessage);
            return returnSuccessInfo(getSuccResponse(icpe == null ? "" : icpe.getAuthorityTreeList()));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_TREE3);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_MERCHANT_AUTHORITY_TREE3, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_TREE3);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/authority/byOrgCode"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_MERCHANT_AUTHORITY_QUERY_BY_ORG_CODE)
    public Object authorityQueryByOrgCode(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.getMerchantListByOrgCode(requestMessage);
            return returnSuccessInfo(getSuccResponse(requestMessage.get("merList")));
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_MERCHANT_AUTHORITY_QUERY_BY_ORG_CODE, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_MERCHANT_AUTHORITY_QUERY_BY_ORG_CODE);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/image/check2Dcaptcha"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ServiceCode(SERVICE_CODE_GET_2D_CAPTCHA)
    public void check2DCaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            System.out.println(session.getAttribute("validation_code"));
        } catch (Throwable e) {
            response.setStatus(500);
        }
    }

    /**
     * 
     * Description: FINANCE-2180 以租代购业务逾期指标内部统计
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年10月20日 下午1:45:37
     */
    @ResponseBody
    @RequestMapping(value = {"/analysis/{isInside}"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ServiceCode(SERVICE_CODE_ANALYSIS_IN)
    public Object analysisIn(@PathVariable Boolean isInside, @RequestBody JSONObject requestMessage) {
        try {
            if (requestMessage.getDate("schedueDate") == null) {
                requestMessage.put("schedueDate", new Date());
            }
            adminServiceImpl.merchantNos(requestMessage);
            return returnSuccessInfo(getSuccResponse(adminServiceImpl.analysisData(requestMessage, isInside, true)));
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_ANALYSIS_IN, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ANALYSIS_IN);
        }
    }

    /**
     * 
     * Description: FINANCE-2488 轮动风控对内逾期剩余本金报表
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2017年12月5日 16:51:44
     */
    @ResponseBody
    @RequestMapping(value = {"/riskManagement/{type}"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ServiceCode(SERVICE_CODE_ANALYSIS_IN)
    public Object riskManagement(@PathVariable String type, @RequestBody JSONObject requestMessage) {
        try {
            if (requestMessage.getDate("schedueDate") == null || "".equals(requestMessage.getDate("schedueDate"))) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(new Date());
                requestMessage.put("schedueDate", format);
            }// 入参加入时间参数
            adminServiceImpl.merchantNos(requestMessage);
            return returnSuccessInfo(adminServiceImpl.analysisDataNew(requestMessage, type, true));

        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_ANALYSIS_IN, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ANALYSIS_IN);
        }
    }

    /****
     * 
     * Description: 导出XLSX，true 内部， false 外部
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年10月19日 下午3:40:01
     */
    @ResponseBody
    @RequestMapping(value = {"/analysisXlsx/{isInside}"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ServiceCode(SERVICE_CODE_ANALYSIS_IN)
    public void analysisIn(@PathVariable Boolean isInside, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject requestMessage = new JSONObject();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                requestMessage.put(paraName, new String(para.trim()));
            }
            if (requestMessage.getDate("schedueDate") == null) {
                requestMessage.put("schedueDate", new Date());
            }
            adminServiceImpl.merchantNos(requestMessage);
            adminServiceImpl.analysisXlsx(requestMessage, isInside, response);
        } catch (DevDefineErrorMsgException e) {
            logger.error("analysisXlsx :", e);
        } catch (Throwable e) {
            logger.error("analysisXlsx :", e);
        }
    }

    /****
     * 
     * Description: 导出XLSX，isInside 内部， noInside 外部 ,int 个数
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2017年12月7日 19:42:00
     */

    @ResponseBody
    @RequestMapping(value = {"/riskManagementXlsx/{type}"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ServiceCode(SERVICE_CODE_ANALYSIS_IN)
    public void analysisIn(@PathVariable String type, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            request.setCharacterEncoding("UTF-8");
            String merchNames = request.getParameter("merchNames");
            // String merchName = new String(merchNames.trim().getBytes("ISO-8859-1"), "UTF-8");

            JSONObject objs = new JSONObject();
            objs.put("merchNames", merchNames);
            if (request.getParameter("schedueDate") == null || "".equals(request.getParameter("schedueDate"))) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(new Date());
                objs.put("schedueDate", format);
            } else {
                objs.put("schedueDate", request.getParameter("schedueDate"));
            }
            adminServiceImpl.merchantNos(objs);
            adminServiceImpl.riskManagementXlsx(objs, type, response);
        } catch (DevDefineErrorMsgException e) {
            logger.error("analysisXlsx :", e);
        } catch (Throwable e) {
            logger.error("analysisXlsx :", e);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_ADMIN_CENTER;
    }

}
