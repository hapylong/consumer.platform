/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:47:11
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.downpayment.DownPaymentBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface DownPaymentDao {

    /**
     * 根据订单号、客户姓名、手机号、门店名称（中文）、资金来源、 资金端上标ID号、开始时间、 结束时间查询首付款列表
     * 
     * @param
     * @return
     */
    List<DownPaymentBean> getDownPaymentList(JSONObject objs);
}
