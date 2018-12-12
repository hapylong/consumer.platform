/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年7月10日下午5:46:23 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author haojinlong 车辆状态跟踪bean
 * 
 */
public class ManageCarInfoBean {
    private String merchantNo; // 商户名称
    private String orderId; // 订单号
    private String realName; // 用户真实姓名
    private String regId; // 手机号码
    private BigDecimal orderAmt; // 订单金额
    private int orderItems; // 订单期数
    private BigDecimal monthInterest;// 月供
    private String orderName; // 订单名称 -品牌+车系
    private BigDecimal preAmt; // 预付款
    private String carNo; // 车架号
    private String plate; // 车牌号码
    private String trailerReason; // 拖车原因
    private Date intoGarageDate; // 入库时间
    private String intoGarageRemark; // 入库备注
    private String lostContactReason; // 失联原因
    private BigDecimal assessAmt; // 门店评估金额
    private String dealerEvaluatesInfo;// 车商评估信息
    private Date afterLoanDate; // 转贷后时间
    private String saleRemark; // 车辆出售备注
    private BigDecimal hqEvaluatesAmt; // 总部评估价格
    private BigDecimal hqCheckAmt; // 总部核对价格
    private String evaluatesRemark; // 评估备注
    private BigDecimal shouldPayAmt; // 应还金额
    private BigDecimal dealAmt; // 处置金额
    private BigDecimal receivedAmt; // 到账金额
    private String serialNumber; // 流水号
    private String subleaseInfo; // 转租信息
    private String returnInfo; // 返还信息
    private String planName; // 产品方案
    private int status;

    // 承租人信息
    private String subleaseOrderId; // 承租订单号
    private String subleaseRegId; // 承租人手机号码
    private String subleaseRealName; // 承租人姓名
    private int subleaseOrderItems; // 承租订单总期数
    private BigDecimal subleaseAmount; // 承租订单金额
    private String subleasePlan; // 承租计划
    private BigDecimal subleasePreAmount; // 承租预付款
    private BigDecimal subleaseMonthInterest; // 承租月供
    private String subleaseRemark; // 承租备注

    private List<Map<String, String>> dealerEvaluatesInfoList;
    private Map<String, String> subleaseInfoMap;
    private Map<String, String> returnInfoMap;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

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

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getTrailerReason() {
        return trailerReason;
    }

    public void setTrailerReason(String trailerReason) {
        this.trailerReason = trailerReason;
    }

    public Date getIntoGarageDate() {
        return intoGarageDate;
    }

    public void setIntoGarageDate(Date intoGarageDate) {
        this.intoGarageDate = intoGarageDate;
    }

    public String getIntoGarageRemark() {
        return intoGarageRemark;
    }

    public void setIntoGarageRemark(String intoGarageRemark) {
        this.intoGarageRemark = intoGarageRemark;
    }

    public String getLostContactReason() {
        return lostContactReason;
    }

    public void setLostContactReason(String lostContactReason) {
        this.lostContactReason = lostContactReason;
    }

    public BigDecimal getAssessAmt() {
        return assessAmt;
    }

    public void setAssessAmt(BigDecimal assessAmt) {
        this.assessAmt = assessAmt;
    }

    public String getDealerEvaluatesInfo() {
        return dealerEvaluatesInfo;
    }

    public void setDealerEvaluatesInfo(String dealerEvaluatesInfo) {
        this.dealerEvaluatesInfo = dealerEvaluatesInfo;
    }

    public Date getAfterLoanDate() {
        return afterLoanDate;
    }

    public void setAfterLoanDate(Date afterLoanDate) {
        this.afterLoanDate = afterLoanDate;
    }

    public String getSaleRemark() {
        return saleRemark;
    }

    public void setSaleRemark(String saleRemark) {
        this.saleRemark = saleRemark;
    }

    public BigDecimal getHqEvaluatesAmt() {
        return hqEvaluatesAmt;
    }

    public void setHqEvaluatesAmt(BigDecimal hqEvaluatesAmt) {
        this.hqEvaluatesAmt = hqEvaluatesAmt;
    }

