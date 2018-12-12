package com.iqb.consumer.batch.biz;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.dao.OrderDao;
import com.iqb.consumer.batch.data.pojo.InstallmentBillPojo;
import com.iqb.consumer.batch.data.pojo.ManageCarInfoBean;
import com.iqb.consumer.batch.data.pojo.OrderBean;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.StringUtil;

@Component
public class OrderBiz extends BaseBiz {

    @Resource
    private OrderDao orderDao;
    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    @Autowired
    private RedisPlatformDao redisPlatformDao;

    public String generateOrderId(String merchantNo, String bizType) {
        if (StringUtil.isEmpty(merchantNo) || StringUtil.isEmpty(bizType)) {
            throw new RuntimeException("生成订单id失败，传入商户号为空");
        }
        String seqRedisKey = this.getOrderRedisKey(merchantNo, bizType);
        return seqRedisKey + this.getSeqFromRedis(seqRedisKey, false);
    }

    public String getOrderRedisKey(String merchantNo, String bizType) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return merchantNo.toUpperCase() + bizType + todayStr;
    }

    private synchronized String getSeqFromRedis(String key, boolean isSub) {
        /** 数字格式化 **/
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        /** 从redis中取值 **/
        String val = this.redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(val)) {
            this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
            return df.format(Integer.parseInt(this.INITIAL_SEQ));
        }
        Integer seq = Integer.parseInt(val);

        /** 判断是否进行减法操作 **/
        if (isSub) {
            seq = seq - 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        } else {
            seq = seq + 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        }
        return df.format(seq);
    }

    /**
     * 
     * Description:查询所有订单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public List<OrderBean> selectAllOrderList(JSONObject objs) {
        super.setDb(1, SLAVE);
        return this.orderDao.selectAllOrderList();
    }

    /**
     * 
     * Description:修改订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public int updateOrderItemsByOrderId(String orderId) {
        super.setDb(0, MASTER);
        return this.orderDao.updateOrderItemsByOrderId(orderId);
    }

    public int updateOrderCarStatusByOrderId(String orderId) {
        super.setDb(0, MASTER);
        return this.orderDao.updateOrderCarStatusByOrderId(orderId);
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
    public List<InstallmentBillPojo> queryBillLastDateThree() {
        super.setDb(0, SLAVE);
        return this.orderDao.queryBillLastDateThree();
    }

    public void updateBillLastDate(List<InstallmentBillPojo> list3) {
        super.setDb(0, MASTER);
        for (InstallmentBillPojo installmentBillPojo : list3) {
            this.orderDao.updateBillLastDate(installmentBillPojo);
        }

    }

    public void insertBillLastDate(List<InstallmentBillPojo> list4) {
        super.setDb(0, MASTER);
        for (InstallmentBillPojo installmentBillPojo : list4) {
            this.orderDao.insertBillLastDate(installmentBillPojo);
        }
    }

    public void insertBillLastDateOne(InstallmentBillPojo installmentBillPojo) {
        super.setDb(0, MASTER);
        this.orderDao.insertBillLastDate(installmentBillPojo);
    }

    public OrderBean getOrderAllInfoByOrderId(String orderId) {
        super.setDb(0, MASTER);
        return orderDao.getOrderAllInfoByOrderId(orderId);
    }

    public InstallmentBillPojo queryBillInfoOne(InstallmentBillPojo installmentBillPojo) {
        super.setDb(0, SLAVE);
        return this.orderDao.queryBillInfoOne(installmentBillPojo);
    }

    public void updateBillInfoOne(InstallmentBillPojo queryBillInfoOne) {
        super.setDb(0, MASTER);
        orderDao.updateBillLastDate(queryBillInfoOne);
    }

    public List<ManageCarInfoBean> queryBillLastDateFive() {
        super.setDb(0, SLAVE);
        return this.orderDao.queryBillLastDateFive();
    }

    public void insertManageCarInfoBean(ManageCarInfoBean manageCarInfoBean) {
        super.setDb(0, MASTER);
        orderDao.insertManageCarInfoBean(manageCarInfoBean);
    }

    public List<ManageCarInfoBean> queryInstManagecarInfo() {
        super.setDb(1, MASTER);
        return orderDao.queryBillLastDateFive();
    }

    public ManageCarInfoBean queryInstManagecarInfoOne(String orderId) {
        super.setDb(1, MASTER);
        return orderDao.queryInstManagecarInfoOne(orderId);
    }

    public InstallmentBillPojo getInstallmentBillPojoByOrderId(String orderId, int repayNo) {
        super.setDb(1, MASTER);
        return orderDao.getInstallmentBillPojoByOrderId(orderId, repayNo + "");
    }

    public int updateInstManageCatStatusByOrderId(String orderId) {
        super.setDb(0, MASTER);
        return orderDao.updateInstManageCatStatusByOrderId(orderId);
    }

    /**
     * 
     * Description:根据订单号 当前期数 处理意见查询数据信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月8日
     */
    public ManageCarInfoBean getInstRemindRecordInfoOne(String orderId, int curItems) {
        super.setDb(1, MASTER);
        return orderDao.getInstRemindRecordInfoOne(orderId, curItems);
    }
}
