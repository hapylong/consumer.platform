package com.iqb.consumer.service.layer.credit_product;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.DetailsInfoPojo;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.GetPlanDetailsRequestMessage;

public interface CreditProductService {

    boolean merchantCertificate(JSONObject requestMessage);

    boolean guaranteePledgeInfoManager(JSONObject requestMessage,
            int serviceCode)
            throws Throwable;

    PlanDetailsPojo getPlanDetails(BigDecimal orderAmt, long planId);

    /** 包含服务费计算月供 **/
    PlanDetailsPojo getSpePlanDetails(BigDecimal orderAmt, long planId);

    boolean persistPlanDetailsX(GetPlanDetailsRequestMessage gdrm, int serviceCode)
            throws CreditProductlInvalidException;

    DetailsInfoPojo getInfoDetailsX(String orderIdx) throws CreditProductlInvalidException;

    /**
     * 
     * Description: 判断主订单是否完成初审
     * 
     * @param
     * @return boolean
     * @throws
     * @Author adam Create Date: 2017年5月18日 下午1:55:27
     */
    boolean isOverFirstTrial(String orderIdx) throws CreditProductlInvalidException;

}
