/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月16日 下午4:40:27
 * @version V1.0
 */
package com.iqb.consumer.service.layer.dto.refund;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 还款数据传输类子类
 * 
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepayList {
    private int repayNo;// 序号
    private BigDecimal amt;// 金额

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
