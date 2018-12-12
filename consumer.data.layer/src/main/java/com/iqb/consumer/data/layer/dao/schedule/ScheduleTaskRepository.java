package com.iqb.consumer.data.layer.dao.schedule;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.schedule.entity.InstScheduleTaskEntity;
import com.iqb.consumer.data.layer.bean.schedule.pojo.CfRequestMoneyPojo;

public interface ScheduleTaskRepository {

    public List<String> getChatIds();

    public InstOrderInfoEntity getDetailsByOid(String orderId);

    public int removeChatId(CfRequestMoneyPojo cf);

    public int updateOrderInfo(@Param("orderId") String orderId, @Param("updateTime") Date updateTime);

    public void createISTE(InstScheduleTaskEntity iste);

    public int getMerchantPublicNo(String merchantNo);

}
