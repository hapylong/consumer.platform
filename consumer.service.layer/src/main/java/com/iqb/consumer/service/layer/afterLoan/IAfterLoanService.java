package com.iqb.consumer.service.layer.afterLoan;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanBean;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanCustomerInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.BreachOfContractBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstGpsInfo;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderCaseExecuteBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawResultBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentBean;
import com.iqb.consumer.data.layer.bean.afterLoan.OutSourceOrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.etep.common.exception.IqbException;

public interface IAfterLoanService {
    /**
     * 
     * @Description: 贷后客户跟踪列表
     * @param @param objs
     * @param @return
     * @return PageInfo<AfterLoanBean>
     * @author chengzhen
     * @data 2018年3月8日 15:26:02
     */
    PageInfo<AfterLoanBean> afterLoanList(JSONObject objs);

    /**
     * @Description: 通过订单号查询已逾期和当期待还的账单
     * @param @param string
     * @param @return
     * @return List<Map<String,Object>>
     * @author chengzhen
     * @data 2018年3月8日 17:11:31
     */
    List<Map<String, Object>> getAllBillInfo(String string);

    /**
     * 
     * 
     * @Description:保存 贷后客户信息补充
     * @param @param objs
     * @return void
     * @author chengzhen
     * @data 2018年3月9日 11:18:15
     */
    void saveAfterLoanCustomer(JSONObject objs);

    /**
     * 
     * Description: FINANCE-3043 新增贷后客户跟踪及查询页面；FINANCE-3071客户信息补充分页展示接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月9日 11:58:05
     */

    PageInfo<AfterLoanCustomerInfoBean> selectCustomerInfoListByOrderId(JSONObject requestMessage);

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
     * 
     * @Description: 根据订单号查询剩余期数
     * @param @param requestMessage
     * @param @return
     * @return List<Map<String,String>>
     * @author chengzhen
     * @data
     */
    Map<Integer, Integer> selectNumberByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3135客户违约状态记录分页查询
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月16日 15:58:36
     */
    PageInfo<BreachOfContractBean> selectBreachContractList(JSONObject requestMessage);

    /**
     * 
     * Description: FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3136批量违约标记接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月19日 10:47:17
     */
    int batchListToMark(JSONObject requestMessage);

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
    public int updateAfterLoanInfo(JSONObject objs) throws GenerallyException, IqbException;

    /**
     * 
     * Description:保存gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public int saveInstGpsInfo(JSONObject objs);

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
    public List<LocalRiskInfoBean> getReportListByRegId(JSONObject objs);

    /**
     * 
     * 根据手机号码,风控类型获取用户最新的风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    public LocalRiskInfoBean getReportByRegId(JSONObject objs);

    /**
     * 
     * 外包受理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public PageInfo<OutSourceOrderBean> selectOutSourcetList(JSONObject objs);

    /**
     * 
     * 外包处理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public PageInfo<OutSourceOrderBean> selectOutSourceProcesstList(JSONObject objs);

    /**
     * 
     * 保存外包处理信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public int saveOutSourceOrder(JSONObject objs) throws IqbException;

    /**
     * 
     * 更新贷后车辆状态信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月10日
     */
    public int updateManagerCarInfo(JSONObject objs);

    /**
     * 
     * 保存贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    public int saveInstManageCarReceive(JSONObject objs);

    /**
     * 
     * 更新贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    public int updateInstManageCarReceiveByOrderId(JSONObject objs) throws IqbException;

    /**
     * 
     * 委案受理查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    public PageInfo<OutSourceOrderBean> selectCasetList(JSONObject objs);

    /**
     * 
     * 保存案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public int saveInstOrderLawnInfo(JSONObject objs);

    /**
     * 
     * 委案受理-新增
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public Map<String, Object> newCase(JSONObject objs) throws IqbException;

    /**
     * 
     * 根据订单号查询立案信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public OutSourceOrderBean getCaseInfoByCaseId(String orderId);

    /**
     * 
     * 更新案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public int updateInstOrderLawnInfo(JSONObject objs);

    /**
     * 
     * 批量保存法务回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public int batchSaveInstReceivedPayment(JSONObject objs);

    /**
     * 
     * 根据订单号查询法务回款列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public List<InstReceivedPaymentBean> selectInstReceivedPaymentList(String orderId, String caseId);

    /**
     * 
     * 庭审登记信息查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public PageInfo<OutSourceOrderBean> selectTrialRegistList(JSONObject objs);

    /**
     * 
     * 根据caseId从查询案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    public InstOrderLawBean getInstOrderLawnInfoByCaseId(JSONObject objs);

    /**
     * 
     * 保存庭审登记信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    public int saveInstOrderLawResult(JSONObject objs);

    /**
     * 
     * 显示庭审登记列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    public PageInfo<InstOrderLawResultBean> selectInstOrderLawResultByCaseId(JSONObject objs);

    /**
     * 
     * 贷后案件查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public PageInfo<Map<String, Object>> selectAfterCaseOrderInfoList(JSONObject requestMessage);

    /**
     * 
     * 贷后案件查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public String exportAfterCaseOrderInfoList(JSONObject requestMessage, HttpServletResponse response);

    /**
     * 
     * 贷后案件查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    public Map<String, Object> totalAfterCaseOrderInfo(JSONObject requestMessage);

    /**
     * 
     * 根据caseId查询案件进度信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    public PageInfo<InstOrderCaseExecuteBean> selectInstOrderCaseExecuteByCaseId(JSONObject objs);

    /**
     * 
     * 保存案件进度信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    public int saveInstOrderCaseExecute(JSONObject objs);

    /**
     * 
     * 法务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public PageInfo<Map<String, Object>> selectAfterCaseOrderLawList(JSONObject requestMessage);

    /**
     * 
     * 法务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public String exportAfterCaseOrderLawList(JSONObject requestMessage, HttpServletResponse response);

    /**
     * 
     * 法务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    public Map<String, Object> totalAfterCaseOrderLaw(JSONObject requestMessage);

    /**
     * 
     * 校验编号是否唯一
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    public int validateCaseNo(JSONObject objs);

    /**
     * 
     * 立案申请方法
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月31日
     */
    public int applyCase(JSONObject objs) throws IqbException;

}
