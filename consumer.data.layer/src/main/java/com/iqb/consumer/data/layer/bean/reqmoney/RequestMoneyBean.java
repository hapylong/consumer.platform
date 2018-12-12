/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月27日 下午3:56:20
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.reqmoney;

import java.util.Calendar;

import com.iqb.consumer.common.constant.DictConvertCenter;
import com.iqb.consumer.common.constant.DictConvertCenter.DICT_TYPE_CODE;
import com.iqb.consumer.common.constant.DictConvertCenter.TARGET_COLUMN;
import com.iqb.consumer.common.constant.DictConvertCenter.WHERE_COLUMN;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class RequestMoneyBean extends BaseEntity {

    private String projectName;// 项目名称
    private String realName;// 真实姓名
    private String merchName;// 商户名称
    private String orderId;// 订单号
    private String orderNo;// 订单C端号
    private String planFullName;// 计划名称
    private Integer orderItems;// 总期数
    private Integer applyItems;// 成功申请期数
    private String leftItems;// 剩余期数
    private String orderAmt;// 订单金额
    @DictConvertCenter(from = WHERE_COLUMN.DICT_VALUE, to = TARGET_COLUMN.DICT_NAME, type = DICT_TYPE_CODE.fund_source)
    private String sourcesFunding;// 资金来源
    private String applyTime;// 资产分配时间
    private String firstApplyTime;// 第一次推标时间
    private Integer status;
    private String remark;// 备注
    private String deadline;// 标的结束时间

    private Integer isWithholding;// 是否放款代扣
    private Integer isPublic;// 是否公开
    private Integer isPushff;// 是否推送饭饭

    private Calendar assetDueDate = Calendar.getInstance();// 资产到期日
    private String allotRegid; // 资产分配人手机号码
    private int pushId; // 上标ID
    private String creditName;// 债权人姓名
    private String creditCardNo;// 债权人身份证号
    private String creditBankCard;// 债权人银行卡号
    private String creditBank;// 债权人开户银行
    private String creditPhone;// 债权人手机号
    private String planLendingTime;// 预计放款时间
    private String regId;// 手机号码
    private String allotName;// 资产分配人
    private String redemptionDate;// 赎回时间
    private String redemptionReason;// 赎回原因
    private int applyInstIDay;// 分配天数
    private String lendersSubject;// 放款主体
    private String applyAmt;// 资产分配金额

    private String curRepayNo;// 当前期数
    private String pushMode;// 推标金额方式 1 按订单金额 2 按剩余未还本金

    private int wfStatus;
    private int riskStatus;

    public String getLendersSubject() {
        return lendersSubject;
    }

    public void setLendersSubject(String lendersSubject) {
        this.lendersSubject = lendersSubject;
    }

    public int getApplyInstIDay() {
        return applyInstIDay;
    }

    public void setApplyInstIDay(int applyInstIDay) {
        this.applyInstIDay = applyInstIDay;
    }

    public String getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(String redemptionDate) {
        this.redemptionDate = redemptionDate;
    }

    public void setLeftItems(String leftItems) {
        this.leftItems = leftItems;
    }

    public String getPlanLendingTime() {
        return planLendingTime;
    }

    public void setPlanLendingTime(String planLendingTime) {
        this.planLendingTime = planLendingTime;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditBankCard() {
        return creditBankCard;
    }

    public void setCreditBankCard(String creditBankCard) {
        this.creditBankCard = creditBankCard;
    }

    public String getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(String creditBank) {
        this.creditBank = creditBank;
    }

    public String getCreditPhone() {
        return creditPhone;
    }

    public void setCreditPhone(String creditPhone) {
        this.creditPhone = creditPhone;
    }

    // 资产到期日 = applyTime + orderItems
    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getFirstApplyTime() {
        return firstApplyTime;
    }

    public void setFirstApplyTime(String firstApplyTime) {
        this.firstApplyTime = firstApplyTime;
        if (orderItems != null) {
            if (!StringUtil.isEmpty(firstApplyTime)) {
                Calendar c = DateUtil.parseCalendar(firstApplyTime, DateUtil.SIMPLE_DATE_FORMAT);
                assetDueDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + orderItems, c.get(Calendar.DATE),
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            }

        }
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
        if (!StringUtil.isEmpty(firstApplyTime)) {
            if (orderItems != null) {
                Calendar c = DateUtil.parseCalendar(firstApplyTime, DateUtil.SIMPLE_DATE_FORMAT);
                assetDueDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + orderItems, c.get(Calendar.DATE),
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            }
        }
    }

    public Integer getApplyItems() {
        return applyItems;
    }

    public void setApplyItems(Integer applyItems) {
        this.applyItems = applyItems;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Integer getIsWithholding() {
        return isWithholding;
    }

    public void setIsWithholding(Integer isWithholding) {
        this.isWithholding = isWithholding;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getIsPushff() {
        return isPushff;
    }

    public void setIsPushff(Integer isPushff) {
        this.isPushff = isPushff;
    }

    public Calendar getAssetDueDate() {
        return assetDueDate;
    }

    public void setAssetDueDate(Calendar assetDueDate) {
        this.assetDueDate = assetDueDate;
    }

    public String getAllotRegid() {
        return allotRegid;
    }

    public void setAllotRegid(String allotRegid) {
        this.allotRegid = allotRegid;
    }

    public int getPushId() {
        return pushId;
    }

    public void setPushId(int pushId) {
        this.pushId = pushId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getAllotName() {
        return allotName;
    }

    public void setAllotName(String allotName) {
        this.allotName = allotName;
    }

    public String getRedemptionReason() {
        return redemptionReason;
    }

    public void setRedemptionReason(String redemptionReason) {
        this.redemptionReason = redemptionReason;
    }

    public String getLeftItems() {
        return leftItems;
    }

    public String getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(String applyAmt) {
        this.applyAmt = applyAmt;
    }

    public String getCurRepayNo() {
        return curRepayNo;
    }

    public void setCurRepayNo(String curRepayNo) {
        this.curRepayNo = curRepayNo;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

}
