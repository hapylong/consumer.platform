package com.iqb.consumer.data.layer.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.admin.entity.InstOperateEntity;
import com.iqb.consumer.data.layer.bean.admin.request.ChatToChangePlanRequestPojo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerCarInfo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerOrderInfo;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderOtherAmtEntity;
import com.iqb.consumer.data.layer.bean.order.OrderAmtDetailBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.bean.order.mplan.InstPlanEntity;
import com.iqb.consumer.data.layer.bean.order.pojo.HouseBillQueryPojo;
import com.iqb.consumer.data.layer.bean.pay.ChatFinanceToRefundRequestMessage;
import com.iqb.consumer.data.layer.bean.user.SysUserWechat;

/**
 * 订单Dao
 */
public interface OrderDao {

    /**
     * 保存订单信息
     * 
     * @param orderBean
     * @return
     */
    int saveOrderInfo(OrderBean orderBean);

    /**
     * 修改预付款相关信息
     * 
     * @param params
     * @return
     */
    int updatePreInfo(Map<String, Object> params);

    /**
     * 保存详细的订单信息
     * 
     * @param orderBean
     * @return
     */
    int saveFullOrderInfo(OrderBean orderBean);

    /**
     * 通过ID保存流程ID
     * 
     * @param params
     * @return
     */
    int updateProcInstId(Map<String, Object> params);

    /**
     * 后台更改订单信息
     */
    public int updateOrderInfo(OrderBean orderBean);

    /**
     * 保存金额和期数
     * 
     * @param orderBean
     * @return
     */
    int saveAmtAndItems(JSONObject objs);

    /**
     * 后台更改订单信息（修改不带基本数据类型的值）
     */
    public int newUpdateOrderInfo(OrderBean orderBean);

    public int newUpdateOrderInfo1(OrderBean orderBean);

    /**
     * 通过订单ID或者NO修改订单风控状态
     * 
     * @param params
     * @return
     */
    int updatePreStatusByNO(Map<String, Object> params);

    /**
     * 通过订单号查询订单信息
     * 
     * @param orderId
     * @return
     */
    public OrderBean selByOrderId(String orderId);

    /**
     * 通过订单ID或者NO修改订单状态
     * 
     * @param params
     * @return
     */
    int updateStatusByIDNO(Map<String, Object> params);

    /**
     * 管理后台的查询(带条件查询)
     */
    public List<OrderBean> selectSelective(JSONObject objs);

    /**
     * 根据orderId获取订单信息
     */
    public OrderBean selectOne(Map<String, Object> params);

    /**
     * 管理后台 (还款管理预支付 )查询(带条件查询)
     */
    public List<OrderBean> selectPreOrderList(JSONObject objs);

    /**
     * 通过订单号修改订单状态
     * 
     * @param orderId
     * @param status
     * @return
     */
    public int updateStatus(@Param("orderId") String orderId, @Param("status") String status);

    /**
     * 订单号修改工作流状态
     * 
     * @param orderId
     * @param wfStatus
     * @return
     */
    public int updateWfStatus(@Param("orderId") String orderId, @Param("wfStatus") String wfStatus);

    public int updateWfStatus2(@Param("orderId") String orderId, @Param("wfStatus") String wfStatus,
            @Param("preAmtStatus") String preAmtStatus);

    public int getMaxCarSortNo(Map<String, Object> params);

    public OrderOtherInfo selectOtherOne(@Param("orderId") String orderId);

    public int insertOrderOtherInfo(OrderOtherInfo orderOtherInfo);

    /**
     * 管理后台带权证资料的查询(带条件查询)
     */
    public List<OrderBean> getAuthorityOrderList(JSONObject objs);

    /**
     * 批量插入交易所订单表
     */
    public int batchInsertJysOrder(List<JYSOrderBean> list);

    /**
     * 
     * Description: 根据订单id获取机构代码
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 上午10:43:19
     */
    public String getOrgCodeByOrderId(String orderId);

    /**
     * 
     * Description: 质押车更新订单状态和风控状态
     * 
     * @param backFlag
     * @param preAmt
     * 
     * @param
     * @return int
     * @throws @Author wangxinbang Create Date: 2017年4月11日 下午3:58:52
     */
    public int updatePledgeWfStatus(@Param("orderId") String orderId, @Param("riskStatus") String riskStatus,
            @Param("wfStatus") String wfStatus, @Param("preAmtStatus") String preAmtStatus,
            @Param("specialTime") String specialTime, @Param("backFlag") String backFlag,
            @Param("preAmt") String preAmt, @Param("showContract") String showContract);

    /**
     * 
     * Description: select * from IOIE
     * 
     * @param
     * @return InstOrderInfoEntity
     * @throws
     * @Author adam Create Date: 2017年6月1日 下午5:07:42
     */
    InstOrderInfoEntity getIOIEByOid(String orderId);

