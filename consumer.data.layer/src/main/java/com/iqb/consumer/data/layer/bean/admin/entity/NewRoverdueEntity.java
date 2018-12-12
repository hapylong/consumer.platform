package com.iqb.consumer.data.layer.bean.admin.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:Mysql Table: finance.r_overdue
 * @author chengzhen
 * @date 2017年12月6日 11:53:57
 * @version
 */
public class NewRoverdueEntity {

    private Long id;
    private String merchantNo;

    private Date schedueDate;
    private BigDecimal overdueMonth;// 逾期总月供
    private BigDecimal overdueMonthMb5;
    private BigDecimal overdueMonthMb4;
    private BigDecimal overdueMonthMb3;
    private BigDecimal overdueMonthMb2;
    private BigDecimal overdueMonthMb1;

    private BigDecimal overduePrincipal;// 总得逾期剩余本金
    private BigDecimal overduePrincipalMb1;
    private BigDecimal overduePrincipalMb2;
    private BigDecimal overduePrincipalMb3;
    private BigDecimal overduePrincipalMb4;
    private BigDecimal overduePrincipalMb5;//

    private int overdueNum;// 总得逾期订单个数
    private int overdueNumMb5;//
    private int overdueNumMb4;//
    private int overdueNumMb3;//
    private int overdueNumMb2;//
    private int overdueNumMb1;//
    private int overdueStockNum;// 总得订单存量

    private BigDecimal stockAmt;// 总资产存量
    private BigDecimal stockAmtMb1;
    private BigDecimal stockAmtMb2;
    private BigDecimal stockAmtMb3;
    private BigDecimal stockAmtMb4;
    private BigDecimal stockAmtMb5;

    private BigDecimal overdueRate;
    private BigDecimal overdueRateMb5;
    private BigDecimal overdueRateMb1;
    private BigDecimal overdueRateMb2;
    private BigDecimal overdueRateMb3;
    private BigDecimal overdueRateMb4;

    /** 总计 **/
    private BigDecimal totalOverduePrincipal;
    private BigDecimal totalStockAmt;
    private BigDecimal totalOverduePrincipalMb5;
    private BigDecimal totalOverduePrincipalMb4;
    private BigDecimal totalOverduePrincipalMb3;
    private BigDecimal totalOverduePrincipalMb2;
    private BigDecimal totalOverduePrincipalMb1;

    private BigDecimal totalOverdueRate;
    private BigDecimal totalOverdueRateMb5;
    private BigDecimal totalOverdueRateMb4;
    private BigDecimal totalOverdueRateMb3;
    private BigDecimal totalOverdueRateMb2;
    private BigDecimal totalOverdueRateMb1;

    private BigDecimal totalOverdueMonth;
    private BigDecimal totalOverdueMonthMb5;
    private BigDecimal totalOverdueMonthMb4;
    private BigDecimal totalOverdueMonthMb3;
    private BigDecimal totalOverdueMonthMb2;
    private BigDecimal totalOverdueMonthMb1;

    private BigDecimal totalOverdueMonthRate;
    private BigDecimal totalOverdueMonthRateMb5;
    private BigDecimal totalOverdueMonthRateMb4;
    private BigDecimal totalOverdueMonthRateMb3;
    private BigDecimal totalOverdueMonthRateMb2;
    private BigDecimal totalOverdueMonthRateMb1;

    private int totalOverdueNum;
    private int totalOverdueNumMb5;
    private int totalOverdueNumMb4;
    private int totalOverdueNumMb3;
    private int totalOverdueNumMb2;
    private int totalOverdueNumMb1;
    private int totalOverdueStockNum;

    private BigDecimal totalOverdueNumRate;
    private BigDecimal totalOverdueNumRateMb5;
    private BigDecimal totalOverdueNumRateMb4;
    private BigDecimal totalOverdueNumRateMb3;
    private BigDecimal totalOverdueNumRateMb2;
    private BigDecimal totalOverdueNumRateMb1;

    public int getOverdueStockNum() {
        return overdueStockNum;
    }

    public void setOverdueStockNum(int overdueStockNum) {
        this.overdueStockNum = overdueStockNum;
    }

    public int getTotalOverdueStockNum() {
        return totalOverdueStockNum;
    }

    public void setTotalOverdueStockNum(int totalOverdueStockNum) {
        this.totalOverdueStockNum = totalOverdueStockNum;
    }

