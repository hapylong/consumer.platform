package com.iqb.consumer.data.layer.biz.pledge;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.dao.pledge.PledgeInquiryDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 质押车Biz
 * 
 * @author guojuan
 * 
 */
@Component
public class PledgeInquiryBiz extends BaseBiz {
    @Autowired
    private PledgeInquiryDao pledgeInquiryDao;

    /**
     * 获取表数据
     * 
     * @param objs
     * @return
     */
    public PledgeInfoBean getPledgeOrderInfo(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return this.pledgeInquiryDao.getPledgeOrderInfo(objs);
    }

    public void updateOrderInfo(JSONObject object) {
        super.setDb(0, super.SLAVE);
        this.pledgeInquiryDao.updateOrderInfo(object);
    }

    public void updatePledgeInfo(JSONObject object) {
        super.setDb(0, super.SLAVE);
        this.pledgeInquiryDao.updatePledgeInfo(object);
    }

    public void updateOrderInfoRiskStatus(Map<String, String> params) {
        super.setDb(0, super.SLAVE);
        this.pledgeInquiryDao.updateOrderInfoRiskStatus(params);
    }

    public int updatePledgeStatus(Map<String, String> params) {
        super.setDb(0, super.MASTER);
        return this.pledgeInquiryDao.updatePledgeStatus(params);
    }

    public String getUuidByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return pledgeInquiryDao.getUuidByOid(orderId);
    }
}
