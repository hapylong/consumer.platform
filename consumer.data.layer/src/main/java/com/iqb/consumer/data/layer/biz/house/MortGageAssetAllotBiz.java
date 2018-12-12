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
 * 2017年8月17日下午2:44:09 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.biz.house;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.house.MortGageAssetAllotBean;
import com.iqb.consumer.data.layer.dao.house.MortGageAssetAllotDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Component
public class MortGageAssetAllotBiz extends BaseBiz {
    @Resource
    private MortGageAssetAllotDao mortGageAssetAllotDao;

    /**
     * 
     * Description:房贷资产分配-保存
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月17日
     */
    public int saveMortGageAssetAllot(MortGageAssetAllotBean mortGageAssetAllotBean) {
        setDb(0, super.MASTER);
        return mortGageAssetAllotDao.saveMortGageAssetAllot(mortGageAssetAllotBean);
    }
}
