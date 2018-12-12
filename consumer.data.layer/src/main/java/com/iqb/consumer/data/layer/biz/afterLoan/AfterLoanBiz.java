package com.iqb.consumer.data.layer.biz.afterLoan;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanBean;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanCustomerInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.BreachOfContractBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstGpsInfo;
import com.iqb.consumer.data.layer.bean.afterLoan.OutSourceOrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.dao.afterLoan.AfterLoanDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 贷后Biz
 * 
 * @author town
 * 
 */
@Repository
public class AfterLoanBiz extends BaseBiz {
    @Autowired
    private AfterLoanDao afterLoanDao;

    /**
     * @Description: 贷后客户跟踪列表
     * @param @param objs
     * @param @return
     * @return Object
     * @author chengzhen
     * @data 2018年3月8日 15:30:39
     */
    public List<AfterLoanBean> afterLoanList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        List<AfterLoanBean> resList = afterLoanDao.afterLoanList(objs);
        return resList;

    }

    /**
     * @Description:保存 贷后客户信息补充
     * @param @param objs
     * @return void
     * @author chengzhen
     * @data 2018年3月9日 11:20:00
     */
    public void saveAfterLoanCustomer(JSONObject objs) {
        setDb(0, super.MASTER);
        afterLoanDao.saveAfterLoanCustomer(objs);
    }

    /**
     * 
     * Description: FINANCE-3043 新增贷后客户跟踪及查询页面；FINANCE-3071客户信息补充分页展示接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月9日 11:58:05
     */
    public List<AfterLoanCustomerInfoBean> selectCustomerInfoListByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return afterLoanDao.selectCustomerInfoListByOrderId(requestMessage);
    }

    /**
     * 
     * Description:FINANCE-3048 新增资产赎回页面；FINANCE-30824.5分配详情页 字典列表
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen
     */
    public List<Map<String, String>> selectPayMoneyListByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.selectPayMoneyListByOrderId(requestMessage);
    }

    /**
     * 
     * Description:FINANCE-3048 根据订单号查询剩余期数
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen
     */
    public Map<Integer, Integer> selectNumberByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.selectNumberByOrderId(requestMessage);
    }

    /**
     * 
     * Description:FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3135客户违约状态记录分页查询
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月16日 15:58:55
     */
    public List<BreachOfContractBean> selectBreachContractList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return afterLoanDao.selectBreachContractList(requestMessage);
    }

    /**
     * 
     * Description: FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3136批量违约标记接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月19日 11:05:39
     */
    public void batchListToMark(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        afterLoanDao.batchListToMark(requestMessage);
    }

    @SuppressWarnings("rawtypes")
    public Map selectOverdueByOrderId(JSONObject jsonObject) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.selectOverdueByOrderId(jsonObject);
    }

    public List<LocalRiskInfoBean> getReportList(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.getReportList(requestMessage);
    }

    public LocalRiskInfoBean getReportByReprotNo(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.getReportByReprotNo(requestMessage);
    }

    public List<LocalRiskInfoBean> getReportStateByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.getReportStateByOrderId(requestMessage);
    }

    public LocalRiskInfoBean getPersonDetail(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.getPersonDetail(requestMessage);
    }

    public void savePersonDetail(JSONObject obj) {
        super.setDb(0, super.MASTER);
        afterLoanDao.savePersonDetail(obj);
    }

    /**
     * 
     * Description:根据订单号查询贷后处置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public AfterLoanBean getAfterLoanInfoByOrderId(String orderId) {
        super.setDb(1, super.MASTER);
        return afterLoanDao.getAfterLoanInfoByOrderId(orderId);
    }

    /**
     * 
     * Description:根据订单号修改贷后车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public int saveGpsInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.saveGpsInfo(objs);
    }

    /**
     * 
     * Description:根据订单号查询gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public List<InstGpsInfo> getGpsInfoByOrderId(String orderId) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.getGpsInfoByOrderId(orderId);
    }

    /**
     * 
     * 根据手机号码获取用户最新的风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    public List<LocalRiskInfoBean> getReportListByRegId(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return afterLoanDao.getReportListByRegId(objs);
    }

    /**
     * 
     * 根据手机号码,风控类型获取用户最新的风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    public LocalRiskInfoBean getReportByRegId(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return afterLoanDao.getReportByRegId(objs);
    }

    /**
     * 
     * 外包受理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public List<OutSourceOrderBean> selectOutSourcetList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return afterLoanDao.selectOutSourcetList(objs);
    }

    /**
     * 
     * 外包处理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public List<OutSourceOrderBean> selectOutSourceProcesstList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return afterLoanDao.selectOutSourceProcesstList(objs);
    }

    /**
     * 
     * 保存外包处理信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    public int saveOutSourceOrder(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return afterLoanDao.saveOutSourceOrder(objs);
    }

    /**
     * 
     * 委案受理查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    public List<OutSourceOrderBean> selectCasetList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return afterLoanDao.selectCasetList(objs);
    }

    /**
     * 
     * 根据案件号查询立案信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public OutSourceOrderBean getCaseInfoByCaseId(String caseId) {
        super.setDb(1, super.MASTER);
        return afterLoanDao.getCaseInfoByCaseId(caseId);
    }

    /**
     * 
     * 庭审登记信息查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<OutSourceOrderBean> selectTrialRegistList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return afterLoanDao.selectTrialRegistList(objs);
    }

    /**
     * 
     * 贷后案件查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<Map<String, Object>> selectAfterCaseOrderInfoList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return afterLoanDao.selectAfterCaseOrderInfoList(requestMessage);
    }

    /**
     * 
     * 贷后案件查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<Map<String, Object>> exportAfterCaseOrderInfoList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.selectAfterCaseOrderInfoList(requestMessage);
    }

    /**
     * 
     * 贷后案件查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    public Map<String, Object> totalAfterCaseOrderInfo(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.totalAfterCaseOrderInfo(requestMessage);
    }

    /**
     * 
     * 法务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public List<Map<String, Object>> selectAfterCaseOrderLawList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return afterLoanDao.selectAfterCaseOrderLawList(requestMessage);
    }

    /**
     * 
     * 法务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    public List<Map<String, Object>> exportAfterCaseOrderLawList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.selectAfterCaseOrderLawList(requestMessage);
    }

    /**
     * 
     * 法务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    public Map<String, Object> totalAfterCaseOrderLaw(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.totalAfterCaseOrderLaw(requestMessage);
    }

    /**
     * 
     * Description:查询委案受理新增案件相关的贷后信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月25日
     */
    public AfterLoanBean getAfterLoanInfoByOrderIdNew(String caseId) {
        super.setDb(0, super.SLAVE);
        return afterLoanDao.getAfterLoanInfoByOrderIdNew(caseId);
    }
}
