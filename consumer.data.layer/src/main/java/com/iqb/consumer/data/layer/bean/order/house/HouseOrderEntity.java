package com.iqb.consumer.data.layer.bean.order.house;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;

@Table(name = "eatep_house_order")
public class HouseOrderEntity {

    private String id; //

    private Integer userCode; // 实际借款人ID

    private Integer houseId; // 抵押房屋ID

    private String orderNo; // ORDER_NO

    private String orderName; // 订单名称

    private BigDecimal approvalAmt; // 核准额度

    private String approvalTerm; // 核准期限

    private Long planId;// 方案ID

    private String returnType; // 还款方式

    private BigDecimal monthlyInterest; // 月利率

    private Integer monthlyInterestType; // 利率类型

    private Integer monthItems;// 分期周期

    private Integer monthItemsType; // 分期周期_月/日

    private String returnSource; // 还款来源

    private Integer review;// 是否上评审会

    private Integer expand; // 是否展期

    private BigDecimal expandInterest; // 展期利率

    private String expandTerm; // 展期期限

    private BigDecimal defaultInterest; // 罚息利率

    private String description; // 备注

    private Date createdTime; // 建立时间

    private Date updateTime; // 更新时间

    private Integer black; // 是否黑名单客户

    private Integer appeal; // 是否涉诉

    private Integer bankCredit; // 是否多头授信

    private Integer majorIssue; // 有无重大事项

    private String channelUser; // 渠道经纪人

    private String channelCode; // 组织结构代码

    private BigDecimal ensureRate; // 保证金费率

    private BigDecimal ensureAmt; // 保证金金额

    private Integer wfstatus; //

    private Integer statusDesc; // 订单状态 1.审批中 2.已审批 3.已到账4.待还款 5.已逾期 6.已结清 7.已拒绝, 10.无可分期金额

    private String approvalDesc; // 评审会意见

    private Integer approvalStatus; // 工作流状态 1.初审 2.下户 3.抵押 4.保证金 5.放款

    private BigDecimal applyMoney; // 申请借款金额

    private Integer applyTerm; // 申请借款期限

    private String businessType; // 业务类型

    private String businessSubtype; // 业务子类

    private String userName; // 借款人姓名

    private String idCard; // 借款人身份证号

    private String mobile; // 借款人手机号

    private String married; // 婚姻状况

    private String transctionNo; // 到账流水号

    private String province; // 省

    private String city; // 市

    private String processId; // 流程ID

    private String reviewDesc; // 上会理由

    private Byte returnFlag; // 是否风控初审标识 0 否 1 是

    private String instDetail; // 分期详情

    private List<InstdetailPojo> instDetailList; // 分期详情

    private String ledprocessId;

    private String bankCard;

    private String bankName;

    private String reserveMobile;

    private String reserveDesc;

    private Integer loanForm;

    private Integer loanFlag;

    private String loanOptions;

    private Long receivableAmt;

    private BigDecimal realInstAmt;

    private Integer allotStatus;// 1可资产分配 2 已经分配完成

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getApprovalAmt() {
        return approvalAmt;
    }

    public void setApprovalAmt(BigDecimal approvalAmt) {
        this.approvalAmt = approvalAmt;
    }

    public String getApprovalTerm() {
        return approvalTerm;
    }

