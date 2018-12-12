package com.iqb.consumer.service.layer.settlementresult;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
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
 * 2018年1月7日下午3:10:03 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface SettlementResultWithHoldService {
    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    PageInfo<SettlementResultWithHoldBean> listSettlementResultWithHold(JSONObject objs);

    /**
     * 
     * Description:导出代扣信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    String exportSettlementResultList(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * Description:还款代扣
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    String billWithHold(JSONObject objs);

    /**
     * 
     * Description:还款代扣回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    int callback(JSONObject objs);
}
