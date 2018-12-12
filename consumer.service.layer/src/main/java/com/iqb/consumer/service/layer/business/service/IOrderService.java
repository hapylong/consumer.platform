package com.iqb.consumer.service.layer.business.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerCarInfo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerOrderInfo;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

public interface IOrderService {

    PageInfo<OrderBean> getOrderInfoByList(JSONObject objs);

    int updateOrderInfo(JSONObject objs);

	void updateIOIE(InstOrderInfoEntity ioie);

    int newUpdateOrderInfo(JSONObject objs);

    OrderBean selectOne(JSONObject objs);

    PageInfo<OrderBean> getPreOrderInfoByList(JSONObject objs);

    List<PlanBean> getPlanByMerNo(JSONObject objs);

    String exportOrderList(JSONObject objs, HttpServletResponse response);

    OrderOtherInfo selectOtherOne(JSONObject objs);

    PageInfo<OrderBean> getAuthorityOrderList(JSONObject objs);

    /**
     * 交易所需求:查询单个订单信息
     * 
     * @param objs
     * @return
     */
    JYSOrderBean getSingleOrderInfo(JSONObject objs);

    /**
     * 交易所需求:通过商户号查询计划
     * 
     * @param objs
     * @return
     */
    List<PlanBean> getPlanByMerAndBType(JSONObject objs);

    /**
     * 交易所需求:查询订单列表分页
     * 
     * @param objs
     * @return
     */
    PageInfo<JYSOrderBean> listJYSOrderInfo(JSONObject objs);

    // 修改交易所订单
    int updateJYSOrderInfo(JSONObject objs);

    // 删除交易所订单
    int deleteJYSOrderInfo(JSONObject objs);

    /**
     * 
     * Description: 根据订单id获取机构代码
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 上午10:41:19
     */
    public String getOrgCodeByOrderId(String orderId);

    /**
     * 查询拆分信息
     * 
     * @param orderId
     * @return
     */
    OrderBreakInfo selOrderInfo(String orderId);

    /**
     * 保存订单拆分金额
     * 
     * @param orderBreakInfo
     * @return
     */
    int saveOrderBreakInfo(OrderBreakInfo orderBreakInfo);

    /**
     * 保存订单金额和期数
     * 
     * @param objs
     * @return
     */
    int updateAmtAndItems(JSONObject objs);

    /**
     * 
     * Description:是否是历史订单更新
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    int updateOrderProInfo(JSONObject objs);

    Object houseHandlerBillQuery(Integer type, JSONObject requestMessage) throws GenerallyException;

    void houseHandlerCreateBill(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException;

    InstOrderInfoEntity getIOIEByOid(String orderId);

    /**
     * 获取放款订单列表信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    PageInfo<InstOrderBean> getLoanOrderList(JSONObject objs);

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

    /**
     * 
     * Description:放款数据导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public String exportLoanOrderList(JSONObject objs, HttpServletResponse response);

    void saveOrUpdateOtherAmt(JSONObject requestMessage, Boolean isSave, Boolean isUpdateIoie)
            throws GenerallyException, DevDefineErrorMsgException;

    Object getOtherAmtList(JSONObject requestMessage, Boolean isPage)
            throws GenerallyException, DevDefineErrorMsgException;

    void finishBill(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException;

    void saveOrUpdateOrderInfo(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException;

    PageInfo<CarOwnerOrderInfo> getCarOwnerOrderInfo(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException;

    void exportCarOwnerOrderInfo(JSONObject requestMessage, HttpServletResponse response);

    Map<String, Object> orderBreak(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException;

    PageInfo<CarOwnerCarInfo> getCarOwnerCarInfoPage(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException;

    void exportCarOwnerCarInfo(JSONObject requestMessage, HttpServletResponse response);

    /**
     * 
     * Description:车主贷订单分期
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    PageInfo<CarOwnerOrderInfo> getOrderBreakInfo(JSONObject requestMessage)
            throws GenerallyException, DevDefineErrorMsgException;

    /**
     * FINANCE-2734 门店签约节点回显可修改开户行名称
     * 
     * @Description: TODO
     * @param @param objs
     * @return void
     * @author chengzhen
     * @data
     */
    void saveBankInfo(JSONObject objs);

    /**
     * 
     * 
     * @Description: 订单查询添加总条数和总金额
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    Map getOrderInfoByListTotal(JSONObject objs);

    PageInfo<OrderBean> getStageOrderList(JSONObject objs);

    OrderBean getOrderInfo(JSONObject objs);

    int updateOrderInfo1(OrderBean objs);

    Map getLoanOrderListTotal(JSONObject objs);

    PageInfo<InstOrderBean> getSelectLoanOrderList(JSONObject objs);

    String exportSelectLoanOrderList(JSONObject objs, HttpServletResponse response);

    InstOrderBean getOrderInfoNew(JSONObject objs);

    void saveApprovalOpinion(JSONObject objs);

    Map<String, String> getApprovalOpinion(JSONObject objs);
}
