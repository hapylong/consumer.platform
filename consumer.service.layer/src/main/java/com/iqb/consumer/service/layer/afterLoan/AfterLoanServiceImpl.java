package com.iqb.consumer.service.layer.afterLoan;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.constant.CommonConstant.CaseStatus;
import com.iqb.consumer.common.constant.CommonConstant.ProcessStatus;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanBean;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanCustomerInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.BreachOfContractBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstGpsInfo;
import com.iqb.consumer.data.layer.bean.afterLoan.InstManageCarReceiveBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderCaseExecuteBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawResultBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentPojo;
import com.iqb.consumer.data.layer.bean.afterLoan.OutSourceOrderBean;
import com.iqb.consumer.data.layer.bean.carstatus.entity.InstManageCarInfoEntity;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBeanPojo;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.AfterLoanBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstManageCarReceiveBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstOrderCaseExecuteBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstOrderLawBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstOrderLawResultBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstReceivedPaymentBiz;
import com.iqb.consumer.data.layer.biz.carstatus.CarStatusManager;
import com.iqb.consumer.data.layer.biz.carstatus.InstRemindPhoneBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;

@Service
public class AfterLoanServiceImpl implements IAfterLoanService {

    protected static final Logger logger = LoggerFactory.getLogger(AfterLoanServiceImpl.class);
    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    public static final String WFInterRetSucc = "1";
    @Autowired
    private AfterLoanBiz afterLoanBiz;
    @Autowired
    private CarStatusManager carStatusManager;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private UserBeanBiz userBeanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private InstRemindPhoneBiz instRemindPhoneBiz;
    @Resource
    private InstManageCarReceiveBiz instManageCarReceiveBiz;
    @Autowired
    private InstOrderLawBiz instOrderLawBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private InstReceivedPaymentBiz instReceivedPaymentBiz;
    @Resource
    private InstOrderLawResultBiz instOrderLawResultBiz;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private InstOrderCaseExecuteBiz instOrderCaseExecuteBiz;
    @Resource
    private SysUserSession sysUserSession;

    /**
     * 
     * 贷后客户跟踪列表
     * 
     * @param @param objs
     * @param @return
     * @return PageInfo<AfterLoanBean>
     * @author chengzhen
     * @data 2018年3月8日 15:26:02
     */
    @Override
    public PageInfo<AfterLoanBean> afterLoanList(JSONObject objs) {
        return new PageInfo<>(afterLoanBiz.afterLoanList(objs));
    }

    /**
     * @Description: 通过订单号查询已逾期和当期待还的账单
     * @param @param string
     * @param @return
     * @return List<Map<String,Object>>
     * @author chengzhen
     * @data 2018年3月8日 17:11:31
     */
    public List<Map<String, Object>> getAllBillInfo(String string) {
        return null;
    }

    /**
     * @Description:保存 贷后客户信息补充
     * @param @param objs
     * @return void
     * @author chengzhen
     * @data 2018年3月9日 11:18:15
     */
    @SuppressWarnings("rawtypes")
    public void saveAfterLoanCustomer(JSONObject objs) {
        OrderBean orderBean = orderBiz.selByOrderId(objs.getString("orderId"));
        if (null != orderBean) {
            UserBean userBean = userBeanBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
            if (null != userBean && !StringUtil.isNull(objs.getString("smsMobile"))) {
                userBean.setSmsMobile(objs.getString("smsMobile"));
                userBeanBiz.updateUserInfo(userBean);
            }
            // 调用账务系统接口同步更新账务系统接收短信手机号码
            List<OrderBean> orderList = orderBiz.getOrderListByRegId(orderBean.getRegId());
            if (!CollectionUtils.isEmpty(orderList) && !StringUtil.isNull(objs.getString("smsMobile"))) {
                // 将最新设置的接收短信手机号码更新到风控信息表中
                RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRIAndRT(orderBean.getRegId(), 3);
                String checkInfo = riskInfoBean.getCheckInfo();

                RiskInfoBeanPojo RiskInfoBeanPojo = JSONObject.parseObject(checkInfo, RiskInfoBeanPojo.class);
                RiskInfoBeanPojo.setSmsMobile(objs.getString("smsMobile"));
                riskInfoBean.setCheckInfo(JSONObject.toJSONString(RiskInfoBeanPojo));
                riskInfoBean.setRegId(userBean.getRegId());
                riskInfoBean.setRiskType(3);
                riskInfoBiz.updateCheckInfo(riskInfoBean);

                JSONObject sourceMap = new JSONObject();
                sourceMap.put("orderList", orderList);
                sourceMap.put("smsMobile", objs.getString("smsMobile"));
                batchUpdateBillSmsMobile(sourceMap);
            }
        }
        afterLoanBiz.saveAfterLoanCustomer(objs);
    }