    public int getTotalOverdueNum() {
        return totalOverdueNum;
    }

    public void setTotalOverdueNum(int totalOverdueNum) {
        this.totalOverdueNum = totalOverdueNum;
    }

    public int getTotalOverdueNumMb5() {
        return totalOverdueNumMb5;
    }

    public void setTotalOverdueNumMb5(int totalOverdueNumMb5) {
        this.totalOverdueNumMb5 = totalOverdueNumMb5;
    }

    public int getTotalOverdueNumMb4() {
        return totalOverdueNumMb4;
    }

    public void setTotalOverdueNumMb4(int totalOverdueNumMb4) {
        this.totalOverdueNumMb4 = totalOverdueNumMb4;
    }

    public int getTotalOverdueNumMb3() {
        return totalOverdueNumMb3;
    }

    public void setTotalOverdueNumMb3(int totalOverdueNumMb3) {
        this.totalOverdueNumMb3 = totalOverdueNumMb3;
    }

    public int getTotalOverdueNumMb2() {
        return totalOverdueNumMb2;
    }

    public void setTotalOverdueNumMb2(int totalOverdueNumMb2) {
        this.totalOverdueNumMb2 = totalOverdueNumMb2;
    }

    public int getTotalOverdueNumMb1() {
        return totalOverdueNumMb1;
    }

    public void setTotalOverdueNumMb1(int totalOverdueNumMb1) {
        this.totalOverdueNumMb1 = totalOverdueNumMb1;
    }

    public BigDecimal getTotalOverdueNumRate() {
        return (totalOverdueNumRate == null ? BigDecimal.ZERO : totalOverdueNumRate);
    }

    public void setTotalOverdueNumRate(BigDecimal totalOverdueNumRate) {
        this.totalOverdueNumRate = totalOverdueNumRate;
    }

    public BigDecimal getTotalOverdueNumRateMb5() {
        return (totalOverdueNumRateMb5 == null ? BigDecimal.ZERO : totalOverdueNumRateMb5);
    }

    public void setTotalOverdueNumRateMb5(BigDecimal totalOverdueNumRateMb5) {
        this.totalOverdueNumRateMb5 = totalOverdueNumRateMb5;
    }

    public BigDecimal getTotalOverdueNumRateMb4() {
        return (totalOverdueNumRateMb4 == null ? BigDecimal.ZERO : totalOverdueNumRateMb4);
    }

    public void setTotalOverdueNumRateMb4(BigDecimal totalOverdueNumRateMb4) {
        this.totalOverdueNumRateMb4 = totalOverdueNumRateMb4;
    }

    public BigDecimal getTotalOverdueNumRateMb3() {
        return (totalOverdueNumRateMb3 == null ? BigDecimal.ZERO : totalOverdueNumRateMb3);
    }

    public void setTotalOverdueNumRateMb3(BigDecimal totalOverdueNumRateMb3) {
        this.totalOverdueNumRateMb3 = totalOverdueNumRateMb3;
    }

    public BigDecimal getTotalOverdueNumRateMb2() {
        return (totalOverdueNumRateMb2 == null ? BigDecimal.ZERO : totalOverdueNumRateMb2);
    }

    public void setTotalOverdueNumRateMb2(BigDecimal totalOverdueNumRateMb2) {
        this.totalOverdueNumRateMb2 = totalOverdueNumRateMb2;
    }

    public BigDecimal getTotalOverdueNumhRateMb1() {
        return (totalOverdueNumRateMb1 == null ? BigDecimal.ZERO : totalOverdueNumRateMb1);
    }

    public void setTotalOverdueNumhRateMb1(BigDecimal totalOverdueNumhRateMb1) {
        this.totalOverdueNumRateMb1 = totalOverdueNumhRateMb1;
    }

    public BigDecimal getTotalOverdueMonthRate() {
        return (totalOverdueMonthRate == null ? BigDecimal.ZERO : totalOverdueMonthRate);
    }

    public void setTotalOverdueMonthRate(BigDecimal totalOverdueMonthRate) {
        this.totalOverdueMonthRate = totalOverdueMonthRate;
    }

    public BigDecimal getTotalOverdueMonthRateMb5() {
        return (totalOverdueMonthRateMb5 == null ? BigDecimal.ZERO : totalOverdueMonthRateMb5);
    }

