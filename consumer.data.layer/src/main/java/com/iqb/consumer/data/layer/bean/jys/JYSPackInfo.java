/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 下午5:21:44
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.jys;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 交易所需求:资产打包表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class JYSPackInfo extends BaseEntity {

    /**
     * 订单号
     */
    private String orderId;
    /**
     * 起息日
     */
    private Date beginInterestDate;
    /**
     * 募捐机构
     */
    private String raiseInstitutions;
    /**
     * 担保机构
     */
    private String guaranteeInstitution;
    /**
     * 担保法人
     */
    private String guaranteeName;
    /**
     * 打包个数
     */
    private int packNum;
    /**
     * 打包金额
     */
    private BigDecimal packAmt;
    /**
     * 备注
     */
    private String remark;
    private long jysOrderId;// 交易所订单主键id

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getBeginInterestDate() {
        return beginInterestDate;
    }

    public void setBeginInterestDate(Date beginInterestDate) {
        this.beginInterestDate = beginInterestDate;
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

    public int getPackNum() {
        return packNum;
    }

    public void setPackNum(int packNum) {
        this.packNum = packNum;
    }

    public BigDecimal getPackAmt() {
        return packAmt;
    }

    public void setPackAmt(BigDecimal packAmt) {
        this.packAmt = packAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getJysOrderId() {
        return jysOrderId;
    }

    public void setJysOrderId(long jysOrderId) {
        this.jysOrderId = jysOrderId;
    }

}