    /**
     * 
     * Description: pojo that chat to finance to change order plan
     * 
     * @param
     * @return ChatToChangePlanRequestPojo
     * @throws
     * @Author adam Create Date: 2017年6月1日 下午5:31:07
     */
    ChatToChangePlanRequestPojo getCTCPByOid(String orderId);

    void persistIOE(InstOperateEntity ioe);

    ChatFinanceToRefundRequestMessage getSCBCRequestMessageByOid(String orderId);

    void updateSpecialTimeByOid(String orderId);

    /**
     * 
     * Description:蒲公英行-更新返回标识
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    public int updateInstOrderBackFlag(@Param("orderId") String orderId, @Param("backFlag") String backFlag);

    /**
     * 蒲公英行流程回调修改风控信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月25日
     */
    public int updateOrderInfoForDandelion(OrderBean orderBean);

    List<HouseBillQueryPojo> queryHouseBill(JSONObject requestMessage);

    HouseOrderEntity queryHOEByOid(String orderId);

    InstPlanEntity queryMPEByPid(Long planId);

    void updateHouseOrderHandler(HouseOrderEntity hoe);

    /****
     * Description: 通过渠道经纪人获取 他下的 单
     * 
     * @param
     * @return List<String>
     * @throws @Author adam Create Date: 2017年8月16日 下午5:13:25
     */
    List<String> getOidsBySongsong(String songsong);

    HouseOrderEntity querySongSongByOid(@Param("orderId") String orderId, @Param("subOrderId") String subOrderId,
            @Param("repayNo") Integer repayNo);

    /** 通过手机号查询渠道经纪人信息 **/
    SysUserWechat getChannelInfo(@Param("orderId") String orderId);

    void updateIOIEResetAmt(InstOrderInfoEntity ioie);

    void updateIOIE(InstOrderInfoEntity ioie);

    /**
     * 获取放款订单列表信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public List<InstOrderBean> getLoanOrderList(JSONObject objs);

    /**
     * 
     * Description:根据订单号修改放款日期
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public long updateLoanDateByOrderIds(JSONObject objs);

    void saveOtherAmtEntity(InstOrderOtherAmtEntity iooae);

    int updateOtherAmtEntity(InstOrderOtherAmtEntity iooae);

    List<InstOrderOtherAmtEntity> getOtherAmtList(InstOrderOtherAmtEntity iooae);

    BigDecimal getFinishBillAmt(String orderId);

    void updatePreamtByOid(@Param("orderId") String orderId, @Param("amt") BigDecimal amt);

    InstOrderOtherAmtEntity getOtherAmtEntity(String orderId);

    void createIOIE(InstOrderInfoEntity ioie);

    List<CarOwnerOrderInfo> getCarOwnerOrderInfo(JSONObject requestMessage);

    List<CarOwnerCarInfo> getCarOwnerCarInfo(JSONObject requestMessage);

    /**
     * 
     * Description:车主贷订单分期
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    List<CarOwnerOrderInfo> getOrderBreakInfo(JSONObject requestMessage);

    /**
     * 
     * Description:订单信息导入分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月7日
     */
    public List<OrderBean> selectOrderInfoForImport(JSONObject objs);

    /**
     * 批量插入订单信息
     */
    public int batchInsertOrderInfo(@Param("list") List<OrderBean> list);

    /**
     * 
     * @Description: 订单查询总量(个数,总金额)
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data 2018年1月4日 16:57:18
     */
    Map getOrderInfoByListTotal(JSONObject objs);

    List<OrderBean> getStageOrderList(JSONObject objs);

    OrderBean getOrderInfo(JSONObject objs);

    int updateOrderInfo1(OrderBean objs);

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
     * Description:根据订单号修改剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int updateOrderInfoForLeftMonthByOrderId(OrderBean orderBean);

    /**
     * 保存订单信息
     * 
     * @param orderBean
     * @return
     */
    int saveOrderInfoForHY(OrderBean orderBean);

    Map getLoanOrderListTotal(JSONObject objs);

    List<InstOrderBean> getSelectLoanOrderList(JSONObject objs);

    String getSysItem2Excel(@Param("itemCode") String itemCode, @Param("value") String value);

    InstOrderBean getOrderInfoNew(JSONObject objs);

    void saveApprovalOpinion(JSONObject objs);

    Map<String, String> getApprovalOpinion(JSONObject objs);

    /**
     * 
     * Description:根据条件查询以租代购费用明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    List<OrderAmtDetailBean> selectOrderAmtDetailList(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询车秒贷金额相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    OrderAmtDetailBean getOrderAmtDetail(String orderId);

    /**
     * 
     * Description:根据手机号获取订单列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月24日
     */
    List<OrderBean> getOrderListByRegId(String regId);
}
