/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.iqb.consumer.data.layer.bean.merchant.MerchantBizTypeBean;
import com.iqb.consumer.service.layer.merchant.service.MerchantBizTypeService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Controller
@RequestMapping("/merchantBizType")
public class MerchantBizTypeController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(MerchantBizTypeController.class);

    public final static String FES_RET_SUCC = "000000";

    @Resource
    private MerchantBizTypeService merchantBizTypeService;

    /*
     * Description: 获取首付款列表
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getMerchantBizTypeList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getDownPaymentList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---获取商户业务类型列表信息开始...");
            List<MerchantBizTypeBean> list = merchantBizTypeService.getMerchantBizTypeList(objs);
            List<Map> newList = new ArrayList<Map>();
            for (MerchantBizTypeBean mbtb : list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("bizType", mbtb.getBizType());
                map.put("name", mbtb.getBizName());
                newList.add(map);
            }
            logger.debug("IQB信息---获取商户业务类型列表结果转换结束...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", newList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    private String getBizName(MerchantBizTypeBean mbtb) {
        if ("2001".equals(mbtb.getBizType())) {
            return "以租代售新车";
        } else if ("2002".equals(mbtb.getBizType())) {
            return "以租代售二手车";
        } else if ("2100".equals(mbtb.getBizType())) {
            return "抵押车";
        } else if ("2200".equals(mbtb.getBizType())) {
            return "质押车";
        } else if ("1100".equals(mbtb.getBizType())) {
            return "易安家";
        } else if ("1000".equals(mbtb.getBizType())) {
            return "医美";
        } else if ("1200".equals(mbtb.getBizType())) {
            return "旅游";
        } else if ("2300".equals(mbtb.getBizType())) {
            return "车秒贷";
        } else {
            return "无对应业务类型";
        }
    }
}
