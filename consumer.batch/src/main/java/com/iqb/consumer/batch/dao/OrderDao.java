package com.iqb.consumer.batch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.batch.data.pojo.InstallmentBillPojo;
import com.iqb.consumer.batch.data.pojo.ManageCarInfoBean;
import com.iqb.consumer.batch.data.pojo.OrderBean;

/**
 * 订单Dao
 */
public interface OrderDao {
    /**
     * 
     * Description:查询所有订单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public List<OrderBean> selectAllOrderList();

    /**
     * 
     * Description:修改订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public int updateOrderItemsByOrderId(@Param("orderId") String orderId);

    /**
     * 
     * Description:每日获取已逾期且逾期天数小于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    public List<InstallmentBillPojo> queryBillLastDateThree();

    public void updateBillLastDate(InstallmentBillPojo installmentBillPojo);

    public void insertBillLastDate(InstallmentBillPojo installmentBillPojo);

    public OrderBean getOrderAllInfoByOrderId(String orderId);

    public InstallmentBillPojo queryBillInfoOne(InstallmentBillPojo installmentBillPojo);

    public List<ManageCarInfoBean> queryBillLastDateFive();

    public void insertManageCarInfoBean(ManageCarInfoBean manageCarInfoBean);

    public List<ManageCarInfoBean> queryInstManagecarInfo();

    public ManageCarInfoBean queryInstManagecarInfoOne(String orderId);

    public int updateOrderCarStatusByOrderId(String orderId);

    public InstallmentBillPojo getInstallmentBillPojoByOrderId(@Param("orderId") String orderId,
            @Param("repayNo") String repayNo);

    /**
     * 
     * Description:通过订单号修改是否存入inst_managecar_info状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月30日
     */
    public int updateInstManageCatStatusByOrderId(@Param("orderId") String orderId);

    /**
     * 
     * Description:根据订单号 当前期数 处理意见查询数据信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月8日
     */
    public ManageCarInfoBean getInstRemindRecordInfoOne(@Param("orderId") String orderId,
            @Param("curItems") int curItems);
}