    public void setTotalOverdueMonthRateMb5(BigDecimal totalOverdueMonthRateMb5) {
        this.totalOverdueMonthRateMb5 = totalOverdueMonthRateMb5;
    }

    public BigDecimal getTotalOverdueMonthRateMb4() {
        return (totalOverdueMonthRateMb4 == null ? BigDecimal.ZERO : totalOverdueMonthRateMb4);
    }

    public void setTotalOverdueMonthRateMb4(BigDecimal totalOverdueMonthRateMb4) {
        this.totalOverdueMonthRateMb4 = totalOverdueMonthRateMb4;
    }

    public BigDecimal getTotalOverdueMonthRateMb3() {
        return (totalOverdueMonthRateMb3 == null ? BigDecimal.ZERO : totalOverdueMonthRateMb3);
    }

    public void setTotalOverdueMonthRateMb3(BigDecimal totalOverdueMonthRateMb3) {
        this.totalOverdueMonthRateMb3 = totalOverdueMonthRateMb3;
    }

    public BigDecimal getTotalOverdueMonthRateMb2() {
        return (totalOverdueMonthRateMb2 == null ? BigDecimal.ZERO : totalOverdueMonthRateMb2);
    }

    public void setTotalOverdueMonthRateMb2(BigDecimal totalOverdueMonthRateMb2) {
        this.totalOverdueMonthRateMb2 = totalOverdueMonthRateMb2;
    }

    public BigDecimal getTotalOverdueMonthRateMb1() {
        return (totalOverdueMonthRateMb1 == null ? BigDecimal.ZERO : totalOverdueMonthRateMb1);
    }

    public void setTotalOverdueMonthRateMb1(BigDecimal totalOverdueMonthRateMb1) {
        this.totalOverdueMonthRateMb1 = totalOverdueMonthRateMb1;
    }

    public BigDecimal getTotalOverdueMonth() {
        return (totalOverdueMonth == null ? BigDecimal.ZERO : totalOverdueMonth);
    }

    public void setTotalOverdueMonth(BigDecimal totalOverdueMonth) {
        this.totalOverdueMonth = totalOverdueMonth;
    }

    public BigDecimal getTotalOverdueMonthMb5() {
        return (totalOverdueMonthMb5 == null ? BigDecimal.ZERO : totalOverdueMonthMb5);
    }

    public void setTotalOverdueMonthMb5(BigDecimal totalOverdueMonthMb5) {
        this.totalOverdueMonthMb5 = totalOverdueMonthMb5;
    }

    public BigDecimal getTotalOverdueMonthMb4() {
        return (totalOverdueMonthMb4 == null ? BigDecimal.ZERO : totalOverdueMonthMb4);
    }

    public void setTotalOverdueMonthMb4(BigDecimal totalOverdueMonthMb4) {
        this.totalOverdueMonthMb4 = totalOverdueMonthMb4;
    }

    public BigDecimal getTotalOverdueMonthMb3() {
        return (totalOverdueMonthMb3 == null ? BigDecimal.ZERO : totalOverdueMonthMb3);
    }

    public void setTotalOverdueMonthMb3(BigDecimal totalOverdueMonthMb3) {
        this.totalOverdueMonthMb3 = totalOverdueMonthMb3;
    }

    public BigDecimal getTotalOverdueMonthMb2() {
        return (totalOverdueMonthMb2 == null ? BigDecimal.ZERO : totalOverdueMonthMb2);
    }

    public void setTotalOverdueMonthMb2(BigDecimal totalOverdueMonthMb2) {
        this.totalOverdueMonthMb2 = totalOverdueMonthMb2;
    }

    public BigDecimal getTotalOverdueMonthMb1() {
        return (totalOverdueMonthMb1 == null ? BigDecimal.ZERO : totalOverdueMonthMb1);
    }

    public void setTotalOverdueMonthMb1(BigDecimal totalOverdueMonthMb1) {
        this.totalOverdueMonthMb1 = totalOverdueMonthMb1;
    }

    public BigDecimal getTotalOverdueRate() {
        return (totalOverdueRate == null ? BigDecimal.ZERO : totalOverdueRate);
    }

    public void setTotalOverdueRate(BigDecimal totalOverdueRate) {
        this.totalOverdueRate = totalOverdueRate;
    }

