package com.iqb.consumer.service.layer.contract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.data.layer.bean.contract.ContractListBean;
import com.iqb.consumer.data.layer.bean.contract.OrderContractListBean;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderOtherAmtEntity;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.contract.ContractInfoBiz;
import com.iqb.consumer.data.layer.biz.contract.ContractListBiz;
import com.iqb.consumer.data.layer.biz.creditorinfo.CreditorInfoBiz;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;
import com.iqb.consumer.service.layer.base.FinanceUtilService;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.consumer.service.layer.util.NumberToCN;
import com.iqb.eatep.ec.asyn.AsynSubmitEcService;
import com.iqb.eatep.ec.contract.bizretbean.GenerateContractRetBean;
import com.iqb.eatep.ec.contract.bizretbean.SelectContractInfoRetBean;
import com.iqb.eatep.ec.contract.ecinfo.service.IEcInfoService;
import com.iqb.eatep.ec.contract.ssq.dosign.service.IDoSignService;
import com.iqb.eatep.ec.contract.ssq.template.service.IEcTemplateService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

/**
 * @author guojuan
 */
@Service("makeContractService")
public class MakeContractServiceImpl extends FinanceUtilService
        implements
        MakeContractService {

    protected static Logger logger = LoggerFactory
            .getLogger(MakeContractServiceImpl.class);

    public final static int MAKE_RET_SUCC = 0; // 处理成功
    public final static int MAKE_RET_FAIL = 1; // 处理失败
    public final static int MAKE_TRAN_FAIL = 2; // 数据转换错误
    public final static int MAKE_POST_FAIL = 3; // 请求接口错误
    public final static int SUBMIT_RET_SUCC = 10; // 处理成功
    public final static int SUBMIT_RET_FAIL = 11; // 处理失败
    public final static int SUBMIT_TRAN_FAIL = 12; // 数据转换错误
    public final static int SUBMIT_POST_FAIL = 13; // 请求接口错误

    /** 商户列表key **/
    private static final String MERLIST_KEY = "merList";

    /** 商户列表key **/
    private static final String MERCHANTNOS_KEY = "merchantNos";

    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat format_day = new SimpleDateFormat("dd");
    @Resource
    private OrderBiz orderBiz;

    @Resource
    private IMerchantService iMerchantService;

    @Resource
    private CreditorInfoBiz creditorInfoBiz;

    @Resource
    private ContractListBiz contractListBiz;

    @Resource
    private ContractInfoBiz contractInfoBiz;

    @Resource
    private IEcTemplateService ecTemplateServiceImpl;

    @Resource
    private IEcInfoService ecInfoServiceImpl;

    @Resource
    private IDoSignService doSignServiceImpl;

    @Autowired
    private AsynSubmitEcService asynSubmitEcService;

    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;

    @Autowired
    private CustomerBiz customerBiz;

    @Autowired
    private SysCoreDictItemBiz sysCoreDictItemBiz;

    @Autowired
    private ContractInfoServiceImpl contractInfoServiceImpl;

    @Override
    @Transactional
    public Map<String, Object> makeContract(JSONObject objs) {
        logger.debug("IQB信息---【服务层】生成合同信息，开始...");
        // 1.初始化
        String notifyUrl = objs.getString("iqb_contract_notify_contract_url"); // 生成合同回调接口
        String taskUrl = objs.getString("iqb_contract_task_contract_url"); // 实际借款人签名后，异步回调
        JSONObject objsRequest = new JSONObject();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        Map<String, Object> map4 = new HashMap<>();
        Map<String, Object> mapEcTemplateAttr = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        ContractInfoBean contractInfoBean = new ContractInfoBean();
        GenerateContractRetBean generateContractRetBean = new GenerateContractRetBean();
        // 2.准备请求参数
        try {
            contractInfoBean = JSONObject.parseObject(JSON.toJSONString(objs),
                    ContractInfoBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】生成合同信息,装换ContractInfoBean类出错...");
            resultMap.put("result", MAKE_TRAN_FAIL);
            resultMap.put("returnResult", null);
            return resultMap;
        }
        // 根据订单号查询 管理费和考察费
        InstOrderOtherAmtEntity orderOtherAmt = orderBiz.getOtherAmtEntity(contractInfoBean.getOrderId());
        // String orgCode = sysUserSession.getOrgCode();
        String orgCode = objs.getString("orgCode");
        OrderBean orderBean = orderBiz.selectOne(objs);
        CreditorInfoBean creditorInfoBean = creditorInfoBiz
                .selectOneByOrderId(contractInfoBean.getOrderId());

        if (creditorInfoBean == null) {
            logger.error("IQB信息---获得债权人的身份证号...");
            resultMap.put("result", MAKE_TRAN_FAIL);
            resultMap.put("returnResult", null);
            return resultMap;
        }

        objsRequest.put("bizId", contractInfoBean.getOrderId());
        objsRequest.put("orgCode", orgCode);
        objsRequest.put("bizType", orderBean.getBizType());
        objsRequest.put("notifyUrl", notifyUrl);// 合同提交成功通知地址
        objsRequest.put("taskUrl", taskUrl);// 合同全部签署完成通知地址

        // 需要处理为String的字段
        MerchantBean merchantBean = iMerchantService.getMerByMerNo(orderBean.getMerchantNo());
        if (merchantBean != null) {
            mapEcTemplateAttr.put("storeName", merchantBean.getMerchantFullName());
            objsRequest.put("storeNo", merchantBean.getMerchantNo());
        } else {
            mapEcTemplateAttr.put("storeName", "");
            objsRequest.put("storeNo", "");
        }

        Map<String, Object> mapCus = new HashMap<>();
        mapCus.put("customerCode", merchantBean.getId());
        CustomerBean customer = new CustomerBean();
        try {
            customer = customerBiz.getCustomerInfoByCustmerCode(mapCus);
        } catch (IqbException e) {
            e.printStackTrace();
        }
        logger.info("电子合同准备数据，查询门店信息,条件" + mapCus.toString() + "| 结果：" + JSON.toJSONString(customer));
        mapEcTemplateAttr.put("customerName", customer.getCustomerName());// 客户名称
        mapEcTemplateAttr.put("customerBankName", customer.getCustomerBankName());// 客户银行名称
        mapEcTemplateAttr.put("customerBankNo", customer.getCustomerBankNo());// 客户账户信息
        mapEcTemplateAttr.put("province", customer.getProvince());// 省
        mapEcTemplateAttr.put("city", customer.getCity());// 市

        map1.put("ecSignerType", "x");// 线上
        map1.put("ecSignerCode", creditorInfoBean.getCreditCardNo());
        list.add(map1);

        map2.put("ecSignerType", "3");// 门店
        map2.put("ecSignerCode", orgCode);
        list.add(map2);
        SysDictItem dictItem =
                sysCoreDictItemBiz.getDictByDTCAndDC(DictTypeCodeEnum.lease_financial, orderBean.getBizType());
        if (dictItem != null) {
            CustomerBean custm = new CustomerBean();
            Map<String, Object> mapC = new HashMap<>();
            try {
                mapC.put("customerCode", dictItem.getDictValue());
                custm = customerBiz.getCustomerInfoByCustmerCode(mapC);
                if (custm != null) {
                    logger.info("电子合同准备数据，查询融租公司信息,条件" + mapC.toString() + "| 结果：" + JSON.toJSONString(custm));
                    map4.put("ecSignerType", "2");// 融租公司
                    map4.put("ecSignerCode", custm.getCustomerCode());
                    mapEcTemplateAttr.put("financialName", custm.getCustomerName());// 公司名称
                    mapEcTemplateAttr.put("financialUserName", custm.getCorporateName());// 公司法人
                    mapEcTemplateAttr.put("financialAddress", custm.getAddressDetail());// 公司地址
                    mapEcTemplateAttr.put("financialPhone", custm.getLegalPersonPhoneNum());// 联系方式
                    list.add(map4);
                }
            } catch (IqbException e) {
                e.printStackTrace();
                logger.info("异常：" + e.getMessage());
            }
        }

        map3.put("ecSignerType", "s");
        map3.put("ecSignerCode", orderBean.getRegId());

        String ecSignNotifyUrl = objs
                .getString("iqb_contract_sign_notify_contract_url_" + merchantBean.getPublicNo()); // 合同签署完毕回调接口
        logger.info("IQB信息---【服务层】电子合同订单号为" + contractInfoBean.getOrderId() + "，微信公众号类型为：" + merchantBean.getPublicNo()
                + "，微信端回调地址,{}", ecSignNotifyUrl);
        map3.put("ecSignNotifyUrl", ecSignNotifyUrl);// 实际借款人签名后 微信端跳转页面
        list.add(map3);

        objsRequest.put("ecSignerList", list);

        mapEcTemplateAttr.put("orderId", contractInfoBean.getOrderId());
        mapEcTemplateAttr.put("orderAmt", orderBean.getOrderAmt());
        mapEcTemplateAttr.put("orderMAmt", NumberToCN.number2CNMontrayUnit(new BigDecimal(
                orderBean.getOrderAmt() == null ? "0" : orderBean.getOrderAmt())));
        mapEcTemplateAttr.put("orderItems", orderBean.getOrderItems());
        mapEcTemplateAttr.put("oItems", orderBean.getOrderItems());
        mapEcTemplateAttr.put("margin", orderBean.getMargin());
        mapEcTemplateAttr.put("marMgin", NumberToCN.number2CNMontrayUnit(new BigDecimal(orderBean.getMargin() == null
                ? "0"
                : orderBean.getMargin())));
        mapEcTemplateAttr.put("downPayment", orderBean.getDownPayment());
        mapEcTemplateAttr.put("downMPayment",
                NumberToCN.number2CNMontrayUnit(new BigDecimal(orderBean.getDownPayment() == null ? "0" : orderBean
                        .getDownPayment())));
        mapEcTemplateAttr.put("serviceFee", NumberToCN.number2CNMontrayUnit(new BigDecimal(orderBean.getServiceFee())));
        mapEcTemplateAttr.put("preAmt", orderBean.getPreAmt());
        mapEcTemplateAttr.put("preMAmt", NumberToCN.number2CNMontrayUnit(new BigDecimal(orderBean.getPreAmt() == null
                ? "0"
                : orderBean.getPreAmt())));
        mapEcTemplateAttr.put("monthInterest",
                ObjectUtils.toString(orderBean.getMonthInterest() == null ? "0" : orderBean.getMonthInterest()));
        mapEcTemplateAttr.put("monthMInterest", NumberToCN.number2CNMontrayUnit(orderBean.getMonthInterest()));
        if (orderBean.getTakePayment() != 0) {
            mapEcTemplateAttr.put(
                    "monthuInterest",
                    ObjectUtils.toString(orderBean.getMonthInterest()));
            mapEcTemplateAttr.put(
                    "monthuMInterest",
                    NumberToCN.number2CNMontrayUnit(orderBean.getMonthInterest()));
        } else {
            mapEcTemplateAttr.put("monthuInterest", "0");
            mapEcTemplateAttr.put("monthuMInterest", "零");
        }

        // 前期费用（上收息）
        mapEcTemplateAttr.put("feeAmount", ObjectUtils.toString(orderBean.getFeeAmount()));
        mapEcTemplateAttr.put("feeMAmt", NumberToCN.number2CNMontrayUnit(orderBean.getFeeAmount() == null
                ? new BigDecimal("0")
                : orderBean.getFeeAmount()));

        PlanBean bean = qrCodeAndPlanBiz.getPlanByID(Long.valueOf(orderBean.getPlanId()));
        Map<String, BigDecimal> mapMonth =
                this.getMonthDetail(new BigDecimal(orderBean.getOrderAmt() == null ? "0" : orderBean.getOrderAmt()),
                        bean);
        mapEcTemplateAttr.put("monthAccrual", ObjectUtils.toString(mapMonth.get("monthAccrual")));
        mapEcTemplateAttr.put("mtAccrual", ObjectUtils.toString(mapMonth.get("monthAccrual")));
        mapEcTemplateAttr.put("monthMAccrual", NumberToCN.number2CNMontrayUnit(mapMonth.get("monthAccrual")));
        mapEcTemplateAttr.put("monthCorpus", ObjectUtils.toString(mapMonth.get("monthCorpus")));
        mapEcTemplateAttr.put("mtCorpus", ObjectUtils.toString(mapMonth.get("monthCorpus")));
        mapEcTemplateAttr.put("monthMCorpus", NumberToCN.number2CNMontrayUnit(mapMonth.get("monthCorpus")));

        mapEcTemplateAttr.put("yearRatio",
                ObjectUtils.toString(mapMonth.get("yearRatio").setScale(2, RoundingMode.HALF_UP)));
        // 首付款
        mapEcTemplateAttr.put("dPRatio",
                ObjectUtils.toString(new BigDecimal(bean.getDownPaymentRatio()).setScale(2, RoundingMode.HALF_UP)));
        // 保证金
        mapEcTemplateAttr.put("mgRatio",
                ObjectUtils.toString(new BigDecimal(bean.getMarginRatio()).setScale(2, RoundingMode.HALF_UP)));
        // 服务费比例
        mapEcTemplateAttr.put("sFRatio",
                ObjectUtils.toString(new BigDecimal(bean.getServiceFeeRatio()).setScale(2, RoundingMode.HALF_UP)));
        // 租赁利率
        mapEcTemplateAttr.put("mFRatio",
                ObjectUtils.toString(new BigDecimal(bean.getFeeRatio()).setScale(2, RoundingMode.HALF_UP)));
        // 合同截止日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, bean.getInstallPeriods());
        mapEcTemplateAttr.put("limitDate", format.format(calendar.getTime()));
        mapEcTemplateAttr
                .put("contractYear", ObjectUtils.toString((new BigDecimal(bean.getInstallPeriods() / 12)).setScale(0,
                        RoundingMode.HALF_UP)));

        if (!StringUtils.isBlank(contractInfoBean.getProvider())) {
            mapEcTemplateAttr.put("supplier", ObjectUtils.toString(contractInfoBean.getProvider()));
        } else {
            mapEcTemplateAttr.put("supplier", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getVendor())) {
            mapEcTemplateAttr.put("vendor", ObjectUtils.toString(contractInfoBean.getVendor()));
        } else {
            mapEcTemplateAttr.put("vendor", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getVendorNo())) {
            mapEcTemplateAttr.put("vendorNo", ObjectUtils.toString(contractInfoBean.getVendorNo()));
        } else {
            mapEcTemplateAttr.put("vendorNo", "-");
        }
        mapEcTemplateAttr.put("seatNum", Integer.toString(contractInfoBean.getSeatNum()));
        if (!StringUtils.isBlank(contractInfoBean.getCarType())) {
            mapEcTemplateAttr.put("carType", ObjectUtils.toString(contractInfoBean.getCarType()));
        } else {
            mapEcTemplateAttr.put("carType", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getFuelForm())) {
            mapEcTemplateAttr.put("fuelForm", ObjectUtils.toString(contractInfoBean.getFuelForm()));
        } else {
            mapEcTemplateAttr.put("fuelForm", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getFuelOilNumber())) {
            mapEcTemplateAttr.put("fuelOilNumber", ObjectUtils.toString(contractInfoBean.getFuelOilNumber()));
        } else {
            mapEcTemplateAttr.put("fuelOilNumber", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getEngineType())) {
            mapEcTemplateAttr.put("engineType", ObjectUtils.toString(contractInfoBean.getEngineType()));
        } else {
            mapEcTemplateAttr.put("engineType", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getCarNo())) {
            mapEcTemplateAttr.put("carNo", ObjectUtils.toString(contractInfoBean.getCarNo()));
        } else {
            mapEcTemplateAttr.put("carNo", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getCarColor())) {
            mapEcTemplateAttr.put("carColor", ObjectUtils.toString(contractInfoBean.getCarColor()));
        } else {
            mapEcTemplateAttr.put("carColor", "-");
        }
        if (!StringUtils.isBlank(contractInfoBean.getRegistrationNo())) {
            mapEcTemplateAttr.put("registrationNo", ObjectUtils.toString(contractInfoBean.getRegistrationNo()));
        } else {
            mapEcTemplateAttr.put("registrationNo", "-");
        }
        // FINANCE-3167 售后回租合同中车辆评估费/考察费参数设置；当评估管理费和考察费为空时，合同中默认为0
        if (orderOtherAmt != null && !StringUtils.isBlank(orderOtherAmt.getAssessMsgAmt())) {
            mapEcTemplateAttr.put("assessMsgAmt", ObjectUtils.toString(orderOtherAmt.getAssessMsgAmt()));
        } else {
            mapEcTemplateAttr.put("assessMsgAmt", "0");
        }
        if (orderOtherAmt != null && !StringUtils.isBlank(orderOtherAmt.getInspectionAmt())) {
            mapEcTemplateAttr.put("inspectionAmt", ObjectUtils.toString(orderOtherAmt.getInspectionAmt()));
        } else {
            mapEcTemplateAttr.put("inspectionAmt", "0");
        }
        // FINANCE-3167 End
        mapEcTemplateAttr.put("date", format.format(new Date()));
        mapEcTemplateAttr.put("day", format_day.format(new Date()));
        BigDecimal prop = new BigDecimal("0.05");
        mapEcTemplateAttr.put(
                "finamt",
                ObjectUtils.toString(new BigDecimal(orderBean.getOrderAmt()).multiply(prop).setScale(2,
                        RoundingMode.HALF_UP)));
        mapEcTemplateAttr.putAll(contractInfoServiceImpl.createRepaymentDetail(contractInfoBean.getOrderId()));
        // 其他字段待定
        objsRequest.put("ecTemplateAttr", JSON.toJSON(mapEcTemplateAttr));

        // 3.调用生成合同接口
        try {
            logger.info("IQB信息---【服务层】生成合同信息，参数：,{}", objsRequest.toString());
            generateContractRetBean = ecTemplateServiceImpl
                    .generateEc(objsRequest);
        } catch (IqbException e1) {
            logger.error("IQB信息---【服务层】生成合同信息,调用接口返回值错误...");
            resultMap.put("result", MAKE_POST_FAIL);
            resultMap.put("returnResult", e1.getMessage());
            e1.printStackTrace();
            return resultMap;
        }
        if (generateContractRetBean == null) {
            logger.error("IQB信息---【服务层】生成合同信息,请求接口出错...");
            resultMap.put("result", MAKE_TRAN_FAIL);
            resultMap.put("returnResult", null);
            return resultMap;
        }
        resultMap.put("result", MAKE_RET_SUCC);
        resultMap.put("returnResult", generateContractRetBean.getEcList());
        // 4.保存合同列表
        List<Map<String, String>> resultList = generateContractRetBean.getEcList();
        if (resultList.size() > 0) {
            for (Map<String, String> map : resultList) {
                ContractListBean contractListBean = new ContractListBean();
                contractListBean.setContractName(map.get("ecName"));
                // contractListBean.setContractUrl(map.get("ecUrl"));
                contractListBean.setCreateTime(new Date());
                contractListBean.setEcId(map.get("ecCode"));
                contractListBean.setOrderId(contractInfoBean.getOrderId());
                contractListBean.setType(1);
                contractListBean.setVersion(1);
                contractListBean.setStatus(1);
                contractListBiz.insertContractList(contractListBean);
            }
        } else {
            resultMap.put("result", MAKE_POST_FAIL);
            resultMap.put("returnResult", "生成合同失败");
            return resultMap;
        }
        logger.debug("IQB信息---【服务层】生成合同信息，结束...");
        return resultMap;
    }

    private Map<String, BigDecimal> getMonthDetail(BigDecimal orderAmt, PlanBean planBean) {
        // 首付
        BigDecimal downPayment = BigDecimalUtil
                .mul(orderAmt, new BigDecimal(planBean.getDownPaymentRatio()).divide(new BigDecimal(100), 5,
                        BigDecimal.ROUND_HALF_UP));

        // 剩余金额 = (总金额-首付)
        BigDecimal leftAmt = BigDecimalUtil.sub(orderAmt, downPayment);
        // 剩余期数 = (总期数-上收期数)
        int leftTerms = planBean.getInstallPeriods() - planBean.getFeeYear();
        // 月利息 = (总金额-首付)*利息率
        BigDecimal feeCount = BigDecimalUtil.mul(leftAmt,
                new BigDecimal(planBean.getFeeRatio()).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP));
        // 剩余利息 = 总利息*(总期数-上收期数)
        BigDecimal leftFee = BigDecimalUtil.mul(feeCount, new BigDecimal(leftTerms));
        // 每月的本金
        BigDecimal monthCorpus =
                leftAmt.divide(new BigDecimal(planBean.getInstallPeriods()), 2, BigDecimal.ROUND_HALF_UP);
        // 每月利息
        BigDecimal monthAccrual =
                leftFee.divide(new BigDecimal(planBean.getInstallPeriods()), 2, BigDecimal.ROUND_HALF_UP);
        // 年息率
        BigDecimal yearRatio = new BigDecimal(planBean.getFeeRatio()).multiply(new BigDecimal(12));
        Map<String, BigDecimal> detailMap = new HashMap<String, BigDecimal>();
        detailMap.put("monthCorpus", monthCorpus);// 每月的本金
        detailMap.put("monthAccrual", monthAccrual);// 每月利息
        detailMap.put("yearRatio", yearRatio);// 每月利息
        return detailMap;
    }

    @Override
    public int submitContract(JSONObject objs) {
        logger.debug("IQB信息---【服务层】提交合同信息，开始...");
        // 1.初始化
        int result = SUBMIT_RET_SUCC;
        JSONObject objsRequest = new JSONObject();
        ContractInfoBean contractInfoBean = new ContractInfoBean();
        // 2.准备请求参数
        try {
            contractInfoBean = JSONObject.parseObject(JSON.toJSONString(objs),
                    ContractInfoBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】提交合同信息,装换ContractInfoBean类出错...");
            return SUBMIT_TRAN_FAIL;
        }
        objsRequest.put("bizId", contractInfoBean.getOrderId());
        // String orgCode = sysUserSession.getOrgCode();
        String orgCode = objs.getString("orgCode");
        OrderBean orderBean = orderBiz.selectOne(objs);
        objsRequest.put("bizId", contractInfoBean.getOrderId());
        objsRequest.put("orgCode", orgCode);
        objsRequest.put("bizType", orderBean.getBizType());
        Object obj = null;
        try {
            obj = asynSubmitEcService.submitEc(objsRequest);
        } catch (IqbException e1) {
            logger.error("IQB信息---【服务层】提交合同信息,调用接口错误...");
            e1.printStackTrace();
            return SUBMIT_POST_FAIL;
        }
        if (obj == null) {
            logger.error("IQB信息---【服务层】生成合同信息,请求接口出错...");
            return SUBMIT_RET_FAIL;
        }
        logger.info("IQB信息---签约结果：" + obj.toString());
        JSONObject object = (JSONObject) JSON.toJSON(obj);
        logger.info("IQB信息---签约格式化后结果：" + object.toString());
        if (!"1".equals(object.get("status"))) {
            result = MAKE_RET_FAIL;
        }
        // 更新contractInfo表状态
        contractInfoBean.getUpdateTime();
        contractInfoBean.setStatus("process");
        contractInfoBiz.updataStatus(contractInfoBean);
        logger.debug("IQB信息---【服务层】提交合同信息，结束...");
        return result;
    }

    @Override
    public PageInfo<OrderContractListBean> orderContractInit(JSONObject objs) {
        return new PageInfo<OrderContractListBean>(this.contractInfoBiz.orderContractInit(objs));
    }

    @Override
    public PageInfo<OrderContractListBean> orderContractFinish(JSONObject objs) {
        /** 获取商户列表 **/
        List<String> merList = this.getMerchantList(objs);
        objs.put(MERCHANTNOS_KEY, merList);
        objs.remove(MERLIST_KEY);

        return new PageInfo<OrderContractListBean>(this.contractInfoBiz.orderContractFinish(objs));
    }

    /**
     * 
     * Description: 获取商户列表
     * 
     * @param
     * @return List<String>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:09:50
     */
    private List<String> getMerchantList(JSONObject objs) {
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get(MERLIST_KEY);
        List<String> merList = new ArrayList<String>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        return merList;
    }

    @Override
    public Object selContractList(JSONObject objs) {
        JSONObject jsonObject = new JSONObject();
        SelectContractInfoRetBean retBean = null;
        String orderId = String.valueOf(objs.get("bizId"));
        ContractInfoBean bean = contractInfoBiz.selContractInfo(orderId);
        if (bean != null) {
            if ("waiting".equals(bean.getStatus()) || "complete".equals(bean.getStatus())) {
                try {
                    retBean = this.ecInfoServiceImpl.selectContractInfo(objs);
                    jsonObject = JSONUtil.filteNullValue(retBean);
                    if (retBean.getEcList().size() < 1) {
                        jsonObject.put("recode", "0");
                        jsonObject.put("msg", "请求参数不正确");
                    } else {
                        jsonObject.put("recode", "1");
                    }
                } catch (IqbException e) {
                    jsonObject.put("recode", "0");
                    jsonObject.put("msg", e.getMessage());
                }
            } else if ("process".equals(bean.getStatus())) {
                jsonObject.put("recode", "0");
                jsonObject.put("msg", "尚未签署完成");
            }
        }
        return jsonObject;
    }

}