    /**
     * Description:根据订单号批量跟新接收短信手机号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月24日
     */
    private Map<String, Object> batchUpdateBillSmsMobile(JSONObject sourceMap) {
        logger.info("根据订单行批量跟新接收短信手机号;参数:{}", sourceMap);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getBatchUpdateMobileUrl(),
                    encryptUtils.encrypt(sourceMap));
            result = JSONObject.parseObject(resultStr);
            logger.info("根据订单行批量跟新接收短信手机号返回结果，返回结果:{}", result);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
        }
        return result;
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
    public PageInfo<AfterLoanCustomerInfoBean> selectCustomerInfoListByOrderId(JSONObject requestMessage) {
        return new PageInfo<>(afterLoanBiz.selectCustomerInfoListByOrderId(requestMessage));
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
        return afterLoanBiz.selectPayMoneyListByOrderId(requestMessage);
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
        return afterLoanBiz.selectNumberByOrderId(requestMessage);
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
    public PageInfo<BreachOfContractBean> selectBreachContractList(JSONObject requestMessage) {
        return new PageInfo<>(afterLoanBiz.selectBreachContractList(requestMessage));
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
    @SuppressWarnings("rawtypes")
    @Override
    public int batchListToMark(JSONObject requestMessage) {
        JSONArray jsonArray = requestMessage.getJSONArray("programmers");
        for (int index = 0; index < jsonArray.size(); index++) {
            Map map = afterLoanBiz.selectOverdueByOrderId(jsonArray.getJSONObject(index));
            if (null != map && map.size() != 0) {
                return 0;
            }
            afterLoanBiz.batchListToMark(jsonArray.getJSONObject(index));
        }
        return 1;
    }

    @Override
    public List<LocalRiskInfoBean> getReportList(JSONObject requestMessage) {
        return afterLoanBiz.getReportList(requestMessage);
    }

    @Override
    public LocalRiskInfoBean getReportByReprotNo(JSONObject requestMessage) {
        return afterLoanBiz.getReportByReprotNo(requestMessage);
    }

    @Override
    public List<LocalRiskInfoBean> getReportStateByOrderId(JSONObject requestMessage) {
        return afterLoanBiz.getReportStateByOrderId(requestMessage);
    }

    @Override
    public LocalRiskInfoBean getPersonDetail(JSONObject requestMessage) {
        return afterLoanBiz.getPersonDetail(requestMessage);
    }

    @Override
    public void savePersonDetail(JSONObject obj) {
        afterLoanBiz.savePersonDetail(obj);

    }

    /**
     * 根据订单号查询贷后处置信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月4日
     */
    @Override
    public AfterLoanBean getAfterLoanInfoByOrderId(String orderId) {
        AfterLoanBean afterLoanBean = afterLoanBiz.getAfterLoanInfoByOrderId(orderId);
        if (afterLoanBean == null) {
            afterLoanBean = afterLoanBiz.getAfterLoanInfoByOrderIdNew(orderId);
        }
        JSONObject objs = new JSONObject();
        objs.put("orderId", orderId);
        Map<String, Object> map = getMaxRepayNoByOrderId(objs);
        if (!CollectionUtils.isEmpty(map) && map.get("retCode").toString().equals("success")) {
            if (map.get("num") != null) {
                int hasRepayNo = (Integer) map.get("num");
                afterLoanBean.setHasRepayNo(hasRepayNo);
            }
            if (map.get("remainPrincipal") != null) {
                BigDecimal remainPrincipal = BigDecimal.ZERO;
                remainPrincipal = (BigDecimal) map.get("remainPrincipal");
                if (remainPrincipal != null) {
                    remainPrincipal = BigDecimalUtil.div100(remainPrincipal);
                }
                afterLoanBean.setRemainPrincipal(remainPrincipal);
            }
        }
        return afterLoanBean;
    }

    /**
     * 根据订单号修改贷后车辆信息
     * 
     * @param params
     * @return
     * @throws GenerallyException
     * @Author haojinlong Create Date: 2018年5月4日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int updateAfterLoanInfo(JSONObject objs) throws GenerallyException, IqbException {
        int resultValue = 0;
        InstManageCarInfoEntity imci = JSONObject.toJavaObject(objs, InstManageCarInfoEntity.class);
        if (imci == null || !imci.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        carStatusManager.updateManagerCarInfoByOrderId(objs);
        return resultValue;
    }

    /**
     * 
     * Description:保存gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    public int saveInstGpsInfo(JSONObject objs) {
        int resultValue = 0;
        if (!StringUtil.isNull(objs.getString("gpsStatus"))) {
            resultValue = afterLoanBiz.saveGpsInfo(objs);
        }
        // 保存是否还款字段
        InstManageCarInfoEntity imci = JSONObject.toJavaObject(objs, InstManageCarInfoEntity.class);
        if (imci == null || !imci.checkEntity()) {
            logger.error("--参数不符合要求---{}", JSONObject.toJSON(imci));
        } else {
            carStatusManager.updateIMCI(imci);
        }
        return resultValue;
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
        return afterLoanBiz.getGpsInfoByOrderId(orderId);
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
        return afterLoanBiz.getReportListByRegId(objs);
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
        return afterLoanBiz.getReportByRegId(objs);
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
    public PageInfo<OutSourceOrderBean> selectOutSourcetList(JSONObject objs) {
        return new PageInfo<>(afterLoanBiz.selectOutSourcetList(objs));
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
    public PageInfo<OutSourceOrderBean> selectOutSourceProcesstList(JSONObject objs) {
        return new PageInfo<>(afterLoanBiz.selectOutSourceProcesstList(objs));
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int saveOutSourceOrder(JSONObject objs) throws IqbException {
        objs.put("operator", sysUserSession.getUserPhone());
        String operateFlag = objs.getString("operateFlag");// 是否转法务 1 是 2 否
        // 转法务,同步更新贷后信息表处理途径为转法务
        if (operateFlag.equals("1")) {
            objs.put("processMethod", 2);
            objs.put("processStatus", ProcessStatus.ProcessStatus_25);
            objs.put("caseSource", 1);
            instRemindPhoneBiz.updateManagerCarInfo(objs);
            saveCaseInfo(objs);
        } else {
            objs.put("processStatus", ProcessStatus.ProcessStatus_15);
            instRemindPhoneBiz.updateManagerCarInfo(objs);
        }
        return afterLoanBiz.saveOutSourceOrder(objs);
    }

    /**
     * 更新贷后车辆状态信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月10日
     */
    @Override
    public int updateManagerCarInfo(JSONObject objs) {
        return instRemindPhoneBiz.updateManagerCarInfo(objs);
    }

    /**
     * 
     * 保存贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    @Override
    public int saveInstManageCarReceive(JSONObject objs) {
        InstManageCarReceiveBean bean = JSONObject.toJavaObject(objs, InstManageCarReceiveBean.class);
        return instManageCarReceiveBiz.saveInstManageCarReceive(bean);
    }

    /**
     * 
     * 更新贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int updateInstManageCarReceiveByOrderId(JSONObject objs) throws IqbException {
        int resultValue = 0;
        String receivedFlag = objs.getString("receivedFlag");
        if (!StringUtil.isNull(receivedFlag)) {
            // 全额回款，案件进度为结束;未全额回款,案件进度由外包处理中变为法务处理中
            if (receivedFlag.equals("1")) {
                objs.put("processStatus", ProcessStatus.ProcessStatus_30);
            } else {
                objs.put("processStatus", ProcessStatus.ProcessStatus_25);
                objs.put("processMethod", "2");
                objs.put("lawTime", new Date());
                objs.put("caseSource", 1);
                saveCaseInfo(objs);
            }
            resultValue = instRemindPhoneBiz.updateManagerCarInfo(objs);
        }

        InstManageCarReceiveBean instManageCarReceiveBean =
                instManageCarReceiveBiz.getInstManageCarReceiveInfoByOrderId(objs);
        if (instManageCarReceiveBean != null) {
            resultValue += instManageCarReceiveBiz.updateInstManageCarReceiveByOrderId(objs);
        } else {
            InstManageCarReceiveBean bean = JSONObject.toJavaObject(objs, InstManageCarReceiveBean.class);
            resultValue += instManageCarReceiveBiz.saveInstManageCarReceive(bean);
        }
        return resultValue;
    }

    /**
     * 
     * 根据订单号获取已还款最大期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    private Map<String, Object> getMaxRepayNoByOrderId(JSONObject objs) {
        logger.info("---根据订单号获取已还款最大期数--调用账务系统接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getQueryOrderHasMaxRepayNo(),
                encryptUtils.encrypt(objs));
        logger.info("---根据订单号获取已还款最大期数--调用账务系统接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
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
    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<OutSourceOrderBean> selectCasetList(JSONObject objs) {
        List<OutSourceOrderBean> caseList = afterLoanBiz.selectCasetList(objs);
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(caseList)) {
            for (OutSourceOrderBean bean : caseList) {
                list.add(bean.getOrderId());
            }
        }
        JSONObject json = new JSONObject();
        json.put("orderList", list);
        Map<String, Object> map = getMaxRepayNosByOrderId(json);
        if (!CollectionUtils.isEmpty(map)) {
            List<Map<String, String>> orderList = (List<Map<String, String>>) map.get("result");
            for (OutSourceOrderBean bean : caseList) {
                for (Map<String, String> tempMap : orderList) {
                    if (bean.getOrderId().equals(tempMap.get("orderId"))) {
                        bean.setHasRepayNo(Integer.parseInt(tempMap.get("repayNo")));
                    }
                }
            }

        }
        return new PageInfo<>(caseList);
    }

    private Map<String, Object> getMaxRepayNosByOrderId(JSONObject objs) {
        logger.info("---根据订单号获取已还款最大期数--调用账务系统接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getQueryOrdersHasMaxRepayNo(),
                encryptUtils.encrypt(objs));
        logger.info("---根据订单号获取已还款最大期数--调用账务系统接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 
     * 保存案件登记资料 1.立案时更新案件进度为法务处理中 2.立案申请缴费成功时启动立案流程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @Override
    public int saveInstOrderLawnInfo(JSONObject objs) {
        InstOrderLawBean instOrderLawBean = JSONObject.toJavaObject(objs, InstOrderLawBean.class);
        // 登记资料,判断是否立案
        if (!StringUtil.isNull(instOrderLawBean.getRegisterFlag()) && instOrderLawBean.getRegisterFlag().equals("1")) {
            objs.put("processStatus", ProcessStatus.ProcessStatus_25);
            objs.put("caseStatus", CaseStatus.CaseStatus_15);
            instRemindPhoneBiz.updateManagerCarInfo(objs);
        } else if (!StringUtil.isNull(instOrderLawBean.getRegisterFlag())
                && instOrderLawBean.getRegisterFlag().equals("2")) {
            objs.put("caseStatus", 10);
        }
        // 更新案件信息
        return instOrderLawBiz.updateInstOrderLawnInfo(objs);
    }

    /**
     * 
     * 委案受理-新增
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @Override
    public Map<String, Object> newCase(JSONObject objs) throws IqbException {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("code", "000000");
        returnMap.put("message", "新增成功");
        if (!StringUtil.isNull(objs.getString("orderId"))) {
            objs.put("caseSource", 2);
            // 生成caseId
            String caseId = generateCaseId(objs.getString("orderId"));
            objs.put("caseId", caseId);
            // 是否立案 1 立案 2 不立案
            if (!StringUtil.isNull(objs.getString("registerFlag")) && objs.getString("registerFlag").equals("1")) {
                objs.put("caseStatus", CaseStatus.CaseStatus_15);
            } else {
                objs.put("caseStatus", CaseStatus.CaseStatus_10);
            }

            instOrderLawBiz.saveInstOrderLawnInfo(objs);
        }
        return returnMap;
    }

    /**
     * 启动立案申请流程
     * 
     * @param orderId
     * @return
     * @throws IqbException LinkedHashMap
     */
    @SuppressWarnings("rawtypes")
    private LinkedHashMap startWF(String orderId, String caseId)
            throws IqbException {
        LinkedHashMap responseMap = null;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            UserBean userBean = userBeanBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    "法务处理" + ";" + merchantBean.getMerchantNo() + ";"
                            + userBean.getRealName() + ";" + orderBean.getRegId();// 摘要添加手机号

            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 80);

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", sysUserSession.getUserCode());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", caseId);
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", orderBean.getOrderAmt());

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = wfConfig.getStartWfurl();
            // 发送Https请求

            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new RuntimeException("工作流接口交易失败");
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        }
        return responseMap;
    }

    /**
     * 根据案件号查询立案信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月17日
     */
    @Override
    public OutSourceOrderBean getCaseInfoByCaseId(String caseId) {
        OutSourceOrderBean outSourceOrderBean = afterLoanBiz.getCaseInfoByCaseId(caseId);
        JSONObject objs = new JSONObject();
        objs.put("orderId", outSourceOrderBean.getOrderId());
        Map<String, Object> map = getMaxRepayNoByOrderId(objs);

        if (!CollectionUtils.isEmpty(map) && map.get("retCode").toString().equals("success")) {
            if (map.get("num") != null) {
                int hasRepayNo = (Integer) map.get("num");
                outSourceOrderBean.setHasRepayNo(hasRepayNo);
            }
        }
        return outSourceOrderBean;
    }

    /**
     * 立案申请-庭前调解保存方法
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月17日
     */
    @Override
    public int updateInstOrderLawnInfo(JSONObject objs) {
        String composeFlag = objs.getString("composeFlag");
        String compromiseFlag = objs.getString("compromiseFlag");
        // composeFlag 是否庭前调解 1 是 2 否
        // compromiseFlag 和解标识 1 是 2 否
        if (composeFlag.equals("1") && compromiseFlag.equals("1")) {
            List<InstReceivedPaymentBean> list = new ArrayList<>();
            String orderId = objs.getString("orderId");
            String caseId = objs.getString("caseId");
            InstReceivedPaymentPojo receivedPaymentPojo = JSONObject.toJavaObject(objs, InstReceivedPaymentPojo.class);
            if (!CollectionUtils.isEmpty(receivedPaymentPojo.getReceivedPaymentList())) {
                // 根据caseId删除法务回款信息
                instReceivedPaymentBiz.deleteReceivedPayInfoByCaseId(caseId);

                InstReceivedPaymentBean bean = null;
                for (Map<String, String> map : receivedPaymentPojo.getReceivedPaymentList()) {
                    bean = new InstReceivedPaymentBean();
                    String receivedPayment = map.get("receivedPayment");
                    String receivedPaymentDate = map.get("receivedPaymentDate");
                    bean.setCaseId(caseId);
                    bean.setOrderId(orderId);
                    bean.setReceivedPayment(receivedPayment);
                    bean.setReceivedPaymentDate(receivedPaymentDate);
                    list.add(bean);
                }
            }
            instReceivedPaymentBiz.batchSaveInstReceivedPayment(list);
        }
        return instOrderLawBiz.updateInstOrderLawnInfo(objs);
    }

    /**
     * 批量保存法务回款信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月17日
     */
    @Override
    public int batchSaveInstReceivedPayment(JSONObject objs) {
        List<InstReceivedPaymentBean> list = new ArrayList<>();
        String orderId = objs.getString("orderId");
        InstReceivedPaymentPojo receivedPaymentPojo = JSONObject.toJavaObject(objs, InstReceivedPaymentPojo.class);
        if (!CollectionUtils.isEmpty(receivedPaymentPojo.getReceivedPaymentList())) {
            InstReceivedPaymentBean bean = null;
            for (Map<String, String> map : receivedPaymentPojo.getReceivedPaymentList()) {
                bean = new InstReceivedPaymentBean();
                String receivedPayment = map.get("receivedPayment");
                String receivedPaymentDate = map.get("receivedPaymentDate");

                bean.setOrderId(orderId);
                bean.setReceivedPayment(receivedPayment);
                bean.setReceivedPaymentDate(receivedPaymentDate);
                list.add(bean);
            }
        }

        return instReceivedPaymentBiz.batchSaveInstReceivedPayment(list);
    }

    /**
     * 根据订单号查询法务回款列表信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月17日
     */
    @Override
    public List<InstReceivedPaymentBean> selectInstReceivedPaymentList(String orderId, String caseId) {
        return instReceivedPaymentBiz.selectInstReceivedPaymentList(orderId, caseId);
    }

    /**
     * 庭审登记信息分页查询
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月18日
     */
    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<OutSourceOrderBean> selectTrialRegistList(JSONObject objs) {
        List<OutSourceOrderBean> registList = afterLoanBiz.selectTrialRegistList(objs);
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(registList)) {
            for (OutSourceOrderBean bean : registList) {
                list.add(bean.getOrderId());
            }
        }
        JSONObject json = new JSONObject();
        json.put("orderList", list);
        Map<String, Object> map = getMaxRepayNosByOrderId(json);
        if (!CollectionUtils.isEmpty(map)) {
            List<Map<String, String>> orderList = (List<Map<String, String>>) map.get("result");
            for (OutSourceOrderBean bean : registList) {
                for (Map<String, String> tempMap : orderList) {
                    if (bean.getOrderId().equals(tempMap.get("orderId"))) {
                        bean.setHasRepayNo(Integer.parseInt(tempMap.get("repayNo")));
                    }
                }
            }
        }
        return new PageInfo<>(registList);
    }

    /**
     * 
     * 根据caseId从查询案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @Override
    public InstOrderLawBean getInstOrderLawnInfoByCaseId(JSONObject objs) {
        return instOrderLawBiz.getInstOrderLawnInfoByCaseId(objs);
    }

    /**
     * 
     * 保存庭审登记信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @Override
    public int saveInstOrderLawResult(JSONObject objs) {
        String executeFlag = objs.getString("executeFlag");
        if (!StringUtil.isNull(executeFlag) && executeFlag.equals("1")) {
            objs.put("caseStatus", CaseStatus.CaseStatus_30);
            instOrderLawBiz.updateInstOrderLawnInfo(objs);
        }
        return instOrderLawResultBiz.saveInstOrderLawResult(objs);
    }

    /**
     * 
     * 显示庭审登记列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @Override
    public PageInfo<InstOrderLawResultBean> selectInstOrderLawResultByCaseId(JSONObject objs) {
        List<InstOrderLawResultBean> instOrderLawResultList =
                instOrderLawResultBiz.selectInstOrderLawResultByOrderId(objs);
        return new PageInfo<>(instOrderLawResultList);
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
    @Override
    public PageInfo<Map<String, Object>> selectAfterCaseOrderInfoList(JSONObject requestMessage) {
        List<Map<String, Object>> caseInfoOrderList = afterLoanBiz.selectAfterCaseOrderInfoList(requestMessage);
        try {
            caseInfoOrderList = appendCollectBillData(caseInfoOrderList);
        } catch (Exception e) {
            logger.error("", e);
        }
        return new PageInfo<>(caseInfoOrderList);
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
    @Override
    public String exportAfterCaseOrderInfoList(JSONObject requestMessage, HttpServletResponse response) {
        try {
            List<Map<String, Object>> caseInfoOrderList = afterLoanBiz.exportAfterCaseOrderInfoList(requestMessage);
            try {
                caseInfoOrderList = appendCollectBillData(caseInfoOrderList);
            } catch (Exception e) {
                logger.error("", e);
            }
            // 2.导出excel表格
            HSSFWorkbook workbook = getCaseInfoExportWorkbook(caseInfoOrderList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=afterCaseInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出贷后案件列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    private HSSFWorkbook getCaseInfoExportWorkbook(List<Map<String, Object>> caseInfoOrderList) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "商户", "借款金额", "月供", "总期数", "已还期数", "逾期天数", "车辆状态", "贷后状态", "GPS状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("贷后案件列表");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle amtStyle = wb.createCellStyle(); // 货币样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        amtStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        if (caseInfoOrderList != null)
            for (int i = 0; i < caseInfoOrderList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> caseOrder = caseInfoOrderList.get(i);
                row.createCell(0).setCellValue(
                        caseOrder.get("orderId") == null ? "" : caseOrder.get("orderId").toString());
                row.createCell(1).setCellValue(
                        caseOrder.get("realName") == null ? "" : caseOrder.get("realName").toString());
                row.createCell(2).setCellValue(caseOrder.get("regId") == null ? "" : caseOrder.get("regId").toString());
                row.createCell(3).setCellValue(
                        caseOrder.get("merchantName") == null ? "" : caseOrder.get("merchantName").toString());
                row.createCell(4).setCellValue(caseOrder.get("orderAmt") == null ? 0 :
                        ((BigDecimal) caseOrder.get("orderAmt")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                row.createCell(5).setCellValue(
                        caseOrder.get("monthInterest") == null ? 0 :
                                ((BigDecimal) caseOrder.get("monthInterest")).setScale(2, BigDecimal.ROUND_HALF_UP)
                                        .doubleValue());
                row.createCell(6).setCellValue(caseOrder.get("orderItems") == null ? "0" :
                        ((Integer) caseOrder.get("orderItems")).intValue() + "");
                row.createCell(7).setCellValue(caseOrder.get("finishItems") == null ? "0" :
                        ((Integer) caseOrder.get("finishItems")).intValue() + "");
                row.createCell(8).setCellValue(caseOrder.get("overDueDays") == null ? "0" :
                        ((Integer) caseOrder.get("overDueDays")).intValue() + "");
                if (caseOrder.get("carStatus") != null)
                    switch (caseOrder.get("carStatus").toString()) {
                        case "5":
                            row.createCell(9).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(9).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(9).setCellValue("贷后处置中");
                            break;
                        case "15":
                            row.createCell(9).setCellValue("待失联处置");
                            break;
                        case "20":
                            row.createCell(9).setCellValue("已失联");
                            break;
                        case "25":
                            row.createCell(9).setCellValue("待入库");
                            break;
                        case "30":
                            row.createCell(9).setCellValue("已入库");
                            break;
                        case "35":
                            row.createCell(9).setCellValue("待出售");
                            break;
                        case "40":
                            row.createCell(9).setCellValue("已出售");
                            break;
                        case "45":
                            row.createCell(9).setCellValue("待转租");
                            break;
                        case "50":
                            row.createCell(9).setCellValue("已转租");
                            break;
                        case "55":
                            row.createCell(9).setCellValue("待返还");
                            break;
                        case "60":
                            row.createCell(9).setCellValue("已返还");
                            break;
                        case "65":
                            row.createCell(9).setCellValue("未拖回");
                            break;
                        case "70":
                            row.createCell(9).setCellValue("正常");
                            break;
                    }
                // 贷后状态: 5 待贷后处理 10贷后处理中 15 待外包处理 20 外包处理中 25 法务处理中 30 处理结束
                if (caseOrder.get("processStatus") != null)
                    switch (caseOrder.get("processStatus").toString()) {
                        case "5":
                            row.createCell(10).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(10).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(10).setCellValue("贷后处理中");
                            break;
                        case "15":
                            row.createCell(10).setCellValue("待外包处理");
                            break;
                        case "20":
                            row.createCell(10).setCellValue("外包处理中");
                            break;
                        case "25":
                            row.createCell(10).setCellValue("法务处理中");
                            break;
                        case "30":
                            row.createCell(10).setCellValue("处理结束");
                            break;
                    }
                // gps状态: 1正常/2有线离线/3无线离线/4断电报警/5拆除报警/6其他
                if (caseOrder.get("gpsStatus") != null)
                    switch ((Integer) caseOrder.get("gpsStatus")) {
                        case 0:
                            row.createCell(11).setCellValue("未标记");
                            break;
                        case 1:
                            row.createCell(11).setCellValue("正常");
                            break;
                        case 2:
                            row.createCell(11).setCellValue("有线离线");
                            break;
                        case 3:
                            row.createCell(11).setCellValue("无线离线");
                            break;
                        case 4:
                            row.createCell(11).setCellValue("断电报警");
                            break;
                        case 5:
                            row.createCell(11).setCellValue("拆除报警");
                            break;
                        case 6:
                            row.createCell(11).setCellValue("其他");
                            break;
                        case 7:
                            row.createCell(11).setCellValue("全部离线");
                            break;
                        case 8:
                            row.createCell(11).setCellValue("出省");
                            break;
                    }
            }
        return wb;

    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> appendCollectBillData(List<Map<String, Object>> caseOrderList) throws Exception {
        if (caseOrderList == null || caseOrderList.isEmpty())
            return new ArrayList<Map<String, Object>>();

        Map<String, Map<String, Object>> collectMap = new HashMap<String, Map<String, Object>>();
        Map<String, String> orderIdMapTmp = new HashMap<String, String>();
        int listSize = caseOrderList.size();
        int maxIndex = listSize - 1;
        for (int i = 0; i < listSize; i++) {
            orderIdMapTmp.put((String) caseOrderList.get(i).get("orderId"), i + "");
            if (orderIdMapTmp.size() % 50 == 0 || i == maxIndex) {
                List<String> orderIdListTmp = new ArrayList<String>();
                orderIdListTmp.addAll(orderIdMapTmp.keySet());

                Map<String, Object> tmpMap = new HashMap<String, Object>();
                tmpMap.put("orderIdList", orderIdListTmp);
                /** 请求按订单分组汇总账单信息 **/
                String resultStr = SimpleHttpUtils.httpPost(
                        consumerConfig.getFinanceBillCollectByOrderUrl(),
                        SignUtil.chatEncode(JSONObject.toJSONString(tmpMap), consumerConfig.getCommonPrivateKey()));
                logger.info("finance billCollectByOrder resultStr:" + resultStr);

                JSONObject responseJso = JSONObject.parseObject(resultStr);
                List<Map<String, Object>> cList =
                        JSONObject.parseObject(JSONObject.toJSONString(responseJso.get("result")),
                                List.class);
                if (cList != null)
                    for (Map<String, Object> caseColMap : cList)
                        collectMap.put((String) caseColMap.get("orderId"), caseColMap);

                orderIdMapTmp = new HashMap<String, String>();
            }
        }

        for (Map<String, Object> caseOrder : caseOrderList) {
            if (collectMap.get(caseOrder.get("orderId")) == null)
                continue;
            Map<String, Object> caseColMap = collectMap.get(caseOrder.get("orderId"));
            caseOrder.put("finishItems", caseColMap.get("finiCnt"));
            caseOrder.put("overDueDays", caseColMap.get("overMax"));
        }

        return caseOrderList;
    }

    /**
     * 
     * 保存案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    private Map<String, Object> saveCaseInfo(JSONObject objs) throws IqbException {
        Map<String, Object> result = new HashMap<>();
        String orderId = objs.getString("orderId");
        // 生成caseId
        String caseId = generateCaseId(orderId);
        objs.put("caseId", caseId);
        // 是否立案 1 立案 2 不立案
        if (!StringUtil.isNull(objs.getString("registerFlag")) && objs.getString("registerFlag").equals("1")) {
            objs.put("caseStatus", CaseStatus.CaseStatus_15);
            // 启动立案申请流程
            startWF(orderId, caseId);
        } else if (!StringUtil.isNull(objs.getString("registerFlag")) && objs.getString("registerFlag").equals("2")) {
            objs.put("caseStatus", CaseStatus.CaseStatus_10);
        } else {
            objs.put("caseStatus", CaseStatus.CaseStatus_05);
        }
        int resultValue = instOrderLawBiz.saveInstOrderLawnInfo(objs);
        if (resultValue == 1) {
            result.put("resultCode", ApiReturnInfo.API_AFTERLOAN_SUCCESS_00000000.getRetCode());
            result.put("resultMsg", ApiReturnInfo.API_AFTERLOAN_SUCCESS_00000000.getRetUserInfo());
        } else {
            result.put("resultCode", ApiReturnInfo.API_AFTERLOAN_FAIL_00000000.getRetCode());
            result.put("resultMsg", ApiReturnInfo.API_AFTERLOAN_FAIL_00000000.getRetUserInfo());
        }
        return result;
    }

    /**
     * 
     * 生成caseId
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    private String generateCaseId(String orderId) throws IqbException {
        String seqRedisKey = this.getOrderRedisKey(orderId);
        return seqRedisKey + "-" + this.getSeqFromRedis(seqRedisKey, false);
    }

    /**
     * 
     * 设置获取案件信息caseId的redis key值
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    private String getOrderRedisKey(String orderId) throws IqbException {
        return "FW" + orderId;
    }

    /**
     * 
     * 从redis中获取caseId序号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    private synchronized String getSeqFromRedis(String key, boolean isSub) throws IqbException {

        Redisson redisson =
                RedisUtils.getInstance()
                        .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
        DecimalFormat df = null;
        Integer seq = 0;
        try {
            RLock rLock = RedisUtils.getInstance().getRLock(redisson, "getSeqFromRedis");
            try {
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    /** 数字格式化 **/
                    df = new DecimalFormat(STR_FORMAT);
                    /** 从redis中取值 **/
                    String val = this.redisPlatformDao.getValueByKey(key);
                    if (StringUtils.isEmpty(val)) {
                        this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
                        return df.format(Integer.parseInt(this.INITIAL_SEQ));
                    }
                    seq = Integer.parseInt(val);

                    /** 判断是否进行减法操作 **/
                    if (isSub) {
                        seq = seq - 1;
                        this.redisPlatformDao.setKeyAndValue(key, seq.toString());
                    } else {
                        seq = seq + 1;
                        this.redisPlatformDao.setKeyAndValue(key, seq.toString());
                    }
                } else {
                    // 未获取到锁
                    throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
                }
            } finally {
                rLock.unlock();
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
        }
        // 关闭连接
        try {
            if (redisson != null) {
                RedisUtils.getInstance().closeRedisson(redisson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return df.format(seq);
    }

    /**
     * 
     * 根据caseId查询案件进度信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    @Override
    public PageInfo<InstOrderCaseExecuteBean> selectInstOrderCaseExecuteByCaseId(JSONObject objs) {
        List<InstOrderCaseExecuteBean> caseExecuteList =
                instOrderCaseExecuteBiz.selectInstOrderCaseExecuteByCaseId(objs);
        return new PageInfo<>(caseExecuteList);
    }

    /**
     * 
     * 保存案件进度信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    @Override
    public int saveInstOrderCaseExecute(JSONObject objs) {
        if (!StringUtil.isNull(objs.getString("executeResult")) && objs.getString("executeResult").equals("1")) {
            // 如果该案件由外包转入，则同步更改其外包状态
            List<InstOrderLawBean> lawList = instOrderLawBiz.selectInstOrderLawnInfoByOrderId(objs);
            if (!CollectionUtils.isEmpty(lawList)) {
                InstOrderLawBean lawBean = lawList.get(0);
                if (lawBean.getCaseSource().equals("1")) {
                    objs.put("processStatus", ProcessStatus.ProcessStatus_30);
                    instRemindPhoneBiz.updateManagerCarInfo(objs);
                }
            }

            objs.put("caseStatus", CaseStatus.CaseStatus_35);
            instOrderLawBiz.updateInstOrderLawnInfo(objs);
        }
        return instOrderCaseExecuteBiz.saveInstOrderCaseExecute(objs);
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
    @Override
    public PageInfo<Map<String, Object>> selectAfterCaseOrderLawList(
            JSONObject requestMessage) {
        List<Map<String, Object>> caseLawOrderList = afterLoanBiz.selectAfterCaseOrderLawList(requestMessage);
        try {
            caseLawOrderList = appendCollectBillData(caseLawOrderList);
        } catch (Exception e) {
            logger.error("", e);
        }
        return new PageInfo<>(caseLawOrderList);
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
    @Override
    public String exportAfterCaseOrderLawList(JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            List<Map<String, Object>> caseLawOrderList = afterLoanBiz.exportAfterCaseOrderLawList(requestMessage);
            try {
                caseLawOrderList = appendCollectBillData(caseLawOrderList);
            } catch (Exception e) {
                logger.error("", e);
            }
            // 2.导出excel表格
            HSSFWorkbook workbook = getCaseLawExportWorkbook(caseLawOrderList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=afterCaseLaw-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出贷后案件列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    private HSSFWorkbook getCaseLawExportWorkbook(List<Map<String, Object>> caseLawOrderList) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "商户", "案件编号", "立案时间", "是否为原告", "案件来源", "案件状态", "立案方"
                , "受理机构", "总回款金额", "执行结果", "车辆状态", "贷后状态", "借款金额", "月供", "总期数", "已还期数", "逾期天数"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("贷后案件列表");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle amtStyle = wb.createCellStyle(); // 货币样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        amtStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        if (caseLawOrderList != null)
            for (int i = 0; i < caseLawOrderList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> caseOrder = caseLawOrderList.get(i);
                row.createCell(0).setCellValue(
                        caseOrder.get("orderId") == null ? "" : caseOrder.get("orderId").toString());
                row.createCell(1).setCellValue(
                        caseOrder.get("realName") == null ? "" : caseOrder.get("realName").toString());
                row.createCell(2).setCellValue(caseOrder.get("regId") == null ? "" : caseOrder.get("regId").toString());
                row.createCell(3).setCellValue(
                        caseOrder.get("merchantName") == null ? "" : caseOrder.get("merchantName").toString());
                row.createCell(4).setCellValue(
                        caseOrder.get("caseNo") == null ? "" : caseOrder.get("caseNo").toString());
                row.createCell(5).setCellValue(
                        caseOrder.get("registerDate") == null ? "" : caseOrder.get("registerDate").toString());
                if (caseOrder.get("accuserFlag") != null)
                    switch ((Integer) caseOrder.get("accuserFlag")) {
                        case 1:
                            row.createCell(6).setCellValue("是");
                            break;
                        case 2:
                            row.createCell(6).setCellValue("否");
                            break;
                    }
                if (caseOrder.get("caseSource") != null) {
                    int caseSourceIntVal = -1;

                    try {
                        caseSourceIntVal = Integer.valueOf(caseOrder.get("caseSource").toString());
                    } catch (Exception eee) {
                        logger.error("", eee);
                    }
                    switch (caseSourceIntVal) {
                        case 1:
                            row.createCell(7).setCellValue("贷后转入 ");
                            break;
                        case 2:
                            row.createCell(7).setCellValue("法务新增");
                            break;
                    }
                }
                // 案件状态: 5 待立案 10 立案中 15已立案 20 待执行
                if (caseOrder.get("caseStatus") != null)
                    switch (caseOrder.get("caseStatus").toString()) {
                        case "5":
                            row.createCell(8).setCellValue("待法务受理");
                            break;
                        case "05":
                            row.createCell(8).setCellValue("待法务受理");
                            break;
                        case "10":
                            row.createCell(8).setCellValue("资料准备中");
                            break;
                        case "15":
                            row.createCell(8).setCellValue("立案申请中");
                            break;
                        case "20":
                            row.createCell(8).setCellValue("已立案");
                            break;
                        case "25":
                            row.createCell(8).setCellValue("庭审登记中");
                            break;
                        case "30":
                            row.createCell(8).setCellValue("执行中");
                            break;
                        case "35":
                            row.createCell(8).setCellValue("结束");
                            break;
                    }

                if (caseOrder.get("register") != null)
                    switch (caseOrder.get("register").toString()) {
                        case "1":
                            row.createCell(9).setCellValue("我方立案");
                            break;
                        case "2":
                            row.createCell(9).setCellValue("委托机构");
                            break;
                    }
                row.createCell(10).setCellValue(
                        caseOrder.get("acceptOrg") == null ? "" : caseOrder.get("acceptOrg").toString());
                row.createCell(11).setCellValue(
                        caseOrder.get("sumReceivedPayment") == null ? 0 :
                                ((BigDecimal) caseOrder.get("sumReceivedPayment"))
                                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                if (caseOrder.get("executeResult") != null)
                    switch ((Integer) caseOrder.get("executeResult")) {
                        case 1:
                            row.createCell(12).setCellValue("全部执行");
                            break;
                        case 2:
                            row.createCell(12).setCellValue("部分执行");
                            break;
                        case 3:
                            row.createCell(12).setCellValue("未执行");
                            break;
                        case 4:
                            row.createCell(12).setCellValue("失信被执行人");
                            break;
                    }
                if (caseOrder.get("carStatus") != null)
                    switch (caseOrder.get("carStatus").toString()) {
                        case "5":
                            row.createCell(13).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(13).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(13).setCellValue("贷后处置中");
                            break;
                        case "15":
                            row.createCell(13).setCellValue("待失联处置");
                            break;
                        case "20":
                            row.createCell(13).setCellValue("已失联");
                            break;
                        case "25":
                            row.createCell(13).setCellValue("待入库");
                            break;
                        case "30":
                            row.createCell(13).setCellValue("已入库");
                            break;
                        case "35":
                            row.createCell(13).setCellValue("待出售");
                            break;
                        case "40":
                            row.createCell(13).setCellValue("已出售");
                            break;
                        case "45":
                            row.createCell(13).setCellValue("待转租");
                            break;
                        case "50":
                            row.createCell(13).setCellValue("已转租");
                            break;
                        case "55":
                            row.createCell(13).setCellValue("待返还");
                            break;
                        case "60":
                            row.createCell(13).setCellValue("已返还");
                            break;
                        case "65":
                            row.createCell(13).setCellValue("未拖回");
                            break;
                        case "70":
                            row.createCell(13).setCellValue("正常");
                            break;
                    }
                // 贷后状态: 5 待贷后处理 10贷后处理中 15 待外包处理 20 外包处理中 25 法务处理中 30 处理结束
                if (caseOrder.get("processStatus") != null)
                    switch (caseOrder.get("processStatus").toString()) {
                        case "5":
                            row.createCell(14).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(14).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(14).setCellValue("贷后处理中");
                            break;
                        case "15":
                            row.createCell(14).setCellValue("待外包处理");
                            break;
                        case "20":
                            row.createCell(14).setCellValue("外包处理中");
                            break;
                        case "25":
                            row.createCell(14).setCellValue("法务处理中");
                            break;
                        case "30":
                            row.createCell(14).setCellValue("处理结束");
                            break;
                    }

                row.createCell(15).setCellValue(caseOrder.get("orderAmt") == null ? 0 :
                        ((BigDecimal) caseOrder.get("orderAmt")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                row.createCell(16).setCellValue(
                        caseOrder.get("monthInterest") == null ? 0 :
                                ((BigDecimal) caseOrder.get("monthInterest")).setScale(2, BigDecimal.ROUND_HALF_UP)
                                        .doubleValue());
                row.createCell(17).setCellValue(caseOrder.get("orderItems") == null ? "0" :
                        ((Integer) caseOrder.get("orderItems")).intValue() + "");
                row.createCell(18).setCellValue(caseOrder.get("finishItems") == null ? "0" :
                        ((Integer) caseOrder.get("finishItems")).intValue() + "");
                row.createCell(19).setCellValue(caseOrder.get("overDueDays") == null ? "0" :
                        ((Integer) caseOrder.get("overDueDays")).intValue() + "");
            }
        return wb;
    }

    @Override
    public Map<String, Object> totalAfterCaseOrderInfo(JSONObject requestMessage) {
        return afterLoanBiz.totalAfterCaseOrderInfo(requestMessage);
    }

    @Override
    public Map<String, Object> totalAfterCaseOrderLaw(JSONObject requestMessage) {
        return afterLoanBiz.totalAfterCaseOrderLaw(requestMessage);
    }

    /**
     * 校验编号是否唯一
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月30日
     */
    @Override
    public int validateCaseNo(JSONObject objs) {
        int resultValue = 0;
        List<InstOrderLawBean> orderLawList = instOrderLawBiz.selectInstOrderLawnInfoByCaseNo(objs);
        if (!CollectionUtils.isEmpty(orderLawList)) {
            resultValue = 1;
        }
        return resultValue;
    }

    /**
     * 立案申请方法
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月31日
     */
    @Override
    public int applyCase(JSONObject objs) throws IqbException {
        InstOrderLawBean instOrderLawBean = JSONObject.toJavaObject(objs, InstOrderLawBean.class);
        // 立案申请-是否缴费成功,缴费成功,启动立案申请流程
        if (!StringUtil.isNull(instOrderLawBean.getPayFlag())) {
            if (instOrderLawBean.getPayFlag().equals("1")) {
                objs.put("caseStatus", CaseStatus.CaseStatus_20);
                startWF(objs.getString("orderId"), objs.getString("caseId"));
            } else if (instOrderLawBean.getPayFlag().equals("2")) {
                objs.put("processStatus", ProcessStatus.ProcessStatus_25);
                objs.put("caseStatus", CaseStatus.CaseStatus_10);
                objs.put("registerFlag", 2);
                instRemindPhoneBiz.updateManagerCarInfo(objs);
            }
        }
        // 更新案件信息
        return instOrderLawBiz.updateInstOrderLawnInfo(objs);
    }
}
