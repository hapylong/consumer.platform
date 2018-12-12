package com.iqb.consumer.data.layer.dao.wf;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyOrderPojo;

public interface SettleApplyBeanDao {

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    SettleApplyBean getSettleBeanByOrderId(String id);

    /**
     * 中阁审批保存原因和备注等信息
     * 
     * @param sab
     * @return
     */
    int updateSettleBean(SettleApplyBean sab);

    /**
     * 通过ID修改审批状态
     * 
     * @param obj
     * @return
     */
    int updateSettleStatus(Map<String, Object> obj);

    int updateCutInfo(SettleApplyBean sab);

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    SettleApplyBean selectSettleBeanByOrderId(String orderId);

    /**
     * 通过ID查询待支付金额
     * 
     * @param id
     * @return
     */
    SettleApplyBean getNeedPayAmt(String id);

    /**
     * 修改退租相关信息
     * 
     * @param params
     * @return
     */
    int updateSettleApply(Map<String, Object> params);

    /**
     * 
     * Description:保存提前结清信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    int saveSettleApplyInfo(SettleApplyBean sab);

    /**
     * 
     * 
     * @Description: 门店申请分页查询接口
     * @param param
     * @return List<SettleApplyOrderPojo>
     * @author chengzhen
     * @data
     */
    List<SettleApplyOrderPojo> selectSettleOrderList(JSONObject param);

    /**
     * 
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public List<SettleApplyBean> selectPrepaymentList(JSONObject objs);

    /**
     * 通过订单号查询提前结清审批数据
     * 
     * @param orderId
     * @return
     */
    SettleApplyBean selectSettleBeanByOrderIdForValidate(String orderId);

    /**
     * 
     * Description:更新提前结清减免后的违约金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月18日
     */
    int updateSettleAmtForOrderId(Map<String, Object> obj);
}
