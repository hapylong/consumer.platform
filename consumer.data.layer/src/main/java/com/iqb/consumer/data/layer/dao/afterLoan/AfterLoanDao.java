package com.iqb.consumer.data.layer.dao.afterLoan;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanBean;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanCustomerInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.BreachOfContractBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstGpsInfo;
import com.iqb.consumer.data.layer.bean.afterLoan.OutSourceOrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;

/**
 * 贷后DAO
 * 
 * @author chengzhen
 */
public interface AfterLoanDao {
    /**
     * @Description: 贷后客户跟踪列表
     * @param @param objs
     * @param @return
     * @return Object
     * @author chengzhen
     * @data 2018年3月8日 15:40:45
     */
    List<AfterLoanBean> afterLoanList(JSONObject objs);

    /**
     * @Description:保存 贷后客户信息补充
     * @param @param objs
     * @return void
     * @author chengzhen
     * @data 2018年3月9日 11:20:00
     */
    void saveAfterLoanCustomer(JSONObject objs);

    List<AfterLoanCustomerInfoBean> selectCustomerInfoListByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:FINANCE-3048 新增资产赎回页面；FINANCE-30824.5分配详情页 字典列表
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen
     */
    List<Map<String, String>> selectPayMoneyListByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:FINANCE-3048 根据订单号查询剩余期数
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen
     */
    Map<Integer, Integer> selectNumberByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3135客户违约状态记录分页查询
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月16日 15:58:55
     */
    List<BreachOfContractBean> selectBreachContractList(JSONObject requestMessage);

    /**
     * 
     * Description: FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3136批量违约标记接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月19日 11:05:39
     */
    void batchListToMark(JSONObject requestMessage);

    @SuppressWarnings("rawtypes")
    Map selectOverdueByOrderId(JSONObject jsonObject);

    List<LocalRiskInfoBean> getReportList(JSONObject requestMessage);

    LocalRiskInfoBean getReportByReprotNo(JSONObject requestMessage);

    List<LocalRiskInfoBean> getReportStateByOrderId(JSONObject requestMessage);

    LocalRiskInfoBean getPersonDetail(JSONObject requestMessage);

    void savePersonDetail(JSONObject obj);

    /**
     * 
     * Description:根据订单号查询贷后处置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public AfterLoanBean getAfterLoanInfoByOrderId(String orderId);

    /**
     * 
     * Description:根据订单号修改贷后车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public int saveGpsInfo(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public List<InstGpsInfo> getGpsInfoByOrderId(String orderId);

    /**
     * 
     * 根据手机号码获取用户最新的风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    List<LocalRiskInfoBean> getReportListByRegId(JSONObject objs);

    /**
     * 
     * 根据手机号码,风控类型获取用户最新的风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    LocalRiskInfoBean getReportByRegId(JSONObject objs);

    /**
     * 
     * 外包受理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public List<OutSourceOrderBean> selectOutSourcetList(JSONObject objs);

    /**
     * 
     * 外包处理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public List<OutSourceOrderBean> selectOutSourceProcesstList(JSONObject objs);

    /**
     * 
     * 保存外包处理信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public int saveOutSourceOrder(JSONObject objs);

    /**
     * 
     * 委案受理查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    public List<OutSourceOrderBean> selectCasetList(JSONObject objs);

    /**
     * 
     * 根据案件号查询立案信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public OutSourceOrderBean getCaseInfoByCaseId(String caseId);

    /**
     * 
     * 庭审登记信息查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<OutSourceOrderBean> selectTrialRegistList(JSONObject objs);

    /**
     * 
     * 贷后案件查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<Map<String, Object>> selectAfterCaseOrderInfoList(JSONObject objs);

    /**
     * 
     * 贷后案件查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public Map<String, Object> totalAfterCaseOrderInfo(JSONObject objs);

    /**
     * 
     * 法务查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public List<Map<String, Object>> selectAfterCaseOrderLawList(JSONObject objs);

    /**
     * 
     * 法务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public Map<String, Object> totalAfterCaseOrderLaw(JSONObject objs);

    /**
     * 
     * Description:查询委案受理新增案件相关的贷后信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月25日
     */
    public AfterLoanBean getAfterLoanInfoByOrderIdNew(String caseId);
}
