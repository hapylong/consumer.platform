package com.iqb.consumer.data.layer.dao.settlementresult;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.settlementresult.BillWithHoldBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultWithHoldBean;

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
 * 2018年1月7日下午2:56:27 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface SettlementResultWithHoldDao {
    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    List<SettlementResultWithHoldBean> listSettlementResultWithHold(JSONObject objs);

    /**
     * 
     * Description:根据订单号、期数查询还款代扣发送结算中心的数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    BillWithHoldBean selectBillWithHoldInfo(Map<String, Object> params);

    /**
     * 
     * Description:根据id修改还款代扣信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    int updateSettlementById(Map<String, Object> params);

    /**
     * 
     * Description:更新代扣信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */

    int updateSettleByTradeNo(SettlementResultWithHoldBean bean);

    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    List<SettlementResultWithHoldBean> listSettlementResult(JSONObject objs);
}
