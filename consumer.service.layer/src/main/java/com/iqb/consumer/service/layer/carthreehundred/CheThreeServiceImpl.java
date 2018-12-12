package com.iqb.consumer.service.layer.carthreehundred;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.carthreehundred.CheThreeHBean;
import com.iqb.consumer.data.layer.bean.carthreehundred.CheThreeHPojo;
import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskBean;
import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskResultBean;
import com.iqb.consumer.data.layer.biz.carthreehundred.CheThreeHBiz;
import com.iqb.consumer.data.layer.biz.carthreehundred.InstOrderloanRiskBiz;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年8月7日下午6:46:06 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class CheThreeServiceImpl implements CheThreeService {
    protected static final Logger logger = LoggerFactory.getLogger(CheThreeServiceImpl.class);
    private String token = "token";
    private String oper = "oper";
    private String status = "status";
    private String message = "message";
    private String error_msg = "error_msg";
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private CheThreeHBiz cheThreeHBiz;
    @Autowired
    private InstOrderloanRiskBiz instOrderloanRiskBiz;
    @Resource
    private ConsumerConfig consumerConfig;

    /**
     * 
     * Description:发起贷前反欺诈查询请求
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    @Override
    public Map<String, String> requestPreLoanAntiFraud(JSONObject objs) {
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        // 根据订单号获取订单相关信息
        CheThreeHBean cheThreeHBean = getOrderInfoByOrderId(objs.getString("orderId"));
        if (cheThreeHBean != null) {
            // 借款人姓名
            paramsMap.put("borrower_name", cheThreeHBean.getRealName());
            // 借款人身份证号
            paramsMap.put("borrower_id", cheThreeHBean.getIdNo());
            // 借款人手机号码
            paramsMap.put("borrower_tel", cheThreeHBean.getRegId());
            // 借款人银行卡号
            paramsMap.put("borrower_bank_id", cheThreeHBean.getBankCardNo());
            // 贷款类型
            paramsMap.put("loan_type", cheThreeHBean.getBizType());
            // 借款城市
            paramsMap.put("apply_city", "");
            if (cheThreeHBean.getBizType().equals("2001")) {
                // 贷款车辆类型
                paramsMap.put("car_model_name", cheThreeHBean.getOrderName());
                // 贷款车辆指导价
                paramsMap.put("car_msrp", cheThreeHBean.getOrderAmt().toBigInteger().toString());
            } else {
                // 车架号
                paramsMap.put("vin", cheThreeHBean.getCarNo());
                // 发动机号
                paramsMap.put("engine_no", cheThreeHBean.getEngine());
                // 车牌号
                paramsMap.put("car_no", cheThreeHBean.getPlate());
                // 初次上牌时间
                paramsMap.put("reg_date", cheThreeHBean.getFirstRegDate());
                // 贷款车辆指导价
                paramsMap.put("mile", String.valueOf(cheThreeHBean.getMileage()));
                // 贷款车辆指导价
                paramsMap.put("car_msrp", cheThreeHBean.getOrderAmt().toBigInteger().toString());
            }

        }
        paramsMap.put(token, urlConfig.getCarThreeRiskToken());
        paramsMap.put(oper, "requestPreLoanAntiFraud");
        String httpPost = "";
        try {
            httpPost =
                    SimpleHttpUtils.httpPost(urlConfig.getCarThreeRiskUrl(), paramsMap);
        } catch (Exception e) {
            logger.error("---调用车300接口requestPreLoanAntiFraud报错,错误信息---{}", e);
        }
        if (!StringUtil.isNull(httpPost) && JSON.parseObject(httpPost) != null) {
            JSONObject jsonObj = JSON.parseObject(httpPost);
            resultMap.put(status, String.valueOf(jsonObj.getIntValue(status)));
            resultMap.put(message, jsonObj.getString(error_msg));
        }
        return resultMap;
    }

    /**
     * 
     * Description:检查贷前反欺诈查询结果
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    @Override
    public Map<String, String> checkPreLoanAntiFraudResult(JSONObject objs) {
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(token, urlConfig.getCarThreeRiskToken());
        paramsMap.put(oper, "checkPreLoanAntiFraudResult");
        String httpPost = "";
        try {
            httpPost =
                    SimpleHttpUtils.httpPost(urlConfig.getCarThreeRiskUrl(), paramsMap);
        } catch (Exception e) {
            logger.error("---调用车300接口checkPreLoanAntiFraudResult报错,错误信息---{}", e);
        }
        if (!StringUtil.isNull(httpPost) && JSON.parseObject(httpPost) != null) {
            JSONObject jsonObj = JSON.parseObject(httpPost);
            resultMap.put(status, String.valueOf(jsonObj.getIntValue(status)));
            resultMap.put(message, jsonObj.getString(error_msg));
        }
        return resultMap;
    }

    /**
     * 
     * Description:注册贷后监控车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    @Override
    public Map<String, String> registerPostLoanMonitorCar(JSONObject objs) {
        Map<String, String> resultMap = new HashMap<>();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(token, urlConfig.getCarThreeRiskToken());
        paramsMap.put(oper, "registerPostLoanMonitorCar");
        CheThreeHPojo chePojo = JSONObject.toJavaObject(objs, CheThreeHPojo.class);
        if (!CollectionUtils.isEmpty(chePojo.getOrderIds())) {
            for (String orderId : chePojo.getOrderIds()) {
                // 根据订单号获取订单相关信息
                CheThreeHBean cheThreeHBean = getOrderInfoByOrderId(orderId);
                if (cheThreeHBean != null) {
                    InstOrderloanRiskBean bean =
                            instOrderloanRiskBiz.getInstOrderloanRiskInfoByOrderId(orderId);
                    if (bean == null) {
                        bean = new InstOrderloanRiskBean();
                        bean.setOrderId(orderId);
                        bean.setSendStatus("1");

                        instOrderloanRiskBiz.saveInstOrderloanRiskInfo(bean);
                    }
                    // 车架号
                    paramsMap.put("vin", cheThreeHBean.getCarNo());
                    // 发动机号
                    paramsMap.put("engine_no", cheThreeHBean.getEngine());
                    // 车牌号
                    paramsMap.put("car_no", cheThreeHBean.getPlate());
                    // 贷款车辆类型
                    paramsMap.put("car_model_name", cheThreeHBean.getOrderName());
                    // 贷款车辆指导价
                    paramsMap.put("car_msrp", String.valueOf(cheThreeHBean.getOrderAmt().toBigInteger()));

                    // 借款人姓名
                    // paramsMap.put("borrower_name", cheThreeHBean.getRealName());
                    paramsMap.put("borrower_name", "");
                    // 借款人身份证号
                    // paramsMap.put("borrower_id", cheThreeHBean.getIdNo());
                    paramsMap.put("borrower_id", "");
                    // 借款人手机号码
                    // paramsMap.put("borrower_tel", cheThreeHBean.getRegId());
                    paramsMap.put("borrower_tel", "");
                    // 借款人银行卡号
                    // paramsMap.put("borrower_bank_id", cheThreeHBean.getBankCardNo());
                    paramsMap.put("borrower_bank_id", "");
                    // 贷款开始时间
                    paramsMap.put("start_loan_date", cheThreeHBean.getLoanDate());
                    // 贷款结束时间
                    paramsMap.put("end_loan_date", cheThreeHBean.getEndLoanDate());
                    // 借款城市
                    paramsMap.put("apply_city", cheThreeHBean.getCity());
                    // 贷款类型
                    paramsMap.put("loan_type", bizTypeToLoanType(cheThreeHBean.getBizType()));
                    // 申请日期
                    paramsMap.put("apply_date", cheThreeHBean.getApplyDate());
                    // 首次应还款时间
                    paramsMap.put("first_repay_time", cheThreeHBean.getFirstPaymentDate());
                    // 首付款
                    // paramsMap.put("down_payment",
                    // String.valueOf(cheThreeHBean.getPreAmt().toBigInteger()));
                    paramsMap.put("down_payment", "");
                    // 尾款
                    // paramsMap.put("balance_payment",
                    // String.valueOf(cheThreeHBean.getBalancePayment().toBigInteger()));
                    paramsMap.put("balance_payment", "");
                    // 月还款
                    /*
                     * BigDecimal monthInterest = cheThreeHBean.getMonthInterest() != null ?
                     * cheThreeHBean.getMonthInterest() : BigDecimal.ZERO;
                     */
                    // paramsMap.put("monthly_payment",
                    // String.valueOf(monthInterest.toBigInteger()));
                    paramsMap.put("monthly_payment", "");
                    // 还款期数
                    paramsMap.put("loan_term", cheThreeHBean.getOrderItems());

                }
                logger.info("--上送参数--{}", JSONObject.toJSONString(paramsMap));

                String httpPost = "";
                try {
                    httpPost =
                            SimpleHttpUtils.httpGet(urlConfig.getCarThreeRiskUrl(), paramsMap);
                } catch (Exception e) {
                    logger.error("---调用车300接口registerPostLoanMonitorCar报错,错误信息---{}", e);
                }
                if (!StringUtil.isNull(httpPost) && JSON.parseObject(httpPost) != null) {
                    JSONObject jsonObj = JSON.parseObject(httpPost);
                    resultMap.put(status, String.valueOf(jsonObj.getIntValue(status)));
                    resultMap.put(message, jsonObj.getString(error_msg));
                    int assetsId = 0;
                    if (jsonObj.getIntValue(status) == 1) {
                        String data = jsonObj.getString("data");
                        JSONObject dataJson = JSONObject.parseObject(data);
                        assetsId = dataJson.getIntValue("assets_id");
                    }
                    resultMap.put("assetsId", String.valueOf(assetsId));
                    updateInstOrderLoanRiskInfo(resultMap, orderId);
                }
            }
        }

        return resultMap;
    }

    /**
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    private String bizTypeToLoanType(String bizType) {
        if (!StringUtil.isNull(bizType)) {
            if (bizType.equals("2001")) {
                return "11";
            } else if (bizType.equals("2002")) {
                return "21";
            } else {
                return "21";
            }
        }
        return null;
    }

    /**
     * 
     * Description:关闭贷后监控
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    @Override
    public Map<String, String> stopPostLoanMonitor(JSONObject objs) {
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(token, urlConfig.getCarThreeRiskToken());
        paramsMap.put(oper, "stopPostLoanMonitor");
        paramsMap.put("assets_id", objs.getString("assets_id"));

        String httpPost = "";
        try {
            httpPost =
                    SimpleHttpUtils.httpPost(urlConfig.getCarThreeRiskUrl(), paramsMap);
        } catch (Exception e) {
            logger.error("---调用车300接口stopPostLoanMonitor报错,错误信息---{}", e);
        }
        if (!StringUtil.isNull(httpPost) && JSON.parseObject(httpPost) != null) {
            JSONObject jsonObj = JSON.parseObject(httpPost);
            resultMap.put(status, String.valueOf(jsonObj.getIntValue(status)));
            resultMap.put(message, jsonObj.getString(error_msg));
        }
        return resultMap;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月10日
     */
    @Override
    public CheThreeHBean getOrderInfoByOrderId(String orderId) {
        CheThreeHBean cheThreeHBean = cheThreeHBiz.getOrderInfoByOrderId(orderId);
        BigDecimal orderAmt = cheThreeHBean.getOrderAmt();
        BigDecimal preAmt = cheThreeHBean.getPreAmt() != null ? cheThreeHBean.getPreAmt() : BigDecimal.ZERO;
        BigDecimal balancePayment = BigDecimalUtil.sub(orderAmt, preAmt);
        cheThreeHBean.setBalancePayment(balancePayment);
        if (!StringUtil.isEmpty(cheThreeHBean.getLoanDate())) {
            Calendar endLoanDate = Calendar.getInstance();
            Calendar c = DateUtil.parseCalendar(cheThreeHBean.getLoanDate(), DateUtil.SHORT_DATE_FORMAT);
            endLoanDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + cheThreeHBean.getOrderItems(),
                    c.get(Calendar.DATE),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            cheThreeHBean.setEndLoanDate(DateUtil.format(endLoanDate, DateUtil.SHORT_DATE_FORMAT));
        }
        String firstPaymentDate = getFirstLastRepayDate(orderId);
        cheThreeHBean.setFirstPaymentDate(firstPaymentDate);
        return cheThreeHBean;
    }

    /**
     * 
     * Description:更新发送车300贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    public void updateInstOrderLoanRiskInfo(Map<String, String> map, String orderId) {
        InstOrderloanRiskBean bean = new InstOrderloanRiskBean();
        if (map.get(status).equals("1")) {
            bean.setSendStatus("3");
        } else {
            bean.setSendStatus("1");
        }
        bean.setOrderId(orderId);
        bean.setStatus(map.get(status));
        bean.setErrorMsg(map.get(message));
        bean.setAssetsId(map.get("assetsId"));
        instOrderloanRiskBiz.updateInstOrderloanRiskInfoByOrderId(bean);
    }

    /**
     * 
     * 灰名单待发送列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @Override
    public PageInfo<Map<String, Object>> selectCheThreeHWaitSendList(
            JSONObject objs) {
        return new PageInfo<>(cheThreeHBiz.selectCheThreeHWaitSendList(objs));
    }

    /**
     * 
     * 车贷监控列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @Override
    public PageInfo<Map<String, Object>> selectCheThreeMonitorList(
            JSONObject objs) {
        return new PageInfo<>(cheThreeHBiz.selectCheThreeMonitorList(objs));
    }

    /**
     * 
     * 车贷监控列表查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @Override
    public String exportCheThreeMonitorList(JSONObject objs,
            HttpServletResponse response) {
        try {
            List<Map<String, Object>> cheThreeMonitorList =
                    cheThreeHBiz.selectCheThreeMonitorListNoPage(objs);
            // try {
            // trafficManageInfoList = appendCollectTrafficManageInfo(caseLawOrderList);
            // } catch (Exception e) {
            // logger.error("", e);
            // }
            // 2.导出excel表格
            HSSFWorkbook workbook = getCheThreeMonitorExportWorkbook(cheThreeMonitorList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=cheThreeMonitor-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出贷后案件列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    private HSSFWorkbook getCheThreeMonitorExportWorkbook(List<Map<String, Object>> cheThreeMonitorList) {
        String[] excelHeader =
        {"商户名称", "订单号", "姓名", "手机号", "监控车辆风险等级", "风险等级详情", "接收时间", "借款金额", "月供"
                , "品牌型号", "车架号", "车牌号", "发动机号", "车辆状态", "贷后状态", "发送时间", "发送状态", "失败原因"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("车务查询列表");

        HSSFRow row = sheet.createRow(0);
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
        if (cheThreeMonitorList != null)
            for (int i = 0; i < cheThreeMonitorList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> cheThreeMonitor = cheThreeMonitorList.get(i);
                row.createCell(0).setCellValue(
                        cheThreeMonitor.get("merchantName") == null ? "" : cheThreeMonitor.get("merchantName")
                                .toString());
                row.createCell(1).setCellValue(
                        cheThreeMonitor.get("orderId") == null ? "" : cheThreeMonitor.get("orderId").toString());
                row.createCell(2).setCellValue(
                        cheThreeMonitor.get("realName") == null ? "" : cheThreeMonitor.get("realName").toString());
                row.createCell(3).setCellValue(
                        cheThreeMonitor.get("regId") == null ? "" : cheThreeMonitor.get("regId").toString());
                if (cheThreeMonitor.get("riskCode") != null)
                    switch (cheThreeMonitor.get("riskCode").toString().toUpperCase()) {
                        case "CHE300PLM010":
                            row.createCell(4).setCellValue("低风险");
                            break;
                        case "CHE300PLM020":
                            row.createCell(4).setCellValue("中风险");
                            break;
                        case "CHE300PLM030":
                            row.createCell(4).setCellValue("高风险");
                            break;
                    }
                row.createCell(5).setCellValue(
                        cheThreeMonitor.get("rules") == null ? "" : cheThreeMonitor.get("rules").toString());
                row.createCell(6)
                        .setCellValue(
                                cheThreeMonitor.get("receiveTime") == null ? "" : cheThreeMonitor.get("receiveTime")
                                        .toString());
                row.createCell(7).setCellValue(
                        cheThreeMonitor.get("orderAmt") == null ? 0 :
                                ((BigDecimal) cheThreeMonitor.get("orderAmt")).setScale(2, BigDecimal.ROUND_HALF_UP)
                                        .doubleValue());
                row.createCell(8).setCellValue(
                        cheThreeMonitor.get("monthInterest") == null ? 0 :
                                ((BigDecimal) cheThreeMonitor.get("monthInterest")).setScale(2,
                                        BigDecimal.ROUND_HALF_UP).doubleValue());
                row.createCell(9).setCellValue(
                        cheThreeMonitor.get("orderName") == null ? "" : cheThreeMonitor.get("orderName").toString());
                row.createCell(10).setCellValue(
                        cheThreeMonitor.get("carNo") == null ? "" : cheThreeMonitor.get("carNo").toString());
                row.createCell(11).setCellValue(
                        cheThreeMonitor.get("plate") == null ? "" : cheThreeMonitor.get("plate").toString());
                row.createCell(12).setCellValue(
                        cheThreeMonitor.get("engine") == null ? "" : cheThreeMonitor.get("engine").toString());

                if (cheThreeMonitor.get("carStatus") != null)
                    switch (cheThreeMonitor.get("carStatus").toString()) {
                        case "5":
                            row.createCell(13).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(13).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(13).setCellValue("贷后处置中");
                            break;
                        case "15":
                            row.createCell(13).setCellValue("待失联处置");
                            break;
                        case "20":
                            row.createCell(13).setCellValue("已失联");
                            break;
                        case "25":
                            row.createCell(13).setCellValue("待入库");
                            break;
                        case "30":
                            row.createCell(13).setCellValue("已入库");
                            break;
                        case "35":
                            row.createCell(13).setCellValue("待出售");
                            break;
                        case "40":
                            row.createCell(13).setCellValue("已出售");
                            break;
                        case "45":
                            row.createCell(13).setCellValue("待转租");
                            break;
                        case "50":
                            row.createCell(13).setCellValue("已转租");
                            break;
                        case "55":
                            row.createCell(13).setCellValue("待返还");
                            break;
                        case "60":
                            row.createCell(13).setCellValue("已返还");
                            break;
                        case "65":
                            row.createCell(13).setCellValue("未拖回");
                            break;
                        case "70":
                            row.createCell(13).setCellValue("正常");
                            break;
                    }
                // 贷后状态: 5 待贷后处理 10贷后处理中 15 待外包处理 20 外包处理中 25 法务处理中 30 处理结束
            if (cheThreeMonitor.get("processStatus") != null)
                switch (cheThreeMonitor.get("processStatus").toString()) {
                    case "5":
                        row.createCell(14).setCellValue("待贷后处理");
                        break;
                    case "05":
                        row.createCell(14).setCellValue("待贷后处理");
                        break;
                    case "10":
                        row.createCell(14).setCellValue("贷后处理中");
                        break;
                    case "15":
                        row.createCell(14).setCellValue("待外包处理");
                        break;
                    case "20":
                        row.createCell(14).setCellValue("外包处理中");
                        break;
                    case "25":
                        row.createCell(14).setCellValue("法务处理中");
                        break;
                    case "30":
                        row.createCell(14).setCellValue("处理结束");
                        break;
                }
            row.createCell(15).setCellValue(
                    cheThreeMonitor.get("sendTime") == null ? "" : cheThreeMonitor.get("sendTime").toString());
            if (cheThreeMonitor.get("sendStatus") != null)
                switch (cheThreeMonitor.get("sendStatus").toString()) {
                    case "1":
                        row.createCell(16).setCellValue("未发送");
                        break;
                    case "9":
                        row.createCell(16).setCellValue("发送失败");
                        break;
                    case "3":
                        row.createCell(16).setCellValue("已发送");
                        break;
                }
            row.createCell(17).setCellValue(
                    cheThreeMonitor.get("errorMsg") == null ? "" : cheThreeMonitor.get("errorMsg").toString());
        }
        return wb;
    }

    /**
     * 
     * 反欺诈详情查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @Override
    public PageInfo<Map<String, Object>> selectCheThreeMonitorReceive(
            JSONObject objs) {
        return new PageInfo<>(cheThreeHBiz.selectCheThreeMonitorReceive(objs));
    }

    /**
     * 
     * Description:根据订单号获取第一期还款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    public String getFirstLastRepayDate(String orderId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("orderId", orderId);
        String resultStr = "";
        String firstRepayDate = "";
        try {
            resultStr = SimpleHttpUtils.httpPost(
                    consumerConfig.getQueryFirstLastRepayDateByOrderId(),
                    SignUtil.chatEncode(JSONObject.toJSONString(tmpMap), consumerConfig.getCommonPrivateKey()));
        } catch (Exception e) {
            logger.error("---根据订单号获取第一期还款时间报错---{}", e);
        }
        if (!StringUtil.isNull(resultStr)) {
            result = JSONObject.parseObject(resultStr);
            firstRepayDate = (String) result.get("result");
        }
        return firstRepayDate;
    }

    /**
     * 
     * Description:贷后风险信息回调
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    public int loanRiskAccept(JSONArray jsonArray) {
        int resultValue = 0;
        List<InstOrderloanRiskResultBean> loanRiskList =
                JSONObject.parseArray(jsonArray.toJSONString(), InstOrderloanRiskResultBean.class);
        if (!CollectionUtils.isEmpty(loanRiskList)) {
            for (InstOrderloanRiskResultBean bean : loanRiskList) {
                resultValue += instOrderloanRiskBiz.insertInstLoanRiskResult(bean);
            }
        }
        return resultValue;
    }
}
