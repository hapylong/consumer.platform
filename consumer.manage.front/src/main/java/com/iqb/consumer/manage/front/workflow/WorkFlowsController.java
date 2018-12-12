/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月14日 上午10:25:27
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetinfo.service.IAssetInfoService;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.business.service.IBankCardService;
import com.iqb.consumer.service.layer.business.service.impl.OrderService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.wfservice.CombinationQueryService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/workFlow")
public class WorkFlowsController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(WorkFlowsController.class);
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String ZF = "zhiya";
    @Resource
    private CombinationQueryService combinationQueryService;
    @Resource
    private XFPayService xfPayService;
    @Resource
    private SysUserSession sysUserSession;

    @Autowired
    private IAssetInfoService assetInfoServiceImpl;

    @Autowired
    private OrderService orderServiceImpl;
    @Resource
    private CalculateService calculateService;
    @Autowired
    private IBankCardService bankCardServiceImpl;

    @Resource
    private AdminService adminServiceImpl;

    /**
     * 资产分配保存接口
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/saveReqParams"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object saveReqParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            int requestTimes = objs.getInteger("requestTimes");// 资金分配期数
            String orderId = objs.getString("orderId");
            String fundSourceId = objs.getString("fundSourceId");// 资金来源ID
            String plantime = objs.getString("plantime");// 资产分配时间
            String desc = objs.getString("desc");// 备注
            String deadline = objs.getString("deadline");// 标的结束时间
            Integer isWithholding = objs.getInteger("isWithholding");
            Integer isPublic = objs.getInteger("isPublic");
            Integer isPushff = objs.getInteger("isPushff");
            logger.info("分配期数:{},资金来源ID:{},资产分配时间:{},描述:{}", requestTimes, fundSourceId, plantime, desc);
            // 查询出对应orderId的剩余期数天数
            Map<Integer, Integer> hadRequestTimes = assetInfoServiceImpl.validataRequesttimes(orderId);
            if (requestTimes <= hadRequestTimes.get("month")) {// 剩余期数大于分配期数
                RequestMoneyBean requestMoneyBean = new RequestMoneyBean();
                Map<String, Object> params = new HashMap<>();
                params.put("regId", sysUserSession.getUserPhone());
                params.put("userCode", sysUserSession.getUserCode());
                UserBean userBean = assetInfoServiceImpl.getSysUserByRegId(params);
                requestMoneyBean.setApplyItems(requestTimes);
                requestMoneyBean.setSourcesFunding(fundSourceId);
                requestMoneyBean.setApplyTime(plantime);
                requestMoneyBean.setStatus(0);
                requestMoneyBean.setOrderId(orderId);
                requestMoneyBean.setRemark(desc);
                requestMoneyBean.setDeadline(deadline);
                requestMoneyBean.setIsWithholding(isWithholding);
                requestMoneyBean.setIsPublic(isPublic);
                requestMoneyBean.setIsPushff(isPushff);
                requestMoneyBean.setAllotRegid(String.valueOf(userBean.getId()));
                // FINANCE-2382 资产分配明细优化
                requestMoneyBean.setCreditBank(objs.getString("creditBank"));// 债权人开户银行
                requestMoneyBean.setCreditBankCard(objs.getString("creditBankCard"));// 债权人银行卡号
                requestMoneyBean.setCreditCardNo(objs.getString("creditCardNo"));// 债权人身份证号
                requestMoneyBean.setCreditName(objs.getString("creditName"));// 债权人姓名
                requestMoneyBean.setCreditPhone(objs.getString("creditPhone"));// 债权人手机号
                requestMoneyBean.setPushMode(objs.getString("pushMode"));
                String curRepayNo =
                        StringUtils.isNotEmpty(objs.getString("curRepayNo")) ? objs.getString("curRepayNo") : "0";
                requestMoneyBean.setCurRepayNo(curRepayNo);
                requestMoneyBean.setApplyAmt(objs.getString("applyAmt"));

                combinationQueryService.insertReqMoney(requestMoneyBean);
                // 更新inst_order表中的剩余期数
                OrderBean ob1 = orderServiceImpl.getOrderInfo(objs);
                ob1.setLeftInstIMonth(hadRequestTimes.get("month") - requestTimes);
                orderServiceImpl.updateOrderInfo1(ob1);
                OrderBean ob = orderServiceImpl.selectOne(objs);
                result.put("retCode", SUCCESS);
                result.put("retMsg", "资产分配成功");
                Map<String, Object> pushMap = new HashMap<>();
                pushMap.put("orderId", orderId);
                pushMap.put("requestId", requestMoneyBean.getId());
                if (ob != null) {
                    pushMap.put("bizType", ob.getBizType());
                }
                if (!assetInfoServiceImpl.validatePGYMerchant(fundSourceId, orderId)) {
                    assetInfoServiceImpl.pushAssetIntoToRedis(pushMap, objs);
                }

            } else {// 说明剩余期数小于分配期数
                // 1.将cf_requestmoney表中的保存实际剩余期数和天数
                RequestMoneyBean requestMoneyBean = new RequestMoneyBean();
                Map<String, Object> params = new HashMap<>();
                params.put("regId", sysUserSession.getUserPhone());
                params.put("userCode", sysUserSession.getUserCode());
                UserBean userBean = assetInfoServiceImpl.getSysUserByRegId(params);
                requestMoneyBean.setApplyItems(hadRequestTimes.get("month"));// 此时应该实际剩余期数
                requestMoneyBean.setApplyInstIDay(hadRequestTimes.get("day"));
                requestMoneyBean.setSourcesFunding(fundSourceId);
                requestMoneyBean.setApplyTime(plantime);
                requestMoneyBean.setStatus(0);
                requestMoneyBean.setOrderId(orderId);
                requestMoneyBean.setRemark(desc);
                requestMoneyBean.setDeadline(deadline);
                requestMoneyBean.setIsWithholding(isWithholding);
                requestMoneyBean.setIsPublic(isPublic);
                requestMoneyBean.setIsPushff(isPushff);
                requestMoneyBean.setAllotRegid(String.valueOf(userBean.getId()));
                // FINANCE-2382 资产分配明细优化
                requestMoneyBean.setCreditBank(objs.getString("creditBank"));// 债权人开户银行
                requestMoneyBean.setCreditBankCard(objs.getString("creditBankCard"));// 债权人银行卡号
                requestMoneyBean.setCreditCardNo(objs.getString("creditCardNo"));// 债权人身份证号
                requestMoneyBean.setCreditName(objs.getString("creditName"));// 债权人姓名
                requestMoneyBean.setCreditPhone(objs.getString("creditPhone"));// 债权人手机号
                requestMoneyBean.setPushMode(objs.getString("pushMode"));
                String curRepayNo =
                        StringUtils.isNotEmpty(objs.getString("curRepayNo")) ? objs.getString("curRepayNo") : "0";
                requestMoneyBean.setCurRepayNo(curRepayNo);
                requestMoneyBean.setApplyAmt(objs.getString("applyAmt"));

                combinationQueryService.insertReqMoney(requestMoneyBean);

                // 2.将inst_order表中剩余月日都update为0
                OrderBean ob2 = orderServiceImpl.getOrderInfo(objs);
                ob2.setLeftInstIMonth(0);
                ob2.setLeftInstIDay(0);
                orderServiceImpl.updateOrderInfo1(ob2);

                OrderBean ob = orderServiceImpl.selectOne(objs);
                result.put("retCode", SUCCESS);
                result.put("retMsg", "资产分配成功");
                Map<String, Object> pushMap = new HashMap<>();
                pushMap.put("orderId", orderId);
                pushMap.put("requestId", requestMoneyBean.getId());
                if (ob != null) {
                    pushMap.put("bizType", ob.getBizType());
                }
                if (!assetInfoServiceImpl.validatePGYMerchant(fundSourceId, orderId)) {
                    assetInfoServiceImpl.pushAssetIntoToRedis(pushMap, objs);
                }
            }
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("资产推送异常:{}", e);
            result.put("retCode", ERROR);
        }
        return result;
    }

    /**
     * 查询支付流水ID
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getTranNoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getTranNoByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        List<PaymentLogBean> paymentLogBean = null;
        try {
            String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
            String flag = objs.getString("flag");
            if (StringUtils.isEmpty(flag)) {
                flag = null;
            }
            paymentLogBean = xfPayService.getPayLogByOrderId(orderId, flag);
            if (paymentLogBean == null) {
                paymentLogBean = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("获取支付流水为空", e);
        }
        return paymentLogBean;
    }

    /**
     * 查询所有待请款数据
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getAllReqMoney"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAllReqMoney(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询订单数据...{} ", objs);
            adminServiceImpl.getMerchantNos(objs);
            Map map = new HashMap<String, String>();
            map = combinationQueryService.getAllRequestTotal(objs);
            PageInfo<RequestMoneyBean> pageInfo = combinationQueryService.getAllRequest(objs);
            logger.debug("IQB信息---分页查询订单数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("resultTotal", map);
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/saveWfInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object saveWfInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
            OrderOtherInfo orderOtherInfo = new OrderOtherInfo();
            orderOtherInfo.setOrderId(orderId);
            orderOtherInfo.setReceiveAmt(objs.getString("receiveAmt"));
            orderOtherInfo.setRemark(objs.getString("remark"));
            orderOtherInfo.setGpsRemark(objs.getString("gpsRemark"));
            combinationQueryService.updateOrderOtherInfo(orderOtherInfo);
            result.put("retCode", SUCCESS);
        } catch (Exception e) {
            result.put("retCode", ERROR);
        }
        return result;
    }

    /**
     * 根据ID风控查询业务信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRiskResult", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getRiskResult(@RequestBody JSONObject objs, HttpServletRequest request) {
        String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
        CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
        return cqb;
    }

    /**
     * 通用订单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderResult", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getOrderResult(@RequestBody JSONObject objs, HttpServletRequest request) {
        String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
        CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
        return cqb;
    }

    /**
     * 根据ID查询人工审核信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getArtificialCheck", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getArtificialCheck(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
            CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
            return cqb;
        } catch (Exception e) {
            logger.info("查询信息异常", e);
        }
        return new CombinationQueryBean();
    }

    /**
     * 保存估价
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add{type}AssessPrice", method = {RequestMethod.POST, RequestMethod.GET})
    public Object addAssessPrice(@RequestBody JSONObject objs, @PathVariable("type") String type,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderId = objs.getString("orderId");// 订单号
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", orderId);
            OrderBean orderBean = combinationQueryService.selectOne(params);
            String assessPrice = objs.getString("assessPrice");// 评估价格
            if (ZF.equalsIgnoreCase(type)) {
                String gpsPrice = objs.getString("gpsPrice");// gps价格
                BigDecimal orderAmt = new BigDecimal(objs.getString("orderAmt"));// 融资金额即分期金额
                PlanBean planBean = combinationQueryService.getPlanByID(Long.parseLong(orderBean.getPlanId()));

                // 调用账务系统计算金额
                Map<String, BigDecimal> detailMap = calculateService.calculateAmt(planBean, orderAmt);
                // Map<String, BigDecimal> detailMap = getDetail(orderAmt, planBean);
                orderBean.setOrderAmt(orderAmt + "");// 带分期总价
                orderBean.setDownPayment(detailMap.get("downPayment") + "");
                orderBean.setFeeAmount(detailMap.get("feeAmount"));
                orderBean
                        .setMonthInterest(BigDecimalUtil.add(
                                detailMap.get("monthMake"),
                                new BigDecimal(objs.getString("gpsTrafficFee") == null ? "0" : objs
                                        .getString("gpsTrafficFee"))));
                orderBean.setMargin(detailMap.get("margin") + "");
                orderBean.setServiceFee(detailMap.get("serviceFee") + "");
                BigDecimal preAmount = detailMap.get("preAmount");
                orderBean.setAssessPrice(assessPrice);
                orderBean.setPreAmt(preAmount + "");
                orderBean.setGpsAmt(new BigDecimal(gpsPrice));
                orderBean.setGpsTrafficFee(new BigDecimal(objs.getString("gpsTrafficFee") == null ? "0" : objs
                        .getString("gpsTrafficFee")));
            } else {
                orderBean.setAssessPrice(assessPrice);
            }
            combinationQueryService.updateOrderInfo(orderBean);
            result.put("retCode", "success");
            result.put("retMsg", "保存成功");
        } catch (Exception e) {
            result.put("retCode", "failed");
            result.put("retMsg", "保存失败");
        }
        return result;
    }

    /**
     * 根据ID查询门店业务审核信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getStoreBiz", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getStoreBiz(@RequestBody JSONObject objs, HttpServletRequest request) {
        String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
        CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
        return cqb;
    }

    /**
     * 根据ID查询内控需要的资料
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getControlBiz", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getControlBiz(@RequestBody JSONObject objs, HttpServletRequest request) {
        String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
        CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
        return cqb;
    }

    /**
     * 根据ID获取财务展示信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFinancialBiz", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getFinancialBiz(@RequestBody JSONObject objs, HttpServletRequest request) {
        String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
        CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
        return cqb;
    }

    /**
     * 业务名店保存订单
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyOrderInfo", method = RequestMethod.POST)
    public Object modifyOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        String orderId = objs.getString("orderId");// 订单号
        String orderAmt = objs.getString("orderAmt");// 订单金额
        String planId = objs.getString("planId");// 计划ID
        String chargeWay = objs.getString("chargeWay");// 收费方式
        logger.info("业务修改订单订单号:{},订单金额:{},计划ID:{},收费方式:{}", orderId, orderAmt, planId, chargeWay);
        // 查询原始订单信息
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", orderId);
            OrderBean ob = combinationQueryService.selectOne(params);
            if (ob == null) {
                result.put("retCode", "failed");
                result.put("retMsg", "订单不存在");
                return result;
            } else {
                ob.setOrderAmt(orderAmt);
                ob.setChargeWay(Integer.parseInt(chargeWay));// 收款方式
                // 判断计划是否修改,如果修改需要根据新的计划号计算订单属性
                if (ob.getPlanId() != planId) {
                    try {
                        BigDecimal amt = new BigDecimal(ob.getOrderAmt());
                        PlanBean planBean = combinationQueryService.getPlanByID(Long.parseLong(planId));// 查询修改后的订单分期计划
                        // 分期期数
                        String orderItems = planBean.getInstallPeriods() + "";
                        // 首付款
                        BigDecimal dpr = new BigDecimal(planBean.getDownPaymentRatio()).divide(new BigDecimal(100));// 首付比例
                        BigDecimal downPayment = amt.multiply(dpr).setScale(2, BigDecimal.ROUND_HALF_UP);// 首付
                        // 服务费
                        BigDecimal sfr = new BigDecimal(planBean.getServiceFeeRatio()).divide(new BigDecimal(100));// 服务费比例
                        BigDecimal serviceFee = amt.multiply(sfr).setScale(2, BigDecimal.ROUND_HALF_UP);// 服务费
                        // 保证金
                        BigDecimal mr = new BigDecimal(planBean.getMarginRatio()).divide(new BigDecimal(100));// 保证金比例
                        BigDecimal margin = amt.multiply(mr).setScale(2, BigDecimal.ROUND_HALF_UP);// 保证金
                        // 费率
                        BigDecimal feeRatio = new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal(100));// 上收费率
                        // 上收息 （总金额-首付）*(费率/100)*上收月份
                        BigDecimal fee = (amt.subtract(downPayment)).multiply(feeRatio)
                                .multiply(new BigDecimal(planBean.getFeeYear())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        // 月供
                        BigDecimal lastFee = (amt.subtract(downPayment)).multiply(feeRatio).multiply(
                                (new BigDecimal(planBean.getInstallPeriods() - planBean.getFeeYear())));// 剩余利息
                        BigDecimal monthMake = (amt.subtract(downPayment).add(lastFee)).divide(
                                new BigDecimal(planBean.getInstallPeriods()), 2, BigDecimal.ROUND_HALF_UP);// 月供
                        BigDecimal monthInterest = monthMake.setScale(0, BigDecimal.ROUND_DOWN);

                        // 预付款
                        BigDecimal preAmount = fee.add(downPayment).add(margin).add(serviceFee).add(monthInterest)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);// 结果保留2位小数
                        // 是否上收月供
                        int takePayment = planBean.getTakePayment();
                        ob.setOrderItems(orderItems);// 分期期数-
                        ob.setPreAmt(preAmount + "");// 预付款-
                        ob.setPlanId(planId);// 计划ID-
                        ob.setTakePayment(takePayment);// 是否上收月供-
                        ob.setMargin(margin + "");// 保证金-
                        ob.setDownPayment(downPayment + "");// 首付-
                        ob.setServiceFee(serviceFee + "");// 服务费-
                        ob.setFeeYear(planBean.getFeeYear());// 上收息月份-
                        ob.setMonthInterest(monthInterest);// 月供-
                        ob.setFeeAmount(fee);// 上收息金额-
                    } catch (Exception e) {
                        logger.error("重算订单相关属性发生异常", e);
                        result.put("retCode", "failed");
                        result.put("retMsg", "计算订单属性发生错误");
                        return result;
                    }
                }
                ob.setOrderAmt(orderAmt);
                ob.setChargeWay(Integer.parseInt(chargeWay));// 收款方式
                // 修改订单
                combinationQueryService.updateOrderInfo(ob);
                result.put("retCode", "success");
                result.put("retMsg", "保存修改成功");
                return result;
            }
        } catch (Exception e) {
            logger.error("订单修改发生异常", e);
            result.put("retCode", "failed");
            result.put("retMsg", "计算订单属性发生错误");
            return result;
        }
    }

    // 获取月供
    @SuppressWarnings("unused")
    private BigDecimal getMonthMake(PlanBean planBean, OrderBean orderBean) {
        BigDecimal amt = new BigDecimal(orderBean.getOrderAmt());
        BigDecimal downPayment = new BigDecimal(orderBean.getDownPayment());
        BigDecimal feeRatio = new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal(100));// 上收费率
        BigDecimal lastFee = (amt.subtract(downPayment)).multiply(feeRatio).multiply(
                (new BigDecimal(planBean.getInstallPeriods() - planBean.getFeeYear())));// 剩余利息
        BigDecimal monthMake = (amt.subtract(downPayment).add(lastFee)).divide(
                new BigDecimal(planBean.getInstallPeriods()), 2, BigDecimal.ROUND_HALF_UP);// 月供
        return monthMake.setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 根据订单号商户查询计划
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMerAllPlan", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getMerAllPlan(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");// 前端给后台的唯一订单号
            CombinationQueryBean cqb = combinationQueryService.getByOrderId(orderId);
            // 订单存在的情况去查询计划
            if (cqb != null) {
                // 根据商户号查询计划
                List<PlanBean> list = combinationQueryService.getPlanByMerNo(cqb.getMerchantNo());
                return list;
            }
        } catch (Exception e) {
            logger.error("查询计划发生异常", e);
        }
        return null;
    }

    /**
     * 
     * Description:查询资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月4日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getAllotDetailByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAllotDetailByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始查询资产分配详情数据...{} ", objs);
            List<RequestMoneyBean> beanList = combinationQueryService.getAllotDetailByOrderId(objs);
            logger.debug("IQB信息---查询资产分配详情数据完成.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", beanList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 资产分配明细查询
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getAllReqAllotMoney"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAllReqAllotMoney(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询资产分配明细数据...{} ", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<RequestMoneyBean> pageInfo = combinationQueryService.getAllReqAllotMoney(objs);
            logger.debug("IQB信息---分页查询资产分配明细数据完成.");
            Map map = new HashMap<String, String>();
            map = combinationQueryService.getAllReqAllotMoneyTotal(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            linkedHashMap.put("resultTotal", map);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/exportAllReqAllotMoney"}, method = {RequestMethod.GET, RequestMethod.POST})
    public void exportAllReqAllotMoney(HttpServletRequest request, HttpServletResponse response) {
        try {

            JSONObject requestMessage = new JSONObject();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                requestMessage.put(paraName, new String(para.trim()));
                adminServiceImpl.getMerchantNos(requestMessage);
            }
            combinationQueryService.exportAllReqAllotMoney(requestMessage, response);

        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
        }
    }

    /**
     * 
     * Description:资产赎回分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getAllotRedemptionInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAllotRedemptionInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询资产赎回数据...{} ", objs);
            adminServiceImpl.getMerchantNos(objs);
            String flag = objs.getString("flag");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            if (StringUtils.isNotEmpty(flag) && flag.equals("1")) {
                PageInfo<RequestMoneyBean> pageInfo = combinationQueryService.geAllotRedemptionInfo(objs);
                linkedHashMap.put("result", pageInfo);
            } else {
                linkedHashMap.put("result", "");
            }
            logger.debug("IQB信息---分页查询资产赎回数据完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:资产赎回
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/allotRedemption"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map geAllotRedemptionInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始资产赎回数据...{} ", objs);
            int result = combinationQueryService.allotRedemption(objs);
            logger.debug("IQB信息---资产赎回完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            if (result > 0) {
                linkedHashMap.put("result", "success");
            } else {
                linkedHashMap.put("result", result);
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-returnRiskController"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map returnRiskController(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("LOACL风控回调的接口参数:{}", objs);
            bankCardServiceImpl.saveReprotContent(objs);
            logger.debug("LOACL风控回调的接口.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", "success");
            linkedHashMap.put("code", "00000");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", "unsuccess");
            linkedHashMap.put("code", "00001");
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

}
