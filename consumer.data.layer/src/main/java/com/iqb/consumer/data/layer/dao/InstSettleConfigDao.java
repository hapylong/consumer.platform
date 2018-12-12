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
 * 2017年8月11日上午10:38:59 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.settlementresult.InstSettleConfigBean;

/**
 * @author haojinlong
 * 
 */
public interface InstSettleConfigDao {
    /**
     * 
     * Description:保存商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int saveInstSettleConfig(List<InstSettleConfigBean> list);

    /**
     * 
     * Description:修改商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int updateInstSettleConfigById(Map<String, Object> params);

    /**
     * 
     * Description:根据条件查询代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultByParams(JSONObject objs);

    /**
     * 
     * Description:禁用启用
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public int updateStatus(@Param("ids") List<String> ids, @Param("status") String status);

    /**
     * 
     * Description:根据id查询商户代扣配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public InstSettleConfigBean queryById(JSONObject objs);
}
