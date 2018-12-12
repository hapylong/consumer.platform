/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 下午5:25:32
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao.jys;

import com.iqb.consumer.data.layer.bean.jys.JYSPackInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface JysPackInfoDao {

    /**
     * 交易所需求:保存打包信息
     * 
     * @param jysPackInfo
     * @return
     */
    int insertPackInfo(JYSPackInfo jysPackInfo);
}
