package com.iqb.consumer.service.layer.sublet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.constant.CommonConstant.RiskStatusConst;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.sign.OrderIdPojo;
import com.iqb.consumer.data.layer.bean.sublet.db.entity.InstSubletRecordEntity;
import com.iqb.consumer.data.layer.bean.sublet.pojo.ChatToGetRepayParamsResponseMessage;
import com.iqb.consumer.data.layer.bean.sublet.pojo.GetSubletRecordPojo;
import com.iqb.consumer.data.layer.bean.sublet.pojo.SubletInfoByOidPojo;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.sublet.SubletCenterManager;
import com.iqb.consumer.data.layer.dao.OrderDao;
import com.iqb.consumer.data.layer.dao.UserBeanDao;
import com.iqb.etep.common.utils.JSONUtil;

import jodd.util.StringUtil;

@Service
public class SubletCenterServiceImpl implements SubletCenterService {
    private static final Logger logger = LoggerFactory.getLogger(SubletCenterServiceImpl.class);
    @Autowired
    private SubletCenterManager subletCenterManager;
    @Resource
    private UrlConfig urlConfig;
    @Resource
    private OrderDao orderDao;
    @Resource
    private UserBeanDao userBeanDao;
    @Resource
    private EncryptUtils encryptUtils;

    @Override
    public SubletInfoByOidPojo getSubletInfoByOid(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        SubletInfoByOidPojo sibp = subletCenterManager.getSubletInfoByOid(orderId);
        if (sibp == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        if (!sibp.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
        Integer hashRepayNo = chatToGetRepayStartParams(orderId);
        sibp.setOverItems(hashRepayNo);
        return sibp;
    }

    @Override
    public boolean persistSubletInfo(JSONObject requestMessage) throws GenerallyException {
        InstSubletRecordEntity isre = JSONUtil.toJavaObject(
                requestMessage, InstSubletRecordEntity.class);
        if (!isre.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        subletCenterManager.persistSubletInfo(isre);
        return true;
    }

    @Override
    public GetSubletRecordPojo getSubletRecord(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        InstSubletRecordEntity isre = subletCenterManager.getSubletEntityByOid(orderId);
        if (isre == null) {
            return null; // 转租表无此条数据，默认为非转租订单。
        }
        GetSubletRecordPojo gsrp = subletCenterManager.getSubletRecord(orderId, isre.getSubletOrderId());
        if (gsrp == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        if (!gsrp.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
        return gsrp;
    }

    @Override
    public InstSubletRecordEntity getSubletEntity(JSONObject requestMessage) throws GenerallyException {
        OrderIdPojo oip =
                (OrderIdPojo) SignUtil.chatDecode(requestMessage, OrderIdPojo.class, urlConfig.getCommonPublicKey());
        String orderId = oip.getOrderId();
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        InstSubletRecordEntity isre = subletCenterManager.getSubletEntityByOid(orderId);
        if (isre == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        if (!isre.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
        return isre;
    }

    private Integer chatToGetRepayStartParams(String orderId) throws GenerallyException {
        String response;
        ChatToGetRepayParamsResponseMessage crp = null;
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("orderId", orderId);
            response = SimpleHttpUtils.httpPost(
                    urlConfig.getRepayParamsUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(request), urlConfig.getCommonPrivateKey()));
            logger.info("PaymentServiceImpl[chatToGetRepayStartParams] response:" + JSONObject.toJSONString(response));

            if (StringUtil.isEmpty(response)) {
                throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("chatToGetContractStatus error:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.B);
        }
        try {
            crp = JSONObject.parseObject(response, ChatToGetRepayParamsResponseMessage.class);
        } catch (Exception e) {
            logger.error("SubletCenterServiceImpl【chatToGetRepayStartParams】  :", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.C);
        }
        if (crp == null || crp.getResult() == null || crp.getResult().getHasRepayNo() == null) {
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.D);
        }
        return crp.getResult().getHasRepayNo();
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> request = new HashMap<String, String>();
        request.put("orderId", "SYQS2002170508001");
        System.err
                .println(SignUtil.chatEncode(
                        JSONObject.toJSONString(request),
                        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIWUmA1eYKeN5O0Ttha48z4PRiywDs0bZBtoLZOof1VM3KnZSM9F49XmYLLv3RYWNzU4GVxRe+FW8NONjJeQ8REKjCyfYkbmgFZxQvKCFA8HSSyiooUapNDBt5pdx6f++3Qf+plsQTJ5gNrz7WM+nHqWehE52fMd7xf4gNzqqADrAgMBAAECgYAk1Dw761pwt+F3L+kTSLjf0mxBo+Tgzf2CxO1T+a/pv8BTH/JAG3/emJ7uls146nUcOjRbzKeAEG1jlnI905ty9hpavj1LXlqW/E8f1soyu7vtbd7qjfztSGKk4HWemy5FbL03LEtShcxiAKYGaAg0EgI1RInHPjdgCqK1TsFlgQJBAMcj7IzuyacsaXMvJLWcjhkeYx0vFpkc+O//79YN/BaCQIPOuxg6kWbdlUuNOZJ4bDvPJVlBHSF+yXl3kCXrWMECQQCruJlDzCS3kk1g035f5cd8zOFFAqYorUVVLrrD4A0sc+ws35NUiwfqfLz6u7quIT/W9XRyB6yvy7M+faln6birAkAWRE4O9CRYLP8dggf4xqic5mjuunUsabDsJRIMPUQSwD22f0csTmAzwFMRP5lQZ2ayyVbDxCQduq+MhXH4y3gBAkBS+MUFXzQ2hrhgmpArYek7wfruz1LdwKsJd6TCVBXJbtGk9PpJFUxj3pWpSN/wlxwzjzIOjuq/nlsjTLB4BxQ1AkAR2y8psESZ/6gTgpCGphzO0WESqHl0Z1IqWWu01dhY1TcfK+WUiEv4PqCsFx+s0O4fSiJEU7l0YFHLcLV6B2V3"));
    }

    /**
     * 以租代购-转租 1:将转租订单用户迁移为新用户。如果包含管理费需要在月供中减掉月供费用 2:将新订单解绑。
     * 3:将账户系统转租账单切换为新用户(inst_info,inst_detail,inst_billinfo,acc_repay_history)
     * 4:有展期的，需要添加对应展期期数，有管理费用的，需要在月供中减掉对应的月供。
     * 
     * @param params
     * @throws GenerallyException
     */
    @Override
    public Map<String, Object> getSubletInfo(Map<String, Object> params) throws GenerallyException {
        logger.info("转租-BillBizServiceImpl-getSubletInfo--开始");
        String resultStr = "";
        String orderId = (String) params.get("orderId");
        String wfStatus = (String) params.get("wfStatus");
        // 1:根据orderId 调用中阁"查询转租记录"接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderId);
        GetSubletRecordPojo subletRecordPojo = getSubletRecord(jsonObject);
        OrderBean newOrderBean = orderDao.selByOrderId(orderId);
        if (subletRecordPojo != null) {
            String regId = newOrderBean.getRegId(); // 新用户手机号
            String subletOrderId = subletRecordPojo.getSubletOrderId(); // 转租人订单号
            int rollOverFlag = subletRecordPojo.getRollOverFlag(); // 是否展期
            String rollOverItems = subletRecordPojo.getRollOverItems(); // 展期期数
            BigDecimal manageFee = subletRecordPojo.getManageFee(); // 管理费用

            // 4:将新订单解绑
            unbundBillinfo(orderId, regId, subletOrderId, rollOverItems, manageFee, wfStatus);

            JSONObject param = new JSONObject();
            param.put("regId", regId);
            param.put("subletOrderId", subletOrderId);
            param.put("rollOverFlag", rollOverFlag);
            param.put("rollOverItems", Integer.parseInt(rollOverItems));
            param.put("manageFee", manageFee);

            resultStr = SimpleHttpUtils.httpPost(urlConfig.getSubletInfoUrl(),
                    encryptUtils.encrypt(param));

        }
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        logger.info("转租-BillBizServiceImpl-getSubletInfo--结束");
        return retMap;

    }

    /**
     * 
     * @param orderId 新订单号
     * @param regId 新用户手机号码
     * @param subletOrderId 老订单号
     * @param rollOverItems 展期期数
     * @param manageFee 管理费
     * @param wfStatus 流程状态 void Author haojinlong Create Date: 2017年5月16日
     */
    private void unbundBillinfo(String orderId, String regId, String subletOrderId, String rollOverItems,
            BigDecimal manageFee, String wfStatus) {
        logger.info("转租-新订单解除绑定开始--新订单号{}", orderId);

        UserBean userBean = userBeanDao.selectOneByRegId(regId);
        if (userBean != null) {
            OrderBean orderBean = new OrderBean();
            // 将新订单解绑
            orderBean.setOrderId(orderId);
            orderBean.setStatus(RiskStatusConst.RiskStatus_2);
            orderDao.updateOrderInfo(orderBean);
            // 更新老订单信息 手机号码 订单期数
            OrderBean oldOrderBean = orderDao.selByOrderId(subletOrderId);
            logger.info("转租-更新老订单信息 手机号码 订单期数--订单号{} 用户id{} 用户手机号码{}", orderId, userBean.getId(), userBean.getRegId());
            orderBean = new OrderBean();
            orderBean.setOrderId(subletOrderId);
            orderBean.setUserId(String.valueOf(userBean.getId()));
            orderBean.setRegId(userBean.getRegId());
            /**
             * FINANCE-1050 转租业务资产分配期数不应包括展期期数 2017-05-26 haojinling update
             */
            /*
             * int sumItem = 0; if (StringUtils.isNotEmpty(rollOverItems)) { sumItem =
             * Integer.parseInt(oldOrderBean.getOrderItems()) + Integer.parseInt(rollOverItems);
             * orderBean.setOrderItems(String.valueOf(sumItem)); }
             */
            if (oldOrderBean.getMonthInterest() != null) {
                orderBean.setMonthInterest(oldOrderBean.getMonthInterest().subtract(manageFee));
            }
            orderBean.setWfStatus(Integer.parseInt(wfStatus));
            orderDao.updateOrderInfo(orderBean);
        } else {
            logger.info("转租-新订单解除绑定结束");
        }

        logger.info("转租-新订单解除绑定结束");
    }
}
