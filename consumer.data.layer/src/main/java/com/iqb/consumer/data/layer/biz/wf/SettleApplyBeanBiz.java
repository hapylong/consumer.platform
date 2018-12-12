package com.iqb.consumer.data.layer.biz.wf;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyOrderPojo;
import com.iqb.consumer.data.layer.dao.wf.SettleApplyBeanDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class SettleApplyBeanBiz extends BaseBiz {

    @Resource
    private SettleApplyBeanDao settleApplyBeanDao;

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    public SettleApplyBean getSettleBeanByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        return settleApplyBeanDao.getSettleBeanByOrderId(orderId);
    }

    /**
     * 中阁审批保存原因和备注等信息
     * 
     * @param sab
     * @return
     */
    public int updateSettleBean(SettleApplyBean sab) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.updateSettleBean(sab);
    }

    /**
     * 通过ID修改审批状态
     * 
     * @param obj
     * @return
     */
    public int updateSettleStatus(Map<String, Object> obj) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.updateSettleStatus(obj);
    }

    public void updateCutInfo(SettleApplyBean sab) {
        setDb(0, super.MASTER);
        settleApplyBeanDao.updateCutInfo(sab);
    }

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    public SettleApplyBean selectSettleBeanByOrderId(String orderId) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.selectSettleBeanByOrderId(orderId);
    }

    /**
     * 通过ID查询待支付金额
     * 
     * @param id
     * @return
     */
    public SettleApplyBean getNeedPayAmt(String id) {
        setDb(0, super.SLAVE);
        return settleApplyBeanDao.getNeedPayAmt(id);
    }

    /**
     * 修改退租相关信息
     * 
     * @param params
     * @return
     */
    public int updateSettleApply(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.updateSettleApply(params);
    }

    /**
     * 
     * Description:保存提前结清信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public int saveSettleApplyInfo(SettleApplyBean sab) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.saveSettleApplyInfo(sab);
    }

    /**
     * 
     * 
     * @Description: 门店申请分页查询接口
     * @param param
     * @return List<SettleApplyOrderPojo>
     * @author chengzhen
     * @data
     */
    public List<SettleApplyOrderPojo> selectSettleOrderList(JSONObject param, boolean isPage) {
        setDb(0, super.MASTER);
        if (isPage) {
            PageHelper.startPage(getPagePara(param));
        }
        return settleApplyBeanDao.selectSettleOrderList(param);
    }

    /**
     * 
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public List<SettleApplyBean> selectPrepaymentList(JSONObject objs) {
        setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return settleApplyBeanDao.selectPrepaymentList(objs);
    }

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    public SettleApplyBean selectSettleBeanByOrderIdForValidate(String orderId) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.selectSettleBeanByOrderIdForValidate(orderId);
    }

    /**
     * 
     * Description:更新提前结清减免后的违约金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月18日
     */
    public int updateSettleAmtForOrderId(Map<String, Object> obj) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.updateSettleAmtForOrderId(obj);
    }
}
