/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月14日 下午4:15:17
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.jys.JYSDictInfo;
import com.iqb.consumer.data.layer.dao.jys.JysDictInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 交易所需求:字典表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class JysDictInfoBiz extends BaseBiz {

    @Resource
    private JysDictInfoDao<JYSDictInfo> jysDictInfoDao;

    /**
     * 交易所需求:查询摘牌机构
     * 
     * @param jysPackInfo
     * @return
     */
    public List<JYSDictInfo> queryDictByKey(String key) {
        setDb(0, super.SLAVE);
        return jysDictInfoDao.queryDictByKey(key);
    }

}
