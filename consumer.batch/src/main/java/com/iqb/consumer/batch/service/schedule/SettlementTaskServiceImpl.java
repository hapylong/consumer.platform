/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月23日下午6:44:56 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.service.schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.biz.InstRemindPhoneBiz;
import com.iqb.consumer.batch.biz.InstSettleConfigBiz;
import com.iqb.consumer.batch.biz.OrderBiz;
import com.iqb.consumer.batch.biz.SettlementResultBiz;
import com.iqb.consumer.batch.config.ConsumerConfig;
import com.iqb.consumer.batch.data.pojo.InstRemindPhoneBean;
import com.iqb.consumer.batch.data.pojo.InstSettleConfigBean;
import com.iqb.consumer.batch.data.pojo.InstallmentBillPojo;
import com.iqb.consumer.batch.data.pojo.ManageCarInfoBean;
import com.iqb.consumer.batch.data.pojo.OrderBean;
import com.iqb.consumer.batch.data.pojo.SettlementResultBean;
import com.iqb.consumer.batch.data.pojo.UserPaymentInfoBean;
import com.iqb.consumer.batch.data.pojo.UserPaymentInfoDto;
import com.iqb.consumer.batch.util.HttpsClientUtil;
import com.iqb.consumer.batch.util.encript.EncryptUtils;
import com.iqb.etep.common.utils.DateUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;

/**
 * @author haojinlong
 */
