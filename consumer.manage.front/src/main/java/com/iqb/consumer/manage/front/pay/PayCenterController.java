package com.iqb.consumer.manage.front.pay;

import java.util.HashMap;
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
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.biz.pay.PayMiddleBiz;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.pay.PayService;

@Controller
@RequestMapping("/iqianpay")
public class PayCenterController extends BasicService {

    protected static final Logger logger = LoggerFactory.getLogger(PayCenterController.class);
    private static final int SERVICE_CODE_SETTLEMENT_CENTER_BUCKLE = 1;
    private static final int SERVICE_CODE_SETTLEMENT_CENTER_CALLBACK = 2;

    @Autowired
    private PayService payServiceImpl;

    @Autowired
    private PayMiddleBiz payMiddleBiz;

    /**
     * 判断是否存在支付记录，如果存在则不允许用户支付
     * 
     * @author Yeoman
     * @param obj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/justPayMiddle"}, method = RequestMethod.POST)
    public Object justPayMiddle(@RequestBody JSONObject obj) {
        logger.info("--判断是否存在中间记录--", obj);
        LinkedHashMap<String, Object> returnObj = null;
        try {
            int midCount = payMiddleBiz.getPayMidCount(obj.getString("orderId"));
            returnObj = new LinkedHashMap<String, Object>();
            if (midCount > 0) {
                returnObj.put(KEY_RESULT, 1);
                return returnSuccessInfo(returnObj);
            }
            returnObj.put(KEY_RESULT, 0);
            return returnSuccessInfo(returnObj);
        } catch (Exception e) {
            logger.error("判断是否存在支付中间记录", e);
            returnObj.put(KEY_RESULT, 0);
            return returnSuccessInfo(returnObj);
        }
    }

    /**
     * 
     * Description: FINANCE-1301 (F)逾期代扣FINANCE-1322 平账 && 结算中心 代扣 回调 【有测试类】
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月21日 下午3:42:08
     */
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-callback"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_SETTLEMENT_CENTER_CALLBACK)
    public Object callback(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            logger.info("结算回调的数据:{}", requestMessage);
            payServiceImpl.callback(requestMessage);
            response.put("succ", "1");
        } catch (GenerallyException e) {
            String errCode = generateCode(SERVICE_CODE_SETTLEMENT_CENTER_CALLBACK, e.getReason());
            response.put("succ", errCode);
        } catch (Throwable e) {
            String errCode = generateCode(SERVICE_CODE_SETTLEMENT_CENTER_CALLBACK, Reason.UNKNOWN_ERROR);
            response.put("succ", errCode);
        }
        return response;
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_PAY_CENTER;
    }

}
