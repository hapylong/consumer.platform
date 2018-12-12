/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.downpayment;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.downpayment.DownPaymentBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface DownPaymentService {

    /**
     * 根据订单号、客户姓名、手机号、门店名称（中文）、资金来源、 资金端上标ID号、开始时间、 结束时间查询首付款列表
     * 
     * @param
     */
    PageInfo<DownPaymentBean> getDownPaymentList(JSONObject objs);

    /**
     * 根据订单号、客户姓名、手机号、门店名称（中文）、资金来源、 资金端上标ID号、开始时间、 结束时间导出首付款列表
     * 
     * @param
     */
    String getDownPaymentListSave(JSONObject objs, HttpServletResponse response);
}
