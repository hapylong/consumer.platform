package com.iqb.consumer.data.layer.dao.afterLoan;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;

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
 * 2018年7月13日下午2:30:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstOrderLawDao {
    /**
     * 
     * 保存案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    int saveInstOrderLawnInfo(JSONObject objs);

    /**
     * 
     * 更新案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    int updateInstOrderLawnInfo(JSONObject objs);

    /**
     * 
     * 根据订单号查询案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    InstOrderLawBean getInstOrderLawnInfoByCaseId(JSONObject objs);

    /**
     * 
     * 根据订单号查询案件登记资料列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    List<InstOrderLawBean> selectInstOrderLawnInfoByOrderId(JSONObject objs);

    /**
     * 
     * 根据caseNo查询案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    List<InstOrderLawBean> selectInstOrderLawnInfoByCaseNo(JSONObject objs);

}
