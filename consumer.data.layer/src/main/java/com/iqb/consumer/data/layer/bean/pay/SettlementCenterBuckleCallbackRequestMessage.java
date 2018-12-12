package com.iqb.consumer.data.layer.bean.pay;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

/**
 * 
 * Description: 结算中心代扣回调 pojo
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月21日    adam       1.0        1.0 Version 
 * </pre>
 */
public class SettlementCenterBuckleCallbackRequestMessage extends BaseEntity {
    public static final int SUCCESS_ALL = 1;
    public static final int FAIL = 2;
    public static final int SUCCESS_PART = 3;

    private static final long serialVersionUID = 1L;

    private String tradeNo; // 订单号
    private Integer status; // 状态 （1成功 2失败 3部分成功）
    private BigDecimal succAmt; // 成功金额
    private BigDecimal failAmt; // 失败金额

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getSuccAmt() {
        return succAmt;
    }

    public void setSuccAmt(BigDecimal succAmt) {
        this.succAmt = succAmt;
    }

    public BigDecimal getFailAmt() {
        return failAmt;
    }

    public void setFailAmt(BigDecimal failAmt) {
        this.failAmt = failAmt;
    }

    public boolean checkRequest() {
        print();
        if (StringUtil.isEmpty(tradeNo) ||
                status == null ||
                succAmt == null ||
                failAmt == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SettlementCenterBuckleCallbackRequestMessage [tradeNo=" + tradeNo + ", status=" + status + ", succAmt="
                + succAmt + ", failAmt=" + failAmt + "]";
    }

}
