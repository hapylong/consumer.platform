/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 抵押车业务分成实现类
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.service.layer.query.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderManageResult;
import com.iqb.consumer.data.layer.bean.query.MortgagecarBean;
import com.iqb.consumer.data.layer.biz.query.MortgagecarBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.page.PageMsg;
import com.iqb.consumer.service.layer.query.MortgagecarService;
import com.iqb.etep.common.utils.DateTools;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
@Service
public class MortgagecarServiceImpl extends BaseService implements MortgagecarService {
    private static Logger logger = LoggerFactory.getLogger(MortgagecarServiceImpl.class);

    @Autowired
    private MortgagecarBiz mortgagecarBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;

    /**
     * 获取所有符合条件的抵押车业务分成信息
     * 
     * @param objs
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<Map<String, Object>> selectMortgagecatList(JSONObject objs) {
        logger.debug("IQB信息---【服务层】获取抵押车业务分成信息，开始...");

        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }

        List<MortgagecarBean> list = mortgagecarBiz.selectMortgagecatList(objs);
        Map<String, Object> retMap = this.queryBillByParamsForMortgage2Finance(objs);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");

        if (res == null) {
            return null;
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        List<Map<String, Object>> returnList = this.mergeBillInfoList(list, recordList);

        int totalCount = (int) res.get("totalCount"); // 总条数
        int numPerPage = (int) res.get("numPerPage"); // 每页条数
        int currentPage = (int) res.get("currentPage"); // 页码
        // int pageCount = (int) res.get("pageCount"); // 总页数
        PageInfo<Map<String, Object>> pageInfo =
                new PageMsg<Map<String, Object>>(returnList, 8, currentPage, totalCount, numPerPage);

        logger.debug("IQB信息---【服务层】获取抵押车业务分成信息，完成...");
        return pageInfo;
    }

    /**
     * 调用接口从账务系统中获取抵押车订单明细信息
     * 
     * @param objs
     * @return
     */
    private Map<String, Object> queryBillByParamsForMortgage2Finance(JSONObject objs) {
        JSONObject params = new JSONObject();
        params.put("regId", objs.get("regId"));
        params.put("lastRepayDate", objs.get("lastRepayDate"));
        params.put("merList", objs.get("merList"));
        params.put("orderId", objs.get("orderId"));
        params.put("status", objs.get("status"));
        params.put("realName", objs.get("realName"));
        params.put("numPerPage", objs.get("pageSize"));
        params.put("pageNum", objs.get("pageNum"));
        logger.info("IQB信息---【服务层】调用接口从账务系统中获取抵押车订单明细信息..." + params);
        logger.info("IQB信息---【服务层】调用接口从账务系统中访问地址:{}...", consumerConfig.getFinanceBillMortgageStatisticsUrl());
        logger.info("IQB信息---【服务层】调用接口从账务系统中获取加密：{}...", encryptUtils.encrypt(params));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillMortgageStatisticsUrl(),
                encryptUtils.encrypt(params));
        logger.info("IQB信息---【服务层】调用接口从账务系统中获取抵押车订单明细信息..." + resultStr);
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);

        return retMap;
    }

    /**
     * 将从财务系统中返回的抵押车订单明细与订单信息进行合并
     * 
     * @param list
     * @param recordList
     * @return
     */
    private List<Map<String, Object>> mergeBillInfoList(List<MortgagecarBean> list, List<Map<String, Object>> recordList) {
        List<Map<String, Object>> returnList = new ArrayList<>(recordList.size());
        Map<String, Object> map = null;
        if (recordList != null && recordList.size() > 0) {

            for (Map<String, Object> recordMap : recordList) {
                map = new LinkedHashMap<String, Object>();

                String orderId = (String) recordMap.get("orderId");
                String realName = (String) recordMap.get("realName");
                Integer repayNo = (Integer) recordMap.get("repayNo");
                Integer status = (Integer) recordMap.get("status");

                Long lastRepayDate = (Long) recordMap.get("lastRepayDate");
                String merchantNo = (String) recordMap.get("merchantNo");
                String regId = (String) recordMap.get("regId");

                for (MortgagecarBean bean : list) {
                    if (bean.getOrderId() != null && bean.getOrderId().equals(orderId)) {

                        map.put("orderId", orderId);
                        map.put("realName", realName);
                        map.put("repayNo", repayNo);
                        map.put("status", status);
                        map.put("merchantNo", merchantNo);

                        map.put("feeratio", bean.getFeeratio());
                        map.put("feeTotal", bean.getFeeTotal());
                        map.put("gpsTrafficFee", bean.getGpsTrafficFee());
                        map.put("interestDiff", bean.getInterestDiff());
                        map.put("lastRepayDate", lastRepayDate);

                        map.put("monthInterest", bean.getMonthInterest());
                        map.put("orderAmt", bean.getOrderAmt());
                        map.put("orderItems", bean.getOrderItems());
                        map.put("serviceCharge", bean.getServiceCharge());
                        map.put("applyTime", bean.getApplyTime());

                        map.put("merchantName", bean.getMerchantName());
                        map.put("regId", regId);
                        returnList.add(map);
                    }
                }

            }
        }

        return returnList;
    }

    /**
     * 抵押车业务分成报表数据导出
     */
    @SuppressWarnings("unchecked")
    @Override
    public String exportMortgageList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            objs = getMerchLimitObject(objs);
            if (objs == null) {
                return null;
            }
            List<MortgagecarBean> list = mortgagecarBiz.selectMortgagecatList(objs);
            Map<String, Object> retMap = this.queryBillByParamsForMortgage2Finance(objs);
            Map<String, Object> res = (Map<String, Object>) retMap.get("result");

            if (res == null) {
                return null;
            }
            List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
            List<Map<String, Object>> returnList = this.mergeBillInfoList(list, recordList);

            // 2.导出excel表格
            HSSFWorkbook workbook = exportOrderList(returnList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    private HSSFWorkbook exportOrderList(List<Map<String, Object>> list) {
        String[] excelHeader =
        {"订单号", "姓名", "电话", "借款金额", "月利率", "月供", "GPS流量费", "期数", "车帮服务费", "商户息差", "费用总额", "上标日期", "总期数",
                "最迟还款日", "还款状态", "商户名称"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单信息页");

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle amtStyle = wb.createCellStyle(); // 货币样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        amtStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            MortgagecarBean orderBean = (MortgagecarBean) list.get(i);

            row.createCell(0).setCellValue(orderBean.getOrderId());
            row.createCell(1).setCellValue(orderBean.getRealName());
            row.createCell(2).setCellValue(orderBean.getRegId());
            row.createCell(3).setCellValue(orderBean.getOrderAmt().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(4).setCellValue(orderBean.getFeeratio());
            row.createCell(5).setCellValue(
                    orderBean.getMonthInterest().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(6).setCellValue(orderBean.getOrderItems());
            row.createCell(7).setCellValue(
                    orderBean.getGpsTrafficFee().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(8).setCellValue(orderBean.getOrderItems());
            row.createCell(9).setCellValue(
                    orderBean.getServiceCharge().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(
                    orderBean.getInterestDiff().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(11).setCellValue(orderBean.getFeeTotal()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            String sTranTime = "";
            if (null != orderBean.getApplyTime()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                sTranTime = sdf.format(orderBean.getApplyTime());
            }
            row.createCell(12).setCellValue(sTranTime);
            row.createCell(12).setCellValue(orderBean.getOrderItems());

            String lastrepayDate = "";
            if (null != orderBean.getLastRepayDate()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                lastrepayDate = sdf.format(orderBean.getLastRepayDate());
            }

            row.createCell(13).setCellValue(lastrepayDate);
            Integer status = orderBean.getStatus();
            String statusName = "";
            if (status == 0) {
                statusName = "逾期";
            } else if (status == 1) {
                statusName = "未还款";
            } else if (status == 2) {
                statusName = "部分还款";
            } else if (status == 3) {
                statusName = "已还款";
            }
            row.createCell(14).setCellValue(statusName);
            row.createCell(15).setCellValue(orderBean.getMerchantName());

        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        sheet.setColumnWidth(3, 10000);// 订单名称
        sheet.setColumnWidth(4, 4000);// 订单时间
        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }

        return wb;
    }
}