@Service
public class SettlementTaskServiceImpl implements SettlementTaskService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(SettlementTaskServiceImpl.class);

    @Autowired
    private EncryptUtils encryptUtils;
    @Autowired
    private ConsumerConfig consumerConfig;
    @Autowired
    private SettlementResultBiz settlementResultBiz;
    @Autowired
    private InstSettleConfigBiz InstSettleConfigBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private InstRemindPhoneBiz instRemindPhoneBiz;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月23日
     */
    @SuppressWarnings("unchecked")
    @Override
    public long insertSettlementResult() {
        JSONObject objs = new JSONObject();
        List<Map<String, String>> paraList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        List<InstSettleConfigBean> settlelist = InstSettleConfigBiz.selectInstSettleConfigResultByParams(params);
        logger.debug("---获取账单自动代扣条件---{}", JSON.toJSONString(settlelist));
        if (!CollectionUtils.isEmpty(settlelist)) {
            Map<String, String> beanMap = null;
            for (InstSettleConfigBean bean : settlelist) {
                beanMap = new HashMap<>();
                beanMap.put("merchantNo", bean.getMerchantNo());
                beanMap.put("openId", bean.getBizType());
                beanMap.put("startDate", bean.getStartDate());
                beanMap.put("orderDate", bean.getOrderDate());
                beanMap.put("flag", bean.getFlag());
                paraList.add(beanMap);
            }
        }

        long result = 0;
        if (!CollectionUtils.isEmpty(paraList)) {
            logger.debug("---账单自动代扣数据---{}", JSONObject.toJSON(paraList));
            for (Map<String, String> map : paraList) {
                ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
                // 迭代取出所有未还账单
                int pageNum = 1;
                int numPerPage = 500;

                objs.put("numPerPage", numPerPage);
                objs.put("pageNum", pageNum);
                objs.put("startDate", map.get("startDate"));
                objs.put("orderDate", map.get("orderDate"));
                objs.put("merchantNo", map.get("merchantNo"));
                objs.put("openId", map.get("openId"));
                objs.put("flag", map.get("flag"));
                Map<String, Object> retMap = selectEveryDayExpireBill(objs);
                if (retMap != null) {
                    Map<String, Object> res = (Map<String, Object>) retMap.get("result");
                    if (retMap.get("retCode").equals("success")) {
                        while (pageNum <= Integer.parseInt(String.valueOf(res.get("endPageIndex")))) {
                            List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
                            List<SettlementResultBean> list = converList2ListBean(recordList);
                            logger.info("---账单数据--{}", JSONObject.toJSONString(list));
                            lock.writeLock().lock();
                            pageNum++;
                            if (list != null && list.size() > 0) {
                                result = settlementResultBiz.insertSettlementResult(list);
                                lock.writeLock().unlock();
                                objs.put("numPerPage", numPerPage);
                                objs.put("pageNum", pageNum);
                                retMap = selectEveryDayExpireBill(objs);
                                res = (Map<String, Object>) retMap.get("result");
                            }
                        }
                    }
                } else {
                    logger.debug("---调用账务系统接口--获取每日还款账单出现异常--");
                }
            }
        }
        return result;
    }

    // 发送账户系统查询应收的明细信息
    private Map<String, Object> selectEveryDayExpireBill(JSONObject objs) {
        logger.info("------调用账务系统接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getEveryDayExpireBill(),
                encryptUtils.encrypt(objs));
        logger.info("---调用账务系统接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 根据订单号 期数批量查询账单信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月21日
     */
    private Map<String, Object> selectQueryBillInfoByOrderIdPage(JSONObject objs) {
        logger.debug("---根据订单号 期数列表--调用账务系统接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getQueryBillInfoByOrderIdPage(),
                encryptUtils.encrypt(objs));
        logger.debug("---根据订单号 期数列表--调用账务系统接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 
     * Description:转化List<Map<String, Object>>为List<SettlementResultBean>
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private List<SettlementResultBean> converList2ListBean(List<Map<String, Object>> recordList) {
        List<SettlementResultBean> list = new ArrayList<>();
        if (recordList != null) {
            for (Map<String, Object> objs : recordList) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", String.valueOf(objs.get("orderId")));
                params.put("repayNo", String.valueOf(objs.get("repayNo")));
                params.put("createTime", DateUtil.toString(new Date(), 23));
                List<SettlementResultBean> listSet = settlementResultBiz.selectSettlementResultByParams(params);
                /**
                 * status 调度发送结算状态(1,未发送 2 发送成功,3,划扣成功，4，划扣部分成功，5，划扣失败 账单数据入库规则,账单数据在
                 * inst_settlementResult 中不存在,则全部入库 如果存在数据,则status=1且number次数为5次以上或status=5
                 * 或status=4 则重新入库
                 */
                if (listSet != null && listSet.size() > 0) {
                    for (SettlementResultBean bean : listSet) {
                        int status = bean.getStatus();
                        int number = bean.getNumber();
                        if ((status == 1 && number >= 5) || status == 5 || status == 4) {
                            SettlementResultBean setBean = new SettlementResultBean();
                            setBean.setOrderId(String.valueOf(objs.get("orderId")));
                            setBean.setRepayNo(Integer.parseInt(String.valueOf(objs.get("repayNo"))));
                            setBean.setOpenId(String.valueOf(objs.get("openId")));
                            BigDecimal curRepayAmt = (BigDecimal) objs.get("curRepayAmt");
                            setBean.setCurRepayAmt(curRepayAmt);
                            setBean.setTradeType(Integer.parseInt(String.valueOf(objs.get("tradeType"))));
                            setBean.setStatus(1);
                            setBean.setCreateTime(new Date());
                            setBean.setMerchantNo(String.valueOf(objs.get("merchantNo")));
                            setBean.setLastRepayDate(DateUtil.getDate(
                                    Long.parseLong(String.valueOf(objs.get("lastRepayDate"))), 2).getTime());
                            setBean.setMonthInterest((BigDecimal) objs.get("monthInterest"));
                            setBean.setOverdueInterest((BigDecimal) objs.get("curRepayOverdueInterest"));
                            setBean.setBillStatus(Integer.parseInt(String.valueOf(objs.get("status"))));
                            setBean.setOverdueDays(Integer.parseInt(String.valueOf(objs.get("overdueDays"))));
                            setBean.setFlag(2);
                            if (bean.getSmsFlag() == 1) {
                                setBean.setSmsFlag(bean.getSmsFlag());
                            } else {
                                setBean.setSmsFlag(2);
                            }
                            list.add(setBean);
                        }
                    }
                } else {
                    SettlementResultBean setBean = new SettlementResultBean();
                    setBean.setOrderId(String.valueOf(objs.get("orderId")));
                    setBean.setRepayNo(Integer.parseInt(String.valueOf(objs.get("repayNo"))));
                    setBean.setOpenId(String.valueOf(objs.get("openId")));
                    BigDecimal curRepayAmt = (BigDecimal) objs.get("curRepayAmt");
                    setBean.setCurRepayAmt(curRepayAmt);
                    setBean.setTradeType(Integer.parseInt(String.valueOf(objs.get("tradeType"))));
                    setBean.setStatus(1);
                    setBean.setCreateTime(new Date());
                    setBean.setMerchantNo(String.valueOf(objs.get("merchantNo")));
                    setBean.setLastRepayDate(DateUtil.getDate(
                            Long.parseLong(String.valueOf(objs.get("lastRepayDate"))), 2).getTime());
                    setBean.setMonthInterest((BigDecimal) objs.get("monthInterest"));
                    setBean.setOverdueInterest((BigDecimal) objs.get("curRepayOverdueInterest"));
                    setBean.setBillStatus(Integer.parseInt(String.valueOf(objs.get("status"))));
                    setBean.setOverdueDays(Integer.parseInt(String.valueOf(objs.get("overdueDays"))));
                    setBean.setFlag(2);
                    setBean.setSmsFlag(2);
                    list.add(setBean);
                }
            }
        }
        return list;
    }

    /**
     * 根据条件将代扣数据保存到inst_settlementresult
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月5日
     */
    @SuppressWarnings("unchecked")
    @Override
    public long insertSettlementResultFor() {
        JSONObject objs = new JSONObject();
        List<Map<String, String>> paraList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        List<InstSettleConfigBean> settlelist = InstSettleConfigBiz.selectInstSettleConfigResultForDByParams(params);

        if (!CollectionUtils.isEmpty(settlelist)) {
            Map<String, String> beanMap = null;
            for (InstSettleConfigBean bean : settlelist) {
                beanMap = new HashMap<>();
                beanMap.put("merchantNo", bean.getMerchantNo());
                beanMap.put("openId", bean.getBizType());
                beanMap.put("startDate", bean.getStartDate());
                beanMap.put("orderDate", bean.getOrderDate());
                beanMap.put("flag", bean.getFlag());
                paraList.add(beanMap);
            }
        }

        long result = 0;
        if (!CollectionUtils.isEmpty(paraList)) {
            logger.debug("---账单手动代扣接收收据---{}", JSONObject.toJSON(paraList));
            for (Map<String, String> map : paraList) {
                // 迭代取出所有未还账单
                int pageNum = 1;
                int numPerPage = 500;

                objs.put("numPerPage", numPerPage);
                objs.put("pageNum", pageNum);
                objs.put("startDate", map.get("startDate"));
                objs.put("orderDate", map.get("orderDate"));
                objs.put("merchantNo", map.get("merchantNo"));
                objs.put("openId", map.get("openId"));
                objs.put("flag", map.get("flag"));
                Map<String, Object> retMap = selectEveryDayExpireBill(objs);
                if (retMap != null) {
                    Map<String, Object> res = (Map<String, Object>) retMap.get("result");
                    if (retMap.get("retCode").equals("success")) {
                        while (pageNum <= Integer.parseInt(String.valueOf(res.get("endPageIndex")))) {
                            List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
                            List<SettlementResultBean> list = converList2ListBeanFor(recordList);
                            logger.info("---账单数据--{}", JSONObject.toJSONString(list));
                            pageNum++;
                            if (list != null && list.size() > 0) {
                                result = settlementResultBiz.insertSettlementResult(list);

                                objs.put("numPerPage", numPerPage);
                                objs.put("pageNum", pageNum);
                                retMap = selectEveryDayExpireBill(objs);
                                res = (Map<String, Object>) retMap.get("result");
                            }
                        }
                    }
                } else {
                    logger.debug("---调用账务系统接口--获取每日还款账单出现异常--");
                }
            }
        }
        return result;
    }

    /**
     * 代扣:将从账务系统查询到的账单转化List<Map<String, Object>>为List<SettlementResultBean>
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private List<SettlementResultBean> converList2ListBeanFor(List<Map<String, Object>> recordList) {
        List<SettlementResultBean> list = new ArrayList<>();
        if (recordList != null) {
            for (Map<String, Object> objs : recordList) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", String.valueOf(objs.get("orderId")));
                params.put("repayNo", String.valueOf(objs.get("repayNo")));
                params.put("createTime", DateUtil.toString(new Date(), 23));
                List<SettlementResultBean> listSet = settlementResultBiz.selectSettlementResultByParams(params);
                /**
                 * status 调度发送结算状态(1,未发送 2 发送成功,3,划扣成功，4，划扣部分成功，5，划扣失败 账单数据入库规则,账单数据在
                 * inst_settlementresult_withhold 中不存在,则全部入库;
                 * 如果存在数据,则status=1且number次数为5次以上或status=5 或status=4 则重新入库
                 */
                if (listSet != null && listSet.size() > 0) {
                    for (SettlementResultBean bean : listSet) {
                        int status = bean.getStatus();
                        int number = bean.getNumber();
                        if ((status == 1 && number >= 5) || status == 5 || status == 4) {
                            SettlementResultBean setBean = new SettlementResultBean();
                            setBean.setOrderId(String.valueOf(objs.get("orderId")));
                            setBean.setRepayNo(Integer.parseInt(String.valueOf(objs.get("repayNo"))));
                            setBean.setOpenId(String.valueOf(objs.get("openId")));
                            BigDecimal curRepayAmt = (BigDecimal) objs.get("curRepayAmt");
                            setBean.setCurRepayAmt(curRepayAmt);
                            setBean.setTradeType(Integer.parseInt(String.valueOf(objs.get("tradeType"))));
                            setBean.setStatus(1);
                            setBean.setCreateTime(new Date());
                            setBean.setMerchantNo(String.valueOf(objs.get("merchantNo")));
                            setBean.setLastRepayDate(DateUtil.getDate(
                                    Long.parseLong(String.valueOf(objs.get("lastRepayDate"))), 2).getTime());
                            setBean.setMonthInterest((BigDecimal) objs.get("monthInterest"));
                            setBean.setOverdueInterest((BigDecimal) objs.get("curRepayOverdueInterest"));
                            setBean.setBillStatus(Integer.parseInt(String.valueOf(objs.get("status"))));
                            setBean.setOverdueDays(Integer.parseInt(String.valueOf(objs.get("overdueDays"))));
                            setBean.setFlag(1);
                            if (bean.getSmsFlag() == 1) {
                                setBean.setSmsFlag(bean.getSmsFlag());
                            } else {
                                setBean.setSmsFlag(2);
                            }
                            list.add(setBean);
                        }
                    }
                } else {
                    SettlementResultBean setBean = new SettlementResultBean();
                    setBean.setOrderId(String.valueOf(objs.get("orderId")));
                    setBean.setRepayNo(Integer.parseInt(String.valueOf(objs.get("repayNo"))));
                    setBean.setOpenId(String.valueOf(objs.get("openId")));
                    BigDecimal curRepayAmt = (BigDecimal) objs.get("curRepayAmt");
                    setBean.setCurRepayAmt(curRepayAmt);
                    setBean.setTradeType(Integer.parseInt(String.valueOf(objs.get("tradeType"))));
                    setBean.setStatus(1);
                    setBean.setCreateTime(new Date());
                    setBean.setMerchantNo(String.valueOf(objs.get("merchantNo")));
                    setBean.setLastRepayDate(DateUtil.getDate(
                            Long.parseLong(String.valueOf(objs.get("lastRepayDate"))), 2).getTime());
                    setBean.setMonthInterest((BigDecimal) objs.get("monthInterest"));
                    setBean.setOverdueInterest((BigDecimal) objs.get("curRepayOverdueInterest"));
                    setBean.setBillStatus(Integer.parseInt(String.valueOf(objs.get("status"))));
                    setBean.setOverdueDays(Integer.parseInt(String.valueOf(objs.get("overdueDays"))));
                    setBean.setFlag(1);
                    setBean.setSmsFlag(2);
                    list.add(setBean);
                }
            }
        }
        return list;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月9日
     */
    @Override
    public long batchUpdateOrderLeftItems() {
        long result = 0;
        JSONObject objs = new JSONObject();
        List<OrderBean> orderList = orderBiz.selectAllOrderList(objs);
        if (!CollectionUtils.isEmpty(orderList)) {
            for (OrderBean bean : orderList) {
                if (!StringUtil.isNull(bean.getOrderId())) {
                    result += orderBiz.updateOrderItemsByOrderId(bean.getOrderId());
                }
            }
        }
        return result;
    }

    /**
     * 
     * Description:每日获取已逾期且逾期天数小于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    @SuppressWarnings("rawtypes")
    public long queryBillLastDateThree() {
        long result = 0;
        List<InstallmentBillPojo> list1 = new ArrayList<>();// 为inst_remind_phone所查list
        List<InstallmentBillPojo> list2 = new ArrayList<>();// 为每日账户系统所查list
        List<InstallmentBillPojo> list3 = new ArrayList<>();// 为相同账单 要执行更新
        List<InstallmentBillPojo> list4 = new ArrayList<>();// 为每日新进来的账单 还没有完善数据
        List<InstallmentBillPojo> list5 = new ArrayList<>();// 更新list1中也就是inst_remind_phone中的老数据的状态
                                                            // 如果已还款标记为已处理,如果逾期天数>5 标记为未处理
        // 电催所有信息
        try {
            list1 = orderBiz.queryBillLastDateThree();
        } catch (Exception e) {
            logger.info("---inst_remind_phone所查list异常--{}", e);
        }
        logger.debug("---inst_remind_phone所查list结果--{}", list1);
        // 账户系统获取已逾期且逾期天数小于等于5天的账单
        Map objs = new HashMap<>();
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getQueryBillLastDateThree(),
                    objs);
            if (!"".equals(resultStr) && resultStr != null) {
                Map<String, Object> retMap = JSONObject.parseObject(resultStr);
                list2 = JSON.parseArray(retMap.get("result").toString(), InstallmentBillPojo.class);
                logger.debug("---账户系统获取已逾期且逾期天数小于等于5天的账单结果--{}", list2);
            }
        } catch (Exception e) {
            logger.error("---账户系统获取已逾期且逾期天数小于等于5天的账单结果异常--{}", e);
        }
        // 将每日获取的逾期5天内的数据且已进入电催表的数据更新相关并且放入集合list3中
        InstallmentBillPojo installmentBillPojo1;
        for (InstallmentBillPojo installmentBillPojo : list2) {
            installmentBillPojo1 =
                    orderBiz.getInstallmentBillPojoByOrderId(installmentBillPojo.getOrderId(),
                            installmentBillPojo.getRepayNo());
            if (installmentBillPojo1 != null) {
                installmentBillPojo1.setCurRepayAmt(installmentBillPojo.getCurRepayAmt());
                installmentBillPojo1.setOverdueAmt(installmentBillPojo.getOverdueAmt());
                installmentBillPojo1.setCurRepayOverdueInterest(installmentBillPojo.getCurRepayOverdueInterest());
                installmentBillPojo1.setOverdueDays(installmentBillPojo.getOverdueDays());
                list3.add(installmentBillPojo1);
            }
        }
        // 说明没有重复元素 将list2全部入库
        if (list3.isEmpty()) {
            for (int i = 0; i < list2.size(); i++) {
                list4.add(list2.get(i));
            }
        } else {
            // 逾期小于5天的且没有入库的数据
            list2.removeAll(list3);
            list4 = list2;
        }

        // 更新list1中也就是inst_remind_phone中的老数据的状态 如果已还款标记为已处理,如果逾期天数>5 标记为未处理

        list1.removeAll(list3);
        list5 = list1;
        // list3执行更新操作
        try {
            if (!list3.isEmpty()) {
                orderBiz.updateBillLastDate(list3);
            }
        } catch (Exception e) {
            logger.info("---list3执行更新操作异常--{}", e);
        }
        // list4执行插入操作
        try {
            if (!list4.isEmpty()) {
                // 完善其他数据
                for (InstallmentBillPojo installmentBillPojo : list4) {
                    String orderId = installmentBillPojo.getOrderId();
                    OrderBean orderBean = orderBiz.getOrderAllInfoByOrderId(orderId);
                    if (orderBean != null) {
                        installmentBillPojo.setRealName(orderBean.getRealName());
                        installmentBillPojo.setMerchantFullName(orderBean.getMerchantNo());
                        installmentBillPojo.setOrderItems(orderBean.getOrderItems());
                    }
                    InstRemindPhoneBean remindBean =
                            instRemindPhoneBiz.selectInstManageCarInfo(installmentBillPojo.getOrderId());
                    if (remindBean == null) {
                        orderBiz.insertBillLastDateOne(installmentBillPojo);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("---list43执行插入操作异常--{}", e);
        }
        // list5执行查询账户系统返回最新的账单数据,然后更新inst_remind_phone对应状态
        try {
            if (!list5.isEmpty()) {
                insertSettlementResultFor2(list5);
            }
        } catch (Exception e) {
            logger.error("---list5执行账单查询,并更新数据库异常--{}", e);
        }
        return result;
    }

    @Override
    public long insertSettlementResultFor2(List<InstallmentBillPojo> list) {
        JSONObject objs = new JSONObject();
        List<Map<String, String>> paraList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            Map<String, String> beanMap = null;
            for (InstallmentBillPojo bean : list) {
                beanMap = new HashMap<>();
                beanMap.put("orderId", bean.getOrderId());
                beanMap.put("repayNo", Integer.toString(bean.getRepayNo()));
                paraList.add(beanMap);
            }
        }
        long result = 0;
        if (!CollectionUtils.isEmpty(paraList)) {
            logger.debug("---查询inst_remind_phone表中得数据{}", JSONObject.toJSON(paraList));
            // 迭代取出所有未还账单
            int pageNum = 1;
            int numPerPage = list.size();
            objs.put("numPerPage", numPerPage);
            objs.put("pageNum", pageNum);
            objs.put("haList", paraList);
            Map<String, Object> retMap = selectQueryBillInfoByOrderIdPage(objs);
            if (retMap != null) {
                if (retMap.get("retCode").equals("success")) {
                    List<InstallmentBillPojo> list1 =
                            JSON.parseArray(retMap.get("result").toString(), InstallmentBillPojo.class);
                    logger.debug("---账单数据--{}", JSONObject.toJSONString(list1));
                    if (list1 != null && !list1.isEmpty()) {
                        for (int j = 0; j < list1.size(); j++) {
                            InstallmentBillPojo queryBillInfoOne = orderBiz.queryBillInfoOne(list1.get(j));
                            if (list1.get(j).getStatus() != 4 && list1.get(j).getStatus() == 3) {// 表示查询的账单的状态为3全部还款,设置状态为已处理
                                try {
                                    queryBillInfoOne.setStatus(3);
                                    queryBillInfoOne.setCurRepayAmt(list1.get(j).getCurRepayAmt());
                                    queryBillInfoOne.setOverdueAmt(list1.get(j).getOverdueAmt());
                                    queryBillInfoOne.setOverdueDays(list1.get(j).getOverdueDays());
                                    queryBillInfoOne.setCurRepayOverdueInterest(list1.get(j)
                                            .getCurRepayOverdueInterest());
                                    orderBiz.updateBillInfoOne(queryBillInfoOne);
                                } catch (Exception e) {
                                    logger.error("---账户系统返回的list5需要查询和更新的数据出现异常--{}", e);
                                }
                            } else if (list1.get(j).getStatus() == 4) {
                                queryBillInfoOne.setCurRepayAmt(list1.get(j).getCurRepayAmt());
                                queryBillInfoOne.setOverdueAmt(list1.get(j).getOverdueAmt());
                                queryBillInfoOne.setCurRepayOverdueInterest(list1.get(j).getCurRepayOverdueInterest());
                                queryBillInfoOne.setStatus(3);
                                queryBillInfoOne.setOverdueDays(list1.get(j).getOverdueDays());
                                orderBiz.updateBillInfoOne(queryBillInfoOne);
                            } else {// 更新逾期天数
                                queryBillInfoOne.setCurRepayAmt(list1.get(j).getCurRepayAmt());
                                queryBillInfoOne.setOverdueAmt(list1.get(j).getOverdueAmt());
                                queryBillInfoOne.setCurRepayOverdueInterest(list1.get(j).getCurRepayOverdueInterest());
                                if (list1.get(j).getOverdueDays() > 5) {
                                    queryBillInfoOne.setStatus(3);
                                }
                                queryBillInfoOne.setOverdueDays(list1.get(j).getOverdueDays());
                                orderBiz.updateBillInfoOne(queryBillInfoOne);
                            }
                        }
                    }
                }
            } else {
                logger.error("---调用账务系统接口--获取每日还款账单出现异常--");
            }
        }
        return result;
    }

    /**
     * 
     * Description:每日获取已逾期且逾期天大于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    @SuppressWarnings("rawtypes")
    public long queryBillLastBigDateFive() {
        long result = 0;
        List<InstallmentBillPojo> list2 = new ArrayList<>();// 为每日账户系统所查list逾期大于五天
        // 账户系统获取已逾期且逾期天数小于等于5天的账单
        Map objs = new HashMap<>();
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getQueryBillLastDateFive(),
                    objs);
            if (!"".equals(resultStr) && resultStr != null) {
                Map<String, Object> retMap = JSONObject.parseObject(resultStr);
                list2 = JSON.parseArray(retMap.get("result").toString(), InstallmentBillPojo.class);
                logger.debug("---账户系统获取已逾期且逾期天数大于5天的账单结果--{}", JSONObject.toJSONString(list2));
            }
        } catch (Exception e) {
            logger.error("---账户系统获取已逾期且逾期天数大于5天的账单结果异常--{}", e);
        }

        for (InstallmentBillPojo bean : list2) {
            // 1.没有进入贷后则转贷后处理
            ManageCarInfoBean manageCarInfoBean = orderBiz.queryInstManagecarInfoOne(bean.getOrderId());
            if (manageCarInfoBean == null || manageCarInfoBean.getOrderId() == null
                    || "".equals(manageCarInfoBean.getOrderId())) {
                try {
                    manageCarInfoBean = new ManageCarInfoBean();
                    manageCarInfoBean.setOrderId(bean.getOrderId());
                    orderBiz.insertManageCarInfoBean(manageCarInfoBean);
                    orderBiz.updateOrderCarStatusByOrderId(bean.getOrderId());
                } catch (Exception e) {
                    logger.error("---跑批插入inst_managecar_info数据有误,更新是否存入inst_managecar_info状态标识有误--{}", e);
                }

            } else {
                orderBiz.updateInstManageCatStatusByOrderId(bean.getOrderId());
                orderBiz.updateOrderCarStatusByOrderId(bean.getOrderId());
            }
            // 2.电催处理意见为保留任务时,将电催处理状态改为已处理
            manageCarInfoBean = orderBiz.getInstRemindRecordInfoOne(bean.getOrderId(), bean.getRepayNo());
            if (manageCarInfoBean != null) {
                logger.info("---逾期5天账单直接转贷后,电催处理意见为保留任务的账单信息-- orderId:{}---curItems:{}", bean.getOrderId(),
                        bean.getRepayNo());
                bean.setStatus(3);
                List<InstallmentBillPojo> list = new ArrayList<>();
                list.add(bean);
                orderBiz.updateBillLastDate(list);
            } else {
                List<InstallmentBillPojo> list = new ArrayList<>();
                list.add(bean);
                orderBiz.updateBillLastDate(list);
            }
        }
        return result;
    }

    /**
     * 划扣失败-短信通知
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月15日
     */
    @Override
    public long failureAfterSms() {
        Map<String, String> params = new HashMap<>();
        List<SettlementResultBean> failSettlementList = settlementResultBiz.selectFailSettlementResultByParams(params);
        if (!CollectionUtils.isEmpty(failSettlementList)) {
            JSONObject objs = new JSONObject();
            objs.put("flag", "2");
            objs.put("list", failSettlementList);
            logger.debug("---划扣失败-短信通知---参数{}:---发送地址{}", objs,consumerConfig.getSmsUrl());
            String resultStr = HttpsClientUtil.doPost(consumerConfig.getSmsUrl(), objs.toString(), "UTF-8");
            if (!StringUtil.isNull(resultStr)) {
                JSONObject json = JSONObject.parseObject(resultStr);
                String retCode = json.getString("retCode");
                String iqbResult = json.getString("iqbResult");
                if (retCode.equals("00000000")) {
                    JSONObject iqbResultJson = JSONObject.parseObject(iqbResult);

                    UserPaymentInfoDto dto = JSONObject.toJavaObject(iqbResultJson, UserPaymentInfoDto.class);

                    if (!CollectionUtils.isEmpty(dto.getResult())) {
                        return doSmsInfo(dto.getResult());
                    }
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    /*
     * 根据发送短信成功列表更新代扣信息表-短信代扣标识
     */
    private long doSmsInfo(List<UserPaymentInfoBean> smsList) {
        long resultValue = 0;
        if (!CollectionUtils.isEmpty(smsList)) {
            for (UserPaymentInfoBean bean : smsList) {
                JSONObject objs = new JSONObject();
                objs.put("orderId", bean.getOrderId());
                objs.put("repayNo", bean.getRepayNo());
                objs.put("smsFlag", "1");
                resultValue += settlementResultBiz.updateSettlementResult(objs);
            }
        }
        return resultValue;
    }
}
