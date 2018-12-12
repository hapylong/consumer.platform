package com.iqb.consumer.manage.front.order;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.OrderAmtDetailBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.orderinfo.OrderInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.StringUtil;

/**
 * Description:订单信息导入Controller
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年12月8日下午2:31:47 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
@RequestMapping("/orderImport")
public class OrderInfoImportController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(OrderInfoImportController.class);

    @Resource
    private OrderInfoService orderInfoService;
    @Autowired
    private AdminService adminServiceImpl;

    /**
     * 
     * Description:订单信息导入接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/import"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map assetXlsImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            logger.debug("---订单导入---开始上传xls文件...");
            String merchantNo = request.getParameter("merchantNo");
            String bizType = request.getParameter("bizType");
            String projectPrefix = request.getParameter("projectPrefix");
            JSONObject objs = new JSONObject();
            objs.put("merchantNo", merchantNo);
            objs.put("bizType", bizType);
            objs.put("projectPrefix", projectPrefix);
            Map<String, Object> result = orderInfoService.orderInfoXlsImport(file, objs);
            logger.debug("---订单导入---上传xls文件结束...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("ZXBIT错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:订单信息导入分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---订单导入---查询订单分页信息...");
            PageInfo<OrderBean> list = orderInfoService.selectOrderInfoForImport(objs);
            logger.debug("---订单导入---查询订单分页信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("ZXBIT错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:批量删除订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/batchDel", method = {RequestMethod.POST, RequestMethod.GET})
    public Map batchDeleteInstOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---订单导入---批量删除订单信息...");
            long result = orderInfoService.updateLoanDateByOrderIds(objs);
            logger.debug("---订单导入---批量删除订单信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询房贷资产分配错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:批量更新订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-batchUpdateOrderLeftItems", method = {RequestMethod.POST, RequestMethod.GET})
    public Map batchUpdateOrderLeftItems(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---批量更新订单剩余期数信息...");
            long result = 0;
            List<OrderBean> orderList = orderInfoService.selectAllOrderList();
            if (!CollectionUtils.isEmpty(orderList)) {
                for (OrderBean bean : orderList) {
                    if (!StringUtil.isNull(bean.getOrderId())) {
                        result += orderInfoService.updateOrderItemsByOrderId(bean.getOrderId());
                    }
                }
            }
            logger.debug("---批量更新订单剩余期数信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询订单信息错误：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据条件查询以租代购费用明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/selectOrderAmtDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectOrderAmtDetailList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据条件查询以租代购费用明细信息...");
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<OrderAmtDetailBean> orderList = orderInfoService.selectOrderAmtDetailList(objs);
            logger.info("---根据条件查询以租代购费用明细完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", orderList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询订单信息错误：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:以租代购费用明细导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月1日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/exportOrderAmtDetailList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportOrderAmtDetailList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("开始导出以租代购费用明细报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            adminServiceImpl.getMerchantNos(objs);
            String result = orderInfoService.exportOrderAmtDetailList(objs, response);
            logger.info("导出以租代购费用明细报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
