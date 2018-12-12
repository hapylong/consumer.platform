package com.iqb.consumer.service.layer.business.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.constant.Constant;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerCarInfo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerOrderInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.order.FinishBillRequestMessage;
import com.iqb.consumer.data.layer.bean.order.InstOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderOtherAmtEntity;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;
import com.iqb.consumer.data.layer.bean.order.OrderManageResult;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.bean.order.house.InstdetailPojo;
import com.iqb.consumer.data.layer.bean.order.mplan.InstPlanEntity;
import com.iqb.consumer.data.layer.bean.order.pojo.HouseBillChatFRequestPojo;
import com.iqb.consumer.data.layer.bean.order.pojo.HouseBillCreateRequestMessage;
import com.iqb.consumer.data.layer.bean.order.pojo.InstdetailChatFPojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.carstatus.CarStatusManager;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.order.OrderBreakBiz;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.back.IBackGroundService;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.credit_product.CreditProductService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.JSONUtil;

import jodd.util.StringUtil;

@Component
public class OrderService extends BaseService implements IOrderService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private JysOrderBiz jysOrderBiz;

    @Autowired
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Autowired
    private OrderBreakBiz orderBreakBiz;
    @Autowired
    private SysCoreDictItemBiz sysCoreDictItemBiz;
    @Resource
    private CalculateService calculateService;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Autowired
    private DictService dictServiceImpl;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private AdminService adminServiceImpl;
    @Autowired
    private IBackGroundService backGroundService;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Autowired
    private CreditProductService creditProductServiceImpl;
    @Autowired
    private CarStatusManager carStatusManager;

    @Override
    public String exportOrderList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            adminServiceImpl.merchantNos(objs);
            List<OrderBean> orderList = orderBiz.selectSelective2(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportOrderList(orderList);
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

    @Override
    public PageInfo<OrderBean> getOrderInfoByList(JSONObject objs) {
        if (objs.get("type") != null && objs.get("type").toString().equals("2")) {
            return new PageInfo<>(orderBiz.getStageOrderList(objs));
        }
        return new PageInfo<>(orderBiz.selectSelective(objs));
    }

    @Override
    public PageInfo<OrderBean> getStageOrderList(JSONObject objs) {
        return new PageInfo<>(orderBiz.getStageOrderList(objs));
    }

    @Override
    public int updateOrderInfo(JSONObject objs) {
        // 获取用户机构代码先查询权限
        OrderBean orderBean = JSONUtil.toJavaObject(objs, OrderBean.class);
        // 计算预支付金额,月供
        Map<String, BigDecimal> calculateOrderMap = calculateOrderInfo(orderBean);
        orderBean.setPreAmt(calculateOrderMap.get("preAmount") + "");
        orderBean.setMonthInterest(calculateOrderMap.get("monthMake"));
        return orderBiz.updateOrderInfo(orderBean);
    }

    @Override
    public int newUpdateOrderInfo(JSONObject objs) {
        // 获取用户机构代码先查询权限
        OrderBean orderBean = JSONUtil.toJavaObject(objs, OrderBean.class);
        return orderBiz.updateOrderInfo(orderBean);
    }

    @Override
    public OrderBean selectOne(JSONObject objs) {
        String orderId = (String) objs.get("orderId");
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        return orderBiz.selectOne(params);
    }

    @Override
    public PageInfo<OrderBean> getPreOrderInfoByList(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        // 分页插件需要对外包装为PageInfo<T>类型,其中会额外封装一些分页参数(如：total)
        return new PageInfo<>(orderBiz.selectPreOrderList(objs));
    }

    @Override
    public List<PlanBean> getPlanByMerNo(JSONObject objs) {
        String orderId = objs.getString("orderId");
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        OrderBean orderBean = orderBiz.selectOne(params);
        return qrCodeAndPlanBiz.getPlanByMerNo(orderBean.getMerchantNo());
    }

    // 获取计算的订单信息
    private Map<String, BigDecimal> calculateOrderInfo(OrderBean orderBean) {
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal result = new BigDecimal("0.00");
        try {
            // 订单基本信息
            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(orderBean.getPlanId()));// 获取订单计划
            BigDecimal amt = new BigDecimal(orderBean.getOrderAmt());// 总额
            int orderTerm = planBean.getInstallPeriods();// 分期期数
            BigDecimal feeRatio = new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal(100));// 月利率
            int feeYear = planBean.getFeeYear();// 上收息期数
            int takePayment = orderBean.getTakePayment();// 是否上收首期
            BigDecimal downPayment = new BigDecimal(orderBean.getDownPayment());// 首付
            BigDecimal margin = new BigDecimal(orderBean.getMargin());// 保证金
            BigDecimal serviceFee = new BigDecimal(orderBean.getServiceFee());// 服务费

            // 计算信息
            BigDecimal lastFee = (amt.subtract(downPayment)).multiply(feeRatio)
                    .multiply((new BigDecimal(orderTerm - feeYear)));// 剩余利息
            BigDecimal monthMake = (amt.subtract(downPayment).add(lastFee)).divide(new BigDecimal(orderTerm), 2);// 月供
            BigDecimal monthFee = feeRatio.multiply(amt.subtract(downPayment));// 月利
            BigDecimal totalFee = monthFee.multiply(new BigDecimal(feeYear));// 总利
            BigDecimal monthAmount = monthMake.multiply(new BigDecimal(takePayment)).setScale(0, BigDecimal.ROUND_DOWN);// 所收月供
            result = totalFee.add(downPayment).add(margin).add(serviceFee).add(monthAmount).setScale(2,
                    BigDecimal.ROUND_HALF_UP);// 结果保留2位小数
            map.put("monthMake", monthMake.setScale(0, BigDecimal.ROUND_DOWN));
            map.put("feeAmount", totalFee);
            map.put("preAmount", result);
        } catch (Exception e) {
            logger.error("获取预付金计算异常:" + e);
        }

        return map;
    }

    // 导出
    private HSSFWorkbook exportOrderList(List<OrderBean> list) {
        String[] excelHeader = {"订单号", "姓名", "手机号", "订单名称", "订单时间", "金额", "期数", "方案", "首付", "保证金", "服务费", "月供", "上收息",
                "上收月供", "预付款", "业务类型", "收取方式", "订单状态", "工作流状态", "上标时间", "分期时间", "放款时间", "商户号", "员工姓名", "员工编号"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单信息页");

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

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            OrderManageResult orderBean = (OrderManageResult) list.get(i);

            row.createCell(0).setCellValue(orderBean.getOrderId());
            row.createCell(1).setCellValue(orderBean.getUserName());
            row.createCell(2).setCellValue(orderBean.getRegId());
            row.createCell(3).setCellValue(orderBean.getOrderName());
            HSSFCell cell = row.createCell(4);
            cell.setCellValue(orderBean.getCreateTime());
            cell.setCellStyle(style);
            row.createCell(5).setCellValue(orderBean.getOrderAmt() == null ? "0.00" : orderBean.getOrderAmt());
            row.createCell(6).setCellValue(orderBean.getOrderItems());
            row.createCell(7).setCellValue(orderBean.getPlanFullName());
            row.createCell(8).setCellValue(orderBean.getDownPayment() == null ? "0.00" : orderBean.getDownPayment());
            row.createCell(9).setCellValue(orderBean.getMargin() == null ? "0.00" : orderBean.getMargin());
            row.createCell(10).setCellValue(orderBean.getServiceFee() == null ? "0.00" : orderBean.getServiceFee());
            row.createCell(11).setCellValue(
                    orderBean.getMonthInterest() == null ? 0 : orderBean.getMonthInterest()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(12).setCellValue(orderBean.getFeeAmount() == null ? "" : orderBean.getFeeAmount() + "");
            row.createCell(13).setCellValue(orderBean.getTakePayment() == 1 ? "是" : "否");
            row.createCell(14).setCellValue(orderBean.getPreAmt() == null ? "0.00" : orderBean.getPreAmt());
            String bizTypeName =
                    sysCoreDictItemBiz.getDictByDTCAndDC(DictTypeCodeEnum.business_type, orderBean.getBizType())
                            .getDictName();
            row.createCell(15).setCellValue(bizTypeName);
            row.createCell(16).setCellValue(orderBean.getChargeWay() == 0 ? "线上收取" : "线下收取");
            row.createCell(17).setCellValue(this.getRisk2Excel(orderBean.getRiskStatus(), orderBean.getWfStatus()));
            row.createCell(18).setCellValue(this.getWFstatus2Excel(orderBean.getWfStatus()));
            String sTranTime = "";
            if (null != orderBean.getApplyTime()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                sTranTime = sdf.format(orderBean.getApplyTime());
            }
            row.createCell(19).setCellValue(sTranTime);
            String stageDat = "";
            if (null != orderBean.getStageDate()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");
                stageDat = sdf.format(orderBean.getStageDate());
            }
            row.createCell(20).setCellValue(stageDat);
            String loanDate = "";
            if (null != orderBean.getLoanDate()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");
                loanDate = sdf.format(orderBean.getLoanDate());
            }
            row.createCell(21).setCellValue(loanDate);
            row.createCell(22).setCellValue(orderBean.getMerchName());
            row.createCell(23).setCellValue(orderBean.getEmployeeName());
            row.createCell(24).setCellValue(orderBean.getEmployeeID());
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

    private String getRisk2Excel(Integer riskStatus, Integer wfStatus) {
        String result = "";
        if (null == riskStatus) {
            return "审核通过";
        }
        switch (riskStatus) {
            case 1:
                result = "审核拒绝";
                break;
            case 2:
                result = "审核中";
                break;
            case 3:
                result = "已分期";
                break;
            case 4:
                if (wfStatus > 4 || wfStatus == 0 || wfStatus == null) {
                    result = "待付款";
                } else if (wfStatus == 4) {
                    result = "审核中";
                }
                break;
            case 6:
                result = "已取消";
                break;
            case 21:
                result = "待估价";
                break;
            case 22:
                result = "已估价";
                break;
            case 7:
                result = "已放款";
                break;
            case 5:
                result = "待确认";
                break;
            case 10:
                result = "已结清";
                break;
            case 11:
                result = "已终止";
                break;
            default:
                result = "审核通过";
                break;
        }
        return result;
    }

    private String getWFstatus2Excel(Integer status) {
        String result = "";
        if (null == status) {
            return "待门店预处理";
        }
        switch (status) {
            case 0:
                result = "流程拒绝";
                break;
            case 2:
                result = "待门店预处理";
                break;
            case 3:
                result = "待风控审批";
                break;
            case 4:
                result = "待抵质押物估价";
                break;
            case 5:
                result = "待项目信息维护";
                break;
            case 6:
                result = "待内控审批";
                break;
            case 7:
                result = "待财务收款确认";
                break;
            case 8:
                result = "待项目初审";
                break;
            case 9:
                result = "流程结束";
                break;
        }
        return result;
    }

    @Override
    public OrderOtherInfo selectOtherOne(JSONObject objs) {
        return orderBiz.selectOtherOne(objs.getString("orderId"));
    }

    @Override
    public PageInfo<OrderBean> getAuthorityOrderList(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }

        return new PageInfo<>(orderBiz.getAuthorityOrderList(objs));
    }

    @Override
    public JYSOrderBean getSingleOrderInfo(JSONObject objs) {
        if (objs.getString("flag") != null) {
            return jysOrderBiz.getOrderInfoById(objs.getString("orderId"));
        } else {
            return jysOrderBiz.getSingleOrderInfo(objs);
        }
    }

    @Override
    public List<PlanBean> getPlanByMerAndBType(JSONObject objs) {
        return qrCodeAndPlanBiz.getPlByMerAndBType(objs.getString("merchantNo"), objs.getString("bizType"));
    }

    @Override
    public PageInfo<JYSOrderBean> listJYSOrderInfo(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        return new PageInfo<>(jysOrderBiz.listJYSOrderInfo(objs));
    }

    @Override
    @Transactional
    public int updateJYSOrderInfo(JSONObject objs) {
        if (objs.get("realName") != null && objs.get("regId") != null) {
            jysOrderBiz.updateJYSUserInfo(objs);
        }
        //
        String planId = objs.getString("planId");
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(planId));
        Map<String, BigDecimal> detail =
                calculateService.calculateAmt(planBean, new BigDecimal(objs.getString("orderAmt")));// getDetail(new
                                                                                                    // BigDecimal(objs.getString("orderAmt")),
                                                                                                    // planBean);
        objs.put("fee", planBean.getFeeRatio());
        objs.put("preAmt", detail.get("preAmt"));
        objs.put("margin", detail.get("margin"));
        objs.put("downPayment", detail.get("downPayment"));
        objs.put("serviceFee", detail.get("serviceFee"));
        objs.put("planId", planId);
        objs.put("takePayment", detail.get("takePayment"));
        objs.put("feeYear", planBean.getFeeYear());
        objs.put("feeAmount", detail.get("feeAmount"));
        logger.info("交易所修改信息:{}", objs);
        return jysOrderBiz.updateJYSOrderInfo(objs);
    }

    @Override
    public int deleteJYSOrderInfo(JSONObject objs) {
        List<Long> list;
        try {
            list = (List<Long>) objs.get("ids");
        } catch (Exception e) {
            logger.debug("获取Id列表失败");
            return 0;
        }
        return jysOrderBiz.deleteJYSOrderInfo(list);
    }

    @Override
    public String getOrgCodeByOrderId(String orderId) {
        return this.orderBiz.getOrgCodeByOrderId(orderId);
    }

    @Override
    public OrderBreakInfo selOrderInfo(String orderId) {
        return orderBreakBiz.selOrderInfo(orderId);
    }

    @Override
    public int saveOrderBreakInfo(OrderBreakInfo orderBreakInfo) {
        OrderBreakInfo breakInfo = orderBreakBiz.selOrderInfo(orderBreakInfo.getOrderId());
        if (breakInfo == null) {
            return orderBreakBiz.insertOrderInfo(orderBreakInfo);
        } else {
            return orderBreakBiz.updateOrderInfo(orderBreakInfo);
        }
    }

    /**
     * 
     * 保存订单金额 期数以及卡号
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月18日
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    @Override
    public int updateAmtAndItems(JSONObject objs) {
        String orderId = objs.getString("orderId");
        String bankCard = objs.getString("bankCard");
        String bankName = objs.getString("bankCardName");
        // 调用接口查询出开户行对应的银行CODE 一并保存
        // String bankCode =
        logger.info("保存银行卡信息入参{}", objs);
        try {
            OrderBean orderBean = orderBiz.selByOrderId(orderId);

            UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
            BankCardBean bankCardBean = bankCardBeanBiz.getOpenBankCardByRegId(String.valueOf(userBean.getId()));
            if (StringUtils.isEmpty(bankCard)) {
                bankCard = bankCardBean.getBankCardNo();
            }
            if (bankCardBean != null) {
                logger.info("更新银行卡信息入参{}{},{}", bankName, bankCard);
                bankCardBeanBiz.updateBankCardNoByUserId(String.valueOf(userBean.getId()), bankCard, bankName);
            } else {
                logger.info("插入银行卡信息入参{}{},{}", bankName, bankCard);
                bankCardBean = new BankCardBean();
                bankCardBean.setUserId(userBean.getId());
                bankCardBean.setBankCardNo(bankCard);
                bankCardBean.setBankMobile(userBean.getRegId());
                bankCardBean.setBankName(bankName);
                // 是否永存bankCode
                // bankCardBean.setBankCode(bankCode);
                bankCardBeanBiz.saveBankCard(bankCardBean);
            }
            return orderBiz.saveAmtAndItems(objs);
        } catch (Exception e) {
            logger.info("获取订单{}失败", orderId);
            return 0;
        }
    }

    /**
     * 
     * FINANCE-2734 门店签约节点回显可修改开户行名称
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月18日
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    @Override
    public void saveBankInfo(JSONObject objs) {
        String orderId = objs.getString("orderId");
        String bankCard = objs.getString("bankCard");
        String bankName = objs.getString("bankCardName");
        // 调用接口查询出开户行对应的银行CODE 一并保存
        // String bankCode =
        try {
            OrderBean orderBean = orderBiz.selByOrderId(orderId);

            UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
            BankCardBean bankCardBean = bankCardBeanBiz.getOpenBankCardByRegId(String.valueOf(userBean.getId()));
            if (StringUtils.isEmpty(bankCard)) {
                bankCard = bankCardBean.getBankCardNo();
            }
            if (bankCardBean != null) {
                bankCardBeanBiz.updateBankCardNoByUserId(String.valueOf(userBean.getId()), bankCard, bankName);
            } else {
                bankCardBean = new BankCardBean();
                bankCardBean.setUserId(userBean.getId());
                bankCardBean.setBankCardNo(bankCard);
                bankCardBean.setBankMobile(userBean.getRegId());
                bankCardBean.setBankName(bankName);
                // 是否永存bankCode
                // bankCardBean.setBankCode(bankCode);
                bankCardBeanBiz.saveBankCard(bankCardBean);
            }
        } catch (Exception e) {
            logger.info("获取订单{}失败", orderId);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月18日
     */
    @Override
    public int updateOrderProInfo(JSONObject objs) {
        String orderRemark =
                objs.getString("flag") + ";" + objs.getString("startDate") + ";" + objs.getString("endDate");
        objs.put("proInfo", orderRemark);
        return orderBiz.saveAmtAndItems(objs);
    }

    @Override
    public Object houseHandlerBillQuery(Integer type, JSONObject requestMessage) throws GenerallyException {
        if (!Constant.HOUSE_BILL_QUERY_TYPE.contains(type)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }

        switch (type) {
            case Constant.HOUSE_BILL_WFSTATUS_100:
                return queryHouseBillWfstatus100(requestMessage, false);
            case Constant.HOUSE_BILL_WFSTATUS_100_PAGE:
                return queryHouseBillWfstatus100(requestMessage, true);
            default:
                throw new IllegalArgumentException("queryBillHandler type not define.");
        }
    }

    @Override
    public void houseHandlerCreateBill(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException {
        HouseBillCreateRequestMessage hbcr;
        try {
            hbcr =
                    JSONObject.toJavaObject(requestMessage, HouseBillCreateRequestMessage.class);
            if (hbcr == null || !hbcr.checkRequest()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("houseHandlerCreateBill error:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }

        // 通过订单号查询订单详情
        HouseOrderEntity hoe = orderBiz.queryHOEByOid(hbcr.getOrderId());
        if (hoe == null) {
            // 应该抛异常为该订单不存在
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        // 通过分期ID查询分期计划
        InstPlanEntity mpe = orderBiz.queryMPEByPid(hoe.getPlanId());
        if (mpe == null) {
            // 抛异常对应分期计划不存在
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.B);
        }

        HouseOrderEntity updateHoe = new HouseOrderEntity();
        updateHoe.setId(hoe.getId());
        updateHoe.setOrderNo(hoe.getOrderNo());
        BigDecimal riAmt = hoe.getRealInstAmt() == null ? BigDecimal.ZERO : hoe.getRealInstAmt();
        int compare = hoe.getApprovalAmt().subtract(hbcr.getInstallAmt()).subtract(riAmt)
                .compareTo(BigDecimal.ZERO);
        if (compare < 0) {
            // 已经不存在可分期金额，提示用户.
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.C);
        } else {
            updateHoe.setRealInstAmt(riAmt.add(hbcr.getInstallAmt()));
            updateHoe.setStatusDesc(7); // 以分期
        }
        HouseBillChatFRequestPojo hbc = new HouseBillChatFRequestPojo();
        // 组装发送账户系统的数据
        processHBC(hbcr, hoe, mpe, hbc);

        // 判断分期用户是否开户,如果未开户先开户
        if (!adminServiceImpl.isAccountExit(hbc.getOpenId(), hoe.getMobile())) {
            adminServiceImpl.applyNewAccount(hoe.getMobile(), hbc.getOpenId(), hoe.getBankCard(),
                    hoe.getUserName(), hoe.getIdCard());
        }

        JSONObject jo = null;
        String response = null;
        try {
            response = SimpleHttpUtils.httpPost(
                    urlConfig.getHouseChatFCreateBill(),
                    SignUtil.chatEncode(JSONObject.toJSONString(hbc), urlConfig.getCommonPrivateKey()));
            logger.info("chatF responseMsg :{}", response);
            jo = JSONObject.parseObject(response);
        } catch (Exception e) {
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        if (jo != null && "success".equals(jo.getString("retCode"))) {
            orderBiz.updateHouseOrderHandler(updateHoe);
        } else {
            throw new DevDefineErrorMsgException(String.format("【账务系统】分期失败：%s", jo.getString("retCode")));
        }
    }

    private void processHBC(HouseBillCreateRequestMessage hbcr, HouseOrderEntity hoe, InstPlanEntity mpe,
            HouseBillChatFRequestPojo hbc) {
        hbc.setBeginDate(hbcr.getBeginDate());
        hbc.setContractAmt(unitChange(hoe.getApprovalAmt()));
        hbc.setInstallSumAmt(unitChange(hoe.getApprovalAmt()));
        hbc.setInstallAmt(unitChange(hbcr.getInstallAmt()));
        hbc.setInstallTerms(mpe.getTakePayment() == 1 ?
                hoe.getInstDetailList().size() + 1 :
                hoe.getInstDetailList().size()); // 上收月供 ? size + 1 : size;
        // 根据实际分期金额去分期
        hbc.setInstDetails(parse(hoe.getInstDetailList(), hbcr.getInstallAmt(), hoe.getMonthlyInterestType() == 1)); // 1年
                                                                                                                     // 2固定天数
        hbc.setMerchantNo(mpe.getMerchantNo());
        hbc.setOpenId(dictServiceImpl.getOpenIdByBizType(hoe.getBusinessType()));
        hbc.setOrderDate(DateUtil.parseYYYYMMDD(hoe.getCreatedTime()));
        hbc.setOrderId(hbcr.getOrderId());
        hbc.setPlanId(mpe.getPlanId());
        hbc.setRegId(hoe.getMobile());
        hbc.setTakeMonth(mpe.getTakePayment() == 1 ? 1 : 2);// 是否上收月供
    }

    private BigDecimal unitChange(BigDecimal amt) {
        return amt.multiply(new BigDecimal(10000));
    }

    /**
     * 生成账户系统分期详情
     * 
     * @param idps
     * @param amt 实际分期金额
     * @param isYear
     * @return
     */
    private List<InstdetailChatFPojo> parse(List<InstdetailPojo> idps, BigDecimal amt, boolean isYear) {
        List<InstdetailChatFPojo> idcs = new ArrayList<>();
        if (idps == null) {
            // 抛异常,不存在分期详情无法分期。
            return null;
        }
        for (InstdetailPojo idp : idps) {
            InstdetailChatFPojo idc = new InstdetailChatFPojo();
            idc.setDays(idp.getDays());
            idc.setRepayNo(idp.getRepayNo());
            if (isYear) {
                Double fee = idp.getFeeRatio() / 365 * idp.getDays();// 365 后续根据inst_plan
                                                                     // 表里面的days去区分365 还是360
                idp.setFeeRatio(fee);
            }
            idc.setInterest(BigDecimalUtil.format(unitChange(amt).multiply(new BigDecimal(idp.getFeeRatio()))));
            idcs.add(idc);
        }
        return idcs;
    }

    private Object queryHouseBillWfstatus100(JSONObject requestMessage, boolean isPage) {
        requestMessage.put("wfStatus", 100);
        requestMessage.put("dictTypeCode", "COMM_BIZ_TYPE");
        if ("全部商户".equals(requestMessage.getString("orgShortName"))) {
            requestMessage.remove("orgShortName");
        }
        return orderBiz.queryHouseBill(requestMessage, isPage);
    }

    @Override
    public InstOrderInfoEntity getIOIEByOid(String orderId) {
        return orderBiz.getIOIEByOid(orderId);
    }

    @Override
    public void updateIOIE(InstOrderInfoEntity ioie) {
        orderBiz.updateIOIE(ioie);
    }

    /**
     * 获取放款订单列表信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月23日
     */
    @Override
    public PageInfo<InstOrderBean> getLoanOrderList(JSONObject objs) {
        // // 获取商户权限的Objs
        // objs = getMerchLimitObject(objs);
        // if (objs == null) {
        // return null;
        // }
        return new PageInfo<>(orderBiz.getLoanOrderList(objs));
    }

    /**
     * 根据订单号修改放款日期
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月23日
     */
    @Override
    public long updateLoanDateByOrderIds(JSONObject objs) {
        objs.put("riskStatus", "7");
        return orderBiz.updateLoanDateByOrderIds(objs);
    }

    /**
     * 
     * 放款数据导出
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月23日
     */
    @Override
    public String exportLoanOrderList(JSONObject objs, HttpServletResponse response) {
        try {
            /*
             * // 获取商户权限的Objs objs = getMerchLimitObject(objs); if (objs == null) { return null; }
             */
            List<InstOrderBean> orderList = orderBiz.getLoanOrderListForExport(objs);

            // 2.导出excel表格
            HSSFWorkbook workbook = exportLoanOrderList(orderList);
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

    // 导出
    private HSSFWorkbook exportLoanOrderList(List<InstOrderBean> list) {
        String[] excelHeader = {"订单号", "姓名", "手机号", "订单名称", "订单时间", "金额", "期数", "方案", "首付款", "保证金", "服务费", "月供", "上收息",
                "上收月供", "预付款", "放款时间", "订单状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单信息页");

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

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            InstOrderBean orderBean = list.get(i);

            row.createCell(0).setCellValue(orderBean.getOrderId());
            row.createCell(1).setCellValue(orderBean.getRealName());
            row.createCell(2).setCellValue(orderBean.getRegId());
            row.createCell(3).setCellValue(orderBean.getOrderName());
            HSSFCell cell = row.createCell(4);
            cell.setCellValue(orderBean.getCreateTime());
            cell.setCellStyle(style);
            row.createCell(5).setCellValue(orderBean.getOrderAmt() == null ? "0.00" : orderBean.getOrderAmt());
            row.createCell(6).setCellValue(orderBean.getOrderItems());
            row.createCell(7).setCellValue(orderBean.getPlanShortName());
            row.createCell(8).setCellValue(orderBean.getDownPayment() == null ? "0.00" : orderBean.getDownPayment());
            row.createCell(9).setCellValue(orderBean.getMargin() == null ? "0.00" : orderBean.getMargin());
            row.createCell(10).setCellValue(orderBean.getServiceFee() == null ? "0.00" : orderBean.getServiceFee());
            row.createCell(11).setCellValue(
                    orderBean.getMonthInterest() == null ? new Double("0.00") : orderBean.getMonthInterest()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(12).setCellValue(orderBean.getFeeAmount() == null ? "" : orderBean.getFeeAmount() + "");
            row.createCell(13).setCellValue(orderBean.getTakePayment() == 1 ? "是" : "否");
            row.createCell(14).setCellValue(orderBean.getPreAmt() == null ? "0.00" : orderBean.getPreAmt());

            row.createCell(15).setCellValue(orderBean.getLoanDate());
            row.createCell(16).setCellValue(this.getRisk2Excel(orderBean.getRiskStatus()));
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

    private String getRisk2Excel(Integer riskStatus) {
        String result = "";
        if (null == riskStatus) {
            return "审核通过";
        }
        switch (riskStatus) {
            case 1:
                result = "审核拒绝";
                break;
            case 2:
                result = "审核中";
                break;
            case 3:
                result = "已分期";
                break;
            case 4:
                result = "已分期";
                break;
            case 5:
                result = "待确认";
                break;
            case 6:
                result = "已取消";
                break;
            case 7:
                result = "已放款";
                break;
            default:
                result = "审核通过";
                break;
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveOrUpdateOtherAmt(JSONObject requestMessage, Boolean isSave, Boolean isUpdateIoie)
            throws GenerallyException, DevDefineErrorMsgException {
        InstOrderOtherAmtEntity iooae = null;
        try {
            iooae = JSONObject.toJavaObject(requestMessage, InstOrderOtherAmtEntity.class);
            if (iooae == null || StringUtil.isEmpty(iooae.getOrderId())) {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("saveOrUpdateOtherAmt error:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        logger.debug("---保存订单金额信息---{}", iooae);
        iooae.checkInfo();
        if (isSave) {
            InstOrderOtherAmtEntity otherAmtBean = orderBiz.getOtherAmtEntity(iooae.getOrderId());
            if (otherAmtBean != null) {
                orderBiz.updateOtherAmtEntity(iooae);
            } else {
                orderBiz.saveOtherAmtEntity(iooae);
            }
        } else {
            int i = 0;
            if (iooae.getPreAmtFlag().equals("2")) {
                iooae.setAssessMsgAmt("0");
                iooae.setTotalCost(BigDecimal.ZERO);
                iooae.setGpsAmt(BigDecimal.ZERO);
                iooae.setRiskAmt(BigDecimal.ZERO);
                iooae.setInspectionAmt("0");
                iooae.setOtherAmt(BigDecimal.ZERO);
                iooae.setServerAmt(BigDecimal.ZERO);
                iooae.setTaxAmt(BigDecimal.ZERO);
                iooae.setPreBusinessTaxAmt(BigDecimal.ZERO);
                i = orderBiz.updateOtherAmtEntity(iooae);
            } else {
                i = orderBiz.updateOtherAmtEntity(iooae);
            }
            if (i != 1) {
                throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.B);
            }
        }

        /** 车秒贷 不需要update **/
        if (isUpdateIoie && iooae.getOnline() != null) {
            /** 以租代购 需要update **/
            /** 线上收取 需要将对应的金额附加到预付款上 即preAmt = preAmt+totalCount **/
            InstOrderInfoEntity ioie = orderBiz.getIOIEByOid(iooae.getOrderId());
            if (ioie == null) {
                throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
            }

            PlanDetailsPojo pdp = creditProductServiceImpl.getPlanDetails(ioie.getOrderAmt(), ioie.getPlanId());

            if (pdp == null) {
                throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
            }
            BigDecimal amt = pdp.getPreAmt();

            if (iooae.getOnline() == 1) {
                amt = BigDecimalUtil.format((pdp.getPreAmt() == null ? BigDecimal.ZERO : pdp.getPreAmt())
                        .add(iooae.getTotalCost() == null ? BigDecimal.ZERO : iooae.getTotalCost()));
            }
            orderBiz.updatePreamtByOid(iooae.getOrderId(), amt);
        }

    }

    @Override
    public Object getOtherAmtList(JSONObject requestMessage, Boolean isPage)
            throws GenerallyException, DevDefineErrorMsgException {
        if (isPage) {
            return new PageInfo<>(orderBiz.getOtherAmtList(requestMessage, isPage));
        }
        return orderBiz.getOtherAmtList(requestMessage, isPage);
    }

    @Override
    public void finishBill(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException {
        List<FinishBillRequestMessage> list = new ArrayList<>();
        FinishBillRequestMessage fbrm = null;
        try {
            fbrm = JSONObject.toJavaObject(requestMessage, FinishBillRequestMessage.class);
            if (fbrm == null || !fbrm.check()) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }

        InstOrderInfoEntity ioie = orderBiz.getIOIEByOid(fbrm.getOrderId());
        if (ioie == null) {
            throw new DevDefineErrorMsgException("未查询到相关订单");
        }
        if (ioie.getRiskStatus() == 10) {
            throw new DevDefineErrorMsgException("该订单记录状态为已结清，如有异议，建议查看相关账单状态");
        }
        BigDecimal amt = orderBiz.getFinishBillAmt(fbrm.getOrderId());
        if (amt == null) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }

        fbrm.setAmt(amt);

        // 如果订单存在车秒贷订单,也要同步结清
        OrderBean orderBean = orderBiz.selByOrderId(fbrm.getOrderId() + "X");
        FinishBillRequestMessage fbrmB = new FinishBillRequestMessage();
        ManageCarInfoBean manageBean = carStatusManager.selectOrderInfoByOrderId(requestMessage);
        
        list.add(fbrm);
        if (orderBean != null && manageBean!= null) {
            if(manageBean.getClearCarloanFlag() == 1){
                fbrmB.setOrderId(orderBean.getOrderId());
                fbrmB.setFinishTime(new Date());
                fbrmB.setAmt(amt);
            }            
            
            list.add(fbrmB);
        }
        logger.debug("要平账的信息---{}",JSONObject.toJSON(list));
        @SuppressWarnings("rawtypes")
        Map result = null;
        try {
            for (FinishBillRequestMessage bean : list) {
                result = SendHttpsUtil.postMsg4GetMap(urlConfig.getFinishBillUrl(),
                        JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(bean),
                                urlConfig.getCommonPrivateKey())));
            }

            logger.info("responseMessage :", JSONObject.toJSONString(result));
        } catch (Exception e) {
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }

        if (result.get("status").equals("ok")) {
            ioie = new InstOrderInfoEntity();
            ioie.setOrderId(fbrm.getOrderId());
            ioie.setRiskStatus(10);
            ioie.setUpdateTime(fbrm.getFinishTime());
            orderBiz.updateIOIE(ioie);
            // 更新车秒贷订单为已结清
            if (orderBean != null) {
                ioie.setOrderId(fbrm.getOrderId() + "X");
                orderBiz.updateIOIE(ioie);
            }
        } else {
            throw new DevDefineErrorMsgException("账务系统记账错误【 " + result.get("errMsg") + " 】");
        }
    }

    @Override
    public void saveOrUpdateOrderInfo(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException {
        InstOrderInfoEntity ioie = null;
        try {
            ioie = JSONObject.toJavaObject(requestMessage, InstOrderInfoEntity.class);
            if (ioie == null || !ioie.check(CheckGroup.A)) {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("saveOrUpdateOrderInfo:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }

    }

    @Override
    public PageInfo<CarOwnerOrderInfo> getCarOwnerOrderInfo(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException {
        return new PageInfo<>(orderBiz.getCarOwnerOrderInfo(requestMessage, true));
    }

    @Override
    public void exportCarOwnerOrderInfo(JSONObject requestMessage, HttpServletResponse response) {
        List<CarOwnerOrderInfo> xlsx = null;
        try {
            xlsx = JSONObject.parseArray(JSONObject.toJSONString(orderBiz.getCarOwnerOrderInfo(requestMessage, false)),
                    CarOwnerOrderInfo.class);
        } catch (Exception e) {
            response.setStatus(500);
            return;
        }
        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = null;
            String fileName = null;
            workbook = orderBiz.convertCarOwnerOrderInfoXlsx(xlsx);
            fileName = "attachment;filename=OrderInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    @Override
    public Map<String, Object> orderBreak(JSONObject objs) throws GenerallyException, DevDefineErrorMsgException {
        Map<String, Object> result = new HashMap<String, Object>();
        // 第一步分期要素组装
        String orderId = objs.getString("orderId"); // 订单号
        String beginDate = objs.getString("beginDate"); // 时间格式"yyyyMMdd"
        OrderBean orderBean = orderBiz.selectOne(objs);
        // 第二步调用账户系统分期
        // 2.1 先开户
        boolean accFlag = (boolean) backGroundService.openAccount(orderBean);
        if (!accFlag) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "用户尚未开户且开户失败");
            return result;
        }
        // 2.2 分期
        result = sendInstallRequest(objs, orderBean);
        // 第三步返回账户系统结果后处理
        if (FinanceConstant.SUCCESS.equals(result.get("retCode"))) {
            try {
                List<String> paraList = new ArrayList<String>();
                paraList.add(objs.getString("orderId"));
                objs.put("orderIds", paraList);
                objs.put("stageDate", beginDate);
                objs.put("riskStatus", "3"); // 已经分期
                orderBiz.updateLoanDateByOrderIds(objs);
            } catch (Exception e) {
                logger.error("修改数据库出现异常，但是账户系统已经分期:{}", e.getMessage());
                result.put("retCode", FinanceConstant.ERROR);
            }
        }
        return result;
    }

    private Map<String, Object> sendInstallRequest(JSONObject objs, OrderBean orderBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        String planId = orderBean.getPlanId();
        String orderId = orderBean.getOrderId();
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(planId));
        String installAmt = null;
        String interestAmt = null;
        try {
            Map<String, String> installInfoMap =
                    getInstallInfoMap(orderBean,
                            new BigDecimal(orderBean.getDownPayment() == null ? "0.00" : orderBean.getDownPayment()),
                            planBean);
            installAmt = installInfoMap.get("installAmt");
            interestAmt = installInfoMap.get("interestAmt");
        } catch (Exception e1) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "分期异常");
            return result;
        }

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderId", orderId);
        sourceMap.put("regId", orderBean.getRegId());
        sourceMap.put("orderDate",
                DateUtil.getDateString(orderBean.getCreateTime(), DateUtil.SHORT_DATE_FORMAT_NO_DASH));
        sourceMap.put(
                "beginDate",
                objs.getString("beginDate") == null ? DateUtil.getDateString(new Date(),
                        DateUtil.SHORT_DATE_FORMAT_NO_DASH) : objs.getString("beginDate"));
        sourceMap.put("openId", dictServiceImpl.getOpenIdByOrderId(orderId));
        sourceMap.put("merchantNo", orderBean.getMerchantNo());
        sourceMap.put("installSumAmt", installAmt);// 分期总本金
        sourceMap.put("installAmt", installAmt);// 本次分期本金
        sourceMap.put("interestAmt", interestAmt);// 剩余利息
        sourceMap.put("takeInterest", Double.parseDouble(interestAmt) > 0 ? "1" : "2");// 是否包含利息
        sourceMap.put("takeMonth", planBean.getTakePayment() == 1 ? "1" : "2");// 是否上收月供
        sourceMap.put("takePaymentAmt", BigDecimalUtil.add(orderBean.getMonthInterest(),
                orderBean.getGpsTrafficFee() == null ? new BigDecimal(0) : orderBean.getGpsTrafficFee()));// 上收月供金额
        sourceMap.put("takePayment", planBean.getTakePayment());// 上收月供数
        sourceMap.put("installTerms", planBean.getInstallPeriods());// 分期期数
        sourceMap.put("planId", planBean.getPlanId());
        sourceMap.put("otherAmt",
                orderBean.getGpsTrafficFee() == null ? new BigDecimal(0) : orderBean.getGpsTrafficFee()); // 其他费用
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceInstallInstUrl(),
                    encryptUtils.encrypt(sourceMap));
            // 根据平账结果返回成功与否
            result = JSONObject.parseObject(resultStr);
            logger.info("正常还款校验参数返回结果，返回参数:{}", result);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
        }

        return result;
    }

    private Map<String, String> getInstallInfoMap(OrderBean orderBean, BigDecimal downPayment, PlanBean planBean)
            throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        // 核准金额
        BigDecimal orderAmt = new BigDecimal(orderBean.getOrderAmt());
        // 剩余本金
        BigDecimal installAmt = BigDecimal.ZERO;
        // 剩余分期利息
        BigDecimal interestAmt = BigDecimal.ZERO;
        try {
            int takePayment = planBean.getTakePayment();// 是否上收月供
            int installPeriods = planBean.getInstallPeriods();// 分期期数
            BigDecimal feeRatio =
                    new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP);// 费率
            BigDecimal floatRatio =
                    new BigDecimal(planBean.getFloatMarginRatio()).divide(new BigDecimal("100"), 6,
                            BigDecimal.ROUND_HALF_UP);// 费率
            int feeYear = planBean.getFeeYear();// 上收息对应的月数
            // 上浮金额 = 核准金额*上浮比例
            BigDecimal floatAmt = BigDecimalUtil.mul(orderAmt, floatRatio);
            // 上标金额 = 核准金额+上浮金额+GPS安装费用
            BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, floatAmt, orderBean.getGpsAmt());
            // 实际剩余的本金 = 总本金-首付款-->车主贷(核准金额+上浮金额+GPS安装费用-首付款)
            BigDecimal calAmt = BigDecimalUtil.sub(sbAmt, downPayment);
            // 每月利息 = (总本金-首付款)*上收费率-->车主贷(实际剩余的本金*月利率)
            BigDecimal preInte = BigDecimalUtil.mul(calAmt, feeRatio);
            // 上收利息 = 每月利息*上收月数
            BigDecimal subInt = BigDecimalUtil.mul(preInte, new BigDecimal(feeYear));
            // 上收本金 = (总本金-首付款)/分期期数 * (是否上收月供(1或0))-->车主贷(实际剩余本金/实际分期期数)
            BigDecimal subAmt =
                    BigDecimalUtil.mul(BigDecimalUtil.div(calAmt, new BigDecimal(installPeriods)), new BigDecimal(
                            takePayment));
            // 总利息 = 每月利息*分期期数
            BigDecimal allInte = BigDecimalUtil.mul(preInte, new BigDecimal(installPeriods));
            // 上收月供中的利息 = (总利息 - 上收利息)/分期期数 * (是否上收月供(1或0))
            BigDecimal subMonInte = BigDecimalUtil.mul(
                    BigDecimalUtil.div(BigDecimalUtil.sub(allInte, subInt), new BigDecimal(installPeriods)),
                    new BigDecimal(takePayment));
            // 剩余本金 = 总本金 - 首付款 - 上收本金－－》车主贷(实际剩余本金-上收本金)
            installAmt = BigDecimalUtil.sub(calAmt, subAmt);
            // 剩余分期利息 = 总利息 - 上收利息 - 上收月供中的利息
            interestAmt = BigDecimalUtil.sub(allInte, BigDecimalUtil.add(subInt, subMonInte));
        } catch (Exception e) {
            logger.error("计算传给账户系统的本金和剩余利息出现异常:{}", e);
            throw new Exception("计算传给账户系统的本金和剩余利息出现异常");
        } finally {}
        logger.debug("分期传给账户系统剩余本金:{},剩余分期利息:{}", installAmt, interestAmt);
        result.put("installAmt", installAmt + "");
        result.put("interestAmt", interestAmt + "");
        return result;
    }

    @Override
    public PageInfo<CarOwnerCarInfo> getCarOwnerCarInfoPage(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException {
        return new PageInfo<>(orderBiz.getCarOwnerCarInfo(requestMessage, true));
    }

    @Override
    public void exportCarOwnerCarInfo(JSONObject requestMessage, HttpServletResponse response) {
        List<CarOwnerCarInfo> xlsx = null;
        try {
            xlsx = JSONObject.parseArray(JSONObject.toJSONString(orderBiz.getCarOwnerCarInfo(requestMessage, false)),
                    CarOwnerCarInfo.class);
        } catch (Exception e) {
            response.setStatus(500);
            return;
        }
        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = null;
            String fileName = null;
            workbook = orderBiz.convertCarOwnerCarInfoXlsx(xlsx);
            fileName = "attachment;filename=CarInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    public static void main(String[] args) {
        Map result =
                SendHttpsUtil
                        .postMsg4GetMap(
                                "http://uat.ishandian.cn:8081/consumer.virtual.account/bill/finishBill",
                                "\"data\":\"lwc1x1mk0yPB8NcnBxMmJ/rF0WkXplXr/iXDZCfTUls4ZmXEt53ANBZscLE6fggJN5Jiystpxf/B0HWOYzIcyl+CJ6O01OnHtsox7QSU+3By6witRSWDargFAax/wSdzX3SU0qPHayZ84iHg/ksHLMI9zOw1hvjQBKx7UF+xUQg=\",\"sign\":\"aLuR8yUxvDC/d+YwmronishH7+y50oA6VnzyqiYPmuvnLcSnzWLocsnC4eTb5p8heOV1thEpv0Q08uq1W0mPBV1aFJsoD2TU/7c/GdvD3VnJkZumRWbzdTmgs+gBdjzq6KwIhgvd6VsbNAmskBrdCn+lZVCcvpdSrVDc9ZAtYE4=\"}");
        System.out.println(JSONObject.toJSONString(result));
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月29日
     */
    @Override
    public PageInfo<CarOwnerOrderInfo> getOrderBreakInfo(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException {
        return new PageInfo<>(orderBiz.getOrderBreakInfo(requestMessage, true));
    }

    /**
     * 查询订单总个数,总金额 2018年1月4日 16:59:09
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Map getOrderInfoByListTotal(JSONObject objs) {

        Map map = orderBiz.getOrderInfoByListTotal(objs);
        return map;
    }

    public OrderBean getOrderInfo(JSONObject objs) {

        return orderBiz.getOrderInfo(objs);
    }

    @Override
    public int updateOrderInfo1(OrderBean objs) {
        return orderBiz.updateOrderInfo1(objs);
    }

    @Override
    public Map getLoanOrderListTotal(JSONObject objs) {
        /*
         * // 获取商户权限的Objs objs = getMerchLimitObject(objs); if (objs == null) { return null; }
         */
        return orderBiz.getLoanOrderListTotal(objs);
    }

    @Override
    public PageInfo<InstOrderBean> getSelectLoanOrderList(JSONObject objs) {
        return new PageInfo<>(orderBiz.getSelectLoanOrderList(objs));
    }

    @Override
    public String exportSelectLoanOrderList(JSONObject objs, HttpServletResponse response) {
        try {

            List<InstOrderBean> orderList = orderBiz.getSelectLoanOrderListForExport(objs);

            // 2.导出excel表格
            HSSFWorkbook workbook = exportLoanOrderList2(orderList);
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

    // 导出
    private HSSFWorkbook exportLoanOrderList2(List<InstOrderBean> list) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "订单名称", "订单时间", "业务类型", "金额", "总期数", "产品方案", "预付款", "月供", "放款时间", "放款主体", "商户名称"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单信息页");

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

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            InstOrderBean orderBean = list.get(i);

            row.createCell(0).setCellValue(orderBean.getOrderId());
            row.createCell(1).setCellValue(orderBean.getRealName());
            row.createCell(2).setCellValue(orderBean.getRegId());
            row.createCell(3).setCellValue(orderBean.getOrderName());
            HSSFCell cell = row.createCell(4);
            cell.setCellValue(orderBean.getCreateTime());
            cell.setCellStyle(style);
            row.createCell(5).setCellValue(getBizType2Excel(orderBean.getBizType()));
            row.createCell(6).setCellValue(orderBean.getOrderAmt() == null ? "0.00" : orderBean.getOrderAmt());
            row.createCell(7).setCellValue(orderBean.getOrderItems());
            row.createCell(8).setCellValue(orderBean.getPlanShortName());
            row.createCell(9).setCellValue(orderBean.getPreAmt() == null ? "0.00" : orderBean.getPreAmt());
            row.createCell(10).setCellValue(
                    orderBean.getMonthInterest() == null ? new Double("0.00") : orderBean.getMonthInterest()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(11).setCellValue(orderBean.getLoanDate());
            row.createCell(12).setCellValue(
                    orderBean.getLendersSubject() == null ? "" : getSysItem2Excel("Lenders_Subject",
                            orderBean.getLendersSubject()));
            row.createCell(13).setCellValue(orderBean.getMerchantFullName());

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

    // 根据字典value查询名称
    private String getSysItem2Excel(String itemCode, String value) {
        return orderBiz.getSysItem2Excel(itemCode, value);
    }

    private String getBizType2Excel(String riskStatus) {
        String result = "";
        if (null == riskStatus) {
            return "";
        }
        switch (riskStatus) {
            case "2001":
                result = "以租代购新车";
                break;
            case "2002":
                result = "以租代购二手车";
                break;
            case "2100":
                result = "抵押车";
                break;
            case "2200":
                result = "质押车";
                break;
            case "1100":
                result = "易安家";
                break;
            case "1000":
                result = "医美";
                break;
            case "1200":
                result = "旅游";
                break;
            case "2300":
                result = "信用贷车秒贷";
                break;
            case "9001":
                result = "流贷海鲜帮";
                break;
            case "9002":
                result = "流贷娱乐帮";
                break;
            case "3001":
                result = "收益权抵押类";
                break;
            case "9000":
                result = "消费金融";
                break;
            case "8001":
                result = "中阁资产";
                break;
            case "8002":
                result = "  过桥垫资";
                break;
            case "8003":
                result = "房屋质押一抵";
                break;
            case "8004":
                result = "房屋质押二抵";
                break;
            case "2400":
                result = "车主贷";
                break;
            case "3002":
                result = "华益周转贷";
                break;
            default:
                result = "暂无配置此bizType";
                break;
        }
        return result;
    }

    @Override
    public InstOrderBean getOrderInfoNew(JSONObject objs) {
        return orderBiz.getOrderInfoNew(objs);
    }

    @Override
    public void saveApprovalOpinion(JSONObject objs) {
        orderBiz.saveApprovalOpinion(objs);
    }

    @Override
    public Map<String, String> getApprovalOpinion(JSONObject objs) {
        return orderBiz.getApprovalOpinion(objs);
    }
}
