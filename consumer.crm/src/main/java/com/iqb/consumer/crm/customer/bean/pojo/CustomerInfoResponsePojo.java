package com.iqb.consumer.crm.customer.bean.pojo;

import java.util.List;

import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;

public class CustomerInfoResponsePojo {

    private List<IqbCustomerStoreInfoEntity> icsi;
    private CustomerBean cb;

    public List<IqbCustomerStoreInfoEntity> getIcsi() {
        return icsi;
    }

    public void setIcsi(List<IqbCustomerStoreInfoEntity> icsi) {
        this.icsi = icsi;
    }

    public CustomerBean getCb() {
        return cb;
    }

    public void setCb(CustomerBean cb) {
        this.cb = cb;
    }
}
