/**
 * Copyright 2017 bejson.com
 */
package com.iqb.consumer.data.layer.bean.jys;

import java.util.List;

public class Bidlist {

    private String bidNumber;
    private String money;
    private List<Maxamountlist> maxAmountList;

    public String getBidNumber() {
        return bidNumber;
    }

    public void setBidNumber(String bidNumber) {
        this.bidNumber = bidNumber;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<Maxamountlist> getMaxAmountList() {
        return maxAmountList;
    }

    public void setMaxAmountList(List<Maxamountlist> maxAmountList) {
        this.maxAmountList = maxAmountList;
    }
}
