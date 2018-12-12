package com.iqb.consumer.manage.front.pledge;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.pledge.IPledgeInquiryService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description: 质押车询价
 * 
 * @author guojuan
 */
@RestController
@RequestMapping("/pledge-inquiry")
public class PledgeInquiryController extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(PledgeInquiryController.class);

    @Autowired
    private IPledgeInquiryService pledgeInquiryService;

    /**
     * 通过订单号获取质押车订单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pledgeOrderInfo", method = RequestMethod.POST)
    public Object getPledgeOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) throws IqbException {
        try {
            logger.debug("IQB信息---获取质押车信息..." + objs.toJSONString());
            Object retObjs = this.pledgeInquiryService.getPledgeOrderInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", retObjs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Object getInfo(@RequestBody JSONObject objs, HttpServletRequest request) throws IqbException {
        try {
            logger.debug("IQB信息---获取质押车信息..." + objs.toJSONString());
            Object retObjs = this.pledgeInquiryService.getPledgeOrderInfoByOid(objs.getString("orderId"));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", retObjs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