    public BigDecimal getTotalOverdueRateMb5() {
        return (totalOverdueRateMb5 == null ? BigDecimal.ZERO : totalOverdueRateMb5);
    }

    public void setTotalOverdueRateMb5(BigDecimal totalOverdueRateMb5) {
        this.totalOverdueRateMb5 = totalOverdueRateMb5;
    }

    public BigDecimal getTotalOverdueRateMb4() {
        return (totalOverdueRateMb4 == null ? BigDecimal.ZERO : totalOverdueRateMb4);
    }

    public void setTotalOverdueRateMb4(BigDecimal totalOverdueRateMb4) {
        this.totalOverdueRateMb4 = totalOverdueRateMb4;
    }

    public BigDecimal getTotalOverdueRateMb3() {
        return (totalOverdueRateMb3 == null ? BigDecimal.ZERO : totalOverdueRateMb3);
    }

    public void setTotalOverdueRateMb3(BigDecimal totalOverdueRateMb3) {
        this.totalOverdueRateMb3 = totalOverdueRateMb3;
    }

    public BigDecimal getTotalOverdueRateMb2() {
        return (totalOverdueRateMb2 == null ? BigDecimal.ZERO : totalOverdueRateMb2);
    }

    public void setTotalOverdueRateMb2(BigDecimal totalOverdueRateMb2) {
        this.totalOverdueRateMb2 = totalOverdueRateMb2;
    }

    public BigDecimal getTotalOverdueRateMb1() {
        return (totalOverdueRateMb1 == null ? BigDecimal.ZERO : totalOverdueRateMb1);
    }

    public void setTotalOverdueRateMb1(BigDecimal totalOverdueRateMb1) {
        this.totalOverdueRateMb1 = totalOverdueRateMb1;
    }

    private Integer version;

    private Date createTime;

    private Date updateTime;

    public BigDecimal getTotalOverduePrincipal() {
        return (totalOverduePrincipal == null ? BigDecimal.ZERO : totalOverduePrincipal);
    }

    public void setTotalOverduePrincipal(BigDecimal totalOverduePrincipal) {
        this.totalOverduePrincipal = totalOverduePrincipal;
    }

    public BigDecimal getTotalStockAmt() {
        return (totalStockAmt == null ? BigDecimal.ZERO : totalStockAmt);
    }

    public void setTotalStockAmt(BigDecimal totalStockAmt) {
        this.totalStockAmt = totalStockAmt;
    }

    public BigDecimal getTotalOverduePrincipalMb5() {
        return (totalOverduePrincipalMb5 == null ? BigDecimal.ZERO : totalOverduePrincipalMb5);
    }

    public void setTotalOverduePrincipalMb5(BigDecimal totalOverduePrincipalMb5) {
        this.totalOverduePrincipalMb5 = totalOverduePrincipalMb5;
    }

    public BigDecimal getTotalOverduePrincipalMb4() {
        return (totalOverduePrincipalMb4 == null ? BigDecimal.ZERO : totalOverduePrincipalMb4);
    }

    public void setTotalOverduePrincipalMb4(BigDecimal totalOverduePrincipalMb4) {
        this.totalOverduePrincipalMb4 = totalOverduePrincipalMb4;
    }

    public BigDecimal getTotalOverduePrincipalMb3() {
        return (totalOverduePrincipalMb3 == null ? BigDecimal.ZERO : totalOverduePrincipalMb3);
    }

    public void setTotalOverduePrincipalMb3(BigDecimal totalOverduePrincipalMb3) {
        this.totalOverduePrincipalMb3 = totalOverduePrincipalMb3;
    }

    public BigDecimal getTotalOverduePrincipalMb2() {
        return (totalOverduePrincipalMb2 == null ? BigDecimal.ZERO : totalOverduePrincipalMb2);
    }

    public void setTotalOverduePrincipalMb2(BigDecimal totalOverduePrincipalMb2) {
        this.totalOverduePrincipalMb2 = totalOverduePrincipalMb2;
    }

    public BigDecimal getTotalOverduePrincipalMb1() {
        return (totalOverduePrincipalMb1 == null ? BigDecimal.ZERO : totalOverduePrincipalMb1);
    }

