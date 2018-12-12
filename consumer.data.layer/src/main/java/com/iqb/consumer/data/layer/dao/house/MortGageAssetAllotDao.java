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
 * 2017年8月17日下午2:41:25 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.dao.house;

import com.iqb.consumer.data.layer.bean.house.MortGageAssetAllotBean;

/**
 * @author haojinlong
 * 
 */
public interface MortGageAssetAllotDao {
    /**
     * 
     * Description:房贷资产分配-保存
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月17日
     */
    public int saveMortGageAssetAllot(MortGageAssetAllotBean mortGageAssetAllotBean);
}
