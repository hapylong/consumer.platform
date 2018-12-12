package com.iqb.consumer.service.layer.back;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.account.AccountBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.schedule.http.ApiRequestMessage;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskAnalysisAllot;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskManager;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;

@Service
public class BackGroundServiceImpl implements IBackGroundService {

    protected static final Logger logger = LoggerFactory.getLogger(BackGroundServiceImpl.class);
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Autowired
    private DictService dictServiceImpl;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private PaymentLogBiz paymentLogBiz;
    @Resource
    private SysUserSession sysUserSession;

    @Autowired
    private ScheduleTaskManager scheduleTaskManager;
    @Autowired
    private ScheduleTaskAnalysisAllot scheduleTaskAnalysisAllot;
    @Autowired
    private JysOrderBiz jysOrderBiz;
    @Resource
    private CalculateService calculateService;

    /**
     * 平账接口
     */
    @Override
    public Map<String, Object> goToRefund(JSONObject objs) {
        Map<String, Object> result = new HashMap<String, Object>();
        String orderId = objs.getString("orderId");
        // 1.查询订单
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        OrderBean orderBean = orderBiz.selectOne(params);
        // 2.查询账单
        UserBean ub = userBeanBiz.selectOne(orderBean.getRegId());
        Map<String, Object> billMap = billInfoService.getBillInfo(orderId, ub.getRegId());
        if (billMap == null) {
            logger.debug("平账操作，订单{}异常，未查询到账单", orderId);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "未查询到账单");
            return result;
        }
        // 2.5 校验期数是否相同
        if (billMap.get("repayNo") != objs.getInteger("repayNo")) {
            logger.debug("平账操作，订单{}异常，获取参数与系统获取平账期数不同, 获取参数期数:{}", orderId, objs.getInteger("repayNo"));
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "平账传递期数异常");
            return result;
        }
        // 3.平账
        // 平账传参
        Map<String, String> signParameters = this.getSignParameters(objs, billMap);
        // 插入支付日志
        try {
            paymentLogBiz.insertPaymentLog(signParameters);
        } catch (Exception e) {
            logger.error("线下平账插入日志出现异常:{}", e);
        }
        // 开始平账
        Map<String, Object> retMap2 = billInfoService.repay(billMap, signParameters);
        if (retMap2 != null && FinanceConstant.SUCCESS.equals(retMap2.get("retCode"))) {

            // 校验账单是否已全部还款并且同步修改订单状态
            validateAndUpdateOrderInfo(orderId, objs.getInteger("repayNo"));

            result.put("retCode", FinanceConstant.SUCCESS);
            result.put("retMsg", "平账成功");
            return result;
        }

        return result;
    }

    /**
     * 获取平账参数
     */
    private Map<String, String> getSignParameters(JSONObject objs, Map<String, Object> billMap) {
        String userCode = sysUserSession.getUserCode();
        Map<String, String> signParameters = new HashMap<String, String>();
        String repayDate = objs.getString("repayDate");
        Date date = DateUtil.parseDate(repayDate, DateUtil.SHORT_DATE_FORMAT);
        BigDecimal curRepayAmt = BigDecimalUtil.mul((BigDecimal) billMap.get("curRepayAmt"), new BigDecimal(100));
        signParameters.put("tranTime", DateUtil.getDateString(date, DateUtil.SIMPLE_DATE_FORMAT));// 还款日期
        signParameters.put("tradeNo", objs.getString("tradeNo"));// 流水号
        signParameters.put("orderId", objs.getString("orderId"));
        signParameters.put("merchantNo", (String) billMap.get("merchantNo"));
        signParameters.put("regId", (String) billMap.get("regId"));
        signParameters.put("amount", curRepayAmt.toString());// 金额
        signParameters.put("remark", "(" + userCode + ")" + "平账原因 : " + objs.getString("reason"));
        signParameters.put("flag", "23");// 线下平账分期付款
        signParameters.put("bankName", objs.getString("bankName"));// 银行名称
        signParameters.put("bankCardNo", objs.getString("bankCardNo"));// 银行卡
        signParameters.put("repayType", "23");// 给账户系统的标识

        return signParameters;
    }

    /**
     * 分期接口
     */
    @Override
    public Map<String, Object> singleInstall(JSONObject objs) {
        Map<String, Object> result = new HashMap<String, Object>();
        OrderBean orderBean = orderBiz.selectOne(objs);
        // 先开户
        boolean accFlag = (boolean) this.openAccount(orderBean);
        if (!accFlag) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "用户尚未开户且开户失败");
            return result;
        }
        // FINANCE-3298 给客户发送还款提醒/逾期提醒/划扣失败手机号取最新更新的接收短信手机号
        // update Date: 2018年5月22日
        UserBean userBean = userBeanBiz.getUserInfo(Long.valueOf(orderBean.getUserId()));
        if (userBean != null && !StringUtil.isNull(userBean.getSmsMobile())) {
            orderBean.setSmsMobile(userBean.getSmsMobile());
        }
        // 分期
        result = sendInstallRequest(objs, orderBean);
        if (FinanceConstant.SUCCESS.equals(result.get("retCode"))) {
            orderBean.setRiskStatus(3);// 已经分期
            try {
                String url = orderBean.getOrderRemark();
                boolean save = scheduleTaskManager.isMerchantNeedSave(orderBean.getMerchantNo());
                ApiRequestMessage data = new ApiRequestMessage();
                data.setOrderId(orderBean.getOrderId());
                data.setStatus(3);
                scheduleTaskAnalysisAllot.send(data.toString(), url, ScheduleTaskAnalysisAllot.MODULE_A,
                        orderBean.getOrderId(), save);
            } catch (Throwable e) {
                logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
            }

            try {
                List<String> paraList = new ArrayList<String>();
                paraList.add(objs.getString("orderId"));
                objs.put("orderIds", paraList);
                objs.put("stageDate", objs.getString("beginDate"));
                objs.put("riskStatus", "3");
                orderBiz.updateLoanDateByOrderIds(objs);
            } catch (Exception e) {
                logger.error("修改数据库出现异常，但是账户系统已经分期:{}", e.getMessage());
                result.put("retCode", FinanceConstant.ERROR);
            }
            String merchList = ymForeignConfig.getYmMerchList();
            List<String> list = ymForeignConfig.getMerchList(merchList);
            if (list.contains(orderBean.getMerchantNo())) {// 胡桃钱包的商户需要回调返回分期信息
                String sendRes = sendFQResult2YM(orderBean.getOrderId());
                logger.debug("分期完毕通知医美类商户返回结果:{}", sendRes);
            }
        }

        return result;
    }

    @Override
    public Object jsyOrderBreak(JSONObject requestMessage) {
        Map<String, Object> result = new HashMap<String, Object>();
        JYSOrderBean job = jysOrderBiz.getSingleOrderInfo(requestMessage);
        // 先开户
        boolean accFlag = openAccount(job);
        if (!accFlag) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "用户尚未开户且开户失败");
            return result;
        }
        // 分期
        result = sendInstallRequest(requestMessage, job);
        if (FinanceConstant.SUCCESS.equals(result.get("retCode"))) {
            job.setRiskStatus(3);// 已经分期

            try {
                String d = requestMessage.getString("beginDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date upperTime = sdf.parse(d);
                job.setUpperTime(upperTime);
            } catch (Throwable e) {
                throw new RuntimeException("invalid request message.", e);
            }
            try {
                String url = job.getOrderRemark();
                boolean save = scheduleTaskManager.isMerchantNeedSave(job.getMerchantNo());
                ApiRequestMessage data = new ApiRequestMessage();
                data.setOrderId(job.getOrderId());
                data.setStatus(3);
                scheduleTaskAnalysisAllot.send(data.toString(), url, ScheduleTaskAnalysisAllot.MODULE_A,
                        job.getOrderId(), save);
            } catch (Throwable e) {
                logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
            }

            try {
                jysOrderBiz.updateOrderRiskStatus(job);
            } catch (Exception e) {
                logger.error("修改数据库出现异常，但是账户系统已经分期:{}", e.getMessage());
                result.put("retCode", FinanceConstant.ERROR);
            }
            String merchList = ymForeignConfig.getYmMerchList();
            List<String> list = ymForeignConfig.getMerchList(merchList);
            if (list.contains(job.getMerchantNo())) {// 胡桃钱包的商户需要回调返回分期信息
                String sendRes = sendFQResult2YM(job.getOrderId());
                logger.debug("分期完毕通知医美类商户返回结果:{}", sendRes);
            }
        }

        return result;
    }

    private Map<String, Object> sendInstallRequest(JSONObject requestMessage, JYSOrderBean job) {
        Map<String, Object> result = new HashMap<String, Object>();
        String planId = job.getPlanId();
        String orderId = job.getOrderId();
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(planId));
        String installAmt = null;
        String interestAmt = null;
        try {
            Map<String, String> installInfoMap =
                    getInstallInfoMap(new BigDecimal(job.getOrderAmt() == null ? "0" : job.getOrderAmt()),
                            new BigDecimal(job.getDownPayment() == null ? "0" : job.getDownPayment()), planBean);
            installAmt = installInfoMap.get("installAmt");
            interestAmt = installInfoMap.get("interestAmt");
        } catch (Exception e1) {
            e1.printStackTrace();
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "分期异常");
            return result;
        }

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderId", orderId);
        sourceMap.put("regId", job.getRegId());
        sourceMap.put("orderDate",
                DateUtil.getDateString(job.getCreateTime(), DateUtil.SHORT_DATE_FORMAT_NO_DASH));
        sourceMap.put(
                "beginDate",
                requestMessage.getString("beginDate") == null ? DateUtil.getDateString(new Date(),
                        DateUtil.SHORT_DATE_FORMAT_NO_DASH) : requestMessage.getString("beginDate"));
        sourceMap.put("openId", dictServiceImpl.getJysOpenIdByOrderId(orderId));
        sourceMap.put("merchantNo", job.getMerchantNo());
        sourceMap.put("installSumAmt", installAmt);
        sourceMap.put("installAmt", installAmt);// 分期金额本金
        sourceMap.put("interestAmt", interestAmt);// 剩余利息
        sourceMap.put("takeInterest", Double.parseDouble(interestAmt) > 0 ? "1" : "2");// 是否上收利息
        sourceMap.put("takeMonth", planBean.getTakePayment() == 1 ? "1" : "2");// 是否上收月供
        sourceMap.put("takePaymentAmt", job.getMonthInterest());// 上收月供金额
        sourceMap.put("takePayment", planBean.getTakePayment());// 上收月供数
        sourceMap.put("installTerms", job.getOrderItems());
        sourceMap.put("planId", planBean.getPlanId());
        sourceMap.put("otherAmt", BigDecimal.ZERO); // 其他费用
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceInstallInstUrl(),
                    encryptUtils.encrypt(sourceMap));
            // 根据平账结果返回成功与否
            result = JSONObject.parseObject(resultStr);
            logger.info("正常还款校验参数返回结果，返回参数:{}", result);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
        }

        return result;
    }

    private boolean openAccount(JYSOrderBean job) {
        String openId = dictServiceImpl.getJysOpenIdByOrderId(job.getOrderId());
        AccountBean ab = new AccountBean();
        ab.setOpenId(openId);
        /** 判断账户是否开户 **/
        ab.setRegId(job.getRegId());
        if (!(boolean) this.queryAccount(ab, new OrderBean())) {
            return true;
        }
        /** 获取银行卡信息 **/
        JYSUserBean jub = userBeanBiz.getJUBByRegId(job.getRegId());
        ab.setBankCardNo(jub.getBankCardNo());
        ab.setRealName(jub.getRealName());
        ab.setIdNo(jub.getIdNo());
        ab.setRegId(jub.getRegId());
        logger.info("开户传入信息：{}", JSONObject.toJSONString(ab));

        /** 请求账户接口 **/
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getFinanceAccountOpenAccUrl(),
                        encryptUtils.encrypt(BeanUtil.entity2map(ab)));
        logger.info("订单{}调用开户接口返回结果:{}", job.getOrderId(), resultStr);
        /** 处理返回结果 **/
        if (StringUtil.isEmpty(resultStr)) {
            return false;
        }
        JSONObject result = JSONObject.parseObject(resultStr);

        if (result == null || !FinanceConstant.SUCCESS.equals(result.getString("retCode"))) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param objs
     * @param orderBean
     * @return 变更日期 2017年5月2日 变更历史 月供金额增加GPS流量费
     */
    private Map<String, Object> sendInstallRequest(JSONObject objs, OrderBean orderBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        String planId = orderBean.getPlanId();
        String orderId = orderBean.getOrderId();
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(planId));
        String installAmt = null;
        String interestAmt = null;
        try {
            Map<String, String> installInfoMap =
                    getInstallInfoMap(new BigDecimal(orderBean.getOrderAmt()),
                            new BigDecimal(orderBean.getDownPayment()), planBean);
            installAmt = installInfoMap.get("installAmt");
            interestAmt = installInfoMap.get("interestAmt");
        } catch (Exception e1) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "分期异常");
            return result;
        }

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderId", orderId);
        sourceMap.put("regId", orderBean.getRegId());
        sourceMap.put("orderDate",
                DateUtil.getDateString(orderBean.getCreateTime(), DateUtil.SHORT_DATE_FORMAT_NO_DASH));
        sourceMap.put(
                "beginDate",
                objs.getString("beginDate") == null ? DateUtil.getDateString(new Date(),
                        DateUtil.SHORT_DATE_FORMAT_NO_DASH) : objs.getString("beginDate"));
        sourceMap.put("openId", dictServiceImpl.getOpenIdByOrderId(orderId));
        sourceMap.put("merchantNo", orderBean.getMerchantNo());
        sourceMap.put("installSumAmt", installAmt);
        sourceMap.put("installAmt", installAmt);// 分期金额本金
        sourceMap.put("interestAmt", interestAmt);// 剩余利息
        sourceMap.put("takeInterest", Double.parseDouble(interestAmt) > 0 ? "1" : "2");// 是否上收利息
        sourceMap.put("takeMonth", planBean.getTakePayment() == 1 ? "1" : "2");// 是否上收月供
        sourceMap.put("takePaymentAmt", BigDecimalUtil.add(orderBean.getMonthInterest(),
                orderBean.getGpsTrafficFee() == null ? new BigDecimal(0) : orderBean.getGpsTrafficFee()));// 上收月供金额
        sourceMap.put("takePayment", planBean.getTakePayment());// 上收月供数
        sourceMap.put("installTerms", orderBean.getOrderItems());
        sourceMap.put("planId", planBean.getPlanId());
        sourceMap.put("otherAmt",
                orderBean.getGpsTrafficFee() == null ? new BigDecimal(0) : orderBean.getGpsTrafficFee()); // 其他费用
        sourceMap.put("smsMobile", orderBean.getSmsMobile());
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceInstallInstUrl(),
                    encryptUtils.encrypt(sourceMap));
            // 根据平账结果返回成功与否
            result = JSONObject.parseObject(resultStr);
            logger.info("正常还款校验参数返回结果，返回参数:{}", result);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
        }

        return result;
    }

    /*
     * 获取分期信息 变更日期 2017年5月18日 变更历史 蒲公英行去总本金时取applyAmt字段
     */
    private Map<String, String> getInstallInfoMap(BigDecimal orderAmt, BigDecimal downPayment, PlanBean planBean)
            throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        // 剩余本金
        BigDecimal installAmt = BigDecimal.ZERO;
        // 剩余分期利息
        BigDecimal interestAmt = BigDecimal.ZERO;
        try {
            int takePayment = planBean.getTakePayment();// 是否上收一期月供
            int installPeriods = planBean.getInstallPeriods();// 分期期数
            BigDecimal feeRatio = new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal("100"), 6,
                    BigDecimal.ROUND_HALF_UP);// 上收费率

            int feeYear = planBean.getFeeYear();// 上收月数
            // 计算本金 = 总本金-首付款
            BigDecimal calAmt = BigDecimalUtil.sub(orderAmt, downPayment);
            // 每月利息 = (总本金-首付款)*上收费率
            BigDecimal preInte = BigDecimalUtil.mul(calAmt, feeRatio);
            // 上收利息 = 每月利息*上收月数
            BigDecimal subInt = BigDecimalUtil.mul(preInte, new BigDecimal(feeYear));
            // 上收本金 = (总本金-首付款)/分期期数 * (是否上收月供(1或0))
            BigDecimal subAmt = BigDecimalUtil.mul(
                    BigDecimalUtil.div(calAmt, new BigDecimal(installPeriods)),
                    new BigDecimal(takePayment));
            // 总利息 = 每月利息*分期期数
            BigDecimal allInte = BigDecimalUtil.mul(preInte, new BigDecimal(installPeriods));
            // 上收月供中的利息 = (总利息 - 上收利息)/分期期数 * (是否上收月供(1或0))
            BigDecimal subMonInte = BigDecimalUtil.mul(
                    BigDecimalUtil.div(BigDecimalUtil.sub(allInte, subInt), new BigDecimal(installPeriods)),
                    new BigDecimal(takePayment));
            // 上收月供 = 上收本金 + 上收月供中的利息
            // BigDecimal subMonMake = BigDecimalUtil.add(subAmt, subMonInte);
            // 剩余本金 = 总本金 - 首付款 - 上收本金
            installAmt = BigDecimalUtil.sub(calAmt, subAmt);
            // 剩余分期利息 = 总利息 - 上收利息 - 上收月供中的利息
            interestAmt = BigDecimalUtil.sub(allInte, BigDecimalUtil.add(subInt, subMonInte));
        } catch (Exception e) {
            logger.error("计算传给账户系统的本金和剩余利息出现异常:{}", e);
            throw new Exception("计算传给账户系统的本金和剩余利息出现异常");
        } finally {}
        logger.debug("分期传给账户系统剩余本金:{},剩余分期利息:{}", installAmt, interestAmt);
        result.put("installAmt", installAmt + "");
        result.put("interestAmt", interestAmt + "");
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> result = new HashMap<String, String>();
        // 剩余本金
        BigDecimal installAmt = BigDecimal.ZERO;
        // 剩余分期利息
        BigDecimal interestAmt = BigDecimal.ZERO;
        try {
            int takePayment = 1;// 是否上收一期月供
            int installPeriods = 9;// 分期期数
            BigDecimal feeRatio = new BigDecimal("0.9").divide(new BigDecimal("100"), 6,
                    BigDecimal.ROUND_HALF_UP);// 上收费率
            BigDecimal orderAmt = new BigDecimal("150000");// 总本金
            BigDecimal downPayment = new BigDecimal("15000");// 首付
            // 计算本金 = 总本金-首付款
            BigDecimal calAmt = BigDecimalUtil.sub(orderAmt, downPayment);
            // 每月利息 = (总本金-首付款)
            BigDecimal preInte = BigDecimalUtil.mul(calAmt, feeRatio);
            int feeYear = 1;// 上收月数
            // 上收利息
            BigDecimal subInt = BigDecimalUtil.mul(preInte, new BigDecimal(feeYear));
            // 上收本金 = (总本金-首付款)/分期期数 * (是否上收月供(1或0))
            BigDecimal subAmt = BigDecimalUtil.mul(
                    BigDecimalUtil.div(calAmt, new BigDecimal(installPeriods)),
                    new BigDecimal(takePayment));
            // 总利息 = 每月利息*分期期数
            BigDecimal allInte = BigDecimalUtil.mul(preInte, new BigDecimal(installPeriods));
            // 上收月供中的利息 = (总利息 - 上收利息)/分期期数 * (是否上收月供(1或0))
            BigDecimal subMonInte = BigDecimalUtil.mul(
                    BigDecimalUtil.div(BigDecimalUtil.sub(allInte, subInt), new BigDecimal(installPeriods)),
                    new BigDecimal(takePayment));
            // 上收月供 = 上收本金 + 上收月供中的利息
            // BigDecimal subMonMake = BigDecimalUtil.add(subAmt, subMonInte);
            // 剩余本金 = 总本金 - 首付款 - 上收本金
            installAmt = BigDecimalUtil.sub(calAmt, subAmt);
            // 剩余分期利息 = 总利息 - 上收利息 - 上收月供中的利息
            interestAmt = BigDecimalUtil.sub(allInte, BigDecimalUtil.add(subInt, subMonInte));
        } catch (Exception e) {
            logger.error("计算传给账户系统的本金和剩余利息出现异常:{}", e);
        } finally {}
        logger.debug("分期传给账户系统剩余本金:{},剩余分期利息:{}", installAmt, interestAmt);
        result.put("installAmt", installAmt + "");
        result.put("interestAmt", interestAmt + "");
        System.out.println(result.get("installAmt"));
        System.out.println(result.get("interestAmt"));
    }

    // 通知医美类商户分期结果
    private String sendFQResult2YM(String orderId) {
        // 通知对方分期结果
        Map<String, Object> retResult = new HashMap<String, Object>();
        retResult.put("appid", ymForeignConfig.getYmAppid());
        retResult.put("extOrderId", orderId);
        retResult.put("note", "分期成功");
        String sendRes = SimpleHttpUtils.httpGet(ymForeignConfig.getYmFqUrl(), retResult);
        return sendRes;
    }

    /**
     * 开户
     * 
     * @param bizType
     * @return
     * @throws IqbException
     */
    @Override
    public Object openAccount(OrderBean orderBean) {
        String openId = dictServiceImpl.getOpenIdByOrderId(orderBean.getOrderId());
        AccountBean accountBean = new AccountBean();
        accountBean.setOpenId(openId);
        /** 判断账户是否开户 **/
        if (!(boolean) this.queryAccount(accountBean, orderBean)) {
            return true;
        }
        /** 获取银行卡信息 **/
        UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
        BankCardBean bankCardBean = bankCardBeanBiz.getOpenBankCardByRegId(userBean.getId() + "");
        if (bankCardBean == null) {
            return false;
        }
        accountBean.setBankCardNo(bankCardBean.getBankCardNo());
        accountBean.setRealName(userBean.getRealName());
        accountBean.setIdNo(userBean.getIdNo());
        accountBean.setRegId(userBean.getRegId());
        logger.info("开户传入信息：{}", JSONObject.toJSONString(accountBean));

        /** 请求账户接口 **/
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getFinanceAccountOpenAccUrl(),
                        encryptUtils.encrypt(BeanUtil.entity2map(accountBean)));
        logger.info("订单{}调用开户接口返回结果:{}", orderBean.getOrderId(), resultStr);
        /** 处理返回结果 **/
        if (StringUtil.isEmpty(resultStr)) {
            return false;
        }
        JSONObject result = JSONObject.parseObject(resultStr);

        if (result == null || !FinanceConstant.SUCCESS.equals(result.getString("retCode"))) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否开户
     */
    private Object queryAccount(AccountBean accountBean, OrderBean orderBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtil.isEmpty(accountBean.getRegId())) {
            accountBean.setRegId(orderBean.getRegId());
        }

        /** 请求账户接口 **/
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getFinanceAccountQueryAccUrl(),
                        encryptUtils.encrypt(BeanUtil.entity2map(accountBean)));
        logger.info("查询账户信息返回:" + resultStr);
        /** 处理返回结果 **/
        if (StringUtil.isEmpty(resultStr)) {
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "查询失败");
            return result;
        }
        result = JSONObject.parseObject(resultStr);
        String retCode = (String) result.get("retCode");

        /** 业务异常 **/
        switch (retCode) {
            case "00":
                return false;

            case "01":
                return true;

            default:
                break;
        }
        return true;
    }

    /**
     * 保存交易所订单信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月20日
     */
    @Override
    public long saveJysOrder(JSONObject objs) {
        long userId = doInsertJysUser(objs);
        objs.put("userId", userId);
        return doInsertJysOrderInfo(objs);
    }

    /**
     * 
     * Description:保存交易所用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private long doInsertJysUser(JSONObject objs) {
        JYSUserBean ubean = new JYSUserBean();
        ubean.setRealName(objs.getString("realName"));
        ubean.setRegId(objs.getString("regId"));
        ubean.setSmsMobile(objs.getString("smsMobile"));
        ubean.setBankCardNo(objs.getString("bankCardNo"));
        ubean.setBankName(objs.getString("bankName"));
        ubean.setStatus("1");
        ubean.setIdNo(objs.getString("idNo"));
        logger.info("推送至交易所-保存用户到jys_user 开始：{}", JSONUtil.objToJson(ubean));
        long userId = userBeanBiz.insertJysUser(ubean);
        logger.info("推送至交易所-保存用户到jys_user 结束：{}", userId);
        return userId;
    }

    /**
     * 
     * Description:执行交易所订单保存
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private long doInsertJysOrderInfo(JSONObject objs) {
        int planId = objs.getInteger("planId");
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(planId);
        Map<String, BigDecimal> detail = calculateService.calculateAmt(planBean, objs.getBigDecimal("applyAmt"));

        JYSOrderBean job = new JYSOrderBean();
        if (detail != null) {
            job.setOrderId(objs.getString("orderId"));
            job.setRegId(objs.getString("regId"));
            job.setUserId(String.valueOf(objs.getLongValue("userId")));
            job.setMerchantNo(objs.getString("merchantNo"));
            job.setBizType(objs.getString("bizType"));
            job.setOrderName(objs.getString("orderName"));
            job.setOrderAmt(String.valueOf(objs.getBigDecimal("applyAmt")));
            job.setOrderItems(String.valueOf(objs.getIntValue("applyItems")));
            job.setStatus("3");
            job.setWfStatus(objs.getIntValue("wfStatus"));
            job.setRiskStatus(objs.getIntValue("riskStatus"));

            job.setFee(planBean.getFeeRatio());
            job.setPreAmt(String.valueOf(detail.get("preAmount")));
            job.setMargin(String.valueOf(detail.get("margin")));
            job.setDownPayment(String.valueOf(detail.get("downPayment")));
            job.setServiceFee(String.valueOf(detail.get("serviceFee")));
            job.setPlanId(String.valueOf(planId));
            job.setTakePayment(detail.get("monthAmount"));
            job.setFeeYear(planBean.getFeeYear());
            job.setFeeAmount(detail.get("feeAmount"));
            job.setMonthInterest(detail.get("monthMake"));
            job.setExpireDate(formatA(objs.getIntValue("deadLine")));
            return jysOrderBiz.insertJysOrder(job);
        }
        return 0;
    }

    /**
     * 
     * Description:将long型时间转化为Date
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private static Date formatA(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time * 1000);
        return c.getTime();
    }

    /**
     * 
     * Description:账单结清时同步更新订单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月24日
     */
    private long validateAndUpdateOrderInfo(String orderId, int repayNo) {
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            if (Integer.parseInt(orderBean.getOrderItems()) == repayNo) {
                orderBean = new OrderBean();
                orderBean.setOrderId(orderId);
                orderBean.setRiskStatus(10);
                result = orderBiz.updateOrderInfo(orderBean);
            }
        }
        return result;
    }
}
