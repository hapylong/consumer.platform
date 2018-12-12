/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:21:08
 * @version V1.0
 */
package com.iqb.consumer.service.layer.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.data.layer.bean.contract.ContractListBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.contract.ContractInfoBiz;
import com.iqb.consumer.data.layer.biz.contract.ContractListBiz;
import com.iqb.consumer.service.layer.base.BaseService;

/**
 * @author guojuan
 */
@Service("contractService")
public class ContractServiceImpl extends BaseService implements ContractService {

    protected static Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Resource
    private OrderBiz orderBiz;
    @Resource
    private ContractListBiz contractListBiz;

    @Resource
    private ContractInfoBiz contractInfoBiz;

    @SuppressWarnings("unchecked")
    @Override
    public int shopCallBack(JSONObject objs) {
        logger.debug("IQB信息---【服务层】门店签约回调处理，开始...");
        // 1.初始化
        int result = 1;
        JSONObject objsNew = new JSONObject();
        String orderId = objs.getString("bizId");

        // 2.更新订单状态
        objsNew.put("orderId", orderId);
        OrderBean orderBean = orderBiz.selectOne(objsNew);
        if (2 == orderBean.getContractStatus()) {
            return 2;
        }
        orderBean.setContractStatus(1);
        result = orderBiz.updateOrderInfo(orderBean);

        // 更新合同补录信息状态
        ContractInfoBean bean = contractInfoBiz.selContractInfo(orderId);
        bean.setStatus("waiting");
        contractInfoBiz.updataStatus(bean);
        // 3.更新合同列表
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list = (List<Map<String, String>>) objs.get("ecList");
        if (list.size() == 0) {
            logger.error("IQB信息---【服务层】门店签约回调处理,返回ContractListBean出错...");
            result = 0;
            return result;
        }
        logger.debug("IQB信息---【服务层】门店签约回调处理，结束...");
        return result;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public int contractReturn(JSONObject objs) {
        logger.debug("IQB信息---合同签约完成回调处理，开始...");
        // 1.初始化
        int result = 1;
        JSONObject objsNew = new JSONObject();
        String orderId = objs.getString("bizId");

        // 2.更新订单状态
        objsNew.put("orderId", orderId);
        OrderBean orderBean = orderBiz.selectOne(objsNew);
        orderBean.setContractStatus(2);
        result = orderBiz.updateOrderInfo(orderBean);

        // 更新合同补录信息状态
        ContractInfoBean bean = contractInfoBiz.selContractInfo(orderId);
        bean.setStatus("complete");
        contractInfoBiz.updataStatus(bean);
        List<Map<String, String>> list = (List<Map<String, String>>) objs.get("ecList");
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            ContractListBean contractListBean = new ContractListBean();
            contractListBean.setOrderId(orderId);
            contractListBean.setEcId(map.get("ecCode"));
            contractListBean.setContractUrl(map.get("ecViewUrl"));
            contractListBean.setUpdateTime(new Date());
        }
        logger.debug("IQB信息---合同签约完成回调处理，结束...");
        return result;
    }

}
