package com.iqb.consumer.data.layer.dao.carthreehundred;

import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskBean;
import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskResultBean;

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
 * 2018年8月14日下午5:58:48 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstOrderloanRiskDao {
    /**
     * 
     * Description:保存发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    int saveInstOrderloanRiskInfo(InstOrderloanRiskBean bean);

    /**
     * 
     * Description:更新发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    int updateInstOrderloanRiskInfoByOrderId(InstOrderloanRiskBean bean);

    /**
     * 
     * Description:根据订单号获取发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    InstOrderloanRiskBean getInstOrderloanRiskInfoByOrderId(String orderId);

    /**
     * 
     * Description:保存贷后风控返回结果信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月17日
     */
    int insertInstLoanRiskResult(InstOrderloanRiskResultBean bean);
}
