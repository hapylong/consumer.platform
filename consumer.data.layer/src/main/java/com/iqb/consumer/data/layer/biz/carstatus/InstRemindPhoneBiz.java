package com.iqb.consumer.data.layer.biz.carstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstallmentBillInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.dao.carstatus.InstRemindPhoneDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年4月26日下午2:41:59 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */

@Component
public class InstRemindPhoneBiz extends BaseBiz {
    protected static Logger logger = LoggerFactory.getLogger(InstRemindPhoneBiz.class);
    @Autowired
    private InstRemindPhoneDao instRemindPhoneDao;
    @Autowired
    private EncryptUtils encryptUtils;
    @Autowired
    private ConsumerConfig consumerConfig;

    /**
     * 根据条件查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public List<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs) {
        logger.info("---根据条件查询电话提醒列表信息---参数---{}", objs);
        super.setDb(1, super.MASTER);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<InstRemindPhoneBean> list = instRemindPhoneDao.selectInstRemindPhoneList(objs);

        List<Map<String, String>> paraList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            Map<String, String> beanMap = null;
            for (InstRemindPhoneBean bean : list) {
                beanMap = new HashMap<>();
                beanMap.put("orderId", bean.getOrderId());
                beanMap.put("repayNo", Integer.toString(bean.getCurItems()));
                paraList.add(beanMap);
            }

            // 调用账户系统查询账单状态
            objs.clear();
            objs.put("haList", paraList);
            Map<String, Object> retMap = selectQueryBillInfoByOrderIdPage(objs);
            if (retMap != null) {
                if (retMap.get("retCode").equals("success")) {
                    List<InstallmentBillInfoBean> list1 =
                            JSON.parseArray(retMap.get("result").toString(), InstallmentBillInfoBean.class);
                    if (!CollectionUtils.isEmpty(list1)) {
                        for (InstRemindPhoneBean bean : list) {
                            for (InstallmentBillInfoBean billInfoBean : list1) {
                                if (bean.getOrderId().equals(billInfoBean.getOrderId())
                                        && bean.getCurItems() == billInfoBean.getRepayNo()) {
                                    bean.setBillInfoStatus(String.valueOf(billInfoBean.getStatus()));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 根据条件更新单条信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月26日 17:36:02
     */
    public void updateInstRemindPhoneBean(InstRemindPhoneBean instRemindPhoneBean) {
        super.setDb(0, super.MASTER);
        instRemindPhoneDao.updateInstRemindPhoneBean(instRemindPhoneBean);
    }

    public InstRemindPhoneBean selectInstRemindPhoneOne(InstRemindRecordBean instRemindRecordBean) {
        super.setDb(1, super.MASTER);
        return instRemindPhoneDao.selectInstRemindPhoneOne(instRemindRecordBean);
    }

    public List<CgetCarStatusInfoResponseMessage> afterLoanList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        List<CgetCarStatusInfoResponseMessage> list = instRemindPhoneDao.afterLoanList(requestMessage);
        return list;
    }

    public OrderBean queryOrderInfoByOrderId(JSONObject requestMessage) {
        super.setDb(1, super.MASTER);
        return instRemindPhoneDao.queryOrderInfoByOrderId(requestMessage);
    }

    public void insertManagecarInfo(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        instRemindPhoneDao.saveManageCarInfo(requestMessage);
    }

    public int updateManagerCarInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return instRemindPhoneDao.updateManagerCarInfo(objs);
    }

    public List<InstRemindPhoneBean> selectInstRemindPhoneList2(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<InstRemindPhoneBean> list = instRemindPhoneDao.selectInstRemindPhoneList2(objs);

        List<Map<String, String>> paraList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            Map<String, String> beanMap = null;
            for (InstRemindPhoneBean bean : list) {
                beanMap = new HashMap<>();
                beanMap.put("orderId", bean.getOrderId());
                beanMap.put("repayNo", Integer.toString(bean.getCurItems()));
                paraList.add(beanMap);
            }

            // 调用账户系统查询账单状态
            objs.put("haList", paraList);
            Map<String, Object> retMap = selectQueryBillInfoByOrderIdPage(objs);
            if (retMap != null) {
                if (retMap.get("retCode").equals("success")) {
                    List<InstallmentBillInfoBean> list1 =
                            JSON.parseArray(retMap.get("result").toString(), InstallmentBillInfoBean.class);
                    if (!CollectionUtils.isEmpty(list1)) {
                        for (InstRemindPhoneBean bean : list) {
                            for (InstallmentBillInfoBean billInfoBean : list1) {
                                if (bean.getOrderId().equals(billInfoBean.getOrderId())
                                        && bean.getCurItems() == billInfoBean.getRepayNo()) {
                                    bean.setBillInfoStatus(String.valueOf(billInfoBean.getStatus()));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 
     * Description:根据订单号查询风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    public InstRemindPhoneBean getRiskInfoByOrderId(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instRemindPhoneDao.getRiskInfoByOrderId(objs);
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
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 
     * Description:根据订单号查询电话电催信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    public InstRemindPhoneBean getRemindInfoByOrderId(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instRemindPhoneDao.getRemindInfoByOrderId(objs);
    }
}
