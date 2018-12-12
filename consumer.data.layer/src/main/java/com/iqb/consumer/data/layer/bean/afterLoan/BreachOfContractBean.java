package com.iqb.consumer.data.layer.bean.afterLoan;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 客户违约状态记录bean
 * 
 * @author chengzhen 2018年3月16日 15:51:44
 */
public class BreachOfContractBean extends BaseEntity {

    private String orderId;
    private String realName;
    private String regId;// 手机号
    private BigDecimal orderAmt;// 金额
    private String orderItems;// 期数
    private String gpsStatus;// GPS状态
    private String PLANFULLNAME;// 方案名称
    private String bizType;
    private String MERCHANTSHORTNAME;// 商户名称

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getPLANFULLNAME() {
        return PLANFULLNAME;
    }

    public void setPLANFULLNAME(String pLANFULLNAME) {
        PLANFULLNAME = pLANFULLNAME;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMERCHANTSHORTNAME() {
        return MERCHANTSHORTNAME;
    }

    public void setMERCHANTSHORTNAME(String mERCHANTSHORTNAME) {
        MERCHANTSHORTNAME = mERCHANTSHORTNAME;
    }

}
