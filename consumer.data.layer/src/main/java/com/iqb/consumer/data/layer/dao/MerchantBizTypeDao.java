/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:47:11
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBizTypeBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface MerchantBizTypeDao {

    /**
     * 根据商户号查询业务类型列表
     * 
     * @param
     * @return
     */
    List<MerchantBizTypeBean> getMerchantBizTypeList(JSONObject objs);

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
    public int insertMerchantBizTypeBySync(JSONObject objs);

    int delMerchantBizType(JSONObject objs);
}