    public BigDecimal getHqCheckAmt() {
        return hqCheckAmt;
    }

    public void setHqCheckAmt(BigDecimal hqCheckAmt) {
        this.hqCheckAmt = hqCheckAmt;
    }

    public String getEvaluatesRemark() {
        return evaluatesRemark;
    }

    public void setEvaluatesRemark(String evaluatesRemark) {
        this.evaluatesRemark = evaluatesRemark;
    }

    public BigDecimal getShouldPayAmt() {
        return shouldPayAmt;
    }

    public void setShouldPayAmt(BigDecimal shouldPayAmt) {
        this.shouldPayAmt = shouldPayAmt;
    }

    public BigDecimal getDealAmt() {
        return dealAmt;
    }

    public void setDealAmt(BigDecimal dealAmt) {
        this.dealAmt = dealAmt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSubleaseInfo() {
        return subleaseInfo;
    }

    public void setSubleaseInfo(String subleaseInfo) {
        this.subleaseInfo = subleaseInfo;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getSubleaseOrderId() {
        return subleaseOrderId;
    }

    public void setSubleaseOrderId(String subleaseOrderId) {
        this.subleaseOrderId = subleaseOrderId;
    }

    public String getSubleaseRegId() {
        return subleaseRegId;
    }

    public void setSubleaseRegId(String subleaseRegId) {
        this.subleaseRegId = subleaseRegId;
    }

    public String getSubleaseRealName() {
        return subleaseRealName;
    }

    public void setSubleaseRealName(String subleaseRealName) {
        this.subleaseRealName = subleaseRealName;
    }

    public int getSubleaseOrderItems() {
        return subleaseOrderItems;
    }

    public void setSubleaseOrderItems(int subleaseOrderItems) {
        this.subleaseOrderItems = subleaseOrderItems;
    }

    public BigDecimal getSubleaseAmount() {
        return subleaseAmount;
    }

    public void setSubleaseAmount(BigDecimal subleaseAmount) {
        this.subleaseAmount = subleaseAmount;
    }

    public String getSubleasePlan() {
        return subleasePlan;
    }

    public void setSubleasePlan(String subleasePlan) {
        this.subleasePlan = subleasePlan;
    }

    public BigDecimal getSubleasePreAmount() {
        return subleasePreAmount;
    }

    public void setSubleasePreAmount(BigDecimal subleasePreAmount) {
        this.subleasePreAmount = subleasePreAmount;
    }

    public BigDecimal getSubleaseMonthInterest() {
        return subleaseMonthInterest;
    }

    public void setSubleaseMonthInterest(BigDecimal subleaseMonthInterest) {
        this.subleaseMonthInterest = subleaseMonthInterest;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSubleaseRemark() {
        return subleaseRemark;
    }

    public void setSubleaseRemark(String subleaseRemark) {
        this.subleaseRemark = subleaseRemark;
    }

    public List<Map<String, String>> getDealerEvaluatesInfoList() {
        return dealerEvaluatesInfoList;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setDealerEvaluatesInfoList(String dealerEvaluatesInfo) {
        JSONArray obj = JSON.parseArray(dealerEvaluatesInfo);
        this.dealerEvaluatesInfoList = (List) obj;
    }

    public Map<String, String> getSubleaseInfoMap() {
        return subleaseInfoMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setSubleaseInfoMap(String subleaseInfo) {
        JSONObject obj = JSON.parseObject(subleaseInfo);
        this.subleaseInfoMap = (Map) obj;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ManageCarInfoBean other = (ManageCarInfoBean) obj;
        if (orderId == null) {
            if (other.orderId != null) return false;
        } else if (!orderId.equals(other.orderId)) return false;
        return true;
    }

    public Map<String, String> getReturnInfoMap() {
        return returnInfoMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setReturnInfoMap(String returnInfo) {
        JSONObject obj = JSON.parseObject(returnInfo);
        this.returnInfoMap = (Map) obj;
    }

}
