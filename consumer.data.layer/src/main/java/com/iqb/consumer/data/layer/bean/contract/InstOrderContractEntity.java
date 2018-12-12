package com.iqb.consumer.data.layer.bean.contract;

import java.util.Date;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

@Table(name = "inst_ordercontract")
public class InstOrderContractEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    protected static final Logger logger = LoggerFactory.getLogger(InstOrderContractEntity.class);
    private String orderId;
    private String loanContractNo; // 借款合同编号
    private String guarantyContractNo; // 担保合同编号
    private String leaseContractNo; // 租赁合同编号
    private Date contractDate; // 合同日期
    private Date contractEndDate; // 到期日

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLoanContractNo() {
        return loanContractNo;
    }

    public void setLoanContractNo(String loanContractNo) {
        this.loanContractNo = loanContractNo;
    }

    public String getGuarantyContractNo() {
        return guarantyContractNo;
    }

    public void setGuarantyContractNo(String guarantyContractNo) {
        this.guarantyContractNo = guarantyContractNo;
    }

    public String getLeaseContractNo() {
        return leaseContractNo;
    }

    public void setLeaseContractNo(String leaseContractNo) {
        this.leaseContractNo = leaseContractNo;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public boolean checkEntity() {
        logger.info("InstOrderContractEntity.checkEntity toString :" + toString());
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(loanContractNo) ||
                contractDate == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InstOrderContractEntity [orderId=" + orderId + ", loanContractNo=" + loanContractNo
                + ", guarantyContractNo=" + guarantyContractNo + ", leaseContractNo=" + leaseContractNo
                + ", contractDate=" + contractDate + ", contractEndDate=" + contractEndDate + "]";
    }

    public void createIOCE(InstOrderContractEntity ioce) {
        this.contractDate = ioce.getContractDate();
        this.contractEndDate = ioce.getContractEndDate();
        this.guarantyContractNo = ioce.getGuarantyContractNo();
        this.leaseContractNo = ioce.getLeaseContractNo();
        this.loanContractNo = ioce.getLoanContractNo();
        this.orderId = ioce.getOrderId();
    }

    public void updateIOIE(InstOrderContractEntity ioce) {
        this.contractDate = ioce.getContractDate();
        this.contractEndDate = ioce.getContractEndDate();
        this.guarantyContractNo = ioce.getGuarantyContractNo();
        this.leaseContractNo = ioce.getLeaseContractNo();
        this.loanContractNo = ioce.getLoanContractNo();
        super.updateTime = new Date();
        super.version = super.version + 1;
    }

}
