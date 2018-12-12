package com.iqb.consumer.data.layer.bean.finance.pojo.f;

import java.util.List;

public class FinanceBillQueryRequestPojo {

    private Long id; // merList
    private List<String> merchantNos; // merList
    private String realName;// 借款人
    private String regId; // regId
    private String orderId; // orderId
    private String subOrderId; // orderId
    private String lastRepayDate; // lastRepayDate
    private String openId; // openId
    private String startDate; // dateMap.get("startDate
    private String endDate;// dateMap.get("endDate
    private String curRepayDate;// 实际还款日期 "yyyyMMdd"
    private String status;// 0 已逾期 1待还款3已还款
    private Integer numPerPage = 10;// pageSize
    private Integer pageNum = 1;// pageNum
    private Integer repayNo;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<String> getMerchantNos() {
        return merchantNos;
    }

    public void setMerchantNos(List<String> merchantNos) {
        this.merchantNos = merchantNos;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurRepayDate() {
        return curRepayDate;
    }

    public void setCurRepayDate(String curRepayDate) {
        this.curRepayDate = curRepayDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

}
