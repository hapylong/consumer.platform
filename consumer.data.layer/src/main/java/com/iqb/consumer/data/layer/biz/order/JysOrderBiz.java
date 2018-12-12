/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 上午11:23:47
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.dao.JysOrderDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 交易所需求订单相关操作
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class JysOrderBiz extends BaseBiz {

    @Resource
    private JysOrderDao jysOrderDao;

    /**
     * 通过订单号查询订单相关信息
     * 
     * @param orderId
     * @return
     */
    public JYSOrderBean getSingleOrderInfo(JSONObject objs) {
        setDb(0, super.SLAVE);
        return jysOrderDao.getSingleOrderInfo(objs);
    }

    /**
     * 分页查询交易所订单信息
     */
    public List<JYSOrderBean> listJYSOrderInfo(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<JYSOrderBean> list = jysOrderDao.listJYSOrderInfo(objs);
        return list;
    }

    /**
     * 修改交易所订单
     * 
     * @param orderId
     * @return
     */
    public int updateJYSOrderInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        return jysOrderDao.updateJYSOrderInfo(objs);
    }

    /**
     * 交易所需求:修改订单状态
     * 
     * @param status
     * @return
     */
    public int updateOrderStatus(String status, String orderId) {
        setDb(0, super.MASTER);
        return jysOrderDao.updateOrderStatus(status, orderId);
    }

    /**
     * 删除订单信息(修改状态为0)
     * 
     * @param id
     * @return
     */
    public int deleteJYSOrderInfo(List list) {
        setDb(0, super.MASTER);
        return jysOrderDao.deleteJYSOrderInfo(list);
    }

    public int updateJYSUserInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        return jysOrderDao.updateJYSUserInfo(objs);
    }

    public void updateOrderRiskStatus(JYSOrderBean job) {
        setDb(0, super.MASTER);
        jysOrderDao.updateOrderRiskStatus(job);
    }

    /**
     * 
     * Description:保存交易所订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public long insertJysOrder(JYSOrderBean job) {
        setDb(0, super.MASTER);
        jysOrderDao.insertJysOrder(job);
        return job.getId();
    }

    /**
     * 
     * Description:根据ID获取交易所订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月6日
     */
    public JYSOrderBean getOrderInfoById(String orderId) {
        setDb(1, super.MASTER);
        return jysOrderDao.getOrderInfoById(orderId);
    }
}
