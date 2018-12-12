/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 下午2:22:00
 * @version V1.0
 */
package com.iqb.consumer.service.layer.bill;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BillInfoService {

    /**
     * 查询账单信息
     * 
     * @param m
     * @return
     */
    public Map<String, Object> getBillInfo(String orderId, String regId);

    /**
     * 通过订单号和期数查询对应账单的详情
     * 
     * @param orderId
     * @param repayNo
     * @param regId
     * @return
     */
    Map<String, Object> getDesignBillInfo(String orderId, String repayNo, String regId);

    /**
     * 通过制定期数查询指定的账单
     * 
     * @param orderId
     * @param repayNo支持多期一起查询1,2
     * @param regId
     * @return
     */
    Map<String, Object> getAssignBill(String orderId, String repayNo, String regId);

    /**
     * 查询订单下所有的账单
     * 
     * @param orderId
     * @param regId
     * @return
     */
    public List<Map<String, Object>> getAllBillInfo(String orderId, String regId);

    /**
     * 平账操作
     * 
     * @param billInfo
     * @return
     */
    Map<String, Object> repay(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 校验平账信息
     * 
     * @param billInfo
     * @param signParameters
     * @return
     */
    Map<String, Object> validateRepay(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 获取账单信息
     * 
     * @param billInfo
     * @param signParameters
     * @return
     */
    String getRepayParams(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 查询订单下所有的账单-中阁使用
     * 
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getAllBillInfo(String orderId);

    /**
     * 查询订单下所有的账单-中阁使用
     * 
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getAllJysBillInfo(String orderId);

    /**
     * 根据订单号 手机号码 还款期数 查询账单信息
     * 
     * @param m
     * @return
     */
    public Map<String, Object> getBillInfo(String orderId, String regId, String repayNos);

    /**
     * 
     * Description:提前结清-平账接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月16日
     */
    public Map<String, Object> allClearPay(String orderId, Map<String, String> preResultParam);

    /**
     * 
     * Description:提前结清
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月16日
     */
    public Map<String, Object> allRepay(Map<String, Object> billInfo, Map<String, String> signParameters);
}
