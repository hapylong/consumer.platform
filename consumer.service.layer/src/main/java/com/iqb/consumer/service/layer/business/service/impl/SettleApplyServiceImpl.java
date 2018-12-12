package com.iqb.consumer.service.layer.business.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.sublet.pojo.ChatToGetRepayParamsResponseMessage;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyOrderPojo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.ProductBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.service.layer.business.service.SettleApplyService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SendHttpsUtil;

import jodd.util.StringUtil;

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
 * 2017年12月25日下午3:42:16 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class SettleApplyServiceImpl implements SettleApplyService {
    private static final Logger logger = LoggerFactory.getLogger(SettleApplyServiceImpl.class);
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private ProductBiz productBiz;
    @Resource
    private UrlConfig urlConfig;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Autowired
    private SysUserSession sysUserSession;

    /**
     * 根据订单号查询提前结清信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月25日
     */
    @Override
    public SettleApplyBean getSettleBeanByOrderId(String orderId) {
        return settleApplyBeanBiz.getSettleBeanByOrderId(orderId);
    }

    /**
     * 根据订单号修改提前结清数据
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月25日
     */
    @Override
    public int updateSettleBean(SettleApplyBean settleApplyBean) {
        return settleApplyBeanBiz.updateSettleBean(settleApplyBean);
    }

    @Override
    public PageInfo<SettleApplyOrderPojo> selectSettleOrderList(JSONObject param) {
        return new PageInfo<>(settleApplyBeanBiz.selectSettleOrderList(param, true));
    }

    /**
     * 
     * @param params
     * @return
     * @throws GenerallyException
     * @Author haojinlong Create Date: 2017年12月26日
     */
    @Override
    public SettleApplyBean selectSettleBean(String orderId) throws GenerallyException {
        // 查询订单客户姓名 、总期数
        SettleApplyBean applyBean = settleApplyBeanBiz.getSettleBeanByOrderId(orderId);
        // 计算金额
        SettleApplyBean recalculateApplyBean = recalculateAmt(orderId);
        recalculateApplyBean.setOrderId(orderId);
        recalculateApplyBean.setOrderItems(applyBean.getOrderItems());
        recalculateApplyBean.setRealName(applyBean.getRealName());
        recalculateApplyBean.setMonthInterest(applyBean.getMonthInterest());
        SettleApplyBean bean = settleApplyBeanBiz.selectSettleBeanByOrderId(orderId);
        if (bean != null) {
            settleApplyBeanBiz.updateSettleBean(recalculateApplyBean);
        } else {
            settleApplyBeanBiz.saveSettleApplyInfo(recalculateApplyBean);
        }

        return recalculateApplyBean;
    }

    /**
     * 
     * Description:计算提前结清金额
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月26日
     * @throws GenerallyException
     */
    private SettleApplyBean recalculateAmt(String orderId) throws GenerallyException {
        SettleApplyBean settleApplyBean = new SettleApplyBean();
        JSONObject objs = new JSONObject();
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            objs.put("id", orderBean.getPlanId());
            PlanBean planBean = productBiz.getPlanByID(objs);
            ChatToGetRepayParamsResponseMessage repayBean = chatToGetRepayStartParams(orderId);
            if (repayBean.getResult() != null) {
                // 逾期期数
                int curItems = repayBean.getResult().getOverdueItems();
                // 每月利息
                BigDecimal monthInterest = BigDecimal.ZERO;
                // 未还利息
                BigDecimal remainInterest = BigDecimal.ZERO;
                // 已还本金
                BigDecimal payPrincipal = BigDecimal.ZERO;
                // 未还本金
                BigDecimal surplusPrincipal = BigDecimal.ZERO;
                // 违约金
                BigDecimal overdueAmt = BigDecimal.ZERO;

                /**
                 * 应退上收息规则： 当期应还是否已还款：当期已还，除去当期多还M期 应退上收息=上收息金额/上收息期数*（上收息期数-已还期数+M）
                 * 当期应还是否已还款：当期未还款，并逾期N期 应退上收息=上收息金额/上收息期数*（上收息期数-已还期数-N-1）
                 */

                BigDecimal refundAmt = BigDecimal.ZERO;
                /**
                 * 累计应还=（逾期期数+1）*每月利息+总罚息+未还本金+减免后违约金-应退上收息
                 */
                BigDecimal totalRepayAmt = BigDecimal.ZERO;

                monthInterest = repayBean.getResult().getMonthInterest();
                // 首付款
                BigDecimal downPayment = BigDecimalUtil.mul(new BigDecimal(orderBean.getOrderAmt()),
                        BigDecimal.valueOf(planBean.getDownPaymentRatio())).divide(BigDecimal.valueOf(100));
                // 当前期数
                Integer curItmes = repayBean.getResult().getCurItems();
                Integer hasItems = repayBean.getResult().getHasRepayNo();
                // 已收上收息
                BigDecimal feeAmount =
                        BigDecimalUtil.mul(new BigDecimal(orderBean.getOrderAmt()).subtract(downPayment),
                                BigDecimal.valueOf(planBean.getFeeRatio()))
                                .multiply(new BigDecimal(planBean.getFeeYear())).divide(new BigDecimal(100));

                remainInterest =
                        monthInterest.multiply(new BigDecimal(Integer.parseInt(orderBean.getOrderItems())
                                - repayBean.getResult().getHasRepayNo()));
                payPrincipal =
                        BigDecimalUtil.mul(repayBean.getResult().getMonthPrincipal(), new BigDecimal(repayBean
                                .getResult()
                                .getHasRepayNo()));
                surplusPrincipal =
                        BigDecimalUtil.sub(new BigDecimal(orderBean.getOrderAmt()), downPayment).subtract(payPrincipal);
                overdueAmt = BigDecimalUtil.mul(surplusPrincipal, new BigDecimal("6")).divide(new BigDecimal("100"));

                if (planBean.getFeeYear() > 0) {
                    BigDecimal feeAmt = orderBean.getFeeAmount() != null ? orderBean.getFeeAmount() : BigDecimal.ZERO;
                    if (hasItems > curItmes) {
                        refundAmt =
                                BigDecimalUtil.div(feeAmt, new BigDecimal(planBean.getFeeYear()))
                                        .multiply(
                                                new BigDecimal(planBean.getFeeYear()
                                                        - repayBean.getResult().getCurItems()));
                    } else {
                        refundAmt =
                                BigDecimalUtil.div(feeAmt, new BigDecimal(planBean.getFeeYear()))
                                        .multiply(
                                                new BigDecimal(planBean.getFeeYear()
                                                        - repayBean.getResult().getHasRepayNo() - curItems - 1));
                    }

                }
                // 应退上收息为负数时将其置为0
                if (refundAmt.compareTo(BigDecimal.ZERO) < 0) {
                    refundAmt = BigDecimal.ZERO;
                }
                // 当期账单还款状态
                int status = repayBean.getResult().getStatus();
                if (status <= 2) {
                    totalRepayAmt =
                            BigDecimalUtil.add(new BigDecimal(curItems + 1)).multiply(monthInterest)
                                    .add(repayBean.getResult().getTotalOverdueInterest()).add(surplusPrincipal)
                                    .subtract(refundAmt).add(overdueAmt);
                } else {
                    totalRepayAmt =
                            BigDecimalUtil.add(repayBean.getResult().getTotalOverdueInterest()).add(surplusPrincipal)
                                    .subtract(refundAmt).add(overdueAmt);
                }

                settleApplyBean.setCurItems(repayBean.getResult().getHasRepayNo());
                settleApplyBean.setMonthPrincipal(repayBean.getResult().getMonthPrincipal());
                settleApplyBean.setTotalOverdueInterest(repayBean.getResult().getTotalOverdueInterest());
                // 应退上收息
                settleApplyBean.setRefundAmt(refundAmt);
                settleApplyBean.setRemainInterest(remainInterest);
                settleApplyBean.setPayPrincipal(payPrincipal);
                settleApplyBean.setMargin(new BigDecimal(orderBean.getMargin()));
                settleApplyBean.setFeeAmount(feeAmount);
                settleApplyBean.setSurplusPrincipal(surplusPrincipal);
                settleApplyBean.setOverdueAmt(overdueAmt);
                settleApplyBean.setTotalRepayAmt(totalRepayAmt);
                settleApplyBean.setUserId(Long.parseLong(orderBean.getUserId()));
                settleApplyBean.setTotalRepayAmtOriginal(totalRepayAmt);
                settleApplyBean.setOverItems(String.valueOf(curItems));

            }

        }

        return settleApplyBean;
    }

    private ChatToGetRepayParamsResponseMessage chatToGetRepayStartParams(String orderId) throws GenerallyException {
        logger.debug("---调用账务系统接口getFactorys---参数--{}", orderId);
        String response;
        ChatToGetRepayParamsResponseMessage crp = null;
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("orderId", orderId);
            response = SimpleHttpUtils.httpPost(
                    urlConfig.getRepayParamsUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(request), urlConfig.getCommonPrivateKey()));
            logger.debug("---调用账务系统接口getFactorys---返回--{}" + JSONObject.toJSONString(response));

            if (StringUtil.isEmpty(response)) {
                throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("---调用账务系统接口getFactorys---报错{}:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.B);
        }
        try {
            crp = JSONObject.parseObject(response, ChatToGetRepayParamsResponseMessage.class);
        } catch (Exception e) {
            logger.error("---调用账务系统接口getFactorys---解析返回结果报错{}  :", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.C);
        }
        return crp;
    }

    /**
     * 
     * Description:提前结清流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月26日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public int prepaymentStartWF(String orderId) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        SettleApplyBean settleApplyBean = settleApplyBeanBiz.selectSettleBeanByOrderId(orderId);
        if (orderBean != null && settleApplyBean != null) {
            UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    userBean.getRealName() + ";" + merchantBean.getMerchantShortName() + ";提前还款";// 摘要添加手机号

            // WFConfig wfConfig =
            wfConfigBiz.getConfigByBizType(orderBean.getBizType(), Integer.parseInt(orderBean.getStatus()));

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", urlConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", urlConfig.getTokenUser());
            hmVariables.put("procTokenPass", urlConfig.getTokenPass());
            hmVariables.put("procTaskUser", sysUserSession.getUserCode());
            hmVariables.put("procTaskRole", urlConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderBean.getOrderId() + "-" + settleApplyBean.getId());
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", "");

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = urlConfig.getStartWfurl();
            // 发送Https请求
            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        } else {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        if (responseMap.get("retCode").toString().equals("00000000")) {
            Map<String, String> map = (HashMap<String, String>) responseMap.get("iqbResult");
            String procInstId = map.get("procInstId");

            orderBean = new OrderBean();
            orderBean.setOrderId(orderId);
            orderBean.setProcInstId(procInstId);
            result = orderBiz.updateOrderInfo(orderBean);

            // 更新提前结清申请状态

            SettleApplyBean sab = settleApplyBeanBiz.selectSettleBeanByOrderId(orderId);
            sab.setId(settleApplyBean.getId());
            sab.setProcInstId(procInstId);
            sab.setSettleStatus(1);

            settleApplyBeanBiz.updateSettleBean(sab);
        }
        return result;
    }

    /**
     * 
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public PageInfo<SettleApplyBean> selectPrepaymentList(JSONObject objs) {
        return new PageInfo<>(settleApplyBeanBiz.selectPrepaymentList(objs));
    }

    @Override
    public void selectSettleOrderListExle(JSONObject objs, HttpServletResponse response) {
        try {
            List<SettleApplyOrderPojo> selectSettleOrderList = settleApplyBeanBiz.selectSettleOrderList(objs, false);

            // 2.导出excel表格
            HSSFWorkbook workbook = null;
            String fileName = null;
            workbook = this.riskIntXlsx(selectSettleOrderList);
            fileName = "attachment;filename=statisticsInt-" + DateTools.getYmdhmsTime() + ".xls";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();

        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
        }

    }

    private HSSFWorkbook riskIntXlsx(List<SettleApplyOrderPojo> newRoverdueEntityList) {

        String[] header =
        {"商户", "订单号", "客户姓名", "手机号", "借款金额", "总期数", "月供", "申请状态"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("提前还款申请信息");
        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }
        if (newRoverdueEntityList != null && newRoverdueEntityList.size() != 0 && newRoverdueEntityList.get(0) != null) {
            logger.debug("查询SIZE", newRoverdueEntityList.size());
            for (int i = 0; i < newRoverdueEntityList.size(); i++) {
                hr = hs.createRow(i + 1);
                hr.createCell(0).setCellValue(newRoverdueEntityList.get(i).getMerchantShortName());
                hr.createCell(1).setCellValue(newRoverdueEntityList.get(i).getOrderId());
                hr.createCell(2).setCellValue(newRoverdueEntityList.get(i).getRealName());
                hr.createCell(3).setCellValue(newRoverdueEntityList.get(i).getRegId());
                hr.createCell(4).setCellValue(newRoverdueEntityList.get(i).getOrderAmt().toString());
                hr.createCell(5).setCellValue(newRoverdueEntityList.get(i).getOrderItems());
                hr.createCell(6).setCellValue(newRoverdueEntityList.get(i).getMonthInterest().toString());
                if (newRoverdueEntityList.get(i).getSettleStatus() == null
                        || "".equals(newRoverdueEntityList.get(i).getSettleStatus())) {
                    hr.createCell(7).setCellValue("");
                }
                if (newRoverdueEntityList.get(i).getSettleStatus() != null
                        && newRoverdueEntityList.get(i).getSettleStatus() == 1) {
                    hr.createCell(7).setCellValue("审核中");
                }
                if (newRoverdueEntityList.get(i).getSettleStatus() != null
                        && newRoverdueEntityList.get(i).getSettleStatus() == 2) {
                    hr.createCell(7).setCellValue("审核通过");
                }
                if (newRoverdueEntityList.get(i).getSettleStatus() != null
                        && newRoverdueEntityList.get(i).getSettleStatus() == 3) {
                    hr.createCell(7).setCellValue("审核拒绝");
                }
                if (newRoverdueEntityList.get(i).getSettleStatus() != null
                        && newRoverdueEntityList.get(i).getSettleStatus() == 4) {
                    hr.createCell(7).setCellValue("审核终止");
                }
            }
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 1; j < header.length; j++) {
            hs.setColumnWidth(0, 3000);
        }
        return hwb;

    }
}
