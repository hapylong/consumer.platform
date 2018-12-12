package com.iqb.consumer.data.layer.bean.credit_product.pojo;

import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderBreakInfoEntity;

public class GuaranteePledgeInfoBridge {
    public enum StatusEnum {
        FAIL, EQUAL, UNVEN
    }

    private StatusEnum status;
    private InstOrderBreakInfoEntity entity;

    public GuaranteePledgeInfoBridge(StatusEnum status,
            InstOrderBreakInfoEntity entity) {
        this.status = status;
        this.entity = entity;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public InstOrderBreakInfoEntity getEntity() {
        return entity;
    }

    public void setEntity(InstOrderBreakInfoEntity entity) {
        this.entity = entity;
    }

}
