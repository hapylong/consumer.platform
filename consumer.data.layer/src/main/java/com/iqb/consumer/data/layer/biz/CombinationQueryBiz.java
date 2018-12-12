/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 上午10:26:01
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.data.layer.dao.CombinationQueryDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class CombinationQueryBiz extends BaseBiz {

    @Resource
    private CombinationQueryDao combinationQueryDao;

    public CombinationQueryBean getByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        CombinationQueryBean cqb = combinationQueryDao.getByOrderId(orderId);
        return cqb;
    }
}
