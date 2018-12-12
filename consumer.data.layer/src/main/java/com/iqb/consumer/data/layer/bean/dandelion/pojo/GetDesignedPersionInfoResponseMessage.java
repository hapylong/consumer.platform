package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.dandelion.entity.InstCreditInfoEntity;

public class GetDesignedPersionInfoResponseMessage {

    private InstCreditInfoEntity icie;
    private RecalculateAmtPojo rap;
    private String planShortName;
    private Long planId;
    private BigDecimal orderAmt;
    private CreditorInfoBean creditorInfoBean;

    public CreditorInfoBean getCreditorInfoBean() {
        return creditorInfoBean;
    }

    public void setCreditorInfoBean(CreditorInfoBean creditorInfoBean) {
        this.creditorInfoBean = creditorInfoBean;
    }

    public InstCreditInfoEntity getIcie() {
        return icie;
    }

    public void setIcie(InstCreditInfoEntity icie) {
        this.icie = icie;
    }

    public RecalculateAmtPojo getRap() {
        return rap;
    }

    public void setRap(RecalculateAmtPojo rap) {
        this.rap = rap;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