    public void setTotalOverduePrincipalMb1(BigDecimal totalOverduePrincipalMb1) {
        this.totalOverduePrincipalMb1 = totalOverduePrincipalMb1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(int overdueNum) {
        this.overdueNum = overdueNum;
    }

    public int getOverdueNumMb5() {
        return overdueNumMb5;
    }

    public void setOverdueNumMb5(int overdueNumMb5) {
        this.overdueNumMb5 = overdueNumMb5;
    }

    public int getOverdueNumMb4() {
        return overdueNumMb4;
    }

    public void setOverdueNumMb4(int overdueNumMb4) {
        this.overdueNumMb4 = overdueNumMb4;
    }

    public int getOverdueNumMb3() {
        return overdueNumMb3;
    }

    public void setOverdueNumMb3(int overdueNumMb3) {
        this.overdueNumMb3 = overdueNumMb3;
    }

    public int getOverdueNumMb2() {
        return overdueNumMb2;
    }

    public void setOverdueNumMb2(int overdueNumMb2) {
        this.overdueNumMb2 = overdueNumMb2;
    }

    public int getOverdueNumMb1() {
        return overdueNumMb1;
    }

    public void setOverdueNumMb1(int overdueNumMb1) {
        this.overdueNumMb1 = overdueNumMb1;
    }

    public int getOverdutStockNum() {
        return overdueStockNum;
    }

    public void setOverdutStockNum(int overdutStockNum) {
        this.overdueStockNum = overdutStockNum;
    }

    public BigDecimal getOverdueMonthMb5() {

        return (overdueMonthMb5 == null ? BigDecimal.ZERO : overdueMonthMb5);
    }

    public void setOverdueMonthMb5(BigDecimal overdueMonthMb5) {
        this.overdueMonthMb5 = overdueMonthMb5;
    }

    public BigDecimal getOverdueMonthMb4() {
        return (overdueMonthMb4 == null ? BigDecimal.ZERO : overdueMonthMb4);
    }

    public void setOverdueMonthMb4(BigDecimal overdueMonthMb4) {
        this.overdueMonthMb4 = overdueMonthMb4;
    }

    public BigDecimal getOverdueMonthMb3() {
        return (overdueMonthMb3 == null ? BigDecimal.ZERO : overdueMonthMb3);
    }

    public void setOverdueMonthMb3(BigDecimal overdueMonthMb3) {
        this.overdueMonthMb3 = overdueMonthMb3;
    }

    public BigDecimal getOverdueMonthMb2() {
        return (overdueMonthMb2 == null ? BigDecimal.ZERO : overdueMonthMb2);
    }

    public void setOverdueMonthMb2(BigDecimal overdueMonthMb2) {
        this.overdueMonthMb2 = overdueMonthMb2;
    }

    public BigDecimal getOverduePrincipal() {
        return (overduePrincipal == null ? BigDecimal.ZERO : overduePrincipal);
    }

    public void setOverduePrincipal(BigDecimal overduePrincipal) {
        this.overduePrincipal = overduePrincipal;
    }

    public BigDecimal getOverduePrincipalMb5() {
        return (overduePrincipalMb5 == null ? BigDecimal.ZERO : overduePrincipalMb5);
    }

    public void setOverduePrincipalMb5(BigDecimal overduePrincipalMb5) {
        this.overduePrincipalMb5 = overduePrincipalMb5;
    }

    public BigDecimal getOverdueMonth() {
        return (overdueMonth == null ? BigDecimal.ZERO : overdueMonth);
    }

    public void setOverdueMonth(BigDecimal overdueMonth) {
        this.overdueMonth = overdueMonth;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getSchedueDate() {
        return schedueDate;
    }

    public void setSchedueDate(Date schedueDate) {
        this.schedueDate = schedueDate;
    }

    public BigDecimal getOverdueMonthMb1() {
        return (overdueMonthMb1 == null ? BigDecimal.ZERO : overdueMonthMb1);
    }

    public void setOverdueMonthMb1(BigDecimal overdueMonthMb1) {
        this.overdueMonthMb1 = overdueMonthMb1;
    }

    public BigDecimal getStockAmt() {
        return (stockAmt == null ? BigDecimal.ZERO : stockAmt);
    }

    public void setStockAmt(BigDecimal stockAmt) {
        this.stockAmt = stockAmt;
    }

    public BigDecimal getStockAmtMb5() {
        return (stockAmtMb5 == null ? BigDecimal.ZERO : stockAmtMb5);
    }

    public void setStockAmtMb5(BigDecimal stockAmtMb5) {
        this.stockAmtMb5 = stockAmtMb5;
    }

    public BigDecimal getOverdueRate() {
        return (overdueRate == null ? BigDecimal.ZERO : overdueRate);
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public BigDecimal getOverdueRateMb5() {
        return (overdueRateMb5 == null ? BigDecimal.ZERO : overdueRateMb5);
    }

    public void setOverdueRateMb5(BigDecimal overdueRateMb5) {
        this.overdueRateMb5 = overdueRateMb5;
    }

    public BigDecimal getOverduePrincipalMb1() {
        return (overduePrincipalMb1 == null ? BigDecimal.ZERO : overduePrincipalMb1);
    }

    public void setOverduePrincipalMb1(BigDecimal overduePrincipalMb1) {
        this.overduePrincipalMb1 = overduePrincipalMb1;
    }

    public BigDecimal getStockAmtMb1() {
        return (stockAmtMb1 == null ? BigDecimal.ZERO : stockAmtMb1);
    }

    public void setStockAmtMb1(BigDecimal stockAmtMb1) {
        this.stockAmtMb1 = stockAmtMb1;
    }

    public BigDecimal getOverdueRateMb1() {
        return (overdueRateMb1 == null ? BigDecimal.ZERO : overdueRateMb1);
    }

    public void setOverdueRateMb1(BigDecimal overdueRateMb1) {
        this.overdueRateMb1 = overdueRateMb1;
    }

    public BigDecimal getOverduePrincipalMb2() {
        return (overduePrincipalMb2 == null ? BigDecimal.ZERO : overduePrincipalMb2);
    }

    public void setOverduePrincipalMb2(BigDecimal overduePrincipalMb2) {
        this.overduePrincipalMb2 = overduePrincipalMb2;
    }

    public BigDecimal getStockAmtMb2() {
        return (stockAmtMb2 == null ? BigDecimal.ZERO : stockAmtMb2);
    }

    public void setStockAmtMb2(BigDecimal stockAmtMb2) {
        this.stockAmtMb2 = stockAmtMb2;
    }

    public BigDecimal getOverdueRateMb2() {
        return (overdueRateMb2 == null ? BigDecimal.ZERO : overdueRateMb2);
    }

    public void setOverdueRateMb2(BigDecimal overdueRateMb2) {
        this.overdueRateMb2 = overdueRateMb2;
    }

    public BigDecimal getOverduePrincipalMb3() {
        return (overduePrincipalMb3 == null ? BigDecimal.ZERO : overduePrincipalMb3);
    }

    public void setOverduePrincipalMb3(BigDecimal overduePrincipalMb3) {
        this.overduePrincipalMb3 = overduePrincipalMb3;
    }

    public BigDecimal getStockAmtMb3() {
        return (stockAmtMb3 == null ? BigDecimal.ZERO : stockAmtMb3);
    }

    public void setStockAmtMb3(BigDecimal stockAmtMb3) {
        this.stockAmtMb3 = stockAmtMb3;
    }

    public BigDecimal getOverdueRateMb3() {
        return (overdueRateMb3 == null ? BigDecimal.ZERO : overdueRateMb3);
    }

    public void setOverdueRateMb3(BigDecimal overdueRateMb3) {
        this.overdueRateMb3 = overdueRateMb3;
    }

    public BigDecimal getOverduePrincipalMb4() {
        return (overduePrincipalMb4 == null ? BigDecimal.ZERO : overduePrincipalMb4);
    }

    public void setOverduePrincipalMb4(BigDecimal overduePrincipalMb4) {
        this.overduePrincipalMb4 = overduePrincipalMb4;
    }

    public BigDecimal getStockAmtMb4() {
        return (stockAmtMb4 == null ? BigDecimal.ZERO : stockAmtMb4);
    }

    public void setStockAmtMb4(BigDecimal stockAmtMb4) {
        this.stockAmtMb4 = stockAmtMb4;
    }

    public BigDecimal getOverdueRateMb4() {
        return (overdueRateMb4 == null ? BigDecimal.ZERO : overdueRateMb4);
    }

    public void setOverdueRateMb4(BigDecimal overdueRateMb4) {
        this.overdueRateMb4 = overdueRateMb4;
    }

}
