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
 * 2017年8月1日下午5:28:39 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.dao.InstSettleConfigDao;
import com.iqb.consumer.batch.data.pojo.InstSettleConfigBean;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Component
public class InstSettleConfigBiz extends BaseBiz {
    @Resource
    private InstSettleConfigDao instSettleConfigDao;

    /**
     * 
     * Description:根据条件查询代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultByParams(Map<String, String> params) {
        setDb(1, super.MASTER);
        return instSettleConfigDao.selectInstSettleConfigResultByParams(params);
    }

    /**
     * 
     * Description:根据条件查询手动代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月5日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultForDByParams(Map<String, String> params) {
        setDb(1, super.MASTER);
        return instSettleConfigDao.selectInstSettleConfigResultForDByParams(params);
    }
}
