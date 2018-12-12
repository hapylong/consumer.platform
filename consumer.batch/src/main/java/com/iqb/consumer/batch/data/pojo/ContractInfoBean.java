package com.iqb.consumer.batch.data.pojo;

/**
 * 
 * Description: 电子合同信息bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年2月24日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class ContractInfoBean {

    private Integer id;
    private String bizId;// 业务ID
    private Integer bizType;// 业务类型
    private String bizConfigId;// 业务模板ID
    private String orgCode;// 机构代码
    private String orgName;// 机构名称
    private String ecContractNo;// 合同编号
    private String ecTplId;// 合同模板ID
    private String ecFileName;// 合同文件名称
    private byte[] ecFile;// 合同文件
    private String ecTheme;// 合同主题
    private String ecAbstract;// 合同内容摘要
    private Integer ecEffectiveDays;// 合同有效天数
    private String ecFileType;// 合同类型
    private byte[] ecFileBlob;// 合同模板数据BLOB
    private String ecSenderName;// 发件人姓名
    private String ecSenderAcc;// 发件人账户
    private Integer ecSenderType;// 发件人用户类型（1.个人；2.企业）
    private Integer isSenderSign;// 发件人是否签署
    private Integer ecSenderSignimgType;// 当用户不存在时生成系统自动签名
    private Integer ecSenderUserFiletype;// 用户使用文件类型
    private String ecType;// 签署方类型（dz:电子合同 zz:纸质合同）
    private String ecNotifyUrl;// 合同提交成功通知地址
    private String ecTaskUrl;// 合同全部签署完成通知地址
    private String tpUploadRetInfo;// 第三方合同上传返回信息
    private String tpUploadRetCode;// 第三方合同上传返回码
    private String tpUploadRetContent;// 第三方合同上传返回信息
    private String tpSignid;// 第三方合同编号
    private String tpDocid;// 第三方文档存储编号
    private String tpVateCode;// 第三方签名码
    private String ecViewUrl;// 合同查看地址
    private String ecDownloadUrl;// 合同下载地址
    private Integer ecDownloadTimes;// 合同下载次数
    private Integer ecSendtime;// 合同发送时间
    private Integer ecSignNum;// 签署人数
    private Integer status;// 合同状态(1待提交，2提交成功，3提交失败)
    private Integer finishSignTime;// 签署完成时间

    /************************************* 业务相关 *****************************************/
    private String ecTemplateAttr;// ec模板替换

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getBizConfigId() {
        return bizConfigId;
    }

    public void setBizConfigId(String bizConfigId) {
        this.bizConfigId = bizConfigId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getEcContractNo() {
        return ecContractNo;
    }

    public void setEcContractNo(String ecContractNo) {
        this.ecContractNo = ecContractNo;
    }

    public String getEcTplId() {
        return ecTplId;
    }

    public void setEcTplId(String ecTplId) {
        this.ecTplId = ecTplId;
    }

    public String getEcFileName() {
        return ecFileName;
    }

    public void setEcFileName(String ecFileName) {
        this.ecFileName = ecFileName;
    }

    public byte[] getEcFile() {
        return ecFile;
    }

    public void setEcFile(byte[] ecFile) {
        this.ecFile = ecFile;
    }

    public String getEcTheme() {
        return ecTheme;
    }

    public void setEcTheme(String ecTheme) {
        this.ecTheme = ecTheme;
    }

    public String getEcAbstract() {
        return ecAbstract;
    }

    public void setEcAbstract(String ecAbstract) {
        this.ecAbstract = ecAbstract;
    }

    public Integer getEcEffectiveDays() {
        return ecEffectiveDays;
    }

    public void setEcEffectiveDays(Integer ecEffectiveDays) {
        this.ecEffectiveDays = ecEffectiveDays;
    }

    public String getEcFileType() {
        return ecFileType;
    }

    public void setEcFileType(String ecFileType) {
        this.ecFileType = ecFileType;
    }

    public byte[] getEcFileBlob() {
        return ecFileBlob;
    }

    public void setEcFileBlob(byte[] ecFileBlob) {
        this.ecFileBlob = ecFileBlob;
    }

    public String getEcSenderName() {
        return ecSenderName;
    }

    public void setEcSenderName(String ecSenderName) {
        this.ecSenderName = ecSenderName;
    }

    public String getEcSenderAcc() {
        return ecSenderAcc;
    }

    public void setEcSenderAcc(String ecSenderAcc) {
        this.ecSenderAcc = ecSenderAcc;
    }

    public Integer getEcSenderType() {
        return ecSenderType;
    }

    public void setEcSenderType(Integer ecSenderType) {
        this.ecSenderType = ecSenderType;
    }

    public Integer getIsSenderSign() {
        return isSenderSign;
    }

    public void setIsSenderSign(Integer isSenderSign) {
        this.isSenderSign = isSenderSign;
    }

    public Integer getEcSenderSignimgType() {
        return ecSenderSignimgType;
    }

    public void setEcSenderSignimgType(Integer ecSenderSignimgType) {
        this.ecSenderSignimgType = ecSenderSignimgType;
    }

    public Integer getEcSenderUserFiletype() {
        return ecSenderUserFiletype;
    }

    public void setEcSenderUserFiletype(Integer ecSenderUserFiletype) {
        this.ecSenderUserFiletype = ecSenderUserFiletype;
    }

    public String getEcType() {
        return ecType;
    }

    public void setEcType(String ecType) {
        this.ecType = ecType;
    }

    public String getEcNotifyUrl() {
        return ecNotifyUrl;
    }

    public void setEcNotifyUrl(String ecNotifyUrl) {
        this.ecNotifyUrl = ecNotifyUrl;
    }

    public String getEcTaskUrl() {
        return ecTaskUrl;
    }

    public void setEcTaskUrl(String ecTaskUrl) {
        this.ecTaskUrl = ecTaskUrl;
    }

    public String getTpUploadRetInfo() {
        return tpUploadRetInfo;
    }

    public void setTpUploadRetInfo(String tpUploadRetInfo) {
        this.tpUploadRetInfo = tpUploadRetInfo;
    }

    public String getTpUploadRetCode() {
        return tpUploadRetCode;
    }

    public void setTpUploadRetCode(String tpUploadRetCode) {
        this.tpUploadRetCode = tpUploadRetCode;
    }

    public String getTpUploadRetContent() {
        return tpUploadRetContent;
    }

    public void setTpUploadRetContent(String tpUploadRetContent) {
        this.tpUploadRetContent = tpUploadRetContent;
    }

    public String getTpSignid() {
        return tpSignid;
    }

    public void setTpSignid(String tpSignid) {
        this.tpSignid = tpSignid;
    }

    public String getTpDocid() {
        return tpDocid;
    }

    public void setTpDocid(String tpDocid) {
        this.tpDocid = tpDocid;
    }

    public String getTpVateCode() {
        return tpVateCode;
    }

    public void setTpVateCode(String tpVateCode) {
        this.tpVateCode = tpVateCode;
    }

    public String getEcViewUrl() {
        return ecViewUrl;
    }

    public void setEcViewUrl(String ecViewUrl) {
        this.ecViewUrl = ecViewUrl;
    }

    public String getEcDownloadUrl() {
        return ecDownloadUrl;
    }

    public void setEcDownloadUrl(String ecDownloadUrl) {
        this.ecDownloadUrl = ecDownloadUrl;
    }

    public Integer getEcDownloadTimes() {
        return ecDownloadTimes;
    }

    public void setEcDownloadTimes(Integer ecDownloadTimes) {
        this.ecDownloadTimes = ecDownloadTimes;
    }

    public Integer getEcSendtime() {
        return ecSendtime;
    }

    public void setEcSendtime(Integer ecSendtime) {
        this.ecSendtime = ecSendtime;
    }

    public Integer getEcSignNum() {
        return ecSignNum;
    }

    public void setEcSignNum(Integer ecSignNum) {
        this.ecSignNum = ecSignNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFinishSignTime() {
        return finishSignTime;
    }

    public void setFinishSignTime(Integer finishSignTime) {
        this.finishSignTime = finishSignTime;
    }

    public String getEcTemplateAttr() {
        return ecTemplateAttr;
    }

    public void setEcTemplateAttr(String ecTemplateAttr) {
        this.ecTemplateAttr = ecTemplateAttr;
    }

}
