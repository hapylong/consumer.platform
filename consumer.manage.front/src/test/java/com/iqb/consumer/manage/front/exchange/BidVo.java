package com.iqb.consumer.manage.front.exchange;

import java.util.List;

/**
 * Created by ckq.
 */
public class BidVo {

    private String bidNumber;

    private Double money;

    private List<MaxAmountVo> maxAmountList;

    public List<MaxAmountVo> getMaxAmountList() {
        return maxAmountList;
    }

    public void setMaxAmountList(List<MaxAmountVo> maxAmountList) {
        this.maxAmountList = maxAmountList;
    }

    public String getBidNumber() {
        return bidNumber;
    }

    public void setBidNumber(String bidNumber) {
        this.bidNumber = bidNumber;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

}
