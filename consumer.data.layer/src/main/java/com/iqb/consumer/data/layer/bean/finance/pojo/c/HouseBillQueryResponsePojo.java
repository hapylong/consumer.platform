package com.iqb.consumer.data.layer.bean.finance.pojo.c;

import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillPojo;

public class HouseBillQueryResponsePojo extends FinanceBillPojo {

    private String channal; // 渠道名称
    private String songsong; // 渠道经纪人

    private String serialNumber; // 资金流水号

    public String getChannal() {
        return channal;
    }

    public void setChannal(String channal) {
        this.channal = channal;
    }

    public String getSongsong() {
        return songsong;
    }

    public void setSongsong(String songsong) {
        this.songsong = songsong;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
