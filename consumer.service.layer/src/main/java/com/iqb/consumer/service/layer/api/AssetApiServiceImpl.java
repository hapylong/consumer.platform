package com.iqb.consumer.service.layer.api;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.api.CfImagePojo;
import com.iqb.consumer.data.layer.bean.api.PicInformationPojo;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.EmergencySummaryPojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.authoritycard.AuthorityCardBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.api.dto.CarAssetDto;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.dto.refund.PaymentDto;
import com.iqb.consumer.service.layer.dto.refund.RepayList;
import com.iqb.eatep.ec.bizconfig.service.BizConfigService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Service
public class AssetApiServiceImpl implements AssetApiService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final Integer RISKTYPE = 4; // 信用贷收益权抵押类的风控类型
    protected static final Integer CARRISKTYPE = 3; // 信用贷收益权抵押类的风控类型
    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    public static final String WFInterRetSucc = "1";
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Resource
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Autowired
    private DictService dictServiceImpl;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Autowired
    private BizConfigService bizConfigServiceImpl;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private CalculateService calculateService;

    @Autowired
    private AuthorityCardBiz authorityCardBiz;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> intoCarAsset(CarAssetDto assetDto) throws IqbException {
        Map<String, Object> result = new HashMap<>();
        // 保存用户信息inst_user
        UserBean saveUserBean = saveUserBean(assetDto.getRealName(), assetDto.getRegId(), assetDto.getIdCardNo());
        // 存开户信息
        saveBankCard(saveUserBean, assetDto.getBankCardNo(), saveUserBean.getRegId());
        // 风控信息保存
        saveRiskInfo(assetDto);
        // 保存订单信息
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(assetDto.getMerchantNo());
        OrderBean orderBean = saveOrderBean(assetDto, saveUserBean);
        // 启动工作流
        String desc = "";
        wfAndProcId(merchantBean, desc, orderBean);
        result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
        result.put("result_msg", ApiReturnInfo.API_SUCCESS_00000000.getRetUserInfo());
        result.put("orderId", orderBean.getOrderId());
        return result;
    }

    private void wfAndProcId(MerchantBean merchantBean, String desc, OrderBean ob) throws IqbException {
        // 启动工作流
        try {
            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(ob.getBizType(), ob.getWfStatus());
            LinkedHashMap linkedHashMap =
                    startWF(wfConfig, ob.getRegId(), desc, ob.getOrderId(), merchantBean.getId() + "",
                            ob.getOrderAmt());
            if (linkedHashMap != null && WFInterRetSucc.equals((linkedHashMap.get("success") + ""))) {
                // 成功回写procInstId
                // 回写工作流标识ID
                Map<String, String> procInstMap = (java.util.Map<String, String>) linkedHashMap.get("iqbResult");
                String procInstId = procInstMap.get("procInstId");
                Map<String, Object> params = new HashMap<>();
                params.put("id", ob.getId());
                params.put("procInstId", procInstId);
                orderBiz.updateProcInstId(params);
            } else {
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000001);
            }
        } catch (IqbException e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
    }

    private OrderBean saveOrderBean(CarAssetDto assetDto, UserBean userBean) throws IqbException {
        // 保存订单信息
        OrderBean ob = null;
        try {
            ob = new OrderBean();
            // 生成订单号
            String orderId = generateOrderId(assetDto.getMerchantNo(), assetDto.getBizType());
            ob.setOrderId(orderId); // 订单号
            ob.setOrderName(assetDto.getOrderName());
            ob.setUserId(userBean.getId() + ""); // 用户ID
            ob.setStatus("1"); //
            ob.setWfStatus(2);// 工作流状态
            ob.setRiskStatus(2);// 订单状态
            ob.setMerchantNo(assetDto.getMerchantNo());// 商户号
            ob.setRegId(assetDto.getRegId());// 手机号
            ob.setBizType(assetDto.getBizType());// 业务类型
            ob.setOrderRemark(assetDto.getAsynnotifyUrl()); // 回调第三方的地址URL
            // 查询计划
            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(assetDto.getPlanId());
            Map<String, BigDecimal> detail = calculateService.calculateAmt(planBean, assetDto.getOrderAmt());// getDetail(assetDto.getOrderAmt(),
                                                                                                             // planBean);
            ob.setOrderAmt(assetDto.getOrderAmt() + "");
            ob.setOrderItems(planBean.getInstallPeriods() + "");
            ob.setPreAmt(detail.get("preAmount") + "");
            ob.setPreAmtStatus("0");
            ob.setMonthInterest(detail.get("monthMake"));// 月供
            ob.setPlanId(assetDto.getPlanId() + "");// 计划ID
            ob.setTakePayment(planBean.getTakePayment());// 首付
            ob.setMargin(detail.get("margin") + "");// 押金
            ob.setDownPayment(detail.get("downPayment") + "");//
            ob.setFee(planBean.getFeeRatio()); // 费率
            ob.setServiceFee(detail.get("serviceFee") + "");// 服务费
            ob.setFeeYear(planBean.getFeeYear());// 上收月数
            ob.setFeeAmount(detail.get("feeAmount"));// 上收利息
            orderBiz.saveFullOrderInfo(ob);
            return ob;
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
        }
    }

    /**
     * 保存风控信息
     * 
     * @param assetDto
     */
    private void saveRiskInfo(CarAssetDto assetDto) throws IqbException {
        RiskInfoBean rib = null;
        try {
            // 判断用户是否已经填写了风控信息，如果填写可以跳过保存风控信息环节
            rib = riskInfoBiz.getRiskInfoByRegId(assetDto.getRegId(), CARRISKTYPE + "");
            if (rib == null) {
                rib = new RiskInfoBean();
                rib.setRegId(assetDto.getRegId()); // 用户手机号
                rib.setRiskType(CARRISKTYPE); // 风控类型
                rib.setCheckInfo(assetDto.getRiskInfo());// 风控数据格式
                riskInfoBiz.saveRiskInfo(rib);
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVERISK_RROR_20000003);
        }
    }

    /**
     * 保存用户信息
     * 
     * @param realName
     * @param regId
     * @param idCardNo
     * @return
     */
    private UserBean saveUserBean(String realName, String regId, String idCardNo) throws IqbException {
        UserBean ub = null;
        try {
            // 根据手机号查询用户是否已经注册，注册返回存在的用户信息
            ub = userBeanBiz.selectOne(regId);
            if (ub == null) {
                ub = new UserBean();
                ub.setRealName(realName); // 姓名
                ub.setRegId(regId);// 手机号
                ub.setSmsMobile(regId); // 短信手机号
                ub.setIdNo(idCardNo);// 身份证
                userBeanBiz.saveUserBean(ub);
            }
            return ub;
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEUSER_ERROR_20000001);
        }
    }

    /**
     * 保存卡信息
     * 
     * @param ub
     * @param infos[bankCard,regId]
     */
    private void saveBankCard(UserBean ub, String... infos) throws IqbException {
        // 保存用户银行卡信息inst_bankcard
        BankCardBean bcb = null;
        try {
            bcb = bankCardBeanBiz.getOpenBankCardByRegId(ub.getId() + "");
            if (bcb == null) {
                bcb = new BankCardBean();
                bcb.setBankCardNo(infos[0]);// 卡号
                bcb.setBankName("");// 银行名称
                bcb.setBankCode("");// 银行代码
                bcb.setBankMobile(infos[1]);
                bcb.setUserId(ub.getId());
                bcb.setStatus(2);
                bankCardBeanBiz.saveBankCard(bcb);
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEBANK_ERROR_20000002);
        }
    }

    /**
     * 蒲公英"收益权抵押类"项目
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> intoCreditLoan(CreditLoanDto creditLoanDto) throws IqbException {
        Map<String, Object> result = new HashMap<>();
        // 保存用户信息inst_user
        UserBean saveUserBean =
                saveUserBean(creditLoanDto.getRealName(), creditLoanDto.getRegId(), creditLoanDto.getIdCard());

        // 保存用户银行卡信息inst_bankcard
        // 存开户信息
        saveBankCard(saveUserBean, creditLoanDto.getBankCard(), saveUserBean.getRegId());

        // 保存用户风控信息inst_riskinfo
        RiskInfoBean rib = null;
        try {
            // 判断用户是否已经填写了风控信息，如果填写可以跳过保存风控信息环节
            rib = riskInfoBiz.getRiskInfoByRegId(creditLoanDto.getRegId(), RISKTYPE + "");
            if (rib == null) {
                rib = new RiskInfoBean();
                rib.setRegId(creditLoanDto.getRegId()); // 用户手机号
                rib.setRiskType(RISKTYPE); // 风控类型
                rib.setCheckInfo(JSONObject.toJSONString(creditLoanDto));// 风控数据格式
                riskInfoBiz.saveRiskInfo(rib);
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVERISK_RROR_20000003);
        }

        // 保存订单信息
        OrderBean ob = null;
        try {
            ob = new OrderBean();
            // 生成订单号
            String orderId = generateOrderId(creditLoanDto.getMerchantId(), "3001");
            ob.setOrderId(orderId); // 订单号
            ob.setUserId(saveUserBean.getId() + ""); // 用户ID
            ob.setStatus("1"); //
            ob.setWfStatus(1);// 工作流状态
            ob.setRiskStatus(2);// 订单状态
            ob.setMerchantNo(creditLoanDto.getMerchantId());// 商户号
            ob.setRegId(creditLoanDto.getRegId());// 手机号
            ob.setBizType("3001");// 业务类型
            ob.setOrderRemark(creditLoanDto.getProType());// 产品类型
            orderBiz.saveOrderInfo(ob);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
        }

        // 启动工作流
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(ob.getMerchantNo());
        String desc =
                creditLoanDto.getRealName() + "," + merchantBean.getMerchantShortName() + "," + "蒲公英-"
                        + creditLoanDto.getProType() + "," + creditLoanDto.getRegId();
        wfAndProcId(merchantBean, desc, ob);
        result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
        result.put("result_msg", ApiReturnInfo.API_SUCCESS_00000000.getRetUserInfo());
        return result;
    }

    /**
     * 启动工作流
     * 
     * @param wfConfig
     * @param procBizMemo
     * @param orderId
     * @param orgId
     * @param amount
     * @return
     * @throws IqbException
     */
    private LinkedHashMap startWF(WFConfig wfConfig, String regId, String procBizMemo, String orderId, String orgId,
            String amount)
            throws IqbException {
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procDefKey", wfConfig.getProcDefKey());

        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1");
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", regId);
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderId);
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", orgId);
        hmBizData.put("procBizMemo", procBizMemo);
        hmBizData.put("amount", amount);
        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        String url = wfConfig.getStartWfurl();
        // 发送Https请求
        LinkedHashMap responseMap = null;
        logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        try {
            responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("调用工作流接口返回信息：{}" + responseMap);
        return responseMap;
    }

    /**
     * 
     * Description: 生成订单号 orderId规则： 业务类型: 20 以租代售 21 抵押车 22 质押车 业务子类：新车01，二手,02.其他00
     * 若业务类型为20，则子类显示01或02；否则业务子类都显示00 新爱车帮帮手 商户号+业务类型(两位)+业务子类(两位)+yyMMdd+流水号（三位，顺序加一)
     * 
     * @param bizType
     * @param
     * @return String
     * @throws IqbException
     * @throws @Author wangxinbang Create Date: 2016年12月9日 下午4:24:49
     */
    @Override
    public String generateOrderId(String merchantNo, String bizType) throws IqbException {
        String seqRedisKey = this.getOrderRedisKey(merchantNo, bizType);
        return seqRedisKey + this.getSeqFromRedis(seqRedisKey, false);
    }

    /**
     * 
     * Description: 获取redis的key
     * 
     * @param bizType
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年12月9日 下午7:29:10
     */
    public String getOrderRedisKey(String merchantNo, String bizType) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return merchantNo.toUpperCase() + bizType + todayStr;
    }

    /**
     * 
     * Description: 从redis中获取订单序号
     * 
     * @param
     * @return String
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2016年12月9日 下午7:20:47
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
     * 根据regId orderId merchantNo bizType获取最近三期待还款账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月7日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getLastThreeOrderInfo(JSONObject jsonObject) throws IqbException {
        Map<String, Object> result = new HashMap<>();

        String orderId = jsonObject.getString("orderId");
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        jsonObject.put("openId", openId);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillCurrentThreeUrl(),
                encryptUtils.encrypt(jsonObject));
        if (resultStr != null) {
            result = JSONObject.parseObject(resultStr);

            List<Map<String, Object>> listMap = (List<Map<String, Object>>) result.get("result");
            if (listMap == null || listMap.size() == 0) {
                throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000003);
            }
            result = convertOrderInfo(jsonObject, listMap);
        } else {
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000002);
        }
        return result;
    }

    /**
     * Description:处理账单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    private Map<String, Object> convertOrderInfo(JSONObject jsonObject, List<Map<String, Object>> listMap) {
        Map<String, Object> result = new HashMap<>();
        String orderId = jsonObject.getString("orderId");

        OrderBean orderBean = orderBiz.selByOrderId(orderId);

        result.put("orderName", orderBean.getOrderName());
        result.put("orderId", orderId);

        List<Map<String, String>> billList = new ArrayList<>();
        for (int i = 0; i < listMap.size(); i++) {
            Map<String, Object> map = listMap.get(i);
            JSONArray jsonArray = (JSONArray) map.get("billList");
            for (int j = 0; j < jsonArray.size(); j++) {
                Map<String, String> temp = new HashMap<>();
                JSONObject obj = (JSONObject) jsonArray.get(j);

                temp.put("id", obj.getString("id"));
                temp.put("repayNo", obj.getString("repayNo"));
                temp.put("curRepayAmt", obj.getString("curRepayAmt"));
                temp.put("earliestPayDate", obj.getString("lastRepayDate"));
                temp.put("overdueDays", obj.getString("overdueDays"));
                temp.put("overdueAmt", obj.getString("curRepayOverdueInterest"));
                billList.add(temp);
            }
        }
        result.put("billList", billList);
        result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
        result.put("result_msg", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetUserInfo());
        return result;
    }

    /**
     * Description:处理账单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertOrderInfoB(JSONObject jsonObject, JSONObject resJson) throws IqbException {
        Map<String, Object> result = new HashMap<>();
        String orderId = jsonObject.getString("orderId");
        JSONObject res = (JSONObject) resJson.get("result");

        OrderBean orderBean = orderBiz.selByOrderId(orderId);

        result.put("orderName", orderBean.getOrderName());
        result.put("orderId", orderId);

        List<Map<String, Object>> listMap = (List<Map<String, Object>>) res.get("recordList");
        if (listMap == null || listMap.size() == 0) {
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000003);
        }

        List<Map<String, String>> billList = new ArrayList<>();
        for (int i = 0; i < listMap.size(); i++) {
            Map<String, Object> map = listMap.get(i);
            Map<String, String> temp = new HashMap<>();

            temp.put("id", String.valueOf(map.get("id")));
            temp.put("repayNo", String.valueOf(map.get("repayNo")));
            temp.put("curRepayAmt", String.valueOf(map.get("curRepayAmt")));
            temp.put("earliestPayDate", String.valueOf(map.get("lastRepayDate")));
            temp.put("overdueDays", String.valueOf(map.get("overdueDays")));
            temp.put("realPayamt", String.valueOf(map.get("realPayamt")));
            temp.put("overdueAmt", String.valueOf(map.get("curRepayOverdueInterest")));
            temp.put("orderDate", String.valueOf(map.get("orderDate")));
            temp.put("status", String.valueOf(map.get("status")));
            billList.add(temp);
        }
        result.put("billList", billList);
        result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
        result.put("result_msg", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetUserInfo());
        return result;
    }

    /**
     * 根据orderId获取全部账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月7日
     */
    @Override
    public Map<String, Object> getAllOrderInfo(JSONObject jsonObject) throws IqbException {
        Map<String, Object> result = new HashMap<>();

        String orderId = jsonObject.getString("orderId");
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        jsonObject.put("openId", openId);
        jsonObject.put("currentPage", "1");
        jsonObject.put("numPerPage", "500");
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillSelectBillsUrl(),
                encryptUtils.encrypt(jsonObject));
        if (resultStr != null) {
            JSONObject resJson = JSONObject.parseObject(resultStr);
            if (resJson.getString("retCode").equals("success")) {
                result = convertOrderInfoB(jsonObject, resJson);
            } else {
                throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000003);
            }
        } else {
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000002);
        }
        return result;
    }

    /**
     * 根据订单号查询订单信息并存入redis
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月12日
     */
    @Override
    public void prePayment(String key, JSONObject json) throws IqbException {
        String orderId = json.getString("traceNo");
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            json.put("amount", orderBean.getPreAmt());
            redisPlatformDao.setKeyAndValue(key, json.toJSONString());
        } else {
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000005);
        }
    }

    /**
     * 根据订单号查询账单信息并存入redis
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月12日
     */
    @Override
    public void normalPayment(String key, JSONObject json) throws IqbException {
        String orderId = json.getString("traceNo");
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("orderId", json.getString("traceNo"));
        jsonObj.put("repayNo", json.getString("repayNo"));
        jsonObj.put("openId", openId);

        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getSelectBillsByRepayNoUrl(),
                encryptUtils.encrypt(jsonObj));
        if (resultStr != null) {
            JSONObject resJson = JSONObject.parseObject(resultStr);
            if (resJson.getString("retCode").equals("success")) {
                json.put("amount", resJson.getString("curRepayAmt"));
            } else {
                logger.error("发送给账户系统出现异常:{}", resultStr);
                throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000003);
            }
        } else {
            logger.error("发送给账户系统出现异常:{}", resultStr);
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000002);
        }

        redisPlatformDao.setKeyAndValue(key, json.toJSONString());
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月13日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> validateAmount(String traceNo, String paymentId) {
        logger.info("--支付ID校验,金额校验--开始-- traceNo {}", traceNo);
        Map<String, Object> result = new HashMap<>();

        String key = "preResult_" + traceNo;
        Map<String, String> resultParam = null;

        String okResult = redisPlatformDao.getValueByKey(key);
        if (okResult == null) {
            result.put("result_msg", ApiReturnInfo.API_GET_FAIL_80000004.getRetUserInfo());
            result.put("result_code", ApiReturnInfo.API_GET_FAIL_80000004.getRetCode());
            return result;
        }
        resultParam = (Map<String, String>) JSONObject.parse(okResult);

        String oldPaymentId = resultParam.get("paymentId");
        if (!paymentId.equals(oldPaymentId)) {
            result.put("result_msg", ApiReturnInfo.API_PAY_PARAMS_NULL_400000017.getRetUserInfo());
            result.put("result_code", ApiReturnInfo.API_PAY_PARAMS_NULL_400000017.getRetCode());
            return result;
        }
        // 预支付上送金额
        String firstAmount = resultParam.get("amount");

        String flag = resultParam.get("flag");
        String keyOrderId = "";
        String preResultKey = "preResult_" + traceNo;
        Map<String, String> orderParam = null;
        Map<String, String> bankParam = null;
        Map<String, String> preResultParam = null;
        // 预付款金额/账单金额
        String amount = "";
        // flag 1 预付款 2正常还款
        if (flag != null) {
            if (flag.equals("1")) {
                keyOrderId = "preAmount_" + traceNo;
                String preResult = redisPlatformDao.getValueByKey(keyOrderId);
                if (preResult == null) {
                    result.put("result_msg", ApiReturnInfo.API_GET_FAIL_80000004.getRetUserInfo());
                    result.put("result_code", ApiReturnInfo.API_GET_FAIL_80000004.getRetCode());
                    return result;
                }
                logger.info("--支付ID校验,金额校验--预付款 redis缓存信息--{}", preResult);
                orderParam = (Map<String, String>) JSONObject.parse(preResult);
                amount = orderParam.get("amount");
                // 金额扩大100倍
                amount = BigDecimalUtil.expand(new BigDecimal(amount)).toString();
                amount.substring(0, amount.indexOf("."));
                BigDecimal repayAmt = new BigDecimal(firstAmount);
                BigDecimal preAmt = new BigDecimal(amount);

                if (repayAmt.compareTo(preAmt) != 0) {
                    result.put("result_msg", ApiReturnInfo.API_PAY_PARAMS_NULL_400000015.getRetUserInfo());
                    result.put("result_code", ApiReturnInfo.API_PAY_PARAMS_NULL_400000015.getRetCode());
                    return result;
                } else {
                    result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
                }

            } else if (flag.equals("2")) {
                keyOrderId = "normalAmount_" + traceNo;
                String normalResult = redisPlatformDao.getValueByKey(keyOrderId);
                String normalPreResult = redisPlatformDao.getValueByKey(preResultKey);
                if (normalResult == null) {
                    result.put("result_msg", ApiReturnInfo.API_GET_FAIL_80000004.getRetUserInfo());
                    result.put("result_code", ApiReturnInfo.API_GET_FAIL_80000004.getRetCode());
                    return result;
                }
                logger.info("--支付ID校验,金额校验--正常还款 redis缓存信息--{}", normalResult);
                bankParam = (Map<String, String>) JSONObject.parse(normalResult);
                preResultParam = (Map<String, String>) JSONObject.parse(normalPreResult);
                try {
                    traceNo = traceNo.substring(0, traceNo.lastIndexOf("_"));
                    String repayNos = bankParam.get("repayNo");
                    OrderBean orderBean = orderBiz.selByOrderId(traceNo);

                    // 查询订单信息
                    Map<String, Object> billInfo = billInfoService.getBillInfo(traceNo,
                            orderBean.getRegId(), repayNos);
                    billInfo.put("sumAmt", preResultParam.get("amount"));
                    logger.info("--支付ID校验,金额校验--查询订单信息缓存信息--{}", billInfo);
                    // 正常还款校验账单参数是否正确
                    Map<String, Object> resultMap = validateRepay(billInfo, null);

                    String retCode = (String) resultMap.get("retCode");
                    if (retCode.equalsIgnoreCase("success")) {
                        result.put("result_msg", ApiReturnInfo.API_SUCCESS_00000000.getRetUserInfo());
                        result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
                    } else {
                        result.put("result_msg", resultMap.get("retMsg"));
                        result.put("result_code", ApiReturnInfo.API_PAY_PARAMS_NULL_400000020.getRetCode());
                    }
                    logger.info("标准接口--确认支付--正常还款 参数校验返回 {}", resultMap);
                } catch (Exception e) {
                    logger.error("标准接口--校验后台代偿订单异常:{}", e);
                    result.put("result_msg", ApiReturnInfo.API_PAY_PARAMS_NULL_400000016.getRetUserInfo());
                    result.put("result_code", ApiReturnInfo.API_PAY_PARAMS_NULL_400000016.getRetCode());
                }
            }
            // 查询绑定的银行卡是否与当前使用的银行卡是否一致,如果不一致将原绑定的银行卡解绑
            autoSwitchUnBindBankCard(resultParam);
        }
        logger.info("--支付ID校验,金额校验--结束--");
        return result;
    }

    /**
     * 
     * Description:验证手机号绑定的银行卡是否与当前使用的银行卡是一致
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, String> validateBankCard(String bankCardNo, String regId) {
        Map<String, String> rstMap = new HashMap<>();
        String flag = "false";
        Map<String, String> param = new HashMap<>();
        param.put("userId", regId);

        String cardNo = "";
        LinkedHashMap resultMap = SendHttpsUtil.postMsg4GetMap(consumerConfig.getIntoXfBindBankCardUrl(), param);
        if (resultMap != null) {
            if (resultMap.get("status").equals("00")) {
                List<Map> bankList = (List<Map>) resultMap.get("bankList");
                Map<String, String> map = bankList.get(0);
                cardNo = map.get("bankCardNo");
                if (!bankCardNo.equals(cardNo)) {
                    flag = "true";
                }
            }
        }
        rstMap.put("cardNo", cardNo);
        rstMap.put("flag", flag);
        return rstMap;
    }

    /**
     * 
     * Description:银行卡解绑
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> unBindCardNo(String bankCardNo, String regId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        param.put("userId", regId);
        param.put("bankCardNo", bankCardNo);

        LinkedHashMap resultMap = SendHttpsUtil.postMsg4GetMap(consumerConfig.getIntoXfUnBindBankCardUrl(), param);
        if (resultMap != null) {
            if (resultMap.get("status").equals("00")) {
                logger.info("--调用先锋接口进行银行卡解绑成功--:{} ", resultMap);
            }
            result.put("result_code", resultMap.get("status"));
            result.put("result_msg", resultMap.get("respMsg"));
        }
        return result;
    }

    /**
     * 正常还款平账
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月15日
     */
    @Override
    public Map<String, Object> repay(Map<String, Object> billInfo, Map<String, String> signParameters) {
        // 1.获取还款参数
        String paymentJson = getRepayParams(billInfo, signParameters);
        // 2.调用账户系统
        Map<String, Object> result = null;
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillRefundUrl(),
                    encryptUtils.encrypt(paymentJson));
            logger.info("订单:{},平账返回结果:{}", billInfo.get("orderId"), resultStr);
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "发送给账户系统异常");
        }
        return result;
    }

    /**
     * 标准支付接口--正常还款--账单信息校验
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月14日
     */
    @Override
    public Map<String, Object> validateRepay(Map<String, Object> billInfo, Map<String, String> signParameters) {
        // 1.获取还款参数
        String paymentJson = getRepayParams(billInfo, signParameters);
        // 2.调用账户系统
        Map<String, Object> result = null;
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillValidateUrl(),
                    encryptUtils.encrypt(paymentJson));
            logger.info("订单:{},校验返回结果:{}", billInfo.get("orderId"), resultStr);
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "发送给账户系统异常");
        }
        return result;
    }

    /**
     * 标准支付接口--正常还款--账单信息校验--参数组装
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月14日
     */
    @SuppressWarnings("unchecked")
    @Override
    public String getRepayParams(Map<String, Object> billInfo, Map<String, String> signParameters) {
        String orderId = (String) billInfo.get("orderId");
        String regId = (String) billInfo.get("regId");
        String sumAmt = (String) billInfo.get("sumAmt");

        List<Map<String, Object>> repayTempList = (List<Map<String, Object>>) billInfo.get("repayList");

        // 获取参数
        String tranTime = null;
        String tradeNo = null;
        Date repayDate = null;

        if (signParameters != null) {
            tranTime = signParameters.get("tranTime");
            tradeNo = signParameters.get("tradeNo");
            repayDate = DateUtil.parseDate(tranTime, DateUtil.SIMPLE_DATE_FORMAT);
        }

        List<RepayList> repayList = new ArrayList<>();
        RepayList rl = null;
        for (int i = 0; i < repayTempList.size(); i++) {
            rl = new RepayList();
            Map<String, Object> map = repayTempList.get(i);
            String repayNo = String.valueOf(map.get("repayNo"));
            String amt = String.valueOf(map.get("amt"));
            rl.setAmt(new BigDecimal(amt));
            rl.setRepayNo(Integer.parseInt(repayNo));
            repayList.add(rl);
        }

        // 组装平账参数
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setOpenId((String) billInfo.get("openId"));
        paymentDto.setOrderId(orderId);
        paymentDto.setRegId(regId);
        paymentDto.setRepayDate(repayDate);
        paymentDto.setRepayList(repayList);
        paymentDto.setRepayModel("normal");
        paymentDto.setSumAmt(new BigDecimal(sumAmt));
        paymentDto.setTradeNo(tradeNo);

        List<PaymentDto> result = new ArrayList<>();
        result.add(paymentDto);

        logger.info("--支付ID校验,金额校验--订单参数拼装--{}", JSON.toJSONString(result));
        return JSON.toJSONString(result);

    }

    /**
     * 自动切换解绑银行卡
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月20日
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Object> autoSwitchUnBindBankCard(Map<String, String> objs) {
        Map<String, Object> result = new HashMap<>();
        LinkedHashMap resultMap = SendHttpsUtil.postMsg4GetMap(consumerConfig.getIntoXfUnBindBankCardUrl(), objs);
        if (resultMap != null) {
            if (resultMap.get("status").equals("00")) {
                logger.info("--调用先锋接口进行银行卡解绑成功--:{} ", resultMap);
            }
            result.put("result_code", resultMap.get("status"));
            result.put("result_msg", resultMap.get("respMsg"));
        }
        return result;
    }

    @Override
    public Map<String, Object> getInfoByRegId(String regId) {
        RiskInfoBean rib = riskInfoBiz.getRiskInfoByRIAndRT(regId, RiskInfoBean.RISK_TYPE_CAR);
        Map<String, Object> responseMsg = new HashMap<>();
        if (rib != null) {
            InstRiskInfoColumnRT3CheckInfoPojo isic =
                    JSONObject.parseObject(rib.getCheckInfo(), InstRiskInfoColumnRT3CheckInfoPojo.class);
            if (isic != null) {
                Set<EmergencySummaryPojo> esp = isic.getEmergencySummary();
                responseMsg.put("emergencyMan", esp);
                responseMsg.put("isMarried", isic.getMarriedstatus());
                responseMsg.put("address", isic.getAddprovince());
            }
        }
        Map<String, Object> accountDetails = userBeanBiz.getInfoByRegId(regId);
        if (accountDetails != null) {
            responseMsg.put("accountDetails", accountDetails);
        }
        return responseMsg;
    }

    @Override
    public PicInformationPojo getPIPByOid(String orderId) {
        PicInformationPojo pip = authorityCardBiz.getPIPByOid(orderId);
        if (pip == null) {
            pip = new PicInformationPojo();
        }
        pip.setOrderId(orderId);
        List<CfImagePojo> cfps = authorityCardBiz.getCFPByOid(orderId);
        if (cfps == null || cfps.isEmpty()) {
            return pip;
        }
        for (CfImagePojo cfp : cfps) {
            if (cfp.check()) {
                switch (cfp.getImgType()) {
                    case 10:
                        pip.setZxbg(cfp.getImgPath());
                        break;
                    case 12:
                        pip.setSfzzfm(cfp.getImgPath());
                        break;
                    case 13:
                        pip.setHkb(cfp.getImgPath());
                        break;
                    case 27:
                        pip.setJsz(cfp.getImgPath());
                        break;
                    case 23:
                        pip.setGcqrd(cfp.getImgPath());
                        break;
                    case 1:
                        pip.setJksy(cfp.getImgPath());
                        break;
                    case 2:
                        pip.setJkwz(cfp.getImgPath());
                        break;
                    case 3:
                        pip.setCl(cfp.getImgPath());
                        break;
                    case 8:
                        pip.setClqz(cfp.getImgPath());
                        break;
                }
            }
        }
        return pip;
    }

}
