/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description:
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.contract;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
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
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.service.layer.contract.ContractInfoService;
import com.iqb.consumer.service.layer.contract.MakeContractService;
import com.iqb.eatep.ec.bizconfig.service.BizConfigService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * @author guojuan
 */
@Controller
@RequestMapping("/contract")
public class ContractController extends BaseService {

    protected static final Logger logger = LoggerFactory
            .getLogger(ContractController.class);

    public final static String FES_RET_SUCC = "000000"; // 处理成功
    public final static String FES_RET_FAIL = "000001"; // 处理失败
    public final static String FES_TRAN_FAIL = "000002"; // 数据转换错误
    public final static String FES_POST_FAIL = "000003"; // 请求接口错误

    @Resource
    private MakeContractService makeContractService;

    @Resource
    private ParamConfig paramConfig;

    @Autowired
    private SysUserSession sysUserSession;

    @Autowired
    private BizConfigService bizConfigServiceImpl;

    @Autowired
    private ContractInfoService contractInfoService;

    /**
     * +- 提交合同
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/submitContract", method = RequestMethod.POST)
    public Map submitContract(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---【控制层】提交合同信息，开始...");
            int result = makeContractService.submitContract(objs);
            logger.debug("IQB信息---【控制层】提交合同信息，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap = getResult(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 判断是否签约
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-judgeSign", method = RequestMethod.POST)
    public Map judgeSignContract(@RequestBody JSONObject objs, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        try {
            linkedHashMap.put("result", bizConfigServiceImpl.judgeSignContract(objs));

            logger.debug("IQB信息---【控制层】提交合同信息，结束...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 查询电子合同列表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-selContracts", method = RequestMethod.POST)
    public Object selContractInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单:{}查询订单所属合同信息", objs);
            Object object = makeContractService.selContractList(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", object);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 判断是否签约
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public Map orderContractInit(@RequestBody JSONObject objs, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        try {
            logger.debug("初始化需要生成合同的订单列表" + objs);
            String orgCode = sysUserSession.getOrgCode();
            if (orgCode == null || orgCode.equals("")) {
                return null;
            } else {
                objs.put("id", orgCode);
            }
            linkedHashMap.put("result", makeContractService.orderContractInit(objs));

            logger.debug("IQB信息---【控制层】提交合同信息，结束...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 获取所有已经正在签约的合同列表
     * 
     * @param objs
     * @param request
     * @return 设定文件 Map 返回类型
     * @throws
     * @author guojuan 2017年10月11日下午5:36:06
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/contractFinish", method = RequestMethod.POST)
    public Map contractFinish(@RequestBody JSONObject objs, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        try {
            logger.debug("初始化需要生成合同的订单列表" + objs);
            linkedHashMap.put("result", makeContractService.orderContractFinish(objs));

            logger.debug("IQB信息---【控制层】提交合同信息，结束...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 电子合同批量下载
     * 
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param objs
     * @param request
     * @return 设定文件 Map 返回类型
     * @throws
     * @author guojuan 2017年10月13日下午6:48:36
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/downContract", method = RequestMethod.GET)
    public Map downContract(@RequestBody JSONObject objs, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        try {
            logger.debug("初始化需要生成合同的订单列表" + objs);
            linkedHashMap.put("result", makeContractService.orderContractFinish(objs));

            logger.debug("IQB信息---【控制层】提交合同信息，结束...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 生成电子合同
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/makeContract", method = RequestMethod.POST)
    public Map makeContract(@RequestBody JSONObject objs, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        String orderId = String.valueOf(objs.get("orderId"));
        ContractInfoBean bean = contractInfoService.selContractInfo(orderId);
        if ("process".equals(bean.getStatus()) || "waiting".equals(bean.getStatus())
                || "complete".equals(bean.getStatus())) {
            linkedHashMap.put("result", FES_RET_FAIL);
            linkedHashMap.put("msg", "合同已经提交签约，请不要反复签约");
            return super.returnSuccessInfo(linkedHashMap);
        }
        try {
            logger.debug("IQB信息---【控制层】生成合同信息，开始...");
            Map<String, Object> resultMap = new HashMap<String, Object>();
            objs.put("iqb_contract_notify_contract_url",
                    paramConfig.getIqb_contract_notify_contract_url()); // 生成合同回调接口
            objs.put("iqb_contract_task_contract_url",
                    paramConfig.getIqb_contract_task_contract_url()); // 合同签署完毕回调接口
            objs.put("iqb_contract_sign_notify_contract_url_2",
                    paramConfig.getIqb_contract_sign_notify_contract_url_2()); // 实际借款人签名后，微信跳转界面帮帮手
            objs.put("iqb_contract_sign_notify_contract_url_7",
                    paramConfig.getIqb_contract_sign_notify_contract_url_7()); // 实际借款人签名后，微信跳转界面轮动
            objs.put("iqb_contract_sign_notify_contract_url_4",
                    paramConfig.getIqb_contract_sign_notify_contract_url_4()); // 实际借款人签名后，微信跳转界面先有车
            objs.put("iqb_contract_sign_notify_contract_url_10",
                    paramConfig.getIqb_contract_sign_notify_contract_url_10()); // 实际借款人签名后，微信跳转界面客户家
            resultMap = makeContractService.makeContract(objs);
            logger.debug("IQB信息---【控制层】生成合同信息，结束...");

            int result = Integer.parseInt(resultMap.get("result").toString());
            linkedHashMap = getResult(result);
            if (result == 0) {
                linkedHashMap.put("returnResult",
                        resultMap.get("returnResult"));
            } else {
                linkedHashMap.put("returnResult", null);
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * 处理结果集
     */
    private LinkedHashMap<String, Object> getResult(int result) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        switch (result) {
            case 0:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "生成合同成功");
                break;
            case 1:
                linkedHashMap.put("result", FES_RET_FAIL);
                linkedHashMap.put("msg", "生成合同失败");
                break;
            case 2:
                linkedHashMap.put("result", FES_TRAN_FAIL);
                linkedHashMap.put("msg", "数据转换错误");
                break;
            case 3:
                linkedHashMap.put("result", FES_POST_FAIL);
                linkedHashMap.put("msg", "请求生成合同接口错误");
                break;
            case 10:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "提交合同成功");
                break;
            case 11:
                linkedHashMap.put("result", FES_RET_FAIL);
                linkedHashMap.put("msg", "提交合同失败");
                break;
            case 12:
                linkedHashMap.put("result", FES_TRAN_FAIL);
                linkedHashMap.put("msg", "数据转换错误");
                break;
            case 13:
                linkedHashMap.put("result", FES_POST_FAIL);
                linkedHashMap.put("msg", "请求提交合同接口错误");
                break;
            default:
                linkedHashMap.put("result", FES_POST_FAIL);
                linkedHashMap.put("msg", "请求提交合同接口错误");
                break;
        }
        return linkedHashMap;
    }
}
