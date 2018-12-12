/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description:
 * @date 2016年9月13日 上午10:21:08
 * @version V1.0
 */
package com.iqb.consumer.service.layer.xfpay;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.bean.bank.BankCardTypeBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardTypeBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.data.layer.biz.xfpay.PayInfoBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.pay.PayService;
import com.iqb.etep.common.utils.DateTools;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service("xfPayService")
public class XFPayServiceImpl extends BaseService implements XFPayService {

    protected static final Logger logger = LoggerFactory
            .getLogger(XFPayServiceImpl.class);

    @Resource
    private PayInfoBiz payInfoBiz;
    @Resource
    private BankCardTypeBiz bankCardTypeBiz;
    @Resource
    private UserBeanBiz userBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private PaymentLogBiz paymentLogBiz;
    @Resource
    private MerchantBeanBiz merchantBiz;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Resource
    private PayService payService;

    // @Autowired
    // private SysUserSession sysUserSession;

    @Override
    public UserBean getUserInfo(long id) {
        return userBiz.getUserInfo(id);
    }

    @Override
    public OrderBean getOrderInfo(Map<String, Object> params) {
        return orderBiz.selectOne(params);
    }

    @Override
    public void insertIfNotExists(PayInfoBean payInfoBean) {

    }

    @Override
    public PayInfoBean getByIdAndOrgId(Map<String, Object> params) {
        return payInfoBiz.getByIdAndOrgId(params);
    }

    @Override
    public List<PayInfoBean> getByOrgId(int orgId) {
        return payInfoBiz.getByOrgId(orgId);
    }

    @Override
    public long insertPayInfo(PayInfoBean payInfoBean) {
        return payInfoBiz.insertPayInfo(payInfoBean);
    }

    @Override
    public PayInfoBean getByCardNo(String cardNo, String orgId) {
        return payInfoBiz.getByCardNo(cardNo, orgId);
    }

    @Override
    public List<BankCardTypeBean> getAllBankType() {
        return bankCardTypeBiz.getAllBankType();
    }

    @Override
    public void unBindCardStatus(Map<String, Object> params) {
        payInfoBiz.unBindCardStatus(params);
    }

    @Override
    public long insertPaymentLog(Map<String, String> params) {
        return paymentLogBiz.insertPaymentLog(params);
    }

    @Override
    public int updateOrderInfo(OrderBean orderBean) {
        return orderBiz.updateOrderInfo(orderBean);
    }

    @Override
    public PaymentLogBean getPayLogByTradeNo(String tradeNo) {
        return paymentLogBiz.getPayLogByTradeNo(tradeNo);
    }

    @Override
    public int updatePreStatusByNO(Map<String, Object> params) {
        return orderBiz.updatePreStatusByNO(params);
    }

    @Override
    public int updateBindCardInfo(PayInfoBean payInfoBean) {
        return payInfoBiz.updateBindCardInfo(payInfoBean);
    }

    @Override
    public void delBindCardInfo(int bankId) {
        payInfoBiz.delBindCardInfo(bankId);
    }

    @Override
    public List<PaymentLogBean> getPayLogByOrderId(String orderId, String flag) {
        return paymentLogBiz.getPayLogByOrderId(orderId, flag);
    }

    @Override
    public PageInfo<PaymentLogBean> getMersPaymentLogList(JSONObject objs) {

        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }

        List<PaymentLogBean> list = paymentLogBiz.getMersPaymentLogList(objs);

