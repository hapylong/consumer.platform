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
 * 2017年8月11日上午11:27:52 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.service.layer.settlementresult;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.settlementresult.InstSettleConfigBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author haojinlong
 * 
 */
public interface InstSettleConfigService {
    /**
     * 
     * Description:保存商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int saveInstSettleConfig(JSONObject objs) throws IqbException;

    /**
     * 
     * Description:修改商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int updateInstSettleConfigById(JSONObject objs);

    /**
     * 
     * Description:根据条件查询代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    public PageInfo<InstSettleConfigBean> selectInstSettleConfigResultByParams(JSONObject objs);

    /**
     * 
     * Description:禁用启用
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public int updateStatus(String status, JSONObject objs);

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
