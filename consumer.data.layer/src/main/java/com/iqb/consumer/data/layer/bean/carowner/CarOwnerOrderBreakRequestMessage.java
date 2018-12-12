package com.iqb.consumer.data.layer.bean.carowner;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.EntityUtil;

public class CarOwnerOrderBreakRequestMessage extends EntityUtil {
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String orderId;
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String beginDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    @Override
    public String toString() {
        return "CarOwnerOrderBreakRequestMessage [orderId=" + orderId + ", beginDate=" + beginDate + "]";
    }

}
