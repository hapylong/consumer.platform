package com.iqb.consumer.data.layer.biz.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.schedule.entity.InstScheduleTaskEntity;
import com.iqb.consumer.data.layer.bean.schedule.pojo.CfRequestMoneyPojo;
import com.iqb.consumer.data.layer.dao.schedule.ScheduleTaskRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年4月11日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class ScheduleTaskManager extends BaseBiz {

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    public List<String> getChatIds() {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.getChatIds();
    }

    public InstOrderInfoEntity getDetailsByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.getDetailsByOid(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class,
            Exception.class})
    public void removeChatId(CfRequestMoneyPojo cf) throws Exception {
        super.setDb(0, super.MASTER);
        int r1 = scheduleTaskRepository.removeChatId(cf);
        int r2 = scheduleTaskRepository.updateOrderInfo(cf.getOrderId(), new Date());
        if (r1 != 1 || r2 != 1) {
            throw new Exception("removeChatId Exception :\r\n"
                    + "  error orderId is [" + cf.getOrderId() + "]\r\n"
                    + "  method[removeChatId]    update [" + r1 + "]\r\n"
                    + "  method[updateOrderInfo] update [" + r2 + "]");
        }
    }

    public void createISTE(InstScheduleTaskEntity iste) {
        super.setDb(0, super.MASTER);
        scheduleTaskRepository.createISTE(iste);
    }

    public boolean isMerchantNeedSave(String merchantNo) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.getMerchantPublicNo(merchantNo) > 3;
    }
}
