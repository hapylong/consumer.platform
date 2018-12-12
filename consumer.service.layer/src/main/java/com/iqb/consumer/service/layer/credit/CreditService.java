/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午2:22:00
 * @version V1.0
 */
package com.iqb.consumer.service.layer.credit;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.InstAssetStockBean;

/**
 * @author gxy
 */
public interface CreditService {

    // 获取逾期查询(分页)
    PageInfo<Map<String, Object>> getRepaymentList(JSONObject objs);

    // 获取财务应收明细查询(分页)
    Map<String, Object> getShouldDebtDetail(JSONObject objs);

    // 导出财务应收明细报表
    public String exportShouldDebtDetailList(JSONObject objs, HttpServletResponse response);

    // 获取财务应收明细查询(不分页)
    public Map<String, Object> getShouldDebtDetail2(JSONObject objs);

    // 获取存量报表数据查询(分页)
    Map<String, Object> listStockStatisticsPage(JSONObject objs);

    // 获取存量报表数据查询(不分页)
    Map<String, Object> listStockStatistics(JSONObject objs);

    // 导出存量报表数据
    String exportStockStatisticsList(JSONObject objs, HttpServletResponse response);

    Object queryBillExportXlsx(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * 资产存量分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    PageInfo<InstAssetStockBean> listStockStatisticsPageNew(JSONObject objs);
}
