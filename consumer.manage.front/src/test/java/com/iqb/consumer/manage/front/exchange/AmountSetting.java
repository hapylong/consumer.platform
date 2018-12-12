package com.iqb.consumer.manage.front.exchange;

/**
 * Created by ckq.
 */
public class AmountSetting {

    private Integer customKind;

    private Double minAmount;

    private Double appendAmount;

    public Integer getCustomKind() {
        return customKind;
    }

    public void setCustomKind(Integer customKind) {
        this.customKind = customKind;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getAppendAmount() {
        return appendAmount;
    }

    public void setAppendAmount(Double appendAmount) {
        this.appendAmount = appendAmount;
    }
}
