package com.iqb.consumer.data.layer.biz.settlementresult;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.settlementresult.BillWithHoldBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultWithHoldBean;
import com.iqb.consumer.data.layer.dao.settlementresult.SettlementResultWithHoldDao;
import com.iqb.etep.common.base.biz.BaseBiz;

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
 * 2018年1月7日下午3:05:06 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Repository
public class SettlementResultWithHoldBiz extends BaseBiz {
    @Resource
    private SettlementResultWithHoldDao settlementResultWithHoldDao;

    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    public List<SettlementResultWithHoldBean> listSettlementResultWithHold(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return settlementResultWithHoldDao.listSettlementResultWithHold(objs);
    }

    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    public List<SettlementResultWithHoldBean> listSettlementResultWithHold2(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        return settlementResultWithHoldDao.listSettlementResultWithHold(objs);
    }

    /**
     * 
     * Description:根据订单号、期数查询还款代扣发送结算中心的数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public BillWithHoldBean selectBillWithHoldInfo(Map<String, Object> params) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        return settlementResultWithHoldDao.selectBillWithHoldInfo(params);
    }

    /**
     * 
     * Description:根据id修改还款代扣信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public int updateSettlementById(Map<String, Object> params) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        return settlementResultWithHoldDao.updateSettlementById(params);
    }

    /**
     * 
     * Description:更新代扣信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */

    public int updateSettleByTradeNo(SettlementResultWithHoldBean bean) {
        setDb(1, super.SLAVE);
        return settlementResultWithHoldDao.updateSettleByTradeNo(bean);
    }

    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    public List<SettlementResultWithHoldBean> listSettlementResult(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return settlementResultWithHoldDao.listSettlementResult(objs);
    }

    public List<SettlementResultWithHoldBean> exportSettlementResultList(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        return settlementResultWithHoldDao.listSettlementResult(objs);
    }
}
