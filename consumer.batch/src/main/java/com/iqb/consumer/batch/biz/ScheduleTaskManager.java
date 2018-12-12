package com.iqb.consumer.batch.biz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iqb.consumer.batch.dao.ScheduleTaskRepository;
import com.iqb.consumer.batch.data.entity.InstScheduleTaskEntity;
import com.iqb.consumer.batch.data.entity.InstSettlementResultEntity;
import com.iqb.consumer.batch.data.pojo.InstOrderInfoEntity;
import com.iqb.consumer.batch.data.pojo.SettlementCenterBuckleRequestMessage;
import com.iqb.consumer.batch.data.pojo.SpecialTimeOrderPojo;
import com.iqb.consumer.batch.data.schedule.pojo.CfRequestMoneyPojo;
import com.iqb.etep.common.base.biz.BaseBiz;

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
    public void removeChatId(CfRequestMoneyPojo cf) {
        super.setDb(0, super.MASTER);
        int r1 = scheduleTaskRepository.removeChatId(cf);
        int r2 = scheduleTaskRepository.updateOrderInfo(cf.getOrderId(), new Date());
        if (r1 != 1 || r2 != 1) {
            try {
                throw new Exception();
            } catch (Exception e) {}
        }
    }

    /**
     * 
     * Description: 获取任务组
     * 
     * @param
     * @return List<InstScheduleTaskEntity>
     * @throws
     * @Author adam Create Date: 2017年6月7日 上午10:23:28
     */
    public List<InstScheduleTaskEntity> cgetISTE(Date start, Date end, Date createTime) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.cgetISTE(start, end, createTime);
    }

    public int updateISTEById(InstScheduleTaskEntity iste) {
        super.setDb(0, super.MASTER);
        return scheduleTaskRepository.updateISTEById(iste);
    }

    /**
     * 
     * Description: 调度任务查找inst_settlementresult
     * 
     * @param end
     * @param start
     * 
     * @param
     * @return List<InstSettlementResultEntity>
     * @throws @Author adam Create Date: 2017年6月26日 下午3:49:07
     */
    public List<InstSettlementResultEntity> getAllISRE(Date start, Date end) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.getAllISRE(start, end);
    }

    /**
     * 
     * Description: 调用结算中心前的相关数据查找
     * 
     * @param
     * @return SettlementCenterBuckleRequestMessage
     * @throws @Author adam Create Date: 2017年6月26日 下午3:48:48
     */
    public SettlementCenterBuckleRequestMessage createBuckleBaseMsg(String orderId) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.createBuckleBaseMsg(orderId);
    }

    /**
     * 
     * Description: inst_settlementresult
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年6月26日 下午3:15:44
     */
    public void updateISREById(InstSettlementResultEntity isre) {
        super.setDb(0, super.MASTER);
        scheduleTaskRepository.updateISREById(isre);
    }

    /**
     * 
     * Description: 通过计算出来的 时间 caculateSpecialTime (CST)查出 wfstatus = 5 的 所有订单
     * 
     * @param
     * @return List<SpecialTimeOrderPojo>
     * @throws @Author adam Create Date: 2017年7月5日 下午6:56:09
     */
    public List<SpecialTimeOrderPojo> getSpecialTime30OrdersByCST(Date caculateSpecialTime) {
        super.setDb(0, super.SLAVE);
        return scheduleTaskRepository.getSpecialTime30OrdersByCST(caculateSpecialTime);
    }

    public void updateSpecialIOIEByOid(String orderId) {
        super.setDb(0, super.MASTER);
        scheduleTaskRepository.updateSpecialIOIEByOid(orderId);
    }

}
