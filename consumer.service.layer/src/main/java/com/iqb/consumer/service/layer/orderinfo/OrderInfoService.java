package com.iqb.consumer.service.layer.orderinfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.OrderAmtDetailBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年12月7日下午4:51:42 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OrderInfoService {
    /**
     * 保存订单信息
     * 
     * @param orderBean
     * @return
     */
    int saveOrderInfo(OrderBean orderBean);

    /**
     * 
     * Description:订单信息导入分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月7日
     */
    public PageInfo<OrderBean> selectOrderInfoForImport(JSONObject objs);

    /**
     * 
     * Description:订单信息导入
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月7日
     */
    public Map<String, Object> orderInfoXlsImport(MultipartFile file, JSONObject objs);

    /**
     * 
     * Description:批量删除订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public long updateLoanDateByOrderIds(JSONObject objs);

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
     * Description:根据条件查询以租代购费用明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    public PageInfo<OrderAmtDetailBean> selectOrderAmtDetailList(JSONObject objs);

    /**
     * 
     * Description:以租代购费用明细导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月1日
     */
    public String exportOrderAmtDetailList(JSONObject objs, HttpServletResponse response);

}
