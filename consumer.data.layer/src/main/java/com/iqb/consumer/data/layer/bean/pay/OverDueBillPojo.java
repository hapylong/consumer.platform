package com.iqb.consumer.data.layer.bean.pay;

import java.math.BigDecimal;

import com.iqb.consumer.common.utils.RandomUtils;

import jodd.util.StringUtil;

public class OverDueBillPojo {
    private String orderId;
    private Integer repayNo; // 应还期数
    private BigDecimal amt; // 应还金额

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String generateTradeNo() {
        return orderId + "@" + repayNo + "@" + RandomUtils.randomInt(8);
    }

    /**
     * 
     * Description: 通过账务中心回调 tradeNo分析出orderId;
     * 
     * @param
     * @return String
     * @throws @Author adam Create Date: 2017年6月21日 下午5:04:38
     */
    public static String getOidByTradeNo(String tradeNo) {
        if (!StringUtil.isEmpty(tradeNo)) {
            return tradeNo.split("@").length == 3 ? tradeNo.split("@")[0] : "";
        }
        return "";
    }

    /**
     * 
     * Description: 通过账务中心回调 tradeNo分析出repayNo;
     * 
     * @param
     * @return String
     * @throws @Author adam Create Date: 2017年6月21日 下午5:04:38
     */
    public static String getRNByTradeNo(String tradeNo) {
        if (!StringUtil.isEmpty(tradeNo)) {
            return tradeNo.split("@").length == 3 ? tradeNo.split("@")[1] : "";
        }
        return "";
    }
}
