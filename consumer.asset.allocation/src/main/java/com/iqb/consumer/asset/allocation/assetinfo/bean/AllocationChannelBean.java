package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:资产分配渠道配置
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年9月12日下午3:56:01 haojinlong   1.0        1.0 Version 
 * </pre>
 */
public class AllocationChannelBean implements Serializable {

    private static final long serialVersionUID = 1044328010024160022L;

    private int id;
    private String merchantNo;// 商户号
    private String bizType; // 业务类型
    private String fundSourceId; // 资金来源
    private String sourcesName;// 资金来源名称
    private String registUrl;// 注册URL
    private String sourceUrl;
    private String status;
    private int virtualAccount;// 是否虚拟账户
    private Date updateTime; // 更新时间
    private Date createTime; // 创建时间
    private String bizTypeName; // 业务类型名称
    private String sourcesFundName; // 资金来源名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getSourcesFundName() {
        return sourcesFundName;
    }

    public void setSourcesFundName(String sourcesFundName) {
        this.sourcesFundName = sourcesFundName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSourcesName() {
        return sourcesName;
    }

    public void setSourcesName(String sourcesName) {
        this.sourcesName = sourcesName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getFundSourceId() {
        return fundSourceId;
    }

    public void setFundSourceId(String fundSourceId) {
        this.fundSourceId = fundSourceId;
    }

    public String getRegistUrl() {
        return registUrl;
    }

    public void setRegistUrl(String registUrl) {
        this.registUrl = registUrl;
    }

    public int getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(int virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

}
