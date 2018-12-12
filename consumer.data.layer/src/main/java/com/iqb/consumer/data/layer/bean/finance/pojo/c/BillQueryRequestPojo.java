package com.iqb.consumer.data.layer.bean.finance.pojo.c;

import java.util.Date;
import java.util.List;

import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;

public class BillQueryRequestPojo {

    private Integer type; // 账单查询类型 【0所有/1待还款/2待还款PAGE/3已还款/4已还款PAGE/5已逾期/6已逾期PAGE】
    private String merchNames; // 渠道名称
    private List<MerchantBean> merList;
    private String realName; // 借款人姓名
    private String orderId; // 父订单
    private String songsong; // 渠道经纪人
    private Date createTime; // 开始时间
    private Date endTime; // 结束时间
    private Date lastRepayTime; // 最后还款日
    private Date repayTime; // 实际还款日期
    private String regId;

    private Integer channal; // 渠道 1以租代售订单，2房账单需要加工
    /** 分页相关 **/
    private Integer pageSize;
    private Integer pageNum;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSongsong() {
        return songsong;
    }

    public void setSongsong(String songsong) {
        this.songsong = songsong;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Date repayTime) {
        this.repayTime = repayTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getMerchNames() {
        return merchNames;
    }

    public void setMerchNames(String merchNames) {
        this.merchNames = merchNames;
    }

    public List<MerchantBean> getMerList() {
        return merList;
    }

    public void setMerList(List<MerchantBean> merList) {
        this.merList = merList;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastRepayTime() {
        return lastRepayTime;
    }

    public void setLastRepayTime(Date lastRepayTime) {
        this.lastRepayTime = lastRepayTime;
    }

    public Integer getChannal() {
        return channal;
    }

    public void setChannal(Integer channal) {
        this.channal = channal;
    }

}