        /* 银行卡号隐匿 */
        for (PaymentLogBean PL : list) {
            // PL.setAmount(PL.getAmount() / 100); 前台处理
            if (null == PL.getBankCardNo()) {
                continue;
            } else {
                String bankCardNo = PL.getBankCardNo();
                PL.setBankCardNo(bankCardNo.substring(0, 3) + "********"
                        + bankCardNo.substring(bankCardNo.length() - 3));
            }
        }
        return new PageInfo<PaymentLogBean>(list);
    }

    @Override
    public Map<String, Object> getMersPaymentLogNumAmt(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        int num = paymentLogBiz.getMersPaymentLogNum(objs);
        Long amt = paymentLogBiz.getMersPaymentLogAmt(objs);
        m.put("num", num);
        m.put("amt", amt);
        // m.put("amt", amt / 100); 前台处理

        return m;
    }

    @Override
    public void getMersPaymentLogListForSave(JSONObject objs,
            HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            objs = getMerchLimitObject(objs);
            if (objs == null) {
                return;
            }

            List<PaymentLogBean> list = paymentLogBiz
                    .getMersPaymentLogListForSave(objs);

            /* 银行卡号隐匿 */
            for (PaymentLogBean PL : list) {
                if (null == PL.getBankCardNo()) {
                    continue;
                } else {
                    String bankCardNo = PL.getBankCardNo();
                    PL.setBankCardNo(bankCardNo.substring(0, 3) + "********"
                            + bankCardNo.substring(bankCardNo.length() - 3));
                }
            }
            // 导出excel表格
            HSSFWorkbook workbook = this.exportDownPaymentList(list);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=PayPayment-"
                    + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出支付日志列表出现异常:{}", e);
            return;
        }
        return;
    }

    // 导出
    private HSSFWorkbook exportDownPaymentList(List<PaymentLogBean> list) {
        String[] excelHeader = {"订单号", "手机号", "客户", "金额", "还款日期", "第三方流水号",
                "银行卡号", "银行名称", "商户", "支付类型"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("支付流水信息页");

        HSSFRow row = sheet.createRow((int) 0);
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
            PaymentLogBean plb = list.get(i);
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            // 订单号 手机号 借款人 金额 还款日期 第三方流水号 银行卡号 卡户银行 商户 支付类型
            row.createCell(0).setCellValue(plb.getOrderId());
            row.createCell(1).setCellValue(plb.getRegId());
            row.createCell(2).setCellValue(plb.getRealName());
            row.createCell(3).setCellValue(df.format(
                    Double.parseDouble(Long.toString(plb.getAmount())) / 100));
            String sTranTime = "";
            if (null != plb.getTranTime()) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                sTranTime = sdf.format(plb.getTranTime());
            }
            row.createCell(4).setCellValue(sTranTime);
            row.createCell(5).setCellValue(plb.getOutOrderId());
            row.createCell(6).setCellValue(plb.getBankCardNo());
            row.createCell(7).setCellValue(plb.getBankName());
            row.createCell(8).setCellValue(plb.getMerchantShortName());
            row.createCell(9).setCellValue(this.getPayType(plb.getFlag()));
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 3500);// 手机号
        sheet.setColumnWidth(2, 2000);// 姓名
        for (int j = 3; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }

        return wb;
    }

    // 获取资金来源
    private String getPayType(int val) {
        if (val != 0) {
            if (11 == val) {
                return "用户支付预付款";
            } else if (12 == val) {
                return "商户代偿预付款";
            } else if (13 == val) {
                return "线下平账预付款";
            } else if (21 == val) {
                return "用户支付分期还款";
            } else if (22 == val) {
                return "商户代偿分期还款";
            } else if (23 == val) {
                return "线下平账分期付款";
            }
            return null;
        }
        return null;
    }

    @Override
    public void prePay(String orderId, BigDecimal amount) {
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        amount = BigDecimalUtil.div(amount, new BigDecimal(100)); // 拆分支付金额
        BigDecimal receivedPreAmt =
                orderBean.getReceivedPreAmt() == null ? BigDecimal.ZERO : orderBean.getReceivedPreAmt();// 已经支付的拆分金额
        BigDecimal preAmt =
                orderBean.getPreAmt() == null ? BigDecimal.ZERO : new BigDecimal(orderBean.getPreAmt()); // 总预付款金额
        BigDecimal receivedAllAmt = BigDecimalUtil.format(amount.add(receivedPreAmt));// 总预付款
        Map<String, Object> preInfo = new HashMap<>();
        preInfo.put("receivedPreAmt", receivedAllAmt);
        preInfo.put("orderId", orderId);
        if (preAmt.compareTo(receivedAllAmt) > 0) {// 预付款未支付完成
            preInfo.put("preAmtStatus", orderBean.getPreAmtStatus());
            preInfo.put("riskStatus", orderBean.getRiskStatus());
            // 修改receivedPreAmt = amount.add(receivedPreAmt)
            orderBiz.updatePreInfo(preInfo);
        } else {
            preInfo.put("preAmtStatus", 1);
            preInfo.put("riskStatus", 0);
            // 修改预付款同时保存订单状态
            orderBiz.updatePreInfo(preInfo);
        }
    }

    @Override
    public void settlePay(SettleApplyBean settleApplyBean, String id, BigDecimal amount, Map<String, String> params,
            String merchantNo) {
        amount = BigDecimalUtil.div(amount, new BigDecimal(100)); // 退租支付金额
        BigDecimal receivedPreAmt =
                settleApplyBean.getReceiveAmt() == null ? BigDecimal.ZERO : settleApplyBean.getReceiveAmt();// 已经支付的拆分金额
        BigDecimal preAmt =
                settleApplyBean.getTotalRepayAmt() == null ? BigDecimal.ZERO : settleApplyBean.getTotalRepayAmt(); // 总预付款金额
        BigDecimal receivedAllAmt = BigDecimalUtil.format(amount.add(receivedPreAmt));// 总预付款
        Map<String, Object> preInfo = new HashMap<>();
        preInfo.put("receiveAmt", receivedAllAmt);
        preInfo.put("id", id);
        if (preAmt.compareTo(receivedAllAmt) > 0) {// 预付款未支付完成
            preInfo.put("amtStatus", settleApplyBean.getAmtStatus());
        } else {
            preInfo.put("amtStatus", 1);
            payService.refundBills(settleApplyBean.getOrderId(), params, merchantNo);
        }
        settleApplyBeanBiz.updateSettleApply(preInfo);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月27日
     */
    @Override
    public Integer selSumAmount(JSONObject objs) {
        return paymentLogBiz.selSumAmount(objs);
    }

    // 获取商户权限的Objs
    // private JSONObject getMerchLimitObject(JSONObject objs) {
    // // 权限树修改
    // String merchName = objs.getString("merchName");
    // if (merchName == null || "".equals(merchName) ||
    // "全部商户".equals(merchName)) { // 未选择从session中获取
    // List<MerchantBean> merchList = getAllMerListByOrgCode(objs);
    // if (merchList == null || merchList.size() == 0) {
    // return null;
    // }
    // objs.put("merchList", merchList);
    // /*
    // * } else if ("全部商户".equals(merchName)) { // 最高权限用户(包含无商户信息）
    // * objs.put("merchList", null);
    // */
    // } else {
    // objs.put("merchantShortName", objs.getString("merchName"));
    // String merchantNo = merchantBiz.getMerCodeByMerSortName(objs);
    // MerchantBean mb = new MerchantBean();
    // mb.setMerchantNo(merchantNo);
    // List<MerchantBean> merchList = new ArrayList<MerchantBean>();
    // merchList.add(mb);
    // objs.put("merchList", merchList);
    // }
    // return objs;
    // }

    // // 获取登录商户下所有子商户列表
    // private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
    // String orgCode = sysUserSession.getOrgCode();
    // objs.put("id", orgCode);
    // List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
    // return list;
    // }
}
