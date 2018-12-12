package com.iqb.consumer.asset.allocation.assetallocine.request;

import java.math.BigDecimal;
import java.util.Date;

public class DivisionAssetsDetailsRequestMessage {
    private String orderId = null; // 资产包编号 -> orderId
    private String raiseInstitutions = null; // 募集机构
    private String guaranteeInstitution = null; // 担保机构
    private Date expireDate = null; // 到期日
    private BigDecimal bOrderAmtBegin = null; // 拆分后的订单金额范围
    private BigDecimal bOrderAmtEnd = null; // 拆分后的订单金额范围
    // FINANCE-2972交易所资产打包/资产拆分/资产分配页面中筛选条件增加业务类型；
    private String bizType = null;// 业务类型

    private String proBeginDateBegin;
    private String proBeginDateEnd;
    private String channel;// 资金来源
    
    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRaiseInstitutions() {
        return raiseInstitutions;
    }

    public void setRaiseInstitutions(String raiseInstitutions) {
        this.raiseInstitutions = raiseInstitutions;
    }

    public String getGuaranteeInstitution() {
        return guaranteeInstitution;
    }

    public void setGuaranteeInstitution(String guaranteeInstitution) {
        this.guaranteeInstitution = guaranteeInstitution;
    }

    public BigDecimal getBOrderAmtBegin() {
        return bOrderAmtBegin;
    }

    public void setBOrderAmtBegin(BigDecimal bOrderAmtBegin) {
        this.bOrderAmtBegin = bOrderAmtBegin;
    }

    public BigDecimal getBOrderAmtEnd() {
        return bOrderAmtEnd;
    }

    public void setBOrderAmtEnd(BigDecimal bOrderAmtEnd) {
        this.bOrderAmtEnd = bOrderAmtEnd;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getbOrderAmtBegin() {
        return bOrderAmtBegin;
    }

    public void setbOrderAmtBegin(BigDecimal bOrderAmtBegin) {
        this.bOrderAmtBegin = bOrderAmtBegin;
    }

    public BigDecimal getbOrderAmtEnd() {
        return bOrderAmtEnd;
    }

    public void setbOrderAmtEnd(BigDecimal bOrderAmtEnd) {
        this.bOrderAmtEnd = bOrderAmtEnd;
    }

    public String getProBeginDateBegin() {
        return proBeginDateBegin;
    }

    public void setProBeginDateBegin(String proBeginDateBegin) {
        this.proBeginDateBegin = proBeginDateBegin;
    }

    public String getProBeginDateEnd() {
        return proBeginDateEnd;
    }

    public void setProBeginDateEnd(String proBeginDateEnd) {
        this.proBeginDateEnd = proBeginDateEnd;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
}