    public void setApprovalTerm(String approvalTerm) {
        this.approvalTerm = approvalTerm;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public BigDecimal getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(BigDecimal monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public Integer getMonthlyInterestType() {
        return monthlyInterestType;
    }

    public void setMonthlyInterestType(Integer monthlyInterestType) {
        this.monthlyInterestType = monthlyInterestType;
    }

    public Integer getMonthItems() {
        return monthItems;
    }

    public void setMonthItems(Integer monthItems) {
        this.monthItems = monthItems;
    }

    public Integer getMonthItemsType() {
        return monthItemsType;
    }

    public void setMonthItemsType(Integer monthItemsType) {
        this.monthItemsType = monthItemsType;
    }

    public String getReturnSource() {
        return returnSource;
    }

    public void setReturnSource(String returnSource) {
        this.returnSource = returnSource;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public Integer getExpand() {
        return expand;
    }

    public void setExpand(Integer expand) {
        this.expand = expand;
    }

    public BigDecimal getExpandInterest() {
        return expandInterest;
    }

    public void setExpandInterest(BigDecimal expandInterest) {
        this.expandInterest = expandInterest;
    }

    public String getExpandTerm() {
        return expandTerm;
    }

    public void setExpandTerm(String expandTerm) {
        this.expandTerm = expandTerm;
    }

    public BigDecimal getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(BigDecimal defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getBlack() {
        return black;
    }

    public void setBlack(Integer black) {
        this.black = black;
    }

    public Integer getAppeal() {
        return appeal;
    }

    public void setAppeal(Integer appeal) {
        this.appeal = appeal;
    }

    public Integer getBankCredit() {
        return bankCredit;
    }

    public Integer getAllotStatus() {
        return allotStatus;
    }

    public void setAllotStatus(Integer allotStatus) {
        this.allotStatus = allotStatus;
    }

    public void setBankCredit(Integer bankCredit) {
        this.bankCredit = bankCredit;
    }

    public Integer getMajorIssue() {
        return majorIssue;
    }

    public void setMajorIssue(Integer majorIssue) {
        this.majorIssue = majorIssue;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public BigDecimal getEnsureRate() {
        return ensureRate;
    }

    public void setEnsureRate(BigDecimal ensureRate) {
        this.ensureRate = ensureRate;
    }

    public BigDecimal getEnsureAmt() {
        return ensureAmt;
    }

    public void setEnsureAmt(BigDecimal ensureAmt) {
        this.ensureAmt = ensureAmt;
    }

    public Integer getWfstatus() {
        return wfstatus;
    }

    public void setWfstatus(Integer wfstatus) {
        this.wfstatus = wfstatus;
    }

    public Integer getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(Integer statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getApprovalDesc() {
        return approvalDesc;
    }

    public void setApprovalDesc(String approvalDesc) {
        this.approvalDesc = approvalDesc;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public Integer getApplyTerm() {
        return applyTerm;
    }

    public void setApplyTerm(Integer applyTerm) {
        this.applyTerm = applyTerm;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessSubtype() {
        return businessSubtype;
    }

    public void setBusinessSubtype(String businessSubtype) {
        this.businessSubtype = businessSubtype;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getTransctionNo() {
        return transctionNo;
    }

    public void setTransctionNo(String transctionNo) {
        this.transctionNo = transctionNo;
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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getReviewDesc() {
        return reviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    public Byte getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(Byte returnFlag) {
        this.returnFlag = returnFlag;
    }

    public List<InstdetailPojo> getInstDetailList() {
        return instDetailList;
    }

    public String getLedprocessId() {
        return ledprocessId;
    }

    public void setLedprocessId(String ledprocessId) {
        this.ledprocessId = ledprocessId;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReserveMobile() {
        return reserveMobile;
    }

    public void setReserveMobile(String reserveMobile) {
        this.reserveMobile = reserveMobile;
    }

    public String getReserveDesc() {
        return reserveDesc;
    }

    public void setReserveDesc(String reserveDesc) {
        this.reserveDesc = reserveDesc;
    }

    public Integer getLoanForm() {
        return loanForm;
    }

    public void setLoanForm(Integer loanForm) {
        this.loanForm = loanForm;
    }

    public Integer getLoanFlag() {
        return loanFlag;
    }

    public void setLoanFlag(Integer loanFlag) {
        this.loanFlag = loanFlag;
    }

    public String getLoanOptions() {
        return loanOptions;
    }

    public void setLoanOptions(String loanOptions) {
        this.loanOptions = loanOptions;
    }

    public Long getReceivableAmt() {
        return receivableAmt;
    }

    public void setReceivableAmt(Long receivableAmt) {
        this.receivableAmt = receivableAmt;
    }

    public BigDecimal getRealInstAmt() {
        return realInstAmt;
    }

    public void setRealInstAmt(BigDecimal realInstAmt) {
        this.realInstAmt = realInstAmt;
    }

    public void setInstDetailList(List<InstdetailPojo> instDetailList) {
        if (instDetailList != null) {
            this.instDetail = JSONObject.toJSONString(instDetailList);
        }
        this.instDetailList = instDetailList;
    }

    public String getInstDetail() {
        return instDetail;
    }

    public void setInstDetail(String instDetail) {
        if (!StringUtil.isEmpty(instDetail)) {
            this.instDetailList = JSONObject.parseArray(instDetail, InstdetailPojo.class);
        } else {
            this.instDetailList = new ArrayList<>();
        }
        this.instDetail = instDetail;
    }

    public boolean checkEntity() {
        return true;
    }
}
