/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月12日 下午2:22:42
 * @version V1.0
 */
package com.iqb.consumer.service.layer.credit;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.creditorinfo.InstAssetStockPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.HouseBillQueryResponsePojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.InstAssetStockBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.finance.FinanceManager;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.order.InstAssetStockBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.reqmoney.RequestMoneyBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.ServiceParaConfig;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.dto.repay.ShouldDebtDetailDto;
import com.iqb.consumer.service.layer.page.PageMsg;
import com.iqb.etep.common.utils.DateTools;

/**
 * @author gxy
 */
@Service
public class CreditServiceImpl extends BaseService implements CreditService {
    protected static Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);
    @Resource
    private ServiceParaConfig serviceParaConfig;
    @Resource
    private MerchantBeanBiz merchantBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private FinanceManager financeManager;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private RequestMoneyBiz RequestMoneyBiz;
    @Autowired
    private PaymentLogBiz paymentLogBiz;
    @Autowired
    private InstAssetStockBiz instAssetStockBiz;

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<String, Object> getShouldDebtDetail(JSONObject objs) {
        Map<String, Object> result = new HashMap<>();
        // 1.获取商户列表
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        // 2.调用账户系统
        Map<String, Object> retMap = sendShouldDebtDetailQuery2Finance(objs, merList);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        BigDecimal totalAmt = (BigDecimal) retMap.get("totalAmt");
        int totalCount = (int) res.get("totalCount");// 总条数
        int numPerPage = (int) res.get("numPerPage");// 每页条数
        int currentPage = (int) res.get("currentPage");// 页码
        PageInfo<Map<String, Object>> pageInfo =
                new PageMsg<>(recordList, 8, currentPage, totalCount, numPerPage);
        result.put("result", pageInfo);
        result.put("totalAmt", totalAmt);
        result.put("totalCount", totalCount);
        return result;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PageInfo<Map<String, Object>> getRepaymentList(JSONObject objs) {
        // 1.获取商户列表
        super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        // 2.调用账户系统
        Map<String, Object> retMap = sendQuery2Finance(objs, merList);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        if (!CollectionUtils.isEmpty(recordList)) {
            for (Map<String, Object> map : recordList) {
                BigDecimal curRealRepayamt = (BigDecimal) map.get("curRealRepayamt");
                int status = (int) map.get("status");
                String orderId = (String) map.get("orderId");
                int repayNo = (int) map.get("repayNo");
                JSONObject json = new JSONObject();
                json.put("orderId", orderId);
                json.put("repayNo", repayNo);
                Integer selSumAmount = 0;
                if (status != 3) {
                    selSumAmount = paymentLogBiz.selSumAmount(json) != null ? paymentLogBiz.selSumAmount(json) : 0;
                }
                BigDecimalUtil.div100(new BigDecimal(selSumAmount));
                curRealRepayamt =
                        BigDecimalUtil.add(curRealRepayamt, BigDecimalUtil.div100(new BigDecimal(selSumAmount)));
                map.put("curRealRepayamt", curRealRepayamt);
            }
        }
        int totalCount = (int) res.get("totalCount");// 总条数
        int numPerPage = (int) res.get("numPerPage");// 每页条数
        int currentPage = (int) res.get("currentPage");// 页码
        PageInfo<Map<String, Object>> pageInfo =
                new PageMsg<>(recordList, 8, currentPage, totalCount, numPerPage);
        return pageInfo;
    }

    @SuppressWarnings("unchecked")
    public List<HouseBillQueryResponsePojo> getHBQRList(JSONObject objs) {
        /** 增加 条件查询 regId 和 realName **/
        super.getMerchLimitObject(objs);
        objs.put("pageNum", "1");
        objs.put("pageSize", "65535");
        // 1.获取商户列表
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        // 2.调用账户系统
        Map<String, Object> retMap = sendQuery2Finance(objs, merList);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        return JSONObject.parseArray(JSONObject.toJSONString(res.get("recordList")), HouseBillQueryResponsePojo.class);
    }

    // 发送账户系统查询应收的明细信息
    private Map<String, Object> sendShouldDebtDetailQuery2Finance(JSONObject objs, List<String> merList) {
        JSONObject params = new JSONObject();
        Map<String, String> dateMap = getStartAndEndDate(objs);
        params.put("merchantNos", merList);
        params.put("regId", objs.get("regId"));
        params.put("orderId", objs.get("orderId"));
        params.put("startDate", dateMap.get("startDate"));
        params.put("endDate", dateMap.get("endDate"));
        params.put("realName", objs.getString("realName"));
        params.put("fundId", objs.getString("fundId"));
        params.put("sourcesFunding", objs.getString("sourcesFunding"));
        params.put("status", "".equals(objs.getString("status")) ? null : objs.get("status"));
        params.put("numPerPage", objs.get("pageSize"));
        params.put("pageNum", objs.get("pageNum"));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillShouldDeptDetailUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    // 发送账户系统查询代偿信息
    private Map<String, Object> sendQuery2Finance(JSONObject objs, List<String> merList) {
        JSONObject params = new JSONObject();
        Map<String, String> dateMap = getStartAndEndDate(objs);
        params.put("merchantNos", merList);
        params.put("realName", objs.get("realName"));
        params.put("regId", objs.get("regId"));
        params.put("orderId", objs.get("orderId"));
        params.put("lastRepayDate", objs.get("lastRepayDate"));
        params.put("openId", objs.get("openId"));
        // 2017年12月12日 11:54:16 chengzhen 将开始时间结束时间不设为必填项
        params.put("startDate", dateMap.get("startDate"));
        params.put("endDate", dateMap.get("endDate"));
        params.put("curRepayDate", dateMap.get("curRepayDate"));// 在账单状态列之前，增加“还款日期”字段
        params.put("curRepayDateBegin", dateMap.get("curRepayDateBegin"));// 在账单状态列之前，增加“还款日期”字段
        params.put("curRepayDateEnd", dateMap.get("curRepayDateEnd"));// 在账单状态列之前，增加“还款日期”字段
        params.put("status", objs.get("status"));
        params.put("numPerPage", objs.get("pageSize"));
        params.put("pageNum", objs.get("pageNum"));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillQueryUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    private Map<String, String> getStartAndEndDate(JSONObject objs) {
        Map<String, String> result = new HashMap<>();
        String startDate = objs.getString("startDate");
        String endDate = objs.getString("endDate");
        String curRepayDate = objs.getString("curRepayDate");
        String curRepayDateBegin = objs.getString("curRepayDateBegin");
        String curRepayDateEnd = objs.getString("curRepayDateEnd");
        if (startDate == null || "".equals(startDate)) {
            startDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {
            Date startTime = DateUtil.parseDate(startDate, DateUtil.SHORT_DATE_FORMAT);
            startDate = DateUtil.getDateString(startTime, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (endDate == null || "".equals(endDate)) {
            endDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {
            Date endTime = DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT);
            endDate = DateUtil.getDateString(endTime, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (curRepayDate != null && !"".equals(curRepayDate)) {
            Date crd = DateUtil.parseDate(curRepayDate, DateUtil.SHORT_DATE_FORMAT);
            curRepayDate = DateUtil.getDateString(crd, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (curRepayDateBegin != null && !"".equals(curRepayDateBegin)) {
            Date crd = DateUtil.parseDate(curRepayDateBegin, DateUtil.SHORT_DATE_FORMAT);
            curRepayDateBegin = DateUtil.getDateString(crd, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (curRepayDateEnd != null && !"".equals(curRepayDateEnd)) {
            Date crd = DateUtil.parseDate(curRepayDateEnd, DateUtil.SHORT_DATE_FORMAT);
            curRepayDateEnd = DateUtil.getDateString(crd, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (("".equals(curRepayDateBegin) || curRepayDateBegin == null)
                && ("".equals(curRepayDateEnd) || curRepayDateEnd == null)) {
            result.put("startDate", startDate);
            result.put("endDate", endDate);
        }else{
            result.put("startDate", objs.getString("startDate"));
            result.put("endDate", objs.getString("endDate"));
        }
        result.put("curRepayDate", curRepayDate);
        result.put("curRepayDateBegin", curRepayDateBegin);
        result.put("curRepayDateEnd", curRepayDateEnd);
        return result;
    }

    @Override
    public String exportShouldDebtDetailList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户列表
            Map<String, Object> retMap = this.getShouldDebtDetail2(objs);
            List<ShouldDebtDetailDto> list =
                    JSONArray.parseArray(retMap.get("result").toString(), ShouldDebtDetailDto.class);
            BigDecimal totalAmt = (BigDecimal) retMap.get("totalAmt");
            // 2.导出excel表格
            HSSFWorkbook workbook = this.exportShouldDebtDetailList(list, totalAmt);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=DebtDetail-" + DateTools.getYmdhmsTime() + ".xls";
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

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getShouldDebtDetail2(JSONObject objs) {
        // 1.获取商户列表
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        // 2.调用账户系统
        Map<String, Object> retMap = sendShouldDebtDetailQuery2Finance2(objs, merList);
        return retMap;
    }

    // 发送账户系统查询应收的明细信息
    private Map<String, Object> sendShouldDebtDetailQuery2Finance2(JSONObject objs, List<String> merList) {
        JSONObject params = new JSONObject();
        Map<String, String> dateMap = getStartAndEndDate(objs);
        params.put("merchantNos", merList);
        params.put("regId", objs.get("regId"));
        params.put("orderId", objs.get("orderId"));
        params.put("startDate", dateMap.get("startDate"));
        params.put("endDate", dateMap.get("endDate"));
        params.put("realName", objs.getString("realName"));
        params.put("fundId", objs.getString("fundId"));
        params.put("sourcesFunding", objs.getString("sourcesFunding"));
        params.put("status", "".equals(objs.getString("status")) ? null : objs.get("status"));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillShouldDeptDetail2Url(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    // 导出
    private HSSFWorkbook exportShouldDebtDetailList(List<ShouldDebtDetailDto> list, BigDecimal totalAmt) {
        String[] excelHeader =
        {"序号", "订单号", "客户姓名", "手机号", "身份证号码", "门店名称", "资金来源", "资金端上标ID号", "订单金额",
                "总期数", "本期期数", "月供本金", "月供利息", "月供", "应还金额", "逾期天数", "罚滞金额", "还款金额", "最迟还款日",
                "实际还款日期", "还款状态", "当前还款银行", "当前还款账户", "还款方式"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("应付明细信息页");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            ShouldDebtDetailDto sdd = list.get(i);
            row.createCell(0).setCellValue(i + 1);// 序号
            row.createCell(1).setCellValue(sdd.getOrderId());// 订单号
            row.createCell(2).setCellValue(sdd.getRealName());// 客户姓名
            row.createCell(3).setCellValue(sdd.getRegId());// 手机号
            row.createCell(4).setCellValue(sdd.getIdNo());// 身份证号码
            row.createCell(5).setCellValue(sdd.getMerchantNo());// 门店名称
            row.createCell(6).setCellValue(sdd.getSourcesFunding());// 资金来源
            row.createCell(7).setCellValue(sdd.getFundId());// 资金端上标ID号
            row.createCell(8).setCellValue(
                    sdd.getContractAmt() == null ? 0 : sdd.getContractAmt().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .doubleValue());// 合同金额
            row.createCell(9).setCellValue(sdd.getInstallTerms());// 总期数
            row.createCell(10).setCellValue(sdd.getRepayNo());// 本期期数
            row.createCell(11).setCellValue(
                    sdd.getCurRepayPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); // 月供本金
            row.createCell(12).setCellValue(
                    sdd.getCurRepayInterest().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 月供利息
            row.createCell(13).setCellValue(sdd.getRealPayAmt().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 月供
            row.createCell(14).setCellValue(sdd.getCurRepayAmt().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 应还金额
            row.createCell(15).setCellValue(sdd.getOverdueDays());// 逾期天数
            row.createCell(16).setCellValue(
                    sdd.getCurRepayOverdueInterest().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 罚滞金额
            row.createCell(17).setCellValue(
                    sdd.getCurRealRepayAmt().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 还款金额
            HSSFCell cell = row.createCell(18);
            cell.setCellValue(sdd.getLastRepayDate());
            cell.setCellStyle(style);// 最迟还款日
            cell = row.createCell(19);
            if (sdd.getCurRepayDate() != null) {
                cell.setCellValue(sdd.getCurRepayDate());
                cell.setCellStyle(style);// 实际还款日期
            }
            row.createCell(20).setCellValue(this.getRepayStatus(sdd.getStatus()));// 还款状态
            row.createCell(21).setCellValue(sdd.getBankName());// 当前还款银行
            row.createCell(22).setCellValue(sdd.getBankCardNo());// 当前还款账户
            row.createCell(23).setCellValue(this.getRepayType(sdd.getRepayType()));// 还款方式
        }
        row = sheet.createRow(list.size() + 4);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("累计");
        row = sheet.createRow(list.size() + 5);
        cell = row.createCell(0);
        cell.setCellValue(totalAmt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        // 设置列宽
        for (int j = 0; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    // 获取还款方式
    // 11.用户支付预付款 12.商户代偿预付款 13.线下平账预付款 21.用户支付分期还款 22.商户代偿分期还款23.线下平账分期付款
    private String getRepayType(String repayType) {
        String result = "";
        if (repayType == null) {
            return result;
        }
        switch (repayType) {
            case "21":
                result = "客户自还";
                break;
            case "22":
                result = "门店代偿";
                break;
            case "23":
                result = "线下平账";
                break;
        }
        return result;
    }

    // 获取还款状态
    private String getRepayStatus(int status) {
        String result = null;
        switch (status) {
            case 0:
                result = "已逾期";
                break;
            case 1:
                result = "未还款";
                break;
            case 2:
                result = "部分还款";
                break;
            case 3:
                result = "已还款";
                break;
        }
        return result;
    }

    /*
     * History:资产存量报表已还本金 未还本金更改 默认商户为全部时,去掉in查询
     * 
     * @author haojinlong 2017-06-13
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> listStockStatisticsPage(JSONObject objs) {
        // 1.获取商户列表
        objs.put("id", "1");
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (int i = 0; i < merchantList.size(); i++) {
                merList.add(merchantList.get(i).getMerchantNo());
            }
        }
        // 2.调用账户系统
        Map<String, Object> retMap = this.sendListStockStatisticsPageQuery2Finance(objs, merList);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");

        InstAssetStockPojo instAssetStockPojo =
                JSONObject.toJavaObject(JSONObject.parseObject(res.toString()), InstAssetStockPojo.class);
        List<InstAssetStockBean> recordList = null;
        recordList = this.calculateStockStatisticsInfo(instAssetStockPojo.getRecordList());// 计算剩余本金返回后的list
        res.put("recordList", recordList);
        retMap.put("result", res);
        return retMap;
    }

    // 处理账户返回信息返回list
    /**
     * History:资产存量报表已还本金 未还本金更改
     * 
     * @author haojinlong 2017-06-13
     */
    private List<InstAssetStockBean> calculateStockStatisticsInfo(List<InstAssetStockBean> recordList) {
        logger.info("-----recordList----{}", JSONObject.toJSONString(recordList));
        if (recordList == null) {
            return null;
        }
        try {
            // 获取所有商户
            JSONObject objs = new JSONObject();
            objs.put("id", 1);
            List<MerchantBean> allList = merchantBiz.getAllMerByID(objs);
            // 处理List
            for (InstAssetStockBean bean : recordList) {
                String merchantNo = bean.getMerchantNo();
                // 获取商户名和父商户名并添加
                Iterator<MerchantBean> iterator = allList.iterator();
                while (iterator.hasNext()) {
                    MerchantBean mb = iterator.next();
                    if (mb.getMerchantNo().equals(merchantNo)) {
                        String merchantShortName = mb.getMerchantShortName();
                        // 显示父商户名
                        String parentMerName = this.getParentMerName(mb);
                        bean.setMerchantShortName(merchantShortName);
                        bean.setParentMerName(parentMerName);
                    }
                }
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", bean.getOrderId());
                OrderBean orderBean = orderBiz.selectOne(params);
                logger.info("-----orderBean----{}", JSONObject.toJSONString(orderBean));
                // 获取资金来源
                RequestMoneyBean requestBean = RequestMoneyBiz.getRequestMoneyByOrderId(params);
                if (requestBean != null) {
                    bean.setSourcesFunding(requestBean.getSourcesFunding());
                    bean.setFundId(requestBean.getPushId() == 0 ? "" : String.valueOf(requestBean.getPushId()));
                }
                // 添加未还本、首付金额
                BigDecimal contractAmt =
                        new BigDecimal(orderBean == null ? "0.0" : orderBean.getOrderAmt());// 借款总额
                /*
                 * BigDecimal contractAmt = (BigDecimal) map.get("contractAmt") == null ?
                 * BigDecimal.ZERO : (BigDecimal) map .get("contractAmt");// 借款总额
                 */BigDecimal installSumAmt =
                        bean.getInstallSumAmt() == null ? BigDecimal.ZERO : bean.getInstallSumAmt();// 应还总额
                BigDecimal curRepayPrincipal =
                        bean.getCurRepayPrincipal() == null ? BigDecimal.ZERO : bean.getCurRepayPrincipal();// 已还本金
                BigDecimal remainPrincipal =
                        bean.getRemainPrincipal() == null ? BigDecimal.ZERO : bean.getRemainPrincipal();// 未还本金
                BigDecimal preAmt = BigDecimal.ZERO;// 首付金额

                // 首付金额 = 借款总额 - 应还总额
                preAmt = contractAmt.subtract(installSumAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
                bean.setContractAmt(contractAmt);
                bean.setPreAmt(preAmt);
                bean.setRemainPrincipal(remainPrincipal);
                bean.setCurRepayPrincipal(curRepayPrincipal);
            }
        } catch (Exception e) {
            logger.error("处理账户系统返回的存量报表数据异常:{}", e);
        }
        return recordList;
    }

    private String getParentMerName(MerchantBean mb) {
        String result = null;
        switch (mb.getParentId()) {
            case "1006":
                result = "消费金融";
                break;
            case "1019":
                result = "爱车帮";
                break;
            case "1020":
                result = "惠淘车";
                break;
            case "1021":
                result = "医美";
                break;
            default:
                result = mb.getMerchantShortName();
                break;
        }
        return result;
    }

    private Map<String, Object> sendListStockStatisticsPageQuery2Finance(JSONObject objs, List<String> merList) {
        JSONObject params = new JSONObject();
        params.put("merchantNos", merList);
        params.put("regId", objs.get("regId"));
        params.put("orderId", objs.get("orderId"));
        params.put("curRepayDate", objs.get("curRepayDate"));
        params.put("numPerPage", objs.get("pageSize"));
        params.put("pageNum", objs.get("pageNum"));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillStockStatisticsPageUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> listStockStatistics(JSONObject objs) {
        // 1.获取商户列表
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<>();
        if (merchantList != null) {
            for (int i = 0; i < merchantList.size(); i++) {
                merList.add(merchantList.get(i).getMerchantNo());
            }
        }
        // 2.调用账户系统
        Map<String, Object> retMap = this.sendListStockStatisticsQuery2Finance(objs, merList);
        return retMap;
    }

    @Override
    public String exportStockStatisticsList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户列表
            objs = super.getMerchLimitObject(objs);
            objs.put("isPageHelper", true);
            List<InstAssetStockBean> list = instAssetStockBiz.selInstAssetStockInfo(objs);

            // 2.导出excel表格
            HSSFWorkbook workbook = this.exportStockStatisticsList(list);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=StockStatistics-" + DateTools.getYmdhmsTime() + ".xls";
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

    private HSSFWorkbook exportStockStatisticsList(List<InstAssetStockBean> list) {
        String[] excelHeader =
        {"序号", "订单号", "客户姓名", "手机号", "资产所属体系", "机构名称", "资金来源", "资金端上标ID号", "总期数",
                "已还期数", "未还期数", "首付金额", "借款总额", "应还总额", "已还本金", "未还本金"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("存量报表页");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            InstAssetStockBean ssd = list.get(i);
            row.createCell(0).setCellValue(i + 1);// 序号
            row.createCell(1).setCellValue(ssd.getOrderId());// 订单号
            row.createCell(2).setCellValue(ssd.getRealName());// 客户姓名
            row.createCell(3).setCellValue(ssd.getRegId());// 手机号
            row.createCell(4).setCellValue(ssd.getParentMerName());// 资产所属体系
            row.createCell(5).setCellValue(ssd.getMerchantShortName());// 门店名称
            row.createCell(6).setCellValue(ssd.getSourcesFunding());// 资金来源
            row.createCell(7).setCellValue(ssd.getFundId());// 资金端上标ID号
            row.createCell(8).setCellValue(ssd.getInstallTerms());// 总期数
            row.createCell(9).setCellValue(ssd.getRepayNo());// 已还期数
            row.createCell(10).setCellValue(ssd.getNonRepayno());// 未还期数
            logger.debug("---{}--ssd.getPreAmt()---{}-----------", ssd.getOrderId(), ssd.getPreAmt());
            row.createCell(11).setCellValue(null == ssd.getPreAmt()
                    ? null
                    : ssd.getPreAmt().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 首付金额
            row.createCell(12).setCellValue(
                    ssd.getContractAmt() == null ? 0 : ssd.getContractAmt().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .doubleValue());// 借款总额

            row.createCell(13).setCellValue(
                    ssd.getInstallSumAmt() == null ? 0.00 : ssd.getInstallSumAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); // 应还总额
            row.createCell(14).setCellValue(ssd.getCurRepayPrincipal() == null ? 0 :
                    ssd.getCurRepayPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); // 应还本金
            row.createCell(15).setCellValue(
                    ssd.getRemainPrincipal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); // 未还本金
        }
        // 设置列宽
        for (int j = 0; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    private Map<String, Object> sendListStockStatisticsQuery2Finance(JSONObject objs, List<String> merList) {
        JSONObject params = new JSONObject();
        params.put("merchantNos", merList);
        params.put("regId", objs.get("regId"));
        params.put("orderId", objs.get("orderId"));
        params.put("curRepayDate", objs.get("curRepayDate"));
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillStockStatisticsUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    @Override
    public String queryBillExportXlsx(JSONObject bqr, HttpServletResponse response) {
        List<HouseBillQueryResponsePojo> xlsx = new ArrayList<>();
        try {
            xlsx = getHBQRList(bqr);
        } catch (Exception e) {
            logger.error("queryBillHandlerExportXlsx convert list error:", e);
            return "fail";
        }
        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = financeManager.convertCSIXlsx(xlsx);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:", e);
            return "fail";
        }
        return "success";
    }

    /**
     * 资产存量分页查询
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月2日
     */
    @Override
    public PageInfo<InstAssetStockBean> listStockStatisticsPageNew(JSONObject objs) {
        // 获取商户列表
        objs = super.getMerchLimitObject(objs);
        List<InstAssetStockBean> assetList = instAssetStockBiz.selInstAssetStockInfo(objs);
        return new PageInfo<>(assetList);
    }
}
