package com.iqb.consumer.service.layer.ownerloan;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.ownerloan.DeptSignInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MortgageInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.OwnerLoanBaseInfoBean;
import com.iqb.etep.common.exception.IqbException;

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
 * 2017年11月10日上午9:43:15 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OwnerLoanService {
    /**
     * 
     * Description:根据订单号获取车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param requestO
     * @return 2017年11月10日
     */
    MortgageInfoBean getCarinfo(JSONObject objs);

    /**
     * 
     * Description:查询车辆抵押 风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月10日
     */
    MortgageInfoBean selectOneByOrderId(JSONObject objs);

    /**
     * 
     * Description:车主贷-获取订单 人员 卡信息接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    OwnerLoanBaseInfoBean getBaseInfo(JSONObject objs);

    /**
     * 
     * Description:车主贷-更新车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    int updateCarInfo(JSONObject objs);

    /**
     * 
     * Description:车主贷-更新车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    int updateMortgageInfo(JSONObject objs);

    /**
     * 
     * Description:车主贷-更新放款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    int updateOrderInfo(JSONObject objs);

    /**
     * 
     * Description:车主贷-风控回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    int ownerLoanRiskNotice(JSONObject objs) throws IqbException;

    /**
     * 
     * Description:车主贷-更新GPS信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    int updateGpsInfo(JSONObject objs);

    /**
     * 
     * Description:车主贷-金额计算
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    Map<String, BigDecimal> recalAmt(JSONObject objs) throws IqbException;

    /**
     * 
     * Description:获取门店签约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    DeptSignInfoBean getDeptSignInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description:获取放款确认信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    DeptSignInfoBean getLoanInfo(JSONObject objs) throws IqbException;
}
