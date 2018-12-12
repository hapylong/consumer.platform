/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午4:01:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.merchant;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBizTypeBean;
import com.iqb.consumer.data.layer.dao.MerchantBizTypeDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Repository
public class MerchantBizTypeBiz extends BaseBiz {

    @Resource
    private MerchantBizTypeDao merchantBizTypeDao;

    public List<MerchantBizTypeBean> getMerchantBizTypeList(JSONObject objs) {
        setDb(0, super.SLAVE);
        return merchantBizTypeDao.getMerchantBizTypeList(objs);
    }

    /**
     * 同步中阁系统接口数据到（inst_merchantbiztype）表中
     * <p>
     * 记录帮帮手业务类型
     * </p>
     * <p>
     * (2001 以租代售新车 2002以租代售二手车 2100 抵押车 2200 质押车 1100 易安家 1000 医美 1200 旅游)
     * </p>
     * 
     * @param 同步数据 by 中阁系统接口
     *        <p>
     *        接口调用方：com.iqb.consumer.crm.customer.service.impl.pushCustomerInfoToXFJR();
     * @return
     */
    public int insertMerchantBizTypeBySync(JSONObject objs) {
        setDb(0, super.MASTER);
        return merchantBizTypeDao.insertMerchantBizTypeBySync(objs);
    }

    public int delMerchantBizType(JSONObject objs) {
        setDb(0, super.MASTER);
        return merchantBizTypeDao.delMerchantBizType(objs);
    }
}
