/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.contract;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.contract.ContractService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Controller
@RequestMapping("/unIntcpt-contract4shop")
public class Contract4ShopController extends BaseService {

    protected static final Logger logger = LoggerFactory
            .getLogger(Contract4ShopController.class);

    public final static String FES_RET_SUCC = "000000"; // 处理成功
    public final static String FES_RET_FAIL = "000001"; // 处理失败

    @Resource
    private ContractService contractService;

    /**
     * 自动签约完成回调
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/call_back", method = {RequestMethod.POST,
            RequestMethod.GET})
    public Map shopCallBack(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        String status = String.valueOf(objs.get("status"));
        if ("0".equals(status)) {
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
        try {
            logger.debug("IQB信息---【控制层】门店签约回调处理，开始...");
            int result = contractService.shopCallBack(objs);
            logger.debug("IQB信息---【控制层】门店签约回调处理，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap = getResult(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 合同签署完成后回调地址
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/contractReturn", method = RequestMethod.POST)
    public Map contractReturn(@RequestBody JSONObject objs, HttpServletRequest request) {
        String status = String.valueOf(objs.get("status"));
        if ("0".equals(status)) {
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
        try {
            logger.debug("IQB信息---合同签署完成，开始...{}", JSONObject.toJSONString(objs));
            int result = contractService.contractReturn(objs);
            logger.debug("IQB信息---合同签署完成，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap = getResult(result);
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
            case 2:
                linkedHashMap.put("result", "2");
                linkedHashMap.put("msg", "合同签署已完成");
                break;
            case 1:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "门店签约回调处理成功");
                break;
            case 0:
                linkedHashMap.put("result", FES_RET_FAIL);
                linkedHashMap.put("msg", "门店签约回调处理失败");
                break;
            default:
                break;
        }
        return linkedHashMap;
    }
}
