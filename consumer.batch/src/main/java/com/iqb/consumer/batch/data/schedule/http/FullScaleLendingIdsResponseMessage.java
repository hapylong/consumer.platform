package com.iqb.consumer.batch.data.schedule.http;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.github.pagehelper.StringUtil;
import com.iqb.consumer.batch.data.pojo.InstOrderInfoEntity;
import com.iqb.consumer.batch.data.schedule.pojo.CfRequestMoneyPojo;

public class FullScaleLendingIdsResponseMessage {
    private String orderId;
    private String creditTime; // 放款时间
    private String fullTime; // 满标时间
    private BigDecimal amt; // 放款金额
    private Double rate;
    private String borrowId; // 标id

    @Column(name = "createtime")
    private String orderDate; // 订单创建时间 deal

    @Column(name = "regid")
    private String regId; // 用户手机号 deal
    private String beginDate; // 起息日 deal

    @Column(name = "biztype")
    private String bizType; // deal
    private String openId; // deal

    @Column(name = "merchantno")
    private String merchantNo; // 商户号 deal

    @Column(name = "orderamt")
    private BigDecimal contractAmt; // 合同金额 deal

    private BigDecimal installSumAmt; // 放款金额 deal?
    private BigDecimal installAmt; // 放款金额 deal?

    @Column(name = "orderitems")
    private Integer installTerms; // 期数 deal

    @Column(name = "planid")
    private String planId; // inst_plan表中的planId deal
    private String fundId; // 标ID
    private final Integer sourcesFunding = 2; // 资金来源 1 饭饭 2 爱钱帮
    List<Plan> plan;

    public static void init() {
        repository = new HashMap<String, CfRequestMoneyPojo>();
    }

    private static Map<String, CfRequestMoneyPojo> repository = new HashMap<String, CfRequestMoneyPojo>();

    public static CfRequestMoneyPojo getRepository(String orderid) {
        return repository.get(orderid);
    }

    public static void setRepository(String orderId, CfRequestMoneyPojo cf) {
        FullScaleLendingIdsResponseMessage.repository.put(orderId, cf);
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = convert(orderDate);
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = convert(beginDate);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
    }

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public Integer getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(Integer installTerms) {
        this.installTerms = installTerms;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public Integer getSourcesFunding() {
        return sourcesFunding;
    }

    public void setCreditTime(String creditTime) {
        this.creditTime = creditTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }

    /****/

    private String convert(Date d) {
        return formatter.format(d);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreditTime() {
        return creditTime;
    }

    public void setCreditTime(Integer i) {
        this.creditTime = convert(i);
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(Integer i) {
        this.fullTime = convert(i);
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        installSumAmt = amt;
        installAmt = amt;
        this.amt = amt;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public List<Plan> getPlan() {
        return plan;
    }

    public void setPlan(List<Plan> plan) {
        this.plan = plan;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        fundId = borrowId;
        this.borrowId = borrowId;
    }

    private final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private String convert(Integer i) {
        return formatter.format(new Date(i * 1000L));
    }

    public boolean integration(InstOrderInfoEntity ioie) {
        if (ioie.getCreateTime() == null ||
                ioie.getBizType() == null ||
                ioie.getOrderAmt() == null ||
                ioie.getOrderItems() == null ||
                ioie.getPlanId() == null ||
                StringUtil.isEmpty(ioie.getMerchantNo()) ||
                StringUtil.isEmpty(ioie.getRegId())) {
            return false;
        }
        this.orderDate = convert(ioie.getCreateTime());
        bizType = ioie.getBizType();
        contractAmt = ioie.getOrderAmt();
        installTerms = ioie.getOrderItems();
        planId = ioie.getPlanId().toString();
        merchantNo = ioie.getMerchantNo();
        regId = ioie.getRegId();
        return true;
    }

    public boolean check() {
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(creditTime) ||
                StringUtil.isEmpty(fullTime) ||
                amt == null ||
                rate == null ||
                plan == null ||
                plan.isEmpty()) {
            return false;
        }
        for (Plan p : plan) {
            if (p.getCapital() == null ||
                    StringUtil.isEmpty(p.getFinalRepaymentDate()) ||
                    p.getInterest() == null ||
                    p.getNumber() == null) {
                return false;
            }
        }
        return true;
    }
}
