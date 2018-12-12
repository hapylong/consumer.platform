package com.iqb.consumer.data.layer.dao.ownerloan;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.ownerloan.DeptSignInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MortgageInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.OwnerLoanBaseInfoBean;

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
 * 2017年11月10日上午10:32:48 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface MortgageInfoDao {
    /**
     * 
     * Description:车主贷-保存车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    int insertMortgageInfo(MortgageInfoBean mortgageInfoBean);

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
     * Description:获取车辆以及抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    MortgageInfoBean getCarinfo(JSONObject objs);

    /**
     * 
     * Description:获取门店签约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    DeptSignInfoBean getDeptSignInfo(JSONObject objs);

    /**
     * 
     * Description:获取放款确认信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    DeptSignInfoBean getLoanInfo(JSONObject objs);

}
