package com.iqb.consumer.data.layer.bean.dandelion.entity;

import javax.persistence.Table;

import com.iqb.consumer.data.layer.bean.BaseEntity;

@Table(name = "inst_designperson")
public class InstDesignPersonEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String designCode;
    private String designName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDesignCode() {
        return designCode;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

}
