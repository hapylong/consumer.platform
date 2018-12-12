/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 下午5:42:00
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.jys.JYSPackInfo;
import com.iqb.consumer.data.layer.dao.jys.JysPackInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class JysPackInfoBiz extends BaseBiz {

    @Resource
    private JysPackInfoDao jysPackInfoDao;

    /**
     * 交易所需求:保存打包信息
     * 
     * @param jysPackInfo
     * @return
     */
    public int insertPackInfo(JYSPackInfo jysPackInfo) {
        setDb(0, super.MASTER);
        return jysPackInfoDao.insertPackInfo(jysPackInfo);
    }
}
