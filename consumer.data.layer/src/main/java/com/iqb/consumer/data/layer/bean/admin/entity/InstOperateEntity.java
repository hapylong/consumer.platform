package com.iqb.consumer.data.layer.bean.admin.entity;

import javax.persistence.Table;

import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.admin.request.ChangePlanRequestMessage;

@Table(name = "inst_operate")
public class InstOperateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String realName;
    private String orgCode;
    private String reason;
    private Integer target; // 0, 重置分期 / 1, 停止分期

    public static final int PLAN_TARGET_RESET = 0;
    public static final int PLAN_TARGET_STOP = 1;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "InstOperateEntity [orderId=" + orderId + ", realName=" + realName + ", orgCode=" + orgCode
                + ", reason=" + reason + ", target=" + target + "]";
    }

    public void createEntity(ChangePlanRequestMessage cprm) throws GenerallyException {
        print();
        reason = cprm.getDescribe();
        realName = cprm.getHandler();
        orgCode = cprm.getMerchantId();
        orderId = cprm.getOrderId();
        target = converTarget(cprm.getTarget());
    }

    private int converTarget(String t) throws GenerallyException {
        switch (t) {
            case ChangePlanRequestMessage.PLAN_TARGET_RESET:
                return InstOperateEntity.PLAN_TARGET_RESET;
            case ChangePlanRequestMessage.PLAN_TARGET_STOP:
                return InstOperateEntity.PLAN_TARGET_STOP;
            default:
                throw new GenerallyException(Reason.UNKNOW_TYPE, Layer.UTIL, Location.A);
        }
    }

}
