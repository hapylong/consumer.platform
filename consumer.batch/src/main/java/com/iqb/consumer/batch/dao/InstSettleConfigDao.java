/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月1日下午5:15:14 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.batch.data.pojo.InstSettleConfigBean;

/**
 * @author haojinlong
 * 
 */
public interface InstSettleConfigDao {
    /**
     * 
     * Description:根据条件查询代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultByParams(Map<String, String> params);

    /**
     * 
     * Description:根据条件查询手动代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月5日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultForDByParams(Map<String, String> params);

}
