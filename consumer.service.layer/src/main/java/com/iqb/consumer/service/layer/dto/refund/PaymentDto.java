/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月16日 下午4:40:27
 * @version V1.0
 */
package com.iqb.consumer.service.layer.dto.refund;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 还款数据传输类
 * 
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDto {

    private String orderId;// 订单号
    private String openId;// 开户号
    private String repayModel;// 还款模式
    private String repayType;// 还款方式
    private String bankCardNo;// 还款银行号
    private String bankName;// 还款银行名称
    private String regId;// 手机号
    private String merchantNo;// 商户号
    private String tradeNo;// 流水号
    private Date repayDate;// 还款时间
    private BigDecimal sumAmt;// 总还款金额
    List<RepayList> repayList;// 还款列表

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<RepayList> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<RepayList> repayList) {
        this.repayList = repayList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public BigDecimal getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(BigDecimal sumAmt) {
        this.sumAmt = sumAmt;
    }

    public String getRepayModel() {
        return repayModel;
    }

    public void setRepayModel(String repayModel) {
        this.repayModel = repayModel;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

}
