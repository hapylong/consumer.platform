/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月23日下午6:09:56 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.bean.settlementresult;

import java.math.BigDecimal;

import javax.persistence.Table;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.pay.SettlementCenterBuckleCallbackRequestMessage;

/**
 * @author haojinlong
 * 
 */
@Table(name = "inst_settlementresult")
public class SettlementResultBean extends BaseEntity {
    private static final long serialVersionUID = 38339231404352387L;

    private String orderId; // 订单号
    private String tradeNo; // 流水号
    private int repayNo; // 还款期数
    private String openId; // 开户ID
    private BigDecimal curRepayAmt; // 当期还款金额
    private int tradeType; // 交易类型 1,划扣正常月供 2 划扣逾期月供
    private int status; // 状态 1,未发送 2 发送成功,3,划扣成功，4，划扣部分成功，5，划扣失败
    private int number; //

    /**
     * @author adam
     */
    public static final int NOT_SEND = 1;
    public static final int SEND = 2;
    public static final int HK_SUCCESS = 3;
    public static final int HK_PART_SUCCESS = 4;
    public static final int HK_FAIL = 5;
    private String describe;// 描述

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setStatusBySCBC(SettlementCenterBuckleCallbackRequestMessage scbc) {
        switch (scbc.getStatus()) {
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_ALL:
                status = SettlementResultBean.HK_SUCCESS;
                break;
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_PART:
                status = SettlementResultBean.HK_PART_SUCCESS;
                break;
            case SettlementCenterBuckleCallbackRequestMessage.FAIL:
                status = SettlementResultBean.HK_FAIL;
                break;
            default:
                throw new RuntimeException("Invalid status map, please update procedure.");
        }
    }
}
