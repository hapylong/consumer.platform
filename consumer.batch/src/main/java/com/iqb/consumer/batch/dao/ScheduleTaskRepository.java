package com.iqb.consumer.batch.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.batch.data.entity.InstScheduleTaskEntity;
import com.iqb.consumer.batch.data.entity.InstSettlementResultEntity;
import com.iqb.consumer.batch.data.pojo.InstOrderInfoEntity;
import com.iqb.consumer.batch.data.pojo.SettlementCenterBuckleRequestMessage;
import com.iqb.consumer.batch.data.pojo.SpecialTimeOrderPojo;
import com.iqb.consumer.batch.data.schedule.pojo.CfRequestMoneyPojo;

public interface ScheduleTaskRepository {

    public List<String> getChatIds();

    public InstOrderInfoEntity getDetailsByOid(String orderId);

    public int removeChatId(CfRequestMoneyPojo cf);

    public int updateOrderInfo(@Param("orderId") String orderId, @Param("updateTime") Date updateTime);

    public List<InstScheduleTaskEntity> cgetISTE(@Param("start") Date start, @Param("end") Date end,
            @Param("createTime") Date createTime);

    public int updateISTEById(InstScheduleTaskEntity iste);

    public List<InstSettlementResultEntity> getAllISRE(@Param("start") Date start, @Param("end") Date end);

    public SettlementCenterBuckleRequestMessage createBuckleBaseMsg(String orderId);

    public void updateISREById(InstSettlementResultEntity isre);

    public List<SpecialTimeOrderPojo> getSpecialTime30OrdersByCST(Date cst);

    public void updateSpecialIOIEByOid(String orderId);

}
