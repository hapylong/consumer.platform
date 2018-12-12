package com.iqb.consumer.manage.front.exchange;

/**
 * 风险承受能力标准 Created by ckq.
 */
public class RiskVo {

    private Integer riskBearAbility;

    private Integer needPoint;

    private Integer riskKind;

    public Integer getRiskBearAbility() {
        return riskBearAbility;
    }

    public void setRiskBearAbility(Integer riskBearAbility) {
        this.riskBearAbility = riskBearAbility;
    }

    public Integer getNeedPoint() {
        return needPoint;
    }

    public void setNeedPoint(Integer needPoint) {
        this.needPoint = needPoint;
    }

    public Integer getRiskKind() {
        return riskKind;
    }

    public void setRiskKind(Integer riskKind) {
        this.riskKind = riskKind;
    }
}
