package com.iqb.consumer.crm.customer.bean;

/**
 * 
 * Description: 实体bean信息
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月19日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class CustomerBean {

    /** ================================= 1.客户基本信息 ====================================== **/
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户简称
     */
    private String customerShortName;

    /**
     * 客户全称
     */
    private String customerFullName;

    /**
     * 客户简称编码
     */
    private String customerShortNameCode;

    /**
     * 客户类型
     */
    private String customerType;

    /** 开户银行名称 **/
    private String customerBankName;
    /** 开户银行号 **/
    private String customerBankNo;

    /**
     * 业务类型（帮帮手）
     */
    private String businessType;

    /**
     * 所属区域
     */
    private String belongsArea;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 客户状态
     */
    private String customerStatus;

    /**
     * 推送时间
     */
    private String pushTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 图片路径
     */
    private String imgUrl;

    /**
     * 删除标识
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private String version;

    /**
     * 联系人姓名
     */
    private String contactUserName;

    /**
     * 联系人电话
     */
    private String contactPhoneNum;

    private String creditorOtherInfo; // 其他债权人信息

    private Integer flag; // 用以标识 主[1]从[0]债权人

    /** ================================= 2.企业客户相关信息 ====================================== **/
    /**
     * 注册登记号
     */
    private String regSerialNum;

    /**
     * 注册地址
     */
    private String regAddress;

    /**
     * 注册资本
     */
    private String regCapital;

    /**
     * 成立日期REG_SERIAL_NUM
     */
    private String establishmenDate;

    /**
     * 商户到期时间
     */
    private String merchantMaturity;

    /**
     * 营业期限
     */
    private String operatingPeriod;

    /**
     * 法人姓名
     */
    private String corporateName;

    /**
     * 法人证件类型
     */
    private String corporateCertificateType;

    /**
     * 法人证件号
     */
    private String corporateCertificateCode;

    /**
     * 统一信用码
     */
    private String uniformCreditCode;

    /**
     * 组织机构代码证件号
     */
    private String organizationCode;

    /**
     * 税务登记证件号
     */
    private String taxCertificateCode;

    /**
     * 开户许可证件号
     */
    private String accountPermitCode;

    /**
     * ICP证件号
     */
    private String icpCode;

    /**
     * 公司印章
     */
    private String companyChop;

    /**
     * 公司印章密码
     */
    private String companyChopPwd;

    /**
     * 公司页面ID
     */
    private String companyPageId;

    /**
     * 公司URL
     */
    private String companyUrl;
    /**
     * 办公区域
     */
    private String workAddress;
    /**
     * 营业执照注册号
     */
    private String businessLicenseNum;
    /**
     * 服务电话
     */
    private String serviceCall;
    /**
     * 企业邮箱
     */
    private String enterpriseMailbox;
    /**
     * 法人电话
     */
    private String legalPersonPhoneNum;
    /**
     * 社会信用代码
     */
    private String socialCreditCode;
    /**
     * 工商注册号
     */
    private String icRegCode;
    /**
     * 企业联系人姓名
     */
    private String businessContactName;
    /**
     * 企业联系人身份证号
     */
    private String businessContactIdcard;

    /** ================================= 3.客户消费金融相关信息 ====================================== **/
    /**
     * 拥有的微信号
     */
    private String holdWeixin;

    /**
     * 风险类型
     */
    private String riskManageType;

    /**
     * 是否存在父节点
     */
    private String isFather;

    /**
     * 层级
     */
    private String layer;

    /**
     * 分期计划
     */
    private String installmentPlan;

    /**
     * 逾期利率
     */
    private String overdueInterestRate;

    /**
     * 逾期固定手续费
     */
    private String overdueFixedFee;

    /**
     * 逾期利率模式
     */
    private String overdueInterestModel;

    /**
     * 是否为虚商户
     */
    private String isVirtualMerc;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 保留字段
     */
    private String reserveField;

    /**
     * 法人印章
     */
    private String corporateImgUrl;

    /**
     * 公司印章
     */
    private String componyImgUrl;

    /** ================================= 3.客户消费金融相关信息 ====================================== **/
    /**
     * 债权人姓名
     */
    private String creditorName;

    /**
     * 债权人身份证号
     */
    private String creditorIdNo;

    /**
     * 债权人银行卡号
     */
    private String creditorBankNo;

    /**
     * 债权人开户银行
     */
    private String creditorBankName;

    /**
     * 债权人手机号
     */
    private String creditorPhone;

    /**
     * 所属担保公司编码
     */
    private String guaranteeCorporationCode;

    /**
     * 所属担保公司名称
     */
    private String guaranteeCorporationName;

    /**
     * 所属担保公司法人姓名
     */
    private String guaranteeCorporationCorName;

    /**
     * 门店状态
     */
    private String creditorStatus;

    /**
     * 是否已鉴权
     */
    private String hasAuthed;

    /**
     * 鉴权渠道
     */
    private String authChannel;

    /**
     * 鉴权结果
     */
    private String authResult;

    /**
     * 上级机构
     */
    private String higherOrgName;

    /**
     * 上级机构
     */
    private String higherOrgCode;

    private int flagId;
    /** 是否在风控系统配置商户编码 **/
    private int riskFlag;
    /** 密钥 **/
    private String riskCode;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerShortName() {
        return customerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        this.customerShortName = customerShortName;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerShortNameCode() {
        return customerShortNameCode;
    }

    public void setCustomerShortNameCode(String customerShortNameCode) {
        this.customerShortNameCode = customerShortNameCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getBelongsArea() {
        return belongsArea;
    }

    public void setBelongsArea(String belongsArea) {
        this.belongsArea = belongsArea;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRegSerialNum() {
        return regSerialNum;
    }

    public void setRegSerialNum(String regSerialNum) {
        this.regSerialNum = regSerialNum;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital;
    }

    public String getEstablishmenDate() {
        return establishmenDate;
    }

    public void setEstablishmenDate(String establishmenDate) {
        this.establishmenDate = establishmenDate;
    }

    public String getMerchantMaturity() {
        return merchantMaturity;
    }

    public void setMerchantMaturity(String merchantMaturity) {
        this.merchantMaturity = merchantMaturity;
    }

    public String getOperatingPeriod() {
        return operatingPeriod;
    }

    public void setOperatingPeriod(String operatingPeriod) {
        this.operatingPeriod = operatingPeriod;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateCertificateType() {
        return corporateCertificateType;
    }

    public void setCorporateCertificateType(String corporateCertificateType) {
        this.corporateCertificateType = corporateCertificateType;
    }

    public String getCorporateCertificateCode() {
        return corporateCertificateCode;
    }

    public void setCorporateCertificateCode(String corporateCertificateCode) {
        this.corporateCertificateCode = corporateCertificateCode;
    }

    public String getUniformCreditCode() {
        return uniformCreditCode;
    }

    public void setUniformCreditCode(String uniformCreditCode) {
        this.uniformCreditCode = uniformCreditCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getTaxCertificateCode() {
        return taxCertificateCode;
    }

    public void setTaxCertificateCode(String taxCertificateCode) {
        this.taxCertificateCode = taxCertificateCode;
    }

    public String getAccountPermitCode() {
        return accountPermitCode;
    }

    public void setAccountPermitCode(String accountPermitCode) {
        this.accountPermitCode = accountPermitCode;
    }

    public String getIcpCode() {
        return icpCode;
    }

    public void setIcpCode(String icpCode) {
        this.icpCode = icpCode;
    }

    public String getCompanyChop() {
        return companyChop;
    }

    public void setCompanyChop(String companyChop) {
        this.companyChop = companyChop;
    }

    public String getCompanyChopPwd() {
        return companyChopPwd;
    }

    public void setCompanyChopPwd(String companyChopPwd) {
        this.companyChopPwd = companyChopPwd;
    }

    public String getCompanyPageId() {
        return companyPageId;
    }

    public void setCompanyPageId(String companyPageId) {
        this.companyPageId = companyPageId;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getHoldWeixin() {
        return holdWeixin;
    }

    public void setHoldWeixin(String holdWeixin) {
        this.holdWeixin = holdWeixin;
    }

    public String getRiskManageType() {
        return riskManageType;
    }

    public void setRiskManageType(String riskManageType) {
        this.riskManageType = riskManageType;
    }

    public String getIsFather() {
        return isFather;
    }

    public void setIsFather(String isFather) {
        this.isFather = isFather;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getInstallmentPlan() {
        return installmentPlan;
    }

    public void setInstallmentPlan(String installmentPlan) {
        this.installmentPlan = installmentPlan;
    }

    public String getOverdueInterestRate() {
        return overdueInterestRate;
    }

    public void setOverdueInterestRate(String overdueInterestRate) {
        this.overdueInterestRate = overdueInterestRate;
    }

    public String getOverdueFixedFee() {
        return overdueFixedFee;
    }

    public void setOverdueFixedFee(String overdueFixedFee) {
        this.overdueFixedFee = overdueFixedFee;
    }

    public String getOverdueInterestModel() {
        return overdueInterestModel;
    }

    public void setOverdueInterestModel(String overdueInterestModel) {
        this.overdueInterestModel = overdueInterestModel;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getReserveField() {
        return reserveField;
    }

    public void setReserveField(String reserveField) {
        this.reserveField = reserveField;
    }

    public String getIsVirtualMerc() {
        return isVirtualMerc;
    }

    public void setIsVirtualMerc(String isVirtualMerc) {
        this.isVirtualMerc = isVirtualMerc;
    }

    public String getCorporateImgUrl() {
        return corporateImgUrl;
    }

    public void setCorporateImgUrl(String corporateImgUrl) {
        this.corporateImgUrl = corporateImgUrl;
    }

    public String getComponyImgUrl() {
        return componyImgUrl;
    }

    public void setComponyImgUrl(String componyImgUrl) {
        this.componyImgUrl = componyImgUrl;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorIdNo() {
        return creditorIdNo;
    }

    public void setCreditorIdNo(String creditorIdNo) {
        this.creditorIdNo = creditorIdNo;
    }

    public String getCreditorBankNo() {
        return creditorBankNo;
    }

    public void setCreditorBankNo(String creditorBankNo) {
        this.creditorBankNo = creditorBankNo;
    }

    public String getCreditorBankName() {
        return creditorBankName;
    }

    public void setCreditorBankName(String creditorBankName) {
        this.creditorBankName = creditorBankName;
    }

    public String getCreditorPhone() {
        return creditorPhone;
    }

    public void setCreditorPhone(String creditorPhone) {
        this.creditorPhone = creditorPhone;
    }

    public String getGuaranteeCorporationCode() {
        return guaranteeCorporationCode;
    }

    public void setGuaranteeCorporationCode(String guaranteeCorporationCode) {
        this.guaranteeCorporationCode = guaranteeCorporationCode;
    }

    public String getGuaranteeCorporationName() {
        return guaranteeCorporationName;
    }

    public void setGuaranteeCorporationName(String guaranteeCorporationName) {
        this.guaranteeCorporationName = guaranteeCorporationName;
    }

    public String getGuaranteeCorporationCorName() {
        return guaranteeCorporationCorName;
    }

    public void setGuaranteeCorporationCorName(String guaranteeCorporationCorName) {
        this.guaranteeCorporationCorName = guaranteeCorporationCorName;
    }

    public String getCreditorStatus() {
        return creditorStatus;
    }

    public void setCreditorStatus(String creditorStatus) {
        this.creditorStatus = creditorStatus;
    }

    public String getHasAuthed() {
        return hasAuthed;
    }

    public void setHasAuthed(String hasAuthed) {
        this.hasAuthed = hasAuthed;
    }

    public String getAuthChannel() {
        return authChannel;
    }

    public void setAuthChannel(String authChannel) {
        this.authChannel = authChannel;
    }

    public String getAuthResult() {
        return authResult;
    }

    public void setAuthResult(String authResult) {
        this.authResult = authResult;
    }

    public String getHigherOrgName() {
        return higherOrgName;
    }

    public void setHigherOrgName(String higherOrgName) {
        this.higherOrgName = higherOrgName;
    }

    public String getHigherOrgCode() {
        return higherOrgCode;
    }

    public void setHigherOrgCode(String higherOrgCode) {
        this.higherOrgCode = higherOrgCode;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public String getContactPhoneNum() {
        return contactPhoneNum;
    }

    public void setContactPhoneNum(String contactPhoneNum) {
        this.contactPhoneNum = contactPhoneNum;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getBusinessLicenseNum() {
        return businessLicenseNum;
    }

    public void setBusinessLicenseNum(String businessLicenseNum) {
        this.businessLicenseNum = businessLicenseNum;
    }

    public String getServiceCall() {
        return serviceCall;
    }

    public void setServiceCall(String serviceCall) {
        this.serviceCall = serviceCall;
    }

    public String getEnterpriseMailbox() {
        return enterpriseMailbox;
    }

    public void setEnterpriseMailbox(String enterpriseMailbox) {
        this.enterpriseMailbox = enterpriseMailbox;
    }

    public String getLegalPersonPhoneNum() {
        return legalPersonPhoneNum;
    }

    public void setLegalPersonPhoneNum(String legalPersonPhoneNum) {
        this.legalPersonPhoneNum = legalPersonPhoneNum;
    }

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getIcRegCode() {
        return icRegCode;
    }

    public void setIcRegCode(String icRegCode) {
        this.icRegCode = icRegCode;
    }

    public String getBusinessContactName() {
        return businessContactName;
    }

    public void setBusinessContactName(String businessContactName) {
        this.businessContactName = businessContactName;
    }

    public String getBusinessContactIdcard() {
        return businessContactIdcard;
    }

    public void setBusinessContactIdcard(String businessContactIdcard) {
        this.businessContactIdcard = businessContactIdcard;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCreditorOtherInfo() {
        return creditorOtherInfo;
    }

    public void setCreditorOtherInfo(String creditorOtherInfo) {
        this.creditorOtherInfo = creditorOtherInfo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getCustomerBankName() {
        return customerBankName;
    }

    public void setCustomerBankName(String customerBankName) {
        this.customerBankName = customerBankName;
    }

    public String getCustomerBankNo() {
        return customerBankNo;
    }

    public void setCustomerBankNo(String customerBankNo) {
        this.customerBankNo = customerBankNo;
    }

    public int getFlagId() {
        return flagId;
    }

    public void setFlagId(int flagId) {
        this.flagId = flagId;
    }

    public int getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(int riskFlag) {
        this.riskFlag = riskFlag;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

}
