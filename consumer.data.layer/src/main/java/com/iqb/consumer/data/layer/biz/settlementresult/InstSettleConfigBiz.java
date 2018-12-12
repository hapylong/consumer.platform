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
package com.iqb.consumer.data.layer.biz.settlementresult;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.settlementresult.InstSettleConfigBean;
import com.iqb.consumer.data.layer.dao.InstSettleConfigDao;
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
     * Description:保存商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int saveInstSettleConfig(List<InstSettleConfigBean> list) {
        setDb(0, super.MASTER);
        return instSettleConfigDao.saveInstSettleConfig(list);
    }

    /**
     * 
     * Description:修改商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月11日
     */
    public int updateInstSettleConfigById(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return instSettleConfigDao.updateInstSettleConfigById(params);
    }

    /**
     * 
     * Description:根据条件查询代扣方案配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    public List<InstSettleConfigBean> selectInstSettleConfigResultByParams(JSONObject objs) {
        setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return instSettleConfigDao.selectInstSettleConfigResultByParams(objs);
    }

    /**
     * 
     * Description:禁用启用
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public int updateStatus(List<String> ids, String status) {
        setDb(0, super.MASTER);
        return instSettleConfigDao.updateStatus(ids, status);
    }

    /**
     * 
     * Description:根据id查询商户代扣配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public InstSettleConfigBean queryById(JSONObject objs) {
        setDb(1, super.MASTER);
        return instSettleConfigDao.queryById(objs);
    }
}
