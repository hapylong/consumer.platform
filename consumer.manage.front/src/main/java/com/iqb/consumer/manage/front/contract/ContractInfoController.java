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
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.service.layer.contract.ContractInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author guojuan
 */
@Controller
@RequestMapping("/contract")
public class ContractInfoController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(ContractInfoController.class);

    public final static String FES_RET_SUCC = "000000"; // 处理成功
    public final static String FES_INSERT_FAIL = "000001"; // 插入数据错误
    public final static String FES_TRAN_FAIL = "000002"; // 数据转换错误

    @Resource
    private ContractInfoService contractInfoService;

    /*
     * Description: 插入合同补录信息
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/insertContractInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Map insertContractInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---【控制层】插入合同补录信息，开始...");
            int result = contractInfoService.insertContractInfo(objs);
            logger.debug("IQB信息---【控制层】插入合同补录信息，结束...");

            LinkedHashMap<String, Object> linkedHashMap = null;
            linkedHashMap = getResult(result);

            return super.returnSuccessInfo(linkedHashMap);

        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/justMerchantType", method = {RequestMethod.POST, RequestMethod.GET})
    public Object justMerchantType(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单:{}查询订单所属商户是轮动还是医美", objs);
            Object object = contractInfoService.justMerchantType(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", object);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/selContractInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object selContractInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单:{}查询订单所属合同信息", objs);
            ContractInfoBean contractInfoBean = contractInfoService.selContractInfo(objs.getString("orderId"));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("contractInfo", contractInfoBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * 处理结果集
     */
    private LinkedHashMap<String, Object> getResult(int result) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        switch (result) {
            case 1:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "合同补录信息成功");
                break;
            case 2:
                linkedHashMap.put("result", FES_TRAN_FAIL);
                linkedHashMap.put("msg", "数据格式转换错误");
                break;
            default:
                linkedHashMap.put("result", FES_INSERT_FAIL);
                linkedHashMap.put("msg", "合同补录信息失败");
                break;
        }
        return linkedHashMap;
    }

    /**
     * 生成电子合同还款明细信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月19日
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-createRepaymentDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Object createRepaymentDetail(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单:{}查询订单所属合同信息", objs);
            Map<String, String> returnMap =
                    contractInfoService.createRepaymentDetail(objs.getString("orderId"));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("contractInfo", returnMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
