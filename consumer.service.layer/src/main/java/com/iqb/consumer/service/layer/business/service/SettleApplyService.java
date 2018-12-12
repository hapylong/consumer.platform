package com.iqb.consumer.service.layer.business.service;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyOrderPojo;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date                     Author          Version     Description 
------------------------------------------------------------------
 * 2017年12月25日下午3:42:05     haojinlong      1.0         1.0 Version 
 * </pre>
 */
public interface SettleApplyService {
    /**
     * 
     * Description:根据订单号查询提前结清信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月25日
     */
    public SettleApplyBean getSettleBeanByOrderId(String orderId);

    /**
     * 
     * Description:根据订单号修改提前结清数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月25日
     */
    public int updateSettleBean(SettleApplyBean settleApplyBean);

    /**
     * 
     * 
     * @Description: 门店申请分页查询接口
     * @param objs
     * @return List<SettleApplyOrderPojo>
     * @author chengzhen
     * @data
     */
    public PageInfo<SettleApplyOrderPojo> selectSettleOrderList(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询提前结清信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月26日
     */
    public SettleApplyBean selectSettleBean(String orderId) throws GenerallyException;

    /**
     * 
     * Description:提前结清流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public int prepaymentStartWF(String orderId) throws IqbException;

    /**
     * 
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public PageInfo<SettleApplyBean> selectPrepaymentList(JSONObject objs);

    /**
     * 
     * Description:提前还款导出接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2017年12月29日 15:31:42
     */
    public void selectSettleOrderListExle(JSONObject objs, HttpServletResponse response);
}
